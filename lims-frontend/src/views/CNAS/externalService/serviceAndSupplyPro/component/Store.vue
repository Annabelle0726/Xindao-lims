<template>
  <div>
    <div style="display: flex;justify-content: flex-end;margin-bottom: 10px;">
      <el-button size="small" type="primary" @click="searchList">刷新</el-button>
      <el-button icon="el-icon-plus" size="small" type="primary" @click="addOrUpdateStore('add')">添加</el-button>
      <el-button icon="el-icon-download" size="small" @click="importExcel">导出excel</el-button>
    </div>
    <div class="table">
      <limsTable :column="tableColumn" :height="'calc(100vh - 20em)'" :table-data="storageTableData"
        :table-loading="tableLoading" :page="page" @pagination="pagination">
      </limsTable>
    </div>

    <!-- 编辑-新增弹框 -->
    <el-dialog :before-close="handleClose" :close-on-click-modal="false" :close-on-press-escape="false" :title="title"
      :visible.sync="dialogVisible" width="70%">
      <!-- 入库单 -->
      <div>
        <div style="display: flex; align-items: center">
          <span style="margin-left: 10px">入库单</span>
        </div>
        <el-form ref="formModel" :model="form" label-width="auto">
          <el-col :span="12">
            <el-form-item label="入库单号">
              <el-input v-model="form.oddNumbers" placeholder="请输入入库单号" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库库存">
              <el-input v-model="form.inventory" placeholder="请输入入库库存" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库总金额">
              <el-input v-model="form.totalAmount" placeholder="请输入入库总金额" size="small"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库日期">
              <el-date-picker v-model="form.storageTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%" type="date" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库人">
              <el-select v-model="form.storageUser" size="small" style="width: 100%">
                <el-option v-for="item in users" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库说明">
              <el-input v-model="form.remark" :rows="2" placeholder="请输入内容" type="textarea">
              </el-input>
            </el-form-item>
          </el-col>
        </el-form>
      </div>
      <!-- 入库明细 -->
      <div>
        <div>入库明细</div>
        <div class="input-form">
          <div>
            <div>
              <span>耗材名称:</span>
              <el-select v-model="formTwo.id" filterable placeholder="请选择耗材名称" size="small" style="width: 70%">
                <el-option v-for="(v, i) in consumableOptions" :key="i" :label="v.consumablesName"
                  :value="v.id"></el-option>
              </el-select>
            </div>
          </div>
          <div>
            <div>
              <span>单价:</span>
              <el-input v-model="formTwo.unitPrice" placeholder="请输入单价" size="small" style="width: 70%">
              </el-input>
            </div>
          </div>
          <div>
            <div>
              <span>入库数量:</span>
              <el-input v-model="formTwo.storeNumber" placeholder="请输入入库数量" size="small" style="width: 70%">
              </el-input>
            </div>
          </div>
          <div>
            <div>
              <span>总价:</span>
              <el-input v-model="formTwo.totalPrice" placeholder="请输入总价" size="small" style="width: 70%">
              </el-input>
            </div>
          </div>
          <el-button size="mini" type="primary" @click="addTableData">添加
          </el-button>
        </div>
        <el-table :data="consumables" style="margin-top: 10px" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column label="编号" type="index" width="120px"></el-table-column>
          <el-table-column label="货号" prop="itemNumber"></el-table-column>
          <el-table-column label="类别" prop="type"></el-table-column>
          <el-table-column label="名称" prop="consumablesName"></el-table-column>
          <el-table-column label="规格" prop="specifications"></el-table-column>
          <el-table-column label="参考供应商" prop="supplier"></el-table-column>
          <el-table-column label="计量单位" prop="unit"></el-table-column>
          <el-table-column label="单价" prop="unitPrice"></el-table-column>
          <el-table-column label="入库数量" prop="storeNumber"></el-table-column>
          <el-table-column label="总价" prop="totalPrice"></el-table-column>
          <el-table-column label="操作" width="80px">
            <template slot-scope="scope">
              <el-button size="mini" style="color: #f56c6c" type="text" @click="deleteTableData(scope.$index)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="save">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  addStore,
  deleteStore,
  exportProcurementSuppliesStoreExcel,
  procurementSuppliesList,
  selectStoreById,
  selectUserCondition,
  storeList,
  updateStore
} from '@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro'
import limsTable from '@/components/Table/lims-table.vue'

export default {
  computed: {
    title() {
      return this.dialogType == "add" ? "新建入库" : "编辑入库";
    },
  },
  dicts: ["consumables_type"],
  props: {
    contentsId: {
      type: Number,
      default: 0
    }
  },
  watch: {

  },
  data() {
    return {
      mutiList: [],
      consumables: [],
      users: [],
      form: {
        oddNumbers: null,
        inventory: null,
        totalAmount: null,
        storageTime: null,
        storageUser: null,
        remark: null,
      },
      formTwo: {
        consumablesName: null,
        unitPrice: null,
        storeNumber: null,
        totalPrice: null,
      },
      dialogVisible: false,
      dialogType: "",
      upIndex: 0,
      tableColumn: [
        {
          label: '入库单号',
          prop: 'oddNumbers',
          minWidth: '100'
        },
        {
          label: '耗材名称',
          prop: 'consumablesName',
          minWidth: '100'
        },
        {
          label: '入库数量',
          prop: 'storeNumber',
          minWidth: '100'
        },
        {
          label: '入库总价',
          prop: 'totalPrice',
          minWidth: '100'
        },
        {
          label: '入库人',
          prop: 'storageUserName',
          minWidth: '100'
        }, {
          label: '入库日期',
          prop: 'storageTime',
          minWidth: '100'
        }, {
          label: '说明',
          prop: 'remark',
          minWidth: '100'
        }, {
          label: '登记人',
          prop: 'registrantName',
          minWidth: '160'
        }, {
          label: '登记日期',
          prop: 'registrantTime',
          minWidth: '100'
        },
        {
          dataType: 'action',
          fixed: 'right',
          minWidth: '120',
          label: '操作',
          operation: [
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.addOrUpdateStore('edit', row);
              },
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.deleteStore(row)
              },
            }
          ]
        }
      ],
      tableLoading: false,
      page: {
        total: 0,
        size: 10,
        current: 1
      },
      consumableOptions: [],
      options: [],
      storageTableData: [],
      saveLoading: false,
    };
  },
  components: {
    limsTable,
  },
  mounted() {
    this.searchList();
    this.getUserList();
    this.fetchListOptions()
    this.options = this.dict.type.consumables_type
  },
  methods: {
    // 保存
    save() {
      let data = {
        store: this.form,
        consumables: this.consumables,
      };
      this.saveLoading = true;
      if (this.dialogType === "update") {
        updateStore(data).then((res) => {
          this.saveLoading = false
          if (res.code == 200) {
            this.$message.success("修改成功");
            this.handleClose();
            // this.$refs.selectList()
            this.searchList()
          }
        }).catch((err) => {
          this.saveLoading = false
        })
      } else {
        addStore(data).then((res) => {
          this.saveLoading = false
          if (res.code == 200) {
            this.$message.success("新增成功");
            this.handleClose();
            // this.$refs.selectList()
            this.searchList()
          }
        }).catch((err) => {
          this.saveLoading = false
        })
      }
    },
    findType(val) {
      console.log(val)
      return this.options.find(item => item.value === val).label
    },
    // 添加表格数据
    addTableData() {
      this.$nextTick()
      console.log('this.consumableOptions', this.consumableOptions)
      this.consumableOptions.forEach(item => {
        if (item.id === this.formTwo.id) {
          this.consumables.push({
            itemNumber: null,
            type: this.findType(item.consumablesType),
            consumablesName: item.consumablesName,
            specifications: item.specifications,
            supplier: item.supplierName,
            unit: item.unit,
            unitPrice: this.formTwo.unitPrice,
            storeNumber: this.formTwo.storeNumber,
            totalPrice: this.formTwo.totalPrice,
          });
        }
      })
    },
    deleteTableData(index) {
      this.consumables.splice(index, 1);
    },
    // 打开弹框
    addOrUpdateStore(type, row) {
      if (type === 'edit') {
        this.dialogType = "update";
        selectStoreById({ id: row.id }).then(res => {
          this.consumables = res.data.consumables
          this.form = { ...res.data.store }
        }).catch(err => {
          console.log('err---', err);
        })
      } else {
        this.dialogType = "add";
      }
      this.dialogVisible = true;
    },
    // 查询列表
    searchList() {
      this.tableLoading = true
      storeList({ ...this.page}).then(res => {
        this.tableLoading = false
        if (res.code === 200) {
          this.storageTableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        console.log('err---', err);
        this.tableLoading = false
      })
    },
    // 导出excel
    async importExcel() {
      exportProcurementSuppliesStoreExcel({ parentId: this.contentsId }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '耗材入库.xlsx')
      })
    },
    handleClose() {
      this.form = {
        oddNumbers: null,
        inventory: null,
        totalAmount: null,
        storageTime: null,
        storageUser: null,
        remark: null,
      };
      this.formTwo = {
        consumablesName: null,
        unitPrice: null,
        storeNumber: null,
        totalPrice: null,
      };
      this.consumables = [];
      this.dialogVisible = false;
    },
    viewIssued(row) {
      console.log(row);
    },
    // 获取所有用户
    getUserList() {
      selectUserCondition().then((res) => {
        this.users = res.data;
      });
    },
    async fetchListOptions() {
      procurementSuppliesList({ contentsId: this.contentsId }).then(res => {
        if (res.code === 200) {
          this.consumableOptions = res.data.records
        }
      })

    },
    // 删除
    deleteStore(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        console.log('row>>>>>>>>>>>>>>s', row)
        deleteStore({
          id: row.id,
          consumablesId: row.consumablesId
        }).then(res => {
          this.tableLoading = false
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.searchList()
          }
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

    pagination(page) {
      this.page.size = page.limit
      this.searchList();
    },
  },
};
</script>

<style scoped>
.el-dialog {
  margin: 6vh auto 50px !important;
}

.el-dialog__body {
  max-height: 42em;
  overflow-y: auto;
}

.input-form {
  display: flex;
  margin: 10px 0;
}
</style>
