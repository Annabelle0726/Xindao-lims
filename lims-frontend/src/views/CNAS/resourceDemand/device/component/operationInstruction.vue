<!-- 作业指导书 -->
<template>
  <div>
    <div style="margin: 10px 0;text-align: right">
      <el-button size="small" type="primary" @click="getList">刷新</el-button>
      <el-button size="small" type="primary" @click="dialogVisible = true">受控申请</el-button>
    </div>
    <el-table :data="tableData" border height="calc(100vh - 18em)"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
      <el-table-column type="index" label="序号" width="120">
        <template v-slot="scope">
          <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请编号" min-width="150" prop="applicationNumber"></el-table-column>
      <el-table-column label="设备名称" min-width="150" prop="deviceName"></el-table-column>
      <el-table-column label="设备编号" min-width="150" prop="deviceNumber"></el-table-column>
      <el-table-column label="设备型号" min-width="150" prop="deviceModel"></el-table-column>
      <el-table-column label="文件编号" min-width="150" prop="documentNumber"></el-table-column>
      <el-table-column label="文件名字" min-width="150" prop="fileName"></el-table-column>
      <el-table-column label="文档说明" min-width="150" prop="documentNote"></el-table-column>
      <el-table-column label="上传人" min-width="150" prop="uploaderName"></el-table-column>
      <el-table-column label="上传时间" min-width="150" prop="updateTime"></el-table-column>
      <el-table-column label="生效时间" min-width="150" prop="entryIntoForceTime"></el-table-column>
      <el-table-column label="审批人" min-width="150" prop="approverName"></el-table-column>
      <el-table-column label="审批状态" min-width="150" prop="status">
        <template v-slot="scope">
          {{ scope.row.status === true ? '通过' : scope.row.status === false ? '不通过' : '未审核' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="180" fixed="right" prop="name">
        <template v-slot="scope">
          <el-button type="text" size="small" @click="downloadFile(scope.row.fileSystemName)">下载</el-button>
          <el-button type="text" size="small" style="color: red;"
            @click="deleteHomeworkGuidebook(scope.row)">删除</el-button>
          <el-button type="text" size="small" @click="instructionEditFun(scope.row)">编辑</el-button>
          <el-button type="text" size="small" @click="approval(scope.row)">审批</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="1"
      :page-sizes="[10, 20, 30, 50, 100]" :page-size="search.size" layout="->,total, sizes, prev, pager, next, jumper"
      :total="search.total" background>
    </el-pagination>
    <el-dialog :visible.sync="dialogVisible" title="受控申请" width="60%">
      <div style="height: 60vh; overflow-y: auto; overflow-x: hidden;">
        <el-form ref="form1" label-width="110px" :model="instructionForm">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申请编号:">
                <el-input v-model="instructionForm.applicationNumber" disabled size="small" clearable></el-input>
              </el-form-item>
              <el-form-item label="附件：" style="float: left;">
                <el-upload ref="uploadFile" :action="action" :before-remove="beforeRemove" :file-list="fileList1"
                  :headers="uploadHeader" :limit="1" :on-error="onError" :on-exceed="handleExceed"
                  :on-remove="handleRemove1" :on-success="onSuccess1" class="upload-demo" multiple>
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请部门:">
                <el-input v-model="instructionForm.applicationDepartment" clearable size="small"></el-input>
              </el-form-item>
              <el-form-item label="责任人:">
                <el-input v-model="instructionForm.personLiable" clearable size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="受控申请说明:" prop="controlledApplicationDescription"
                :rules="[{ required: true, message: '请输入受控申请说明', trigger: 'blur' }]">
                <el-input v-model="instructionForm.controlledApplicationDescription" type="textarea"
                  clearable></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div style="text-align: right; margin-bottom: 10px">
          <el-button size="small" type="primary" @click="openAddDia">添加受控文件</el-button>
          <el-button size="small" @click="delFile">删除</el-button>
        </div>
        <el-table :data="documentTableData" border style="width: 100%;" tooltip-effect="dark" :row-key="getRowKey"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
          @selection-change="handleSelectionChange" height="29em">
          <el-table-column type="selection" width="55%">
          </el-table-column>
          <el-table-column label="序号" prop="id" type="index" width="60"></el-table-column>
          <el-table-column label="设备名称" prop="deviceName" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="设备编号" prop="deviceNumber" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="设备型号" prop="deviceModel" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="文档编号" prop="documentNumber" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="文件名称" prop="fileName" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="上传人" prop="author" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column label="上传时间" prop="updateTime" show-overflow-tooltip min-width="125"></el-table-column>
          <el-table-column min-width="100" label="操作" fixed="right">
            <template v-slot="scope">
              <el-button type="text" size="small" @click="downloadFile(scope.row.fileSystemName)">下载</el-button>
              <el-button type="text" @click="editFun(scope.row, scope.$index)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer">
        <el-button @click="dialogVisible = false" size="small">取 消</el-button>
        <el-button size="small" type="primary" @click="submitFun">提交</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisible1" title="添加设备作业指导书" width="40%">
      <div style="height: 50vh;">
        <el-form ref="form" :model="form" label-width="90px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="文件类型:" prop="documentType"
                :rules="[{ required: true, message: '请输入文件类型', trigger: 'blur' }]">
                <el-input v-model="form.documentType" clearable size="small" placeholder="请输入文件类型"></el-input>
              </el-form-item>
              <el-form-item label="设备名称:" prop="deviceName"
                :rules="[{ required: true, message: '请选择设备', trigger: 'change' }]">
                <el-select v-model="form.deviceName" size="small" clearable style="width: 100%" placeholder="请选择设备名称"
                  @change="onDeviceNameChange" filterable>
                  <el-option v-for="item in deviceNameOption" :key="item.id" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="文档编号:" prop="documentNumber"
                :rules="[{ required: true, message: '请输入文档编号', trigger: 'blur' }]">
                <el-input v-model="form.documentNumber" size="small" clearable placeholder="请输入文档编号"></el-input>
              </el-form-item>
              <el-form-item label="管理编号:">
                <el-input v-model="form.deviceNumber" size="small" clearable disabled placeholder="请输入管理编号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="文件版本:" prop="documentVersion"
                :rules="[{ required: true, message: '请输入文件版本', trigger: 'blur' }]">
                <el-input v-model="form.documentVersion" size="small" placeholder="请输入文件版本" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="作者:" prop="author" :rules="[{ required: true, message: '请输入作者', trigger: 'blur' }]">
                <el-input v-model="form.author" size="small" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="提交日期:" prop="submitDate"
                :rules="[{ required: true, message: '请选择提交日期', trigger: 'change' }]">
                <el-date-picker v-model="form.submitDate" format="yyyy-MM-dd" style="width: 100%" size="small" clearable
                  placeholder="选择日期" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="文档说明:" prop="documentNote"
                :rules="[{ required: true, message: '请输入文档说明', trigger: 'blur' }]">
                <el-input v-model="form.documentNote" placeholder="请输入文档说明" type="textarea" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="附件：" style="float: left;">
                <el-upload ref="uploadFile" :action="action" :before-remove="beforeRemove" :file-list="fileList"
                  :headers="uploadHeader" :limit="1" :on-error="onError" :on-exceed="handleExceed"
                  :on-remove="handleRemove" :on-success="onSuccess" class="upload-demo" multiple>
                  <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer">
        <el-button @click="closeDia" size="small">取 消</el-button>
        <el-button type="primary" @click="addFile" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import fileDownload from "@/utils/file";
import {
  approvalOfHomeworkInstructionManual,
  homeworkGuidebook,
  homeworkGuidebookEditor,
  newHomeworkGuidebookAdded,
  deleteHomeworkGuidebook,
  pageByPageQueryOfHomeworkInstructions,
  selectDeviceParameter,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  name: '',
  components: {},
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0
      },
      dialogVisible: false,
      dialogVisible1: false,
      value: '',
      selectRow: null,
      device: null,
      tableDataIndex: '',
      form: {
        documentType: '',
        documentNumber: '',
        deviceName: '',
        deviceModel: '',
        deviceNumber: '',
        fileName: '',
        fileSystemName: '',
        submitDate: '',
        documentNote: '',
      },
      tableData: [],
      documentTableData: [],
      formData: {},
      fileList: [],
      fileList1: [],
      laboratoryNameIsNull: false,
      devices: [], //设备列表数据
      deviceNameOption: [], //设备名称下拉框数据
      entity: {
        deviceName: null,
        laboratoryName: '',
        storagePoint: ''
      },
      selectedRow: [],
      instructionForm: {
        applicationNumber: '', // 申请编号
        applicationDepartment: '', // 申请部门
        personLiable: '', // 责任人
        controlledApplicationDescription: '', // 受控申请说明
      }
    }
  },
  mounted() {
    this.getAllDevice();
    this.getList()
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal) {
        this.getList()
        this.clickSidebar(newVal)
      }
    },
    dialogVisible(newVal) {
      if (newVal === false) {
        this.instructionForm = {}
        this.documentTableData = []
        this.fileList1 = []
      }
    }
  },
  methods: {
    approval(row) {
      this.$confirm('是否审批通过！', '提示', {
        confirmButtonText: '通过',
        cancelButtonText: '不通过',
        type: 'warning'
      }).then(() => {
        this.approvalFun(row.id, true)
      }).catch(() => {
        this.approvalFun(row.id, false)
      });
    },
    approvalFun(id, status) {
      approvalOfHomeworkInstructionManual({ id, status }).then(res => {
        this.getList()
        this.$message({
          type: 'success',
          message: '操作成功!'
        });
      })

    },
    getRowKey(row) {
      return row.index
    },
    deleteHomeworkGuidebook(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        homeworkGuidebook({
          id: row.id,
          instructionId: row.instructionId
        }).then(res => {
          this.$message.success('删除成功！')
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    downloadFile(fileName) {
      let state = /\.(jpg|jpeg|png|gif)$/i.test(fileName)
      if (state) {
        let url = this.javaApi + '/img/' + fileName;
        fileDownload.downloadIamge(url, fileName)
      } else {
        const url = this.javaApi + '/word/' + fileName
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        link.click();
        this.$message.success('下载成功')
      }
    },
    instructionEditFun(row) {
      this.dialogVisible = true
      homeworkGuidebookEditor({ instructionId: row.instructionId }).then(res => {
        if (res.code == 200) {
          this.instructionForm = res.data.instruction;
          if (this.instructionForm.fileSystemName) {
            this.fileList1.push({ name: this.instructionForm.fileName })
          }
          this.documentTableData = res.data.list;
          // 删除用
          this.documentTableData.forEach((v, k) => {
            v.index = k
          })
        }
      })
    },
    handleSizeChange(val) {
      this.search.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getList()
    },
    submitFun() {
      this.$refs.form1.validate((valid) => {
        if (valid) {
          this.instructionForm.feTempHumRecordList = this.documentTableData
          newHomeworkGuidebookAdded(this.instructionForm).then(res => {
            if (res.code == 200) {
              this.$message.success('操作成功！')
              this.dialogVisible = false
              this.getList()
            }
          })
        } else {
          return false;
        }
      });
    },
    clickSidebar(clickNodeVal) {
      this.laboratoryNameIsNull = false
      // 是否存在value，存在value代表为三级
      if (!clickNodeVal.value) {
        this.list = [];
        this.entity.laboratoryName = null
        this.entity.storagePoint = null
        // 等于1代表为树的一级，label为部门
        if (clickNodeVal.label === '其他') {
          this.laboratoryNameIsNull = true
          this.getAllDevice()
          return
        }
        if (clickNodeVal.level === 1) {
          this.entity.laboratoryName = clickNodeVal.label
          // 等于二级。label为存储地点
        } else if (clickNodeVal.level === 2) {
          // 其他表示没有配置实验室，只配置了地点
          if (clickNodeVal.parent.label === '其他') {
            this.laboratoryNameIsNull = true
          } else {
            this.entity.laboratoryName = clickNodeVal.parent.label
          }
          this.entity.storagePoint = clickNodeVal.label
        }
        this.getAllDevice()
      }
    },
    // 新增
    addFile() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.tableDataIndex !== undefined) {
            this.$set(this.documentTableData, this.tableDataIndex, this.form)
            this.dialogVisible1 = false;
            this.$message.success('修改成功');
            console.log('this.documentTableData---edit', this.documentTableData[this.tableDataIndex])
          } else {
            this.documentTableData.push(this.form);
            this.dialogVisible1 = false;
            this.$message.success('添加成功');
          }
        } else {
          return false;
        }
      });
    },
    openAddDia() {
      this.dialogVisible1 = true
      this.tableDataIndex = undefined;
      this.form = {}
      this.form.deviceName = this.clickNodeVal.label
      this.onDeviceNameChange()
      this.fileList = []

    },
    editFun(row, index) {
      this.form = { ...row }
      this.tableDataIndex = index;
      console.log('this.tableDataIndex', this.tableDataIndex)
      // 回显列表
      if (this.form.fileSystemName) {
        this.fileList.push({ name: this.form.fileName })
      }
      this.dialogVisible1 = true
    },
    closeDia() {
      this.dialogVisible1 = false
      this.$refs['form'].resetFields()
      this.fileList = []
    },
    handleSelectionChange(selected) {
      this.selectedRow = selected
    },
    async delFile() {
      if (this.selectedRow.length === 0) {
        this.$message.error('请选择要删除的选项');
        return;
      }
      // 删除保存好的数据
      let ids = this.selectedRow
        .filter(item => item.id !== undefined)
        .map(item => item.id).join(',');
      //有错误
      const res = await deleteHomeworkGuidebook({ ids });
      if (res.code === 200) {
        // 删除视图的数据
        for (const resKey in this.selectedRow) {
          this.documentTableData = this.documentTableData.filter(item => item.index === this.selectedRow[resKey].index)
        }
        this.$message.success({
          message: '删除成功',
          type: 'success'
        });
      }
    },
    onSuccess(response, file, fileList) {
      this.form.fileName = file.name;
      this.form.fileSystemName = response.data;
    },
    onSuccess1(response, file, fileList) {
      this.instructionForm.fileName = file.name;
      this.instructionForm.fileSystemName = response.data;
    },
    onError(error, file, fileList) {
      this.$message.error('上传失败:', error, file, fileList);
    },
    handleRemove(file, fileList) {
      this.form.fileName = ''
      this.form.fileSystemName = ''
    },
    handleRemove1(file, fileList) {
      this.form.fileName = ''
      this.form.fileSystemName = ''
    },
    beforeRemove(file) {
      return this.$confirm(`确定移除 ${file.name}？`)
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件。`);
    },
    getList() {
      pageByPageQueryOfHomeworkInstructions({ ...this.search }).then(res => {
        if (res.code == 200) {
          this.tableData = res.data.records;
          this.search.total = res.data.total
        }
      })
    },
    getAllDevice() {
      selectDeviceParameter({ laboratoryNameIsNull: this.laboratoryNameIsNull }).then(res => {
        if (res.code == 200) {
          this.devices = res.data.records;
          this.updateDeviceNameOptions();
        }
      })
    },
    // 更新设备名称下拉框的选项
    updateDeviceNameOptions() {
      this.deviceNameOption = this.devices.map(device => ({
        value: device.deviceName,
        label: device.deviceName
      }));
    },
    // 设备名称改变时触发
    onDeviceNameChange() {
      // 根据选中的设备名称，更新管理编号下拉框的选项
      const selectedDevice = this.devices.find(device => device.deviceName === this.form.deviceName);
      if (selectedDevice) {
        this.form.deviceNumber = selectedDevice.managementNumber;
        this.form.deviceModel = selectedDevice.specificationModel;
        this.form.deviceId = selectedDevice.id
      }
    },
  },
  computed: {
    action() {
      return this.javaApi + '/personBasicInfo/saveCNASFile'
    }
  },
}
</script>

<style scoped>
h4 {
  font-weight: 400;
  font-size: 16px;
  display: flex;
  justify-content: flex-end;
  margin: 10px 0;
}
</style>
