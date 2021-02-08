<template>
  <div>
    <div v-if="code">授权认证中...</div>
    <div v-if="error">
      <p> 发生认证错误：{{ error }}</p>
      <p>
        <router-link :to="{name:'login'}">重新登录</router-link>
      </p>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import http from '@/http'
  import URLS from '../urls'

  @Component
  export default class OAuth2Login extends Vue {
    @Prop({
      type: [String],
      default: () => ''
    })
    redirect!: string

    @Prop({
      type: [String],
      default: () => ''
    })
    state!: string

    @Prop({
      type: [String],
      default: () => ''
    })
    code!: string

    @Prop({
      type: [String],
      required: false
    })
    registrationId?: string

    @Prop({
      type: [String],
      required: false
    })
    error?: string

    created (): void {
      if (this.code) {
        http.get(URLS.oauth2Login, {
          params: {
            code: this.code,
            state: this.state
          }
        }).then(() => {
          const saveRequest = sessionStorage.getItem('SAVED_REQUEST')
          if (saveRequest) {
            window.location.href = saveRequest
          } else {
            this.$router.replace('/')
          }
          sessionStorage.removeItem('SAVED_REQUEST')
        }).catch(error => (this.error = error))
      }
    }
  }
</script>l

<style scoped>

</style>
