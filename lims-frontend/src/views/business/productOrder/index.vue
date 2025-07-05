<template>
  <div class="app-container">
    <div class="search">
      <el-form :model="entity" ref="entity" size="small" :inline="true">
        <el-form-item label="委托编号" prop="entrustCode">
          <el-input v-model="entity.entrustCode" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable">
          </el-input>
        </el-form-item>
        <el-form-item label="样品名称" prop="sampleName">
          <el-input v-model="entity.sampleName" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable"></el-input>
        </el-form-item>
        <el-form-item label="样品型号" prop="sampleModel">
          <el-input v-model="entity.sampleModel" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable()"></el-input>
        </el-form-item>
        <el-form-item label="下单时间" prop="createTime" v-if="more">
          <el-date-picker v-model="entity.createTime" clearable format="yyyy-MM-dd" placeholder="选择日期" size="small"
                          @change="refreshTable"
            style="width:100%" type="date" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="委托人" prop="prepareUser" v-if="more">
          <el-input v-model="entity.prepareUser" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable">
          </el-input>
        </el-form-item>
        <el-form-item label="委托单位" prop="company" v-if="more">
          <el-input v-model="entity.company" clearable placeholder="请输入" size="small"
            @keyup.enter.native="refreshTable">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :icon="!more ? 'el-icon-arrow-down' : 'el-icon-arrow-up'" style="color: #3A7BFA;" type="text"
                     @click="more = !more">{{ !more ? '更多' : '收起' }}</el-button>
          <el-button size="mini" type="primary" @click="refreshTable()">查询</el-button>
          <el-button size="mini" @click="refresh()">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="container" v-loading="isLoading">
      <div class="table-tab">
        <div>
          <ul class="tab">
            <li :class="{ active: tabIndex === 0 }" v-if="checkPermi(['get:insOrder:reviewed'])" @click="handleTab(0)">待审核</li>
            <li :class="{ active: tabIndex === 1 }" v-if="checkPermi(['get:insOrder:checkout'])" @click="handleTab(1)">检验中</li>
            <li :class="{ active: tabIndex === 2 }" v-if="checkPermi(['get:insOrder:customerOrdered'])" @click="handleTab(2)">委托已检</li>
            <li :class="{ active: tabIndex === 3 }" v-if="checkPermi(['get:insOrder:spotCheck'])" @click="handleTab(3)">抽样已检</li>
            <li :class="{ active: tabIndex === 4 }" v-if="checkPermi(['get:insOrder:sendBack'])" @click="handleTab(4)">退回</li>
            <li :class="{ active: tabIndex === 5 }" v-if="checkPermi(['get:insOrder:revocation'])" @click="handleTab(5)">撤销</li>
            <li :class="{ active: tabIndex === 6 }" v-if="checkPermi(['get:insOrder:all'])" @click="handleTab(6)">全部</li>
          </ul>
        </div>
        <div>
          <el-button v-if="tabIndex === 2 || tabIndex === 3 || tabIndex === 6" size="small"
                     @click="downLoad">导出</el-button>
          <el-button v-if="tabIndex === 1" size="small" type="primary" @click="openAddIns">添加检验项</el-button>
          <el-button size="small" type="primary" @click="openPrint">标签打印</el-button>
          <el-button v-if="checkPermi(['add:insOrder'])" size="small" type="primary" @click="playOrder(1)">下单</el-button>
        </div>
      </div>
      <div class="table">
        <lims-table :tableData="tableData" :column="column" :isSelection="true" :handleSelectionChange="selectMethod"
                    @pagination="pagination" :height="'calc(100vh - 280px)'" :key="upIndex" :page="page"
                    :tableLoading="tableLoading"></lims-table>
      </div>
    </div>
    <div>
      <!-- 审核 -->
      <el-dialog :before-close="handleClose" :visible.sync="verifyDialogVisible" title="下单审核" width="30%">
        <p v-if="!isPass" style="font-size:16px;color:#333333">委托编号<span
            style="color:#34BD66">ZTMS2023071001</span>的信息是否通过</p>
        <el-form v-else ref="ruleForm" :label-position="labelPosition" :model="formData" label-width="150px">
          <el-form-item label="请输入样品库位号：">
            <el-input v-model="formData.specificationModel" size="small" style="width:60%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-row v-if="!isPass">
            <el-button @click="handleClose">退 回</el-button>
            <el-button :loading="upLoad" type="primary" @click="submitForm">通 过</el-button>
          </el-row>
          <el-row v-else>
            <el-button @click="handleClose">返 回</el-button>
            <el-button :loading="upLoad" type="primary" @click="submitForm">确 定</el-button>
          </el-row>
        </span>
      </el-dialog>
      <!-- 撤销 -->
      <el-dialog :before-close="handleClose" :visible.sync="quashDialogVisible" title="下单撤销" width="30%">
        <p v-if="!isQuash" style="font-size:16px;color:#333333">委托编号<span
            style="color:#34BD66">{{ this.insOrderRow.entrustCode }}</span>的信息是否撤销</p>
        <el-form v-else ref="ruleForm" :label-position="labelPosition" :model="formData" label-width="150px">
          <el-form-item label="请输入撤销原因：">
            <el-input v-model="formData.specificationModel" size="small" style="width:60%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-row v-if="!isQuash">
            <el-button @click="handleClose">取 消</el-button>
            <el-button :loading="upLoad" type="primary" @click="submitForm">确 定</el-button>
          </el-row>
          <el-row v-else>
            <el-button @click="handleClose">返 回</el-button>
            <el-button :loading="upLoad" type="primary" @click="submitForm">确 定</el-button>
          </el-row>
        </span>
      </el-dialog>
      <!-- 下发 -->
      <el-dialog :before-close="handleClose" :visible.sync="issuedDialogVisible" title="检验分配" width="400px">
        <div class="body" style="max-height: 60vh;">
          <el-row>
            <el-col class="search_thing" style="width: 95%;">
              <div class="search_label"><span class="required-span">* </span>约定时间：</div>
              <div class="search_input">
                <el-date-picker v-model="distributeData.appointed" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                  style="width: 100%;" type="date" value-format="yyyy-MM-dd">
                </el-date-picker>
              </div>
            </el-col>
            <el-col class="search_thing" style="width: 95%;">
              <div class="search_label"><span class="required-span">* </span>指派人员：</div>
              <div class="search_input">
                <el-select v-model="distributeData.userId" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;" @change="changeUser">
                  <el-option v-for="(item, i) in personList" :key="i + 'gbnm.'" :label="item.label" :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
            <el-col class="search_thing" style="width: 95%;">
              <div class="search_label"><span class="required-span">* </span>试验室：</div>
              <div class="search_input">
                <el-select v-model="distributeData.sonLaboratory" clearable filterable placeholder="请选择" size="small"
                  style="width: 100%;">
                  <el-option v-for="(item, i) in sonLaboratoryList" :key="i + 'oooo'" :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-row>
            <el-button @click="handleClose2">取 消</el-button>
            <el-button :loading="upLoad" type="primary" @click="submitForm2">确 定</el-button>
          </el-row>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dataDialogVisible" title="数据查看" width="80%" @close="closeDia">
        <div v-if="dataDialogVisible">
          <lims-table :tableData="tableDataLook" :column="tableDataLookColumn" @pagination="tableDataLookPagination"
            height="500px" key="tableDataLook" :page="tableDataLookPage"
            :tableLoading="tableDataLookTableLoading"></lims-table>
        </div>
      </el-dialog>
      <el-dialog :visible.sync="filesDialogVisible" title="附件查看" width="80%">
        <div v-if="filesDialogVisible">
          <lims-table :tableData="tableDataFile" :column="columnFile" @pagination="paginationFile" height="500px"
            key="tableDataFile" :page="pageFile" :tableLoading="tableLoadingFile"></lims-table>
        </div>
      </el-dialog>
      <el-dialog :title="deleteTilte" :visible.sync="deleteDialogVisible" width="80%">
        <div v-if="deleteDialogVisible" style="height: 70vh;overflow-y: auto;">
          <lims-table :tableData="componentDataDelete" :column="columnDelete" :isSelection="true"
            :handleSelectionChange="selectDelete" @pagination="paginationDelete" height="500px"
            key="componentDataDelete" :page="pageDelete" :tableLoading="tableLoadingDelete"></lims-table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-row>
            <el-button @click="handleNo">{{ deleteTilte == '撤销' ? '取 消' : '不通过' }}</el-button>
            <el-button :loading="printLoading" type="primary" @click="submitDelete">{{ deleteTilte == '撤销' ? '确 定' : '通过'}}</el-button>
          </el-row>
        </span>
      </el-dialog>
    </div>
    <el-dialog :visible.sync="printDialogVisible" title="标签打印" top="5vh" width="40%">
      <div v-loading="loadPint" style="width:100%;height: 400px;overflow-y: auto;">
        <div class="dia_body">
          <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" style="margin: 10px 5px;"
            @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="checkIndexList" @change="changePrintCode()">
            <el-card v-for="(item, i) in qrData" :key="i + 'wwwww'" class="box-card"
              style="margin-bottom: 15px; font-size: 16px !important;">
              <el-checkbox :key="i" :label="i" style="position: relative;top:-20px;left:5px"><br></el-checkbox>
              <div>
                <el-row style="font-size: 14px;padding-left: 20px;">
                  <el-col style="font-weight: bold;">{{ item.pName }}</el-col>
                </el-row>
                <div style="display: flex;">
                  <div>
                    <el-col :offset="4" :span="8">
                      <vueQr :margin="2" :size="140" :text="JSON.stringify(item.sampleCode)"></vueQr>
                    </el-col>
                  </div>
                  <div style="margin-left: 50px;width: 100%;">
                    <el-row>
                      <el-col style="font-weight: bold;font-size: 14px;"><span>样品编号:&nbsp; </span>{{ item.sampleCode
                        }}</el-col>
                    </el-row>
                    <el-row style="margin-top: 1px;font-size: 14px;">
                      <el-col style="font-weight: bold;"><span>委托单号:&nbsp; </span>{{ item.code }}</el-col>
                    </el-row>
                    <el-row style="margin-top: 1px;font-size: 14px;">
                      <el-col style="font-weight: bold;"><span>规格型号:&nbsp; </span>{{ item.model }}</el-col>
                    </el-row>
                    <el-row style="margin-top: 1px;font-size: 14px;">
                      <el-col style="font-weight: bold;"><span>样品名称:&nbsp; </span>{{ item.sample }}</el-col>
                    </el-row>
                    <el-row class="ellipsis-multiline" style="margin-top: 1px;font-size: 14px;">
                      <el-col style="font-weight: bold;"><span>检测项目:&nbsp; </span>{{ item.item }}</el-col>
                    </el-row>
                    <el-row style="margin-top: 3px;font-size: 14px;">
                      <el-col style="font-weight: bold;display: flex;align-items: center;"><span>样品状态:&nbsp; </span>
                        <el-radio-group :value="item.insState" disabled style="margin-top: 7px;margin-left: 4px;">
                          <el-radio :label="0" style="font-weight: bold;margin-right: 7px;">待检</el-radio>
                          <el-radio :label="1" style="font-weight: bold;margin-right: 7px;">在检</el-radio>
                          <el-radio :label="2" style="font-weight: bold;margin-right: 7px;">已检</el-radio>
                        </el-radio-group><el-radio v-model="item.isLeave" :label="1" disabled
                          style="font-weight: bold;margin-top: 7px;">留样</el-radio></el-col>
                    </el-row>
                  </div>
                </div>
              </div>
            </el-card>
          </el-checkbox-group>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="printDialogVisible = false">取 消</el-button>
          <el-button :loading="printLoading" type="primary" @click="submitPrint">打 印</el-button>
        </el-row>
      </span>
    </el-dialog>
    <div class="el-dialog__body"
      style="overflow-y: auto;margin-top: 0;position: fixed;top: 999px;z-index: 99999;display: none;">
      <div id="printMOrder" ref="printMOrder" class="printMOrder">
        <el-card v-for="(item, i) in checkDataList" :key="i + 'uuuuu'" class="box-card"
          style="font-size: 0.20cm !important;page-break-after: always;color: #000;box-shadow: none;margin: 0 !important;padding: 0 !important;">
          <div style="display: flex;">
            <div>
              <el-col :offset="2" :span="10">
                <vueQr :margin="2" :size="60" :text="JSON.stringify(item.sampleCode)"></vueQr>
              </el-col>
            </div>
            <div style="margin-left: 0.32cm;line-height: 0.22cm;">
              <el-row>
                <el-col class="single-line-ellipsis" style="font-size: 0.20cm;width: 4cm;"><span>样品编号:&nbsp; </span>{{
                  item.sampleCode }}</el-col>
              </el-row>
              <el-row style="font-size: 0.20cm;">
                <el-col><span>委托单号:&nbsp; </span>{{ item.code }}</el-col>
              </el-row>
              <el-row style="font-size: 0.20cm;">
                <el-col><span>规格型号:&nbsp; </span>{{ item.model }}</el-col>
              </el-row>
              <el-row style="font-size: 0.20cm;">
                <el-col><span>样品名称:&nbsp; </span>{{ item.sample }}</el-col>
              </el-row>
              <el-row style="font-size: 0.20cm;width: 4cm;">
                <el-col class="ellipsis-multiline"><span>检测项目:&nbsp; </span>{{ item.item }}</el-col>
              </el-row>
              <el-row style="margin-top: 0.01cm;font-size: 0.20cm;">
                <el-col style="display: flex;align-items: center;"><span>样品状态:&nbsp;
                  </span>
                  <span style="white-space: nowrap;"><span v-if="item.insState == 0">√</span><span v-if="item.insState != 0"
                      class="scor"></span>待检
                    <span v-if="item.insState == 1">√</span><span v-if="item.insState != 1" class="scor"></span>在检
                    <span v-if="item.insState == 2">√</span><span v-if="item.insState != 2" class="scor"></span>已检
                    <span v-if="item.isLeave == 1">√</span><span v-if="item.isLeave != 1" class="scor"></span>留样</span>
                  <!-- <el-radio-group :value="item.insState" style="margin-top: 3px;margin-left: 1px;" disabled>
                  <el-radio :label="0" style="margin-right: 3px;font-size: 6px;">待检</el-radio>
                  <el-radio :label="1" style="margin-right: 3px;font-size: 6px;">在检</el-radio>
                  <el-radio :label="2" style="margin-right: 3px;font-size: 6px;">已检</el-radio>
                </el-radio-group><el-radio :label="1" style="margin-top: 3px;font-size: 8px;" v-model="item.isLeave" disabled>留样</el-radio> -->
                </el-col>
              </el-row>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    <!-- 修改委托编号弹框 -->
    <el-dialog :visible.sync="entrustCodeVisible" title="提示" width="30%">
      <el-input v-model="entrustCodeInfo.entrustCode"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="entrustCodeVisible = false">取 消</el-button>
        <el-button :loading="submitCodeLoading" type="primary" @click="submitCode">确 定</el-button>
      </span>
    </el-dialog>
    <!--标签打印弹框-->
    <print-dialog v-if="printDialog" ref="printDialog" :printDialog="printDialog"
      @closePrintDialog="closePrintDialog"></print-dialog>
    <!--添加遗漏检验项弹框-->
    <add-inspection-dia v-if="addInspectionDia" ref="addInspectionDia"></add-inspection-dia>
    <!--修改样品型号弹框-->
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false"
               :visible.sync="dialogVisible" title="修改样品型号" width="80%">
      <el-table ref="sampleTable" :data="sampleList" border highlight-current-row
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
                max-height="400px" tooltip-effect="dark">
        <el-table-column align="center" label="序号" type="index" width="65"></el-table-column>
        <el-table-column align="center" label="样品名称" min-width="100" prop="sample"> </el-table-column>
        <el-table-column align="center" label="样品编号" min-width="100" prop="sampleCode"></el-table-column>
        <el-table-column align="center" label="样品型号" min-width="60" prop="model">
          <template slot-scope="scope">
            <el-input v-model="scope.row.model" clearable placeholder="不填写则系统自动生成"
                      size="small"></el-input>
          </template>
        </el-table-column>
        <el-table-column align="center" label="检验标准" min-width="100" prop="standardMethodName"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
        <el-button :loading="submitListLoad" type="primary" @click="submitList">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import vueQr from 'vue-qr'
import PrintJS from 'print-js'
import PrintDialog from "@/views/business/productOrder/components/printDialog.vue";
import AddInspectionDia from "@/views/business/productOrder/components/addInspectionDia.vue";
import limsTable from "@/components/Table/lims-table.vue";
import {
  checkUpdate, delInsOrder, getSampleByOrderId,
  rawAllInsOrderExport,
  selectInsOrderParameter, selectNoProducts, selectOrderManDay,
  updateInspected,
  updateOrderEntrustCode, updateSampleModel, updateStatus, upInsOrder, upPlanUser2
} from "@/api/business/productOrder";
import { selectUserCondition } from "@/api/performance/class";
import { downFile, getFileList, selectSampleAndProductByOrderId } from "@/api/business/rawMaterialOrder";
import {mapGetters} from "vuex";
export default {
  name: 'ProductOrder',
  components: {
    limsTable,
    AddInspectionDia,
    PrintDialog,
    vueQr
  },
  data() {
    return {
      isLoading: false, // 控制加载状态
      entity: {
        orderType: '',
        state: '',
      },
      upIndex: 0,
      tableData: [],
      tableLoading: false,
      column: [
        {
          label: "委托编号",
          prop: "entrustCode",
          width: "160px",
          dataType: "link",
          linkMethod: "changeEntrustCode",
        },
        { label: '委托单位', prop: 'company' },
        {
          label: "样品名称",
          prop: "sampleName",
          width: "160px",
          dataType: "link",
          linkMethod: "selectAllByOne",
        },
        {
          label: "样品型号",
          prop: "sampleModel",
          width: "160px",
          dataType: "link",
          linkMethod: "editSampleModel",
        },
        { label: '样品数量', prop: 'sampleNum' },
        { label: '检验人', prop: 'testingName' },
        {
          dataType: 'tag',
          label: '紧急程度',
          prop: 'type',
          formatData: (params) => {
            if (params == 0) {
              return '普通'
            } else if (params == 1) {
              return '优先'
            } else {
              return '紧急'
            }
          },
          formatType: (params) => {
            if (params == 0) {
              return 'success'
            } else if (params == 1) {
              return 'warning'
            } else {
              return 'danger'
            }
          }
        },
        { label: '备注', prop: 'remark' },
        { label: '检验进度%', prop: 'insProgress' },
        { label: '约定时间', prop: 'appointed' },
        {
          dataType: 'tag',
          label: '检验结果',
          prop: 'insResult',
          formatData: (params) => {
            if (params == 1) {
              return '合格'
            } else if (params == 0) {
              return '不合格'
            } else if (params == 3) {
              return '不判定'
            } else {
              return null
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 0) {
              return 'danger'
            } else if (params == 3) {
              return ''
            } else {
              return null
            }
          }
        },
        { label: '下单时间', prop: 'createTime' },
        { label: '下发时间', prop: 'sendTime' },
        { label: '退回理由', prop: 'tell' },
        { label: '委托人', prop: 'prepareUser' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          operation: [
            {
              name: '数据查看',
              type: 'text',
              clickFun: (row) => {
                this.handleDataLook(row);
              },
              disabled: (row, index) => {
                return row.state != 1 && row.state != 4
              },
              showHide: (row) => {
                return this.tabIndex === 1 || this.tabIndex === 2 || this.tabIndex === 3 || this.tabIndex === 6
              }
            },
            {
              name: '附件查看',
              type: 'text',
              clickFun: (row) => {
                this.handleFileLook(row);
              },
              disabled: (row, index) => {
                return row.state != 1 && row.state != 4
              },
              showHide: (row) => {
                return this.tabIndex === 1 || this.tabIndex === 2 || this.tabIndex === 3 || this.tabIndex === 6
              }
            },
            {
              name: '报告下载',
              type: 'text',
              clickFun: (row) => {
                this.download(row);
              },
              disabled: (row, index) => {
                return row.state != 4 || row.reportId == null
              },
              showHide: (row) => {
                return this.tabIndex === 2 || this.tabIndex === 3 || this.tabIndex === 6
              }
            },
            {
              name: '审核',
              type: 'text',
              clickFun: (row) => {
                this.handleVerify(row);
              },
              disabled: (row, index) => {
                return row.state != 0
              },
              showHide: (row) => {
                return this.tabIndex === 0
              }
            },
            {
              name: '撤销',
              type: 'text',
              clickFun: (row) => {
                this.handlEquash(row);
              },
              disabled: (row, index) => {
                return row.state != 1 && row.state != 0
              },
              showHide: (row) => {
                return this.tabIndex === 0 || this.tabIndex === 1
              }
            },
            {
              name: '撤销审核',
              type: 'text',
              clickFun: (row) => {
                this.handlEquashCheck(row);
              },
              disabled: (row, index) => {
                return (row.state != 1 && row.state != 0) || (this.tabIndex != 1) || row.isRevocation != 1
              },
              showHide: (row) => {
                return this.tabIndex === 1
              }
            },
            {
              name: '分配',
              type: 'text',
              clickFun: (row) => {
                this.handleIssued(row);
              },
              disabled: (row, index) => {
                return row.state != 1 || !!row.assign
              },
              showHide: (row) => {
                return this.tabIndex === 1
              }
            },
            {
              name: '删除',
              type: 'text',
              clickFun: (row) => {
                this.handleDelete(row);
              },
              showHide: (row) => {
                return this.tabIndex === 4 || this.tabIndex === 5
              }
            },
            {
              name: '原始记录',
              type: 'text',
              clickFun: (row) => {
                this.viewInspectInfo(row);
              },
              showHide: (row) => {
                return this.tabIndex === 2 || this.tabIndex === 3 || this.tabIndex === 6
              }
            },
          ]
        }
      ],
      page: {
        total: 0,
        size: 20,
        current: 1
      },
      state: 0,// 0:台账页，1：检验页面,2检验页面(复核)，默认为0,3数据查看
      InspectionKey: 1,
      inspectorList: [],//检验人员列表
      sonLaboratory: null,// 0:委托，1：原材料
      typeSource: null,// 0:成品下单，1：原材料下单
      deleteTilte: '撤销',
      examine: null,
      deleteList: [],
      loadPint: false,
      checkAll: false,
      isIndeterminate: true,
      printLoading: false,
      printDialogVisible: false,
      //是否审核通过  true是 false不是
      isPass: false,
      //是否审核撤销  true是 false不是
      isQuash: false,
      verifyDialogVisible: false,
      quashDialogVisible: false,
      issuedDialogVisible: false,
      dataDialogVisible: false, // 数据查看弹框
      currentRow: {}, // 数据查看弹框
      tableDataLookTableLoading: false, // 数据查看弹框
      tableDataLook: [],
      tableDataLookPage: {
        total: 0,
        size: 20,
        current: 1
      },
      tableDataLookColumn: [
        { label: '样品编号', prop: 'sampleCode', width: '160px' },
        { label: '样品名称', prop: 'sample' },
        { label: '检验项分类', prop: 'inspectionItemClass' },
        { label: '检验项', prop: 'inspectionItem' },
        { label: '检验子项', prop: 'inspectionItemSubclass' },
        { label: '单位', prop: 'unit' },
        { label: '样品型号', prop: 'model' },
        { label: '条件', prop: 'radius' },
        { label: '电缆标识', prop: 'cableTag' },
        { label: '试验要求', prop: 'tell' },
        { label: '检验结果', prop: 'lastValue' },
        {
          dataType: 'tag',
          label: '结果判定',
          prop: 'insResult',
          formatData: (params) => {
            if (params == 1) {
              return '合格'
            } else if (params == 0) {
              return '不合格'
            } else if (params == 3) {
              return '不判定'
            } else {
              return ''
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 0) {
              return 'danger'
            } else if (params == 3) {
              return ''
            } else {
              return ''
            }
          }
        },
      ],
      filesDialogVisible: false, // 附件查看弹框
      tableDataFile: [],
      tableLoadingFile: false,
      columnFile: [
        {
          dataType: 'tag',
          label: '类型',
          prop: 'type',
          formatData: (params) => {
            if (params == 1) {
              return '图片'
            } else if (params == 2) {
              return '文件'
            } else {
              return ''
            }
          },
          formatType: (params) => {
            if (params == 1) {
              return 'success'
            } else if (params == 2) {
              return 'warning'
            } else {
              return ''
            }
          }
        },
        { label: '附件名称', prop: 'fileName' },
        { label: '上传人', prop: 'name' },
        { label: '上传时间', prop: 'createTime' },
        {
          dataType: 'action',
          fixed: 'right',
          label: '操作',
          width: '170px',
          operation: [
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.handleDown(row);
              }
            }
          ]
        }
      ],
      pageFile: {
        total: 0,
        size: 20,
        current: 1
      },
      formData: {},
      formData0: {},
      formData1: {},
      distributeData: {
        orderId: '',
        sampleId: '',
        appointed: '',
        userId: '',
        sonLaboratory: ''
      },
      entrustCodeVisible: false, // 修改委托编号弹框
      entrustCodeInfo: {},
      submitCodeLoading: false,
      // 人员列表
      personList: [],
      orderId: '',
      revocationInsProductIds: '',
      componentDataDelete: [],
      tableLoadingDelete: false,
      columnDelete: [
        { label: '样品编号', prop: 'sampleCode', width: '160px' },
        { label: '检验项分类', prop: 'inspectionItemClass' },
        { label: '检验项', prop: 'inspectionItem' },
        { label: '检验项子类', prop: 'inspectionItemSubclass' },
        { label: '样品分类', prop: 'sampleType' },
        { label: '样品', prop: 'sample' },
        { label: '型号', prop: 'model' },
        { label: '线芯', prop: 'cableTag' },
        { label: '试验室', prop: 'sonLaboratory' },
        { label: '要求值', prop: 'ask' },
        { label: '要求描述', prop: 'tell' },
      ],
      pageDelete: {
        total: 0,
        size: 20,
        current: 1
      },
      deleteDialogVisible: false,
      upLoad: false,
      tabList: [
        {
          label: '待审核',
          value: 0
        }, {
          label: '检验中',
          value: 1
        },
        {
          label: '委托已检',
          value: 4
        },
        {
          label: '抽样已检',
          value: 4
        },
        {
          label: '退回',
          value: 2
        },
        {
          label: '撤销',
          value: 3
        },
        {
          label: '全部',
          value: -2
        }
      ],
      tabIndex: 0,
      active: 0, //1：下单，2：查看，3：审核，4：光纤配置,默认为0
      currentId: null,
      more: false,
      insOrderRow: {},
      checkIndexList: [],
      checkDataList: [
        {
          sampleCode: '11111'
        }
      ],
      qrData: [],
      multipleSelection: [],
      sonLaboratoryList: [],
      printDialog: false,
      addInspectionDia: false,
      dialogVisible: false,
      submitListLoad: false,
      sampleList: []
    }
  },
  watch: {
    printDialogVisible(newVal) {
      if (!newVal) {
        this.qrData = []
        this.checkIndexList = []
        this.checkDataList = []
        this.isIndeterminate = true;
      }
    }
  },
  computed: {
    ...mapGetters(["nickName"]),
  },
  mounted() {
    if (this.checkPermi(['get:raw:await'])) {
      this.tabIndex = 0
    } else {
      this.tabIndex = 2
    }
    this.refreshTable()
    this.getAuthorizedPerson()
  },
  activated() {
    this.refreshTable()
  },
  methods: {
    // 查询列表数据
    refreshTable() {
      if (this.tabIndex !== 2 && this.tabIndex !== 3) {
        this.entity.orderType = ''
      } else {
        if (this.tabIndex === 2) {
          this.entity.orderType = 'Customer-ordered test'
        } else {
          this.entity.orderType = '抽检'
        }
      }
      const params = { ...this.entity, state: this.tabList[this.tabIndex].value }
      this.isLoading = true; // 开始加载
      selectInsOrderParameter({...this.page,...params}).then(res => {
        this.isLoading = false; // 结束加载
        this.upIndex++
        this.tableLoading = false
        if (res.code === 200) {
          this.tableData = res.data.records
          this.page.total = res.data.total
        }
      }).catch(err => {
        this.isLoading = false; // 结束加载
      })
    },
    refresh() {
      this.resetForm('entity')
      this.refreshTable()
    },
    pagination(page) {
      this.page.size = page.limit
      this.refreshTable()
    },
    // 打开修改委托编号弹框
    changeEntrustCode(row) {
      if (this.tabIndex !== 1) {
        return
      }
      this.entrustCodeVisible = true
      this.entrustCodeInfo = { ...row }
    },
    // 提交修改委托编号
    submitCode() {
      this.submitCodeLoading = true
      updateOrderEntrustCode({
        id: this.entrustCodeInfo.id,
        entrustCode: this.entrustCodeInfo.entrustCode,
      }).then(res => {
        if (res.code === 200) {
          this.entrustCodeVisible = false
          this.refreshTable()
          this.$message.success('修改成功')
        }
        this.submitCodeLoading = false
      }).catch((err) => {
        this.submitCodeLoading = false
      })
    },
    selectMethod(val) {
      this.multipleSelection = val
    },
    //选择要打印的二维码
    changePrintCode() {
      let indexList = this.checkIndexList
      let arr = []
      indexList.forEach(i => {
        arr.push(this.qrData[i])
      })
      this.checkDataList = arr
    },
    //全选
    handleCheckAllChange(val) {
      if (val) {
        for (var i = 0; i < this.qrData.length; i++) {
          this.checkIndexList.push(i)
        }
        this.checkDataList = this.qrData
      } else {
        this.checkIndexList = []
        this.checkDataList = []
      }
      this.isIndeterminate = false;
    },
    submitPrint() {
      if (this.checkDataList.length < 1) {
        this.$message.warning("请选择要打印的二维码")
        return
      }
      this.printDialogVisible = false;
      PrintJS({
        printable: 'printMOrder',//页面
        type: "html",//文档类型
        maxWidth: 360,
        style: `@page {
                margin:0;
                size: 400px 75px collapse;
                margin-top:3px;
                &:first-of-type{
                  margin-top:0 !important;
                }
              }
              html{
                zoom:100%;
              }
              @media print{
                width: 400px;
                height: 75px;
                margin:0;
              }`,
        onPrintDialogClose: this.erexcel = false,
        targetStyles: ["*"], // 使用dom的所有样式，很重要
        font_size: '0.20cm',
      });
    },
    // 打开添加检验项弹框
    openAddIns() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择需要添加检验项的数据')
        return
      }
      if (this.multipleSelection.length > 1) {
        this.$message.warning('只能选择一条数据')
        return
      }
      this.addInspectionDia = true
      this.$nextTick(() => {
        this.$refs.addInspectionDia.getDataList(this.multipleSelection[0])
      })
    },
    // 导出记录
    downLoad() {
      const params = { ...this.entity, state: this.tabList[this.tabIndex].value }
      rawAllInsOrderExport({ ...params }).then(res => {
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '委托检测信息导出.xlsx');
      }).catch(err => {
        console.log('err---', err);
      })
    },
    // 打开标签打印弹框
    openPrint() {
      if (this.multipleSelection.length > 0) {
        this.printDialog = true
        this.$nextTick(() => {
          let selection = this.multipleSelection
          this.$refs.printDialog.getLabelPrinting(selection)
        })
      } else {
        this.$message.error('请选择需要打印的数据')
      }
    },
    // 关闭标签打印弹框
    closePrintDialog() {
      this.printDialog = false
    },
    // 获取指派人员下拉列表
    getAuthorizedPerson() {
      selectUserCondition({ type: 1 }).then(res => {
        let data = []
        res.data.forEach(a => {
          data.push({
            label: a.name,
            value: a.id
          })
        })
        this.personList = data
      })
    },
    handleClose() {
      this.verifyDialogVisible = false;
      this.quashDialogVisible = false;
      this.issuedDialogVisible = false;
      this.dataDialogVisible = false;
      this.upLoad = false;
    },
    handleClose2() {
      this.verifyDialogVisible = false;
      this.quashDialogVisible = false;
      this.issuedDialogVisible = false;
      this.dataDialogVisible = false;
      this.upLoad = false;
    },
    // 点击样品名称
    selectAllByOne(row) {
      this.$router.push({
        path: "/productOrder/addView", query: {
          examine: 1,
          active: 2,
          currentId: row.id,
          tabIndex: this.tabIndex,
        }
      });
    },
    // 修改样品型号
    editSampleModel (row) {
      if (this.tabIndex !== 1) {
        return
      }
      this.dialogVisible = true
      this.getDataList(row)
    },
    getDataList(row) {
      this.dialogVisible = true
      getSampleByOrderId({insOrderId: row.id}).then(res => {
        this.sampleList = res.data
      })
    },
    // 提交样品型号修改
    submitList () {
      this.submitListLoad = true
      updateSampleModel(this.sampleList).then(res => {
        this.submitListLoad = false
        this.dialogVisible = false
        this.$message.success('修改成功')
        this.refreshTable()
      }).catch(err => {
        this.submitListLoad = false
      })
    },
    // 数据查看
    handleDataLook(row) {
      this.dataDialogVisible = true;
      this.currentRow = row;
      this.getDataTableList(this.currentRow)
    },
    // 查询数据查看列表数据
    getDataTableList(row) {
      this.tableDataLookTableLoading = true
      selectSampleAndProductByOrderId({ id: row.id, ...this.tableDataLookPage }).then(res => {
        this.tableDataLookTableLoading = false
        if (res.code === 200) {
          this.tableDataLook = res.data.records
          this.tableDataLookPage.total = res.data.total
        }
      }).catch(err => {
        this.tableDataLookTableLoading = false
      })
    },
    tableDataLookPagination(page) {
      this.tableDataLookPage.size = page.limit
      this.getDataTableList(this.currentRow)
    },
    closeDia () {
      this.tableDataLookPage = {
        total: 0,
        size: 20,
        current: 1
      }
      this.dataDialogVisible = false
    },
    // 附件查看
    handleFileLook(row) {
      this.filesDialogVisible = true
      this.getFileList(row)
    },
    getFileList(row) {
      this.tableLoadingFile = true
      getFileList({ insOrderId: row.id }).then(res => {
        this.tableLoadingFile = false
        if (res.code === 200) {
          this.tableDataFile = res.data.records
          this.pageFile.total = res.data.total
        }
      }).catch(err => {
        this.tableLoadingFile = false
      })
    },
    paginationFile(page) {
      this.pageFile.size = page.limit
      this.getFileList()
    },
    // 附件下载
    handleDown(row) {
      downFile({
        id: row.id,
      }).then(res => {
        this.$download.saveAs(res.data.fileUrl, row.fileName);
      }).catch(error => {

      })
    },
    // 下载报告
    download(row) {
      let url = row.urlS ? row.urlS : row.url;
      if(url){
        url = url.split('.')[0]+'.pdf'
        const link = document.createElement('a');
        link.href = this.javaApi + url;
        link.target = '_blank';
        document.body.appendChild(link);
        link.click();
      }
    },
    // 撤销
    handlEquash(row) {
      if (this.tabIndex != 1) {
        this.quashDialogVisible = true;
        this.insOrderRow = row
      } else {
        this.orderId = row.id
        this.revocationInsProductIds = ''
        this.deleteTilte = '撤销'
        this.deleteDialogVisible = true
        this.getDeleteList()
      }
    },
    getDeleteList() {
      this.tableLoadingDelete = true
      selectNoProducts({ orderId: this.orderId, revocationInsProductIds: this.revocationInsProductIds, ...this.pageDelete }).then(res => {
        this.tableLoadingDelete = false
        this.componentDataDelete = res.data.records
        this.pageDelete.total = res.data.total
      }).catch(err => {
        this.tableLoadingDelete = false
      })
    },
    paginationDelete(page) {
      this.pageDelete.size = page.limit
      this.getDeleteList()
    },
    selectDelete(arr) {
      this.deleteList = arr;
    },
    submitDelete() {
      if (this.deleteTilte == '撤销') {
        if (this.deleteList.length == 0) {
          this.$message.error('请选择要撤销的项')
          return
        }
        let ids = this.deleteList.map(m => m.id).join(',')
        this.printLoading = true;
        updateInspected({ ids: ids, orderId: this.orderId }).then(res => {
          if (res.code === 200) {
            this.printLoading = false
            this.deleteDialogVisible = false
            this.refreshTable('page')
            this.$message.success("更新成功")
          }
        })
      } else {
        checkUpdate({ orderId: this.orderId, state: 1 }).then(res => {
          if (res.code === 200) {
            this.printLoading = false
            this.deleteDialogVisible = false
            this.refreshTable('page')
            this.$message.success("更新成功")
          }
        })
      }
    },
    handlEquashCheck(row) {
      this.orderId = row.id
      this.revocationInsProductIds = row.revocationInsProductIds
      this.deleteTilte = '撤销审核'
      this.deleteDialogVisible = true;
    },
    handleNo() {
      if (this.deleteTilte == '撤销') {
        this.deleteDialogVisible = false
      } else {
        checkUpdate({ orderId: this.orderId, state: 0 }).then(res => {
          if (res.code === 200) {
            this.printLoading = false
            this.deleteDialogVisible = false
            this.refreshTable('page')
            this.$message.success("更新成功")
          }
        })
      }
    },
    // 下发
    handleIssued(row) {
      this.issuedDialogVisible = true;
      selectOrderManDay({
        id: row.id
      }).then(res => {
        this.distributeData.orderId = row.id
        this.distributeData.sampleId = row.sampleId
        this.distributeData.appointed = res.data
        this.distributeData.type = row.type
      })
      upPlanUser2({
        orderId: row.id,
      }).then(res => {
        if (res.code === 200 && res.data.length > 0) {
          this.sonLaboratoryList = [];
          res.data.forEach(m => {
            this.sonLaboratoryList.push({
              value: m,
              label: m
            })
          })
        }
      })
    },
    submitForm2() {
      if (this.distributeData.appointed == null || this.distributeData.appointed == '') {
        this.$message.error('约定时间未填写')
        return
      }
      //
      if (this.distributeData.userId == null || this.distributeData.userId == '') {
        this.$message.error('指派人员未填写')
        return
      }
      if (this.distributeData.userId && (this.distributeData.sonLaboratory == null || this.distributeData.sonLaboratory == '')) {
        this.$message.error('试验室未填写')
        return
      }
      this.upLoad = true;
      upInsOrder({
        orderId: this.distributeData.orderId,
        sampleId: this.distributeData.sampleId,
        appointed: this.distributeData.appointed,
        userId: this.distributeData.userId,
        sonLaboratory: this.distributeData.sonLaboratory,
      }).then(res => {
        this.$message.success('修改成功')
        this.upLoad = false
        this.issuedDialogVisible = false
        this.refreshTable()
      }).catch(e => {
        this.$message.error('修改失败')
        this.upLoad = false
      })
    },
    submitForm() {
      this.upLoad = true;
      if (this.tabIndex == 1) {
        updateInspected({ id: this.insOrderRow.id }).then(res => {
          if (res.code === 200) {
            this.upLoad = false
            this.quashDialogVisible = false
            this.refreshTable()
            this.$message.success("更新成功")
          }
        })
      } else {
        updateStatus({ id: this.insOrderRow.id }).then(res => {
          if (res.code === 200) {
            this.upLoad = false
            this.quashDialogVisible = false
            this.refreshTable('page')
            this.$message.success("更新成功")
          }
        })
      }
      let authorizedPerson = this.formData.authorizedPerson && this.formData.authorizedPerson.length > 0 ? this.formData.authorizedPerson.join(',') : ''
      delete this.formData.createTime
      delete this.formData.updateTime
      delete this.formData.createUser
      delete this.formData.updateUser
      this.formData.authorizedPerson = authorizedPerson
    },
    // 下单
    playOrder(num) {
      this.$router.push({ path: "/productOrder/addOrder", query: { examine: 0, active: num, tabIndex: this.tabIndex } });
    },
    // 审核
    handleVerify(row) {
      this.$router.push({ path: "/productOrder/addView", query: { examine: 1, active: 3, currentId: row.id } });
    },
    handleTab(i) {
      this.tabIndex = i;
      this.refreshTable()
    },
    // 查看检验数据
    viewInspectInfo(row) {
      //当前检验任务的检验人列表
      let inspectorList = []
      if (row.userName) {
        inspectorList = row.userName.split(',')
      }
      inspectorList.push(this.nickName)
      if (this.checkPermi(['business:inspectionView'])) {
        this.$router.push({
          name: "InspectionView",
          query: {
            sonLaboratory: row.sonLaboratory,
            state: 3,
            typeSource: row.typeSource,
            orderId: row.id,
            inspectorList: inspectorList,
          },
        })
      } else {
        this.$router.push({
          path: "/inspectionTask/inspection",
          query: {
            sonLaboratory: row.sonLaboratory,
            state: 3,
            typeSource: row.typeSource,
            orderId: row.id,
            inspectorList: inspectorList,
          },
        })
      }
    },
    goback() {
      this.state = 0
      this.refreshTable('page')
    },
    // 刷新页面
    refreshView() {
      this.InspectionKey++
    },
    changeUser() {
      if (this.sonLaboratoryList.length > 0) {
        this.distributeData.sonLaboratory = this.sonLaboratoryList[0].value
      }
    },
    handleDelete(row) {
      this.$confirm('是否删除当前数据?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        delInsOrder({ insOrderId: row.id }).then(res => {
          this.$message.success('删除成功')
          this.refreshTable()
        }).catch(e => {
          this.$message.error('删除失败')
        })
      }).catch(() => { })
    }
  }
}
</script>
<style scoped>
.table-tab {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tab {
  list-style-type: none;
  display: flex;
  margin-bottom: 12px;
  margin-top: 0;
  padding-left: 0;
}

.tab li {
  line-height: 24px;
  padding: 6px 14px;
  font-size: 14px;
  color: #333333;
  border: 1px solid #EEEEEE;
  cursor: pointer;
}

.tab li:nth-child(1) {
  border-radius: 8px 0 0 8px;
}

.tab li:nth-child(7) {
  border-radius: 0 8px 8px 0;
}

.tab li.active {
  border-color: #3A7BFA;
  color: #3A7BFA;
}

.box-card>>>.el-radio__inner {
  border-radius: 2px !important;
}

.box-card>>>.el-radio__input.is-checked .el-radio__inner::after {
  content: '';
  width: 8px;
  height: 3px;
  border: 1px solid white;
  border-top: transparent;
  border-right: transparent;
  text-align: center;
  display: block;
  position: absolute;
  top: 3px;
  left: 2px;
  transform: rotate(-45deg);
  border-radius: 0px;
  background: none;
}

>>>.el-radio__label {
  color: #000 !important;
}

.el-dialog__body>>>.el-radio__label {
  font-size: 8px;
}

.el-dialog__body>>>.el-radio__input.is-checked .el-radio__inner::after {
  content: '';
  width: 4px;
  height: 3px;
  border: 1px solid #000;
  border-top: transparent;
  border-right: transparent;
  text-align: center;
  display: block;
  position: absolute;
  top: 1px;
  left: 2px;
  transform: rotate(-45deg);
  border-radius: 0px;
  background: none;
}

>>>.el-radio__input.is-disabled.is-checked .el-radio__inner {
  background: #3A7BFA;
}

.el-dialog__body>>>.el-radio__input.is-disabled.is-checked .el-radio__inner {
  background: transparent;
}

.el-dialog__body>>>.el-radio__inner {
  width: 8px !important;
  height: 8px !important;
}

.el-dialog__body>>>.el-radio__label {
  padding-left: 2px !important;
}

.el-dialog__body>>>.el-card__body {
  padding: 0 !important;
}

.el-dialog__body>>>.el-card {
  border: none;
}

.el-dialog__body>>>.el-radio__input.is-disabled .el-radio__inner {
  border-color: #000 !important;
}

.el-dialog__body>>>.el-radio__input.is-disabled.is-checked .el-radio__inner {
  border: none !important;
}

.scor {
  width: 0.01cm;
  height: 0.01cm;
  border-radius: 1px;
  border: 1px solid #000;
  display: inline-block;
}

.ellipsis-multiline {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  word-wrap: break-word;
  max-height: 3.0em;
  /* 高度为字体大小的两倍 */
  line-height: 1.5em;
  /* 行高 */
  height: 3.0em;
  /* 高度为行高的两倍 */
}
</style>
