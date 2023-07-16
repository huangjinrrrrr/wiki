<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
      <h3 v-if="level1.length === 0">对不起，找不到相关文档</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            :tree-data="level1"
            @select="onSelect"
            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
            :defaultExpandAll="true"
            :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{doc.name}}</h2>
            <div>
              <span>阅读数：{{doc.viewCount}}</span> &nbsp; &nbsp;
              <span>点赞数：{{doc.voteCount}}</span>
            </div>
            <a-divider style="height: 2px; background-color: #9999cc"/>
          </div>
          <div class="wangeditor" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button type="primary" shape="round" :size="'large'" @click="vote">
              <template #icon><LikeOutLine /> &nbsp;点赞：{{doc.voteCount}}</template>
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

    const docs = ref();

    const html = ref();

    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

    //当前选中的文档
    const doc = ref();
    doc.value = {};

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

          if (Tool.isNotEmpty(level1.value)){
            //将结点设置为选中状态
            defaultSelectedKeys.value = [level1.value[0].id];
            handleQueryContent(level1.value[0].id);

            //显示初始文档
            doc.value = level1.value[0];
          }

        } else {
          message.error(data.message);
        }

      });
    };




    const onSelect = (selectedKeys: any, info: any) => {
      console.log('selected',selectedKeys, info);
      if (Tool.isNotEmpty(selectedKeys)){
        //加载文档信息
        doc.value = info.selectedNodes[0].props;

        //加载内容
        handleQueryContent(selectedKeys[0]);
      }
    };
    
    
    const vote = () => {
      axios.get('/doc/vote/' + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      });
    };


    onMounted(() => {
      handleQuery();


    });

    return {
      level1,
      html,
      onSelect,
      defaultSelectedKeys,
      doc,
      vote
    }
  }
});

</script>


<style>
.vote-div {
  padding: 15px;
  text-align: center;
}


/* wangeditor默认样式 */
.wangeditor {
  border: 3px solid #ffffff;
  border-radius: 5px;
  padding: 0 10px;
  margin-top: 20px;
  overflow-x: auto;
}

.wangeditor p,
.wangeditor li {
  white-space: pre-wrap; /* 保留空格 */
}

.wangeditor blockquote {
  border-left: 8px solid #d0e5f2;
  padding: 10px 10px;
  margin: 10px 0;
  background-color: #f1f1f1;
}

.wangeditor code {
  font-family: monospace;
  background-color: #eee;
  padding: 3px;
  border-radius: 3px;
}
.wangeditor pre>code {
  display: block;
  padding: 10px;
}

.wangeditor table {
  border-collapse: collapse;
}
.wangeditor td,
.wangeditor th {
  border: 1px solid #ccc;
  min-width: 50px;
  height: 20px;
}
.wangeditor th {
  background-color: #f1f1f1;
}

.wangeditor ul,
.wangeditor ol {
  padding-left: 20px;
}

.wangeditor input[type="checkbox"] {
  margin-right: 5px;
}

/* 和antdv p冲突，覆盖掉 */
.wangeditor p {
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

