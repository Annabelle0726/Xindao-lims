<template>
  <div class="parent-class">
    <div style="display: flex; justify-content: flex-end; margin-right: 20px">
      <el-button
        type="primary"
        @click="addContents"
        size="small"
        icon="el-icon-plus"
        >添加子节点</el-button
      >
      <el-button
        type="danger"
        @click="deletetContents"
        size="small"
        icon="el-icon-delete"
        >删除子节点</el-button
      >
      <el-button
        type="warning"
        @click="updateContents"
        size="small"
        icon="el-icon-edit"
        >更新子节点</el-button
      >
    </div>
    <el-form label-width="100px">
      <el-form-item label="节点名称">
        <el-input
          v-model="form.nodeName"
          style="width: 200px"
          size="small"
        ></el-input>
      </el-form-item>
      <el-form-item label="代号">
        <el-input
          v-model="form.code"
          style="width: 200px"
          size="small"
        ></el-input>
      </el-form-item>
      <el-form-item label="更新人">
        <el-select v-model="form.updateUser">
          <el-option
            v-for="item in users"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-input
          v-model="form.updateTime"
          style="width: 200px"
          size="small"
        ></el-input>
      </el-form-item>
    </el-form>

    <!-- 新增弹框 -->
    <el-dialog title="添加节点" :visible.sync="dialogVisible" width="40%">
      <el-form
        label-width="100px"
        :model="addForm"
        ref="addForm"
        :rules="rules"
      >
        <el-form-item label="父节点名称">
          <!-- <el-select v-model="addForm.parentId">
              <el-option v-for="(item,index) in nodeNames" :key="index" :label="item.nodeName" :value="item.id"></el-option>
            </el-select> -->
          <el-cascader
            v-model="addForm.parentId"
            :options="treeData"
            :props="{ checkStrictly: true, value: 'id', label: 'nodeName' }"
            clearable
          ></el-cascader>
        </el-form-item>
        <el-form-item label="节点名称" prop="nodeName">
          <el-input
            v-model="addForm.nodeName"
            style="width: 200px"
            size="small"
          ></el-input>
        </el-form-item>
        <el-form-item label="代号">
          <el-input
            v-model="addForm.code"
            style="width: 200px"
            size="small"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>

import {
  addSuppliersDirectoryContents,
  updateSuppliersDirectoryContents,
  deleteSuppliersDirectoryContentsById,
  getSuppliersDirectoryContentsNodeNames,
  selectUserCondition,
  selectProcurementSuppliesContentById,
  getProcurementSuppliesContentsNodeNames,
  addProcurementSuppliesContents, deleteProcurementSuppliesContentById
} from '@/api/cnas/externalService/serviceAndSupplyPro/serviceAndSupplyPro'
export default {
  props: {
    id: {
      type: Number,
      default: 0,
    },
    treeData: {
      type: Array,
      default: () => [],
    },
    from: {
      type: String,
      default: "耗材树"
    }
  },
  data() {
    return {
      nodeNames: [],
      users: [],
      dialogVisible: false,
      form: {
        nodeName: "",
        code: "",
        updateUser: "",
        updateTime: "",
      },
      addForm: {
        nodeName: "",
        code: "",
        updateUser: "",
        updateTime: "",
        parentId: null,
      },
      rules: {
        nodeName: [
          { required: true, message: "请输入节点名称", trigger: "blur" },
        ],
      },
    };
  },
  mounted() {
    if (this.id !== 0) {
      this.getContentsDetail();
    }
    this.getNodeNames();
    this.getUserList();
  },
  watch: {
    id(newVal, oldVal) {
      if (newVal !== 0) {
        console.log(newVal, oldVal);
        this.getContentsDetail();
      }
    },
  },
  methods: {
    // 获取目录详情
    getContentsDetail() {
      selectProcurementSuppliesContentById({id:this.id})
        .then((res) => {
          if (res.data == null) {
            this.form = {
              nodeName: "",
              code: "",
              updateUser: "",
              updateTime: "",
            };
            return;
          }
          this.form = res.data;
        });
    },
    // 新建
    addContents() {
      this.dialogVisible = true;
      this.resetForm();
    },
    resetForm() {
      this.addForm = {
        nodeName: "",
        code: "",
        updateUser: "",
        updateTime: "",
        parentId: null,
      };
    },
    async submitForm() {
      let flag = true;
      this.$refs.addForm.validate((valid) => {
        if (!valid) {
          flag = false;
          return false;
        }
      });
      if (this.addForm.parentId) {
        this.addForm.parentId =
          this.addForm.parentId[this.addForm.parentId.length - 1];
      }
      if (!flag) {
        return;
      }
      try {
        let res = null
        console.log('this.from',this.from)
        if(this.from == '耗材树' ){
          console.log('111')
          res = await addProcurementSuppliesContents(this.addForm)
        }else {
          console.log('222')
          res = await addSuppliersDirectoryContents(this.addForm)
        }
        if (res.code === 200) {
          this.$message.success("添加成功");
          this.dialogVisible = false;
          this.$emit("contentsUpdate", res.data);
          this.getContentsDetail();
        }
      }catch (error){
        this.$message.error("添加失败");
      }
    },
    // 更新
    updateContents() {
      Object.keys(this.form).forEach((key) => {
        if (key == "children") {
          delete this.form[key];
        }
      });
      updateSuppliersDirectoryContents(this.form).then((res) => {
          if (res.code === 200) {
            this.$message.success("更新成功");
            this.$emit("contentsUpdate", this.id);
            this.dialogVisible = false;
            this.getContentsDetail();
          }
        });
    },
    // 删除
    deletetContents() {
      if (
        this.form.id == null ||
        this.form.id == "" ||
        this.form.id == undefined
      ) {
        this.$message.error("请选择要删除的节点");
        return;
      }
      this.$confirm("此操作将删除该节点, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        if(this.from == '耗材树'){
          deleteProcurementSuppliesContentById({id:this.form.id}).then((res) => {
            if (res.code == 200) {
              this.$message.success("删除成功");
              this.$emit("contentsUpdate", this.form.parentId, true);
              this.getContentsDetail();
            }
          });
        }else {
          deleteSuppliersDirectoryContentsById({id:this.form.id}).then((res) => {
            if (res.code == 200) {
              this.$message.success("删除成功");
              this.$emit("contentsUpdate", this.form.parentId, true);
              this.getContentsDetail();
            }
          });
        }
      });
    },
    // 获取所有目录节点
    getNodeNames() {
      if(this.from == '耗材树'){
        getProcurementSuppliesContentsNodeNames().then(res => {
          this.nodeNames = res.data;
        })
      }else {
        getSuppliersDirectoryContentsNodeNames().then(res => {
          this.nodeNames = res.data;
        })
      }
    },
    // 获取所有用户
    getUserList() {
      selectUserCondition().then((res) => {
        this.users = res.data;
      })
    },
  },
  created() {},
};
</script>

  <style scoped>
.parent-class {
  margin-top: 20px;
}
</style>
