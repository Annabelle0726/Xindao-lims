<!--铜单丝下单-->
<template>
  <div class="app-container">
    <div class="header">
      <div>
        <span>铜单丝下单</span>
      </div>
      <div>
        <el-button v-show="active==1 && addObj.orderType === '进厂检验'" :loading="noNeedCheckLoad" size="small" type="primary" @click="noNeedCheck">免检</el-button>
        <el-button v-show="active==1" :loading="saveLoad" size="small" type="primary" @click="save">提交</el-button>
        <el-button size="small" @click="goBack">
          <span style="color: #3A7BFA;">返回</span>
        </el-button>
      </div>
    </div>
    <div class="container">
      <div class="search">
        <el-form ref="addObj" :inline="true" :model="addObj" :rules="addObjRules" label-width="90px">
          <el-row>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="样品名称:" prop="sample">
                <el-input v-model="addObj.sample" readonly size="small" style="width: 208px">
                  <template slot="append"><el-button slot="append" :disabled="active>1" icon="el-icon-search"
                                                     @click="selectStandardTree = true"></el-button></template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="到货数量:" prop="qtyArrived" required>
                <el-input v-model="addObj.qtyArrived" :disabled="active > 1" class="addObj-info"
                          clearable placeholder="" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="单位:" prop="buyUnitMeas">
                <el-input v-model="addObj.buyUnitMeas" :disabled="active > 1" clearable class="addObj-info" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="批次号:" prop="updateBatchNo">
                <el-input v-model="addObj.updateBatchNo" :disabled="active > 1" class="addObj-info" clearable size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="检验类型:">
                <el-select v-model="addObj.orderType" clearable disabled size="small" style="width: 100%">
                  <el-option v-for="(a, ai) in dict.type.check_type" :key="ai" :label="a.label" :value="a.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="约定时间:" prop="appointed">
                <el-date-picker
                  v-model="addObj.appointed"
                  :disabled="active > 1"
                  format="yyyy-MM-dd"
                  placeholder="选择日期"
                  size="small"
                  style="width: 100%"
                  type="date"
                  value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="材料厂家:" prop="supplierName">
                <el-input v-model="addObj.supplierName" :disabled="active > 1" class="addObj-info" clearable placeholder="" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="抽检数量:" prop="testQuantity">
                <el-input v-model="addObj.testQuantity" :disabled="active > 1" class="addObj-info" clearable placeholder="" size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="规格型号:" placeholder="请填写" prop="partDetail">
                <el-tooltip :content="addObj.partDetail" :disabled="!addObj.partDetail">
                  <el-input v-model="addObj.partDetail" :disabled="active > 1" clearable class="addObj-info" size="small"></el-input>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="到货时间:" prop="declareDate">
                <el-date-picker
                    v-model="addObj.declareDate"
                    :disabled="active > 1"
                    format="yyyy-MM-dd"
                    placeholder="选择日期"
                    size="small"
                    style="width: 100%"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item class="addObj-form-item" label="备注:">
                <el-input v-model="addObj.remark" :autosize="{ minRows: 2, maxRows: 2}" :placeholder="active>1 ? '' : '请输入'" :disabled="active>1" clearable
                          size="small" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div style="display: flex;justify-content: space-between;background-color: #F5F7FB;margin-bottom: 6px">
        <div v-if="active==1">
          <el-form :inline="true" :model="addObj1" label-width="90px">
            <el-form-item label="样品型号:" style="margin-bottom: 6px;margin-top: 6px">
              <el-select v-model="model" :placeholder="active>1 ? '' : '请输入'"
                         allow-create clearable default-first-option filterable
                         size="small"
                         @change="changeModel">
                <el-option v-for="item in models" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="检验标准:" style="margin-bottom: 6px;margin-top: 6px">
              <el-select v-model="standardMethodListId" :loading="methodLoad"
                         :placeholder="active>1 ? '' : '请输入'" clearable size="small"
                         @change="changeStandardMethodListId" @focus="methodFocus">
                <el-option v-for="item in methods" :key="item.id" :label="item.code" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-bottom: 6px;margin-top: 6px">
          <el-button v-show="active==1" :disabled="sampleList.length === 3" size="small" type="primary" @click="handleSplitCountNum">拆分</el-button>
        </div>
      </div>
      <div style="height: auto;">
        <el-table ref="sampleTable" :data="sampleList" class="el-table sampleTable" highlight-current-row
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                  max-height="400px" tooltip-effect="dark" @selection-change="selectSample" @row-click="rowClick">
          <el-table-column v-if="active==1" :selectable="selectable" type="selection" width="60"></el-table-column>
          <el-table-column align="center" label="序号" type="index" width="65"></el-table-column>
          <el-table-column align="center" label="样品名称" min-width="100" prop="sample">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sample" :readonly="active>1" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="样品编号" min-width="140" prop="sampleCode">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sampleCode" :readonly="active>1" clearable placeholder="不填写则系统自动生成"
                        size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="样品型号" min-width="100" prop="model">
            <template slot-scope="scope">
              <el-select v-model="scope.row.model" :disabled="active>1" allow-create default-first-option filterable
                         placeholder="样品型号" size="small" style="width: 100%;" @change="handleChangeModel">
                <el-option v-for="item in models" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="型号参数" prop="modelNum" width="130">
            <template slot-scope="scope">
              <el-input v-model="scope.row.modelNum" :disabled="active>1|| scope.$index !== 0" clearable placeholder="非必填"
                        size="small"
                        @input="methodChange(scope.row.standardMethodListId, scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="检验标准" min-width="100" prop="standardMethodListId">
            <template slot-scope="scope">
              <el-select v-model="scope.row.standardMethodListId" :disabled="scope.row.model==null||active>1 || scope.$index !== 0"
                         :loading="methodLoad" :readonly="active>1" clearable placeholder="检验标准"
                         size="small" style="width: 100%;" @change="(value)=>methodChange(value, scope.row)" @clear="productList = []" @focus="methodFocus">
                <el-option v-for="item in methods" :key="item.id" :label="item.code" :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="addObj.mating==1" align="center" label="配套样品名称" prop="joinName" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.joinName" :autosize="{ minRows: 1, maxRows: 1}" size="small"
                        type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column v-if="addObj.mating==1" align="center" label="配套样品型号" prop="joinModel" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.joinModel" :autosize="{ minRows: 1, maxRows: 1}" size="small"
                        type="textarea"></el-input>
            </template>
          </el-table-column>
          <el-table-column v-if="addObj.mating==1" align="center" label="配套样品数量" prop="joinNum" width="140">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.joinNum" :controls="false" :max="100" :min="1" :precision="0"
                               size="small" style="width: 80%;"></el-input-number>
            </template>
          </el-table-column>
          <el-table-column align="center" label="待检项数量" prop="quantity" width="105"></el-table-column>
        </el-table>
        <el-table ref="productTable" v-loading="getProductLoad" :data="productList" :row-class-name="tableRowClassName" class="el-table"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                  max-height="400px" style="margin-bottom: 10px;" tooltip-effect="dark"
                  @select="selectOne" @selection-change="selectProduct" @select-all="handleAll">
          <el-table-column v-if="active==1" :selectable="selectable" type="selection" width="65"></el-table-column>
          <el-table-column label="检验项分类" min-width="140" prop="inspectionItemClass" show-overflow-tooltip></el-table-column>
          <el-table-column label="检验项" min-width="140" prop="inspectionItem" show-overflow-tooltip>
            <template slot="header" slot-scope="scope">
              <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
                <span>检验项</span>
                <el-input
                  v-if="active==1"
                  v-model="inspectionItem"
                  placeholder="请输入"
                  size="mini"
                  @input="searchFilterList"/>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="检验项子项" min-width="140" prop="inspectionItemSubclass"
                           show-overflow-tooltip>
            <template slot="header" slot-scope="scope">
              <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
                <span>检验项子项</span>
                <el-input
                  v-if="active==1"
                  v-model="inspectionItemSubclass"
                  placeholder="请输入"
                  size="mini"
                  @input="searchFilterList"/>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="要求值" min-width="220px" prop="ask">
            <template slot-scope="scope">
              <el-input v-if="active==1&&isAskOnlyRead&&scope.row.inspectionValueType!='5'" v-model="scope.row.ask" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求值"
                        size="small" type="textarea"
                        @change="e=>requestChange(e,scope.row,'ask')"></el-input>
              <span v-else>
                <!-- <template v-if="(scope.row.ask.indexOf('D')>-1
                ||scope.row.ask.indexOf('W')>-1
                ||scope.row.ask.indexOf('X')>-1
                ||scope.row.ask.indexOf('RTS')>-1
                ||scope.row.ask.indexOf('H')>-1
                ||scope.row.ask.indexOf('L')>-1)&&active==1">
                  <input class="askRts" v-model="scope.row.rts" /><br />{{ scope.row.ask }}
                </template>
                <template v-else>{{ scope.row.ask }}</template> -->
                <template >{{ scope.row.ask }}</template>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="要求描述" min-width="220px" prop="tell">
            <template slot-scope="scope">
              <el-input v-if="active==1&&isAskOnlyRead" v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求描述"
                        size="small" type="textarea"
                        @change="e=>requestChange(e,scope.row,'tell')"></el-input>
              <span v-else>
                <template >{{ scope.row.tell }}</template>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="条件" min-width="140" prop="radius" show-overflow-tooltip></el-table-column>
          <el-table-column label="试验方法" min-width="120" prop="methodS" show-overflow-tooltip>
            <template slot="header" slot-scope="scope">
              <div style="display: flex;align-items: center;flex-direction: column;font-size: 14px">
                <span>试验方法</span>
                <el-input
                  v-if="active==1"
                  v-model="methodS"
                  placeholder="请输入"
                  size="mini"
                  @input="searchFilterList"/>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="计量单位" prop="unit" show-overflow-tooltip width="100"></el-table-column>
          <el-table-column label="单价" prop="price" show-overflow-tooltip width="100"></el-table-column>
          <!-- <el-table-column prop="manDay" label="预计时间(天)" width="120" show-overflow-tooltip></el-table-column>
          <el-table-column prop="manHour" label="工时系数" width="100" show-overflow-tooltip></el-table-column> -->
          <!-- <el-table-column prop="deviceGroup" label="设备组" width="120" show-overflow-tooltip></el-table-column> -->
          <el-table-column label="区间" min-width="120" prop="section" show-overflow-tooltip></el-table-column>
          <el-table-column :filter-method="filterHandler" :filters="filters" label="子实验室" min-width="130" prop="sonLaboratory"
                           show-overflow-tooltip></el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog :visible.sync="selectStandardTree" title="选择样品" width="500px">
      <div v-if="selectStandardTree" v-loading="selectStandardTreeLoading" class="body"  style="height: 60vh;overflow-y: auto;user-select: none;">
        <el-row>
          <el-col :span="24">
            <el-input v-model="search" clearable placeholder="输入关键字进行搜索" size="small"
                      style="margin-bottom: 5px;" suffix-icon="el-icon-search" @blur="searchFilter" @clear="searchFilter"
                      @keyup.enter.native="searchFilter"></el-input>
          </el-col>
        </el-row>
        <el-tree ref="tree" :data="list" :default-expanded-keys="expandedKeys" :filter-node-method="filterNode"
                 :props="{ children: 'children', label: 'label' }" highlight-current node-key="label" @node-click="handleNodeClick"
                 @node-expand="nodeOpen" @node-collapse="nodeClose" @dblclick.native="activeStandardTree">
          <div slot-scope="{ node, data }" class="custom-tree-node">
            <el-row>
              <el-col :span="24">
                <span><i
                  :class="`node_i ${data.children != undefined ? (data.code==='[1]'?'el-icon-folder-opened':'el-icon-folder') : 'el-icon-tickets'}`"></i>
                  {{ data.code }} {{ data.label }}</span>
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
    <!--特殊值处理框-->
    <el-dialog :before-close="beforeClose" :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false"
               :visible.sync="bsm1DiaAll"
               min-width="400px"
               title="检测到特殊项，请作出以下选择">
      <div v-for="(item, index) in bsm1DiaList" :key="item.id" class="body" style="max-height: 60vh;">
        <span>{{item.inspectionItem}}</span>
        <el-row v-if="item.bsm1">
          <el-col :span="24" class="search_thing" style="height: initial;margin: 5px 0;">
            <div class="search_label" style="width: 80px;"><span class="required-span">* </span>选项：</div>
            <div class="search_input">
              <el-radio-group v-model="item.bsm1Val" v-removeAriaHidden @input="upBsmAll(item)">
                <el-radio v-for="(a, ai) in JSON.parse(item.bsmRow.sectionCopy)" :key="ai" :label="a" style="margin-bottom: 2px;margin-top: 2px;"></el-radio>
              </el-radio-group>
            </div>
          </el-col>
          <el-col :span="24" class="search_thing" style="height: initial;margin: 5px 0;">
            <div class="search_label" style="width: 80px;">要求值：</div>
            <div class="search_input">
              <el-radio-group v-model="item.bsm1Val" v-removeAriaHidden @input="upBsmAll(item)">
                <el-radio v-for="(a, ai) in JSON.parse(item.bsmRow.sectionCopy)" :key="ai"
                          :label="a">{{JSON.parse(item.bsmRow.askCopy)[ai]}}</el-radio>
              </el-radio-group>
            </div>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button :loading="saveLoad" type="primary" @click="save1">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :show-close="false" :visible.sync="bsm3Dia"
               title="区间值填写" width="800px">
      <el-table
        :data="editTable"
        height="80vh" style="width: 100%">
        <!-- inspectionItemList -->
        <el-table-column
          label="检验项"
          prop="inspectionItemList"
          width="180">
        </el-table-column>
        <el-table-column
          label="样品编号"
          prop="sampleCode"
          width="180">
        </el-table-column>
        <el-table-column
          label="样品型号"
          prop="model"
          width="180">
        </el-table-column>
        <el-table-column
          label="识别符号"
          prop="symbolItem">
        </el-table-column>
        <el-table-column
          label="识别符值"
          prop="value">
          <template slot-scope="scope">
            <el-input v-model="scope.row.value" placeholder="请输入" size="small" @input="inputValueHandler(scope.row,scope.$index)"></el-input>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="bsm3Dia=false">取 消</el-button>
          <el-button :loading="saveLoad" type="primary" @click="save0">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog
      :visible.sync="noNeedCheckDia"
      title="免检提示"
      width="32%">
      <span>确认免检当前检验单？</span>
      <span slot="footer" class="dialog-footer">
        <el-button :loading="noNeedCheckLoad" @click="noNeedCheckDia = false">取 消</el-button>
        <el-button :loading="noNeedCheckLoad" type="primary" @click="handleNoNeedCheck">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Vue from 'vue'
import {
  addRawCopperOrder,
  getInsOrder,
  getUserNow,
  addRawCopperOrderExemptionOrder,
  selectStandardTreeList2,
  selectsStandardMethodByFLSSM,
  selectInsOrderTemplate,
  selectStandardProductList,
  selectStandardMethodEnum
} from "@/api/business/rawMaterialOrder";

var validateSixDecimalPositives = (rule, value, callback) => {
  if (value == 0 || value == null) {
    callback(new Error('请输入正确的数字或小数'))
  } else if (!/^[0-9]+.?[0-9]*$/.test(value)) {
    callback(new Error('请输入数字或小数'))
  } else {
    callback()
  }
}

export default {
  name: 'CopperOrder',
  components: {},
  dicts: ['check_type', 'urgency_level'],
  data() {
    return {
      active: '',
      currentId: '',
      sampleSelectionList: [],//样品表格选中数据
      editTable:[],
      noNeedCheckLoad: false, // 免检按钮loading
      template: null,
      templates: [],
      addObj: {
        testQuantity: null, // 抽检数量
        custom: null,
        company: null,
        userId: null,
        companyId: '',
        type: '0', // 紧急程度
        code: null,
        remark: null, // 备注
        mating: 0,
        sample: null, // 样品名称
        appointed: '', // 约定时间
        buyUnitMeas: null, // 单位
        updateBatchNo: null, // 批次号
        partDetail: null, // 规格型号
        declareDate: '', // 到货时间
        factory: null,
        laboratory: null,
        sampleType: null,
        sampleNum: 1,
        unit: null,
        model: null,
        method: null,
        phone: null,
        processing: 1,
        isLeave: 0,
        orderType: '进厂检验', // 检验类型
        supplierName: null, // 材料厂家
        qtyArrived: null, // 到货数量
      },
      sample: {
        sampleCode: null,
        laboratory: null,
        factory: null,
        sampleType: null,
        sample: null,
        model: null,
        modelNum: null,
        sampleNum: 1,
        isLeave: 0,
        unit: null
      },
      selectStandardTree: false,
      search: null,
      list: [],
      selectStandardTreeLoading: false,
      selectTree: null,
      expandedKeys: [],
      sampleList: [],
      sampleIds: [],
      methodList: [],
      addSampleDia: false,
      count: 1,
      productList: [],
      productList0: [],
      bsm1DiaList: [],
      productIds: [],
      productListSelected: [],
      getProductLoad: false,
      saveLoad: false,
      templateDia: false,
      templateLoading: false,
      templateName: '',
      upLoad: false,
      units: [],
      models: [],
      methods: [],
      methodLoad: false,
      filters: [],
      currentMethod: null,
      isAskOnlyRead: false,
      sampleId: null,
      bsmRow: null,
      bsm1: false,
      bsm1Val: null,
      bsm1DiaAll: false,
      bsm2: false,
      bsm2Val: null,
      bsm2Dia: false,
      bsmRow3: null,
      bsm3Val: null,
      bsm3Dia: false,
      // total: 0,
      RTS: '',
      totalArr: [],
      addObj1: {},
      model: null,
      standardMethodListId: null,
      symbolList:['RTS'],
      inspectionItem:null,
      inspectionItemSubclass:null,
      methodS:null,
      isBsm2Val2:false,
      isShowInput: false,
      temId: '',
      selectiveEcho: [], // 检验下单的时候勾选检验项目,如果使用筛选提交显示检验项目为空 回显列表
      addObjRules: { // 表单校验规则
        sample: [
          { required: true, message: '请选择样品', trigger: 'change' }
        ],
        qtyArrived: [
          { validator: validateSixDecimalPositives, trigger: 'blur' }
        ],
        testQuantity: [
          { required: true, message: '请填写抽检数量', trigger: 'blur' }
        ],
        supplierName: [
          { required: true, message: '请填写材料厂家', trigger: 'blur' }
        ],
        appointed: [
          { required: true, message: '请选择约定时间', trigger: 'change' }
        ],
        buyUnitMeas: [
          { required: true, message: '请填写单位', trigger: 'blur' }
        ],
        updateBatchNo: [
          { required: true, message: '请填写批次号', trigger: 'blur' }
        ],
        partDetail: [
          { required: true, message: '请填写规格型号', trigger: 'blur' }
        ],
        declareDate: [
          { required: true, message: '请填写到货时间', trigger: 'change' }
        ]
      },
      noNeedCheckDia: false,
    }
  },
  watch: {
    sampleList() {
      this.addObj.method = null
      this.productList = []
    },
    productList: {
      deep: true,
      handler(val) {
        if (val && val.length > 0) {
          let arr = [];
          val.forEach(item => {
            if (item.sonLaboratory && !arr.find(a => a.value == item.sonLaboratory)) {
              arr.push({
                text: item.sonLaboratory,
                value: item.sonLaboratory
              })
            }
          })
          this.filters = arr
        }
      }
    },
    sampleList: {
      deep: true,
      handler(val) {
        this.getTotal()
      }
    },
    'addObj.sample'(val) {
      this.model = null
      this.standardMethodListId = null
    },
    'addObj.sampleNum'(val) {
      this.model = null
      this.standardMethodListId = null
    }
  },
  mounted() {
    this.active = this.$route.query.active
    this.currentId = this.$route.query.currentId
    this.getInfoRow();
  },
  activated() {
    this.active = this.$route.query.active
    this.currentId = this.$route.query.currentId
    this.getInfoRow();
  },
  methods: {
    getInfoRow() {
      this.getUserNowInfo()
      this.selectStandardTreeList()
      this.selectStandardMethods()
      this.getInfo() // 获取数据
    },
    // 拆分
    handleSplitCountNum () {
      this.sample.joinName = null
      this.sample.joinModel = null
      this.sample.joinNum = 1
      // 两条数据保持一致
      this.sample.sample = this.sampleList[0].sample
      this.sample.model = this.sampleList[0].model
      this.sample.unit = this.sampleList[0].unit
      this.sample.modelNum = this.sampleList[0].modelNum
      this.sample.standardMethodListId = this.sampleList[0].standardMethodListId
      this.sample.insProduct = []
      this.sample.id = this.count
      this.sample.childSampleList = []
      this.sample.insulating = null
      this.sample.sheath = null
      this.sampleList.push(this.HaveJson(this.sample))
      this.sampleList.forEach(item => {
        item.quantity = 1
      })
      this.count++
    },
    noNeedCheck () {
      this.$refs['addObj'].validate((valid) => {
        if (valid) {
          if (!this.sampleList.every(m => m.standardMethodListId)) {
            this.$message.error('请选择检验标准')
            return
          }
          // 检验类型为进厂检验时可选择免检，但不能选择检验项
          const sampleList = this.HaveJson(this.sampleList)
          sampleList.forEach(item => {
            item.insProduct = this.productList
          })
          this.getTotal(sampleList)
          let projectNum = this.totalArr.filter(a => a.state == 1).length
          if (projectNum!=0){
            this.$message.error('免检不可选择检验项')
            return
          }
          this.noNeedCheckDia = true
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleNoNeedCheck () {
      const sampleList = this.HaveJson(this.sampleList)
      sampleList.forEach(item => {
        item.insProduct = this.productList
      })
      this.getTotal(sampleList)
      let projectNum = this.totalArr.filter(a => a.state == 1).length
      if (projectNum!=0){
        this.$message.error('免检不可选择检验项')
      } else {
        let trees = this.selectTree.split(" - ")
        if (trees.length < 3) {
          this.$message.error('未选择对象')
          return
        }
        this.addObj.factory = trees[0]
        this.addObj.laboratory = trees[1]
        this.addObj.sampleType = trees[2]
        if (trees[3] === undefined || trees[3] === '') {
          this.addObj.sample = trees[2]
        } else {
          this.addObj.sample = trees[3]
        }
        this.addObj.model = (trees[4] == undefined ? null : trees[4])
        this.noNeedCheckLoad = true
        addRawCopperOrderExemptionOrder({
          copperInsOrder: this.addObj,
          sampleList: sampleList.map(a => {
            if (a.insProduct.length > 0) {
              a.insProduct.forEach(c => {
                delete c.id
              })
            }
            if (a.modelNum) {
              // 根据x号判断型号参数与样品型号的拼接位置
              // 例如×4，就为500ML×4；4×，就为4×500ML
              const index = a.modelNum.indexOf('×')
              if (index === 0) {
                a.model = a.model + a.modelNum
              } else if (index === -1) {
                a.model = a.model + '-' + a.modelNum
              } else {
                a.model = a.modelNum + a.model
              }
            } else {
              a.model = a.model + ((a.modelNum == null || a.modelNum == '' || a.modelNum == 'null') ? '' : ('-' + a.modelNum))
            }
            a.insProduct = a.insProduct.filter(b=>b.state === 1)
            return a
          })
        }).then(res => {
          this.noNeedCheckLoad = false
          this.$message.success('已提交')
          this.closeOpenPage()
        }).catch(e=>{
          this.noNeedCheckLoad = false
        })
      }
    },
    // 获取数据
    getInfo () {
      if (this.active != 1) {
        // 查看流程
        // 请求接口，回显数据
        getInsOrder({orderId: this.currentId}).then(res => {
          this.addObj = {
            ...res.data.insOrder
          }
          this.$set(this.addObj, 'qtyArrived', res.data.insOrderTemplate.qtyArrived)
          this.$set(this.addObj, 'buyUnitMeas', res.data.insOrderTemplate.buyUnitMeas)
          this.$set(this.addObj, 'updateBatchNo', res.data.insOrderTemplate.updateBatchNo)
          this.$set(this.addObj, 'supplierName', res.data.insOrderTemplate.supplierName)
          this.$set(this.addObj, 'declareDate', res.data.insOrderTemplate.declareDate)
          this.addObj.type = String(this.addObj.type)
          this.sampleList = this.HaveJson(res.data.sampleProduct);
          this.getProNum()
          this.addObj.sampleNum = this.sampleList.length
          this.$nextTick(() => {
            this.$refs.sampleTable.doLayout()
            if (this.sampleList.length > 0) { // 查看时如果有样品默认选中第一条
              this.productList = this.sampleList[0].insProduct
              this.$refs.sampleTable.setCurrentRow(this.sampleList[0], true)
              this.rowClick(this.sampleList[0])
            }
          })
        })
      }
    },
    //特殊值填写处理
    inputValueHandler(row,index){
      if(row){
        const nextIndex = index+1
        for (let i = nextIndex; i < this.editTable.length; i++) {
          const element = this.editTable[i];
          if(element.model==row.model&&row.symbolItem==element.symbolItem){
            this.editTable[i].value = row.value
          }
        }
      }
    },
    changeProductList0(){
      this.productList0.forEach(a=>{
        let obj = this.productList.find(m => m.id == a.id)
        if(obj){
          a.state = obj.state
          a.section = obj.section
          a.ask = obj.ask
          a.manHour = obj.manHour
          a.price = obj.price
          a.tell = obj.tell
        }
        if(a.state == 0&&a.bsmRow){
          a = this.HaveJson(a.bsmRow)
        }
      })
    },
    searchFilterList () {
      const vtw = {
        inspectionItem: this.inspectionItem, // 检验项
        inspectionItemSubclass: this.inspectionItemSubclass, // 检验项子项
        methodS: this.methodS, // 试验方法
      }
      const isHaveValue = Object.values(vtw).some(item => {
        return item
      })
      this.changeProductList0()
      if (isHaveValue) {
        for(let i in vtw) {
          if (vtw[i]) {
            this.productList = this.productList0.filter((item) => {
              return item[i] && item[i].includes(vtw[i])
            })
          }
        }
        this.$nextTick(()=>{
          this.productList.forEach(a => {
            if (a.state == 1) this.toggleSelection(a)
          })
        })
      } else {
        // 没有查询条件时渲染所有数据
        this.productList = this.productList0
        this.$nextTick(()=>{
          this.productList.forEach(a => {
            if (a.state == 1) this.toggleSelection(a)
          })
        })
      }
    },
    containsValue(str) {
      if(str){
        let symbolItem = ''
        this.symbolList.some(value =>{
          if(str.includes(value)){
            symbolItem  = value
            return true
          }
        })
        return symbolItem
      }
    },
    handleAsk(ask,symbolItem, value) {
      try{
        let code = [">", "<", "=", "＞", "＜", "≥", "≤", "±"];
        let code2 = ['+', '*', '/', '-']
        if (ask.includes('&')) {
          // 多个条件
          let arr0 = ask.split('&')
          let arr1 = []
          arr0.forEach(m => {
            let index = code.findIndex(b => m.includes(b))
            if (index > -1) {
              let arr = m.split(code[index]).filter(b => !!b)
              let num = eval(this.replaceAll(arr[0], symbolItem, value))
              m = code[index] + '' + num
              arr1.push(m)
            }
          })
          return arr1.join('&')
        } else if (ask.includes('～') || ask.includes('~')) {
          let arr0 = []
          if (ask.includes('～')) {
            arr0 = ask.split('～')
          } else {
            arr0 = ask.split('~')
          }
          // 多个条件
          let arr1 = []
          arr0.forEach(m => {
            m = m.replace(symbolItem, value)
            const index = code2.findIndex(b => m.includes(b))
            if (index > -1) {
              m = eval(m)
            }
            arr1.push(m)
          })
          return arr1.join('～')
        } else {
          // 单个条件
          let index = code.findIndex(b => ask.includes(b))
          if (index > -1) {
            let arr = ask.split(code[index]).filter(b => !!b)
            let num = eval(this.replaceAll(arr[0], symbolItem, value))
            return code[index] + '' + num
          }
        }
      }catch(e){}
    },
    handleTell(tell,symbolItem, value){
      try{
        let num = this.replaceAll(tell, symbolItem, value)
        return num
      }catch(e){}
    },
    replaceAll(str,find,value) {
      if (str === undefined) {
        return str
      }
      return str.replaceAll(find, value);
    },
    save() {
      this.$refs['addObj'].validate((valid) => {
        if (valid) {
          if (!this.sampleList.every(m => m.standardMethodListId)) {
            this.$message.error('请选择检验标准')
            return
          }
          const sampleList = this.HaveJson(this.sampleList)
          sampleList.forEach(item => {
            item.quantity = 1
            item.insProduct = this.productList
          })
          this.getTotal(sampleList)
          let projectNum = this.totalArr.filter(a => a.state == 1).length
          if(projectNum==0){
            this.$confirm('检验项目为空，是否确认提交?', "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(() => {
              this.saveMethod(sampleList)
            }).catch(() => {})
          }else{
            let isRTS = this.totalArr.find(a => a.ask != null && this.symbolList.find(b=>a.ask.includes(b)) && a.state == 1)
            if (isRTS) {
              this.editTable = this.handleData(sampleList,this.containsValue, 0)
              this.editTable.forEach(item => {
                item.value = item.modelNum
              })
              this.bsm3Dia = true;
              return
            }
            this.saveMethod(sampleList)
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    save0(){
      if(this.editTable.every(m=>m.value)){
        this.sampleList.forEach(item => {
          item.quantity = 1
          item.insProduct = this.productList
        })
        let sampleList = this.handleData(this.HaveJson(this.sampleList),this.handleAsk,1)
        sampleList.forEach(a => {
          a.insProduct = a.insProduct.filter(b=>b.state === 1)
        })
        this.saveMethod(sampleList)
      }else{
        this.$message.error('请填写识别符值')
      }
    },
    handleData(sampleList,calBack,type){
      let editTable = []
      sampleList.forEach(item => {
        let obj = {
          sampleCode:item.sampleCode,
          model:item.model,
          symbolList:[],
          sampleId:item.id,
          modelNum:item.modelNum,
        }
        if (item.insProduct && item.insProduct.length > 0) {
          item.insProduct.forEach(a => {
            if (a.state == 1) {
              if(type==0){
                let str = calBack(a.ask)
                str&&obj.symbolList.push({
                  symbolItem:str,
                  inspectionItem:a.inspectionItem,
                })
              }else if(type==1){
                let arr = this.editTable.filter(b => b.sampleId == item.id)
                for (var i=0;i<arr.length;i++){
                  if(a.ask){
                    if(a.ask.includes(arr[i].symbolItem)){
                      let ask = calBack(a.ask, arr[i].symbolItem,arr[i].value)
                      if (ask) {
                        a.ask = ask
                      }
                      let tell = this.handleAsk(a.tell, arr[i].symbolItem,arr[i].value)
                      if (tell) {
                        a.tell = tell
                      }
                    }
                  }else{
                    this.$message.error('要求值为空，需要去标准库维护！')
                  }
                }
              }
            }
          })
        }
        // 光纤带项目
        if (item.bushing && item.bushing.length > 0) {
          item.bushing.forEach(a => {
            if (a.fiber && a.fiber.length > 0) {
              a.fiber.forEach(b => {
                if (b.productList && b.productList.length > 0) {
                  b.productList.forEach(c => {
                    if (c.state == 1) {
                      if(type==0){
                        let str = calBack(c.ask)
                        str&&obj.symbolList.push({
                          symbolItem:str,
                          inspectionItem:c.inspectionItem,
                        })
                      }else if(type==1){
                        let arr = this.editTable.filter(b => b.sampleId == item.id)
                        arr.forEach(f => {
                          if(c.ask.includes(f.symbolItem)){
                            let ask = calBack(c.ask, f.symbolItem,f.value)
                            if (ask) {
                              c.ask = ask
                            }
                            let tell = this.handleTell(c.tell, f.symbolItem,f.value)
                            if (tell) {
                              c.tell = tell
                            }
                          }
                        })
                      }
                    }
                  })
                }
              })
            }
            if (a.fibers && a.fibers.length > 0) {
              a.fibers.forEach(b => {
                if (b.productList && b.productList.length > 0) {
                  b.productList.forEach(c => {
                    if (c.state == 1) {
                      if(type==0){
                        let str = calBack(c.ask)
                        str&&obj.symbolList.push({
                          symbolItem:str,
                          inspectionItem:c.inspectionItem,
                        })
                      }else if(type==1){
                        // let ask = calBack(c.ask, c.rts)
                        // if (ask && c.state == 1) {
                        //   c.ask = csk
                        // }
                        let arr = this.editTable.filter(b => b.sampleId == item.id)
                        arr.forEach(f => {
                          if(c.ask.includes(f.symbolItem)){
                            let ask = calBack(c.ask, f.symbolItem,f.value)
                            if (ask) {
                              c.ask = ask
                            }
                            let tell = this.handleTell(c.tell, f.symbolItem,f.value)
                            if (tell) {
                              c.tell = tell
                            }
                          }
                        })
                      }
                    }
                  })
                }
                if (b.fiber && b.fiber.length > 0) {
                  b.fiber.forEach(c => {
                    if (c.productList && c.productList.length > 0) {
                      c.productList.forEach(d => {
                        if (d.state == 1) {
                          if(type==0){
                            let str = calBack(d.ask)
                            str&&obj.symbolList.push({
                              symbolItem:str,
                              inspectionItem:d.inspectionItem,
                            })
                          }else if(type==1){
                            // let ask = calBack(d.ask, d.rts)
                            // if (ask && d.state == 1) {
                            //   d.ask = ask
                            // }
                            let arr = this.editTable.filter(b => b.sampleId == item.id)
                            arr.forEach(f => {
                              if(d.ask.includes(f.symbolItem)){
                                let ask = calBack(d.ask, f.symbolItem,f.value)
                                if (ask) {
                                  d.ask = ask
                                }
                                let tell = this.handleTell(d.tell, f.symbolItem,f.value)
                                if (tell) {
                                  d.tell = tell
                                }
                              }
                            })
                          }
                        }
                      })
                    }
                  })
                }
              })
            }
          })
        }
        if(type==0){
          editTable.push(obj)
        }
      })
      if(type==0){
        editTable.forEach(a => {
          a.symbolList.forEach(b => {
            let arr = a.symbolList.filter(c => c.symbolItem == b.symbolItem);
            b.inspectionItemList = arr.map(c => c.inspectionItem).join(',')
          })
        })
        editTable.forEach(a => {
          let mySet = new Set();
          a.symbolList = a.symbolList.filter(b =>{
            let num0 = mySet.size;
            mySet.add(b.symbolItem);
            let num1 = mySet.size;
            if(num0!=num1){
              return true;
            }else{
              return false
            }
          })
        })
        let editTableNew = []
        editTable.forEach(a => {
          a.symbolList.forEach(b => {
            let obj = {
              sampleCode:a.sampleCode,
              model:a.model,
              symbolItem:b.symbolItem,
              sampleId:a.sampleId,
              value:null,
              inspectionItemList:b.inspectionItemList,
              modelNum:a.modelNum,
            }
            editTableNew.push(obj)
          })
        })
        return editTableNew
      }else{
        return sampleList
      }
    },
    saveMethod(sampleList){
      this.saveLoad = true
      addRawCopperOrder({
        copperInsOrder: this.addObj,
        sampleList: sampleList.map(a => {
          if (a.insProduct.length > 0) {
            a.insProduct.forEach(c => {
              delete c.id
            })
          }
          if (a.modelNum) {
            const index = a.modelNum.indexOf('×')
            if (index === 0) {
              a.model = a.model + a.modelNum
            } else if (index === -1) {
              a.model = a.model + '-' + a.modelNum
            } else {
              a.model = a.modelNum + a.model
            }
          } else {
            a.model = a.model + ((a.modelNum == null || a.modelNum == '' || a.modelNum == 'null') ? '' : ('-' + a.modelNum))
          }
          a.insProduct = a.insProduct.filter(b=>b.state === 1)
          return a
        })
      }).then(res => {
        this.saveLoad = false
        this.$message.success('已提交')
        this.bsm3Dia = false;
        this.closeOpenPage()
      }).catch(e=>{
        this.saveLoad = false
      })
    },
    getUserNowInfo() {
      getUserNow().then(res => {
        let selects = res.data
        if (selects == null) return
        this.addObj.userId = selects.id
        this.addObj.company = selects.company
        this.addObj.custom = selects.name
        this.addObj.code = selects.code
        this.addObj.phone = selects.phone
        this.addObj.companyId = selects.departId
        // if(this.active==1){
        //   this.selectInsOrderTemplateInfo()
        // }
      })
    },
    getProNum() {
      this.$refs.sampleTable.doLayout()
    },
    searchFilter() {
      this.$refs.tree.filter(this.search)
    },
    nodeOpen(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder-opened')
    },
    nodeClose(data, node, el) {
      $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder')
    },
    handleNodeClick(val, node, el) {
      this.selectTree = ''
      this.models = val.children
      this.getNodeParent(node)
      let flag = false
      if (node.level == 3) {
        if(node.data.children.length>0){
          node.data.children.forEach(a => {
            let key = Object.keys(a)
            if(!key.includes('level')) {
              flag = true
            }
          })

        }
      }
      this.selectTree = this.selectTree.replace(' - ', '')
      if(flag) {
        this.selectTree =  ' -  - ' + this.selectTree
      }
      let data = this.selectTree.split(' - ')
      let data2 = ''
      for (let index = data.length - 1; index >= 0; index--) {
        data2 += " - " + data[index]
      }
      this.selectTree = data2.replace(' - ', '')
    },
    getNodeParent(val) {
      if (val.parent != null) {
        if (val.data.children === null) {
          this.selectTree += ' - ' + val.label + ' - ' + ''
        } else {
          this.selectTree += ' - ' + val.label
        }
        this.getNodeParent(val.parent)
      }
    },
    selectStandardTreeList() {
      this.selectStandardTreeLoading = true
      selectStandardTreeList2().then(res => {
        this.list = res.data
        this.list.forEach(a => {
          this.expandedKeys.push(a.label)
        })
        this.selectStandardTreeLoading=false
      })
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    activeStandardTree() {
      let trees = this.selectTree.split(" - ")
      if (trees.length < 3) {
        this.$message.error('未选择对象')
        return
      }
      this.addObj.factory = trees[0]
      this.addObj.laboratory = trees[1]
      this.addObj.sampleType = trees[2]
      if (trees[3] === undefined || trees[3] === '' || trees[3] === '- ') {
        this.addObj.sample = trees[2]
      } else {
        this.addObj.sample = trees[3]
      }
      this.addObj.model = (trees[4] == undefined ? null : trees[4])
      this.selectStandardTree = false
      this.sampleList = []
      for (var i = 0; i < this.addObj.sampleNum; i++) {
        this.sample.joinName = null
        this.sample.joinModel = null
        this.sample.joinNum = 1
        this.sample.sample = this.addObj.sample
        this.sample.model = this.addObj.model
        this.sample.unit = this.addObj.unit
        this.sample.standardMethodListId = null
        this.sample.insProduct = []
        this.sample.id = this.count
        this.sample.childSampleList = []
        this.sample.insulating = null
        this.sample.sheath = null
        this.sampleList.push(this.HaveJson(this.sample))
        this.count++
      }
      this.$refs.sampleTable.doLayout()
      // this.selectsStandardMethodByFLSSM()
    },
    selectsStandardMethodByFLSSM() {
      this.methodLoad = true
      selectsStandardMethodByFLSSM({tree: this.selectTree}).then(res => {
        this.methodLoad = false
        try {
          if (res.data.standardMethodList.length == 0 && this.selectTree.split('-').length == 5) {
            let arr = this.selectTree.split('-')
            let arr0 = arr.slice(0, arr.length - 1)
            let selectTree = arr0.join('-').substring(0, arr0.join('-').length - 1)
            selectsStandardMethodByFLSSM({tree: selectTree}).then(ress => {
              this.methods = ress.data.standardMethodList
            })
          } else {
            this.methods = res.data.standardMethodList
          }
        } catch (e) {}
      })
    },
    addStandardTree() {
      if (this.selectTree == null) return
      this.sampleList = []
      this.productList = []
      for (var i = 0; i < this.addObj.sampleNum; i++) {
        this.sample.joinName = null
        this.sample.joinModel = null
        this.sample.joinNum = 1
        this.sample.sample = this.addObj.sample
        this.sample.model = this.addObj.model
        this.sample.unit = this.addObj.unit
        this.sample.standardMethodListId = null
        this.sample.insProduct = []
        this.sample.id = parseInt(i+1)
        this.sample.childSampleList = []
        this.sample.insulating = null
        this.sample.sheath = null
        this.sampleList.push(this.HaveJson(this.sample))
        // this.count++
      }
    },
    selectSample(val) {
      this.sampleIds = []
      val.forEach(a => {
        this.sampleIds.push(a.id)
      })
      this.sampleSelectionList = val
    },
    selectProduct(val) {
      this.productListSelected = val
      this.productIds = []
      val.forEach(a => {
        this.productIds.push(a.id)
      })
    },
    rowClick(row, column, event) {
      this.currentMethod = row
      let obj = this.methods.find(a => a.id == this.currentMethod.standardMethodListId)
      if (obj && obj.code == '技术要求') {
        this.isAskOnlyRead = true
      } else {
        this.isAskOnlyRead = false
      }
      this.sampleId = row.id
      if (this.active !== 1) {
        this.sampleIds = []
        this.sampleSelectionList.forEach(ele=>{
          if(ele.id == row.id){
            this.sampleIds.push(row.id)
          }
        })
      }
      // this.productList = row.insProduct
      if (this.productList !== null) {
        setTimeout(() => {
          this.productList.forEach(a => {
            if (a.state == 1) this.toggleSelection(a)
          })
        }, 200)
      }
    },
    toggleSelection(row) {
      this.$refs.productTable.toggleRowSelection(row, true);
    },
    selectOne(selection, row) {
      this.bsm1DiaList = []
      row.state = row.state == 1 ? 0 : 1
      if(row.section === null) {
        row.section = ""
      }
      if (row.sectionCopy === undefined && row.section) {
        if (row.section.indexOf('[') > -1) {
          this.$set(row, 'sectionCopy', row.section)
        }
      }
      if (row.ask.includes('[')) {
        this.$set(row, 'askCopy', row.ask)
      }
      if (row.tell.includes('[')) {
        this.$set(row, 'tellCopy', row.tell)
      }
      // if (row.manHour.includes('[')) {
      //   this.$set(row, 'manHourCopy', row.manHour)
      // }
      // if (row.price.includes('[')) {
      //   this.$set(row, 'priceCopy', row.price)
      // }
      let arr = this.productList.filter(m=>m.state==1&&row.sectionCopy&&row.sectionCopy.includes(m.sectionCopy)&&m.ask&&m.sectionCopy.indexOf('[')==-1)
      if (row.bsm === '1' && row.sectionCopy !== '' && row.sectionCopy !== null && row.sectionCopy !== undefined && row.state === 1&&arr.length==0) {
        if (row.sectionCopy.indexOf('[') > -1) {
          row.bsmRow = this.HaveJson(row)
        }
        row.bsm1 = true
        this.bsm1DiaList.push(row)
        this.bsm1DiaAll = true
      } else if (row.bsm === '1' && row.sectionCopy !== '' && row.sectionCopy !== null && row.state === 0&&arr.length==0) {
        row.bsm1 = false
      }else if(arr.length>0){
        try{
          row.bsmRow = this.HaveJson(row)
          let sectionCopy = arr[0].sectionCopy
          let arr0 = JSON.parse(row.sectionCopy)
          let arr1 = JSON.parse(row.ask)
          // let arr2 = JSON.parse(row.manHour)
          // let arr3 = JSON.parse(row.price)
          let arr4 = JSON.parse(row.tell)
          let index = arr0.indexOf(sectionCopy)
          row.sectionCopy = sectionCopy
          row.ask = arr1[index]
          // row.manHour = arr2[index]
          // row.price = arr3[index]
          row.tell = arr4[index]
        } catch(e) {}
      }
      this.sampleList.map(item => {
        if (this.sampleIds.indexOf(item.id) > -1) {
          item.insProduct.map(m => {
            if (m.id == row.id) {
              m.state = row.state;
            }
            return m;
          })
        }
        return item
      })
      this.changeProductList0()
      this.currentMethod.insProduct = this.productList0
      this.getProNum()
    },
    permute(nums) {
      const result = [];

      function backtrack(temp, nums) {
        if (temp.length === 2) {
          result.push([...temp]);
          return;
        }
        for (let i = 0; i < nums.length; i++) {
          if (temp.includes(nums[i])) continue;
          // 避免重复数字
          if (temp.length > 0 && nums[i] < temp[temp.length - 1]) continue; // 规定顺序，避免重复组合
          temp.push(nums[i]);
          backtrack(temp, nums);
          temp.pop();
        }
      }
      backtrack([], nums);
      return result;
    },
    tableRowClassName({row, rowIndex}) {
      if (row.state === 0) {
        return '';
      }
      return 'warning-row';
    },
    // selectInsOrderTemplateInfo() {
    //   selectInsOrderTemplate({company: this.addObj.company}).then(res => {
    //     this.templates = res.data
    //   })
    // },
    methodChange(val, row) {
      if (val === null || val === '') return
      if (this.sampleList.length > 1) {
        // 有两条样品时，第二条样品信息和第一条保持一致
        this.sampleList.forEach(item => {
          item.standardMethodListId = val
          item.modelNum = row.modelNum
        })
      }
      this.currentMethod = row
      let obj = this.methods.find(a => a.id == this.currentMethod.standardMethodListId)
      if (obj && obj.code == '技术要求') {
        this.isAskOnlyRead = true
      } else {
        this.isAskOnlyRead = false
      }
      this.getProductLoad = true
      let selectTreeList = this.selectTree.split(" - ")
      this.addObj.model&&(selectTreeList[selectTreeList.length - 1] = this.addObj.model)
      selectStandardProductList({
        model: this.addObj.model?this.addObj.model:row.model,
        modelNum: row.modelNum,
        standardMethodListId: val,
        factory: selectTreeList.join(" - "),
      }).then(res => {
        res.data.forEach(a => {
          a.state = 0
        })
        row.insProduct = this.HaveJson(res.data)
        this.getProductLoad = false
        this.productList = row.insProduct
        this.productList0 = JSON.parse(JSON.stringify(this.productList))
        this.$refs.sampleTable.setCurrentRow(row)
        setTimeout(() => {
          this.productList.forEach(a => {
            if (a.state == 1) this.toggleSelection(a)
          })
        }, 200)
      })
      // this.searchTemList()
    },
    // searchTemList () {
    //   this.temperatureList = []
    //   this.$axios.post(this.$api.enums.selectEnumByCategory, {
    //     category: "电力温度循环检验"
    //   }).then(res => {
    //     if (res.data.length > 0) {
    //       this.temperatureEngList = res.data
    //       res.data.forEach(item => {
    //         this.temperatureList.push(item.label)
    //       })
    //     }
    //   })
    // },
    changeModel() {
      this.sampleList.forEach(a => {
        let obj = this.sampleIds.find(b => b == a.id)
        if (obj) {
          a.model = this.model
        }
      })
    },
    changeStandardMethodListId() {
      this.sampleList.forEach(a => {
        let obj = this.sampleIds.find(b => b == a.id)
        if (obj) {
          a.standardMethodListId = this.standardMethodListId
          this.methodChange(this.standardMethodListId, a)
        }
      })
    },
    methodFocus() {
      this.selectsStandardMethodByFLSSM()
    },
    handleChangeModel(e) {
      this.productList = []
      let num = this.selectTree.split('-').length;
      if (num != 5) {
        this.selectTree = this.selectTree + ' - ' + e
      } else {
        let arr = this.selectTree.split('-')
        let arr0 = arr.slice(0, arr.length - 1)
        this.selectTree = arr0.join('-') + '- ' + e
      }
    },
    // 要求值变化时
    requestChange(e, row,type) {
      //this.currentMethod 当前样品id
      this.sampleList.forEach(item=>{
        if(item.id == this.currentMethod.id){
          item.insProduct.forEach(i=>{
            if(i.id == row.id){
              i[type] = e
            }
          })
        }
      })
    },
    selectStandardMethods() {
      selectStandardMethodEnum().then(res => {
        this.methods = res.data
      })
    },
    selectable() {
      if (this.active > 1) {
        return false
      } else {
        return true
      }
    },
    handleAll(e) {
      if (e.length > 0) {
        this.productList.map(m => {
          if(e.find(a=>a.id == m.id)){
            m.state = 1
          }
          return m
        })
      } else {
        this.productList.map(m => {
          m.state = 0
          return m
        })
      }
      this.bsmRow3 = [];
      this.bsm1DiaList = []
      this.productList.forEach(p => {
        if (p.sectionCopy === undefined && p.section) {
          if (p.section.indexOf('[') > -1) {
            this.$set(p, 'sectionCopy', p.section)
          }
        }
        if (p.ask.includes('[')) {
          this.$set(p, 'askCopy', p.ask)
        }
        if (p.tell.includes('[')) {
          this.$set(p, 'tellCopy', p.tell)
        }
        // if (p.manHour.includes('[')) {
        //   this.$set(p, 'manHourCopy', p.manHour)
        // }
        // if (p.price.includes('[')) {
        //   this.$set(p, 'priceCopy', p.price)
        // }
        if (p.bsm === '1' && p.sectionCopy !== '' && p.sectionCopy !== null && p.sectionCopy !== undefined && p.state === 1) {
          if (p.sectionCopy.indexOf('[') > -1) {
            p.bsmRow = this.HaveJson(p)
          }
          p.bsm1 = true
          this.bsm1DiaList.push(p)
          this.bsm1DiaAll = true
        } else if (p.bsm === '1' && p.sectionCopy !== '' && p.sectionCopy !== null && p.state === 0) {
          p.bsm1 = false
        }
      })
      if (e.length > 0) {
        this.sampleList.map(item => {
          if (this.sampleIds.indexOf(item.id) > -1) {
            item.insProduct.map(m => {
              m.state = 1
              return m;
            })
          }
          return item
        })
      } else {
        this.sampleList.map(item => {
          if (this.sampleIds.indexOf(item.id) > -1) {
            item.insProduct.map(m => {
              m.state = 0
              return m;
            })
          }
          return item
        })
      }
      this.changeProductList0()
      this.currentMethod.insProduct = this.productList0
      this.getProNum()
      this.$nextTick(() => {
        this.$refs.productTable.doLayout()
      })
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    // 全选特殊值处理框选择要求值的回调
    upBsmAll (item) {
      const i = this.bsm1DiaList.findIndex(obj => obj.id === item.id)
      if (i > -1) {
        // 找到相应的检验项赋值
        this.bsm1DiaList[i].bsm1Val = item.bsm1Val
        let sections = this.bsm1DiaList[i].bsmRow.sectionCopy && JSON.parse(this.bsm1DiaList[i].bsmRow.sectionCopy);
        let asks = this.bsm1DiaList[i].bsmRow.askCopy && JSON.parse(this.bsm1DiaList[i].bsmRow.askCopy);
        let tells = this.bsm1DiaList[i].bsmRow.tellCopy && JSON.parse(this.bsm1DiaList[i].bsmRow.tellCopy);
        // let manHours = this.bsm1DiaList[i].bsmRow.manHourCopy && JSON.parse(this.bsm1DiaList[i].bsmRow.manHourCopy);
        // let prices = this.bsm1DiaList[i].bsmRow.priceCopy && JSON.parse(this.bsm1DiaList[i].bsmRow.priceCopy);
        for (var a in sections) {
          if (this.bsm1DiaList[i].bsm1Val === sections[a]) {
            this.productList.forEach(p => {
              // 将选择好的要求值赋值到列表里
              if (p.id === this.bsm1DiaList[i].bsmRow.id) {
                p.section = sections[a]
                p.ask = asks[a]
                p.tell = tells[a]
                // p.manHour = manHours[a]
                // p.price = prices[a]
              }
            })
            break
          }
        }
      }
      this.changeProductList0()
      this.currentMethod.insProduct = this.productList0
    },
    save1 () {
      if (this.bsm1DiaList.length > 0) {
        this.bsm1DiaList.forEach(item => {
          if (!item.bsm1Val) {
            throw this.$message.error('特殊项目必须处理')
          }
        })
      }
      this.bsm1DiaAll = false
    },
    beforeClose(done) {
      if (this.bsm1DiaList.length > 0) {
        this.bsm1DiaList.forEach(item => {
          if (!item.bsm1Val) {
            throw this.$message.error('特殊项目必须处理')
          }
        })
      }
      done()
    },
    beforeClose0(done) {
      if (!this.bsm3Val) {
        this.$message.error('RST必须填写')
        return
      } else {
        if (Array.isArray(this.bsmRow3)) {
          this.bsmRow3.forEach(item => {
            item.rts = this.bsm3Val
          })
        } else {
          this.bsmRow3.rts = this.bsm3Val
        }
      }
      done()
    },
    getTotal() {
      this.totalArr = []
      // this.total = 0;
      this.sampleList.forEach(item => {
        if (item.insProduct && item.insProduct.length > 0) {
          item.insProduct.forEach(a => {
            this.totalArr.push(a)
          })
        }
        if (item.bushing && item.bushing.length > 0) {
          item.bushing.forEach(a => {
            if (a.fiber && a.fiber.length > 0) {
              a.fiber.forEach(b => {
                if (b.productList && b.productList.length > 0) {
                  b.productList.forEach(c => {
                    this.totalArr.push(c)
                  })
                }
              })
            }
            if (a.fibers && a.fibers.length > 0) {
              a.fibers.forEach(b => {
                if (b.productList && b.productList.length > 0) {
                  b.productList.forEach(c => {
                    this.totalArr.push(c)
                  })
                }
                if (b.fiber && b.fiber.length > 0) {
                  b.fiber.forEach(c => {
                    if (c.productList && c.productList.length > 0) {
                      c.productList.forEach(d => {
                        this.totalArr.push(d)
                      })
                    }
                  })
                }
              })
            }
          })
        }
      })
      let mySet = new Set();
      let arr0 = this.totalArr.filter(item => {
        if (item.state == 1) {
          let num1 = mySet.size
          if (item.manHourGroup === '' || !item.manHourGroup) {
            return true
          } else {
            mySet.add(item.manHourGroup)
            let num2 = mySet.size
            if (num2 > num1) {
              return true
            } else {
              return false
            }
          }
        }
      })
      // arr0.forEach(item => {
      //   this.total += Number(item.price)
      // })

    },
    goBack () {
      if (this.active == 1) {
        this.$router.go(-1)
      } else {
        this.closeOpenPage()
      }
    },
    closeOpenPage() {
      this.$router.go(-1)
      this.$tab.closeOpenPage()
    },
  }
}
</script>
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
>>>.warning-row {
  color: #1890FF;
}
.node_i {
  color: orange;
  font-size: 18px;
}
</style>
