<template>
  <div class="chat-header">
    <div class="chat-title">
      <span>LIMS Agent</span>
      <span class="bot-badge">AI 助手</span>
    </div>
    <div>
      <el-button type="text" @click="minimize" icon="el-icon-minus" style="color: white;"></el-button>
      <el-button type="text" @click="close" icon="el-icon-close" style="color: white;"></el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatHeader',
  emits: ['minimize', 'close'],
  methods: {
    minimize() {
      this.$emit('minimize');
    },
    close() {
      this.$emit('close');
    }
  }
}
</script>
