<template>
  <div>
    <el-dialog
      title="厂家要求值绑定"
      :visible.sync="isShow"
      width="800px"
      @close="$emit('closeBindPartDialog')"
    >
      <div
        class="body"
        v-if="isShow"
        style="height: 500px; overflow-y: auto; padding: 5px 0"
      >
        <el-table
          ref="bindPartTable"
          :data="bindPartData"
          v-loading="bindPartTableLoading"
          highlight-current-row
          style="width: 100%"
        >
          <el-table-column
            type="index"
            width="70"
            label="序号"
          ></el-table-column>
          <el-table-column
            property="supplierName"
            label="厂家"
            width="200"
          ></el-table-column>
          <el-table-column property="ask" label="要求值"></el-table-column>
          <el-table-column property="tell" label="要求描述"></el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click="updateBind(scope.row, 'edit')"
                >编辑</el-button
              >
              <el-button type="text" size="small" @click="deleteBind(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--      <div class="page" v-if="bindPartData.length > 0">-->
      <!--        <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page="page.current"-->
      <!--                       :page-size="20" layout="prev, pager, next, jumper"-->
      <!--                       :total="bindPartData.length">-->
      <!--        </el-pagination>-->
      <!--      </div>-->
      <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('closeBindPartDialog')">取 消</el-button>
        <el-button
          type="primary"
          @click="addBindPart('add')"
          :loading="addBindLoad"
          >新 增</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="厂家要求值绑定"
      :visible.sync="addBindSupplierDensityDialog"
      v-if="addBindSupplierDensityDialog"
      width="400px"
      @close="closeBindPartDialog"
      :close-on-click-modal="false"
    >
      <div class="body" v-if="addBindSupplierDensityDialog">
        <el-form
          label-position="right"
          label-width="100px"
          ref="bindSupplierDensityData"
          :rules="bindPartDataRules"
          :model="bindSupplierDensityData"
        >
          <el-form-item label="厂家：" prop="supplierName">
            <el-input
              v-model="bindSupplierDensityData.supplierName"
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="要求值：" prop="ask">
            <el-input
              v-model="bindSupplierDensityData.ask"
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="要求描述：" prop="tell">
            <el-input
              v-model="bindSupplierDensityData.tell"
              size="small"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeBindPartDialog">取 消</el-button>
        <el-button type="primary" @click="submitBind" :loading="bindLoad"
          >确 认</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  selectSupplierAsk,
  addProductSupplierAsk,
  updateProductSupplierAsk,
  deleteProductSupplierAsk,
} from "@/api/standard/standardLibrary";

export default {
  name: "bindSupplierDensityDialogAsk",
  props: {
    bindSupplierDensityDialog: {
      type: Boolean,
      default: () => false,
    },
    currentRow: {
      // 选择本条数据的信息
      type: Object,
      default: () => {},
    },
  },
  data() {
    // 这里存放数据
    return {
      isShow: this.bindSupplierDensityDialog,
      bindPartData: [],
      addBindSupplierDensityDialog: false,
      bindSupplierDensityData: {
        supplierName: null, // 厂家
        ask: null,
        tell: null,
        supplierAskId: null,
      },
      bindPartDataRules: {
        supplierName: [
          { required: true, message: "请填写厂家名称", trigger: "blur" },
        ],
        ask: [{ required: true, message: "请填写要求值", trigger: "blur" }],
        tell: [{ required: true, message: "请填写要求描述", trigger: "blur" }],
      },
      bindLoad: false,
      upIndex: 0,
      addBindLoad: false,
      entity: {
        productListId: "",
      },
      page: {
        current: 1,
        size: 20,
      },
      bindPartTableLoading: false,
      buttonType: "",
    };
  },
  mounted() {
    this.entity.productListId = this.currentRow.id;
    this.searchTableData();
  },
  // 方法集合
  methods: {
    searchTableData() {
      this.bindPartTableLoading = true;
      selectSupplierAsk(this.entity)
        .then((res) => {
          if (res.code === 200) {
            this.bindPartData = res.data;
          }
          this.bindPartTableLoading = false;
        })
        .catch((err) => {
          console.log(err);
          this.bindPartTableLoading = false;
        });
    },
    updateBind(row, type) {
      this.buttonType = type;
      this.addBindSupplierDensityDialog = true;
      this.bindSupplierDensityData = { ...row };
    },
    deleteBind(row) {
      this.$confirm("是否删除当前数据?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteProductSupplierAsk({ supplierAskId: row.supplierAskId })
            .then((res) => {
              this.$message.success("删除成功");
              this.searchTableData();
            })
            .catch((e) => {
              this.$message.error("删除失败");
            });
        })
        .catch(() => {});
    },
    sizeChange(val) {
      this.page.size = val;
    },
    currentChange(val) {
      this.page.current = val;
    },
    addBindPart(type) {
      this.buttonType = type;
      this.addBindSupplierDensityDialog = true;
    },
    // 提交零件绑定
    submitBind() {
      this.$refs["bindSupplierDensityData"].validate((valid) => {
        if (valid) {
          // 根据类型判断是检验对象零件绑定还是产品维护零件绑定
          const str = JSON.stringify({
            productListId: this.entity.productListId,
            supplierName: this.bindSupplierDensityData.supplierName,
            ask: this.bindSupplierDensityData.ask,
            tell: this.bindSupplierDensityData.tell,
            supplierAskId: this.bindSupplierDensityData.supplierAskId,
          });
          this.bindLoad = true;
          if (this.buttonType === "add") {
            addProductSupplierAsk({ str })
              .then((res) => {
                this.bindLoad = false;
                if (res.code === 200) {
                  this.$refs["bindSupplierDensityData"].resetFields();
                  this.addBindSupplierDensityDialog = false;
                  this.$message.success("操作成功");
                  this.searchTableData();
                }
              })
              .catch((err) => {
                this.bindLoad = false;
                console.log(err);
              });
          } else {
            updateProductSupplierAsk({ str })
              .then((res) => {
                this.bindLoad = false;
                if (res.code === 200) {
                  this.$refs["bindSupplierDensityData"].resetFields();
                  this.addBindSupplierDensityDialog = false;
                  this.$message.success("操作成功");
                  this.searchTableData();
                }
              })
              .catch((err) => {
                this.bindLoad = false;
                console.log(err);
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    closeBindPartDialog() {
      this.$refs["bindSupplierDensityData"].resetFields();
      this.bindSupplierDensityData = {};
      this.addBindSupplierDensityDialog = false;
    },
  },
};
</script>

<style scoped>
.page {
  width: 100%;
  height: 30px;
  text-align: right;
  margin-top: 10px;
}
</style>
