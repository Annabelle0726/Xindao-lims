import axios from "axios";
import { Loading, Message } from "element-ui";
import { saveAs } from "file-saver";
import { getToken } from "@/utils/auth";
import errorCode from "@/utils/errorCode";
import { blobValidate } from "@/utils/ruoyi";
import Vue from "vue";

const baseURL = process.env.VUE_APP_BASE_API;
let downloadLoadingInstance;

export default {
  name(name, isDelete = true) {
    var url =
      baseURL +
      "/common/download?fileName=" +
      encodeURIComponent(name) +
      "&delete=" +
      isDelete;
    axios({
      method: "get",
      url: url,
      responseType: "blob",
      headers: { Authorization: "Bearer " + getToken() },
    }).then((res) => {
      const isBlob = blobValidate(res.data);
      if (isBlob) {
        const blob = new Blob([res.data]);
        this.saveAs(blob, decodeURIComponent(res.headers["download-filename"]));
      } else {
        this.printErrMsg(res.data);
      }
    });
  },
  resource(resource) {
    var url =
      baseURL +
      "/common/download/resource?resource=" +
      encodeURIComponent(resource);
    axios({
      method: "get",
      url: url,
      responseType: "blob",
      headers: { Authorization: "Bearer " + getToken() },
    }).then((res) => {
      const isBlob = blobValidate(res.data);
      if (isBlob) {
        const blob = new Blob([res.data]);
        this.saveAs(blob, decodeURIComponent(res.headers["download-filename"]));
      } else {
        this.printErrMsg(res.data);
      }
    });
  },
  zip(url, name) {
    var url = baseURL + url;
    downloadLoadingInstance = Loading.service({
      text: "正在下载数据，请稍候",
      spinner: "el-icon-loading",
      background: "rgba(0, 0, 0, 0.7)",
    });
    axios({
      method: "get",
      url: url,
      responseType: "blob",
      headers: { Authorization: "Bearer " + getToken() },
    })
      .then((res) => {
        const isBlob = blobValidate(res.data);
        if (isBlob) {
          const blob = new Blob([res.data], { type: "application/zip" });
          this.saveAs(blob, name);
        } else {
          this.printErrMsg(res.data);
        }
        downloadLoadingInstance.close();
      })
      .catch((r) => {
        console.error(r);
        Message.error("下载文件出现错误，请联系管理员！");
        downloadLoadingInstance.close();
      });
  },
  async saveAs(text, name, opts) {
    if (typeof text === "string") {
      try {
        let state = /\.(jpg|jpeg|png|gif)$/i.test(text) // 判断是否为图片
        let url1 = ''
        if (state) {
          url1 = Vue.prototype.javaApi + '/img/' + text;
        } else {
          if (text.startsWith("/word/")) {
            url1 = Vue.prototype.javaApi + text
          } else if (text.startsWith("word/")) {
            url1 = Vue.prototype.javaApi + '/' + text
          } else {
            url1 = Vue.prototype.javaApi + '/word/' + text
          }
        }
        // 使用 fetch 获取文件
        const response = await fetch(url1);
        if (!response.ok) {
          throw new Error('文件下载失败: ' + response.statusText);
        }
        // 将文件转换为 Blob
        const blob = await response.blob();
        // 使用 saveAs 保存文件
        saveAs(blob, name);
        Message.success("数据导出成功");
      } catch (error) {
        Message.error(error);
      }
    } else {
      // 流下载
      blobToText(text)
        .then((result) => {
          Message.error(result.msg);
        })
        .catch(() => {
          saveAs(text, name, opts);
          Message.success("数据导出成功");
        });
    }
  },
  async printErrMsg(data) {
    const resText = await data.text();
    const rspObj = JSON.parse(resText);
    const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode["default"];
    Message.error(errMsg);
  },
};

// 将blob转成文本
function blobToText(blob) {
  return new Promise((resolve, reject) => {
    const fileReader = new FileReader();
    fileReader.readAsText(blob);
    fileReader.onload = function () {
      try {
        const result = JSON.parse(this.result);
        if (result && result["code"] !== 200) {
          resolve(result);
        } else {
          reject();
        }
      } catch (e) {
        reject();
      }
    };
  });
}
