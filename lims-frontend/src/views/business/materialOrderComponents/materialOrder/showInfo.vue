<template>
  <div>
    <el-dialog title="产业链信息" :visible.sync="isShow" width="600px" @close="$parent.showInfoDialog = false">
      <div class="info" v-for="item in infoLIst" :key="item.fieldNameCn">
        <span class="title">{{item.fieldNameCn}}</span>：
        <span class="information">{{item.fieldNameValue}}</span>
      </div>
      <div class="info" v-if="infoLIst.length === 0">
        <span>暂无产业链数据</span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "showInfo",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    showInfoDialog: {
      type: Boolean,
      default: () => false
    },
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.showInfoDialog,
      infoLIst: []
    }
  },
  // 方法集合
  methods: {
    getInfo (id) {
      console.log('id----', id)
      this.$axios.get(this.$api.rawMaterialOrder.getIndustryChain + '?id=' + id).then(res => {
        if (res.code === 200 && res.data !== null) {
          this.infoLIst = JSON.parse(res.data)
          console.log('this.infoLIst----', this.infoLIst)
        }
      })
    }
  },
}
</script>

<style scoped>
.info {
  margin: 6px 0;
}
.title {
  display: inline-block;
  width: 100px;
  text-align-last: justify;
  font-size: 16px;
  font-weight: 600;
}
.information {
  font-size: 16px;
}
</style>
