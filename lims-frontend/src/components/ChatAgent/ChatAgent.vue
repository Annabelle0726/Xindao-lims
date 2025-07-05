<template>
  <div class="chat-agent-container">
    <button class="chat-toggle-btn" @click="toggleChat">
      <i class="el-icon-chat-dot-round"></i>
      <span class="unread-badge" v-if="unreadCount > 0">{{ unreadCount }}</span>
    </button>

    <div class="chat-window" :class="{ open: isOpen }">
      <ChatHeader @minimize="minimizeChat" @close="closeChat" />
      <ChatHistory
        :messages="messages"
        :isProcessing="isProcessing"
        @action="handleContextAction"
      />

      <div class="prompt-popup-container" v-if="showInitialPrompts">
        <div class="prompt-popup">
          <div class="prompt-categories">
            <div
              v-for="category in promptCategories"
              :key="category.name"
              class="prompt-category"
            >
              <h4>{{ category.name }}</h4>
              <div class="prompt-buttons">
                <button
                  v-for="(prompt, index) in category.prompts"
                  :key="index"
                  class="prompt-btn"
                  @click="selectInitialPrompt(prompt)"
                >
                  {{ prompt.text }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <RatingWidget
        v-if="showRating"
        @submit="handleRatingSubmit"
      />

      <PromptSuggestions
        v-if="showDynamicPrompts && !showRating && !showInitialPrompts"
        :category="promptCategory"
        :context="currentContext"
        @select="handlePromptSelect"
      />

      <ChatInput
        v-if="!showRating"
        @send="handleSendMessage"
        @file-uploaded="handleFileUploaded"
      />
    </div>
  </div>
</template>

<script>
import ChatHeader from './ChatHeader.vue';
import ChatHistory from './ChatHistory.vue';
import ChatInput from './ChatInput.vue';
import PromptSuggestions from './PromptSuggestions.vue';
import RatingWidget from './RatingWidget.vue';
import { getToken } from '@/utils/auth';

export default {
  name: 'ChatAgent',
  components: {
    ChatHeader,
    ChatHistory,
    ChatInput,
    PromptSuggestions,
    RatingWidget
  },
  data() {
    return {
      isOpen: false,
      unreadCount: 0,
      showInitialPrompts: true,
      showDynamicPrompts: false,
      showRating: false,
      promptCategory: 'general',
      currentContext: {},
      messages: [
        {
          id: this.generateMessageId(),
          sender: 'bot',
          text: '您好！我是 LIMS Agent，您的实验室智能助手。',
          timestamp: new Date().getTime(),
          type: 'text',
          context: {}
        }
      ],
      sessionId: this.generateSessionId(),
      isProcessing: false,
      rating: 0,
      error: null,
      promptCategories: [
        {
          name: '检验管理',
          prompts: [
            { text: '如何提交新的检验申请？', category: '检验申请' },
            { text: '查看我的待处理任务', category: '任务管理' },
            { text: '生成上月质量报告', category: '报告编制' }
          ]
        },
        {
          name: '数据分析',
          prompts: [
            { text: '分析最近的检测数据', category: '数据分析' },
            { text: '比较历史检测结果', category: '数据对比' },
            { text: '生成数据可视化图表', category: '数据展示' }
          ]
        },
        {
          name: '系统帮助',
          prompts: [
            { text: '如何使用LIMS系统？', category: '系统使用' },
            { text: '查看实验室资质', category: '实验室信息' },
            { text: '联系技术支持', category: '技术支持' }
          ]
        }
      ]
    };
  },
  methods: {
    generateSessionId() {
      return `session-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
    },
    generateMessageId() {
      return `msg-${Date.now()}-${Math.random().toString(36).substr(2, 6)}`;
    },
    toggleChat() {
      this.isOpen = !this.isOpen;
      if (this.isOpen) this.unreadCount = 0;
    },
    minimizeChat() {
      this.isOpen = false;
    },
    closeChat() {
      this.isOpen = false;
    },
    handleSendMessage(message) {
      if (!message.trim() || this.isProcessing) return;

      const userMessage = {
        id: this.generateMessageId(),
        sender: 'user',
        text: message,
        timestamp: new Date().getTime(),
        type: 'text'
      };

      this.addMessage(userMessage);
      this.processUserInput(message);
    },
    handleFileUploaded(file) {
      const userMessage = {
        id: this.generateMessageId(),
        sender: 'user',
        text: `已上传文件: ${file.name}`,
        timestamp: new Date().getTime(),
        type: 'file',
        fileData: file
      };

      this.addMessage(userMessage);
      this.processFileUpload(file);
    },
    async callGptApi(message) {
      try {
        const response = await this.$http.post('/chat/api/generate', {
          sessionId: this.sessionId,
          message: message,
          context: JSON.stringify(this.currentContext)
        });

        return {
          responseText: response.data,
          contextUpdate: {}
        };
      } catch (error) {
        console.error('API调用失败:', error);
        return {
          responseText: '请求处理失败，请稍后重试',
          contextUpdate: {}
        };
      }
    },
    async processFileUpload(file) {
      this.isProcessing = true;
      this.showDynamicPrompts = false;

      try {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('sessionId', this.sessionId);

        // 显示上传中状态
        this.addBotMessage(`正在上传文件: ${file.name}...`);

        const response = await this.$http.post('/chat/api/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': 'Bearer ' + getToken()
          }
        });

        if (response.data.code === 200) {
          const result = response.data.data; // 现在是FileUploadResult对象
          this.addBotMessage(`文件上传成功: ${result.fileName}`);

          // 显示文件摘要
          if (result.contentSummary) {
            this.addBotMessage(`文件摘要: ${result.contentSummary}`);
          }

          // 自动分析文件
          const analysisResult = await this.analyzeFile(result.fileId);
          this.addBotMessage(analysisResult.message, analysisResult.context);
        } else {
          this.addBotMessage(`文件上传失败: ${response.data.msg}`);
        }
      } catch (error) {
        this.addBotMessage(`文件上传失败: ${error.message}`);
      } finally {
        this.isProcessing = false;
        this.showDynamicPrompts = true;
      }
    },
    handlePromptSelect(prompt) {
      if (this.isProcessing) return;

      const userMessage = {
        id: this.generateMessageId(),
        sender: 'user',
        text: prompt.text,
        timestamp: new Date().getTime(),
        type: 'text',
        promptData: prompt
      };

      this.addMessage(userMessage);
      this.processPrompt(prompt);
    },
    selectInitialPrompt(prompt) {
      this.showInitialPrompts = false;
      this.showDynamicPrompts = true;

      const userMessage = {
        id: this.generateMessageId(),
        sender: 'user',
        text: prompt.text,
        timestamp: new Date().getTime(),
        type: 'text',
        promptData: prompt
      };

      this.addMessage(userMessage);
      this.processPrompt(prompt);
    },
    addMessage(message) {
      this.messages.push(message);
      if (!this.isOpen && message.sender === 'bot') {
        this.unreadCount++;
      }
      this.$nextTick(() => {
        const container = this.$el.querySelector('.chat-history');
        if (container) container.scrollTop = container.scrollHeight;
      });
    },
    addBotMessage(text, context = {}) {
      const botMessage = {
        id: this.generateMessageId(),
        sender: 'bot',
        text: text,
        timestamp: new Date().getTime(),
        type: 'text',
        context: context
      };
      this.addMessage(botMessage);
      return botMessage;
    },
    async processUserInput(message) {
      this.isProcessing = true;
      this.showInitialPrompts = false;
      this.showDynamicPrompts = false;

      try {
        const gptResponse = await this.callGptApi(message);
        const { responseText, contextUpdate, showRating } = gptResponse;
        this.currentContext = { ...this.currentContext, ...contextUpdate };
        this.addBotMessage(responseText, this.currentContext);

        if (showRating) {
          this.showRating = true;
        } else {
          this.promptCategory = this.determinePromptCategory();
          this.showDynamicPrompts = true;
        }
      } catch (error) {
        this.error = `处理请求时出错: ${error.message}`;
        this.addBotMessage(this.error);
      } finally {
        this.isProcessing = false;
      }
    },
    async processPrompt(prompt) {
      this.isProcessing = true;
      this.showDynamicPrompts = false;

      try {
        const response = await this.handlePrompt(prompt);
        this.addBotMessage(response.message, response.context);
        this.showDynamicPrompts = true;
      } catch (error) {
        this.error = `处理提示词时出错: ${error.message}`;
        this.addBotMessage(this.error);
      } finally {
        this.isProcessing = false;
      }
    },
    async handlePrompt(prompt) {
      switch (prompt.category) {
        case '报告编制':
          return this.generateReport(prompt.context);
        case '费用统计':
          return this.handleExpenseIntent({
            category: 'expenses',
            context: { period: 'current_month' }
          });
        case '文件分析':
          return this.analyzeFile(this.currentContext.fileId);
        default:
          return {
            message: `已处理您的请求: "${prompt.text}"`,
            context: {}
          };
      }
    },

    async analyzeFile(fileId) {
      try {
        const analysisDTO = {
          fileId: fileId,
          sessionId: this.sessionId,
          prompt: "请总结文件主要内容并提取关键数据"
        };

        const response = await this.$http.post('/chat/api/analyze', analysisDTO);

        if (response.data.code === 200) {
          const result = response.data.data; // FileUploadResult对象
          return {
            message: result.contentSummary || "分析完成",
            context: {fileId: fileId}
          };
        } else {
          return {
            message: `文件分析失败: ${response.data.msg}`,
            context: {}
          };
        }
      } catch (error) {
        return {
          message: `文件分析失败: ${error.message}`,
          context: {}
        };
      }
    },

    determinePromptCategory() {
      if (this.currentContext.fileId) return 'files';
      if (this.currentContext.taskId) return 'tasks';
      if (this.currentContext.reportId) return 'reports';
      return 'general';
    },
    handleContextAction(action) {
      if (action.action === 'restart') {
        this.restartConversation();
      }
    },
    handleRatingSubmit(rating) {
      this.rating = rating;
      this.sendRatingToServer(rating);
      this.addBotMessage(`感谢您的评分！已开始新会话。`);
      setTimeout(() => this.restartConversation(), 1500);
    },
    async sendRatingToServer(rating) {
      try {
        await this.$http.post('/api/chat/rating', {
          sessionId: this.sessionId,
          rating,
          messages: this.messages
        });
      } catch (error) {
        console.error('评分提交失败:', error);
      }
    },
    restartConversation() {
      this.sessionId = this.generateSessionId();
      this.currentContext = {};
      this.showRating = false;
      this.showInitialPrompts = true;
      this.showDynamicPrompts = false;
      this.promptCategory = 'general';
      this.messages = [
        {
          id: this.generateMessageId(),
          sender: 'bot',
          text: '您好！我是 LIMS Agent，您的实验室智能助手。',
          timestamp: new Date().getTime(),
          type: 'text',
          context: {}
        }
      ];
    },
    async generateReport(context) {
      try {
        const response = await this.$http.post('/api/reports/generate', {
          taskId: context.taskId,
          userId: this.$store.state.user.userId
        });
        return {
          message: `报告已生成：${response.data.reportId}\n\n${response.data.previewUrl ? `预览链接：${response.data.previewUrl}` : '请下载完整报告'}`,
          context: {
            reportId: response.data.reportId,
            downloadUrl: response.data.downloadUrl
          }
        };
      } catch (error) {
        throw new Error(`报告生成失败：${error.response?.data?.message || error.message}`);
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.chat-agent-container {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 1000;

  .chat-toggle-btn {
    position: relative;
    width: 60px;
    height: 60px;
    background: #409EFF;
    border: none;
    border-radius: 50%;
    color: #fff;
    font-size: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
    transition: 0.3s ease;
    cursor: pointer;

    &:hover {
      background: #66b1ff;
      transform: scale(1.05);
    }

    .unread-badge {
      position: absolute;
      top: -6px;
      right: -6px;
      width: 20px;
      height: 20px;
      background: #f56c6c;
      color: white;
      border-radius: 50%;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .chat-window {
    width: 400px;
    height: 600px;
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0,0,0,0.2);
    overflow: hidden;
    display: flex;
    flex-direction: column;
    position: absolute;
    bottom: 70px;
    right: 0;
    transition: all 0.3s ease;
    opacity: 0;
    transform: translateY(20px);
    visibility: hidden;

    &.open {
      opacity: 1;
      transform: translateY(0);
      visibility: visible;
    }
  }

  .prompt-popup-container {
    padding: 0 16px 16px;

    .prompt-popup {
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      padding: 20px;
      margin-top: 10px;

      h3 {
        margin-top: 0;
        color: #333;
        font-size: 18px;
      }

      p {
        color: #666;
        margin-bottom: 20px;
        font-size: 14px;
      }

      .prompt-categories {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .prompt-category {
          h4 {
            margin: 0 0 8px 0;
            font-size: 14px;
            color: #409EFF;
            font-weight: 600;
          }

          .prompt-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
          }
        }
      }

      .prompt-btn {
        background: #f5f7fa;
        border: 1px solid #dcdfe6;
        border-radius: 16px;
        padding: 8px 16px;
        font-size: 13px;
        color: #606266;
        cursor: pointer;
        transition: all 0.2s;
        text-align: left;

        &:hover {
          background: #ecf5ff;
          border-color: #c6e2ff;
          color: #409EFF;
        }
      }
    }
  }

  .error-message {
    background: #fef0f0;
    color: #f56c6c;
    padding: 10px;
    border-radius: 4px;
    margin: 10px;
    text-align: center;

    button {
      background: #f56c6c;
      color: white;
      border: none;
      border-radius: 4px;
      padding: 5px 10px;
      margin-top: 5px;
      cursor: pointer;
    }
  }
}
</style>
