<template>
  <div class="menu-authority">
    <el-tree
      ref="tree"
      node-key="id"
      show-checkbox
      default-expand-all
      :expand-on-click-node="false"
      :props="treeProp"
      :data="menus"/>
  </div>
</template>
<script>
  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  import { namespace } from 'vuex-class'

  const authorityScope = namespace('system/menuAuthority')
  const menuScope = namespace('system/menu')

  @Component
  export default class MenuAuthority extends Vue {
    @Prop()
    name

    @menuScope.Getter('tree')
    menus

    get treeProp () {
      return { // 树形菜单显示属性
        children: 'children',
        label: 'title'
      }
    }

    // 更新权限信息
    @authorityScope.Action('update')
    updateAuthority

    // 获取菜单列表
    @menuScope.Action('listAll')
    listMenus

    // 获取权限信息
    @authorityScope.Action('get')
    getAuthority

    submit () {
      const data = {
        roleName: this.name,
        authorityMenus: this.$refs.tree.getCheckedKeys().map(id => ({ id }))
      }
      return this.updateAuthority(data)
    }

    created () {
      this.listMenus()
      this.getAuthority({ name: this.name }).then(({ data: { roleId, roleName, authorityMenus = [] } } = { data: { authorityMenus: [] } }) => {
        const checked = authorityMenus.map(menu => menu.id)
        this.$refs.tree.setCheckedKeys(checked)
      })
    }
  }
</script>
<style lang="less">

</style>
