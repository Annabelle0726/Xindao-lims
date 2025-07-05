<template>
  <!-- 所有文件（内、外部文件）的发放与回收记录 -->
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="文件名称" prop="documentName">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.documentName"
                      @keyup.enter.native="refreshTable()"></el-input>
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
        <el-button size="small" type="primary" @click="openAdd">新增</el-button>
        <el-upload :action="action" :multiple="false" :show-file-list="false" accept='.doc,.docx'
                   :headers="uploadHeader" :on-change="beforeUpload" :on-error="onError" ref='upload'
                   :on-success="handleSuccessUp" style="display:inline-block;margin: 0 8px;">
          <el-button type="primary" size="small">导入</el-button></el-upload>
        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 260px)'" @pagination="pagination"></lims-table>
    </div>
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
            <div class="search_label">版号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.documentVersion"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">份数：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.pages"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件类别：</div>
            <div class="search_input">
              <el-select v-model="addInfo.documentType" placeholder="请选择" size="small" style="width: 100%;">
                <el-option v-for="item in fileType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">分发号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.number"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">接受人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.receiveUser" placeholder="请选择" size="small" style="width: 100%;"
                filterable="">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">接收部门：</div>
            <div class="search_input">
              <el-select v-model="addInfo.departLims" placeholder="请选择" size="small" style="width: 100%;">
                <el-option v-for="item in list" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">接受日期：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.receiveDate" type="date" size="small" placeholder="选择日期"
                format="yyyy-MM-dd" value-format="yyyy-MM-dd" style="width: 100%;">
              </el-date-picker>
            </div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  getYearAndMonthAndDays
} from '@/utils/date'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  exportOutManageRecordIssueRecycle,
  doManageRecordIssueRecycle,
  addManageRecordIssueRecycle,
  selectCNSAPersonTree,
  pageManageRecordIssueRecycle,
  delManageRecordIssueRecycle,
} from '@/api/cnas/systemManagement/documentRecords.js'
import {
  pageManageDocumentList,
} from '@/api/cnas/systemManagement/documentControl.js'
import { mapGetters } from "vuex";
export default {
  name: 'DistributionCollectionRecord',
  components: {
    limsTable
  },
  data() {
    return {
      addPower: true,
      outPower: true,
      upPower: true,
      outLoading: false,
      addLoading: false,
      title: '新增',
      addDialogVisible: false,
      addInfo: {},
      personList: [],
      fileType: [],
      fileList: [],
      list: [],
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        { label: "文件名称", prop: "documentName", width: "120px" },
        {
          label: "版号",
          prop: "documentVersion",
        },
        { label: "份数", prop: "pages" },
        { label: "文件类别", prop: "documentType" },
        { label: "分发号", prop: "number" },
        { label: "接受人", prop: "receiveUserName" },
        { label: "接收部门", prop: "departLims" },
        { label: "接受日期", prop: "receiveDate" },
        { label: "签收人", prop: "signedUserName" },
        { label: "签收日期", prop: "signedDate" },
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
              disabled: (row) => {
                return !!row.signedUser
              },
            },
            {
              name: "回收",
              type: "text",
              clickFun: (row) => {
                this.handleBack(row);
              },
              disabled: (row) => {
                return !!row.signedUser
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row) => {
                return !!row.signedUser
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
    action() {
      return this.javaApi + '/manageRecordIssueRecycle/exportInManageRecordIssueRecycle'
    },
    ...mapGetters(["userId"]),
  },
  mounted() {
    // this.entityCopy = this.HaveJson(this.componentData.entity);
    this.getList()
    this.getAuthorizedPerson()
    this.selectEnumByCategory()
    this.getFileList()
    // TODO 部门接口需要重写
    // this.selectTreeList()
  },
  methods: {
    // 新增
    openAdd() {
      // this.$refs.ValueTable.openAddDia(this.$api.manageRecordIssueRecycle.addManageRecordIssueRecycle);
      this.addInfo = {}
      this.title = '新增'
      this.addDialogVisible = true;
    },
    handleEdit(row) {
      this.addInfo = row
      this.title = '编辑'
      this.addDialogVisible = true;
    },
    // 导出
    handleDown() {
      this.outLoading = true
      exportOutManageRecordIssueRecycle(this.queryParams).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.data, '文件发放与回收记录')
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordIssueRecycle({ ...param })
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
    getAuthorizedPerson() {
      selectUserCondition().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.personList = data
      })
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
    selectEnumByCategory() {
      // 文件类别
      this.getDicts("document_type").then((response) => {
        this.fileType = this.dictToValue(response.data);
      });
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
    getCurrentFile(e) {
      let obj = this.fileList.find(m => m.documentCode == e)
      if (obj) {
        this.$set(this.addInfo, 'documentName', obj.name)
        this.$set(this.addInfo, 'documentVersion', obj.version)
        this.$set(this.addInfo, 'documentType', obj.type)
      }
    },
    handleAdd() {
      if (!this.addInfo.documentCode) return this.$message.error('请输入文件编号')
      this.addLoading = true
      let obj = {}
      if (this.title == '新增') {
        obj = this.HaveJson(this.addInfo)
      } else {
        let { id, documentCode, documentName, documentVersion, pages, documentType, number, receiveUser, receiveDate, departLims } = this.addInfo
        obj = { id, documentCode, documentName, documentVersion, pages, documentType, number, receiveUser, receiveDate, departLims }
      }
      if (this.title == '新增') {
        addManageRecordIssueRecycle(obj).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      } else {
        doManageRecordIssueRecycle(obj).then(res => {
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
    // 回收
    handleBack(row) {
      this.$confirm('是否确认回收?', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        let obj = {}
        let { id, documentCode, documentName, documentVersion, pages, documentType, number, receiveUser, receiveDate, departLims } = row
        obj = { id, documentCode, documentName, documentVersion, pages, documentType, number, receiveUser, receiveDate, departLims, signedDate: getYearAndMonthAndDays(), signedUser: this.userId }
        doManageRecordIssueRecycle(obj).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      }).catch(() => { })
    },
    // 查询树形列表
    selectTreeList() {
      selectCNSAPersonTree().then((res) => {
        this.list = res.data[0].children;
      });
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordIssueRecycle({ id: row.id }).then((res) => {
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
  width: 110px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 110px);
}
</style>
