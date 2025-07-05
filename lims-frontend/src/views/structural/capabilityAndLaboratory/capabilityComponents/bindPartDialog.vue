<template>
  <div>
    <el-dialog title="零件绑定" :visible.sync="isShow" width="800px" @close="$emit('closeBindPartDialog')">
      <lims-table :tableData="tableData" :column="column" height="460"
                  :page="page" :tableLoading="tableLoading"></lims-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('closeBindPartDialog')">取 消</el-button>
        <el-button type="primary" @click="addBindPart('add')" :loading="addBindLoad">新 增</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="dialogTitle" :visible.sync="addBindPartDialog" width="400px" @close="closeBindPartDialog"
               :close-on-click-modal="false" >
      <div class="body" v-if="addBindPartDialog">
        <el-form label-position="right" label-width="80px"
                 ref="bindPartData"
                 :rules="bindPartDataRules"
                 :model="bindPartData">
          <el-form-item label="零件号：" prop="partNo">
            <el-input v-model="bindPartData.partNo" size="small"></el-input>
          </el-form-item>
          <el-form-item label="颜色：" prop="color">
            <el-input v-model="bindPartData.color" size="small"></el-input>
          </el-form-item>
          <el-form-item label="色标：" prop="colorCode">
            <el-input v-model="bindPartData.colorCode" size="small"></el-input>
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
  addProductPart,
  addTestObjectPart, deleteProductPart, deleteTestObjectPart,
  selectByProductId,
  selectByTestObjectId, updateProductPart, updateTestObjectPart
} from "@/api/structural/structureTestObjectPart";
import {delProduct} from "@/api/structural/capability";

export default {
  name: "bindPartDialog",
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable},
  props: {
    bindPartDialog: {
      type: Boolean,
      default: () => false
    },
    type: { // 零件绑定的类型--0：检验对象，1：产品维护
      type: Number,
      default: () => null
    },
    currentRow : { // 选择本条数据的信息
      type: Object,
      default: () => {}
    }
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.bindPartDialog,
      dialogTitle: '新增零件绑定',
      operationType: '',
      tableData: [],
      tableLoading: false,
      column: [
        {label: '零件号', prop: 'partNo'},
        {label: '颜色', prop: 'color'},
        {label: '色标', prop: 'colorCode'},
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.addBindPart('edit', row);
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
      bindPartComponent: {
        entity: {
          orderBy: {
            field: 'id',
            order: 'asc'
          }
        },
        isPage: true,
        isIndex: true,
        showSelect: false,
        select: true,
        do: [{
          id: 'update',
          font: '编辑',
          type: 'text',
          method: 'doDiy',
          field: []
        }, {
          id: 'delete',
          font: '删除',
          type: 'text',
          method: 'doDiy'
        }],
        tagField: {},
        selectField: {},
        requiredAdd: ['partNo'],
        requiredUp: ['partNo'],
      },
      addBindPartDialog: false,
      bindPartData: {
        partNo: '', // 零件号
        color: '', // 颜色
        colorCode: '', // 色标
      },
      bindPartDataRules: {
        partNo: [
          { required: true, message: '请填写零件号', trigger: 'blur' }
        ]
      },
      bindLoad: false,
      upIndex: 0,
      addBindLoad: false,
    }
  },
  mounted() {
    this.getList()
  },
  // 方法集合
  methods: {
    getList() {
      this.tableLoading = true
      // 根据类型判断是检验对象零件绑定还是产品维护零件绑定
      if (this.type === 0) {
        selectByTestObjectId({testObjectId: this.currentRow.id}).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.tableData = res.data.records
            this.page.total = res.data.total
          }
        }).catch(err => {
          this.tableLoading = false
        })
      } else {
        selectByProductId({productId: this.currentRow.id}).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.tableData = res.data.records
            this.page.total = res.data.total
          }
        }).catch(err => {
          this.tableLoading = false
        })
      }
    },
    addBindPart (type, row) {
      this.addBindPartDialog = true
      this.dialogTitle = type === 'add' ? '新增零件绑定' : '修改零件绑定'
      this.operationType = type
      if (type === 'edit') {
        this.bindPartData = this.HaveJson(row)
      }
    },
    // 提交零件绑定
    submitBind () {
      this.$refs['bindPartData'].validate((valid) => {
        if (valid) {
          // 根据类型判断是检验对象零件绑定还是产品维护零件绑定
          const params = this.type === 0 ? {
            testObjectId: this.currentRow.id,
            color: this.bindPartData.color,
            colorCode: this.bindPartData.colorCode,
            partNo: this.bindPartData.partNo,
            } : {
            productId: this.currentRow.id,
            color: this.bindPartData.color,
            colorCode: this.bindPartData.colorCode,
            partNo: this.bindPartData.partNo,
          }
          this.bindLoad = true
          if (this.operationType === 'add') {
            if (this.type === 0) {
              addTestObjectPart(params).then(res => {
                if (res.code === 200) {
                  this.resetForm('bindPartData')
                  this.addBindPartDialog = false
                  this.$message.success('新增成功')
                  this.getList()
                }
              }).catch(err => {
                this.bindLoad = false
                console.log(err)
              })
            } else {
              addProductPart(params).then(res => {
                if (res.code === 200) {
                  this.resetForm('bindPartData')
                  this.addBindPartDialog = false
                  this.$message.success('新增成功')
                  this.getList()
                }
              }).catch(err => {
                this.bindLoad = false
                console.log(err)
              })
            }
          } else {
            if (this.type === 0) {
              updateTestObjectPart(params).then(res => {
                if (res.code === 200) {
                  this.resetForm('bindPartData')
                  this.addBindPartDialog = false
                  this.$message.success('修改成功')
                  this.getList()
                }
              }).catch(err => {
                this.bindLoad = false
                console.log(err)
              })
            } else {
              updateProductPart(params).then(res => {
                if (res.code === 200) {
                  this.resetForm('bindPartData')
                  this.addBindPartDialog = false
                  this.$message.success('修改成功')
                  this.getList()
                }
              }).catch(err => {
                this.bindLoad = false
                console.log(err)
              })
            }
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    delete (row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.type === 0) {
          deleteTestObjectPart({id:row.id}).then(res => {
            if (res.code === 200) {
              this.$message.success('删除成功')
              this.getList();
            }
          })
        } else {
          deleteProductPart({id:row.id}).then(res => {
            if (res.code === 200) {
              this.$message.success('删除成功')
              this.getList();
            }
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    closeBindPartDialog () {
      this.resetForm('bindPartData')
      this.addBindPartDialog = false
    },
  },
}
</script>

<style scoped>
</style>
