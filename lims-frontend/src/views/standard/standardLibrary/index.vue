<template>
  <div class="standard">
    <div class="left">
      <el-row>
        <el-col :span="20">
          <el-input v-model="search" clearable placeholder="输入关键字进行搜索" size="small" style="margin-bottom: 5px"
            suffix-icon="el-icon-search" @blur="searchFilter" @clear="searchFilter"></el-input>
        </el-col>
        <el-col v-if="checkPermi(['standard:standardLibrary:add'])" :span="4"
          style="text-align: center; line-height: 30px">
          <el-button circle icon="el-icon-plus" size="mini" type="primary" @click="addDia = true"></el-button>
        </el-col>
      </el-row>
      <el-tree ref="tree" v-loading="treeLoad" :allow-drop="allowDrop" :data="list"
        :default-expanded-keys="expandedKeys" :draggable="true" :filter-node-method="filterNode"
        :props="{ children: 'children', label: 'label' }" highlight-current node-key="label" style="
          height: calc(100% - 30px);
          overflow-y: scroll;
          scrollbar-width: none;
        " @node-click="handleNodeClick"
               @node-drop="handleDrop">
        <div slot-scope="{ node, data }" class="custom-tree-node">
          <el-row style="width: 100%">
            <el-col :class="{ sort: node.level > 3 }" :span="19" :title="data.label" style="text-align: left">
              <span>
                <i :class="`node_i ${data.children != undefined
                  ? data.code === '[1]'
                    ? 'el-icon-folder-opened'
                    : 'el-icon-folder'
                  : 'el-icon-tickets'
                  }`"></i>
                {{ data.label }}
              </span>
            </el-col>
            <el-col v-if="
              checkPermi(['standard:standardLibrary:delStandardTree']) &&
              (node.data.children === null ||
                node.data.children === undefined)
            " :span="2" style="text-align: right">
              <el-button size="mini" type="text" @click.stop="editTreeName(node.data)">
                <i class="el-icon-edit"></i>
              </el-button>
            </el-col>
            <el-col v-if="
              checkPermi(['standard:standardLibrary:delStandardTree']) &&
              (node.data.children === null ||
                node.data.children === undefined)
            " :span="2" style="text-align: right">
              <el-button size="mini" type="text" @click.stop="remove(node, data)">
                <i class="el-icon-delete"></i>
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-tree>
    </div>
    <div class="right">
      <el-row class="title" style="width: 100%">
        <el-col :span="20" style="font-size: 14px; color: #999">{{
          selectTree
        }}</el-col>
        <el-col :span="4">
          <el-button v-if="isShowCopy" size="small" style="position: absolute; right: 20px; top: 1px" type="primary"
            @click="openCopyDia">批量复制</el-button>
        </el-col>
      </el-row>
      <el-row v-loading="tableLoad" class="standard_table">
        <el-table ref="standard" :data="standardList" class="el-table" header-row-class-name="header-class" height="220"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          highlight-current-row style="width: 100%; height: 220px !important" tooltip-effect="dark"
          @row-click="rowClick">
          <el-table-column label="标准编号" prop="code" show-overflow-tooltip width="200">
            <template slot-scope="scope">
              <span style="color: red; font-size: 14px">{{
                scope.row["code"]
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="标准名称" prop="name" show-overflow-tooltip></el-table-column>
          <el-table-column label="备注" prop="remark" show-overflow-tooltip></el-table-column>
        </el-table>
      </el-row>
      <el-row v-loading="tableLoad2" class="product_table">
        <el-table id="templateParamTable" ref="productTable" v-loading="productTableLoading" :data="productList"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
          :fit="true" :row-class-name="tableRowClassName" class="productTable"
          header-row-class-name="header-class" height="100%" row-key="id" stripe style="width: 100%"
          tooltip-effect="dark" @select="upProductSelect" @selection-change="handleSelectionChange"
          @select-all="handleAll">
          <el-table-column type="selection" width="50"> </el-table-column>
          <el-table-column label="产品" min-width="100" prop="sample" show-overflow-tooltip></el-table-column>
          <el-table-column label="型号" min-width="100" prop="model" show-overflow-tooltip></el-table-column>
          <el-table-column label="检验项分类" min-width="140" prop="inspectionItemClass"
            show-overflow-tooltip></el-table-column>
          <el-table-column label="检验项" min-width="140" prop="inspectionItem" show-overflow-tooltip></el-table-column>
          <el-table-column label="检验项子项" min-width="140" prop="inspectionItemSubclass"
            show-overflow-tooltip></el-table-column>
          <el-table-column label="子实验室" prop="sonLaboratory" show-overflow-tooltip width="130"></el-table-column>
          <el-table-column label="要求值" min-width="200px" prop="ask">
            <template slot-scope="scope">
              <el-input v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.ask" :autosize="{ minRows: 1, maxRows: 3 }" clearable placeholder="要求值" size="small"
                type="textarea" @change="(value) => upStandardProductList(value, scope.row.id)"></el-input>
              <span v-else>{{ scope.row.ask }}</span>
            </template>
          </el-table-column>
          <el-table-column label="要求描述" min-width="220px" prop="tell">
            <template slot-scope="scope">
              <el-input v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 3 }" clearable placeholder="要求描述"
                size="small" type="textarea" @change="(value) => upStandardProductListOfTell(value, scope.row.id)
                  "></el-input>
              <span v-else>{{ scope.row.ask }}</span>
            </template>
          </el-table-column>
          <el-table-column label="试验方法" prop="method" width="200">
            <template slot-scope="scope">
              <el-select v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.methodS" clearable placeholder="试验方法" size="small" @change="(value) => upStandardProductListOfMethodS(value, scope.row.id)
                ">
                <el-option v-for="(a, i) in scope.row.method &&
                  JSON.parse(scope.row.method)" :key="i" :label="a" :value="a"></el-option>
              </el-select>
              <span v-else>{{ scope.row.methodS }}</span>
            </template>
          </el-table-column>
          <el-table-column label="条件" min-width="140" prop="radius" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-select v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.radius" clearable placeholder="条件" size="small" @change="(value) => upStandardProductListOfRadius(value, scope.row.id)
                ">
                <el-option v-for="(a, i) in scope.row.radiusList &&
                  JSON.parse(scope.row.radiusList)" :key="i" :label="a" :value="a"></el-option>
              </el-select>
              <span v-else>{{ scope.row.radius }}</span>
            </template>
          </el-table-column>
          <el-table-column label="计量单位" prop="unit" show-overflow-tooltip width="100"></el-table-column>
          <el-table-column label="单价(元)" prop="price" width="120">
            <template slot-scope="scope">
              <el-input v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.price" placeholder="单价(元)" size="small" @change="(value) => upStandardProductListOfPrice(value, scope.row.id)
                ">
              </el-input>
              <span v-else>{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column label="工时系数" prop="manHour" width="120">
            <template slot-scope="scope">
              <el-input v-if="
                checkPermi(['standard:standardLibrary:upStandardProduct'])
              " v-model="scope.row.manHour" placeholder="单价(元)" size="small" @change="(value) => upStandardProductListOfManHour(value, scope.row.id)
                ">
              </el-input>
              <span v-else>{{ scope.row.manHour }}</span>
            </template>
          </el-table-column>
          <el-table-column label="工时分组" prop="manHourGroup" show-overflow-tooltip width="100"></el-table-column>
          <el-table-column label="模板" prop="templateId" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.templateId" :disabled="!checkPermi(['standard:standardLibrary:upStandardProduct'])
                " filterable size="small" @change="(value) =>
                  upStandardProductListOfTemplate(value, scope.row.id)
                  ">
                <el-option v-for="(a, ai) in templateList" :key="ai" :label="a.name" :value="a.id"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="区间" prop="section" show-overflow-tooltip width="120"></el-table-column>
          <el-table-column label="操作" prop="section" width="160">
            <template slot-scope="scope">
              <el-button type="text" @click="sectionUp(scope.row)" :disabled="!checkPermi(['standard:standardLibrary:upStandardProduct'])
                ">区间设置</el-button>
              <el-button type="text" :disabled="!checkPermi(['standard:standardLibrary:upStandardProduct'])
                " @click="bindSupplierDensitySecond(scope.row)">绑定厂家</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- <el-pagination :current-page="currentPage" :page-size="300" :total="total" layout="total"
          style="position: absolute; right: 16px; bottom: 1px" @current-change="handleCurrentChange">
        </el-pagination> -->
        <p style="text-align: right;margin-right: 20px;color: #333;">共{{ total }}条</p>
      </el-row>
    </div>

    <el-dialog :visible.sync="addDia" title="分类添加" width="400px">
      <div class="body">
        <el-row style="line-height: 50px">
          <el-col :span="6" style="text-align: right">
            <span class="required-span">* </span>型号：
          </el-col>
          <el-col :offset="1" :span="16">
            <el-input v-model="addOb.model" clearable placeholder="请输入型号" size="small"
              @keyup.enter.native="addStandardTree"></el-input>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDia = false">取 消</el-button>
        <el-button :loading="addLoad" type="primary" @click="addStandardTree">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="updateDia" title="分类修改" width="400px">
      <div class="body">
        <el-row style="line-height: 50px">
          <el-col :span="6" style="text-align: right">
            <span class="required-span">* </span>型号：
          </el-col>
          <el-col :offset="1" :span="16">
            <el-input v-model="addOb.model" clearable placeholder="请输入型号" size="small"
              @keyup.enter.native="updateStandardTree"></el-input>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="updateDia = false">取 消</el-button>
        <el-button :loading="updateLoad" type="primary" @click="updateStandardTree">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :visible.sync="sectionUpDia" title="区间设置" width="80%">
      <div class="body" style="padding: 5px 0">
        <el-table :data="sectionList" border height="350px" style="width: 100%"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }">
          <el-table-column align="center" label="序号" type="index" width="70">
          </el-table-column>
          <el-table-column align="center" label="区间">
            <template slot-scope="scope">
              <el-input v-model="scope.row.thing" clearable placeholder="区间" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="芯数">
            <template slot-scope="scope">
              <el-input v-model="scope.row.cores" clearable placeholder="芯数" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="要求值">
            <template slot-scope="scope">
              <el-input v-model="scope.row.ask" clearable placeholder="要求值" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="要求描述">
            <template slot-scope="scope">
              <el-input v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 2 }" clearable placeholder="要求描述"
                size="small" type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="导体材质">
            <template slot-scope="scope">
              <el-input v-model="scope.row.conductorMaterial" clearable placeholder="导体材质" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="导体类型">
            <template slot-scope="scope">
              <el-input v-model="scope.row.conductorType" clearable placeholder="导体类型" size="small"></el-input>
            </template>
          </el-table-column>
          <!--          <el-table-column align="center" label="单价" width="120">-->
          <!--            <template slot-scope="scope">-->
          <!--              <el-input v-model="scope.row.price" clearable placeholder="单价" size="small"></el-input>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <!--          <el-table-column align="center" label="工时系数" width="120">-->
          <!--            <template slot-scope="scope">-->
          <!--              <el-input v-model="scope.row.manHour" clearable placeholder="工时系数" size="small"></el-input>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column align="center" label="操作" width="70">
            <template slot-scope="scope">
              <el-button circle icon="el-icon-minus" size="mini" type="danger"
                @click="sectionList.splice(scope.$index, 1)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="sectionUpDia = false">取 消</el-button>
        <el-button :loading="sectionLoad" type="primary" @click="sectionLoadAdd">保 存</el-button>
        <el-button icon="el-icon-plus" type="primary" @click="sectionList.push({ thing: '' })"></el-button>
      </span>
    </el-dialog>
    <bindSupplierDensityDialogAsk v-if="bindSupplierDensityDialog"
      :bindSupplierDensityDialog="bindSupplierDensityDialog" :currentRow="currentSupplierDensityRow"
      @closeBindPartDialog="closeBindSupplierDensityDialog">
    </bindSupplierDensityDialogAsk>
    <BatchCopy v-if="batchCopyDia" ref="BatchCopy" :selectTree1="selectTree" :standardId="standardId"
      @refreshList="refreshList"></BatchCopy>
  </div>
</template>

<script>
import Sortable from "sortablejs";
import draggable from "vuedraggable";
import {
  updateTreeSort,
  resetTreeDragBatch,
  delStandardTree,
  selectStandardTreeList,
  obtainItemParameterList,
  selectTestObjectByName,
  selectStandardMethods,
  addStandardTree,
  updateStandardTree,
  selectsStandardMethodByFLSSM,
  upStandardProductList,
  selectStandardProductListByMethodId,
  updateSection,
  upStandardProducts,
  getStandardTemplate,
  selectStandardProductByMethodId,
  selectStandardProductEnumByMethodId,
} from "@/api/standard/standardLibrary";
import bindSupplierDensityDialogAsk from "./components/bindSupplierDensityDialogAsk.vue";
import BatchCopy from "./components/BatchCopy.vue";
export default {
  name: 'StandardLibrary',
  components: {
    BatchCopy,
    bindSupplierDensityDialogAsk,
    draggable,
  },
  dicts: ["sys_factory", "sys_sub_lab", "sys_samp_type"],
  data() {
    return {
      tableKey: "productList",
      search: null,
      list: [],
      selectTree: "",
      factory: [],
      addDia: false,
      updateDia: false, // 修改树名字弹框
      oldModel: "",
      addOb: {
        factory: null,
        laboratory: null,
        sampleType: null,
        sample: null,
        model: null,
      },
      laboratory: [],
      addLoad: false,
      updateLoad: false,
      sampleType: [],
      sample: [],
      treeLoad: false,
      addPower: false,
      standardList: [],
      standardEnum: [],
      addStandardDia: false,
      addLoad2: false,
      productList: [],
      productTableLoading: false,
      tableLoad: false,
      tableLoad2: false,
      selects: [],
      addProductDia: false,
      productId: null,
      addLoad3: false,
      productEnum: [],
      expandedKeys: [],
      filters: [],
      sectionUpDia: false,
      sectionLoad: false,
      sectionRow: null,
      sectionList: [],
      templateList: [],
      total: 0,
      currentPage: 1,
      standardId: 0,
      total0: 0,
      currentPage0: 1,
      productList0: [],
      methodList: [],
      productTableLoading0: false,
      filters0: [],
      filters1: [],
      pages: 1,
      inspectionItem: null,
      inspectionItemSubclass: null,
      sonLaboratory: null,
      token: null,
      fileList: [],
      uploading: false,
      isEquipment: true,
      isHaveChildren: {},
      sortTable: null,
      currentSupplierDensityRow: {}, // 选择零件绑定本条数据的信息
      bindSupplierDensityDialog: false,
      isShowCopy: false,
      batchCopyDia: false,
      VUE_APP_BASE_API: process.env.VUE_APP_BASE_API,
      moreSelects: [],
    };
  },
  mounted() {
    this.selectEnumByCategoryForFactory();
    this.selectStandardTreeList();
    this.obtainItemParameterList();
    this.selectTestObjectByName();
    this.selectStandardMethods();
    this.selectEnumByCategoryForSonLaboratory();
    this.selectEnumByCategoryForsampleType();
    this.getStandardTemplate();
    this.selectStandardMethodsSec();
    this.token = {
      token: sessionStorage.getItem("token"),
    };
  },
  methods: {
    // 修改最子级名字
    editTreeName(info) {
      this.updateDia = true;
      this.oldModel = info.label;
    },
    // 拖拽时判定目标节点能否被放置
    // 'prev'、'inner' 和 'next'，前、插入、后
    allowDrop(draggingNode, dropNode, type) {
      if (draggingNode.level !== 3) return;
      if (draggingNode.data.level === dropNode.data.level) {
        if (draggingNode.data.parentId === dropNode.data.parentId) {
          return type === "prev" || type === "next";
        } else {
          return false;
        }
      } else {
        // 不同级进行处理
        return false;
      }
    },
    // tree拖拽成功完成时触发的事件
    handleDrop(draggingNode, dropNode, dropType, ev) {
      try {
        this.treeLoad = true;
        updateTreeSort(this.list).then((res) => {
          if (res.code === 200) {
            this.$message.success("操作成功");
          }
          this.treeLoad = false;
        });
      } catch (e) {
        this.treeLoad = false;
        console.log("e----", e);
      }
    },
    rowDrop(methodId) {
      const that = this;
      const tbody = document.querySelector(
        "#templateParamTable .el-table__body-wrapper tbody"
      );
      if (!this.sortTable) {
        this.sortTable = Sortable.create(tbody, {
          animation: 200, //动画时长
          handle: ".el-table__row", //可拖拽区域class
          //拖拽中事件
          onMove: ({ dragged, related }) => {
            const oldRow = that.productList[dragged.rowIndex]; //旧位置数据
            const newRow = that.productList[related.rowIndex]; //被拖拽的新数据
          },
          //拖拽结束事件
          onEnd: (evt) => {
            const curRow = that.productList.splice(evt.oldIndex, 1)[0]; // 当前被拖拽的信息
            that.productList.splice(evt.newIndex, 0, curRow);
            let arr = [];
            this.productList.forEach((item, index) => {
              const obj = Object.assign({
                sort: index,
                id: item.id,
              });
              arr.push(obj);
            });
            this.productTableLoading = true;
            // 拖拽完成后传给后端保存数据
            resetTreeDragBatch({ params: arr }).then((res) => {
              if (res.code === 200) {
                this.$message.success("保存成功");
              }
              this.productTableLoading = false;
            });
          },
        });
      }
    },
    hasChildWithId(nodes, name) {
      for (let node of nodes) {
        const comName = node.label + node.code;
        if (comName == name) {
          //判断递归结束条件
          this.isHaveChildren = node;
          return node;
        } else if (node.children && node.children.length > 0) {
          //判断children是否有数据
          this.hasChildWithId(node.children, name); //递归调用
        }
      }
    },
    // 调用tree过滤方法 中文英过滤
    filterNode (value, data, node) {
      if (!value) {　　　　//如果数据为空，则返回true,显示所有的数据项
        return true
      }
      // 查询列表是否有匹配数据，将值小写，匹配英文数据
      let val = value.toLowerCase()
      return this.chooseNode(val, data, node) // 调用过滤二层方法
    },
    // 过滤父节点 / 子节点 (如果输入的参数是父节点且能匹配，则返回该节点以及其下的所有子节点；如果参数是子节点，则返回该节点的父节点。name是中文字符，enName是英文字符.
    chooseNode (value, data, node) {
      if (data.label.indexOf(value) !== -1) {
        return true
      }
      const level = node.level
      // 如果传入的节点本身就是一级节点就不用校验了
      if (level === 1) {
        return false
      }
      // 先取当前节点的父节点
      let parentData = node.parent
      // 遍历当前节点的父节点
      let index = 0
      while (index < level - 1) {
        // 如果匹配到直接返回，此处name值是中文字符，enName是英文字符。判断匹配中英文过滤
        if (parentData.data.label.indexOf(value) !== -1) {
          return true
        }
        // 否则的话再往上一层做匹配
        parentData = parentData.parent
        index++
      }
      // 没匹配到返回false
      return false
    },

    searchFilter() {
      this.$refs.tree.filter(this.search);
    },
    refresh() {
      this.upIndex++;
    },
    handleNodeClick(val, node, el) {
      //树的值
      if (node.childNodes.length === 0) {
        this.isShowCopy = true;
      } else {
        this.isShowCopy = false;
      }
      this.total = 0;
      this.currentPage = 1;
      this.selectTree = "";
      this.getNodeParent(node);
      this.selectTree = this.selectTree.replace(" - ", "");
      let data = this.selectTree.split(" - ");
      let data2 = "";
      for (let index = data.length - 1; index >= 0; index--) {
        data2 += " - " + data[index];
      }
      this.selectTree = data2.replace(" - ", "");
      if (node.childNodes.length === 0) {
        this.selectsStandardMethodByFLSSM();
      }
      let trees = this.selectTree.split(" - ");
      this.addOb.factory = trees[0];
      this.addOb.laboratory = trees[1];
      this.addOb.sampleType = trees[2];
      this.addOb.sample = trees[3];
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
    remove(node, data) {
      this.$confirm("是否删除该层级", "提示", {
        type: "error",
      })
        .then(() => {
          // this.treeLoad = true
          this.selectTree = "";
          this.getNodeParent(node);
          this.selectTree = this.selectTree.replace(" - ", "");
          let data = this.selectTree.split(" - ");
          let data2 = "";
          for (let index = data.length - 1; index >= 0; index--) {
            data2 += " - " + data[index];
          }
          this.selectTree = data2.replace(" - ", "");
          delStandardTree({ tree: this.selectTree }).then((res) => {
            this.$message.success("已删除");
            let arr = this.selectTree.split(" - ");
            this.deleteStandard(this.list, arr[arr.length - 1]);
            this.selectTree = "";
            this.standardList = [];
            this.productList = [];
          });
        })
        .catch((e) => { });
    },
    deleteStandard(arr, label) {
      arr.forEach((item, index) => {
        if (item.label == label) {
          arr.splice(index, 1);
        } else {
          item.children && this.deleteStandard(item.children, label);
        }
      });
    },
    selectEnumByCategoryForFactory() {
      this.getConfigKey("sys_factory").then((response) => {
        this.factory = response.msg;
      });
    },
    selectEnumByCategoryForSonLaboratory() {
      let arr = [];
      this.getConfigKey("sys_sub_lab").then((response) => {
        arr = response.msg;
      });
      arr.forEach((a) => {
        this.filters.push({
          text: a.label,
          value: a.value,
        });
      });
    },
    selectEnumByCategoryForsampleType() {
      this.getConfigKey("sys_samp_type").then((response) => {
        this.sampleType = response.msg;
      });
    },
    selectStandardTreeList() {
      this.treeLoad = true;
      selectStandardTreeList().then((res) => {
        this.list = res.data;
        this.list.forEach((a) => {
          this.expandedKeys.push(a.label);
        });
        this.treeLoad = false;
      });
    },
    obtainItemParameterList() {
      obtainItemParameterList().then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.laboratoryName,
            value: a.laboratoryName,
          });
        });
        this.laboratory = data;
      });
    },
    selectTestObjectByName() {
      selectTestObjectByName().then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.specimenName,
            value: a.specimenName,
          });
        });
        this.sampleType = data;
      });
    },
    selectStandardMethods() {
      selectStandardMethods().then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.standardEnum = data;
      });
    },
    // 提交分类添加
    addStandardTree() {
      if (this.addOb.sampleType == null || this.addOb.sampleType == "") {
        this.$message.error("对象不存在");
        return;
      }
      if (this.addOb.model == null || this.addOb.model == "") {
        this.$message.error("请填写型号");
        return;
      }
      this.addLoad = true;
      addStandardTree(this.addOb)
        .then((res) => {
          this.$message.success("添加成功");
          this.addDia = false;
          this.list
            .find((a) => a.label == this.addOb.factory)
            .children.find((a) => a.label == this.addOb.laboratory)
            .children.find((a) => a.label == this.addOb.sampleType)
            .children.find((a) => a.label == this.addOb.sample)
            .children.push({
              code: "[5]",
              label: this.addOb.model,
              value: this.addOb.model,
            });
          this.addLoad = false;
        })
        .catch((e) => {
          this.addDia = false;
          this.addLoad = false;
        });
    },
    // 修改型号
    updateStandardTree() {
      if (this.addOb.sampleType == null || this.addOb.sampleType == "") {
        this.$message.error("对象不存在");
        return;
      }
      if (this.addOb.model == null || this.addOb.model == "") {
        this.$message.error("请填写型号");
        return;
      }
      this.addOb.oldModel = this.oldModel;
      this.updateLoad = true;
      updateStandardTree(this.addOb)
        .then((res) => {
          this.$message.success("添加成功");
          this.$tab.refreshPage();
          // this.selectStandardTreeList();
          this.updateDia = false;
          this.updateLoad = false;
        })
        .catch((e) => {
          this.updateDia = false;
          this.updateLoad = false;
        });
    },
    selectsStandardMethodByFLSSM() {
      this.tableLoad = true;
      selectsStandardMethodByFLSSM({
        tree: this.selectTree,
      }).then((res) => {
        this.tableLoad = false;
        this.standardList = res.data.standardMethodList;
        if (this.standardList && this.standardList.length > 0) {
          this.$refs.standard.setCurrentRow(this.standardList[0]);
          this.rowClick(this.standardList[0]);
        } else {
          this.productList = [];
        }
      });
    },
    upStandardProductList(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          ask: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    upStandardProductListOfTell(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          tell: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    // 标准库选择实验方法的回调
    upStandardProductListOfMethodS(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          methodS: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    // 标准库选择条件的回调
    upStandardProductListOfRadius(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          radius: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    upStandardProductListOfPrice(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          price: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    upStandardProductListOfManHour(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          manHour: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    upStandardProductListOfTemplate(value, index) {
      upStandardProductList({
        str: JSON.stringify({
          id: index,
          templateId: value,
        }),
      }).then((res) => {
        this.$message.success("已保存");
      });
    },
    handleSelectionChange(val) {
      this.selects = [];
      val.forEach((a) => {
        this.selects.push(a.id);
      });
    },

    refreshList() {
      this.batchCopyDia = false;
      const index = this.standardList.findIndex(
        (item) => item.id == this.standardId
      );
      if (index > -1) {
        this.rowClick(this.standardList[index]);
      }
    },
    rowClick(row, column, event) {
      this.currentPage = 1;
      this.tableLoad2 = true;
      this.standardId = row.id;
      selectStandardProductListByMethodId({
        id: row.id,
        tree: this.selectTree,
        page: this.currentPage,
      }).then((res) => {
        this.productList = res.data.productList;
        this.total = res.data.total;
        setTimeout(() => {
          this.productList.forEach((a) => {
            if (a.state == 1) this.toggleSelection(a);
          });
        }, 300);
        this.tableLoad2 = false;
        const tree = this.selectTree.split(" - ");
        // 选择最后一层树才可以拖拽排序
        if (tree.length === 4) {
          const name = tree[3] + "[4]";
          this.hasChildWithId(this.list, name);
          if (
            this.isHaveChildren.children &&
            this.isHaveChildren.children.length > 0
          ) {
            if (this.sortTable) {
              this.sortTable.destroy();
              this.sortTable = null;
            }
            return;
          }
        } else if (tree.length < 4) {
          if (this.sortTable) {
            this.sortTable.destroy();
            this.sortTable = null;
          }
          return;
        }
        this.$nextTick(() => {
          this.rowDrop(row.id);
        });
      });
    },
    toggleSelection(row) {
      this.$refs.productTable.toggleRowSelection(row, true);
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.state === 0) {
        return "warning-row";
      }
      return "";
    },
    upProductSelect(selection, row) {
      row.state = row.state == 1 ? 0 : 1;
      upStandardProductList({
        str: JSON.stringify({
          id: row.id,
          state: row.state,
        }),
      }).then((res) => {
        this.$message.success('已保存')
      });
    },
    filterHandler(value) {
      for (let column in value) {
        if (value[column].length === 0) {
          if (column === "inspectionItem") {
            this.inspectionItem = null;
          } else if (column === "inspectionItemSubclass") {
            this.inspectionItemSubclass = null;
          } else if (column === "sonLaboratory") {
            this.sonLaboratory = null;
          }
        } else {
          if (column === "inspectionItem") {
            this.inspectionItem = value[column][0];
          } else if (column === "inspectionItemSubclass") {
            this.inspectionItemSubclass = value[column][0];
          } else if (column === "sonLaboratory") {
            this.sonLaboratory = value[column][0];
          }
        }
        this.getList();
      }
    },
    handleAll(e) {
      if (e.length > 0) {
        this.productList.map((m) => {
          m.state = 0;
          this.upProductSelect(null, m);
          return m;
        });
      } else {
        this.productList.map((m) => {
          m.state = 1;
          this.upProductSelect(null, m);
          return m;
        });
      }
    },
    // 设置区间
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
                // price: JSON.parse(this.sectionRow.price)[ai],
                // manHour: JSON.parse(this.sectionRow.manHour)[ai],
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
                // price: JSON.parse(this.sectionRow.price)[ai],
                // manHour: JSON.parse(this.sectionRow.manHour)[ai],
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
    // 提交区间所填的数据
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
          // priceList.push(a.price)
          // manHourList.push(a.manHour)
          coresList.push(a.cores);
          conductorMaterialList.push(a.conductorMaterial);
          conductorTypeList.push(a.conductorType);
        }
      });
      if (sectionList.length === 0) {
        this.sectionRow.section = null;
        this.sectionRow.ask = null;
        this.sectionRow.tell = null;
        // this.sectionRow.price = null
        // this.sectionRow.manHour = null
        this.sectionRow.cores = null;
        this.sectionRow.conductorMaterial = null;
        this.sectionRow.conductorType = null;
      } else {
        this.sectionRow.section = JSON.stringify(sectionList);
        this.sectionRow.ask = JSON.stringify(askList);
        this.sectionRow.tell = JSON.stringify(tellList);
        // this.sectionRow.price = JSON.stringify(priceList)
        // this.sectionRow.manHour = JSON.stringify(manHourList)
        this.sectionRow.cores = JSON.stringify(coresList);
        this.sectionRow.conductorMaterial = JSON.stringify(
          conductorMaterialList
        );
        this.sectionRow.conductorType = JSON.stringify(conductorTypeList);
      }
      this.sectionLoad = true;
      if (this.moreSelects.length === 0) {
        updateSection({
          str: JSON.stringify({
            id: this.sectionRow.id,
            section: this.sectionRow.section,
            ask: this.sectionRow.ask,
            tell: this.sectionRow.tell,
            // price: this.sectionRow.price,
            // manHour: this.sectionRow.manHour,
            cores: this.sectionRow.cores,
            conductorMaterial: this.sectionRow.conductorMaterial,
            conductorType: this.sectionRow.conductorType,
          }),
        }).then((res) => {
          this.sectionLoad = false;
          this.$message.success("已保存");
          this.sectionUpDia = false;
        });
      } else {
        upStandardProducts({
          ids: JSON.stringify(this.moreSelects.map((a) => a.id)),
          standardProductList: {
            section: this.sectionRow.section,
            ask: this.sectionRow.ask,
            tell: this.sectionRow.tell,
            // price: this.sectionRow.price,
            // manHour: this.sectionRow.manHour,
            cores: this.sectionRow.cores,
            conductorMaterial: this.sectionRow.conductorMaterial,
            conductorType: this.sectionRow.conductorType,
          },
        }).then((res) => {
          this.sectionLoad = false;
          this.$message.success("已保存");
          this.sectionUpDia = false;
          this.currentPage0 = 1;
          this.getList();
          this.rowClick({
            id: this.standardId,
          });
        });
      }
    },
    bindSupplierDensitySecond(row) {
      this.bindSupplierDensity(row);
    },
    // 打开厂家绑定弹框
    bindSupplierDensity(row) {
      this.currentSupplierDensityRow = row;
      this.bindSupplierDensityDialog = true;
    },
    closeBindSupplierDensityDialog() {
      this.bindSupplierDensityDialog = false;
    },
    getStandardTemplate() {
      getStandardTemplate().then((res) => {
        this.templateList = res.data;
      });
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.tableLoad2 = true;
      selectStandardProductListByMethodId({
        id: this.standardId,
        tree: this.selectTree,
        page: val,
      }).then((res) => {
        this.productList = res.data.productList;
        setTimeout(() => {
          this.productList.forEach((a) => {
            if (a.state == 1) this.toggleSelection(a);
          });
        }, 200);
        this.tableLoad2 = false;
      });
    },
    openCopyDia() {
      this.batchCopyDia = true;
      this.$nextTick(() => {
        this.$refs.BatchCopy.getList();
      });
    },
    selectStandardMethodsSec() {
      selectStandardMethods().then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.code,
            value: a.code,
          });
        });
        this.methodList = data;
      });
    },
    handleSelectAll0(rows) {
      if (rows.length) {
        rows.forEach((a) => {
          if (!this.moreSelects.find((b) => a.id === b.id)) {
            this.moreSelects.push(a);
          }
        });
      } else {
        this.productList0.forEach((a) => {
          this.moreSelects = this.moreSelects.filter((b) => b.id != a.id);
        });
      }
    },
    handleSelectionChange0(val, row) {
      if (this.moreSelects.find((a) => a.id === row.id)) {
        this.moreSelects = this.moreSelects.filter((a) => a.id != row.id);
      } else {
        this.moreSelects.push(row);
      }
    },
    getList() {
      this.productTableLoading0 = true;
      this.getItemEnum();
      selectStandardProductByMethodId({
        id: this.standardId,
        tree: this.selectTree,
        page: this.currentPage0,
        laboratory: this.sonLaboratory,
        items: this.inspectionItemSubclass,
        item: this.inspectionItem,
      }).then((res) => {
        this.productList0 = res.data.records;
        this.total0 = res.data.total;
        this.productTableLoading0 = false;
        this.page = res.data.pages;
        this.$nextTick(() => {
          this.productList0.forEach((a, i) => {
            if (this.moreSelects.find((b) => a.id == b.id)) {
              this.$refs.productTable0.toggleRowSelection(
                this.productList0[i],
                true
              );
            }
          });
        });
      });
    },
    handleCurrentChange0(e) {
      this.currentPage0 = e;
      this.getList();
    },
    getItemEnum() {
      selectStandardProductEnumByMethodId({
        id: this.standardId,
        tree: this.selectTree,
        item: this.inspectionItem,
      }).then((res) => {
        this.filters0 = [];
        this.filters1 = [];
        res.data.item.forEach((a) => {
          this.filters0.push({
            text: a.inspectionItem,
            value: a.inspectionItem,
          });
        });
        res.data.items.forEach((a) => {
          if (a != null) {
            this.filters1.push({
              text: a.inspectionItemSubclass,
              value: a.inspectionItemSubclass,
            });
          }
        });
      });
    },
  },
};
</script>
<style scoped>
.standard {
  padding-top: 10px;
  display: flex;
  height: calc(100vh - 90px);
}

.left {
  width: 330px;
  height: calc(100% - 40px - 10px);
  background-color: white;
  padding: 15px;
}

.custom-tree-node {
  width: 100%;
  line-height: 32px;
}

.custom-tree-node .el-icon-delete {
  color: #3a7bfa;
  opacity: 0;
  font-size: 18px;
}

.custom-tree-node:hover .el-icon-delete {
  opacity: 1;
}

.custom-tree-node .el-icon-edit {
  color: #3a7bfa;
  opacity: 0;
  font-size: 18px;
}

.custom-tree-node:hover .el-icon-edit {
  opacity: 1;
}

.node_i {
  color: orange;
  font-size: 18px;
}

.right {
  margin-left: 5px;
  width: calc(100% - 350px);
  height: calc(100% - 40px);
}

.right .title {
  height: 34px;
  line-height: 34px;
  padding: 0 10px;
  background-color: white;
}

.standard_table {
  border-top: 1px solid #ebeef5;
  background-color: white;
}

.product_table {
  border-top: 1px solid #ebeef5;
  height: calc(100% - 235px);
  margin-top: 5px;
  background-color: white;
  user-select: none;
}

.product_table .el-table {
  height: calc(100% - 35px) !important;
}

.sort {
  width: 80% !important;
  overflow: hidden;
}

>>>.el-table__body-wrapper {
  height: calc(100% - 46px) !important;
}

>>>.header-class {
  height: 40px !important;
}

>>>.header-class th.el-table__cell>.cell {
  line-height: 20px !important;
  padding-top: 0 !important;
  padding-bottom: 0 !important;
}

>>>.el-table__row {
  height: 35px !important;
}

.search {
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding-bottom: 10px;
}

.search-item {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.search-item .el-row {
  display: flex;
  align-items: center;
}

.search-item .el-col {
  margin-left: 0;
}

.more-edit .dialog-footer {
  position: absolute;
  top: 15px;
  right: 70px;
}

>>>.is-disabled .el-textarea__inner {
  background: rgba(0, 0, 0, 0.05) !important;
}
</style>
<style scoped>
.standard .el-tree-node__content {
  height: 32px;
  font-size: 14px;
  border-radius: 2px;
}

.standard .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
  color: #3a7bfa;
}

.standard .has-gutter .el-table__cell .cell {
  line-height: 34px;
  background-color: #f8f8f8;
}

.standard .has-gutter .el-table__cell {
  background-color: #fafafa !important;
}

.standard .standard_table .el-table__row .cell {
  font-size: 14px;
}

.standard .el-table .warning-row .cell {
  color: #bababa;
}

.standard .el-table-filter__list {
  max-height: 400px;
  overflow-y: auto;
}

.standard .el-upload {
  width: 100%;
}

.standard .el-upload-dragger {
  width: 100%;
}

.standard .handleBtn.is-disabled .el-upload:focus {
  color: #c0c4cc !important;
}

.standard .avatar-uploader .el-upload {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
