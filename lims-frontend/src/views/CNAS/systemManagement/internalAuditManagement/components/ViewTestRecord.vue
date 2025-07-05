<template>
  <div>
    <el-dialog :visible.sync="filesDialogVisible" title="附件" width="80%" @closed="closeFilesLook">
      <div style="display: flex;justify-content: space-between;">
        <el-upload ref='upload' :action="fileAction" :auto-upload="true" :before-upload="fileBeforeUpload"
          :data="{ correctId: info.correctId }" :headers="uploadHeader" :on-error="onError"
          :on-success="handleSuccessUp" :show-file-list="false"
          accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' style="width: 80px !important;">
          <el-button size="small" style="height: 38px" type="primary">附件上传</el-button>
        </el-upload>
      </div>
      <div>
        <limsTable ref="yearTable" :column="columnData" :height="'calc(100vh - 30em)'" :highlightCurrentRow="true"
          :table-data="tableData" :table-loading="tableLoading" style="margin-top: 0.5em;" :page="page">
        </limsTable>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.fileUrl"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from '@/components/Preview/filePreview.vue'
import {
  getInternalCorrectFileList,
  delInternalCorrectFile,
} from '@/api/cnas/systemManagement/internalAuditManagement.js'
export default {
  name: 'ViewTestRecord',
  // import 引入的组件需要注入到对象中才能使用
  components: { filePreview, limsTable },
  data() {
    // 这里存放数据
    return {
      filesDialogVisible: false,
      tableLoading: false,
      filesLookInfo: {},
      columnData: [
        {
          label: '文件名称',
          prop: 'fileName',
          minWidth: '150px'
        },
        {
          dataType: 'action',
          minWidth: '100',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '预览',
              type: 'text',
              clickFun: (row) => {
                this.handleLook(row)
              }
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.upload(row)
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delete(row)
              }
            }
          ]
        }
      ],
      tableData: [],
      info: {},
      currentInfo: {},
      lookDialogVisible: false,
      page: {
        total: 0,
        size: -1,
        current: -1,
      },
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.filesDialogVisible = true
      this.info = row
      this.searchTableList()
    },
    // 查询附件列表
    searchTableList() {
      this.tableLoading = true
      getInternalCorrectFileList({ correctId: this.info.correctId }).then(res => {
        this.tableLoading = false
        this.tableData = res.data
      }).catch(err => {
        this.tableLoading = false
        console.log('err---', err);
      })
    },
    closeFilesLook() {
      this.filesDialogVisible = false
    },
    // 查看文件
    handleLook(row) {
      this.currentInfo = row
      this.lookDialogVisible = true
    },
    // 下载
    upload(row) {
      this.$download.saveAs(row.fileUrl, row.fileName)
    },
    // 删除
    delete(row) {
      this.tableLoading = true
      delInternalCorrectFile({ correctFileId: row.correctFileId }).then(res => {
        this.tableLoading = false
        this.$message.success('删除成功')
        this.searchTableList()
      }).catch(err => {
        this.tableLoading = false
        console.log('err---', err);
      })
    },
    // 上传验证
    fileBeforeUpload(file) {
      let flag = true
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    onError(err, file, fileList, type) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response,) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.searchTableList()
      } else {
        this.$message.error(response.message);
      }
    },
  },
  computed: {
    fileAction() {
      return this.javaApi + '/internalCorrect/uploadInternalCorrectFile'

    }
  },
};
</script>

<style scoped></style>
