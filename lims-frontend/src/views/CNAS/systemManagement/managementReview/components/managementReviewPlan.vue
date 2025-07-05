<template>
  <div>
    <div class="search">
      <div>
        <el-form :model="searchForm" ref="searchForm" size="small" :inline="true">
          <el-form-item label="评审地点" prop="judgingLocation">
            <el-input v-model="searchForm.judgingLocation" clearable size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  size="mini" @click="searchList">查询</el-button>
            <el-button size="mini" @click="resetSearchForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="openFormDia('add')">新 增</el-button>
      </div>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 23em)'" :table-data="tableData"
        :table-loading="tableLoading" :page="page" @pagination="pagination">
      </limsTable>
    </div>
    <management-form-d-ia v-if="managementFormDIa" ref="managementFormDIa"
      @closeImplementDia="closeImplementDia"></management-form-d-ia>
    <el-dialog :visible.sync="listDialogVisible" title="文件查看" top="15vh" width="400px">
      <div style="max-height:60vh;overflow-y: auto;">
        <p v-for="(item, index) in fileList" :key="index">
          <span>{{ item.fileName }}</span>
          <el-button icon="el-icon-view" size="small" style="margin-left: 20px;" type="text"
            @click="lookFile(item.url, item.fileName)">预览</el-button>
          <el-button icon="el-icon-bottom" size="small" style="margin-left: 20px;" type="text"
            @click="handleDown0(item.url, item.fileName)">下载</el-button>
        </p>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="lookDialogVisible" fullscreen title="查看附件" top="5vh" width="800px">
      <filePreview v-if="lookDialogVisible" :currentFile="{}" :fileUrl="javaApi + '/word/' + currentInfo.url"
        style="height: 90vh;overflow-y: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import ManagementFormDIa from './managementFormDIa.vue';
import filePreview from "@/components/Preview/filePreview.vue";
import {
  addReviewProgramFile,
  selectReviewProgramFile,
  getPageReviewProgram,
  deleteReviewProgram,
  exportReviewProgram,
} from '@/api/cnas/systemManagement/managementReview.js'

export default {
  name: 'managementReviewPlan',
  // import 引入的组件需要注入到对象中才能使用
  components: { ManagementFormDIa, limsTable, filePreview },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        judgingLocation: '',
      },
      listDialogVisible: false,
      lookDialogVisible: false,
      fileList: [],
      currentInfo: {},
      tableColumn: [
        {
          label: '评审时间',
          prop: 'reviewTime',
        },
        {
          label: '评审地点',
          prop: 'judgingLocation',
        },
        {
          label: '评审目的',
          prop: 'judgingPurpose',
        },
        {
          label: '评审方式',
          prop: 'judgingMethod',
        },
        {
          label: '评审范围',
          prop: 'judgingScope',
        },
        // {
        //   dataType: 'tag',
        //   label: '批准状态',
        //   prop: 'approve',
        //   minWidth: '100',
        //   formatData: (params) => {
        //     if (params === 0) {
        //       return '不通过';
        //     } else if (params === 1) {
        //       return '通过';
        //     } else {
        //       return null;
        //     }
        //   },
        //   formatType: (params) => {
        //     if (params === 0) {
        //       return 'danger';
        //     } else if (params === 1) {
        //       return 'success';
        //     } else {
        //       return null;
        //     }
        //   }
        // },
        {
          dataType: 'action',
          minWidth: '110',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('edit', row);
              },
              disabled: (row) => {
                return !!row.approve
              },
            },
            {
              name: '上传',
              type: 'upload',
              multiple: true,
              limit: 20,
              accept: '.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png,.pdf',
              clickFun: async (row, file, fileList) => {
                const formData = new FormData();
                formData.append('file', file.raw); // 文件字段
                formData.append('id', row.id); // 文件名字段
                let res = await addReviewProgramFile(formData)
                if (res.code == 200) {
                  this.$message({ message: '上传成功', type: 'success' });
                  // this.searchList()
                  return
                } else {
                  this.$message({ message: '上传失败', type: 'error' });
                  return
                }
              },
              disabled: (row) => {
                return !!row.approve
              },
            },
            {
              name: '查看附件',
              type: 'text',
              clickFun: (row) => {
                selectReviewProgramFile({ id: row.id }).then(res => {
                  this.listDialogVisible = true;
                  this.fileList = res.data.fileList
                });
              },
            },
            {
              name: '批准',
              type: 'text',
              clickFun: (row) => {
                this.openFormDia('ratify', row);
              },
              disabled: (row) => {
                return !!row.approve
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delPlan(row)
              },
              disabled: (row) => {
                if (row.qualityStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row)
              }
            },
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
      managementFormDIa: false,
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      this.tableLoading = true
      getPageReviewProgram({
        judgingLocation: this.searchForm.judgingLocation,
        pages: this.page.current,
        size: this.page.size
      }).then(res => {
        this.tableLoading = false
        this.tableData = res.data.records
        this.page.total = res.data.total
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 新增，编辑，批准弹框
    openFormDia(type, row) {
      this.managementFormDIa = true
      this.$nextTick(() => {
        this.$refs.managementFormDIa.openDia(type, row)
      })
    },
    closeImplementDia() {
      this.managementFormDIa = false
      this.searchList()
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.judgingLocation = '';
      this.searchList()
    },
    // 删除
    delPlan(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        deleteReviewProgram({ id: row.id }).then(res => {
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
    handleDown(row) {
      exportReviewProgram({ id: row.id }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '评审计划.docx');
      })
    },
    lookFile(url, name) {
      this.currentInfo.url = url
      this.currentInfo.name = name
      this.lookDialogVisible = true
    },
    handleDown0(url, name) {
      if (!url) return this.$message.warning('文件未上传')
      this.$download.saveAs(url, name);
    }
  }
};
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
}
</style>
