<template>
  <!-- 文件修订申请审批记录 -->
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
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
        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading" v-if="outPower"
                   style="display:inline-block;margin-left: 20px;">导出</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 260px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog :title="title" :visible.sync="addDialogVisible" width="800px" top="6vh">
      <el-row>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">申请类型：</div>
            <div class="search_input">
              <el-radio-group v-model="addInfo.method" size="small" :disabled="title.includes('意见')">
                <el-radio label="修订">修订</el-radio>
                <el-radio label="作废">作废</el-radio>
              </el-radio-group>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color: red;margin-left: 4px;">* </span>文件编号：</div>
            <div class="search_input">
              <el-select v-model="addInfo.documentCode" size="small" style="width: 100%;" @change="getCurrentFile"
                allow-create clearable filterable :disabled="title.includes('意见')">
                <el-option v-for="item in fileList" :key="item.documentCode" :label="item.title"
                  :value="item.documentCode">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件名称：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.documentName"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">章节号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.capter"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">页码：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.pages"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修改次数：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.number"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修改前版本号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.beforeVersion"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修改后版本号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.afterVersion"
                :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修订人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.alterUser" placeholder="请选择" size="small" style="width: 100%;" filterable
                :disabled="title.includes('意见')">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">原分发部门：</div>
            <div class="search_input">
              <el-select v-model="addInfo.beforeDepart" placeholder="请选择" size="small" style="width: 100%;"
                :disabled="title.includes('意见')">
                <el-option v-for="item in list" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">修订后分发部门：</div>
            <div class="search_input">
              <el-select v-model="addInfo.afterDepart" placeholder="请选择" size="small" style="width: 100%;"
                :disabled="title.includes('意见')">
                <el-option v-for="item in list" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col><el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">日期：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.date" type="date" size="small" placeholder="选择日期" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd" style="width: 100%;">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing" style="width: 90%;">
            <div class="search_label">修改内容：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.alterThing"
                type="textarea" :rows="2" :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="24" style="margin-bottom: 16px;">
          <div class="search_thing" style="width: 90%;">
            <div class="search_label">变化原因：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.reason"
                type="textarea" :rows="2" :disabled="title.includes('意见')"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">上传附件：</div>
            <div class="search_input"><el-upload style="margin: 8px 0 0px 50px;" action="#" :auto-upload="false"
                :multiple="false" accept='.pdf' :on-change="handleChangeUpload"
                v-if="addDialogVisible && !title.includes('意见')">
                <el-button size="small" type="primary">上传附件</el-button>
              </el-upload></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;" v-if="title.includes('意见')">
          <div class="search_thing">
            <div class="search_label">申请部门主管意见：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.applicant"
                type="textarea" :rows="2" :disabled="title != '申请部门主管意见'"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;" v-if="title.includes('意见')">
          <div class="search_thing">
            <div class="search_label">原定制部门意见：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.formulation"
                type="textarea" :rows="2" :disabled="title != '原定制部门意见'"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;" v-if="title.includes('意见')">
          <div class="search_thing">
            <div class="search_label">原审核部门意见：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.audit"
                type="textarea" :rows="2" :disabled="title != '原审核部门意见'"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :title="title0" :visible.sync="detailDialogVisible" width="900px" top="20px"
      :class="{ downPdf: title0 == '下载' }" :modal="title0 != '下载'">
      <div style="max-height: 75vh;overflow-y: auto;">
        <div id="dialogBody">
          <h4 style="display: flex;align-items: center;flex-direction: column;justify-content: center;">
            <span style="font-size: 20px;">文件更改/作废申请表</span>
            <span>Application for alteration/cancellation of Document</span>
          </h4>
          <p style="display: flex;justify-content: space-between;margin-top: 16px;">
            <span>{{ currentInfo.documentCode }}</span>
            <span>{{ currentInfo.method }}</span>
          </p>
          <table border="1" class="tables" cellpadding="10">
            <tr>
              <td>
                <p>申请部门</p>
                <p class="en">Application department</p>
              </td>
              <td>{{ currentInfo.createUserDepart }}</td>
              <td>
                <p>申请人</p>
                <p>Proposer</p>
              </td>
              <td>{{ currentInfo.createUserName }}</td>
              <td>
                <p>申请时间</p>
                <p class="en">Application date</p>
              </td>
              <td>{{ currentInfo.createTime }}</td>
            </tr>
            <tr>
              <td>
                <p>文件名称</p>
                <p class="en">File name</p>
              </td>
              <td colspan="3">{{ currentInfo.documentName }}</td>
              <td>
                <p>文件编号</p>
                <p class="en">Document number</p>
              </td>
              <td>{{ currentInfo.documentCode }}</td>
            </tr>
            <tr>
              <td>
                <p>修改前版次</p>
                <p class="en">Modify previous editions</p>
              </td>
              <td colspan="2">{{ currentInfo.beforeVersion }}</td>
              <td>
                <p>修改后版次</p>
                <p class="en">Revised edition</p>
              </td>
              <td colspan="2">{{ currentInfo.afterVersion }}</td>
            </tr>
            <tr>
              <td>
                <p>变化原因</p>
                <p class="en">Changing reason</p>
              </td>
              <td colspan="5">
                <div class="user-content">
                  <p style="text-align: left;line-height: 26px;">{{ currentInfo.reason }}</p>
                </div>
              </td>
            </tr>
            <tr>
              <td>
                <p>原文内容</p>
                <p class="en">Original content</p>
              </td>
              <td colspan="2">见附件</td>
              <td>
                <p>拟修订内容</p>
                <p class="en">Content be revised</p>
              </td>
              <td colspan="2">{{ currentInfo.alterThing }}</td>
            </tr>
            <tr>
              <td>
                <p>修订后分发部门</p>
                <p class="en">Distribute to the department after revision</p>
              </td>
              <td colspan="2">{{ currentInfo.afterDepart }}</td>
              <td>
                <p>原分放部门</p>
                <p class="en">The original distribution department</p>
              </td>
              <td colspan="2">{{ currentInfo.beforeDepart }}</td>
            </tr>
            <tr>
              <td>
                <p>申请部门主管意见</p>
                <p class="en">Opinion of the head of the applicant department</p>
              </td>
              <td colspan="2">
                <p>原制定部门意见</p>
                <p class="en">Original formulation of the department's opinion</p>
              </td>
              <td colspan="2">
                <p>原审核部门意见</p>
                <p class="en">Opinion of the original audit department</p>
              </td>
              <td>
                <p>批准</p>
                <p class="en">Ratify</p>
              </td>
            </tr>
            <tr>
              <td>
                {{ currentInfo.applicant }}
              </td>
              <td colspan="2">
                {{ currentInfo.formulation }}
              </td>
              <td colspan="2">
                {{ currentInfo.audit }}
              </td>
              <td>
                <img :src="javaApi + 'img/' + currentInfo.ratifyUserUrl" style="width: 100%;" alt="批准人签名">
              </td>
            </tr>
          </table>
        </div>
      </div>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="lookDialogVisible" width="800px" top="5vh" fullscreen>
      <h4>修订内容</h4>
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.afterUrl" :currentFile="{}" />
      <h4>原内容</h4>
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.beforeUrl" :currentFile="{}" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import { exportHtmlToPDF } from '@/utils/downHtmlToPDF'
import filePreview from '@/components/Preview/filePreview.vue'
import {
  exportOutManageRecordAudit,
  addManageRecordAudit,
  doManageRecordAudit,
  selectCNSAPersonTree,
  ratifyManageRecordAudit,
  pageManageRecordAudit,
  delManageRecordAudit,
} from '@/api/cnas/systemManagement/documentRecords.js'
import {
  pageManageDocumentList,
} from '@/api/cnas/systemManagement/documentControl.js'
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
export default {
  name: 'RevisionRecord',
  components: {filePreview, limsTable},
  data() {
    return {
      addPower: true,
      outPower: true,
      outLoading: false,
      lookDialogVisible: false,
      addLoading: false,
      title: '新增',
      addInfo: {
        method: '修订',
      },
      addDialogVisible: false,
      detailDialogVisible: false,
      fileList: [],
      personList: [],
      list: [],
      file: null,
      title0: '查看',
      currentInfo: {},
      queryParams: {},
      tableData: [],
      column: [
        { label: "文件编号", prop: "documentCode" },
        { label: "章节号", prop: "capter", width: "120px" },
        {
          label: "页码",
          prop: "pages",
        },
        { label: "修订次数", prop: "number" },
        { label: "修改前版本号", prop: "beforeVersion" },
        { label: "修改后版本号", prop: "afterVersion" },
        { label: "修改内容", prop: "alterThing" },
        { label: "修订人", prop: "alterUserName" },
        { label: "批准人", prop: "ratifyUserName" },
        { label: "日期", prop: "date" },
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
            },
            {
              name: "文件预览",
              type: "text",
              clickFun: (row) => {
                this.handleLook0(row);
              },
            },
            {
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleOut(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
              },
            },
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.handleEdit(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
              },
            },
            {
              name: "批准",
              type: "text",
              clickFun: (row) => {
                this.handleRatify(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
              },
            },
            {
              name: "申请意见",
              type: "text",
              clickFun: (row) => {
                this.handlework0(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
              },
            },
            {
              name: "原定制意见",
              type: "text",
              clickFun: (row) => {
                this.handlework1(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
              },
            },
            {
              name: "原审核意见",
              type: "text",
              clickFun: (row) => {
                this.handlework2(row);
              },
              disabled: (row) => {
                return !!row.ratifyUser
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
    this.getFileList()
    this.getAuthorizedPerson()
    this.selectTreeList()
  },
  methods: {
    openAdd() {
      this.addInfo = {
        method: '修订',
      }
      this.title = '新增'
      this.file = null
      this.addDialogVisible = true;
    },
    // 导出
    handleDown() {
      this.outLoading = true
      exportOutManageRecordAudit(this.queryParams).then(res => {
        this.outLoading = false
        this.$download.saveAs(res.data, "文件修订表");
      })
    },
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageRecordAudit({ ...param })
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
    getCurrentFile(e) { },
    handleAdd() {
      if (!this.addInfo.documentCode) return this.$message({ type: 'error', message: "请输入文件编号" })
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
        this.addLoading = true
        addManageRecordAudit(fd).then(res => {
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
        let { id, method, documentCode, documentName, capter, pages, beforeVersion, afterVersion, alterUser, alterThing, reason, beforeDepart, afterDepart, number, applicant, formulation, audit, date } = this.addInfo
        let fd = new FormData();
        //文件信息中raw才是真的文件
        if (this.file) {
          fd.append("file", this.file.raw);
        }
        let obj = { id, method, documentCode, documentName, capter, pages, beforeVersion, afterVersion, alterUser, alterThing, reason, beforeDepart, afterDepart, number, applicant, formulation, audit, date }
        for (let m in obj) {
          if (this.addInfo[m]) {
            fd.append(m, this.addInfo[m])
          }
        }
        this.addLoading = true
        doManageRecordAudit(fd).then(res => {
          this.addLoading = false
          if (res.code == 200) {
            this.refreshTable()
            this.addDialogVisible = false
          } else {
            this.$message({
              type: 'error',
              message: '添加失败'
            })
          }
        })
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
    // 查询树形列表
    selectTreeList() {
      selectCNSAPersonTree().then((res) => {
        this.list = res.data[0].children;
      });
    },
    handleChangeUpload(file, fileLists) {
      this.file = file
      // this.$set(this.addInfo,'name',file.name)
    },
    handleEdit(row) {
      this.addInfo = row
      this.title = '修改'
      this.file = null
      this.addDialogVisible = true;
    },
    // 审批
    handleRatify(row) {
      this.$confirm('是否批准通过?', '批准', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          ratifyManageRecordAudit({ id: row.id }).then(res => {
            this.$message.success('操作成功');
            this.refreshTable();
          })
            .catch(e => {
              this.$message.error('操作失败');
            });
        })
        .catch(() => {
        });
    },
    handlework0(row) {
      this.addInfo = row
      this.title = '申请部门主管意见'
      this.addDialogVisible = true;
    },
    handlework1(row) {
      this.addInfo = row
      this.title = '原定制部门意见'
      this.addDialogVisible = true;
    },
    handlework2(row) {
      this.addInfo = row
      this.title = '原审核部门意见'
      this.addDialogVisible = true;
    },
    handleLook(row) {
      this.title0 = '查看'
      this.currentInfo = row
      this.detailDialogVisible = true;
    },
    handleOut(row) {
      this.currentInfo = row
      this.title0 = '下载'
      this.detailDialogVisible = true;
      setTimeout(() => {
        this.$nextTick(() => {
          const element = document.getElementById("dialogBody");
          exportHtmlToPDF(element, '文件' + row.method + '申请表').then(res => {
            this.detailDialogVisible = false;
          })
        })
      }, 500);
    },
    handleLook0(row) {
      this.currentInfo = row;
      this.lookDialogVisible = true
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delManageRecordAudit({ id: row.id }).then((res) => {
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

.tables {
  table-layout: fixed;
  width: 100%;
  margin-top: 10px;
  border-collapse: collapse;
}

.tables td {
  height: 40px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}

.en {
  font-size: 12px;
  word-break: break-word;
  /* 自动断行 */
  overflow-wrap: break-word;
  /* 防止溢出 */
  white-space: normal;
  /* 默认换行 */
}

.heads td {
  border: 1px dashed black;
  /* 单元格的虚线 */
  padding: 8px;
  text-align: left;
}

.downPdf {
  opacity: 0 !important;
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
