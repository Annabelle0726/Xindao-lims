<template>
  <div class="app-container">
    <el-row :gutter="20">
      <splitpanes :horizontal="this.$store.getters.device === 'mobile'" class="default-theme">
        <!--部门数据-->
        <pane size="12">
          <el-col>
            <div class="head-container addButton">
              <el-input v-model="deptName" placeholder="部门名称" clearable size="small" prefix-icon="el-icon-search"
                style="margin-bottom: 20px" />
              <!-- <el-button style="margin-left: 4px" type="primary" plain icon="el-icon-plus" size="mini" circle
                @click="addSchema"></el-button> -->
            </div>
            <div class="head-container">
              <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false"
                :filter-node-method="filterNode" ref="tree" node-key="id" default-expand-all highlight-current
                @node-click="handleNodeClick" />
            </div>
          </el-col>
        </pane>
        <!--用户数据-->
        <pane size="88">
          <div class="search_form">
            <div>
              <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
                <el-form-item label="用户名称" prop="nickName">
                  <el-input v-model="queryParams.nickName" placeholder="请输入用户名称" clearable
                    @keyup.enter.native="handleQuery" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                  <el-select v-model="queryParams.status" placeholder="用户状态" clearable @change="handleQuery">
                    <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="mini" @click="handleQuery">查询</el-button>
                  <el-button size="mini" @click="resetQuery">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="options_button">
              <el-button type="primary" size="mini" @click="openthirdParty"
                v-hasPermi="['system:user:add']">获取三方人员</el-button>
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                v-hasPermi="['system:user:add']">新增用户</el-button>
            </div>
          </div>
          <el-col>
            <el-table v-loading="loading" :data="userList" :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border>
              <el-table-column label="序号" align="center" type="index" />
              <el-table-column label="姓名" align="center" key="nickName" prop="nickName" :show-overflow-tooltip="true" />
              <el-table-column label="账号" align="center" key="userName" prop="userName" :show-overflow-tooltip="true" />
              <el-table-column label="角色" align="center" key="roleName" prop="roleName" :show-overflow-tooltip="true" />
              <el-table-column label="状态" align="center" key="status">
                <template slot-scope="scope">
                  <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
                    @change="handleStatusChange(scope.row)"></el-switch>
                </template>
              </el-table-column>
              <el-table-column label="手机号码" align="center" key="phonenumber" prop="phonenumber" width="120" />
              <el-table-column label="操作" align="center" width="160" class-nickName="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                    v-hasPermi="['system:user:edit']">修改</el-button>
                  <!--                  <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:user:remove']">删除</el-button>-->
                  <!--                  <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">-->
                  <!--                    <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>-->
                  <!--                    <el-dropdown-menu slot="dropdown">-->
                  <!--                      <el-dropdown-item command="handleResetPwd" icon="el-icon-key" v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>-->
                  <!--                      <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check" v-hasPermi="['system:user:edit']">分配角色</el-dropdown-item>-->
                  <!--                    </el-dropdown-menu>-->
                  <!--                  </el-dropdown>-->
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize" @pagination="getList" />
          </el-col>
        </pane>
      </splitpanes>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body @close="reset">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账号" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户昵称" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{
                  dict.label
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话号码" prop="phonenumber">
              <el-input v-model="form.phonenumber" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色" prop="roleIds">
              <el-select v-model="form.roleIds" multiple placeholder="请选择角色" clearable>
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"
                  :disabled="item.status == 1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名EN" prop="nameEn">
              <el-input v-model="form.nameEn" placeholder="请输入姓名EN" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单位" prop="company">
              <el-select v-model="form.company" placeholder="请选择单位" style="width: 100%" clearable>
                <el-option v-for="item in postOptions" :key="item.id" :label="item.company"
                  :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="enabledDeptOptions" :show-count="true"
                placeholder="请选择归属部门" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="签名">
              <el-upload class="avatar-uploader" :action="uploadAction" :show-file-list="false"
                :headers="upload.headers" accept=".png, .jpg, .jpeg, .gif" :on-error="handleUploadError"
                :on-success="handleUploadSuccess" :before-upload="handleBeforeUpload">
                <img v-if="form.signatureUrl" :src="javaApi + '/img/' + form.signatureUrl" class="avatar" alt="">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="个人照片">
              <el-upload class="avatar-uploader" :action="uploadAction" :show-file-list="false"
                :headers="upload.headers" accept=".png, .jpg, .jpeg, .gif" :on-error="handleUploadError1"
                :on-success="handleUploadSuccess1" :before-upload="handleBeforeUpload1">
                <img v-if="form.pictureUrl" :src="javaApi + '/img/' + form.pictureUrl" class="avatar" alt="">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="upload.open = false">取 消</el-button>
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 获取人事系统人员信息 -->
    <el-dialog title="获取人事系统人员信息" :visible.sync="addthirdParty" width="70%">
      <div class="body" v-loading="thirdPartyLoading">
        <el-row>
          <el-col :span="8" style="height: 70vh;overflow: hidden;" v-if="companiesList.length != 1">
            <el-input v-model="search2" placeholder="输入关键字搜索" clearable size="small" @clear="searchFilter2"
              @keyup.enter.native="searchFilter2" prefix-icon="el-icon-search" style="margin-bottom: 20px;width: 90%" />
            <el-tree :data="datathirdParty" node-key="companyId" :props="defaultProps1" @node-click="nodeClick2"
              style="height: calc(100% - 42px);" @node-expand="nodeOpen0" :filter-node-method="filterNode2" ref="tree2"
              highlight-current>
            </el-tree>
          </el-col>
          <el-col :span="companiesList.length == 1 ? 24 : 16" style="height: 70vh;padding-left: 8px;">
            <div style="display: flex;align-items: center;margin-bottom: 10px">
              <span style="width: 140px">员工号/员工姓名:</span>
              <el-input size="small" placeholder="请输入员工号/员工姓名" @clear="searchPerson" clearable v-model="userSearch2"
                style="width: 50%" @keyup.enter.native="searchPerson()"></el-input>
            </div>
            <el-table height="67vh" stripe :data="personList" v-loading="personLoad" ref="personTable"
                      :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
              @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50">
              </el-table-column>
              <el-table-column prop="employeeID" label="员工号">
              </el-table-column>
              <el-table-column prop="name" label="员工姓名">
              </el-table-column>
              <el-table-column prop="department" label="部门" min-width="200">
              </el-table-column>
              <el-table-column prop="isLive" label="已存在" width="80" align="center">
                <template slot-scope="scope">
                  <el-tag type="success" effect="dark" v-if="scope.row.isLive === 1">√</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addthirdParty = false">取 消</el-button>
        <el-button type="primary" @click="addUser2" :loading="addLoad">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="架构新增" :visible.sync="addDia" width="400px">
      <div class="body">
        <el-row style="line-height: 50px;">
          <el-col :span="6" style="text-align: right;">
            <span class="required-span">* </span>架构名称：
          </el-col>
          <el-col :span="16" :offset="1">
            <el-input v-model="addOb.nickName" placeholder="请输入架构名称" clearable size="small"></el-input>
          </el-col>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDia = false">取 消</el-button>
        <el-button type="primary" @click="addStandardTree" :loading="addLoad">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  resetUserPwd,
  changeUserStatus,
  deptTreeSelect,
  selectCompaniesList, selectSimpleList, addPersonUser, uploadFile, selectRoleList, selectCustomEnum, addDepartment
} from "@/api/system/user";
import { getToken } from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";

export default {
  nickName: "User",
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { Treeselect, Splitpanes, Pane },
  data() {
    return {
      // 遮罩层
      loading: true,
      multipleSelection: [],
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 所有部门树选项
      deptOptions: undefined,
      // 过滤掉已禁用部门树选项
      enabledDeptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      defaultProps1: {
        children: "children",
        label: "companyName"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      uploadAction: process.env.VUE_APP_BASE_API + '/deviceScope/uploadFile',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "请填写姓名", trigger: "blur" },
        ],
        userName: [
          { required: true, message: "请填写账号", trigger: "blur" }
        ],
        // nameEn: [
        //   { required: true, message: "请输入姓名EN", trigger: "blur" }
        // ],
        status: [
          { required: true, message: "请选择状态", trigger: "change" }
        ],
        roleIds: [
          { required: true, message: "请选择角色", trigger: "change" }
        ],
        // password: [
        //   { required: true, message: "密码不能为空", trigger: "blur" },
        // ],
        phonenumber: [
          {
            required: true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      // 获取三方人员弹框
      addthirdParty: false,
      thirdPartyLoading: false,
      companiesList: [],
      search2: '',
      datathirdParty: [],
      currentCompaniesList: [],
      personList: [],
      personListCopy: [],
      personLoad: false,
      userSearch2: '',
      addLoad: false,
      //
      addDia: false, // 添加架构弹框
      addOb: {
        fatherId: 10001,
        nickName: '',
      },
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getDeptTree();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },
    // 打开添加架构弹框
    addSchema() {
      this.addDia = true
    },
    // 提交架构
    addStandardTree() {
      if (this.addOb.nickName == null || this.addOb.factory == '') {
        this.$message.error('构架名称是必填项')
        return
      }
      this.addLoad = true
      addDepartment(this.addOb).then(res => {
        this.$message.success('添加成功')
        this.addDia = false
        this.getList()
        this.addLoad = false
        this.addOb.nickName = ''
      }).catch(e => {
        this.addDia = false
        this.addLoad = false
      })
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
        this.enabledDeptOptions = this.filterDisabledDept(JSON.parse(JSON.stringify(response.data)));
      });
    },
    // 过滤禁用的部门
    filterDisabledDept(deptList) {
      return deptList.filter(dept => {
        if (dept.disabled) {
          return false;
        }
        if (dept.children && dept.children.length) {
          dept.children = this.filterDisabledDept(dept.children);
        }
        return true;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.nickName + '"用户吗？').then(function () {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        nickName: undefined,
        userName: undefined,
        phonenumber: undefined,
        password: undefined,
        nameEn: undefined,
        email: undefined,
        sex: undefined,
        status: "0",
        company: '',
        roleIds: [],
        signatureUrl: '',
        pictureUrl: ''
      };
      // this.signatureUrl = '';
      // this.pictureUrl = ''
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.deptId = undefined;
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    // 获取三方人员
    async openthirdParty() {
      try {
        this.addthirdParty = true;
        this.thirdPartyLoading = true;

        const companiesResponse = await selectCompaniesList();
        this.companiesList = this.HaveJson(companiesResponse.data);

        if (this.companiesList.length === 1) {
          const personResponse = await selectSimpleList({ companyId: this.companiesList[0].companyId });
          this.personListCopy = JSON.parse(JSON.stringify(personResponse.data));
          this.personList = personResponse.data;
          this.$refs.personTable.doLayout();
        }

        this.datathirdParty = this.tranListToTreeData(this.companiesList, "ROOT");
        console.log(' this.datathirdParty---',  this.datathirdParty)
      } catch (error) {
        console.error("Error fetching third party data:", error);
      } finally {
        this.thirdPartyLoading = false;
      }
    },
    tranListToTreeData(list, rootValue) {
      return list.reduce((arr, item) => {
        if (item.parentCompanyId === rootValue) {
          const children = this.tranListToTreeData(list, item.companyId);
          if (children.length) {
            item.children = children;
          }
          arr.push(item);
        }
        return arr;
      }, []);
    },
    searchPerson() {
      let arr = JSON.parse(JSON.stringify(this.personListCopy))
      this.personList = arr.filter(a => {
        if (a.employeeID.includes(this.userSearch2) || a.nickName.includes(this.userSearch2)) {
          return true
        }
      })
    },
    searchFilter2() {
      this.$refs.tree2.filter(this.search2)
    },
    nodeClick2(ob, node, el) {
      this.getNodeParent(node)
      if (ob.companyId !== 'SC21') {
        this.personLoad = true
        selectSimpleList({ companyId: ob.companyId }).then(res => {
          this.personListCopy = JSON.parse(JSON.stringify(res.data))
          this.personList = res.data
          this.personLoad = false
          this.$refs.personTable.doLayout()
        })
      }
    },
    getNodeParent(val) {
      this.currentCompaniesList[val.level - 1] = val.data.companyId
      if (val.parent != null) {
        this.getNodeParent(val.parent)
      }
    },
    nodeOpen0(data, node, el) {
      // this.currentCompaniesList[node.level - nodeOpen01] = data.id
    },
    filterNode2(value, data) {
      if (!value) return true;
      return data['companyName'].indexOf(value) !== -1;
    },
    addUser2() {
      if (this.multipleSelection.length === 0) {
        return this.$message.error('请选择人员')
      }
      if (this.currentCompaniesList.length === 0) {
        return this.$message.error('请选择组织')
      }
      /* for (let index = this.currentCompaniesList.length-1; index >1; index--) {
        let obj = this.multipleSelection.find(a=>a.companyId==this.currentCompaniesList[index])
        if(!obj){
          this.currentCompaniesList.splice(index,1)
        }
      } */
      let arr = []
      this.currentCompaniesList.forEach(b => {
        let obj = this.companiesList.find(a => a.companyId == b)
        arr.push(obj)
      })
      this.addLoad = true
      addPersonUser({
        company: arr,
        person: this.multipleSelection,
        // roleId: this.componentData.entity.roleId
      }).then(res => {
        this.$message.success('操作成功')
        this.multipleSelection = []
        this.$refs.personTable.clearSelection()
        this.addLoad = false
        this.addthirdParty = false
        this.userSearch2 = ''
        this.getList()
      }).catch(e => {
        this.addLoad = false
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      selectCustomEnum().then(res => {
        this.postOptions = res.data;
      })
      getUser().then(response => {
        this.roleOptions = response.roles;
        this.title = "添加用户";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      selectCustomEnum().then(res => {
        this.postOptions = res.data;
      })
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.form.password = ''
        this.roleOptions = response.roles;
        this.$set(this.form, "roleIds", response.roleIds);
        this.open = true;
        this.title = "修改用户";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.nickName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间",
        inputValidator: (value) => {
          if (/<|>|"|'|\||\\/.test(value)) {
            return "不能包含非法字符：< > \" ' \\\ |"
          }
        },
      }).then(({ value }) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess("修改成功，新密码是：" + value);
        });
      }).catch(() => { });
    },
    /** 分配角色操作 */
    handleAuthRole: function (row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function () {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {
      }, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      const type = ["image/jpeg", "image/jpg", "image/png", "image/svg"];
      const isJPG = type.includes(file.type);
      // 检验文件格式
      if (!isJPG) {
        this.$message.error(`图片格式错误!`);
        return false;
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      return true;
    },
    handleUploadSuccess(res, file) {
      // 如果上传成功
      if (res.code == 200) {
        // 获取富文本组件实例
        // let quill = this.Quill;
        // // 获取光标所在位置
        // let length = quill.getSelection().index;
        // // 插入图片  res.url为服务器返回的图片地址
        // quill.insertEmbed(length, "image", process.env.VUE_APP_BASE_API + res.fileName);
        // // 调整光标到最后
        // quill.setSelection(length + 1);
        this.form.signatureUrl = res.data.url
      } else {
        this.$message.error("图片插入失败");
      }
    },
    handleUploadError() {
      this.$message.error("图片插入失败");
    },
    // 上传前校检格式和大小
    handleBeforeUpload1(file) {
      const type = ["image/jpeg", "image/jpg", "image/png", "image/svg"];
      const isJPG = type.includes(file.type);
      // 检验文件格式
      if (!isJPG) {
        this.$message.error(`图片格式错误!`);
        return false;
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize;
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`);
          return false;
        }
      }
      return true;
    },
    handleUploadSuccess1(res, file) {
      // 如果上传成功
      if (res.code == 200) {
        // 获取富文本组件实例
        // let quill = this.Quill;
        // // 获取光标所在位置
        // let length = quill.getSelection().index;
        // // 插入图片  res.url为服务器返回的图片地址
        // quill.insertEmbed(length, "image", process.env.VUE_APP_BASE_API + res.fileName);
        // // 调整光标到最后
        // quill.setSelection(length + 1);
        this.form.pictureUrl = res.data.url
      } else {
        this.$message.error("图片插入失败");
      }
    },
    handleUploadError1() {
      this.$message.error("图片插入失败");
    },
  }
};
</script>

<style scoped lang="scss">
.search_form {
  display: flex;
  justify-content: space-between;

  .options_button {
    margin-top: 3px;
  }
}

.avatar-uploader ::v-deep .el-upload {
  border: 1px dashed #666666;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader ::v-deep .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 20px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  line-height: 90px;
  text-align: center;
}

.avatar {
  width: 90px;
  height: 90px;
  display: block;
}

.addButton {
  display: flex;
  align-items: flex-start;
}
</style>
