<template>
  <div class="scroll-pagination"  ref="content" @scroll="onScroll">
    <slot></slot>
    <el-button
        v-if="!finishLoding&&isLoding"
        type="text"
        style="display: flex; margin: 0 auto; color: #909399"
        ><i class="el-icon-loading" style="font-size:20px"></i
      ></el-button>
    <el-button
      type="text"
      v-show="finishLoding"
      style="display: flex; margin: 0 auto; color: #909399"
      >已经没有更多啦~</el-button
    >
  </div>
</template>

<script>
export default {
  name: 'ScrollPagination',
  props: {
    finishLoding: {
      type:Boolean,
      default:false
    },
    list:{
      type:Array,
      default:()=>[]
    }
  },
  watch: {
    list:{
      deep:true,
      handler(){
        this.isLoding = false
      }
    }
  },
  data() {
    return {
      isLoding: false,
    }
  },
  created(){
    // 防抖
    this.onScroll = this.debounce(this.scrollFn,500);
    // 节流
    // this.onScroll = this.throttle(this.scrollFn,1000);
  },
  mounted(){
  },
  methods: {
    onScroll(){},
    scrollFn() {
      let content = this.$refs.content
      if (content.scrollTop + content.clientHeight+2 >= content.scrollHeight) {
        if(!this.finishLoding){
          this.loadMore()
        }else{
          this.isLoding = false
        }
      }
    },
    loadMore(){
      if (this.isLoding) return
      this.isLoding = true
      setTimeout(() => {
        this.$emit('load')
      }, 500)
    },
    debounce(func, delay) {
      let timer = null;
      return function () {
        if (timer) {
          clearTimeout(timer);
          timer = null;
        }
        timer = setTimeout(() => {
          func(...arguments);
        }, delay);
      };
    },
    throttle(func, delay) {
      let time = null;
      return function () {
        let args = Array.from(arguments);
        if (time === null) {
          time = setTimeout(() => {
            func(...args);
            clearTimeout(time);
            time = null;
          }, delay);
        }
      };
    }
  }
}
</script>

<style scoped>
.scroll-pagination {
  height: 100%;
  overflow-y: auto;
}
</style>
