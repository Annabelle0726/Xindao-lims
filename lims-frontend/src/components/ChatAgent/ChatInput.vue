<template>
  <div class="chat-input-area">
    <div class="input-container">
      <!-- 上传按钮 -->
      <el-upload
        class="file-upload"
        action="/api/chat/upload"
        :headers="uploadHeaders"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-success="handleFileSuccess"
        :on-error="handleFileError"
      >
        <i class="el-icon-upload upload-icon"></i>
      </el-upload>

      <!-- 输入框 -->
      <textarea
        ref="messageInput"
        class="message-input"
        v-model="message"
        placeholder="输入消息..."
        @keydown.enter.exact.prevent="sendMessage"
        @input="resizeTextarea"
      ></textarea>

      <!-- 发送按钮 -->
      <button
        class="send-btn"
        :disabled="!message.trim()"
        @click="sendMessage"
      >
        <i class="el-icon-s-promotion"></i>
      </button>
    </div>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth';

export default {
  name: 'ChatInput',
  data() {
    return {
      message: '',
      isInitialized: false,
      uploadHeaders: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  mounted() {
    this.initializeChat().then(() => {
      this.isInitialized = true;
    });
  },
  methods: {
    initializeChat() {
      return new Promise(resolve => {
        setTimeout(resolve, 500);
      });
    },
    sendMessage() {
      if (this.message.trim()) {
        this.$emit('send', this.message);
        this.message = '';
        this.$nextTick(() => {
          this.resizeTextarea();
        });
      }
    },
    resizeTextarea() {
      const textarea = this.$refs.messageInput;
      textarea.style.height = 'auto';
      textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px';
    },
    focusInput() {
      this.$refs.messageInput.focus();
    },
    beforeUpload(file) {
      if (file.size / 1024 / 1024 > 10) {
        this.$message.error('上传文件不能超过10MB');
        return false;
      }
      return true;
    },
    handleFileSuccess(response, file) {
      if (response.code === 200) {
        this.$emit('file-uploaded', {
          name: file.name,
          type: 'file'
        });
        this.$message.success('文件上传成功');
      } else {
        this.$message.error(response.msg || '上传失败');
      }
    },
    handleFileError(err) {
      console.error('上传失败:', err);
      this.$message.error('文件上传失败');
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-input-area {
  padding: 12px 16px;
  border-top: 1px solid #ebeef5;
  background: #f5f7fa;
}

.input-container {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #fff;
  //border: 1px solid #dcdfe6;
  border-radius: 24px;
  padding: 8px 14px;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.03);
  transition: border-color 0.2s;

  &:focus-within {
    border-color: #409EFF;
  }
}

.upload-icon {
  font-size: 18px;
  color: #909399;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: #409EFF;
    transform: scale(1.1);
  }
}

.message-input {
  flex: 1;
  min-height: 36px;
  max-height: 120px;
  resize: none;
  font-size: 14px;
  line-height: 1.5;
  font-family: inherit;
  background: transparent;
  overflow: hidden;
  box-shadow: none;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  height: 44px;
  outline: none;
  transition: border-color 0.3s;

  &:focus {
    border-color: var(--primary-color);
  }

}

.send-btn {
  background: transparent;
  border: none;
  font-size: 18px;
  color: #c0c4cc;
  cursor: pointer;
  transition: all 0.2s;

  &:not(:disabled) {
    color: #409EFF;

    &:hover {
      transform: scale(1.1);
    }
  }

  &:disabled {
    cursor: not-allowed;
    opacity: 0.6;
  }
}

</style>
