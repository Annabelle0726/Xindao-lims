<template>
  <!-- 外来文件确认记录 -->
  <div class="capacity-scope">
    <el-tabs type="border-card" v-model="activeName" style="height: 100%;" @change="getTableData">
      <el-tab-pane label="填写" name="填写" style="height: 100%;">
        <div style="display: flex;align-items: center;justify-content: flex-end;margin-bottom: 10px">
          <el-button size="small" type="primary" @click="openAdd('新增')" style="margin-left: 20px;">新增</el-button>
          <el-upload :action="action" :multiple="false" :show-file-list="false" accept='.doc,.docx'
            :headers="uploadHeader" :on-change="beforeUpload" :on-error="onError" ref='upload'
            :on-success="handleSuccessUp" style="display:inline-block;margin-left: 20px;">
            <el-button size="small" type="primary" :loading="upLoading">导入</el-button></el-upload>
        </div>
        <lims-table :tableData="tableData0" :column="column0" :page="page0" :tableLoading="tableLoading0"
                    :height="'calc(100vh - 290px)'" @pagination="pagination0"></lims-table>
      </el-tab-pane>
      <el-tab-pane label="历史记录" name="历史记录" style="height: 100%;">
        <div>
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
            <el-form-item label="年" prop="year">
              <el-date-picker v-model="queryParams.year" type="year" placeholder="选择年" format="yyyy" value-format="yyyy"
                              size="small" @change="refreshTable()">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
              <el-button size="mini" @click="refresh">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
                    :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
      </el-tab-pane>
    </el-tabs>
    <!-- 详情/下载/审核 -->
    <el-dialog :title="title0" :visible.sync="lookDialogVisible" width="800px" :class="{ downPdf: title0 == '下载' }"
      :modal="title0 != '下载'" top="5vh">
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.url" :currentFile="{}"
        style="max-height: 70vh;overflow-y: auto;" />
      <span slot="footer" class="dialog-footer" v-if="title0 == '审核' || title0 == '批准'">
        <el-button @click="submitCheck('不通过')" :loading="noCheckLoading">不通过</el-button>
        <el-button type="primary" @click="submitCheck('通过')" :loading="checkLoading">通 过</el-button>
      </span>
    </el-dialog>
    <!-- 新增/编辑 -->
    <el-dialog :title="title" :visible.sync="addDia" width="500px">
      <el-form :model="addForm" ref="addForm" :rules="addRules" label-position="right" label-width="120px">
        <el-form-item label="外来文件名称" prop="documentName">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.documentName"></el-input>
        </el-form-item>
        <el-form-item label="文件编号" prop="documentCode">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.documentCode">
          </el-input>
        </el-form-item>
        <el-form-item label="标准规范名称" prop="standardName">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.standardName">
          </el-input>
        </el-form-item>
        <el-form-item label="标准号" prop="standardCode">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.standardCode">
          </el-input>
        </el-form-item>
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker v-model="addForm.effectiveDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
            style="width: 100%" type="date" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="作废日期" prop="cancelDate">
          <el-date-picker v-model="addForm.cancelDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
            style="width: 100%" type="date" value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input size="small" placeholder="请输入" type="textarea" :rows="2" clearable v-model="addForm.note">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitProduct('addForm')">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from '@/components/Preview/filePreview.vue'
import {
  addManageRecordVerify,
  pageManageRecordVerify,
  submitManageRecordTotal,
  ratifyManageRecordTotal,
  delManageRecordVerify,
  pageManageRecordTotal,
  doManageRecordVerify
} from '@/api/cnas/systemManagement/documentRecords.js'
export default {
  name: 'OutDocumenRecordt',
  components: {
    filePreview,
    limsTable,
  },
  data() {
    return {
      title0: '查看',
      activeName: '填写',
      lookDialogVisible: false,
      noCheckLoading: false,
      checkLoading: false,
      addPower: true,
      upPower: true,
      currentInfo: {},
      upLoading: false,
      queryParams: {},
      tableData: [],
      column: [
        { label: "年份", prop: "year" },
        { label: "总数量", prop: "totalNum", width: "120px" },
        {
          label: "拟制人",
          prop: "submitUserName",
        },
        { label: "拟制日期", prop: "submitDate" },
        { label: "批准人", prop: "ratifyUserName" },
        { label: "批准日期", prop: "ratifyDate" },
        { label: "批准结果", prop: "ratifyState" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "查看",
              type: "text",
              clickFun: (row) => {
                this.handleLook(row);
              },
              disabled: (row) => {
                return !row.url
              },
            },
            {
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleDown0(row);
              },
              disabled: (row) => {
                return !row.url
              },
            },
            {
              name: "提交",
              type: "text",
              clickFun: (row) => {
                this.handleSubmit(row);
              },
              disabled: (row) => {
                return !!row.submitUserName
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleApproval(row);
              },
              disabled: (row) => {
                return !row.submitUserName || !!row.ratifyUserName
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
      tableData0: [],
      column0: [
        { label: "外来文件名称", prop: "documentName" },
        { label: "文件编号", prop: "documentCode", width: "120px" },
        {
          label: "标准规范名称",
          prop: "standardName",
        },
        { label: "标准号", prop: "standardCode" },
        { label: "生效日期", prop: "effectiveDate" },
        { label: "作废日期", prop: "cancelDate" },
        { label: "备注", prop: "note" },
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
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
            },
          ],
        },
      ],
      page0: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading0: false,
      title: '新增',
      addForm: {},
      addDia: false,
      addRules: {
        documentName: [{ required: true, message: "请输入外来文件名称", trigger: "blur" }],
        documentCode: [
          { required: true, message: "请输入文件编号", trigger: "blur" },
        ],
      },
      uploading: false,
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/manageRecordVerify/exportManageRecordVerify'
    }
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    getTableData() {
      if (this.activeName === '填写') {
        this.getList0()
      } else {
        this.getList()
      }
    },
    openAdd(title, row) {
      this.title = title;
      if (row) {
        this.addForm = row;
      } else {
        this.addForm = {};
      }
      this.addDia = true;
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordTotal({ ...param })
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
    getList0() {
      this.tableLoading0 = true;
      let param = { ...this.page0 };
      delete param.total;
      pageManageRecordVerify({ ...param })
        .then((res) => {
          this.tableLoading0 = false;
          if (res.code === 200) {
            this.tableData0 = res.data.records;
            this.page0.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading0 = false;
        });
    },
    pagination0({ page, limit }) {
      this.page0.current = page;
      this.page0.size = limit;
      this.getList0();
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
        this.getList0();
      } else {
        this.$message.error('上传失败');
      }
    },
    submitCheck(type) { },
    // 查看
    handleLook(row) {
      this.title0 = '查看'
      this.commonFun(row)
    },
    // 公用方法
    commonFun(row, callbanck) {
      this.currentInfo = row
      pageManageRecordVerify({
        current: -1,
        size: -1,
        manageRecordTotalId: row.id
      }).then(res => {
        this.currentInfo.arr = res.data.records
        this.lookDialogVisible = true
        if (callbanck) {
          callbanck()
        }
      }).catch(err => { });
    },
    handleDown0(row) {
      this.$download.saveAs(row.url, '外来文件确认记录')
    },
    // 提交
    handleSubmit(row) {
      this.$confirm('是否提交 ' + row.year + ' 年的数据', '提交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitManageRecordTotal({
          id: row.id
        }).then(res => {
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          this.refreshTable()
        }).catch(err => { });
      })
    },
    // 批准
    handleApproval(row) {
      this.title0 = '批准'
      this.commonFun(row)
    },
    // 提交审核/批准
    submitCheck(state) {
      if (state == '通过') {
        this.checkLoading = true
      } else {
        this.noCheckLoading = true
      }
      ratifyManageRecordTotal({
        id: this.currentInfo.id,
        ratifyState: state
      }).then(res => {
        this.checkLoading = false
        this.noCheckLoading = false
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
        this.refreshTable()
        this.lookDialogVisible = false
      }).catch(err => { });
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordVerify({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.getList0()
          });
        })
        .catch(() => { });
    },
    submitProduct(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.uploading = true;
          if (this.title == "新增") {
            addManageRecordVerify(this.addForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.getList0()
                this.addDia = false;
              })
              .catch((err) => {
                this.uploading = false;
              });
          } else {
            doManageRecordVerify(this.addForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.getList0()
                this.addDia = false;
              })
              .catch((err) => {
                this.uploading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
  }
}
</script>

<style scoped>
.title {
  height: 60px;
  line-height: 60px;
}

.search {
  background-color: #fff;
  height: 40px;
  display: flex;
  align-items: center;
  position: relative;
}

.search_thing {
  width: 350px;
  display: flex;
  align-items: center;
}

.search_label {
  width: 30px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 110px);
}

.table {
  background-color: #fff;
  height: 100%;
  margin-top: 10px;
}

>>>.el-tabs__content {
  height: 100%;
}
</style>
