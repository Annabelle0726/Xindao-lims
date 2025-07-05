<template>
  <div>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="quarterSampleDia" title="季度抽样" width="90%" @close="quarterSampleDia = false">
      <div style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 10px">
        <div style="width: 30%;display: flex;align-items: center;" >
          <span style="width: 50px">编号：</span>
          <el-input v-model="quarterSampleForm.quarterNo" :disabled="operationType !== 'add'" size="small"></el-input>
        </div>
        <div v-if="operationType === 'add'">
          <el-button size="small" type="primary" @click="addQuarter">添加</el-button>
          <el-button size="small" type="danger" @click="clearTable">清空</el-button>
        </div>
      </div>
      <div>
        <el-table v-loading="tableLoading" :data="quarterItems" height="400" style="width: 100%"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column label="产品类型" prop="productType" width="200">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.productType" size="small"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="责任人" prop="dutyUser" width="100"></el-table-column>
          <el-table-column label="型号" prop="productModel" width="120">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.productModel" size="small" :disabled="operationType === 'view'"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="抽样数量" min-width="340" prop="spotCheckNumber">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.spotCheckNumber" size="small" :disabled="operationType === 'view'"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType === 'add'" label="数量" min-width="120" prop="number">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.number" size="small" :disabled="operationType === 'view'"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType !== 'add'" label="抽样时间" prop="spotCheckTime" width="160">
            <template slot-scope="{row}">
              <template>
                <el-date-picker v-model="row.spotCheckTime"
                                format="yyyy-MM-dd"
                                :disabled="operationType === 'view'"
                                placeholder="选择日期"
                                size="small"
                                clearable
                                style="width:100%"
                                type="date"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType !== 'add'" label="试样结论" prop="result" width="130">
            <template v-slot="scope">
              <el-select v-model="scope.row.result" placeholder="请选择" size="small" clearable :disabled="operationType === 'view'">
                <el-option label="合格" value="合格"></el-option>
                <el-option label="不合格" value="不合格"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType !== 'add'" label="取样人员" prop="samplingUser" width="120">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.samplingUser" size="small" :disabled="operationType === 'view'"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="itemRemark" width="200">
            <template slot-scope="{row}">
              <template>
                <el-input v-model="row.itemRemark" size="small" :disabled="operationType === 'view'"/>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="operationType !== 'view'" fixed="right" label="操作" width="100">
            <template slot-scope="scope">
              <el-button size="small" style="color: #f56c6c" type="text" @click="deleteScope(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="display: flex;align-items: center;margin: 10px 0">
        <span style="width: 70px">备注：</span>
        <el-input v-model="quarterSampleForm.remark" :disabled="operationType === 'view'" size="small" style="width: 43%" type="textarea"></el-input>
      </div>
      <div v-if="operationType !== 'add'">
        <el-form ref="form" :model="editForm" label-width="70px">
          <el-col :span="12">
            <el-form-item label="编制人：">
              <el-select v-model="editForm.writeUser" :disabled="operationType !=='edit'" placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会签人：">
              <el-select v-model="editForm.countersignUser" :disabled="operationType !=='edit'" multiple placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核人：">
              <el-select v-model="editForm.examineUser" :disabled="operationType !=='edit'" placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批准人：">
              <el-select v-model="editForm.ratifyUser" :disabled="operationType !=='edit'" placeholder="请选择" size="small" style="width: 100%">
                <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="quarterSampleDia = false">取 消</el-button>
        <el-button v-if="operationType === 'add'" @click="quarterSampleDia = false">保 存</el-button>
        <el-button v-if="operationType !== 'add' && operationType !== 'view'" type="primary" @click="handleSample">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {addQuarter, getQuarter, updateQuarterOnOrder} from "@/api/business/productSamplingInfo";
import {selectUserCondition} from "@/api/performance/class";

export default {
  name: "addQuarterItem",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      tableLoading: false,
      quarterSampleDia: false, // 季度抽样弹框
      quarterSampleForm: {
        quarterNo: '', // 编号
      },
      quarterItems: [],
      quarterTemItems: [],
      operationType: '',
      editForm: {
        quarterId: '',
        writeUser: '', // 编制人
        countersignUser: [], // 会签人
        examineUser: '', // 审核人
        ratifyUser: '', // 批准人
      },
      userList: [],
      quarterRow: {},
    }
  },
  mounted() {
    this.getUserList()
  },
  // 方法集合
  methods: {
    openDia (quarterTemItems, type) {
      this.quarterSampleDia = true
      this.operationType = type
      if (type === 'add') {
        if (quarterTemItems.length > 0) {
          this.quarterItems = quarterTemItems
        }
        this.quarterSampleForm.quarterNo = getCurrentMonthTwoDigits()
      } else {
        this.tableLoading = true
        this.quarterRow = quarterTemItems
        getQuarter({quarterId: quarterTemItems.quarterId}).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.quarterItems = res.data.quarterItems
            this.quarterSampleForm.quarterNo = res.data.quarterNo
            this.quarterSampleForm.remark = res.data.remark
            this.editForm.quarterId = res.data.quarterId
            this.editForm.writeUser = res.data.writeUser
            this.editForm.examineUser = res.data.examineUser
            this.editForm.ratifyUser = res.data.ratifyUser
            if (res.data.countersignUser) {
              this.editForm.countersignUser = res.data.countersignUser.split(",").map(Number)
            }
          }
        })
      }
    },
    // 手动添加
    addQuarter () {
      this.quarterItems.push({
        productType: '',
        dutyUser: JSON.parse(localStorage.getItem("user")).name,
        productModel: '',
        spotCheckNumber: '',
        number: '',
        spotCheckTime: '',
        result: '',
        samplingUser: '',
        itemRemark: '',
      })
    },
    // 提交季度抽样
    handleSample () {
      this.$confirm('是否提交该数据', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.operationType === 'add') {
          this.quarterSampleForm.quarterItems = JSON.parse(JSON.stringify(this.quarterItems))
          this.quarterSampleForm.quarterItems.forEach(item => {
            item.spotCheckNumber = item.spotCheckNumber + ' ' + (item.number == null ? "" : item.number)
          })
          addQuarter(this.quarterSampleForm).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功')
              this.quarterItems = []
              this.$parent.quarterTemItems = []
              this.closeQuarterSampleDia()
            }
          })
        } else {
          const params = {...this.editForm}
          if (params.countersignUser.length > 0) {
            params.countersignUser = params.countersignUser.join(',')
          } else {
            params.countersignUser = ''
          }
          params.quarterItems = JSON.parse(JSON.stringify(this.quarterItems))
          updateQuarterOnOrder(params).then(res => {
            if (res.code == 200) {
              this.$message.success('提交成功')
              this.quarterItems = []
              this.closeQuarterSampleDia()
            }
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });

    },
    // 清空抽样计划
    clearTable () {
      this.quarterItems = []
      this.$parent.quarterTemItems = []
    },
    // 手动删除
    deleteScope (index) {
      this.quarterItems.splice(index, 1)
    },
    // 关闭季度抽样弹框
    closeQuarterSampleDia () {
      this.quarterSampleDia = false
      if (this.operationType === 'add') {
        this.$parent.handleStockList()
      } else {
        this.$parent.refreshTable()
      }
    },
    getUserList(){
      selectUserCondition().then((res) => {
        this.userList = res.data;
      })
    },
  },
}
function getCurrentMonthTwoDigits() {
  const currentDate = new Date();
  const year = currentDate.getFullYear();
  const currentMonth = currentDate.getMonth() + 1;
  return year + currentMonth.toString().padStart(2, '0');
}
</script>

<style scoped>
</style>
