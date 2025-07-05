<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">报告名称</span>
          <el-input v-model="queryParams.reportName" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button :loading="outLoading" size="small" type="primary" style="margin-right: 10px" @click="handleDown0">导出</el-button>
        <el-upload ref='upload' :action="action" :headers="uploadHeader" :on-change="beforeUpload" :on-error="onError"
                   :on-success="handleSuccessUp" :show-file-list="false" style="display: inline-block;"
                   accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'>
          <el-button :loading="upLoading" size="small" type="primary">导入</el-button></el-upload>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 270px)'"
        :page="page" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :visible.sync="addDialogVisible" title="评价" width="400px">
      <el-row>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">评价：</div>
            <div class="search_input"><el-input v-model="addInfo.note" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">日期：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.evaluateTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="handleAdd">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.reportUrl"
        style="max-height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from "@/components/Preview/filePreview.vue";
import {
  exportProcessEvaluate,
  doProcessEvaluate,
  delProcessEvaluate,
  pageProcessEvaluate,
} from '@/api/cnas/process/uncertainty.js'
import { mapGetters } from "vuex";
export default {
  name: 'Uncertainty',
  components: {
    limsTable,
    filePreview
  },
  data() {
    return {
      addPower: false,
      outPower: false,
      outLoading: false,
      upLoading: false,
      addDialogVisible: false,
      addInfo: {},
      addLoading: false,
      currentInfo: {},
      lookDialogVisible: false,
      queryParams: {},
      tableData: [],
      column: [
        { label: "报告名称", prop: "reportName" },
        { label: "评价人", prop: "evaluateUserName" },
        { label: "评价日期", prop: "evaluateTime" },
        { label: "备注", prop: "note" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "评价",
              type: "text",
              clickFun: (row) => {
                this.handleTell(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
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
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
    }
  },
  // 用于上传文件的信息
  computed: {
    ...mapGetters(["userId"]),
    action() {
      return this.javaApi + '/processEvaluate/addProcessEvaluate'
    }
  },
  mounted() {
    // this.entityCopy = this.HaveJson(this.componentData.entity);
    this.getList()
  },
  methods: {
    handleDown0() {
      this.outLoading = true
      exportProcessEvaluate(this.queryParams).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '测量不确定度的评定.xlsx');
      })
    },
    handleDown(row) {
      if (!row.reportUrl) {
        this.$message.warning('暂无文件')
        return
      }
      this.$download.saveAs(row.reportUrl, row.reportName);
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
      }
    },
    handleTell(row) {
      this.addInfo = row;
      this.addDialogVisible = true;
    },
    handleAdd() {
      this.addLoading = true;
      doProcessEvaluate({
        id: this.addInfo.id,
        note: this.addInfo.note,
        evaluateTime: this.addInfo.evaluateTime,
        evaluateUser: this.userId
      }).then((res) => {
        this.addLoading = false;
        this.$message.success('评价成功');
        this.addDialogVisible = false;
        this.refreshTable()
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageProcessEvaluate({ ...param })
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
    refresh() {
      this.queryParams = {};
      this.page.current = 1;
      this.getList();
    },
    refreshTable() {
      this.page.current = 1;
      this.getList();
    },
    handleLook(row) {
      this.currentInfo = row;
      this.lookDialogVisible = true;
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delProcessEvaluate({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refresh();
          });
        })
        .catch(() => { });
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

.search_thing {
  width: 350px;
  display: flex;
  align-items: center;
}

.search_label {
  width: 80px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 80px);
}
</style>
