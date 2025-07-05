<template>
  <!-- 作废文件销毁记录 -->
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
            <div class="search_label">数量：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.qty"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">销毁原因：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.reason"></el-input></div>
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
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  exportOutManageRecordCancel,
  addManageRecordCancel,
  doManageRecordCancel,
  ratifyManageRecordCancel,
  delManageRecordCancel,
  pageManageRecordCancel
} from '@/api/cnas/systemManagement/documentRecords.js'
import {
  pageManageDocumentList,
} from '@/api/cnas/systemManagement/documentControl.js'
export default {
  name: 'CancellationRecord',
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
      addInfo: {},
      title: '新增',
      addInfo: {},
      addDialogVisible: false,
      fileList: [],
      personList: [],
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        { label: "文件名称", prop: "documentName", width: "120px" },
        {
          label: "数量",
          prop: "qty",
        },
        { label: "销毁原因", prop: "reason" },
        { label: "申请人", prop: "createUserName" },
        { label: "申请日期", prop: "createTime" },
        { label: "批准人", prop: "ratifyUserName" },
        { label: "批准日期", prop: "ratifyTime" },
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
                this.handleEdit0(row);
              },
              disabled: (row) => {
                return row.ratifyState == '通过'
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleRatify(row);
              },
              disabled: (row) => {
                return row.ratifyState == '通过'
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row) => {
                return row.ratifyState == '通过'
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
      return this.javaApi + '/manageRecordCancel/exportInManageRecordCancel'
    }
  },
  mounted() {
    // this.entityCopy = this.HaveJson(this.componentData.entity);
    this.getList()
    this.getAuthorizedPerson()
    this.getFileList()
  },
  methods: {
    openAdd() {
      this.addInfo = {}
      this.title = '新增'
      this.addDialogVisible = true;
    },
    handleEdit0(row) {
      this.addInfo = row
      this.title = '修改'
      this.addDialogVisible = true;
    },
    // 导出
    handleDown() {
      this.outLoading = true
      exportOutManageRecordCancel(this.queryParams).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.data, "作废文件销毁记录");
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordCancel({ ...param })
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
    handleAdd() {
      if (!this.addInfo.documentCode) return this.$message.error('请输入文件编号')
      this.addLoading = true
      let obj = {}
      if (this.title == '新增') {
        obj = this.HaveJson(this.addInfo)
      } else {
        let { id, documentCode, documentName, qty, reason, remark } = this.addInfo
        obj = { id, documentCode, documentName, qty, reason, remark }
      }
      if (this.title == '新增') {
        addManageRecordCancel(obj).then(res => {
          this.addLoading = false
          this.addDialogVisible = false
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.refreshTable()
        }).catch(err => { })
      } else {
        doManageRecordCancel(obj).then(res => {
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
    getCurrentFile(e) {
      let obj = this.fileList.find(m => m.documentCode == e)
      if (obj) {
        this.$set(this.addInfo, 'documentName', obj.name)
      }
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
    handleRatify(row) {
      this.$confirm('是否批准通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        closeOnClickModal: false, // 禁止点击遮罩层关闭
        distinguishCancelAndClose: true,
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 点击“确定”按钮，允许关闭
            ratifyManageRecordCancel({ id: row.id, ratifyState: '通过' }).then(res => {
              this.refreshTable()
              done();
              this.$message({
                type: 'success',
                message: '提交成功'
              })
            })
              .catch(err => {

              })
          } else if (action === 'cancel') {
            // 点击“取消”按钮，不允许关闭
            ratifyManageRecordCancel({ id: row.id, ratifyState: '不通过' }).then(res => {
              this.refreshTable()
              done();
              this.$message({
                type: 'success',
                message: '提交成功'
              })
            })
              .catch(err => {

              })
            console.log("取消按钮点击事件，不关闭弹框");
          } else if (action === 'close') {
            // 点击“×”按钮，不允许关闭
            done();
            console.log("×按钮点击事件，不关闭弹框");
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordCancel({ id: row.id }).then((res) => {
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
