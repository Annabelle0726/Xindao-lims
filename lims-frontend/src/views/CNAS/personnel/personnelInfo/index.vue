<template>
  <div class="main">
    <div class="main_left">
      <el-row>
        <el-col :span="20">
          <el-input
            v-model="search"
            class="div_left_input"
            clearable
            placeholder="输入关键字进行搜索"
            size="small"
            suffix-icon="el-icon-search"
            @blur="searchFilter"
            @clear="searchFilter"
            @keyup.enter.native="searchFilter"
          ></el-input>
        </el-col>
        <el-col :span="4" style="text-align: center;line-height: 30px; margin-top: 14px">
          <el-button circle icon="el-icon-plus" size="mini" type="primary" @click="handleAdd"></el-button>
        </el-col>
      </el-row>
      <el-tree
        ref="tree"
        :data="list"
        :default-expanded-keys="[22]"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        :props="{ children: 'children', label: 'name' }"
        highlight-current
        node-key="id"
        style="height:calc(100% - 70px);overflow-y: scroll;scrollbar-width: none;"
        @node-click="handleNodeClick"
        @node-expand="nodeOpen"
        @node-collapse="nodeClose"
      >
        <div slot-scope="{ node, data }" class="custom-tree-node">
          <el-row style="width: 100%;">
            <el-col :span="22" style="text-align: left;">
              <span><i
                :class="`node_i ${data.children != undefined&&data.children.length>0 ? 'el-icon-folder-opened' : 'el-icon-tickets'}`"></i>
                {{ data.name }}</span>
            </el-col>
            <el-col v-if="node.level>1 && data.id !== null" :span="2" style="text-align: right;">
              <el-button size="mini" type="text" @click.stop="remove(node, data)">
                <i class="el-icon-delete"></i>
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-tree>
    </div>
    <div class="main_right">
      <el-tabs v-model="activeName" :lazy="true" type="border-card">
        <el-tab-pane label="人员基本信息" name="人员基本信息">
          <PersonnelList v-if="activeName === '人员基本信息' && isShowAll" ref="personnelList" :currentCompaniesList="currentCompaniesList"
                         :departId="departId" @refreshTree="refreshTree"
                         @updatePerson="updatePerson"></PersonnelList>
          <personnelInformation v-if="activeName === '人员基本信息' && !isShowAll" :clickNodeVal="clickNodeVal"></personnelInformation>
        </el-tab-pane>
        <el-tab-pane label="培训计划" name="培训计划">
          <PersonnelTraining v-if="activeName === '培训计划'" ref="personnelTraining"
                             :departId="departId" :isDepartment="isDepartment"></PersonnelTraining>
        </el-tab-pane>
        <el-tab-pane label="岗位职责" name="岗位职责">
          <job-responsibilities v-if="activeName === '岗位职责'" ref="jobResponsibilities"
                                :departId="departId"
                                :isDepartment="isDepartment"></job-responsibilities>
        </el-tab-pane>
        <el-tab-pane label="奖惩记录" name="奖惩记录">
          <rewardPunishmentRecord v-if="activeName === '奖惩记录'"
                                  :departId="departId" :isDepartment="isDepartment"></rewardPunishmentRecord>
        </el-tab-pane>
        <el-tab-pane label="培训记录" name="培训记录">
          <training-record v-if="activeName === '培训记录'" ref="trainingRecord"
                           :departId="departId"
                           :isDepartment="isDepartment"></training-record>
        </el-tab-pane>
        <el-tab-pane label="任职授权记录" name="任职授权记录">
          <Mandate v-if="activeName === '任职授权记录'" ref="manDateRef" :departId="departId" :isDepartment="isDepartment"></Mandate>
        </el-tab-pane>
        <el-tab-pane label="人员能力" name="人员能力">
          <personnel-capacity v-if="activeName === '人员能力'" ref="personnelCapacity"
                              :departId="departId"
                              :isDepartment="isDepartment"></personnel-capacity>
        </el-tab-pane>
        <el-tab-pane label="沟通记录" name="沟通记录">
          <Communicate v-if="activeName === '沟通记录'" ref="communicateRef" :departId="departId" :isDepartment="isDepartment"></Communicate>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog :visible.sync="addDia" title="架构新增" width="400px">
      <div class="body">
        <el-row style="line-height: 50px;">
          <el-col :span="6" style="text-align: right;">
            <span class="required-span">* </span>架构名称：
          </el-col>
          <el-col :offset="1" :span="16">
            <el-input v-model="addOb.name" clearable placeholder="请输入架构名称" size="small" @keyup.enter.native="addStandardTree"></el-input>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDia = false">取 消</el-button>
        <el-button type="primary" @click="addStandardTree">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PersonnelList from './tabs/personnel-list.vue'
import personnelInformation from './tabs/personnel-information.vue'
import PersonnelTraining from './tabs/personnelTraining';
import JobResponsibilities from './tabs/job-responsibilities.vue';
import rewardPunishmentRecord from "./tabs/reward-punishment-record.vue";
import TrainingRecord from './tabs/training-record.vue';
import Mandate from './tabs/mandate.vue';
import PersonnelCapacity from './tabs/personnel-capacity.vue';
import Communicate from './tabs/communicate.vue'
import {addDepartmentLims, delDepartmentLims, selectCNSAPersonTree} from "@/api/cnas/personal/personalList";
export default {
  name: 'PersonnelInfo',
  components: {
    PersonnelList, personnelInformation, PersonnelTraining, JobResponsibilities, rewardPunishmentRecord, TrainingRecord, Mandate, PersonnelCapacity, Communicate
  },
  data() {
    return {
      isShowAll: true, //  是否展示标签栏
      activeName: '人员基本信息',
      departId: 1,
      list: [],
      addDia: false,
      addOb: {
        name: '',
        fatherId: ''
      },
      search: '',
      clickNodeVal: {},
      addUserForm: {
        name: ''
      },
      currentCompaniesList: [],
      entity: {
        name: '',
        departLimsId: '',
        orderBy: {
          field: 'id',
          order: 'asc'
        }
      },
      overallRecord: '人员列表',
      isDepartment: false,
    };
  },
  mounted() {
    this.activeName = this.$route.query.activeName || '人员基本信息'
    this.geList();
  },
  methods: {
    remove(node, data) {
      this.$confirm("是否删除该层级", "提示", {
        type: "error"
      }).then(() => {
        delDepartmentLims({
          id: data.id
        }).then(res => {
          this.$message.success('已删除')
          this.geList();
        })
      }).catch(e => {})
    },
    addStandardTree() {
      if (this.addOb.name == null || this.addOb.factory == '') {
        this.$message.error('构架名称是必填项')
        return
      }
      addDepartmentLims(this.addOb).then(res => {
        this.$message.success('添加成功')
        this.addDia = false
        this.geList();
        this.addOb.name = ''
        this.addOb.fatherId = ''
      })
    },
    handleAdd() {
      if (this.addOb.fatherId) {
        this.addDia = true;
      } else {
        this.$message.error('请选择一个架构层级')
      }
    },
    // 人员列表编辑
    updatePerson(row) {
      const node = this.findNodeById(this.list, row.name);
      if (node) {
        this.handleNodeClick(node);
      } else {
        this.$message.warning('未找到该人员');
      }
    },
    // 新建人员后刷新树
    refreshTree () {
      this.geList()
    },
    findNodeById(treeData, name) {
      for (let i = 0; i < treeData.length; i++) {
        if (treeData[i].name === name) {
          return treeData[i]; // 找到节点，返回该节点
        }
        if (treeData[i].children && treeData[i].children.length > 0) {
          const foundNode = this.findNodeById(treeData[i].children, name);
          if (foundNode) {
            return foundNode; // 在子节点中找到，返回该节点
          }
        }
      }
      return null; // 没有找到节点，返回null
    },
    searchFilter() {
      this.$refs.tree.filter(this.search);
    },
    // 获取树
    geList() {
      selectCNSAPersonTree().then(res => {
        this.list = res.data;
        if(this.list.length > 0) {
          this.isDepartment = true;
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    nodeClose(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder');
    },
    nodeOpen(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder-opened');
    },
    handleNodeClick(val, node, el) {
      //树的值
      this.clickNodeVal = val;
      // 存储父级节点级数
      if (node) {
        this.getNodeParent(node);
        this.clickNodeVal.level = node.level;
        this.clickNodeVal.parent = node.parent.data;
      }
      this.entity.departLimsId = val.id;
      this.addOb.fatherId = val.id;
      // 是否显示设备详情
      this.isShowAll = val.id !== null;
      if (val.id) {	// 如果是实验室
        this.departId = val.id;
        this.isDepartment = true;
      }
      if (val.userId) { // 是人员
        this.departId = val.userId;
        this.isDepartment = false
      }
    },
    getNodeParent(val) {
      if (val.parent != null) {
        this.currentCompaniesList[val.level - 1] = val.data.id;
        this.selectTree += ' - ' + val.label;
        this.getNodeParent(val.parent);
      }
    }
  }
};
</script>

<style scoped>
.node_i {
  color: orange;
  font-size: 18px;
}
.custom-tree-node {
  width: 80%;
  line-height: 32px;
}
.custom-tree-node .el-icon-delete {
  color: #3A7BFA;
  opacity: 0;
  font-size: 15px;
}

.custom-tree-node:hover .el-icon-delete {
  opacity: 1;
}

.main {
  display: flex;
  padding: 15px 0;
}

.main_left {
  background: #ffffff;
  text-align: center;
  height: calc(100vh - 8em);
  width: 270px;
  border-radius: 15px;
}

.main_right {
  width: calc(100% - 288px);
  border-radius: 15px;
}

.div_left_input {
  margin: 15px 0;
  width: 90%;
}

>>> .el-tabs--border-card > .el-tabs__header .el-tabs__item {
  border: 0 none;
}

>>> .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active {
  color: black;
}

>>> .el-tabs--border-card > .el-tabs__header {
  border-bottom: none;
}
</style>
