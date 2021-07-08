<template>
  <el-form ref="form"
           class="resource"
           :model="data"
           :rules="rules"
           label-width="120px">
    <el-row>
      <el-col :span="12">
        <el-form-item label="资源名称" prop="name">
          <el-input v-model="data.name" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="资源路径" prop="matcher">
          <el-input v-model="data.matcher" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="请求类型" prop="methods">
          <el-select v-model="data.methods" multiple style="width: 100%;">
            <el-option value="GET" />
            <el-option value="POST" />
            <el-option value="PUT" />
            <el-option value="PATCH" />
            <el-option value="DELETE" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="资源匹配模式" prop="matchType">
          <el-select v-model="data.matchType" placeholder="请选择" style="width: 100%;">
            <el-option value="REGEXP" label="正则表达式" />
            <el-option value="ANT_PATH" label="路径匹配" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="资源所属范围" prop="scope">
      <el-select v-model="data.scope"
                 style="width: 100%;"
                 filterable
                 multiple
                 default-first-option
                 allow-create
                 placeholder="请选择">
        <el-option v-for="scope in scopes"
                   :key="scope"
                   :value="scope" />
      </el-select>
    </el-form-item>

    <el-form-item label="允许访问">
      <el-select v-model="data.accessAuthorities"
                 style="width: 100%;"
                 :disabled="data.denyAll"
                 value-key="id"
                 multiple>
        <el-option v-for="role in roles"
                   :key="role.id"
                   :value="role"
                   :label="role.fullname" />
      </el-select>
    </el-form-item>

    <el-form-item label="拒绝访问">
      <el-select v-model="data.denyAuthorities"
                 style="width: 100%;"
                 value-key="id"
                 multiple>
        <el-option v-for="role in roles"
                   :key="role.id"
                   :value="role"
                   :label="role.fullname" />
      </el-select>
    </el-form-item>

    <el-form-item label="描述信息" prop="description">
      <el-input v-model="data.description" type="textarea" />
    </el-form-item>

    <el-form-item label-width="0" style="text-align: center">
      <el-button @click="$router.back()">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </el-form-item>
  </el-form>
</template>
<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import http from '@/http'
  import URLS from '../URLS'
  import { Rules } from 'async-validator'
  import { MatchType, ResourceDto, RoleDto } from '@/types/service'

  @Component
  export default class Resource extends Vue {
    data: ResourceDto = {
      name: '',
      matcher: '',
      matchType: MatchType.ANT_PATH
    }

    scopes = []

    roles: RoleDto[] = []

    @Prop({
      type: [Number, String],
      default: () => 0
    })
    id!: number | string

    get rules (): Rules {
      return {
        matcher: [{
          required: true,
          message: '资源路径不能为空',
          trigger: 'blur'
        }],
        name: [{
          required: true,
          message: '资源名称不能为空',
          trigger: 'blur'
        }],
        matchType: [{
          required: true,
          message: '资源匹配模式不能为空',
          trigger: 'blur'
        }]
      }
    }

    created (): void {
      if (this.id !== 0) {
        http.get(`${URLS.resource}/${this.id}`).then(({ data }) => (this.data = data))
      }
      http.get(URLS.role).then(({ data }) => (this.roles = data))
      http.get(URLS.scope).then(({ data }) => (this.scopes = data))
    }

    submit (): Promise<unknown> {
      return (this.$refs.form as any).validate().then(() => {
        return this.id === 0
          ? http.post(URLS.resource, this.data)
          : http.put(`${URLS.resource}/${this.id}`, this.data)
      }).then(() => this.$router.back())
    }

    authenticatedChange (v: boolean): void {
      if (v) {
        this.data.denyAll = false
      }
    }

    denyAllChange (v: boolean): void {
      if (v) {
        this.data.authenticated = false
        this.data.accessAuthorities = []
      }
    }
  }
</script>

<style lang="less">
</style>
