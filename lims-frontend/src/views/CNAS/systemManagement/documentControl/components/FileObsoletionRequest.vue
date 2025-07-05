<template>
  <!-- 文件作废申请 -->
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">文件编号</span>
          <el-input v-model="queryParams.documentCode" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" icon="el-icon-plus"
                   @click="addDialogVisible = true, addInfo = {}">文件作废申请</el-button>
        <el-button :loading="outLoading" size="small" type="primary" @click="handleOut">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :visible.sync="addDialogVisible" title="文件作废申请" top="10vh" width="800px">
      <el-row>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>申请编号：</div>
            <div class="search_input">
              <el-select v-model="addInfo.documentCode" allow-create clearable filterable size="small"
                style="width: 100%;" @change="changeFileList">
                <el-option v-for="item in fileList" :key="item.documentCode" :label="item.documentCode"
                  :value="item.documentCode">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">审批人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.checkUser" filterable size="small" style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件名称：</div>
            <div class="search_input"><el-input v-model="addInfo.name" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件版本：</div>
            <div class="search_input"><el-input v-model="addInfo.version" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件状态：</div>
            <div class="search_input">
              <el-select v-model="addInfo.state" size="small" style="width: 100%;">
                <el-option v-for="(item, index) in fileState" :key="index" :label="item.label"
                  :value="item.value"></el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">期望作废时间：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.expectCancelDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">实际作废时间：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.actuallyCancelDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">作废说明：</div>
            <div class="search_input"><el-input v-model="addInfo.cancelNote" :rows="2" clearable placeholder="请输入"
                size="small" type="textarea"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="handleAdd">确 定</el-button>
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
  addManageDocumentCancel,
  pageManageDocumentList,
  doManageDocumentCancel,
  checkManageDocumentCancel,
  exportManageDocumentCancel,
  delManageDocumentCancel,
  pageManageDocumentCancel,
} from '@/api/cnas/systemManagement/documentControl.js'
import { mapGetters } from "vuex";
export default {
  components: {
    limsTable
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  data() {
    return {
      addPower: false,
      outPower: false,
      addInfo: {},
      addLoading: false,
      addDialogVisible: false,
      personList: [],
      fileList: [],
      outLoading: false,
      fileState: [],
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        { label: "申请人", prop: "createUserName", width: "120px" },
        {
          label: "作废说明",
          prop: "cancelNote",
        },
        { label: "期望作废时间", prop: "expectCancelDate" },
        { label: "实际作废日期", prop: "actuallyCancelDate" },
        {
          label: "作废状态", prop: "state", dataType: "tag",
          formatData: (params) => {
            return params;
          },
          formatType: (params) => {
            if (params == '通过') {
              return 'success'
            } else {
              return 'danger'
            }
          }
        },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleUpdate(row);
              },
              disabled: (row, index) => {
                return row.state == '通过'
              }
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row, index) => {
                return row.state == '通过'
              }
            },
            {
              name: "审核",
              type: "text",
              clickFun: (row) => {
                this.handleCheck(row);
              },
              disabled: (row, index) => {
                return row.checkUser != this.userId || row.state == '通过'
              }
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
  mounted() {
    this.getList()
    this.getAuthorizedPerson()
    this.getFileList()
    this.selectEnumByCategory()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageDocumentCancel({ ...param })
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
    selectEnumByCategory() {
      // 文件状态
      this.getDicts("document_state").then((response) => {
        this.fileState = this.dictToValue(response.data);
      });
    },
    // 获取人员列表
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
    // 获取文件列表
    getFileList() {
      pageManageDocumentList({
        current: -1,
        size: -1
      }).then(res => {
        this.fileList = res.data.records
      }).catch(err => { })
    },
    // 提交
    handleAdd() {
      if (!this.addInfo.documentCode) {
        this.$message.error('请输入申请编号')
        return false
      }
      this.addInfo.method = '作废'
      this.addLoading = true
      if (!this.addInfo.id) {
        // 新增
        addManageDocumentCancel(this.addInfo).then(res => {
          this.addLoading = false
          this.refreshTable()
          this.$message({
            type: 'success',
            message: '提交成功'
          })
          this.addDialogVisible = false
        }).catch(err => { })
      } else {
        // 编辑
        doManageDocumentCancel({
          id: this.addInfo.id,
          method: '作废',
          documentCode: this.addInfo.documentCode,
          checkUser: this.addInfo.checkUser,
          name: this.addInfo.name,
          version: this.addInfo.version,
          documentState: this.addInfo.documentState,
          expectCancelDate: this.addInfo.expectCancelDate,
          actuallyCancelDate: this.addInfo.actuallyCancelDate,
          cancelNote: this.addInfo.cancelNote,
        }).then(res => {
          this.addLoading = false
          this.refreshTable()
          this.$message({
            type: 'success',
            message: '提交成功'
          })
          this.addDialogVisible = false
        }).catch(err => { })
      }

    },
    // 选中文件
    changeFileList(e) {
      if (e) {
        let obj = this.fileList.find(a => a.documentCode == e)
        if (obj) {
          this.addInfo.name = obj.name
          this.addInfo.version = obj.version
          this.addInfo.documentState = obj.state
        }
      }
    },
    handleUpdate(row) {
      this.addInfo = this.HaveJson(row)
      this.addDialogVisible = true
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
            checkManageDocumentCancel({ id: row.id, state: '通过' }).then(res => {
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
            checkManageDocumentCancel({ id: row.id, state: '不通过' }).then(res => {
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
    // 导出
    handleOut() {
      this.outLoading = true
      exportManageDocumentCancel(this.queryParams).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '文件作废表.xlsx')
      })
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageDocumentCancel({ id: row.id }).then((res) => {
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
