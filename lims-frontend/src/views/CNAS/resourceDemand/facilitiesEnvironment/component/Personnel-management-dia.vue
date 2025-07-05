<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :visible.sync="formDia"
               title="外来人员管理信息"
               width="80%" @close="closeThreeWastesDia">
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-col :span="12">
          <el-form-item label="日期" prop="registerDate">
            <el-date-picker v-model="form.registerDate"
                            clearable
                            format="yyyy-MM-dd"
                            placeholder="选择日期"
                            size="small"
                            type="date"
                            value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="进入区域" prop="area">
            <el-input v-model="form.area" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="进入人员" prop="personnel">
            <el-input v-model="form.personnel" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="进入原因" prop="reason">
            <el-input v-model="form.reason" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="陪同人员" prop="accompanyingId">
            <el-select v-model="form.accompanyingId" clearable filterable
                       placeholder="请选择陪同人员" size="small">
              <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="批准人签名" prop="approveId">
            <el-select v-model="form.approveId" clearable filterable
                       placeholder="请选择批准人" size="small">
              <el-option v-for="(item,i) in personList" :key="i" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保密及其它情况" prop="confidentiality">
            <el-input v-model="form.confidentiality" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeThreeWastesDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getForeignRegisterOne,
  selectUserCondition,
  addForeignRegister,
  updateForeignRegister
} from '@/api/cnas/resourceDemand/foreignRegister/foreignRegister'
import {mapGetters} from "vuex";

export default {
  name: 'Personnel-management-dia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        registerDate: '',
        area: '',
        personnel: '',
        reason: '',
        accompanyingId: '',
        accompanyingName: '',
        approveId: '',
        approveName: '',
        confidentiality: '',
        remark: '',
      },
      rules: {
        registerDate: [{required: true, message: '请选择日期',trigger: 'change'}],
        area: [{required: true, message: '请填写进入区域',trigger: 'blur'}],
        personnel: [{required: true, message: '请填写进入人员',trigger: 'blur'}],
        reason: [{required: true, message: '请填写进入原因',trigger: 'blur'}],
        accompanyingId: [{required: true, message: '请选择陪同人员',trigger: 'change'}],
        approveId: [{required: true, message: '请选择批准人',trigger: 'change'}],
        confidentiality: [{required: true, message: '请填写保密及其他情况',trigger: 'blur'}],
        remark: [{required: false, message: '请填写备注',trigger: 'blur'}],
      },
      operationType: '',
      personList: []
    };
  },
  mounted() {

  },
  computed: {
    ...mapGetters(["userId"]),
  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia (type, row) {
      this.getAuthorizedPerson()
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.searchInfo(row)
      } else {
        this.form.accompanyingId = this.userId
        this.form.area = '装备电缆试验室'
        this.form.reason = '参观实验室'
        this.form.accompanyingId = 64
        this.form.approveId = 65
        this.form.confidentiality = '符合'
      }
    },
    // 查询详情
    searchInfo (row) {
      this.diaLoading = true
      getForeignRegisterOne({registerId : row.registerId}).then(res =>{
        this.diaLoading = false
        if (res.code === 200){
          this.form = res.data
        }
      }).catch(err => {
        console.log(err)
        this.diaLoading = false
      })
    },
    // 提交弹框数据
    handleEdit () {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          const internalImplementDto = this.HaveJson(this.form)
          const index = this.personList.findIndex(item => item.value === internalImplementDto.accompanyingId)
          if (index > -1) {
            internalImplementDto.accompanyingName = this.personList[index].label
          }
          const index1 = this.personList.findIndex(item => item.value === internalImplementDto.approveId)
          if (index1 > -1) {
            internalImplementDto.approveName = this.personList[index].label
          }
          if (this.operationType === 'add') {
            addForeignRegister(internalImplementDto).then(res => {
              this.loading = false
              if (res.code === 200){
                this.$message.success('操作成功')
                this.closeThreeWastesDia()
              }
            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          } else if (this.operationType === 'edit') {
            updateForeignRegister(internalImplementDto).then(res => {
              this.loading = false
              if (res.code === 200){
                this.$message.success('操作成功')
                this.closeThreeWastesDia()
              }

            }).catch(err => {
              console.log('err---', err);
              this.loading = false
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    closeThreeWastesDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeThreeWastesDia')
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
  }
};
</script>

<style scoped>
</style>
