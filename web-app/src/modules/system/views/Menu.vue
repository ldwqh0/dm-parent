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
        <el-form-item label="父菜单" prop="parent">
          <el-cascader v-model="parentId"
                       clearable
                       expand-trigger="hover"
                       :options="menuTree"
                       :props="treeProp"
                       change-on-select />
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
        <el-form-item label="菜单类型" prop="type">
          <el-select v-model="menu.type">
            <el-option label="组件" value="COMPONENT" />
            <el-option label="网页" value="HYPERLINK" />
            <el-option label="子菜单" value="SUBMENU" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col v-if="menu.type!=='SUBMENU'" :span="12">
        <el-form-item label="菜单链接" prop="url">
          <el-input v-model="menu.url" :maxlength="1000" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="状态" prop="enabled">
          <el-checkbox v-model="menu.enabled">启用</el-checkbox>
        </el-form-item>
      </el-col>
      <el-col v-if="menu.type!=='SUBMENU'" :span="12">
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
  import http from '@/http'
  import URLS from '../URLS'
  import icons from './icons'
  import { Rules } from 'async-validator'
  import { MenuDto, MenuTreeItem, MenuType } from '@/types/service'
  import { CascaderProps } from 'element-ui/types/cascader-panel'
  import { AxiosResponse } from 'axios'
  import isEmpty from 'lodash/isEmpty'
  import isNil from 'lodash/isNil'
  import { namespace } from 'vuex-class'

  const menuModule = namespace('system/menu')

  @Component
  export default class Menu extends Vue {
    @Prop({
      type: [Number],
      default: () => 0
    })
    id!: number | string

    @menuModule.Getter('tree')
    menuTree!: MenuTreeItem[]

    @menuModule.Getter('map')
    menuMap!: { [key: string]: MenuDto }

    icons = icons

    treeProp: CascaderProps<MenuDto, any> = {
      children: 'children',
      label: 'title',
      value: 'id',
      emitPath: false
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
            if (isNil(value)) {
              callback()
            } else {
              let parent: MenuDto = this.menuMap[`${value.id}`]
              while (!isEmpty(parent)) {
                if (this.menu.id === parent.id) {
                  return callback(new Error('不能将一个菜单的父菜单设置为它自己或它的子菜单'))
                }
                parent = this.menuMap[`${parent.parent?.id}`]
              }
              callback()
            }
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

    created (): void {
      if (Number.parseInt(`${this.id}`) > 0) {
        http.get(`${URLS.menus}/${this.id}`).then(({ data }) => (this.menu = data))
      }
    }
  }
</script>

<style lang="less">
</style>
