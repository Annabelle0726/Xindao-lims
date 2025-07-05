<template>
  <div>
    <el-dialog title="新增不合格复测" :visible.sync="isShow" width="70%" :show-close="false" :close-on-click-modal="false"
      :close-on-press-escape="false">
      <div class="table">
        <el-table class="el-table" ref="productTable" :data="productList" max-height="800px" tooltip-effect="dark"
                  v-loading="tableLoading" @selection-change="selectProduct" style="margin-bottom: 10px;"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border @select-all="handleAll">
          <el-table-column type="selection" width="65"></el-table-column>
          <el-table-column prop="isBinding" label="类型" min-width="140" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-model="scope.row.isBinding" clearable size="small">
                <el-option :value="1" label="绑定值"></el-option>
                <el-option :value="0" label="不合格值"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="inspectionItemClass" label="检验项分类" min-width="140" show-overflow-tooltip></el-table-column>
          <el-table-column prop="inspectionItem" label="检验项" min-width="140" show-overflow-tooltip></el-table-column>
          <el-table-column prop="inspectionItemSubclass" label="检验项子项" min-width="140"
            show-overflow-tooltip></el-table-column>
          <el-table-column prop="tell" label="要求描述" min-width="220px"></el-table-column>
          <el-table-column prop="ask" label="要求值" min-width="220px"></el-table-column>
          <el-table-column prop="lastValue" label="检验结果" min-width="140" show-overflow-tooltip></el-table-column>
          <el-table-column prop="insResult" label="结果判定" min-width="140" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-tag type="success" v-if="scope.row.insResult === 1">合格</el-tag>
              <el-tag type="danger" v-if="scope.row.insResult === 0">不合格</el-tag>
              <el-tag type="info" v-if="scope.row.insResult === 3">不判定</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('resetAddUnPass')">取 消</el-button>
        <el-button type="primary" @click="submitHandle" :loading="handlePassLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {addUnqualifiedRetest, getInsProductUnqualified} from '@/api/business/unpass.js'
import {getInsProduct} from "@/api/business/inspectionTask";
export default {
  name: "addUnPass",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    addUnPassDialog: {
      type: Boolean,
      default: () => false
    },
  },
  data() {
    // 这里存放数据
    return {
      type: '',
      isShow: this.addUnPassDialog,
      handlePassLoading: false,
      productList: [],
      productListSelected: [],
      productIds: [],
      tableLoading: false
    }
  },
  mounted() {
  },
  // 方法集合
  methods: {
    getInsOrder(info) {
      this.tableLoading = true
      try {
        getInsProduct({ id: info.id, type: info.type, laboratory: info.laboratory, rawMaterialTag: info.rawMaterialTag,repetitionTag: info.repetitionTag,cableTag: info.cableTag }).then(res => {
          if (res.code === 200) {
            this.productList = res.data
          }
          this.tableLoading = false
        })
      } catch (err) {
        this.tableLoading = false
      }
    },
    // 提交不合格处理
    submitHandle() {
      if (this.productListSelected.length === 0) {
        this.$message.warning('请选择需要复测的检验项')
        return
      }
      // 检查是否所有选中的行都选择了 isBinding
      const hasUnselectedBinding = this.productListSelected.some(
        (row) => row.isBinding === null || row.isBinding === undefined
      );

      if (hasUnselectedBinding) {
        this.$message.error("请确保选中的数据都选择了类型！");
        return;
      }
      this.$confirm('确认提交不合格复测?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.handlePass()
      }).catch(() => {

      });
    },
    handlePass() {
      const ids = this.productListSelected.map(item => ({
        id: item.id,
        isBinding: item.isBinding
      }));
      this.handlePassLoading = true
      try {
        addUnqualifiedRetest(ids).then(res => {
          if (res.code === 200) {
            this.$message.success('提交成功')
            this.$emit('resetAddUnPass')
          }
          this.handlePassLoading = false
        }).catch(e => {
          this.handlePassLoading = false
        })
      } catch (e) {
        this.handlePassLoading = false
      }
    },
    selectProduct(val) {
      this.productListSelected = val
      this.productIds = []
      val.forEach(a => {
        this.productIds.push(a.id)
      })
    },
    handleAll(val) {
      this.productListSelected = val
      this.productIds = []
      val.forEach(a => {
        this.productIds.push(a.id)
      })
    },
  },
}
</script>

<style scoped></style>
