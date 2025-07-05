<template>
  <div>
    <el-dialog :visible.sync="filesDialogVisible" title="查看校准证书" width="80%" @closed="closeFilesLook">
      <div>
        <limsTable
          ref="yearTable"
          :column="columnData"
          :height="'calc(100vh - 47em)'"
          :highlightCurrentRow="true"
          :table-data="tableData"
          :table-loading="tableLoading"
          style="margin-top: 0.5em;">
        </limsTable>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="lookDialogVisible"
      fullscreen
      title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}"
                   :fileUrl="javaApi+'/img/'+currentInfo.fileUrl" style="max-height: 90vh;overflow-y: auto;"/>
    </el-dialog>
  </div>
</template>

<script>
import filePreview from '@/views/tool/file-preview.vue';
import limsTable from '@/components/Table/lims-table.vue'
import file from '@/utils/file';

export default {
  name: 'calibrationsFileDia',
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable, filePreview },
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
          label: '设备名称',
          prop: 'deviceName',
          minWidth: '150px'
        },
        {
          label: '设备编号',
          prop: 'managementNumber',
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
          ]
        }
      ],
      tableData: [],
      info: {},
      currentInfo:{},
      lookDialogVisible: false,
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    openDia(row) {
      this.filesDialogVisible = true
      this.info = row
      this.tableData = this.info.calibrationsFileList
    },
    closeFilesLook () {
      this.filesDialogVisible = false
    },
    // 查看文件
    handleLook(row){
      this.currentInfo = row
      this.lookDialogVisible = true
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
  }
};
</script>

<style scoped>
</style>
