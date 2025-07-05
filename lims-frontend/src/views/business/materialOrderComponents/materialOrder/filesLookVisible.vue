<template>
  <div>
    <el-dialog title="附件查看" :visible.sync="isShow" width="80%" @closed="$emit('closeFilesLook')">
      <div style="display: flex;justify-content: space-between;">
        <ul class="tab">
          <li v-for="(m, i) in dataVisibleTitle" :key="i" :class="{ active: i === dataVisibleIndex }"
            @click="handleDataVisibleTab(m, i)">{{ m.label }}</li>
        </ul>
        <el-upload :action="action" :auto-upload="true"
          :data="{ orderId: dataVisibleIndex === 0 ? filesLookInfo.enterOrderId : filesLookInfo.quarterOrderId }"
          :on-success="handleSuccessUp" :show-file-list="false"
          accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' :headers="uploadHeader"
          :before-upload="beforeUpload" style="width: 80px !important;" :on-error="onError" ref='upload'>
          <el-button size="small" type="primary" style="height: 38px">附件上传</el-button>
        </el-upload>
      </div>
      <div v-if="filesDialogVisible">
        <lims-table :tableData="tableDataFile" :column="columnFile" @pagination="paginationFile" height="500px"
          key="tableDataFile" :page="pageFile" :tableLoading="tableLoadingFile"></lims-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import { delFile, downFile, getFileList } from "@/api/business/rawMaterialOrder";
export default {
  name: "filesLookVisible",
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
  props: {
    filesDialogVisible: {
      type: Boolean,
      default: () => false
    },
    filesLookInfo: {
      type: Object,
      default: () => { }
    },
  },
  data() {
    // 这里存放数据
    return {
      tableDataFile: [],
      tableLoadingFile: false,
      columnFile: [
        {
          dataType: 'tag',
          label: '类型',
          prop: 'type',
          formatData: (params) => {
            if (params == 1) {
              return '图片'
            } else if (params == 2) {
              return '文件'
            } else {
              return ''
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'warning'
            } else {
              return ''
            }
          }
        },
        { label: '附件名称', prop: 'fileName' },
        { label: '上传人', prop: 'name' },
        { label: '上传时间', prop: 'createTime' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '170px',
          operation: [
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row);
              }
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.delete(row);
              }
            },
          ]
        }
      ],
      pageFile: {
        total: 0,
        size: 10,
        current: 1
      },
      isShow: this.filesDialogVisible,
      dataVisibleTitle: [
        {
          label: '进厂检验',
          value: 0
        },
        {
          label: '季度检验',
          value: 1
        },
      ],
      dataVisibleIndex: 0, // tab栏选择值
      entity: {
        insOrderId: ''
      },
    }
  },
  mounted() {
    this.getFileList()
  },
  // 方法集合
  methods: {
    // 切换数据查看tab栏
    handleDataVisibleTab(m, i) {
      this.dataVisibleIndex = i
      this.getFileList()
    },
    // 查询回调
    getFileList() {
      if (this.dataVisibleIndex === 0) {
        this.entity.insOrderId = this.filesLookInfo.enterOrderId
      } else {
        this.entity.insOrderId = this.filesLookInfo.quarterOrderId
      }
      this.tableLoadingFile = true
      const params = { ...this.entity }
      getFileList(params).then(res => {
        this.tableLoadingFile = false
        if (res.code === 200) {
          this.tableDataFile = res.data.records
          this.pageFile.total = res.data.total
        }
      }).catch(err => {
        this.tableLoadingFile = false
      })
    },
    paginationFile(page) {
      this.pageFile.size = page.limit
      this.getFileList()
    },
    // 下载
    handleDown(row) {
      downFile({ id: row.id, }).then(res => {
        this.$download.saveAs(res.data.fileUrl, row.fileName);
      }).catch(error => {

      })
    },
    handleSuccessUp(response,) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.getFileList()
      } else {
        this.$message.error(response.msg);
      }
    },
    beforeUpload(file) {
      let flag = true
      if (this.dataVisibleIndex === 0 && !this.filesLookInfo.enterOrderId) {
        this.$message.warning('没有相应的检验任务')
        return
      } else if (this.dataVisibleIndex === 1 && !this.filesLookInfo.quarterOrderId) {
        this.$message.warning('没有相应的检验任务')
        return
      }
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    // 删除
    delete(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delFile({ id: row.id }).then(res => {
          this.$message.success('删除成功')
          this.getFileList()
        })
      }).catch(() => { })
    }
  },
  computed: {
    action() {
      return this.javaApi + '/insOrderPlan/uploadFile'
    }
  },
}
</script>

<style scoped>
.tab {
  list-style-type: none;
  display: flex;
  margin-top: 0 !important;
  padding-left: 0 !important;
}

.tab li {
  line-height: 24px;
  padding: 6px 14px;
  font-size: 14px;
  color: #333333;
  border: 1px solid #EEEEEE;
  cursor: pointer;
}

.tab li:nth-child(1) {
  border-radius: 8px 0 0 8px;
}

.tab li:nth-child(2) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}
</style>
