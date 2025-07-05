<template>
  <div>
    <div style="text-align: right;margin-bottom: 10px">
      <!--       <el-button size="small" @click="$emit('goBackList')">返回</el-button>-->
      <el-button size="small" type="primary" @click="downPerson">下载档案</el-button>
      <el-button size="small" type="primary" @click="dialogVisible = true">人员分类</el-button>
      <el-button :loading="saveLoading" size="small" type="primary" @click="save">保存</el-button>
    </div>
    <div>
      <div style="display: flex;flex-direction: row;">
        <div style="width: 12em">
          <el-image :src="javaApi + '/img/' + form.pictureUrl" fit="fill"
            style="width:100%;height: 300px;border: 1px solid #000;border-radius: 10px;margin-left: 6px;margin-top: 10px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline" style="font-size: 40px;"></i>
            </div>
          </el-image>
          <el-image :src="javaApi + '/img/' + form.signatureUrl" fit="fill"
            style="width:80%;height: 50px;border: 1px solid #000;border-radius: 10px;margin-left: 22px;margin-top: 20px;">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline" style="font-size: 40px;"></i>
            </div>
          </el-image>
        </div>
        <div style="height: calc(100vh - 14em);overflow-y: auto">
          <el-form ref="form" :model="form" label-width="110px">
            <el-row>
              <el-col :span="8">
                <el-form-item label="姓名">
                  <el-input disabled v-model="form.name" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="工号">
                  <el-input disabled v-model="form.account" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="当前状态">
                  <el-radio-group v-model="form.currentState">
                    <el-radio label="1formal" size="mini">正式</el-radio>
                    <el-radio label="2intern" size="mini">实习</el-radio>
                    <el-radio label="3leaveOffice" size="mini">离职</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.sex">
                    <el-radio label="1" size="mini">男</el-radio>
                    <el-radio label="0" size="mini">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="公司名称">
                  <el-input v-model="form.corporateName" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="所属部门">
                  <el-cascader v-model="form.departLimsId" :options="department"
                    :props="{ label: 'name', value: 'id', checkStrictly: true }" filterable
                    style="width: 100%;"></el-cascader>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="岗位名称">
                  <el-input v-model="form.postName" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="入集团时间">
                  <el-date-picker v-model="form.groupTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="劳动关系">
                  <el-radio-group v-model="form.laborRelations">
                    <el-radio :label=0 size="mini">合同工</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="公司邮箱">
                  <el-input v-model="form.email" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="工作时间">
                  <el-date-picker v-model="form.workingTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="合同到期时间">
                  <el-date-picker v-model="form.contractLifeTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="职称">
                  <el-input v-model="form.professionalTitle" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="16" style="text-align: left;">
                <el-form-item label="人员分类">
                  <span style="color: #000;">{{ form.personnelClassification }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="出生日期">
                  <el-date-picker v-model="form.dateBirth" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    @change="getAge" style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="籍贯">
                  <el-input v-model="form.nativePlace" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="民族">
                  <el-input v-model="form.nation" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="身份证号">
                  <el-input v-model="form.identityCard" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="年龄">
                  <el-input-number v-model="form.age" :max="130" :min="1" controls-position="right" size="small"
                    style="width: 99%;"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="婚姻状况">
                  <el-radio-group v-model="form.maritalStatus">
                    <el-radio :label=0 size="mini">已婚</el-radio>
                    <el-radio :label=1 size="mini">未婚</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="证件有效期">
                  <el-date-picker v-model="form.validityPeriod" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="证件地址">
                  <el-input v-model="form.idAddress" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="证件详细地址">
                  <el-input v-model="form.idDetailAddress" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="政治面貌">
                  <el-input v-model="form.politicalStatus" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="入党/团时间">
                  <el-date-picker v-model="form.dumplingTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="手机号">
                  <el-input v-model="form.telephone" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="最高学历">
                  <el-input v-model="form.officialAcademicRedentials" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最高学位">
                  <el-input v-model="form.highestDegree" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="毕业院校1">
                  <el-input v-model="form.graduatedInstitutions1" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="专业1">
                  <el-input v-model="form.major1" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="毕业时间1">
                  <el-date-picker v-model="form.graduationTime1" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="毕业院校2">
                  <el-input v-model="form.graduatedInstitutions2" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="专业2">
                  <el-input v-model="form.major2" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="毕业时间2">
                  <el-date-picker v-model="form.graduationTime2" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="登记时间">
                  <el-date-picker v-model="form.lastUpdateTime" format="yyyy-MM-dd" placeholder="选择日期" size="small"
                    style="width: 99%;" type="date" value-format="yyyy-MM-dd HH:mm:ss">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注">
                  <el-input type="textarea" v-model="form.remarks" clearable size="small"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="18">
                <el-form-item label="个人照片">
                  <el-input v-model="form.pictureUrl" disabled size="small">
                    <el-button v-if="form.pictureUrl" slot="append" icon="el-icon-delete-solid"
                      @click="deleteFile(form.pictureUrl, 'pictureUrl')"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-upload ref="upload" :action="action" :headers="uploadHeader"
                  :on-success="(response, file, fileList) => onSuccess(response, file, fileList, 'pictureUrl')"
                  :show-file-list="false" style="float: left; margin: 0 10px 0 10px;">
                  <el-button slot="trigger" class="uploadFile" size="mini" type="primary">上传</el-button>
                </el-upload>
                <el-button v-if="form.pictureUrl" class="uploadFile" size="mini" type="primary"
                  @click="downloadFile(form.pictureUrl)">下载</el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="18">
                <el-form-item label="电子签名">
                  <el-input v-model="form.signatureUrl" disabled size="small">
                    <el-button v-if="form.signatureUrl" slot="append" icon="el-icon-delete-solid"
                      @click="deleteFile(form.signatureUrl, 'signatureUrl')"></el-button>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-upload ref="upload" :action="action" :headers="uploadHeader"
                  :on-success="(response, file, fileList) => onSuccess(response, file, fileList, 'signatureUrl')"
                  :show-file-list="false" style="float: left; margin: 0 10px 0 10px;">
                  <el-button slot="trigger" class="uploadFile" size="small" type="primary">上传</el-button>
                </el-upload>
                <el-button v-if="form.signatureUrl" class="uploadFile" size="small" type="primary"
                  @click="downloadFile(form.signatureUrl)">下载</el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="20">
                <el-form-item label="证书资料">
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-button size="mini" style="float: right;margin-right: 25px" type="primary"
                  @click="annexAdd(0)">新增</el-button>
              </el-col>
            </el-row>
            <lims-table :tableData="annexList" :column="columnData2" style="width: 96%;margin-left: 34px" height="200"
              :tableLoading="tableLoading2"></lims-table>
            <el-row style="margin-top: 10px">
              <el-col :span="20">
                <el-form-item label="附件资料">
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-upload ref='upload' :action="fileAction" :auto-upload="true" :data="{ userId: clickNodeVal.userId }"
                  :before-upload="fileBeforeUpload" :headers="uploadHeader" :on-error="onError"
                  :on-success="handleSuccessUp" :show-file-list="false"
                  accept='.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'
                  style="width: 80px !important;">
                  <el-button size="small" type="primary">附件上传</el-button>
                </el-upload>
              </el-col>
            </el-row>
            <lims-table :tableData="tableData" :column="columnData" style="width: 96%;float: right;" height="200"
              :tableLoading="tableLoading"></lims-table>
            <el-row style="margin-top: 10px">
              <el-col :span="20">
                <el-form-item label="工作经历">
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-button size="mini" style="float: right;margin-right: 25px" type="primary"
                  @click="annexAdd1('add')">新增</el-button>
              </el-col>
            </el-row>
            <el-table :data="tableData1" border height="200" style="width: 96%;float: right;"
                      :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
                      v-loading="tableLoading1">
              <el-table-column label="序号" type="index" width="55px" align="center">
              </el-table-column>
              <el-table-column label="工作经历" prop="workExperience">
              </el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="annexAdd1('edit', scope.row)">编辑</el-button>
                  <el-button type="text" size="mini" @click="deleteAnnex1(scope.row)"
                    style="color: #f56c6c">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </div>
      </div>
    </div>
    <!-- 人员分类弹框 -->
    <el-dialog :visible.sync="dialogVisible" title="提示" width="40%" @open="getComparisonList">
      <div style="height: 30vh;">
        <el-row>
          <el-col :span="4">
            人员分类：
          </el-col>
          <el-col :span="20" style="text-align: left;">
            <el-checkbox-group v-model="checkList">
              <el-checkbox v-for="v in dict.type.personnl_type" :key="v.value" :label="v.value"></el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="clickPersonnelClassificationSure">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 新增附件资料 -->
    <el-dialog :before-close="handleClose" :title="title" :visible.sync="dialogVisible1" width="40%"
      @open="getComparisonList">
      <el-form ref="annex" :model="annex" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="证件号" prop="idNumber">
              <el-input v-model="annex.idNumber" clearable size="small" style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="发证单位" prop="issueUnit">
              <el-input v-model="annex.issueUnit" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="级别" prop="level">
              <el-input v-model="annex.level" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="有效期" prop="periodValidity">
              <el-input v-model="annex.periodValidity" clearable size="small"></el-input>
              <!--              <el-date-picker v-model="annex.periodValidity" format="yyyy-MM-dd" placeholder="选择日期" size="small"-->
              <!--                              style="width: 99%;" type="date" value-format="yyyy-MM-dd">-->
              <!--              </el-date-picker>-->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="复印件" prop="copy">
              <el-input v-model="annex.copy" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="原件" prop="original">
              <el-input v-model="annex.original" clearable size="small"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="margin-top: 15px">
          <el-col :span="16">
            <el-form-item label="文件">
              <el-upload :action="action" :before-upload="beforeAvatarUpload" :headers="uploadHeader"
                         ref="fileName"
                :on-success="(response, file, fileList) => onSuccess(response, file, fileList, 'fileName')"
                :show-file-list="false">
                <span v-if="annex.fileName">{{ annex.fileName }}</span>
                <!--                <img v-if="imageUrl" :src="imageUrl" class="avatar">-->
                <i v-else class="el-icon-upload avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancellation">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 新增工作经历 -->
    <el-dialog @close="handleClose2" title="添加工作经历" :visible.sync="dialogVisible2" width="40%">
      <el-form ref="annex2" :model="annex2" label-width="100px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="工作经历" prop="idNumber">
              <el-input type="textarea" v-model="annex2.workExperience" clearable size="small"
                style="width: 100%;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose2">取 消</el-button>
        <el-button type="primary" @click="submitForm2">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import fileDownload from '@/utils/file'
import {
  addAnnex,
  addBasicInfoWork,
  delBasicInfoFileList,
  delBasicInfoWorkList,
  deleteAnnex, deleteCNASFile,
  exportPersonBasicInfoById,
  getAnnex,
  getAnnexByUserId,
  getBasicInfoFileList,
  getBasicInfoWorkList,
  getCNASPersonnelInfo,
  saveCNASPersonnelInfo,
  updateAnnex,
  updateBasicInfoWorkList
} from "@/api/cnas/personal/personalList";
import limsTable from "@/components/Table/lims-table.vue";

export default {
  props: {
    clickNodeVal: {
      type: Object,
      default: () => { return {} }
    },
  },
  dicts: ['personnl_type'],
  data() {
    return {
      operationType: '',
      basicInfoWorkId: '',
      dialogVisible2: false,
      annex2: {
        workExperience: ''
      },
      tableLoading: false,
      tableData: [],
      columnData: [
        {
          label: '文件名称',
          prop: 'fileName',
          minWidth: '150px'
        },
        {
          dataType: 'action',
          minWidth: '100',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.upload(row)
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.delete(row)
              }
            }
          ]
        }
      ],
      columnData2: [
        {
          label: '证件号',
          prop: 'idNumber',
          minWidth: '150px'
        }, {
          label: '发证单位',
          prop: 'issueUnit',
          minWidth: '150px'
        }, {
          label: '文件名称',
          prop: 'fileName',
          minWidth: '200px'
        }, {
          label: '级别',
          prop: 'level',
          minWidth: '150px'
        }, {
          label: '有效期',
          prop: 'periodValidity',
          minWidth: '150px'
        }, {
          label: '添加时间',
          prop: 'createTime',
          minWidth: '150px'
        },
        {
          dataType: 'action',
          minWidth: '130',
          label: '操作',
          fixed: 'right',
          operation: [
            {
              name: '下载',
              type: 'text',
              clickFun: (row) => {
                this.downloadFile(row.fileName)
              }
            },
            {
              name: '编辑',
              type: 'text',
              clickFun: (row) => {
                this.annexAdd(1, row)
              }
            },
            {
              name: '删除',
              type: 'text',
              color: '#f56c6c',
              clickFun: (row) => {
                this.deleteAnnex(row)
              }
            }
          ]
        }
      ],
      tableLoading1: false,
      tableLoading2: false,
      tableData1: [],
      addOrupdate: null,
      title: '',
      annexList: [],
      imageUrl: '',
      annex: {
        userId: '',
        idNumber: '',
        issueUnit: '',
        level: '',
        periodValidity: '',
        copy: '',
        original: '',
        fileName: ''
      },
      rules: {
        idNumber: [
          { required: true, message: '请输入证件号', trigger: 'blur' }
        ],
        issueUnit: [
          { required: true, message: '请输入发证单位', trigger: 'blur' }
        ],
        periodValidity: [
          { required: false, message: '请选择有效期', trigger: 'blur' }
        ]
      },
      dialogVisible1: false,
      form: {
        userId: '',
        name: '',
        account: '',
        currentState: '',
        sex: '',
        corporateName: '',
        department: '',
        departLimsId: [],
        postName: '',
        groupTime: '',
        laborRelations: '',
        workingTime: '',
        contractLifeTime: '',
        personnelClassification: '',
        dateBirth: '',
        nativePlace: '',
        nation: '',
        identityCard: '',
        age: '',
        validityPeriod: '',
        maritalStatus: '',
        idAddress: '',
        idDetailAddress: '',
        politicalStatus: '',
        dumplingTime: '',
        telephone: '',
        email: '',
        officialAcademicRedentials: '',
        highestDegree: '',
        graduatedInstitutions1: '',
        major1: '',
        graduationTime1: '',
        graduatedInstitutions2: '',
        major2: '',
        graduationTime2: '',
        lastUpdateTime: '',
        pictureUrl: '',
        signatureUrl: '',
        professionalTitle: '',
        remarks: '',
      },
      department: [],
      saveLoading: false,
      dialogVisible: false,
      checkList: [],
      successFileList: [], // 防止后端出现脏数据
      isSave: false,
    }
  },
  components: { limsTable, fileDownload },
  created() {
    this.init()
    this.searchTableList()
    this.searchTableList2()
  },
  computed: {
    action() {
      return this.javaApi + '/personBasicInfo/saveCNASFile'
    },
    fileAction() {
      return this.javaApi + '/personBasicInfo/uploadBasicInfoFile'
    },
  },
  methods: {
    // 下载档案
    downPerson() {
      console.log('this.clickNodeVal.userId',this.clickNodeVal.userId)
      exportPersonBasicInfoById({ id: this.clickNodeVal.userId }).then(res => {
        const blob = new Blob([res], { type: 'application/msword' });
        this.$download.saveAs(blob, '人员档案.docx');
      })
    },
    // 上传验证
    fileBeforeUpload(file) {
      let flag = true
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    onError(err, file, fileList, type) {
      this.$message.error('上传失败')
      this.$refs.upload.clearFiles()
    },
    handleSuccessUp(response,) {
      this.upLoading = false;
      if (response.code == 200) {
        this.$message.success('上传成功');
        this.searchTableList()
      } else {
        this.$message.error(response.message);
      }
    },
    // 查询附件列表
    searchTableList() {
      this.tableLoading = true
      getBasicInfoFileList({ userId: this.clickNodeVal.userId }).then(res => {
        this.tableLoading = false
        this.tableData = res.data
      }).catch(err => {
        this.tableLoading = false
        console.log('err---', err);
      })
    },
    // 下载
    upload(row) {
      let url = '';
      if (row.type == 1) {
        url = this.javaApi + '/img/' + row.fileUrl
        fileDownload.downloadIamge(url, row.fileName)
      } else {
        url = this.javaApi + '/word/' + row.fileUrl
        const link = document.createElement('a');
        link.href = url;
        link.download = row.fileName;
        link.click();
      }
    },
    // 删除
    delete(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading = true
        delBasicInfoFileList({ basicInfoFileId: row.basicInfoFileId }).then(res => {
          this.tableLoading = false
          this.$message.success('删除成功')
          this.searchTableList();
        }).catch(err => {
          this.tableLoading = false
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 打开工作经历探况
    annexAdd1(type, row) {
      this.operationType = type
      if (type === 'edit') {
        this.basicInfoWorkId = row.basicInfoWorkId
        this.annex2.workExperience = row.workExperience
      } else {
        this.basicInfoWorkId = ''
        this.annex2.workExperience = ''
      }
      this.dialogVisible2 = true
    },
    // 提交工作经历
    submitForm2() {
      const params = {
        workExperience: this.annex2.workExperience,
        userId: this.clickNodeVal.userId,
        basicInfoWorkId: this.basicInfoWorkId
      }
      this.tableLoading1 = true
      if (this.operationType === 'add') {
        addBasicInfoWork(params).then(res => {
          this.tableLoading1 = false
          if (res.code == 200) {
            this.dialogVisible2 = false
            this.$message.success('新增成功')
            this.searchTableList2();
          }
        }).catch(err => {
          this.tableLoading1 = false
        })
      } else {
        updateBasicInfoWorkList(params).then(res => {
          this.tableLoading1 = false
          this.dialogVisible2 = false
          this.$message.success('修改成功')
          this.searchTableList2();
        }).catch(err => {
          this.tableLoading1 = false
        })
      }
    },
    // 关闭工作经历弹框
    handleClose2() {
      this.dialogVisible2 = false
      this.annex2.workExperience = ''
    },
    // 删除工作经历
    deleteAnnex1(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableLoading1 = true
        delBasicInfoWorkList({ basicInfoWorkId: row.basicInfoWorkId }).then(res => {
          this.tableLoading1 = false
          this.$message.success('删除成功')
          this.searchTableList2();
        }).catch(err => {
          this.tableLoading1 = false
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 查询工作经历列表
    searchTableList2() {
      this.tableLoading1 = true
      getBasicInfoWorkList({ userId: this.clickNodeVal.userId }).then(res => {
        this.tableLoading1 = false
        this.tableData1 = res.data
      }).catch(err => {
        this.tableLoading1 = false
        console.log('err---', err);
      })
    },
    annexAdd(type, row) {
      if (type === 1) {
        this.title = '编辑附件资料'
        this.addOrupdate = 1
        getAnnex({ id: row.id }).then(res => {
          this.annex = res.data
          this.imageUrl = this.javaApi + '/img/' + res.data.fileName
        })
      } else {
        this.title = '新增附件资料'
        this.addOrupdate = 2
      }
      this.dialogVisible1 = true
    },
    submitForm() {
      this.$refs['annex'].validate((valid) => {
        if (valid) {
          this.addAnnex()
        } else {
          return false
        }
      })
    },
    addAnnex() {
      if (this.annex.fileName == "" || this.annex.fileName == null || this.annex.fileName == undefined) {
        this.$message.error("请上传文件")
        return
      }
      this.annex.userId = this.clickNodeVal.userId
      if (this.addOrupdate === 1) {
        updateAnnex(this.annex).then(res => {
          if (res.code == 200) {
            getAnnexByUserId({ userId: this.clickNodeVal.userId }).then(res => {
              this.imageUrl = ''
              this.$refs.fileName.clearFiles()
              this.annex.fileName = ''
              this.resetForm('annex')
              this.annexList = res.data
              this.dialogVisible1 = false
              this.$message.success('更新成功！')
            })
          }
        })
      } else {
        this.annex.id = null
        addAnnex(this.annex).then(res => {
          if (res.code == 200) {
            getAnnexByUserId({ userId: this.clickNodeVal.userId }).then(res => {
              this.imageUrl = ''
              this.$refs.fileName.clearFiles()
              this.annex.fileName = ''
              this.resetForm('annex')
              this.annexList = res.data
              this.dialogVisible1 = false
              this.$message.success('保存成功')
            })
          }
        })
      }
    },
    deleteAnnex(row) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAnnex({ id: row.id }).then(res => {
          this.$message.success('删除成功！')
          this.annexList = this.annexList.filter(item => item.id != row.id)
        })
      })
    },
    beforeAvatarUpload(file) {
      let flag = true
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        this.$refs.upload.clearFiles()
        flag = false
      }
      if (!flag) {
        return Promise.reject(flag); //正确的终止
      }
    },
    downloadFile(fileName) {
      let state = /\.(jpg|jpeg|png|gif)$/i.test(fileName)
      if (state) {
        let url = this.javaApi + '/img/' + fileName;
        fileDownload.downloadIamge(url, fileName)
      } else {
        const url = this.javaApi + '/word/' + fileName
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        link.click();
        this.$message.success('下载成功')
      }
    },
    async onSuccess(response, file, fileList, entityVal) {
      if (entityVal == 'fileName') {
        this.annex.fileName = response.data
      }
      // 在保存赋值新文件
      this.successFileList.push(response.data)
      this.$set(this.form, entityVal, response.data)
    },
    // 初始化调用
    init() {
      if (!this.clickNodeVal.userId) {
        let user = JSON.parse(localStorage.getItem('user'))
        this.getUserBasisInfo(user.userId)
        this.clickNodeVal.userId = user.userId
      } else {
        this.getUserBasisInfo(this.clickNodeVal.userId)
      }
    },
    getUserBasisInfo(userId) {
      getCNASPersonnelInfo({ userId: userId }).then(res => {
        this.form = res.data.PersonBasicInfoDto
        this.department = res.data.department
        this.annexList = res.data.annexList
        this.form.departLimsId = res.data.PersonBasicInfoDto.departLimsId.split(',').filter(a => a != "").map(Number)
      })
    },
    async save() {
      this.saveLoading = true
      this.form.userId = this.clickNodeVal.userId
      if (Array.isArray(this.form.departLimsId)) {
        if (this.form.departLimsId.length > 0) {
          this.form.departLimsId = this.form.departLimsId.join(',').trim() + ','
        } else {
          this.form.departLimsId = ''
        }
      }
      saveCNASPersonnelInfo(this.form).then(res => {
        this.saveLoading = false
        this.isSave = true
        this.getUserBasisInfo(this.clickNodeVal.userId)
        this.$message.success('保存成功！')
      })
    },
    // 取人员分类的字典
    getComparisonList() {
      if (this.checkList.length > 0) {
        this.form.personnelClassification = this.checkList.split('，')
      }
    },
    clickPersonnelClassificationSure() {
      this.dialogVisible = false
      this.form.personnelClassification = this.checkList.filter(m => m).join('，')
    },
    async deleteFile(fileName, entityVal) {
      await deleteCNASFile({ fileName: fileName }).then(res => {
        this.$message.success('删除成功！')
        this.$set(this.form, entityVal, null)
        let index = this.successFileList.indexOf(fileName)
        if (index != -1) {
          this.successFileList.splice(index, 1)
        }
      })
    },
    cancellation() {
      this.resetForm('annex')
      this.$refs.fileName.clearFiles()
      this.annex.fileName = ''
      this.imageUrl = ''
      this.dialogVisible1 = false
    },
    handleClose(done) {
      this.imageUrl = ''
      this.annex = {
        userId: '',
        idNumber: '',
        issueUnit: '',
        level: '',
        periodValidity: '',
        copy: '',
        original: '',
        fileName: ''
      }
      done();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    getAge(val) {
      this.form.age = this.calculateAge(val)
    },
    calculateAge(birthDateString) {
      // 解析出生日期字符串为日期对象
      const birthDate = new Date(birthDateString);

      // 获取当前日期
      const currentDate = new Date();

      // 计算年份差
      let age = currentDate.getFullYear() - birthDate.getFullYear();

      // 检查是否已经过了今年的生日
      const currentMonth = currentDate.getMonth();
      const currentDay = currentDate.getDate();
      const birthMonth = birthDate.getMonth();
      const birthDay = birthDate.getDate();

      if (currentMonth < birthMonth || (currentMonth === birthMonth && currentDay < birthDay)) {
        // 如果还没到今年的生日，年龄减1
        age--;
      }

      return age;
    }
  },
  watch: {
    // 监听点击el-tree的数据，进行数据刷新
    clickNodeVal: {
      handler(newVal, oldVal) {
        if (newVal.userId) {
          this.getUserBasisInfo(newVal.userId)
          this.searchTableList()
          this.searchTableList2()
        }
      },
    }
  }
}
</script>

<style scoped>
>>>.el-form-item {
  margin-bottom: 3px;
}

.el-input {
  border-radius: 15px;
}

.el-icon-picture-outline {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.uploadFile {
  margin-top: 2px;
  float: left;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #0f8bf1;
  width: 178px;
  height: 50px;
  text-align: center;
  border: 1px solid #d9d9d9;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
