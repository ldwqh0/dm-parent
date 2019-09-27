<!--左侧菜单模板-->
<template>
  <el-container id="app" class="app" v-loading="loading">
    <el-header style="background:#1D2023">
      <el-row class="title-wrapper">
        <el-col :span="12">
          <div class="title">
            <router-link to="/">系统管理</router-link>
          </div>
        </el-col>
        <el-col :span="12">
          <el-menu mode="horizontal" background-color="#1D2023">
            <el-menu-item index="0"
                          style="padding: 0 15px;"
                          class="avatar">
              <img src="@/assets/timg.jpg" alt="">
            </el-menu-item>
            <el-submenu index="2">
              <template slot="title">{{ displayUsername }}</template>
              <el-menu-item @click="doLogout" index="2-1" style="color: white">退出</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>
      </el-row>
    </el-header>
    <!--      <el-aside class="left-template">-->
    <!--        <el-menu :collapse="true"-->
    <!--                 style="min-height: 120px"-->
    <!--                 text-color="#FFF"-->
    <!--                 background-color="#1D2023">-->
    <!--          <menu-item v-for="(menu,index) in systemMenus"-->
    <!--                     :key="index"-->
    <!--                     :item="menu"/>-->
    <!--        </el-menu>-->
    <!--      </el-aside>-->
    <el-main v-loading="routing">
      <!--      <el-row class="breadCurb" style="">-->
      <!--        <el-col :span="24">-->
      <!--          <el-breadcrumb separator="/"-->
      <!--                         v-if="crumbs && crumbs.length>0">-->
      <!--            <el-breadcrumb-item v-for="crumb in crumbs"-->
      <!--                                :key="crumb.id">-->
      <!--              {{ crumb.title }}-->
      <!--            </el-breadcrumb-item>-->
      <!--          </el-breadcrumb>-->
      <!--        </el-col>-->
      <!--      </el-row>-->
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { State, Getter, namespace } from 'vuex-class'
  import MenuItem from '@/components/theme/MenuItem'

  const securityModule = namespace('security')
  @Component({
    components: {
      MenuItem
    }
  })
  export default class LeftTemplate extends Vue {
    @securityModule.Getter('menus')
    menus

    @Getter('loading')
    loading

    @securityModule.Action('logout')
    logout

    @securityModule.Action('loadMenu')
    loadMenu

    @securityModule.Action('loadCurrentUser')
    loadCurrentUser

    @securityModule.State('user')
    currentUser

    @State('routing')
    routing

    @Getter('crumbs')
    crumbs

    get systemMenus () {
      try {
        let menus = this.menus.filter(menu => menu.name === '系统管理')
        if (menus !== null && menus !== undefined && menus.length > 0) {
          return menus[0].submenus
        } else {
          return []
        }
      } catch (e) {
        return []
      }
    }

    get displayUsername () {
      let { fullname, username } = this.currentUser
      return fullname === undefined || fullname === null ? username : fullname
    }

    doLogout () {
      this.logout().then(() => {
        this.$router.push({ name: 'login' })
      })
    }

    created () {
      this.loadCurrentUser()
      // this.loadMenu()
    }
  }
</script>

<style lang="less">
  * {
    margin: 0;
    padding: 0;
  }

  html, body, #app {
    height: 100%;
  }

  body {
    margin: 0;
  }

  .left-template.el-aside {
    background: #1D2023;
    width: 150px !important;

    .el-menu-item {
      /*text-align: center;*/
    }

    .el-menu-item:hover, .el-submenu__title:hover {
      background: #85bdfb !important;
    }

    .el-menu {
      .el-submenu__title {
        height: 50px;
        line-height: 50px;
      }
    }
  }

  .el-header {
    .el-submenu__title {
      font-size: 18px;
      color: white !important;
    }

    .el-menu-item:hover, .el-submenu__title:hover {
      background: rgb(29, 32, 35) !important;
    }
  }

  .my-submenu {
    .el-menu--popup {
      margin-left: 0 !important;
    }

    .el-menu {
      .el-menu-item {
        height: 50px;
        /*text-align: center;*/
        min-width: 150px;
      }

      .el-menu-item:hover {
        background: #85bdfb !important;
      }
    }
  }

  .el-menu--collapse {
    width: unset;
  }

  /*.el-header {*/
  /*  background: #1D2023;*/
  /*}*/

  .title-wrapper {
    width: 100%;
    height: 100%;

    .el-col {
      width: 50%;
      height: 100%;

      .title {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        font-size: 26px;
        padding-left: 30px;
        color: #fff;
      }
    }

    .avatar {
      img {
        display: block;
        width: 50px;
        height: 50px;
        border-radius: 100%;
        margin-top: 5px;
      }
    }

    a.router-link-active {
      color: white;
      text-decoration: none;
    }
  }

  .el-menu--horizontal {
    display: flex;
    justify-content: flex-end;
  }

  .el-menu--horizontal {
    li:nth-child(3) {
      width: 15px;

      .el-submenu__title {
        height: 61px;
        background: red;

        .el-submenu__icon-arrow {
          top: 64%;
          right: 48px;
        }
      }
    }
  }

  .breadCurb {
    margin: -20px -20px 10px -20px;
    z-index: 10;
    background: #D9DBDB;
    height: 35px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding-left: 10px;
  }

  .router-link-active:hover {
    color: #aaa8af;
    text-decoration: underline;
  }
</style>
