<!-- 设备总览 -->
<template>
  <div class="role_manage">
    <div style="display: flex;justify-content: space-between;margin-top: 10px;">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 48px;font-size: 14px;font-weight: 700;color: #606266;">状态</span>
          <el-select v-model="entity.deviceStatus" placeholder="全部"
                     @change="currentPage = 1, keyMap = {}, list = [], finishLoding = false, refreshTable()"
                     size="small" clearable>
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">设备名称</span>
          <el-input size="small" placeholder="请输入" clearable v-model="entity.deviceName"
                    @keyup.enter.native="currentPage = 1, keyMap = {}, list = [], finishLoding = false, refreshTable()"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button size="mini" type="primary" @click="currentPage = 1, keyMap = {}, list = [], finishLoding = false, refreshTable()">查询</el-button>
          <el-button size="mini" @click="refresh()">重置</el-button>
        </div>
      </div>
    </div>
    <div class="table" v-loading="loading">
      <scroll-pagination @load="refreshTable()" :finishLoding="finishLoding" :list="list"
        v-if="list.length > 0 || loading">
        <ul class="card">
          <li v-for="(m, i) in list" :key="i">
            <el-image class="img" :src="javaApi + '/img/' + m.imageUpload">
              <div slot="error" class="image-error" style="width: 88px;
            height: 88px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid #EEEEEE;">
                <i class="el-icon-picture-outline" style="font-size:20px;color:#666666;"></i>
              </div>
            </el-image>
            <div class="list-left">
              <div class="list-left-info">
                <span class="label" style="white-space: nowrap;">设备名称：</span>
                <span style="color:#3A7BFA;
              font-size: 16px;
              display: inline-block;
              width: 110px;
              word-break: break-all;" class="text-truncate" :title="m.deviceName">{{ m.deviceName ? m.deviceName : '无'
                  }}</span>
              </div>
              <div class="list-left-info">
                <span class="label">规格型号：</span>
                <span style="color:#333333;font-size: 16px;">{{ m.specificationModel ? m.specificationModel : '无'
                  }}</span>
              </div>
              <div class="list-left-info">
                <span class="label">状态：</span>
                <el-tag :type="m.type" v-if="m.deviceStatusName">{{ m.deviceStatusName }}</el-tag>
                <span v-else style="color:#333333;font-size: 16px;">无</span>
              </div>
            </div>
            <div class="circles" :class="m.type">
              <div class="circle0 circle"></div>
              <div class="circle1 circle"></div>
              <div class="circle2 circle"></div>
              <div class="circle3 circle"></div>
            </div>
          </li>
        </ul>
      </scroll-pagination>
      <div v-if="list.length < 1 && !loading" style="color:#909399;font-size:14px;text-align: center;margin-top:200px">
        暂无数据
      </div>
    </div>
  </div>
</template>

<script>
import ScrollPagination from '@/components/index/scroll-paging.vue'
import {
  selectDeviceParameter,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  components: {
    ScrollPagination
  },
  data() {
    return {
      entity: {
        deviceStatus: null,
        deviceName: null,
      },
      options: [],
      list: [],
      currentPage: 1, // 当前页
      pageSize: 16, // 一页16条
      total: '',
      loading: true, // 组件loading的展示,默认为true
      finishLoding: false, // 加载完成，显示已经没有更多了
      keyMap: {},
      laboratoryNameIsNull: false,
    }
  },
  created() {
    this.selectEnumByCategory()
    this.keyMap = {};
    this.currentPage = 1;
    this.list = [];
    this.clickSidebar(this.clickNodeVal)
  },
  methods: {
    refreshTable() {
      const key = `_${this.currentPage}`
      const value = this.keyMap[key]
      // 如果value存在，表示缓存有值，那么阻止请求
      if (value) {
        return
      }
      // value不存在，表示第一次请求,设置占位
      this.keyMap[key] = 'temp'
      if (this.currentPage == 1) {
        this.loading = true
      }
      if (this.list.length == 0) {
        this.finishLoding = false;
      }
      selectDeviceParameter({
        laboratoryNameIsNull: this.laboratoryNameIsNull,
        current: this.currentPage,
        size: this.pageSize,
        ...this.entity
      }).then(res => {
        if (res.code == 200) {
          this.total = res.data.total
          let list = res.data.records.map(m => {
            switch (m.deviceStatus) {
              case 0:
                // 正常
                m.type = 'success'
                break;
              case 1:
                // 维修
                m.type = 'warning'
                break;
              case 2:
                // 停用
                m.type = 'danger'
                break;
              case 3:
                // 报废
                m.type = 'info'
                break;
            }
            this.options.forEach(j => {
              if (m.deviceStatus == j.value) {
                m.deviceStatusName = j.label
              }
            })
            return m;
          })
          if (list.length == 0) {
            this.finishLoding = true;
          } else {
            if (list.length < this.pageSize) {
              this.finishLoding = true;
            }
            this.list = this.list.concat(list)
            if (this.total == this.list.length) {
              this.finishLoding = true;
            }
            this.currentPage++;
          }
        }
        this.loading = false
      })
    },
    refresh() {
      this.keyMap = {};
      this.currentPage = 1;
      this.list = [];
      this.finishLoding = false;
      this.entity = {
        deviceStatus: null,
        deviceName: null,
      };
      this.refreshTable()
    },
    // 获取字典
    selectEnumByCategory() {
      // 设备状态
      this.getDicts("device_status").then((response) => {
        this.options = this.dictToValue(response.data);
      });
    },
    // 点击侧边栏刷新
    clickSidebar(clickNodeVal) {
      this.laboratoryNameIsNull = false
      // 是否存在value，存在value代表为三级
      if (!clickNodeVal.value) {
        this.finishLoding = false;
        this.keyMap = {};
        this.currentPage = 1;
        this.list = [];
        this.entity.laboratoryName = null
        this.entity.storagePoint = null
        // 等于1代表为树的一级，label为部门
        if (clickNodeVal.label == '其他') {
          this.laboratoryNameIsNull = true
          this.refreshTable();
          return
        }
        if (clickNodeVal.level == 1) {
          this.entity.laboratoryName = clickNodeVal.label
          // 等于二级。label为存储地点
        } else if (clickNodeVal.level == 2) {
          // 其他表示没有配置实验室，只配置了地点
          if (clickNodeVal.parent.label == '其他') {
            this.laboratoryNameIsNull = true
          } else {
            this.entity.laboratoryName = clickNodeVal.parent.label
          }
          this.entity.storagePoint = clickNodeVal.label
        }
        this.refreshTable();
      }
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      this.clickSidebar(newVal)
    }
  }
}
</script>
<style scoped>
.table {
  background-color: #fff;
  height: calc(100vh - 17em);
  /* padding: 10px; */
  overflow-y: auto;
}

.card {
  list-style-type: none;
  display: grid;
  grid-template-columns: repeat(auto-fit, 330px);
  justify-content: start;
  grid-gap: 16px;
  min-height: 300px;
  padding-left: 0;
}

.card li {
  width: 330px;
  height: 165px;
  border-radius: 8px 8px 8px 8px;
  border: 1px solid #EEEEEE;
  margin: 0 !important;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 16px;
  font-size: 14px;
  color: #666666;
  position: relative;
  overflow: hidden;
}

.card li .img {
  width: 90px;
  height: 90px;
  border-radius: 16px;
  margin-right: 10px;
}

.list-left .list-left-info {
  line-height: 36px;
  display: flex;
}

.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

.circles {
  position: absolute;
  width: 70px;
  height: 70px;
  right: 0;
  top: 0;
  z-index: 0;
}

.circles .circle {
  border-radius: 50%;
  opacity: 0.2;
}

.circle0,
.circle1,
.circle2 {
  width: 60px;
  height: 60px;
  position: absolute;
}

.circle3 {
  width: 16px;
  height: 16px;
  position: absolute;
}

.circle0 {
  bottom: 6px;
  left: 0;
}

.circle1 {
  bottom: 0;
  right: -30px;
}

.circle2 {
  left: 0;
  top: -30px;
}

.circle3 {
  top: 0;
  right: 0;
}

.success .circle {
  background: #34BD66;
}

.danger .circle {
  background: #FF3838;
}

.warning .circle {
  background: #FBB247;
}

.info .circle {
  background: #909399;
}

.el-form-item {
  margin-top: 11px;
  margin-bottom: 11px;
}
</style>
