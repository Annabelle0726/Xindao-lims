<!-- 设备档案 -->
<template>
  <div class="page">
    <div class="page-header">
      <h4><span class="line"></span><span>设备档案</span></h4>
      <div class="btns">
        <el-button type="primary" size="small" @click="openEditForm" style="">修订档案</el-button>
        <el-button type="primary" size="small" @click="dialogVisible1 = true" style="">添加附件</el-button>
        <el-button type="primary" size="small" @click="deviceDialog = true" style="">查看设备二维码</el-button>
        <el-button type="primary" size="small" @click="handleDownOne" style="">导出</el-button>
      </div>
    </div>
    <el-divider></el-divider>
    <div class="main_div">
      <el-row :gutter="20">
        <el-col :span="6">
          <!-- 加时间戳避免浏览器缓存问题 -->
          <el-image :src="`${javaApi}/img/${Mdata.imageUpload}`" fit="fill" :key="'personalPicture' + timeStamp"
            style="width: 200px;height: 300px;border: 1px solid #000;border-radius: 10px;margin-left: 30px;margin-top: 20px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline" style="font-size: 40px;"></i>
            </div>
          </el-image>
        </el-col>
        <el-col :span="8">
          <div class="form-item">
            <label>设备名称</label>
<!--             <p>{{ Mdata.deviceName }}</p> -->
            <el-tooltip class="item" :content="Mdata.deviceName">
              <span class="text-ellipsis">{{ Mdata.deviceName }}</span>
            </el-tooltip>
          </div>
          <div class="form-item">
            <label>设备类型</label>
            <p>{{ Mdata.largeCategory }}</p>
          </div>

          <div class="form-item">
            <label>管理编号</label>
            <p>{{ Mdata.managementNumber }}</p>
          </div>
          <div class="form-item">
            <label>所属部门</label>
            <p>{{ Mdata.laboratoryName }}</p>
          </div>
          <div class="form-item">
            <label>生产厂家</label>
            <!-- <p>{{ Mdata.manufacturer }}</p> -->
            <el-tooltip class="item" :content="Mdata.manufacturer">
              <span class="text-ellipsis">{{ Mdata.manufacturer }}</span>
            </el-tooltip>
          </div>
          <div class="form-item">
            <label>资产编码</label>
            <p class="text-ellipsis">{{ Mdata.assetCode }}</p>
          </div>

          <div class="form-item">
            <label>出厂日期</label>
            <p>{{ Mdata.acquisitionDate | formaterDateTime }}</p>
          </div>
          <div class="form-item">
            <label>校准有效期</label>
            <p>{{ Mdata.activationDate | formaterDateTime }}</p>
          </div>
          <div class="form-item">
            <label>核准周期(月)</label>
            <p>{{ Mdata.calibrationDate }}</p>
          </div>

          <div class="form-item">
            <label>报废时间</label>
            <p>{{ handleData() }}</p>
          </div>
          <div class="form-item">
            <label>准确度量值</label>
            <el-tooltip class="item" :content="Mdata.technicalIndicators">
              <span class="text-ellipsis">{{ Mdata.technicalIndicators }}</span>
            </el-tooltip>
            <!-- <p>{{ Mdata.technicalIndicators }}</p> -->
          </div>
          <div class="form-item">
            <label>维修记录</label>
            <p>{{ handleData() }}</p>
          </div>
          <div class="form-item">
            <label>被授权人</label>
            <p>{{ Mdata.authorizedPersonName }}</p>
          </div>

        </el-col>
        <el-col :span="10">
          <div class="form-item">
            <label>规格型号</label>
            <p>{{ Mdata.specificationModel }}</p>
          </div>
          <!-- <div class="form-item">
          <label>细类</label>
          <p>{{ handleData() }}</p>
        </div> -->
          <div class="form-item">
            <label>出厂编号</label>
            <p>{{ Mdata.factoryNo }}</p>
          </div>
          <div class="form-items">
            <label>设备状态</label>
            <el-radio-group v-model="Mdata.deviceStatus" style="margin-left: 10px;">
              <el-radio :label="0" :disabled="Mdata.deviceStatus != 0">正常</el-radio>
              <el-radio :label="1" :disabled="Mdata.deviceStatus != 1">维修</el-radio>
              <el-radio :label="2" :disabled="Mdata.deviceStatus != 2">停用</el-radio>
              <el-radio :label="3" :disabled="Mdata.deviceStatus != 3">报废</el-radio>
            </el-radio-group>
          </div>
          <div class="form-item">
            <label>存放点</label>
            <p>{{ Mdata.storagePoint }}</p>
          </div>
          <div class="form-item">
            <label>产地</label>
            <p>{{ Mdata.origin }}</p>
          </div>
          <div class="form-item">
            <label>检验项</label>
            <el-tooltip class="item" effect="dark" :content="Mdata.insProductItem" placement="top">
              <span class="text-ellipsis">{{ Mdata.insProductItem }}</span>
            </el-tooltip>
          </div>
          <div class="form-item">
            <label>购置日期</label>
            <p>{{ Mdata.acquisitionDate | formaterDateTime }}</p>
          </div>

          <div class="form-item">
            <label>最近校准日期</label>
            <p>{{ Mdata.lastCalibrationDate | formaterDateTime }}</p>
          </div>
          <div class="form-item">
            <label>下次校准日期</label>
            <p>{{ Mdata.nextCalibrationDate | formaterDateTime }}</p>
          </div>
          <div class="form-item">
            <label>采购费用(元)</label>
            <p>{{ Mdata.unitPrice }}</p>
          </div>
          <div class="form-item">
            <label>设备负责人</label>
            <p>{{ Mdata.equipmentManagerName }}</p>
          </div>
          <div class="form-item">
            <label>验收记录</label>
            <p>{{ handleData() }}</p>
          </div>
          <div class="form-item">
            <label>核准证书</label>
            <p>{{ Mdata.calibrateNo }}</p>
          </div>
        </el-col>
      </el-row>
      <h4 style="margin-bottom: 10px;margin-top: 10px;"><span class="line"></span><span>附件和相关文档</span></h4>
      <el-table height="200px" :data="tableDataA" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
        <el-table-column header-align="center" align="center" prop="prop" label="序号" type="index" width="60" />
        <el-table-column prop="documentType" label="类型" min-width="100">
          <template v-slot="scope">
            {{ scope.row.documentType === '0' ? '附件' : scope.row.documentType === '1' ? '文档' : '' }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column prop="number" label="编号" min-width="150" />
        <el-table-column prop="version" label="版本" min-width="80" />
        <el-table-column prop="quantity" label="份数" min-width="80" />
        <el-table-column prop="pageCount" label="页码" min-width="80" />
        <el-table-column prop="provider" label="提供者" min-width="150" />
        <el-table-column prop="provideDate" label="归档日期" min-width="170" />
        <el-table-column prop="comments" label="备注" min-width="150" />
        <el-table-column fixed="right" label="操作" min-width="180" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleAttachmentClick(scope.row)">下载附件</el-button>
            <el-button type="text" size="small" @click="handleViewClick(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDeleteClick(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 修订 -->
    <el-dialog title="档案修订" top="5vh" :visible.sync="dialogVisible" width="70%" @open="openFileRevisionDialog"
      :before-close="handleClose">
      <el-row style="display:flex;justify-content: space-around;max-height: 75vh;overflow-y: auto;">
        <!-- 左边布局 -->
        <el-col :span="7">
          <el-col>
            <!-- 图片 -->
            <el-image class="img" style="width:100%;height: 320px;margin-bottom:16px"
              :src="javaApi + '/img/' + editData.imageUpload">
              <div slot="error" class="image-error" style="width: calc(100% - 2px);
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
            <el-form :model="editData" label-width="120px" ref="rules1" :rules="rules1">
              <el-form-item label="仪器名称:" prop="deviceName">
                <el-input v-model="editData.deviceName" size="small"></el-input>
              </el-form-item>
              <el-form-item label="仪器名称EN:" prop="enDeviceName">
                <el-input v-model="editData.enDeviceName" size="small"></el-input>
              </el-form-item>
              <el-form-item label="规格型号:" prop="specificationModel">
                <el-input v-model="editData.specificationModel" size="small"></el-input>
              </el-form-item>
              <el-form-item label="生产厂家:">
                <el-input v-model="editData.manufacturer" size="small"></el-input>
              </el-form-item>
            </el-form>
          </el-col>
        </el-col>
        <!-- 中间布局 -->
        <el-col :span="7">
          <el-form :model="editData" label-width="116px" ref="rules2" :rules="rules1">
            <el-form-item label="校准服务机构:">
              <el-input v-model="editData.calibrationServices" size="small"></el-input>
            </el-form-item>
            <el-form-item label="资产编码:">
              <el-input v-model="editData.assetCode" size="small"></el-input>
            </el-form-item>
            <el-form-item label="产地:">
              <el-input v-model="editData.origin" size="small"></el-input>
            </el-form-item>
            <el-form-item label="出厂编号:">
              <el-input v-model="editData.factoryNo" size="small"></el-input>
            </el-form-item>
            <el-form-item label="管理编号:" prop="managementNumber">
              <el-input v-model="editData.managementNumber" size="small"></el-input>
            </el-form-item>
            <el-form-item label="购置日期:">
              <el-date-picker style="width:100%" v-model="editData.acquisitionDate" type="date" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="校准有效期:" prop="activationDate">
              <el-date-picker style="width:100%" v-model="editData.activationDate" type="date" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss" size="small" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="负责人:">
              <el-select v-model="editData.equipmentManager" placeholder="请选择" size="small" style="width:100%">
                <el-option v-for="item in responsiblePersonList" :key="item.name" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="存放点:">
              <el-input v-model="editData.storagePoint" size="small"></el-input>
            </el-form-item>
            <el-form-item label="技术指标:">
              <el-input v-model="editData.technicalIndicators" :rows="7" type="textarea" size="small"></el-input>
            </el-form-item>
          </el-form>
        </el-col>
        <!-- 右边布局 -->
        <el-col :span="7">
          <el-form :model="editData" label-width="140px" ref="ruleForm" :rules="rules1">
            <!-- 实验室列表 -->
            <el-form-item label="所属部门:" prop="subordinateDepartmentsId">
              <el-select v-model="editData.subordinateDepartmentsId" placeholder="请选择" size="small" style="width:100%">
                <el-option v-for="item in subordinateDepartmentsList" :key="item.value" :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="检测项目:">
              <el-cascader v-model="editData.insProductIds" :options="options" :show-all-levels="false" :props="props"
                placeholder="请选择" size="small" style="width:100%;" :collapse-tags="true" separator="," filterable
                clearable></el-cascader>
            </el-form-item>
            <el-form-item label="最近校准日期:">
              <el-date-picker style="width:100%" v-model="editData.lastCalibrationDate" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="下次校准日期:">
              <el-date-picker style="width:100%" v-model="editData.nextCalibrationDate" format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss" type="date" size="small" placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="设备类型:">
              <el-select v-model="editData.largeCategory" placeholder="请选择" size="small" style="width:100%">
                <el-option v-for="item in equipmentList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="单价(万元):">
              <el-input v-model="editData.unitPrice" size="small"></el-input>
            </el-form-item>
            <el-form-item label="当前状态:" prop="deviceStatus">
              <el-select v-model="editData.deviceStatus" placeholder="请选择" size="small" style="width:100%">
                <el-option v-for="item in deviceStatusList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="校准周期（月）:" prop="calibrationDate">
              <el-input v-model="editData.calibrationDate" size="small"></el-input>
            </el-form-item>
            <el-form-item label="被授权人:">
              <el-select v-model="editData.authorizedPerson" multiple placeholder="请选择" size="small" style="width:100%">
                <el-option v-for="item in responsiblePersonList" :key="item.name" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="图片:">
              <!-- <div
                style="border: 1px solid #DCDFE6;border-radius:4px;height:32px;lineHeight:32px;display:flex;justify-content: space-around;font-size: 13px;">
                <div v-show="Mdata.imageName" class="picName">{{ Mdata.imageName }}</div>
                <el-upload :action="action" :on-success="handleSuccessUpImg2" :show-file-list="false"
                  accept='image/jpg,image/jpeg,image/png' :headers="headers" :on-change="beforeUpload"
                  :on-error="onError" ref='upload'>
                  <el-button type="text" style="height:30px;padding-top:8px"><span v-if="Mdata.imageName"></span></el-button>
                </el-upload>
              </div> -->

              <div>
                <div v-show="editData.imageName" class="picName">{{ editData.imageName }}</div>
                <el-upload :action="action" :on-success="handleSuccessUpImg2" :show-file-list="false"
                  accept='image/jpg,image/jpeg,image/png' :headers="uploadHeader" :on-change="beforeUpload"
                  :on-error="onError" ref='upload' class="avatar-uploader">
                  <img v-if="editData.imageUpload" :src="javaApi + '/img/' + editData.imageUpload" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </div>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="upLoad">确 定</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog title="设备附件" top="5vh" v-if="dialogVisible1" :visible.sync="dialogVisible1" width="40%">
      <el-form ref="form" :model="addFile" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维护类型：">
              <el-radio-group v-model="addFile.documentType">
                <el-radio :label="0">附件</el-radio>
                <el-radio :label="1">文档</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="档案名称：">
              <el-input size="small" placeholder="请输入" clearable v-model="addFile.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库号/资产编号:">
              <el-input size="small" placeholder="请输入" clearable v-model="addFile.number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本：">
              <el-input size="small" placeholder="请输入" clearable v-model="addFile.version"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="份数：">
              <el-input-number v-model="addFile.quantity" controls-position="right" style="width: 100%;" size="small"
                :min="0" :max="10"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="页码：">
              <el-input-number v-model="addFile.pageCount" controls-position="right" style="width: 100%;" size="small"
                :min="0" :max="10"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提供者：">
              <el-input size="small" placeholder="请输入" clearable v-model="addFile.provider"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归档日期：">
              <el-date-picker type="date" style="width:100%" placeholder="请选择日期" size="small"
                v-model="addFile.provideDate" format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注：">
              <el-input type="textarea" :rows="2" size="small" v-model="addFile.comments"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="附件：" style="float: left;">
              <el-upload class="upload-demo" :action="action" :headers="uploadHeader" ref="uploadFile"
                :on-error="onError" :on-remove="handleRemove" :on-success="onSuccess" :before-remove="beforeRemove"
                multiple :limit="1" :on-exceed="handleExceed" :file-list="fileList">
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-row>
          <el-button @click="dialogVisible1 = false" size="small">取 消</el-button>
          <el-button type="primary" @click="saveRecord" size="small" :loading="isLoading">{{ isAddFileUpdate == true ?
            '更新' :
            '确 定' }}</el-button>
        </el-row>
      </span>
    </el-dialog>
    <el-dialog title="设备二维码" :visible.sync="deviceDialog" top="5vh" width="30%">
      <div style="width:90%;text-align: right;">
        <el-button type="primary" size="mini" @click="clickGeneratePicture">下载图片</el-button>
        <el-button type="primary" size="mini" @click="labelPrint">标签打印</el-button>
      </div>
      <div class="device-main" ref="deviceQrCode" id="deviceCode">
        <div class="device-center">
          <p class="device-title">{{ Mdata.deviceName }}</p>
          <div class="device-footer">
            <div>
              <vueQr class="qr-code"
                :text="'https://zttx-lims.ztt.cn:8021/lims/qr/qrScan?code=' + Mdata.managementNumber + '&type=device'"
                :size="200" :margin="10"></vueQr>
            </div>
            <div class="device-number">
              <span class="device-text">设备编码:</span>
              <span class="device-text">{{ Mdata.managementNumber }}</span>
            </div>
            <div class="device-name">
              <span class="device-text">设备型号:</span>
              <span class="device-text">{{ Mdata.specificationModel }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { MessageBox } from 'element-ui'
import fileDownload from '@/utils/file'
import vueQr from 'vue-qr'
import PrintJS from 'print-js'
import domtoimage from 'dom-to-image';
import {
  deleteCNASFile,
  updateDocument,
  addDocument,
  deleteDocumentById,
  getAllDocuments,
  selectDeviceByCode,
  upDeviceParameter,
  exportDeviceFile,
  getInsProduction,
} from '@/api/cnas/resourceDemand/device.js'
import { selectUserCondition } from "@/api/business/inspectionTask";
import {
  obtainItemParameterList,
} from '@/api/cnas/resourceDemand/device.js'
export default {
  filters: {
    formaterDateTime(dateTime) {
      if (dateTime === undefined || dateTime === null || dateTime === '') {
        return dateTime
      }
      return dateTime.split(" ")[0]
    }
  },
  props: {
    clickNodeVal: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  components: {
    vueQr
  },
  data() {
    return {
      deviceDialog: false,//查看设备二维码弹框
      rules1: {
        deviceName: [
          { required: true, message: '请输入仪器名称', trigger: 'blur' },
        ],
        enDeviceName: [
          { required: true, message: '请输入仪器名称EN', trigger: 'blur' },
        ],
        specificationModel: [
          { required: true, message: '请输入规格型号', trigger: 'blur' },
        ],
        managementNumber: [
          { required: true, message: '请输入管理编号', trigger: 'blur' },
        ],
        activationDate: [
          { required: true, message: '请输入校准有效期', trigger: 'blur' },
        ],
        deviceStatus: [
          { required: true, message: '请输入当前状态', trigger: 'blur' },
        ],
        calibrationDate: [
          { required: true, message: '请输入校准周期(月)', trigger: 'blur' },
        ],
        subordinateDepartmentsId: [
          { required: true, message: '请选择所属部门', trigger: 'change' },
        ]
      },
      // dateForm: {

      // },
      fileList: [], // 添加附件上传文件列表
      timeStamp: '', // 给图片添加key，在每次加载页面的时候新建时间戳
      equipmentManager: [],
      formLabelWidth: '100px',
      dialogVisible1: false,
      recordNumber: '',
      inLoading: false, // 控制上传按钮加载状态
      isLoading: false, // 控制确定按钮加载状态
      formData: {},
      tableDataA: [],
      num: 1,
      //添加附件数据收集
      addFile: {
        documentType: '', // 维护类型
        provideDate: null, // 归档日期
        name: '', // 档案名称
        number: '', // 库号/资产编号
        version: '', // 版本
        quantity: 1, // 份数
        pageCount: 1, // 页码
        provider: '', // 提供者
        comments: "", // 备注
        fileName: "", // 文件原名称
        systemFileName: "", // 系统生成名称
      },
      componentData: {
        entity: { name: "", orderBy: { field: "id", order: "asc" } },
        page: { current: "1", size: "80" }
      },
      upIndex: 0,
      dialogVisible: false,
      upLoad: false,
      responsiblePersonList: [],
      subordinateDepartmentsList: [],
      options: [],
      equipmentList: [],
      deviceStatusList: [],
      props: { multiple: true, emitPath: false, value: 'id', label: 'name' },
      // 收集设备档案数据
      Mdata: {
        deviceName: null
      },
      isAddFileUpdate: false,
      editData: {
        authorizedPerson: [],
      },
    }
  },
  computed: {
    action() {
      return this.javaApi + '/deviceScope/uploadFile'
    }
  },
  mounted() {
    // 收集数据
    this.getList(this.clickNodeVal.value)
    // 获取相关文档的数据
    this.getPage()
    this.timeStamp = Date.now()
  },
  methods: {
    /**
   * 将页面指定节点内容转为图片
   * 1.拿到想要转换为图片的内容节点DOM；
   * 2.转换，拿到转换后的canvas
   * 3.转换为图片
   */
    clickGeneratePicture() {
      const _than = this
      domtoimage.toPng(_than.$refs.deviceQrCode, { quality: 1, width: 440, }).then(function (dataUrl) {
        // 将转换后的图像数据存储为 data URL
        fileDownload.downloadIamge(dataUrl, _than.Mdata.deviceName)
      });
    },
    //打印设备二维码
    labelPrint() {
      PrintJS({
        printable: 'deviceCode',//页面
        type: "html",//文档类型
        maxWidth: 360,
        css: ['/static/css/device-print.css'],
        style: '@page { size: auto;  margin: 0mm;}',
        targetStyles: ["*"], // 使用dom的所有样式，很重要
      });
    },
    openFileRevisionDialog() {
      // 获取档案修订负责人下拉框数据
      this.selectDevicePrincipal()
      // 获取档案修订所属部门下拉框数据
      this.obtainItemParameterList()
      // 获取档案修订设备状态下拉框数据
      this.selectEnumByCategory()
      // 获取档案修订检验项目级联弹框数据
      this.getInsProductIds()
    },
    //附件和相关文档事件
    handleAttachmentClick(row) {
      console.log(row)
      if (row.fileName) {
        this.$download.saveAs(row.fileName)
      } else {
        this.$message.warning('未上传文件！')
      }
    },
    handleViewClick(row) {
      this.isAddFileUpdate = true
      this.addFile = { ...row }
      this.dialogVisible1 = true
      if (row.fileName) {
        const obj = Object.assign({
          name: row.fileName,
          url: row.fileName,
          status: 'success',
          uid: Date.now(),
        })
        this.fileList.push(obj)
      }
    },
    handleDeleteClick(row) {
      // 删除逻辑
      MessageBox.confirm('确定要删除该记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        deleteDocumentById({ id: row.id }).then(res => {
          if (res.code == 200) {
            this.$message.success('删除成功')
            this.getPage()
          }
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    saveRecord() {
      //转换数据
      this.addFile.deviceId = this.clickNodeVal.value
      let obj = this.addFile.systemFileName
      this.addFile.systemFileName = obj.name
      this.addFile.fileName = obj.url

      // 更新
      if (this.isAddFileUpdate) {
        updateDocument(this.addFile).then(res => {
          if (res.code == 200) {
            this.$message.success('更新成功')
            this.dialogVisible1 = false;
            this.getPage()
          }
        })
      } else {
        // 新增
        addDocument(this.addFile).then(res => {
          if (res.code == 200) {
            this.$message.success('保存成功')
            this.dialogVisible1 = false;
            this.getPage()
          }
        })
      }
    },
    onSuccess(response, file, fileList) {
      if (response.code == 200) {
        this.addFile.systemFileName = response.data
        this.addFile.fileName = file.name
      } else {
        this.$refs.uploadFile.clearFiles()
        this.$message.error('上传失败：' + response.message)
      }
    },
    handleRemove(file, fileList) {
      this.deleteFile(this.addFile.systemFileName)
    },
    deleteFile(fileName) {
      deleteCNASFile({ fileName }).then(res => {
        this.$message.success('删除成功！')
      })
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    // 获取相关文档数据的api
    getPage() {
      getAllDocuments({ deviceId: this.clickNodeVal.value }).then(res => {
        if (res.code == 200)
          this.tableDataA = res.data
      })
    },
    // 收集数据
    getList(id) {
      selectDeviceByCode({ id }).then(res => {
        // 将分类列表的信息存起来
        if (res.code == 200) {
          this.Mdata = res.data;
          this.Mdata.insProductIds = this.Mdata.insProductIds.split(',')
          if (this.Mdata.authorizedPerson) {
            this.Mdata.authorizedPerson = JSON.parse(this.Mdata.authorizedPerson)
          }
        }
      });
    },
    handleData(m) {
      if (m) {
        return m
      } else {
        return '-'
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    //打开修订档案弹框
    openEditForm() {
      this.editData = JSON.parse(JSON.stringify(this.Mdata))
      this.$nextTick(() => {
        this.dialogVisible = true
      })
    },
    //修订档案
    submitForm() {
      let flag = true
      this.$refs['rules1'].validate((valid) => {
        if (!valid) {
          flag = false;
          return false;
        }
      });
      this.$refs['rules2'].validate((valid) => {
        if (!valid) {
          flag = false;
          return false;
        }
      });
      this.$refs['ruleForm'].validate((valid) => {
        if (!valid) {
          flag = false;
          return false;
        }
      });
      if (!flag) {
        return;
      }
      delete this.editData.createTime
      delete this.editData.updateTime
      delete this.editData.createUser
      delete this.editData.updateUser
      delete this.editData.orderBy
      const formData = this.HaveJson(this.editData)
      formData.userAllow = this.equipmentManager.toString();
      formData.insProductIds = Array.isArray(formData.insProductIds) ? formData.insProductIds.join(',') : '';
      if (formData.authorizedPerson.length === 0) {
        formData.authorizedPerson = ''
      } else {
        formData.authorizedPerson = JSON.stringify(formData.authorizedPerson)
      }
      this.upLoad = true;
      upDeviceParameter(formData).then(res => {
        this.$message.success('修改成功')
        this.upLoad = false
        this.getList(this.clickNodeVal.value)
        this.dialogVisible = false
      }).catch(e => {
        this.$message.error('修改失败')
        this.dialogVisible = false
        this.upLoad = false
      })
    },
    // 导出
    handleDownOne(row) {
      this.outLoading = true
      exportDeviceFile({ deviceId: this.clickNodeVal.value }).then(res => {
        this.outLoading = false
        const blob = new Blob([res], { type: 'application/octet-stream' });
        this.$download.saveAs(blob, '设备档案卡.doc')
      })
    },
    handleSuccessUpImg2(response) {
      if (response.code == 200) {
        this.$nextTick(() => {
          this.editData.imageUpload = response.data.url;
          this.editData.imageName = response.data.name;
          console.log(this.javaApi + 'img/' + this.editData.imageUpload);

        })
      }
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
    // 获取负责人列表
    selectDevicePrincipal() {
      selectUserCondition().then(res => {
        this.responsiblePersonList = res.data;
      })
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
    getInsProductIds() {
      getInsProduction().then(res => {
        this.options = res.data.map((m, i) => {
          m.id = m.name;
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
    // 获取字典
    selectEnumByCategory() {
      // 设备状态
      this.getDicts("device_status").then((response) => {
        this.deviceStatusList = this.dictToValue(response.data)
        this.deviceStatusList.forEach(a => {
          if (!isNaN(a.value)) {
            a.value = parseInt(a.value)
          }
        })
      });
      // 设备分类
      this.getDicts("device_type").then((response) => {
        this.equipmentList = this.dictToValue(response.data);
      });
    },
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal(newVal) {
      if (newVal.value) {
        this.getList(newVal.value)
        this.getPage()
      }
    },
    dialogVisible1(newVal) {
      if (newVal == false) {
        this.isAddFileUpdate = false
        this.addFile = {}
        this.fileList = []
      }
    }
  }
}
</script>

<style scoped>
.main_div {
  height: calc(100vh - 19em);
  overflow-y: auto;
  overflow-x: hidden;
}

.page {
  width: 100%;
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
}

h4 {
  display: flex;
  align-items: center;
}

h4 .line {
  display: inline-block;
  width: 3px;
  height: 16px;
  background: #3A7BFA;
  margin-right: 4px;
}

.tables {
  width: 100%;
}

.el-image {
  position: relative;
}

.el-icon-picture-outline {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.form-item {
  line-height: 34px;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.text-ellipsis {
  display: inline-block;
  width: 200px;
  /* 或者您可以设置一个具体的宽度值 */
  box-sizing: border-box;
  /* 确保padding和border不影响元素的总宽度 */
  white-space: nowrap;
  /* 禁止文本换行 */
  text-overflow: ellipsis;
  /* 使用省略号表示被截断的文本 */
  overflow: hidden;
  /* 隐藏超出容器的内容 */
}

.form-items {
  line-height: 34px;
  /* display: flex; */
  /* align-items: center;  */
  margin-left: 15%;
  font-size: 14px;
}


.form-item label {
  min-width: 130px;
  display: inline-block;
  text-align: right;
  margin-right: 20px;
  color: #999;
}

.form-item p {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  /* 这里设置你想要的行数 */
  -webkit-box-orient: vertical;
}

.btns {
  display: flex;
  align-items: center;
}

.search_thing,
.check_thing {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.search_label,
.check_label {
  width: 120px;
}

.search_input,
.check_data,
.check_input {
  flex-grow: 1;
}

.avatar-uploader .el-upload {
  border: 1px dashed #190505;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.device-main {
  width: 90%;
  margin: 0px 5%;
  height: 500px;
  padding: 25px 0px;
  background-color: #fff;
}

.device-center {
  width: 90%;
  height: 490px;
  margin: 0px 5%;
  border-radius: 15px;
  background-color: #fff;
  overflow: hidden;
  /* box-shadow: 3px 3px 8px 0 rgba(0, 0, 0, 0.3); */
  filter: drop-shadow(0px 5px 5px rgba(0, 0, 0, 0.3))
}

.device-title {
  position: relative;
  top: 20px;
  z-index: 2;
  font-size: clamp(1rem, 0.582rem + 1.59vw, 1.475rem);
  font-weight: bold;
  color: #4f6ab2;
  width: 100%;
  height: 90px;
  line-height: 100px;
  text-align: center;
}

.device-footer {
  width: 100%;
  height: 420px;
  background-color: #3361d0;
  position: relative
}

.device-footer::after {
  content: "";
  width: 100%;
  height: 70px;
  position: absolute;
  top: -30px;
  border-radius: 0 0 50% 50%;
  background-color: #fff;
}

.device-footer .qr-code {
  width: 55%;
  height: 200px;
  position: relative;
  top: 60px;
  left: 22.5%;
  background-color: #fff;
  border-radius: 15px;
  overflow: hidden;
}

.device-number {
  width: 80%;
  height: 20px;
  margin-left: 20%;
  position: relative;
  top: 80px;
}

.device-text {
  color: #fff;
  font-weight: bold;
  font-size: 100%;
}

.device-name {
  width: 80%;
  margin-left: 20%;
  height: 20px;
  position: relative;
  top: 90px;
}
</style>
