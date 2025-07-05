<template>
  <div class="capacity-scope">
    <div style="display: flex;justify-content: space-between">
      <div style="display: flex;">
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;" v-if="tabIndex === '0'">
          <span style="width: 68px;font-size: 14px;font-weight: 700;color: #606266;">标准号</span>
          <el-input size="small" placeholder="请输入" clearable v-model="searchForm.standardNo"
                    @keyup.enter.native="searchList"></el-input>
        </div>
        <div style="margin-bottom: 18px;margin-right: 10px;display: flex;align-items: center;line-height: 32px;" v-if="tabIndex === '1'">
          <span style="width: 88px;font-size: 14px;font-weight: 700;color: #606266;">存档名称</span>
          <el-input size="small" placeholder="请输入" clearable v-model="searchForm.archivedName"
                    @keyup.enter.native="searchList"></el-input>
        </div>
        <div style="line-height: 30px;">
          <el-button type="primary" size="mini" @click="searchList">查询</el-button>
          <el-button size="mini" @click="resetSearchForm">重置</el-button>
        </div>
      </div>
      <div style="line-height: 30px;">
        <el-button size="small" type="primary" @click="openApprovalDialog1">建 档</el-button>
        <el-upload ref='upload' :action="action" :before-upload="beforeUpload" :headers="uploadHeader" :on-error="onError"
                   :on-success="handleSuccessUp" :show-file-list="false"
                   accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'
                   style="display: inline-block; margin-left: 10px;">
          <el-button :loading="upLoading" size="small" type="primary">导入</el-button>
        </el-upload>
        <el-button size="small" style="margin-left: 10px;" type="primary" @click="openFormDia">新 增</el-button>
      </div>
    </div>
    <div>
      <div class="table">
        <div class="table-tab">
          <el-radio-group v-model="tabIndex" @change="searchList">
            <el-radio-button label="0">今年标准记录</el-radio-button>
            <el-radio-button label="1">历史档案</el-radio-button>
          </el-radio-group>
        </div>
        <div v-if="tabIndex === '0'">
          <lims-table :column="tableColumn" :height="'calc(100vh - 26em)'" :table-data="tableData"
                      :table-loading="tableLoading" :page="page" @pagination="pagination">
          </lims-table>
        </div>
        <div v-if="tabIndex === '1'">
          <lims-table :column="oldTableColumn" :height="'calc(100vh - 20em)'" :table-data="oldTableData"
                      :table-loading="oldTableLoading" :page="oldPage" @pagination="oldPagination">
          </lims-table>
        </div>
      </div>
      <!--      历史档案批准弹框-->
      <el-dialog :visible.sync="approvalDialog" title="批准" width="30%" @close="approvalDialog = false">
        <span>
          批准备注：
          <el-input v-model="qualityRemark" type="textarea"></el-input>
        </span>
        <span slot="footer" class="dialog-footer">
          <el-button :loading="approvalLoading" @click="handleApproval(0)">不批准</el-button>
          <el-button :loading="approvalLoading" type="primary" @click="handleApproval(1)">批 准</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="approvalDialog1" title="建档" width="30%" @close="approvalDialog1 = false">
        <span>
          档案名称：
          <el-input v-model="archivedName" type="textarea"></el-input>
        </span>
        <span slot="footer" class="dialog-footer">
          <el-button :loading="approvalLoading" @click="approvalDialog1 = false, archivedName = ''">取 消</el-button>
          <el-button :loading="approvalLoading" type="primary" @click="handleApproval1()">确 定</el-button>
        </span>
      </el-dialog>
      <FormDialog v-if="formDialog" ref="formDialog" @closeDia="closeDia"></FormDialog>
      <EditFormDia v-if="editFormDialog" ref="editFormDialog" @closeEditDia="closeEditDia"></EditFormDia>
      <el-dialog :visible.sync="viewRocordDia" title="详情" width="80%" @close="approvalDialog = false">
        <limsTable :column="tableColumn1" :height="'calc(100vh - 26em)'" :table-data="tableData1"
                   :table-loading="tableLoading1" style="padding: 0 15px;margin-bottom: 16px" :page="page1"
                   @pagination="pagination1">
        </limsTable>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import limsTable from '@/components/Table/lims-table.vue'
import FormDialog from './component/FormDialog.vue';
import EditFormDia from './component/EditFormDia.vue';
import {
  addSearchNewArchived, exportMethodSearchNew,
  pageMethodSearchNew, pageSearchNewArchived, pageSearchNewBackups, ratifySearchNewArchivedr
} from '@/api/cnas/process/method/standardNoveltyRetrieval'

export default {
  name: 'StandardNoveltyRetrieval',
  // import 引入的组件需要注入到对象中才能使用
  components: {
    FormDialog,
    limsTable,
    EditFormDia
  },
  data() {
    // 这里存放数据
    return {
      searchForm: {
        year: '',
        standardNo: '',
        archivedName: ''
      },
      options: [
        { label: '上半年', value: '1' },
        { label: '下半年', value: '2' },
      ],
      tableColumn: [
        {
          label: '标准名称',
          prop: 'methodName',
          minWidth: '100'
        },
        {
          label: '标准号',
          prop: 'standardNo',
          minWidth: '100'
        },
        {
          label: '文件编号',
          prop: 'fileNo',
          minWidth: '100'
        },
        {
          dataType: 'tag',
          label: '是否是新标准',
          prop: 'isNewStandard',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '否';
            } else if (params == 1) {
              return '是';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'danger';
            } else if (params == 1) {
              return 'success';
            } else {
              return null
            }
          }
        },
        {
          label: '新标准名称',
          prop: 'newMethodName',
          minWidth: '100'
        },
        {
          label: '新标准号',
          prop: 'newStandardNo',
          minWidth: '100'
        },
        {
          dataType: 'tag',
          label: '查询记录来源',
          prop: 'searchNewSource',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '标准网';
            } else if (params == 1) {
              return '委托情报';
            } else if (params == 2) {
              return '标准数';
            } else if (params == 3) {
              return '其他';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success';
            } else if (params == 1) {
              return 'danger';
            } else if (params == 2) {
              return 'warning';
            } else if (params == 3) {
              return 'info';
            } else {
              return null
            }
          }
        },
        {
          dataType: 'tag',
          label: '备注',
          prop: 'remark',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '作废';
            } else if (params == 1) {
              return '替换';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'danger';
            } else if (params == 1) {
              return 'success';
            } else {
              return null
            }
          }
        },
        {
          dataType: 'action',
          minWidth: '100',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.openEditFormDia(row);
              },
            },
          ]
        }
      ],
      tableColumn1: [
        {
          label: '标准名称',
          prop: 'methodName',
          minWidth: '100'
        },
        {
          label: '标准号',
          prop: 'standardNo',
          minWidth: '100'
        },
        {
          label: '文件编号',
          prop: 'fileNo',
          minWidth: '100'
        },
        {
          dataType: 'tag',
          label: '是否是新标准',
          prop: 'isNewStandard',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '否';
            } else if (params == 1) {
              return '是';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'danger';
            } else if (params == 1) {
              return 'success';
            } else {
              return null
            }
          }
        },
        {
          label: '新标准名称',
          prop: 'newMethodName',
          minWidth: '100'
        },
        {
          label: '新标准号',
          prop: 'newStandardNo',
          minWidth: '100'
        },
        {
          dataType: 'tag',
          label: '查询记录来源',
          prop: 'searchNewSource',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '标准网';
            } else if (params == 1) {
              return '委托情报';
            } else if (params == 2) {
              return '标准数';
            } else if (params == 3) {
              return '其他';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success';
            } else if (params == 1) {
              return 'danger';
            } else if (params == 2) {
              return 'warning';
            } else if (params == 3) {
              return 'info';
            } else {
              return null
            }
          }
        },
        {
          dataType: 'tag',
          label: '备注',
          prop: 'remark',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '作废';
            } else if (params == 1) {
              return '替换';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'danger';
            } else if (params == 1) {
              return 'success';
            } else {
              return null
            }
          }
        }
      ],
      tableData: [],
      tableData1: [],
      tableLoading: false,
      tableLoading1: false,
      page: {
        size: 10,
        current: 1,
        total: 0
      },
      page1: {
        size: 10,
        current: 1,
        total: 0
      },
      oldPage: {
        size: 10,
        current: 1,
        total: 0,
      },
      oldTableColumn: [
        {
          label: '存档名称',
          prop: 'archivedName',
          minWidth: '100'
        },
        {
          label: '编制人',
          prop: 'writeName',
          minWidth: '100'
        },
        {
          label: '编制时间',
          prop: 'writeTime',
          minWidth: '100'
        },
        {
          label: '批准人',
          prop: 'ratifyName',
          minWidth: '100'
        },
        {
          label: '批准信息',
          prop: 'ratifyRemark',
          minWidth: '100'
        },
        {
          dataType: 'tag',
          label: '批准状态',
          prop: 'ratifyStatus',
          minWidth: '100',
          formatData: (params) => {
            if (params == 0) {
              return '不通过';
            } else if (params == 1) {
              return '通过';
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'danger';
            } else if (params == 1) {
              return 'success';
            } else {
              return null
            }
          }
        },
        {
          label: '批准时间',
          prop: 'ratifyTime',
          minWidth: '100'
        },
        {
          dataType: 'action',
          minWidth: '100',
          label: '操作',
          operation: [
            {
              name: '批准',
              type: 'text',
              clickFun: (row) => {
                this.openApprovalDialog(row);
              },
              disabled: (row) => {
                if (row.ratifyStatus === 1) {
                  return true
                } else {
                  return false
                }
              },
            },
            {
              name: '查看',
              type: 'text',
              clickFun: (row) => {
                this.openViewDia(row);
              },
            },
            {
              name: '导出',
              type: 'text',
              clickFun: (row) => {
                this.handleOut(row);
              },
            }
          ]
        }
      ],
      oldTableData: [],
      oldTableLoading: false,
      tabIndex: '0',
      formDialog: false,
      editFormDialog: false,
      upLoading: false,
      outLoading: false,
      ratifyInfo: {
        writeUserId: '',
        ratifyUserId: '',
        writeDate: '',
        ratifyDate: '',
      },
      approvalDialog: false,
      approvalDialog1: false,
      viewRocordDia: false,
      approvalLoading: false,
      qualityRemark: '',
      archivedName: '',
      archivedId: '',
    };
  },
  mounted() {
    this.searchList()
  },
  // 方法集合
  methods: {
    // 查询列表
    searchList() {
      if (this.tabIndex === '0') {
        this.tableLoading = true
        pageMethodSearchNew({ standardNo: this.searchForm.standardNo, ...this.page }).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.tableData = res.data.records
            this.page.total = res.data.total
          }
        }).catch(err => {
          console.log('err---', err);
          this.tableLoading = false
        })
      } else {
        this.oldTableLoading = true
        pageSearchNewArchived({archivedName: this.searchForm.archivedName, ...this.oldPage}).then(res => {
          this.oldTableLoading = false
          if (res.code === 200) {
            this.oldTableData = res.data.records
            this.oldPage.total = res.data.total
          }

        }).catch(err => {
          console.log('err---', err);
          this.oldTableLoading = false
        })
      }
    },
    // 打开历史档案详情弹框
    openViewDia(row) {
      this.archivedId = row.archivedId
      this.viewRocordDia = true
      this.pageSearchNewBackups()
    },
    pageSearchNewBackups() {
      const entity = {
        archivedId: this.archivedId,
      }
      this.tableLoading1 = true
      pageSearchNewBackups({ ...this.page1, ...entity }).then(res => {
        this.tableLoading1 = false
        if (res.code === 200) {
          this.tableData1 = res.data.records
          this.page1.total = res.data.total
        }

      }).catch(err => {
        console.log('err---', err);
        this.tableLoading1 = false
      })
    },
    // 打开批准弹框
    openApprovalDialog(row) {
      this.approvalDialog = true
      this.archivedId = row.archivedId
    },
    // 打开批准弹框
    openApprovalDialog1(row) {
      this.approvalDialog1 = true
      this.archivedId = row.archivedId
    },
    // 提交批准
    handleApproval(status) {
      this.approvalLoading = true
      let internalReport = {
        archivedId: this.archivedId,
        ratifyStatus: status,
        ratifyRemark: this.ratifyRemark
      }
      ratifySearchNewArchivedr(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog = false
          this.searchList()
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    // 提交批准
    handleApproval1(status) {
      this.approvalLoading = true
      let internalReport = {
        archivedName: this.archivedName,
      }
      addSearchNewArchived(internalReport).then(res => {
        if (res.code === 200) {
          this.$message.success('提交成功！');
          this.approvalDialog1 = false
          this.searchList()
        }
        this.approvalLoading = false
      }).catch(() => {
        this.approvalLoading = false
      })
    },
    // 重置查询条件
    resetSearchForm() {
      this.searchForm.standardNo = '';
      this.searchList()
    },
    // 操作新增框
    openFormDia(row) {
      this.formDialog = true
      this.$nextTick(() => {
        this.$refs.formDialog.openDia(row)
      })
    },
    // 关闭新增弹框
    closeDia() {
      this.formDialog = false
      this.searchList()
    },
    // 打开修改弹框
    openEditFormDia(row) {
      this.editFormDialog = true
      this.$nextTick(() => {
        this.$refs.editFormDialog.openDia(row)
      })
    },
    //
    closeEditDia() {
      this.editFormDialog = false
      this.searchList()
    },
    // 导出
    handleOut(row) {
      this.outLoading = true
      exportMethodSearchNew({ archivedId: row.archivedId }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '标准查新导出.xlsx')
      })
    },
    // 导入
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
    // 分页
    pagination(page) {
      this.page.size = page.limit
      this.searchList();
    },
    pagination1(page) {
      this.page1.size = page.limit
      this.pageSearchNewBackups();
    },
    oldPagination(page) {
      this.oldPage.size = page.limit
      this.searchList();
    },
  },
  // 用于上传文件的信息
  computed: {
    action() {
      return this.javaApi + '/processMethodSearchNew/importMethodSearchNew'
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
  margin-bottom: 10px;
}
</style>
