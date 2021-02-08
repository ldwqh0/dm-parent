<template>
  <el-form ref="form"
           class="role"
           :model="role"
           :rules="rules"
           label-width="120px">
    <el-form-item label="角色名称：" prop="name">
      <el-input v-model="role.name" />
    </el-form-item>
    <el-form-item label="角色组：">
      <el-select v-model="role.group"
                 placeholder="请选择角色所属组,也可以直接输入新增一个角色组"
                 allow-create
                 filterable>
        <el-option v-for="roleGroup in roleGroups"
                   :key="roleGroup"
                   :value="roleGroup" />
      </el-select>
    </el-form-item>
    <el-form-item label="状态：" prop="state">
      <el-checkbox v-model="role.state" true-label="ENABLED" false-label="DISABLED">启用</el-checkbox>
    </el-form-item>
    <el-form-item label="描述：" prop="state">
      <el-input
        v-model="role.description"
        type="textarea" />
    </el-form-item>
  </el-form>
</template>
<script lang="ts">
  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  import { RoleDto, Status } from '@/types/service'
  import urls from '../URLS'
  import http from '@/http'
  import { Rules } from 'async-validator'

  @Component
  export default class Role extends Vue {
    @Prop({
      type: [String, Number],
      default: () => '0'
    })
    id!: string

    roleGroups: string[] = []

    role: RoleDto = {
      state: Status.ENABLED
    }

    rules: Rules = {
      name: [{
        required: true,
        message: '名称不能为空',
        trigger: 'blur'
      }],
      group: {
        required: true,
        message: '角色组不能为空'
      }
    }

    created (): void {
      http.get(`${urls.roleGroups}`).then(({ data }) => {
        this.roleGroups = data
      })
      if (Number.parseInt(this.id) > 0) {
        http.get(`${urls.role}/${this.id}`).then(({ data }) => {
          this.role = data
        })
      }
    }

    submit (): Promise<RoleDto> {
      return (this.$refs.form as any).validate().then(() => {
        // 对数据进行一下造型，主要是手动输入的角色是名称，需要在后台添加
        if (Number.parseInt(this.id) > 0) {
          return http.put(`${urls.role}/${this.id}`, this.role)
        } else {
          return http.post(`${urls.role}`, this.role)
        }
      })
    }
  }
</script>
