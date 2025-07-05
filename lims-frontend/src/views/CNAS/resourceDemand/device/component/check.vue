<!-- 设备核查 -->
<template>
  <div>
    <div class="btns">
      <el-button size="small" type="primary" @click="calibrationMaintenance()">核查项目维护</el-button>
      <el-button size="small" type="primary" @click="add('add')">添加核查记录</el-button>
      <el-button size="small" type="primary">导出Excel</el-button>
    </div>
    <div class="tables" style="margin-top: 16px;">
      <el-table :data="tableData" height="calc(100vh - 20em)" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column label="序号" type="index" width="120">
          <template v-slot="scope">
            <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="记录编号" min-width="150" prop="processNumber"></el-table-column>
        <el-table-column label="核查机构" min-width="150" prop="calibrationInstitution"
          show-overflow-tooltip></el-table-column>
        <el-table-column label="核查结论" min-width="150" prop="status">
          <template v-slot="scope">
            {{ scope.row.status === '0yes' ? '合格' : scope.row.status === '1no' ? '不合格' : '其他' }}
          </template>
        </el-table-column>
        <el-table-column label="核查证书编号" min-width="150" prop="certificateSerialNumber"></el-table-column>
        <el-table-column label="说明" min-width="150" prop="remark" show-overflow-tooltip></el-table-column>
        <el-table-column label="核查日期" min-width="150" prop="calibrationDate"></el-table-column>
        <el-table-column label="下次核查日期" min-width="150" prop="nextCalibrationDate"></el-table-column>
        <el-table-column label="登记人" min-width="150" prop="createUser"></el-table-column>
        <el-table-column label="登记日期" min-width="150" prop="createTime"></el-table-column>
        <el-table-column fixed="right" label="操作" min-width="150">
          <template #default="{ row }">
            <el-button size="small" type="text" @click="handleAttachmentClick(row)">附件</el-button>
            <el-button size="small" type="text" @click="handleViewClick('view', row)">查看</el-button>
            <el-button size="small" type="text" @click="handleDeleteClick(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
        :total="search.total" layout="->,total, sizes, prev, pager, next, jumper" style="margin-right: 5%;"
        @size-change="handleSizeChange" @current-change="handleCurrentChange">
      </el-pagination>
    </div>

    <!-- 校准项目维护 -->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible0"
      title="核查项目维护" top="5vh" width="70%">
      <h4>
        <span style="display: flex;align-items: center;"><span class="line"></span><span>设备核查参数维护</span></span>
        <el-button :loading="addCalibrateLoading" size="small" type="primary" @click="addCalibrate">添 加</el-button>
      </h4>
      <div>
        <el-form ref="form0" :model="form0" :rules="form0Rules" label-position="right" label-width="120px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="核查参数:" prop="measurementParameter">
                <el-input v-model="form0.measurementParameter" clearable placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="核查范围:" prop="rangeOfMeasurement">
                <el-input v-model="form0.rangeOfMeasurement" clearable placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最大允许误差:" prop="maxPermissibleError">
                <el-input v-model="form0.maxPermissibleError" clearable placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="判定标准:" prop="judgmentCriteria">
                <el-input v-model="form0.judgmentCriteria" clearable placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <h4>
        <span style="display: flex;align-items: center;"><span class="line"></span><span>设备核查参数</span></span>
      </h4>
      <!-- 设备校准参数表格 -->
      <el-table ref="calibrateTable" v-loading="calibrateParamsLoading" :data="calibrateParams" max-height="450" stripe
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
        style="width: 100%">
        <el-table-column label="编号" type="index" width="80"></el-table-column>
        <el-table-column label="核查参数" prop="measurementParameter"></el-table-column>
        <el-table-column label="核查范围" prop="rangeOfMeasurement"></el-table-column>
        <el-table-column label="最大允许误差" prop="maxPermissibleError"></el-table-column>
        <el-table-column label="判定标准" prop="judgmentCriteria"></el-table-column>
        <el-table-column label="创建人" prop="createdBy"></el-table-column>
        <el-table-column label="创建时间" prop="creationTime"></el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 添加核查记录 -->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="dialogVisible1"
      title="添加核查记录" top="5vh" width="80%" @close="resetcalibrationRecord">
      <div class="dialog-content">
        <h4>
          <span style="display: flex;align-items: center;">
            <span class="line"></span>
            <span>添加设备核查记录</span>
          </span>
        </h4>
        <el-form ref="calibrationRecord" :model="calibrationRecord" :rules="formRules" label-position="right"
          label-width="120px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="核查人:" prop="unitOfMeasure">
                <el-input v-model="calibrationRecord.unitOfMeasure" :disabled="operationType === 'view'"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="核查日期:" prop="calibrationDate">
                <el-date-picker v-model="calibrationRecord.calibrationDate" :disabled="operationType === 'view'"
                  format="yyyy-MM-dd" placeholder="选择日期" size="small" style="width: 90%" type="date"
                  value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="下次核查日期:" label-width="140px" prop="nextCalibrationDate">
                <el-date-picker v-model="calibrationRecord.nextCalibrationDate" :disabled="operationType === 'view'"
                  :picker-options="{ disabledDate: this.disabledDate }" format="yyyy-MM-dd" placeholder="选择日期"
                  size="small" style="width: 90%" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="核查器具:" prop="calculatingApparatus">
                <el-input v-model="calibrationRecord.calculatingApparatus" :disabled="operationType === 'view'"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="核查标准量程:" prop="standardRange">
                <el-input v-model="calibrationRecord.standardRange" :disabled="operationType === 'view'" size="small"
                  style="width: 90%"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="核查标准不确定度:" label-width="140px" prop="calibrationStandardUncertainty">
                <el-input v-model="calibrationRecord.calibrationStandardUncertainty"
                  :disabled="operationType === 'view'" size="small" style="width: 90%"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="核查指导书:" prop="byDocument">
                <el-input v-model="calibrationRecord.byDocument" :disabled="operationType === 'view'"
                  size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="检查报告编号:" prop="certificateSerialNumber">
                <el-input v-model="calibrationRecord.certificateSerialNumber" :disabled="operationType === 'view'"
                  size="small" style="width: 90%"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="核查总结论:" label-width="140px" prop="status">
                <el-radio-group v-model="calibrationRecord.status" :disabled="operationType === 'view'">
                  <el-radio label="0yes">合格</el-radio>
                  <el-radio label="1no">不合格</el-radio>
                  <el-radio label="2other">其他</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="23">
              <el-form-item label="附件：" prop="fileName">
                <el-input v-model="calibrationRecord.fileName"
                  :style="`width: ${operationType === 'add' ? '90%' : '100%'};}`" disabled size="small">
                  <el-button v-if="operationType === 'add'" slot="append" icon="el-icon-delete-solid"
                    @click="deleteFile"></el-button>
                </el-input>
                <el-upload v-if="operationType === 'add'" ref="upload" :action="action" :before-upload="beforeUpload"
                  :headers="uploadHeader" :limit="1" :on-error="onError" :on-success="handleSuccessUp"
                  :show-file-list="false" style="float: right;">
                  <el-button :loading="upLoading" size="small" style="position: relative; top: -4px;"
                    type="primary">附件上传
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="备注:">
                <el-input v-model="calibrationRecord.remark" :disabled="operationType === 'view'" :rows="3" size="small"
                  style="width: 96%" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <h4>
          <span style="display: flex;align-items: center;">
            <span class="line"></span><span>核查条目确认结果</span>
          </span>
        </h4>
        <el-table ref="calibrateTable" :data="calibrateParams" stripe style="width: 100%;">
          <el-table-column label="编号" type="index" width="80"></el-table-column>
          <el-table-column label="核查参数" prop="measurementParameter" width="150"></el-table-column>
          <el-table-column label="核查范围" prop="rangeOfMeasurement" width="150"></el-table-column>
          <el-table-column label="最大允许误差" prop="maxPermissibleError" width="150"></el-table-column>
          <el-table-column label="判定标准" prop="judgmentCriteria" width="150"></el-table-column>
          <el-table-column label="是否核查" prop="isCalibration">
            <template slot="header" slot-scope="scope">
              <span class="required-span">* </span>是否核查
            </template>
            <template slot-scope="scope">
              <el-radio-group v-model="scope.row.isCalibration" :disabled="operationType === 'view'">
                <el-radio label="0yes">是</el-radio>
                <el-radio label="1no">否</el-radio>
              </el-radio-group>
            </template>
          </el-table-column>
          <el-table-column label="判定结果" prop="result">
            <template slot="header" slot-scope="scope">
              <span class="required-span">* </span>判定结果
            </template>
            <template slot-scope="scope">
              <el-radio-group v-model="scope.row.result" :disabled="operationType === 'view'" @input="checkRadio()">
                <el-radio label="0yes">合格</el-radio>
                <el-radio label="1no">不合格</el-radio>
                <el-radio label="2other">其他</el-radio>
              </el-radio-group>
            </template>
          </el-table-column>
          <el-table-column label="单项结果说明" prop="singleResultStatement">
            <template slot-scope="scope">
              <el-input v-model="scope.row.singleResultStatement" :disabled="operationType === 'view'"
                size="small"></el-input>
            </template>
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer" style="float: right;margin-top: 10px">
          <el-button v-if="operationType === 'add'" @click="dialogVisible1 = false">取 消</el-button>
          <el-button v-if="operationType === 'add'" type="primary" @click="addRecord">确 定</el-button>
        </span>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import file from '@/utils/file';
import { mapGetters } from "vuex";
export default {
  components: {},
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {
    return {
      search: {
        size: 20,
        current: 1,
        total: 0
      },
      calibrateParamsLoading: false,
      addCalibrateLoading: false,
      form0: {
        measurementParameter: '', // 计量参数
        rangeOfMeasurement: '',
        maxPermissibleError: '',
        judgmentCriteria: '',
        createdBy: '',
        action: '',
        deviceId: null
      },
      form0Rules: {
        measurementParameter: [
          { required: true, message: '请输入计量参数', trigger: 'blur' }
        ],
        rangeOfMeasurement: [
          { required: true, message: '请输入量程范围', trigger: 'blur' }
        ],
        maxPermissibleError: [
          { required: true, message: '请输入最大允许误差', trigger: 'blur' }
        ],
        judgmentCriteria: [
          { required: true, message: '请输入判定标准', trigger: 'blur' }
        ],
      },
      dialogVisible0: false,
      calibrationRecord: {
        unitOfMeasure: '', // 核查人
        calibrationDate: '', // 核查日期
        nextCalibrationDate: '', // 下次核查日期
        calculatingApparatus: '', // 核查器具
        standardRange: '', // 核查标准量程
        calibrationStandardUncertainty: '', // 核查标准不确定度
        byDocument: '', // 核查指导书
        certificateSerialNumber: '', // 检查报告指导书
        fileName: '', // 附件
        status: '', // 核查总结论
        remark: '', // 备注
      },
      formRules: {
        unitOfMeasure: [{ required: true, message: '请输入核查人', trigger: 'blur' }],
        calibrationDate: [{ required: true, message: '请选择核查日期', trigger: 'change' }],
        nextCalibrationDate: [{ required: true, message: '请选择下次核查日期', trigger: 'change' }],
        calculatingApparatus: [{ required: true, message: '请输入核查器具', trigger: 'blur' }],
        standardRange: [{ required: true, message: '请输入核查标准量程', trigger: 'blur' }],
        calibrationStandardUncertainty: [{ required: true, message: '请输入核查标准不确定度', trigger: 'blur' }],
        byDocument: [{ required: true, message: '请输入核查指导书', trigger: 'blur' }],
        certificateSerialNumber: [{ required: true, message: '请输入检查报告指导书', trigger: 'blur' }],
        status: [{ required: true, message: '请选择核查总结论', trigger: 'change' }]
      },
      calibrateParams: [],
      tableData: [],
      operationType: '',
      dialogVisible1: false,
      formData: {
        person: '',
        data1: '',
        name: '',
      },
      upLoading: false
    }
  },
  // 用于上传文件的信息
  computed: {
    ...mapGetters(["nickName"]),
    action() {
      return this.javaApi + this.$api.personnel.saveCNASFile
    }
  },
  mounted() {
    this.getTableList(this.clickNodeVal.value) // 获取设备核查列表数据
  },
  methods: {
    //状态判定
    checkRadio() {
      let resultList = this.calibrateParams.map(ele => ele.result)
      if (resultList && resultList.filter(ele => ele == '1no').length > 0) {
        this.calibrationRecord.status = '1no'
      } else if (resultList && resultList.filter(ele => ele == '2other').length == resultList.length) {
        this.calibrationRecord.status = '2other'
      } else if (resultList && resultList.filter(ele => ele == '0yes').length == resultList.length) {
        this.calibrationRecord.status = '0yes'
      }
    },
    //下次核查日期:禁用在核查日期前的日期
    disabledDate(time) {
      let selectDate = this.calibrationRecord.calibrationDate
      if (selectDate) {
        let oldDate = new Date(selectDate)
        return time <= oldDate.getTime()
      }
      return false
    },
    handleSizeChange(val) {
      this.search.size = val
      this.getTableList(this.clickNodeVal.value);
    },
    handleCurrentChange(val) {
      this.search.current = val
      this.getTableList(this.clickNodeVal.value);
    },
    // 获取设备校准参数table信息
    async getXmsg() {
      this.calibrateParamsLoading = true
      try {
        await this.$axios.get(this.$api.deviceCheck.selectDeviceMetric + "?deviceId=" + this.clickNodeVal.value + "&type=examine").then(res => {
          if (res.code == 200) {
            this.calibrateParams = res.data
          }
          this.calibrateParamsLoading = false
        })
      } catch (e) {
        this.calibrateParamsLoading = false
      }
    },
    // 校准项目维护
    calibrationMaintenance() {
      this.dialogVisible0 = true
      this.getXmsg();
    },
    addCalibrate() {
      this.$refs['form0'].validate((valid) => {
        if (valid) {
          // 保存
          this.calibrateParamsLoading = true
          this.addCalibrateLoading = true
          this.form0.deviceId = this.clickNodeVal.value;
          this.form0.createdBy = this.nickName;
          this.form0.type = 'examine'
          this.$axios.post(this.$api.deviceCheck.saveOrUpdateDeviceMetric, this.form0, {
            headers: {
              'Content-Type': 'application/json'
            }
          }).then(res => {
            if (res.code == 200) {
              this.$message.success('保存成功')
              this.$refs['form0'].resetFields()
              this.getXmsg() // 刷新设备校准参数表格
            }
            this.calibrateParamsLoading = false
            this.addCalibrateLoading = false
          })
        } else {
          this.addCalibrateLoading = false
          this.$message.warning('有必填项未填');
        }
      })
    },
    // 查询设备核查列表
    getTableList(deviceId) {
      this.$axios.get(this.$api.deviceCheck.deviceMetricRecordPage + '?deviceId=' + deviceId + "&size=" + this.search.size + "&current=" + this.search.current + "&type=examine").then(res => {
        this.tableData = res.data.records
        this.search.total = res.data.total
      })
    },
    // 提交要添加的记录
    addRecord() {
      this.$refs['calibrationRecord'].validate((valid) => {
        if (valid) {
          try {
            if (this.calibrateParams.some(m => m.isCalibration === undefined)) {
              this.$message.error('请选择是否核查')
              return
            }
            if (!this.calibrateParams.every(m => m.result !== undefined)) {
              this.$message.error('请选择判定结果')
              return
            }
            this.addRecordLoading = true
            this.calibrationRecord.deviceId = this.clickNodeVal.value;
            this.calibrationRecord.createUser = this.nickName
            this.calibrationRecord.deviceMetricsCopyList = this.calibrateParams
            this.calibrationRecord.type = 'examine'
            this.$axios.post(this.$api.deviceCheck.addOrUpdateDeviceMetricRecord, this.calibrationRecord, {
              headers: {
                'Content-Type': 'application/json'
              }
            }).then(res => {
              if (res.code == 200) {
                this.$message.success('添加成功')
                this.dialogVisible1 = false
                this.getTableList(this.clickNodeVal.value)
              }
              this.addRecordLoading = false
            })
          } catch (e) {
            console.log('addRecord---', e)
            this.addRecordLoading = false
          }
        } else {
          this.$message.warning('有必填项未填');
        }
      })
    },
    resetcalibrationRecord() {
      this.$refs.calibrationRecord.resetFields()
    },
    // 附件上传
    handleSuccessUp(response, file) {
      if (response.code == 200) {
        // 在保存赋值新文件
        this.calibrationRecord.fileName = file.name
        this.calibrationRecord.systemFileName = response.data
        this.upLoading = false;
      } else {
        this.$message.error(response.message)
      }
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        this.upLoading = true;
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    deleteFile() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(this.$api.personnel.deleteCNASFile + "?fileName=" + this.form.systemFileName).then(res => {
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
    // end
    handleAttachmentClick(row) {
      // 模拟下载附件
      const imageUrl = this.javaApi + '/img/' + row.systemFileName; // 图片 URL
      file.downloadIamge(imageUrl, row.fileName)
      // downloadImage(imageUrl, row.fileName);
    },
    // 添加核查记录
    add(type) {
      this.operationType = type
      this.dialogVisible1 = true
      this.getXmsg()
    },
    // 查看详情
    handleViewClick(type, row) {
      this.$axios.get(this.$api.deviceCheck.showDeviceMetricsCopy + "?id=" + row.id + "&type=examine").then(res => {
        this.calibrateParams = res.data
      })
      this.calibrationRecord = { ...row }
      this.operationType = type
      this.dialogVisible1 = true
    },

    // 表格删除操作
    handleDeleteClick(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(this.$api.deviceCheck.deleteDeviceMetricRecord + "?id=" + row.id).then(res => {
          this.getTableList(this.clickNodeVal.value) // 获取设备校准列表数据
          this.$message.success('删除成功！')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(this.$api.deviceCheck.deleteDeviceMetrics + "?id=" + row.id).then(res => {
          this.getXmsg();
          this.$message.success('删除成功！')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getTableList(newVal.value);
      }
    },
  }
}

function downloadImage(url, name) {
  const link = document.createElement('a');
  link.href = url;
  link.download = name; // 文件名
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}
</script>
<style scoped>
.dialog-content {
  max-height: 70vh;
  /* 设置最大高度 */
  overflow-y: auto;
  /* 启用垂直滚动 */
}

.tables {
  width: 100%;
  height: calc(100vh - 230px);
}

.search {
  background-color: #fff;
  height: 40px;
  display: flex;
  align-items: center;
  position: relative;
}

.search_thing {
  display: flex;
  align-items: center;
  height: 40px;
}

.search_label {
  width: 70px;
  font-size: 14px;
  text-align: right;
  margin-right: 10px;
}

.search_input {
  width: calc(100% - 110px);
}

.btns {
  text-align: right;
}

h4 {
  font-weight: 400;
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
}

h4 .line {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: #3A7BFA;
  margin-right: 4px;
}

.check {
  background-color: #fff;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.check_thing {
  flex: 0 0 calc(20% - 10px);
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.check_label {
  font-size: 14px;
  margin-bottom: 5px;
}

.check_data .check_input {
  width: 100%;
}

.el-table {
  font-size: 14px;
  color: #333;
}

.el-table thead {
  background-color: #f5f5f5;
}
</style>
