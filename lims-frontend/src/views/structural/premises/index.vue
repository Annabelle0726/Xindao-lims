<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="实验室名称" prop="laboratoryName">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.laboratoryName"
              @keyup.enter.native="refreshTable"></el-input>
          </el-form-item>
          <el-form-item label="实验室编码" prop="laboratoryNumber">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.laboratoryNumber"
              @keyup.enter.native="refreshTable"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openAdd('add')" icon="el-icon-plus">新增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :height="'calc(100vh - 250px)'" @pagination="pagination"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <!--    新增实验室-->
    <el-dialog :title="formTitle" :visible.sync="addDia" width="450px">
      <el-form ref="laboratoryForm" :model="laboratoryForm" :rules="userRules" label-position="right"
        label-width="100px">
        <el-form-item label="实验室名称" prop="laboratoryName">
          <el-input v-model="laboratoryForm.laboratoryName" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="场所编码" prop="laboratoryNumber">
          <el-input v-model="laboratoryForm.laboratoryNumber" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="实验室代号" prop="laboratoryCode">
          <el-input v-model="laboratoryForm.laboratoryCode" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="负责人" prop="head">
          <el-input v-model="laboratoryForm.head" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="负责人电话" prop="phoneNumber">
          <el-input v-model="laboratoryForm.phoneNumber" size="small" clearable></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="laboratoryForm.address" size="small" clearable></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reset">取 消</el-button>
        <el-button type="primary" @click="customAdd" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="印章管理" :visible.sync="fileVisible" width="60vw">
      <div class="btns">
        <el-button size="medium" type="primary" @click="openUpload">更新印章</el-button>
      </div>
      <lims-table :tableData="fileComponentData" :column="fileComponentDataColumn" @pagination="fileComponentPagination"
        height="500px" :page="fileComponentPage" :tableLoading="fileComponentTableLoading"></lims-table>
    </el-dialog>
    <el-dialog title="更新印章" :visible.sync="upFileVisible" width="400px">
      <el-form ref="dataForm" :model="dataForm" :rules="dataFormRules" label-position="right" label-width="80px">
        <el-form-item label="印章类型" prop="type">
          <el-cascader v-model="dataForm.type" :options="options" :show-all-levels="false" :props="props"
            placeholder="请选择" size="small" style="width:100%" collapse-tags clearable></el-cascader>
        </el-form-item>
        <el-form-item label="印章图片" prop="address">
          <el-upload class="avatar-uploader" :action="action" :headers="uploadHeader"
            accept='image/jpg,image/jpeg,image/png' :show-file-list="false" :on-success="handleSuccess"
            :on-change="beforeUpload" ref="upload" :on-error="onError">
            <img v-if="dataForm.address" :src="javaApi + '/img/' + dataForm.address" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="upFileVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmConnect" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  addParameter,
  addSeal,
  delParameter,
  selectItemParameter,
  selectSeal,
  upParameter
} from "@/api/structural/laboratoryScope";
import { getCertificationDetail } from "@/api/structural/laboratory";
export default {
  components: {
    limsTable

  },
  computed: {
    action() {
      return this.javaApi + '/deviceScope/uploadFile'
    }
  },
  data() {
    return {
      queryParams: {
        laboratoryName: '',
        laboratoryNumber: '',
      },
      tableData: [],
      tableLoading: false,
      column: [
        { label: '实验室名称', prop: 'laboratoryName' },
        { label: '场所编码', prop: 'laboratoryNumber' },
        { label: '实验室代号', prop: 'laboratoryCode' },
        { label: '负责人', prop: 'head' },
        { label: '负责人电话', prop: 'phoneNumber' },
        { label: '地址', prop: 'address' },
        { label: '创建人', prop: 'createUserName' },
        { label: '创建时间', prop: 'createTime' },
        {
          dataType: 'action',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openAdd('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.delete(row);
              },
            },
            {
              name: '印章管理',
              type: 'text',
              clickFun: (row) => {
                this.fileManagement(row);
              },
            },
          ]
        }
      ],
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      addDia: false,
      formTitle: '',
      laboratoryForm: {

      },
      operationType: '',
      userRules: {
        laboratoryName: [{ required: true, message: '请输入实验室名称', trigger: 'blur' }],
        laboratoryNumber: [{ required: true, message: '请输入场所编码', trigger: 'blur' }],
        head: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
        phoneNumber: [{ required: true, message: '请输入负责人电话', trigger: 'blur' }],
      },
      currentRow: {},
      fileComponentTableLoading: false,
      fileComponentData: [],
      fileComponentDataColumn: [
        { label: '实验室名称', prop: 'laboratoryName' },
        { label: '印章图片', prop: 'address', dataType: 'image' },
        { label: '印章类型', prop: 'type' },
      ],
      fileComponentPage: {
        total: 0,
        size: 10,
        current: 1,
        layout: 'total, prev, pager, next'
      },
      fileVisible: false,
      upFileVisible: false,
      loading: false,
      dataForm: {
        type: '',
        address: '',
      },
      dataFormRules: {
        type: [{ required: true, message: '请选择印章类型', trigger: 'change' }],
        address: [{ required: false, message: '请上传图片', trigger: 'change' }],
      },
      props: { multiple: false, emitPath: false, },
      options: [
        {
          value: '实验室资质',
          label: '实验室资质',
          children: []
        },
        {
          value: '委托报告',
          label: '委托报告',
          children: null
        },
        {
          value: '进厂报告',
          label: '进厂报告',
          children: null
        },
      ],
    }
  },
  mounted() {
    this.refreshTable()
  },
  methods: {
    refreshTable() {
      this.tableLoading = true
      selectItemParameter({ ...this.page, ...this.queryParams }).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.tableLoading = false
      })
    },
    // 重置
    refresh() {
      this.resetForm('queryForm')
      this.refreshTable()
    },
    // 分页切换
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    openAdd(type, row) {
      this.formTitle = type === 'add' ? '新增实验室' : '编辑实验室'
      this.operationType = type
      if (type === 'edit') {
        this.laboratoryForm = this.HaveJson(row)
      }
      this.addDia = true
    },
    // 提交新增、编辑实验室表单
    customAdd() {
      this.$refs['laboratoryForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.operationType === 'add') {
            addParameter(this.laboratoryForm).then(res => {
              this.loading = false
              if (res.code !== 200) return
              this.$message.success('新增成功')
              this.refreshTable()
              this.reset()
            }).catch(err => {
              this.loading = false
            })
          } else {
            upParameter(this.laboratoryForm).then(res => {
              this.loading = false
              if (res.code !== 200) return
              this.$message.success('修改成功')
              this.refreshTable()
              this.reset()
            }).catch(err => {
              this.loading = false
            })
          }
        }
      })
    },
    reset() {
      this.resetForm('laboratoryForm')
      this.addDia = false
    },
    // 删除实验室
    delete(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delParameter({ id: row.id }).then(res => {
          this.$message.success('删除成功')
          this.refreshTable()
        })
      }).catch(() => { })
    },
    // 打开印章管理弹框
    fileManagement(row) {
      this.fileVisible = true;
      this.fileComponentTableLoading = true
      this.currentRow = row
      this.getFileComponentList()
    },
    getFileComponentList() {
      selectSeal({ id: this.currentRow.id, ...this.fileComponentPage }).then(res => {
        this.fileComponentTableLoading = false
        if (res.code === 200) {
          this.fileComponentData = res.data.records
          this.fileComponentPage.total = res.data.total
        }
      }).catch(err => {
        this.fileComponentTableLoading = false
      })
    },
    fileComponentPagination(page) {
      this.fileComponentPage.size = page.limit
      this.getFileComponentList()
    },
    // 打开更新印章弹框
    openUpload() {
      this.dataForm.type = '';
      this.dataForm.address = '';
      this.upFileVisible = true;
      this.getCertificationOperation()
    },
    // 查询印章类型
    getCertificationOperation() {
      const params = {
        current: -1,
        size: -1,
      }
      getCertificationDetail(params).then(res => {
        this.options[0].children = res.data.records.map(m => {
          m.value = m.name;
          m.label = m.name;
          return m
        });
      })
    },
    // 提交更新印章
    confirmConnect() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loading = true;
          addSeal({ labId: this.currentRow.id, ...this.dataForm }).then(res => {
            this.loading = false;
            this.getFileComponentList()
            this.upFileVisible = false;
          })
        }
      })
    },
    handleSuccess(response,) {
      if (response.code === 200) {
        this.dataForm.address = response.data.url
      }
    },
    beforeUpload(file, type) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError(err, file, fileList, type) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
  }
}
</script>
<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}

.btns {
  text-align: right;
  margin-bottom: 10px;
}

::v-deep .el-dialog__body {
  padding-top: 8px !important;
}

.avatar-uploader ::v-deep .el-upload {
  border: 1px dashed #666666;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader ::v-deep .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 20px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
}
</style>
