<template>
  <el-container class="menus">
    <el-aside>
      <el-tree
        @current-change="nodeChange"
        :expand-on-click-node="false"
        :default-expanded-keys="['']"
        node-key="id"
        :props="treeProp"
        :data="menuTree"/>
    </el-aside>
    <el-main style="padding: 0;">
      <el-form inline
               :model="search"
               class="clear-float">
        <el-row>
          <el-col :span="12">
            <el-form-item class="pull-left">
              <el-input
                placeholder="请输入关键字"
                v-model="search.search"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="float-right">
              <el-button type="primary" @click="add">新增</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <ele-data-tables :ajax="ajax"
                       :server-params="search"
                       pagination-layout="total, sizes, prev, pager, next, jumper"
                       ref="table">
        <el-table-column prop="name" label="菜单名称"/>
        <el-table-column prop="title" label="菜单标题"/>
        <el-table-column label="菜单状态">
          <template slot-scope="scope"><span>{{ scope.row.enabled?'启用':'禁用' }}</span></template>
        </el-table-column>
        <el-table-column prop="id" label="操作" :min-width="120">
          <template v-slot="scope">
            <el-button v-if="scope.row.enabled"
                       type="text"
                       @click="patch({id:scope.row.id,enabled:false})">
              禁用
            </el-button>
            <el-button v-if="!scope.row.enabled"
                       type="text"
                       @click="patch({id:scope.row.id,enabled:true})">
              启用
            </el-button>
            <el-button type="text" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" @click="move({id:scope.row.id,position:'UP'})">上移</el-button>
            <el-button type="text" @click="move({id:scope.row.id,position:'DOWN'})">下移</el-button>
            <el-button type="text" @click="del(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </ele-data-tables>
    </el-main>
    <el-dialog v-if="menuDialogVisible"
               :close-on-click-modal="false"
               :visible.sync="menuDialogVisible">
      <e-menu ref="menu" :id="currentMenu.id"/>
      <template slot="footer">
        <el-button @click="menuDialogVisible=false">取消</el-button>
        <el-button @click="saveMenu" type="primary">确定</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import {Component} from 'vue-property-decorator'
  import EleDataTables from 'element-datatables'
  import {namespace} from 'vuex-class'
  import Menu from './Menu'

  const menuModule = namespace('system/menu')
  @Component({
    components: {
      EMenu: Menu,
      EleDataTables
    }
  })
  export default class Menus extends Vue {
    menuDialogVisible = false

    currentMenu = {}

    treeProp = { // 树形机构显示熟属性
      children: 'children',
      label: 'title'
    }

    @menuModule.Getter('tree')
    tree

    @menuModule.State('url')
    ajax

    @menuModule.Action('listAll')
    loadAll

    @menuModule.Action('move')
    moveMenu

    @menuModule.Action('patch')
    patchMenu

    @menuModule.Action('delete')
    delete

    get menuTree () {
      return [{
        title: '根',
        children: this.tree,
        id: ''
      }]
    }

    search = {
      parentId: null,
      search: null
    }

    add () {
      this.currentMenu = {}
      this.menuDialogVisible = true
    }

    created () {
      this.loadAll()
    }

    edit (data) {
      this.currentMenu = data
      this.menuDialogVisible = true
    }

    move (position) {
      this.moveMenu(position).then(res => {
        this.$message({
          message: '修改成功！',
          type: 'success'
        })
        this.$refs.table.reloadData()
      })
    }

    patch (data) {
      this.patchMenu(data).then(res => {
        this.$refs.table.reloadData()
      })
    }

    del (data) {
      this.$confirm('此操作将永久删除该菜单, 删除菜单可能导致系统不正常的结果，是否继续?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return this.delete(data)
      }).then(() => {
        this.$refs.table.reloadData()
      }).catch(e => {
        console.error(e)
      })
    }

    nodeChange (data) {
      this.search.parentId = data.id
    }

    saveMenu () {
      this.$refs.menu.submit().then(() => {
        this.loadAll()
        this.$refs.table.reloadData()
        this.menuDialogVisible = false
      })
    }
  }
</script>

<style lang="less">
</style>
