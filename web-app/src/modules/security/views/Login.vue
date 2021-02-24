<template>
  <div class="login-page">
    <el-form v-if="false"
             :model="user"
             label-width="100px"
             style="width: 400px"
             class="login-form">
      <el-form-item label="用户名">
        <el-input v-model="user.username" type="text" />
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="user.password" type="password" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doLogin">登录</el-button>
      </el-form-item>
      <el-row>
        <el-col><a class="float-right" href="/dcm/gw/oauth2/authorization/iam?prefix">使用华为认证平台登录</a></el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator'
  import axios from 'axios'
  import qs from 'qs'

  const http = axios.create()

  @Component
  export default class Login extends Vue {
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

    user = {
      username: 'admin',
      password: '123456',
      grant_type: 'password',
      scope: 'userinfo'
    }

    doLogin (): void {
      http.post('/oauth2/token', qs.stringify(this.user), {
        auth: {
          username: 'app',
          password: '123456'
        }
      }).then(({ data }) => {
        const token = JSON.stringify(data)
        sessionStorage.setItem('token', token)
      }).then(() => {
        if (this.redirect) {
          window.location.href = this.redirect
        } else {
          this.$router.replace('/')
        }
      })
    }

    created (): void {
      if (this.redirect) {
        sessionStorage.setItem('SAVED_REQUEST', this.redirect)
      }
      window.location.href = `${env.CONTEXT_PATH}gw/oauth2/authorization/oauth2?prefix=${env.CONTEXT_PATH}&redirect=${encodeURI(this.redirect)}`
    }
  }
</script>

<style scoped lang="less">
  .login-page {
    display: flex;
    justify-content: center;
    height: 100%;
    align-items: center;

    .login-form {

    }
  }
</style>
