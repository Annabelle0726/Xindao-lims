<template>
  <el-dialog :close-on-press-escape="false" :visible.sync="isShow" custom-class="unPassCheck" title="不合格复测" width="90%"
    @close="$emit('closeUnPassCheckDialog')">
    <div v-loading="loading" class="inspection">
      <!--      <el-row class="title">-->
      <!--        <el-col :span="24" style="text-align: right;">-->
      <!--          <el-button size="small" type="primary" @click="addVerifyDia = true" v-if="state==1">提交</el-button>-->
      <!--        </el-col>-->
      <!--      </el-row>-->
      <div class="center">
        <div class="search" style="text-align: left;display: flex;align-items: center;justify-content: space-between;">
          <div style="display: flex;align-items: center;">
            <span v-if="tableList.length > 0">检验模板：</span>
            <el-radio-group v-model="currentTable" size="small">
              <el-radio-button v-for="(item, index) in tableLists" :key="index" :label="item.templateId" size="small">{{
                item.templateName }}</el-radio-button>
            </el-radio-group>
          </div>
          <div style="display: flex;align-items: center;">
            <span>&nbsp;&nbsp;复测次数：</span>
            <el-select v-model="retestTag" placeholder="请选择" size="small"
              @change="m => handleChangeCableTag(currentSample.id, 4, 'cableTag', m)">
              <el-option v-for="item in unPassNumList" :key="item.retestTag" :label="item.retestTag"
                :value="item.retestTag">
              </el-option>
            </el-select>
          </div>
        </div>
        <!-- 常规检验原始记录 -->
        <div id="tableBox" v-loading="tableLoading" class="center-box">
          <table v-for="(item, index) in tableList" :key="index + currentTable + currentSample.id" border="1"
            cellpadding="10" class="tables">
            <tbody>
              <tr v-for="(m, i) in item.arr" :key="i">
                <td v-for="(n, j) in m" v-if="n.v.mc == undefined || Object.keys(n.v.mc).length === 4"
                  :id='item.templateId + "-" + n.i + "-" + n.r + "-" + n.c' :key="j"
                  :colspan="n.v.mc && n.v.mc.cs ? n.v.mc.cs : 1" :rowspan="n.v.mc && n.v.mc.rs ? n.v.mc.rs : 1"
                  :style="`background:${n.v.bg ? n.v.bg : ''};color:${n.v.fc};font-size:${n.v.fs}px;width:${handleWidth(n)}px !important;height:${item.style.rowlen[n.r]}px;font-wight:${n.v.bl ? 'bold' : ''};`">
                  <div :class="`content-h-${n.v.ht} content-v-${n.v.vt}`"
                    :style="`width:${handleWidth(n)}px !important;min-height:${item.style.rowlen[n.r]}px;`"
                    class="content">
                    <template
                      v-if="n.v.ps != undefined && typeof n.v.ps.value === 'string' && n.v.ps.value.includes('检验值') && state == 1">
                      <el-input v-if="getInspectionValueType(n.i) == 1"
                        :key="'abc-' + '000' + index + '000' + i + '000' + j" v-model="n.v.v"
                        :disabled="(getInspectionItemType(n.i) == 1 && !dataAcquisitionEidtAble) || (n.u != userId && n.u != undefined && n.u != '')"
                        class="table_input"
                        @change="m => changeInput(m, `${item.templateId}-${n.r}-${n.c}-${n.i}`, n, 'getDataType')"
                        @input="handleInput(n)" @mousewheel.native.prevent
                        @keydown.enter="changeInput('', `${item.templateId}-${n.r}-${n.c}-${n.i}`, n, 'getDataType')">
                      </el-input>
                      <el-input v-else-if="getInspectionValueType(n.i) == 2" v-model="n.v.v"
                        :disabled="getInspectionItemType(n.i) == 1 || (n.u != userId && n.u != undefined && n.u != '')"
                        class="table_input" type="textarea"
                        @change="m => changeInput(m, `${item.templateId}-${n.r}-${n.c}-${n.i}`, n, 'getDataType')" />
                      <!--                    <el-select v-else-if="getInspectionValueType(n.i) == 5" v-model="n.v.v" :disabled="state>1||getInspectionItemType(n.i) == 1 || (n.u != userId && n.u != undefined && n.u != '')"-->
                      <!--                               class="table_input" @change="m=>changeInput(m,`${item.templateId}-${n.r}-${n.c}-${n.i}`,n,'getDataType')"-->
                      <!--                               @visible-change="e=>getDic(e,n.i)">-->
                      <!--                      <el-option v-for="(e, i) in enumList" :key="i" :label="e.label" :value="e.value"></el-option>-->
                      <!--                    </el-select>-->
                      <span v-else-if="getInspectionValueType(n.i) == 4"
                        :style="`font-family:${n.v.ff} !important;`">/</span>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '结论'">
                      <el-select
                        v-if="(getInspectionValueType(n.i) == 2 || getInspectionValueType(n.i) == 5) && state == 1 && PROJECT == '装备电缆'"
                        v-model="n.v.v" class="table_input"
                        @change="m => changeInput(m, `${item.templateId}-${n.r}-${n.c}-${n.i}`, n, 'getDataType')">
                        <el-option :value="1" label="合格"></el-option>
                        <el-option :value="0" label="不合格"></el-option>
                        <el-option :value="3" label="不判定"></el-option>
                        <el-option :value="2" label="待定"></el-option>
                      </el-select>
                      <template v-if="state > 1">
                        <span v-if="n.v.v === 1" :style="`font-family:${n.v.ff} !important;color: green;`">合格</span>
                        <span v-else-if="n.v.v === 0" :style="`font-family:${n.v.ff} !important;color: red;`">不合格</span>
                        <span v-else-if="n.v.v === 3"
                          :style="`font-family:${n.v.ff} !important;color: #3A7BFA;`">不判定</span>
                        <span v-else :style="`font-family:${n.v.ff} !important;`">待定</span>
                      </template>
                      <template v-if="getInspectionValueType(n.i) != 2 && state == 1">
                        <span v-if="n.v.v === 1" :style="`font-family:${n.v.ff} !important;color: green;`">合格</span>
                        <span v-else-if="n.v.v === 0" :style="`font-family:${n.v.ff} !important;color: red;`">不合格</span>
                        <span v-else-if="n.v.v === 3"
                          :style="`font-family:${n.v.ff} !important;color: #3A7BFA;`">不判定</span>
                        <span v-else :style="`font-family:${n.v.ff} !important;`">待定</span>
                      </template>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '设备编码' && state == 1">
                      <span>{{ n.v.v }}</span>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '设备名称'">
                      <el-select v-model="n.v.v" :disabled="state > 1" class="table_input" filterable multiple
                        placeholder="设备" remote @change="(val) => changeEquip(val, n)"
                        @visible-change="e => getEquipOptions(e, n.i)">
                        <el-option v-for="item in equipOptions" :key="item.value" :label="item.label"
                          :value="item.value">
                          {{ item.label + '--' + item.value }}
                        </el-option>
                      </el-select>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '要求值' && state == 1">
                      <span :style="`font-family:${n.v.ff} !important;`">{{ getTell(n.i) }}</span>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '计算值' && state == 1"><span
                        :style="`font-family:${n.v.ff} !important;`">{{ toFixed(n.v.v, n.v.ct) }}</span></template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '最终值' && state == 1">
                      <span :style="`font-family:${n.v.ff} !important;`">{{ toFixed(n.v.v, n.v.ct) }}</span>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '样品编号'">
                      <div :title="currentSample.sampleCode"
                        style="display: flex;flex-wrap: nowrap;align-items: center;width: 100%">
                        <i v-if="!currentFiberOpticTape && !currentFiberOptic" class="el-icon-caret-left table_caret"
                          style="width: 16px;" @click="caretSample(-1)"></i>
                        <div
                          :style="`font-family:${n.v.ff} !important;overflow: hidden;white-space: nowrap;width: calc(100% - 32px);`">
                          {{ currentSample.sampleCode }}</div>
                        <i v-if="!currentFiberOpticTape && !currentFiberOptic" class="el-icon-caret-right table_caret"
                          style="width: 16px;" @click="caretSample(1)"></i>
                      </div>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '样品型号'">
                      <div v-if="currentSample.model !== undefined && currentSample.model !== null"
                        :style="`font-family:${n.v.ff} !important;`">{{ currentSample.model }}</div>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '套管'">
                      <div style="display: flex;flex-wrap: nowrap;align-items: center;">
                        <div :style="`font-family:${n.v.ff} !important;`">{{ currentBushing ? currentBushing.color : ''
                          }}
                        </div>
                      </div>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '光纤带'">
                      <div style="display: flex;flex-wrap: nowrap;align-items: center;">
                        <i v-if="currentFiberOpticTape" class="el-icon-caret-left table_caret" @click="caretTape(-1)
                          "></i>
                        <div :style="`font-family:${n.v.ff} !important;`">
                          {{ currentFiberOpticTape ? currentFiberOpticTape.code : '' }}</div>
                        <i v-if="currentFiberOpticTape" class="el-icon-caret-right table_caret"
                          @click="caretTape(1)"></i>
                      </div>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '光纤'">
                      <div style="display: flex;flex-wrap: nowrap;align-items: center;">
                        <i v-if="currentFiberOptic" class="el-icon-caret-left table_caret" @click="caretOptic(-1)"></i>
                        <div :style="`font-family:${n.v.ff} !important;`">
                          {{ currentFiberOptic ? currentFiberOptic.color : '' }}
                        </div>
                        <i v-if="currentFiberOptic" class="el-icon-caret-right table_caret" @click="caretOptic(1)"></i>
                      </div>
                    </template>
                    <span v-else :style="`font-family:${n.v.ff} !important;`" v-html="getValue(n.v)"></span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <el-dialog :visible.sync="reviewDia" title="检验复核" width="500px">
        <div v-if="reviewDia" class="body" style="display: flex;padding: 10px;">
          <div class="search_label" style="width: 150px;"><span class="required-span">* </span>不通过的理由：</div>
          <div class="search_input" style="width: 100%;">
            <el-input v-model="noReason" :autosize="{ minRows: 4 }" clearable size="small" type="textarea"></el-input>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="reviewDia = false">取 消</el-button>
          <el-button :loading="reviewLoading" type="primary" @click="handleReviewDia">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="addVerifyDia" title="指定复核人员" width="400px">
        <div class="body" style="display: flex;padding: 10px;align-items: center;">
          <div class="search_label" style="width: 150px;"><span class="required-span">*</span>复核人</div>
          <div class="search_input" style="width: 100%;">
            <el-select v-model="verifyUser" clearable filterable placeholder="请选择" size="small" style="width: 100%;">
              <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="addVerifyDia = false">取 消</el-button>
          <el-button :loading="submitLoading" type="primary" @click="submit()">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false"
        :visible.sync="dataGetDia" custom-class="custom-dialog" title="数据采集">
        <div>
          <table border="1" cellpadding="10" class="thermal-table">
            <tr>
              <td style="width: 120px;">检验项</td>
              <td style="width: 120px;">检验子项</td>
              <td>数采数据</td>
            </tr>
            <template v-for="(item, index) in getData">
              <tr>
                <td :rowspan="item.child.length">{{ item.faName }}</td>
                <td>{{ item.child[0].name }}</td>
                <td style="text-align: left;">
                  <el-checkbox-group v-model="getDataIndex" :max="item.child[0].maxNum">
                    <el-checkbox v-for="(n, j) in item.child[0].arr" :key="j" :label="j">{{ n }}</el-checkbox>
                  </el-checkbox-group>
                </td>
              </tr>
              <tr v-for="(m, i) in item.child" v-show="i > 0" :key="i + 'bbbbbbbbbbbbbb'">
                <td>{{ m.name }}</td>
                <td style="text-align: left;">
                  <el-checkbox-group v-model="getDataIndex" :max="m.maxNum">
                    <el-checkbox v-for="(n, j) in m.arr" :key="j" :label="j">{{ n }}</el-checkbox>
                  </el-checkbox-group>
                </td>
              </tr>
            </template>
          </table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button :loading="getDataIndexLoading" type="primary" @click="submitDataGet()">确 定</el-button>
        </span>
      </el-dialog>
      <UnPassDialog v-if="unPassDialog" ref="unPassDialog" :orderId="orderId" :unPassDialog="unPassDialog"
        @resetForm="resetForm"></UnPassDialog>
      <el-dialog :close-on-click-modal="false" :visible.sync="addCheck" title="指定报告审核人员" width="400px"
        @close="closeAddVerifyDia">
        <div class="body" style="display: flex;padding: 10px;align-items: center;">
          <div class="search_label" style="width: 150px;"><span class="required-span">*</span>审核人：</div>
          <div class="search_input" style="width: 100%;">
            <el-select v-model="checkUser" clearable filterable placeholder="请选择" size="small" style="width: 100%;">
              <el-option v-for="(item, i) in personList" :key="i" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="closeAddVerifyDia">取 消</el-button>
          <el-button :loading="reviewLoading" type="primary" @click="upInsReview(1)">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </el-dialog>
</template>

<script>
import excelFunction from '@/utils/excelFountion'
import UnPassDialog from "@/views/business/unpass/components/unPassDialog.vue";
import AddUnPass from "@/views/business/unpass/components/addUnPass.vue";
import {
  checkSubmitPlan,
  doInsOrder, downFile,
  getInsProductUnqualifiedRetest,
  search, selectUserCondition,
  submitPlan,
  verifyPlan
} from "@/api/business/inspectionTask";
import { getUserNow, saveUnqualifiedContext } from "@/api/business/rawMaterialOrder";
import InspectionWorker from '@/workers/InspectionWorker.worker';
import DataWorker from '@/workers/DataWorker.worker';
export default {
  props: ['sonLaboratory', 'orderId', 'state', 'inspectorList', 'typeSource', 'unPassCheck', 'rawMaterialTag','cableTag', 'repetitionTag'],
  components: {
    AddUnPass,
    UnPassDialog
  },
  data() {
    return {
      isShow: this.unPassCheck,
      sagData: [],
      sagForm: {
        sampleCode: null,
        model: null,
        inspection: null,
        methodName: null,
        tensileForce: null,
        spanLength: null,
        load: null
      },
      dataGetDia: false,
      wareTableDataLoading: false,
      fileAdd: false,
      submitLoading: false,
      searchForm: {
        sampleName: null,
        state: null
      },
      id: 0,
      componentData0: {
        entity: {
          insOrderId: ''
        },
        isIndex: true,
        showSelect: false,
        select: false,
        sort: false,
        init: false,
        do: [
          {
            id: 'handleDown',
            font: '下载',
            type: 'text',
            method: 'handleDown'
          }, {
            id: 'delete',
            font: '删除',
            type: 'text',
            method: 'doDiy',
            disabled: (row, index) => {
              return this.state != 1
            }
          }
        ],
        isPage: false,
        linkEvent: {},
        tagField: {
          type: {
            select: [
              {
                value: 1,
                label: '图片'
              },
              {
                value: 2,
                label: '文件'
              }
            ]
          }
        },
        currentId: '',
        selectField: {},
        requiredAdd: [],
        requiredUp: []
      },
      changeType: null,
      getReportModelLoading: false,
      insOrder: {},
      sampleProduct: [],
      typeList: [],
      urgentList: [],
      currentSample: {}, //当前样品信息
      tableList: [],
      loading: false,
      ps: {},
      param: {},
      currentKey0: 1,
      currentKey1: 1,
      currentKey2: 1,
      comparisonList: [],
      excelMethodList: [],
      equipOptions: [],
      userId: 0,
      reviewLoading: false,
      reviewDia: false,
      noReason: '',
      tableWidth: 1000,
      currentTable: null,
      tableLists: [],
      widthList: [],
      addVerifyDia: false,
      verifyUser: null,
      personList: [],
      enumList: [],
      tableLoading: false,
      upLoading: false,
      temptList: null,
      currentTab: null,
      wareForm: {
        inspectionItem: 1,
        inspectionItemSubclass: '20(常温)',
      },
      wareForm0: {},
      numOptions: [],
      temperatureOptions: [],
      wareTableData: [],
      otherForm: {
        humidity: null,
        temperature: null,
      },
      equipForm: {
        value0: null,
        code0: null,
        value1: null,
        code1: null,
      },
      result: null,
      worker: null,
      worker0: null,
      wareLength: [],
      dataAcquisitionInfo: {},
      dataAcquisitionInfoNew: {},
      dataAcquisitionEidtAble: false,
      isGet: false,
      dataAcquisitionLoading: false,
      collected: false,
      // 热循环---开始
      thermalCyclingInfo: {
        max: 0,
        inspectionItem: 1,
        arr: [],
        length: 1,
        inspectionItemClass: null,
      },
      thermalCyclingLoading: false,
      temDataAcquisition: false,
      getData: [
        {
          faName: '波长附加衰减',
          child: [
            {
              name: '1285nm~1330nm',
              arr: [12, 13, 14, 15],
            },
            {
              name: '1525nm~1575nm',
              arr: [12, 13, 14, 15],
            },
          ]
        },
        {
          faName: '截至波长',
          child: [
            {
              name: '截至波长',
              arr: [12, 13, 14, 15],
            }
          ]
        }
      ],
      getDataIndex: [],
      getDataIndexLoading: false,
      getDataTypeId: '',
      getDataType: null,
      unPassDialog: false, // 不合格处理弹框
      retestTag: '1', // 复测次数
      addCheck: false, // 指定审核人员弹框
      checkUser: '',
      type: '',
      unPassNumList: [
        { retestTag: '1' },
        { retestTag: '2' },
      ],
    }
  },
  // 用于上传文件的信息
  computed: {
    headers() {
      return {
        'token': sessionStorage.getItem('token')
      }
    },
    action() {
      return this.javaApi + "/insOrderPlan/uploadFile";
    }
  },
  created() {
    this.id = this.orderId;
    this.getUserInfo()
  },
  mounted() {
    this.getTypeDicts() // 获取紧急程度下拉框选项
    this.getInsStateDicts()
    this.getComparisonList()
    this.getAuthorizedPerson()
    this.startWorker()
  },
  watch: {
    // 监听任务id，获取任务信息
    id(val) {
      this.loading = true
      doInsOrder({
        id: val,
        laboratory: this.sonLaboratory
      }).then(async res => {
        this.insOrder = res.data.insOrder;
        this.componentData0.entity.insOrderId = val;
        this.urgentList.forEach(m => {
          if (m.value == this.insOrder.type) {
            this.insOrder.typeName = m.label
          }
        })
        if (!res.data.sampleProduct || res.data.sampleProduct.length == 0) {
          return this.$message.error('该任务没有样品信息')
        }
        // 赋值当前样品列表
        this.sampleProduct = res.data.sampleProduct
        this.currentSample = this.HaveJson(this.sampleProduct[0])
        let insProduct = this.HaveJson(this.currentSample.insProduct)
        // 温度、湿度赋值
        this.otherForm = {
          temperature: this.insOrder.temperature ? this.insOrder.temperature : null,
          humidity: this.insOrder.humidity ? this.insOrder.humidity : null,
        }
        if (this.typeSource == '1') {
          this.retestTag = '1'
        }
        this.getEquipOptions(1)
        // 获取当前样品的检验项
        let list = await this.getCurrentProduct(this.currentSample.id, 0)
        if (list === undefined) {
          this.$message.warning('暂无不合格复测数据')
          this.$emit('closeUnPassCheckDialog')
          return
        }
        this.currentSample.insProduct = this.HaveJson(list)
        // 初始化传递到后端的参数
        this.param = {}
        this.changeType = 0;
        this.currentSample.insProduct.forEach(a => {
          // 是否为成品电缆下的松套管项目，不是则执行初始化
          if (this.handleCasing(a.inspectionItem)) {
            this.param[a.id] = {
              insValue: [],
              comValue: [],
              resValue: null,
              equipValue: [],
              equipName: [],
              insResult: null
            }
          }
        })
        // await this.determineWhetherToCollectData()//是否需要数采
        if (this.currentSample.index == undefined) this.currentSample['index'] = 1
        let bushing = this.currentSample.bushing
        this.getTableLists();//处理模板列表信息
        this.loading = false

      })
    },
    // 监听当前模板变化
    currentTable(val1, val0) {
      if (val0 != null && val1 != val0) {
        if (this.changeType && this.changeType > 0) {
          // 如果是光纤、光纤带，则不执行下面操作
          return
        }
        this.tableLists.forEach(async (m, i) => {
          if (m.templateId == val1) {
            let list = await this.getCurrentProduct(this.currentSample.id, 0)
            this.currentSample.insProduct = this.HaveJson(list)//赋值当前样品的检验项
            this.param = {}//初始化传到后端的参数
            this.currentSample.insProduct.forEach((a, j) => {
              if (this.handleCasing(a.inspectionItem)) {
                this.param[a.id] = {
                  insValue: [],
                  comValue: [],
                  resValue: null,
                  equipValue: [],
                  equipName: [],
                  insResult: null
                }
              }
            })
            // 去重模板，返回有几个模板
            const mySet1 = new Set();
            this.tableLists = this.currentSample.insProduct.filter(m => {
              let num0 = mySet1.size;
              if (m.templateId != null && m.template != null) {
                try {
                  mySet1.add(JSON.stringify({
                    template: m.template,
                    templateId: m.templateId
                  }))
                } catch (error) {
                  console.log(222, error);
                }
              }
              let num1 = mySet1.size;
              if (num1 > num0) {
                return m
              }
            });
            if (this.tableLists && this.tableLists.length > 0) {
              this.tableList = null;
              this.tableList = this.tableLists.filter(m => m.templateId == val1)
              // 对模板进行处理
              this.handleTableData()
            }
          }
        })
      }
    },
    // 特殊检验项--监听设备信息改变
    equipForm: {
      deep: true,
      handler(val) {
        if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录' || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验')) && this.equipOptions && this.equipOptions.length > 0) {
          // 初始化设备信息
          this.param[this.currentSample.insProduct[0].id].equipValue = []
          this.param[this.currentSample.insProduct[0].id].equipName = []
          if (this.equipForm.code0) {
            // 赋值第一个设备的信息
            this.equipForm.value0 = this.equipOptions.find(m => m.value == this.equipForm.code0).label
            this.param[this.currentSample.insProduct[0].id].equipValue.push({
              i: this.currentSample.insProduct[0].id,
              v: {
                v: this.equipForm.code0
              }
            })
            this.param[this.currentSample.insProduct[0].id].equipName.push({
              i: this.currentSample.insProduct[0].id,
              v: {
                v: this.equipForm.value0
              }
            })
          }
          if (this.equipForm.code1) {
            // 赋值第二个设备的信息
            this.equipForm.value1 = this.equipOptions.find(m => m.value == this.equipForm.code1).label
            this.param[this.currentSample.insProduct[0].id].equipValue.push({
              i: this.currentSample.insProduct[0].id,
              v: {
                v: this.equipForm.code1
              }
            })
            this.param[this.currentSample.insProduct[0].id].equipName.push({
              i: this.currentSample.insProduct[0].id,
              v: {
                v: this.equipForm.value1
              }
            })
          }
          // 保存数据
          this.saveInsContext()
        }
      }
    },
  },
  beforeDestroy() {
    // 在组件销毁前确保停止 Worker，避免内存泄漏
    this.stopWorker();
  },
  methods: {
    closeAddVerifyDia() {
      this.addCheck = false
      this.checkUser = ''
    },
    // 关闭不合格处理弹框
    resetForm() {
      this.$refs.unPassDialog.$refs['unPassForm'].resetFields();
      this.unPassDialog = false
    },
    handleDataAcquisition(data) {
      // 是否可以编辑数采数据
      if (this.dataAcquisitionEidtAble) {
        this.getDataType = 1;
      } else {
        this.getDataType = 2;
      }
      this.dataAcquisitionInfo = {}
      this.getData = []
      for (let i in data) {
        let obj = {
          faName: i,
          child: []
        }
        // 循环数采数据
        for (let j in data[i]) {
          // 拼接字符串  检验项+检验子项
          let str0 = ''
          if (i == j) {
            str0 = i + ','
          } else {
            str0 = i + ',' + j
          }
          if (j != 'frequency' && data[i][j] && (!data[i][j].result || typeof data[i][j].result == 'string')) {
            // 处理数采信息格式
            this.dataAcquisitionInfo[str0] = {
              value: data[i][j].result,
              frequency: data[i].frequency
            }
            let list = this.tableList[0].arr
            // 循环页面列表信息，判断数采数据对应页面列表信息的检验项是哪个，并给当前检验项绑定设备信息
            list.forEach((item, index) => {
              let num0 = 0;
              let str = ''
              item.forEach(m => {
                if (m.v.ps && (m.v.ps.value == '检验子项' || m.v.ps.value == '检验项')) {
                  if (m.v.ps && m.v.ps.value == '检验项') {
                    if (num0 == 0) {
                      str = m.v.v + ','
                      num0++
                    }
                  }
                  if (m.v.ps && m.v.ps.value == '检验子项') {
                    if (num0 == 1) {
                      str = str + m.v.v
                    }
                  }
                }
                // 绑定设备信息
                if (data[i][j].equipName && data[i][j].equipValue && m.v && m.v.ps && m.v.ps.value == '设备名称' && str0 == str) {
                  if (!m.v.v) {
                    this.changeEquip(data[i][j].equipValue, m, data[i][j].equipName)
                  }
                }
              })
            })
          } else if (j != 'frequency' && data[i][j] && Array.isArray(data[i][j].result)) {
            // 如果返回的数采数据是数组，则处理数组
            // 以下逻辑为获取每个检验项可输入的检验值的最大个数
            let str0 = ''
            if (i == j) {
              str0 = i + ','
            } else {
              str0 = i + ',' + j
            }
            let list = this.tableList[0].arr
            let maxNum = 0
            list.forEach((item, index) => {
              let num0 = 0;
              let str = ''
              item.forEach(m => {
                if (m.v.ps && (m.v.ps.value == '检验子项' || m.v.ps.value == '检验项')) {
                  if (m.v.ps && m.v.ps.value == '检验项') {
                    if (num0 == 0) {
                      str = m.v.v + ','
                      num0++
                    }
                  }
                  if (m.v.ps && m.v.ps.value == '检验子项') {
                    if (num0 == 1) {
                      str = str + m.v.v
                    }
                  }
                  let num = 0;
                  list[index].forEach(n => {
                    if (n.v.ps && n.v.ps.value && typeof n.v.ps.value == 'string' && n.v.ps.value.includes('检验值')) {
                      num++
                    }
                  })
                  if (str0 == str) {
                    maxNum = num
                  }
                }
                // 绑定设备
                if (data[i][j].equipName && data[i][j].equipValue && m.v && m.v.ps && m.v.ps.value == '设备名称' && str0 == str) {
                  if (!m.v.v) {
                    this.changeEquip(data[i][j].equipValue, m, data[i][j].equipName)
                  }
                }
              })
            })
            // 获取到最大检验值输入个数后重组数据
            let obj0 = {
              name: j,
              arr: data[i][j].result,
              maxNum: maxNum,
              value: []
            }
            // 如果数采返回的数组长度大于最大输入个数，则将数采数据在弹框中展示，用户选择需要手动选择数采的信息
            if (data[i][j].result && Array.isArray(data[i][j].result) && data[i][j].result.length > maxNum) {
              obj.child.push(obj0)
            } else {
              this.dataAcquisitionInfo[str0] = {
                value: data[i][j].result
              }
            }
          }
        }
        if (obj.child.length > 0) {
          this.getData.push(obj)
        }
      }
      // 如果存在数采返回的数组长度大于最大输入个数，则弹出弹框选择
      if (this.getData.length > 0) {
        this.dataGetDia = true
        this.getDataIndex = []
      } else {
        // 如果都不存在，则，进入处理数采线程里去处理数据
        try {
          // 向 Worker 发送消息，开始处理逻辑
          this.getDataIndexLoading = false
          this.dataGetDia = false
          this.getDataTypeId = ''
          this.worker0.postMessage(JSON.stringify({
            dataAcquisitionInfo: this.dataAcquisitionInfo,
            list: this.tableList[0].arr
          }));
        } catch (error) {
          console.log(1111, error);
        }
      }
      // 监听 Worker 返回的结果
      this.worker0.onmessage = (event) => {
        let result = JSON.parse(event.data);
        if (result.method == 'changeInput') {
          // 采集后的数据，需要进行计算的线程进行计算
          let { list, n } = result.value
          this.$set(this.tableList[0], 'arr', list)
          this.changeInput('', `${this.currentSample.insProduct[0].templateId}-${n.r}-${n.c}-${n.i}`, n)
        } else if (result.getDataTypeId) {
          // 获取到数采最后一项，检验项的ID
          this.getDataTypeId = result.getDataTypeId
        }
      };
    },
    // 如果存在数采返回的数组长度大于最大输入个数，则弹出弹框选择，这里是弹框的提交
    submitDataGet() {
      if (this.getDataIndex.length == 0) {
        this.$message.error('请选择需要采集的数据')
        return
      }
      this.getDataIndex.sort((a, b) => a - b);
      for (let i = 0; i < this.getData.length; i++) {
        for (let j = 0; j < this.getData[i].child.length; j++) {
          // 对用户选择的数采信息进行处理，赋值
          let arr = []
          for (let k = 0; k < this.getDataIndex.length; k++) {
            arr.push(this.dataAcquisitionInfoNew[this.getData[i].faName][this.getData[i].child[j].name].result[this.getDataIndex[k]])
          }
          this.dataAcquisitionInfoNew[this.getData[i].faName][this.getData[i].child[j].name].result = arr
        }
      }
      this.getDataIndexLoading = true
      // 赋值完成后需要再次进入处理数采线程里去处理数据
      this.handleDataAcquisition(this.dataAcquisitionInfoNew)
    },
    // 多线程
    startWorker() {
      if (this.worker) {
        this.stopWorker(); // 确保之前的 Worker 已停止
      }
      // 创建 Worker 实例
      this.worker = new InspectionWorker();
      if (this.worker0) {
        this.stopWorker(); // 确保之前的 Worker 已停止
      }
      // 创建 Worker 实例
      this.worker0 = new DataWorker();
    },
    // 停止多线程
    stopWorker() {
      if (this.worker) {
        this.worker.terminate();
        this.worker = null;
      }
      if (this.worker0) {
        this.worker0.terminate();
        this.worker0 = null;
      }
    },
    // 根据类型、任务id、实验室来获取样品的检验项信息
    async getCurrentProduct(id, type, cableTag) {
      this.tableLoading = true;
      type = this.typeSource == '1' ? 5 : type
      this.type = type
      const params = {
        id: id,
        type: type,
        laboratory: this.sonLaboratory,
        retestTag: this.retestTag,
        rawMaterialTag: this.rawMaterialTag,
        repetitionTag: this.repetitionTag,
        cableTag: this.cableTag,
      }
      let res = await getInsProductUnqualifiedRetest(params)
      console.log('res---', res)
      if (res.code === 200 && res.data.length > 0) {
        this.tableLoading = false;
        this.scrollInit()
        return res.data
      }
    },
    // 松套管的判断\如果changeType不等于3那么页面不展示松套管检验项
    handleCasing(inspectionItem) {
      if (this.changeType != 3) {
        if (inspectionItem.includes('松套管')) {
          return false
        } else {
          return true
        }
      } else {
        return true
      }
    },
    async handleChangeCableTag(m, type, num, m2) {
      let cableTag = ''
      if (num === 'cableTag') {
        cableTag = m2
      }
      if (!m2) {
        type = 0
      }
      this.changeType = type
      if (m) {
        let list = await this.getCurrentProduct(m, type, cableTag)
        if (list.length > 0) {
          this.param = {}
          list.forEach(a => {
            this.param[a.id] = {
              insValue: [],
              comValue: [],
              resValue: null,
              equipValue: [],
              equipName: [],
              insResult: null
            }
          })
          this.getTableLists0(list)
          this.worker.postMessage(JSON.stringify({
            type: 'saveData',
            tableList: this.tableList,
            param: this.param,
            currentTable: this.currentTable,
            bushing: m
          }));
        } else {
          this.tableLists = []
          this.tableList = []
          this.$message.error('检验项为空')
        }
      }
    },
    // 字典获取信息
    getTypeDicts() {
      this.getDicts("urgency_level").then((response) => {
        this.urgentList = this.dictToValue(response.data);
      });
    },
    // 字典获取信息
    getInsStateDicts() {
      this.getDicts("inspection_task_state").then((response) => {
        this.typeList = this.dictToValue(response.data);
      })
    },
    // 字典获取信息
    getComparisonList() {
      this.getDicts("coordinate_transformation").then((response) => {
        this.comparisonList = this.dictToValue(response.data);
      });
    },
    // 处理页面列表数据--去重,生成检验模板切换列表
    getTableLists() {
      const mySet1 = new Set();
      this.tableLists = this.currentSample.insProduct.filter(m => {
        let num0 = mySet1.size;
        if (m.templateId != null && m.template != null) {
          try {
            mySet1.add(JSON.stringify({
              template: m.template,
              templateId: m.templateId
            }))
          } catch (error) {
            console.log(222, error);
          }
        }
        let num1 = mySet1.size;
        if (num1 > num0) {
          return m
        }
      });
      if (this.tableLists && this.tableLists.length > 0) {
        this.tableList = null;
        this.tableList = [this.tableLists[0]]
        this.currentTable = this.tableLists[0].templateId;
        // 处理页面列表数据
        this.handleTableData()
      }
    },
    // 光纤配置相关模板table列表
    getTableLists0(list) {
      const mySet1 = new Set();
      this.tableLists = list.filter(m => {
        let num0 = mySet1.size;
        if (m.templateId != null && m.template != null) {
          try {
            mySet1.add(JSON.stringify({
              template: m.template,
              templateId: m.templateId
            }))
          } catch (error) {
            console.log(333, error);
          }
        }
        let num1 = mySet1.size;
        if (num1 > num0) {
          return m
        }
      });
      if (this.tableLists && this.tableLists.length > 0) {
        this.tableList = null;
        this.tableList = [this.tableLists[0]]
        this.currentTable = this.tableLists[0].templateId;
        this.currentSample.insProduct = this.HaveJson(list)
        // 处理页面列表数据
        this.handleTableData()
      }
    },
    // 处理页面列表数据
    handleTableData() {
      console.log("ttt-------->>>>", this.tableList);
      this.excelMethodList = []//excel函数列表
      this.widthList = this.tableList[0].style.columnlen;//页面宽度--根据模板来的
      // 温度循环检验原始记录--开始
      if (this.tableLists.find(m => m.templateId == this.currentTable) && this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录') {
        // 对要求值进行拆分处理,进而得出页面内容
        let ask = this.currentSample.insProduct[0].ask
        let askList = ask.split(';')
        // 获取循环次数
        this.numOptions = []
        for (let i = 1; i <= askList[askList.length - 1]; i++) {
          this.numOptions.push({
            value: i,
            label: i
          })
        }
        let mySet1 = new Set();
        askList.forEach((m, i) => {
          if (i < askList.length - 1) {
            mySet1.add(m.split(',')[0].replace('℃', ''))
          }
        })
        // 获取温度点列表
        this.temperatureOptions = []
        mySet1.forEach(m => {
          this.temperatureOptions.push({
            value: String(m),
            label: m
          })
        })
      }
      // 温度循环检验原始记录---结束
      // 热循环检验原始记录---开始
      if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验'))) {
        // 根据要求值拆分数据,得到页面渲染的信息
        let ask = this.currentSample.insProduct[0].ask
        let askList = ask.split(';')
        this.thermalCyclingInfo.max = Number(askList[askList.length - 1])
        let arr = []
        for (let i = 0; i < askList.length - 1; i++) {
          arr.push(askList[i].split(',')[0])
        }
        arr = arr.map(item => {
          let obj = {}
          obj.name = item;
          if (obj.insResult == null || obj.insResult == undefined) {
            obj.arr = [
              {
                value0: '',
                value1: '',
              }
            ]
            obj.insResult = null;
          }
          return obj
        })
        this.thermalCyclingInfo.arr = arr;
      }
      // 热循环检验原始记录---结束
      // 本次循环主要做页面渲染层面的处理--单元格合并预处理
      this.tableList.forEach(a => {
        let mcList = []
        a.template.forEach(b => {
          if (b.v.mc != undefined && b.v.mc.cs != undefined && b.v.mc.rs != undefined) {
            mcList.push(b)
          }
        })
        let count = 0
        mcList.forEach(b => {
          for (var c in a.template) {
            for (var i = 0; i < b.v.mc.cs; i++) {
              for (var i2 = 0; i2 < b.v.mc.rs; i2++) {
                if (a.template[c].c === b.c + i && a.template[c].r === b.r + i2) {
                  let bb = this.HaveJson(b)
                  a.template[c].v.v = bb.v.v
                  a.template[c].v.ps = bb.v.ps
                  a.template[c].v.fc = bb.v.fc
                  a.template[c].v.fs = bb.v.fs
                  a.template[c].v.ht = bb.v.ht
                  a.template[c].mc = count
                  break
                }
              }
            }
          }
          count++
        })
      })
      // 本次循环主要是控制合并,以及控制检验项信息是否展示出来,以便后续检验
      this.tableList.forEach(a => {
        let dels = new Set()//需要删除的行
        let ids = []//所有检验项的id
        let set3 = new Set()
        a.template.forEach(b => {
          let size1 = set3.size
          let size2 = set3.add(b.r).size
          if (size1 < size2) {
            let str = ''
            let str2 = ''
            let unit2 = ''
            let count4 = 0
            let isThree = 0
            a.template.forEach(c => {
              // 获取到 检验项分类+检验项+检验子项的拼接,如果模板里的信息跟接口返回的检验项信息能够匹配则展示出来
              if (b.r === c.r) {
                if (c.v.ps != undefined && c.v.ps.value === '检验项分类' && count4 === 0) {
                  // 三级分类
                  isThree = 1
                } else if (c.v.ps != undefined && c.v.ps.value === '检验项' && count4 === 0) {
                  // 二级分类
                  isThree = 0
                }
                if (isThree == 0) {
                  if (c.v.ps != undefined && c.v.ps.value === '检验项') {
                    if (count4 === 0) {
                      str += c.v.v
                      count4 += 1
                    }
                  } else if (c.v.ps != undefined && c.v.ps.value === '检验子项') {
                    if (count4 === 1) {
                      str += c.v.v
                      count4 += 1
                    }
                  }
                } else if (isThree == 1) {
                  if (c.v.ps != undefined && c.v.ps.value === '检验项分类') {
                    if (count4 === 0) {
                      str += c.v.v
                      count4 += 1
                    }
                  } else if (c.v.ps != undefined && c.v.ps.value === '检验项') {
                    if (count4 === 1) {
                      str += c.v.v
                      count4 += 1
                    }
                  } else if (c.v.ps != undefined && c.v.ps.value === '检验子项') {
                    if (count4 === 2) {
                      str += c.v.v
                      count4 += 1
                    }
                  }
                }
                if (str === '机械性能干态拉伸强度(纵向)') {
                  if (c.v.ps != undefined && c.v.ps.value === '单位') {
                    str2 = str + c.v.v
                    unit2 = c.v.v

                  }
                }
              }
            })
            if (str != '') {
              let count2 = 0
              for (let i in this.currentSample.insProduct) {
                let inspectionItemClass = this.currentSample.insProduct[i].inspectionItemClass == null || this.currentSample.insProduct[i].inspectionItemClass == undefined ? '' : this.currentSample.insProduct[i].inspectionItemClass
                let inspectionItem = this.currentSample.insProduct[i].inspectionItem == null || this.currentSample.insProduct[i].inspectionItem == undefined ? '' : this.currentSample.insProduct[i].inspectionItem
                let inspectionItemSubclass = this.currentSample.insProduct[i].inspectionItemSubclass == null || this.currentSample.insProduct[i].inspectionItemSubclass == undefined ? '' : this.currentSample.insProduct[i].inspectionItemSubclass
                if (inspectionItemSubclass === '干态拉伸强度(纵向)') {
                  // 检验子项为'干态拉伸强度(纵向)'时，模版里是两个计算值对应相同的检验值并且计算方式不同，要根据相同的'单位'做特殊的渲染
                  const unit = this.currentSample.insProduct[i].unit
                  if ((this.currentSample.insProduct[i].templateId === a.templateId && inspectionItemClass + inspectionItem + inspectionItemSubclass + unit === str2) || (this.currentSample.insProduct[i].templateId === a.templateId && !unit2.includes('/') && inspectionItemClass + inspectionItem + inspectionItemSubclass === str)) {
                    ids.push({
                      r: b.r,
                      id: this.currentSample.insProduct[i].id,
                      product: this.currentSample.insProduct[i]
                    })
                    break
                  }
                } else {
                  // 如果相等,那么说明找到了,并且把id存起来,后续检验项也会在页面中显示出来
                  if (this.currentSample.insProduct[i].templateId === a.templateId && inspectionItemClass + inspectionItem + inspectionItemSubclass === str) {
                    ids.push({
                      r: b.r,
                      id: this.currentSample.insProduct[i].id,
                      product: this.currentSample.insProduct[i]
                    })
                    break
                  }
                }
                count2++
              }
              if (count2 == this.currentSample.insProduct.length) {
                dels.add(b.r)
              }
            }
          }
        })
        // 操作删除
        dels.forEach(del => {
          for (let b = 0; b < a.template.length; b++) {
            if (a.template[b].r === del) {
              a.template.splice(b, 1)
              b -= 1
            }
          }
        })
        // 操作赋值--主要赋值单位,试验方法等信息
        ids.forEach(id => {
          for (let b = 0; b < a.template.length; b++) {
            if (a.template[b].r === id.r) {
              a.template[b].i = id.id
              if (a.template[b].v.ps != undefined && a.template[b].v.ps.value === '单位') {
                a.template[b].v.v = id.product.unit
              }
              if (a.template[b].v.ps != undefined && (a.template[b].v.ps.value === '试验方法' || a.template[b].v
                .ps.value === '检测方法')) {
                a.template[b].v.v = id.product.methodS
              }
            }
          }
        })
        let set2 = new Set()
        // 合并的数据处理,cs  rs  代表合并的数量
        a.template.forEach(b => {
          let size1 = set2.size
          let size2 = set2.add(b.mc).size
          if (b.mc != undefined && size1 < size2) {
            b.v.mc.rs = 0
            b.v.mc.cs = 0
            a.template.forEach(c => {
              if (b.mc === c.mc) {
                if (b.r === c.r) {
                  b.v.mc.cs += 1
                }
                if (b.c === c.c) {
                  b.v.mc.rs += 1
                }
              }
            })
          }
        })
      })
      // 本次循环主要是对后端传参进行初始化,样式逻辑修改
      this.tableList.forEach(a => {
        let arrs = []
        let set = new Set()
        let count1 = 0
        let conclusionList = []; //结论列表
        let finalList = []; //最终值列表
        // 结论与最终值在这里一一对应,以下两个列表长度肯定是一样的,如果有不一样,那么多半是模板配置得问题
        conclusionList = a.template.filter(n => n.v.ps != undefined && n.v.ps.value === '结论')//结论列表
        finalList = a.template.filter(n => n.v.ps != undefined && n.v.ps.value === '最终值')//最终值列表
        a.template.forEach(b => {
          if (b.v.ps != undefined && b.v.ps.value === '序号' && (b.v.mc == undefined || Object.keys(b.v.mc).length === 4)) {
            // 对序号进行赋值
            count1++
            b.v.v = count1
          }
          if (b.v.ps != undefined && b.v.ps.value === '要求值') {
            // 对要求值进行赋值
            b.v.v = this.getAsk(b.i)
          }
          // 对页面的和给后端传参的检验值,计算值,设备编码,设备名称,最终值,结论进行初始化
          if (b.v.ps != undefined && typeof b.v.ps.value === 'string' && b.v.ps.value.includes('检验值')) {
            this.$set(b.v, 'v', '')
            // b.v.v = ''
            b.u = ''
            b.i && this.param[b.i] && this.param[b.i].insValue.push(b)
          }
          if (b.v.ps != undefined && b.v.ps.value === '计算值') {
            this.$set(b.v, 'v', '')
            // b.v.v = ''
            b.i && this.param[b.i] && this.param[b.i].comValue.push(b)
          }
          if (b.v.ps != undefined && b.v.ps.value === '设备编码') {
            // b.v.v = ''
            this.$set(b.v, 'v', '')
            b.i && this.param[b.i] && this.param[b.i].equipValue.push(b)
          }
          if (b.v.ps != undefined && b.v.ps.value === '设备名称') {
            this.$set(b.v, 'v', '')
            // b.v.v = ''
            b.i && this.param[b.i] && this.param[b.i].equipName.push(b)
          }
          if (b.v.ps != undefined && b.v.ps.value === '最终值') {
            // b.v.v = ''
            this.$set(b.v, 'v', '')
            if (b.i !== undefined && this.param[b.i] && !this.param[b.i].resValue) {
              this.param[b.i].resValue = b
            }
          }
          if (b.v.ps != undefined && b.v.ps.value === '结论') {
            if (b.i !== undefined && this.param[b.i] && !this.param[b.i].insResult) {
              this.param[b.i].insResult = b
              conclusionList.forEach((n, i) => {
                if (n.r == b.r && n.c == b.c) {
                  b.v.f =
                    `(${this.comparisonList.find(j => j.value == (finalList[i].c)).label}${finalList[i].r + 1})`

                }
              })
            }
          }
          set.add(b.r)
          // 如果模板列表的函数存在,那么加入到excel函数列表里面
          if (b.v.f) {
            this.excelMethodList.push(b)
          }
        })
        // 以下是样式处理逻辑
        // set = Array.sort(set)
        set = [...set]
        set.forEach(b => {
          let arr = []
          a.template.forEach(c => {
            if (c.r === b) {
              arr.push(c)
            }
          })
          arrs.push(arr)
        })
        a.arr = arrs
        this.tableWidth = 0
        for (let i = 0; i < arrs[0].length; i++) {
          this.tableWidth += (a.style.columnlen[i] === undefined ? 100 : a.style.columnlen[i])
        }
      })
      // 本次循环主要是对页面及后端传参进行初始化赋值
      this.currentSample.insProduct.forEach(async a => {
        try {
          // 计算值赋值
          let comValue = JSON.parse(a.insProductResult.comValue)
          for (var i = 0; i < comValue.length; i++) {
            this.param[a.id].comValue[i].v.v = this.toFixed(comValue[i].v, this.param[a.id].comValue[i].v.ct)
          }
        } catch (e) { }
        try {
          // 检验值赋值
          let insValue = JSON.parse(a.insProductResult.insValue)
          for (let i = 0; i < insValue.length; i++) {
            if (this.param[a.id].insValue.find(m => m.c == insValue[i].c && m.r == insValue[i].r)) {
              this.param[a.id].insValue.find(m => m.c == insValue[i].c && m.r == insValue[i].r).v.v = this.toFixed(insValue[i].v, this.param[a.id].insValue.find(m => m.c == insValue[i].c && m.r == insValue[i].r).v.ct)
              this.param[a.id].insValue.find(m => m.c == insValue[i].c && m.r == insValue[i].r).u = insValue[i].u
              // this.param[a.id].insValue[i].v.v = insValue[i].v
              // this.param[a.id].insValue[i].u = insValue[i].u
            }
          }
        } catch (e) { }
        try {
          // 设备编号赋值
          let equipValue = JSON.parse(a.insProductResult.equipValue)
          if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录' || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验'))) {
            // 特殊项目初始化
            this.param[a.id].equipValue = []
            for (let i = 0; i < equipValue.length; i++) {
              this.param[a.id].equipValue.push({
                v: {
                  v: ''
                }
              })
            }
          }
          for (let i = 0; i < equipValue.length; i++) {
            if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录' || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验'))) {
              // 温度循环设备赋值
              this.$set(this.equipForm, `code` + i, equipValue[i].v)
              this.param[a.id].equipValue[i].v.v = equipValue[i].v
            } else {
              // 普通设备赋值
              this.param[a.id].equipValue[i].v.v = equipValue[i].v
            }
          }
        } catch (e) { }
        try {
          // 设备名称赋值
          let equipName = JSON.parse(a.insProductResult.equipName)
          for (let i = 0; i < equipName.length; i++) {
            equipName[i].v !== '' && equipName[i].v.map(val => {
              const index = this.equipOptions.findIndex(item => item.value === val)
              if (index > -1) {
                // 根据设备编码转换为相应的设备名称
                val = this.equipOptions[index].deviceName
              }
            })
          }
          if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录' || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验'))) {
            // 设备名称初始化
            this.param[a.id].equipName = []
            for (let i = 0; i < equipName.length; i++) {
              this.param[a.id].equipName.push({
                v: {
                  v: ''
                }
              })
            }
          }
          for (let i = 0; i < equipName.length; i++) {
            if (this.tableLists.find(m => m.templateId == this.currentTable) && (this.tableLists.find(m => m.templateId == this.currentTable).templateName == '温度循环检验原始记录' || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('热循环') || this.tableLists.find(m => m.templateId == this.currentTable).templateName.includes('温升试验'))) {
              // 温度循环赋值
              this.$set(this.equipForm, `value` + i, equipName[i].v)
              this.param[a.id].equipName[i].v.v = equipName[i].v
            } else {
              // 普通设备名称赋值
              this.param[a.id].equipName[i].v.v = equipName[i].v
            }
          }
        } catch (e) {
          console.log('设备名称赋值----', e)
        }
        try {
          // 最终值赋值
          this.param[a.id].resValue.v.v = this.toFixed(a.lastValue, this.param[a.id].resValue.v.ct)
          // 结论赋值
          this.param[a.id].insResult.v.v = a.insResult
        } catch (e) { }
      })
      // 对excel函数进行处理
      this.handleExcelMethod()
    },
    // 检验值输入后触发的函数
    changeInput(m, code, n, getDataType) {
      // 为数采定义一个逻辑参数
      if (getDataType == 'getDataType') {
        this.getDataType = 2;
      }
      let currentInsItemId = null//当前检验项id
      if (n) {
        currentInsItemId = JSON.parse(JSON.stringify(n.i))
        // 定义一个函数来验证分数是否有效
        if (typeof n.v.v == 'string') {
          function isValidFraction(fraction) {
            const [numerator, denominator] = fraction.split('/'); // 分子和分母
            return !(!denominator || !numerator);
          }
          const isTrue = isValidFraction(n.v.v)
          if (!isTrue) {
            n.v.v = n.v.v.replace('/', '')
          }
        }
      }
      try {
        // 向 Worker 发送消息，开始处理逻辑
        this.worker.postMessage(JSON.stringify({
          code: code,
          tableList: this.tableList,
          excelMethodList: this.excelMethodList,
          comparisonList: this.comparisonList,
          currentSample: this.currentSample,
          PROJECT: this.PROJECT,
          param: this.param,
          currentTable: this.currentTable,
          getDataTypeId: this.getDataTypeId,
          modelType: this.sampleProduct[0].model,
          currentInsItem: n
        }));
      } catch (error) {
        console.log(444, error);
      }

      // 监听 Worker 返回的结果
      this.worker.onmessage = (event) => {
        this.result = JSON.parse(event.data);
        switch (this.result.method) {
          case 'saveInsContext':
            console.log(`output->`, 11111111111111)
            this.$nextTick(() => {
              // this.$delete(this.tableList[0],'arr')
              this.$set(this.tableList[0], 'arr', this.result.value.tableList[0].arr)
              this.param = this.result.value.param
              if (this.result.value.currentInsItem) {
                currentInsItemId = this.result.value.currentInsItem.i
              }
              // 特殊处理一下结论,会有这种特殊情况
              for (var i in this.param) {
                if (this.param[i].insResult && this.param[i].insResult.v && this.param[i].insResult.v.v) {
                  if (this.param[i].insResult.v.v == '合格') {
                    this.$set(this.param[i].insResult.v, 'v', 1)
                  } else if (this.param[i].insResult.v.v == '不合格') {
                    this.$set(this.param[i].insResult.v, 'v', 0)
                  }
                }
              }
            })
            break;
          case 'tableList':
            this.$nextTick(() => {
              // 更新数据
              this.$delete(this.tableList[0], 'arr')
              this.$set(this.tableList[0], 'arr', this.result.value[0].arr)
              // this.param = this.result.value.param
              if (this.result.value.currentInsItem) {
                currentInsItemId = this.result.value.currentInsItem.i
              }
            })
            break;
          case 'getCurrentInsProduct':
            // 更新页面数据
            this.getCurrentInsProduct(this.result.value)
            break;
        }
      };
      // 保存数据
      setTimeout(() => {
        this.saveInsContext(currentInsItemId)
      }, 200)
    },
    // 是否需要数采
    // async determineWhetherToCollectData() {
    //   let res = determineWhetherToCollectData({ managementNumber: '' })
    //   this.isGet = res.data
    // },
    // 根据后端传参更新页面数据   param => this.tableList[0].insProductResult
    getCurrentInsProduct(pId) {
      if (!this.tableList[0].insProductResult) {
        this.tableList[0].insProductResult = {}
      }
      for (let m in this.param[pId]) {
        let value = this.param[pId][m]
        switch (m) {
          case 'comValue':
            // 赋值计算值
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                }
                this.tableList[0].insProductResult[m].push(obj);
              })
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(this.tableList[0].insProductResult[m])
              } catch (error) {
                console.log(555, error);
              }
            }
            break;
          // 赋值检验值
          case 'insValue':
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                  u: a.u,
                }
                this.tableList[0].insProductResult[m].push(obj);
              })
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(this.tableList[0].insProductResult[m])
              } catch (error) {
                console.log(666, error);
              }
            }
            break;
          // 赋值设备编号
          case 'equipValue':
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                }
                this.tableList[0].insProductResult[m].push(obj);
              })
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(this.tableList[0].insProductResult[m])
              } catch (error) {
                console.log(777, error);
              }
            }
            break;
          // 赋值设备名称
          case 'equipName':
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                }
                this.tableList[0].insProductResult[m].push(obj);
              })
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(this.tableList[0].insProductResult[m])
              } catch (error) {
                console.log(888, error);
              }
            }
            break;
          // 赋值最终值
          case 'resValue':
            this.tableList[0].lastValue = value ? value.v.v : ''
            break;
          // 赋值结论
          case 'insResult':
            this.tableList[0].insResult = value ? value.v.v : ''
            break;
        }
      }
    },
    // 对EXCEL函数进行处理
    handleExcelMethod() {
      if (this.excelMethodList.length > 0) {
        this.excelMethodList.map(item => {
          // 得到每个函数的参数列表
          item.valueList = excelFunction.changeParameter(item.v.f);
          return item;
        })
      }
    },
    getValue(v) {
      // 对页面展示数据进行处理,@,代表换行
      let str = v.v ? v.v : (v.v === 0 ? v.v : (v.ct && v.ct.s ? v.ct.s.length > 0 && v.ct.s[0].v.replace(new RegExp('\n', 'g'), '<br/>').replace(new RegExp('@', 'g'), '<br/>') : ''))
      // 对数据保留小数点进行处理
      if (v.ct && v.ct.fa && v.ct.fa.includes('.') && str) {
        let num = 0
        let str0 = v.ct.fa.split('.')[1]
        num = str0.length
        str = Number(str).toFixed(num)
      }
      if (v.v && typeof v.v == 'string' && v.v.includes('@')) {
        str = v.v.replace(new RegExp('@', 'g'), '<br/>')
      }
      return str
    },
    // 获取当前输入框类型
    getInspectionValueType(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].inspectionValueType
        }
      }
    },
    // 获取要求描述
    getTell(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].tell
        }
      }
    },
    // 动态获取单元格宽度
    handleWidth(n) {
      let sum = 0;
      if (n.v.mc && n.v.mc.cs && n.v.mc.c != undefined) {
        for (let i = 0; i < n.v.mc.cs; i++) {
          let num = this.widthList[i + n.v.mc.c] ? this.widthList[i + n.v.mc.c] : 100
          sum += num;
        }
      } else {
        sum = this.widthList[n.c] ? this.widthList[n.c] : 100
      }
      return sum
    },
    // 对输入值进行格式校验
    handleInput(n) {
      try {
        n.v.v = n.v.v.replace(/[^\d.^e>\-/+]/g, '');
        n.v.v = n.v.v.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
        n.v.v = n.v.v.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        n.v.v = n.v.v.replace(/\/{2,}/g, "/"); //只保留第一个/清除多余的
        n.v.v = n.v.v.replace("/", "$#$").replace(/\//g, "").replace("$#$", "/");
      } catch (error) {
        console.log(error);
      }

    },
    getInspectionItemType(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].inspectionItemType
        }
      }
    },
    // 获取要求值
    getAsk(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].ask
        }
      }
    },
    getSystemValue(n) {
      let code = null
      try {
        this.param[n.i].equipValue.forEach(a => {
          if (a.r === n.r) {
            if (a.v.v == null || a.v.v == '') {
              this.$message.error('请先选择采集的设备')
              return
            } else {
              code = a.v.v
            }
          }
        })
      } catch (e) {
        // console.log(e);
        this.$message.error('找不到设备内容')
      }
      // console.log(n, code);
      fetch('http://localhost:82/微信图片_20240518100811.png').then(res => res.blob()).then(blob => {
        // console.log(blob);
        const url = URL.createObjectURL(blob)
        // console.log(url);
      })
      /* this.$message.error('采集失败【已开放手动方式】')
        for (var a in this.currentSample.insProduct) {
          if (this.currentSample.insProduct[a].id == n.i) {
            this.currentSample.insProduct[a].inspectionItemType = 0
          }
        } */
    },
    // 获取所有设备
    getEquipOptions(e, id) {
      if (e) {
        this.equipOptions = []
        search({ status: 0 }).then(res => {
          if (res.code === 200 && res.data) {
            this.equipOptions = res.data.map(m => {
              m.value = m.managementNumber
              m.label = m.deviceName
              return m
            })
          }
        }).catch(error => {
          console.error(error)
        })
      }
    },
    getUserInfo() {
      getUserNow().then(res => {
        this.userId = res.data.id
      })
    },
    // 复核
    upInsReview(e) {
      if (e == 1) {
        // 通过
        this.reviewLoading = true;
        verifyPlan({
          orderId: this.orderId,
          type: 1,
          laboratory: this.sonLaboratory,
          tell: null,
          userId: this.checkUser
        }).then(res => {
          if (res.code === 200) {
            this.$message.success("操作成功")
            this.$emit('goback')
            this.addCheck = false
          }
          this.reviewLoading = false;
        }).catch(error => {
          console.error(error)
          this.reviewLoading = false;
        })
      } else {
        // 不通过
        this.reviewDia = true;
      }
    },
    handleReviewDia() {
      if (this.noReason) {
        this.reviewLoading = true;
        verifyPlan({
          orderId: this.orderId,
          type: 0,
          laboratory: this.sonLaboratory,
          tell: this.noReason
        }).then(res => {
          if (res.code === 200) {
            this.$message.success("操作成功")
            this.$emit('goback')
          }
          this.reviewLoading = false;
        }).catch(error => {
          console.error(error)
          this.reviewLoading = false;
        })
      } else {
        this.$message.error('未输入不通过原因')
      }
    },
    submit() {
      if (this.verifyUser === null || this.verifyUser === '') {
        this.$message.error("请指定复核人员")
        return
      }
      if (!this.otherForm.humidity) {
        this.$message.error("请输入湿度")
        return
      }
      if (!this.otherForm.temperature) {
        this.$message.error("请输入温度")
        return
      }
      this.submitLoading = true;
      checkSubmitPlan({
        orderId: this.orderId,
        laboratory: this.sonLaboratory,
      }).then(res => {
        if (res.code === 200) {
          if (!res.data || res.data.length == 0) {
            this.submitLoading = true;
            submitPlan({
              orderId: this.orderId,
              laboratory: this.sonLaboratory,
              verifyUser: this.verifyUser,
              entrustCode: this.insOrder.entrustCode
            }).then(res => {
              if (res.code === 200) {
                this.$message.success("操作成功")
                this.$emit('goback')
                this.submitLoading = false;
                this.addVerifyDia = false
              }
            }).catch(error => {
              console.error(error)
              this.submitLoading = false;
            })
          } else {
            let newData = []
            const h = this.$createElement
            for (let i in res.data) {
              const lastChar = res.data[i].slice(-1);
              if (lastChar == '-') {
                res.data[i] = res.data[i].slice(0, -1);
              }
              newData.push(h('p', { style: 'font-size: 14px;color: red;' }, (Number(i) + 1) + '、' + res.data[i]))
            }
            newData.push(h('p', { style: 'font-size: 16px;color:#000;margin-top:12px;overflow-y: auto;max-height:80vh' }, '以上项目不合格，确定提交？'))
            this.$confirm('提示', {
              title: '提示',
              message: h('div', null, newData),
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: ""
            }).then(() => {
              this.submitLoading = true;
              submitPlan({
                orderId: this.orderId,
                laboratory: this.sonLaboratory,
                verifyUser: this.verifyUser
              }).then(res => {
                if (res.code === 200) {
                  this.$message.success("操作成功")
                  this.addVerifyDia = false
                  this.$emit('goback')
                }
                this.submitLoading = false;
              }).catch(error => {
                console.error(error)
                this.submitLoading = false;
              })
            }).catch(() => { })
          }
        }
      }).catch(error => {
        console.error(error)
        this.submitLoading = false;
      })
      return
    },
    // 统一在这里保存数据
    saveInsContext(currentInsItemId) {
      try {
        if (this.param) {
          let param = null
          if (currentInsItemId) {
            param = { [currentInsItemId]: this.param[currentInsItemId] }
          } else {
            param = this.param
          }
          saveUnqualifiedContext({
            param: JSON.stringify(param),
            currentTable: this.currentTable,
            sampleId: this.currentSample.id
          }).then(res => {
            this.$message.success('已保存')
          })
          // 向 Worker 发送消息，开始处理逻辑
          this.worker.postMessage(JSON.stringify({
            modelType: this.sampleProduct[0].model,
            type: 'saveData',
            tableList: this.tableList,
            param: this.param,
            currentTable: this.currentTable
          }));
        }
      } catch (error) {
        console.log(999, error);
      }
    },
    // 设备改变
    changeEquip(val, n, v) {
      try {
        // this.$set(n.v,'v',val)
        this.tableList[0].arr.forEach((item, index) => {
          item.forEach((m, i) => {
            if (this.param[m.i]) {
              this.param[m.i].state = 1
            }
            // if(m.i==n.i&&m.v.ps&&m.v.ps.value=='设备名称'&&v){
            //   this.$set(m.v,'v',v)
            // }
          })
        })
        for (let i in this.param) {
          if (this.param[i].state != 1) {
            delete this.param[i]
          }
        }
        // if(val&&v){
        //   for (let i1 in this.param[n.i].equipName) {
        //     if (this.param[n.i].equipName[i1].i === n.i && this.param[n.i].equipName[i1].r === n.r) {
        //       this.$delete(this.param[n.i].equipValue[i1].v,'v')
        //       this.$set(this.param[n.i].equipValue[i1].v,'v',val)
        //       this.$delete(this.param[n.i].equipName[i1].v,'v')
        //       this.$set(this.param[n.i].equipName[i1].v,'v',v)
        //     }
        //   }
        // }
        // this.equipOptions为设备名称下拉框选项数据
        for (let i1 in this.param[n.i].equipName) {
          if (this.param[n.i].equipName[i1].i === n.i && this.param[n.i].equipName[i1].r === n.r) {
            this.$delete(this.param[n.i].equipValue[i1].v, 'v')
            // 将数组赋值给设备编码
            this.$set(this.param[n.i].equipValue[i1].v, 'v', val.join('，'))
            this.$delete(this.param[n.i].equipName[i1].v, 'v')
            // 将数组赋值给设备编码
            this.$set(this.param[n.i].equipName[i1].v, 'v', val)
            this.tableList[0].arr.forEach((item, index) => {
              item.forEach((m) => {
                if (m.i == n.i && m.v.ps && m.v.ps.value == '设备编码') {
                  this.$set(m.v, 'v', val.join('，'))
                }
                if (m.i == n.i && m.v.ps && m.v.ps.value == '设备名称') {
                  this.$set(m.v, 'v', val)
                }
              })
            })
          }
        }
        // 保存数据
        this.saveInsContext(n.i)
      } catch (e) {
        console.log('changeEquip----', e)
      }
    },
    getAuthorizedPerson() {
      selectUserCondition({ type: 1 }).then((res) => {
        let data = [];
        res.data.forEach((a) => {
          data.push({
            label: a.name,
            value: a.id,
          });
        });
        this.personList = data;
      });
    },
    scrollInit() {
      // 获取要绑定事件的元素
      const nav = document.getElementById("tableBox")
      let flag; // 鼠标按下
      let downX; // 鼠标点击的x下标
      let scrollLeft; // 当前元素滚动条的偏移量
      nav.addEventListener("mousedown", function (event) {
        flag = true;
        downX = event.clientX; // 获取到点击的x下标
        scrollLeft = this.scrollLeft; // 获取当前元素滚动条的偏移量
      });
      nav.addEventListener("mousemove", function (event) {
        if (flag) { // 判断是否是鼠标按下滚动元素区域
          let moveX = event.clientX; // 获取移动的x轴
          let scrollX = moveX - downX; // 当前移动的x轴下标减去刚点击下去的x轴下标得到鼠标滑动距离
          this.scrollLeft = scrollLeft - scrollX // 鼠标按下的滚动条偏移量减去当前鼠标的滑动距离
        }
      });
      // 鼠标抬起停止拖动
      nav.addEventListener("mouseup", function () {
        flag = false;
      });
      // 鼠标离开元素停止拖动
      nav.addEventListener("mouseleave", function (event) {
        flag = false;
      });
    },
    async caretTape(num) {
      let index = this.currentKey1 + num
      if (index < 1) {
        this.$message.error('当前是第一个光纤带')
        return
      } else if (index > this.fiberOpticTape.length) {
        this.$message.error('当前是最后一个光纤带')
        return
      }
      this.currentKey1 = index
      this.currentFiberOpticTape = this.HaveJson(this.fiberOpticTape[index - 1])
      this.param = {}
      this.fiberOptic = []
      this.currentFiberOptic = null;
      this.currentFiberOpticTape.productList.forEach(a => {
        this.param[a.id] = {
          insValue: [],
          comValue: [],
          resValue: null,
          equipValue: [],
          equipName: [],
          insResult: null
        }
      })
      this.fiberOpticTapeVisible = false;
      let list = await this.getCurrentProduct(this.currentFiberOpticTape.id, 1)
      this.getTableLists0(list)
      if (this.currentFiberOpticTape.fiber && this.currentFiberOpticTape.fiber.length > 0) {
        // 配置光纤
        this.fiberOptic = this.currentFiberOpticTape.fiber;
      }
    },
    async caretOptic(num) {
      let index = this.currentKey2 + num
      if (index < 1) {
        this.$message.error('当前是第一个光纤')
        return
      } else if (index > this.fiberOptic.length) {
        this.$message.error('当前是最后一个光纤')
        return
      }
      this.currentKey2 = index
      this.currentFiberOptic = this.HaveJson(this.fiberOptic[index - 1])
      this.currentFiberOptic.productList.forEach(a => {
        this.param[a.id] = {
          insValue: [],
          comValue: [],
          resValue: null,
          equipValue: [],
          equipName: [],
          insResult: null
        }
      })
      let list = await this.getCurrentProduct(this.currentFiberOptic.id, 2)
      this.getTableLists0(list)
    },
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
    handleDown(row) {
      downFile({
        id: row.id,
      }).then(res => {
        this.$download.saveAs(res.data.fileUrl, row.fileName);
      }).catch(error => {

      })
    },
    /**
     * 将数值v保留ct.fa中'##'后的指定小数位数，并返回格式化后的字符串。
     *
     * @param v 要格式化的数值
     * @param ct 包含格式化配置的对象
     * @param ct.fa 格式化配置字符串，若包含'##'则按照其后的内容确定小数位数
     * @returns 格式化后的字符串或原始数值（若配置不符合要求）
     */
    toFixed(v, ct) {
      if (v && ct && ct.fa) {
        if (ct.fa.includes('.')) {
          let num = ct.fa.slice(4).length
          return Number(v).toFixed(num)
        } else {
          return v
        }
      } else {
        return v
      }
    }
  }
}
</script>
<style scoped>
.custom-table .el-table__header-wrapper th {
  background-color: #87CEEB;
  /* 只对带有my-custom-table类的表格生效 */
  color: #fff;
}

.container {
  overflow: auto;
  /* 确保容器能包裹浮动元素 */
  margin-bottom: 10px;
}

.right-button {
  float: right;
}

.inspection {
  height: 100%;
  overflow-y: auto;
}

.inspection::-webkit-scrollbar {
  width: 0;
}

.title {
  height: 60px;
  line-height: 60px;
}

.search {
  width: 100%;
  margin-bottom: 10px;
  background-color: #fff;
  border-radius: 3px;
}

.search .form-inline {
  padding-top: 20px;
  padding-left: 0px;
  text-align: left;
}

.center {
  width: calc(100% - 40px);
  /* max-height: 580px; */
  background-color: #fff;
  border-radius: 3px;
  padding: 20px;
  overflow: auto;
}

.center-box {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  flex-wrap: wrap;
  width: 100%;
  overflow-x: auto;
  /* overflow-x: scroll; */
  cursor: grab;
}

.center-box:active {
  cursor: grabbing;
}

.tables {
  table-layout: fixed;
  margin: 5px 5px 16px;
}

.tables td {
  height: 40px;
  width: 100px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
}

.thermal-table {
  min-width: calc(100% - 10px);
  margin: 5px 5px 0;
  table-layout: fixed;
}

.thermal-table td {
  min-width: 70px;
  text-align: center;
  font-size: 14px;
  word-wrap: break-word;
  white-space: normal;
  padding: 5px;
}

.table-container {
  overflow-x: auto;
  /* 使容器支持横向滚动 */
  max-width: 100%;
  /* 限制容器的最大宽度 */
  margin-bottom: 16px;
}

.content {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: left;
  padding: 5px;
  box-sizing: border-box;
  overflow: hidden;
  user-select: none;
}

.content * {
  user-select: none;
}

.content-h-0 {
  justify-content: center;
}

.content-h-1 {
  justify-content: start;
}

.content-h-2 {
  justify-content: flex-end;
}

.content-v-0 {
  align-items: center;
}

.content-v-1 {
  align-items: start;
}

.content-v-2 {
  align-items: end;
}

.table_input {
  width: 100%;
  height: 100%;
}

.table_input>>>.el-input__inner {
  border-color: rgba(0, 0, 0, 0.5) !important;
}

.collection {
  width: 50px;
  height: 100%;
  margin-left: 5px;
  border-color: transparent;
  background-color: #409eff;
  color: #fff;
  border-radius: 4px;
  font-size: 12px;
}

.collection:active {
  opacity: .7;
}

.table_caret {
  font-size: 16px;
  margin: 0 5px;
  color: rgba(0, 0, 0, 0.5);
}

.table_caret:hover {
  color: #409eff;
  cursor: pointer;
}

.table_caret:active {
  opacity: .8;
}

>>>input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
  /* 隐藏微调按钮 */
  margin: 0 !important;
  /* 移除微调按钮的边距 */
}

>>>input[type=number] {
  -moz-appearance: textfield !important;
  /* 针对 Firefox */
}

>>>.el-form-item__content {
  display: inline-flex;
  align-items: center;
}
</style>
<style>
.inspection .el-form-item__label {
  color: #000;
}

.inspection .el-drawer__header::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 30.24px;
  background: #3A7BFA;
  border-radius: 10px;
  margin-left: 32px;
  margin-right: 8.5px;
}

.inspection .el-drawer__header {
  color: #303133;
  text-align: left;
}

.inspection .el-input-group__append {
  padding: 0 14px;
  color: #3A7BFA;
  background-color: #fff;
  height: 100%;
  display: flex;
  align-items: center;
}

.inspection .center-box .el-input__inner {
  font-size: 12px;
  padding: 0 6px;
  text-align: center;
}

.inspection .el-textarea__inner {
  padding: 2px;
}

.inspection .el-textarea__inner::-webkit-scrollbar {
  width: 0;
}

.inspection .el-select {
  display: flex;
  align-items: center;
}

.inspection .el-textarea__inner {
  min-height: 100% !important;
}

.inspection .tables .el-input {
  display: flex;
  align-items: center;
}

.thermal-table .el-input {
  display: flex;
  align-items: center;
}

.custom-dialog .el-dialog__body {
  max-width: 1000px;
  /* 设置最大宽度 */
}

.unPassCheck .el-dialog__body {
  overflow: auto;
  max-height: 800px;
  /* 设置最大宽度 */
}
</style>
