<template>
  <!-- 文件变更申请 -->
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 128px;font-size: 14px;font-weight: 700;color: #606266;">申请文件编号</span>
          <el-input v-model="queryParams.code" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" icon="el-icon-plus"
                   @click="addDialogVisible = true, addInfo = {}, currentFile = {}, title = '文件变更申请', file = null">文件变更申请</el-button>
        <el-button :loading="outLoading" size="small" type="primary" @click="handleOut">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :title="title" :visible.sync="addDialogVisible" top="0vh" width="950px">
      <div v-loading="diaLoading">
        <el-row>
          <el-col :span="12" style="margin-bottom: 16px;">
            <div class="search_thing">
              <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>申请编号：</div>
              <div class="search_input"><el-input v-model="addInfo.code" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="12" style="margin-bottom: 16px;">
            <div class="search_thing">
              <div class="search_label">审批人：</div>
              <div class="search_input">
                <el-select v-model="addInfo.checkUser" :disabled="title == '审核'" filterable size="small"
                           style="width: 100%;">
                  <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </div>
          </el-col>
          <el-col :span="12" style="margin-bottom: 16px;">
            <div class="search_thing">
              <div class="search_label">期望变更时间：</div>
              <div class="search_input">
                <el-date-picker v-model="addInfo.expectAlterDate" :disabled="title == '审核'" format="yyyy-MM-dd"
                                placeholder="选择日期" size="small" style="width: 100%;" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </div>
            </div>
          </el-col>
          <el-col :span="12" style="margin-bottom: 16px;">
            <div class="search_thing">
              <div class="search_label">实际变更时间：</div>
              <div class="search_input">
                <el-date-picker v-model="addInfo.actuallyAlterDate" :disabled="title == '审核'" format="yyyy-MM-dd"
                                placeholder="选择日期" size="small" style="width: 100%;" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </div>
            </div>
          </el-col>
          <el-col :span="24" style="margin-bottom: 16px;">
            <div class="search_thing">
              <div class="search_label">选择文件：</div>
              <div class="search_input">
                <el-select v-model="addInfo.alterBeforeCode" :disabled="title == '审核'" allow-create clearable filterable
                           size="small" style="width: 100%;" @change="getCurrentFile">
                  <el-option v-for="item in fileList" :key="item.documentCode" :label="item.title"
                             :value="item.documentCode">
                  </el-option>
                </el-select>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <h4 class="title">当前文件信息</h4>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">文件编号：</div>
              <div class="search_input"><el-input v-model="currentFile.documentCode" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">文件名称：</div>
              <div class="search_input"><el-input v-model="currentFile.name" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">文件版本：</div>
              <div class="search_input"><el-input v-model="currentFile.version" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">提交人：</div>
              <div class="search_input"><el-select v-model="currentFile.createUser" disabled filterable size="small"
                                                   style="width: 100%;">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">提交时间：</div>
              <div class="search_input"><el-input v-model="currentFile.createTime" clearable disabled placeholder="请输入"
                                                  size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">审核时间：</div>
              <div class="search_input"><el-input v-model="currentFile.effectiveDate" clearable disabled placeholder="请输入"
                                                  size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">文件状态：</div>
              <div class="search_input">
                <el-select v-model="currentFile.state" disabled size="small" style="width: 100%;">
                  <el-option v-for="item in fileState" :key="item.value" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </div>
          </el-col>
          <el-col :span="24">
            <h4 class="title">文件变更申请</h4>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">变更后编号：</div>
              <div class="search_input"><el-input v-model="addInfo.alterAfterCode" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">变更后名称：</div>
              <div class="search_input"><el-input v-model="addInfo.alterAfterName" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">前一版本处理：</div>
              <div class="search_input">
                <el-select v-model="addInfo.method" :disabled="title == '审核'" size="small" style="width: 100%;">
                  <el-option label="作废" value="作废"></el-option>
                  <el-option label="存档不可用" value="存档不可用">
                  </el-option>
                </el-select>
              </div>
            </div>
          </el-col>
          <el-col :span="8" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">变更后版本：</div>
              <div class="search_input"><el-input v-model="addInfo.alterAfterVersion" :disabled="title == '审核'" clearable
                                                  placeholder="请输入" size="small"></el-input></div>
            </div>
          </el-col>
          <el-col :span="16" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">变更说明：</div>
              <div class="search_input"><el-input v-model="addInfo.alterNote" :disabled="title == '审核'" :rows="2"
                                                  clearable placeholder="请输入" size="small" type="textarea"></el-input></div>
            </div>
          </el-col>
          <el-col v-if="title != '审核'" :span="24" style="margin-bottom: 16px;">
            <div class="search_thing" style="width: 100%;">
              <div class="search_label">相关附件：</div>
              <div class="search_input"><el-upload v-if="addDialogVisible" :auto-upload="false" :multiple="false"
                                                   :on-change="handleChangeUpload" accept='.pdf' action="#" style="margin: 8px 0 0px 50px;">
                <el-button size="small" type="primary">上传附件</el-button>
              </el-upload></div>
            </div>
          </el-col>
          <UpPdfStamp v-if="title == '审核' && addDialogVisible" ref="UpPdfStamp" :isUpFile="false" @uploadPDF="uploadPDF">
          </UpPdfStamp>
        </el-row>
      </div>
      <span v-if="title != '审核'" slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="handleAdd">确 定</el-button>
      </span>
      <span v-else slot="footer" class="dialog-footer">
        <el-button :loading="noCheckLoading" @click="handleCheckSub('不通过')">不通过</el-button>
        <el-button :loading="checkLoading" type="primary" @click="handleCheckSub('通过')">通 过</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.alterAfterUrl"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import UpPdfStamp from '@/components/UpPdfStamp/index.vue'
import filePreview from '@/components/Preview/filePreview.vue'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  exportManageDocumentAlter,
  pageManageDocumentList,
  addManageDocumentAlter,
  doManageDocumentAlter,
  checkManageDocumentAlterPdf,
  checkManageDocumentAlter,
  delManageDocumentAlter,
  pageManageDocumentAlter,
} from '@/api/cnas/systemManagement/documentControl.js'
import { mapGetters } from "vuex";
export default {
  components: {
    limsTable,
    filePreview,
    UpPdfStamp,
  },
  computed: {
    ...mapGetters(["userId"]),
  },
  data() {
    return {
      title: '文件变更申请',
      noCheckLoading: false,
      checkLoading: false,
      addDialogVisible: false,
      diaLoading: false,
      addInfo: {},
      addPower: false,
      outPower: false,
      outLoading: false,
      personList: [],
      fileList: [],
      currentFile: {},
      fileState: [],
      file: null,
      addLoading: false,
      lookDialogVisible: false,
      currentInfo: {
      },
      type: null,
      fileName: null,
      queryParams: {},
      tableData: [],
      column: [
        { label: "申请编号", prop: "code" },
        { label: "申请人", prop: "createUserName", width: "120px" },
        {
          label: "变更说明",
          prop: "alterNote",
        },
        { label: "期望变更时间", prop: "expectAlterDate" },
        { label: "实际变更时间", prop: "actuallyAlterDate" },
        {
          label: "状态", prop: "state", dataType: "tag",
          formatData: (params) => {
            return params;
          },
          formatType: (params) => {
            if (params == '通过') {
              return 'success'
            } else {
              return 'danger'
            }
          },
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
            {
              name: "查看附件",
              type: "text",
              clickFun: (row) => {
                this.handleLook(row);
              },
            },
            {
              name: "下载附件",
              type: "text",
              clickFun: (row) => {
                this.handleDown(row);
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
      pageManageDocumentAlter({ ...param })
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
    // 导出
    handleOut() {
      this.outLoading = true
      exportManageDocumentAlter(this.queryParams).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '文件变更记录.xlsx')
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
      })
    },
    // 获取文件列表--文件清单
    getFileList() {
      pageManageDocumentList({
        current: -1,
        size: -1
      }).then(res => {
        this.fileList = res.data.records.map(m => {
          m.title = m.documentCode + ':' + m.name
          return m
        })
      }).catch(err => { })
    },
    // 当前文件
    getCurrentFile(e) {
      this.currentFile = this.fileList.find(m => m.documentCode == e)
      if (!this.currentFile) {
        this.currentFile = {}
      }
    },
    selectEnumByCategory() {
      // 文件状态
      this.getDicts("document_state").then((response) => {
        this.fileState = this.dictToValue(response.data);
      });
    },
    handleChangeUpload(file, fileLists) {
      this.file = file
      this.$set(this.addInfo, 'alterAfterName', file.name)
    },
    handleAdd() {
      if (!this.addInfo.code) return this.$message({ type: 'error', message: "请输入申请编号" })
      if (!this.addInfo.id) {
        // 新增
        let fd = new FormData();
        //文件信息中raw才是真的文件
        if (this.file) {
          fd.append("file", this.file.raw);
        }
        for (let m in this.addInfo) {
          fd.append(m, this.addInfo[m])
        }
        let { name, version, documentCode } = this.currentFile;
        fd.append("alterBeforeName", name);
        fd.append("alterBeforeVersion", version);
        // fd.append("alterBeforeCode",documentCode);
        this.addLoading = true
        addManageDocumentAlter(fd).then(res => {
          this.addLoading = false
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '添加成功'
            })
            this.refreshTable()
            this.addDialogVisible = false
          } else {
            this.$message({
              type: 'error',
              message: '添加失败'
            })
          }
        })
      } else {
        // 修改
        let fd = new FormData();
        //文件信息中raw才是真的文件
        if (this.file) {
          fd.append("file", this.file.raw);
        }
        let { name, version } = this.currentFile;
        fd.append("alterBeforeName", name);
        fd.append("alterBeforeVersion", version);
        let { code, checkUser, expectAlterDate, actuallyAlterDate, alterAfterCode, method, alterAfterVersion, alterNote, alterAfterName, id } = this.addInfo
        fd.append("code", code);
        fd.append("checkUser", checkUser);
        fd.append("expectAlterDate", expectAlterDate);
        fd.append("actuallyAlterDate", actuallyAlterDate);
        fd.append("alterAfterCode", alterAfterCode);
        fd.append("method", method);
        fd.append("alterAfterVersion", alterAfterVersion);
        fd.append("alterNote", alterNote);
        fd.append("alterAfterName", alterAfterName);
        fd.append("id", id);
        this.addLoading = true
        doManageDocumentAlter(fd).then(res => {
          this.addLoading = false
          if (res.code == 200) {
            this.$message({
              type: 'success',
              message: '修改成功'
            })
            this.refreshTable()
            this.addDialogVisible = false
          } else {
            this.$message({
              type: 'error',
              message: '修改失败'
            })
          }
        })
      }
    },
    // 编辑
    handleUpdate(row) {
      this.title = '文件变更申请'
      this.addInfo = this.HaveJson(row)
      let alterBeforeCode = this.addInfo.alterBeforeCode
      this.getCurrentFile(alterBeforeCode)
      this.file = null
      this.addDialogVisible = true
    },
    // 预览
    handleLook(row) {
      this.currentInfo = this.HaveJson(row)
      this.lookDialogVisible = true
    },
    // 下载附件
    handleDown(row) {
      if (!row.alterAfterUrl) return this.$message.warning('文件未上传')
      this.$download.saveAs(row.alterAfterUrl, row.alterAfterUrl)
    },
    // 打开审核弹框
    handleCheck(row) {
      this.title = '审核'
      this.fileName = row.alterAfterName
      if (!row.alterAfterUrl) return this.$message.warning('文件未上传')
      this.addInfo = this.HaveJson(row)
      let alterBeforeCode = this.addInfo.alterBeforeCode
      this.getCurrentFile(alterBeforeCode)
      this.addDialogVisible = true
      this.diaLoading = true
      checkManageDocumentAlterPdf({ id: row.id }).then(res => {
        //
        this.diaLoading = false
        const blob = new Blob([res]);
        const file = new File([blob], row.name, { type: 'application/pdf' })
        this.$refs.UpPdfStamp.lookFile(file)
        this.currentInfo = row
      }).catch(err => {
        this.diaLoading = false
        console.log(err)
      })
    },
    // 审核保存
    uploadPDF(pdfBlob) {
      const file = new File([pdfBlob], this.fileName, { type: 'application/pdf' })
      console.log('file---', file)
      const formData = new FormData();
      formData.append('file', file, this.fileName + '.pdf'); // 文件字段
      formData.append('id', this.currentInfo.id); // 文件名字段
      formData.append('state', this.type); // 文件名字段
      checkManageDocumentAlter(formData).then(res => {
        this.$message({ message: '操作成功', type: 'success' });
        this.addDialogVisible = false;
        this.checkLoading = false
        this.noCheckLoading = false
        this.refreshTable()
        return true
      }).catch(err => {
        this.checkLoading = false
        this.noCheckLoading = false
        return false
      })
    },
    // 提交审核
    handleCheckSub(type) {
      this.type = type
      if (type == '通过') {
        this.checkLoading = true
      } else {
        this.noCheckLoading = true
      }
      this.addLoading = true
      this.$refs['UpPdfStamp'].generatePDF()
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageDocumentAlter({ id: row.id }).then((res) => {
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
.title {
  height: 60px;
  line-height: 60px;
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

.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}

h4.title {
  position: relative;
  height: 30px;
  line-height: 30px;
  box-sizing: border-box;
  padding-left: 16px;
  margin-left: 10px;
  margin-bottom: 10px;
}

h4.title::after {
  content: '';
  width: 4px;
  height: 20px;
  background: #3A7BFA;
  position: absolute;
  top: 5px;
  left: 0;
}
</style>
