<template>
  <div>
    <el-dialog :visible.sync="addTrainingPlanDia" title="新增培训计划" width="50%" @close="closeAdd">
      <div class="body">
        <el-form ref="trainingPlanForm" :model="trainingPlan" label-position="right" label-width="90px" :rules="trainingPlanRules">
          <el-row>
            <el-col :span="12">
              <el-form-item label="培训日期:" prop="trainingDate">
                <el-date-picker v-model="trainingPlan.trainingDate" format="yyyy-MM"
                                placeholder="选择日期" size="small" value-format="yyyy-MM"
                                type="month" style="width: 100%"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="课时:" prop="classHour">
                <el-input type="number" v-model="trainingPlan.classHour" label="描述文字" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训目标:" prop="trainingObjectives">
                <el-input v-model="trainingPlan.trainingObjectives" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="参加对象:" prop="participants">
                <el-input v-model="trainingPlan.participants" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训内容:" prop="trainingContent">
                <el-input v-model="trainingPlan.trainingContent" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训讲师:" prop="trainingLecturerId">
                <el-select v-model="trainingPlan.trainingLecturerId"
                           clearable filterable
                           placeholder="请选择" size="small" style="width: 100%;">
                  <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="培训方式:" prop="trainingMode">
                <el-input v-model="trainingPlan.trainingMode" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="举办部门:" prop="holdingDepartment">
                <el-input v-model="trainingPlan.holdingDepartment" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="备注:" prop="remarks">
                <el-input v-model="trainingPlan.remarks" placeholder="请输入" size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
				<el-button @click="closeAdd">取 消</el-button>
        <el-button type="primary" :loading="submitAddLoading" @click="submitAdd">确 定</el-button>
			</span>
    </el-dialog>
  </div>
</template>

<script>
import {addOrUpdatePersonTrainingDetailed} from "@/api/cnas/personal/personalTraining";
import {selectUserCondition} from "@/api/system/user";

export default {
  props: {
    currentChangeRow: {
      type: Object,
      default: () => {
        return {}
      }
    },
  },
  name: 'Add',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      addTrainingPlanDia: false,
      submitAddLoading: false,
      trainingPlan: {
        planId: '',
        id: '',
        trainingDate: '',
        classHour: '',
        trainingObjectives: '',
        participants: '',
        trainingContent: '',
        trainingLecturerId: '',
        trainingMode: '',
        holdingDepartment: '',
        remarks: '',
      },
      trainingPlanRules: {
        trainingDate: [{ required: true, message: '请选择培训日期', trigger: 'change' }],
        trainingContent: [{ required: true, message: '请输入培训内容', trigger: 'blur' }],
        trainingLecturerId: [{ required: true, message: '请选择培训讲师', trigger: 'blur' }],
      },
      responsibleOptions: [],
      operationType: ''
    };
  },
  // 方法集合
  methods: {
    showDialog(id, type, row) {
      this.addTrainingPlanDia = true;
      this.operationType = type
      if (this.operationType === 'edit') {
        this.trainingPlan = this.HaveJson(row)
      } else {
        this.trainingPlan = {
          id: '',
          trainingDate: '',
          classHour: '',
          trainingObjectives: '',
          participants: '',
          trainingContent: '',
          trainingLecturerId: '',
          trainingMode: '',
          holdingDepartment: '',
          remarks: '',
        }
      }
      this.trainingPlan.planId = id
      this.getUserList()
    },
    // 提交新增
    submitAdd() {
      this.$refs.trainingPlanForm.validate((valid) => {
        if (valid) {
          this.submitAddLoading = true
          this.trainingPlan.planId = this.currentChangeRow.id
          const personTrainingDetailed = this.trainingPlan
          addOrUpdatePersonTrainingDetailed(personTrainingDetailed).then(res => {
            this.submitAddLoading = false
            if (res.code == 200) {
              this.$message.success('提交成功');
              this.closeAdd();
            }
          }).catch(() => {
            this.submitAddLoading = false
          })
        }
      })
    },
    // 关闭弹框
    closeAdd() {
      this.$refs['trainingPlanForm'].resetFields();
      this.$emit('search')
      this.addTrainingPlanDia = false;
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition({type: 2}).then(res => {
        if (res.code == 200) {
          this.responsibleOptions = res.data
        }
      })
    },
  },
};
</script>

<style scoped>
</style>
