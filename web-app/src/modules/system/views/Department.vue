<template>
  <el-form ref="form"
           size="mini"
           :rules="rules"
           :model="department"
           label-width="100px">
    <el-row>
      <el-col :span="24">
        <el-form-item label="部门名称：" prop="fullname">
          <el-input v-model="department.fullname"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="部门简称：" prop="shortname">
          <el-input v-model="department.shortname"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="部门类型：" prop="type">
          <el-select v-model="department.type" placeholder="请选择">
            <el-option label="机构" value="ORGANS"/>
            <el-option label="部门" value="DEPARTMENT"/>
            <el-option label="分组" value="GROUP"/>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <!--
    <el-row>
      <el-col :span="24">
        <el-form-item label="状态：">
          <el-checkbox true-label="ENABLED" false-label="DISABLED" v-model="department.state">启用</el-checkbox>
        </el-form-item>
      </el-col>
    </el-row>
    -->
    <el-row>
      <el-col :span="24">
        <el-form-item label="上级单位：" prop="parent">
          <el-cascader
            clearable
            v-model="parent"
            expand-trigger="hover"
            :options="departmentTree"
            :props="treeProp"
            change-on-select/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-form-item label="描述信息：" prop="description">
          <el-input type="textarea" v-model="department.description"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'

  import { namespace } from 'vuex-class'

  const departmentModule = namespace('system/department')
  @Component
  export default class Department extends Vue {
    @Prop({ type: [String, Number], default: 'new' })
    id

 // 当前项目的ID,为"new时代表新增"
    department = {// department对象
      type: 'DEPARTMENT',
      state: 'ENABLED'
    }

    treeProp = {
      children: 'children',
      label: 'fullname',
      value: 'id',
      emitPath: false
    }

    rules = {
      fullname: [{
        required: true,
        message: '部门全称不能为空',
        trigger: 'blur'
      }],
      shortname: [{
        required: true,
        message: '部门简称不能为空',
        trigger: 'blur'
      }]
    }

    @departmentModule.Getter('tree')
    departmentTree

    @departmentModule.Action('get')
    getDepartment

    @departmentModule.Action('save')
    save

    @departmentModule.Action('update')
    update

    get parent () {
      return this.department.parent ? this.department.parent.id : null
    }

    set parent (id) {
      this.$set(this.department, 'parent', { id })
    }

    created () {
      if (this.id !== 'new') {
        this.getDepartment({ id: this.id }).then(data => (this.department = data))
      }
    }

    // 获取行政区划
    getRegionCity (val) {
      const len = val.length - 1
      this.department.areaCode = val[len]
    }

    submit () {
      return this.$refs.form.validate().then(() => {
        return this.id === 'new' ? this.save(this.department) : this.update(this.department)
      })
    }
  }
</script>
<style lang="less">

</style>
