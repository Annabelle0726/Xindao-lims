<template>
  <div>
    <el-dialog
      title="不合格复测查看"
      :visible.sync="isShow"
      @closed="$emit('closeRetestLook')"
      width="40%">
      <el-table
        :data="retestInfo"
        border
        style="width: 100%">
        <el-table-column type="index" label="复测次数" align="center" width="100">
        </el-table-column>
        <el-table-column prop="tell" label="试验要求" width="120">
        </el-table-column>
        <el-table-column prop="lastValue" label="检验结果" width="120">
        </el-table-column>
        <el-table-column prop="insResult" label="结果判定">
          <template slot-scope="scope">
            <el-tag type="danger" v-if="scope.row.insResult === 0" disable-transitions>不合格</el-tag>
            <el-tag type="success" v-if="scope.row.insResult === 1" disable-transitions>合格</el-tag>
            <el-tag v-if="scope.row.insResult === 3" disable-transitions>不判定</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "unPassRetestResult",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    retestVisible: {
      type: Boolean,
      default: () => false
    },
    retestInfo: {
      type: Array,
      default: () => []
    }
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.retestVisible,
    }
  },
  // 方法集合
  methods: {},
}
</script>

<style scoped>
</style>
