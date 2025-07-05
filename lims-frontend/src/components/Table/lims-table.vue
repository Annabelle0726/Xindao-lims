<template>
  <div>
    <!-- 表格 -->
    <el-table ref="multipleTable" v-loading="tableLoading" :border="border" :data="tableData"
              :header-cell-style="{ background: '#f8f8f9', color: '#515a6e' }" :height="height"
              :highlight-current-row="highlightCurrentRow" :row-class-name="rowClassName" :row-style="rowStyle"
              :row-key="rowKey" :span-method="spanMethod" stripe style="width: 100%" tooltip-effect="dark"
              @row-click="rowClick"
              @current-change="currentChange" @selection-change="handleSelectionChange" class="lims-table">
      <el-table-column align="center" type="selection" width="55" v-if="isSelection"/>
      <el-table-column align="center" label="序号" type="index" width="60" :index="indexMethod"/>

      <el-table-column v-for="(item, index) in column" :key="index" :column-key="item.columnKey"
                       :filter-method="item.filterHandler" :filter-multiple="item.filterMultiple"
                       :filtered-value="item.filteredValue"
                       :filters="item.filters" :fixed="item.fixed" :label="item.label" :prop="item.prop"
                       :show-overflow-tooltip="item.dataType === 'action' || item.dataType === 'slot' ? false : true"
                       :min-width="item.dataType == 'action' ? btnWidth : item.width"
                       :sortable="item.sortable ? true : false" :type="item.type"
                       :width="item.dataType == 'action' ? btnWidth : item.width" align="center">
        <!-- <div class="123" v-if="item.type == ''"> -->
        <template v-if="item.hasOwnProperty('colunmTemplate')" :slot="item.colunmTemplate" slot-scope="scope">
          <slot v-if="item.theadSlot" :index="index" :name="item.theadSlot" :row="scope.row"/>
        </template>

        <template slot-scope="scope">
          <!-- 插槽 -->
          <div v-if="item.dataType == 'slot'">
            <slot v-if="item.slot" :index="scope.$index" :name="item.slot" :row="scope.row"/>
          </div>
          <!-- 进度条 -->
          <div v-else-if="item.dataType == 'progress'">
            <el-progress :percentage="Number(scope.row[item.prop])"/>
          </div>
          <!-- 图片 -->
          <div v-else-if="item.dataType == 'image'">
            <img :src="javaApi + '/img/' + scope.row[item.prop]" alt=""
                 style="width: 40px; height: 40px; margin-top: 10px"/>
          </div>

          <!-- tag -->
          <div v-else-if="item.dataType == 'tag'">
            <el-tag v-if="
              typeof dataTypeFn(scope.row[item.prop], item.formatData) ==
              'string'
            " :title="scope.row[item.prop] | formatters(item.formatData)"
                    :type="formatType(scope.row[item.prop], item.formatType)">{{
                scope.row[item.prop] |
                  formatters(item.formatData)
              }}
            </el-tag>
            <el-tag v-for="(tag, index) in dataTypeFn(
              scope.row[item.prop],
              item.formatData
            )" v-else-if="
              typeof dataTypeFn(scope.row[item.prop], item.formatData) ==
              'object'
            " :key="index" :title="scope.row[item.prop] | formatters(item.formatData)"
                    :type="formatType(tag, item.formatType)">{{
                item.tagGroup
                  ? tag[item.tagGroup.label]
                    ? tag[item.tagGroup.label]
                    : tag
                  : tag
              }}
            </el-tag>
            <el-tag v-else :title="scope.row[item.prop] | formatters(item.formatData)"
                    :type="formatType(scope.row[item.prop], item.formatType)">{{
                scope.row[item.prop] |
                  formatters(item.formatData)
              }}
            </el-tag>
          </div>

          <!-- 按钮 -->
          <div v-else-if="item.dataType == 'action'"
               :style="`min-width:${getWidth(item.operation, scope.row)}`">
            <template v-for="(o, key) in item.operation">
              <el-button v-show="o.type != 'upload'" size="mini" v-if="o.showHide ? o.showHide(scope.row) : true"
                         :disabled="o.disabled ? o.disabled(scope.row) : false" :icon="iconFn(o)" :plain="o.plain"
                         :style="{ color: o.name === '删除' ? '#f56c6c' : o.color }" :type="o.type | typeFn(scope.row)"
                         @click="o.clickFun(scope.row)" :key="key">
                {{ o.name }}
              </el-button>
              <el-upload :action="javaApi + o.url + '?id=' + (o.uploadIdFun ? o.uploadIdFun(scope.row) : scope.row.id)"
                         :key="uploadKeys[scope.$index]"
                         ref="uploadRef"
                         size="mini" :multiple="o.multiple ? o.multiple : false" :limit="1"
                         :disabled="o.disabled ? o.disabled(scope.row) : false" :accept="o.accept
                  ? o.accept
                  : '.jpg,.jpeg,.png,.gif,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.zip,.rar'
                  " v-if="o.type == 'upload'" style="display: inline-block; width: 50px"
                         v-show="o.showHide ? o.showHide(scope.row) : true" :headers="uploadHeader"
                         :before-upload="(file) => beforeUpload(file, scope.$index)"
                         :on-change="(file, fileList) => handleChange(file, fileList, scope.$index)"
                         :on-error="(error, file, fileList) => onError(error, file, fileList, scope.$index)"
                         :on-success="(response, file, fileList) => handleSuccessUp(response, file, fileList, scope.$index)"
                         :on-exceed="onExceed" :show-file-list="false">
                <el-button :size="o.size ? o.size : 'small'" type="text"
                           :disabled="o.disabled ? o.disabled(scope.row) : false">{{ o.name }}
                </el-button>
              </el-upload>
            </template>
          </div>
          <!-- 可点击的文字 -->
          <div v-else-if="item.dataType == 'link'" class="cell link" style="width: 100%"
               @click="goLink(scope.row, item.linkMethod)">
            <span v-if="!item.formatData">{{ scope.row[item.prop] }}</span>
          </div>
          <!-- 默认纯展示数据 -->
          <div v-else class="cell" style="width: 100%">
            <span v-if="!item.formatData">{{ scope.row[item.prop] }}</span>
            <span v-else>{{
                scope.row[item.prop] | formatters(item.formatData)
              }}</span>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="page.total > 0" :total="page.total" :layout="page.layout" :page.sync="page.current"
                :limit.sync="page.size" @pagination="pagination"/>
  </div>
</template>

<script>
// 注：以下是所有配置项，其中最常使用的就是label、width、prop、dataType、slot
// label ：列名，就是表头上的标签叫什么些什么，类型 string
// width：该列宽度， string
// prop：  table绑定数据字段 string,这一列要展示哪个tableData里面的字段就写哪个字段
// dataType：  内置多个基本的element组件可供直接使用 string
// slot  当`dataType`为`slot`时必带参数，参数值为插槽的 `slot` 值 string，具体使用方法请看下面的第4点slot插槽的使用
// fixed：列是否固定在左侧或者右侧，true 表示固定在左侧 string, boolean
// sortable 对应列是否可以排序 boolean, string
// filters 数据过滤的选项，数组格式，数组中的元素需要有 text 和 value 属性。Array
// columnKey ：column 的 key，如果需要使用 filter-change 事件，则需要此属性标识是哪个 column 的筛选条件
// filteredValue  选中的数据过滤项，如果需要自定义表头过滤的渲染方式，可能会需要此属性。
// filterMultiple  数据过滤的选项是否多选
// minWidth 对应列的最小宽度，与 width 的区别是 width 是固定的，min-width 会把剩余宽度按比例分配给设置了 min-width 的列 string
// formatData  对数据进行数据处理，接受一个回调函数 (params?: {prop}) => {}
// formatType  当 `dataType`为 `tag`时，对标签颜色设置 (params?: {prop}) => { return 'danger'| 'success'... }`
// operation 当 `dataType` 为 `option`时，对按钮的 配置，具体配置项以下图为准object
// tagGroup 当 `dataType`为 `tag`时，绑定数据集字段显示名称 object
/**
 * 插槽的使用方法:
 */
/* <div
       slot="protocolSlot"
       slot-scope="scope"
      >
          <span>{{ scope.row.protocol }}</span>
      </div>
  */
/**
 * 使用formatData进行数据处理
 */
//  {
//           type: '',
//           label: '启用状态',
//           prop: 'is_active',
//           formatData: (item) => {
//             const str = item == true ? '已启用' : '未启用'
//             return str
//           }
//         },
/**
 * operation配置
 */
//  operation配置主要是来用于表格里面的操作那一列，通常就会有很多按钮，有以下参数：
// name：按钮名称，string
// type：按钮类型，`string` | `danger | success`，以elementUi 参数为准
// size：按钮大小，以elementUi 参数为准
// icon：按钮上的icon，以elementUi 参数为准
// plain：按elementUi 文档为准
// clickFun：按钮的回调函数
export default {
  name: "ZTTable",
  filters: {
    typeFn(val, row) {
      if (typeof val === "function") {
        return val(row);
      } else return val;
    },
    describeConts(val, describeCont) {
      if (typeof describeCont === "function") {
        return describeCont(val);
      } else return val;
    },
    formatters(val, format) {
      if (typeof format === "function") {
        return format(val);
      } else return val;
    },
  },
  props: {
    isSelection: {
      type: Boolean,
      default: false,
    },
    height: {
      type: String,
      default: null,
    },
    tableLoading: {
      type: Boolean,
      default: false,
    },
    handleSelectionChange: {
      type: Function,
      default: () => {
        return () => {
        };
      },
    },
    rowClick: {
      type: Function,
      default: () => {
        return () => {
        };
      },
    },
    currentChange: {
      type: Function,
      default: () => {
        return () => {
        };
      },
    },
    border: {
      type: Boolean,
      default: true,
    },
    highlightCurrentRow: {
      type: Boolean,
      default: false,
    },
    headerCellStyle: {
      type: Object,
      default: () => {
        return {};
      },
    },
    column: {
      type: Array,
      default() {
        return [];
      },
    },
    rowClassName: {
      type: Function,
      default: () => {
      },
    },
    rowStyle: {
      type: Object || Function,
      default: () => null,
    },
    tableData: {
      type: Array,
      default() {
        return [];
      },
    },
    rowKey: {
      type: String,
      default: undefined,
    },
    page: {
      type: Object,
      default() {
        return {
          total: 0,
          current: 0,
          size: 10,
          layout: "total, sizes, prev, pager, next, jumper",
        };
      },
    },
  },
  data() {
    return {
      spanList: [],
      btnWidth: "120px",
      uploadRefs: [],
      currentFiles: {}, // 用于存储每行的当前文件
      uploadKeys: {} // 用于动态重置组件
    };
  },
  mounted() {
    this.calculateSpanInfo();
    this.$nextTick(() => {
      this.$refs.multipleTable.doLayout();
      this.tableData.forEach((_, index) => {
        if (this.$refs.uploadRef) {
          this.$refs.uploadRef[index] = this.$refs.uploadRef[index] || {};
        }
      });
    })
  },
  watch: {
    tableData: {
      handler() {
        // 当表格数据变化时，初始化 uploadKeys
        this.tableData.forEach((_, index) => {
          this.$set(this.uploadKeys, index, Date.now());
        });
      },
      immediate: true
    }
  },
  methods: {
    getWidth(row, row0) {
      let count = 0;
      row.forEach((a) => {
        if (a.showHide !== undefined && a.showHide(row0)) {
          count += a.name.length;
        } else if (!a.showHide) {
          count += a.name.length;
        }
      });
      this.btnWidth = count * 15 + 60 + "px";
      return count * 15 + 60 + "px";
    },
    getTitleWidth(row) {
      if (row.label.includes('时间') || row.label.includes('编号') || row.label.includes('样品名称') || row.label.includes('零件')) {
        return 160
      } else if (row.label.includes('操作')) {
        return row.width
      } else {
        const span = document.createElement('span');
        span.innerText = row.label;
        document.body.appendChild(span);
        const width = `${span.offsetWidth + 50}px`;
        document.body.removeChild(span);

        return width;
      }
    },
    iconFn(row) {
      if (row.name === "编辑" || row.name === "修改") {
        return "el-icon-edit";
      } else if (row.name === "查看") {
        return "el-icon-view";
      } else {
        return row.icon;
      }
    },
    formatType(val, format) {
      if (typeof format === "function") {
        return format(val);
      } else return "";
    },
    dataTypeFn(val, format) {
      if (typeof format === "function") {
        return format(val);
      } else return val;
    },
    setCurrent(row) {
      this.$refs.multipleTable.setCurrentRow();
    },
    handleSuccessUp(response, file, fileList, index) {
      if (response.code == 200) {
        // 清除文件列表并更新当前文件
        if (this.uploadRefs[index]) {
          this.uploadRefs[index].clearFiles();
        }
        this.currentFiles[index] = file;
        this.$message.success("上传成功");
        // 重置组件状态
        this.resetUploadComponent(index);

      } else {
        this.$message.error(response.message);
      }
    },
    resetUploadComponent(index) {
      // 动态改变 key 来强制重置组件
      this.uploadKeys[index] = Date.now(); // 使用时间戳作为新的 key
    },
    handleChange(file, fileList, index) {
      // 如果文件数量超过限制，移除最早的文件
      if (fileList.length > 1) {
        // 移除最早的文件
        const earliestFile = fileList[0];
        this.uploadRefs[index].handleRemove(earliestFile);
      }

      // 更新当前文件
      this.currentFiles[index] = file;
    },
    beforeUpload(file, index) {
      this.currentFiles[index] = {}
      if (file.size > 1024 * 1024 * 10) {
        this.$message.error('上传文件不超过10M');
        // this.$refs.upload.clearFiles()
        return false;
      } else {
        return true;
      }
    },
    onError(error, file, fileList, index) {
      this.$message.error('文件上传失败，请重试');

      // 清除文件列表
      if (this.uploadRefs[index]) {
        this.uploadRefs[index].clearFiles();
      }
    },
    onExceed() {
      this.$message.warning("超出文件个数");
    },
    pagination({page, limit}) {
      this.$emit("pagination", {page: page, limit: limit});
    },
    indexMethod(index) {
      return (this.page.current - 1) * this.page.size + index + 1;
    },
    // 点击单元格link事件
    goLink(row, linkMethod) {
      if (!linkMethod) {
        return this.$message.warning("请配置lingk事件");
      }
      this.$parent[linkMethod](row);
    },
    // 合并单元格
    calculateSpanInfo() {
      // 初始化每列的合并信息
      this.spanList = [];
      this.column.forEach((m, i) => {
        if (m.mergeCol) {
          this.spanList.push({
            arr: [],
            position: 0,
            name: m.prop,
            index: i + 1,
          });
        }
      });
      this.spanList.forEach((item, i) => {
        this.rowspan(
          this.spanList[i].arr,
          this.spanList[i].position,
          item.name
        );
      });
    },
    rowspan(spanArr, position, spanName) {
      this.tableData.forEach((item, index) => {
        if (index === 0) {
          spanArr.push(1);
          position = 0;
        } else {
          if (
            this.tableData[index][spanName] ===
            this.tableData[index - 1][spanName]
          ) {
            spanArr[position] += 1;
            spanArr.push(0);
          } else {
            spanArr.push(1);
            position = index;
          }
        }
      });
    },
    // 合并单元格
    spanMethod({row, column, rowIndex, columnIndex}) {
      // 一般的合并行
      if (this.column.find((m) => m.mergeCol)) {
        let i = null;
        let obj = this.spanList.find((item, index) => {
          i = index;
          return item.index == columnIndex;
        });
        if (obj) {
          const _row = this.spanList[i].arr[rowIndex];
          const _col = _row > 0 ? 1 : 0;
          return {
            rowspan: _row,
            colspan: _col,
          };
        }
      }
    },
  },
};
</script>

<style scoped>
.el-table >>> .el-table__empty-text {
  text-align: center;
}

.link {
  color: rgb(64, 158, 255);
  cursor: pointer;
}

.el-table ::v-deep .cell {
  padding: 0 !important;
}

.cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 4px !important;
  padding-left: 10px !important;
}

.lims-table .highlight-warning-row-border td:first-child {
  border-left: 4px solid #ffcd29;
}

.lims-table .highlight-warning-row-border td:last-child {
  border-right: 4px solid #ffcd29;
}

.lims-table .highlight-danger-row-border td:first-child {
  border-left: 4px solid #f56c6c;
}

.lims-table .highlight-danger-row-border td:last-child {
  border-right: 4px solid #f56c6c;
}
</style>
