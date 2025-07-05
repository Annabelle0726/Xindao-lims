<template>
  <div class="app-container">
    <div style="display: flex;justify-content: space-between">
      <div class="search_box">
        <div class="search_item">
          <span class="search_label">客户名称</span>
          <el-input size="small" placeholder="请输入" clearable v-model="queryParams.company"
                    @keyup.enter.native="getList"></el-input>
        </div>
        <div class="search_button">
          <el-button type="primary" size="mini" @click="getList">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openFormDia('add')" icon="el-icon-plus">新增</el-button>
      </div>
    </div>
    <div>
      <lims-table :tableData="tableData" :column="column"
                  :height="'calc(100vh - 250px)'"
                  :page="page" :tableLoading="tableLoading"
                  @pagination="pagination"></lims-table>
    </div>
    <el-dialog :title="formTitle" :visible.sync="addDia" width="450px">
      <el-form ref="userForm" :model="user" :rules="userRules" label-position="right" label-width="100px">
        <el-form-item label="客户名称" prop="company">
          <el-input v-model="user.company" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="客户单位EN" prop="companyEn">
          <el-input v-model="user.companyEn" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="单位地址" prop="address">
          <el-input type="textarea" v-model="user.address" size="small" clearable
                    :autosize="{minRows: 2, maxRows: 4}"></el-input>
        </el-form-item>
        <el-form-item label="单位地址EN" prop="addressEn">
          <el-input type="textarea" v-model="user.addressEn" size="small" clearable
                    :autosize="{minRows: 2, maxRows: 4}"></el-input>
        </el-form-item>
        <el-form-item label="单位电话" prop="phone">
          <el-input v-model="user.phone" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="加急额度" prop="num">
          <el-input v-model="user.num" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="客户编号" prop="code2">
          <el-input v-model="user.code2" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="工厂域" prop="code">
          <el-input v-model="user.code" size="small" clearable></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
				<el-button @click="reset">取 消</el-button>
				<el-button type="primary" @click="customAdd" :loading="loading">确 定</el-button>
			</span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import {addCustom, delCustomById, selectCustomPageList, upCustom} from "@/api/system/customer";
export default {
  name: 'Customer',
  components: {
    limsTable
  },
  data() {
    return {
      queryParams: {
        company: ''
      },
      tableData: [],
      column: [
        {label: '客户名称', prop: 'company'},
        {label: '客户单位EN', prop: 'companyEn'},
        {label: '单位地址', prop: 'address'},
        {label: '单位地址EN', prop: 'addressEn'},
        {label: '单位电话', prop: 'phone'},
        {label: '加急额度', prop: 'num'},
        {label: '客户编号', prop: 'code2'},
        {label: '工厂域', prop: 'code'},
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
            },{
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.delete(row);
              },
            },
          ]
        }
      ],
      page: {
        total:0,
        size:20,
        current:1
      },
      tableLoading: false,
      // 编辑客户弹框
      operationType: '',
      formTitle: '',
      addDia: false,
      addPower: true,
      user: {
        company: ''
      },
      userRules: {
        company: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
        companyEn: [{ required: true, message: '请输入客户名称EN', trigger: 'blur' }],
        address: [{ required: true, message: '请输入单位地址', trigger: 'blur' }],
        addressEn: [{ required: true, message: '请输入单位地址EN', trigger: 'blur' }],
        num: [{ required: true, message: '请输入加急额度', trigger: 'blur' }],
        code2: [{ required: true, message: '请输入客户编号', trigger: 'blur' }],
      },
      loading: false,
      //
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 查询客户列表
    getList() {
      this.tableLoading = true
      selectCustomPageList({...this.queryParams, ...this.page}).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置table
    refresh() {
      this.queryParams.company = ''
      this.getList()
    },
    pagination (page) {
      this.page.size = page.limit
      this.getList()
    },
    openFormDia (type, row) {
      this.addDia = true
      this.user = {}
      this.formTitle = type === 'add' ? '新增客户' : '编辑客户'
      this.operationType = type
      if (type === 'edit') {
        this.user = this.HaveJson(row)
      }
    },
    customAdd() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.operationType === 'add') {
            addCustom(this.user).then(res => {
              this.loading = false
              if (res.code !== 200) return
              this.$message.success('新增成功')
              this.getList()
              this.reset()
            }).catch(err => {
              this.loading = false
            })
          } else {
            upCustom(this.user).then(res => {
              this.loading = false
              if (res.code !== 200) return
              this.$message.success('修改成功')
              this.getList()
              this.reset()
            }).catch(err => {
              this.loading = false
            })
          }
        }
      })
    },
    reset () {
      this.user = {}
      this.resetForm("userForm");
      this.addDia = false
    },
    delete (row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delCustomById({id: row.id}).then(res => {
          if (res.code === 500) {
            return
          }
          this.$message.success('删除成功')
          this.getList()
        }).catch(e => {
          this.$message.error('删除失败')
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.search_box {
  display: flex;
}
.search_item {
  margin-bottom: 18px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  line-height: 32px;
}
.search_label {
  width: 88px;
  font-size: 14px;
  font-weight: 700;
  color: #606266;
}
.search_button {
  line-height: 30px;
}
</style>
