<template>
  <div>
    <el-dialog :title="operationType === 'add' ? '新增' : '编辑'" :visible.sync="editFormDia" width="60%" @close="closeDia">
      <el-form ref="editForm" :model="editForm" :rules="editFormRules" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验项：" prop="inspectionItem">
              <el-input v-model="editForm.inspectionItem" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验项EN：" prop="inspectionItemEn">
              <el-input v-model="editForm.inspectionItemEn" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验子项：" prop="inspectionItemSubclass">
              <el-input v-model="editForm.inspectionItemSubclass" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验子项EN：" prop="inspectionItemSubclassEn">
              <el-input v-model="editForm.inspectionItemSubclassEn" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验对象：" prop="sample">
              <el-cascader v-model="editForm.sample" :options="itemParameterData.cascaderField.sample.tree"
                           :props="{value:'name',label:'name',checkStrictly: true, multiple: true}"
                           :show-all-levels="false"  clearable
                           filterable placeholder="请选择" size="small" style="width: 100%;"></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价(元)：" prop="price">
              <el-input v-model="editForm.price" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="场所：" prop="laboratory">
              <el-select v-model="editForm.laboratory" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in laboratoryList" :key="item.label" :label="item.label" :value="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="试验室：" prop="sonLaboratory">
              <el-select v-model="editForm.sonLaboratory" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in dict.type.sys_sub_lab" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="要求描述：" prop="askTell">
              <el-input v-model="editForm.askTell" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="要求值：" prop="ask">
              <el-input v-model="editForm.ask" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="计量单位：" prop="unit">
              <el-select v-model="editForm.unit" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in dict.type.sys_unit" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工时(H)：" prop="manHour">
              <el-input v-model="editForm.manHour" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预计时间(H)：" prop="manDay">
              <el-input v-model="editForm.manDay" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工时分组：" prop="manHourGroup">
              <el-input v-model="editForm.manHourGroup" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验项类型：" prop="inspectionItemType">
              <el-select v-model="editForm.inspectionItemType" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in dict.type.inspection_item_type" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="editForm.inspectionItemType == 1">
            <el-form-item label="设备绑定：" prop="deviceId">
              <el-select v-model="editForm.deviceId" clearable placeholder="请选择"
                         size="small" style="width: 100%" multiple filterable>
                <el-option v-for="item in equipOptions" :key="item.value" :label="item.label" :value="item.value">
                  {{item.label + ' - ' + item.managementNumber}}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验值类型：" prop="inspectionValueType">
              <el-select v-model="editForm.inspectionValueType" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in dict.type.inspection_value_type" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="特殊标识：" prop="bsm">
              <el-select v-model="editForm.bsm" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in dict.type.inspection_bsm" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="原始记录模板：" prop="templateId">
              <el-select v-model="editForm.templateId" clearable placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in templateIdList" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检验项分类：" prop="inspectionItemClass">
              <el-input v-model="editForm.inspectionItemClass" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检验项分类EN：" prop="inspectionItemClassEn">
              <el-input v-model="editForm.inspectionItemClassEn" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="试验方法：" prop="method">
              <el-select v-model="editForm.method" clearable multiple placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in methodList" :key="item.value" :label="item.label" :value="item.label"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="条件：" prop="radiusList">
              <el-select v-model="editForm.radiusList" allow-create default-first-option filterable multiple
                         placeholder="请选择"
                         size="small"
                         style="width: 100%">
                <el-option v-for="item in radiusListList" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收费标准(元/次)：" prop="rates">
              <el-input v-model="editForm.rates" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
<!--        <el-col :span="12">-->
<!--          <el-form-item label="字典类型：" prop="dic">-->
<!--            <el-select v-model="editForm.dic" clearable placeholder="请选择" size="small" style="width: 100%">-->
<!--              <el-option v-for="item in dicList" :key="item.value" :label="item.label" :value="item.value"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="editLoad" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import {
  addItemParameter,
  getItemTree,
  getStandardTemplate,
  obtainItemParameterList,
  upItemParameter
} from "@/api/structural/capability";
import {selectStandardMethods} from "@/api/structural/standardMethod";
import {search} from "@/api/business/inspectionTask";

export default {
  name: "EditForm",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  dicts: ['sys_sub_lab', 'inspection_item_type', 'sys_unit', 'inspection_value_type', 'inspection_bsm'],
  data() {
    // 这里存放数据
    return {
      editFormDia: false,
      editLoad: false,
      editForm: {
        inspectionItem: '', // 检验项
        inspectionItemEn: '', // 检验项EN
        inspectionItemSubclass: '', // 检验子项
        inspectionItemSubclassEn: '', // 检验子项EN
        sample: [], // 检验对象
        price: '', // 单价
        laboratory: '', // 场所
        sonLaboratory: '', // 试验室
        askTell: '', // 要求描述
        ask: '', // 要求值
        unit: '', // 计量单位
        manHour: '', // 工时
        manDay: '', // 预计时间
        manHourGroup: '', // 工时分组
        inspectionItemType: '', // 检验项类型
        inspectionValueType: '', // 检验值类型
        dic: '', // 字典类型
        bsm: '', // 特殊标识
        templateId: '', // 原始记录模板
        inspectionItemClass: '', // 检验项分类
        inspectionItemClassEn: '', // 检验项分类EN
        method: '', // 试验方法
        radiusList: [], // 条件
        rates: '', // 条件
        deviceId: [], // 设备
      },
      sampleList: [], // 检验对象下拉框
      laboratoryList: [], // 场所下拉框
      inspectionItemTypeList: [], // 检验项类型下拉框
      dicList: [], // 字典类型下拉框
      templateIdList: [], // 原始记录模板下拉框
      methodList: [], // 试验方法下拉框
      radiusListList: [], // 条件下拉框
      itemParameterData: {
        cascaderField: {
          sample: {
            tree: []
          },
        }
      },
      editFormRules: {
        inspectionItem: [
          { required: true, message: '请选择检验项', trigger: 'change' }
        ],
        sonLaboratory: [
          { required: true, message: '请选择试验室', trigger: 'change' }
        ],
        unit: [
          { required: true, message: '请选择计量单位', trigger: 'change' }
        ],
        inspectionItemType: [
          { required: true, message: '请选择检验项类型', trigger: 'change' }
        ],
        inspectionValueType: [
          { required: true, message: '请选择检验值类型', trigger: 'change' }
        ],
        templateId: [
          { required: true, message: '请选择原始记录模板', trigger: 'change' }
        ]
      },
      operationType: '',
      equipOptions: []
    }
  },
  // 方法集合
  methods: {
    openDia (type, row) {
      this.operationType = type
      this.editFormDia = true
      this.getEquipOptions() // 获取所有设备
      this.obtainItemParameterList() // 场所类型
      this.getStandardTemplate() // 原始记录模板下拉框
      this.getSelectStandardMethods() // 试验方法
      this.selectTestObjectByName() // 检验对象
      if (type === 'add') {
        this.editForm = {
          inspectionItem: '', // 检验项
            inspectionItemEn: '', // 检验项EN
            inspectionItemSubclass: '', // 检验子项
            inspectionItemSubclassEn: '', // 检验子项EN
            sample: [], // 检验对象
            price: '', // 单价
            laboratory: '', // 场所
            sonLaboratory: '', // 试验室
            askTell: '', // 要求描述
            ask: '', // 要求值
            unit: '', // 计量单位
            manHour: '', // 工时
            manDay: '', // 预计时间
            manHourGroup: '', // 工时分组
            inspectionItemType: '', // 检验项类型
            inspectionValueType: '', // 检验值类型
            dic: '', // 字典类型
            bsm: '', // 特殊标识
            templateId: '', // 原始记录模板
            inspectionItemClass: '', // 检验项分类
            inspectionItemClassEn: '', // 检验项分类EN
            method: '', // 试验方法
            radiusList: [], // 条件
            rates: '', // 条件
        }
        this.resetForm('editForm')
      } else {
        this.editForm = this.HaveJson(row)
        this.editForm.sample = JSON.parse(this.editForm.sample)
        this.editForm.radiusList = this.editForm.radiusList && JSON.parse(this.editForm.radiusList)
        this.editForm.method = JSON.parse(this.editForm.method)
        const ids = this.editForm.deviceIds && this.editForm.deviceIds.split(",") || [];
        const numericIds = ids.map(Number);
        this.$set(this.editForm, 'deviceId', numericIds);
      }
    },
    // 提交编辑
    handleEdit () {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          this.editLoad = true
          let obj = this.HaveJson(this.editForm)
          obj.method = JSON.stringify(obj.method)
          obj.sample = JSON.stringify(obj.sample)
          if (obj.radiusList?.length > 0) {
            obj.radiusList = JSON.stringify(obj.radiusList)
          } else {
            obj.radiusList = null
          }
          if (obj.deviceId?.length > 0) {
            obj.deviceIds = obj.deviceId.join(',')
          } else {
            obj.deviceIds = null
          }
          if(obj.id){
            // 修改
            upItemParameter(obj).then(res => {
              this.editLoad = false
              if (res.code === 500) {
                return
              }
              this.$message.success('修改成功')
              this.editFormDia = false
              this.$emit('refreshList')
            }).catch(e => {
              this.editLoad = false
            })
          }else{
            // 新增
            addItemParameter(obj).then(res => {
              this.editLoad = false
              if (res.code === 500) {
                return
              }
              this.$message.success('添加成功')
              this.editFormDia = false
              this.$emit('refreshList')
            }).catch(e => {
              this.editLoad = false
            })
          }
        }
      })
    },
    // 关闭弹框
    closeDia () {
      this.editFormDia = false
      this.resetForm("editForm");
    },
    selectTestObjectByName() {
      getItemTree().then(res => {
        if (res.data.length > 0) {
          res.data.forEach(a=>{
            this.cascaderFieldData(a)
          })
          this.itemParameterData.cascaderField.sample.tree = res.data
        }
      })
    },
    cascaderFieldData(val){
      if(val.children === undefined) {
        return
      }else if(val.children.length==0){
        val.label = val.name
        val.value = val.id
        delete val.children
      }else{
        val.label = val.name
        val.value = val.id
        val.children.forEach(a=>{
          a.label = a.name
          a.value = a.id
          this.cascaderFieldData(a)
        })
      }
    },
    getSelectStandardMethods() {
      selectStandardMethods().then(res => {
        let data = []
          if (res.data.length > 0) {
            res.data.forEach(a => {
              data.push({
                label: a.code,
                value: a.id,
              })
            })
            this.methodList = data
          }
      })
    },
    obtainItemParameterList() {
      obtainItemParameterList().then(res => {
        let data = []
          if (res.data.length > 0) {
            res.data.forEach(a => {
              data.push({
                label: a.laboratoryName,
                value: a.id
              })
            })
          }
        this.laboratoryList = data
      })
    },
    getStandardTemplate() {
      getStandardTemplate().then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id,
            type: 'success'
          })
        })
        this.templateIdList = data
      })
    },
    // 获取所有设备
    getEquipOptions() {
      this.equipOptions = [];
      search({ status: 0 }).then((res) => {
        if (res.code === 200 && res.data) {
          this.equipOptions = res.data.map((m) => {
            m.value = m.id;
            m.label = m.deviceName;
            m.managementNumber = m.managementNumber;
            return m;
          });
        }
      }).catch((error) => {
        console.error(error);
      });
    },
  },
}
</script>

<style scoped>
::v-deep .el-dialog__body {
  height: 620px;
  overflow-y: auto;
}
</style>
