<template>
  <div v-loading="loading" class="inspection" style="background-color: rgb(245, 247, 251);">
    <el-row class="title">
      <el-col :span="8" style="text-align: left">
        <el-form :inline="true" :model="otherForm" class="form-inline" label-width="50px"
          style="text-align: right; padding-top: 0; display: inline">
          <el-form-item label="温度:" style="margin-bottom: 0">
            <el-input v-model="otherForm.temperature" :disabled="state > 1" placeholder="" size="small"
              style="width: 90px; line-height: 60px" @change="(m) => subOtherForm(m, 'temperature')"></el-input>
            <span style="margin-left: 4px">℃</span>
          </el-form-item>
          <el-form-item label="湿度:" style="margin-bottom: 0">
            <el-input v-model="otherForm.humidity" :disabled="state > 1" placeholder="" size="small"
              style="width: 90px; line-height: 60px" @change="(m) => subOtherForm(m, 'humidity')"></el-input>
            <span style="margin-left: 4px">%</span>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="16" style="text-align: right">
        <el-button size="small" type="primary" @click="refreshView">刷新</el-button>
        <el-button v-if="typeSource == 1" size="small" type="primary" @click="openPurchase">进货验证</el-button>
        <el-button v-if="state == 1 && typeSource == 1" size="small" type="primary"
          @click="openUnPassDialog('add')">不合格处理</el-button>
        <el-button size="small" type="primary" @click="sampleVisible = true; uploadSample();">样品切换</el-button>
        <!--        <el-button v-if="state == 1" size="small" type="primary" @click="taskVisible = true">任务切换</el-button>-->
        <el-button v-if="state == 1" size="small" type="primary" @click="addVerifyDia = true">提交</el-button>
        <!-- 复核 -->
        <el-button v-if="state == 2" size="medium" type="primary" @click="openAddCheck">通过</el-button>
        <el-button v-if="state == 2" size="medium" @click="upInsReview(0)">不通过</el-button>
        <el-button type="primary" size="small" @click="exportTable('myTable')">下载原始记录</el-button>
        <el-button size="small" @click="goback">返回</el-button>
      </el-col>
    </el-row>
    <div class="search">
      <el-form :inline="true" :model="searchForm" class="form-inline" label-position="right" label-width="100px">
        <el-form-item label="委托编号:">
          <el-input v-model="insOrder.entrustCode" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="样品编号:">
          <el-tooltip :content="currentSample.sampleCode" :disabled="!currentSample.sampleCode">
            <el-input v-model="currentSample.sampleCode" clearable disabled placeholder="请输入" size="small"></el-input>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="样品名称:">
          <el-input v-model="currentSample.sample" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="样品数量:">
          <el-input v-model="sampleProduct.length" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="样品型号:">
          <el-input v-model="currentSample.model" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="下发时间:">
          <el-input v-model="insOrder.sendTime" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="紧急程度:">
          <el-input v-model="insOrder.typeName" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="约定时间:">
          <el-input v-model="insOrder.appointed" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item v-if="typeSource === 1" label="抽检数量:">
          <el-input v-model="insOrder.testQuantity" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item v-if="typeSource === 1" label="厂家密度:">
          <el-input v-model="supplierDensity" clearable disabled placeholder="请输入" size="small"></el-input>
        </el-form-item>
        <el-form-item label="当前样品位数:" label-width="120px">
          <el-tag v-if="currentKey">{{ `NO.${currentKey}` }}</el-tag>
        </el-form-item>
        <el-form-item label="备注:">
          <!--          <span style="color:red">{{ insOrder.remark?insOrder.remark:'-' }}</span>-->
          <el-input v-model="insOrder.remark" :disabled="state != 1" clearable placeholder="请输入" size="small"
            @blur="subOtherForm(insOrder.remark, 'remark')"></el-input>
          <!-- <el-tag v-if="currentKey">{{ insOrder.remark }}</el-tag> -->
        </el-form-item>
      </el-form>
    </div>
    <div class="center">
      <div class="search" style="
          text-align: left;
          display: flex;
          align-items: center;
          justify-content: space-between;
        ">
        <div style="display: flex; align-items: center">
          <span v-if="tableList.length > 0">检验模板：</span>
          <el-radio-group v-model="currentTable" size="small">
            <el-radio-button v-for="(item, index) in tableLists" :key="index" :label="item.templateId" size="small">{{
              item.templateName }}</el-radio-button>
          </el-radio-group>
        </div>
        <div style="display: flex; align-items: center">
          <el-button v-if="state == 1" size="small" type="primary" @click="openAddUnpass">新增不合格复测</el-button>
          <el-button v-if="state == 1" size="small" type="primary" @click="unpassCheck">不合格复测</el-button>
          <el-button v-if="state > 1" size="small" type="primary" @click="viewUnpassCheck">查看不合格复测</el-button>
          <span v-if="cableTagList.length > 0">&nbsp;&nbsp;电缆配置：</span>
          <el-select v-if="cableTagList.length > 0" v-model="currentTab" clearable placeholder="请选择" size="small"
            @change="(m) => handleChangeCableTag(currentSample.id, 0, 'cableTag', m)
              " @focus="getCableTag(currentSample.id)">
            <el-option v-for="item in cableTagList" :key="item.cableTag" :label="item.cableTag" :value="item.cableTag">
              <span style="float: left">{{ item.cableTag }}</span>
              <el-tag v-if="item.status == 0" size="small" style="float: right; margin-top: 5px"
                type="danger">未检</el-tag>
              <el-tag v-if="item.status == 1" size="small" style="float: right; margin-top: 5px"
                type="warning">检验中</el-tag>
              <el-tag v-if="item.status == 2" size="small" style="float: right; margin-top: 5px"
                type="success">已检</el-tag>
            </el-option>
          </el-select>
          <span v-if="repetitionTagList.length > 0">&nbsp;&nbsp;重复检验项：</span>
          <el-select v-if="repetitionTagList.length > 0" v-model="repetitionTag" clearable placeholder="请选择"
            size="small" @change="(m) =>
              handleChangeCableTag(currentSample.id, 0, 'repetitionTag', m)
              " @focus="getRepetitionTag(currentSample.id)">
            <el-option v-for="item in repetitionTagList" :key="item.repetitionTag" :label="item.radius"
              :value="item.repetitionTag">
              <span style="float: left">{{ item.radius }}</span>
              <el-tag v-if="item.status == 0" size="small" style="float: right; margin-top: 5px"
                type="danger">未检</el-tag>
              <el-tag v-if="item.status == 1" size="small" style="float: right; margin-top: 5px"
                type="warning">检验中</el-tag>
              <el-tag v-if="item.status == 2" size="small" style="float: right; margin-top: 5px"
                type="success">已检</el-tag>
            </el-option>
          </el-select>
          <span v-if="typeSource == '1'">&nbsp;&nbsp;待检验数量：</span>
          <el-select v-if="typeSource == '1'" v-model="rawMaterialTag" placeholder="请选择" size="small" @change="(m) => handleChangeCableTag(currentSample.id, 4, 'cableTag', m)
            " @focus="getRawMaterialTag(currentSample.id)">
            <el-option v-for="item in rawMaterialTagList" :key="item.rawMaterialTag" :label="item.rawMaterialTag"
              :value="item.rawMaterialTag">
              <span style="float: left">{{ item.rawMaterialTag }}</span>
              <el-tag v-if="item.status == 0" size="small" style="float: right; margin-top: 5px"
                type="danger">未检</el-tag>
              <el-tag v-if="item.status == 1" size="small" style="float: right; margin-top: 5px"
                type="warning">检验中</el-tag>
              <el-tag v-if="item.status == 2" size="small" style="float: right; margin-top: 5px"
                type="success">已检</el-tag>
            </el-option>
          </el-select>
          <el-button :loading="dataAcquisitionLoading" v-if="state == 1" size="small" type="primary"
            @click="getDataAcquisitionDevice">数据采集</el-button>
          <el-button :type="dataAcquisitionEidtAble ? '' : 'primary'" v-if="state == 1" size="small"
            @click="dataAcquisitionEidtAble = !dataAcquisitionEidtAble">{{ dataAcquisitionEidtAble ? "关闭编辑" : "编辑数采"
            }}</el-button>
        </div>
      </div>
      <!-- 常规检验原始记录 -->
      <div id="nav" v-loading="tableLoading" class="center-box">
        <template v-if="
          tableLists.find((m) => m.templateId == currentTable)">
          <table v-for="(item, index) in tableList" :key="index + currentTable + currentSample.id" border="1"
            cellpadding="10" class="tables" id="myTable">
            <tbody>
              <tr style="white-space: nowrap">
                委托编号：{{
                  insOrder.entrustCode
                }}
              </tr>
              <tr v-for="(m, i) in item.arr" :key="i">
                <td v-for="(n, j) in m" v-if="n.v.mc == undefined || Object.keys(n.v.mc).length === 4"
                  :id="item.templateId + '-' + n.i + '-' + n.r + '-' + n.c" :key="j"
                  :colspan="n.v.mc && n.v.mc.cs ? n.v.mc.cs : 1" :rowspan="n.v.mc && n.v.mc.rs ? n.v.mc.rs : 1" :style="`background:${n.v.bg ? n.v.bg : ''};color:${n.v.fc
                    };font-size:${n.v.fs}px;width:${handleWidth(
                      n
                    )}px !important;height:${item.style.rowlen[n.r]}px;font-wight:${n.v.bl ? 'bold' : ''
                    };`">
                  <div :class="`content-h-${n.v.ht} content-v-${n.v.vt}`" :style="`width:${handleWidth(n)}px !important;min-height:${item.style.rowlen[n.r]
                    }px;`" class="content">
                    <template v-if="
                      n.v.ps != undefined &&
                      typeof n.v.ps.value === 'string' &&
                      n.v.ps.value.includes('检验值') &&
                      state == 1
                    ">
                      <el-input v-if="getInspectionValueType(n.i) == 1"
                        :key="'abc-' + '000' + index + '000' + i + '000' + j" v-model="n.v.v" :disabled="(getInspectionItemType(n.i) == 1 &&
                          !dataAcquisitionEidtAble) ||
                          (n.u != userId && n.u != undefined && n.u != '')
                          " class="table_input" @change="(m) =>
                            changeInput(
                              m,
                              `${item.templateId}-${n.r}-${n.c}-${n.i}`,
                              n,
                              'getDataType'
                            )
                            " @input="handleInput(n)" @mousewheel.native.prevent @keydown.enter="
                              changeInput(
                                '',
                                `${item.templateId}-${n.r}-${n.c}-${n.i}`,
                                n,
                                'getDataType'
                              )
                              ">
                      </el-input>
                      <el-input v-else-if="getInspectionValueType(n.i) == 2" v-model="n.v.v" :disabled="getInspectionItemType(n.i) == 1 ||
                        (n.u != userId && n.u != undefined && n.u != '')
                        " class="table_input" type="textarea" @change="(m) =>
                          changeInput(
                            m,
                            `${item.templateId}-${n.r}-${n.c}-${n.i}`,
                            n,
                            'getDataType'
                          )
                          " />
                      <!-- <el-select v-else-if="getInspectionValueType(n.i) == 5" v-model="n.v.v" :disabled="state > 1 ||
                      getInspectionItemType(n.i) == 1 ||
                      (n.u != userId && n.u != undefined && n.u != '')
                      " class="table_input" @change="(m) =>
                        changeInput(
                          m,
                          `${item.templateId}-${n.r}-${n.c}-${n.i}`,
                          n,
                          'getDataType'
                        )
                        " @visible-change="(e) => getDic(e, n.i)">
                      <el-option v-for="(e, i) in enumList" :key="i" :label="e.label" :value="e.value"></el-option>
                    </el-select> -->
                      <span v-else-if="getInspectionValueType(n.i) == 4"
                        :style="`font-family:${n.v.ff} !important;`">/</span>
                    </template>
                    <template v-else-if="n.v.ps != undefined && n.v.ps.value === '结论'">
                      <el-select v-if="
                        (getInspectionValueType(n.i) == 2 ||
                          getInspectionValueType(n.i) == 5) &&
                        state == 1
                      " v-model="n.v.v" class="table_input" @change="(m) =>
                        changeInput(
                          m,
                          `${item.templateId}-${n.r}-${n.c}-${n.i}`,
                          n,
                          'getDataType',
                          'changeSelect'
                        )
                        ">
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
                      <template v-if="getInspectionValueType(n.i) != 2 &&
                        state == 1">
                        <span v-if="n.v.v === 1" :style="`font-family:${n.v.ff} !important;color: green;`">合格</span>
                        <span v-else-if="n.v.v === 0" :style="`font-family:${n.v.ff} !important;color: red;`">不合格</span>
                        <span v-else-if="n.v.v === 3"
                          :style="`font-family:${n.v.ff} !important;color: #3A7BFA;`">不判定</span>
                        <span v-else :style="`font-family:${n.v.ff} !important;`">待定</span>
                      </template>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined &&
                      n.v.ps.value === '设备编码' &&
                      state == 1
                    ">
                      <span>{{ n.v.v }}</span>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined && n.v.ps.value === '设备名称'
                    ">
                      <el-select v-model="n.v.v" :disabled="state > 1" class="table_input" filterable multiple
                        placeholder="设备" remote @change="(val) => changeEquip(val, n)"
                        @visible-change="(e) => getEquipOptions(e, n.i)">
                        <el-option v-for="item in equipOptions" :key="item.value" :label="item.label"
                          :value="item.value">
                          {{ item.label + "--" + item.value }}
                        </el-option>
                      </el-select>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined &&
                      n.v.ps.value === '要求值' &&
                      state == 1
                    ">
                      <span :style="`font-family:${n.v.ff} !important;`">{{
                        getTell(n.i)
                      }}</span>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined &&
                      n.v.ps.value === '计算值' &&
                      state == 1
                    "><span :style="`font-family:${n.v.ff} !important;`">{{
                      toFixed(n.v.v, n.v.ct)
                    }}</span></template>
                    <template v-else-if="
                      n.v.ps != undefined &&
                      n.v.ps.value === '最终值' &&
                      state == 1
                    ">
                      <span :style="`font-family:${n.v.ff} !important;`">{{
                        toFixed(n.v.v, n.v.ct)
                      }}</span>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined && n.v.ps.value === '样品编号'
                    ">
                      <div :title="currentSample.sampleCode" style="
                        display: flex;
                        flex-wrap: nowrap;
                        align-items: center;
                        width: 100%;
                      ">
                        <i class="el-icon-caret-left table_caret" style="width: 16px" @click="caretSample(-1)"></i>
                        <div
                          :style="`font-family:${n.v.ff} !important;overflow: hidden;white-space: nowrap;width: calc(100% - 32px);`">
                          {{ currentSample.sampleCode }}
                        </div>
                        <i class="el-icon-caret-right table_caret" style="width: 16px" @click="caretSample(1)"></i>
                      </div>
                    </template>
                    <template v-else-if="
                      n.v.ps != undefined && n.v.ps.value === '样品型号'
                    ">
                      <div v-if="
                        currentSample.model !== undefined &&
                        currentSample.model !== null
                      " :style="`font-family:${n.v.ff} !important;`">
                        {{ currentSample.model }}
                      </div>
                    </template>
                    <span v-else :style="`font-family:${n.v.ff} !important;`" v-html="getValue(n.v)"></span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </template>
      </div>
      <el-upload v-if="state == 1" ref="upload" :action="action" :before-upload="beforeUpload" :data="{
        orderId: id,
      }" :headers="uploadHeader" :on-error="onError" :on-success="handleSuccessUp" :show-file-list="false"
        accept=".jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar"
        style="width: 80px !important;margin-top: 10px;">
        <el-button v-if="state == 1" size="small" type="primary">附件上传</el-button></el-upload>
      <lims-table :tableData="tableData" :column="column" :page="page" :tableLoading="tableLoading"
        :height="'calc(100vh - 290px)'" @pagination="pagination" style="height: 100%; margin-top: 16px"></lims-table>
    </div>
    <el-drawer :size="550" :visible.sync="sampleVisible" title="样品切换">
      <el-table v-if="sampleVisible" ref="productTable" :current-row-key="currentKey" :data="sampleProduct"
                :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
        :row-class-name="tableRowClassName" :row-key="(record) => record.index" border class="el-table" height="100%"
        highlight-current-row tooltip-effect="dark" @row-click="handleChangeSample">
        <el-table-column :key="Math.random()" align="center" label="序号" type="index" width="70px">
        </el-table-column>
        <el-table-column label="样品编号" min-width="100px" prop="sampleCode" show-overflow-tooltip></el-table-column>
        <el-table-column label="样品型号" min-width="100px" prop="model" show-overflow-tooltip></el-table-column>
        <el-table-column label="检测状态" prop="insState" show-overflow-tooltip width="100px">
          <template slot-scope="scope">
            <el-tag v-for="(item, i) in typeList" v-if="item.value == scope.row.insState" :key="i" :type="item.type"
              size="medium" style="margin-right: 5px">{{ item.label }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column align="center" label="是否留样" prop="isLeave" show-overflow-tooltip width="95px">
          <template slot-scope="scope">
            <span>{{ scope.row.isLeave == 0 ? "否" : "是" }}</span>
          </template>
        </el-table-column>
        <!-- 检验单会有一个是否有配套样品字段控制显示 -->
        <el-table-column v-if="insOrder.mating > 0" label="配套样品型号" min-width="130px" prop="joinModel"
          show-overflow-tooltip></el-table-column>
        <el-table-column v-if="insOrder.mating > 0" label="配套样品名称" min-width="130px" prop="joinName"
          show-overflow-tooltip></el-table-column>
        <el-table-column v-if="insOrder.mating > 0" label="配套样品数量" min-width="130px" prop="joinNum"
          show-overflow-tooltip></el-table-column>
      </el-table>
    </el-drawer>
    <el-drawer :size="500" :visible.sync="taskVisible" title="任务切换">
      <lims-table :tableData="tableData0" :column="column0" :page="page0" :tableLoading="tableLoading0"
        :height="'calc(100vh - 90px)'" :currentChange="handleChangeTask"></lims-table>
    </el-drawer>
    <el-dialog :visible.sync="reviewDia" title="检验复核" width="500px">
      <div v-if="reviewDia" class="body" style="display: flex; padding: 10px">
        <div class="search_label" style="width: 150px">
          <span class="required-span">* </span>不通过的理由：
        </div>
        <div class="search_input" style="width: 100%">
          <el-input v-model="noReason" :autosize="{ minRows: 4 }" clearable size="small" type="textarea"></el-input>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="reviewDia = false">取 消</el-button>
        <el-button :loading="reviewLoading" type="primary" @click="handleReviewDia">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="addVerifyDia" title="指定复核人员" width="400px">
      <div class="body" style="display: flex; padding: 10px; align-items: center">
        <div class="search_label" style="width: 150px">
          <span class="required-span">*</span>复核人
        </div>
        <div class="search_input" style="width: 100%">
          <el-select v-model="verifyUser" clearable filterable placeholder="请选择" size="small" style="width: 100%">
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
      :visible.sync="dataGetDia" title="数据采集" width="80%">
      <div>
        <table border="1" cellpadding="10" class="thermal-table">
          <tr>
            <td style="width: 120px">检验项分类</td>
            <td style="width: 120px">检验项</td>
            <td style="width: 120px">检验子项</td>
            <td>数采数据</td>
          </tr>
          <template v-for="(item, index) in getData">
            <tr>
              <td :rowspan="item.child.length">{{ item.sortName }}</td>
              <td :rowspan="item.child.length">{{ item.faName }}</td>
              <td>{{ item.child[0].name }}</td>
              <td style="text-align: left">
                <el-select v-if="item.child[0].isShowSelect" v-model="item.child[0].getDataIndex1"
                  :multiple-limit="item.child[0].maxNum" clearable filterable multiple placeholder="请选择"
                  @change="setData(item)">
                  <el-option v-for="(item, index) in item.child[0].arr" :key="index" :filter-method="filterMethod"
                    :label="index + 1 + '^' + item.result" :value="index + '^' + item.result">
                    <span>{{
                      "序号：" +
                      (index + 1) +
                      "，" +
                      "结果：" +
                      item.result +
                      "，" +
                      "厚度：" +
                      item.thickness +
                      "，" +
                      "时间：" +
                      item.mergedDateTime
                    }}</span>
                  </el-option>
                </el-select>
                <el-checkbox-group v-if="!item.child[0].isShowSelect" v-model="item.child[0].getDataIndex1"
                  :max="item.child[0].maxNum">
                  <el-checkbox v-for="(n, j) in item.child[0].arr" :key="j" :label="j + '^' + n">{{ n }}</el-checkbox>
                </el-checkbox-group>
              </td>
            </tr>
            <tr v-for="(m, i) in item.child" v-show="i > 0" :key="i + 'bbbbbbbbbbbbbb'">
              <td>{{ m.name }}</td>
              <td style="text-align: left">
                <el-select v-if="m.isShowSelect" v-model="m.getDataIndex1" :multiple-limit="m.maxNum" clearable
                  filterable multiple placeholder="请选择">
                  <el-option v-for="(item, index1) in m.arr" :key="index1" :label="index + 1 + '^' + item.result"
                    :value="index1 + '^' + item.result">
                    <span>{{
                      "序号：" +
                      (index1 + 1) +
                      "，" +
                      "结果：" +
                      item.result +
                      "，" +
                      "厚度：" +
                      item.thickness +
                      "，" +
                      "时间：" +
                      item.mergedDateTime
                    }}</span>
                  </el-option>
                </el-select>
                <el-checkbox-group v-if="!m.isShowSelect" v-model="m.getDataIndex1" :max="m.maxNum">
                  <el-checkbox v-for="(n, j) in m.arr" :key="j" :label="j + '^' + n">{{ n }}</el-checkbox>
                </el-checkbox-group>
              </td>
            </tr>
          </template>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dataGetDia = false">取 消</el-button>
        <el-button :loading="getDataIndexLoading" type="primary" @click="submitDataGet()">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :close-on-click-modal="false" :visible.sync="addCheck" title="指定报告审核人员" width="400px"
      @close="closeAddVerifyDia">
      <div class="body" style="display: flex; padding: 10px; align-items: center">
        <div class="search_label" style="width: 150px">
          <span class="required-span">*</span>审核人：
        </div>
        <div class="search_input" style="width: 100%">
          <el-select v-model="checkUser" clearable filterable placeholder="请选择" size="small" style="width: 100%">
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
    <!-- 添加不合格复测弹框-->
    <add-un-pass v-if="addUnPassDialog" ref="addUnPassDialog" :addUnPassDialog="addUnPassDialog"
      @resetAddUnPass="resetAddUnPass"></add-un-pass>
    <!-- 不合格处理弹框-->
    <UnPassDialog v-if="unPassDialog" ref="unPassDialog" :orderId="orderId" :unPassDialog="unPassDialog"
      @resetForm="resetForm"></UnPassDialog>
    <!-- 不合格复测弹框-->
    <InspectionWord v-if="unPassCheck" :inspectorList="inspectorList" :orderId="orderId"
                    :cableTag="temCableTag" :repetitionTag="repetitionTag"
      :rawMaterialTag="rawMaterialTag" :sonLaboratory="sonLaboratory" :state="state" :typeSource="typeSource"
      :unPassCheck="unPassCheck" @closeUnPassCheckDialog="closeUnPassCheckDialog" @refreshView="refreshView" />
    <!-- 进货验证弹框-->
    <purchase-verification v-if="purchaseDialog" ref="purchaseDialog" :orderId="orderId"
      :purchaseDialog="purchaseDialog" @resetPurchaseDialog="resetPurchaseDialog"></purchase-verification>
    <!--查看工时弹框-->
    <viewManHourDia ref="viewManHourDia" @submit="openAddVerifyDia"></viewManHourDia>
  </div>
</template>

<script>
import excelFunction from "@/utils/excelFountion";
import limsTable from "@/components/Table/lims-table.vue";
import UnPassDialog from "../unpass/components/unPassDialog.vue";
import AddUnPass from "../unpass/components/addUnPass.vue";
import InspectionWord from "./components/InspectionWord.vue";
import PurchaseVerification from "../unpass/components/PurchaseVerification.vue";
import {
  doInsOrder,
  getCableTag,
  getRepetitionTag,
  getRawMaterialTag,
  dataCollection,
  write,
  getInsProduct,
  search,
  verifyPlan,
  checkSubmitPlan,
  submitPlan,
  saveInsContext,
  selectUserCondition,
  downFile,
  getFileList,
  delfile,
  inspectionOrderDetailsTaskSwitching
} from "@/api/business/inspectionTask.js";
import InspectionWorker from '@/workers/InspectionWorker.worker';
import DataWorker from '../../../workers/DataWorker.worker';
import html2canvas from "html2canvas";
import { mapGetters } from "vuex";
import viewManHourDia from "@/views/business/inspectionTask/components/viewManHourDia.vue";
export default {
  name: 'Inspection',
  components: {
    viewManHourDia,
    PurchaseVerification,
    AddUnPass,
    limsTable,
    UnPassDialog,
    InspectionWord,
  },
  data() {
    return {
      sonLaboratory: null,
      orderId: null,
      state: null,
      inspectorList: null,
      typeSource: null,
      sagData: [],
      sagForm: {
        sampleCode: null,
        model: null,
        inspection: null,
        methodName: null,
        tensileForce: null,
        spanLength: null,
        load: null,
      },
      dataGetDia: false,
      fileAdd: false,
      sampleVisible: false,
      taskVisible: false,
      submitLoading: false,
      searchForm: {
        sampleName: null,
        state: null,
      },
      id: null,
      changeType: null,
      insOrder: {},
      sampleProduct: [],
      supplierDensity: "", // 厂家密度
      typeList: [],
      urgentList: [],
      currentSample: {}, //当前样品信息
      tableList: [],
      loading: false,
      ps: {},
      param: {},
      currentKey: 1,
      currentKey0: 1,
      currentKey1: 1,
      currentKey2: 1,
      comparisonList: [],
      excelMethodList: [],
      equipOptions: [],
      reviewLoading: false,
      reviewDia: false,
      noReason: "",
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
        inspectionItemSubclass: "20(常温)",
      },
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
      dataAcquisitionLoading: false,
      collected: false,
      temDataAcquisition: false,
      getData: [
        {
          faName: "波长附加衰减",
          child: [
            {
              name: "1285nm~1330nm",
              arr: [12, 13, 14, 15],
            },
            {
              name: "1525nm~1575nm",
              arr: [12, 13, 14, 15],
            },
          ],
        },
        {
          faName: "截至波长",
          child: [
            {
              name: "截至波长",
              arr: [12, 13, 14, 15],
            },
          ],
        },
      ],
      getDataIndex: [],
      getDataIndexLoading: false,
      getDataTypeId: "",
      getDataType: null,
      unPassDialog: false, // 不合格处理弹框
      cableTagList: [],
      rawMaterialTag: "", // 批次
      rawMaterialTagList: [], // 批次选项
      addCheck: false, // 指定审核人员弹框
      checkUser: "",
      addUnPassDialog: false,
      type: "",
      retestTag: "",
      unPassCheck: false,
      unPassTableList: [],
      unPassTableLoading: false,
      purchaseDialog: false, // 进货验证原始记录弹框
      temCableTag: "",
      repetitionTagList: [],
      repetitionTag: "",
      // 文件列表相关--开始
      tableData: [],
      column: [
        {
          label: "类型", prop: "type", dataType: "tag",
          formatData: (params) => {
            if (params == 1) {
              return '图片'
            } else {
              return '文件'
            }
          }
        },
        { label: "附件名称", prop: "fileName" },
        { label: "上传人", prop: "name" },
        { label: "上传时间", prop: "createTime" },
        {
          dataType: "action",
          fixed: "right",
          label: "操作",
          operation: [
            {
              name: "下载",
              type: "text",
              clickFun: (row) => {
                this.handleDown(row);
              },
            },
            {
              name: "删除",
              type: "text",
              clickFun: (row) => {
                this.handleDelete(row);
              },
              disabled: (row) => {
                return this.state != 1;
              },
            },
          ],
        },
      ],
      page: {
        total: 0,
        size: 10,
        current: 0,
      },
      tableLoading: false,
      // 文件列表相关--结束
      // 任务切换列表--开始
      tableData0: [],
      column0: [
        { label: "委托编号", prop: "entrustCode", width: '160px' },
        { label: "检验对象", prop: "sampleType", width: '140px' },
        {
          label: "紧急程度", prop: "type", dataType: "tag",
          formatData: (params) => {
            return this.urgentList.find(m => m.value == params).label
          },
          formatType: (params) => {
            return this.urgentList.find(m => m.value == params).type
          }
        },
        {
          label: "状态", prop: "insState", dataType: "tag",
          formatData: (params) => {
            return this.typeList.find(m => m.value == params).label
          },
          formatType: (params) => {
            return this.typeList.find(m => m.value == params).type
          }
        },
        { label: "约定时间", prop: "appointed", width: '140px' },
        { label: "下发时间", prop: "sendTime", width: '140px' },
      ],
      page0: {
        total: 0,
        size: -1,
        current: -1,
      },
      tableLoading0: false,
      // 任务切换列表--结束
    };
  },
  // 用于上传文件的信息
  computed: {
    ...mapGetters(["userId"]),
    action() {
      return this.javaApi + "/insOrderPlan/uploadFile";
    },
  },
  created() {
    let { sonLaboratory, orderId, state, inspectorList, typeSource } =
      this.$route.query;
    this.sonLaboratory = sonLaboratory;
    this.orderId = orderId;
    this.id = this.orderId;
    this.state = state;
    this.inspectorList = inspectorList;
    this.typeSource = typeSource;
  },
  mounted() {
    this.getTypeDicts(); // 获取紧急程度下拉框选项
    this.getInsStateDicts();
    this.getComparisonList();
    this.getAuthorizedPerson();
    this.startWorker();
    // this.getList0() // 任务切换
    this.scrollInit();
  },
  activated() {
    this.getTypeDicts(); // 获取紧急程度下拉框选项
    this.getInsStateDicts();
    this.getComparisonList();
    this.getAuthorizedPerson();
    this.startWorker();
    // this.getList0() // 任务切换
    this.scrollInit();
  },
  watch: {
    // 监听任务id，获取任务信息
    id(val) {
      this.refreshView()
    },
    // 监听当前模板变化
    currentTable(val1, val0) {
      if (val0 != null && val1 != val0) {
        if (this.changeType && this.changeType > 0) {
          // 如果是光纤、光纤带，则不执行下面操作
          return;
        }
        this.tableLists.forEach(async (m, i) => {
          if (m.templateId == val1) {
            let list = await this.getCurrentProduct(this.currentSample.id, 0);
            this.currentSample.insProduct = this.HaveJson(list); //赋值当前样品的检验项
            this.param = {}; //初始化传到后端的参数
            this.currentSample.insProduct.forEach((a, j) => {
              this.param[a.id] = {
                insValue: [],
                comValue: [],
                resValue: null,
                equipValue: [],
                equipName: [],
                insResult: null,
              };
            });
            // 去重模板，返回有几个模板
            const mySet1 = new Set();
            this.tableLists = this.currentSample.insProduct.filter((m) => {
              let num0 = mySet1.size;
              if (m.templateId != null && m.template != null) {
                try {
                  mySet1.add(
                    JSON.stringify({
                      template: m.template,
                      templateId: m.templateId,
                    })
                  );
                } catch (error) {
                  console.log(222, error);
                }
              }
              let num1 = mySet1.size;
              if (num1 > num0) {
                return m;
              }
            });
            if (this.tableLists && this.tableLists.length > 0) {
              this.tableList = null;
              this.tableList = this.tableLists.filter(
                (m) => m.templateId == val1
              );
              // 对模板进行处理
              this.handleTableData();
            }
          }
        });
      }
    },
    // 特殊检验项--监听设备信息改变
    equipForm: {
      deep: true,
      handler(val) { },
    },
  },
  beforeDestroy() {
    // 在组件销毁前确保停止 Worker，避免内存泄漏
    this.stopWorker();
  },
  methods: {
    // 文件管理--开始
    getList() {
      this.tableLoading = true;
      let param = { ...this.page };
      delete param.total;
      getFileList({ insOrderId: this.id, ...param })
        .then((res) => {
          this.tableLoading = false;
          if (res.code === 200) {
            this.tableData = res.data.records;
            this.page.total = res.data.total;
          }
        })
        .catch((err) => {
          this.tableLoading = false;
        });
    },
    pagination({ page, limit }) {
      this.page.current = page;
      this.page.size = limit;
      this.getList();
    },
    handleDelete(row) {
      this.$confirm("是否删除该条数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        delfile({ id: row.id }).then((res) => {
          this.$message.success("删除成功");
          this.getList();
        });
      }).catch(() => { });
    },
    // 文件管理--结束

    // 任务切换--开始
    getList0() {
      this.tableLoading0 = true;
      let param = { userId: this.userId, sonLaboratory: this.sonLaboratory, ...this.page0 };
      delete param.total;
      inspectionOrderDetailsTaskSwitching({ ...param })
        .then((res) => {
          this.tableLoading0 = false;
          if (res.code === 200) {
            this.tableData0 = res.data.records;
          }
        })
        .catch((err) => {
          this.tableLoading0 = false;
        });
    },
    // 任务切换--结束

    closeAddVerifyDia() {
      this.addCheck = false;
      this.checkUser = "";
    },
    // 刷新页面
    refreshView() {
      this.loading = true;
      doInsOrder({
        id: this.id,
        laboratory: this.sonLaboratory,
      }).then(async (res) => {
        this.insOrder = res.data.insOrder;
        this.supplierDensity = res.data.supplierDensity;
        this.getList()
        this.urgentList.forEach((m) => {
          if (m.value == this.insOrder.type) {
            this.insOrder.typeName = m.label;
          }
        });
        this.loading = false;
        if (!res.data.sampleProduct || res.data.sampleProduct.length == 0) {
          this.goback();
          return this.$message.error("该任务没有样品信息");
        }
        // 赋值当前样品列表
        this.sampleProduct = res.data.sampleProduct;
        this.currentSample = this.HaveJson(this.sampleProduct[0]);
        let insProduct = this.HaveJson(this.currentSample.insProduct);
        // 温度、湿度赋值
        this.otherForm = {
          temperature: this.insOrder.temperature
            ? this.insOrder.temperature
            : null,
          humidity: this.insOrder.humidity ? this.insOrder.humidity : null,
        };
        if (this.typeSource == "1") {
          this.getRawMaterialTag(this.currentSample.id); // 原材料的检验任务查询批数
          this.rawMaterialTag = "1";
        }
        this.getEquipOptions(1);
        // 获取当前样品的检验项
        let list = await this.getCurrentProduct(this.currentSample.id, 0);
        this.currentSample.insProduct = this.HaveJson(list);
        // 初始化传递到后端的参数
        this.param = {};
        this.changeType = 0;
        this.currentSample.insProduct.forEach((a) => {
          this.param[a.id] = {
            insValue: [],
            comValue: [],
            resValue: null,
            equipValue: [],
            equipName: [],
            insResult: null,
          };
        });
        if (this.currentSample.index == undefined)
          this.currentSample["index"] = 1;
        let bushing = this.currentSample.bushing;
        this.getTableLists(); //处理模板列表信息

        this.currentKey = 1;
        this.getCableTag(this.currentSample.id); // 查询是否有要多次检验的电缆配置
        this.getRepetitionTag(this.currentSample.id); // 查询是否有要重复检验项
      });
    },
    closeUnPassCheckDialog() {
      this.unPassCheck = false;
    },
    // 查询是否有要多次检验的电缆配置
    getCableTag() {
      getCableTag({
        id: this.currentSample.id,
        laboratory: this.sonLaboratory,
      }).then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.cableTagList = res.data;
        }
      });
    },
    // 查询是否有要重复检验项
    getRepetitionTag() {
      getRepetitionTag({
        id: this.currentSample.id,
        laboratory: this.sonLaboratory,
        cableTag: this.temCableTag,
      }).then((res) => {
        if (res.code === 200) {
          this.repetitionTagList = res.data;
        }
      });
    },
    getRawMaterialTag() {
      getRawMaterialTag({
        id: this.currentSample.id,
        laboratory: this.sonLaboratory,
      }).then((res) => {
        if (res.code === 200) {
          res.data.forEach((item) => {
            this.rawMaterialTagList = res.data;
          });
        }
      });
    },
    // 添加需要复测的不合格检验项
    openAddUnpass() {
      this.addUnPassDialog = true;
      const info = {
        id: this.currentSample.id,
        type: this.type,
        laboratory: this.sonLaboratory,
        cableTag: this.temCableTag,
        rawMaterialTag: this.rawMaterialTag,
        repetitionTag: this.repetitionTag,
      };
      this.$nextTick(() => {
        this.$refs.addUnPassDialog.getInsOrder(info);
      });
    },
    resetAddUnPass() {
      this.addUnPassDialog = false;
    },
    // 进行不合格复测
    unpassCheck() {
      this.unPassCheck = true;
      this.retestTag = "1";
    },
    viewUnpassCheck() {
      this.unPassCheck = true;
      this.retestTag = "1";
    },
    // 打开不合格处理弹框
    openUnPassDialog(type) {
      this.unPassDialog = true;
      this.$nextTick(() => {
        this.$refs.unPassDialog.getInsOrder(type);
      });
    },
    // 关闭不合格处理弹框
    resetForm() {
      this.$refs.unPassDialog.$refs["unPassForm"].resetFields();
      this.unPassDialog = false;
    },
    // 打开进货验证弹框
    openPurchase() {
      const operationType = this.state == 1 ? "add" : "view";
      this.purchaseDialog = true;
      const item = {
        id: this.currentSample.id,
        sonLaboratory: this.sonLaboratory,
        cableTag: this.temCableTag,
        rawMaterialTag: this.rawMaterialTag,
        typeSource: this.typeSource,
      };
      this.$nextTick(() => {
        this.$refs.purchaseDialog.getInsOrder(operationType, item);
      });
    },
    resetPurchaseDialog() {
      this.$refs.purchaseDialog.$refs["purchaseForm"].resetFields();
      this.purchaseDialog = false;
    },
    // 数据采集
    getDataAcquisitionDevice() {
      let itemIds = [];
      this.currentSample.insProduct.forEach((item) => {
        if (item.inspectionItemType === "1") {
          itemIds.push(item.id);
        }
      });
      const params = {
        entrustCode: this.insOrder.entrustCode,
        sampleCode: this.currentSample.sampleCode,
        id: this.currentSample.id,
        itemIds: itemIds,
      };
      this.dataAcquisitionLoading = true;
      dataCollection(params).then((res) => {
        this.dataAcquisitionLoading = false;
        if (res.code != 200) {
          return;
        }
        this.dataAcquisitionInfoNew = this.HaveJson(res.data);
        // 对数采回来的值进行处理
        this.handleDataAcquisition(res.data);
      }).catch(err => {
        this.dataAcquisitionLoading = false;
      });
    },
    objectOrder(obj) {
      let newkey = Object.keys(obj).sort();
      let newObj = {};
      for (let i = 0; i < newkey.length; i++) {
        newObj[newkey[i]] = obj[newkey[i]];
      }
      return newObj;
    },
    handleDataAcquisition(data, noDialog) {
      // 是否可以编辑数采数据
      if (this.dataAcquisitionEidtAble) {
        this.getDataType = 1;
      } else {
        this.getDataType = 2;
      }
      this.dataAcquisitionInfo = {};
      this.getData = [];
      for (let i in this.objectOrder(data)) {
        let obj = {};
        if (i.includes("@")) {
          obj = {
            faName: i.split("@")[0],
            sortName: i.split("@")[1],
            child: [],
          };
        } else {
          obj = {
            faName: i,
            child: [],
          };
        }
        // 循环数采数据
        for (let j in data[i]) {
          // 拼接字符串  检验项分类+检验项+检验子项
          let str0 = "";
          if (i.includes("@")) {
            if (i.split("@")[0] == j) {
              str0 = i.split("@")[0] + ",";
            } else {
              str0 = i.split("@")[1] + "," + i.split("@")[0] + "," + j;
            }
          } else {
            if (i == j) {
              str0 = i + ",";
            } else {
              str0 = i + "," + j;
            }
          }
          if (
            j != "frequency" &&
            data[i][j] &&
            (!data[i][j].result || typeof data[i][j].result == "string")
          ) {
            // 处理数采信息格式
            if (data[i][j].result) {
              this.dataAcquisitionInfo[str0] = {
                value: data[i][j].result,
                frequency: data[i].frequency,
              };
            }
            let list = this.tableList[0].arr;
            // 循环页面列表信息，判断数采数据对应页面列表信息的检验项是哪个，并给当前检验项绑定设备信息
            list.forEach((item, index) => {
              let num0 = 0;
              let str = "";
              item.forEach((m) => {
                if (
                  m.v.ps &&
                  (m.v.ps.value == "检验子项" ||
                    m.v.ps.value == "检验项" ||
                    m.v.ps.value == "检验项分类")
                ) {
                  if (m.v.ps && m.v.ps.value == "检验项分类") {
                    if (num0 == 0) {
                      num0++;
                      str = m.v.v + ",";
                    }
                  }
                  if (m.v.ps && m.v.ps.value == "检验项") {
                    if (num0 == 1) {
                      str = str + m.v.v + ",";
                      num0++;
                    } else {
                      str = m.v.v + ",";
                      num0++;
                    }
                  }
                  if (m.v.ps && m.v.ps.value == "检验子项") {
                    str = str + m.v.v;
                  }
                }
                // 绑定设备信息
                if (
                  data[i][j].equipName &&
                  data[i][j].equipValue &&
                  m.v &&
                  m.v.ps &&
                  m.v.ps.value == "设备名称" &&
                  str0 == str
                ) {
                  if (!m.v.v) {
                    // this.changeEquip(data[i][j].equipValue,m,data[i][j].equipName)
                  }
                }
              });
            });
          } else if (
            j != "frequency" &&
            data[i][j] &&
            Array.isArray(data[i][j].result)
          ) {
            // 如果返回的数采数据是数组，则处理数组
            // 以下逻辑为获取每个检验项可输入的检验值的最大个数
            let str0 = "";
            if (i.includes("@")) {
              if (i.split("@")[0] == j) {
                str0 = i.split("@")[0] + ",";
              } else {
                str0 = i.split("@")[1] + "," + i.split("@")[0] + "," + j;
              }
            } else {
              if (i == j) {
                str0 = i + ",";
              } else {
                str0 = i + "," + j;
              }
            }
            let list = this.tableList[0].arr;
            let maxNum = 0;
            list.forEach((item, index) => {
              let num0 = 0;
              let str = "";
              item.forEach((m) => {
                if (
                  m.v.ps &&
                  (m.v.ps.value == "检验子项" ||
                    m.v.ps.value == "检验项" ||
                    m.v.ps.value == "检验项分类")
                ) {
                  if (m.v.ps && m.v.ps.value == "检验项分类") {
                    if (num0 == 0) {
                      num0++;
                      str = m.v.v + ",";
                    }
                  }
                  if (m.v.ps && m.v.ps.value == "检验项") {
                    if (num0 == 1) {
                      str = str + m.v.v + ",";
                      num0++;
                    } else {
                      str = m.v.v + ",";
                      num0++;
                    }
                  }
                  if (m.v.ps && m.v.ps.value == "检验子项") {
                    str = str + m.v.v;
                  }
                  let num = 0;
                  list[index].forEach((n) => {
                    if (
                      n.v.ps &&
                      n.v.ps.value &&
                      typeof n.v.ps.value == "string" &&
                      n.v.ps.value.includes("检验值")
                    ) {
                      num++;
                      if (str0 == str) {
                        maxNum = num;
                      }
                    }
                  });
                }
                // 绑定设备
                if (
                  data[i][j].equipName &&
                  data[i][j].equipValue &&
                  m.v &&
                  m.v.ps &&
                  m.v.ps.value == "设备名称" &&
                  str0 == str
                ) {
                  if (!m.v.v) {
                    // this.changeEquip(data[i][j].equipValue,m,data[i][j].equipName)
                  }
                }
              });
            });
            // 获取到最大检验值输入个数后重组数据
            let obj0 = {
              name: j,
              arr: data[i][j].result,
              maxNum: maxNum,
              equipValue: data[i][j].equipValue,
              value: [],
            };
            // 如果数采返回的数组长度大于最大输入个数，则将数采数据在弹框中展示，用户选择需要手动选择数采的信息
            if (
              (data[i][j].result &&
                Array.isArray(data[i][j].result) &&
                data[i][j].result.length > maxNum &&
                noDialog !== "noDialog") ||
              (obj0.equipValue === "JCZX-ZB-FF01014" && noDialog !== "noDialog")
            ) {
              obj.child.push(obj0);
            } else {
              this.dataAcquisitionInfo[str0] = {
                value: data[i][j].result,
              };
            }
          }
        }
        // 循环数采数据
        if (obj.child.length > 0) {
          obj.child.forEach((m) => {
            this.$set(m, "getDataIndex1", []);
            // 设备选择为拉力机，就显示多选下拉框
            if (m.equipValue === "JCZX-ZB-FF01014") {
              this.$set(m, "isShowSelect", true);
            } else {
              this.$set(m, "isShowSelect", false);
            }
          });
          this.getData.push(obj);
        }
      }
      // 如果存在数采返回的数组长度大于最大输入个数，则弹出弹框选择
      if (this.getData.length > 0) {
        this.dataGetDia = true;
      } else {
        // 如果都不存在，则，进入处理数采线程里去处理数据
        try {
          // 向 Worker 发送消息，开始处理逻辑
          this.getDataIndexLoading = false;
          this.dataGetDia = false;
          this.getDataTypeId = "";
          this.worker0.postMessage(
            JSON.stringify({
              dataAcquisitionInfo: this.dataAcquisitionInfo,
              list: this.tableList[0].arr,
            })
          );
        } catch (error) {
          console.log(1111, error);
        }
      }
      // 监听 Worker 返回的结果
      this.worker0.onmessage = (event) => {
        let result = JSON.parse(event.data);
        if (result.method == "changeInput") {
          // 采集后的数据，需要进行计算的线程进行计算
          let { list, n } = result.value;
          this.$set(this.tableList[0], "arr", list);
          this.changeInput(
            "",
            `${this.currentSample.insProduct[0].templateId}-${n.r}-${n.c}-${n.i}`,
            n
          );
        } else if (result.getDataTypeId) {
          // 获取到数采最后一项，检验项的ID
          this.getDataTypeId = result.getDataTypeId;
        }
      };
      this.dataAcquisitionLoading = false;
    },
    setData(item) {
      if (item.child.length > 1) {
        let arr1 = [];
        // 多个检验子项的话，第一个检验子项选的第几个下面的就赋值第几个
        for (let j = 0; j < item.child.length; j++) {
          let arr = [];
          if (j === 0) {
            for (let k = 0; k < item.child[j].getDataIndex1.length; k++) {
              let val1 = item.child[j].getDataIndex1[k];
              const index = val1.indexOf("^");
              if (index > -1) {
                val1 = val1.substring(0, index);
                arr.push(val1);
              }
            }
            arr1 = arr;
          } else {
            this.$set(item.child[j], "getDataIndex1", []);
            arr1.map((a) => {
              const value1 = a + "^" + item.child[j].arr[Number(a)].result;
              item.child[j].getDataIndex1.push(value1);
            });
          }
        }
      }
    },
    // 拉力机数采
    filterMethod(val) {
      console.log("val---", val);
    },
    // 如果存在数采返回的数组长度大于最大输入个数，则弹出弹框选择，这里是弹框的提交
    submitDataGet() {
      for (let i = 0; i < this.getData.length; i++) {
        for (let j = 0; j < this.getData[i].child.length; j++) {
          // 对用户选择的数采信息进行处理，赋值
          let arr = [];
          for (
            let k = 0;
            k < this.getData[i].child[j].getDataIndex1.length;
            k++
          ) {
            if (this.getData[i].child[j].getDataIndex1[k].includes("^")) {
              const index =
                this.getData[i].child[j].getDataIndex1[k].indexOf("^");
              if (index > -1) {
                this.getData[i].child[j].getDataIndex1[k] = this.getData[
                  i
                ].child[j].getDataIndex1[k].slice(
                  index + 1,
                  this.getData[i].child[j].getDataIndex1[k].length
                );
              }
            }
            arr.push(this.getData[i].child[j].getDataIndex1[k]);
          }
          if (this.getData[i].sortName) {
            this.dataAcquisitionInfoNew[
              this.getData[i].faName + "@" + this.getData[i].sortName
            ][this.getData[i].child[j].name].result = arr;
          } else {
            this.dataAcquisitionInfoNew[this.getData[i].faName][
              this.getData[i].child[j].name
            ].result = arr;
          }
        }
      }
      this.getDataIndexLoading = true;
      // 赋值完成后需要再次进入处理数采线程里去处理数据
      this.handleDataAcquisition(this.dataAcquisitionInfoNew, "noDialog");
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
    // 保存湿度、温度数据
    subOtherForm(m, type) {
      write({
        [type]: type === "remark" ? m : Number(m),
        id: this.insOrder.id,
      }).then((res) => {
        this.$message.success("保存成功");
      });
    },
    exportTable(tableId) {
      // 获取table元素
      var table = document.getElementById(tableId);

      // 使用html2canvas库生成canvas
      html2canvas(table).then(function (canvas) {
        // 创建一个图片元素
        var img = canvas.toDataURL("image/png");

        // 创建一个a标签模拟点击进行下载
        var a = document.createElement("a");
        a.href = img;
        a.download = "table.png";
        a.click();
      });
    },
    uploadSample() {
      doInsOrder({
        id: this.id,
        laboratory: this.sonLaboratory,
      }).then(async (res) => {
        this.sampleProduct = res.data.sampleProduct;
      });
    },
    // 根据类型、任务id、实验室来获取样品的检验项信息
    async getCurrentProduct(id, type, cableTag) {
      if (cableTag === undefined) {
        cableTag = "";
      }
      this.tableLoading = true;
      type = this.typeSource == "1" ? 5 : type;
      this.type = type;
      let res = await getInsProduct({
        id: id,
        type: type,
        laboratory: this.sonLaboratory,
        cableTag: (cableTag ? cableTag : this.temCableTag),
        rawMaterialTag: this.rawMaterialTag,
        repetitionTag: this.repetitionTag
      });
      this.tableLoading = false;
      if (res.data.length > 0) {
        return res.data;
      }
    },
    // 切换样品
    async handleChangeSample(row, column, event) {
      // 初始化数据
      this.param = {};
      this.sampleVisible = false;
      // 赋值样品
      this.currentSample = this.HaveJson(row);
      let list = await this.getCurrentProduct(row.id, 0);
      // 赋值样品检验项
      this.currentSample.insProduct = this.HaveJson(list);
      // 初始化后端传参
      this.param = {};
      this.changeType = 0;
      this.currentSample.insProduct.forEach((a, j) => {
        this.param[a.id] = {
          insValue: [],
          comValue: [],
          resValue: null,
          equipValue: [],
          equipName: [],
          insResult: null,
        };
      });
      // 处理页面列表数据
      this.getTableLists();
      this.currentKey = row.index;
      this.currentTab = null;
      // 向多线程去保存页面列表数据,后端传参,当前模板信息
      this.worker.postMessage(
        JSON.stringify({
          modelType: this.sampleProduct[0].model,
          type: "saveData",
          tableList: this.tableList,
          param: this.param,
          currentTable: this.currentTable,
        })
      );
    },
    async handleChangeCableTag(m, type, num, m2) {
      let cableTag = "";
      let repetitionTag = "";
      if (num === "cableTag") {
        cableTag = m2;
        this.temCableTag = cableTag;
        this.getRepetitionTag();
      }
      if (num === "repetitionTag") {
        repetitionTag = m2;
        this.repetitionTag = repetitionTag;
      }
      if (!m2) {
        type = 0;
      }
      this.changeType = type;
      if (m) {
        let list = await this.getCurrentProduct(m, type, cableTag);
        if (list && list.length > 0) {
          this.param = {};
          list.forEach((a) => {
            this.param[a.id] = {
              insValue: [],
              comValue: [],
              resValue: null,
              equipValue: [],
              equipName: [],
              insResult: null,
            };
          });
          this.getTableLists0(list);
          this.worker.postMessage(
            JSON.stringify({
              type: "saveData",
              tableList: this.tableList,
              param: this.param,
              currentTable: this.currentTable,
              bushing: m,
            })
          );
        } else {
          this.tableLists = [];
          this.tableList = [];
          this.$message.error("检验项为空");
        }
      }
    },
    // 改变任务
    handleChangeTask(row) {
      if (row) this.id = row.id;
      this.taskVisible = false;
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
      });
    },
    // 字典获取信息
    getComparisonList() {
      this.getDicts("coordinate_transformation").then((response) => {
        this.comparisonList = this.dictToValue(response.data);
      });
    },
    // 获取检验值为下拉时的下拉列表
    // selectEnumByCategoryOfSelect(val) {
    //   this.enumList = [];
    //   if (val === undefined || val === null) {
    //     return;
    //   }
    //   this.$axios
    //     .post(this.$api.enums.selectEnumByCategory, {
    //       category: val,
    //     })
    //     .then((res) => {
    //       this.enumList = res.data;
    //     });
    // },
    tableRowClassName({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    // 处理页面列表数据--去重,生成检验模板切换列表
    getTableLists() {
      const mySet1 = new Set();
      this.tableLists = this.currentSample.insProduct.filter((m) => {
        let num0 = mySet1.size;
        if (m.templateId != null && m.template != null) {
          try {
            mySet1.add(
              JSON.stringify({
                template: m.template,
                templateId: m.templateId,
              })
            );
          } catch (error) {
            console.log(222, error);
          }
        }
        let num1 = mySet1.size;
        if (num1 > num0) {
          return m;
        }
      });
      if (this.tableLists && this.tableLists.length > 0) {
        this.tableList = null;
        this.tableList = [this.tableLists[0]];
        this.currentTable = this.tableLists[0].templateId;
        // 处理页面列表数据
        this.handleTableData();
      }
    },
    // 光纤配置相关模板table列表
    getTableLists0(list) {
      const mySet1 = new Set();
      this.tableLists = list.filter((m) => {
        let num0 = mySet1.size;
        if (m.templateId != null && m.template != null) {
          try {
            mySet1.add(
              JSON.stringify({
                template: m.template,
                templateId: m.templateId,
              })
            );
          } catch (error) {
            console.log(333, error);
          }
        }
        let num1 = mySet1.size;
        if (num1 > num0) {
          return m;
        }
      });
      if (this.tableLists && this.tableLists.length > 0) {
        this.tableList = null;
        this.tableList = [this.tableLists[0]];
        this.currentTable = this.tableLists[0].templateId;
        this.currentSample.insProduct = this.HaveJson(list);
        // 处理页面列表数据
        this.handleTableData();
      }
    },
    // 处理页面列表数据
    handleTableData() {
      this.excelMethodList = []; //excel函数列表
      this.widthList = this.tableList[0].style.columnlen; //页面宽度--根据模板来的
      // 本次循环主要做页面渲染层面的处理--单元格合并预处理
      this.tableList.forEach((a) => {
        let mcList = a.template.filter(
          (b) =>
            b.v.mc != undefined &&
            b.v.mc.cs != undefined &&
            b.v.mc.rs != undefined
        );
        let count = 0;
        // 将 a.template 转换为 Map 以加速查找
        let templateMap = new Map();
        a.template.forEach((item) => {
          templateMap.set(`${item.c}-${item.r}`, item);
        });
        mcList.forEach((b) => {
          let cs = b.v.mc.cs;
          let rs = b.v.mc.rs;
          for (let i = 0; i < cs; i++) {
            for (let i2 = 0; i2 < rs; i2++) {
              let key = `${b.c + i}-${b.r + i2}`;
              let item = templateMap.get(key);
              if (item) {
                let bb = this.HaveJson(b);
                item.v.v = bb.v.v;
                item.v.ps = bb.v.ps;
                item.v.fc = bb.v.fc;
                item.v.fs = bb.v.fs;
                item.v.ht = bb.v.ht;
                item.mc = count;
              }
            }
          }
          count++;
        });
      });
      // 本次循环主要是控制合并,以及控制检验项信息是否展示出来,以便后续检验
      this.tableList.forEach((a) => {
        let dels = new Set(); //需要删除的行
        let ids = []; //所有检验项的id
        let set3 = new Set();
        a.template.forEach((b) => {
          let size1 = set3.size;
          let size2 = set3.add(b.r).size;
          if (size1 < size2) {
            let str = "";
            let str2 = "";
            let unit2 = "";
            let count4 = 0;
            let isThree = 0;
            a.template.forEach((c) => {
              // 获取到 检验项分类+检验项+检验子项的拼接,如果模板里的信息跟接口返回的检验项信息能够匹配则展示出来
              if (b.r === c.r) {
                if (
                  c.v.ps != undefined &&
                  c.v.ps.value === "检验项分类" &&
                  count4 === 0
                ) {
                  // 三级分类
                  isThree = 1;
                } else if (
                  c.v.ps != undefined &&
                  c.v.ps.value === "检验项" &&
                  count4 === 0
                ) {
                  // 二级分类
                  isThree = 0;
                }
                if (isThree == 0) {
                  if (c.v.ps != undefined && c.v.ps.value === "检验项") {
                    if (count4 === 0) {
                      if (c.v.v) {
                        c.v.v = c.v.v.replace(/\s*/g, "");
                      }
                      str += c.v.v;
                      count4 += 1;
                    }
                  } else if (
                    c.v.ps != undefined &&
                    c.v.ps.value === "检验子项"
                  ) {
                    if (count4 === 1) {
                      if (c.v.v) {
                        c.v.v = c.v.v.replace(/\s*/g, "");
                      }
                      str += c.v.v;
                      count4 += 1;
                    }
                  }
                } else if (isThree == 1) {
                  if (c.v.ps != undefined && c.v.ps.value === "检验项分类") {
                    if (count4 === 0) {
                      if (c.v.v) {
                        c.v.v = c.v.v.replace(/\s*/g, "");
                      }
                      str += c.v.v;
                      count4 += 1;
                    }
                  } else if (c.v.ps != undefined && c.v.ps.value === "检验项") {
                    if (count4 === 1) {
                      if (c.v.v) {
                        c.v.v = c.v.v.replace(/\s*/g, "");
                      }
                      str += c.v.v;
                      count4 += 1;
                    }
                  } else if (
                    c.v.ps != undefined &&
                    c.v.ps.value === "检验子项"
                  ) {
                    if (count4 === 2) {
                      if (c.v.v) {
                        c.v.v = c.v.v.replace(/\s*/g, "");
                      }
                      str += c.v.v;
                      count4 += 1;
                    }
                  }
                }
                if (str === "机械性能干态拉伸强度(纵向)") {
                  if (c.v.ps != undefined && c.v.ps.value === "单位") {
                    if (c.v.v) {
                      c.v.v = c.v.v.replace(/\s*/g, "");
                    }
                    str2 = str + c.v.v;
                    unit2 = c.v.v;
                  }
                }
              }
            });
            if (str != "") {
              let count2 = 0;
              for (let i in this.currentSample.insProduct) {
                let inspectionItemClass =
                  this.currentSample.insProduct[i].inspectionItemClass ==
                    null ||
                    this.currentSample.insProduct[i].inspectionItemClass ==
                    undefined
                    ? ""
                    : this.currentSample.insProduct[i].inspectionItemClass;
                inspectionItemClass = inspectionItemClass.replace(/\s*/g, "");
                let inspectionItem =
                  this.currentSample.insProduct[i].inspectionItem == null ||
                    this.currentSample.insProduct[i].inspectionItem == undefined
                    ? ""
                    : this.currentSample.insProduct[i].inspectionItem;
                inspectionItem = inspectionItem.replace(/\s*/g, "");
                let inspectionItemSubclass =
                  this.currentSample.insProduct[i].inspectionItemSubclass ==
                    null ||
                    this.currentSample.insProduct[i].inspectionItemSubclass ==
                    undefined
                    ? ""
                    : this.currentSample.insProduct[i].inspectionItemSubclass;
                inspectionItemSubclass = inspectionItemSubclass.replace(
                  /\s*/g,
                  ""
                );
                if (inspectionItemSubclass === "干态拉伸强度(纵向)") {
                  // 检验子项为'干态拉伸强度(纵向)'时，模版里是两个计算值对应相同的检验值并且计算方式不同，要根据相同的'单位'做特殊的渲染
                  const unit = this.currentSample.insProduct[i].unit;
                  if (
                    (this.currentSample.insProduct[i].templateId ===
                      a.templateId &&
                      inspectionItemClass +
                      inspectionItem +
                      inspectionItemSubclass +
                      unit ===
                      str2) ||
                    (this.currentSample.insProduct[i].templateId ===
                      a.templateId &&
                      !unit2.includes("/") &&
                      inspectionItemClass +
                      inspectionItem +
                      inspectionItemSubclass ===
                      str)
                  ) {
                    ids.push({
                      r: b.r,
                      id: this.currentSample.insProduct[i].id,
                      product: this.currentSample.insProduct[i],
                    });
                    break;
                  }
                } else {
                  // 如果相等,那么说明找到了,并且把id存起来,后续检验项也会在页面中显示出来
                  if (
                    this.currentSample.insProduct[i].templateId ===
                    a.templateId &&
                    inspectionItemClass +
                    inspectionItem +
                    inspectionItemSubclass ===
                    str
                  ) {
                    ids.push({
                      r: b.r,
                      id: this.currentSample.insProduct[i].id,
                      product: this.currentSample.insProduct[i],
                    });
                    break;
                  }
                }
                count2++;
              }
              if (count2 == this.currentSample.insProduct.length) {
                dels.add(b.r);
              }
            }
          }
        });
        // 操作删除
        dels.forEach((del) => {
          for (let b = 0; b < a.template.length; b++) {
            if (a.template[b].r === del) {
              a.template.splice(b, 1);
              b -= 1;
            }
          }
        });
        // 操作赋值--主要赋值单位,试验方法等信息
        ids.forEach((id) => {
          for (let b = 0; b < a.template.length; b++) {
            if (a.template[b].r === id.r) {
              a.template[b].i = id.id;
              if (
                a.template[b].v.ps != undefined &&
                a.template[b].v.ps.value === "单位"
              ) {
                a.template[b].v.v = id.product.unit;
              }
              if (
                a.template[b].v.ps != undefined &&
                (a.template[b].v.ps.value === "试验方法" ||
                  a.template[b].v.ps.value === "检测方法")
              ) {
                a.template[b].v.v = id.product.methodS;
              }
            }
          }
        });
        let set2 = new Set();
        // 合并的数据处理,cs  rs  代表合并的数量
        a.template.forEach((b) => {
          let size1 = set2.size;
          let size2 = set2.add(b.mc).size;
          if (b.mc != undefined && size1 < size2) {
            b.v.mc.rs = 0;
            b.v.mc.cs = 0;
            a.template.forEach((c) => {
              if (b.mc === c.mc) {
                if (b.r === c.r) {
                  b.v.mc.cs += 1;
                }
                if (b.c === c.c) {
                  b.v.mc.rs += 1;
                }
              }
            });
          }
        });
      });
      // 本次循环主要是对后端传参进行初始化,样式逻辑修改
      this.tableList.forEach((a) => {
        let arrs = [];
        let set = new Set();
        let count1 = 0;
        let conclusionList = []; //结论列表
        let finalList = []; //最终值列表
        // 结论与最终值在这里一一对应,以下两个列表长度肯定是一样的,如果有不一样,那么多半是模板配置得问题
        conclusionList = a.template.filter(
          (n) => n.v.ps != undefined && n.v.ps.value === "结论"
        ); //结论列表
        finalList = a.template.filter(
          (n) => n.v.ps != undefined && n.v.ps.value === "最终值"
        ); //最终值列表
        a.template.forEach((b) => {
          if (
            b.v.ps != undefined &&
            b.v.ps.value === "序号" &&
            (b.v.mc == undefined || Object.keys(b.v.mc).length === 4)
          ) {
            // 对序号进行赋值
            count1++;
            b.v.v = count1;
          }
          if (b.v.ps != undefined && b.v.ps.value === "要求值") {
            // 对要求值进行赋值
            b.v.v = this.getAsk(b.i);
          }
          // 对页面的和给后端传参的检验值,计算值,设备编码,设备名称,最终值,结论进行初始化
          if (
            b.v.ps != undefined &&
            typeof b.v.ps.value === "string" &&
            b.v.ps.value.includes("检验值")
          ) {
            this.$set(b.v, "v", "");
            // b.v.v = ''
            b.u = "";
            b.i && this.param[b.i] && this.param[b.i].insValue.push(b);
          }
          if (b.v.ps != undefined && b.v.ps.value === "计算值") {
            this.$set(b.v, "v", "");
            // b.v.v = ''
            b.i && this.param[b.i] && this.param[b.i].comValue.push(b);
          }
          if (b.v.ps != undefined && b.v.ps.value === "设备编码") {
            // b.v.v = ''
            this.$set(b.v, "v", "");
            b.i && this.param[b.i] && this.param[b.i].equipValue.push(b);
          }
          if (b.v.ps != undefined && b.v.ps.value === "设备名称") {
            this.$set(b.v, "v", "");
            // b.v.v = ''
            b.i && this.param[b.i] && this.param[b.i].equipName.push(b);
          }
          if (b.v.ps != undefined && b.v.ps.value === "最终值") {
            // b.v.v = ''
            this.$set(b.v, "v", "");
            if (
              b.i !== undefined &&
              this.param[b.i] &&
              !this.param[b.i].resValue
            ) {
              this.param[b.i].resValue = b;
            }
          }
          if (b.v.ps != undefined && b.v.ps.value === "结论") {
            if (
              b.i !== undefined &&
              this.param[b.i] &&
              !this.param[b.i].insResult
            ) {
              this.param[b.i].insResult = b;
              conclusionList.forEach((n, i) => {
                if (n.r == b.r && n.c == b.c) {
                  b.v.f = `(${this.comparisonList.find((j) => j.value == finalList[i].c)
                    .label
                    }${finalList[i].r + 1})`;
                }
              });
            }
          }
          set.add(b.r);
          // 如果模板列表的函数存在,那么加入到excel函数列表里面
          if (b.v.f) {
            this.excelMethodList.push(b);
          }
        });
        // 以下是样式处理逻辑
        set = [...set]
        // set = set.sort();
        set.forEach((b) => {
          let arr = [];
          a.template.forEach((c) => {
            if (c.r === b) {
              arr.push(c);
            }
          });
          arrs.push(arr);
        });
        a.arr = arrs;
        this.tableWidth = 0;
        for (let i = 0; i < arrs[0].length; i++) {
          this.tableWidth +=
            a.style.columnlen[i] === undefined ? 100 : a.style.columnlen[i];
        }
      });
      // 本次循环主要是对页面及后端传参进行初始化赋值
      this.currentSample.insProduct.forEach(async (a) => {
        try {
          // 计算值赋值
          let comValue = JSON.parse(a.insProductResult.comValue);
          for (var i = 0; i < comValue.length; i++) {
            if (
              this.param[a.id].comValue.find(
                (m) => m.c == comValue[i].c && m.r == comValue[i].r
              )
            ) {
              this.param[a.id].comValue.find(
                (m) => m.c == comValue[i].c && m.r == comValue[i].r
              ).v.v = this.toFixed(
                comValue[i].v,
                this.param[a.id].comValue.find(
                  (m) => m.c == comValue[i].c && m.r == comValue[i].r
                ).v.ct
              );
            } else if (!comValue[i].c || !comValue[i].r) {
              this.param[a.id].comValue[i].v.v = this.toFixed(
                comValue[i].v,
                this.param[a.id].comValue[i].v.ct
              );
            }
          }
        } catch (e) { }
        try {
          // 检验值赋值
          let insValue = JSON.parse(a.insProductResult.insValue);
          for (let i = 0; i < insValue.length; i++) {
            if (
              this.param[a.id].insValue.find(
                (m) => m.c == insValue[i].c && m.r == insValue[i].r
              )
            ) {
              this.param[a.id].insValue.find(
                (m) => m.c == insValue[i].c && m.r == insValue[i].r
              ).v.v = this.toFixed(
                insValue[i].v,
                this.param[a.id].insValue.find(
                  (m) => m.c == insValue[i].c && m.r == insValue[i].r
                ).v.ct
              );
              this.param[a.id].insValue.find(
                (m) => m.c == insValue[i].c && m.r == insValue[i].r
              ).u = insValue[i].u;
              // this.param[a.id].insValue[i].v.v = insValue[i].v
              // this.param[a.id].insValue[i].u = insValue[i].u
            }
          }
        } catch (e) { }
        try {
          // 设备编号赋值
          let equipValue = JSON.parse(a.insProductResult.equipValue);
          for (let i = 0; i < equipValue.length; i++) {
            // 普通设备赋值
            this.param[a.id].equipValue[i].v.v = equipValue[i].v;
          }
        } catch (e) { }
        try {
          // 设备名称赋值
          let equipName = JSON.parse(a.insProductResult.equipName);
          for (let i = 0; i < equipName.length; i++) {
            equipName[i].v !== "" &&
              equipName[i].v.map((val) => {
                const index = this.equipOptions.findIndex(
                  (item) => item.value === val
                );
                if (index > -1) {
                  // 根据设备编码转换为相应的设备名称
                  val = this.equipOptions[index].deviceName;
                }
              });
          }
          for (let i = 0; i < equipName.length; i++) {
            // 普通设备名称赋值
            this.param[a.id].equipName[i].v.v = equipName[i].v;
          }
        } catch (e) {
          console.log("设备名称赋值----", e);
        }
        try {
          // 最终值赋值
          this.param[a.id].resValue.v.v = this.toFixed(
            a.lastValue,
            this.param[a.id].resValue.v.ct
          );
          // 结论赋值
          this.param[a.id].insResult.v.v = a.insResult;
        } catch (e) { }
      });
      // 对excel函数进行处理
      this.handleExcelMethod();
    },
    // 检验值输入后触发的函数
    changeInput(m, code, n, getDataType, changeSelect) {
      // 为数采定义一个逻辑参数
      if (getDataType == "getDataType") {
        this.getDataType = 2;
      }
      let currentInsItemId = null; //当前检验项id
      if (n) {
        currentInsItemId = JSON.parse(JSON.stringify(n.i));
        // 定义一个函数来验证分数是否有效
        if (typeof n.v.v == "string") {
          function isValidFraction(fraction) {
            const [numerator, denominator] = fraction.split("/"); // 分子和分母
            return !(!denominator || !numerator);
          }
          const isTrue = isValidFraction(n.v.v);
          if (!isTrue) {
            n.v.v = n.v.v.replace("/", "");
          }
        }
      }
      try {
        // 向 Worker 发送消息，开始处理逻辑
        this.worker.postMessage(
          JSON.stringify({
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
            currentInsItem: n,
          })
        );
      } catch (error) {
        console.log(444, error);
      }

      // 监听 Worker 返回的结果
      this.worker.onmessage = (event) => {
        this.result = JSON.parse(event.data);
        switch (this.result.method) {
          case "saveInsContext":
            this.$nextTick(() => {
              // this.$delete(this.tableList[0],'arr')
              this.$set(
                this.tableList[0],
                "arr",
                this.result.value.tableList[0].arr
              );
              this.param = this.result.value.param;
              if (this.result.value.currentInsItemId) {
                currentInsItemId = this.result.value.currentInsItemId;
              }
              // 特殊处理一下结论,会有这种特殊情况
              for (var i in this.param) {
                if (
                  this.param[i].insResult &&
                  this.param[i].insResult.v &&
                  this.param[i].insResult.v.v
                ) {
                  if (this.param[i].insResult.v.v == "合格") {
                    this.$set(this.param[i].insResult.v, "v", 1);
                  } else if (this.param[i].insResult.v.v == "不合格") {
                    this.$set(this.param[i].insResult.v, "v", 0);
                  }
                }
              }
              this.saveInsContext(currentInsItemId);
            });
            break;
          case "tableList":
            this.$nextTick(() => {
              // 更新数据
              this.$delete(this.tableList[0], "arr");
              this.$set(this.tableList[0], "arr", this.result.value[0].arr);
              // this.param = this.result.value.param
              if (this.result.value.currentInsItem) {
                currentInsItemId = this.result.value.currentInsItem.i;
              }
            });
            break;
          case "getCurrentInsProduct":
            // 更新页面数据
            this.getCurrentInsProduct(this.result.value);
            break;
        }
      };
      // 保存数据
      setTimeout(() => {
        if (changeSelect) {
          this.saveInsContext(currentInsItemId);
        }
      }, 200);
    },
    // 根据后端传参更新页面数据   param => this.tableList[0].insProductResult
    getCurrentInsProduct(pId) {
      if (!this.tableList[0].insProductResult) {
        this.tableList[0].insProductResult = {};
      }
      for (let m in this.param[pId]) {
        let value = this.param[pId][m];
        switch (m) {
          case "comValue":
            // 赋值计算值
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                };
                this.tableList[0].insProductResult[m].push(obj);
              });
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(
                  this.tableList[0].insProductResult[m]
                );
              } catch (error) {
                console.log(555, error);
              }
            }
            break;
          // 赋值检验值
          case "insValue":
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                  u: a.u,
                };
                this.tableList[0].insProductResult[m].push(obj);
              });
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(
                  this.tableList[0].insProductResult[m]
                );
              } catch (error) {
                console.log(666, error);
              }
            }
            break;
          // 赋值设备编号
          case "equipValue":
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                };
                this.tableList[0].insProductResult[m].push(obj);
              });
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(
                  this.tableList[0].insProductResult[m]
                );
              } catch (error) {
                console.log(777, error);
              }
            }
            break;
          // 赋值设备名称
          case "equipName":
            if (value && value.length > 0) {
              this.tableList[0].insProductResult[m] = [];
              value.forEach((a, i) => {
                let obj = {
                  v: a.v.v,
                };
                this.tableList[0].insProductResult[m].push(obj);
              });
              try {
                this.tableList[0].insProductResult[m] = JSON.stringify(
                  this.tableList[0].insProductResult[m]
                );
              } catch (error) {
                console.log(888, error);
              }
            }
            break;
          // 赋值最终值
          case "resValue":
            this.tableList[0].lastValue = value ? value.v.v : "";
            break;
          // 赋值结论
          case "insResult":
            this.tableList[0].insResult = value ? value.v.v : "";
            break;
        }
      }
    },
    // 对EXCEL函数进行处理
    handleExcelMethod() {
      if (this.excelMethodList.length > 0) {
        this.excelMethodList.map((item) => {
          // 得到每个函数的参数列表
          item.valueList = excelFunction.changeParameter(item.v.f);
          return item;
        });
      }
    },
    getValue(v) {
      // 对页面展示数据进行处理,@,代表换行
      let str = v.v
        ? v.v
        : v.v === 0
          ? v.v
          : v.ct && v.ct.s
            ? v.ct.s.length > 0 &&
            v.ct.s[0].v
              .replace(new RegExp("\n", "g"), "<br/>")
              .replace(new RegExp("@", "g"), "<br/>")
            : "";
      // 对数据保留小数点进行处理
      if (v.ct && v.ct.fa && v.ct.fa.includes(".") && str) {
        let num = 0;
        let str0 = v.ct.fa.split(".")[1];
        num = str0.length;
        str = Number(str).toFixed(num);
      }
      if (v.v && typeof v.v == "string" && v.v.includes("@")) {
        str = v.v.replace(new RegExp("@", "g"), "<br/>");
      }
      return str;
    },
    // 获取当前输入框类型
    getInspectionValueType(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].inspectionValueType;
        }
      }
    },
    // 获取要求描述
    getTell(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].tell;
        }
      }
    },
    // 动态获取单元格宽度
    handleWidth(n) {
      let sum = 0;
      if (n.v.mc && n.v.mc.cs && n.v.mc.c != undefined) {
        for (let i = 0; i < n.v.mc.cs; i++) {
          let num = this.widthList[i + n.v.mc.c]
            ? this.widthList[i + n.v.mc.c]
            : 100;
          sum += num;
        }
      } else {
        sum = this.widthList[n.c] ? this.widthList[n.c] : 100;
      }
      return sum;
    },
    // 对输入值进行格式校验
    handleInput(n) {
      try {
        n.v.v = n.v.v.replace(/[^\d.^e>＞≥≤<＜断裂脆化\-/+]/g, "");
        n.v.v = n.v.v.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
        n.v.v = n.v.v
          .replace(".", "$#$")
          .replace(/\./g, "")
          .replace("$#$", ".");
        n.v.v = n.v.v.replace(/\/{2,}/g, "/"); //只保留第一个/清除多余的
        n.v.v = n.v.v
          .replace("/", "$#$")
          .replace(/\//g, "")
          .replace("$#$", "/");
      } catch (error) {
        console.log(error);
      }
    },
    getInspectionItemType(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].inspectionItemType;
        }
      }
    },
    // 获取要求值
    getAsk(id) {
      for (var a in this.currentSample.insProduct) {
        if (this.currentSample.insProduct[a].id == id) {
          return this.currentSample.insProduct[a].ask;
        }
      }
    },
    // 获取所有设备
    getEquipOptions(e, id) {
      if (e) {
        this.equipOptions = [];
        search({ status: 0 }).then((res) => {
          if (res.code === 200 && res.data) {
            this.equipOptions = res.data.map((m) => {
              m.value = m.managementNumber;
              m.label = m.deviceName;
              return m;
            });
          }
        })
          .catch((error) => {
            console.error(error);
          });
      }
    },
    // getDic(e, id) {
    //   if (e) {
    //     for (var a in this.currentSample.insProduct) {
    //       if (this.currentSample.insProduct[a].id == id) {
    //         let str = this.currentSample.insProduct[a].dic;
    //         this.selectEnumByCategoryOfSelect(str);
    //         return str;
    //       }
    //     }
    //   }
    // },
    openAddCheck() {
      this.addCheck = true;
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
          userId: this.checkUser,
        }).then((res) => {
          if (res.code === 200) {
            this.$message.success("操作成功");
            this.goback();
            this.addCheck = false;
          }
          this.reviewLoading = false;
        })
          .catch((error) => {
            console.error(error);
            this.reviewLoading = false;
          });
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
          tell: this.noReason,
        }).then((res) => {
          if (res.code === 200) {
            this.$message.success("操作成功");
            this.goback();
          }
          this.reviewLoading = false;
        })
          .catch((error) => {
            console.error(error);
            this.reviewLoading = false;
          });
      } else {
        this.$message.error("未输入不通过原因");
      }
    },
    // 查看工时
    viewManHour() {
      this.$refs.viewManHourDia.showDialog(this.id, 99);
    },
    openAddVerifyDia() {
      this.addVerifyDia = true;
    },
    submit() {
      if (this.verifyUser === null || this.verifyUser === "") {
        this.$message.error("请指定复核人员");
        return;
      }
      if (!this.otherForm.humidity) {
        this.$message.error("请输入湿度");
        return;
      }
      if (!this.otherForm.temperature) {
        this.$message.error("请输入温度");
        return;
      }
      this.submitLoading = true;
      checkSubmitPlan({
        orderId: this.orderId,
        laboratory: this.sonLaboratory,
      }).then((res) => {
        if (res.code === 200) {
          if (!res.data || res.data.length == 0) {
            this.submitLoading = true;
            submitPlan({
              orderId: this.orderId,
              laboratory: this.sonLaboratory,
              verifyUser: this.verifyUser,
              entrustCode: this.insOrder.entrustCode,
            }).then((res) => {
              if (res.code === 200) {
                this.$message.success("操作成功");
                this.goback();
                this.submitLoading = false;
                this.addVerifyDia = false;
              }
            })
              .catch((error) => {
                console.error(error);
                this.submitLoading = false;
              });
          } else {
            let newData = [];
            const h = this.$createElement;
            for (let i in res.data) {
              const lastChar = res.data[i].slice(-1);
              if (lastChar == "-") {
                res.data[i] = res.data[i].slice(0, -1);
              }
              newData.push(
                h(
                  "p",
                  { style: "font-size: 14px;color: red;" },
                  Number(i) + 1 + "、" + res.data[i]
                )
              );
            }
            newData.push(
              h(
                "p",
                {
                  style:
                    "font-size: 16px;color:#000;margin-top:12px;overflow-y: auto;max-height:80vh",
                },
                "以上项目不合格，确定提交？"
              )
            );
            this.$confirm("提示", {
              title: "提示",
              message: h("div", null, newData),
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "",
            })
              .then(() => {
                this.submitLoading = true;
                submitPlan({
                  orderId: this.orderId,
                  laboratory: this.sonLaboratory,
                  verifyUser: this.verifyUser,
                }).then((res) => {
                  if (res.code === 200) {
                    this.$message.success("操作成功");
                    this.addVerifyDia = false;
                    this.goback();
                  }
                  this.submitLoading = false;
                })
                  .catch((error) => {
                    console.error(error);
                    this.submitLoading = false;
                  });
              })
              .catch(() => { });
          }
        }
      })
        .catch((error) => {
          console.error(error);
          this.submitLoading = false;
        });
      return;
    },
    // 统一在这里保存数据
    saveInsContext(currentInsItemId) {
      try {
        if (this.param) {
          let param = null;
          if (currentInsItemId) {
            param = { [currentInsItemId]: this.param[currentInsItemId] };
          } else {
            param = this.param;
          }
          saveInsContext({
            param: JSON.stringify(param),
            currentTable: this.currentTable,
            sampleId: this.currentSample.id,
            orderId: this.orderId,
            sonLaboratory: this.sonLaboratory
          }).then((res) => {
            this.$message.success("已保存");
          });
          // 向 Worker 发送消息，开始处理逻辑
          this.worker.postMessage(
            JSON.stringify({
              modelType: this.sampleProduct[0].model,
              type: "saveData",
              tableList: this.tableList,
              param: this.param,
              currentTable: this.currentTable,
            })
          );
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
              this.param[m.i].state = 1;
            }
            // if(m.i==n.i&&m.v.ps&&m.v.ps.value=='设备名称'&&v){
            //   this.$set(m.v,'v',v)
            // }
          });
        });
        for (let i in this.param) {
          if (this.param[i].state != 1) {
            delete this.param[i];
          }
        }
        // this.equipOptions为设备名称下拉框选项数据
        for (let i1 in this.param[n.i].equipName) {
          if (
            this.param[n.i].equipName[i1].i === n.i &&
            this.param[n.i].equipName[i1].r === n.r
          ) {
            this.$delete(this.param[n.i].equipValue[i1].v, "v");
            // 将数组赋值给设备编码
            this.$set(this.param[n.i].equipValue[i1].v, "v", val.join("，"));
            this.$delete(this.param[n.i].equipName[i1].v, "v");
            // 将数组赋值给设备编码
            this.$set(this.param[n.i].equipName[i1].v, "v", val);
            this.tableList[0].arr.forEach((item, index) => {
              item.forEach((m) => {
                if (m.i == n.i && m.v.ps && m.v.ps.value == "设备编码") {
                  this.$set(m.v, "v", val.join("，"));
                }
                if (m.i == n.i && m.v.ps && m.v.ps.value == "设备名称") {
                  this.$set(m.v, "v", val);
                }
              });
            });
          }
        }
        // 保存数据
        this.saveInsContext(n.i);
      } catch (e) {
        console.log("changeEquip----", e);
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
      const nav = document.getElementById("nav");
      var flag; // 鼠标按下
      var downX; // 鼠标点击的x下标
      var scrollLeft; // 当前元素滚动条的偏移量
      nav.addEventListener("mousedown", function (event) {
        flag = true;
        downX = event.clientX; // 获取到点击的x下标
        scrollLeft = this.scrollLeft; // 获取当前元素滚动条的偏移量
      });
      nav.addEventListener("mousemove", function (event) {
        if (flag) {
          // 判断是否是鼠标按下滚动元素区域
          var moveX = event.clientX; // 获取移动的x轴
          var scrollX = moveX - downX; // 当前移动的x轴下标减去刚点击下去的x轴下标得到鼠标滑动距离
          this.scrollLeft = scrollLeft - scrollX; // 鼠标按下的滚动条偏移量减去当前鼠标的滑动距离
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
    // 页面里面切换样品
    async caretSample(num) {
      let index = this.currentKey + num;
      if (index < 1) {
        this.$message.error("当前是第一个样品");
        return;
      } else if (index > this.sampleProduct.length) {
        this.$message.error("当前是最后一个样品");
        return;
      }
      this.currentKey = index;
      this.currentSample = this.HaveJson(this.sampleProduct[index - 1]);
      // 获取检验项
      let list = await this.getCurrentProduct(this.currentSample.id, 0);
      this.currentSample.insProduct = this.HaveJson(list);
      // 初始化后端传参
      this.param = {};
      this.changeType = 0;
      this.currentSample.insProduct.forEach((a, j) => {
        this.param[a.id] = {
          insValue: [],
          comValue: [],
          resValue: null,
          equipValue: [],
          equipName: [],
          insResult: null,
        };
      });
      // 页面数据处理
      this.getTableLists();
      // 更新到多线程
      this.worker.postMessage(
        JSON.stringify({
          modelType: this.sampleProduct[0].model,
          type: "saveData",
          tableList: this.tableList,
          param: this.param,
          currentTable: this.currentTable,
        })
      );
    },
    handleSuccessUp(response) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success("上传成功");
        this.getList();
      }
    },
    beforeUpload(file) {
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error("上传文件不超过10M");
        this.$refs.upload.clearFiles();
        return false;
      } else {
        this.upLoading = true;
        return true;
      }
    },
    onError(err, file, fileList) {
      this.$message.error("上传失败");
      this.$refs.upload.clearFiles();
    },
    handleDown(row) {
      downFile({ id: row.id }).then((res) => {
        this.$download.saveAs(res.data.fileUrl, row.fileName)
      })
        .catch((error) => { });
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
        if (ct.fa.includes(".")) {
          let num = ct.fa.slice(4).length;
          return Number(v).toFixed(num);
        } else {
          return v;
        }
      } else {
        return v;
      }
    },
    goback() {
      this.$router.go(-1)
    }
  },
};
</script>
<style scoped>
.custom-table .el-table__header-wrapper th {
  background-color: #87ceeb;
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
  padding: 0 16px;
}

.search {
  width: 100%;
  background-color: #fff;
  border-radius: 3px;
  margin-bottom: 10px;
}

.search .form-inline {
  padding-top: 20px;
  padding-left: 0px;
  text-align: left;
}

.center {
  width: 100%;
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
  border-collapse: collapse;
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
  opacity: 0.7;
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
  opacity: 0.8;
}

>>>input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
  /* 隐藏微调按钮 */
  margin: 0 !important;
  /* 移除微调按钮的边距 */
}

>>>input[type="number"] {
  -moz-appearance: textfield !important;
  /* 针对 Firefox */
}

>>>.el-form-item__content {
  display: inline-flex;
  align-items: center;
}
</style>
<style scoped>
/* .inspection .el-form-item__label {
  color: #000;
} */

.inspection .el-drawer__header::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 30.24px;
  background: #3a7bfa;
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
  color: #3a7bfa;
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
</style>
