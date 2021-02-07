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
            :options="tree"
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
        <el-form-item label="状态" prop="enabled">
          <el-checkbox v-model="menu.enabled">启用</el-checkbox>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="openInNewWindow">
          <el-checkbox v-model="menu.openInNewWindow">在新窗口中打开链接</el-checkbox>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="menu.description" type="textarea" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'
  import http from '@/http'
  import URLS from '../URLS'
  import icons from './icons'
  import { Rules } from 'async-validator'
  import { MenuDto, MenuType } from '@/types/Service'

  const menuModule = namespace('system/menu')
  @Component
  export default class Menu extends Vue {
    @Prop({
      type: [Number],
      default: () => 0
    })
    id!: number

    @menuModule.Getter('tree')
    tree!: any

    @menuModule.Action('loadAll')
    loadTree!: () => Promise<any>

    icons = icons

    treeProp = {
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
        }]
      }
    }

    save (menu: MenuDto): Promise<any> {
      return http.post(URLS.menus, menu)
    }

    update (id: number, menu: MenuDto): Promise<any> {
      return http.put(`${URLS.menus}/${id}`, menu)
    }

    loadMenu (id: number = this.id): Promise<any> {
      return http.get(`${URLS.menus}/${id}`).then(({ data }) => (this.menu = data))
    }

    submit (): Promise<any> {
      return (this.$refs.menuform as any).validate()
          .then(() => this.id === 0 ? this.save(this.menu) : this.update((this.id as number), this.menu))
    }

    created (): void {
      this.loadTree()
      if (this.id !== 0) {
        this.loadMenu()
      }
    }
  }
</script>

<style lang="less">
</style>
