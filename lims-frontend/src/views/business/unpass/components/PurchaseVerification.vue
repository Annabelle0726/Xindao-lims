<template>
  <div>
    <el-dialog title="进货验证原始记录" :visible.sync="isShow" width="70%" :show-close="false" :close-on-click-modal="false"
               :modal-append-to-body="false" :close-on-press-escape="false">
      <div class="search">
        <el-form :inline="true" :model="purchaseForm" label-position="right" :rules="purchaseFormRules"
          ref="purchaseForm" class="form-inline" label-width="120px">
          <el-row>
            <el-col :span="8">
              <el-form-item label="检验编号:">
                <el-input clearable v-model="purchaseForm.entrustCode" disabled size="small"
                  placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="材料厂家:">
                <el-input clearable v-model="purchaseForm.supplierName" disabled size="small"
                  placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="到货日期:">
                <el-input clearable v-model="purchaseForm.declareDate" disabled size="small"
                  placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="材料名称:">
                <el-input clearable v-model="purchaseForm.sample" disabled size="small" placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="规格型号:">
                <el-input clearable v-model="purchaseForm.model" disabled size="small" placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="材料批号:">
                <el-input clearable v-model="purchaseForm.updateBatchNo" disabled size="small"
                  placeholder="请输入"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <div>
            <el-form-item class="headLine" label="验证依据文件编号:" label-width="160px" style="width: 100%"
              prop="verifyFileCode">
              <el-input clearable v-model="purchaseForm.verifyFileCode" size="small"
                :disabled="operationType === 'view'" placeholder="请输入"></el-input>
            </el-form-item>
          </div>
          <h4>
            <span style="display: flex;align-items: center;">
              <span class="line"></span><span>验证内容</span>
            </span>
          </h4>
          <div class="title">一、供方产品检测报告的基本信息确认</div>
          <el-form-item label="材料名称:" prop="basicName">
            <el-radio-group v-model="purchaseForm.basicName" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="规格型号:" prop="basicModel">
            <el-radio-group v-model="purchaseForm.basicModel" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="材料批号:" prop="basicBatchNo">
            <el-radio-group v-model="purchaseForm.basicBatchNo" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="执行标准:" prop="basicStandard">
            <el-radio-group v-model="purchaseForm.basicStandard" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="生产日期:" prop="basicDate">
            <el-radio-group v-model="purchaseForm.basicDate" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="供货数量:" prop="basicNumber">
            <el-radio-group v-model="purchaseForm.basicNumber" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="材料颜色:" prop="basicColor">
            <el-radio-group v-model="purchaseForm.basicColor" :disabled="operationType === 'view'">
              <el-radio label="1">符合</el-radio>
              <el-radio label="2">不符合</el-radio>
              <el-radio label="3">不适用</el-radio>
            </el-radio-group>
          </el-form-item>
          <div>
            <el-form-item label="其他:">
              <el-input clearable v-model="purchaseForm.basicOtherValue" size="small"
                :disabled="operationType === 'view'" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item>
              <el-radio-group v-model="purchaseForm.basicOther" :disabled="operationType === 'view'">
                <el-radio label="1">符合</el-radio>
                <el-radio label="2">不符合</el-radio>
              </el-radio-group>
            </el-form-item>
            <div class="titleSec">
              <span>二、供方产品检测报告中各项性能指标的验证</span>
              <span>
                <el-button type="primary" size="small" @click="addItem"
                  v-if="operationType === 'add'">手动添加检验项目</el-button>
                <el-button type="primary" @click="addFactoryVerifyItem" v-if="operationType === 'add'"
                  size="small">添加验证项目</el-button>
              </span>
            </div>
            <div>
              <el-table :data="factoryVerifyItemList" style="width: 98%" v-loading="tableLoading"
                        :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
                <el-table-column type="index" label="序号" width="180" align="center"></el-table-column>
                <el-table-column prop="inspectionItem" label="验证项目" width="300"></el-table-column>
                <el-table-column prop="result" label="验证结果">
                  <template slot-scope="scope">
                    <el-radio-group v-model="scope.row.result" :disabled="operationType === 'view'">
                      <el-radio label="1">符合</el-radio>
                      <el-radio label="2">不符合</el-radio>
                      <el-radio label="3">不适用</el-radio>
                    </el-radio-group>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="120" v-if="operationType === 'add'">
                  <template slot-scope="scope">
                    <el-button @click.native.prevent="deleteRow(scope.$index)" type="text" size="small">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('resetPurchaseDialog')">取 消</el-button>
        <el-button type="primary" @click="handlePurchase" :loading="handlePurchaseLoading" v-if="operationType === 'add'">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="待验证项目" :visible.sync="factoryVerifyItemDia" width="1000px"
      style="max-height: 96vh; margin-top: 2vh" :show-close="false" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <el-table :data="tableList" style="width: 100%" v-loading="tableLoading"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
        <el-table-column prop="inspectionItem" label="验证项目"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="factoryVerifyItemDia = false">取 消</el-button>
        <el-button type="primary" @click="handleFactoryVerifyItem" :loading="handleFactoryVerifyLoading">添 加</el-button>
      </span>
    </el-dialog>
    <el-dialog title="手动添加验证项目" :visible.sync="inspectionItemDia" width="500px" @close="resetInput" :show-close="false"
      :close-on-click-modal="false" :close-on-press-escape="false">
      <el-input v-model="inspectionItem"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="inspectionItemDia = false">取 消</el-button>
        <el-button type="primary" @click="handleAdd">添 加</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getFactoryVerify, addFactoryVerify } from '@/api/business/unpass.js'
import {
  getInsProduct,
} from "@/api/business/inspectionTask.js";
export default {
  name: "PurchaseVerification",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    purchaseDialog: {
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
      info: {},
      isShow: this.purchaseDialog,
      purchaseForm: {
        entrustCode: '', // 检验编号
        supplierName: '', // 材料厂家
        declareDate: '', // 到货日期
        sample: '', // 材料名称
        model: '', // 规格型号
        updateBatchNo: '', // 材料批号
        verifyFileCode: '', // 验证依据文件编号
        // 确认信息
        basicName: '', // 材料名称
        basicModel: '', // 规格型号
        basicBatchNo: '', // 材料批号
        basicStandard: '', // 执行标准
        basicDate: '', // 生产日期
        basicNumber: '', // 供货数量
        basicColor: '', // 材料颜色
        basicOtherValue: '', // 其他名称
        basicOther: '', // 其他
      },
      purchaseFormRules: {
        verifyFileCode: [{ required: true, message: '请填写验证依据文件编号', trigger: 'blur' }],
        basicName: [{ required: false, message: '请选择材料名称验证信息', trigger: 'change' }],
        basicModel: [{ required: false, message: '请选择规格型号验证信息', trigger: 'change' }],
        basicStandard: [{ required: false, message: '请选择执行标准验证信息', trigger: 'change' }],
        basicDate: [{ required: false, message: '请选择生产日期验证信息', trigger: 'change' }],
        basicNumber: [{ required: false, message: '请选择供货数量验证信息', trigger: 'change' }],
        basicColor: [{ required: false, message: '请选择材料颜色验证信息', trigger: 'change' }],
      },
      factoryVerifyItemList: [], // 供方产品检测报告中各项性能指标的验证
      tableList: [], //
      multipleSelection: [], //
      operationType: '',
      handlePurchaseLoading: false,
      tableLoading: false,
      factoryVerifyItemDia: false, // 添加验证项目弹框
      handleFactoryVerifyLoading: false,
      inspectionItemDia: false,
      inspectionItem: ''
    }
  },
  // 方法集合
  methods: {
    async getInsOrder(operationType, item) {
      this.operationType = operationType
      this.info = item
      // 查询已提交的数据
      let factoryVerify = await getFactoryVerify({ insOrderId: this.orderId })
      this.purchaseForm = factoryVerify.data
      this.factoryVerifyItemList = factoryVerify.data.factoryVerifyItemList || []
    },
    // 打开添加验证项目弹框
    async addFactoryVerifyItem() {
      this.factoryVerifyItemDia = true
      this.tableList = []
      if (this.info.cableTag === undefined) {
        this.info.cableTag = ''
      }
      this.tableLoading = true;
      const type1 = 5
      // 查询检验项目
      let res = await getInsProduct({ id: this.info.id, type: type1, laboratory: this.info.sonLaboratory, cableTag: this.info.cableTag, rawMaterialTag: this.info.rawMaterialTag })
      if (res.data.length > 0) {
        res.data.map((item) => {
          if (item.inspectionItemSubclass) {
            item.inspectionItem = item.inspectionItem + '-' + item.inspectionItemSubclass
          }
          const obj = Object.assign({
            inspectionItem: item.inspectionItem,
            result: item.result,
          })
          this.tableList.push(obj)
        })
        this.tableLoading = false;
      }
    },
    handleSelectionChange(val) {
      console.log('val---', val)
      this.multipleSelection = val;
    },
    addItem() {
      this.inspectionItemDia = true
    },
    deleteRow(index) {
      this.factoryVerifyItemList.splice(index, 1);
    },
    handleAdd() {
      if (!this.inspectionItem) {
        this.$message.warning('请填写内容')
        return
      }
      this.factoryVerifyItemList.push({
        inspectionItem: this.inspectionItem,
        result: null,
      })
      this.inspectionItemDia = false
    },
    resetInput() {
      this.inspectionItem = ''
      this.inspectionItemDia = false
    },
    // 提交添加要验证的检验项
    handleFactoryVerifyItem() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择数据')
      } else {
        this.factoryVerifyItemList = JSON.parse(JSON.stringify(this.multipleSelection))
        this.factoryVerifyItemDia = false
      }
    },
    handlePurchase() {
      this.$refs['purchaseForm'].validate((valid) => {
        if (valid) {
          if (this.factoryVerifyItemList.some(item => item.result === undefined || item.result === null || item.result === '')) {
            this.$message.warning('请选择供方产品验证结果')
            return
          }
          this.factoryVerifyItemList.forEach((item, index) => {
            item.sort = index
          })
          this.purchaseForm.factoryVerifyItemList = JSON.parse(JSON.stringify(this.factoryVerifyItemList))
          this.handlePurchaseLoading = true
          addFactoryVerify(this.purchaseForm).then(res => {
            if (res.code === 200) {
              this.$message.success('提交成功')
              this.$emit('resetPurchaseDialog')
            }
            this.handlePurchaseLoading = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
  },
}
</script>

<style scoped>
.headLine>>>.el-form-item__content {
  width: 68%;
}
>>>.el-form-item__content {
  display: inline-block;
}

h4 .line {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: #3A7BFA;
  margin-right: 4px;
}

.title {
  margin: 10px 0;
}

.titleSec {
  margin: 10px 0;
  display: flex;
  justify-content: space-between;
}

>>>.el-dialog__body {
  max-height: 600px;
  overflow-y: auto;
}
</style>
