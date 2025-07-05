<template>
  <div>
    <div v-if="isImage">
      <img :src="imgUrl" alt="Image Preview" />
    </div>
    <div v-if="isPdf">
      <object :data="fileUrl" type="application/pdf" width="100%" height="750px">
        <p>您的浏览器不支持 PDF 预览。<a :href="fileUrl" style="color: #3a7bfa;" target="_blank">下载 PDF 文件</a></p>
      </object>
    </div>
    <div v-if="isDoc">
      <p v-if="!isDocShow">文档无法直接预览，请下载查看。</p>
      <a :href="fileUrl" v-if="!isDocShow">下载文件</a>
      <vue-office-docx v-else :src="fileUrl" style="height: 100vh;" @rendered="renderedHandler" @error="errorHandler" />
    </div>
    <div v-if="isXls">
      <p v-if="!isDocShow">文档无法直接预览，请下载查看。</p>
      <a :href="fileUrl" v-if="!isDocShow">下载文件</a>
      <vue-office-excel v-else :src="fileUrl" :options="options" style="height: 100vh;" @rendered="renderedHandler"
        @error="errorHandler" />
    </div>
    <div v-if="isZipOrRar">
      <p>压缩文件无法直接预览，请下载查看。</p>
      <a :href="fileUrl">下载文件</a>
    </div>
    <div v-if="isCsv">
      <p v-if="csvList.length == 0">CSV 文件无法直接预览，请下载查看。</p>
      <a :href="fileUrl" v-if="csvList.length == 0">下载文件</a>
      <el-tabs type="border-card" v-if="csvList.length > 0" tab-position="bottom">
        <el-tab-pane :label="item.sheetName" v-for="(item, index) in csvList" :key="index">
          <el-table :data="item.tableData" height="75vh">
            <el-table-column :label="m.label" :prop="m.prop" v-for="(m, i) in item.column" :key="i" min-width="120px"
              show-overflow-tooltip>
              <template slot-scope="scope" slot="header">
                <div>
                  <el-tooltip :content="m.label" placement="top">
                    <div class="oneLine">
                      <span>{{ m.label }}</span>
                    </div>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div v-if="!isSupported">
      <p>不支持的文件格式</p>
    </div>
  </div>
</template>

<script>
import VueOfficeDocx from '@vue-office/docx'
//引入相关样式
import '@vue-office/docx/lib/index.css'
import VueOfficeExcel from '@vue-office/excel'
//引入相关样式
import '@vue-office/excel/lib/index.css'
export default {
  components: {
    VueOfficeDocx,
    VueOfficeExcel,
  },
  props: {
    fileUrl: {
      type: String,
      required: true
    },
    currentFile: {
      type: Object,
      required: true
    },
  },
  data() {
    return {
      isDocShow: true,
      options: {
        xls: false,       //预览xlsx文件设为false；预览xls文件设为true
        minColLength: 0,  // excel最少渲染多少列，如果想实现xlsx文件内容有几列，就渲染几列，可以将此值设置为0.
        minRowLength: 0,  // excel最少渲染多少行，如果想实现根据xlsx实际函数渲染，可以将此值设置为0.
        widthOffset: 10,  //如果渲染出来的结果感觉单元格宽度不够，可以在默认渲染的列表宽度上再加 Npx宽
        heightOffset: 10, //在默认渲染的列表高度上再加 Npx高
        beforeTransformData: (workbookData) => { return workbookData }, //底层通过exceljs获取excel文件内容，通过该钩子函数，可以对获取的excel文件内容进行修改，比如某个单元格的数据显示不正确，可以在此自行修改每个单元格的value值。
        transformData: (workbookData) => { return workbookData }, //将获取到的excel数据进行处理之后且渲染到页面之前，可通过transformData对即将渲染的数据及样式进行修改，此时每个单元格的text值就是即将渲染到页面上的内容
      },
      csvList: [],//csv文件数据
      imgUrl: ''
    }
  },
  computed: {
    isImage() {
      let state = /\.(jpg|jpeg|png|gif)$/i.test(this.fileUrl)
      this.imgUrl = this.fileUrl
      if (state) {
        this.imgUrl = this.fileUrl.replaceAll('word', 'img')
      }
      return state;
    },
    isPdf() {
      return /\.pdf$/i.test(this.fileUrl);
    },
    isDoc() {
      return /\.(doc|docx)$/i.test(this.fileUrl);
    },
    isXls() {
      let state = /\.(xls|xlsx)$/i.test(this.fileUrl)
      if (state) {
        if (/\.(xlsx)$/i.test(this.fileUrl)) {
          this.options.xls = false
        } else {
          this.options.xls = true
        }
      }
      return state;
    },
    isZipOrRar() {
      return /\.(zip|rar)$/i.test(this.fileUrl);
    },
    isCsv() {
      let state = /\.csv$/i.test(this.fileUrl)
      if (state) {
        this.loadCSVData();
        // this.main()
      }
      return state;
    },
    isSupported() {
      return this.isImage || this.isPdf || this.isDoc || this.isZipOrRar || this.isCsv || this.isXls;
    }
  },
  methods: {
    renderedHandler() {
      console.log("渲染完成")
      this.isDocShow = true
      this.resetStyle()
    },
    errorHandler() {
      console.log("渲染失败")
      this.isDocShow = false
    },
    async loadCSVData() {
      this.$axios.post(this.$api.insOrderPlan.preview, {
        id: this.currentFile.id,
      }).then(res => {
        let arr = res.data
        arr = arr.map(m => {
          let obj = {
            sheetName: m.sheetName,
            tableData: [],
            column: []
          }
          obj.tableData = this.formatCSVToTable(m.content.replaceAll('null', ' '))
          // .replaceAll('MIN','=MIN').replaceAll('MAX','=MAX').replaceAll('AVERAGE','=AVERAGE')
          for (let item in obj.tableData[0]) {
            obj.column.push({
              label: item,
              prop: item,
            })
          }
          return obj
        })
        this.csvList = arr
      }).catch(err => {
        console.log(err)
      })
    },
    formatCSVToTable(str) {
      const result = [];
      const jsonObj = str.split("\n");
      let arrHeader = [];
      for (const i in jsonObj) {
        if (typeof jsonObj[i] === 'string' && jsonObj[i].length > 0) {
          const row = `${jsonObj[i]}`;
          if (row.trim().length > 0) {
            const kv = jsonObj[i].split(',');
            if (i == 0) {
              // 获取column表头
              arrHeader = kv;
            } else {
              const obj = {};
              for (let index = 0; index < arrHeader.length; index++) {
                // 组装表格数据
                const name = String(arrHeader[index]);
                if (!arrHeader[index]) continue
                if (!obj[name]) {
                  try {
                    if (kv[index]) {
                      obj[name] = String(kv[index]);
                    } else {
                      obj[name] = '';
                    }
                  } catch (err) {
                    obj[name] = '';
                  }
                }
              }
              result.push(obj);
            }
          }
        }
      }
      return result
    },
    resetStyle() {
      const elements = document.querySelectorAll('[style*="pt"]');
      for (const element of elements) {
        const style = element.getAttribute('style');
        if (!!style) {
          element.setAttribute('style', style.replace(/pt/g, 'px'));
        }
      }
    },
  }
}
</script>

<style scoped>
img {
  max-width: 100%;
}

.oneLine {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
