<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            :tree-data="level1"
            @select="onSelect"
            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
            :defaultExpandAll="true"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2></h2>
            <div>
              <span>阅读数：</span> &nbsp; &nbsp;
              <span>点赞数：</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="editor-content-view" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button type="primary">
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>




<script lang="ts">
import { defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import { Tool } from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Doc',
  setup() {
    const route = useRoute()
    console.log("路由：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);


    const docs = ref();

    const html = ref();

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
      level1.value= [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        const data = response.data;
        if (data.success){
          docs.value=data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value,0);

        } else {
          message.error(data.message);
        }

      });
    };


    /**
     * 内容查询
     **/
    const handleQueryContent = (id: number) => {
      axios.get("/doc/find-content/" + id ).then((response) => {
        const data = response.data;
        if (data.success){
          html.value = data.content;
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected',selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)){
        //加载内容
        handleQueryContent(selectedKeys[0]);
      }
    }


    onMounted(() => {
      handleQuery();


    });

    return {
      level1,
      html,
      onSelect
    }
  }
});

</script>


<style>
/* wangeditor默认样式 */
.editor-content-view {
  border: 3px solid #ccc;
  border-radius: 5px;
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
}

.editor-content-view p,
.editor-content-view li {
  white-space: pre-wrap; /* 保留空格 */
}

.editor-content-view blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.editor-content-view code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.editor-content-view pre>code {
  display: block;
  padding: 10px;
}

.editor-content-view table {
  border-collapse: collapse;
}
.editor-content-view td,
.editor-content-view th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.editor-content-view th {
  background-color: #f1f1f1;
}

.editor-content-view ul,
.editor-content-view ol {
  padding-left: 20px;
}

.editor-content-view input[type="checkbox"] {
  margin-right: 5px;
}

/* 和antdv p冲突，覆盖掉 */
.editor-content-view p {
  font-family:"YouYuan";
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight:600;
}

/* 点赞 */
.vote-div {
  padding: 15px;
  text-align: center;
}

/* 图片自适应 */
.wangeditor img {
  max-width: 100%;
  height: auto;
}

/* 视频自适应 */
.wangeditor iframe {
  width: 100%;
  height: 400px;
}
</style>

