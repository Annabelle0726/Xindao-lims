<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div class="search_box">
        <div class="search_item">
          <span class="search_label">人员名称</span>
          <el-input v-model="entity.name" clearable @keyup.enter.native="currentPage= 1,list=[],finishLoding = false,keyMap = {},refreshTable()"></el-input>
        </div>
        <div class="search_button">
          <el-button size="mini" type="primary" @click="currentPage= 1,list=[],finishLoding = false,keyMap = {},refreshTable()">查询</el-button>
          <el-button @click="refresh" size="mini">重置</el-button>
        </div>
      </div>
    </div>
    <div class="table" v-loading="loading">
      <scroll-pagination @load="refreshTable" :finishLoding="finishLoding" :list="list" v-if="list.length>0||loading">
        <ul class="card">
          <li v-for="(m,i) in list" :key="i">
            <el-image style="width: 80px;height: 112px;" :src="javaApi+'/img/'+m.pictureUrl">
              <div slot="error" class="image-error" style="width: 79px;
            height: 110px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid #EEEEEE;">
                <i class="el-icon-picture-outline" style="font-size:30px;color:#666666;"></i>
              </div>
            </el-image>
            <el-image style="width: 195px;
            height: 112px;" :src="javaApi+'/img/'+m.signatureUrl">
              <div slot="error" class="image-error" style="width: 194px;
            height: 110px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid #EEEEEE;">
                <i class="el-icon-picture-outline" style="font-size:30px;color:#666666;"></i>
              </div>
            </el-image>
            <div class="title">{{ m.name }}</div>
          </li>
        </ul>
      </scroll-pagination>
      <div v-if="list.length<1&&!loading" style="color:#909399;font-size:14px;text-align: center;margin-top:200px" >暂无数据</div>
    </div>
  </div>
</template>

<script>
import ScrollPagination from '@/components/index/scroll-paging.vue'
import {selectUserCondition} from "@/api/business/inspectionTask";
export default {
  name: 'Personaloverview',
  components: {
    ScrollPagination
  },
  data() {
    return {
      entity:{
        name: null,
        status: 0,
      },
      list:[],
      currentPage: 1, // 当前页
      pageSize: 16, // 一页16条
      total: '',
      loading: true, // 组件loading的展示,默认为true
      finishLoding: false, // 加载完成，显示已经没有更多了
      keyMap: {}
    }
  },
  created() {
    this.keyMap = {};
    this.currentPage = 1;
    this.list = [];
    this.refreshTable();
  },
  methods: {
    refreshTable() {
      const key = `_${this.currentPage}`
      const value = this.keyMap[key]
      // 如果value存在，表示缓存有值，那么阻止请求
      if(value) {
        return
      }
      // value不存在，表示第一次请求,设置占位
      this.keyMap[key] = 'temp'
      if(this.currentPage==1){
        this.loading = true
      }
      selectUserCondition({...this.entity}).then(res => {
        this.total = res.data.length
        let list = res.data
        if(list.length==0){
          this.finishLoding = true;
        }else{
          if(list.length<this.pageSize){
            this.finishLoding = true;
          }
          this.list = this.list.concat(list)
          if(this.total==this.list.length){
            this.finishLoding = true;
          }
          this.currentPage++;
        }
        this.loading = false
      }).catch(err => {
        this.loading = false
      })
    },
    refresh() {
      this.keyMap = {};
      this.currentPage= 1;
      this.list=[];
      this.finishLoding = false;
      this.entity={
        name: null,
        state:1,
        orderBy: {field: "id", order: "asc"}
      };
      this.refreshTable()
    },
    // 滚动触底加载
    scrollFn() {
      let clientHeight = document.documentElement.clientHeight - 18; //可视区域
      let scrollHeight = document.body.scrollHeight; // 滚动文档高度
      let scrollTop = parseInt(document.documentElement.scrollTop); // 已滚动的高度
      let height = 300;
      if (
        scrollTop + clientHeight >= scrollHeight - height &&
        scrollHeight != 0
      ) {
        if (!this.finishLoding&&this.currentPage*this.pageSize<this.total) {
          this.currentPage = this.currentPage + 1;
          this.refreshTable();
        }
      } else {
        return false;
      }
    },
    throttle(fn, wait) {
      // 封装函数进行节流
      var timer = null;
      return function () {
        var context = this;
        var args = arguments;
        if (!timer) {
          timer = setTimeout(function () {
            fn.apply(context, args);
            timer = null;
          }, wait);
        }
      };
    },
  },
  destroyed() {
    window.removeEventListener("scroll", this.throttle(), false);
  },
}
</script>
<style scoped>
.table {
  width: 100%;
  height: calc(100% - 60px - 80px - 10px - 40px);
  overflow-y: auto;
}

.card {
  list-style-type: none;
  display: grid;
  grid-template-columns: repeat(auto-fit, 314px);
  /* justify-content: center; */
  grid-gap: 16px;
  min-height: 200px;
  padding-left: 0 !important;
}
.card li{
  width: 320px;
  border-radius: 8px 8px 8px 8px;
  box-shadow: 4px 4px 8px 0px rgba(51,51,51,0.04);
  border: 1px solid #EEEEEE;
  margin: 0 !important;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
  padding: 26px 16px 16px;
  font-size: 14px;
  position: relative;
}
li .title{
  width: 147px;
  height: 20px;
  background: url(~@/assets/images/renyuan-title.svg) no-repeat;
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  line-height: 20px;
  color: #fff;
}
.search_box {
  display: flex;
}
.search_item {
  margin-bottom: 18px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  line-height: 32px;
}
.search_label {
  width: 88px;
  font-size: 14px;
  font-weight: 700;
  color: #606266;
}
.search_button {
  line-height: 30px;
}
</style>
