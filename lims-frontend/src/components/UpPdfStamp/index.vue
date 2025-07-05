<template>
  <div class="up-pdf-stamp">
    <div class="work" style="display: flex;justify-content: space-between;align-items: center;margin-bottom: 10px;"
      v-if="isUpFile">
      <input type="file" @change="handleFileUpload" accept="application/pdf" ref="fileInput" />
    </div>
    <div style="color: red;font-size: 12px;margin: 16px 0;" v-if="canvasNumPages > 0">提示：在文件范围内，单击鼠标盖章，双击鼠标已盖好章处可删除当前章</div>
    <canvas ref="pdfCanvas" @click="e => handleCanvasClick(e, index)" style="border: 1px solid #000;"
      @dblclick="e => removeStamp(e, index)" v-for="(item, index) in canvasNumPages" :key="index"></canvas>
  </div>
</template>

<script>
import jsPDF from "jspdf";
import file from '@/utils/file.js'
export default {
  props: ['isUpFile'],
  data() {
    return {
      pdfDoc: null, // 存储上传的 PDF 数据
      stamps: [], // 记录盖章的位置
      contextList: [],//canvas列表
      canvasNumPages: 0,// 存储 PDF 总页数
      stampWidth: 120, // 盖章宽度
      stampHeight: 80, // 盖章高度
      stampsName: '',
      stampsList: ['主任', '质量负责人', '技术负责人', '综合室', '通信', '电力', '装备', '储能', '射频'],
      fileName: '文件名'
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file.size > 20 * 1024 * 1024) {
        this.$refs.fileInput.value = ""; // 清空文件输入框内容
        return this.$message.error('文件大小不能超过20M')
      }
      this.lookFile(file)
    },
    lookFile(file, currentStamp) {
      this.fileName = file.name
      if (currentStamp) {
        this.stampsName = currentStamp
      } else {
        const index = this.stampsList.indexOf(m => file.name.includes(m))
        if (index > -1) {
          this.stampsName = this.stampsList[index]
        } else {
          this.stampsName = '综合室'
        }
      }
      if (file && file.type === 'application/pdf') {
        const reader = new FileReader();
        reader.onload = (e) => {
          const typedArray = new Uint8Array(e.target.result);
          this.loadPDF(typedArray);
        };
        reader.readAsArrayBuffer(file);
      } else {
        this.$message.error('请选择 PDF 文件');
      }
    },
    loadPDF(typedArray) {
      pdfjsLib.getDocument(typedArray).promise.then(pdfDoc_ => {
        this.pdfDoc = pdfDoc_;
        this.canvasNumPages = this.pdfDoc._pdfInfo.numPages
        this.stamps = []
        this.contextList = []
        for (let i = 1; i <= this.canvasNumPages; i++) {
          this.$nextTick(() => {
            this.renderPage(i); // 渲染页面
          });
          this.stamps.push([])
        }
      });
    },
    // 渲染指定页面
    renderPage(pageNum) {
      this.pdfDoc.getPage(pageNum).then(page => {
        const canvas = this.$refs.pdfCanvas[pageNum - 1];
        this.contextList.push(canvas.getContext("2d"))
        const viewport = page.getViewport({ scale: 1.5 });

        canvas.width = viewport.width;
        canvas.height = viewport.height;

        page.render({
          canvasContext: this.contextList[pageNum - 1],
          viewport: viewport
        }).promise.then(() => {
          this.stamps[pageNum - 1].forEach(m => {
            this.drawStamps(m.x, m.y, pageNum - 1)
          })
        });
      });
    },
    // 单击--添加章
    handleCanvasClick(event, i) {
      const x = event.offsetX;
      const y = event.offsetY;
      const index = this.stamps[i].findIndex(stamp => {
        let x0 = x - stamp.x;
        let y0 = y - stamp.y;
        return x0 > 0 && x0 < this.stampWidth && y0 > 0 && y0 < this.stampHeight;
      });
      if (index > -1) return;
      this.drawStamps(x, y, i)
      this.stamps[i].push({ x, y });
    },
    // 双击--删除盖章
    removeStamp(event, i) {
      const x = event.offsetX;
      const y = event.offsetY;
      // 查找被双击的盖章
      const index = this.stamps[i].findIndex(stamp => {
        let x0 = x - stamp.x;
        let y0 = y - stamp.y;
        return x0 > 0 && x0 < this.stampWidth && y0 > 0 && y0 < this.stampHeight;
      });
      if (index === -1) return;
      this.stamps[i].splice(index, 1); // 删除指定的盖章
      this.contextList[i].clearRect(0, 0, this.contextList[i].width, this.contextList[i].height);
      this.renderPage(i + 1)
    },
    // 渲染章
    drawStamps(x, y, index) {
      var img = new Image();
      // 设置图片源
      img.src = require("@/assets/stamps/" + this.stampsName + ".png"); // 替换为你的图片链接
      let that = this
      img.onload = function () {
        // 图片加载完成后，将图片绘制到canvas上
        that.contextList[index].drawImage(img, x, y, that.stampWidth, that.stampHeight);
      };
    },
    // 生成 PDF 的函数
    async generatePDF() {
      if (this.contextList.length === 0) {
        this.$message({ message: '请先上传PDF文件', type: 'error' });
        this.$emit('uploadPDFErr')
        return false
      }
      const pdf = new jsPDF("p", "mm", "a4");
      for (let i = 0; i < this.contextList.length; i++) {
        const imgData = this.$refs.pdfCanvas[i].toDataURL('image/jpeg', 0.7);
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (this.$refs.pdfCanvas[i].height * pdfWidth) / this.$refs.pdfCanvas[i].width;
        pdf.addImage(imgData, "JPEG", 0, 0, pdfWidth, pdfHeight); // 将图片添加到 PDF
        if (i !== this.contextList.length - 1) {
          pdf.addPage(); // 添加新的一页
        }
      }

      // 将 PDF 文件保存或上传
      const pdfOutput = pdf.output('blob'); // 获取 PDF 文件的 Blob 对象

      // 上传到后端
      return this.$emit('uploadPDF', pdfOutput, this.fileName)
    },
  }
}
</script>

<style scoped></style>
