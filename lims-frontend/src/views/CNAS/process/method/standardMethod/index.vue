<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>

<template>
  <div class="capacity-scope">
    <div class="search">
      <div>
        <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
          <el-form-item label="标准编号" prop="code">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.code"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item label="标准名称" prop="name">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.name"
                      @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openAdd" v-if="addPower">新增</el-button>
      </div>
    </div>
    <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
      :height="'calc(100vh - 250px)'" @pagination="pagination"></lims-table>
    <!-- 新增/编辑 -->
    <el-dialog :title="title" :visible.sync="addDlog" width="500px">
      <el-form :model="addForm" ref="addForm" :rules="addRules" label-position="right" label-width="120px">
        <el-form-item label="领域" prop="field">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.field"></el-input>
        </el-form-item>
        <el-form-item label="标准编号" prop="code">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.code">
          </el-input>
        </el-form-item>
        <el-form-item label="检验对象" prop="structureTestObjectId">
          <el-cascader size="small" :options="tandardTree" v-model="addForm.structureTestObjectId" collapse-tags
            :props="{ multiple: true, checkStrictly: true }" clearable style="width: 100%"></el-cascader>
        </el-form-item>
        <el-form-item label="标准描述" prop="name">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.name">
          </el-input>
        </el-form-item>
        <el-form-item label="标准描述EN" prop="nameEn">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.nameEn">
          </el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input size="small" placeholder="请输入" clearable v-model="addForm.remark">
          </el-input>
        </el-form-item>
        <el-form-item label="资质" prop="qualificationId">
          <el-select v-model="addForm.qualificationId" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option v-for="item in qualificationList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否产品标准" prop="isProduct">
          <el-select v-model="addForm.isProduct" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option label="否" :value="0"> </el-option>
            <el-option label="是" :value="1"> </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="isUse">
          <el-select v-model="addForm.isUse" size="small" clearable placeholder="请选择" style="width: 100%">
            <el-option label="否" :value="0"> </el-option>
            <el-option label="是" :value="1"> </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDlog = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="submitProduct('addForm')">确 认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  selectStandardMethodList,
  getStandardTree2,
  delStandardMethod,
  addStandardMethod,
  upStandardMethod,
} from "@/api/cnas/process/method/standardMethod";
export default {
  name: 'StandardMethod',
  components: {
    limsTable,
  },
  data() {
    return {
      addPower: true,
      title: "新增",
      inPower: true,
      queryParams: {},
      tableData: [],
      column: [
        { label: "领域", prop: "field", width: "100px" },
        { label: "标准编号", prop: "code", width: "100px" },
        { label: "检验对象", prop: "structureTestObjectId", width: "140px" },
        { label: "标准描述", prop: "name", width: "100px" },
        { label: "标准描述EN", prop: "nameEn", width: "100px" },
        { label: "备注", prop: "remark" },
        { label: "资质", prop: "qualificationId" },
        {
          label: "是否产品标准",
          prop: "isProduct",
          width: "100px",
          dataType: "tag",
          formatData: (params) => {
            if (params == 0) {
              return "否";
            } else {
              return "是";
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "danger";
            } else {
              return "primary";
            }
          },
        },
        {
          label: "是否启用",
          prop: "isUse",
          formatData: (params) => {
            if (params == 0) {
              return "否";
            } else {
              return "是";
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return "danger";
            } else {
              return "primary";
            }
          },
        },
        { label: "创建人", prop: "createUserName" },
        { label: "创建时间", prop: "createTime" },
        { label: "更新人", prop: "updateUserName" },
        { label: "更新时间", prop: "updateTime" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.openEdit(row)
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 1,
      },
      tableLoading: false,
      addForm: {},
      addDlog: false,
      addLoading: false,
      qualificationList: [],
      tandardTree: [],
      addRules: {
        code: [{ required: true, message: "请输入标准编号", trigger: "blur" }],
        name: [{ required: true, message: "请输入标准描述", trigger: "blur" }],
        nameEn: [
          { required: true, message: "请输入标准描述EN", trigger: "blur" },
        ],
        isProduct: [
          { required: true, message: "请选择是否产品标准", trigger: "change" },
        ],
        isUse: [
          { required: true, message: "请选择是否启用", trigger: "change" },
        ],
      },
    };
  },
  mounted() {
    this.getQualificationList();
    this.selectTestObjectByName();
    this.getList();
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      selectStandardMethodList({ ...param })
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
    refreshTable() {
      this.page.current = 1;
      this.getList();
      this.selectTestObjectByName();
    },
    refresh() {
      this.selectTestObjectByName();
      this.page.current = 1;
      this.queryParams = {};
      this.getList();
    },
    openAdd() {
      this.title = "新增";
      this.addForm = {};
      this.addDlog = true;
    },
    openEdit(row) {
      this.title = "编辑";
      this.addDlog = true;
      this.addForm = this.HaveJson(row)
      this.addForm.structureTestObjectId = JSON.parse(
        this.addForm.structureTestObjectId
      );
    },
    getQualificationList() {
      this.getDicts("cnas_method_qualification").then((response) => {
        let arr = response.data.map((m) => {
          return {
            label: m.dictLabel,
            value: m.dictValue,
          };
        });
        this.qualificationList = arr;
      });
    },
    selectTestObjectByName() {
      getStandardTree2().then((res) => {
        res.data.forEach((a) => {
          this.cascaderFieldData(a);
        });
        this.tandardTree = res.data;
      });
    },
    cascaderFieldData(val) {
      if (val.children === undefined) {
        return;
      } else if (val.children.length == 0) {
        delete val.children;
      } else {
        val.children.forEach((a) => {
          this.cascaderFieldData(a);
        });
      }
    },
    submitProduct(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.addLoading = true;
          let addForm = JSON.parse(JSON.stringify(this.addForm));
          addForm.structureTestObjectId = JSON.stringify(
            addForm.structureTestObjectId
          );
          if (this.title == "新增") {
            addStandardMethod(addForm)
              .then((res) => {
                this.addLoading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.getList();
                this.addDlog = false;
              })
              .catch((err) => {
                this.addLoading = false;
              });
          } else {
            upStandardMethod(addForm)
              .then((res) => {
                this.addLoading = false;
                if (res.code != 200) {
                  return;
                }
                this.$message.success("提交成功");
                this.getList();
                this.addDlog = false;
              })
              .catch((err) => {
                this.addLoading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
    // 删除
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delStandardMethod({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.getList();
          });
        })
        .catch(() => { });
    },
  },
};
</script>
