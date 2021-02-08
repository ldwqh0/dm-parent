<template>
  <page-wrapper :header="['系统设置','菜单管理']">
    <el-container class="menus">
      <el-aside>
        <el-tree
          :expand-on-click-node="false"
          :default-expanded-keys="['']"
          node-key="id"
          :props="treeProp"
          :data="menuTree"
          @current-change="nodeChange" />
      </el-aside>
      <el-main style="padding: 0;">
        <el-form inline
                 :model="search"
                 class="clear-float">
          <el-row>
            <el-col :span="12">
              <el-form-item class="pull-left">
                <el-input
                  v-model="search.search"
                  placeholder="请输入关键字" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="float-right">
                <el-button type="primary" @click="add">新增</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <ele-data-tables ref="table"
                         :ajax="ajax"
                         :server-params="search"
                         pagination-layout="total, sizes, prev, pager, next, jumper">
          <el-table-column prop="name" label="菜单名称" />
          <el-table-column prop="title" label="菜单标题" />
          <el-table-column label="菜单状态">
            <template #default="{row}"><span>{{ row.enabled ? '启用' : '禁用' }}</span></template>
          </el-table-column>
          <el-table-column prop="id" label="操作" :min-width="120">
            <template #default="{row:{enabled,id}}">
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
              <el-button type="text" @click="edit({id})">编辑</el-button>
              <el-button type="text" @click="move({id,position:'UP'})">上移</el-button>
              <el-button type="text" @click="move({id,position:'DOWN'})">下移</el-button>
              <el-button type="text" @click="del({id})">删除</el-button>
            </template>
          </el-table-column>
        </ele-data-tables>
      </el-main>
      <el-dialog v-if="menuDialogVisible"
                 :visible.sync="menuDialogVisible"
                 :close-on-click-modal="false"
                 :close-on-press-escape="false">
        <e-menu :id="currentMenu.id" ref="menu" />
        <template #footer>
          <el-button @click="menuDialogVisible=false">取消</el-button>
          <el-button type="primary" :loading="loading" @click="saveMenu">确定</el-button>
        </template>
      </el-dialog>
    </el-container>
  </page-wrapper>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { Getter, namespace } from 'vuex-class'
  import Menu from './Menu.vue'
  import http from '@/http'
  import URLS from '../URLS'
  import { MenuDto, MenuTreeItem } from '@/types/Service'
  import { TreeProps } from 'element-ui/types/tree'
  import { PageWrapper } from '@/components'

  const menuModule = namespace('system/menu')
  @Component({
    components: {
      PageWrapper,
      EMenu: Menu
    }
  })
  export default class Menus extends Vue {
    menuDialogVisible = false
    currentMenu = {}

    treeProp: TreeProps = { // 树形机构显示熟属性
      children: 'children',
      label: 'title',
      disabled: 'disabled',
      isLeaf: 'isLeaf'
    }

    @Getter('loading')
    loading!: boolean

    ajax = URLS.menus

    @menuModule.Getter('tree')
    tree!: MenuTreeItem[]

    @menuModule.Action('loadAll')
    loadAll!: () => Promise<any>

    search: {
      parentId?: number,
      search?: string
    } = {}

    /**
     * 获取菜单的树型结构
     */
    get menuTree (): MenuTreeItem [] {
      return this.tree
      // return [{
      //   id: 0,
      //   title: '根1',
      //   url: '',
      //   openInNewWindow: false,
      //   children: this.tree,
      //   type: MenuType.COMPONENT
      // }]
    }

    add (): void {
      this.currentMenu = {}
      this.menuDialogVisible = true
    }

    created (): void {
      this.loadAll()
    }

    edit (data: MenuDto): void {
      this.currentMenu = data
      this.menuDialogVisible = true
    }

    move ({ id, position }: { id: number, position: string }): Promise<any> {
      return http.put(`${URLS.menus}/${id}/order`, { position })
        .then(this.reloadAll)
        .then(() => this.$message({
          message: '修改成功！',
          type: 'success'
        }))
    }

    patch (menu: MenuDto): Promise<any> {
      return http.patch(`${URLS.menus}/${menu.id}`, menu).then(this.reloadAll)
    }

    del (data: MenuDto): Promise<any> {
      return this.$confirm('此操作将永久删除该菜单, 删除菜单可能导致系统不正常的结果，是否继续?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${URLS.menus}/${data.id}`))
        .then(this.reloadAll)
        .catch(e => {
          if (e === 'cancel') {
            this.$message.info('已取消删除')
          }
        })
    }

    nodeChange (data: MenuDto): void {
      if (data.id === 0) {
        this.$set(this.search, 'parentId', null)
      } else {
        this.$set(this.search, 'parentId', data.id)
      }
    }

    saveMenu (): Promise<any> {
      return (this.$refs.menu as any).submit().then(this.reloadAll).then(() => {
        this.menuDialogVisible = false
      })
    }

    reloadAll (): Promise<any> {
      return Promise.all([this.loadAll(), (this.$refs.table as any).reloadData()])
    }
  }
</script>

<style lang="less">
</style>
