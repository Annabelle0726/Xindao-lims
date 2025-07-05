<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="viewDeviceDialog" title="查看设备" width="80%" @close="closeDia">
      <div style="text-align: right;margin-bottom: 10px">
        <el-button size="small" type="primary" @click="addRow">添加</el-button>
        <el-button size="small" type="danger" @click="clearTable">清空</el-button>
      </div>
      <div>
        <el-table :data="machineAttachmentList" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
                  border height="500" style="width: 100%">
          <el-table-column header-align="center" label="名称" prop="machineName" width="200">
            <template slot="header" slot-scope="scope">
              <p>名称</p>
            </template>
            <template slot-scope="scope">
              <el-select v-model="scope.row.machineName"
                         class="table_input"
                         clearable
                         filterable
                         placeholder="设备名称"
                         size="small" @change="(val)=>changeMachineName(val, scope.$index)">
                <el-option v-for="item in equipOptions" :key="item.value"  :label="item.label" :value="item.value">
                  {{item.label + item.value}}
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="型号规格" prop="machineSpecification" width="180">
            <template slot="header" slot-scope="scope">
              <p>型号规格</p>
            </template>
            <template slot-scope="{row}">
              <el-input v-model="row.machineSpecification" size="small"/>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="测量范围" prop="machineMeasuringRange">
            <template slot="header" slot-scope="scope">
              <p>测量范围</p>
            </template>
            <template slot-scope="{row}">
              <el-input v-model="row.machineMeasuringRange" size="small" type="textarea"/>
            </template>
          </el-table-column>
          <el-table-column header-align="center" label="①扩展不确定度②最大允差③准确度等级" prop="other">
            <template slot="header" slot-scope="scope">
              <p>①扩展不确定度②最大允差③准确度等级</p>
            </template>
            <template slot-scope="{row}">
              <el-input v-model="row.other" size="small" type="textarea"/>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button type="primary" @click="handleDeviceInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { deviceScopeSearch } from '@/api/cnas/process/method/standardMethodsChange'

export default {
  name: 'ViewDeviceDialog',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      viewDeviceDialog: false,
      machineAttachmentList: [],
      equipOptions: [],
    };
  },
  // 方法集合
  methods: {
    openDia(info) {
      this.viewDeviceDialog = true
      this.machineAttachmentList = info.machineAttachmentList
      this.getEquipOptions()
    },
    // 增加表格行数据
    addRow () {
      this.machineAttachmentList.push({
        machineName: '',
        machineSpecification: '',
        machineMeasuringRange: '',
        other: '',
      })
    },
    // 清空表格数据
    clearTable () {
      this.machineAttachmentList = []
    },
    // 选择设备的回调
    changeMachineName (val, index) {
      const index1 = this.equipOptions.findIndex(item => item.value === val);
      if (index1 !== -1) {
        this.machineAttachmentList[index].deviceId = this.equipOptions[index1].id;
        this.machineAttachmentList[index].machineName = this.equipOptions[index1].deviceName;
      }
      this.machineAttachmentList[index].machineSpecification = val
    },
    // 提交设备信息
    handleDeviceInfo () {
      if (this.machineAttachmentList.length === 0) {
        this.$message.warning('请添加信息')
        return
      }
      this.viewDeviceDialog = false
      this.$emit('handleDeviceInfo', this.machineAttachmentList)
    },
    closeDia () {
      this.viewDeviceDialog = false
      this.$emit('closDeviceDia')
    },
    // 获取所有设备
    getEquipOptions() {
      this.equipOptions = []
      deviceScopeSearch({status:'0'}).then(res => {
        if (res.code === 200 && res.data) {
          this.equipOptions = res.data.map(m => {
            m.value = m.managementNumber
            m.label = m.deviceName
            return m
          })
        }
      }).catch(error => {
        console.error(error)
      })
    },
  }
};
</script>

<style scoped>
</style>
