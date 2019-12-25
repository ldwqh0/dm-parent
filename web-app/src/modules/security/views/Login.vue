<template>
  <div><a href="javascript:void(0)" @click="oauthLogin">使用oauth授权登录</a></div>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  const SAVE_LOCATION_KEY = 'SAVE_LOCATION_KEY'

  @Component
  export default class Login extends Vue {
    @Prop()
    redirect

    oauthLogin () {
      // 点击登录时，如果没有跳转地址，保存跳转地址为应用地址
      let savedLocation = sessionStorage.getItem(SAVE_LOCATION_KEY)
      if (!savedLocation) {
        sessionStorage.setItem(SAVE_LOCATION_KEY, CONTEXT_PATH)
      }
      window.location.href = '/gw/oauth2/authorization/oauth2'
    }

    created () {
      // 如果包含跳转信息，直接跳转
      if (this.redirect) {
        sessionStorage.setItem(SAVE_LOCATION_KEY, this.redirect)
        // 如果要设置为自动登录的话，直接在这里跳转
        // window.location.href= 跳转地址
      } else {
        // 当时从别的页面跳转到此处
        // 进入检测是否有保存的跳转，如果有，先直接跳转
        let savedLocation = sessionStorage.getItem(SAVE_LOCATION_KEY)
        if (savedLocation) {
          sessionStorage.removeItem(SAVE_LOCATION_KEY)
          window.location.href = savedLocation
        }
      }
    }
  }
</script>

<style scoped>

</style>
