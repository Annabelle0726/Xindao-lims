<template>
  <div></div>
</template>
<script>
export default {
  data() {
    return {
    };
  },
  created() {
    this.initLogin();
  },
  methods: {
    initLogin() {
      this.$store.dispatch('LoginBySSO', {code: this.$route.query.code}).then(() => {
        this.$router.push({path: '/'});
      });
    }
  }
};
</script>
<style scoped></style>