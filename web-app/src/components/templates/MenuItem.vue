<template>
  <el-submenu
    :index="item.id + ''"
    popper-class="my-submenu"
    v-if="item.children && item.children.length > 0">
    <template v-slot:title>
      <i :class="item.icon ? item.icon : 'el-icon-s-tools'" />
      <span>{{ item.title }}</span>
    </template>
    <menu-item
      :item="submenu"
      :key="index"
      v-for="(submenu, index) in item.children" />
  </el-submenu>
  <el-menu-item
    :index="item.id + ''"
    @click="toTarget(item)"
    v-else>
    <i :class="item.icon ? item.icon : 'el-icon-s-tools'" />
    <template v-slot:title>
      <span>{{ item.title }}</span>
    </template>
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

    toTarget ({ type = 'COMPONENT', url = '/', openInNewWindow = false } = {
      type: 'COMPONENT',
      url: '/',
      openInNewWindow: false
    }) {
      if (type === 'COMPONENT') {
        if (openInNewWindow) {
          const target = this.$router.resolve(url)
          window.open(target.url, '_blank')
        } else {
          this.$router.push(url)
        }
      } else if (type === 'HYPERLINK') {
        if (openInNewWindow) {
          window.open(url, '_blank')
        } else {
          window.location.href = url
        }
      }
    }
  }
</script>

<style scoped></style>
<style lang="less">
  .my-submenu {
    &.el-menu--vertical {
      width: 100px;
    }

    .el-menu--popup-right-start {
      margin-left: 0px;
    }
  }
</style>
