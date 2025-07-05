<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">人员名称</span>
          <el-input size="small" placeholder="请输入" clearable v-model="entity.name"
                    @keyup.enter.native="refreshTable"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button :loading="outLoading" size="small" type="primary" @click="handleDown">导出</el-button>
        <el-button size="small" type="primary" @click="openSelectUserDia">新建</el-button>
      </div>
    </div>
    <div class="search-table">
      <el-table v-loading="tableLoading" :data="tableData"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                height="calc(100vh - 21em)" style="width: 100%;">
        <el-table-column align="center" label="序号" type="index" width="60"></el-table-column>
        <el-table-column label="员工编号" prop="account" width="150"></el-table-column>
        <el-table-column label="姓名" prop="name" width="120"></el-table-column>
        <el-table-column label="籍贯" prop="nativePlace" width="120"></el-table-column>
        <el-table-column label="证件地址" prop="idAddress" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="手机号" prop="telephone" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="毕业院校" prop="graduatedInstitutions1" width="100"></el-table-column>
        <el-table-column label="所学专业" prop="major1" width="100"></el-table-column>
        <el-table-column label="毕业时间" prop="graduationTime1" width="100"></el-table-column>
        <el-table-column label="最高学历" prop="officialAcademicRedentials" width="100"></el-table-column>
        <el-table-column label="最高学位" prop="highestDegree" width="100"></el-table-column>
        <el-table-column label="职称" prop="professionalTitle" width="100"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button size="small" type="text" @click="$emit('updatePerson', scope.row)">编辑</el-button>
            <el-button size="small" type="text" @click="deletePerson(scope.row)" style="color: #f56c6c">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :current-page.sync="page.current" :page-size="page.size"
        :page-sizes="[10, 20, 30, 50, 100]" :total="page.total" background
        layout="->,total, sizes, prev, pager, next, jumper" style="margin-top: 10px" @size-change="handleSizeChange"
        @current-change="handleCurrentChange">
      </el-pagination>
    </div>
    <el-dialog :visible.sync="selectUserDia" title="选择用户" width="70%">
      <div class="search" style="margin-bottom: 9px;">
        <div class="search_thing">
          <div class="search_label">用户名：</div>
          <div class="search_input">
            <el-input v-model="addUserTableInfo.name" clearable placeholder="请输入" size="small"
              @change="selectUserList"></el-input>
          </div>
          <el-button size="small" style="margin-left: 10px" type="primary" @click="selectUserList">查询</el-button>
        </div>
      </div>
      <div v-if="selectUserDia" class="body" style="height: 60vh;">
        <lims-table :tableData="tableData1" :column="column1" :isSelection="true" :handleSelectionChange="selectMethod"
          height="520" :tableLoading="tableLoading1"></lims-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="selectUserDia = false">取 消</el-button>
        <el-button type="primary" @click="selectUser">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { selectUserCondition } from "@/api/business/inspectionTask";
import limsTable from "@/components/Table/lims-table.vue";
import {
  basicInformationOfPersonnelSelectPage,
  delUserDepardLimsId,
  exportPersonBasicInfo,
  upUserDepardLimsId
} from "@/api/cnas/personal/personalList";
import store from "@/store";
import { Message } from "element-ui";

export default {
  name: 'PersonnelList',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
  props: {
    departId: {
      type: Number,
      default: () => {
        return null;
      }
    },
    isDepartment: {
      type: Boolean,
      default: false
    },
    currentCompaniesList: {
      type: Array,
      default: []
    }
  },
  data() {
    // 这里存放数据
    return {
      page: {
        size: 20,
        current: 1,
      },
      outLoading: false,
      tableLoading: false,
      tableData: [], // 人员总列表数据
      tableData1: [],
      tableLoading1: false,
      column1: [
        { label: '姓名', prop: 'name' },
        { label: '账号', prop: 'account' },
        { label: '角色', prop: 'roleName' },
        {
          dataType: 'tag',
          label: '状态',
          prop: 'status',
          formatData: (params) => {
            if (params == 0) {
              return '启用'
            } else {
              return ''
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success'
            } else {
              return 'danger'
            }
          }
        },
        { label: '电话号码', prop: 'phone' },
      ],
      page1: {
        total: 0,
        size: 10,
        current: 1
      },
      selectUserDia: false, // 添加人员弹框
      entity: {
        name: '',
        orderBy: {
          field: 'id',
          order: 'asc'
        }
      },
      addUserTableInfo: {
        name: null,
        isCustom: 0,
      },
      multipleSelection: []
    };
  },
  mounted() {
    this.refreshTable();
  },
  // 方法集合
  methods: {
    /**
     * @desc 获取设备id
     */
    // 重置
    refresh() {
      this.page = {
        size: 20,
        current: 1,
      };
      this.entity.name = ''
      this.refreshTable();
    },
    // 查询人员列表数据
    refreshTable() {
      this.tableLoading = true;
      this.entity.departLimsId = this.departId;
      const params = {
        size: this.page.size,
        current: this.page.current,
        departmentId: this.entity.departLimsId,
        name: this.entity.name,
      }
      basicInformationOfPersonnelSelectPage(params).then(res => {
        this.tableLoading = false;
        this.page.total = res.data.total;
        this.tableData = res.data.records;
      }).catch(err => {
        this.tableLoading = false;
      })
    },
    // 删除人员
    deletePerson(row) {
      this.$confirm('是否删除当前数据?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUserDepardLimsId({ id: row.userId }).then(res => {
          this.$message.success('删除成功');
          this.refreshTable();
          this.$emit('refreshTree')
        }).catch(e => {
          this.$message.error('删除失败');
        });
      }).catch(() => {
      });
    },
    handleSizeChange(val) {
      this.page.size = val;
      this.refreshTable();
    },
    handleCurrentChange(val) {
      this.page.current = val;
      this.refreshTable();
    },
    // 打开新增人员弹框
    openSelectUserDia() {
      this.selectUserDia = true;
      this.selectUserList()
    },
    // 查询新增弹框的人员列表
    selectUserList() {
      this.tableLoading1 = true
      selectUserCondition().then(res => {
        this.tableLoading1 = false
        this.tableData1 = res.data
      }).catch(err => {
        this.tableLoading1 = false
      })
    },
    // 表格选择方法
    selectMethod(val) {
      this.multipleSelection = val
    },
    // 提交需要添加的人员
    selectUser() {
      if (!this.currentCompaniesList.length > 0) {
        this.$message.warning("请选择部门！")
        return;
      }
      let selects = this.HaveJson(this.multipleSelection)
      if (selects.length === 0) {
        this.$message.error('未选择数据');
        return;
      }
      let ids = [];
      selects.forEach(a => {
        ids.push(a.id);
      });
      let str = '';
      this.currentCompaniesList.forEach(a => {
        if (a) {
          str += a + ',';
        }
      });
      upUserDepardLimsId({
        ids: JSON.stringify(ids),
        id: str
      }).then(res => {
        this.selectUserDia = false;
        this.$message.success('添加成功');
        this.refreshTable();
        this.$emit('refreshTree')
      });
    },
    // 导出人员信息
    handleDown() {
      this.outLoading = true;
      let entity = this.HaveJson(this.entity)
      delete entity.orderBy;
      exportPersonBasicInfo({ ...entity }).then(res => {
        this.outLoading = false;
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '人员信息.xlsx')
      })
    },
  },
  watch: {
    departId: {
      handler(newId, oldId) {
        if (newId) {
          this.page.current = 1
          this.refreshTable();
        }
      }
    }
  }
};
</script>

<style scoped>
.search_thing {
  display: flex;
  align-items: center;
}
</style>
