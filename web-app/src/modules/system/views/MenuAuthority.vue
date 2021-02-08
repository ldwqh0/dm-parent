<template>
  <div class="menu-authority">
    <el-tree
      ref="tree"
      node-key="id"
      show-checkbox
      default-expand-all
      :expand-on-click-node="false"
      :props="treeProp"
      :data="menus" />
  </div>
</template>
<script lang="ts">
  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  import { namespace } from 'vuex-class'
  import http from '@/http'
  import URLS from '../URLS'
  import { TreeProps } from 'element-ui/types/tree'

  // const authorityScope = namespace('system/menuAuthority')
  const menuScope = namespace('system/menu')

  @Component
  export default class MenuAuthority extends Vue {
    @Prop({
      required: true,
      type: [String, Number]
    })
    roleId!: string | number

    @menuScope.Getter('tree')
    menus!: any

    get treeProp (): TreeProps {
      return { // 树形菜单显示属性
        children: 'children',
        label: 'title',
        disabled: 'disabled',
        isLeaf: 'isLeaf'
      }
    }

    // 更新权限信息
    // @authorityScope.Action('update')
    // updateAuthority

    // 获取菜单列表
    @menuScope.Action('loadAll')
    listMenus!: () => Promise<any>

    submit (): Promise<any> {
      const data = {
        roleId: this.roleId,
        authorityMenus: (this.$refs.tree as any).getCheckedKeys().map((id: any) => ({ id }))
      }
      return http.put(`${URLS.menuAuthorities}/${this.roleId}`, data)
    }

    created (): Promise<any> {
      return Promise.all([this.listMenus(), http.get(`${URLS.menuAuthorities}/${this.roleId}`)])
        .then(([, { data: { authorityMenus = [] } }]) => {
          const checked = authorityMenus.map((menu: any) => menu.id);
          (this.$refs.tree as any).setCheckedKeys(checked)
        })
    }
  }
</script>
<style lang="less">

</style>
