<template>
  <el-form :model="menu"
           :rules="rules"
           label-width="120px"
           ref="menuform">
    <el-row>
      <el-col :span="12">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menu.name"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="menu.title"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="菜单类型" prop="type">
          <el-select v-model="menu.type">
            <el-option label="组件" value="COMPONENT"/>
            <el-option label="网页" value="HYPERLINK"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="菜单链接" prop="url">
          <el-input v-model="menu.url"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="父菜单" prop="parent">
          <el-cascader
            clearable
            v-model="parentId"
            expand-trigger="hover"
            :options="tree"
            :props="treeProp"
            change-on-select/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="图标" prop="icon">
          <el-select v-model="menu.icon"
                     filterable
                     allow-create
                     default-first-option
                     clearable>
            <template v-slot:prefix>
              <i :class="menu.icon"/>
            </template>
            <el-option v-for="icon in icons" :value="icon" :key="icon">
              <i :class="icon"/>
              <span>{{ icon }}</span>
            </el-option>
          </el-select>
          <!--          <el-input v-model="menu.icon" />-->
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
          <el-input v-model="menu.description"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
  import Vue from 'vue'
  import {Component, Prop} from 'vue-property-decorator'
  import {namespace} from 'vuex-class'
  import icons from './icons'

  const menuModule = namespace('system/menu')
  @Component
  export default class Menu extends Vue {
    @Prop({default: () => 'new'})
    id

    @menuModule.Action('get')
    getMenu

    @menuModule.Action('save')
    save

    @menuModule.Action('update')
    update

    @menuModule.Getter('tree')
    tree

    @menuModule.Action('listAll')
    loadTree

    get parentId () {
      return this.menu.parent ? this.menu.parent.id : null
    }

    set parentId (value) {
      this.$set(this.menu, 'parent', {id: value})
    }

    icons = icons

    treeProp = {
      children: 'children',
      label: 'title',
      value: 'id',
      emitPath: false
    }

    menu = {
      enabled: true,
      type: 'COMPONENT'
    }

    get rules () {
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

    submit () {
      return this.$refs.menuform.validate().then(valid => {
        return this.id === 'new' ? this.save(this.menu) : this.update(this.menu, {id: this.id})
      })
    }

    created () {
      this.loadTree()
      if (this.id !== 'new') {
        this.getMenu({id: this.id}).then(({data}) => (this.menu = data))
      }
    }
  }
</script>

<style lang="less">
</style>
