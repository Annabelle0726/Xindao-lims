<template>
  <div class="app-container">
    <div v-show="!cableConfigShow&&!auxiliaryShow">
      <div style="display: flex;justify-content: space-between;align-items:center;">
        <div>委托单信息</div>
        <div>
          <el-cascader
            v-model="addObj.quarterItemId"
            :disabled="active>1"
            :options="quarterItemOptions"
            size="small"
            style="width: 380px"
            @focus="getQuarterOnOrderList"></el-cascader>
          <el-select v-show="active==1" v-model="template" placeholder="下单模板" size="small"
                     @change="selectInsOrderTemplateById">
            <el-option v-for="(a, ai) in templates" :key="ai" :label="a.name" :value="a.id">
              <span style="float: left">{{ a.name }}</span>
              <i class="el-icon-delete" style="float: right; color: #66b1ff; font-size: 16px"
                 @click.stop="handleDelete(a)"></i>
            </el-option>
          </el-select>
          <el-button v-show="active==1" size="small" @click="templateDia=true">
            <span style="color: #3A7BFA;">保存模板</span>
          </el-button>
          <el-button v-if="active==1&&addObj.sampleType!=undefined&&(addObj.sampleType.indexOf('电缆')>-1 ||addObj.sampleType.indexOf('综合')>-1)" size="small" type="primary" @click="openCableConfig">电缆配置</el-button>
          <el-button v-if="active==1&&addObj.sampleType!=undefined&&addObj.sampleType.indexOf('电缆')>-1" size="small" type="primary" @click="openAuxiliaryCore">辅助线芯配置</el-button>
          <el-button v-show="active==1||(tabIndex==4&&active==2)" :loading="saveLoad" size="small" type="primary" @click="save">提交</el-button>
          <!-- 审核 -->
          <el-button v-show="active==3" :disabled="saveLoad" :loading="saveLoad" size="small"
                     @click="upInsOrderOfState(2)">不通过</el-button>
          <el-button v-show="active==3" :loading="saveLoad" size="small" type="primary"
                     @click="upInsOrderOfState(1)">通过</el-button>
          <el-button size="small" @click="goBack">
            <span style="color: #3A7BFA;">返回</span>
          </el-button>
        </div>
      </div>
    </div>
    <div v-show="!cableConfigShow&&!auxiliaryShow" style="margin-top: 10px">
      <div class="search">
        <el-form ref="addObj" :inline="true" :model="addObj" :rules="addObjRules" label-width="108px" label-position="right">
          <el-row>
            <el-col :span="6">
              <el-form-item label="委托编号:" prop="entrustCode">
                <el-input v-model="addObj.entrustCode" clearable disabled placeholder="系统生成" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="检验类别:" prop="orderType">
                <el-select v-model="addObj.orderType" :disabled="active>1&&tabIndex!=4" clearable size="small" style="width: 100%;">
                  <el-option v-for="(a, ai) in dict.type.check_type1" :key="ai" :label="a.label" :value="a.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="制单人:" prop="custom">
                <el-input v-model="addObj.custom" disabled size="small" clearable></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="委托单位:" prop="company">
                <el-input v-model="addObj.company" disabled placeholder="选择委托单位" size="small" style="width: 208px">
                  <template slot="append">
                    <el-button slot="append" :disabled="active>1&&tabIndex!=4" icon="el-icon-search"
                               @click="openCompanyList"></el-button>
                  </template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="联系方式:" prop="phone">
                <el-input v-model="addObj.phone" :disabled="active>1&&tabIndex!=4" clearable placeholder="选择委托客户" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="紧急程度:" prop="type">
                <el-select v-model="addObj.type" :disabled="active>1&&tabIndex!=4" clearable size="small">
                  <el-option v-for="(a, ai) in dict.type.urgency_level" :key="ai" :label="a.label" :value="a.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="样品名称:" prop="sample">
                <el-input v-model="addObj.sample" disabled size="small" style="width: 208px">
                  <template slot="append"><el-button slot="append" :disabled="active>1&&tabIndex!=4" icon="el-icon-search"
                                                     @click="selectStandardTree = true"></el-button></template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="样品数量:" prop="sampleNum" style="margin-right: 0">
                <el-input-number v-model="addObj.sampleNum" :disabled="active>1" :max="100" :min="1" :precision="0"
                                 size="small" @change="addStandardTree"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="样品状态:" prop="sampleStatus">
                <el-select v-model="addObj.sampleStatus" :disabled="active>1&&tabIndex!=4" size="small">
                  <el-option v-for="(a,ai) in dict.type.sample_status_list" :key="ai" :label="a.label" :value="a.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="抽检数量:" prop="testQuantity">
                <el-input v-model="addObj.testQuantity" :disabled="active>1&&tabIndex!=4" clearable size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="来样方式:" prop="formType">
                <el-select v-model="addObj.formType" :disabled="active>1&&tabIndex!=4" size="small" clearable>
                  <el-option v-for="(a,ai) in dict.type.form_type" :key="ai" :label="a.label" :value="a.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="报告发送方式:" prop="send">
                <el-radio-group v-model="addObj.send" :disabled="active>1&&tabIndex!=4" size="mini">
                  <el-radio :label="1" border style="margin-right: 0">自取</el-radio>
                  <el-radio :label="0" border>其他</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="样品处理方式:" prop="processing">
                <el-radio-group v-model="addObj.processing" :disabled="active>1&&tabIndex!=4" size="mini" style="display: flex; flex-direction: column;">
                  <el-radio :label="0" border style="margin-right: 0">委托单位取回</el-radio>
                  <el-radio :label="1" border>实验室处理</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="生产单位:" prop="production">
                <el-input v-model="addObj.production" :disabled="active>1&&tabIndex!=4" clearable placeholder="请输入"
                          size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="生产单位EN:" prop="productionEn">
                <el-input v-model="addObj.productionEn" :disabled="active>1&&tabIndex!=4" clearable placeholder="请输入"
                          size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="是否留样:" prop="isLeave">
                <el-radio-group v-model="addObj.isLeave" border :disabled="active>1&&tabIndex!=4" size="mini">
                  <el-radio :label="0" border style="margin-right: 0">不留样</el-radio>
                  <el-radio :label="1" border>留样</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="委托人:" prop="prepareUser">
                <el-input v-model="addObj.prepareUser" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="委托人英文:" prop="prepareUserEn">
                <el-input v-model="addObj.prepareUserEn" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="备注:" prop="remark">
                <el-input v-model="addObj.remark" :autosize="{ minRows: 2, maxRows: 2}" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable
                          size="small" style="width: 100%" type="textarea"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="备注英文:" prop="remarkEn">
                <el-input v-model="addObj.remarkEn" :autosize="{ minRows: 2, maxRows: 2}" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable
                          size="small" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="样品名称:" prop="sampleView">
                <el-input v-model="addObj.sampleView" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable
                          size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="样品名称英文:" prop="sampleViewEn">
                <el-input v-model="addObj.sampleViewEn" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable
                          size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="委托人工号:" prop="prepareCode">
                <el-input v-model="addObj.prepareCode" :disabled="active>1&&tabIndex!=4" :placeholder="active>1 ? '' : '请输入'" clearable
                          size="small"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div>
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
          <div style="display: flex;align-items: center;margin-bottom: 6px;margin-top: 6px">
            <span style="width: 150px;font-size: 14px;text-align: right;">特殊标准：</span>
            <el-input v-model="specialStandardMethod" :disabled="!isSpecial" clearable size="small"></el-input>
            <el-button v-show="active==1" size="small" style="margin-left: 10px"
                       type="primary" @click="editSpecial">编辑</el-button>
          </div>
        </div>
        <el-table ref="sampleTable" :data="sampleList" border class="el-table sampleTable" highlight-current-row
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
          max-height="400px" tooltip-effect="dark" @selection-change="selectSample" @row-click="rowClick">
          <el-table-column v-if="active==1" :selectable="selectable" type="selection" width="60"></el-table-column>
          <el-table-column align="center" label="序号" type="index" width="65"></el-table-column>
          <el-table-column align="center" label="样品名称" min-width="100" prop="sample">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sample" :disabled="active>1" clearable size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="样品编号" min-width="100" prop="sampleCode">
            <template slot-scope="scope">
              <el-input v-model="scope.row.sampleCode" :disabled="active>1" clearable placeholder="不填写则系统自动生成"
                size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="样品型号" min-width="60" prop="model">
            <template slot-scope="scope">
              <el-select v-model="scope.row.model" :disabled="active>1" allow-create default-first-option filterable
                placeholder="样品型号" size="small" style="width: 100%;" @change="handleChangeModel">
                <el-option v-for="item in models" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="芯数" prop="cores" width="130">
            <template slot-scope="scope">
              <el-input v-model="scope.row.cores" clearable placeholder="非必填" size="small"
                        @input="spliceString(scope.row, scope.row.standardMethodListId, 'cores')"></el-input>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="型号参数" prop="modelNum" width="110">
            <template slot-scope="scope">
              <el-input v-model="scope.row.modelNum" clearable placeholder="非必填" size="small"
                @input="methodChange(scope.row.standardMethodListId, scope.row, 'splice')"></el-input>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="最终样品型号" prop="endModels" width="160">
            <template slot-scope="scope">
              <el-input v-model="scope.row.endModels" clearable placeholder="非必填" size="small"></el-input>
            </template>
          </el-table-column>
          <el-table-column align="center" label="检验标准" min-width="100" prop="standardMethodListId">
            <template slot-scope="scope">
              <el-select v-model="scope.row.standardMethodListId" :disabled="scope.row.model==null||active>1"
                :loading="methodLoad" :readonly="active>1" clearable placeholder="检验标准"
                size="small" style="width: 100%;" @change="(value)=>methodChange(value, scope.row)" @clear="productList = []" @focus="methodFocus">
                <el-option v-for="item in methods" :key="item.id" :label="item.code" :value="item.id">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="导体材质" prop="conductorMaterial" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.conductorMaterial" clearable placeholder="非必填" size="small"
                        @input="spliceString(scope.row, scope.row.standardMethodListId)"></el-input>
            </template>
          </el-table-column>
          <el-table-column v-if="!(active>1)" align="center" label="导体类型" prop="conductorType" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.conductorType" clearable placeholder="非必填" size="small"
                        @input="spliceString(scope.row, scope.row.standardMethodListId)"></el-input>
            </template>
          </el-table-column>
        </el-table>
        <el-table ref="productTable" v-loading="getProductLoad" :data="productList"
                  :row-class-name="tableRowClassName" border class="el-table"
                  :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }"
          max-height="400px" style="margin-bottom: 10px;" tooltip-effect="dark"
          @select="selectOne" @selection-change="selectProduct" @select-all="handleAll">
          <el-table-column v-if="active==1" :selectable="selectable0" type="selection" width="65"></el-table-column>
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
              <el-input v-if="(active==1||tabIndex==4)&&(isAskOnlyRead || isSpecial)&&scope.row.inspectionValueType!='5'" v-model="scope.row.ask" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求值"
                        size="small" type="textarea"
                        @change="e=>requestChange(e,scope.row,'ask')"></el-input>
              <span v-else>
                <template >{{ scope.row.ask }}</template>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="要求描述" min-width="220px" prop="tell">
            <template slot-scope="scope">
              <el-input v-if="(active==1||tabIndex==4)&&(isAskOnlyRead || isSpecial)" v-model="scope.row.tell" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="要求描述"
                size="small" type="textarea"
                @change="e=>requestChange(e,scope.row,'tell')"></el-input>
              <span v-else>
                <template >{{ scope.row.tell }}</template>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="条件" min-width="140" prop="radius" show-overflow-tooltip>
            <template slot-scope="scope">
              <el-input v-if="(active==1||tabIndex==4)&&(isAskOnlyRead || isSpecial)" v-model="scope.row.radius" :autosize="{ minRows: 1, maxRows: 3}" clearable placeholder="条件"
                        size="small" type="textarea"
                        @change="e=>requestChange(e,scope.row,'radius')">
              </el-input>
              <span v-else>{{scope.row.radius}}</span>
            </template>
          </el-table-column>
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
          <el-table-column label="区间" min-width="120" prop="section" show-overflow-tooltip></el-table-column>
          <el-table-column :filter-method="filterHandler" :filters="filters" label="子实验室" min-width="130" prop="sonLaboratory"
            show-overflow-tooltip></el-table-column>
            <el-table-column v-if="isSpecial&&active==1" label="操作" width="100">
            <template slot-scope="scope">
              <el-button v-if="!scope.row.repetitionTag" size="small" type="text" @click="addProductList(productList,scope.row,scope.$index)">插入行</el-button>
              <el-button v-if="!!scope.row.repetitionTag&&scope.row.delete" size="small" type="text" @click="deleteProductList(scope.$index,productList)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog :visible.sync="selectUserDia" title="选择单位" width="70%">
      <div v-if="selectUserDia" class="body">
        <lims-table :tableData="tableData1" :column="column1"
                    :isSelection="true" :handleSelectionChange="selectMethod"
                    @pagination="pagination1" height="400px" key="tableData1"
                    :page="page1" :tableLoading="tableLoading1"></lims-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="selectUserDia = false">取 消</el-button>
        <el-button type="primary" @click="selectUser">确 定</el-button>
      </span>
    </el-dialog>
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
    <el-dialog :visible.sync="templateDia" title="保存模板" width="400px">
      <div v-if="templateDia" class="body" style="display: flex;align-items: center;">
        <div class="search_label" style="width: 90px;"><span class="required-span">* </span>模板名称：</div>
        <div class="search_input">
          <el-input v-model="templateName" clearable size="small"></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="templateDia = false">取 消</el-button>
        <el-button :loading="templateLoading" type="primary" @click="addTemplateDia">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" :visible.sync="issuedDialogVisible" title="检验下发" width="400px">
      <div class="body" style="max-height: 60vh;">
        <el-row>
          <el-col :span="22" class="search_thing">
            <div class="search_label"><span class="required-span">* </span>约定时间：</div>
            <div class="search_input">
              <el-date-picker v-model="distributeData.appointed" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                style="width: 100%;" type="date" value-format="yyyy-MM-dd">
              </el-date-picker>
            </div>
          </el-col>
          <el-col :span="22" class="search_thing">
            <div class="search_label"><span class="required-span">* </span>指派人员：</div>
            <div class="search_input">
              <el-select v-model="distributeData.userId" filterable placeholder="请选择" size="small" style="width: 100%;" @change="changeUser">
                <el-option v-for="item in personList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </el-col>
          <el-col v-if="distributeData.userId" :span="22" class="search_thing">
            <div class="search_label"><span class="required-span">* </span>试验室：</div>
            <div class="search_input">
              <el-select v-model="distributeData.sonLaboratory" filterable placeholder="请选择" size="small" style="width: 100%;">
                <el-option v-for="item in sonLaboratoryList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </div>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="issuedDialogVisible=false;closeOpenPage">取 消</el-button>
          <el-button :loading="upLoad" type="primary" @click="submitForm2">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="noDialogVisible" title="不通过原因" width="400px">
      <div class="body" style="max-height: 60vh;">
        <el-row>
          <el-col :span="22" class="search_thing">
            <div class="search_label"><span class="required-span">* </span>不通过原因：</div>
            <div class="search_input">
              <el-input v-model="tell" placeholder="请输入不通过原因" size="small"></el-input>
            </div>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="noDialogVisible=false,tell=''">取 消</el-button>
          <el-button :loading="noLoading" type="primary" @click="submitTell">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <!--电缆配置-->
    <cableConfig v-if="cableConfigShow" ref="cableConfigShow" :active="active" :isSpecial="isSpecial" :sampleSelectionList="sampleSelectionList" @goBackAdd="goBackAdd"/>
    <!--辅助线芯配置-->
    <auxiliary-wire-core v-if="auxiliaryShow" :active="active" :isSpecial="isSpecial" :sampleSelectionList="sampleSelectionList" @goBackAdd2="goBackAdd2"></auxiliary-wire-core>
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
    <!--区间填写-->
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
  </div>
</template>

<script>
import cableConfig from './cable-config.vue'
import AuxiliaryWireCore from "./auxiliaryWireCore.vue";
import {
  getInsOrder,
  getUserNow, selectInsOrderTemplate,
  selectStandardMethodEnum, selectStandardProductList,
  selectStandardTreeList2
} from "@/api/business/rawMaterialOrder";
import {
  addInsOrder, addInsOrderTemplate, delInsOrderTemplate,
  getQuarterOnOrder, selectInsOrderTemplateById,
  selectOrderManDay,
  updateInsOrder,
  upInsOrder,
  upInsOrderOfState
} from "@/api/business/add";
import {selectUserCondition} from "@/api/performance/class";
import {selectsStandardMethodByFLSSM} from "@/api/standard/standardLibrary";
import limsTable from "@/components/Table/lims-table.vue";
import {selectCustomPageList} from "@/api/system/customer";
import {mapGetters} from "vuex";

export default {
  name: 'AddOrder',
  components: {
    limsTable,
    cableConfig,
    AuxiliaryWireCore
  },
  computed:{
    ...mapGetters(["nickName", 'nameEn', 'userName']),
  },
  dicts: ['check_type1', 'urgency_level', 'form_type', 'sample_status_list'],
  data() {
    return {
      active: '',
      tabIndex: '',
      currentId: '',
      sampleSelectionList: [],//样品表格选中数据
      editTable:[],
      template: null,
      templates: [],
      addObj: {
        entrustCode: null,
        custom: null,
        company: null,
        userId: null,
        type: '0',
        code: null,
        appointed: null,
        remark: null, // 备注
        remarkEn: null, // 备注英文
        sample: null,
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
        orderType: null,
        send: 1,
        formType: '送检', //  来样方式
        sampleStatus: 'In good condition', // 样品状态
        testQuantity: '', // 样品状态
        production: null,
        productionEn: null,
        companyId: null,
        prepareUser:null, // 委托人
        prepareUserEn:null, // 委托人英文
        prepareCode: '', // 委托人工号
        quarterItemId: '',
        sampleView: '', // 样品名称（报告展示字段）
        sampleViewEn: '', // 样品名称英文（报告展示字段）
      },
      addObjRules: { // 表单校验规则
        orderType: [
          { required: true, message: '请选择检验类别', trigger: 'change' }
        ],
        company: [
          { required: true, message: '请选择委托单位', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请填写联系方式', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择紧急程度', trigger: 'change' }
        ],
        sample: [
          { required: true, message: '请选择样品名称', trigger: 'change' }
        ],
        sampleNum: [
          { required: true, message: '请填写样品数量', trigger: 'blur' }
        ],
        sampleStatus: [
          { required: true, message: '请选择样品状态', trigger: 'change' }
        ],
        formType: [
          { required: true, message: '请选择来样方式', trigger: 'change' }
        ],
        testQuantity: [
          { required: true, message: '请填写抽检数量', trigger: 'blur' }
        ],
        production: [
          { required: true, message: '请填写生产单位', trigger: 'blur' }
        ],
        productionEn: [
          { required: true, message: '请填写生产单位EN', trigger: 'blur' }
        ],
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
      type: [],
      selectUserDia: false,
      tableData1: [],
      tableLoading1: false,
      multipleSelection: [],
      column1: [
        {label: '客户名称', prop: 'company'},
        {label: '客户单位EN', prop: 'companyEn'},
        {label: '单位地址', prop: 'address'},
        {label: '单位地址EN', prop: 'addressEn'},
        {label: '单位电话', prop: 'phone'},
        {label: '加急额度', prop: 'num'},
        {label: '客户编号', prop: 'code2'},
        {label: '工厂域', prop: 'code'}
      ],
      page1: {
        total:0,
        size:10,
        current:1
      },
      selectStandardTree: false,
      search: null,
      list: [],
      selectStandardTreeLoading: false,
      selectTree: null,
      sampleViewEn: null,
      expandedKeys: [],
      sampleList: [],
      upIndex: 0,
      sampleIds: [],
      methodList: [],
      addSampleDia: false,
      count: 1,
      productList: [],
      productList0: [],
      bsm1DiaList: [],
      productIds: [],
      getProductLoad: false,
      saveLoad: false,
      templateDia: false,
      templateLoading: false,
      templateName: '',
      issuedDialogVisible: false,
      distributeData: {
        appointed: '',
        userId: '',
        sonLaboratory:''
      },
      personList: [],
      upLoad: false,
      models: [],
      methods: [],
      methodLoad: false,
      noDialogVisible: false,
      tell: '',
      noLoading: false,
      orderType: [],
      filters: [],
      formType: [],
      currentMethod: null,
      isAskOnlyRead: false,
      sampleId: null,
      bsmRow: null,
      bsm1: false,
      bsm1Val: null,
      bsm1DiaAll: false,
      cableConfigShow: false,
      auxiliaryShow: false,
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
      temperatureEngList: [],
      isShowInput: false,
      temId: '',
      sonLaboratoryList:[],
      selectiveEcho: [], // 检验下单的时候勾选检验项目,如果使用筛选提交显示检验项目为空 回显列表
      quarterItemOptions: [], // 查询季度信息
      specialStandardMethod: '',
      isSpecial: false,
    }
  },
  watch: {
    // sampleList() {
    //   this.addObj.method = null
    //   this.productList = []
    // },
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
    },
    tabIndex(val){
      if(val==4&&this.active==2){
        this.isSpecial = true
      }else{
        this.isSpecial = false
      }
    }
  },
  mounted() {
    this.active = this.$route.query.active
    this.tabIndex = this.$route.query.tabIndex
    this.currentId = this.$route.query.currentId
    this.getUserNowData()
    this.getInfo()
    if(this.tabIndex==4&&this.active==2){
      this.isSpecial = true
    }else{
      this.isSpecial = false
    }
  },
  activated() {
    this.active = this.$route.query.active
    this.tabIndex = this.$route.query.tabIndex
    this.currentId = this.$route.query.currentId
    this.getInfo();
  },
  methods: {
    getInfo() {
      this.selectStandardTreeList()
      this.getAuthorizedPerson();
      this.selectStandardMethods()
      if (this.active != 1) {
        // 查看/审核流程
        // 请求接口，回显数据
        getInsOrder({
          orderId: this.currentId
        }).then(res => {
          this.addObj = {
            ...res.data.insOrder
          };
          this.addObj.type = String(this.addObj.type)
          this.sampleList = this.HaveJson(res.data.sampleProduct);
          this.specialStandardMethod = this.sampleList[0].specialStandardMethod
          this.getProNum()
          this.addObj.sampleNum = this.sampleList.length
          this.$nextTick(() => {
            this.$refs.sampleTable.doLayout()
            if (this.addObj.sampleNum > 0) {
              this.$refs.sampleTable.setCurrentRow(this.sampleList[0], true)
              this.rowClick(this.sampleList[0])
            }
          })
        })
      }

    },
      // 编辑要求值表格
      editSpecial () {
        this.isSpecial = true
        this.$nextTick(() => {
          this.$refs.productTable.doLayout();
        });
      },
      getQuarterOnOrderList () {
        getQuarterOnOrder().then(res => {
          if (res.code === 200) {
            this.quarterItemOptions = res.data
          }
        })
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
          let obj = this.productList.find(m => {
            if(a.repetitionTag){
              return m.id === a.id&&m.repetitionTag == a.repetitionTag
            }else{
              return m.id === a.id&&!m.repetitionTag
            }
          })

          if(obj){
            a.state = obj.state
            a.section = obj.section
            a.ask = obj.ask
            a.manHour = obj.manHour
            a.price = obj.price
            a.tell = obj.tell
            a.radius = obj.radius
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
      // 获取用户列表
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
      openCompanyList () {
        this.selectUserDia = true
        this.getCompanyList()
      },
      getCompanyList () {
        selectCustomPageList({...this.page1}).then(res => {
          this.tableLoading1 = false
          if (res.code === 200) {
            this.tableData1 = res.data.records
            this.page1.total = res.data.total
          }
        }).catch(err => {
          this.tableLoading1 = false
        })
      },
      pagination1 (page) {
        this.page1.size = page.limit
        this.getCompanyList()
      },
      selectMethod (row) {
        this.multipleSelection = row
      },
      selectUser() {
        if (this.multipleSelection.length === 0) {
          this.$message.error('未选择数据')
          return
        }
        this.addObj.company = this.multipleSelection[0].company
        this.addObj.code = this.multipleSelection[0].code
        this.addObj.companyId = this.multipleSelection[0].id
        this.selectUserDia = false
        if(this.active==1){
          // TODO
          this.selectInsOrderTemplate()
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
      replaceAll(str,find,value) {
        if (str === undefined) {
          return str
        }
        return str.replaceAll(find, value);
      },
      save() {
        this.$refs['addObj'].validate((valid) => {
          if (valid) {
            this.sampleList.forEach(item => {
              item.specialStandardMethod = this.specialStandardMethod
            })
            try {
              this.sampleList.forEach(item => {
                if (item.insulating){
                  let sampleListArr = []
                  let insulatingArr = []
                  // 有电缆配置时，比较电缆配置的检验项是否与外面重复
                  item.insProduct.filter(a => a.state == 1).forEach(obj => {
                    sampleListArr.push(obj.id)
                  })
                  item.insulating.insProduct.filter(a => a.state == 1).forEach(obj => {
                    insulatingArr.push(obj.id)
                  })
                  const sameLength = sampleListArr.filter(id => insulatingArr.indexOf(id) > -1).length
                  if (sameLength > 0) {
                    this.$message.error('检验项与电缆配置检验项不能重复')
                  }
                  throw sameLength > 0
                }
              })
            } catch (e) {
              console.log('e---', e)
              if (e === true) throw e
            }
            let sampleList = this.HaveJson(this.sampleList)
            sampleList.forEach(a => {
              if (a.insProduct.length > 0) {
                a.insProduct.forEach(c => {
                  if (this.tabIndex != 4) {
                    delete c.id
                  }
                })
              }
              if (a.endModels) {
                a.model = a.endModels
              }
              a.insProduct = a.insProduct.filter(b=>b.state === 1)
            })
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
          }
        })
      },
      save0(){
        if(this.editTable.every(m=>m.value)){
          let sampleList = this.handleData(this.HaveJson(this.sampleList),this.handleAsk,1)
          sampleList.forEach(a => {
            if (a.insProduct.length > 0) {
              a.insProduct.forEach(c => {
                if (this.tabIndex != 4) {
                  delete c.id
                }
              })
            }
            if (a.endModels) {
              a.model = a.endModels
            }
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
        if (this.addObj.quarterItemId) {
          this.addObj.quarterItemId = this.addObj.quarterItemId[1]
        }
        if(this.tabIndex==4&&this.active==2){
          if (this.addObj.createTime) {
            delete this.addObj.createTime
          }
          // 退回后提交
          updateInsOrder({insOrder: this.addObj, sampleProduct: sampleList}).then(res => {
            this.saveLoad = false
            this.$message.success('已提交')
            this.bsm3Dia = false;
            this.closeOpenPage()
          }).catch(e=>{
            this.saveLoad = false
          })
        }else{
          // 常规提交
          addInsOrder({insOrder: this.addObj, sampleList: sampleList}).then(res => {
            this.saveLoad = false
            this.$message.success('已提交')
            this.bsm3Dia = false;
            this.closeOpenPage()
          }).catch(e=>{
            this.saveLoad = false
          })
        }

      },
      upInsOrderOfState(state) {
        if (state == 1) {
          this.saveLoad = true
          upInsOrderOfState({
            state,
            id: this.currentId,
            companyId:this.addObj.companyId,
            laboratory:this.addObj.laboratory,
            company: this.addObj.company
          }).then(res => {
            this.saveLoad = false
            this.$message.success('提交成功')
            selectOrderManDay({
              id: this.currentId
            }).then(ress => {
              this.distributeData.orderId = this.currentId
              this.distributeData.appointed = ress.data
            })
            setTimeout(() => {
              this.issuedDialogVisible = true;
            }, 1000)
          })
        } else {
          // 不通过
          this.noDialogVisible = true
        }
      },
      // 下发
      submitForm2() {
        if (this.distributeData.appointed == null || this.distributeData.appointed == '') {
          this.$message.error('约定时间未填写')
          return
        }
        if (this.distributeData.userId == null || this.distributeData.userId == '') {
          this.$message.error('指派人员未填写')
          return
        }
        if (this.distributeData.userId&&(this.distributeData.sonLaboratory== null ||this.distributeData.sonLaboratory== '')) {
          this.$message.error('试验室未填写')
          return
        }
        this.upLoad = true;
        upInsOrder({
          orderId: this.distributeData.orderId,
          appointed: this.distributeData.appointed,
          userId: this.distributeData.userId,
          sonLaboratory:this.distributeData.sonLaboratory
        }).then(res => {
          this.$message.success('提交成功')
          this.upLoad = false
          this.issuedDialogVisible = false
          this.closeOpenPage()
        }).catch(e => {
          this.$message.error('提交失败')
          this.upLoad = false
        })
      },
      getUserNowData() {
      this.addObj.prepareUser = this.nickName
      this.addObj.prepareUserEn = this.nameEn
      this.addObj.prepareCode = this.userName
        getUserNow().then(res => {
          let selects = res.data
          if (selects == null) return
          this.addObj.userId = selects.id
          this.addObj.company = selects.company
          this.addObj.custom = selects.name
          this.addObj.code = selects.code
          this.addObj.phone = selects.phone
          this.addObj.companyId = selects.departId
          this.addObj.production = '中天科技装备电缆有限公司'
          this.addObj.productionEn = 'Zhongtian Technology Industrial Wire&Cable System CO.,LTD'
          if(this.active==1){
            this.selectInsOrderTemplate()
          }
        })
      },
      getProNum() {
        this.sampleSelectionList.forEach((m, i) => {
          this.$set(this.sampleSelectionList[i], 'proNum', m.insProduct.filter(a => a.state == 1).length)
        })
        this.$refs.sampleTable.doLayout()
      },
      searchFilter() {
        this.$refs.tree.filter(this.search)
      },
      nodeOpen(data, node, el) {
        $($(el.$el).find('.node_i')[0]).attr('class', 'node_i el-icon-folder-opened')
        if (node.data.code === '[3]') {
          this.sampleViewEn = data.sampleTypeEn
        } else if (node.data.code === '[4]') {
          this.sampleViewEn = data.sampleEn
        }
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
        if (node.data.code === '[3]') {
          this.sampleViewEn = val.sampleTypeEn
        } else if (node.data.code === '[4]') {
          if (node.data.children!==null && node.data.children.length>0) {
            this.sampleViewEn = val.sampleEn
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
        this.addObj.sampleView = this.addObj.sample
        this.addObj.sampleViewEn = this.sampleViewEn
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
      selectsStandardMethodByFLSSMList() {
        this.methodLoad = true
        selectsStandardMethodByFLSSM({
          tree: this.selectTree
        }).then(res => {
          this.methodLoad = false
          try {
            if (res.data.standardMethodList.length == 0 && this.selectTree.split('-').length == 5) {
              let arr = this.selectTree.split('-')
              let arr0 = arr.slice(0, arr.length - 1)
              let selectTree = arr0.join('-').substring(0, arr0.join('-').length - 1)
              selectsStandardMethodByFLSSM({
                tree: selectTree
              }).then(ress => {
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
        this.productList = row.insProduct
        this.productList0 = JSON.parse(JSON.stringify(this.productList))
        setTimeout(() => {
          this.productList.forEach(a => {
            if (a.state == 1) {
              this.toggleSelection(a)
            }else{
              this.$refs.productTable.toggleRowSelection(a, false);
            }
          })
        }, 200)
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
            let arr4 = JSON.parse(row.tell)
            let index = arr0.indexOf(sectionCopy)
            row.sectionCopy = sectionCopy
            row.ask = arr1[index]
            row.tell = arr4[index]
          } catch(e) {}
        }
        this.sampleList.forEach(item => {
          if (this.sampleIds.indexOf(item.id) > -1||this.currentMethod.id==item.id) {
            item.insProduct.forEach(m => {
              if (m.id == row.id) {
                m.state = row.state;
              }
            })
          }
        })
        this.productList.forEach(item => {
          if (item.id == row.id) {
            item.state = row.state;
          }
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
        if (row.state == 1) {
          return 'warning-row';
        } else {
          return '';
        }
      },
      selectInsOrderTemplate() {
        selectInsOrderTemplate({company: this.addObj.company}).then(res => {
          this.templates = res.data
        })
      },
      // 删除模板--调用接口
      handleDelete(row) {
        this.$confirm('是否删除当前数据?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          delInsOrderTemplate({
            id: row.id
          }).then(res => {
            this.$message.success('删除成功')
            this.selectInsOrderTemplate()
          }).catch(e => {
            this.$message.error('删除失败')
          })
        }).catch(() => {})
      },
      // 保存模板
      addTemplateDia() {
        if (this.templateName) {
          this.templateLoading = true;
          addInsOrderTemplate({
            name: this.templateName,
            thing: JSON.stringify({
              addObj: this.addObj,
              sampleList: this.sampleList,
              selectTree:this.selectTree
            })
          }).then(res => {
            this.templateLoading = false;
            this.templateDia = false;
            this.$message.success('保存成功')
            this.selectInsOrderTemplate()
            this.templateName = ''
          }).catch(e => {
            this.templateLoading = false;
          })
        } else {
          this.$message.error('请填写模板名称')
        }
      },
      // 查询模板
      selectInsOrderTemplateById(e) {
        selectInsOrderTemplateById({id: e}).then(res => {
          let obj = JSON.parse(res.data)
          //制单人设置为当前登录用户
          let user = this.nickName
          obj.addObj.custom = user.name
          obj.addObj.userId = user.userId
          this.addObj = obj.addObj;
          this.sampleList = obj.sampleList;
          this.selectTree = obj.selectTree
          this.rowClick(this.sampleList[0])
        })
      },
      delSampleAndProduct() {
        this.sampleList.splice(scope.$index, 1)
        this.productList = []
      },
      spliceString (row, val, type) {
        if (type === 'cores') {
          const index = this.sampleList.findIndex(item => item.id === row.id)
          if (index > -1) {
            if (this.sampleList[index].modelNum) {
              this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ' ' + this.sampleList[index].cores + '*' + this.sampleList[index].modelNum)
            } else {
              this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ' ' + this.sampleList[index].cores)
            }
            if (this.sampleList[index].modelNum && !this.sampleList[index].cores) {
              this.$set(this.sampleList[index], 'endModels',this.sampleList[index].model + ' ' + this.sampleList[index].modelNum)
            }
          }
        }
        this.getProductLoad = true
        let selectTreeList = this.selectTree.split(" - ")
        this.addObj.model&&(selectTreeList[selectTreeList.length - 1] = this.addObj.model)
        selectStandardProductList({
          model: this.addObj.model?this.addObj.model:row.model,
          modelNum: row.modelNum,
          standardMethodListId: val,
          factory: selectTreeList.join(" - "),
          cores: row.cores,
          conductorMaterial: row.conductorMaterial,
          conductorType: row.conductorType,
        }).then(res => {
          res.data.forEach(a => {
            a.state = 0
          })
          row.insProduct = this.HaveJson(res.data)
          this.getProductLoad = false
          this.productList = row.insProduct
          this.productList0 = JSON.parse(JSON.stringify(this.productList))
          this.$refs.sampleTable.setCurrentRow(row)
          this.$nextTick(() => {
            this.$refs.productTable.doLayout();
            this.upIndex++
          });
          setTimeout(() => {
            this.productList.forEach(a => {
              if (a.state == 1) this.toggleSelection(a)
            })
          }, 200)
        })
      },
      methodChange(val, row, type) {
        if (type === 'splice') {
          const index = this.sampleList.findIndex(item => item.id === row.id)
          if (index > -1) {
            if (this.sampleList[index].cores) {
              this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ' ' + this.sampleList[index].cores + '*' + this.sampleList[index].modelNum)
            } else {
              if (this.sampleList[index].modelNum) {
                const index2 = this.sampleList[index].modelNum.indexOf('×')
                if (index2 === 0) {
                  this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + this.sampleList[index].modelNum)
                } else if (index2 === -1) {
                  this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ' ' + this.sampleList[index].modelNum)
                } else {
                  this.$set(this.sampleList[index], 'endModels', this.sampleList[index].modelNum + this.sampleList[index].model)
                }
              } else {
                this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ((this.sampleList[index].modelNum == null || this.sampleList[index].modelNum == '' || this.sampleList[index].modelNum == 'null') ? '' : (' ' + this.sampleList[index].modelNum)))
              }
            }
            if (!this.sampleList[index].modelNum && this.sampleList[index].cores) {
              this.$set(this.sampleList[index], 'endModels', this.sampleList[index].model + ' ' + this.sampleList[index].cores)
            }
          }
        }
        if (val === null || val === '') return
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
          cores: row.cores,
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
      },
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
        this.selectsStandardMethodByFLSSMList()
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
                if(row.repetitionTag){
                  if(row.repetitionTag==i.repetitionTag){
                    i[type] = e
                  }
                }else{
                  if(!i.repetitionTag){
                    i[type] = e
                  }
                }
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
      selectable0(row,index) {
        if (this.active > 1||row.repetitionTag) {
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
            if (this.sampleIds.indexOf(item.id) > -1||this.currentMethod.id==item.id) {
              item.insProduct.map(m => {
                m.state = 1
                return m;
              })
            }
            return item
          })
        } else {
          this.sampleList.map(item => {
            if (this.sampleIds.indexOf(item.id) > -1||this.currentMethod.id==item.id) {
              item.insProduct.map(m => {
                m.state = 0
                return m;
              })
            }
            return item
          })
        }
        this.productList.forEach(item => {
          if (item.id == row.id) {
            item.state = row.state;
          }
        })
        this.changeProductList0()
        this.currentMethod.insProduct = this.productList0
        this.getProNum()
        this.$nextTick(() => {
          this.$refs.productTable.doLayout()
        })
      },
      submitTell() {
        if (!this.tell) {
          this.$message.error('请输入不通过原因')
          return
        }
        this.noLoading = true;
        upInsOrderOfState({
          state: 2,
          id: this.currentId,
          tell: this.tell
        }).then(res => {
          this.noLoading = false;
          this.tell = '';
          this.$message.success('提交成功')
          this.issuedDialogVisible = false;
          this.closeOpenPage()
        })
      },
      filterHandler(value, row, column) {
        const property = column['property'];
        return row[property] === value;
      },
      areObjectsValuesEqual (objects, property) {
        if (!objects || objects.length === 0) return false;
        const firstValue = objects[0][property];
        return objects.every(obj => obj[property] === firstValue);
      },
      // 辅助线芯配置
      openAuxiliaryCore () {
        if (this.active == 1) {
          if (this.sampleIds.length === 0) {
            this.$message.error("未选择样品")
          } else if (this.sampleIds.length === 1) {
            if (!this.sampleSelectionList[0].standardMethodListId) {
              this.$message.error("样品未选择检验标准")
              return
            }
            this.auxiliaryShow = true
          } else {
            // 同时配置多个样品的电缆配置时必须选择相同的检验标准
            if (!this.sampleSelectionList.every(value => value.standardMethodListId)) {
              this.$message.error("样品未选择检验标准")
            } else {
              if (!this.areObjectsValuesEqual(this.sampleSelectionList, 'standardMethodListId')) {
                this.$message.error("请选择相同的检验标准")
                return
              }
              this.auxiliaryShow = true
            }
          }
        } else {
          if (this.sampleId === null) {
            this.$message.error('未选中样品')
          }
        }
      },
      openCableConfig() {
        if (this.active == 1) {
          if (this.sampleIds.length === 0) {
            this.$message.error("未选择样品")
          } else if (this.sampleIds.length === 1) {
            if (!this.sampleSelectionList[0].standardMethodListId) {
              this.$message.error("样品未选择检验标准")
              return
            }
            this.cableConfigShow = true
          } else {
            // 同时配置多个样品的电缆配置时必须选择相同的检验标准
            if (!this.sampleSelectionList.every(value => value.standardMethodListId)) {
              this.$message.error("样品未选择检验标准")
            } else {
              if (!this.areObjectsValuesEqual(this.sampleSelectionList, 'standardMethodListId')) {
                this.$message.error("请选择相同的检验标准")
                return
              }
              this.cableConfigShow = true
            }
          }
        } else {
          if (this.sampleId === null) {
            this.$message.error('未选中样品')
          }
        }
      },
      goBackAdd () {
        this.cableConfigShow = false
      },
      goBackAdd2 () {
        this.auxiliaryShow = false
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
      getTotal() {
        this.totalArr = []
        // this.total = 0;
        this.sampleList.forEach(item => {
          if (item.insProduct && item.insProduct.length > 0) {
            item.insProduct.forEach(a => {
              this.totalArr.push(a)
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
        let arr1 = this.totalArr.filter(item => item.state == 1)
        let mySet0 = new Set();
        this.sonLaboratoryList = []
        arr1.forEach(item => {
          let num1 = mySet0.size
          mySet0.add(item.sonLaboratory)
          let num2 = mySet0.size
          if(num2>num1){
            this.sonLaboratoryList.push({
              label:item.sonLaboratory,
              value:item.sonLaboratory,
            })
          }
        })
      },
      changeUser(){
        if(this.sonLaboratoryList.length>0){
          this.distributeData.sonLaboratory = this.sonLaboratoryList[0].value
        }
      },
      addProductList(productList,row,index){
        let list = productList.filter(m=>{
          if(m.id==row.id){
            m.delete = false
          }
          return m.id==row.id
        })
        let num = list.length
        let obj = this.HaveJson(row)
        obj.repetitionTag = num
        obj.delete = true
        productList.splice(index+num,0,obj)
        this.addProductList0(row,index)
      },
      addProductList0(row,index){
        let list = this.productList0.filter(m=>{
          if(m.id==row.id){
            m.delete = false
          }
          return m.id==row.id
        })
        let num = list.length
        let obj = this.HaveJson(row)
        obj.repetitionTag = num
        obj.delete = true
        this.productList0.splice(index+num,0,obj)
      },
      deleteProductList(index,list){
        if(list[index-1]&&list[index-1].repetitionTag){
          list[index-1].delete=true
        }
        list.splice(index,1)
        this.deleteProductList0(index)
      },
      deleteProductList0(index){
        if(this.productList0[index-1]&&this.productList0[index-1].repetitionTag){
          this.productList0[index-1].delete=true
        }
        this.productList0.splice(index,1)
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
>>>.warning-row {
  color: #1890FF;
}
.node_i {
  color: orange;
  font-size: 18px;
}
</style>
