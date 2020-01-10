<template>
  <el-container id="app" class="left-template">
    <el-header class="header">
      <h1>
        <span>综合管理平台</span>
        <a href="javascript:void(0)" @click="fullscreen"><i class="el-icon-full-screen"></i></a>
      </h1>
      <el-menu mode="horizontal">
        <el-submenu index="2">
          <template slot="title"><img src="../../assets/avast.png" class="avast">{{currentUser.fullname||currentUser.username}}
          </template>
          <el-menu-item index="2-0" @click="logout">修改密码</el-menu-item>
          <el-menu-item index="2-1" @click="logout">退出</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-header>
    <el-container>
      <el-aside width="250px" class="aside">
        <el-menu v-bind="menuStyle" class="menu" unique-opened>
          <menu-item v-for="menu in menuTree" :key="menu.id" :item="menu"/>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import MenuItem from './MenuItem'
  import {Component} from 'vue-property-decorator'
  import {namespace} from 'vuex-class'

  const securityModule = namespace('security')
  @Component({
    components: {
      MenuItem
    }
  })
  export default class LeftTemplate extends Vue {
    fullState = false
    activeName = 'home'

    menuStyle = {
      backgroundColor: '#223332',
      textColor: '#a5afb8',
      activeTextColor: '#e5f4ff'
    }

    @securityModule.Action('loadMenus')
    loadMenus

    @securityModule.Getter('menuTree')
    menuTree

    @securityModule.Action('loadCurrentUser')
    loadCurrentUser

    @securityModule.State('currentUser')
    currentUser

    get menus () {
      const menus = {}
      this.menuTree.forEach(menu => {
        if (!menu.children) {
          menus[menu.name] = [menu]
        } else {
          menus[menu.name] = [...menu.children]
        }
      })
      return menus
    }

    created () {
      this.loadMenus()
      this.loadCurrentUser()
    }

    logout () {
      this.$http.post('/gw/logout').then(() => {
        // alert('登出成功')
      }).finally(() => {
        window.location.href = `http://127.0.0.1:8887/oauth/logout`
      })
    }

    fullscreen () {
      let element = document.documentElement
      // 判断是否已经是全屏
      // 如果是全屏，退出
      if (this.fullState) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else { // 否则，进入全屏
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      // 改变当前全屏状态
      this.fullState = !this.fullState
    }
  }
</script>

<style lang="less">
  .left-template {
    height: 100%;

    .aside {
      background: #2b333e;

      .menu {
        border-right: unset;

        .el-submenu__title, .el-menu-item {
          &:hover {
            background-color: #1287d1 !important;
          }

          &.is-active {
            background-color: #1492e1 !important;
          }
        }
      }

      /*border-right: solid 1px #e6e6e6;*/
    }

    .header {
      display: flex;
      padding: 0 !important;

      .avast {
        margin-right: 10px;
        margin-top: -5px;
      }

      h1 {
        color: #323233;
        padding: 0 20px;
        margin: 0;
        line-height: 60px;
        flex: 1 auto;
        border-bottom: solid 1px #e6e6e6;
        display: flex;
        align-items: center;
        border-right: solid 1px #e6e6e6;

        a {
          color: #323233;
        }

        span {
          font-weight: normal;
          flex: 1 auto;
        }
      }
    }
  }
</style>
