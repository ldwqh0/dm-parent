<template>
  <el-submenu v-if="item.submenus && item.submenus.length>0"
              popper-class="my-submenu"
              :index="item.id+''">
    <template slot="title"> {{ item.title }}</template>
    <menu-item v-for="(submenu,index) in item.submenus"
               :item="submenu"
               :key="index"/>
  </el-submenu>
  <el-menu-item v-else
                @click="toTarget(item)"
                :index="item.id+''">
    <template>{{ item.title }}</template>
  </el-menu-item>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'

  @Component({
    name: 'menu-item'
  })
  export default class MenuItem extends Vue {
    @Prop({ default: () => ({}) })
    item

    toTarget ({ type = 'COMPONENT', href = '/' } = { type: 'COMPONENT', href: '/' }) {
      if (type === 'COMPONENT') {
        this.$router.push(href)
      } else if (type === 'HYPERLINK') {
        window.location.href = href
      }
    }
  }
</script>

<style scoped>

</style>
<style lang="less">
  .my-submenu {
    &.el-menu--vertical {
      width: 100px;
    }
  }
</style>
