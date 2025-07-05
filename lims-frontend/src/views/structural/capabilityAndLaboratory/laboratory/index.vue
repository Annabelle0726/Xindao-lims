<template>
  <div class="capacity-scope">
    <div>
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
        <el-form-item label="资质名称" prop="name">
          <el-select v-model="queryParams.name" placeholder="选择资质名称" size="small" @change="refreshTable(), list = []">
            <el-option v-for="dict in dict.type.cnas_method_qualification" :key="dict.value" :label="dict.label"
              :value="dict.value">
              {{ dict.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
          <el-button size="mini" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-row class="title">
        <el-col :span="12" style="text-align: left">
          <el-radio-group v-model="radio" @input="selectorSwitch" size="medium" fill="#409EFF">
            <el-radio-button :label="0">资质明细</el-radio-button>
            <el-radio-button :label="1">资质总览</el-radio-button>
          </el-radio-group>
        </el-col>
        <el-col :span="12" style="text-align: right" v-if="radio === 0">
          <el-button size="small" type="primary" @click="openAdd">资质更新</el-button>
          <el-button size="small" icon="el-icon-delete" @click="handleDel">删除</el-button>
        </el-col>
      </el-row>
    </div>
    <div v-if="radio === 0">
      <lims-table :tableData="tableData" :column="column" :isSelection="true"
        :handleSelectionChange="handleSelectionChange" @pagination="pagination" :height="'calc(100vh - 300px)'"
        :page="page" :tableLoading="tableLoading"></lims-table>
    </div>
    <div class="table" v-if="radio === 1" v-loading="pageLoading" @scroll="scrollFn">
      <el-row :gutter="16">
        <el-col :span="8" v-for="(m, i) in list" :key="i" :xs="24" :sm="12" :md="8" :lg="8" :xl="6"
          style="margin-bottom: 16px">
          <div class="table-item">
            <el-image style="
                width: 102px;
                height: 102px;
                margin-right: 20px;
                border-radius: 16px;
              " :src="javaApi + '/img/' + m.imageUrl">
              <div slot="error" class="image-error" style="
                  width: 100px;
                  height: 100px;
                  border-radius: 16px;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  border: 1px solid #eeeeee;
                ">
                <i class="el-icon-picture-outline" style="font-size: 30px; color: #666666"></i>
              </div>
            </el-image>
            <div class="table-item-right" style="flex: 1; font-size: 12px; color: #666666">
              <p style="line-height: 26px">
                资质名称：<span style="color: #3a7bfa">{{ m.name }}</span>
              </p>
              <p style="line-height: 26px">颁发时间：{{ m.recentlyTime }}</p>
              <p style="line-height: 26px">到期时间：{{ m.expireTime }}</p>
              <p>
                <span>状态：</span>
                <el-tag :type="m.state === 0 ? 'danger' : 'success'" size="small">{{ m.state === 0 ? "失效" : "有效"
                  }}</el-tag>
              </p>
            </div>
          </div>
        </el-col>
      </el-row>
      <div v-if="list.length < 1 && !pageLoading && !isLoding" style="
          color: #909399;
          font-size: 14px;
          text-align: center;
          margin-top: 200px;
        ">
        暂无数据
      </div>
      <div v-if="list.length > 0">
        <el-button v-if="isLoding" type="text" style="display: flex; margin: 0 auto; color: #909399">
          <i class="el-icon-loading" style="font-size: 20px"></i>
        </el-button>
        <el-button type="text" v-if="finishLoding"
          style="display: flex; margin: 0 auto; color: #909399">已经没有更多啦~</el-button>
      </div>
    </div>
    <el-dialog title="资质更新" :visible.sync="qualificationsConnectVisible" width="400px">
      <el-form ref="formDataRef" :model="formData" label-position="right" :rules="formDataRules" label-width="78px">
        <el-form-item label="资质名称" prop="name">
          <el-select v-model="formData.name" placeholder="请选择" style="width: 100%" size="small" clearable>
            <el-option v-for="dict in dict.type.cnas_method_qualification" :key="dict.value" :label="dict.label"
              :value="dict.value">
              {{ dict.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资质编码" prop="code">
          <el-input size="small" placeholder="请输入" clearable v-model="formData.code"></el-input>
        </el-form-item>
        <el-form-item label="颁发机构" prop="organization">
          <el-input size="small" placeholder="请输入" clearable v-model="formData.organization"></el-input>
        </el-form-item>
        <el-form-item label="资质说明" prop="explanation">
          <el-input size="small" placeholder="请输入" clearable v-model="formData.explanation"></el-input>
        </el-form-item>
        <el-form-item label="颁发时间" prop="dateOfIssuance">
          <el-date-picker style="width: 100%" v-model="formData.dateOfIssuance" type="datetime" size="small"
            format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" clearable placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="到期时间" prop="expireTime">
          <el-date-picker style="width: 100%" v-model="formData.expireTime" type="datetime" size="small"
            format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" clearable placeholder="选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="资质图片">
          <el-upload ref="upload" :action="uploadAction" :on-success="(m) => handleSuccessUpImg(m, 'imageUrl')"
            accept="image/jpg,image/jpeg,image/png" :multiple="false" :limit="1" :headers="headers"
            :on-change="beforeUpload" :on-error="onError">
            <el-button slot="trigger" size="small" type="primary">选取图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="资质附件">
          <el-upload ref="upload1" :action="uploadAction" :on-success="(m) => handleSuccessUpImg(m, 'fileUrl')"
            accept="image/jpg,image/jpeg,image/png,application/pdf,.doc,.docx" :headers="headers" :multiple="false"
            :limit="1" :on-change="beforeUpload1" :on-error="onError1">
            <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="qualificationsConnectVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmQualifications" :loading="loading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import {
  addCertificationDetail,
  delCertificationDetail,
  getCertificationDetail,
} from "@/api/structural/laboratory";
import { getToken } from "@/utils/auth";

export default {
  name: 'Laboratory',
  components: {
    limsTable,
  },
  dicts: ["cnas_method_qualification"],
  data() {
    return {
      radio: 0,
      queryParams: {
        name: "",
      },
      tableData: [],
      selection: [],
      column: [
        { label: "资质名称", prop: "name" },
        { label: "资质编码", prop: "code" },
        { label: "颁发机构", prop: "organization" },
        { label: "资质说明", prop: "explanation" },
        { label: "首次颁发时间", prop: "firstIssuanceDate" },
        { label: "最近颁发时间", prop: "latestIssuanceDate" },
        { label: "到期颁发时间", prop: "expireTime" },
        {
          dataType: "action",
          label: "操作",
          operation: [
            {
              name: "附件下载",
              type: "text",
              clickFun: (row) => {
                this.handleDownLoad(row);
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
      qualificationsList: [],
      qualificationsConnectVisible: false,
      formData: {},
      formDataRules: {
        name: [
          { required: true, message: "请填写资质名称", trigger: "change" },
        ],
        code: [{ required: true, message: "请填写资质编码", trigger: "blur" }],
        organization: [
          { required: true, message: "请填写颁发机构", trigger: "blur" },
        ],
        dateOfIssuance: [
          { required: true, message: "请选择颁发时间", trigger: "change" },
        ],
        expireTime: [
          { required: true, message: "请填写到期时间", trigger: "change" },
        ],
      },
      loading: false,
      pageLoading: false,
      isLoding: false, // 加载中，loading图标,默认为true
      finishLoding: false, // 加载完成，显示已经没有更多了
      currentPage: 1, // 当前页
      pageSize: 16, // 一页16条
      total: "",
      list: [],
      uploadAction: process.env.VUE_APP_BASE_API + "/deviceScope/uploadFile",
      headers: { Authorization: "Bearer " + getToken() },
    };
  },
  mounted() {
    this.refreshTable();
  },
  methods: {
    selectorSwitch(radio) {
      if (radio === 1) {
        this.list = [];
        this.refreshTable();
      }
    },
    refreshTable() {
      if (this.radio === 0) {
        this.tableLoading = true;
        getCertificationDetail({ ...this.page, ...this.queryParams })
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
      } else {
        if (this.currentPage > 1) {
          this.isLoding = true;
        } else {
          this.pageLoading = true;
        }
        if (this.list.length === 0) {
          window.addEventListener(
            "scroll",
            this.throttle(this.scrollFn, 20000)
          );
        }
        getCertificationDetail({
          current: this.currentPage,
          size: this.pageSize,
          ...this.queryParams,
        })
          .then((res) => {
            if (res.code === 200) {
              this.total = res.data.total;
              let list = res.data.records;
              if (list.length === 0) {
                this.finishLoding = true;
              } else {
                if (list.length < this.pageSize) {
                  this.finishLoding = true;
                }
                this.list = this.list.concat(list);
                if (this.total === this.list.length) {
                  this.finishLoding = true;
                }
              }
            }
            this.pageLoading = false;
            this.isLoding = false;
          })
          .catch((err) => {
            this.pageLoading = false;
            this.isLoding = false;
          });
      }
    },
    // 重置
    refresh() {
      if (this.radio === 0) {
        this.queryParams.name = "";
        this.page.size = 10;
        this.page.current = 1;
        this.refreshTable();
      } else {
        this.finishLoding = false;
        this.currentPage = 1;
        this.list = [];
        this.refreshTable();
      }
    },
    // 表格多选
    handleSelectionChange(selection) {
      this.selection = selection;
    },
    pagination(page) {
      this.page.size = page.limit;
      this.refreshTable();
    },
    // 资质明细批量删除
    handleDel() {
      if (this.selection.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      const delIds = [];
      this.selection.forEach((item) => {
        delIds.push(item.id);
      });
      delCertificationDetail({ ids: delIds.join(',') }).then((res) => {
        if (res.code !== 200) return;
        this.refreshTable();
        this.$message.success("删除成功");
      });
    },
    // 资质明细附件下载
    handleDownLoad(row) {
      if (!row.fileUrl) {
        this.$message.warning('暂无附件')
        return;
      }
      this.$download.saveAs(row.fileUrl, row.fileUrl);
    },
    openAdd() {
      this.qualificationsConnectVisible = true;
      this.$nextTick(() => {
        this.$refs.upload.clearFiles();
        this.$refs.upload1.clearFiles();
      });
      this.formData = {};
    },
    handleSuccessUpImg(response, name) {
      if (response.code === 200) {
        this.formData[name] = response.data.url;
      }
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error("上传文件不超过10M");
        this.$refs.upload.clearFiles();
        return false;
      } else {
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error("上传失败");
      this.$refs.upload.clearFiles();
    },
    beforeUpload1(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error("上传文件不超过10M");
        this.$refs.upload1.clearFiles();
        return false;
      } else {
        return true;
      }
    },
    onError1(err, file, fileList) {
      this.$message.error("上传失败");
      this.$refs.upload1.clearFiles();
    },
    confirmQualifications() {
      this.$refs["formDataRef"].validate((valid) => {
        if (valid) {
          this.loading = true;
          addCertificationDetail({ ...this.formData })
            .then((res) => {
              this.loading = false;
              this.$message.success("已提交");
              this.refreshTable();
              this.resetForm("formDataRef");
              this.qualificationsConnectVisible = false;
            })
            .catch((err) => {
              this.loading = false;
            });
        }
      });
    },
    // 滚动触底加载
    scrollFn() {
      let clientHeight = document.documentElement.clientHeight - 18; //可视区域
      let scrollHeight = document.body.scrollHeight; // 滚动文档高度
      let scrollTop = parseInt(document.documentElement.scrollTop); // 已滚动的高度
      let height = 300;
      if (
        scrollTop + clientHeight >= scrollHeight - height &&
        scrollHeight !== 0
      ) {
        if (
          !this.finishLoding &&
          this.currentPage * this.pageSize < this.total
        ) {
          this.currentPage = this.currentPage + 1;
          this.refreshTable();
        }
      } else {
        return false;
      }
    },
    throttle(fn, wait) {
      // 封装函数进行节流
      var timer = null;
      return function () {
        var context = this;
        var args = arguments;
        if (!timer) {
          timer = setTimeout(function () {
            fn.apply(context, args);
            timer = null;
          }, wait);
        }
      };
    },
  },
  destroyed() {
    window.removeEventListener("scroll", this.throttle(), false);
  },
};
</script>

<style scoped>
.title {
  height: 40px;
  line-height: 40px;
  margin-bottom: 10px;
}

.table-item {
  border-radius: 8px 8px 8px 8px;
  box-shadow: 4px 4px 8px 0px rgba(51, 51, 51, 0.04);
  border: 1px solid #eeeeee;
  box-sizing: border-box;
  padding: 14px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
