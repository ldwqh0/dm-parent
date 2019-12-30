<template>
  <el-container>
    <el-main>
      <el-row>
        <el-row>
          <el-col :span="12">{{ roleName }}&nbsp;&nbsp;的菜单权限</el-col>
        </el-row>
        <el-col :span="12" class="col_tittle">
          <router-link class="el-button el-button--default" :to="{name:'roles'}">
            返回
          </router-link>
          <el-button type="primary" @click="doSubmit">提交</el-button>
        </el-col>
      </el-row>
      <el-tree
        ref="tree"
        node-key="id"
        show-checkbox
        :default-expand-all="true"
        :expand-on-click-node="false"
        :props="treeProp"
        :data="menus"/>
    </el-main>
  </el-container>
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

    roleName = ''

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

    doSubmit () {
      let data = {
        roleName: this.name,
        authorityMenus: this.$refs['tree'].getCheckedKeys().map(id => ({ id }))
      }
      this.updateAuthority(data).then(rep => {
        this.$router.push({ name: 'roles' })
      })
    }

    created () {
      this.listMenus()
      this.getAuthority({ name: this.name }).then(({ roleId, roleName, authorityMenus = [] } = { authorityMenus: [] }) => {
        this.roleName = roleName
        let checked = authorityMenus.map(menu => menu.id)
        this.$refs['tree'].setCheckedKeys(checked)
      })
    }
  }
</script>
<style lang="less">
  .el-button--default {
    padding: 6px 19px !important;
  }

  .col_tittle {
    display: flex;
    justify-content: flex-end;
  }
</style>
