<template>
  <!-- 文件受控申请 -->
  <div>
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 128px;font-size: 14px;font-weight: 700;color: #606266;">申请文件编号</span>
          <el-input size="small" placeholder="请输入" clearable v-model="queryParams.documentCode"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" icon="el-icon-plus"
                   @click="addDialogVisible = true, addInfo = {}, file = null">文件受控申请</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination"></lims-table>
    </div>
    <el-dialog title="文件受控申请" :visible.sync="addDialogVisible" width="800px" top="10vh">
      <el-row>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label"><span style="color:red;margin-right: 4px;">*</span>申请编号：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.documentCode"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">责任人：</div>
            <div class="search_input">
              <el-select v-model="addInfo.dutyUser" size="small" style="width: 100%;" filterable>
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件类别：</div>
            <div class="search_input">
              <el-select v-model="addInfo.type" size="small" style="width: 100%;">
                <el-option v-for="item in fileType" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件名称：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.name"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">文件版本：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable
                v-model="addInfo.version"></el-input></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">上传附件：</div>
            <div class="search_input"><el-upload style="margin: 8px 0 0px 50px;" action="#" :auto-upload="false"
                :multiple="false" accept='.pdf' :on-change="handleChangeUpload" v-if="addDialogVisible">
                <el-button size="small" type="primary">上传附件</el-button>
              </el-upload></div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">作者：</div>
            <div class="search_input">
              <!-- <el-input size="small" placeholder="请输入" clearable v-model="addInfo.writer"></el-input> -->
              <el-select v-model="addInfo.writer" size="small" style="width: 100%;" filterable>
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.label">
                </el-option>
              </el-select>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">提交日期：</div>
            <div class="search_input">
              <el-date-picker v-model="addInfo.submitDate" type="date" size="small" placeholder="选择日期"
                format="yyyy-MM-dd" value-format="yyyy-MM-dd" style="width: 100%;">
              </el-date-picker>
            </div>
          </div>
        </el-col>
        <el-col :span="12" style="margin-bottom: 16px;">
          <div class="search_thing">
            <div class="search_label">说明：</div>
            <div class="search_input"><el-input size="small" placeholder="请输入" clearable v-model="addInfo.instructions"
                type="textarea" :rows="2"></el-input></div>
          </div>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd" :loading="addLoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="查看附件" :visible.sync="lookDialogVisible" width="800px" top="5vh" fullscreen>
      <filePreview v-if="lookDialogVisible" :fileUrl="javaApi + '/word/' + currentInfo.url" :currentFile="{}"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
    <el-dialog title="审核" :visible.sync="checkDialogVisible" width="1000px" top="5vh">
      <UpPdfStamp ref="UpPdfStamp" v-if="checkDialogVisible" @uploadPDF="uploadPDF" :isUpFile="false"></UpPdfStamp>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCheckSub('不通过')" :loading="noCheckLoading">不通过</el-button>
        <el-button type="primary" @click="handleCheckSub('通过')" :loading="checkLoading">通 过</el-button>
      </span>
    </el-dialog>
    <el-dialog title="选择受控章" :visible.sync="checkStampDialogVisible" width="600px" top="5vh">
      <div class="stamp-list">
        <img :src="require('@/assets/stamps/' + item + '.png')" alt="" v-for="(item, index) in stampsList" :key="index"
          style="width: 120px;height: 80px;margin: 6px;" class="stamp" :class="{ active: currentStamp == item }"
          @click="currentStamp = item">
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="checkStampDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleCheck0(currentInfo)">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import UpPdfStamp from '@/components/UpPdfStamp/index.vue'
import filePreview from '@/components/Preview/filePreview.vue'
import limsTable from "@/components/Table/lims-table.vue";
import {
  selectUserCondition,
} from "@/api/business/inspectionTask.js";
import {
  addManageDocumentControlled,
  doManageDocumentControlled,
  checkManageDocumentControlledPdf,
  delManageDocumentControlled,
  pageManageDocumentControlled,
  checkManageDocumentControlled,
} from '@/api/cnas/systemManagement/documentControl.js'
import { mapGetters } from "vuex";
export default {
  components: {
    filePreview,
    UpPdfStamp,
    limsTable
  },
  computed: {
    ...mapGetters(["nickName"]),
  },
  data() {
    return {
      upLoading: false,
      addPower: false,
      addDialogVisible: false,
      addLoading: false,
      lookDialogVisible: false,
      checkDialogVisible: false,
      checkStampDialogVisible: false,
      addInfo: {},
      personList: [],
      fileType: [],
      file: null,
      currentInfo: {},
      checkLoading: false,
      noCheckLoading: false,
      type: '',
      stampsList: ['主任', '质量负责人', '技术负责人', '综合室', '通信', '电力', '装备', '储能', '射频'],
      currentStamp: '主任',
      queryParams: {},
      tableData: [],
      column: [
        { label: "申请文件编号", prop: "documentCode" },
        {
          label: "文件类别", prop: "type", width: "120px", dataType: "tag",
          formatData: (params) => {
            if(this.fileType.find((m) => m.value == params)) {
              return this.fileType.find((m) => m.value == params).label;
            } else {
              return null
            }
          },
          formatType: (params) => {
            if(this.fileType.find((m) => m.value == params)) {
              return this.fileType.find((m) => m.value == params).type;
            } else {
              return null
            }
          },
        },
        {
          label: "申请人",
          prop: "createUserName",
        },
        { label: "申请时间", prop: "createTime" },
        { label: "说明", prop: "instructions" },
        { label: "提交日期", prop: "submitDate" },
        { label: "责任人", prop: "dutyUserName" },
        {
          label: "申请状态", prop: "state", dataType: "tag",
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
              disabled: (row) => {
                return row.state == '通过'
              }
            },
            {
              name: "审核",
              type: "text",
              clickFun: (row) => {
                this.handleCheck(row);
              },
              disabled: (row) => {
                if(row.dutyUserName != null && row.dutyUserName != "") {
                  return !row.dutyUserName.includes(this.nickName) || row.state == '通过'
                }else {
                  return false
                }

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
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleDown(row);
              },
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
    this.selectEnumByCategory()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      pageManageDocumentControlled({ ...param })
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
    refreshTable() {
      this.page.current = 1;
      this.getList();
    },
    refresh() {
      this.queryParams = {};
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
    selectEnumByCategory() {
      // 文件类别
      this.getDicts("document_type").then((response) => {
        this.fileType = this.dictToValue(response.data);
      });
    },
    // 提交
    handleAdd() {
      if (!this.addInfo.documentCode) return this.$message({ type: 'error', message: "请输入编号" })
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
        addManageDocumentControlled(fd).then(res => {
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
        let { id, documentCode, dutyUser, type, name, version, writer, submitDate, instructions } = this.addInfo
        let fd = new FormData();
        //文件信息中raw才是真的文件
        if (this.file) {
          fd.append("file", this.file.raw);
        }
        fd.append("id", id);
        fd.append("documentCode", documentCode);
        fd.append("dutyUser", dutyUser);
        fd.append("type", type);
        fd.append("name", name);
        fd.append("version", version);
        fd.append("writer", writer);
        fd.append("submitDate", submitDate);
        fd.append("instructions", instructions);
        this.addLoading = true
        doManageDocumentControlled(fd).then(res => {
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
    handleChangeUpload(file, fileLists) {
      this.file = file
      this.$set(this.addInfo, 'name', file.name)
    },
    // 编辑
    handleUpdate(row) {
      this.title = '文件变更申请'
      this.addInfo = this.HaveJson(row)
      this.file = null
      this.addDialogVisible = true
    },
    // 查看附件
    handleLook(row) {
      this.currentInfo = row
      this.lookDialogVisible = true
    },
    // 审核
    handleCheck(row) {
      this.title = '审核'
      this.currentInfo = row
      if (!row.url) return this.$message.warning('文件未上传')
      this.checkStampDialogVisible = true
    },
    handleCheck0(row) {
      this.checkStampDialogVisible = false
      this.checkDialogVisible = true
      checkManageDocumentControlledPdf({ id: row.id }).then(res => {
        const blob = new Blob([res]);
        const file = new File([blob], row.name, { type: 'application/pdf' })
        this.$refs.UpPdfStamp.lookFile(file, this.currentStamp)
      }).catch(err => {
        console.log(err)
      })
    },
    handleDown(row) {
      if (!row.url) return this.$message.warning('文件未上传')
      this.$download.saveAs(row.url, row.url);
    },
    async uploadPDF(pdfBlob) {
      const formData = new FormData();
      formData.append('file', pdfBlob, this.fileName + '.pdf'); // 文件字段
      formData.append('id', this.currentInfo.id); // 文件名字段
      formData.append('state', this.type); // 文件名字段
      formData.append('writer', this.currentInfo.writer); // 文件名字段

      try {
        let res = await checkManageDocumentControlled(formData)
        this.checkLoading = false
        this.noCheckLoading = false
        if (res.code == 200) {
          this.$message({ message: '操作成功', type: 'success' });
          this.checkDialogVisible = false;
          this.refreshTable()
          return true
        } else {
          this.$message({ message: '操作失败', type: 'error' });
          return false
        }
      } catch (e) {
        this.checkLoading = false
        this.noCheckLoading = false
      }


    },
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
          delManageDocumentControlled({ id: row.id }).then((res) => {
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
.stamp {
  cursor: pointer;
  border: #fff 1px solid;
}

.stamp:hover {
  border: #3A7BFA 1px solid;
  box-shadow: inset 0px 0px 15px #3A7BFA;
}

.stamp.active {
  border: #3A7BFA 1px solid;
  box-shadow: inset 0px 0px 15px #3A7BFA;
}
</style>
