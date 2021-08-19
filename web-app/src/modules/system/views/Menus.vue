<template>
  <el-container class="menus">
    <el-aside width="250px" style="border-right: solid 1px #efefef;box-sizing: content-box">
      <el-tree ref="tree"
               :expand-on-click-node="false"
               :default-expanded-keys="['']"
               node-key="id"
               :props="treeProp"
               :load="loadNode"
               lazy
               @current-change="nodeChange">
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
              <el-button type="primary" @click="add">新增</el-button>
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
  import http, { simpleHttp } from '@/http'
  import URLS from '../URLS'
  import { MenuDto, Page } from '@/types/service'
  import { TreeNode } from 'element-ui/types/tree'
  import isNil from 'lodash/isNil'
  import { TreeProps } from '@/types/element'
  import { AxiosResponse } from 'axios'
  import Menu from './Menu.vue'

  const httpModel = namespace('http')
  @Component({
    components: {
      EMenu: Menu
    }
  })
  export default class Menus extends Vue {
    current: MenuDto | null = null
    currentEdit: MenuDto | null = null

    treeProp: TreeProps<unknown, MenuDto> = { // 树形机构显示熟属性
      children: 'children',
      label: 'title',
      disabled: 'disabled',
      isLeaf: (menu: MenuDto) => !menu.hasChildren
    }

    @httpModel.Getter('loading')
    loading!: boolean

    ajax = URLS.menus

    search: {
      keyword?: string
    } = {}

    get serverParams (): Record<string, unknown> {
      return {
        keyword: this.search.keyword,
        parentId: this.current?.id
      }
    }

    add (): void {
      this.currentEdit = {}
    }

    edit (data: MenuDto): void {
      this.currentEdit = data
    }

    move ({
      id,
      position
    }: { id: number, position: string }): Promise<unknown> {
      return http.put<MenuDto>(`${URLS.menus}/${id}/order`, { position })
        .then(({ data: { parent } }) => this.reloadTreeAndTable(parent))
        .then(() => this.$message({
          message: '修改成功！',
          type: 'success'
        }))
    }

    /**
     * 从网络获取一个树的子节点
     * */
    loadChildren (parentId: string | number): Promise<AxiosResponse<Page<MenuDto>>> {
      return simpleHttp.get<Page<MenuDto>>(`${URLS.menus}`, {
        params: {
          parentId,
          enabled: true,
          page: 0,
          size: 2000
        }
      })
    }

    /**
     * 左侧树懒加载节点
     */
    loadNode ({ key }: TreeNode<number, MenuDto>, resolve: (r: MenuDto[]) => void): void {
      if (isNil(key)) {
        resolve([{
          id: '' as any,
          title: '根菜单'
        }])
      } else {
        this.loadChildren(key).then(({ data: { content } }) => resolve(content ?? []))
      }
    }

    patch (menu: MenuDto): Promise<any> {
      return http.patch<MenuDto>(`${URLS.menus}/${menu.id}`, menu)
        .then(({ data: { parent } }) => this.reloadTreeAndTable(parent)
        )
    }

    del (data: MenuDto): Promise<any> {
      return this.$confirm('此操作将永久删除该菜单, 删除菜单可能导致系统不正常的结果，是否继续?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${URLS.menus}/${data.id}`))
        .then(() => this.reloadTreeAndTable(this.current))
        .catch(e => {
          if (e === 'cancel') {
            this.$message.info('已取消删除')
          }
        })
    }

    nodeChange (current: MenuDto): void {
      this.current = current
    }

    saveMenu (): Promise<any> {
      return (this.$refs.menu as any).submit()
        .then(({ data: { parent } }: any) => {
          this.reloadTreeAndTable(parent)
          this.currentEdit = null
        })
    }

    /**
     * 重新加载树和表格
     * @param node
     */
    reloadTreeAndTable (node?: MenuDto | null): void {
      const id = node?.id ?? '';
      // 重新刷新表格
      (this.$refs.table as any).reloadData()
      this.loadChildren(id)
        // 重新加载某个父节点的子节点
        .then(({ data: { content } }) => (this.$refs.tree as any).updateKeyChildren(id, content ?? []))
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
