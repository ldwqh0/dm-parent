<template>
  <el-form ref="form"
           :rules="rules"
           :model="department"
           label-width="100px">
    <el-form-item label="节点名称：" prop="fullname">
      <el-input v-model="department.fullname" maxlength="100" />
    </el-form-item>

    <el-form-item label="节点简称：" prop="shortname">
      <el-input v-model="department.shortname" maxlength="100" />
    </el-form-item>

    <el-form-item label="节点类型：" prop="type">
      <el-select v-model="department.type" placeholder="请选择" style="width: 100%;">
        <el-option label="机构" value="ORGANS" />
        <el-option label="部门" value="DEPARTMENT" />
        <el-option label="分组" value="GROUP" />
      </el-select>
    </el-form-item>

    <el-form-item label="上级单位：" prop="parent">
      <el-cascader v-model="parent"
                   style="width: 100%;"
                   clearable
                   :options="departmentTree"

                   :props="cascadeProps" />
    </el-form-item>

    <el-form-item label="描述信息：" prop="description">
      <el-input v-model="department.description"
                type="textarea"
                show-word-limit
                maxlength="1000" />
    </el-form-item>
  </el-form>
</template>
<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { DepartmentDto, DepartmentTreeItem, Types } from '@/types/service'
  import { CascaderProps } from 'element-ui/types/cascader-panel'
  import http, { simpleHttp } from '@/http'
  import urls from '../URLS'
  import { listToTree } from '@/utils'
  import isNil from 'lodash/isNil'
  import { Rules } from 'async-validator'

  @Component
  export default class Department extends Vue {
    @Prop({
      type: [String, Number],
      required: false,
      default: '0'
    })
    id!: string

    @Prop({
      type: [String, Number],
      required: true
    })
    parentId!: string

    allDepartments: DepartmentDto[] = []

    get departmentTree (): DepartmentTreeItem[] {
      return listToTree(this.allDepartments)
    }

    department: DepartmentDto = {
      type: Types.DEPARTMENT,
      parent: { id: Number.parseInt(this.parentId) }
    }

    cascadeProps: CascaderProps<number, DepartmentDto> = {
      children: 'children',
      label: 'fullname',
      value: 'id',
      expandTrigger: 'hover',
      emitPath: false,
      checkStrictly: true
    }

    rules: Rules = {
      type: [{
        required: true,
        message: '节点类型不能为空'
      }],
      fullname: [{
        required: true,
        message: '部门全称不能为空',
        trigger: 'blur'
      }, {
        type: 'string',
        max: 100,
        message: '不能超过100个字符'
      }, {
        trigger: 'blur',
        validator: this.validateFullname
      }],
      shortname: [{
        required: true,
        message: '部门简称不能为空',
        trigger: 'blur'
      }, {
        type: 'string',
        max: 100,
        message: '不能超过100个字符'
      }],
      description: [{
        type: 'string',
        max: 1000,
        message: '不能超过1000个字符'
      }]
    }

    validateFullname (rules: unknown, value: string, callback: (error?: Error) => void): Promise<unknown> {
      return simpleHttp.get(`${urls.department}/validation`, {
        params: {
          fullname: value,
          parentId: this.department.parent?.id,
          exclude: this.id
        }
      }).then(({
        data: {
          result,
          message
        }
      }) => {
        if (result === 'success') {
          callback()
        } else {
          callback(new Error(message))
        }
      }).catch(e => {
        callback(new Error(e))
      })
    }

    get parent (): number | null {
      return this.department.parent?.id ?? null
    }

    set parent (id: number | null) {
      if (isNil(id)) {
        this.$delete(this.department, 'parent')
      } else {
        this.$set(this.department, 'parent', { id })
      }
    }

    created (): void {
      // 加载所有部门的列表
      http.get(`${urls.department}`, { params: { scope: 'all' } }).then(({ data }) => {
        this.allDepartments = data
      })
      // 加载当前部门信息
      if (Number.parseInt(this.id) > 0) {
        http.get(`${urls.department}/${this.id}`).then(({ data }) => (this.department = data))
      }
    }

    submit (): Promise<DepartmentDto> {
      return (this.$refs.form as any).validate().then(() => {
        if (Number.parseInt(this.id) > 0) {
          return http.put(`${urls.department}/${this.id}`, this.department)
        } else {
          return http.post(`${urls.department}`, this.department)
        }
      })
    }
  }
</script>
<style lang="less">

</style>
