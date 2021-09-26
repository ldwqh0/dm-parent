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
  import isNil from 'lodash/isNil'
  import { Rules } from 'async-validator'
  import { ValidationResult } from '@/types/service/service-common'
  import { namespace } from 'vuex-class'
  import isEmpty from 'lodash/isEmpty'

  const departmentModule = namespace('system/department')
  @Component
  export default class Department extends Vue {
    @Prop({
      type: [String, Number],
      required: false,
      default: () => 0
    })
    id!: string | number

    @Prop({
      type: [String, Number],
      required: false
    })
    parentId?: number | string

    @departmentModule.Getter('tree')
    departmentTree!: DepartmentTreeItem[]

    @departmentModule.Getter('map')
    departmentMap!: { [key: string]: DepartmentDto }

    department: DepartmentDto = {
      type: Types.DEPARTMENT
    }

    cascadeProps: CascaderProps<number, DepartmentDto> = {
      children: 'children',
      label: 'fullname',
      value: 'id',
      expandTrigger: 'hover',
      emitPath: false,
      checkStrictly: true
    }

    get rules (): Rules {
      return {
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
          validator: (rules, value, callback): Promise<unknown> => {
            return simpleHttp.get<ValidationResult>(`${urls.department}/validation`, {
              params: {
                fullname: value,
                parentId: this.department.parent?.id,
                exclude: this.id
              }
            }).then(({ data: { success, message = '无错误信息' } }) => {
              if (success) {
                callback()
              } else {
                callback(new Error(message))
              }
            }).catch(e => {
              callback(new Error(e))
            })
          }
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
        }],
        parent: [{
          validator: (rule, value, callback) => {
            if (isNil(value)) {
              callback()
            } else {
              let parent: DepartmentDto = this.departmentMap[`${value.id}`]
              while (!isEmpty(parent)) {
                if (this.department.id === parent.id) {
                  return callback(new Error('不能将一个节点的上级节点设置为它自身或它的子节点'))
                }
                parent = this.departmentMap[`${parent.parent?.id}`]
              }
              callback()
            }
          }
        }]
      }
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
      // 加载当前部门信息
      if (Number.parseInt(`${this.id}`) > 0) {
        http.get(`${urls.department}/${this.id}`).then(({ data }) => (this.department = data))
      } else {
        if (!isNil(this.parentId) && !isNaN(Number.parseInt(`${this.parentId}`))) {
          this.department.parent = { id: Number.parseInt(`${this.parentId}`) }
        }
      }
    }

    submit (): Promise<DepartmentDto> {
      return (this.$refs.form as any).validate().then(() => {
        if (Number.parseInt(`${this.id}`) > 0) {
          return http.put(`${urls.department}/${this.id}`, this.department)
        } else {
          return http.post(`${urls.department}`, this.department)
        }
      })
    }
  }
</script>
<style lang="less" scoped>

</style>
