<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false"
               :close-on-press-escape="false"
               :visible.sync="formDia"
               title="三废处理信息"
               width="80%" @close="closeThreeWastesDia">
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-col :span="24">
          <el-form-item label="备注" prop="purposes">
            <el-input v-model="form.remark" clearable size="small"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div style="text-align: right;margin-bottom: 10px">
        <el-button size="small" type="primary" @click="addRow">添加</el-button>
        <el-button size="small" type="danger" @click="clearTable">清空</el-button>
      </div>
      <el-table :data="wastesDetailList" border height="300" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
        <el-table-column align="center" label="序号" type="index" width="60" />
        <el-table-column header-align="center" label="接收人" prop="acceptor">
          <template slot-scope="{row}">
            <el-input v-model="row.acceptor" size="small"/>
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="接收单位" prop="receivingUnit">
          <template slot-scope="{row}">
            <el-input v-model="row.receivingUnit" size="small"/>
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="名称" prop="designation">
          <template slot-scope="{row}">
            <el-input v-model="row.designation" size="small"/>
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="移交人" prop="transferPeople" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.transferPeople" size="small"/>
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="体积" prop="volume" width="180">
          <template slot-scope="{row}">
            <el-input v-model="row.volume" size="small"/>
          </template>
        </el-table-column>
        <el-table-column header-align="center" label="送处理日期" prop="deliveryDate" width="180">
          <template slot-scope="{row}">
            <!-- <el-input v-model="row.deliveryDate" size="small"/> -->
            <el-date-picker
                value-format="yyyy-MM-dd"
                style="width: 100%"
                format="yyyy-MM-dd"
                size="small"
                v-model="row.deliveryDate"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeThreeWastesDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInternalWastesOne,
  addInternalWastes,
  updateInternalWastes
} from '@/api/cnas/resourceDemand/facilitiesEnvironment/internalWastes'

export default {
  name: 'three-wastes-dialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      form: {
        remark: '',
        wastesId: '',
      },
      rules: {
        remark: [{required: false, message: '请填写备注',trigger: 'blur'}],
      },
      wastesDetailList: [],
      operationType: '',
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia (type, row) {
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.searchInfo(row)
      }
    },
    // 查询详情
    searchInfo (row) {
      this.diaLoading = true
      getInternalWastesOne({wastesId: row.wastesId}).then(res => {
        this.diaLoading = false
        if (res.code === 200){
          this.form = res.data
          this.wastesDetailList = this.form.wastesDetailList
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
          if (this.wastesDetailList.length === 0) {
            this.$message.warning('请添加表格数据')
            return
          }
          this.loading = true
          const internalImplementDto = this.HaveJson(this.form)
          internalImplementDto.wastesDetailList = this.HaveJson(this.wastesDetailList)
          if (this.operationType === 'add') {
            addInternalWastes(internalImplementDto).then(res => {
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
            updateInternalWastes(internalImplementDto).then(res => {
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
    // 增加表格行数据
    addRow () {
      this.wastesDetailList.push({
        acceptor: '',
        receivingUnit: '',
        designation: '',
        transferPeople: '',
        volume: '',
        deliveryDate: '',
      })
    },
    // 清空表格数据
    clearTable () {
      this.wastesDetailList = []
    },
    closeThreeWastesDia () {
      this.$refs.form.resetFields();
      this.formDia = false
      this.$emit('closeThreeWastesDia')
    },
  }
};
</script>

<style scoped>
</style>
