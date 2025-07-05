<template>
  <div class="prompt-suggestions">
    <div class="main-prompts">
      <h4>建议操作：</h4>
      <div class="prompt-list">
        <button
          v-for="(prompt, index) in dynamicPrompts"
          :key="index"
          class="prompt-btn"
          @click="selectPrompt(prompt)"
        >
          <i :class="getPromptIcon(prompt.category)"></i>
          {{ prompt.text }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PromptSuggestions',
  props: {
    category: {
      type: String,
      default: 'general'
    },
    context: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['select'],
  data() {
    return {
      promptLibrary: {
        general: [
          { text: "如何提交新的检验申请？", category: "检验申请" },
          { text: "查看我的待处理任务", category: "任务管理" },
          { text: "生成上月质量报告", category: "报告编制" }
        ],
        tasks: [
          { text: "显示高优先级任务", category: "任务筛选" },
          { text: "提交检验结果", category: "任务处理" },
          { text: "申请任务延期", category: "任务管理" }
        ],
        files: [
          { text: "分析上传的检测数据", category: "数据分析" },
          { text: "生成数据可视化图表", category: "数据展示" },
          { text: "导出分析报告", category: "报告编制" }
        ],
        reports: [
          { text: "定制报告模板", category: "报告管理" },
          { text: "发送报告给质量部门", category: "报告分发" },
          { text: "归档当前报告", category: "报告管理" }
        ]
      },
      categoryIcons: {
        '检验申请': 'el-icon-truck',
        '任务管理': 'el-icon-tickets',
        '报告编制': 'el-icon-document',
        '数据分析': 'el-icon-data-analysis',
        '任务筛选': 'el-icon-filter',
        '任务处理': 'el-icon-check',
        '数据展示': 'el-icon-pie-chart',
        '报告管理': 'el-icon-folder',
        '报告分发': 'el-icon-s-promotion',
        '系统使用': 'el-icon-question',
        '实验室信息': 'el-icon-office-building',
        '技术支持': 'el-icon-service'
      }
    };
  },
  computed: {
    dynamicPrompts() {
      if (!this.promptLibrary) return [];

      const basePrompts = this.promptLibrary[this.category] || this.promptLibrary.general;

      if (this.context?.taskId) {
        return [
          ...basePrompts,
          { text: `更新任务 ${this.context.taskId} 状态`, category: "任务更新" },
          { text: `为任务 ${this.context.taskId} 添加备注`, category: "任务管理" }
        ];
      }

      if (this.context?.fileId) {
        return [
          ...basePrompts,
          { text: "比较历史数据", category: "数据分析" },
          { text: "验证数据准确性", category: "数据验证" }
        ];
      }

      return basePrompts;
    }
  },
  methods: {
    selectPrompt(prompt) {
      this.$emit('select', prompt);
    },
    getPromptIcon(category) {
      return this.categoryIcons[category] || 'el-icon-chat-dot-round';
    }
  }
};
</script>

<style lang="scss" scoped>
.prompt-suggestions {
  padding: 12px 16px;
  background-color: #f9f9f9;
  border-top: 1px solid #eee;

  .main-prompts {
    h4 {
      margin-bottom: 8px;
      font-size: 14px;
      color: #666;
    }

    .prompt-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
  }

  .prompt-btn {
    background-color: #fff;
    border: 1px solid #dcdfe6;
    border-radius: 16px;
    padding: 8px 16px;
    font-size: 13px;
    color: #606266;
    cursor: pointer;
    transition: all 0.2s;
    display: flex;
    align-items: center;

    i {
      margin-right: 6px;
      font-size: 14px;
      color: #409EFF;
    }

    &:hover {
      background-color: #ecf5ff;
      border-color: #c6e2ff;
      color: #409EFF;
    }
  }
}
</style>
