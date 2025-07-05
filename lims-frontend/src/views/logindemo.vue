<template>
  <div></div>
</template>
<script>
import { getSsoAuthUrl } from '@/api/login'
export default {
  data() {
    return {}
  },
  created() {
    this.goLogin()
  },
  computed: {},
  methods: {
    async goLogin() {
      await getSsoAuthUrl().then(res => {
        window.location.href = res.data
      })
    }
  }
}
</script>
<style scoped></style>
