<template>
  <!-- 文件定期审查记录 -->
  <div class="capacity-scope">
    <el-tabs type="border-card" v-model="activeName" style="height: 100%;">
      <el-tab-pane label="填写" name="填写" style="height: 100%;">
        <div class="search">
          <div>
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
              <el-form-item label="文件名称" prop="documentName">
                <el-input size="small" placeholder="请输入" clearable
                          v-model="queryParams.documentName" @keyup.enter.native="refreshTable()"></el-input>
              </el-form-item>
              <el-form-item label="文件编号" prop="documentCode">
                <el-input size="small" placeholder="请输入" clearable v-model="queryParams.documentCode"
                          @keyup.enter.native="refreshTable()"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
                <el-button size="mini" @click="refresh">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div>
            <el-button size="small" type="primary" @click="openAdd" v-if="addPower">新增</el-button>
            <el-upload :action="action" :multiple="false" :show-file-list="false" accept='.doc,.docx'
                       :headers="uploadHeader" :on-change="beforeUpload" :on-error="onError" ref='upload' v-if="upPower"
                       :on-success="handleSuccessUp" style="display:inline-block;margin-left: 20px;">
              <el-button type="primary" size="small">导入</el-button></el-upload>
          </div>
        </div>
        <div class="table">
          <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
            :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="历史记录" name="历史记录" style="height: 100%;">
        <div>
          <el-form :model="queryParams0" ref="queryParams0" size="small" :inline="true">
            <el-form-item label="年" prop="year">
              <el-date-picker v-model="queryParams0.year" type="year" placeholder="选择年" format="yyyy"
                              value-format="yyyy" size="small" @change="refreshTable(1)">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="refreshTable(1)">查询</el-button>
              <el-button size="mini" @click="refresh(1)">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="table">
          <lims-table :tableData="tableData0" :column="column0" :page="page0" :tableLoading="tableLoading"
            :height="'calc(100vh - 290px)'" @pagination="pagination0"></lims-table>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :title="title" :visible.sync="addDialogVisible" width="400px" top="6vh">
      <el-row>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color: red;margin-left: 4px;">* </span>文件编号：</div>
            <div class="search_input">
              <el-select v-model="addInfo.documentCode" size="small" style="width: 100%;" @change="getCurrentFile"
                allow-create clearable filterable>
                <el-option v-for="item in fileList" :key="item.documentCode" :label="item.title"
                  :value="item.documentCode">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件名称：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.documentName"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">版本号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.documentVersion"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修订号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.revision"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">适宜性：</div>
            <div class="search_input">
              <el-select v-model="addInfo.suitability" placeholder="请选择" size="small" style="width: 100%;">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">备注：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.remark"
                type="textarea" :rows="2"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 详情/审核 -->
    <el-dialog :title="title0" :visible.sync="lookDialogVisible" width="800px" top="5vh">
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.url" :currentFile="{}"
        style="max-height: 70vh;overflow-y: auto;" />
      <span slot="footer" class="dialog-footer" v-if="title0 == '审核' || title0 == '批准'">
        <el-button @click="submitCheck('不通过')" :loading="noCheckLoading">不通过</el-button>
        <el-button type="primary" @click="submitCheck('通过')" :loading="checkLoading">通 过</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from '@/components/Preview/filePreview.vue'
import {
  exportOutManageRecordIntervals,
  addManageRecordIntervals,
  doManageRecordIntervals,
  ratifyManageRecordIntervalsTotal,
  submitManageRecordIntervalsTotal,
  pageManageRecordIntervals,
  pageManageRecordIntervalsTotal,
  delManageRecordIntervals,
} from '@/api/cnas/systemManagement/documentRecords.js'
import {
  pageManageDocumentList,
} from '@/api/cnas/systemManagement/documentControl.js'
export default {
  name: 'RegularReviewsRecord',
  components: {
    limsTable,
    filePreview
  },
  data() {
    return {
      activeName: '填写',
      title0: '新增',
      addPower: true,
      outPower: true,
      upPower: true,
      outLoading: false,
      addLoading: false,
      lookDialogVisible: false,
      noCheckLoading: false,
      checkLoading: false,
      addInfo: {},
      title: '新增',
      addInfo: {},
      addDialogVisible: false,
      fileList: [],
      typeList: [],
      currentInfo: {},
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件名称", prop: "documentName" },
        { label: "文件编号", prop: "documentCode", width: "120px" },
        {
          label: "版本号",
          prop: "documentVersion",
        },
        { label: "修订号", prop: "revision" },
        { label: "适宜性", prop: "suitability" },
        { label: "备注", prop: "remark" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleEdit(row);
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
      queryParams0: {},
      tableData0: [],
      column0: [
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
                return !!row.submitUserName && row.ratifyState != '不通过'
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleApproval(row);
              },
              disabled: (row) => {
                return (!row.submitUserName || !!row.ratifyUserName) && row.ratifyState != '不通过'
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
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/manageRecordIntervals/exportInManageRecordIntervals'
    }
  },
  mounted() {
    this.getList()
    this.getList0()
    this.getFileList()
    this.selectEnumByCategory()
  },
  methods: {
    // 新增
    openAdd() {
      this.addInfo = {}
      this.title = '新增'
      this.addDialogVisible = true;
    },
    // 导出
    handleDown() {
      this.outLoading = true
      exportOutManageRecordIntervals(this.queryParams).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.message, "文件定期审查记录");
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordIntervals({ ...param })
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
      this.tableLoading = true;
      let param = { ...this.queryParams0, ...this.page0 };
      delete param.total;
      pageManageRecordIntervalsTotal({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData0 = res.data.records;
            this.page0.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination0({ page, limit }) {
      this.page0.current = page;
      this.page0.size = limit;
      this.getList0();
    },
    refreshTable(num) {
      if (num) {
        this.page0.current = 1;
        this.getList0();
      } else {
        this.page.current = 1;
        this.getList();
      }
    },
    refresh(num) {
      if (num) {
        this.queryParams0 = {};
        this.page0.current = 1;
        this.getList0();
      } else {
        this.queryParams = {};
        this.page.current = 1;
        this.getList();
      }
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
      } else {
        this.$message.error('上传失败');
      }
    },
    getCurrentFile(e) {
      let obj = this.fileList.find(m => m.documentCode == e)
      if (obj) {
        this.$set(this.addInfo, 'documentName', obj.name)
        this.$set(this.addInfo, 'documentVersion', obj.version)
        // this.addInfo.documentName = obj.name
        // this.addInfo.documentVersion = obj.version
      }
    },
    // 获取文件列表--文件清单
    getFileList() {
      pageManageDocumentList({
        current: -1,
        size: -1
      }).then(res => {
        this.fileList = res.data.records.map(m => {
          m.title = m.documentCode
          return m
        })
      }).catch(err => { })
    },
    selectEnumByCategory() {
      // 文件类别
      this.getDicts("suitability").then((response) => {
        this.typeList = this.dictToValue(response.data);
      });
    },
    // 提交
    handleAdd() {
      if (!this.addInfo.documentCode) return this.$message.error('请输入文件编号')
      this.addLoading = true
      let obj = {}
      if (this.title == '新增') {
        obj = this.HaveJson(this.addInfo)
      } else {
        let { id, documentCode, documentName, documentVersion, revision, suitability, remark } = this.addInfo
        obj = { id, documentCode, documentName, documentVersion, revision, suitability, remark }
      }
      if (this.title == '新增') {
        addManageRecordIntervals(obj).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      } else {
        doManageRecordIntervals(obj).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      }
    },
    // 修改
    handleEdit(row) {
      this.addInfo = row
      this.title = '编辑'
      this.addDialogVisible = true;
    },
    submitCheck(state) {
      if (state == '通过') {
        this.checkLoading = true
      } else {
        this.noCheckLoading = true
      }
      ratifyManageRecordIntervalsTotal({
        id: this.currentInfo.id,
        ratifyState: state
      }).then(res => {
        this.checkLoading = false
        this.noCheckLoading = false
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
        this.refreshTable(1);
        this.lookDialogVisible = false
      }).catch(err => { });
    },
    // 查看
    handleLook(row) {
      this.title0 = '查看'
      this.commonFun(row)
    },
    commonFun(row, callbanck) {
      this.currentInfo = row
      this.lookDialogVisible = true
      if (callbanck) {
        callbanck()
      }
    },
    handleDown0(row) {
      this.$download.saveAs(row.url, "所有文件定期检查记录");
    },
    // 提交
    handleSubmit(row) {
      this.$confirm('是否提交 ' + row.year + ' 年的数据', '提交', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitManageRecordIntervalsTotal({
          id: row.id
        }).then(res => {
          this.$message({
            type: 'success',
            message: '提交成功!'
          });
          this.refreshTable(1);
        }).catch(err => { });
      })
    },
    // 批准
    handleApproval(row) {
      this.title0 = '批准'
      this.commonFun(row)
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordIntervals({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refreshTable();
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
  width: 110px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 110px);
}

>>>.el-tabs__content {
  height: 100%;
}
</style>
