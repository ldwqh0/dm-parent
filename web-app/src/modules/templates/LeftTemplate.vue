<template>
  <el-container class="left-template" @keydown.native="refreshOperationTime" @click.native="refreshOperationTime">
    <el-header class="header">
      <div class="title">{{ title }}</div>
      <el-menu background-color="#386be1"
               text-color="#ffffff"
               class="menu"
               mode="horizontal">
        <el-submenu index="1">
          <template #title>{{ displayName }}</template>
          <el-menu-item @click="$router.push({name:'myInfo'})">个人信息</el-menu-item>
          <el-menu-item @click="dialogVisible=true">修改密码</el-menu-item>
        </el-submenu>
        <el-menu-item @click="logout"><i style="color: white" class="el-icon-switch-button" />注销</el-menu-item>
      </el-menu>
    </el-header>
    <el-container v-loading="loading" style="height: 100%;overflow-y: hidden">
      <el-aside :width="menuCollapse?'65px':'200px'" class="template-aside">
        <div class="menu-control">
          <i :class="menuCollapse?'el-icon-s-unfold':'el-icon-s-fold'" @click="menuCollapse=!menuCollapse" />
          <!--          <el-button size="mini" @click="toggleMenuDisplay">显示{{ menuLabel === 'name' ? '标题' : '名称' }}</el-button>-->
        </div>
        <el-menu background-color="#2d2e42"
                 text-color="#ffffff"
                 class="aside-menu"
                 :collapse="menuCollapse">
          <menu-item v-for="(menu,index) in menuTree"
                     :key="index"
                     :item="menu"
                     :label-prop="menuLabel" />
        </el-menu>
      </el-aside>
      <el-main class="content-wrapper">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
<script lang="ts">
  import Vue from 'vue'
  import { Component, Watch } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'
  import MenuItem from './MenuItem.vue'
  import { MenuDto } from '@/types/service'
  import { ErrorState } from '@/http'
  import urls from './urls'
  import axios from 'axios'

  const securityModel = namespace('security')
  const httpModel = namespace('http')
  const ONE_MINUTE = 60 * 1000
  @Component({
    components: {
      MenuItem
    }
  })
  export default class LeftTemplate extends Vue {
    menuCollapse = false
    dialogVisible = false
    menuLabel = 'title'

    // 记录最后操作时间
    lastOperationTime: number = new Date().getTime()
    interval = 0

    @httpModel.Getter('loading')
    loading!: boolean

    @httpModel.State('error')
    error!: ErrorState

    @securityModel.State('user')
    currentUser!: UserDto

    @securityModel.Getter('menuTree')
    menuTree?: MenuDto[]

    @securityModel.Action('loadMenus')
    loadMenus!: () => Promise<MenuItem[]>

    @securityModel.Action('loadUserInfo')
    loadCurrentUser!: () => Promise<unknown>

    @securityModel.Action('logout')
    logoutAction!: () => Promise<undefined>

    get title (): string {
      return document.title
    }

    get displayName (): string {
      return this.currentUser.fullname ?? this.currentUser.username ?? '未知用户'
    }

    created (): void {
      this.loadMenus()
      this.loadCurrentUser()
      // 每隔一分钟向后台发送一次保活请求
      this.interval = window.setInterval(() => {
        if ((new Date().getTime() - this.lastOperationTime) < ONE_MINUTE) {
          axios.create().get(`${urls.session}`)
        } else if ((new Date().getTime() - this.lastOperationTime) > ONE_MINUTE * 12) {
          this.$alert('<p>检测到你长时间未操作系统，会话已经过期，需要重新登录</p>', '会话超时',
            {
              dangerouslyUseHTMLString: true
            }
          ).finally(() => this.logout())
          // 登录超时后，清除定时器
          window.clearInterval(this.interval)
        }
        // 检测当前时间是否在指定时间之内
      }, ONE_MINUTE)
    }

    destroyed (): void {
      // 组件销毁时，清除定时器
      window.clearInterval(this.interval)
    }

    logout (): void {
      this.logoutAction()
        .finally(() => this.$router.replace({
          name: 'login',
          query: {
            redirect: window.location.href
          }
        }))
    }

    @Watch('error', {
      immediate: false,
      deep: true
    })
    onErrorChange (newValue: ErrorState): void {
      if (newValue.count > 0) {
        this.$message.error(newValue.message)
      }
    }

    refreshOperationTime (): void {
      // 激活保持
      this.lastOperationTime = new Date().getTime()
    }

    toggleMenuDisplay (): void {
      if (this.menuLabel === 'name') {
        this.menuLabel = 'title'
      } else {
        this.menuLabel = 'name'
      }
    }

    submit (): Promise<any> {
      return (this.$refs.form as any).submit().then(() => {
        this.dialogVisible = false
      })
    }
  }
</script>
<style lang="less">
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
  }

  .el-table a {
    margin-right: 5px;
  }

  .float-right {
    float: right;
  }
</style>

<style lang="less" scoped>
  .left-template {
    height: 100%;

    .header {
      display: flex;
      align-items: center;
      //border-bottom: solid 1px #eceaea;
      justify-content: space-between;
      background: #386be1;
      color: white;

      .title {
        font-size: 24px;
      }

    }

    .content-wrapper {
      //padding: 0;
    }

    .template-aside {
      display: flex;
      flex-direction: column;
      background: #2d2e42;
      // 暴力的将超出部分隐藏起来
      overflow-x: hidden;

      .menu-control {
        color: white;
        border-right: solid 1px #2d2e42;
        text-align: center;
        cursor: pointer;
      }

      .aside-menu {
        flex: 1 1 auto;
        border-right: solid 1px #2d2e42;
      }
    }
  }
</style>
