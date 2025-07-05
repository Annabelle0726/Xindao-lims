<template>
  <div>
    <el-dialog :visible.sync="filesDialogVisible" title="附件上传" width="80%" @closed="closeFilesLook">
      <div style="display: flex;justify-content: space-between;">
        <el-upload ref='upload'
                   :action="fileAction"
                   :auto-upload="true"
                   :before-upload="fileBeforeUpload" :data="{methodVerifyId: info.methodVerifyId}"
                   :headers="uploadHeader"
                   :on-error="onError"
                   :on-success="handleSuccessUp"
                   :show-file-list="false"
                   accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' style="width: 80px !important;">
          <el-button size="small" style="height: 38px" type="primary">附件上传</el-button>
        </el-upload>
      </div>
      <div>
        <lims-table
          ref="yearTable"
          :column="columnData"
          :height="'calc(100vh - 47em)'"
          :highlightCurrentRow="true"
          :table-data="tableData"
          :table-loading="tableLoading"
          style="margin-top: 0.5em;">
        </lims-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import file from "@/utils/file";
import { delVerifyMethodFileList, getVerifyMethodFileList } from '@/api/cnas/process/method/standardMethodsChange'

export default {
  name: 'ViewTestRecord',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
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
      info: {}
    };
  },
  // 方法集合
  methods: {
    openDia(row) {
      this.filesDialogVisible = true
      this.info = row
      if (this.info === undefined) {
        this.info = {
          methodVerifyId: ''
        }
      }
      this.searchTableList()
    },
    // 查询附件列表
    searchTableList () {
      this.tableLoading = true
      getVerifyMethodFileList({methodVerifyId:this.info.methodVerifyId}).then(res => {
        this.tableLoading = false
        if (res.code === 200){
          this.tableData = res.data
        }
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
      this.tableLoading = true
      delVerifyMethodFileList({methodFileId:row.methodFileId}).then(res => {
        this.tableLoading = false
        if (res.code === 200){
          this.$message.success('删除成功')
          this.searchTableList()
        }

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
  },
  computed: {
    fileAction() {
      return this.javaApi + '/processMethodVerify/uploadVerifyMethodFile'

    }
  },
};
</script>

<style scoped>
</style>
