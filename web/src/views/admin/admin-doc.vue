<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-input v-model:value="param.name" placeholder="分类名称">
                </a-input>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleQuery()">
                  查询
                </a-button>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="add">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-table
              v-if="level1.length> 0 "
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>

                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button danger size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical" :label-col="{span : 6}" :wrapper-col="{span : 18}">
            <a-form-item>
              <a-input v-model:value="doc.name"  placeholder="文档名称"/>
            </a-form-item>
            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>

            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closeable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="editor-content-view" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>

<!--  <a-modal title="文档表单"-->
<!--    v-model:visible="modalVisible"-->
<!--    :confirm-loading="modalLoading"-->
<!--    @ok="handleModalOk">-->
<!--    -->
<!--  </a-modal>-->
</template>



<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message, Modal} from 'ant-design-vue';
import { Tool } from "@/util/tool";
import {useRoute} from "vue-router";
import {ExclamationCircleOutlined} from "@ant-design/icons-vue";
import E from 'wangeditor'

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route = useRoute()
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);

    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: { customRender: 'name' }
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];


    /*  一级分类树
    [{
      id: "",
      name: "",
      children: [{
        id: "",
        name: ""
      }]
    }]
     */
    const level1 = ref();
    level1.value = [];


    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      level1.value= [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success){
          docs.value=data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("树形结构：" , level1.value);

          //父文档下拉框初始化，打开页面不用点击新增
          treeSelectData.value = Tool.copy(level1.value) || [];
          treeSelectData.value.unshift({id: 0, name: '无'});

        } else {
          message.error(data.message);
        }

      });
    };


    //表单
    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    // const modalVisible = ref(false);
    const modalLoading = ref(false);
    const editor = new E('#content');
    editor.config.zIndex = 0;


    const handleSave = () => {
      modalLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save",doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success){
          message.success("保存成功")
          // modalVisible.value =false;
          handleQuery();
        } else {
          message.error(data.message);
        }
      })
    };

    /**
     * 将某结点及其子孙结点全部设置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      //遍历某一层的结点
      for (let i = 0 ;i < treeSelectData.length; i++){
        const node = treeSelectData[i];
        if (node.id === id){
          //当前结点就是目标结点
          console.log("disabled",node);
          node.disabled = true;

          //把所有子结点都加上disable
          const children = node.children;
          if (Tool.isNotEmpty(children)){
            for (let j = 0; j < children.length; j++){
              setDisable(children, children[j].id);
            }
          }

        } else {
          //当前结点不是目标结点，去子结点寻找
          const children = node.children;
          if (Tool.isNotEmpty(children)){
            setDisable(children, id);
          }
        }
      }

    };

    const deleteIds : Array<string> = [];
    const deleteNames : Array<string> = [];
    /**
     * 查找整根树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      //遍历某一层的结点
      for (let i = 0 ;i < treeSelectData.length; i++){
        const node = treeSelectData[i];
        if (node.id === id){
          //当前结点就是目标结点
          deleteIds.push(node.id);
          deleteNames.push(node.name);

          //把所有子结点都加上
          const children = node.children;
          if (Tool.isNotEmpty(children)){
            for (let j = 0; j < children.length; j++){
              getDeleteIds(children, children[j].id);
            }
          }

        } else {
          //当前结点不是目标结点，去子结点寻找
          const children = node.children;
          if (Tool.isNotEmpty(children)){
            getDeleteIds(children, id);
          }
        }
      }

    };


    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id ).then((response) => {
        const data = response.data;
        if (data.success){
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      //清空富文本框
      editor.txt.html("");

      // modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'});

    };

    /**
     * 增加
     */
    const add = () => {
      //清空富文本框
      editor.txt.html("");

      // modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value) || [];

      treeSelectData.value.unshift({id: 0, name: '无'});

    };

    const handleDelete = (id : number) => {
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value,id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：[' + deleteNames.join("，") + "] 删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
      });
    };


    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };


    onMounted(() => {
      handleQuery();

      editor.create();

    });

    return {
      param,
      // docs,
      level1,
      columns,
      loading,
      handleQuery,

      edit,
      add,

      doc,
      // modalVisible,
      modalLoading,
      handleSave,

      handleDelete,

      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose
    }
  }
});

</script>

