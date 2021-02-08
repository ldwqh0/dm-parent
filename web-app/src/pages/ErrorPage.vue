<template>
  <div class="error-wrapper">
    <div>
      <h1>错误码：{{ code }}</h1>
      <p>{{ displayMessage }}</p>
      <router-link v-if="code===403 || code==='403'" :to="{name:'login',query:{redirect:loginRedirect}}">
        点此重新登录
      </router-link>
      <router-link v-else to="/">点此返回首页</router-link>
    </div>
  </div>
</template>

<script lang="ts">

  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  import { namespace } from 'vuex-class'

  const messages: { [key: string]: string } = {
    401: '该用户未登录',
    403: '权限不足',
    404: '指定的页面不存在',
    500: '未知服务器异常'
  }

  const securityModule = namespace('security')
  @Component
  export default class ErrorPage extends Vue {
    @Prop({
      required: true,
      type: [String]
    })
    code!: string

    @Prop({
      required: false,
      default: () => '',
      type: [String]
    })
    message!: string

    /**
     * 错误的来源页面
     */
    @Prop({
      required: false,
      type: [String]
    })
    from?: string

    @securityModule.Action('logout')
    logout!: () => Promise<unknown>

    created (): void {
      // 如果错误码是403，退出一下
      if (Number.parseInt(this.code) === 403) {
        this.logout()
      }
    }

    get loginRedirect (): string | undefined {
      const base: string | undefined = this.$router.options.base
      if (this.from && base) {
        return `${base}${this.from}`.replaceAll('//', '/')
      } else if (this.from) {
        return this.from
      } else {
        return undefined
      }
    }

    get displayMessage (): string {
      if (this.message) {
        return this.message
      } else {
        return messages[this.code]
      }
    }
  }
</script>
<style scoped lang="less">
  .error-wrapper {
    display: flex;
    align-items: center;
    height: 100%;
    justify-content: center;
  }
</style>
