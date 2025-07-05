<style scoped>
#luckysheet {
  margin: 0px;
  padding: 0px;
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0px;
  top: 0px;
  z-index: 99999 !important;
}
</style>
<style>
#luckysheet .save {
  width: 50px;
  height: 26px;
  color: #fff;
  background-color: #3a7bfa;
  border-radius: 4px;
  border-color: transparent;
  cursor: pointer;
}

#luckysheet .returnView {
  width: 50px;
  height: 26px;
  background-color: #fff;
  border: 1px solid #dcdfe6;
  color: #606266;
  border-radius: 4px;
  margin-right: 12px;
  cursor: pointer;
}

#luckysheet .save_i {
  margin-right: 20px;
  font-size: 20px;
  cursor: help;
}

#luckysheet .save:hover {
  opacity: 0.8;
}

#luckysheet .save:active {
  opacity: 0.7;
}

/* #luckysheet-postil-overshow {
  z-index: 9999 !important;
} */
</style>
<template>
  <div id="luckysheet"></div>
</template>

<script>
// import { data } from 'jquery';
export default {
  props: ["data", "execlTitle"],
  data() {
    return {};
  },
  mounted() {
    this.$nextTick(() => {
      this.templateWrite();
    });
  },
  methods: {
    templateWrite() {
      var option = {};
      if (this.data != null && this.data != "") {
        option = JSON.parse(this.data);
      } else {
        let rowlen = {};
        let columnlen = {};
        for (let a = 0; a < 50; a++) {
          rowlen["" + a] = 40;
        }
        for (let a = 0; a < 26; a++) {
          columnlen["" + a] = 100;
        }
        option = {
          data: [
            {
              name: "模板",
              order: "0",
              config: {
                rowlen: rowlen,
                columnlen: columnlen,
              },
            },
          ],
          row: 50,
          column: 26,
        };
      }
      option.title = this.execlTitle;
      (option.container = "luckysheet"),
        (option.lang = "zh"),
        (option.showsheetbar = false),
        (option.defaultFontSize = 14);
      (option.showstatisticBarConfig = {
        view: false,
      }),
        (option.enableAddRow = false),
        (option.enableAddBackTop = false),
        (option.showtoolbarConfig = {
          chart: false, // '图表'
          pivotTable: false, //'数据透视表'
          protection: false, // '工作表保护'
          dataVerification: false, // '数据验证'
          frozenMode: false, // '冻结方式'
          currencyFormat: false, //货币格式
          percentageFormat: true, //百分比格式
          numberDecrease: true, // '减少小数位数'
          numberIncrease: true, // '增加小数位数
          moreFormats: true, // '更多格式'
          strikethrough: false, // '删除线 (Alt+Shift+5)'
          underline: false, // '下划线 (Alt+Shift+6)'
          italic: false, // '斜体 (Ctrl+I)'
          textWrapMode: true, // '换行方式'
          textRotateMode: false, // '文本旋转方式'
          conditionalFormat: false, // '条件格式'
          splitColumn: false, // '分列'
        }),
        (option.cellRightClickConfig = {
          copyAs: false, // 复制为
          hideRow: false, // 隐藏选中行和显示选中行
          hideColumn: false, // 隐藏选中列和显示选中列
          sort: false, // 排序选区
          filter: false, // 筛选选区
          chart: false, // 图表生成
          image: false, // 插入图片
          matrix: false, // 矩阵操作选区
          data: false, // 数据验证
          cellFormat: false, // 设置单元格格式
        }),
        (option.myFolderUrl = this.LOCATIONVUE + "/"),
        // option.functionButton = '<i class="save_i el-icon-info" title="系统支持的变量：&#10样品编号&#10样品型号&#10序号&#10检验项&#10检验子项&#10设备名称&#10设备编码&#10单位&#10要求值&#10试验方法 | 检测方法&#10最终值&#10结论&#10计算值&#10检验值"></i><button onClick="excelExport()" class="save">导出</button><button onClick="excelClosed()" class="save">保存</button>'
        (option.functionButton =
          '<i class="save_i el-icon-info" title="系统支持的变量：&#10样品编号&#10样品型号&#10序号&#10检验项&#10检验子项&#10设备名称&#10设备编码&#10单位&#10要求值&#10试验方法 | 检测方法&#10最终值&#10结论&#10计算值&#10检验值"></i><button onClick="returnView()" class="returnView">取消</button><button onClick="excelClosed()" class="save">保存</button>');
      console.log(luckysheet);
      luckysheet.create(option);
    },
  },
};
</script>
