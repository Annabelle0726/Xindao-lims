<template>
  <div class="chat-history">
    <div
      v-for="message in messages"
      :key="message.id"
      :class="['message', message.sender]"
    >
      <div v-if="message.sender === 'bot'" class="bot-message">
        <div class="avatar">
          <i class="fas fa-robot"></i>
        </div>
        <div class="content">
          <div class="text" v-html="formatMessage(message.text)"></div>

          <!-- 显示上下文操作 -->
          <div v-if="message.context && message.context.actions" class="context-actions">
            <button
              v-for="(action, index) in message.context.actions"
              :key="index"
              class="action-btn"
              @click="handleAction(action)"
            >
              {{ action.label }}
            </button>
          </div>

          <div class="timestamp">{{ formatTime(message.timestamp) }}</div>
        </div>
      </div>

      <div v-else class="user-message">
        <div class="content">
          <div class="text">
            <span v-if="message.type === 'file'" class="file-message">
              <i class="el-icon-document"></i> {{ message.text }}
            </span>
            <span v-else>{{ message.text }}</span>
          </div>
          <div class="timestamp">{{ formatTime(message.timestamp) }}</div>
        </div>
        <div class="avatar">
          <i class="el-icon-user-solid"></i>
        </div>
      </div>
    </div>

    <div v-if="isProcessing" class="loading-indicator">
      <div class="dot-flashing"></div>
    </div>
  </div>
</template>

<script>

export default {

  name: 'ChatHistory',
  props: {
    messages: {
      type: Array,
      required: true
    },
    isProcessing: Boolean
  },
  methods: {
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    },
    formatMessage(text) {
      // 将换行符转换为HTML换行
      return text.replace(/\n/g, '<br>');
    },
    handleAction(action) {
      // 新增重启对话动作
      if (action.action === 'restart') {
        this.$emit('action', action);
      } else {
        // 原有处理逻辑
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-history {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;

  .message {
    max-width: 85%;

    &.bot {
      align-self: flex-start;
    }

    &.user {
      align-self: flex-end;
    }
  }

  .bot-message,
  .user-message {
    display: flex;
    gap: 12px;

    .avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      background: #ecf5ff;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;

      i {
        font-size: 16px;
        color: #409EFF;
      }
    }

    .content {
      max-width: calc(100% - 48px);
      display: flex;
      flex-direction: column;

      .text {
        padding: 12px 16px;
        border-radius: 18px;
        font-size: 14px;
        line-height: 1.5;

        .file-message {
          color: #409EFF;
        }
      }

      .timestamp {
        font-size: 11px;
        color: #999;
        margin-top: 4px;
      }
    }
  }

  .bot-message {
    .content {
      .text {
        background: #f0f2f5;
        border-bottom-left-radius: 4px;
      }

      .context-actions {
        margin-top: 8px;
        display: flex;
        gap: 8px;
        flex-wrap: wrap;

        .action-btn {
          background: #fff;
          border: 1px solid #409EFF;
          color: #409EFF;
          border-radius: 14px;
          padding: 4px 12px;
          font-size: 12px;
          cursor: pointer;
          transition: all 0.2s;

          &:hover {
            background: #409EFF;
            color: #fff;
          }
        }
      }
    }
  }

  .message.bot {
    margin-bottom: 0; /* 移除底部间距 */
  }

  .user-message {
    flex-direction: row-reverse;

    .content {
      align-items: flex-end;

      .text {
        background: #409EFF;
        color: #fff;
        border-bottom-right-radius: 4px;
      }
    }

    .avatar {
      background: #409EFF;

      i {
        color: #fff;
      }
    }
  }

  .loading-indicator {
    display: flex;
    justify-content: center;
    padding: 10px;

    .dot-flashing {
      position: relative;
      width: 10px;
      height: 10px;
      border-radius: 5px;
      background-color: #409EFF;
      color: #409EFF;
      animation: dot-flashing 1s infinite linear alternate;
      animation-delay: 0.5s;

      &::before, &::after {
        content: "";
        display: inline-block;
        position: absolute;
        top: 0;
        width: 10px;
        height: 10px;
        border-radius: 5px;
        background-color: #409EFF;
        color: #409EFF;
      }

      &::before {
        left: -15px;
        animation: dot-flashing 1s infinite alternate;
        animation-delay: 0s;
      }

      &::after {
        left: 15px;
        animation: dot-flashing 1s infinite alternate;
        animation-delay: 1s;
      }
    }
  }
}

@keyframes dot-flashing {
  0% {
    background-color: #409EFF;
  }
  50%, 100% {
    background-color: rgba(64, 158, 255, 0.2);
  }
}
</style>
