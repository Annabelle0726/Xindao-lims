<template>
  <div>
    <el-dialog title="新增不合格处理" :visible.sync="isShow" width="740px" :show-close="false" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <div class="search">
        <el-form :inline="true" :model="unPassForm" :rules="unPassFormRules" ref="unPassForm" class="form-inline"
          label-width="120px">
          <div>
            <el-form-item class="headLine" label="标题:" style="width: 100%" prop="headline">
              <el-input clearable v-model="unPassForm.headline" size="small" :disabled="type === 'view'" type="textarea"
                placeholder="请输入"></el-input>
            </el-form-item>
          </div>
          <el-form-item label="供应商名称:">
            <el-input clearable v-model="unPassForm.supplierName" disabled size="small" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="物料名称:">
            <el-tooltip class="item" effect="dark" placement="top" :content="unPassForm.materialName">
              <el-input clearable v-model="unPassForm.materialName" disabled size="small" placeholder="请输入"></el-input>
            </el-tooltip>
          </el-form-item>
          <el-form-item label="生产批次:">
            <el-input clearable v-model="unPassForm.productionBatch" disabled size="small" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="到货数量:">
            <el-input clearable v-model="unPassForm.cargoQuantity" disabled size="small" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="规格型号:">
            <el-input clearable v-model="unPassForm.specsModels" disabled size="small" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="报检时间:">
            <el-date-picker v-model="unPassForm.inspectTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" size="small"
              disabled style="width: 175px" type="date" placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="反馈人:">
            <el-input clearable v-model="unPassForm.feedbackUser" disabled size="small" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="反馈时间:" prop="feedbackTime">
            <el-date-picker v-model="unPassForm.feedbackTime" :disabled="type === 'view'" format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss" size="small" style="width: 175px" type="date" placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="分类:" prop="classification">
            <el-select v-model="unPassForm.classification" :disabled="type === 'view'" size="small" placeholder="请选择">
              <el-option v-for="item in classificationOptions" :key="item.value" :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="不合格归属:" prop="offGradeAscription">
            <el-select v-model="unPassForm.offGradeAscription" :disabled="type === 'view'" size="small"
              placeholder="请选择">
              <el-option v-for="item in offGradeAscriptionOptions" :key="item.value" :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="不合格情况描述:" prop="unqualifiedDesc">
            <el-input clearable type="textarea" v-model="unPassForm.unqualifiedDesc" :disabled="type === 'view'"
              style="width: 484px" size="small" placeholder="请输入"></el-input>
          </el-form-item>
        </el-form>
        <el-upload v-if="type === 'add'" ref="upload" :action="action2" :on-change="beforeUpload" :on-error="onError"
          :on-remove="handleRemoveFile" :on-success="getUnpassUrl" :headers="uploadHeader" :file-list="unPassFilesList">
          <el-button size="small" type="primary" style="text-align: left">附件上传</el-button>
        </el-upload>
        <div v-if="type !== 'add'">
          <div style="padding-left: 40px;margin-bottom: 4px">附件：</div>
          <div style="padding-left: 40px;margin-bottom: 4px" v-for="item in unqualifiedHandlerFiles">
            <span>{{ item.fileName }}</span><el-link type="primary" style="margin-left: 20px;vertical-align: top"
              :underline="false" @click="handleDown(item)">下载</el-link>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('resetForm')">取 消</el-button>
        <el-button type="primary" @click="handlunPass" :loading="handlunPassLoading" v-if="type === 'add'">确
          定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getInsOrder, getUnqualifiedHandler, downFile, addUnqualifiedHandler } from '@/api/business/unpass.js'
import {mapGetters} from "vuex";
export default {
  name: "unPassDialog",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    unPassDialog: {
      type: Boolean,
      default: () => false
    },
    orderId: {
      type: String,
      default: () => null
    },
  },
  data() {
    // 这里存放数据
    return {
      type: '',
      isShow: this.unPassDialog,
      unPassForm: { // 不合格处理数据
        headline: '', // 标题
        inventoryQuantityId: '', // 原材料id,不做展示，只传值
        supplierName: '', // 供应商名称
        materialName: '', // 物料名称
        productionBatch: '', // 生产批次
        cargoQuantity: '', // 到货数量
        specsModels: '', // 规格型号
        inspectTime: '', // 报检时间
        feedbackUser: '', // 反馈人
        feedbackTime: '', // 反馈时间
        classification: '', // 分类
        offGradeAscription: '', // 不合格归属
        unqualifiedDesc: '', // 不合格情况描述
        unqualifiedHandlerFiles: [], // 不合格附件URL
      },
      unPassFormRules: {
        offGradeAscription: [
          { required: true, message: '请选择不合格归属', trigger: 'change' }
        ],
        headline: [
          { required: true, message: '请填写标题', trigger: 'blur' }
        ]
      },
      unPassFilesList: [], // 不合格处理附件数据
      classificationOptions: [], // 不合格分类下拉框
      offGradeAscriptionOptions: [], // 不合格归属下拉框
      handlunPassLoading: false,
      unqualifiedHandlerFiles: []
    }
  },
  mounted() {
    this.getClassificationOptions() // 获取不合格分类下拉框选项
    this.getOffGradeAscriptionOptions() // 获取不合格归属下拉框选项
  },
  // 方法集合
  methods: {
    getInsOrder(type, row) {
      this.type = type
      if (type === 'add') {
        // 赋值默认数据
        getInsOrder({
          orderId: this.orderId
        }).then(res => {
          if (res.code === 200) {
            this.unPassForm.headline = `No.0005-中天装备电线-外购品不合格反馈评审及纠正预防流程(正式版)-${this.nickName}-${new Date().toISOString().substring(0, 10)}` // 标题
            this.unPassForm.feedbackTime = new Date().toISOString().substring(0, 10) // 报检时间
            this.unPassForm.feedbackUser = this.nickName // 反馈人
            this.unPassForm.insOrderId = res.data.insOrder.id // 订单id
            this.unPassForm.materialName = res.data.insOrder.sampleType // 物料名称
            this.unPassForm.specsModels = res.data.insOrder.partDetail // 规格型号
            this.unPassForm.inventoryQuantityId = res.data.insOrderTemplate.inventoryQuantityId // 原材料id
            this.unPassForm.supplierName = res.data.insOrderTemplate.supplierName // 供应商名称
            this.unPassForm.productionBatch = res.data.insOrderTemplate.updateBatchNo // 生产批次
            this.unPassForm.cargoQuantity = res.data.insOrderTemplate.qtyArrived + res.data.insOrderTemplate.buyUnitMeas // 到货数量
            this.unPassForm.inspectTime = res.data.insOrderTemplate.sendTime.substring(0, 10)  // 报检时间
          }
        })
      } else {
        this.getInfo(row.handlerId)
        this.unPassForm = { ...row }
      }
    },
    getInfo(handlerId) {
      getUnqualifiedHandler({
        id: handlerId
      }).then(res => {
        if (res.code === 200) {
          this.unqualifiedHandlerFiles = this.HaveJson(res.data.unqualifiedHandlerFiles)
        }
      })
    },
    handleDown(row) {
      downFile({
        id: row.id,
      }).then(res => {
        this.$download.saveAs(res.data.fileUrl, row.fileName);
      }).catch(error => {

      })
    },
    // 提交不合格处理
    handlunPass() {
      this.$refs['unPassForm'].validate((valid) => {
        if (valid) {
          this.unPassForm.unqualifiedHandlerFiles.forEach(item => {
            delete item.orderBy
          })
          this.handlunPassLoading = true
          addUnqualifiedHandler(this.unPassForm).then(res => {
            if (res.code === 200) {
              this.$message.success('提交成功')
              this.$emit('resetForm')
            }
            this.handlunPassLoading = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    getUnpassUrl(response, file, fileList) {
      // 附件上传成功后，将url在提交不合格处理时传给后台
      if (response.code == 200) {
        this.$message.success('上传成功')
        this.unPassForm.unqualifiedHandlerFiles.push(response.data)
      }
    },
    // 移除附件
    handleRemoveFile(file) {
      const index = this.unPassForm.unqualifiedHandlerFiles.findIndex(val => val === file.response.data)
      if (index > -1) {
        this.unPassForm.unqualifiedHandlerFiles.splice(index, 1)
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
    getClassificationOptions() {
      // 不合格分类
      this.getDicts("categories_no_conformities").then((response) => {
        this.classificationOptions = this.dictToValue(response.data);
      });
    },
    getOffGradeAscriptionOptions() {
      // 不合格归属
      this.getDicts("attribution_no_conformities").then((response) => {
        this.offGradeAscriptionOptions = this.dictToValue(response.data);
      });
    },
  },
  computed: {
    ...mapGetters(["nickName"]),
    action2() {
      return this.javaApi + '/unqualifiedHandler/uploadFileByUnqualified'
    }
  },
}
</script>

<style scoped>
.headLine>>>.el-form-item__content {
  width: 68%;
}
</style>
