<template>
  <el-dialog
    :title="dialogTitle"
    width="60%"
    :visible.sync="dialogVisible"
  >
    <el-form :model="model" label-width="100px">
      <el-col :span="12">
        <el-form-item label="项目名称">
          <el-select
            v-model="model.name"
            placeholder="请选择项目名称"
            style="width: 100%"
            :disabled="row ? true:false"
            @change="handleSelect"
          >
            <el-option
              v-for="(v, i) in consumableOptions"
              :label="v.consumablesName"
              :value="v"
              :key="i"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="消耗数量">
          <el-input-number v-model="model.amount" :min="0" :step="1" placeholder="请输入消耗数量"></el-input-number>
        </el-form-item>
      </el-col>
    </el-form>
    <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="sumbit">确 定</el-button>
        </span>
  </el-dialog>
</template>

<script>

import {
  addProcurementSuppliesExpends,
  procurementSuppliesList
} from "@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro";

export default {
  data() {
    return {
      dialogTitle: "",
      dialogVisible: false,
      model: {
        listId: undefined,
        name: ""
      },
      consumableOptions: [],
      row: undefined
    }
  },
  created() {
    this.fetchConsumableOptions()
  },
  methods: {
    fetchConsumableOptions() {
      procurementSuppliesList().then(res => {
        if (res.code === 200) {
          this.consumableOptions = res.data.records
        }
      })
    },
    openDialog(row) {
      if(row) {
        console.log(row, 'true')
        this.dialogTitle = `${row.consumablesName}添加消耗项`
        this.row = row
        this.model.listId = row.id
        this.model.name = row.consumablesName
      } else {
        this.dialogTitle = '添加消耗项'
      }
      this.dialogVisible = true
    },
    handleSelect(item) {
      console.log(item)
      this.model.listId = item.id
      this.model.name = item.consumablesName
    },
    sumbit() {
      addProcurementSuppliesExpends(this.model).then(res => {
        if (res.code === 200) {
          this.dialogVisible = false
          this.$emit('submit')
        }
      })
    }
    }
  }
</script>
