<!-- 设备工具明细 -->
<template>
	<div class="role_manage">
    <div class="search" v-show="!showData">
      <div>
        <el-form :model="queryParams" ref="queryParams" size="small" :inline="true">
          <el-form-item label="状态" prop="deviceStatus" class="form-item">
            <el-select v-model="queryParams.deviceStatus" clearable
                       placeholder="全部" size="small" @change="refreshTable">
              <el-option v-for="item in deviceStatusList" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设备名称" prop="deviceName">
            <el-input size="small" placeholder="请输入" clearable v-model="queryParams.deviceName"
                      @keyup.enter.native="refreshTable()">
            </el-input>
          </el-form-item>
          <el-form-item label="规格型号" prop="specificationModel">
            <el-input size="small" placeholder="请输入" clearable
                      v-model="queryParams.specificationModel" @keyup.enter.native="refreshTable()"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="refreshTable">查询</el-button>
            <el-button size="mini" @click="refresh">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button size="small" type="primary" @click="dialogVisible2 = true">新增</el-button>
        <el-button size="small" type="primary" @click="handleDownOne">导出</el-button>
      </div>
    </div>
		<div class="table" v-show="!showData">
			<lims-table :tableData="tableData" :column="column" :tableLoading="tableLoading" :height="'calc(100vh - 300px)'"
				:page="page" @pagination="pagination"></lims-table>
		</div>
		<el-dialog :title="isUp ? '设备详情' : '档案修订'" :visible.sync="dialogVisible" width="70%" top="5vh"
			:before-close="handleClose">
			<el-row style="display:flex;justify-content: space-around;max-height: 75vh;overflow-y: auto;">
				<!-- 左边布局 -->
				<el-col :span="7">
					<el-col>
						<!-- 图片 -->
						<el-image class="img" style="width:100%;height: 320px;marginBottom:16px"
							:src="javaApi + '/img/' + formData.imageUpload">
							<div slot="error" class="image-error" style="width: calc(100% -2px);
            height: 318px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 1px solid #EEEEEE;">
								<i class="el-icon-picture-outline" style="font-size:60px;color:#666666;"></i>
							</div>
						</el-image>
						<!-- 表单 -->
						<el-form :label-position="labelPosition" :model="formData" label-width="120px">
							<el-form-item label="仪器名称:" required prop="deviceName">
								<el-input :disabled="isUp" v-model="formData.deviceName" size="small"></el-input>
							</el-form-item>
							<el-form-item label="仪器名称EN:" required>
								<el-input :disabled="isUp" v-model="formData.enDeviceName" size="small"></el-input>
							</el-form-item>
							<el-form-item label="规格型号:" required>
								<el-input :disabled="isUp" v-model="formData.specificationModel" size="small"></el-input>
							</el-form-item>
							<el-form-item label="生产厂家:">
								<el-input :disabled="isUp" v-model="formData.manufacturer" size="small"></el-input>
							</el-form-item>
						</el-form>
					</el-col>
				</el-col>
				<!-- 中间布局 -->
				<el-col :span="7">
					<el-form :label-position="labelPosition" :model="formData" label-width="116px">
						<el-form-item label="校准服务机构:">
							<el-input :disabled="isUp" v-model="formData.calibrationServices" size="small"></el-input>
						</el-form-item>
						<el-form-item label="资产编码:">
							<el-input v-model="formData.assetCode" :disabled="isUp" size="small"></el-input>
						</el-form-item>
						<el-form-item label="产地:">
							<el-input v-model="formData.origin" :disabled="isUp" size="small"></el-input>
						</el-form-item>
						<el-form-item label="出厂编号:">
							<el-input :disabled="isUp" v-model="formData.factoryNo" size="small"></el-input>
						</el-form-item>
						<el-form-item label="管理编号:" required>
							<el-input :disabled="isUp" v-model="formData.managementNumber" size="small"></el-input>
						</el-form-item>
						<el-form-item label="购置日期:">
							<el-date-picker :disabled="isUp" style="width:100%" v-model="formData.acquisitionDate" type="date"
								format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="校准有效期:" required>
							<el-date-picker :disabled="isUp" style="width:100%" v-model="formData.activationDate" type="date"
								format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="负责人:">
							<el-select v-model="formData.equipmentManager" placeholder="请选择" size="small" :disabled="isUp"
								style="width:100%">
								<el-option v-for="item in responsiblePersonList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="存放点:">
							<el-input :disabled="isUp" v-model="formData.storagePoint" size="small"></el-input>
						</el-form-item>
						<el-form-item label="技术指标:">
							<el-input :disabled="isUp" v-model="formData.technicalIndicators" :rows="7" type="textarea"
								size="small"></el-input>
						</el-form-item>
					</el-form>
				</el-col>
				<!-- 右边布局 -->
				<el-col :span="7">
					<el-form :label-position="labelPosition" :model="formData" label-width="140px" ref="ruleForm">
						<!-- 实验室列表 -->
						<el-form-item label="所属部门:" required>
							<el-select :disabled="isUp" v-model="formData.subordinateDepartmentsId" placeholder="请选择" size="small"
								style="width:100%">
								<el-option v-for="item in subordinateDepartmentsList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="检测项目:">
							<el-cascader :disabled="isUp" v-model="formData.insProductIds" :options="options" :show-all-levels="false"
								:props="props" placeholder="请选择" size="small" style="width:100%;" :collapse-tags="!isUp" separator=","
								filterable clearable></el-cascader>
						</el-form-item>
						<el-form-item label="最近校准日期:">
							<el-date-picker :disabled="isUp" style="width:100%" v-model="formData.lastCalibrationDate"
								format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="下次校准日期:">
							<el-date-picker :disabled="isUp" style="width:100%" v-model="formData.nextCalibrationDate"
								format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="设备类型:">
							<el-select :disabled="isUp" v-model="formData.largeCategory" placeholder="请选择" size="small"
								style="width:100%">
								<el-option v-for="item in equipmentList" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="单价(万元):">
							<el-input :disabled="isUp" v-model="formData.unitPrice" size="small"></el-input>
						</el-form-item>
						<el-form-item label="当前状态:" required>
							<el-select :disabled="isUp" v-model="formData.deviceStatus" placeholder="请选择" size="small"
								style="width:100%">
								<el-option v-for="item in deviceStatusList" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="校准周期（月）:" required>
							<el-input :disabled="isUp" v-model="formData.calibrationDate" size="small"></el-input>
						</el-form-item>
						<el-form-item label="被授权人:">
							<el-select v-model="formData.authorizedPerson" multiple :disabled="isUp" placeholder="请选择" size="small"
								style="width:100%">
								<el-option v-for="item in responsiblePersonList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="图片:">
							<div
								style="border: 1px solid #DCDFE6;border-radius:4px;height:32px;lineHeight:32px;display:flex;justify-content: space-around;font-size: 13px;">
								<div v-show="formData.imageName" class="picName">{{ formData.imageName }}</div>
								<el-upload :disabled="isUp" :action="action" :on-success="handleSuccessUpImg2" :show-file-list="false"
									accept='image/jpg,image/jpeg,image/png' :headers="uploadHeader" :on-change="beforeUpload"
									:on-error="onError" ref='upload'>
									<el-button type="text" style="height:30px;padding-top:8px">上传</el-button>
								</el-upload>
							</div>
						</el-form-item>
					</el-form>
				</el-col>
			</el-row>
			<span slot="footer" class="dialog-footer">
				<el-row v-if="!isUp">
					<el-button @click="handleClose">取 消</el-button>
					<el-button type="primary" @click="submitForm" :loading="upLoad">确 定</el-button>
				</el-row>
			</span>
		</el-dialog>
		<!-- 新增-->
		<el-dialog title="新增设备" top="5vh" :visible.sync="dialogVisible2" width="70%" :before-close="handleClose2">
			<el-row style="display:flex;justify-content: space-around;">
				<!-- 左边布局 -->
				<el-col :span="7">
					<el-col>
						<!-- 图片 -->
						<el-image class="img" style="width:100%;height: 320px;margin-bottom:16px"
							:src="javaApi + '/img/' + formData2.imageUpload">
							<div slot="error" class="image-error" style="width: calc(100% -2px);
          height: 318px;
          border-radius: 16px;
          display: flex;
          align-items: center;
          justify-content: center;
          border: 1px solid #EEEEEE;">
								<i class="el-icon-picture-outline" style="font-size:60px;color:#666666;"></i>
							</div>
						</el-image>
						<!-- 表单 -->
						<el-form :label-position="labelPosition" :model="formData2" label-width="120px">
							<el-form-item label="仪器名称:" required>
								<el-input v-model="formData2.deviceName" size="small"></el-input>
							</el-form-item>
							<el-form-item label="仪器名称EN:" required>
								<el-input v-model="formData2.enDeviceName" size="small"></el-input>
							</el-form-item>
							<el-form-item label="规格型号:" required>
								<el-input v-model="formData2.specificationModel" size="small"></el-input>
							</el-form-item>
							<el-form-item label="生产厂家:">
								<el-input v-model="formData2.manufacturer" size="small"></el-input>
							</el-form-item>
						</el-form>
					</el-col>
				</el-col>
				<!-- 中间布局 -->
				<el-col :span="7">
					<el-form :label-position="labelPosition" :model="formData2" label-width="110px">
						<!-- <el-form-item label="生产厂家EN:">
              <el-input v-model="formData2.factoryNo" size="small"></el-input>
            </el-form-item> -->
						<el-form-item label="校准服务机构:">
							<el-input v-model="formData2.calibrationServices" size="small"></el-input>
						</el-form-item>
						<el-form-item label="资产编码:">
							<el-input v-model="formData2.assetCode" size="small"></el-input>
						</el-form-item>
						<el-form-item label="产地:">
							<el-input v-model="formData2.origin" size="small"></el-input>
						</el-form-item>
						<el-form-item label="出厂编号:">
							<el-input v-model="formData2.factoryNo" size="small"></el-input>
						</el-form-item>
						<el-form-item label="管理编号:" required>
							<el-input v-model="formData2.managementNumber" size="small"></el-input>
						</el-form-item>
						<el-form-item label="购置日期:">
							<el-date-picker style="width:100%" v-model="formData2.acquisitionDate" type="date" format="yyyy-MM-dd"
								value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="校准有效期:" required>
							<el-date-picker style="width:100%" v-model="formData2.activationDate" type="date" format="yyyy-MM-dd"
								value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="负责人:">
							<el-select v-model="formData2.equipmentManager" placeholder="请选择" size="small" style="width:100%">
								<el-option v-for="item in responsiblePersonList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="存放点:">
							<el-input v-model="formData2.storagePoint" size="small"></el-input>
						</el-form-item>
						<el-form-item label="技术指标:">
							<el-input v-model="formData2.technicalIndicators" :rows="7" type="textarea" size="small"></el-input>
						</el-form-item>
					</el-form>
				</el-col>
				<!-- 右边布局 -->
				<el-col :span="7">
					<el-form :label-position="labelPosition" :model="formData2" label-width="120px" ref="ruleForm">
						<!-- 实验室列表 -->
						<el-form-item label="所属部门:" required>
							<el-select v-model="formData2.subordinateDepartmentsId" placeholder="请选择" size="small" style="width:100%">
								<el-option v-for="item in subordinateDepartmentsList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="检测项目:">
							<el-cascader v-model="formData2.insProductIds" :options="options" :show-all-levels="false" :props="props"
								placeholder="请选择" size="small" style="width:100%" collapse-tags separator="," filterable
								clearable></el-cascader>
						</el-form-item>
						<el-form-item label="最近校准日期:">
							<el-date-picker style="width:100%" v-model="formData2.lastCalibrationDate" format="yyyy-MM-dd"
								value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="下次校准日期:">
							<el-date-picker style="width:100%" v-model="formData2.nextCalibrationDate" format="yyyy-MM-dd"
								value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
							</el-date-picker>
						</el-form-item>
						<el-form-item label="设备类型:">
							<el-select v-model="formData2.largeCategory" placeholder="请选择" size="small" style="width:100%">
								<el-option v-for="item in equipmentList" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="单价(万元):">
							<el-input v-model="formData2.unitPrice" size="small"></el-input>
						</el-form-item>
						<el-form-item label="当前状态:" required>
							<el-select v-model="formData2.deviceStatus" placeholder="请选择" size="small" style="width:100%">
								<el-option v-for="item in deviceStatusList" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="校准周期（月）:" required>
							<el-input v-model="formData2.calibrationDate" size="small"></el-input>
						</el-form-item>
						<el-form-item label="被授权人:">
							<el-select v-model="formData2.authorizedPerson" multiple placeholder="请选择" size="small"
								style="width:100%">
								<el-option v-for="item in responsiblePersonList" :key="item.value" :label="item.label"
									:value="item.value">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="图片:">
							<div
								style="border: 1px solid #DCDFE6;border-radius:4px;height:32px;line-height:32px;display:flex;justify-content: space-around;font-size: 13px;">
								<div v-show="formData2.imageName" class="picName">{{ formData2.imageName }}</div>
								<el-upload :action="action" :on-success="handleSuccessUpImg2" :show-file-list="false"
									accept='image/jpg,image/jpeg,image/png' :headers="uploadHeader" :on-change="beforeUpload"
									:on-error="onError" ref='upload'>
									<el-button type="text" style="height:30px;padding-top:8px">上传</el-button>
								</el-upload>
							</div>
						</el-form-item>
					</el-form>
				</el-col>
			</el-row>
			<span slot="footer" class="dialog-footer">
				<el-row>
					<el-button @click="handleClose2">取 消</el-button>
					<el-button type="primary" @click="submitForm2" :loading="upLoad2">确 定</el-button>
				</el-row>
			</span>
		</el-dialog>
		<el-dialog title="数采配置" :visible.sync="dialogVisible3" width="400px">
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>IP：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.ip"></el-input>
			</div>
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>采集地址：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.collectUrl"></el-input>
			</div>
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>储存地址：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.storageUrl"></el-input>
			</div>
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>参照：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.refer"></el-input>
			</div>
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>X：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.x"></el-input>
			</div>
			<div class="search_thing" style="margin-bottom: 14px;">
				<div class="search_label"><span style="color:red;margin-right: 4px;">*</span>Y：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.y"></el-input>
			</div>
			<div class="search_thing">
				<div class="search_label">公式：</div>
				<el-input size="small" placeholder="请输入" clearable v-model="configForm.formula"></el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-row>
					<el-button @click="dialogVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="submitForm3" :loading="upLoad3">确 定</el-button>
				</el-row>
			</span>
		</el-dialog>
		<dataAcquisitionConfig v-if="showData" :deviceId="deviceId" />
	</div>
</template>

<script>
import limsTable from "@/components/Table/lims-table.vue";
import dataAcquisitionConfig from './acquisitionConfig.vue'
import {
	obtainItemParameterList,
	exportEquipmentDetails,
	getInsProduction,
	upDeviceParameter,
	addDeviceParameter,
	numberCollect,
	getDeviceParameter,
	delDeviceParameter,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
export default {
	props: {
		clickNodeVal: {
			type: Object,
			default: () => {
				return {};
			}
		}
	},
	components: {
		limsTable,
		dataAcquisitionConfig,
	},
	data() {
		return {
			dateFormat: 'yyyy-MM-dd',
			deviceId: '',
			fileTypeOptions: [
				{ label: 'csv', value: '.csv' },
				{ label: 'db', value: '.db' },
				{ label: 'mdb', value: '.mdb' },
				{ label: 'word', value: '.docx' },
				{ label: 'excel', value: '.xlsx' },
				{ label: 'txt', value: '.txt' },
				{ label: 'png', value: '.png' },
			],
			//是否是档案修订  true不是 false是
			isUp: true,
			formData: {
				authorizedPerson: []
			},
			formData2: {
				imageUpload: '',
				imageName: '',
				deviceStatus: '',
				authorizedPerson: []
			},
			value: '',
			props: { multiple: true, emitPath: false, value: 'id', label: 'name' },
			options: [],
			labelPosition: 'right',
			dialogVisible: false,
			dialogVisible2: false,
			showData: false, // 数采配置页面
			tableList: [],
			addDia: true,
			//设备类型列表
			equipmentList: [],
			// 负责人列表
			responsiblePersonList: [],
			// 授权人列表
			authorizerList: [],
			// 当前状态列表
			deviceStatusList: [],
			// 所属部门
			subordinateDepartmentsList: [],
			upLoad: false,
			upLoad2: false,
			dialogVisible3: false,
			upLoad3: false,
			configForm: {},
			laboratoryNameIsNull: false,
			queryParams: {},
			tableData: [],
			column: [
				{ label: "设备名称", prop: "deviceName", dataType: 'link', linkMethod: 'selectAllByOne' },
				{ label: "设备名称EN", prop: "enDeviceName" },
				{
					label: "规格型号",
					prop: "specificationModel",
				},
				{ label: "生产厂家", prop: "manufacturer" },
				{ label: "出厂编号", prop: "factoryNo" },
				{ label: "管理编号", prop: "managementNumber" },
				{ label: "技术指标", prop: "technicalIndicators" },
				{ label: "购置日期", prop: "acquisitionDate" },
				{ label: "启用日期", prop: "activationDate" },
				{ label: "管理人", prop: "equipmentManagerUser" },
				{ label: "存放点", prop: "storagePoint" },
				{ label: "所属部门", prop: "laboratoryName" },
				{ label: "检验项目", prop: "insProductItem" },
				{ label: "校准服务机构", prop: "calibrationServices" },
				{ label: "最近校准日期", prop: "lastCalibrationDateTwo" },
				{ label: "最近核查日期", prop: "lastCalibrationDate" },
				{ label: "下次校准日期", prop: "nextCalibrationDateTwo" },
				{ label: "下次核查日期", prop: "nextCalibrationDate" },
				{
					label: "设备分类", prop: "largeCategory", dataType: "tag",
					formatData: (params) => {
            if (this.equipmentList.find(m => m.value == params)) {
						  return this.equipmentList.find(m => m.value == params).label
            } else {
              return null
            }
					},
					formatType: (params) => {
            if (this.equipmentList.find(m => m.value == params)) {
              return this.equipmentList.find(m => m.value == params).type
            } else {
              return null
            }
					}
				},
				{ label: "单价", prop: "unitPrice" },
				{
					label: "设备状态", prop: "deviceStatus", dataType: "tag",
					formatData: (params) => {
						return this.deviceStatusList.find(m => m.value == params).label
					},
					formatType: (params) => {
						return this.deviceStatusList.find(m => m.value == params).type
					}
				},
				{ label: "校准周期(月)", prop: "calibrationDate" },
				{
					dataType: "action",
					fixed: "right",
					label: "操作",
					operation: [
						{
							name: "档案修订",
							type: "text",
							clickFun: (row) => {
								this.isUpdate(row);
							},
						},
						{
							name: "数采配置",
							type: "text",
							clickFun: (row) => {
								this.handleConfig(row);
							},
							disabled: (row) => {
								return row.insProductItem == null || row.insProductItem === ''
							}
						},
						{
							name: "删除",
							type: "text",
							clickFun: (row) => {
								this.handleDelete(row);
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
		}
	},
	computed: {
		action() {
			return this.javaApi + '/deviceScope/uploadFile'
		}
	},
	mounted() {
		this.selectEnumByCategory()
		this.selectDevicePrincipal()
		this.obtainItemParameterList()
		this.getInsProductIds()
		// 初始化
		this.clickSidebar(this.clickNodeVal)
	},
	methods: {
		//分类
		handleNotification(cate) {
			this.queryParams.largeCategory = cate
		},
		obtainItemParameterList() {
			obtainItemParameterList().then(res => {
				let data = []
				res.data.forEach(a => {
					data.push({
						label: a.laboratoryName,
						value: a.id
					})
				})
				this.subordinateDepartmentsList = data
			})
		},
		getList() {
			this.tableLoading = true;
			let param = { laboratoryNameIsNull: this.laboratoryNameIsNull, ...this.queryParams, ...this.page };
			delete param.total;
			getDeviceParameter({ ...param })
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
		refresh() {
			this.queryParams = {};
			this.page.current = 1;
			this.getList();
		},
		refreshTable() {
			this.page.current = 1;
			this.getList();
		},
		// 导出
		handleDownOne() {
			this.outLoading = true
			exportEquipmentDetails().then(res => {
				this.outLoading = false
				const blob = new Blob([res], { type: 'application/octet-stream' });
				this.$download.saveAs(blob, '设备仪器一览表.doc')
			})
		},
		// 获取字典
		selectEnumByCategory() {
			// 设备状态
			this.getDicts("device_status").then((response) => {
				this.deviceStatusList = this.dictToValue(response.data);
			});
			// 设备分类
			this.getDicts("device_type").then((response) => {
				this.equipmentList = this.dictToValue(response.data);
			});
		},
		// 获取负责人列表
		selectDevicePrincipal() {
			selectUserCondition().then(res => {
				let data = [];
				res.data.forEach((a) => {
					data.push({
						label: a.name,
						value: a.id,
					});
				});
				this.responsiblePersonList = data
				this.authorizerList = data
			})
		},
		getInsProductIds() {
			getInsProduction().then(res => {
				this.options = res.data.map((m, i) => {
					m.id = m.name;
					let children = m.children.map(n => {
						n.label = n.name;
						n.value = n.id;
						return n
					})
					return m
				})
				this.options.forEach(item => {
					if (item.children.length == 0) {
						item.children = null;
					} else {
						item.children.forEach(m => {
							if (m.children.length == 0) {
								m.children = null;
							} else {
								m.children.forEach(n => {
									if (n.children && n.children.length == 0) {
										n.children = null;
									}
								})
							}
						})
					}
				})
			})
		},
		handleClose() {
			this.formData = {
				authorizedPerson: []
			}
			this.formData2 = {
				imageUpload: '',
				imageName: '',
				authorizedPerson: []
			}
			this.dialogVisible = false;
			this.upLoad = false;
		},
		handleClose2() {
			this.formData = {
				authorizedPerson: []
			}
			this.formData2 = {
				imageUpload: '',
				imageName: '',
				authorizedPerson: []
			}
			this.dialogVisible2 = false;
			this.upLoad = false;
		},
		selectAllByOne(row) {
			this.isUp = true
			//打开弹框
			this.dialogVisible = true;
			//row = 点击对应行值
			//复制给formData
			this.formData = this.HaveJson(row);
			console.log(row.insProductIds + 'valll');
			this.formData.insProductIds = row.insProductIds ? row.insProductIds.split(',') : [];
		},
		isUpdate(row) {
			//修改  isUp 为档案修改
			this.isUp = false
			//打开弹框
			this.dialogVisible = true;
			//row = 点击对应行值一行值
			//复制给formData
			this.formData = this.HaveJson(row);
			if (typeof (row.insProductIds) === 'number') {
				row.insProductIds = row.insProductIds + ''
			}
			this.formData.deviceStatus = this.formData.deviceStatus + ''
			this.formData.insProductIds = row.insProductIds ? row.insProductIds.split(',') : [];
			// 将时间格式为yyyy-MM-dd 进行转换
			const dateRegex = /^\d{4}-\d{2}-\d{2}$/
			Object.keys(this.formData).forEach(key => {
				if (dateRegex.test(this.formData[key])) {
					this.formData[key] = `${this.formData[key]} 00:00:00`
				}
			})
		},
		beforeUpload(file) {
			if (file.size > 1024 * 1024 * 10) {
				this.$message.error('上传文件不超过10M');
				this.$refs.upload.clearFiles()
				return false;
			} else {
				return true;
			}
		},
		onError(err, file, fileList) {
			this.$message.error('上传失败')
			this.$refs.upload.clearFiles()
		},
		// 上传图片成功
		handleSuccessUpImg(response,) {
			if (response.code == 200) {
				this.formData.imageUpload = response.data.url;
				this.formData.imageName = response.data.name;
			}
		},
		handleSuccessUpImg2(response) {
			if (response.code == 200) {
				this.$nextTick(() => {
					this.formData.imageUpload = response.data.url;
					this.formData.imageName = response.data.name;
					this.formData2.imageUpload = response.data.url;
					this.formData2.imageName = response.data.name;
				})
			}
		},
		submitForm() {
			if (!this.formData.deviceName) {
				this.$message.error('未输入仪器名称')
				return
			}
			if (!this.formData.enDeviceName) {
				this.$message.error('未输入仪器名称EN')
				return
			}
			if (!this.formData.specificationModel) {
				this.$message.error('未输入规格型号')
				return
			}
			if (!this.formData.managementNumber) {
				this.$message.error('未输入管理编号')
				return
			}
			if (!this.formData.activationDate) {
				this.$message.error('未输入校准有效期')
				return
			}
			if (!this.formData.subordinateDepartmentsId) {
				this.$message.error('未选择所属部门')
				return
			}
			if (this.formData.deviceStatus === '' || this.formData.deviceStatus === null) {
				this.$message.error('未选择当前状态')
				return
			}
			if (!this.formData.calibrationDate) {
				this.$message.error('未输入校准周期（月）')
				return
			}
			delete this.formData.createTime
			delete this.formData.updateTime
			delete this.formData.createUser
			delete this.formData.updateUser
			const formData = this.HaveJson(this.formData)
			formData.insProductIds = formData.insProductIds ? formData.insProductIds.join() : ''
			if (formData.authorizedPerson.length === 0) {
				formData.authorizedPerson = ''
			} else {
				formData.authorizedPerson = JSON.stringify(formData.authorizedPerson)
			}
			this.upLoad = true;
			upDeviceParameter(formData).then(res => {
				this.$message.success('修改成功')
				this.upLoad = false
				this.refreshTable('page')
				this.dialogVisible = false
			}).catch(e => {
				this.$message.error('修改失败')
				this.dialogVisible = false
				this.upLoad = false
			})
		},
		// 提交档案修订--新增
		submitForm2() {
			if (!this.formData2.deviceName) {
				this.$message.error('未输入仪器名称')
				return
			}
			if (!this.formData2.enDeviceName) {
				this.$message.error('未输入仪器名称EN')
				return
			}
			if (!this.formData2.specificationModel) {
				this.$message.error('未输入规格型号')
				return
			}
			if (!this.formData2.managementNumber) {
				this.$message.error('未输入管理编号')
				return
			}
			if (!this.formData2.activationDate) {
				this.$message.error('未输入校准有效期')
				return
			}
			if (!this.formData2.deviceStatus) {
				this.$message.error('未选择当前状态')
				return
			}
			if (!this.formData2.calibrationDate) {
				this.$message.error('输入校准周期（月）')
				return
			}
			const formData = this.HaveJson(this.formData2)
			formData.insProductIds = formData.insProductIds ? formData.insProductIds.join() : ''
			if (formData.authorizedPerson.length === 0) {
				formData.authorizedPerson = ''
			} else {
				formData.authorizedPerson = JSON.stringify(formData.authorizedPerson)
			}
			this.upLoad2 = true;
			addDeviceParameter(formData).then(res => {
				this.$message.success('提交成功')
				this.upLoad2 = false
				this.refreshTable('page')
				this.dialogVisible2 = false
				this.formData2 = {
					imageUpload: '',
					imageName: '',
					authorizedPerson: []
				}
			}).catch(e => {
				this.$message.error('提交失败')
				this.dialogVisible2 = false
				this.upLoad2 = false
			})
		},
		handleConfig(row) {
			let list = []
			if (row.insProductItem) {
				list = row.insProductItem.split(';')
			}
			let list2 = []
			list.map((item) => {
				const obj = Object.assign({
					deviceId: row.id,
					insProductItem: item,
				})
				list2.push(obj)
			})
			this.tableList = list2
			this.deviceId = row.id
			this.$nextTick(() => {
				this.showData = true
			})
		},
		closeDataVue() {
			this.clickSidebar(this.clickNodeVal)
			this.showData = false
		},
		submitForm3() {
			if (!this.configForm.ip) {
				this.$message.error('请填写IP');
				return
			}
			if (!this.configForm.collectUrl) {
				this.$message.error('请填写采集地址');
				return
			}
			if (!this.configForm.storageUrl) {
				this.$message.error('请填写储存地址');
				return
			}
			if (!this.configForm.refer) {
				this.$message.error('请填写参照');
				return
			}
			if (!this.configForm.x) {
				this.$message.error('请填写X');
				return
			}
			if (!this.configForm.y) {
				this.$message.error('请填写Y');
				return
			}

			this.upLoad3 = true
			numberCollect(this.configForm).then(res => {
				this.upLoad3 = false
				this.$message.success('操作成功')
				this.refreshTable('page')
				this.dialogVisible3 = false
			}).catch(e => {
				this.$message.error('操作失败')
				this.dialogVisible3 = false
				this.upLoad3 = false
			})
		},
		// 点击侧边栏刷新
		clickSidebar(clickNodeVal) {
			this.laboratoryNameIsNull = false
			// 是否存在value，存在value代表为三级
			if (!clickNodeVal.value) {
				this.list = [];
				this.queryParams.laboratoryName = null
				this.queryParams.storagePoint = null
				// 等于1代表为树的一级，label为部门
				if (clickNodeVal.label == '其他') {
					this.laboratoryNameIsNull = true
					this.refreshTable('page')
					return
				}
				if (clickNodeVal.level == 1) {
					this.queryParams.laboratoryName = clickNodeVal.label
					// 等于二级。label为存储地点
				} else if (clickNodeVal.level == 2) {
					// 其他表示没有配置实验室，只配置了地点
					if (clickNodeVal.parent.label == '其他') {
						this.laboratoryNameIsNull = true
					} else {
						this.queryParams.laboratoryName = clickNodeVal.parent.label
					}
					this.queryParams.storagePoint = clickNodeVal.label
				}
				this.refreshTable('page')
			}
		},
		handleDelete(row) {
			this.$confirm("是否删除该条数据?", "提示", {
				confirmButtonText: "确定",
				cancelButtonText: "取消",
				type: "warning",
			})
				.then(() => {
					delDeviceParameter({ id: row.id }).then((res) => {
						this.$message.success("删除成功");
						this.refresh();
					});
				})
				.catch(() => { });
		},
	},
	watch: {
		// 监听点击el-tree的数据，进行数据刷新
		clickNodeVal(newVal) {
			this.clickSidebar(newVal)
		}
	}
}
</script>

<style scoped>
.search {
  height: 46px;
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.search_thing {
	width: 14em;
	display: flex;
	align-items: center;
}

.search_label {
	width: 80px;
	font-size: 14px;
	text-align: right;
}

.table {
	background-color: #fff;
	height: calc(100vh - 16em);
}
.picName {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	word-break: break-all;
	width: 120px;
}
.form-item >>>.el-form-item__content {
  width: 120px;
}
</style>
