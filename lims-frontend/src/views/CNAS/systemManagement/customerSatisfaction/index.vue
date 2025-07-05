<template>
  <div class="capacity-scope">
    <div>
      <div class="search">
        <div>
          <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
            <el-form-item label="单位名称" prop="unitName" v-if="tabIndex === '0'">
              <el-input v-model="searchForm.unitName" clearable size="small"></el-input>
            </el-form-item>
            <el-form-item label="文件名称" prop="fileName" v-if="tabIndex === '1'">
              <el-input v-model="searchForm1.fileName" clearable size="small"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="searchList">查询</el-button>
              <el-button size="mini" @click="resetSearchForm">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <el-button v-if="tabIndex === '0'" size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
          <el-upload v-if="tabIndex === '1'" ref='upload' :action="action" :before-upload="beforeUpload"
                     :headers="uploadHeader" :on-error="onError" :on-success="handleSuccessUp" :show-file-list="false"
                     accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'>
            <el-button :loading="upLoading" size="small" type="primary">导入</el-button>
          </el-upload>
        </div>
      </div>
      <div class="table">
        <div class="table-tab">
          <el-radio-group v-model="tabIndex" @change="searchList" size="small">
            <el-radio-button label="0">客户满意度</el-radio-button>
            <el-radio-button label="1">综合分析</el-radio-button>
          </el-radio-group>
        </div>
        <div v-if="tabIndex === '0'">
          <limsTable :column="tableColumn" :height="'calc(100vh - 22em)'" :table-data="tableData"
                     :table-loading="tableLoading" @pagination="pagination"
                     :page="page">
          </limsTable>
        </div>
        <div v-if="tabIndex === '1'">
          <limsTable :column="tableColumn1" :height="'calc(100vh - 22em)'" :table-data="tableData1"
                     :table-loading="tableLoading1" @pagination="pagination0">
          </limsTable>
        </div>
      </div>
    </div>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.fileUrl"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
    <FormDialog v-if="formDialog" ref="formDialog" @closeFormDia="closeFormDia"></FormDialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import FormDialog from './components/formDialog.vue';
import filePreview from '@/components/Preview/filePreview.vue'
import {
  pageClientSatisfaction,
  pageAnalyseFile,
  delClientSatisfaction,
  delAnalyseFile,
} from '@/api/cnas/systemManagement/customerSatisfaction.js'

export default {
  name: 'CustomerSatisfaction',
  // import 引入的组件需要注入到对象中才能使用
  components: { filePreview, FormDialog, limsTable },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        unitName: '',
      },
      searchForm1: {
        fileName: '',
      },
      tabIndex: '0',
      tableColumn: [
        {
          label: '单位名称',
          prop: 'unitName',
          minWidth: '100'
        },
        {
          label: '日期',
          prop: 'fillDate',
          minWidth: '100'
        },
        {
          label: '姓名',
          prop: 'userName',
          minWidth: '100'
        },
        {
          label: '部门',
          prop: 'department',
          minWidth: '100'
        },
        {
          label: '联系电话',
          prop: 'contactNumber',
          minWidth: '100'
        },
        {
          label: '创建日期',
          prop: 'createTime',
          minWidth: '100',
        },
        {
          dataType: 'action',
          minWidth: '80',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              }
            }
          ]
        }
      ],
      tableData: [],
      tableLoading: false,
      page: {
        size: 20,
        current: 1,
        total: 0,
      },
      tableColumn1: [
        {
          label: '附件名称',
          prop: 'fileName',
          minWidth: '100'
        },
        {
          label: '创建人',
          prop: 'userName',
          minWidth: '100'
        },
        {
          label: '创建时间',
          prop: 'createTime',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '50',
          label: '操作',
          operation: [
            {
              name: '预览',
              type: 'text',
              clickFun: (row) => {
                this.handleLook(row)
              }
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.upload(row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delFile(row)
              }
            }
          ]
        }
      ],
      tableData1: [],
      tableLoading1: false,
      page1: {
        size: 20,
        current: 1,
        total: 0,
      },
      formDialog: false,
      upLoading: false,
      currentInfo: {},
      lookDialogVisible: false,
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      const entity = this.tabIndex === '0' ? this.searchForm : this.searchForm1
      const page = this.tabIndex === '0' ? this.page : this.page1
      if (this.tabIndex === '0') {
        this.tableLoading = true
        pageClientSatisfaction({ ...entity, ...page }).then(res => {
          this.tableLoading = false
          this.tableData = res.data.records
          this.page.total = res.data.total
        }).catch(err => {
          console.log('err---', err);
          this.tableLoading = false
        })
      } else {
        this.tableLoading1 = true
        pageAnalyseFile({ ...entity, ...page }).then(res => {
          this.tableLoading1 = false
          this.tableData1 = res.data.records
          this.page1.total = res.data.total
        }).catch(err => {
          console.log('err---', err);
          this.tableLoading1 = false
        })
      }
    },
    openFormDia(type, row) {
      this.formDialog = true
      this.$nextTick(() => {
        this.$refs.formDialog.openDia(type, row);
      })
    },
    closeFormDia() {
      this.formDialog = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.unitName = '';
      this.searchForm1.fileName = '';
      this.searchList()
    },
    // 导入流程
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        return false;
      } else {
        this.upLoading = true;
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.searchList()
      }
    },
    // 删除客户满意度
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delClientSatisfaction({ clientSatisfactionId: row.clientSatisfactionId }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 查看文件
    handleLook(row) {
      this.currentInfo = row
      this.lookDialogVisible = true
    },
    // 下载客户福建
    upload(row) {
      this.$download.saveAs(row.fileUrl, row.fileName);
    },
    // 删除客户分析附件
    delFile(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delAnalyseFile({ analyseFileId: row.analyseFileId }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchList()
        }).catch(err => {
          this.tableLoading = false
          console.log('err---', err);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 分页
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
    // 分页
    pagination0({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.searchList();
    },
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/clientSatisfaction/uploadAnalyseFile'
    }
  },
};
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}

.table-tab {
  margin: 0 0 20px 0;
}
</style>
