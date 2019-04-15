<template>
  <el-form size="mini" :model="department" label-width="100px">
    <el-row>
      <el-button type="success"

                 icon="el-icon-arrow-left"
                 @click="goBack"/>
      <el-button type="success"

                 @click="onSubmit">
        提交
      </el-button>
    </el-row>
    <el-form-item label="部门名称：">
      <el-input v-model="department.fullname"/>
    </el-form-item>
    <el-form-item label="部门简称：">
      <el-input v-model="department.shortname"/>
    </el-form-item>
    <el-form-item label="部门类型：">
      <el-select v-model="department.type" placeholder="请选择">
        <el-option label="机构" value="ORGANS"/>
        <el-option label="部门" value="DEPARTMENT"/>
        <el-option label="分组" value="GROUP"/>
      </el-select>
    </el-form-item>
    <el-form-item label="状态：">
      <el-checkbox true-label="ENABLED" false-label="DISABLED" v-model="department.state">启用</el-checkbox>
    </el-form-item>
    <el-form-item label="上级单位：">
      <el-cascader
        v-model="department.parents"
        expand-trigger="hover"
        :options="tree"
        :props="treeProp"
        change-on-select/>
    </el-form-item>
  </el-form>
</template>
<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { DepartmentService } from '../service'

  @Component
  export default class Department extends Vue {
    @Prop({ type: [String, Number], default: 'new' })
    id // 当前项目的ID,为"new时代表新增"
    department = {// department对象
      type: 'DEPARTMENT',
      state: 'ENABLED'
    }
    tree = []
    treeProp = {
      children: 'children',
      label: 'text',
      value: 'id'
    }
    selectCity = []
    placeholder = '请选择'

    created () {
      DepartmentService.tree().then(tree => {
        this.tree = tree
      })
      if (this.id !== 'new') {
        DepartmentService.get({ id: this.id }).then(department => {
          this.department = department
          // 获取之后造型一下，使之符合表单需求
          let parents = []
          let cityCode = department.areaCode.slice(0, 4) + '00000'
          this.selectCity.push(cityCode)
          this.selectCity.push(department.areaCode)
          if (this.department.parents) {
            for (let parent of this.department.parents) {
              parents.push(parent.id + '')
            }
          }
          this.department.parents = parents
        })
      }
    }

    goBack () {
      this.$router.push({ name: 'departments' })
    }

    // 获取行政区划
    getRegionCity (val) {
      let len = val.length - 1
      this.department.areaCode = val[len]
    }

    onSubmit () {
      // 保存之前先造型一下，使之符号表单需求
      let department = Object.assign({}, this.department)
      let parents = []
      if (this.department.parents) {
        for (let id of this.department.parents) {
          parents.push({ id })
        }
      }
      department.parents = parents
      if (this.id !== 'new') {
        DepartmentService.update(department, { id: this.id }).then(data => {
          this.$router.push({ name: 'departments' })
        })
      } else {
        DepartmentService.save(department).then(data => {
          this.$router.push({ name: 'departments' })
        })
      }
    }
  }
</script>
<style lang="less">

</style>
