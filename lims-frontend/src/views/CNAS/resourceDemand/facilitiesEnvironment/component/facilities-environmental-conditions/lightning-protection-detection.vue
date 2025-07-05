<template>
  <div>
    <div class="header">
      <div>防雷检测</div>
      <div>
        <el-button size="small" type="primary" @click="clickAdd">导 入</el-button>
        <el-button size="small" type="primary" @click="downLoadPost">导 出</el-button>
      </div>
    </div>
    <el-table
      :data="tableData"
      height="calc(100vh - 18em)"
      :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" border
      style="width: 100%">
      <el-table-column label="序号" type="index" width="120">
        <template v-slot="scope">
          <span>{{ (search.current - 1) * search.size + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="原文件名"
        min-width="180"
        prop="fileName">
      </el-table-column>
      <el-table-column
        label="检测日期"
        min-width="180"
        prop="detectionDate">
      </el-table-column>
      <el-table-column
        label="有效期"
        min-width="180"
        prop="termValidity">
      </el-table-column>
      <el-table-column
        label="检测单位"
        min-width="180"
        prop="detectionUnit">
      </el-table-column>
      <el-table-column fixed="right" label="操作" min-width="100">
        <template v-slot="scope">
          <el-button size="small" type="text" @click="edit(scope.row)">编辑</el-button>
          <el-button size="small" type="text" @click="deleteRowFun(scope.row)">删除</el-button>
          <el-button size="small" type="text" @click="download(scope.row)">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination :current-page="1" :page-size="search.size" :page-sizes="[10, 20, 30, 50, 100]"
                   :total="search.total" layout="->,total, sizes, prev, pager, next, jumper"
                   @size-change="handleSizeChange" background
                   @current-change="handleCurrentChange">
    </el-pagination>
    <el-dialog
      :visible.sync="dialogVisible"
      title="新 增"
      width="50%">
      <div style="height: 50vh;">
        <el-form ref="form" :model="form" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入测试地点', trigger: 'blur' }]" label="检测日期"
                            prop="detectionDate">
                <el-date-picker
                  v-model="form.detectionDate"
                  format="yyyy-MM-dd"
                  placeholder="选择日期"
                  size="small"
                  style="width: 100%"
                  type="date"
                  value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :rules="[{ required: true, message: '请输入测试地点', trigger: 'blur' }]" label="有效期"
                            prop="termValidity">
                <el-date-picker
                  v-model="form.termValidity"
                  format="yyyy-MM-dd"
                  placeholder="选择日期"
                  size="small"
                  style="width: 100%"
                  type="date"
                  value-format="yyyy-MM-dd">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item :rules="[{ required: true, message: '请输入测试地点', trigger: 'blur' }]" label="检测单位"
                            prop="detectionUnit">
                <el-input v-model="form.detectionUnit" size="small"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检测单位" >
                <el-upload
                  :file-list="form.fileList"
                  :http-request="httpRequest"
                  :limit="1"
                  :on-exceed="handleExceed"
                  :on-remove="handleRemove"
                  action="#"
                  class="upload-demo"
                  drag
                  multiple>
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addImport">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

import {
  getLightningProtectionDetection,
  addLightningProtectionDetection,
  deleteLightningProtectionDetection,
  exportOfLightningProtectionDetection
} from '@/api/cnas/resourceDemand/facilitiesEnvironment/facilitiesAndEnvironment'

import axios from 'axios';
import {deleteCNASFile} from "@/api/cnas/personal/personalList";

export default {
  data() {
    return {
      search: {
        size: 10,
        current: 1,
        total: 0
      },
      tableData: [],
      dialogVisible: false,
      form: {
        fileList: [],
        fileData: []
      },
    }
  },
  mounted() {
    this.initData()
  },
  watch: {
    dialogVisible(newVal) {
      if (!newVal) {
        this.form = {
          fileList: [],
          fileData: []
        }
      }
    }
  },
  methods: {
    // 导出
    downLoadPost() {
      exportOfLightningProtectionDetection().then(res => {
        this.outLoading = false
        const blob = new Blob([res],{ type: 'application/msword' });
        //将Blob 对象转换成字符串
        this.$download.saveAs(blob,'防雷检测导出.xlsx')
      })
    },
    initData() {
      getLightningProtectionDetection({...this.search}).then(res => {
        if (res.code === 200){
          this.tableData = res.data.records;
          this.search.total = res.data.total;
        }

      });
    },
    handleSizeChange(val) {
      this.search.size = val;
      this.initData();
    },
    handleCurrentChange(val) {
      this.search.current = val;
      this.initData();
    },
    clickAdd() {
      this.dialogVisible = true
    },
    // 文件数量过多时提醒
    handleExceed() {
      this.$message({type: 'error', message: '最多支持1个附件上传'})
    },
    // 覆盖默认的上传行为，可以自定义上传的实现，将上传的文件依次添加到fileList数组中,支持多个文件
    httpRequest(option) {
      this.form.fileData.push(option)
    },
    addImport() {
      console.log(this.form)
      this.$refs.form.validate((valid) => {
        if (valid) {
          let params = new FormData()
          if (this.form.lightningProtectionId) {
            params.append("lightningProtectionId", this.form.lightningProtectionId)
          }
          params.append("termValidity", this.form.termValidity)
          params.append("detectionUnit", this.form.detectionUnit)
          params.append("detectionDate", this.form.detectionDate)
          if (this.form.fileData.length > 0) {
            params.append("file", this.form.fileData[0].file)
          }
          addLightningProtectionDetection(params).then(res => {
            if (res.code === 200){
              this.dialogVisible = false
              this.initData()
            }
          });
        }
      });
    },
    edit(row) {
      this.dialogVisible = true
      this.form = {...row}
      this.form.fileList = []
      this.form.fileData = []
      this.form.fileList.push({name: row.systemFileName, url: "123434"})
      console.log(this.form)
    },
    deleteRowFun(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteLightningProtectionDetection({lightningProtectionId:row.lightningProtectionId}).then(res => {
          this.$message.success('删除成功！')
          this.initData()
        })
      })
    },
    handleRemove(file) {
      deleteCNASFile({fileName: file.name}).then(res => {
        this.$message.success('删除成功！')
        let index = this.form.fileList.indexOf(file.name)
        if (index !== -1) {
          this.form.fileList.splice(index, 1)
        }
      })
    },
    download(row) {
      if (!row.systemFileName) {
        this.$message.warning('暂无文件')
        return
      }
      this.$download.saveAs(row.systemFileName, row.fileName)
    }
  }
}
</script>

<style scoped>
.header {
  height: 3em;
  width: 100%;
  display: flex;
  justify-content: space-between;
}
</style>
