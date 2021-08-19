<template>
  <el-form ref="menuform"
           :model="menu"
           :rules="rules"
           label-width="120px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menu.name" :maxlength="50" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="menu.title" :maxlength="50" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="菜单类型" prop="type">
          <el-select v-model="menu.type">
            <el-option label="组件" value="COMPONENT" />
            <el-option label="网页" value="HYPERLINK" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="菜单链接" prop="url">
          <el-input v-model="menu.url" :maxlength="1000" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="父菜单" prop="parent">
          <el-cascader
            v-model="parentId"
            clearable
            expand-trigger="hover"
            :options="menuParents"
            :props="treeProp"
            change-on-select
            @visible-change="resetParents" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="图标" prop="icon">
          <el-select v-model="menu.icon"
                     filterable
                     allow-create
                     default-first-option
                     clearable>
            <template #prefix>
              <i :class="menu.icon" />
            </template>
            <el-option v-for="icon in icons"
                       :key="icon"
                       :value="`el-icon-${icon}`"
                       :label="icon">
              <i :class="`el-icon-${icon}`" />
              <span>{{ icon }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="状态" prop="enabled">
          <el-checkbox v-model="menu.enabled">启用</el-checkbox>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="openInNewWindow" label-width="0px">
          <el-checkbox v-model="menu.openInNewWindow">在新窗口中打开链接</el-checkbox>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="描述信息" prop="description">
      <el-input v-model="menu.description" type="textarea" />
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import http, { simpleHttp } from '@/http'
  import URLS from '../URLS'
  import icons from './icons'
  import { Rules } from 'async-validator'
  import { MenuDto, MenuTreeItem, MenuType, Page } from '@/types/service'
  import { CascaderNode, CascaderProps } from 'element-ui/types/cascader-panel'
  import { AxiosResponse } from 'axios'
  import isEmpty from 'lodash/isEmpty'
  import cloneDeep from 'lodash/cloneDeep'

  @Component
  export default class Menu extends Vue {
    @Prop({
      type: [Number],
      default: () => 0
    })
    id!: number

    menuParents?: MenuTreeItem[] | null = null

    loadedMenus: { [key: string]: MenuDto } = {}

    // 将某些初始化值保持在这里，在某些情况下可以还原
    initValue: {
      menu?: MenuDto,
      parents?: MenuTreeItem[]
    } = {}

    icons = icons

    treeProp: CascaderProps<MenuDto, any> = {
      children: 'children',
      label: 'title',
      value: 'id',
      emitPath: false,
      lazy: true,
      leaf: 'isLeaf',
      lazyLoad: async (node: CascaderNode<MenuDto, any>, resolve: (v: any) => void) => {
        const hasChildren = node.data?.hasChildren ?? true
        if (hasChildren) {
          const { data: { content } } = await simpleHttp.get<Page<MenuDto>>(URLS.menus, {
            params: {
              page: 0,
              size: 2000,
              enabled: true,
              parentId: node.data?.id ?? ''
            }
          })
          content?.forEach(item => {
            this.loadedMenus[`${item.id}`] = item
            item.isLeaf = !item.hasChildren
          })
          resolve(content)
        } else {
          resolve([])
        }
      }
    }

    menu: MenuDto = {
      id: 0,
      name: '',
      title: '',
      url: '',
      enabled: true,
      openInNewWindow: false,
      type: MenuType.COMPONENT
    }

    get parentId (): number | undefined {
      return this.menu.parent?.id
    }

    set parentId (value: number | undefined) {
      if (value !== undefined) {
        this.$set(this.menu, 'parent', { id: value })
      } else {
        this.$set(this.menu, 'parent', null)
      }
    }

    get rules (): Rules {
      return {
        name: [{
          required: true,
          message: '菜单名称不能为空',
          trigger: 'blur'
        }],
        title: [{
          required: true,
          message: '菜单标题不能为空',
          trigger: 'blur'
        }],
        parent: [{
          validator: (rule, value, callback) => {
            let parent: MenuDto = this.loadedMenus[`${value.id}`]
            while (!isEmpty(parent)) {
              if (this.menu.id === parent.id) {
                callback(new Error('不能将一个菜单的父菜单设置为它自己或它的子菜单'))
                return
              }
              parent = this.loadedMenus[`${parent.parent?.id}`]
            }
            callback()
          }
        }]
      }
    }

    save (menu: MenuDto): Promise<AxiosResponse<MenuDto>> {
      return http.post<MenuDto>(URLS.menus, menu)
    }

    update (id: number, menu: MenuDto): Promise<AxiosResponse<MenuDto>> {
      return http.put<MenuDto>(`${URLS.menus}/${id}`, menu)
    }

    submit (): Promise<AxiosResponse<MenuDto>> {
      return (this.$refs.menuform as any).validate()
        .then(() => this.id === 0 ? this.save(this.menu) : this.update((this.id as number), this.menu))
    }

    /**
     * 在懒加载的机制下，需要一种模式来实现element-cascade的回显,这个方法在element展开的时候
     * @param event
     */
    resetParents (event: boolean): void {
      // 如果是下拉选展开，则清空下拉选列表，除非延迟加载逻辑
      if (event) {
        this.menuParents = undefined
      } else {
        /**
         * 如果是收起，需要判断是否对现有值进行了更改，如果没有发生变化，可能需要的下拉选都没有被加载出来，
         * 这个时候，最好的逻辑就是把原来的值拿出来
         */
        if (this.initValue.menu?.parent?.id === this.menu?.parent?.id) {
          this.menuParents = this.initValue.parents
        }
      }
    }

    created (): void {
      // 构建父级菜单的菜单树
      function buildParents (parents: MenuTreeItem[]): MenuTreeItem[] | undefined {
        if (isEmpty(parents)) {
          return undefined
        } else {
          const all = parents.reverse()
          all.forEach((v, index, all) => {
            if (index < all.length - 1) {
              v.children = [all[index + 1]]
            }
          })
          return [all[0]]
        }
      }

      if (this.id !== 0) {
        http.get(`${URLS.menus}/${this.id}`)
          .then(({ data }) => {
            this.menu = data
            this.loadedMenus[`${data.id}`] = data
            this.initValue.menu = cloneDeep(data)
            // 如果菜单的父级菜单不为空，加载父级菜单的列表
            if (!isEmpty(this.menu.parent)) {
              http.get(`${URLS.menus}/${this.id}/parents`)
                .then(({ data }) => {
                  this.initValue.parents = this.menuParents = buildParents(data)
                })
            }
          })
      }
    }
  }
</script>

<style lang="less">
</style>
