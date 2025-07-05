<template>
  <el-dialog :visible.sync="dialogVisible" title="标准物质验收记录" width="72%" v-loading="loading">
    <el-form :model="acceptance" ref="acceptance" label-width="140px" size="small">
      <el-col :span="12">
        <el-form-item label="厂家代表" prop="producer">
          <el-input v-model="acceptance.producer"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="相关附件" prop="file">
          <div class="table-between">
            <el-input v-model="acceptance.file" style="width: 80%;" disabled></el-input>
            <el-upload
              ref="upload"
              style="float: right;"
              :headers="uploadHeader"
              :action="action"
              :show-file-list="false"
              :on-success="onSuccess"
            >
              <el-button type="primary">
                附件上传
              </el-button>
            </el-upload>
          </div>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="接收人" prop="recipient">
          <el-input v-model="acceptance.recipient"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="接受签字" prop="signature">
          <el-input v-model="acceptance.signature"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="验收情况" prop="situation">
          <el-input v-model="acceptance.situation"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="安装调试情况" prop="installation">
          <el-input v-model="acceptance.installation"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="清单" prop="substanceId"
                      :rule="[{ required: true, message: '请选择清单', trigger: 'change' }]">
          <el-select v-model="acceptance.substanceId" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="到货日期" prop="arriveDate">
          <el-date-picker
            v-model="acceptance.arriveDate"
            align="right"
            type="date"
            placeholder="选择日期"
            style="width: 100%"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="维修单位" prop="maintenanceUnit">
          <el-input v-model="acceptance.maintenanceUnit"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备件确认">
          <el-button type="primary" style="float: right;" @click="addSpareTable">增加行</el-button>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <lims-table :tableData="list" :column="spareColumns" height="300">
          <template slot="name" slot-scope="{ row, index }">
            <el-input v-if="row.isEdit" size="small" v-model="row.name"></el-input>
            <span v-else>{{ row.name }}</span>
          </template>
          <template slot="number" slot-scope="{ row, index }">
            <el-input v-if="row.isEdit" size="small" v-model="row.number"></el-input>
            <span v-else>{{ row.number }}</span>
          </template>
          <template slot="action" slot-scope="{ row, index }">
            <div v-if="row.isEdit">
              <el-button type="text" @click="save(index)">保存</el-button>
              <el-button type="text">取消</el-button>
            </div>
            <div v-else>
              <el-button type="text" @click="edit(index)">修改</el-button>
            </div>
          </template>
        </lims-table>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="submit">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  addAcceptance, getAcceptanceDetails,
  getStandardSubstanceAll,
  updateAcceptance
} from "@/api/cnas/resourceDemand/standardMaterialAccept/standardMaterialAccept";
export default {
  components: {
    limsTable,
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      acceptance: {
        id: undefined,
        producer: undefined, // 厂家代表
        file: undefined,   // 相关附件
        recipient: undefined,   // 接收人
        signature: undefined,   // 接受签字
        situation: undefined,   // 验收情况
        installation: undefined,    // 安装调试情况
        substanceId: undefined, // 清单
        arriveDate: undefined,   // 到货日期
        maintenanceUnit: undefined,   // 维修单位
        list: [],
      },
      list: [],
      spareColumns: [
        {
          label: "名称",
          prop: "name",
          align: "center",
          dataType: "slot",
          slot: "name",
        }, {
          label: "数量",
          prop: "number",
          align: "center",
          dataType: "slot",
          slot: "number",
        }, {
          label: "操作",
          align: "center",
          dataType: "slot",
          slot: "action",
          width: 150
        }
      ],
      options: []
    }
  },
  mounted() {
    this.getStand()
  },
  methods: {
    openDialog(id) {
      if(id) {
        this.getDetail(id)
      } else {
        this.clearForm()
      }
      this.dialogVisible = true
    },
    getDetail(id) {
      this.loading = true
      getAcceptanceDetails({id: id}).then(res => {
        this.acceptance = res.data.acceptance
        this.list = res.data.list
        this.loading = false
      }).catch(err => {
        this.loading = false
      })
    },
    getStand() {
      getStandardSubstanceAll().then(res => {
        this.options = res.data
      })
    },
    addSpareTable() {
      this.list.push({
        acceptanceId: this.acceptance.id,
        name: '',
        number: '',
        isEdit: true
      })
    },
    onSuccess(response) {
      this.acceptance.file = response.data
    },
    save(index) {
      this.$set(this.list[index], 'isEdit', false)
    },
    edit(index) {
      this.$set(this.list[index], 'isEdit', true)
    },
    clearForm() {
      this.list = []
      this.resetForm('acceptance')
    },
    closeDialog() {
      this.clearForm()
      this.resetForm('acceptance')
      this.dialogVisible = false
    },
    submit() {
      if (!this.acceptance.substanceId) {
        this.$message.warning('请选择清单')
        return
      }
      this.acceptance.list = this.HaveJson(this.list)
      this.submitLoading = true
      if(this.acceptance.id) {
        updateAcceptance({acceptance: this.acceptance, list: this.list}).then(res => {
          this.$message.success('编辑成功')
          this.submitLoading = false
          this.closeDialog()
          this.$emit('submit')
        }).catch((e) => {
          this.submitLoading = false
        })
      } else {
        addAcceptance({acceptance: this.acceptance, list: this.list}).then(res => {
          this.$message.success('新增成功')
          this.closeDialog()
          this.$emit('submit')
        }).catch((e) => {
          this.submitLoading = false
        })
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
>>>.el-dialog__body {
  height: 580px;
  overflow-y: auto;
}
</style>
