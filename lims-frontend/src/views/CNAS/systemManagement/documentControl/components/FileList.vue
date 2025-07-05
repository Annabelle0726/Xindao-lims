<template>
  <!-- 文件清单 -->
  <div>
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="文件名称" prop="name">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.name"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="文件状态" prop="state">
            <el-select v-model="queryParams.state" size="small" @change="refreshTable()" clearable>
              <el-option :label="item.label" :value="item.value" v-for="(item, index) in fileState"
                         :key="index"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-upload :action="action" :multiple="false" accept='.xls,.xlsx' :headers="uploadHeader"
                   :on-change="beforeUpload" :on-error="onError" ref='upload' :on-success="handleSuccessUp"
                   :show-file-list="false">
          <el-button size="small" type="primary" :loading="upLoading">导入</el-button></el-upload>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog title="上传" :visible.sync="addDialogVisible" width="1000px" top="3vh">
      <UpPdfStamp ref="UpPdfStamp" v-if="addDialogVisible" @uploadPDF="uploadPDF" :isUpFile="true"
        @uploadPDFErr="uploadPDFErr"></UpPdfStamp>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" v-loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="查看附件" :visible.sync="lookDialogVisible" width="800px" top="5vh" fullscreen>
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.url" :currentFile="{}"
        style="max-height: 90vh;overflow-y: auto;" />
    </el-dialog>
    <!-- 新增/编辑 -->
    <el-dialog :title="title" :visible.sync="addDia" width="500px">
      <el-form :model="currentInfo" ref="currentInfoForm" :rules="rules" label-position="right" label-width="120px">
        <el-form-item label="文件编号" prop="documentCode">
          <el-input size="small" placeholder="请输入" clearable v-model="currentInfo.documentCode"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-select v-model="currentInfo.type" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in fileType" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input size="small" placeholder="请输入" clearable v-model="currentInfo.name">
          </el-input>
        </el-form-item>
        <el-form-item label="文件版本" prop="version">
          <el-input size="small" placeholder="请输入" clearable v-model="currentInfo.version">
          </el-input>
        </el-form-item>
        <el-form-item label="作者" prop="writer">
          <el-input size="small" placeholder="请输入" clearable v-model="currentInfo.writer">
          </el-input>
        </el-form-item>
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker v-model="currentInfo.effectiveDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd" type="date"
            size="small" placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="文件状态" prop="state">
          <el-select v-model="currentInfo.state" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in fileState" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitProduct('currentInfoForm')">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import UpPdfStamp from '@/components/UpPdfStamp/index.vue'
import filePreview from '@/components/Preview/filePreview.vue'
import limsTable from "@/components/Table/lims-table.vue";
import {
  uploadFileManageDocumentList,
  pageManageDocumentList,
  delManageDocumentList,
  doManageDocumentList,
} from '@/api/cnas/systemManagement/documentControl.js'
export default {
  components: {
    UpPdfStamp,
    filePreview,
    limsTable,
  },
  data() {
    return {
      addDialogVisible: false,
      lookDialogVisible: false,
      addPower: false,
      upLoading: false,
      addLoading: false,
      currentInfo: {},
      fileType: [],
      fileState: [],
      title: '新增',
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        {
          label: "类别", prop: "type", width: "120px", dataType: "tag",
          formatData: (params) => {
            if (this.fileType.find((m) => m.value == params)) {
              return this.fileType.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (this.fileType.find((m) => m.value == params)) {
              return this.fileType.find((m) => m.value == params).type;
            } else {
              return null
            }
          },
        },
        {
          label: "名称",
          prop: "name",
        },
        { label: "文件版本", prop: "version" },
        { label: "作者", prop: "writer" },
        { label: "生效日期", prop: "effectiveDate" },
        {
          label: "文件状态", prop: "state", dataType: "tag",
          formatData: (params) => {
            if (this.fileState.find((m) => m.value == params)) {
              return this.fileState.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (this.fileState.find((m) => m.value == params)) {
              return this.fileState.find((m) => m.value == params).type;
            } else {
              return null
            }
          },
        },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.openAdd("编辑", row);
              },
            },
            {
              name: "上传",
              type: "text",
              clickFun: (row) => {
                this.handleUp(row);
              },
            },
            {
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleDown(row);
              },
            },
            {
              name: "查看附件",
              type: "text",
              clickFun: (row) => {
                this.handleLook(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
      addDia: false,
      rules: {
        documentCode: [{ required: true, message: "请输入文件编号", trigger: "blur" }],
      },
      uploading: false,
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/manageDocumentList/exportManageDocumentList'
    }
  },
  mounted() {
    this.getList()
    this.selectEnumByCategory()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageDocumentList({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData = res.data.records;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    refreshTable() {
      this.page.current = 1;
      this.getList();
    },
    refresh() {
      this.queryParams = {};
      this.page.current = 1;
      this.getList();
    },
    openAdd(title, row) {
      this.title = title;
      if (row) {
        this.currentInfo = row;
      } else {
        this.currentInfo = {};
      }
      this.addDia = true;
    },
    // 下载文件
    handleDown(row) {
      if (!row.url) return this.$message.warning('文件未上传')
      this.$download.saveAs(row.url, row.url);
    },
    // 查看文件
    handleLook(row) {
      if (!row.url) return this.$message.warning('文件未上传')
      this.currentInfo = row
      this.lookDialogVisible = true
    },
    // 上传文件
    handleUp(row) {
      this.currentInfo = row
      this.addDialogVisible = true;
    },
    // 提交上传
    handleAdd() {
      this.addLoading = true
      this.$refs['UpPdfStamp'].generatePDF()
    },
    uploadPDFErr() {
      this.addLoading = false
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        // this.upLoading = true;
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.refreshTable()
      }else {
        this.$message.error(response.msg);
      }
    },
    selectEnumByCategory() {
      // 文件类别
      this.getDicts("document_type").then((response) => {
        this.fileType = this.dictToValue(response.data);
      });
      // 文件状态
      this.getDicts("document_state").then((response) => {
        this.fileState = this.dictToValue(response.data);
      });
    },
    async uploadPDF(pdfBlob, fileName) {
      const formData = new FormData();
      formData.append('file', pdfBlob, fileName); // 文件字段
      formData.append('id', this.currentInfo.id); // 文件名字段

      try {
        let res = await uploadFileManageDocumentList(formData)
        this.addLoading = false
        if (res.code == 200) {
          this.$message({ message: '上传成功', type: 'success' });
          this.addDialogVisible = false;
          this.refreshTable()
          return true
        } else {
          this.$message({ message: '上传失败', type: 'error' });
          return false
        }
      } catch (e) {
        this.addLoading = false
      }

    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageDocumentList({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refresh();
          });
        })
        .catch(() => { });
    },
    submitProduct(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.uploading = true;
          doManageDocumentList(this.currentInfo)
            .then((res) => {
              this.uploading = false;
              if (res.code != 200) {
                return;
              }
              this.$message.success("提交成功");
              this.refresh();
              this.addDia = false;
            })
            .catch((err) => {
              this.uploading = false;
            });
        } else {
          return false;
        }
      });
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
</style>
