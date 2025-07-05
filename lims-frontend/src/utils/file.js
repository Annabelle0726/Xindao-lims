import { convertToHtml } from "mammoth";
import pako from "pako";
import Vue from "vue";

export default {
  async convertFileToHtml(url) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", Vue.prototype.javaApi + url, true); //获取文件流的接口
    xhr.send();
    xhr.responseType = "blob"; //不能漏
    let xhrPromise = new Promise((resolve, reject) => {
      xhr.onload = async function () {
        if (this.status === 200) {
          // 返回的文件流，转换成blob对象
          var blob = new Blob([this.response], {
            type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
          });
          // 使用mammoth将Word转换为HTML
          let reader = new FileReader();
          reader.readAsArrayBuffer(blob);
          let htmlContentPromise = new Promise((resolve, reject) => {
            reader.onload = async function () {
              var arrayBuffer = xhr.response; //arrayBuffer
              const result = await convertToHtml({ arrayBuffer: arrayBuffer });
              let html = result.value
                .replace(//g, "")
                .replace("<h1>", '<h1 style="text-align: center;">')
                .replace(
                  /<table>/g,
                  '<table style="border-collapse: collapse;border: 1px solid #000;">'
                )
                .replace(/<tr>/g, '<tr style="height: 30px;">')
                .replace(/<td>/g, '<td style="border: 1px solid #000;">')
                .replace(/<p>/g, '<p style="text-indent: 2em;">')
                .replace(/<a [^>]*>/g, "")
                .replace(/<\/a>/g, "");
              // .replace(/em/g, "cm");
              resolve(html);
            };
          });
          resolve(await htmlContentPromise);
        }
      };
    });
    return await xhrPromise;
  },
  downloadIamge(imgsrc, name) {
    //下载图片地址和图片名
    var image = new Image();
    // 解决跨域 Canvas 污染问题
    image.setAttribute("crossOrigin", "anonymous");
    image.onload = function () {
      var canvas = document.createElement("canvas");
      canvas.width = image.width;
      canvas.height = image.height;
      var context = canvas.getContext("2d");
      context.drawImage(image, 0, 0, image.width, image.height);
      var url = canvas.toDataURL("image/png"); //得到图片的base64编码数据

      var a = document.createElement("a"); // 生成一个a元素
      var event = new MouseEvent("click"); // 创建一个单击事件
      a.download = name || "photo"; // 设置图片名称
      a.href = url; // 将生成的URL设置为a.href属性
      a.dispatchEvent(event); // 触发a的单击事件
    };
    image.src = imgsrc;
  },
  // 压缩blob
  compressBlob(blob) {
    const reader = new FileReader();
    reader.readAsArrayBuffer(blob);
    return new Promise((resolve) => {
      reader.onload = () => {
        const arrayBuffer = reader.result;
        const uint8Array = new Uint8Array(arrayBuffer);
        const compressedData = pako.deflate(uint8Array);
        const compressedBlob = new Blob([compressedData], { type: blob.type });
        resolve(compressedBlob);
      };
    });
  },
};
