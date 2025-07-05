<template>
  <div class="data-acquisition-config">
    <div>
      <el-row class="title">
        <el-col :span="6" style="padding-left: 20px;text-align: left;">数采配置</el-col>
        <el-col :span="18" style="text-align: right;">
          <el-button size="small" type="primary" @click="fileConfiguration">文件配置</el-button>
          <el-button size="small" type="primary" @click="addItem">检验项新增</el-button>
          <el-button size="small" @click="$parent.closeDataVue()">
            <span style="color: #3A7BFA;">返回</span>
          </el-button>
        </el-col>
      </el-row>
    </div>
    <div class="table">
      <el-table :data="tableList.slice((page.current - 1) * page.size,page.current * page.size)"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                tooltip-effect="dark" height="100%">
        <el-table-column type="index" label="序号" align="center" width="65"></el-table-column>
        <el-table-column prop="deviceName" align="center" min-width="100" label="设备名称"></el-table-column>
        <el-table-column prop="sample" align="center" label="检验对象" show-overflow-tooltip
          min-width="150"></el-table-column>
        <el-table-column prop="inspectionItemClass" align="center" label="检验项分类" min-width="120"></el-table-column>
        <el-table-column prop="inspectionItem" align="center" label="检验项" min-width="100"></el-table-column>
        <el-table-column prop="inspectionItemSubclass" align="center" label="检验子项" min-width="100"></el-table-column>
        <el-table-column prop="referx" align="center" label="参照X" min-width="100"></el-table-column>
        <el-table-column prop="x" align="center" label="X"></el-table-column>
        <el-table-column prop="refery" align="center" label="参照Y" min-width="100"></el-table-column>
        <el-table-column prop="y" align="center" label="Y"></el-table-column>
        <el-table-column prop="anotherName" align="center" label="别名" min-width="100"></el-table-column>
        <el-table-column prop="matchingName" align="center" label="匹配名称" min-width="100"></el-table-column>
        <el-table-column prop="formula" align="center" label="公式"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="dataConfig(scope.row)">数采配置</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="page" v-if="tableList.length > 0">
      <el-pagination @size-change="sizeChange" @current-change="currentChange" :current-page="page.current"
        :page-sizes="[10, 20, 30, 50, 100]" :page-size="page.size" layout="total, sizes, prev, pager, next, jumper"
        :total="tableList.length">
      </el-pagination>
    </div>
    <el-dialog title="数采配置" :visible.sync="dialogVisible3" width="920px" :before-close="closeForm">
      <el-form :model="configForm" label-position="top" size="small" ref="configForm" label-width="100px"
        class="demo-ruleForm">
        <el-table :data="domains" style="width: 100%" height="300"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
          <el-table-column label="序号" type="index" width="80">
            <template v-slot="scope">
              {{ getIndexWithAlphabet(scope.$index) }}
            </template>
          </el-table-column>
          <el-table-column prop="referx" label="参照X" min-width="140">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.referx"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="x" label="X" min-width="100">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.x"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="refery" label="参照Y" min-width="140">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.refery"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="y" label="Y" min-width="100">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.y"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="anotherName" label="别名" min-width="140">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.anotherName"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="matchingName" label="匹配名称" min-width="140">
            <template v-slot="scope">
              <el-input style="width: 100%" v-model="scope.row.matchingName"></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template v-slot="scope">
              <el-button @click.prevent="removeDomain(scope.row)" size="small" type="text">删除</el-button>
              <el-button @click="addDomain" size="small" type="text">新增</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="fomItem2">
          <el-form-item class="fomItemInput1">
            <template v-slot="label">
              公式：
              <el-tooltip v-for="(formula, key) in formulas" class="itemFomItem" effect="dark"
                :content="formula.content" :key="key" placement="top">
                <label>{{ formula.label }}()</label>
              </el-tooltip>
              <el-input type="textarea" autosi:autosize="{ minRows: 2, maxRows: 4}" ze placeholder="请输入内容"
                v-model="configForm.formula" @change="evalResult">
              </el-input>
            </template>
          </el-form-item>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="closeForm">取 消</el-button>
          <el-button type="primary" @click="submitForm3" :loading="upLoad3">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog title="文件配置" :visible.sync="dialogVisible4" width="400px">
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">
          <span style="color:red;margin-right: 4px;">*</span>IP：
        </div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.ip"></el-input>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">
          <span style="color:red;margin-right: 4px;">*</span>采集地址：
        </div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.collectUrl"></el-input>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">储存地址：</div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.storageUrl"></el-input>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">
          <span style="color:red;margin-right: 4px;">*</span>文件后缀：
        </div>
        <el-select v-model="configForm.fileType" size="small" placeholder="请选择" style="width: 100%;">
          <el-option v-for="item in fileTypeOptions" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">委托字段：</div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.entrustCode"></el-input>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">样品字段：</div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.sampleCode"></el-input>
      </div>
      <div class="search_thing" style="margin-bottom: 14px;">
        <div class="search_label">文件名称：</div>
        <el-input size="small" placeholder="请输入" clearable v-model="configForm.dbFileName"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="dialogVisible4 = false">取 消</el-button>
          <el-button type="primary" @click="submitForm4" :loading="upLoad4">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog title="添加检验项" :visible.sync="dialogVisible5" width="1000px">
      <lims-table :tableData="tableData5" :column="column5"
                  :isSelection="true" :handleSelectionChange="selectMethod"
                  height="540" :page="page5" :tableLoading="tableLoading5"
                  @pagination="pagination5"></lims-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible5 = false">取 消</el-button>
        <el-button type="primary" @click="submitForm5" :loading="loading5">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryDataAcquisitionConfiguration,
  saveDataAcquisitionConfiguration,
  deleteDataAcquisitionConfiguration,
  getDeviceById,
  saveDeviceFileConfiguration,
  queryProductConfiguration,
  getNoConfigProduct,
} from '@/api/cnas/resourceDemand/device.js'
import limsTable from "@/components/Table/lims-table.vue";
export default {
  name: "dataAcquisitionConfig",
  // import 引入的组件需要注入到对象中才能使用
  components: {limsTable},
  props: {
    deviceId: {
      type: Number,
      default: () => []
    }
  },
  data() {
    // 这里存放数据
    return {
      formulas: [
        {
          label: "MAX",
          content: "求参数最大值，参数个数最少两个，如：MAX(A1,B2)"
        },
        {
          label: "MIN",
          content: "求参数最小值，参数个数最少两个，如：MIN(A1,B2)"
        },
        {
          label: "SUM",
          content: "求和，参数个数最少两个，如：SUM(A1,B2)"
        },
        {
          label: "ABS",
          content: "求绝对值，参数个数最少一个，如：ABS(A1)"
        },
        {
          label: "AVERAGE",
          content: "求平均值，参数个数最少两个个，如：AVERAGE(A1,B2)"
        },
        {
          label: "MEDIAN",
          content: "求中值，参数个数最少两个个，如：MEDIAN(A1,B2)"
        },
      ],
      dialogVisible4: false,
      tableList: [],
      page: {
        current: 1,
        size: 20
      },
      dialogVisible3: false,
      configForm: {
        formula: "",
        collectUrl: "",
        storageUrl: "",
        entrustCode: "",
        sampleCode: "",
        dbFileName: "",
        fiberOpticRibbon: ""
      },
      domains: [
        {
          referx: "",
          refery: "",
          x: "",
          y: "",
          anotherName: "",
          matchingName: ""
        }
      ],
      upLoad3: false,
      upLoad4: false,
      fileTypeOptions: [
        { label: "csv", value: ".csv" },
        { label: "db", value: ".db" },
        { label: "mdb", value: ".mdb" },
        { label: "word", value: ".docx" },
        { label: "excel", value: ".xlsx" },
        { label: "txt", value: ".txt" },
        { label: "png", value: ".png" }
      ],
      spanList: [],
      specialSpanList: [],
      spanConfig: {
        special: {
          main: "inspectionItemSubclass",
          rows: [
            {
              name: "deviceName",
              index: 1
            },
            {
              name: "fileType",
              index: 2
            },
            {
              name: "collectUrl",
              index: 3
            },
            {
              name: "storageUrl",
              index: 4
            },
            {
              name: "ip",
              index: 5
            },
            {
              name: "inspectionItem",
              index: 6
            },
            {
              name: "inspectionItemSubclass",
              index: 7
            },
            {
              name: "formula",
              index: 13
            },
            {
              name: "section",
              index: 14
            }
          ]
        }
      },
      deleteList: [],
      tableData5: [],
      column5: [
        {label: '样品名称', prop: 'sample'},
        {label: '检验项分类', prop: 'inspectionItemClass'},
        {label: '检验项', prop: 'inspectionItem'},
        {label: '检验子项', prop: 'inspectionItemSubclass'},
      ],
      page5: {
        total:0,
        size:20,
        current:1
      },
      tableLoading5: false,
      dialogVisible5: false,
      loading5: false,
      multipleSelection: []
    };
  },
  mounted() {
    this.init();
  },
  // 方法集合
  methods: {
    // 查询列表
    init() {
      queryDataAcquisitionConfiguration({ deviceId: this.deviceId }).then(res => {
        this.tableList = res.data;
        this.tableList.forEach(i => {
          let isIncludes = false
          if (i.formula && i.formula != "") {
            this.formulas.forEach(j => {
              if (i.formula.includes(j.label)) {
                isIncludes = true
              }
            })
          }
          if (!isIncludes && i.formula) {
            i.formula = i.formula.slice(1, -1)
          }
        });

      });
    },
    getIndexWithAlphabet(index) {
      const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
      const letterIndex = index % 26;
      return alphabet.charAt(letterIndex) + (index + 1);
    },
    // 打开新增检验项弹框
    addItem () {
      this.dialogVisible5 = true;
      this.getList5()
    },
    getList5() {
      this.tableLoading5 = true;
      getNoConfigProduct({deviceId: this.deviceId, ...this.page5 }).then(res => {
        this.tableLoading5 = false;
        this.tableData5 = res.data.records
        this.page5.total = res.data.total
      }).catch(err => {
        this.tableLoading5 = false;
      })
    },
    pagination5 (page) {
      this.page5.size = page.limit
      this.getList5()
    },
    // 表格选择方法
    selectMethod(val) {
      this.multipleSelection = val
    },
    // 提交新增检验项
    submitForm5 () {
      this.loading5 = true;
      saveDataAcquisitionConfiguration({
        deviceId: this.deviceId,
        dataConfigList: this.multipleSelection,
      }).then(res => {
        this.loading5 = false;
        this.dialogVisible5 = false;
        this.init();
        this.$message.success("添加成功");
      }).catch(err => {
        this.loading5 = false;
      })
    },
    // 打开文件配置弹框
    fileConfiguration() {
      this.dialogVisible4 = true;
      this.getInfo()
    },
    // 查询文件配置信息
    getInfo() {
      getDeviceById({deviceId: this.deviceId}).then(res => {
        this.$set(this.configForm, "fileType", res.data.fileType);
        this.$set(this.configForm, "collectUrl", res.data.collectUrl);
        this.$set(this.configForm, "storageUrl", res.data.storageUrl);
        this.$set(this.configForm, "ip", res.data.ip);
        this.$set(this.configForm, "entrustCode", res.data.entrustCode);
        this.$set(this.configForm, "sampleCode", res.data.sampleCode);
        this.$set(this.configForm, "dbFileName", res.data.dbFileName);
        this.$set(this.configForm, "fiberOpticRibbon", res.data.fiberOpticRibbon)
      })
    },
    // 提交文件配置
    submitForm4() {
      const obj = Object.assign({
        deviceId: this.deviceId,
        fileType: this.configForm.fileType,
        collectUrl: this.configForm.collectUrl,
        storageUrl: this.configForm.storageUrl,
        ip: this.configForm.ip,
        entrustCode: this.configForm.entrustCode,
        sampleCode: this.configForm.sampleCode,
        dbFileName: this.configForm.dbFileName,
        fiberOpticRibbon: this.configForm.fiberOpticRibbon
      });
      this.upLoad4 = true;
      saveDeviceFileConfiguration({ deviceId: this.deviceId, ...obj }).then(res => {
        if (res.code == 200) {
          this.dialogVisible4 = false;
          this.init();
          this.$message.success("操作成功");
        }
        this.upLoad4 = false;
      })
        .catch(err => {
          this.upLoad4 = false;
        });
    },
    // 打开数采配置弹框
    dataConfig(row) {
      this.configForm = {
        deviceId: row.deviceId,
        inspectionItem: row.inspectionItem.trim(),
        inspectionItemSubclass: row.inspectionItemSubclass,
        structureItemParameterId: row.structureItemParameterId,
        inspectionItemClass: row.inspectionItemClass,
      };
      this.dialogVisible3 = true;
      queryProductConfiguration({
        deviceId: this.deviceId,
        inspectionItem: row.inspectionItem.trim(),
        isDevice: true,
        inspectionItemSubclass: row.inspectionItemSubclass.trim(),
        inspectionItemClass: row.inspectionItemClass.trim(),
        structureItemParameterId: row.structureItemParameterId
      }).then(res => {
        const data = res.data;
        if (data[0]) {
          this.domains.splice(0, 1);
          let formula = data[0].formula
          let isIncludes = false
          if (formula && formula != "") {
            for (let key in this.formulas) {
              if (formula.includes(this.formulas[key].label)) {
                isIncludes = true
              }
            }
          }
          if (!isIncludes && formula) {
            let formulaValue = formula.slice(1, -1)
            this.$set(this.configForm, "formula", formulaValue);
          } else {
            this.$set(this.configForm, "formula", data[0].formula);
          }
          data.forEach(i => {
            this.domains.push({
              referx: i.referx,
              refery: i.refery,
              x: i.x,
              y: i.y,
              anotherName: i.anotherName,
              matchingName: i.matchingName,
              id: i.id
            });
          });
        }
      });
    },
    // 提交数采配置
    submitForm3() {
      this.$refs.configForm.validate(valid => {
        // 表单校验
        if (valid) {
          this.domains.forEach(item => {
            const isEmpty = Object.values(item).every(val => val === "");
            if (isEmpty) {
              this.$message.error("请填写参照数据");
            }
          });
          this.domains.forEach((i, index) => {
            let isIncludes = false
            if (this.configForm.formula && this.configForm.formula !== "") {
              for (let formula in this.formulas) {
                if (this.configForm.formula.includes(this.formulas[formula].label)) {
                  isIncludes = true
                }
              }
            }
            if (isIncludes === false && this.configForm.formula !== "" && this.configForm.formula !== undefined) {
              i.formula = "(" + this.configForm.formula + ")";
            } else {
              i.formula = this.configForm.formula;
            }
            i.deviceId = this.deviceId;
            i.inspectionItem = this.configForm.inspectionItem;
            i.inspectionItemSubclass = this.configForm.inspectionItemSubclass;
            i.structureItemParameterId = this.configForm.structureItemParameterId;
            i.inspectionItemClass = this.configForm.inspectionItemClass;
            i.serialNumber = this.getIndexWithAlphabet(index)
          });
          saveDataAcquisitionConfiguration({
            deviceId: this.deviceId,
            dataConfigList: this.domains,
            isDevice: false
          }).then(res => {
            this.dialogVisible3 = false;
            this.domains = [
              {
                referx: "",
                refery: "",
                x: "",
                y: "",
                id: ""
              }
            ];
            if (this.deleteList.length > 0) {
              deleteDataAcquisitionConfiguration({ ids: this.deleteList.join() }).then(res => {
                if (res.code == 200) {
                  this.init();
                }
              });
            }
            this.init();
            this.$message.success("添加成功");
          });
        } else {
          return false;
        }
      });
    },
    removeDomain(item) {
      // 删除公式formItem
      const index = this.domains.indexOf(item);
      if (index !== -1 && this.domains.length > 1) {
        if (item.id) {
          this.deleteList.push(item.id);
          this.domains.splice(index, 1);
        } else {
          this.domains.splice(index, 1);
        }
      } else {
        this.$message.error("不允许删除最后一条数据！");
      }
    },
    addDomain() {
      // 添加公式formItem
      this.domains.push({
        referx: "",
        refery: "",
        x: "",
        y: "",
        id: ""
      });
    },
    evalResult(val) {
      this.configForm.formula = val;
      // 根据公式计算出结果
    },
    sizeChange(val) {
      this.page.size = val;
    },
    currentChange(val) {
      this.page.current = val;
    },
    closeForm() {
      this.deleteList = [];
      this.domains = [
        {
          referx: "",
          refery: "",
          x: "",
          y: "",
          id: ""
        }
      ];
      this.dialogVisible3 = false;
      this.$refs.configForm.resetFields();
    }
  }
};
</script>

<style scoped>
.itemFomItem {
  margin-left: 20px;
}

.search_thing {
  width: 350px;
  display: flex;
  align-items: center;
}

.search_label {
  width: 110px;

  font-size: 14px;
  text-align: right;
}

.data-acquisition-config {
  width: 100%;
  height: 100%;
  overflow-y: hidden;
  overflow-x: hidden;

  .title {
    height: 60px;
    line-height: 60px;
  }
}

.page {
  width: 100%;
  height: 30px;
  text-align: right;
  margin-top: 10px;
}

.table {
  background-color: #fff;
  height: calc(100vh - 18em);
}

.fomItem1 {
  display: flex;
  height: 74px;
}

.fomItem2 {
  display: flex;
  width: 100%;
}

.fomItemInput {
  width: 180px;
  margin-right: 6px;
}

.fomItemInput1 {
  width: 100%;
  margin-right: 6px;
}

>>>.el-form-item__label {
  padding-bottom: 0 !important;
}
</style>
