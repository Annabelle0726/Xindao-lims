<template>
  <div>
    <el-dialog :visible.sync="filesDialogVisible" title="附件上传" width="80%" @closed="closeFilesLook">
      <div style="display: flex;justify-content: space-between;">
        <el-upload ref='upload'
                   :action="fileAction"
                   :auto-upload="true"
                   :before-upload="fileBeforeUpload" :data="{trainingDetailedId: info.id}"
                   :headers="uploadHeader" :on-error="onError"
                   :on-success="handleSuccessUp"
                   :show-file-list="false"
                   accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' style="width: 80px !important;">
          <el-button size="small" style="height: 38px" type="primary">附件上传</el-button>
        </el-upload>
      </div>
      <div>
        <lims-table :tableData="tableData" :column="columnData"
                    :height="'calc(100vh - 47em)'"
                    :highlightCurrentRow="true"
                    :tableLoading="tableLoading"></lims-table>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="lookDialogVisible"
      fullscreen
      title="查看附件" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}"
                   :fileUrl="javaApi+'/word/'+currentInfo.fileUrl" style="height: 90vh;overflow-y: auto;top: 0"/>
    </el-dialog>
  </div>
</template>

<script>
import file from '@/utils/file';
import filePreview from '@/components/Preview/filePreview.vue';
import limsTable from "@/components/Table/lims-table.vue";
import {delTrainingDetailedFileList, getTrainingDetailedFileList} from "@/api/cnas/personal/personalTraining";
import {delCustomById} from "@/api/system/customer";

export default {
  name: 'ViewRecord',
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable, filePreview },
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
      currentInfo:{},
      lookDialogVisible: false,
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
    searchTableList () {
      this.tableLoading = true
      getTrainingDetailedFileList({trainingDetailedId: this.info.id}).then(res => {
        this.tableLoading = false
        this.tableData = res.data
      }).catch(err => {
        this.tableLoading = false
        console.log('err---', err);
      })
    },
    closeFilesLook () {
      this.filesDialogVisible = false
    },
    // 下载
    upload (row) {
      let url = '';
      if(row.type==1){
        url = this.javaApi+'/img/'+row.fileUrl
        file.downloadIamge(url,row.fileName)
      }else{
        url = this.javaApi+'/word/'+row.fileUrl
        const link = document.createElement('a');
        link.href = url;
        link.download = row.fileName;
        link.click();
      }
    },
    // 删除
    delete (row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.tableLoading = true
        delTrainingDetailedFileList({detailedFileId: row.detailedFileId}).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchTableList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {})
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
    onError(err, file, fileList,type) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response, ) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.searchTableList()
      }
    },
    // 查看文件
    handleLook(row){
      this.currentInfo = row
      this.lookDialogVisible = true
    },
  },
  computed: {
    fileAction() {
      return this.javaApi + '/personTraining/uploadTrainingDetailedFile'

    }
  },
};
</script>

<style scoped>

</style>
