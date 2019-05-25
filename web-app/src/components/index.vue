<template>
  <div class="index">
    <el-form class="layout">
      <ul class="layoutContent">
        <router-link :to="menu.href"
                     v-for="menu in rootMenus"
                     tag="li"
                     :key="menu.id">
          <div class="icon"/>
          <div class="desc">{{ menu.title }}</div>
        </router-link>
      </ul>
    </el-form>
  </div>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const securityModule = namespace('security')

  @Component
  export default class Index extends Vue {
    @securityModule.Action('loadMenu')
    loadMenu

    @securityModule.Action('loadUserInfo')
    loadUserInfo

    @securityModule.State('menus')
    menus

    get rootMenus () {
      return this.menus
    }

    created () {
      this.loadUserInfo()
      this.loadMenu()
    }
  }
</script>

<style lang="less">
  *{
    margin: 0;
    padding: 0;
  }
  html,body{
    width: 100%;
    height: 100%;
  }
  li {
    list-style: none;
  }
  .index {
    width: 100%;
    height: 100%;
    .layout {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      .layoutContent {
        width: 850px;
        height: 160px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        li {
          width: 150px;
          height: 170px;
          margin: 20px;
          border: 1px solid #262228;
          border-radius: 3px;

          .icon {
            width: 100%;
            height: 120px;
            background: url("../assets/logo.png") no-repeat;
            background-size: 100% 100%;
          }
          .desc {
            width: 100%;
            height: 30px;
            line-height: 30px;
            text-align: center;
            font-size: 18px;
            font-weight: bold;
          }
        }
      }
    }
  }
</style>
