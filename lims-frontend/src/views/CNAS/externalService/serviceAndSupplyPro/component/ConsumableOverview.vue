<template>
  <div>
    <el-row class="card-box">
      <el-col :span="4" v-for="(item, index) in cardList" :key="index">
        <CardPanel
          :isActive="isActive"
          :data="item"
          :index="index"
          @handleCard="handleCard"
        />
      </el-col>
      <el-col :span="24" v-if="cardList.length==0" style="color: #909399;font-size: 14px;text-align: center;margin-top: 20px;">暂无数据</el-col>
    </el-row>
    <div class="title">耗材信息</div>
    <limsTable
      style="margin-top: 18px; padding: 0 15px;"
      :height="'20vh'"
      :column="columns"
      :table-data="tableData"
    >
    </limsTable>
  </div>
</template>
<script>
import CardPanel from './CardPanel.vue';
import { procurementSuppliesList } from "@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro"
import limsTable from '@/components/Table/lims-table.vue'

export default {
    components: { CardPanel, limsTable },
    props: {
        contentsId: {
            type: Number,
            default: 0
        }
    },
    data() {
        return {
            isActive: -1,
            columns: [
                {
                    label: "耗材编号",
                    prop: "itemNumber"
                },
                {
                    label: "耗材名称",
                    prop: "consumablesName"
                },
                {
                    label: "耗材类型",
                    prop: "consumablesType"
                },
                {
                    label: "规格",
                      prop: "specifications"
                },
                {
                    label: "单位",
                    prop: "unit"
                },
                {
                    label: "单价",
                    prop: "referencePrice"
                },
                {
                    label: "当前库存",
                    prop: "currentAmount"
                },
                {
                    label: "负责人",
                    prop: "personInChargeName"
                },
                {
                    label: "上次更新时间",
                    prop: "updateTime"
                }
            ],
            cardList: [],
            tableData: []
        }
    },
    watch: {
        contentsId(newVal, oldVal) {
            this.getTableData(newVal)
        }
    },
    mounted() {
        this.getTableData(this.contentsId)
    },
    methods: {
        handleCard(data, index) {
            this.isActive = index
            this.tableData = [data]
        },
        async getTableData(id) {
          procurementSuppliesList({contentsId: id}).then(res => {
            if(res.code == 200) {
              this.cardList = res.data.records.map(m=>{
                m.logo = m.consumablesIcon
                return m
              })
            }
          })
        }
    }
}
</script>
<style scoped>
.title {
  position: relative;
  font-size: 18px;
  color: #333;
  font-weight: 400;
  padding-left: 10px;
  margin-left: 15px;
}

.title::before {
  position: absolute;
  left: 0;
  top: 4px;
  content: '';
  width: 4px;
  height: 18px;
  background-color: #3A7BFA;
  border-radius: 2px;
}
.card-box {
    width: 100%;
    padding-left: 5px;
    padding-right: 5px;
    height: 30vh;
    overflow-y: auto;
}
</style>
