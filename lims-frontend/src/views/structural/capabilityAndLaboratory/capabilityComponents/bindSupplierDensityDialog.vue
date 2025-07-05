<template>
  <div>
    <el-dialog title="厂家密度绑定" :visible.sync="isShow" width="800px" @close="$emit('closeBindPartDialog')">
      <lims-table :tableData="tableData" :column="column" height="460"
                  :page="page" :tableLoading="tableLoading"></lims-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('closeBindPartDialog')">取 消</el-button>
        <el-button type="primary" @click="addBindPart" :loading="addBindLoad">新 增</el-button>
      </span>
    </el-dialog>
    <el-dialog title="操作厂家密度绑定" :visible.sync="addBindSupplierDensityDialog" width="400px" @close="closeBindPartDialog":close-on-click-modal="false" >
      <div class="body" v-if="addBindSupplierDensityDialog">
        <el-form label-position="right" label-width="80px"
                 ref="bindSupplierDensityData"
                 :rules="bindPartDataRules"
                 :model="bindSupplierDensityData">
          <el-form-item label="型号：" prop="model">
            <el-input v-model="bindSupplierDensityData.model" size="small"></el-input>
          </el-form-item>
          <el-form-item label="厂家：" prop="supplierName">
            <el-input v-model="bindSupplierDensityData.supplierName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="密度：" prop="densityValue">
            <el-input v-model="bindSupplierDensityData.densityValue" size="small"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeBindPartDialog">取 消</el-button>
        <el-button type="primary" @click="submitBind" :loading="bindLoad">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  addProductSupplierDensity,
  deleteProductSupplierDensity,
  selectSupplierDensityByProductId, updateProductSupplierDensity
} from "@/api/structural/capability";

export default {
  name: "bindSupplierDensityDialog",
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable},
  props: {
    bindSupplierDensityDialog: {
      type: Boolean,
      default: () => false
    },
    currentRow : { // 选择本条数据的信息
      type: Object,
      default: () => {}
    }
  },
  data() {
    // 这里存放数据
    return {
      tableData: [],
      column: [
        {label: '型号', prop: 'model'},
        {label: '厂家名称', prop: 'supplierName'},
        {label: '密度值', prop: 'densityValue'},
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '140px',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.editForm(row);
              },
            },
            {
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
        size:10,
        current:1,
        layout: 'total, prev, pager, next'
      },
      tableLoading: false,
      searchUrl: '', // 查询
      upUrl: '', // 编辑
      delUrl: '',  // 删除
      isShow: this.bindSupplierDensityDialog,
      addBindSupplierDensityDialog: false,
      bindSupplierDensityData: {
        id: '',
        model: '', // 型号
        supplierName: '', // 厂家
        densityValue: '', // 密度
      },
      bindPartDataRules: {
        supplierName: [
          { required: true, message: '请填写厂家名称', trigger: 'blur' }
        ],
        densityValue: [
          { required: true, message: '请填写密度', trigger: 'blur' }
        ],
      },
      bindLoad: false,
      upIndex: 0,
      addBindLoad: false,
      operationType: ''
    }
  },
  mounted() {
    this.getList()
  },
  // 方法集合
  methods: {
    getList () {
      this.tableLoading = true
      selectSupplierDensityByProductId({...this.page, productId: this.currentRow.id}).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    addBindPart () {
      this.addBindSupplierDensityDialog = true
      this.operationType = 'add'
    },
    editForm (row) {
      this.addBindSupplierDensityDialog = true
      this.bindSupplierDensityData = this.HaveJson(row)
      this.operationType = 'edit'
    },
    // 提交厂家密度绑定
    submitBind () {
      this.$refs['bindSupplierDensityData'].validate((valid) => {
        if (valid) {
          // 根据类型判断是检验对象零件绑定还是产品维护零件绑定
          const params = {
            id: this.bindSupplierDensityData.id,
            productId: this.currentRow.id,
            model: this.bindSupplierDensityData.model,
            supplierName: this.bindSupplierDensityData.supplierName,
            densityValue: this.bindSupplierDensityData.densityValue,
            }
          this.bindLoad = true
          if (this.operationType === 'add') {
            addProductSupplierDensity(params).then(res => {
              this.bindLoad = false
              if (res.code === 200) {
                this.resetForm('bindSupplierDensityData')
                this.addBindSupplierDensityDialog = false
                this.$message.success('新增成功')
                this.getList()
              }
            }).catch(err => {
              this.bindLoad = false
              console.log(err)
            })
          } else {
            updateProductSupplierDensity(params).then(res => {
              this.bindLoad = false
              if (res.code === 200) {
                this.resetForm('bindSupplierDensityData')
                this.addBindSupplierDensityDialog = false
                this.$message.success('修改成功')
                this.getList()
              }
            }).catch(err => {
              this.bindLoad = false
              console.log(err)
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    // 删除
    delete (row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProductSupplierDensity({id:row.id}).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.getList();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    closeBindPartDialog () {
      this.$refs['bindSupplierDensityData'].resetFields();
      this.addBindSupplierDensityDialog = false
    },
  },
}
</script>

<style scoped>
</style>
