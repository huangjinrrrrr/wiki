<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>

            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal title="文档表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk">
    <a-form :model="doc" :label-col="{span : 6}" :wrapper-col="{span : 18}">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="父分类">
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

      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from "@/util/tool";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父分类',
        key: 'parent',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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


    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      level1.value= [];
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success){
          docs.value=data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);
          console.log("树形结构：" , level1.value);

        } else {
          message.error(data.message);
        }

      });
    };


    //表单
    const treeSelectData = ref();
    treeSelectData.value=[];
    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);

    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/doc/save",doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;
        if (data.success){

          modalVisible.value =false;
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

    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);

      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    const add = () => {
      modalVisible.value = true;
      doc.value = {}

      treeSelectData.value = Tool.copy(level1.value);

      treeSelectData.value.unshift({id: 0, name: '无'});
    };

    const handleDelete = (id : number) => {
      axios.delete("/doc/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success){
          handleQuery();
        }
      });
    };


    onMounted(() => {
      handleQuery();
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
      modalVisible,
      modalLoading,
      handleModalOk,

      handleDelete,

      treeSelectData
    }
  }
});

</script>

