<template>
  <div class="capacity-scope">
    <div class="search">
      <div class="search_box">
        <div class="search_item">
          <span class="search_label">模板名称</span>
          <el-input v-model="queryParams.name" clearable placeholder="请输入" size="small"
                    @keyup.enter.native="refreshTable()"></el-input>
        </div>
        <div class="search_button">
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </div>
      </div>
      <div>
        <el-button v-if="checkPermi(['standard:model:add'])" size="small" type="primary" @click="openAdd">新增</el-button>
      </div>
    </div>
    <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
      :height="'calc(100vh - 250px)'" @pagination="pagination"></lims-table>
    <el-dialog :before-close="isClose" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="isShow" title="模板编制" width="85%">
      <div v-if="isShow" style="width: 100%; height: 82vh; overflow: auto">
        <Excel v-loading="loading" :data="row.thing" :execlTitle="row.name"></Excel>
      </div>
    </el-dialog>
    <el-dialog :before-close="closeCopyTem" :close-on-click-modal="false" :close-on-press-escape="false"
      :visible.sync="isShowCopyTem" :title="title" width="35%" :modal-append-to-body="false">
      <el-form ref="copyForm" :model="copyForm" :rules="copyFormRules" label-position="right" label-width="80px">
        <el-form-item label="模版编号" prop="number">
          <el-input v-model="copyForm.number" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="模版名称" prop="name">
          <el-input v-model="copyForm.name" clearable size="small"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="copyForm.remark" clearable size="small"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeCopyTem">取 消</el-button>
        <el-button :loading="submitCopyInfoLoading" type="primary" @click="submitCopyInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import Excel from "@/components/Excel/luckysheet.vue";
import {
  selectStandardTemplatePageList,
  copyStandardTemplate,
  addStandardTemplate,
  upStandardTemplate,
  delStandardTemplate,
  getEditTemplatePreparation,
} from "@/api/standard/model";
export default {
  name: 'Model',
  components: {
    Excel,
    limsTable,
  },
  data() {
    return {
      addPower: true,
      isShow: false,
      loading: false,
      title: "新增",
      row: {
        id: null,
        thing: null,
        name: null,
      },
      isShowCopyTem: false,
      submitCopyInfoLoading: false,
      copyForm: {
        number: "",
        name: "",
        remark: "",
        id: "",
      },
      copyFormRules: {
        name: [{ required: true, message: "请输入模版名称", trigger: "blur" }],
      },
      queryParams: {
        name: null,
      },
      tableData: [],
      column: [
        { label: "模板编号", prop: "number" },
        { label: "模板名称", prop: "name" },
        { label: "备注", prop: "remark" },
        { label: "创建用户", prop: "createUserName" },
        { label: "创建时间", prop: "createTime", width: "160" },
        { label: "更新用户", prop: "updateUserName" },
        { label: "修改时间", prop: "updateTime", width: "160" },
        {
          dataType: "action",
          label: "操作",
          operation: [
            {
              name: "编辑",
              type: "text",
              clickFun: (row) => {
                this.title = "编辑";
                this.copyForm = this.HaveJson(row);
                this.isShowCopyTem = true;
              },
              showHide: (row) => {
                return this.checkPermi(["standard:model:edit"]);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              showHide: (row) => {
                return this.checkPermi(["standard:model:del"]);
              },
            },
            {
              name: "复制模板",
              type: "text",
              clickFun: (row) => {
                this.copyTemplate(row);
              },
              showHide: (row) => {
                return this.checkPermi(["standard:model:copy"]);
              },
            },
            {
              name: "模板编制",
              type: "text",
              clickFun: (row) => {
                this.templateWrite(row);
              },
              showHide: (row) => {
                return this.checkPermi([
                  "standard:model:edit",
                  "standard:model:add",
                ]);
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
    };
  },
  mounted() {
    window.excelClosed = this.confirmSave;
    window.returnView = this.returnView;
    this.getList();
  },
  methods: {
    getList() {
      this.tableLoading = true;
      let param = { ...this.queryParams, ...this.page };
      delete param.total;
      selectStandardTemplatePageList({ ...param })
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
    refreshTable(event) {
      if (event && typeof event.preventDefault === 'function') {
        event.preventDefault(); // 阻止默认行为
      }
      this.page.current = 1;
      this.getList();
    },
    refresh() {
      this.queryParams = {};
      this.page.current = 1;
      this.getList();
    },
    openAdd() {
      this.title = "新增";
      this.copyForm = {};
      this.isShowCopyTem = true;
    },
    // 复制模版
    copyTemplate(row) {
      this.title = "复制模版";
      this.isShowCopyTem = true;
      this.copyForm.id = row.id;
    },
    // 新增/编辑/复制模板
    submitCopyInfo() {
      this.$refs["copyForm"].validate((valid) => {
        if (valid) {
          this.submitCopyInfoLoading = true;
          const params = {
            id: this.copyForm.id,
            name: this.copyForm.name,
            number: this.copyForm.number,
            remark: this.copyForm.remark,
          };
          switch (this.title) {
            case "新增":
              delete params.id;
              addStandardTemplate(params)
                .then((res) => {
                  this.isShowCopyTem = false;
                  this.submitCopyInfoLoading = false;
                  this.$message.success("新增成功");
                  this.refreshTable("page");
                })
                .catch((err) => {
                  console.log("copyTemplate----", err);
                  this.submitCopyInfoLoading = false;
                });
              break;
            case "编辑":
              params.thing = this.copyForm.thing ? this.copyForm.thing : "";
              upStandardTemplate(params)
                .then((res) => {
                  this.isShowCopyTem = false;
                  this.submitCopyInfoLoading = false;
                  this.$message.success("修改成功");
                  this.refreshTable("page");
                })
                .catch((err) => {
                  console.log("copyTemplate----", err);
                  this.submitCopyInfoLoading = false;
                });
              break;
            case "复制模版":
              copyStandardTemplate(params)
                .then((res) => {
                  this.isShowCopyTem = false;
                  this.submitCopyInfoLoading = false;
                  this.$message.success("复制成功");
                  this.refreshTable("page");
                })
                .catch((err) => {
                  console.log("copyTemplate----", err);
                  this.submitCopyInfoLoading = false;
                });
              break;
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    closeCopyTem() {
      this.$refs.copyForm.resetFields();
      this.isShowCopyTem = false;
    },
    // 删除
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delStandardTemplate({ id: row.id }).then((res) => {
            this.$message.success("删除成功");
            this.refreshTable("page");
          });
        })
        .catch(() => { });
    },
    templateWrite(row) {
      getEditTemplatePreparation({ id: row.id }).then((res) => {
        if (res.code != 200) {
          return;
        }
        this.row = row;
        this.row.thing = res.data;
        this.isShow = true;
      });
    },
    returnView() {
      this.isShow = false;
    },
    confirmSave() {
      this.$confirm("是否需要保存?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.closed();
        })
        .catch(() => { });
    },
    closed() {
      this.loading = true;
      let data = luckysheet.toJson();
      delete data.title;
      delete data.container;
      delete data.lang;
      delete data.showsheetbar;
      delete data.showstatisticBarConfig;
      delete data.enableAddRow;
      delete data.enableAddBackTop;
      delete data.showtoolbarConfig;
      delete data.cellRightClickConfig;
      delete data.myFolderUrl;
      delete data.functionButton;
      if (data.data[0].config.borderInfo != undefined) {
        for (var i = 0; i < data.data[0].config.borderInfo.length; i++) {
          let str = data.data[0].config.borderInfo;
          if (str[i].rangeType === "range") {
            if (str[i].borderType === "border-none") {
              data.data[0].config.borderInfo.splice(i, 1);
              i -= 1;
            }
          }
        }
      }
      data.data[0].celldata.forEach((a) => {
        if (
          a.v.ps != undefined &&
          (a.v.ps.value === "检验值" ||
            a.v.ps.value === "设备名称" ||
            a.v.ps.value === "设备编码" ||
            a.v.ps.value === "结论")
        ) {
          if (a.v.v === undefined) {
            a.v.v = "";
          }
        }
      });
      upStandardTemplate({
        id: this.row.id,
        thing: JSON.stringify(data),
        name: luckysheet.getWorkbookName(["name"]),
      }).then((res) => {
        this.loading = false;
        this.$message.success("已保存");
        this.isShow = false;
      });
    },
    isClose(done) {
      this.$confirm("是否需要保存?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.closed();
        })
        .catch(() => {
          done();
        });
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
.search_box {
  display: flex;
}
.search_item {
  margin-bottom: 18px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  line-height: 32px;
}
.search_label {
  width: 88px;
  font-size: 14px;
  font-weight: 700;
  color: #606266;
}
.search_button {
  line-height: 26px;
}
</style>
