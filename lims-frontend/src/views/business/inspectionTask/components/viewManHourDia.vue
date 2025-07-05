<template>
  <div>
    <el-dialog
      :visible.sync="editInspectionDia"
      title="查看工时"
      width="80%"
      @close="editInspectionDia = false"
    >
      <div>
        <el-form :model="entity" :inline="true">
          <el-form-item label="检验项" prop="outputWorkTime">
            <el-input v-model="entity.inspectionItem" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="getList0">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <lims-table :tableData="tableData0" :column="column0"
                  :page="page0" :tableLoading="tableLoading"
                  height="500" @pagination="pagination0"></lims-table>
      <span slot="footer" class="dialog-footer" v-if="insState == 99">
        <el-button @click="editInspectionDia = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="editAskDia" title="修改" width="50%">
      <el-form ref="form" :model="editForm" label-width="100px">
        <el-form-item label="检测人" prop="name">
          <el-select v-model="editForm.name" clearable size="small">
            <el-option v-for="item in responsibleOptions" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="产量工时" prop="outputWorkTime">
          <el-input v-model="editForm.outputWorkTime" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="日期" prop="dateTime">
          <el-date-picker
            v-model="editForm.dateTime"
            placeholder="选择日期"
            size="small"
            style="width: 100%;"
            type="date"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editAskDia = false">取 消</el-button>
        <el-button
          :loading="handleEditLoading"
          type="primary"
          @click="handleEdit"
        >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  getWorkingHoursByOrderId, updateWorkingHours,
} from "@/api/business/inspectionTask.js";
import {selectUserCondition} from "@/api/system/user";
export default {
  name: "EditInspectionItem",
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
  data() {
    // 这里存放数据
    return {
      editInspectionDia: false,
      entity: {
        orderId: 0,
        inspectionItem: ''
      },
      insState: 0,
      tableData0: [],
      tableLoading: false,
      column0: [
        { label: "检测人", prop: "name" },
        { label: "检测项分类", prop: "inspectionItemClass" },
        { label: "检测项", prop: "inspectionItem", width: 110 },
        { label: "检测子项", prop: "inspectionItemSubclass", width: 110  },
        { label: "样品编号", prop: "sample", width: 160 },
        { label: "委托单号", prop: "orderNo", width: 160 },
        { label: "电缆标识", prop: "cableTag" },
        { label: "产量工时", prop: "outputWorkTime" },
        { label: "工时分组", prop: "manHourGroup" },
        { label: "单价", prop: "price" },
        { label: "日期", prop: "dateTime" },
        { label: "周次", prop: "week" },
        { label: "星期", prop: "weekDay" },
        {
          dataType: "action",
          width: "80px",
          label: "操作",
          fixed: "right",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.editIns(row);
              },
              disabled: (row) => {
                return this.insState == 3 || this.insState == 5 || this.insState == 99
              },
            },
          ],
        },
      ],
      page0: {
        total: 0,
        size: 10,
        current: 1,
      },
      editAskDia: false,
      editForm: {
        id: "",
        name: "",
        outputWorkTime: "",
        inspectionValueType: "",
      },
      handleEditLoading: false,
      inspectionValueType: [],
      responsibleOptions: []
    };
  },
  mounted() {

  },
  // 方法集合
  methods: {
    showDialog(id, insState) {
      this.insState = insState
      this.editInspectionDia = true;
      this.entity.orderId = id;
      this.getList0();
    },
    getList0() {
      this.tableLoading = true;
      getWorkingHoursByOrderId({ ...this.entity, ...this.page0 })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData0 = res.data.records;
            this.page0.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    // 修改弹框
    editIns(row) {
      this.editAskDia = true;
      this.editForm = { ...row };
      this.getUserList()
    },
    // 提交修改
    handleEdit() {
      this.handleEditLoading = true;
      updateWorkingHours({...this.editForm})
        .then((res) => {
          this.handleEditLoading = false;
          this.$message.success("修改成功");
          this.editAskDia = false;
          this.getList0();
        })
        .catch(() => {
          this.handleEditLoading = false;
        });
    },
    // 确认工时
    handleSubmit () {
      this.editInspectionDia = false;
      this.$emit("submit");
    },
    pagination0({ page, limit }) {
      this.page0.current = page;
      this.page0.size = limit;
      this.getList0();
    },
    // 获取负责人信息接口
    getUserList() {
      selectUserCondition({ type: 1 }).then((res) => {
        this.responsibleOptions = res.data;
      })
    }
  },
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: space-between;
}
</style>
