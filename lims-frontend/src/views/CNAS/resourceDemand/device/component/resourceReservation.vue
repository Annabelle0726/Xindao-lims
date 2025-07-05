<!-- 资源预定 -->
<template>
  <div class="">
    <div style="margin-top: 10px">
      <el-row class="title">
        <el-col :span="12" style="text-align: left;">预定总览</el-col>
        <el-col :span="12" style="text-align: left;padding-bottom:10px">
          <el-date-picker
          v-model="startTime"
          format="yyyy-MM-dd"
          placeholder="选择起始日期"
          size="mini"
          type="date"
          value-format="yyyy-MM-dd"/>
          至
          <el-date-picker
          v-model="endTime"
          format="yyyy-MM-dd"
          placeholder="选择结束日期"
          size="mini"
          type="date"
          value-format="yyyy-MM-dd"/>
          <el-button size="mini" type="primary" @click="ValidateAndQuery">查询</el-button>
        </el-col>
      </el-row>
    </div>
    <div class="table-container">
      <el-table :data="tableData" class="scrollable-table" style="width: 100%"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column label="序号"
                         type="index"
                         width="60">
        </el-table-column>
        <el-table-column label="设备" min-width="60" prop="deviceName"/>
        <el-table-column label="时间" min-width="60" prop="time"/>
        <el-table-column v-for="date in dates" :key="date" :label="date">
          <template #default="{ row }">
            <el-button v-if="!row[date]" size="mini" type="primary" @click="openModal(date, row)">
              {{ getDisplayText(row, date) }}
            </el-button>
            <span v-else></span>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="showModal" title="预定信息"
                 top="5vh" width="70%">
        <h4>
          <el-button v-if="isBeforeDate()" size="small" @click="cancelReservation(selectedRow)">取消预定</el-button>
          <el-button v-if="isBeforeDate()" size="small" type="primary" @click="openAdd()">新建预定</el-button>
        </h4>
        <el-table ref="table" :data="tableData2" style="width: 100%" tooltip-effect="dark"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55%">
          </el-table-column>
          <el-table-column label="序号" width="120">
            <template #default="{ row, $index }">
              <!-- 使用 $index 来获取行索引，通常从0开始，所以+1以符合常规序号习惯 -->
              {{ $index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="预定编号" prop="deviceNumber" width="120">
          </el-table-column>
          <el-table-column label="客户名称" prop="customerName" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="联系人" prop="linkPerson" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="联系电话" prop="phone" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="预定说明" prop="reservationSpecification" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="创建人" prop="name" show-overflow-tooltip>
          </el-table-column>
          <el-table-column label="创建时间" prop="createDate" show-overflow-tooltip>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
    <el-dialog :visible.sync="addVisiable" title="新建预定" top="5vh" width="40%">
      <el-form ref="addReservationForm" :model="addReservation" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预定设备:">
              <el-input v-model="addReservation.deviceName"></el-input>
            </el-form-item>
            <el-form-item label="客户名称:">
              <el-input v-model="addReservation.customerName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预定时间:">
              <el-input v-model="addReservation.reservationTime"></el-input>
            </el-form-item>
            <el-form-item label="流程编号:">
              <el-input v-model="addReservation.deviceNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系人:">
              <el-input v-model="addReservation.linkPerson"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话:" prop="phone">
              <el-input v-model="addReservation.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预定说明:" required>
              <el-input v-model="addReservation.reservationSpecification" type="textarea"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addVisiable = false">取 消</el-button>
        <el-button type="primary" @click="addRecord">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  data() {

    return {
      // startDate: new Date(),
      // endDate: new Date(new Date().setDate(new Date().getDate() + 3)),
      // startDate:'',
      // endDate:'',
      timeSlots: ['09:00-12:00', '13:00-18:00', '18:00-22:00'],
      dates: [],
      showModal: false,
      selectedDate: '',
      selectedTime: '',
      selectedDevice: '',
      tableData: [
        {
          id: '',
          deviceName: '',
          date: ''
        }
      ],
      tableData2: [],
      selectedReservationId: null, // 用于存储选中的ID
      selectedRow: null,
      addVisiable: false,
      laboratoryNameIsNull: false,
      currentPage: 1, // 当前页
      pageSize: 16, // 一页16条
      startTime: '',
      endTime: '',
      entity: {
        deviceName: null,
        laboratoryName: '',
        storagePoint: '',
      },
      addReservation: {
        deviceName: '',
        reservationTime: '',
        specificTime: '',
        customerName: '',
        deviceNumber: '',
        linkPerson: '',
        phone: '',
        reservationSpecification: ''
      },
      total: '',
      // loading:true,
      componentData: {
        entity: {
          largeCategory: null,
          orderBy: {
            field: 'id',
            order: 'asc'
          }
        },
      },
      yuyue: null,
      yuyuetime: '',
      rules: {
        phone: [
          {required: true, message: '请输入联系电话',
            trigger: 'blur'},
          {pattern: /^1[3456789]\d{9}$/,
            message: '请输入正确的手机号码',
            trigger: ['blur', 'change']
          }
        ]
      },
      appointment: ''
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      this.clickSidebar(newVal)
    }
  },

  created() {
    this.clickSidebar(this.clickNodeVal)
    this.initDate();
  },
  mounted() {
    this.getAllDeviceParam();

  },
  methods: {
    isBeforeDate() {
      let currentDate = this.$moment().format('YYYY-MM-DD')
      let currentTime = this.$moment().format('HH')
      const appointment = this.appointment && this.appointment.split('-')
      let appointment0 = ''
      let appointment1 = ''
      if (appointment !== '') {
        appointment0 = appointment[0].slice(0,2)
        appointment1 = appointment[1].slice(0,2)
      }
      if (currentDate < this.yuyuetime) {
        // 未来的日期，一定能新增预约
        return true
      } else if (currentDate === this.yuyuetime) {
        // 当天的日期，已经过了的时间不可取消和新增
        if (currentTime < appointment1) {
          return true
        } else if (currentTime > appointment1) {
          return false
        }
      }
    },
    getDisplayText(row, date) {
      if (!row || row.dateList === null || row.dateList === undefined || row.dateList === "") {
        return '预约';
      }
      var dateLst = row.dateList
      for (const dateLstElement of dateLst) {
        if (dateLstElement.date === date) {
          if (dateLstElement.value === 0) {
            return '预约';
          } else {
            return dateLstElement.value;
          }
        }
      }
      return '预约';
    },

    ValidateAndQuery() {
      if (!this.startTime || !this.endTime) {
        this.$message.error('请选择日期范围');
        return;
      }
      if((new Date(this.endTime)-new Date(this.startTime))/(1000*60*60*24) >= 10) {
        this.$message.error('只能预约10天以内');
        return;
      }
      if (new Date(this.startTime) > new Date(this.endTime)) {
        this.$message.error('开始日期不能大于结束日期');
        return;
      }
      this.query();
    },
    initDate(){
      if(this.startTime=='' || this.endTime=='' ||this.startTime==null||this.endTime==null){
      const daysAdd = 3;
      const now =  new Date();
      this.startTime = now.getFullYear() + "-" +
        String(now.getMonth() + 1).padStart(2, '0') + "-" +
        String(now.getDate()).padStart(2, '0');
        let endTime = new Date(now);
        endTime.setDate(now.getDate() + daysAdd);
        this.endTime = endTime.getFullYear() + "-" +
          String(endTime.getMonth() + 1).padStart(2, '0') + "-" +
          String(endTime.getDate()).padStart(2, '0');
      const start = new Date(this.startTime);
      const end = new Date(this.endTime);
      const tempDates = [];
      while (start<=end) {
        const year = start.getFullYear();
        const month = String(start.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以加1，并使用padStart补0
        const day = String(start.getDate()).padStart(2, '0'); // 使用padStart补0
        tempDates.push(`${year}-${month}-${day}`);
        start.setDate(start.getDate() + 1);
      }
      this.dates = tempDates;
      this.getStartTimeAndEndTime();
    }},
    query() {
      if (this.startTime && this.endTime) {
        const start = new Date(this.startTime);
        const end = new Date(this.endTime);
        const tempDates = [];
        while (start <= end) {
          const year = start.getFullYear();
          const month = String(start.getMonth() + 1).padStart(2, '0'); // 月份从0开始，所以加1，并使用padStart补0
          const day = String(start.getDate()).padStart(2, '0'); // 使用padStart补0
          tempDates.push(`${year}-${month}-${day}`);
          start.setDate(start.getDate() + 1);
        }
        this.dates = tempDates;
        this.getStartTimeAndEndTime();
      }
    },
    //查询时间
    getStartTimeAndEndTime(){
      //查询逻辑
      this.$axios.post(this.$api.deviceReservate.get + "?laboratoryNameIsNull=" + this.laboratoryNameIsNull + "&starttime=" + this.startTime + "&endtime=" + this.endTime, {
        page: {
          current: this.currentPage,
          size: this.pageSize
        },
        entity: this.entity
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        if (res.code == 200) {
          const devices = res.data.body;
          this.initTable(devices)
        }
      })
    },
    updateDates() {
      if (this.startTime && this.endTime) {
        const start = new Date(this.startDate);
        const end = new Date(this.endDate);
        const tempDates = [];
        while (start <= end) {
          const month = start.getMonth() + 1;
          const day = start.getDate();
          tempDates.push(`${month}/${day}`);
          start.setDate(start.getDate() + 1);
        }
        this.dates = tempDates;
        this.initTableData(devices);
      }
    },
    getAllDeviceParam() {
      this.$axios.post(this.$api.deviceReservate.get + "?laboratoryNameIsNull=" + this.laboratoryNameIsNull, {
        page: {
          current: this.currentPage,
          size: this.pageSize
        },
        entity: this.entity
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      }).then(res => {
        if (res.code == 200) {
          const devices = res.data.body;
          this.initTable(devices)
        }
      })
    },
    initTable(devices) {
      this.tableData = devices;
    },
    openModal(date, row) {
      this.yuyue = row;
      this.yuyuetime = date;
      this.appointment = row.time
      this.showModal = true;
      this.getList();
    },
    openAdd() {
      this.addVisiable = true;
      this.addReservation.deviceId = this.yuyue.id;
      // this.addReservation.deviceNumber = this.yuyue.deviceNumber;
      this.addReservation.deviceName = this.yuyue.deviceName;
      this.addReservation.reservationTime = this.yuyuetime + " " + this.yuyue.time;
      this.addReservation.specificTime = this.yuyue.time;
      this.addReservation.deviceNumber = 'TX-ABC-01'+Math.floor(Math.random() * 10 + 1);
    },
    handleSelectionChange(selected) {
      this.selectedRow = selected.length > 0 ? selected : null;
    },
    async cancelReservation(selectedRow) {
      if (!selectedRow || selectedRow.length == 0) {
        this.$message.error('请选择要删除的预约');
        return;
      }
      let ids = selectedRow.map(item => item.id).join(',');
      try {
        const res = await this.$axios.delete(this.$api.deviceReservate.del + '/' + ids);
        if (res.code === 200) {
          this.$message.success({
            message: '删除成功',
            type: 'success'
          });
          this.getList();
          this.query();
          this.showModal = false;
        } else {
          this.$message.error({
            message: '删除失败',
            type: 'error'
          });
        }
      } catch (error) {
        this.$message.error({
          message: '删除过程中发生错误，请稍后重试',
          type: 'error'
        });
      }
    },
    addRecord() {
      this.$refs.addReservationForm.validate((valid) => {
        if (valid) {
          this.addReservation.reservationTime = this.yuyuetime;
          this.$axios.post(this.$api.deviceReservate.save, this.addReservation).then(res => {
            if (res.code == 200) {
              this.$message.success('保存成功')
              this.addVisiable = false
              this.getList();
              this.query();
              this.addReservation = {};
            }
          })
        } else {
          this.$message.error({message: '请检查表单填写是否正确', type: 'error'});
          return false;
        }
      });
    },
    getList() {
      this.$axios.post(this.$api.deviceReservate.list + "?deviceId=" + this.yuyue.id + "&reservationTime=" + this.yuyuetime + "&specificTime=" + this.yuyue.time).then(res => {
        if (res.code == 200) {
          this.tableData2 = res.data;
        }
      })
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
          this.getAllDeviceParam();
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
        this.getAllDeviceParam();
      }
    }
  },

}
</script>

<style scoped>
.table-container {
  overflow: auto;
  height: 500px;
  /* 可以根据需要调整高度 */
}

.scrollable-table {
  max-height: 100%;
  overflow-y: auto;
  /* overflow-x: auto; */
}

h4 {
  font-weight: 400;
  font-size: 16px;
  display: flex;
  justify-content: flex-end;
  margin: 10px 0;
}

.form-row {
  display: flex;
  justify-content: space-between;
}
</style>
