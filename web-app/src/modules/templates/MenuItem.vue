<template>
  <el-submenu v-if="item.children && item.children.length>0"
              popper-class="my-submenu"
              :index="item.id+''">
    <template #title>
      <i :class="item.icon?item.icon:'el-icon-s-tools'" />
      <span>{{ item[labelProp] }}</span>
    </template>
    <menu-item v-for="(submenu,index) in item.children"
               :key="index"
               :label-prop="labelProp"
               :item="submenu" />
  </el-submenu>
  <el-menu-item v-else
                :index="item.id+''"
                @click="toTarget(item)">
    <i :class="item.icon?item.icon:'el-icon-s-tools'" />
    <template #title>
      <span>{{ item[labelProp] }}</span>
    </template>
  </el-menu-item>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { MenuTreeItem, MenuType } from '@/types/service'

  @Component({
    name: 'menu-item'
  })
  export default class MenuItem extends Vue {
    @Prop({ default: () => ({}) })
    item!: MenuTreeItem

    /**
     * 菜单显示的属性，可以是name或者title,用于区分开发时的显示
     */
    @Prop({
      required: false,
      type: [String],
      default: () => 'title'
    })
    labelProp!: string

    toTarget ({ type = MenuType.COMPONENT, url = '/', openInNewWindow = false }: MenuTreeItem = {
      title: '',
      type: MenuType.COMPONENT,
      url: '/',
      openInNewWindow: false
    }): Promise<any> | void {
      if (type === 'COMPONENT') {
        if (openInNewWindow) {
          const target = this.$router.resolve(url)
          window.open(target.href, '_blank')
        } else {
          // 隐藏菜单导航的错误
          return this.$router.push(url).catch(() => ({}))
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

<style scoped>

</style>
<style lang="less">
</style>
