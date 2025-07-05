<template>
  <el-dialog
    :visible.sync="dialogVisible"
    title="添加耗材项目信息"
    width="70%"
  >
    <el-form ref="modelForm" :model="model" :rules="rules" label-width="100px">
      <el-col :span="12">
        <el-form-item label="耗材类型" prop="consumablesType">
          <el-select v-model="model.consumablesType" placeholder="请选择耗材类型" size="small" style="width: 100%">
            <el-option v-for="(v, i) in dict.type.consumables_type" :key="i" :label="v.label" :value="v.value"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="货号">
          <el-input v-model="model.itemNumber" placeholder="请输入货号" size="small"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="耗材名称">
          <el-input v-model="model.consumablesName" placeholder="请输入耗材名称" size="small"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="规格">
          <el-input v-model="model.specifications" placeholder="请输入规格" size="small"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="计量单位">
          <el-input v-model="model.unit" placeholder="请输入计量单位" size="small"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="参考价格">
          <el-input-number v-model="model.referencePrice" :min="0" :precision="2" :step="0.01" placeholder="请输入参考价格"
                           size="small"></el-input-number>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="存放位置">
          <el-select v-model="model.contentId" placeholder="请选择存放位置" size="small" style="width: 100%">
            <el-option v-for="(v, i) in procurementSuppliesContentOptions" :key="i" :label="v.nodeName"
                       :value="v.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="负责人">
          <el-select v-model="model.personInCharge" placeholder="请选择负责人" size="small" style="width: 100%">
            <el-option v-for="(v, i) in userOptions" :key="i" :label="v.name" :value="v.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="库存上限">
          <el-input-number v-model="model.upperLimit" :min="0" :step="1" placeholder="请输入库存上限" size="small"></el-input-number>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="库存下限">
          <el-input-number v-model="model.lowerLimit" :min="0" :step="1" placeholder="请输入库存下限" size="small"></el-input-number>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="供应商">
          <el-select v-model="model.supplier" placeholder="请选择供应商" size="small" style="width: 100%">
            <el-option v-for="(v, i) in supplierOptions" :key="i" :label="v.supplierName"
                       :value="v.supplierManagementId"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="耗材图标">
          <div class="rows">
            <el-input v-model="model.consumablesIcon" placeholder="请输入耗材图标" size="small" style="width: 90%;"/>
            <el-upload
              ref="upload"
              :action="action"
              :headers="uploadHeader"
              :on-success="onSuccessIcon"
              :show-file-list="false"
              style="float: left; margin: 0 12px 0 20px;"
            >
              <el-button slot="trigger" class="uploadFile" size="small" type="primary">浏览</el-button>
            </el-upload>
          </div>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="耗材附件">
          <div class="rows">
            <el-input v-model="model.attachment" placeholder="请输入耗材附件" size="small" style="width: 90%;"/>
            <el-upload
              ref="upload"
              :action="action"
              :on-success="onSuccessFile"
              :show-file-list="false"
              :headers="uploadHeader"
              style="float: left; margin: 0 12px 0 20px;"
            >
              <el-button slot="trigger" class="uploadFile" size="small" type="primary">浏览</el-button>
            </el-upload>
          </div>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注">
          <el-input
            v-model="model.remark"
            :rows="2"
            placeholder="请输入备注"
            size="small"
            type="textarea"
          >
          </el-input>
        </el-form-item>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
            <el-button @click="resetForm">取 消</el-button>
            <el-button type="primary" @click="submit">保 存</el-button>
        </span>
  </el-dialog>
</template>

<script>
import {
  addProcurementSuppliesList,
  updateProcurementSuppliesList,
  selectSupplierManagementAll,
  getProcurementSuppliesContentsNodeNames,
  selectUserCondition
} from '@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro'
export default {
  data() {
    return {
      dialogVisible: false,
      model: {
        consumablesType: undefined,
        consumablesIcon: undefined,
        attachment: undefined,
      },
      procurementSuppliesContentOptions: [],
      userOptions: [],
      supplierOptions: [],
      rules: {
        consumablesType: [{ required: true, message: '请选择耗材类型', trigger: 'blur' }],
      },
    }
  },
  dicts: ["consumables_type"],
  props: {
    contentsId: {
      type: Number,
      required: true,
    }
  },
  computed: {
    action() {
      return this.javaApi + '/personBasicInfo/saveCNASFile'
    }
  },
  mounted() {
    this.findProcurementSuppliesContentOptions()
    this.findUserOptions()
    this.findSupplierOptions()
  },
  methods: {
    // 重置表单
    resetForm() {
      this.$refs.modelForm.resetFields();
      this.dialogVisible = false
    },
    openDialog(row) {
      if (row) {
        console.log("row", row)
        this.model = row
      } else {
        this.model = {}
      }
      this.dialogVisible = true
    },
    findProcurementSuppliesContentOptions() {
      getProcurementSuppliesContentsNodeNames().then(res => {
        if (res.code === 200) {
          this.procurementSuppliesContentOptions = res.data
        }
      })
    },
    findUserOptions() {
      selectUserCondition().then(res => {
        if (res.code === 200) {
          this.userOptions = res.data
        }
      })
    },
    findSupplierOptions() {
      selectSupplierManagementAll().then(res => {
        if (res.code === 200) {
          this.supplierOptions = res.data
        }
      })
    },
    async onSuccessIcon(response) {
      this.$set(this.model, "consumablesIcon", response.data)
    },
    async onSuccessFile(response) {
      this.$set(this.model, "attachment", response.data)
    },
    submit() {
      this.$refs.modelForm.validate((valid) => {
        if (valid) {
          if (this.model.id) {
            updateProcurementSuppliesList(this.model).then(res => {
              if (res.code === 200) {
                this.dialogVisible = false
              }
            })
          } else {
            addProcurementSuppliesList(this.model).then(res => {
              if (res.code === 200) {
                this.dialogVisible = false
                this.$emit('submit')
              }
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}
>>> .el-dialog__body {
  max-height: 42em;
  overflow-y: auto;
}
.rows {
  width: 100%;
  display: flex;
  justify-content: space-between;
}
>>>.el-form-item__content {
  line-height: 39px;
}
</style>
