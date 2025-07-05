<!-- 设备借用 -->
<template>
  <div>
    <div class="search">
      <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
        <el-form-item label="流程编号">
          <el-input size="small" placeholder="请输入" clearable v-model="queryParams.processNumber"
                    @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="mini" type="primary" @click="refreshTable()">查询</el-button>
          <el-button size="mini" @click="refresh()">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        <el-button size="small" type="primary" @click="add">新增</el-button>
        <!--        <el-button size="small" type="primary" @click="handleDown" :loading="outLoading">导出</el-button>-->
      </div>
    </div>
    <div class="tables">
      <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 290px)'"
        :page="page" @pagination="pagination"></lims-table>
    </div>
    <el-dialog title="仪器设备领(借)用登记" top="5vh" :visible.sync="dialogVisible" width="60%">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step style="cursor:pointer" v-for="(v, i) in steps" :title="v" :key="i"
          @click.native="choiceStep(i)"></el-step>
      </el-steps>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <div v-show="currentStepClick === 0">
          <el-card style="margin-top: 1em; height: 51vh; overflow-y: scroll;">
            <!-- 新增设备事记录卡片 -->
            <el-row>
              <el-col :span="12">
                <el-form-item label="编号：" prop="processNumber">
                  <el-input v-model="form.processNumber" size="small" :disabled="currentStep > 0"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备名称：">
                  <el-input v-model="form.deviceName" size="small" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="管理编号：" prop="unifyNumber">
                  <el-input v-model="form.unifyNumber" size="small" :disabled="currentStep > 0"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="借用人：" prop="recipientUser">
                  <el-select v-model="form.recipientUser" filterable placeholder="请选择" clearable size="small"
                    style="width: 100%;" :disabled="currentStep > 0">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="借用人联系方式：" prop="borrowerContactInformation" label-width="150px">
                  <el-input v-model="form.borrowerContactInformation" size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="借用日期：" prop="recipientTime">
                  <el-date-picker v-model="form.recipientTime" type="date" placeholder="选择日期" size="small"
                    format="yyyy-MM-dd" style="width: 100%" value-format="yyyy-MM-dd" :disabled="currentStep > 0">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="借用时状态：">
                  <el-radio-group v-model="form.recipientState" :disabled="currentStep !== 0">
                    <el-radio :label="0">合格</el-radio>
                    <el-radio :label="1">维修</el-radio>
                    <el-radio :label="2">停用</el-radio>
                    <el-radio :label="3">报废</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="相关附件：" prop="fileName">
                  <el-input v-model="form.fileName" disabled size="small"
                    :style="`width: ${currentStep == 0 ? '88%' : '100%'};`">
                    <el-button slot="append" v-if="currentStep === 0" icon="el-icon-delete-solid"
                      @click="deleteFile"></el-button>
                  </el-input>
                  <el-upload ref="upload" style="float: right;" :action="action" :show-file-list="false"
                    :on-success="onSuccess" :disabled="currentStep !== 0" :headers="uploadHeader">
                    <el-button style="position: relative;top: -4px" class="uploadFile" slot="trigger" size="small"
                      type="primary" v-if="currentStep === 0">附件上传</el-button>
                  </el-upload>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下环节责任人：" prop="nextUser">
                  <el-select v-model="form.nextUser" filterable placeholder="请选择" clearable size="small"
                    style="width: 100%;" :disabled="currentStep !== 0">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.submitOperationUser }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.submitOperationTime }}
            </el-col>
          </el-row>
        </div>
        <div v-show="currentStepClick === 1">
          <el-card style="margin-top: 1em; height: 51vh; overflow-y: scroll;">
            <el-row>
              <el-col :span="12">
                <el-form-item label="归还人：" prop="rebackUser"
                  :rules="[{ required: currentStep === 1, message: '请输入归还人', trigger: 'change' }]">
                  <el-select v-model="form.rebackUser" filterable placeholder="请选择" clearable size="small"
                    style="width: 50%;" :disabled="currentStep !== 1">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="归还日期：" prop="rebackTime"
                  :rules="[{ required: currentStep === 1, message: '请选择归还日期', trigger: 'change' }]">
                  <el-date-picker v-model="form.rebackTime" :disabled="currentStep !== 1" type="date" placeholder="选择日期"
                    size="small" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="接收状况：">
                  <el-radio-group v-model="form.receiveState" :disabled="currentStep !== 1">
                    <el-radio :label="0">合格</el-radio>
                    <el-radio :label="1">维修</el-radio>
                    <el-radio :label="2">停用</el-radio>
                    <el-radio :label="3">报废</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="设备负责人：" prop="deviceUser"
                  :rules="[{ required: currentStep === 1, message: '请选择设备负责人', trigger: 'change' }]">
                  <el-select v-model="form.deviceUser" filterable placeholder="请选择" clearable size="small"
                    style="width: 50%;" :disabled="currentStep !== 1">
                    <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.name">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="备注：">
                  <el-input type="textarea" v-model="form.note" :disabled="currentStep !== 1" size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
          <el-row style="margin-top: 1em;">
            <el-col :span="4">
              操作人：{{ form.receiveOperationUser }}
            </el-col>
            <el-col :span="6">
              日期：{{ form.receiveOperationTime }}
            </el-col>
          </el-row>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="submitForm('3reject')" v-if="currentStep !== 0 && currentStep !== 2">驳回</el-button>
        <el-button @click="submitForm('2save')" v-if="currentStep === 0">保存</el-button>
        <el-button type="primary" v-if="currentStep !== 2" @click="submitForm('1submit')">{{ currentStep === 0 ? '提交' :
          '通过'
          }}</el-button>
      </span>
    </el-dialog>
    <el-dialog title="流程跟踪" top="5vh" :visible.sync="dialogVisible0" width="60%">
      <el-table :data="deviceLogs" style="width: 100%" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column type="index" width="50">
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="180">
        </el-table-column>
        <el-table-column prop="operationTime" label="操作日期" width="180">
        </el-table-column>
        <el-table-column prop="operationType" label="提交类型">
        </el-table-column>
        <el-table-column prop="operationContent" label="操作内容">
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import { dateFormat } from '@/utils/date'
import {
  saveDeviceBorrow,
  deleteCNASFile,
  getDeviceBorrow,
  deviceBorrowExport,
  deleteDeviceBorrow,
  deviceBorrowPage,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
import { mapGetters } from "vuex";
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  components: {
    limsTable
  },
  data() {
    return {
      dialogVisible: false,
      dialogVisible0: false,
      currentStep: 0, // 步骤条显示第几步
      currentStepClick: 0, // 点击步骤条变化
      steps: ['借出', '借用'],
      responsibleOptions: [], // 下环节负责人list
      form: {
        processNumber: null,
        deviceName: null,
        unifyNumber: null,
        recipientUser: null,
        recipientTime: null,
        nextUser: null,
        rebackUser: null,
        rebackTime: null,
        receiveState: null,
        deviceUser: null,
        fileName: null,
      },
      rules: {
        processNumber: [{ required: true, message: '请输入编号', trigger: 'blur' }],
        deviceName: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
        unifyNumber: [{ required: true, message: '请输入管理编号', trigger: 'blur' }],
        recipientUser: [{ required: true, message: '请输入借用人', trigger: 'blur' }],
        borrowerContactInformation: [{ required: true, message: '请输入借用人联系方式', trigger: 'blur' }],
        recipientTime: [{ required: true, message: '请选择借用日期', trigger: 'change' }],
        nextUser: [{ required: true, message: '请选择下环节负责人', trigger: 'change' }],
      },
      deviceLogs: [],
      outLoading: false,
      recipientStateList: [{
        value: 0,
        type: 'success',
        label: '合格'
      }, {
        value: 1,
        type: 'warning',
        label: '维修'
      }, {
        value: 2,
        type: 'info',
        label: '停用'
      }, {
        value: 3,
        type: 'danger',
        label: '报废'
      }],
      queryParams: {},
      tableData: [],
      column: [
        { label: "流程编号", prop: "processNumber" },
        { label: "设备名称", prop: "deviceName" },
        {
          label: "管理编号",
          prop: "unifyNumber"
        },
        { label: "借用人", prop: "recipientUser" },
        { label: "借用人联系方式", prop: "borrowerContactInformation", width: '140px' },
        { label: "借用日期", prop: "recipientTime" },
        {
          label: "借用时状态", prop: "recipientState", dataType: "tag",
          formatData: (params) => {
            return this.recipientStateList.find((m) => m.value == params).label;
          },
          formatType: (params) => {
            return this.recipientStateList.find((m) => m.value == params).type;
          },
        },
        { label: "借出人", prop: "submitUser" },
        { label: "借出日期", prop: "createTime" },
        {
          label: "当前状态", prop: "nowState"
        },
        { label: "当前责任人", prop: "nowUser" },
        { label: "附件", prop: "fileName" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "查看",
              type: "text",
              clickFun: (row) => {
                this.lookDetail(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
            },
            {
              name: "流程跟踪",
              type: "text",
              clickFun: (row) => {
                this.handleLookList(row);
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
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        thisqueryParams.deviceId = this.clickNodeVal.value
        this.refreshTable()
      }
    },
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {}
        this.currentStep = 0 // 步骤条显示第几步
        this.currentStepClick = 0 // 点击步骤条变化
        this.$refs['form'].clearValidate()
      }
    }
  },
  computed: {
    ...mapGetters(["nickName"]),
    action() {
      return this.javaApi + '/personBasicInfo/saveCNASFile'
    }
  },
  mounted() {
    this.queryParams.deviceId = this.clickNodeVal.value
    this.getUserList()
    this.refreshTable()
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      deviceBorrowPage({ ...param })
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
    choiceStep(index) {
      this.currentStepClick = index
    },
    //提交表单
    async submitForm(saveState) {
      this.$refs.form.validate((valid) => {
        if (valid === true || saveState !== '1submit') {
          // 给当前环节设置创建人与时间
          let user = JSON.parse(localStorage.getItem('user'))
          const dateTime = dateFormat(new Date())
          // 获取当前环节操作人与日期
          switch (this.currentStep) {
            case 0:
              this.form.submitOperationUser = this.nickName
              this.form.submitOperationTime = dateTime
              break
            case 1:
              this.form.receiveOperationUser = this.nickName
              this.form.receiveOperationTime = dateTime
              break
            default:
              break
          }
          // 获取当前环节负责人
          switch (saveState === '3reject' ? this.currentStep - 1 : this.currentStep) {
            case 0:
              this.form.nowUser = this.form.nextUser
              break
            default:
              break
          }
          let currentStepAction;
          // 设置该操作判断是否为提交，保存，驳回，通过
          switch (saveState) {
            // 提交，通过
            case '1submit':
              currentStepAction = this.currentStep + 1
              break
            // 保存
            case '2save':
              currentStepAction = this.currentStep
              break
            // 驳回
            case '3reject':
              currentStepAction = this.currentStep - 1
              break
            default:
              break
          }
          // 获取当前状态
          this.form.nowState = currentStepAction === 2 ? '关闭' : this.steps[currentStepAction]
          this.form.deviceId = this.clickNodeVal.value
          delete this.form.deviceLogs
          saveDeviceBorrow(this.form).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功')
              this.dialogVisible = false
              this.refreshTable()
            }
          })
        } else {
          let step = this.steps[this.currentStep]
          this.$message.warning(step + '  流程中有必填项未填！');
        }
      });
    },
    deleteFile() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCNASFile({ fileName: this.form.fileName }).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功！')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    onSuccess(response, file, fileList) {
      if (response.code == 200) {
        // 在保存赋值新文件
        this.form.fileName = file.name
        this.form.url = response.data
      } else {
        this.$message.error(response.message)
      }
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition().then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
    // 查看详情
    lookDetail(row) {
      getDeviceBorrow({ id: row.id }).then(res => {
        if (res.code == 200) {
          this.form = res.data
          this.form.deviceName = this.clickNodeVal.label
          this.form.deviceId = this.clickNodeVal.value
          this.deviceLogs = res.data.deviceLogs
          let i = this.steps.findIndex(item => item == row.nowState)
          if (i == -1) {
            this.currentStep = 2
            this.currentStepClick = 0
          } else {
            this.currentStep = i
            this.currentStepClick = i
          }
          console.log(this.currentStepClick)
          this.dialogVisible = true
        }
      })
    },
    // 新增
    add() {
      this.dialogVisible = true
      this.form = {
        processNumber: null,
        deviceName: null,
        unifyNumber: null,
        recipientUser: null,
        recipientTime: null,
        nextUser: null,
        rebackUser: null,
        rebackTime: null,
        receiveState: null,
        deviceUser: null,
        fileName: null,
      }
      this.form.deviceName = this.clickNodeVal.label
      this.form.deviceId = this.clickNodeVal.value
    },
    //导出
    handleDown() {
      this.outLoading = true
      deviceBorrowExport({ deviceId: this.clickNodeVal.value }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], {
          type: 'application/force-download'
        })
        const filename = decodeURI(this.clickNodeVal.label + '设备借出统计' + '.xlsx')
        this.$download.saveAs(blob, filename)
      })
    },
    handleLookList(row) {
      getDeviceBorrow({ id: row.id }).then(res => {
        if (res.code == 200) {
          this.deviceLogs = res.data.deviceLogs
          this.dialogVisible0 = true
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
          deleteDeviceBorrow({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refresh();
          });
        })
        .catch(() => { });
    },
  },
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
