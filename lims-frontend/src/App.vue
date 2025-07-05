<template>
  <div id="app">

    <router-view v-show="!isLoading" />
    <theme-picker v-show="!isLoading" />
    <ChatAgent v-if="isAuthenticated && !isLoading" />

  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";
import ChatAgent from "./components/ChatAgent/ChatAgent.vue";

export default {
  name: "App",
  components: {ChatAgent, ThemePicker },
  computed: {
    isAuthenticated() {
      return this.$store.getters.isAuthenticated;
    }
  },
  data() {
    return {
      isLoading: true
    }
  },
  mounted() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}


</style>
