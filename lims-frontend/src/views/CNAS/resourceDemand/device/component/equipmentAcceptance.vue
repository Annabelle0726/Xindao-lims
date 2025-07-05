<template>
  <div>
    <div>
      <div style="margin: 10px 0;text-align: right">
        <el-button size="small" type="primary" @click="handleForm('add')">新增</el-button>
      </div>
      <div>
        <el-table ref="yearTable" v-loading="yearTableDetailDataLoading" :data="yearTableDetailData"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          height="calc(100vh - 18em)" style="width: 100% ;">
          <!-- 表格列 -->
          <el-table-column align="center" header-align="center" label="序号" prop="prop" type="index"
            width="70"></el-table-column>
          <el-table-column label="到货日期" min-width="150" prop="arrivalDate"></el-table-column>
          <el-table-column label="金额" min-width="100" prop="goldAmount"></el-table-column>
          <el-table-column label="维修单位" min-width="150" prop="maintenanceunit"></el-table-column>
          <!-- 操作按钮 -->
          <el-table-column align="center" label="操作" min-width="180">
            <template slot-scope="scope">
              <el-button size="small" type="text" @click="handleForm('edit', scope.row.acceptanceId)">编辑</el-button>
              <el-button size="small" type="text" @click="handleForm('view', scope.row.acceptanceId)">查看</el-button>
              <el-button size="small" type="text" @click="record(scope.row)">附件</el-button>
              <el-button size="small" type="text" @click="handleDownOne(scope.row.acceptanceId)">导出</el-button>
              <el-button size="small" style="color: #f56c6c" type="text"
                @click="deleteFun(scope.row.acceptanceId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination :current-page="1" :page-size="pagination1.size" :page-sizes="[10, 20, 30, 50, 100]"
          :total="pagination1.total" layout="->,total, sizes, prev, pager, next, jumper" background
          @size-change="handleSizeChange1" @current-change="handleCurrentChange1">
        </el-pagination>
      </div>
    </div>
    <acceptance-form v-if="applicationForm" ref="applicationForm" @closeDialog="closeDialog"></acceptance-form>
    <!--上传报告-->
    <el-dialog :visible.sync="filesDialogVisible" title="附件" width="80%" @closed="closeFilesLook">
      <div style="display: flex;justify-content: space-between;">
        <el-upload ref='upload' :action="fileAction" :auto-upload="true" :before-upload="fileBeforeUpload"
          :data="{ acceptanceId: acceptanceId }" :headers="uploadHeader" :on-error="onError"
          :on-success="handleSuccessUp" :show-file-list="false"
          accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar' style="width: 80px !important;">
          <el-button size="small" style="height: 38px" type="primary">附件上传</el-button>
        </el-upload>
      </div>
      <div>
        <limsTable ref="yearTable" :column="columnData" :height="'calc(100vh - 30em)'" :highlightCurrentRow="true"
          :table-data="tableData" :table-loading="tableLoading" style="margin-top: 0.5em;" :page="pagination1">
        </limsTable>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="fileUrl"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import AcceptanceForm from "./acceptanceForm.vue";
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from "@/components/Preview/filePreview.vue";
import {
  pageDeviceAcceptance,
  getDeviceAcceptanceFileList,
  delDeviceAcceptanceFileList,
  exportDeviceAcceptance,
  delDeviceAcceptance,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  name: "equipment-acceptance",
  // import 引入的组件需要注入到对象中才能使用
  components: { filePreview, limsTable, AcceptanceForm },
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
      }
    }
  },
  data() {
    // 这里存放数据
    return {
      pagination1: {
        size: 10,
        current: 1,
        total: 0,
      },
      yearTableDetailDataLoading: false,
      yearTableDetailData: [],
      applicationForm: false,
      filesDialogVisible: false,
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
      tableLoading: false,
      acceptanceId: '', // 上传附件当前行的id
      currentInfo: {},
      lookDialogVisible: false,
      fileUrl: ''
    }
  },
  mounted() {
    this.getYearTableDetailData(this.clickNodeVal.value)
  },
  // 方法集合
  methods: {
    // 查询
    getYearTableDetailData(deviceId) {
      this.yearTableDetailDataLoading = true
      pageDeviceAcceptance({
        current: this.pagination1.current,
        size: this.pagination1.size,
        deviceId: deviceId,
      }).then(res => {
        if (res.code == 200) {
          this.yearTableDetailData = res.data.records
          this.pagination1.total = res.data.total
        }
        this.yearTableDetailDataLoading = false
      }).catch(err => {
        this.yearTableDetailDataLoading = false
      })
    },
    handleForm(type, id) {
      this.applicationForm = true
      this.$nextTick(() => {
        this.$refs.applicationForm.openDialog(type, id, this.clickNodeVal.value)
      })
    },
    closeDialog() {
      this.applicationForm = false
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 打开报告弹框
    record(row) {
      this.filesDialogVisible = true
      this.acceptanceId = row.acceptanceId
      this.searchTableList()
    },
    // 查询附件列表
    searchTableList() {
      this.tableLoading = true
      getDeviceAcceptanceFileList({ acceptanceId: this.acceptanceId }).then(res => {
        this.tableLoading = false
        this.tableData = res.data
      }).catch(err => {
        this.tableLoading = false
        console.log('err---', err);
      })
    },
    // 查看文件
    handleLook(row) {
      this.currentInfo = row
      this.lookDialogVisible = true
      const state = /\.(jpg|jpeg|png|gif)$/i.test(this.currentInfo.fileUrl)
      if (state) {
        this.fileUrl = this.javaApi + '/img/' + this.currentInfo.fileUrl
      } else {
        this.fileUrl = this.javaApi + '/word/' + this.currentInfo.fileUrl
      }
    },
    // 下载
    upload(row) {
      this.$download.saveAs(row.fileUrl, row.fileName)
    },
    // 删除
    delete(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delDeviceAcceptanceFileList({ acceptanceFileId: row.acceptanceFileId }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchTableList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 上传附件
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
      }
    },
    closeFilesLook() {
      this.filesDialogVisible = false
    },
    // 分页
    handleSizeChange1(val) {
      this.pagination1.size = val
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 分页
    handleCurrentChange1(val) {
      this.pagination1.current = val
      this.getYearTableDetailData(this.clickNodeVal.value)
    },
    // 导出
    handleDownOne(id) {
      this.outLoading = true
      exportDeviceAcceptance({ acceptanceId: id }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备验收.doc')
      })
    },
    // 删除
    deleteFun(id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delDeviceAcceptance({ acceptanceId: id }).then(res => {
          this.$message.success('删除成功！');
          this.getYearTableDetailData(this.clickNodeVal.value);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getYearTableDetailData(newVal.value);
      }
    },
  },
  computed: {
    fileAction() {
      return this.javaApi + '/deviceAcceptance/uploadDeviceAcceptanceFile'

    }
  },
}
</script>

<style scoped>
.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-search {
  display: flex;
  align-items: center;
  margin: 10px 0;
}
</style>
