<template>
  <div class="capacity-scope">
    <div>
      <el-row class="title">
        <el-col :span="12" style="text-align: left">
          <el-radio-group v-model="radio" size="medium" fill="#409EFF" @change="refreshTable">
            <el-radio-button :label="0">检验项目参数</el-radio-button>
            <el-radio-button :label="1">检验对象</el-radio-button>
          </el-radio-group>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <el-upload v-if="radio === 0" ref='upload1'
                     style="display: inline;margin-right: 8px"
                     :action="uploadAction1"
                     :before-upload="beforeUpload1" :headers="token" :on-error="onError1"
                     :on-success="handleSuccessUp1" :show-file-list="false" accept='.doc,.docx,.xls,.xlsx'>
            <el-button size="small" type="primary">导入</el-button>
          </el-upload>
          <el-button size="small" type="primary" v-if="radio === 1" @click="uploadDia = true">导入</el-button>
          <el-button size="small" type="primary" @click="openAdd()">新增</el-button>
        </el-col>
      </el-row>
    </div>
    <div>
      <el-form :model="itemParameterForm" ref="itemParameterForm" size="small" :inline="true">
        <el-form-item label="检验项" prop="inspectionItem" v-if="radio===0">
          <el-input size="small" placeholder="请输入" clearable
                    v-model="itemParameterForm.inspectionItem" @keyup.enter.native="refreshTable()">
          </el-input>
        </el-form-item>
        <el-form-item label="检验子项" prop="inspectionItemSubclass" v-if="radio===0">
          <el-input size="small" placeholder="请输入" clearable
                    v-model="itemParameterForm.inspectionItemSubclass" @keyup.enter.native="refreshTable()">
          </el-input>
        </el-form-item>
        <el-form-item label="检验对象" prop="sample" v-if="radio===0">
          <el-input size="small" placeholder="请输入" clearable
                    v-model="itemParameterForm.sample" @keyup.enter.native="refreshTable()">
          </el-input>
        </el-form-item>
        <el-form-item label="检验对象" prop="specimenName" v-if="radio===1">
          <el-input size="small" placeholder="请输入" clearable
                    v-model="itemParameterForm.specimenName" @keyup.enter.native="refreshTable()">
          </el-input>
        </el-form-item>
        <el-form-item label="零件号" prop="partNo" v-if="radio===1">
          <el-input size="small" placeholder="请输入" clearable
                    v-model="itemParameterForm.partNo" @keyup.enter.native="refreshTable()">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
<!--      检验项目参数表格-->
      <div class="table" v-if="radio===0">
        <lims-table :tableData="tableData" :column="column" key="tableData"
                    @pagination="pagination" :height="'calc(100vh - 290px)'"
                    :page="page" :tableLoading="tableLoading"></lims-table>
      </div>
<!--      检验对象表格-->
      <div class="table" v-if="radio===1">
        <lims-table :tableData="testObjectTableData" :column="testObjectColumn" key="testObjectTableData"
                    @pagination="pagination" :height="'calc(100vh - 290px)'"
                    :page="testObjectPage" :tableLoading="tableLoading"></lims-table>
      </div>
    </div>
    <!--产品维护弹框-->
    <el-dialog title="产品维护" :visible.sync="diaProduct" width="900px">
      <lims-table :tableData="productData" :column="productColumn" height="460"
                  @pagination="productPagination"
                  :page="productPage" :tableLoading="productableLoading"></lims-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="diaProduct = false">取 消</el-button>
        <el-button type="primary" @click="editProduct('add')" :loading="productLoad">新 增</el-button>
      </span>
    </el-dialog>
    <!--产品维护编辑-->
    <el-dialog title="操作产品维护" :visible.sync="productEditDia" width="400px">
      <el-form :model="productEditForm" ref="productEditForm" :rules="productRules" label-position="right" label-width="100px">
        <el-form-item label="产品名称" prop="name">
          <el-input size="small" placeholder="请输入" clearable v-model="productEditForm.name"></el-input>
        </el-form-item>
        <el-form-item label="产品名称EN" prop="nameEn">
          <el-input size="small" placeholder="请输入" clearable v-model="productEditForm.nameEn">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeProduct">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitProduct">确 认</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="uploadDia" title="数据导入" width="500px">
      <div style="margin: 0 auto;">
        <el-upload ref="upload" :action="uploadAction"
                   :auto-upload="false" :file-list="fileList"
                   :headers="token" :limit="1"
                   :on-change="beforeUpload" :on-error="onError" :on-success="onSuccess" accept=".xlsx" drag
                   name="file">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="uploadDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitUpload()">上 传</el-button>
      </span>
    </el-dialog>
    <!--检验项目参数-编辑弹框-->
    <EditForm ref="editForm" @refreshList="refreshList1"></EditForm>
    <testObjectEditForm ref="testObjectEditForm" @refreshList="refreshList"></testObjectEditForm>
    <BindPartDialog v-if="bindPartDialog" :bindPartDialog="bindPartDialog"
                    :currentRow="currentRow"
                    :type="type"
                    @closeBindPartDialog="closeBindPartDialog"></BindPartDialog>
    <BindSupplierDensityDialog v-if="bindSupplierDensityDialog"
                               :bindSupplierDensityDialog="bindSupplierDensityDialog"
                               :currentRow="currentSupplierDensityRow"
                               @closeBindPartDialog="closeBindSupplierDensityDialog"></BindSupplierDensityDialog>
  </div>
</template>

<script>
import BindPartDialog from "@/views/structural/capabilityAndLaboratory/capabilityComponents/bindPartDialog.vue"
import BindSupplierDensityDialog from "@/views/structural/capabilityAndLaboratory/capabilityComponents/bindSupplierDensityDialog.vue"
import {
  addProduct,
  delItemParameter, delProduct, delTestObject, selectItemParameterList, selectProductListByObjectId,
  selectTestObjectList, upProduct,
} from "@/api/structural/capability";
import limsTable from "@/components/Table/lims-table.vue";
import EditForm from "@/views/structural/capabilityAndLaboratory/capabilityComponents/EditForm.vue";
import testObjectEditForm from "@/views/structural/capabilityAndLaboratory/capabilityComponents/testObjectEditForm.vue";
import {getToken} from "@/utils/auth";
import { obtainItemParameterList } from '@/api/structural/laboratoryScope'

export default {
  name: 'Capability',
  components: {limsTable, EditForm, testObjectEditForm, BindPartDialog, BindSupplierDensityDialog},
  data() {
    return {
      uploadAction: process.env.VUE_APP_BASE_API + '/capacityScope/importExcel',
      uploadAction1: process.env.VUE_APP_BASE_API + '/capacityScope/importEquipData',
      tableData: [],
      tableLoading: false,
      column: [
        {label: '检验项', prop: 'inspectionItem'},
        {label: '检验项EN', prop: 'inspectionItemEn'},
        {label: '检验子项', prop: 'inspectionItemSubclass'},
        {label: '检验子项EN', prop: 'inspectionItemSubclassEn'},
        {label: '检验项分类', prop: 'inspectionItemClass'},
        {label: '检验项分类EN', prop: 'inspectionItemClassEn'},
        {label: '检验对象', prop: 'sample'},
        {label: '单价(元)', prop: 'price'},
        {label: '试验室', prop: 'sonLaboratory'},
        {label: '要求描述', prop: 'askTell'},
        {label: '要求值', prop: 'ask'},
        {label: '计量单位', prop: 'unit'},
        {label: '工时(H)', prop: 'manHour'},
        {label: '预计时间(H)', prop: 'manDay'},
        {label: '工时分组', prop: 'manHourGroup'},
        {label: '创建时间', prop: 'createTime'},
        {label: '修改时间', prop: 'updateTime'},
        {label: '条件', prop: 'radiusList'},
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
        current:1
      },
      testObjectTableData: [],
      testObjectColumn: [
        {
          dataType: 'tag',
          label: '场所',
          prop: 'laboratoryId',
          minWidth: '130',
          formatData: (params) => {
            let index = this.laboratoryList.findIndex(item => item.value == params)
            if(index > -1) {
              return this.laboratoryList[index].label
            }else {
              return null
            }
            // if (params == 1) {
            //   return '装备电缆试验室'
            // } else if (params == 5) {
            //   return '通信产品实验室'
            // } else if (params == 6) {
            //   return '电力产品实验室'
            // } else if (params == 8) {
            //   return '储能产品实验室'
            // } else {
            //   return '射频线缆实验室'
            // }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 5) {
              return 'info'
            } else if (params == 6) {
              return 'warning'
            } else if (params == 8) {
              return 'danger'
            } else {
              return null
            }
          }
        },
        {label: '检验对象', prop: 'specimenName'},
        {label: '检验对象EN', prop: 'specimenNameEn'},
        {label: '产品', prop: 'product'},
        {label: '对象代号', prop: 'code'},
        {label: '对象类型', prop: 'objectType',
          dataType: 'tag',
          formatData: (params) => {
            if (params == 1) {
              return '原材料'
            } else if (params == 2) {
              return '成品'
            } else {
              return '辅材'
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'info'
            } else {
              return 'warning'
            }
          }},
        {label: '创建人', prop: 'createUserName'},
        {label: '更新人', prop: 'updateUserName'},
        {label: '创建时间', prop: 'createTime'},
        {label: '更新时间', prop: 'updateTime'},
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '240px',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.editTestObjectForm(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteTest(row);
              },
            },
            {
              name: '产品维护',
              type: 'text',
              clickFun: (row) => {
                this.upProduct(row);
              },
            },
            {
              name: '零件绑定',
              type: 'text',
              clickFun: (row) => {
                this.bindPartFirst(row);
              },
            },
          ]
        }
      ],
      testObjectPage: {
        total:0,
        size:10,
        current:0
      },
      addOrUpdate: '',
      tree: null,
      loading: true,
      itemParameterForm: {
        inspectionItem: null,
        inspectionItemSubclass: null,
        sample: null,
        specimenName: null,
        partNo: null
      },
      radio: 0,
      productLoad: false,
      diaProduct: false,
      productColumn: [
        {label: '产品名称', prop: 'name'},
        {label: '产品名称EN', prop: 'nameEn'},
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.editProduct('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.deleteProduct(row);
              },
            },
            {
              name: '零件绑定',
              type: 'text',
              clickFun: (row) => {
                this.bindPartSecond(row);
              },
            },
            {
              name: '厂家密度绑定',
              type: 'text',
              clickFun: (row) => {
                this.bindSupplierDensitySecond(row);
              },
            },
          ]
        }
      ],
      productData: [],
      productPage: {
        total:0,
        size:10,
        current:1,
        partNo: null,
        layout: 'total, prev, pager, next'
      },
      productableLoading: false,
      productEditDia: false,
      operationType: '',
      productEditForm: {
        objectId: '',
        id: '',
        name: '',
        nameEn: ''
      },
      objectId: '',
      productRules: {
        name: [{ required: true, message: '请填写产品名称', trigger: 'blur' }],
        nameEn: [{ required: true, message: '请填写产品名称EN', trigger: 'blur' }]
      },
      uploadDia: false,
      fileList: [],
      token: {Authorization: "Bearer " + getToken()},
      uploading: false,
      currentRow: {}, // 选择零件绑定本条数据的信息
      currentSupplierDensityRow: {}, // 选择零件绑定本条数据的信息
      bindSupplierDensityDialog: false,
      bindPartDialog: false,
      type: null,  // 零件绑定的类型--0：检验对象，1：产品维护,
      laboratoryList:[]
    }
  },
  created() {
    this.getItemParameterList()
    this.refreshTable()
  },
  computed: {
    title() {
      return this.addOrUpdate === 1 ? '新增' : '编辑'
    }
  },
  methods: {
    submitUpload() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.$message.error('未选择文件')
        return
      }
      this.uploading = true
      this.$refs.upload.submit();
    },
    onSuccess(response, file, fileList) {
      this.$refs.upload.clearFiles()
      this.uploadDia = false
      this.uploading = false
      if (response.code !== 200) {
        this.$message.error(response.msg)
        return
      }
      this.$message.success('上传成功')
      this.productList = []
      this.refreshTable()
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
      this.uploading = false
    },
    beforeUpload(file, fileList) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError1(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload1.clearFiles()
      this.uploading = false
    },
    beforeUpload1(file, fileList) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload1.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    handleSuccessUp1 (response, file, fileList) {
      this.$refs.upload1.clearFiles()
      if (response.code !== 200) {
        this.$message.error(response.msg)
        return
      }
      this.$message.success('上传成功')
      this.refreshTable()
    },
    refreshList () {
      this.refreshTable()
    },
    refreshList1 () {
      this.refreshTable()
    },
    refreshTable() {
      this.tableLoading = true
      if (this.radio === 0) {
        selectItemParameterList({...this.page, ...this.itemParameterForm}).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.tableData = res.data.records
            this.page.total = res.data.total
          }
        }).catch(err => {
          this.tableLoading = false
        })
      } else {
        selectTestObjectList({...this.testObjectPage, ...this.itemParameterForm}).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.testObjectTableData = res.data.records
            this.testObjectPage.total = res.data.total
          }
        }).catch(err => {
          this.tableLoading = false
        })
      }
    },
    refresh() {
      this.resetForm('itemParameterForm')
      this.page.current = 1
      this.refreshTable()
    },
    pagination (page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    // 检验项目参数新增
    openAdd() {
      if (this.radio === 0) {
        this.$refs.editForm.openDia('add')
      } else {
        this.$refs.testObjectEditForm.openDia('add')
      }
    },
    // 检验项目参数-打开修改弹框
    editForm (row) {
      this.$refs.editForm.openDia('edit', row)
    },
    // 检验项目参数-删除
    delete (row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delItemParameter({id:row.id}).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.refreshTable();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 检验对象-打开修改弹框
    editTestObjectForm (row) {
      this.$refs.testObjectEditForm.openDia('edit', row)
    },
    // 检验项目参数-删除
    deleteTest (row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delTestObject({id:row.id}).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.refreshTable();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 产品维护
    upProduct(row) {
      this.diaProduct = true
      this.objectId = row.id
      this.productPage.current = 1
      this.productPage.partNo = this.itemParameterForm.partNo
      this.getProductList(row)
    },
    // 查询产品维护列表数据
    getProductList () {
      const params = {
        objectId: this.objectId,
      }
      this.productableLoading = true
      selectProductListByObjectId({...params, ...this.productPage}).then(res => {
        this.productableLoading = false
        this.productData = res.data.records
        this.productPage.total = res.data.total
      }).catch(err => {
        this.productableLoading = false
      })
    },
    // 产品维护列表分页
    productPagination(page) {
      this.productPage.current = page.page
      this.getProductList()
    },
    // 产品维护-新增-编辑
    editProduct (type, row) {
      this.productEditDia = true
      this.operationType = type
      if (type === 'edit') {
        this.productEditForm = this.HaveJson(row)
      }
    },
    // 提交产品维护修改
    submitProduct () {
      this.$refs['productEditForm'].validate((valid) => {
        if (valid) {
          this.uploading = true
          this.productEditForm.objectId = this.objectId
          if (this.operationType === 'add') {
            addProduct(this.productEditForm).then(res => {
              this.uploading = false
              if (res.code === 200) {
                this.$message.success('新增成功')
                this.closeProduct()
                this.getProductList();
              }
            }).catch(err => {
              this.uploading = false
            })
          } else {
            upProduct(this.productEditForm).then(res => {
              this.uploading = false
              if (res.code === 200) {
                this.$message.success('新增成功')
                this.closeProduct()
                this.getProductList();
              }
            }).catch(err => {
              this.uploading = false
            })
          }
        }
      })
    },
    closeProduct() {
      this.resetForm('productEditForm')
      this.productEditDia = false
    },
    // 产品维护-删除
    deleteProduct (row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProduct({id:row.id}).then(res => {
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.getProductList();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 零件绑定
    bindPartFirst (row) {
      this.bindPart(row, 0)
    },
    bindPartSecond (row) {
      this.bindPart(row, 1)
    },
    // 厂家密度绑定
    bindSupplierDensitySecond (row) {
      this.bindSupplierDensity(row)
    },
    // 打开厂家密度绑定弹框
    bindSupplierDensity (row) {
      this.currentSupplierDensityRow = row
      this.bindSupplierDensityDialog = true
    },
    // 打开零件绑定弹框
    bindPart (row, index) {
      this.type = index
      this.currentRow = row
      this.bindPartDialog = true
    },
    closeBindPartDialog () {
      this.bindPartDialog = false
    },
    closeBindSupplierDensityDialog () {
      this.bindSupplierDensityDialog = false
    },
    getItemParameterList(){
      obtainItemParameterList().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.laboratoryName,
            value: a.id
          })
        })
        this.laboratoryList = data
      })
    }
  }
}
</script>

<style scoped>
.title {
  height: 40px;
  line-height: 40px;
  margin-bottom: 10px;
}
</style>
