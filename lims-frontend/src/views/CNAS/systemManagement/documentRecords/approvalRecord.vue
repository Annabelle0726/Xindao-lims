<template>
  <!-- 文件审批记录 -->
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
        <el-button size="small" type="primary" @click="openAdd('新增')">新增</el-button>
        <el-upload :action="action" :multiple="false" :show-file-list="false" accept='.doc,.docx'
                   :headers="uploadHeader" :on-change="beforeUpload" :on-error="onError" ref='upload'
                   :on-success="handleSuccessUp" style="display: inline-block;margin: 0 8px">
          <el-button type="primary" size="small">导入</el-button></el-upload>
        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 230px)'" @pagination="pagination"></lims-table>
    </div>
    <!-- 新增/编辑 -->
    <el-dialog :title="title" :visible.sync="addDia" width="500px">
      <el-form :model="addForm" ref="addForm" :rules="addRules" label-position="right" label-width="100px">
        <el-form-item label="文件名称" prop="documentName">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.documentName"></el-input>
        </el-form-item>
        <el-form-item label="文件编号" prop="documentCode">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.documentCode">
          </el-input>
        </el-form-item>
        <el-form-item label="版/次" prop="documentVersion">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.documentVersion">
          </el-input>
        </el-form-item>
        <el-form-item label="编制" prop="writeUser">
          <el-select v-model="addForm.writeUser" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核" prop="checkUser">
          <el-select v-model="addForm.checkUser" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批准" prop="ratifyUser">
          <el-select v-model="addForm.ratifyUser" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批准日期" prop="ratifyDate">
          <el-date-picker v-model="addForm.ratifyDate" style="width: 100%" value-format="yyyy-MM-dd" ormat="yyyy-MM-dd"
            type="date" size="small"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input size="small" placeholder="请输入" type="textarea" :rows="2" clearable v-model="addForm.remarks">
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
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  addManageRecordCheck,
  doManageRecordCheck,
  exportOutManageRecordCheck,
  checkManageRecordCheck,
  ratifyManageRecordCheck,
  delManageRecordCheck,
  pageManageRecordCheck
} from '@/api/cnas/systemManagement/documentRecords.js'
export default {
  name: 'ApprovalRecord',
  components: {
    limsTable
  },
  data() {
    return {
      addPower: true,
      outPower: true,
      upPower: true,
      outLoading: false,
      personList: [],
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件名称", prop: "documentName" },
        { label: "文件编号", prop: "documentCode", width: "120px" },
        {
          label: "版/次",
          prop: "documentVersion",
        },
        { label: "编制", prop: "writeUser" },
        { label: "审核人", prop: "checkUserName" },
        { label: "批准人", prop: "ratifyUserName" },
        // { label: "批准日期", prop: "ratifyDate" },
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
                this.openAdd("编辑", row);
              },
              disabled: (row) => {
                return row.checkState == '通过' || row.ratifyState == '通过'
              },
            },
            {
              name: "审核",
              type: "text",
              clickFun: (row) => {
                this.handleCheck(row);
              },
              disabled: (row) => {
                return row.checkState == '通过' || row.ratifyState == '通过'
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleApproval(row);
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
                return row.checkState == '通过' || row.ratifyState == '通过'
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
      title: "新增",
      addDia: false,
      addForm: {},
      addRules: {
        documentName: [{ required: true, message: "请输入文件名称", trigger: "blur" }],
      },
      uploading: false,
    }
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/manageRecordCheck/exportInManageRecordCheck'
    }
  },
  mounted() {
    this.getAuthorizedPerson()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordCheck({ ...param })
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
    // 新增/编辑
    openAdd(title, row) {
      this.title = title
      if (row) {
        this.addForm = row
      } else {
        this.addForm = {}
      }
      this.addDia = true;
    },
    // 导出
    handleDown() {
      this.outLoading = true
      // queryParams
      exportOutManageRecordCheck(this.queryParams).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.data, '文件审批记录')
      })
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
        this.refreshTable()
      })
    },
    // 审核
    handleCheck(row) {
      this.$confirm('是否审核通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        closeOnClickModal: false, // 禁止点击遮罩层关闭
        distinguishCancelAndClose: true,
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 点击“确定”按钮，允许关闭
            checkManageRecordCheck({ id: row.id, checkState: '通过' }).then(res => {
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
            checkManageRecordCheck({ id: row.id, checkState: '不通过' }).then(res => {
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
    // 批准
    handleApproval(row) {
      this.$confirm('是否批准通过?', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning',
        closeOnClickModal: false, // 禁止点击遮罩层关闭
        distinguishCancelAndClose: true,
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            // 点击“确定”按钮，允许关闭
            ratifyManageRecordCheck({ id: row.id, ratifyState: '通过' }).then(res => {
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
            ratifyManageRecordCheck({ id: row.id, ratifyState: '不通过' }).then(res => {
              this.refreshTable()
              done();
              this.$message({
                type: 'success',
                message: '提交成功'
              })
            })
              .catch(err => {

              })
          } else if (action === 'close') {
            // 点击“×”按钮，不允许关闭
            done();
            console.log("×按钮点击事件，不关闭弹框");
          }
        }
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
    // 新增/修改
    submitProduct(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.uploading = true;
          if (this.title == "新增") {
            addManageRecordCheck(this.addForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.refresh();
                this.addDia = false;
              })
              .catch((err) => {
                this.uploading = false;
              });
          } else {
            doManageRecordCheck(this.addForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.refresh();
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
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordCheck({ id: row.id }).then((res) => {
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
</style>
