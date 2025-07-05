<template>
  <div class="purchase-page">
    <div class="purchase-left">
      <el-input
        v-model="form.nodeName"
        placeholder="请输入节点名称"
        suffix-icon="el-icon-search"
        size="small"
        clearable
        @keyup.enter.native="searchFilter(treeData)"
        @blur="searchFilter(treeData)"
        @clear="searchFilter(treeData)"
      >
      </el-input>
      <el-tree
        ref="tree"
        :data="treeData"
        :props="defaultProps"
        @node-click="handleNodeClick"
        :default-expanded-keys="expandedKeys"
        node-key="id"
      >
      </el-tree>
    </div>
    <div class="purchase-right">
      <el-tabs v-model="activeName">
        <el-tab-pane label="耗材总览" name="first" :lazy="true">
          <ConsumableOverview v-if="activeName == 'first'" :contentsId="contentsId"></ConsumableOverview>
        </el-tab-pane>
        <el-tab-pane label="耗材列表" name="second" :lazy="true">
          <ConsumableList v-if="activeName == 'second'" :contentsId="contentsId"></ConsumableList>
        </el-tab-pane>
        <el-tab-pane label="耗材入库" name="third" :lazy="true">
          <Store v-if="activeName == 'third'" :contentsId="contentsId"></Store>
        </el-tab-pane>
        <el-tab-pane label="目录维护" name="fourth" :lazy="true">
          <Contents
            v-if="activeName == 'fourth'"
            :id="contentsId"
            @contentsUpdate="contentsUpdate"
            :treeData="treeData"
            from="耗材树"
          ></Contents>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
import Contents from "./component/contents.vue";
import Store from "./component/Store.vue";
import ConsumableOverview from "./component/ConsumableOverview.vue";
import ConsumableList from "./component/ConsumableList.vue"
import {
  directoryListing
} from '@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro'

export default {
  name: 'ServiceAndSupplyPro',
  data() {
    return {
      tabsKey: 0,
      contentsId: 0,
      activeName: "first",
      form: {
        nodeName: "",
      },
      treeData: [],
      expandedKeys: [],
      defaultProps: {
        children: "children",
        label: "nodeName",
      },
    };
  },
  components: {
    Contents,
    Store,
    ConsumableOverview,
    ConsumableList
  },
  watch: {
    contentsId(newVal, oldVal) {
      if(newVal != oldVal) {
        this.tabsKey = Math.random();
      }
    }
  },
  methods: {
    searchFilter() {
      this.treeData = JSON.parse(JSON.stringify(this.treeData));
      this.expandedKeys = [];
      if(this.form.nodeName == "") {
        return;
      }
      const findNodesWithFiber = (nodes) => {
        nodes.forEach((node) => {
          if (node.nodeName && node.nodeName.includes(this.form.nodeName)) {
            this.expandedKeys.push(node.id);
          }
          if (node.children && node.children.length > 0) {
            findNodesWithFiber(node.children);
          }
        });
      };
      findNodesWithFiber(this.treeData);
    },

    // 目录维护更新
    contentsUpdate(val, flag = false) {
      if (val) {
        this.getTreeData();
        this.expandedKeys = [];
        if (flag) {
          const findNodesWithFiber = (nodes) => {
            nodes.forEach((item) => {
              if (item.parentId == val) {
                this.expandedKeys.push(item.id);
              }
              if(item.children && item.children.length > 0) {
                findNodesWithFiber(item.children);
              }
            });
          };
          findNodesWithFiber(this.treeData);
          this.expandedKeys = this.expandedKeys.filter((item) => item !== val);
        } else {
          this.expandedKeys.push(val);
        }
      }else {
        this.getTreeData();
      }
    },
    // 查询所有目录
    getTreeData() {
      directoryListing().then((res) => {
          this.treeData = res.data;
        });
    },
    // 点击树节点
    handleNodeClick(data) {
      this.contentsId = data.id;
    },
  },
  created() {
    this.getTreeData();
  },
};
</script>

<style scoped>
.purchase-left {
  width: 250px;
  height: 100%;
  background: #fff;
  border-radius: 16px;
  box-sizing: border-box;
  padding: 10px 16px;
  flex-shrink: 0;
}
.purchase-right {
  background: #fff;
  width: calc(100% - 15em);
  height: 100%;
  box-sizing: border-box;
  padding: 0 20px 0 10px;
}
.purchase-page {
  display: flex;
  padding-top: 10px;
  padding-bottom: 10px;
  box-sizing: border-box;
  width: 100%;
}
</style>
