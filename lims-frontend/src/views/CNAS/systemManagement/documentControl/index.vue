<template>
  <div class="capacity-scope">
    <el-tabs type="border-card" v-model="activeName" style="height: 100%;">
      <el-tab-pane :label="item.name" :name="item.component" v-for="(item, index) in tabList" :key="index"
        style="height: 100%;">
        <component :is="item.component" :key="item.component" v-if="activeName === item.component"></component>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import FileList from './components/FileList.vue'
import ControlledFileApplication from './components/ControlledFileApplication.vue'
import DistributionCollectionRecord from './components/DistributionCollectionRecord.vue'
import FileChangeRequest from './components/FileChangeRequest.vue'
import FileObsoletionRequest from './components/FileObsoletionRequest.vue'
export default {
  name: 'DocumentControl',
  components: {
    FileList,
    ControlledFileApplication,
    DistributionCollectionRecord,
    FileChangeRequest,
    FileObsoletionRequest
  },
  data() {
    return {
      tabList: [
        {
          name: '文件清单',
          component: 'FileList'
        },
        {
          name: '文件受控申请',
          component: 'ControlledFileApplication'
        },
        {
          name: '发放回收记录',
          component: 'DistributionCollectionRecord'
        },
        {
          name: '文件变更申请',
          component: 'FileChangeRequest'
        },
        {
          name: '文件作废申请',
          component: 'FileObsoletionRequest'
        },
      ],
      activeName: 'FileList'
    };
  },
}
</script>

<style scoped>
</style>
