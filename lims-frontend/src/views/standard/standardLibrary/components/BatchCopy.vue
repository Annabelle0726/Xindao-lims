<template>
  <div>
    <el-dialog
      :visible.sync="batchCopyDia"
      class="more-edit"
      title="批量编辑"
      width="90%"
    >
      <el-row>
        <el-col :span="6" class="search_thing">
          <div class="search_label">样品名称：</div>
          <div class="search_input">
            <el-input v-model="sample" disabled size="small">
              <template slot="append"
                ><el-button
                  slot="append"
                  icon="el-icon-search"
                  @click="selectStandardTree = true"
                ></el-button
              ></template>
            </el-input>
          </div>
        </el-col>
        <el-col :span="6" class="search_thing">
          <div class="search_label">检验标准：</div>
          <div class="search_input">
            <el-select
              v-model="standardMethodListId"
              :loading="methodLoad"
              clearable
              placeholder="请输入"
              size="small"
              @change="changeStandardMethodListId"
              @focus="methodFocus"
            >
              <el-option
                v-for="item in methods"
                :key="item.id"
                :label="item.code"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-button size="small" type="primary" @click="sortCopy"
            >排序复制</el-button
          >
          <el-button size="small" type="primary" @click="compareAlone"
            >单独对比</el-button
          >
          <el-button size="small" type="primary" @click="compare"
            >复制</el-button
          >
        </el-col>
      </el-row>
      <div
        class="body"
        style="display: flex; flex-direction: row; height: 80vh"
      >
        <div v-loading="productTableLoading1" style="width: 50%">
          <div>需要复制的检验项</div>
          <el-table
            ref="productTable1"
            :data="productList1"
            :fit="true"
            :row-key="(row) => row.id"
            border
            header-row-class-name="header-class"
            height="90%"
            highlight-current-row
            stripe
            style="width: 98%"
            tooltip-effect="dark"
            @select="handleSelectionChange1"
            @select-all="handleSelectAll1"
          >
            <el-table-column type="selection" width="50"> </el-table-column>
            <el-table-column
              label="产品"
              min-width="100"
              prop="sample"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="型号"
              min-width="100"
              prop="model"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="检验项分类"
              min-width="140"
              prop="inspectionItemClass"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              column-key="inspectionItem"
              label="检验项"
              min-width="140"
              prop="inspectionItem"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              column-key="inspectionItemSubclass"
              label="检验项子项"
              min-width="140"
              prop="inspectionItemSubclass"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="要求值"
              min-width="200px"
              prop="ask"
            ></el-table-column>
            <el-table-column
              label="要求描述"
              min-width="220px"
              prop="tell"
            ></el-table-column>
          </el-table>
        </div>
        <div v-loading="productTableLoading0" style="width: 50%">
          <div>当前检验项</div>
          <el-table
            ref="productTable0"
            :data="productList0"
            :fit="true"
            :row-key="(row) => row.id"
            border
            header-row-class-name="header-class"
            height="90%"
            highlight-current-row
            stripe
            style="width: 98%"
            tooltip-effect="dark"
            @select="handleSelectionChange0"
            @select-all="handleSelectAll0"
          >
            <el-table-column type="selection" width="50"> </el-table-column>
            <el-table-column
              label="产品"
              min-width="100"
              prop="sample"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="型号"
              min-width="100"
              prop="model"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="检验项分类"
              min-width="140"
              prop="inspectionItemClass"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              column-key="inspectionItem"
              label="检验项"
              min-width="140"
              prop="inspectionItem"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              column-key="inspectionItemSubclass"
              label="检验项子项"
              min-width="140"
              prop="inspectionItemSubclass"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="要求值"
              min-width="200px"
              prop="ask"
            ></el-table-column>
            <el-table-column
              label="要求描述"
              min-width="220px"
              prop="tell"
            ></el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="selectStandardTree"
      title="选择样品"
      width="500px"
    >
      <div
        v-if="selectStandardTree"
        v-loading="selectStandardTreeLoading"
        class="body"
        style="height: 60vh; overflow-y: auto; user-select: none"
      >
        <el-row>
          <el-col :span="24">
            <el-input
              v-model="search"
              clearable
              placeholder="输入关键字进行搜索"
              size="small"
              style="margin-bottom: 5px"
              suffix-icon="el-icon-search"
              @blur="searchFilter"
              @clear="searchFilter"
              @keyup.enter.native="searchFilter"
            ></el-input>
          </el-col>
        </el-row>
        <el-tree
          ref="tree"
          :data="list"
          :default-expanded-keys="expandedKeys"
          :filter-node-method="filterNode"
          :props="{ children: 'children', label: 'label' }"
          highlight-current
          node-key="label"
          @node-click="handleNodeClick"
          @node-expand="nodeOpen"
          @node-collapse="nodeClose"
        >
          <div slot-scope="{ node, data }" class="custom-tree-node">
            <el-row>
              <el-col :span="24">
                <span
                  ><i
                    :class="`node_i ${
                      data.children != undefined
                        ? data.code === '[1]'
                          ? 'el-icon-folder-opened'
                          : 'el-icon-folder'
                        : 'el-icon-tickets'
                    }`"
                  ></i>
                  {{ data.code }} {{ data.label }}</span
                >
              </el-col>
            </el-row>
          </div>
        </el-tree>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="selectStandardTree = false">取 消</el-button>
        <el-button type="primary" @click="activeStandardTree">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="compareDia" title="确认复制信息" width="80%">
      <div style="width: 100%">
        <el-table
          ref="compareList"
          :data="compareList"
          :fit="true"
          :row-key="(row) => row.id"
          border
          header-row-class-name="header-class"
          height="90%"
          highlight-current-row
          stripe
          style="width: 98%"
          tooltip-effect="dark"
        >
          <el-table-column
            label="产品"
            min-width="100"
            prop="sample"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="型号"
            min-width="100"
            prop="model"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            label="检验项分类"
            min-width="140"
            prop="inspectionItemClass"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            column-key="inspectionItem"
            label="检验项"
            min-width="140"
            prop="inspectionItem"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            column-key="inspectionItemSubclass"
            label="检验项子项"
            min-width="140"
            prop="inspectionItemSubclass"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="要求值" min-width="200px" prop="ask">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.ask"
                :autosize="{ minRows: 1, maxRows: 3 }"
                clearable
                placeholder="要求值"
                size="small"
                type="textarea"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="要求描述" min-width="220px" prop="tell">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.tell"
                :autosize="{ minRows: 1, maxRows: 3 }"
                clearable
                placeholder="要求描述"
                size="small"
                type="textarea"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="section" width="160">
            <template slot-scope="scope">
              <el-button type="text" @click="sectionUp(scope.row)"
                >区间设置</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
          <el-button size="small" @click="compareDia = false">取 消</el-button>
          <el-button
            :loading="handleCompareLoading"
            size="small"
            type="primary"
            @click="handleCompare"
            >确 定</el-button
          >
        </span>
      </div>
    </el-dialog>
    <el-dialog
      :close-on-click-modal="false"
      :visible.sync="sectionUpDia"
      title="区间设置"
      width="80%"
    >
      <div class="body" style="padding: 5px 0">
        <el-table :data="sectionList" border height="350px" style="width: 100%">
          <el-table-column align="center" label="序号" type="index" width="70">
          </el-table-column>
          <el-table-column align="center" label="区间">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.thing"
                clearable
                placeholder="区间"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="芯数">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.cores"
                clearable
                placeholder="芯数"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="要求值">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.ask"
                clearable
                placeholder="要求值"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="要求描述">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.tell"
                :autosize="{ minRows: 1, maxRows: 2 }"
                clearable
                placeholder="要求描述"
                size="small"
                type="textarea"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="导体材质">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.conductorMaterial"
                clearable
                placeholder="导体材质"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="导体类型">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.conductorType"
                clearable
                placeholder="导体类型"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="单价" width="120">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.price"
                clearable
                placeholder="单价"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="工时系数" width="120">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.manHour"
                clearable
                placeholder="工时系数"
                size="small"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="70">
            <template slot-scope="scope">
              <el-button
                circle
                icon="el-icon-minus"
                size="mini"
                type="danger"
                @click="sectionList.splice(scope.$index, 1)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="sectionUpDia = false">取 消</el-button>
        <el-button type="primary" @click="sectionLoadAdd">保 存</el-button>
        <el-button
          icon="el-icon-plus"
          type="primary"
          @click="sectionList.push({ thing: '', price: '', manHour: '' })"
        ></el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  selectStandardProductListByMethodId,
  copyStandardProductSort,
  copyStandardProductOne,
  copyStandardProductList,
  updateStandardProductListBatch,
  selectStandardTreeList2,
  selectsStandardMethodByFLSSM,
} from "@/api/standard/standardLibrary";
export default {
  name: "BatchCopy",
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {
    selectTree1: {
      type: String,
      default: () => "",
    },
    standardId: {
      type: Number,
      default: () => null,
    },
  },
  data() {
    // 这里存放数据
    return {
      batchCopyDia: false,
      productTableLoading0: false,
      productList0: [],
      productList1: [],
      productTableLoading1: false,
      moreSelects: [],
      templateList: [],
      sample: "", // 样品名称
      selectStandardTree: false,
      selectStandardTreeLoading: false,
      list: [],
      search: null,
      expandedKeys: [],
      selectTree: null,
      standardMethodListId: null,
      methodLoad: false,
      methods: [],
      oldStandardProductList: [],
      newStandardProductList: [],
      compareDia: false,
      compareList: [],
      sectionUpDia: false,
      sectionList: [],
      sectionRow: null,
      handleCompareLoading: false,
    };
  },
  mounted() {
    this.selectStandardTreeList();
  },
  // 方法集合
  methods: {
    // 获取左边表格数据
    getList() {
      this.batchCopyDia = true;
      this.productTableLoading0 = true;
      selectStandardProductListByMethodId({
        id: this.standardId,
        tree: this.selectTree1,
        page: 1,
      }).then((res) => {
        this.productList0 = res.data.productList;
        this.productTableLoading0 = false;
      });
    },
    // 排序复制
    sortCopy() {
      const params = {
        oldStandardProductList: this.oldStandardProductList,
        newStandardProductList: this.newStandardProductList,
      };
      // 排序复制
      copyStandardProductSort(params).then((res) => {
        this.$message.success("复制成功");
        this.compareDia = false;
        this.batchCopyDia = false;
        this.$emit("refreshList");
      });
    },
    compareAlone() {
      const params = {
        oldStandardProductList: this.oldStandardProductList,
        newStandardProductList: this.newStandardProductList,
      };
      // 调取单独比较左右要求值的接口
      copyStandardProductOne(params).then((res) => {
        this.compareDia = true;
        this.compareList = res.data;
      });
    },
    compare() {
      const params = {
        oldStandardProductList: this.oldStandardProductList,
        newStandardProductList: this.newStandardProductList,
      };
      // 调取比较左右要求值的接口
      copyStandardProductList(params).then((res) => {
        this.compareDia = true;
        this.compareList = res.data;
      });
    },
    // 提交复制信息
    handleCompare() {
      const params = {
        standardProductList: this.compareList,
      };
      this.handleCompareLoading = true;
      // 调取比较左右要求值的接口
      updateStandardProductListBatch(params)
        .then((res) => {
          this.handleCompareLoading = false;
          this.$message.success("复制成功");
          this.compareDia = false;
          this.batchCopyDia = false;
          this.$emit("refreshList");
          this.compareList = res.data;
        })
        .catch((res) => {
          this.handleCompareLoading = false;
        });
    },
    // 区间设置
    sectionUp(row) {
      if (Array.isArray(row)) {
        // 值是一个数组
        if (row.length === 0) {
          return this.$message.error("请选择检验项");
        }
        this.sectionRow = {};
        this.sectionUpDia = true;
        this.sectionList = [];
      } else {
        // 值是一个对象
        this.sectionRow = row;
        this.sectionUpDia = true;
        this.sectionList = [];
        if (this.sectionRow.section != null && this.sectionRow.section != "") {
          JSON.parse(this.sectionRow.section).forEach((a, ai) => {
            if (this.sectionRow.cores !== null) {
              this.sectionList.push({
                thing: a,
                ask: JSON.parse(this.sectionRow.ask)[ai],
                tell: JSON.parse(this.sectionRow.tell)[ai],
                price: JSON.parse(this.sectionRow.price)[ai],
                manHour: JSON.parse(this.sectionRow.manHour)[ai],
                cores: JSON.parse(this.sectionRow.cores)[ai],
                conductorMaterial:
                  this.sectionRow.conductorMaterial &&
                  JSON.parse(this.sectionRow.conductorMaterial)[ai],
                conductorType:
                  this.sectionRow.conductorType &&
                  JSON.parse(this.sectionRow.conductorType)[ai],
              });
            } else {
              this.sectionList.push({
                thing: a,
                ask: JSON.parse(this.sectionRow.ask)[ai],
                tell: JSON.parse(this.sectionRow.tell)[ai],
                price: JSON.parse(this.sectionRow.price)[ai],
                manHour: JSON.parse(this.sectionRow.manHour)[ai],
                conductorMaterial:
                  this.sectionRow.conductorMaterial &&
                  JSON.parse(this.sectionRow.conductorMaterial)[ai],
                conductorType:
                  this.sectionRow.conductorType &&
                  JSON.parse(this.sectionRow.conductorType)[ai],
              });
            }
          });
        }
      }
    },
    // 区间保存
    sectionLoadAdd() {
      let sectionList = [];
      let askList = [];
      let tellList = [];
      let priceList = [];
      let manHourList = [];
      let coresList = [];
      let conductorMaterialList = [];
      let conductorTypeList = [];
      this.sectionList.forEach((a) => {
        if (a.thing !== "") {
          sectionList.push(a.thing);
          askList.push(a.ask);
          tellList.push(a.tell);
          priceList.push(a.price);
          manHourList.push(a.manHour);
          coresList.push(a.cores);
          conductorMaterialList.push(a.conductorMaterial);
          conductorTypeList.push(a.conductorType);
        }
      });
      if (sectionList.length === 0) {
        this.sectionRow.section = null;
        this.sectionRow.ask = null;
        this.sectionRow.tell = null;
        this.sectionRow.price = null;
        this.sectionRow.manHour = null;
        this.sectionRow.cores = null;
        this.sectionRow.conductorMaterial = null;
        this.sectionRow.conductorType = null;
      } else {
        this.sectionRow.section = JSON.stringify(sectionList);
        this.sectionRow.ask = JSON.stringify(askList);
        this.sectionRow.tell = JSON.stringify(tellList);
        this.sectionRow.price = JSON.stringify(priceList);
        this.sectionRow.manHour = JSON.stringify(manHourList);
        this.sectionRow.cores = JSON.stringify(coresList);
        this.sectionRow.conductorMaterial = JSON.stringify(
          conductorMaterialList
        );
        this.sectionRow.conductorType = JSON.stringify(conductorTypeList);
      }
      this.sectionUpDia = false;
    },
    // 获取样品名称树
    selectStandardTreeList() {
      this.selectStandardTreeLoading = true;
      selectStandardTreeList2().then((res) => {
        this.list = res.data;
        this.list.forEach((a) => {
          this.expandedKeys.push(a.label);
        });
        this.selectStandardTreeLoading = false;
      });
    },
    // 选择样品名称的回调
    handleNodeClick(val, node, el) {
      this.selectTree = "";
      this.getNodeParent(node);
      let flag = false;
      if (node.level == 3) {
        if (node.data.children.length > 0) {
          node.data.children.forEach((a) => {
            let key = Object.keys(a);
            if (!key.includes("level")) {
              flag = true;
            }
          });
        }
      }
      this.selectTree = this.selectTree.replace(" - ", "");
      if (flag) {
        this.selectTree = " -  - " + this.selectTree;
      }
      let data = this.selectTree.split(" - ");
      let data2 = "";
      for (let index = data.length - 1; index >= 0; index--) {
        data2 += " - " + data[index];
      }
      this.selectTree = data2.replace(" - ", "");
    },
    getNodeParent(val) {
      if (val.parent != null) {
        if (val.data.children === null) {
          this.selectTree += " - " + val.label + " - " + "null";
        } else {
          this.selectTree += " - " + val.label;
        }
        this.getNodeParent(val.parent);
      }
    },
    changeStandardMethodListId() {
      // 根据检验标准查右边table数据
      this.getRightList();
    },
    getRightList() {
      this.productTableLoading1 = true;
      selectStandardProductListByMethodId({
        id: this.standardMethodListId,
        tree: this.selectTree,
        page: 1,
      }).then((res) => {
        this.productList1 = res.data.productList;
        this.productTableLoading1 = false;
      });
    },
    // 聚焦时，查询检验标准
    methodFocus() {
      this.selectsStandardMethodByFLSSM();
    },
    selectsStandardMethodByFLSSM() {
      this.methodLoad = true;
      selectsStandardMethodByFLSSM({
        tree: this.selectTree,
      }).then((res) => {
        this.methodLoad = false;
        try {
          if (
            res.data.standardMethodList.length == 0 &&
            this.selectTree.split("-").length == 5
          ) {
            let arr = this.selectTree.split("-");
            let arr0 = arr.slice(0, arr.length - 1);
            let selectTree = arr0
              .join("-")
              .substring(0, arr0.join("-").length - 1);
            selectsStandardMethodByFLSSM({
              tree: selectTree,
            }).then((ress) => {
              this.methods = ress.data.standardMethodList;
            });
          } else {
            this.methods = res.data.standardMethodList;
          }
        } catch (e) {}
      });
    },
    activeStandardTree() {
      let trees = this.selectTree.split(" - ");
      if (trees.length < 3) {
        this.$message.error("未选择对象");
        return;
      }
      if (trees[3] === undefined || trees[3] === "" || trees[3] === "- ") {
        this.sample = trees[2];
      } else {
        this.sample = trees[3];
      }
      this.selectStandardTree = false;
    },
    handleSelectionChange0(val) {
      this.oldStandardProductList = val;
    },
    handleSelectAll0(val) {
      this.oldStandardProductList = val;
    },
    handleSelectionChange1(val) {
      this.newStandardProductList = val;
    },
    handleSelectAll1(val) {
      this.newStandardProductList = val;
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    searchFilter() {
      this.$refs.tree.filter(this.search);
    },
    nodeOpen(data, node, el) {
      $($(el.$el).find(".node_i")[0]).attr(
        "class",
        "node_i el-icon-folder-opened"
      );
    },
    nodeClose(data, node, el) {
      $($(el.$el).find(".node_i")[0]).attr("class", "node_i el-icon-folder");
    },
  },
};
</script>

<style scoped>
.title {
  height: 60px;
  line-height: 60px;
}

.search {
  width: calc(100% - 40px);
  background-color: #fff;
  padding: 5px 40px 5px 0;
}

.search_thing {
  display: flex;
  align-items: center;
  height: 50px;
}

.search_label {
  width: 70px;
  font-size: 14px;
  text-align: right;
}

.search_input {
  width: calc(100% - 80px);
}
.node_i {
  color: orange;
  font-size: 18px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 10px;
}
</style>
