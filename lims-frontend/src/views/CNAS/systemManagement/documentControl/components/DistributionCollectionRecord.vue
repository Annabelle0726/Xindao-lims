<template>
  <!-- 发放回收记录 -->
  <div>
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
          <el-form-item label="文件编号" prop="documentCode">
            <el-input v-model="queryParams.documentCode" clearable placeholder="请输入" size="small"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="文件名称" prop="laboratoryNumber">
            <el-input v-model="queryParams.name" clearable placeholder="请输入" size="small"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button v-if="addPower" size="small" type="primary" icon="el-icon-plus"
                   @click="addDialogVisible = true, addInfo = {}, radio = '发放', file = null">添加发放记录</el-button>
        <el-button v-if="outPower" :loading="outLoading" size="small" type="primary" @click="handleOut">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :title="'添加' + radio + '记录'" :visible.sync="addDialogVisible" top="10vh" width="800px">
      <el-row v-if="addDialogVisible">
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>申请编号：</div>
            <div class="search_input">
              <el-select v-model="addInfo.documentCode" :disabled="radio == '回收'" allow-create clearable filterable
                size="small" style="width: 100%;" @change="changeFileList">
                <el-option v-for="item in fileList" :key="item.documentCode" :label="item.documentCode"
                  :value="item.documentCode">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col v-if="radio == '发放'" :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>发放人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.issueUser" filterable size="small" style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col v-else :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>回收人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.recycleUser" filterable size="small" style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>审批人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.receiveUser" :disabled="radio != '发放'" filterable size="small"
                style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件名称：</div>
            <div class="search_input"><el-input v-model="addInfo.name" :disabled="radio == '回收'" clearable
                placeholder="请输入" size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件版本：</div>
            <div class="search_input"><el-input v-model="addInfo.version" :disabled="radio == '回收'" clearable
                placeholder="请输入" size="small"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件状态：</div>
            <div class="search_input">
              <el-select v-model="addInfo.state" :disabled="radio == '回收'" size="small" style="width: 100%;">
                <el-option v-for="(item, index) in fileState" :key="index" :label="item.label"
                  :value="item.value"></el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col v-if="radio == '发放'" :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发放编号：</div>
            <div class="search_input"><el-input v-model="addInfo.issueCode" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col v-else :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">回收编号：</div>
            <div class="search_input"><el-input v-model="addInfo.recycleCode" clearable placeholder="请输入"
                size="small"></el-input></div>
          </div>
        </el-col>
        <el-col v-if="radio == '发放'" :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发放时间：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.issueDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col v-else :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">回收时间：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.recycleDate" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col v-if="radio == '发放'" :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">发放说明：</div>
            <div class="search_input"><el-input v-model="addInfo.issueNote" :rows="2" clearable placeholder="请输入"
                size="small" type="textarea"></el-input></div>
          </div>
        </el-col>
        <el-col v-else :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">回收说明：</div>
            <div class="search_input"><el-input v-model="addInfo.recycleNote" :rows="2" clearable placeholder="请输入"
                size="small" type="textarea"></el-input></div>
          </div>
        </el-col>
        <el-col v-if="radio == '发放'" :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">上传附件：</div>
            <div class="search_input"><el-upload :auto-upload="false" :multiple="false" :on-change="handleChangeUpload"
                accept='.pdf,.doc,.docx,.xls,.xlsx' action="#" style="margin: 8px 0 0px 50px;">
                <el-button size="small" type="primary">上传附件</el-button>
              </el-upload></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="handleAdd">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.url"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import filePreview from '@/components/Preview/filePreview.vue'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  pageManageDocumentList,
  pageManageDocumentCancel,
  exportManageDocumentIssueRecycle,
  addManageDocumentIssueRecycle,
  doManageDocumentIssueRecycle,
  checkManageDocumentIssueRecycle,
  pageManageDocumentIssueRecycle,
  delManageDocumentIssueRecycle,
} from '@/api/cnas/systemManagement/documentControl.js'
import { mapGetters } from "vuex";
export default {
  components: {
    filePreview,
    limsTable
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  data() {
    return {
      ddPower: false,
      outPower: true,
      addInfo: {},
      addPower: true,
      addLoading: false,
      addDialogVisible: false,
      outLoading: false,
      personList: [],
      fileList: [],
      fileList0: [],
      radio: '发放',
      fileState: [],
      file: null,
      currentInfo: {},
      lookDialogVisible: false,
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        { label: "文件名称", prop: "name" },
        {
          label: "文件版本",
          prop: "version",
        },
        { label: "文件状态", prop: "documentState" },
        { label: "发放编号", prop: "issueCode" },
        { label: "发放人", prop: "issueUserName" },
        { label: "发放日期", prop: "issueDate" },
        { label: "回收人", prop: "recycleUserName" },
        { label: "回收日期", prop: "recycleDate" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "回收记录",
              type: "text",
              clickFun: (row) => {
                this.handleUpdate(row);
              }
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row, index) => {
                return row.documentState == '通过'
              }
            },
            {
              name: "查看附件",
              type: "text",
              clickFun: (row) => {
                this.handleLook(row);
              },
              disabled: (row, index) => {
                return !row.url
              }
            },
            {
              name: "审核",
              type: "text",
              clickFun: (row) => {
                this.handleCheck(row);
              },
              disabled: (row, index) => {
                return row.receiveUser != this.userId || row.documentState == '通过'
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
    this.getFileList0()
    this.selectEnumByCategory()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageDocumentIssueRecycle({ ...param })
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
    // 获取文件列表--文件清单
    getFileList() {
      pageManageDocumentList({
        current: -1,
        size: -1
      }).then(res => {
        this.fileList = res.data.records
      }).catch(err => { })
    },
    // 获取文件列表--作废文件
    getFileList0() {
      pageManageDocumentCancel({
        current: -1,
        size: -1
      }).then(res => {
        this.fileList0 = res.data.records
      }).catch(err => { })
    },
    // 导出
    handleOut() {
      this.outLoading = true
      // queryParams
      exportManageDocumentIssueRecycle(this.queryParams).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '发放回收记录.xlsx');
      })
    },
    changeFileList(e) {
      if (e) {
        let obj = this.fileList.find(a => a.documentCode == e)
        if (obj) {
          this.addInfo.name = obj.name
          this.addInfo.version = obj.version
          this.addInfo.state = obj.state
        }
      }
    },
    // 提交
    handleAdd() {
      if (!this.addInfo.documentCode) {
        this.$message.error('请选择文件')
        return
      }
      if (!this.addInfo.receiveUser) {
        this.$message.error('请选择审批人')
        return
      }
      if (this.radio == '发放') {
        if (!this.addInfo.issueUser) {
          this.$message.error('请选择发放人')
          return
        }
      } else {
        if (!this.addInfo.recycleUser) {
          this.$message.error('请选择回收人')
          return
        }
      }
      this.addLoading = true;
      if (!this.addInfo.id) {
        // 新增发放记录
        let fd = new FormData();
        //文件信息中raw才是真的文件
        if (this.file) {
          fd.append("file", this.file.raw);
        }
        for (let key in this.addInfo) {
          fd.append(key, this.addInfo[key])
        }
        addManageDocumentIssueRecycle(fd).then(res => {
          this.addLoading = false;
          if (res.code == 200) {
            this.$message.success('发放成功')
            this.addDialogVisible = false
            this.refreshTable()
          }
        })
      } else {
        let { documentCode, id, issueUser, recycleUser, receiveUser, name, version, documentState, issueCode, recycleCode, issueDate, recycleDate, issueNote, recycleNote } = this.addInfo
        // 添加回收记录
        doManageDocumentIssueRecycle({
          documentCode,
          id,
          issueUser,
          recycleUser,
          receiveUser,
          name,
          version,
          documentState,
          issueCode,
          recycleCode,
          issueDate,
          recycleDate,
          issueNote,
          recycleNote
        }).then(res => {
          this.addLoading = false;
          if (res.code == 200) {
            this.$message.success('提交成功')
            this.addDialogVisible = false
            this.refreshTable()
          }
        })
      }
    },
    // 添加回收
    handleUpdate(row) {
      this.addInfo = this.HaveJson(row)
      this.radio = '回收'
      this.file = null
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
            checkManageDocumentIssueRecycle({ id: row.id, documentState: '通过' }).then(res => {
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
            checkManageDocumentIssueRecycle({ id: row.id, documentState: '不通过' }).then(res => {
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
    handleChangeUpload(file, fileLists) {
      this.file = file
      this.$set(this.addInfo, 'name', file.name)
    },
    // 查看附件
    handleLook(row) {
      this.currentInfo = this.HaveJson(row)
      this.lookDialogVisible = true
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageDocumentIssueRecycle({ id: row.id }).then((res) => {
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
