<template>
  <div>
    <el-dialog v-loading="diaLoading" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="formDia" title="不符合项的分布" width="90%" @close="closeDia">
      <table border="1" cellspacing="10" class="table">
        <tr>
          <td>章节号</td>
          <th class="div-with-line">
            <span style="float: left;">要素</span>
            <span style="float: right;">部门</span>
          </th>
          <th>主任</th>
          <th>技术负责人</th>
          <th>质量负责人</th>
          <th>综合室</th>
          <th>试验室</th>
          <th>合计</th>
          <th>占%</th>
        </tr>
        <tr v-for="(item, index) in distributionDetailList" :key="item.value">
          <td>{{ item.chapterNumber }}</td>
          <th>{{ item.essentials }}</th>
          <th>
            <el-input-number v-model="item.director" :precision="0" clearable size="small"></el-input-number>
          </th>
          <th>
            <el-input-number v-model="item.technology" :precision="0" clearable size="small"></el-input-number>
          </th>
          <th>
            <el-input-number v-model="item.quality" :precision="0" clearable size="small"></el-input-number>
          </th>
          <th>
            <el-input-number v-model="item.comprehensive" :precision="0" clearable size="small"></el-input-number>
          </th>
          <th>
            <el-input-number v-model="item.testing" :precision="0" clearable size="small"></el-input-number>
          </th>
          <th>
            {{ item.total }}
          </th>
          <th>
            {{ (item.proportion ? item.proportion : 0) + '%' }}
          </th>
        </tr>
        <tr>
          <td> </td>
          <th>
            占比
          </th>
          <th>
            {{ (distributionProportion.director ? distributionProportion.director : 0) + '%' }}
          </th>
          <th>
            {{ (distributionProportion.technology ? distributionProportion.technology : 0) + '%' }}
          </th>
          <th>
            {{ (distributionProportion.quality ? distributionProportion.quality : 0) + '%' }}
          </th>
          <th>
            {{ (distributionProportion.comprehensive ? distributionProportion.comprehensive : 0) + '%' }}
          </th>
          <th>
            {{ (distributionProportion.testing ? distributionProportion.testing : 0) + '%' }}
          </th>
          <th>
            {{ distributionProportion.total ? distributionProportion.total : 0 }}
          </th>
          <th> </th>
        </tr>
      </table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDia">取 消</el-button>
        <el-button :loading="loading" type="primary" @click="handleEdit">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getInconsistentDistributionOne,
  addInconsistentDistribution,
  updateInconsistentDistribution
} from '@/api/cnas/process/nonconformingWork.js'
export default {
  name: 'formDia',
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  data() {
    // 这里存放数据
    return {
      formDia: false,
      diaLoading: false,
      loading: false,
      distributionDetailList: [],
      form: {

      },
      distributionProportion: {}
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    // 打开弹框
    openDia(type, row) {
      this.formDia = true
      this.operationType = type
      if (type !== 'add') {
        this.searchInfo(row)
      } else {
        this.selectEnumByCategory()
      }
    },
    // 查询详情
    searchInfo(row) {
      this.diaLoading = true
      getInconsistentDistributionOne({ distributionId: row.distributionId }).then(res => {
        this.diaLoading = false
        this.form = res.data
        this.distributionDetailList = this.form.distributionDetailList
        this.distributionProportion = this.form.distributionProportion
      }).catch(err => {
        console.log(err)
        this.diaLoading = false
      })
    },
    // 提交弹框数据
    handleEdit() {
      const date = new Date();
      const year = date.getFullYear();
      this.loading = true
      const internalPlan = this.HaveJson(this.form)
      internalPlan.distributionYear = year
      internalPlan.distributionDetailList = this.HaveJson(this.distributionDetailList)
      if (this.operationType === 'add') {
        addInconsistentDistribution(internalPlan).then(res => {
          this.loading = false
          this.$message.success('操作成功')
          this.closeDia()
        }).catch(err => {
          console.log('err---', err);
          this.loading = false
        })
      } else if (this.operationType === 'edit') {
        updateInconsistentDistribution(internalPlan).then(res => {
          this.loading = false
          this.$message.success('操作成功')
          this.closeDia()
        }).catch(err => {
          console.log('err---', err);
          this.loading = false
        })
      }
    },
    closeDia() {
      this.formDia = false
      this.$emit('closeDia')
    },
    selectEnumByCategory() {
      this.distributionDetailList = []
      // 要素
      this.getDicts("essential").then((response) => {
        let arr = this.dictToValue(response.data)
        arr.map((item) => {
          const obj = Object.assign({
            essentials: item.label,
            chapterNumber: item.value,
          })
          this.distributionDetailList.push(obj)
        })
      });
    },
  }
};
</script>

<style scoped>
>>>.el-dialog {
  margin: 6vh auto 50px !important;
}

>>>.el-dialog__body {
  max-height: 68vh;
  overflow-y: auto;
}

>>>.is-required {
  margin-bottom: 6px;
}

.table {
  width: 100%;
  margin-top: 20px;
}

.table td {
  width: 30px;
  text-align: center;
}

.table th {
  width: 70px;
  height: 70px;
  text-align: center;
}

.div-with-line {
  width: 70px;
  height: 70px;
  position: relative;
  /*overflow: hidden; /* 隐藏溢出内容 */
}

.div-with-line::after {
  content: '';
  position: absolute;
  bottom: 0;
  height: 1px;
  background-color: #000000;
  left: 50%;
  transform: translateX(-50%) rotate(45deg);
  transform-origin: center 50%;
  top: 50%;
  width: 100px;
}
</style>
