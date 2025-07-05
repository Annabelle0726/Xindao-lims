<template>
  <div>
    <el-dialog :visible.sync="isShow" title="标签打印" top="5vh" width="600px" @close="$emit('closePrintDialog')">
      <div style="width:100%;height: 400px;overflow-y: auto;text-align: left">
        <div class="dia_body">
          <el-checkbox
            v-model="checkAll"
            :indeterminate="isIndeterminate"
            style="margin: 10px 5px;"
            @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="checkIndexList" @change="changePrintCode()">
            <el-card v-for="(item, i) in barcodeData" :key="i" class="box-card" style="margin-bottom: 15px; font-size: 16px !important;">
              <el-checkbox :key="i" :label="i" style="position: relative;top:0;left:10px"><br></el-checkbox>
              <div>
                <div class="titleH1" style="text-align: center; margin-bottom: 2px;font-size: 16px">检测中心样品标识卡</div>
                <div style="text-align: center;">
                  <barcode :displayValue="false" :height="34" :value="item.barcode" :width="2"></barcode>
                </div>
                <div style="margin-left: 20px;text-align: left">
                  <div class="item">
                    <span class="full-title">样品名称</span>:
                    <span class="info">{{ item.sampleView }}</span>
                  </div>
                  <div class="item">
                    <span class="full-title">生产单位</span>:
                    <span class="info">{{ item.production }}</span>
                  </div>
                  <div class="item2">
                    <span class="full-title">规格型号</span>:
                    <span class="info">{{ item.sampleModel }}</span>
                  </div>
                  <div class="item">
                    <span class="full-title">委托日期</span>:
                    <span class="info">{{ item.sendTime }}</span>
                  </div>
                  <div class="item">
                    <span class="full-title">委托人</span>:
                    <span class="info2">{{ item.prepareUser }}</span>
                    <span class="full-title">检测编号</span>:
                    <span class="info">{{ item.entrustCode }}</span>
                  </div>
                  <div class="item">
                    <span class="full-title">样品数量</span>:
                    <span class="info2">{{ item.testQuantity }}</span>
                  </div>
                  <div style="font-weight: bold;display: flex;align-items: center;">
                    <span class="full-title">样品状态</span>:
                    <el-radio-group v-model="item.insState" v-removeAriaHidden style="margin-top: 7px;margin-left: 4px;">
                      <el-radio :label="0" style="font-weight: bold;margin-right: 7px;">待检</el-radio>
                      <el-radio :label="1" style="font-weight: bold;margin-right: 7px;">在检</el-radio>
                      <el-radio :label="2" style="font-weight: bold;margin-right: 7px;">已检</el-radio>
                    </el-radio-group>
                    <span>
                      <el-radio v-model="item.isLeave"
                                :label="true"
                                size="small" style="margin-left: 14px;margin-top: 3px;" @click.native.prevent="changeIsLeave(item)">留样</el-radio>
                    </span>
                  </div>
                </div>
              </div>
            </el-card>
          </el-checkbox-group>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="$emit('closePrintDialog')">取 消</el-button>
          <el-button type="primary" @click="submitPrint">打 印</el-button>
        </el-row>
      </span>
    </el-dialog>
    <div class="el-dialog-body" style="overflow-y: auto;margin-top: 0;position: fixed;top: 20px;right: 10px;z-index: 99999;">
      <div id="printOrder" ref="printOrder" class="printOrder">
        <el-card v-for="(item, i) in checkDataList" :key="i" class="box-card" style="font-size: 0.29cm !important;font-weight: 700;page-break-after: always;color: #000;box-shadow: none;margin: 0 !important;padding: 0 !important;">
          <div>
            <div class="titleH1" style="text-align: center;margin-bottom: 1px">检测中心样品标识卡</div>
            <div style="text-align: center;">
              <barcode :displayValue="false" :height="22" :value="item.barcode" :width="1.6"></barcode>
            </div>
            <div style="margin-left: 12px;text-align: left">
              <div class="item">
                <span class="full-title4">样品名称:</span>
                <span class="info4">{{ item.sampleView }}</span>
              </div>
              <div class="item">
                <span class="full-title2">生产单位</span>:
                <span class="info">{{ item.production }}</span>
              </div>
              <div class="item2">
                <span class="full-title4">规格型号:</span>
                <span class="info4">{{ item.sampleModel }}</span>
              </div>
              <div class="item">
                <span class="full-title2">委托日期</span>:
                <span class="info">{{ item.sendTime }}</span>
              </div>
              <div class="item">
                <span class="full-title2">委托人</span>:
                <span class="info3">{{ item.prepareUser }}</span>
                <span class="full-title2">检测编号</span>:
                <span class="info">{{ item.entrustCode }}</span>
              </div>
              <div class="item">
                <span class="full-title2">样品数量</span>:
                <span class="info3">{{ item.testQuantity }}</span>
              </div>
              <div>
                <span class="full-title2">样品状态</span>:
                <span style="white-space: nowrap;margin-left: 2px">
                  待检<span v-if="item.insState!==0" class="scor"></span><span v-if="item.insState===0" class="checked">√</span>
                  在检<span v-if="item.insState!==1" class="scor"></span><span v-if="item.insState===1" class="checked">√</span>
                  已检<span v-if="item.insState!==2" class="scor"></span><span v-if="item.insState===2" class="checked">√</span>
                  留样<span v-if="!item.isLeave" class="scor"></span><span v-if="item.isLeave" class="checked">√</span>
                </span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import PrintJS from "print-js";
import {labelOrderPrinting} from "@/api/business/productOrder";

export default {
  name: "printDialog",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    printDialog: {
      type: Boolean,
      default: () => false
    },
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.printDialog,
      loadPint: false,
      isIndeterminate: false, // 多选框样式
      checkAll: false,
      checkIndexList: [], // 选择要打印的数据
      barcodeData: [],
      printLoading: false,
      checkDataList: []
    }
  },
  // 方法集合
  methods: {
    // 获取要打印的数据
    getLabelPrinting(selection) {
      try {
        this.loadPint = true;
        let ids = []
        selection.map(m => {
          ids.push(m.id)
        })
        labelOrderPrinting({
          ids: ids
        }).then(res => {
          if (res.code === 200 && res.data.length > 0) {
            res.data.forEach(item => {
              item.sendTime = item.sendTime && item.sendTime.substring(0, 10)
              item.sampleNumber = item.qtyArrived + item.buyUnitMeas
              this.$set(item, 'barcode', item.entrustCode)
              this.$set(item, 'isLeave', item.insState === '2')
            })
            this.barcodeData = res.data
          }
        })
      } catch (e) {
        console.log('获取要打印的数据---', e)
      }

    },
    changeIsLeave(item) {
      const index = this.barcodeData.findIndex(val => val.entrustCode === item.entrustCode)
      if (index > -1) {
        this.barcodeData[index].isLeave = !this.barcodeData[index].isLeave
      }
    },
    // 全选多选框回调
    handleCheckAllChange(val) {
      if (val) {
        for (var i = 0; i < this.barcodeData.length; i++) {
          this.checkIndexList.push(i)
        }
        this.checkDataList = this.barcodeData
      } else {
        this.checkIndexList = []
        this.checkDataList = []
      }
      this.isIndeterminate = false;
    },
    changeType(type) {
      type = type === '1' ? '0' : '1'
    },
    //选择要打印的二维码
    changePrintCode() {
      let indexList = this.checkIndexList
      let arr = []
      indexList.forEach(i => {
        if (i !== undefined) {
          arr.push(this.barcodeData[i])
        }
      })
      console.log('arr---', arr)
      this.checkDataList = arr
    },
    // 提交打印
    submitPrint() {
      if (this.checkDataList.length < 1) {
        this.$message.warning("请选择要打印的条形码")
        return
      }
      this.printDialogVisible = false;
      PrintJS({
        targetStyles: ["*"], // 使用dom的所有样式，很重要
        printable: 'printOrder',//页面
        type: "html",//文档类型
        maxWidth: 360,
        header: '',
        style:
          `@page {
            margin: 0.4cm;
            margin-right: 0.4cm;
            margin-top: 0.4cm;
            margin-bottom: 0.4cm;
            padding-bottom: 0px;
            size: 400px 75px collapse;
          }
          html{
            zoom:100%;

          }
          @media print{
            width: 400px;
            height: 75px;
            margin:0;
          }`,
        onPrintDialogClose: this.erexcel = false,
        font_size: '0.29cm',
      });
    },
  },
  watch: {
    printDialog(newVal) {
      if (!newVal) {
        this.barcodeData = []
        this.checkIndexList = []
        this.checkDataList = []
        this.isIndeterminate = true;
      }
    }
  },
}
</script>

<style scoped>
.item {
  margin-bottom: 4px;
}

.item2 {
  margin-bottom: 8px;
  vertical-align: top;
}

.full-title {
  display: inline-block;
  width: 80px;
  text-align-last: justify;
}

.full-title2 {
  display: inline-block;
  width: 50px;
  text-align-last: justify;
}

.full-title4 {
  display: inline-block;
  width: 53px;
  text-align-last: justify;
  vertical-align: top;
}

.info {
  margin-left: 2px;
}

.info2 {
  margin-left: 2px;
  margin-right: 30px;
  width: 80px;
  display: inline-block;
}

.info3 {
  margin-left: 2px;
  margin-right: 8px;
  width: 44px;
  display: inline-block;
}

.info4 {
  display: inline-block;
  margin-left: 2px;
  white-space: normal;
  width: 260px;
}

.checkboxInfo {
  display: inline-block;
  margin-left: 10px;
}

.scor {
  width: 0.06cm;
  height: 0.06cm;
  border-radius: 1px;
  border: 1px solid #000;
  display: inline-block;
  margin-right: 14px;
  margin-left: 4px;
}

.checked {
  margin-right: 14px;
  margin-left: 4px;
}

>>> .el-checkbox {
  margin-right: 10px;
}

>>> .el-card {
  border: none;
}

>>> .el-card__body {
  padding: 4px 2px 6px 14px;
}
</style>
