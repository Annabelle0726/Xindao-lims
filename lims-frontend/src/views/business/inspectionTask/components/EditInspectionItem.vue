<template>
  <div>
    <el-dialog
      :visible.sync="editInspectionDia"
      title="修改检验项"
      width="80%"
      @close="editInspectionDia = false"
    >
      <div>
        <el-form :model="entity" :inline="true">
          <el-form-item label="检验项" prop="outputWorkTime">
            <el-input v-model="entity.inspectionItem" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="small" type="primary" @click="getTableData">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <lims-table
        :column="editColumn"
        :table-data="editTableData"
        :table-loading="editLoading"
        :page="page"
        height="560"
        @pagination="pagination"
      >
      </lims-table>
    </el-dialog>
    <el-dialog :visible.sync="editAskDia" title="修改" width="50%">
      <el-form ref="form" :model="editForm" label-width="100px">
        <el-form-item label="要求值">
          <el-input v-model="editForm.ask" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="要求描述">
          <el-input v-model="editForm.tell" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="检验值类型">
          <el-select
            v-model="editForm.inspectionValueType"
            clearable
            placeholder="请选择"
            size="small"
            style="width: 100%"
          >
            <el-option
              v-for="dict in inspectionValueType"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
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
  selectSampleAndProductByOrderId,
  updateProductOnPlan,
} from "@/api/business/inspectionTask.js";
export default {
  name: "EditInspectionItem",
  // import 引入的组件需要注入到对象中才能使用
  components: { limsTable },
  data() {
    // 这里存放数据
    return {
      editInspectionDia: false,
      entity: {
        id: 0,
        inspectionItem: ''
      },
      editColumn: [
        {
          label: "样品编号",
          minWidth: "160px",
          prop: "sampleCode",
        },
        {
          label: "样品名称",
          minWidth: "120px",
          prop: "sample",
        },
        {
          label: "检验项分类",
          minWidth: "120px",
          prop: "inspectionItemClass",
        },
        {
          label: "检验项",
          minWidth: "120px",
          prop: "inspectionItem",
        },
        {
          label: "检验子项",
          minWidth: "120px",
          prop: "inspectionItemSubclass",
        },
        {
          label: "条件",
          minWidth: "120px",
          prop: "radius",
        },
        {
          label: "电缆标识",
          minWidth: "120px",
          prop: "cableTag",
        },
        {
          label: "单位",
          prop: "unit",
        },
        {
          label: "要求值",
          minWidth: "300px",
          prop: "ask",
        },
        {
          label: "要求描述",
          minWidth: "300px",
          prop: "tell",
        },
        {
          label: "样品型号",
          minWidth: "120px",
          prop: "model",
        },
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
            },
          ],
        },
      ],
      editTableData: [],
      editLoading: false,
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      editAskDia: false,
      editForm: {
        id: "",
        ask: "",
        tell: "",
        inspectionValueType: "",
      },
      handleEditLoading: false,
      inspectionValueType: [],
    };
  },
  mounted() {
    this.getDicts("inspection_value_type").then((response) => {
      this.inspectionValueType = this.dictToValue(response.data);
    });
  },
  // 方法集合
  methods: {
    showDialog(id) {
      this.editInspectionDia = true;
      this.entity.id = id;
      this.getTableData();
    },
    // 获取列表数据
    getTableData() {
      this.editLoading = true;
      selectSampleAndProductByOrderId({
        ...this.entity,
        ...this.page,
      })
        .then((res) => {
          this.editLoading = false;
          this.editTableData = res.data.records;
          this.page.total = res.data.total;
        })
        .catch(() => {
          this.editLoading = false;
        });
    },
    // 修改弹框
    editIns(row) {
      this.editAskDia = true;
      this.editForm = { ...row };
    },
    // 提交修改
    handleEdit() {
      const params = {
        id: this.editForm.insProductId,
        ask: this.editForm.ask,
        tell: this.editForm.tell,
        inspectionValueType: this.editForm.inspectionValueType,
      };
      this.handleEditLoading = true;
      updateProductOnPlan(params)
        .then((res) => {
          this.handleEditLoading = false;
          this.$message.success("修改成功");
          this.editAskDia = false;
          this.getTableData();
        })
        .catch(() => {
          this.handleEditLoading = false;
        });
    },
    // 年度计划分页
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getTableData();
    },
  },
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: space-between;
}
</style>
