<template>
  <el-container class="menus">
    <el-aside width="250px" style="border-right: solid 1px #efefef;box-sizing: content-box">
      <el-tree ref="tree"
               :expand-on-click-node="false"
               :default-expanded-keys="[current.id]"
               node-key="id"
               :props="treeProp"
               :data="displayMenuTree"
               @current-change="selectNode">
        <template #default="{node}">
          <span>
            <i :class="node.data.icon || 'el-icon-menu'" />&nbsp;{{ node.label }}
          </span>
        </template>
      </el-tree>
    </el-aside>
    <el-main style="padding: 0 0 0 10px">
      <el-form inline :model="search" class="clear-float">
        <el-row>
          <el-col :span="12">
            <el-form-item class="pull-left">
              <el-input v-model="search.keyword" clearable placeholder="请输入关键字" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="float-right">
              <el-button type="primary" @click="currentEdit={}">新增</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <ele-data-tables ref="table"
                       :ajax="ajax"
                       :server-params="serverParams"
                       pagination-layout="total, sizes, prev, pager, next, jumper">
        <el-table-column prop="name" label="菜单名称" />
        <el-table-column prop="title" label="菜单标题" />
        <el-table-column label="菜单状态">
          <template #default="{row}">
            <span v-if="row.enabled">启用</span>
            <span v-else style="color: red">禁用</span>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="操作" :min-width="120">
          <template #default="{row:{enabled,id},row}">
            <el-button v-if="enabled"
                       type="text"
                       @click="patch({id,enabled:false})">
              禁用
            </el-button>
            <el-button v-if="!enabled"
                       type="text"
                       @click="patch({id,enabled:true})">
              启用
            </el-button>
            <el-button type="text" @click="currentEdit=row">编辑</el-button>
            <el-button type="text" @click="move({id,position:'UP'})">上移</el-button>
            <el-button type="text" @click="move({id,position:'DOWN'})">下移</el-button>
            <el-button type="text" @click="del({id})">删除</el-button>
          </template>
        </el-table-column>
      </ele-data-tables>
    </el-main>
    <el-dialog v-if="currentEdit"
               visible
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               @close="currentEdit=null">
      <e-menu :id="currentEdit.id" ref="menu" v-loading="loading" />
      <template #footer>
        <el-button @click="currentEdit=null">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveMenu">确定</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'
  import http from '@/http'
  import URLS from '../URLS'
  import { MenuDto, MenuTreeItem } from '@/types/service'
  import { TreeProps } from '@/types/element'
  import Menu from './Menu.vue'

  const menuModule = namespace('system/menu')
  const httpModel = namespace('http')
  @Component({
    components: {
      EMenu: Menu
    }
  })
  export default class Menus extends Vue {
    current: MenuDto = { id: 0 }
    currentEdit: MenuDto | null = null

    treeProp: TreeProps<unknown, MenuDto> = { // 树形机构显示熟属性
      children: 'children',
      label: 'title',
      disabled: 'disabled',
      isLeaf: (menu: MenuDto) => !menu.hasChildren
    }

    @httpModel.Getter('loading')
    loading!: boolean

    @menuModule.Action('loadAll')
    loadAll!: () => Promise<MenuDto[]>

    @menuModule.Getter('tree')
    menuTree!: MenuTreeItem[]

    ajax = URLS.menus

    search: {
      keyword?: string
    } = {}

    get serverParams (): Record<string, unknown> {
      return {
        keyword: this.search.keyword,
        parentId: this.current?.id === 0 ? null : this.current?.id
      }
    }

    get displayMenuTree (): MenuTreeItem[] {
      return [{
        id: 0,
        title: '全部',
        children: this.menuTree
      }]
    }

    created (): void {
      this.loadAll().then(() => this.selectNode(this.current))
    }

    move ({ id, position }: { id: number, position: string }): Promise<unknown> {
      return http.put<MenuDto>(`${URLS.menus}/${id}/order`, { position })
        .then(() => this.reload())
        .then(() => this.$message({
          message: '修改成功！',
          type: 'success'
        }))
    }

    saveMenu (): Promise<any> {
      return (this.$refs.menu as any).submit()
        .then(() => {
          this.reload()
          this.currentEdit = null
        })
    }

    patch (menu: MenuDto): Promise<any> {
      return http.patch<MenuDto>(`${URLS.menus}/${menu.id}`, menu)
        .then(() => this.reload())
    }

    del (data: MenuDto): Promise<any> {
      return this.$confirm('此操作将永久删除该菜单, 删除菜单可能导致系统不正常的结果，是否继续?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${URLS.menus}/${data.id}`))
        .then(() => this.reload())
        .catch(e => {
          if (e === 'cancel') {
            this.$message.info('已取消删除')
          }
        })
    }

    selectNode (current: MenuDto): void {
      (this.$refs.tree as any).setCurrentNode(current)
      this.current = current
    }

    reload (): void {
      (this.$refs.table as any).reloadData()
      this.loadAll().then(() => this.selectNode(this.current))
    }
  }
</script>

<style lang="less">
  // 自定义选中项目的样式
  .menus {
    .el-tree-node.is-current > .el-tree-node__content {
      background: #386be1 !important;
      color: white;
    }
  }
</style>
