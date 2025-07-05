<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="dialogVisible"
      title="添加检验项"
      width="80%">
      <el-table ref="sampleTable" :data="sampleList" border class="el-table sampleTable" highlight-current-row
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
                max-height="400px" tooltip-effect="dark">
        <el-table-column align="center" label="序号" type="index" width="65"></el-table-column>
        <el-table-column align="center" label="样品名称" min-width="100" prop="sample">
          <template slot-scope="scope">
            <el-input v-model="scope.row.sample" clearable disabled size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="样品编号" min-width="100" prop="sampleCode">
          <template slot-scope="scope">
            <el-input v-model="scope.row.sampleCode" clearable disabled placeholder="不填写则系统自动生成"
                      size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="样品型号" min-width="60" prop="model">
          <template slot-scope="scope">
            <el-input v-model="scope.row.model" clearable disabled placeholder="不填写则系统自动生成"
                      size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验标准" min-width="100" prop="standardMethodName">
          <template slot-scope="scope">
            <el-input v-model="scope.row.standardMethodName" clearable disabled placeholder="不填写则系统自动生成"
                      size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="120">
          <template #default="{ row }">
            <el-button size="small" type="text" @click="openAddItemDia(row)">添加</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="itemDialogVisible"
      title="添加检验项"
      width="90%">
      <el-table ref="productTable" v-loading="getProductLoad" :data="productList" :row-class-name="tableRowClassName" border class="el-table"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
                max-height="580px" tooltip-effect="dark" @selection-change="selectProduct">
        <el-table-column type="selection" width="65"></el-table-column>
        <el-table-column label="检验项分类" min-width="140" prop="inspectionItemClass" show-overflow-tooltip></el-table-column>
        <el-table-column label="检验项" min-width="140" prop="inspectionItem" show-overflow-tooltip></el-table-column>
        <el-table-column label="检验项子项" min-width="140" prop="inspectionItemSubclass" show-overflow-tooltip></el-table-column>
        <el-table-column label="要求值" min-width="220px" prop="ask">
          <template slot-scope="scope">
            <el-input v-if="active==1&&scope.row.inspectionValueType!='5'" v-model="scope.row.ask" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求值"
                      size="small" type="textarea"></el-input>
            <span v-else>
                <template >{{ scope.row.ask }}</template>
              </span>
          </template>
        </el-table-column>
        <el-table-column label="要求描述" min-width="220px" prop="tell">
          <template slot-scope="scope">
            <el-input v-if="active==1" v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求描述"
                      size="small" type="textarea"></el-input>
            <span v-else>
                <template >{{ scope.row.tell }}</template>
              </span>
          </template>
        </el-table-column>
        <el-table-column label="条件" min-width="140" prop="radius" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-input v-if="active==1&&!scope.row.inspectionItem.includes('高温压力试验')" v-model="scope.row.radius" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="条件"
                      size="small" type="textarea">
            </el-input>
            <el-select v-else-if="scope.row.inspectionItem.includes('高温压力试验') && active==1" v-model="scope.row.radius" clearable
                       placeholder="条件"
                       size="small">
              <el-option v-for="(a,i) in JSON.parse(scope.row.radiusList)" :key="i" :label="a" :value="a"></el-option>
            </el-select>
            <span v-else>{{scope.row.radius}}</span>
          </template>
        </el-table-column>
        <el-table-column label="线芯" min-width="120" prop="cableTag" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-input v-if="active==1" v-model="scope.row.cableTag" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="线芯"
                      size="small" type="textarea"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="试验方法" min-width="120" prop="methodS" show-overflow-tooltip></el-table-column>
        <el-table-column label="计量单位" prop="unit" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="单价" prop="price" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="区间" min-width="120" prop="section" show-overflow-tooltip></el-table-column>
        <el-table-column :filter-method="filterHandler" :filters="filters" label="子实验室" min-width="130" prop="sonLaboratory"
                         show-overflow-tooltip></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
				<el-button @click="itemDialogVisible = false">取 消</el-button>
        <el-button :loading="upLoad" type="primary" @click="submitList">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {addOmitOrderProduct, getProductTreeBySampleId, getSampleByOrderId} from "@/api/business/productOrder";

export default {
  name: "addInspectionDia",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      dialogVisible: false,
      itemDialogVisible: false,
      getProductLoad: false,
      sampleList: [], // 检验标准表格数据
      productList: [], // 检验项表格数据
      productListSelected: [], // 选择的检验项表格
      active: 1,
      methodS:null,
      id:null,
      filters: [],
      upLoad: false,
    }
  },
  mounted() {

  },
  // 方法集合
  methods: {
    getDataList(row) {
      this.dialogVisible = true
      getSampleByOrderId({insOrderId: row.id}).then(res => {
        this.sampleList = res.data
      })
    },
    openAddItemDia (row) {
      this.itemDialogVisible = true
      this.getProductLoad = true
      this.id=row.id
      getProductTreeBySampleId({insSampleId: row.id}).then(res => {
        this.getProductLoad = false
        this.productList = res.data
      }).catch(err => {
        this.getProductLoad = false
      })
    },
    selectProduct(val) {
      val.forEach((item) => {
        item.state = 1
      })
      const index = this.productList.findIndex(obj => obj.id === val.id)
      if (index > -1) {
        this.productList[index].state = 1
      }
      this.productListSelected = val
    },
    submitList () {
      this.$confirm('确认添加此检验项?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.upLoad = true
        addOmitOrderProduct({insProductBindingList:this.productListSelected,insSampleId:this.id}).then(res => {
          this.upLoad = false
          this.$message.success('添加成功！')
          this.itemDialogVisible = false
        }).catch(err => {
          this.upLoad = false
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消添加'
        });
      });
    },
    tableRowClassName({row, rowIndex}) {
      if (row.state === 0) {
        return '';
      }
      return 'warning-row';
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
  },
}
</script>

<style scoped>
.ins_order_add .el-table .warning-row .cell {
  color: #3A7BFA;
}
</style>
