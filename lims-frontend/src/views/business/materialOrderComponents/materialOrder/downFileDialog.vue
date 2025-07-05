<template>
  <div>
    <el-dialog title="报告查看下载" :visible.sync="isShow" width="500px" @closed="$emit('closeDownFileDialog')">
      <div style="margin-bottom: 18px">
        <span style="font-size: 16px;">进厂检验报告下载</span>
        <i class="el-icon-paperclip" style="color:#409EFF;margin-left: 6px"></i>
        <el-link :underline="false" style="vertical-align: bottom;font-size: 16px;"
                 :disabled="!downLoadInfo.enterUrls && !downLoadInfo.enterUrl"
                 type="primary" @click="downLoad0">查看</el-link>
      </div>
      <div>
        <span  style="font-size: 16px;">季度检验报告下载</span>
        <i class="el-icon-paperclip" style="color:#409EFF;margin-left: 6px"></i>
        <el-link :underline="false" style="vertical-align: bottom;font-size: 16px;"
                 :disabled="!downLoadInfo.quarterUrls && !downLoadInfo.quarterUrl"
                 type="primary" @click="downLoad1">查看</el-link>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "downFileDialog",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    downFileDialogVisible: {
      type: Boolean,
      default: () => false
    },
    downLoadInfo: {
      type: Object,
      default: () => {}
    },
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.downFileDialogVisible,
    }
  },
  // 方法集合
  methods: {
    // 进厂检验报告下载
    downLoad0 () {
      let url = this.downLoadInfo.enterUrlS ? this.downLoadInfo.enterUrlS : this.downLoadInfo.enterUrl
      if(url){
        url = url.split('.')[0]+'.pdf'
        const link = document.createElement('a');
        link.href = this.javaApi + url;
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
      }
    },
    // 季度检验报告下载
    downLoad1 () {
      let url = this.downLoadInfo.quarterUrlS ? this.downLoadInfo.quarterUrlS : this.downLoadInfo.quarterUrl
      if(url){
        url = url.split('.')[0]+'.pdf'
        const link = document.createElement('a');
        link.href = this.javaApi + url;
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
      }
    }
  },
}
</script>

<style scoped>
>>> .el-dialog {
  height: 160px;
}
</style>
