<template>
  <div class="login">
    <div v-if="useSso">登录中...</div>
    <el-form class="content" v-if="!useSso">
      <div class="title">系统登录</div>
      <el-form-item label="用户名：" label-width="80px">
        <el-input v-model="data.username"/>
      </el-form-item>
      <el-form-item label="密码：" label-width="80px">
        <el-input type="password" v-model="data.password"/>
      </el-form-item>
      <el-button @click="login" type="primary">登录</el-button>
    </el-form>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const securityModule = namespace('security')

  @Component({
    components: {}
  })
  export default class Login extends Vue {
    @Prop({})
    redirect

    @Prop({ default: () => '' })
    code

    // 是否启用单点登录，这是一个全局变量
    useSso = USE_SSO || false

    data = {
      'grant_type': 'password',
      'client_id': 'ownerapp',
      'client_secret': '123456',
      'username': 'admin',
      'password': '123456'
    }

    @securityModule.Action('token')
    getToken

    @securityModule.Action('login')
    doLogin

    goHome () {
      // 如果保存的有历史，要返回历史
      let {
        protocol,
        host
      } = window.location
      let savedUrl = sessionStorage.getItem('savedUrl')
      if (
        savedUrl !== null &&
        savedUrl !== undefined &&
        savedUrl !== '' &&
        savedUrl !== `${protocol}//${host}${CONTEXT_PATH}login`
      ) {
        window.location.href = savedUrl
      } else {
        this.$router.push({
          path: '/index'
        })
      }
    }

    // 手动登录按钮
    login () {
      this.doLogin(this.data).then(() => {
        if (this.redirect) {
          let target = JSON.parse(this.redirect)
          this.$router.replace(target)
        } else {
          this.$router.replace('/')
        }
      })
      // this.getToken(this.data).then(this.goHome)
    }
  }
</script>

<style lang="less">
  * {
    margin: 0;
    padding: 0;
  }

  html, body {
    width: 100%;
    height: 100%;
  }

  li {
    list-style: none;
  }

  .login {
    width: 100%;
    height: 100%;
    background: #2d3a4b;
    display: flex;
    justify-content: center;
    align-items: center;

    .content {
      width: 450px;
      height: 300px;

      .title {
        width: 100%;
        height: 80px;
        font-size: 25px;
        line-height: 80px;
        text-align: center;
        color: white;
      }

      .el-form-item__label {
        font-size: 16px;
        color: white;
      }

      .el-button {
        float: right;
        margin: 25px 0 0 0;
      }
    }
  }
</style>
