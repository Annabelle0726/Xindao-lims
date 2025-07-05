<template>
  <div class="ins_order_config">
    <div class="search_form">
      <div class="search_input">
        <el-radio-group v-model="currentTab" size="small" style="margin-right: 20px;" @input="changeTab">
          <el-radio-button label="绝缘">绝 缘</el-radio-button>
        </el-radio-group>
        <el-form :model="auxiliaryWireCore" ref="entity" size="small" :inline="true">
          <el-form-item label="芯数" prop="num">
            <el-select v-model="auxiliaryWireCore.num" allow-create
                       clearable
                       default-first-option
                       filterable
                       multiple
                       size="small">
              <el-option v-for="item in quantityList" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="检验标准" prop="standardMethodListId">
            <el-select v-model="auxiliaryWireCore.standardMethodListId" disabled placeholder="请选择检验标准"
                       size="small"
                       @change="(value)=>methodChange(value)">
              <el-option v-for="item in standards" :key="item.id" :label="item.code" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" @click="outConfig">返 回</el-button>
        <el-button size="small" type="primary" @click="save">保 存</el-button>
      </div>
    </div>
    <div class="table">
      <el-table ref="productTable" v-loading="getProductLoad" :data="productList"
                :row-class-name="tableRowClassName" border class="el-table" :height="'calc(100vh - 200px)'"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
                tooltip-effect="dark" @select="upProductSelect"
                @selection-change="selectProduct" @select-all="handleAll">
        <el-table-column :selectable="selectable" type="selection" width="65"></el-table-column>
        <el-table-column label="检验项分类" min-width="140" prop="inspectionItemClass" show-overflow-tooltip></el-table-column>
        <el-table-column label="检验项" min-width="140" prop="inspectionItem" show-overflow-tooltip>
          <template slot="header" slot-scope="scope">
            <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
              <span>检验项</span>
              <el-input
                v-if="active==1"
                v-model="inspectionItem"
                placeholder="请输入"
                size="mini"
                @input="searchFilterList"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="检验项子项" min-width="140" prop="inspectionItemSubclass" show-overflow-tooltip>
          <template slot="header" slot-scope="scope">
            <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
              <span>检验项子项</span>
              <el-input
                v-if="active==1"
                v-model="inspectionItemSubclass"
                placeholder="请输入"
                size="mini"
                @input="searchFilterList"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="要求描述" min-width="220px" prop="tell">
          <template slot-scope="scope">
            <el-input v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求描述"
                      size="small" type="textarea"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="要求值" min-width="220px" prop="ask">
          <template slot-scope="scope">
            <el-input v-model="scope.row.ask" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求值"
                      size="small" type="textarea"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="条件" min-width="140" prop="radius" show-overflow-tooltip>
          <template slot-scope="scope">
            <el-input v-if="!scope.row.inspectionItem.includes('高温压力试验')" v-model="scope.row.radius" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求描述"
                      size="small" type="textarea">
            </el-input>
            <el-select v-if="scope.row.inspectionItem.includes('高温压力试验')" v-model="scope.row.radius" clearable
                       placeholder="条件"
                       size="small">
              <el-option v-for="(a,i) in JSON.parse(scope.row.radiusList)" :key="i" :label="a" :value="a"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="试验方法" min-width="120" prop="methodS" show-overflow-tooltip>
          <template slot="header" slot-scope="scope">
            <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
              <span>试验方法</span>
              <el-input
                v-if="active==1"
                v-model="methodS"
                placeholder="请输入"
                size="mini"
                @input="searchFilterList"/>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="计量单位" prop="unit" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="单价" prop="price" show-overflow-tooltip width="100"></el-table-column>
        <el-table-column label="区间" min-width="120" prop="section" show-overflow-tooltip></el-table-column>
        <el-table-column label="子实验室" min-width="130" prop="sonLaboratory" show-overflow-tooltip></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { Tree } from 'element-ui'
import {selectsStandardMethodByFLSSM, selectStandardProductList} from "@/api/business/rawMaterialOrder";
export default {
  props: {
    active: {
      type: String,
      default: () => '0'
    },
    sampleSelectionList: {
      type: Array,
      default: () => []
    },
    isSpecial: {
      type: Boolean,
      default: () => false
    },
  },
  data(vm) {
    return {
      standardList: [],
      vaule0: '',
      currentTab: '绝缘',
      upIndex: 0,
      productList: [],
      getProductLoad: false,
      productIds: [],
      sample: [],
      tree: '',
      standards: [],
      auxiliaryWireCore: {
        standardMethodListId: null,
        insProduct: [],
        num: [],
        modelNum: ''
      },
      // sheath: {
      //   standardMethodListId: null,
      //   insProduct: []
      // },
      isAskOnlyRead: false,
      inspectionItem: null,
      inspectionItemSubclass: null,
      methodS: null,
      filters: [],
      quantityList: [],
    }
  },
  mounted() {
    this.$parent.sampleIds.forEach(a => {
      for (var i = 0; i < this.$parent.sampleList.length; i++) {
        if (this.$parent.sampleList[i].id == a) {
          this.sample.push(this.$parent.sampleList[i])
          if (this.$parent.sampleList[i].auxiliaryWireCore !== undefined && this.$parent.sampleList[i].auxiliaryWireCore !== null) {
            this.auxiliaryWireCore = this.$parent.sampleList[i].auxiliaryWireCore
          }
          // if(this.$parent.sampleList[i].sheath !== undefined && this.$parent.sampleList[i].sheath !== null){
          //   this.sheath = this.$parent.sampleList[i].sheath
          // }
          break
        }
      }
    })
    this.selectsStandardMethodByFLSSM2()
  },
  methods: {
    // 检验项列表筛选
    searchFilterList() {
      const vtw = {
        inspectionItem: this.inspectionItem, // 检验项
        inspectionItemSubclass: this.inspectionItemSubclass, // 检验项子项
        methodS: this.methodS, // 试验方法
      }
      const isHaveValue = Object.values(vtw).some(item => {
        return item
      })
      if (isHaveValue) {
        for (let i in vtw) {
          if (vtw[i]) {
            this.productList = this.productList0.filter((item) => {
              return item[i] && item[i].includes(vtw[i])
            })
          }
        }
      } else {
        // 没有查询条件时渲染所有数据
        this.productList = this.productList0
      }
    },
    // 要求值变化时
    requestChange(e, row,type) {
      this.sampleList.map(item => {
        if (this.sampleIds.indexOf(item.id) > -1) {
          item.insProduct.map(i => {
            if(i.id == row.id){
              if(row.repetitionTag){
                if(row.repetitionTag==i.repetitionTag){
                  i[type] = e
                }
              }else{
                if(!i.repetitionTag){
                  i[type] = e
                }
              }
            }
            return i
          })
        }
        return item
      })
    },
    outConfig() {
      this.$parent.auxiliaryShow = false
    },
    selectProduct(val) {
      this.productIds = []
      val.forEach(a => {
        this.productIds.push(a.id)
      })
    },
    upProductSelect(selection, row) {
      row.state = row.state == 1 ? 0 : 1
    },
    handleAll(e) {
      if (e.length > 0) {
        this.productList.map(m => {
          m.state = 1
          return m
        })
      } else {
        this.productList.map(m => {
          m.state = 0
          return m
        })
      }
      this.$nextTick(() => {
        this.$refs.productTable.doLayout()
      })
    },
    tableRowClassName({row, rowIndex}) {
      if (row.state === 0) {
        return '';
      }
      return 'warning-row';
    },
    selectable() {
      if (this.active > 1) {
        return false
      } else {
        return true
      }
    },
    selectsStandardMethodByFLSSM2() {
      this.standards = []
      selectsStandardMethodByFLSSM({
        tree: this.$parent.selectTree
      }).then(res => {
        try {
          if (res.data.standardMethodList.length == 0 && this.selectTree.split('-').length == 5) {
            let arr = this.selectTree.split('-')
            let arr0 = arr.slice(0, arr.length - 1)
            let selectTree = arr0.join('-').substring(0, arr0.join('-').length - 1)
            selectsStandardMethodByFLSSM({
              tree: selectTree
            }).then(ress => {
              this.standards = ress.data.standardMethodList
            })
          } else {
            this.standards = res.data.standardMethodList
          }
        } catch (e) {
        }
      })
      this.auxiliaryWireCore.standardMethodListId = this.sampleSelectionList[0].standardMethodListId
      // 查询检测标准下拉框选项
      if (this.auxiliaryWireCore.insProduct.length > 0) {
        this.productList = this.auxiliaryWireCore.insProduct
        setTimeout(() => {
          this.productList.forEach(a => {
            if (a.state == 1) this.toggleSelection(a)
          })
        }, 200)
      } else {
        this.methodChange(this.sampleSelectionList[0].standardMethodListId)
      }
    },
    methodChange(val) {
      if (val === null || val === '') return
      this.getProductLoad = true
      let standard = this.standards.find(a => a.id === val)
      this.isAskOnlyRead = standard != null && standard.code === '技术要求';
      let selectTreeList = this.$parent.selectTree.split(" - ")
      this.$parent.addObj.model && (selectTreeList[selectTreeList.length - 1] = this.$parent.addObj.model)
      const model = this.sampleSelectionList[0].model
      const cores = this.sampleSelectionList[0].cores
      const conductorMaterial = this.sampleSelectionList[0].conductorMaterial
      const conductorType = this.sampleSelectionList[0].conductorType
      const modelNum = this.sampleSelectionList[0].modelNum
      selectStandardProductList({
        model: this.$parent.addObj.model ? this.$parent.addObj.model : model,
        modelNum: modelNum,
        standardMethodListId: val,
        factory: selectTreeList.join(" - "),
        cores: cores,
        conductorMaterial: conductorMaterial,
        conductorType: conductorType,
        isCableTag: '1',
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        res.data.forEach(a => {
          a.state = 0
        })
        this.productList = res.data
        this.getProductLoad = false
      }).catch(err => {
        this.getProductLoad = false
      })
    },
    toggleSelection(row) {
      this.$refs.productTable.toggleRowSelection(row, true);
    },
    save() {
      if (this.auxiliaryWireCore.insProduct.length !== 0) {
        if (this.auxiliaryWireCore.num.length === 0) {
          this.$message.error('缺少芯数无法保存')
          return
        }
      }
      this.auxiliaryWireCore.insProduct = this.HaveJson(this.productList)
      this.auxiliaryWireCore.insProduct.forEach(a => {
        delete a.id
      })
      if (this.currentTab === '绝缘') {
        this.sample.forEach(a => {
          a.auxiliaryWireCore = this.auxiliaryWireCore
        })
      }
      this.$emit('goBackAdd2')
      this.$message.success('已保存')
    },
    changeTab(val) {
      if (val === '绝缘') {
        this.productList = this.auxiliaryWireCore.insProduct
      }
      setTimeout(() => {
        this.productList.forEach(a => {
          if (a.state == 1) this.toggleSelection(a)
        })
      }, 100)
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
  }
}
</script>

<style scoped>
.search_form {
  display: flex;
  justify-content: space-between;
}
.search_input {
  display: flex;
  justify-content: space-between;
}
.ins_order_config .has-gutter .el-table__cell .cell {
  line-height: 30px;
  background-color: #fafafa;
}

.ins_order_config .has-gutter .el-table__cell {
  background-color: #fafafa !important;
}

.ins_order_config .el-table__row .cell {
  font-size: 12px;
}

>>>.warning-row {
  color: #1890FF;
}
</style>
