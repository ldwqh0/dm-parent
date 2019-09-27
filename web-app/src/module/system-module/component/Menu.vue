<template>
  <el-container class="menu">
    <el-main>
      <el-form size="mini"
               :model="menu"
               :rules="rules"
               label-width="120px"
               ref="menuform">
        <el-row>
          <el-col :span="24" class="flex-right" style="margin-top: 10px;">
            <router-link class="el-button el-button--default el-button--mini" :to="{name:'menus'}">
              取消
            </router-link>
            <el-button size="mini" type="primary" @click="doSubmit">提交</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="name">
              <el-input v-model="menu.name"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
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
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单链接" prop="url">
              <el-input v-model="menu.url"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="父菜单" prop="parents">
              <el-cascader

                v-model="menu.parents"
                expand-trigger="hover"
                :options="tree"
                :props="treeProp"
                change-on-select/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="状态" prop="enabled">
              <el-checkbox v-model="menu.enabled">启用</el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <el-input v-model="menu.icon"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="描述信息" prop="description">
              <el-input v-model="menu.description"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const menuModule = namespace('system/menu')
  @Component
  export default class Menu extends Vue {
    @Prop({ default: () => 'new' })
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

    get treeProp () {
      return {
        children: 'children',
        label: 'title',
        value: 'id'
      }
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

    doSubmit () {
      this.$refs.menuform.validate(valid => {
        if (valid) {
          let p
          if (this.id === 'new') {
            p = this.save(this.menu)
          } else {
            p = this.update(this.menu, { id: this.id })
          }
          p.then(() => {
            this.$router.push({ name: 'menus' })
          }).catch(e => {
            console.error(e)
          })
        } else {
          console.error('校验不通过')
        }
      })
    }

    created () {
      this.loadTree()

      if (this.id === 'new') {

      } else {
        this.getMenu({ id: this.id }).then(data => {
          this.menu = data
        })
      }
    }
  }
</script>

<style lang="less">
</style>
