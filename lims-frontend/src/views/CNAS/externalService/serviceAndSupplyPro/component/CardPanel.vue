<template>
    <div class="card-container" @click="handleCard">
        <div class="card-panel" :class="[isActive == index ? 'isActive' : '']">
            <el-image
                style="width: 80%; height: 140px"
                :src="javaApi + '/img/' + data.logo"
                fit="fill"
            />
        </div>
        <div class="title">
            {{ data.supplierRef }}
        </div>
    </div>
</template>
<script>

export default {
    props: {
        data: {
            type: Object,
            default: () => {}
        },
        index: {
            type: Number,
            default: -1
        },
        isActive: {
            type: Number,
            default: -1
        }
    },
    data(){
        return {

        }
    },
    mounted() {
      // console.log(1111,this.data)
    },
    methods: {
        handleCard() {
            this.$emit('handleCard', this.data, this.index)
        }
    }
}
</script>
<style scoped>
.card-container  {
    margin: 10px 10px 10px 0;
    text-align: center;
}
.card-panel {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 90%;
    height: 180px;
    box-shadow: 0px 0px 20px 0px #0000001A;
    cursor: pointer;
    border-radius: 5px;
    border: 1px solid transparent;
}
.card-panel:hover {
    border: 1px solid #409EFF;
    background: #1D56C50D;
}
.isActive {
    border: 1px solid #409EFF;
    background: #1D56C50D;
}
.title {
    margin-top: 15px;
}
</style>
