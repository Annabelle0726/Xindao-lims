<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
          <el-form-item label="编号" prop="number">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.number"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="实验室" prop="laboratory">
            <el-select v-model="queryParams.laboratory" placeholder="全部" size="small" @change="refreshTable()" clearable>
              <el-option v-for="item in laboratoryList" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="部门" prop="department">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.department"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openAdd('新增')"
                   v-if="checkPermi(['performance:manHour:workTimeConfig:add'])">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 290px)'"
        :page="page" @pagination="pagination"></lims-table>
    </div>
    <!-- 新增/编辑 -->
    <el-dialog :title="title" :visible.sync="timeDia" width="500px">
      <el-form :model="timeForm" ref="timeForm" :rules="timeRules" label-position="right" label-width="120px">
        <el-form-item label="编号" prop="number">
          <el-input size="small" placeholder="请输入" clearable v-model="timeForm.number"></el-input>
        </el-form-item>
        <el-form-item label="辅助项目名称" prop="auxiliaryProject">
          <el-input size="small" placeholder="请输入" clearable v-model="timeForm.auxiliaryProject">
          </el-input>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input size="small" placeholder="请输入" clearable v-model="timeForm.department">
          </el-input>
        </el-form-item>
        <el-form-item label="实验室" prop="laboratory">
          <el-select v-model="timeForm.laboratory" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in laboratoryList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-select v-model="timeForm.unit" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in unitList" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="核准工时" prop="approvedWorkingHour">
          <el-input size="small" placeholder="请输入" clearable v-model="timeForm.approvedWorkingHour">
          </el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input size="small" placeholder="请输入" type="textarea" :rows="2" clearable v-model="timeForm.remarks">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="timeDia = false">取 消</el-button>
        <el-button :loading="uploading" type="primary" @click="submitProduct('timeForm')">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  selectAuxiliaryWorkingHours,
  insertAuxiliaryWorkingHours,
  obtainItemParameterList,
  upAuxiliaryWorkingHours,
  deleteAuxiliaryWorkingHours,
} from "@/api/performance/manHour";
export default {
  components: {
    limsTable,
  },
  dicts: ["sys_unit"],
  data() {
    return {
      laboratoryList: [],
      queryParams: {},
      tableData: [],
      column: [
        { label: "编号", prop: "number" },
        { label: "辅助项目名称", prop: "auxiliaryProject", width: "120px" },
        {
          label: "实验室",
          prop: "laboratory",
          dataType: "tag",
          formatData: (params) => {
            return this.laboratoryList.find((m) => m.value == params).label;
          },
        },
        { label: "单位", prop: "unit" },
        { label: "核准工时", prop: "approvedWorkingHour" },
        { label: "部门", prop: "department" },
        { label: "备注", prop: "remarks" },
        {
          dataType: "action",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.openAdd("编辑", row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "performance:manHour:workTimeConfig:edit",
                ]);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "performance:manHour:workTimeConfig:del",
                ]);
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 20,
        current: 0,
      },
      tableLoading: false,
      unitList: [],
      title: "新增",
      timeDia: false,
      timeForm: {},
      timeRules: {
        number: [{ required: true, message: "请输入编号", trigger: "blur" }],
        auxiliaryProject: [
          { required: true, message: "请输入辅助项目名称", trigger: "blur" },
        ],
        laboratory: [
          { required: true, message: "请选择实验室", trigger: "change" },
        ],
        unit: [{ required: true, message: "请选择单位", trigger: "change" }],
        approvedWorkingHour: [
          { required: true, message: "请输入核准工时", trigger: "blur" },
        ],
      },
      uploading: false,
    };
  },
  mounted() {
    this.obtainItemParameterList();
    this.selectEnumByCategoryForUnit();
    this.getList();
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      selectAuxiliaryWorkingHours({ ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData = res.data.records;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    refresh() {
      this.queryParams = {};
      this.page.current = 1;
      this.getList();
    },
    refreshTable() {
      this.page.current = 1;
      this.getList();
    },
    openAdd(title, row) {
      this.title = title;
      if (row) {
        this.timeForm = row;
        this.timeForm.laboratory = Number(this.timeForm.laboratory);
      } else {
        this.timeForm = {};
      }
      this.timeDia = true;
    },
    obtainItemParameterList() {
      obtainItemParameterList().then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.laboratoryName,
            value: a.id,
          });
        });
        this.laboratoryList = data;
      });
    },
    selectEnumByCategoryForUnit() {
      this.getDicts("sys_unit").then((response) => {
        this.unitList = response.data;
      });
    },
    submitProduct(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.uploading = true;
          if (this.title == "新增") {
            insertAuxiliaryWorkingHours(this.timeForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.refresh();
                this.timeDia = false;
              })
              .catch((err) => {
                this.uploading = false;
              });
          } else {
            upAuxiliaryWorkingHours(this.timeForm)
              .then((res) => {
                this.uploading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.refresh();
                this.timeDia = false;
              })
              .catch((err) => {
                this.uploading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteAuxiliaryWorkingHours({ id: row.id }).then((res) => {
            if (res.code == 200){
              this.$message.success("删除成功");
              this.refresh();
            }
          });
        })
        .catch(() => { });
    },
  },
};
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
