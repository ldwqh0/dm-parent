<template>
  <el-form ref="form"
           label-width="100px"
           :model="model"
           :rules="rules">
    <el-row>
      <el-col :span="12">
        <el-form-item label="应用名称：" prop="name">
          <el-input v-model.trim="model.name" />
        </el-form-item>
      </el-col>
      <el-col v-if="model.id" :span="12">
        <el-form-item label="clientId：">{{ model.id }}</el-form-item>
      </el-col>
    </el-row>
    <el-row v-if="model.id===null||model.id===undefined">
      <el-col :span="12">
        <el-form-item label="客户端密钥：">
          {{ model.secret }}
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="应用类型" prop="type">
          <el-select v-model="model.type" style="width: 100%;">
            <el-option value="CLIENT_PUBLIC" label="公共客户端" />
            <el-option value="CLIENT_CONFIDENTIAL" label="机密客户端" />
            <el-option value="CLIENT_RESOURCE" label="资源服务器" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="autoApprove" label="">
          <el-checkbox v-model="model.autoApprove" label="允许自动授权" title="是否会跳转到用户授权页面" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item prop="accessTokenValiditySeconds" label="ACCESS_TOKEN 有效期：" label-width="200px">
          <el-input-number v-model="model.accessTokenValiditySeconds" />
          毫秒
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="refreshTokenValiditySeconds" label="REFRESH_TOKEN 有效期：" label-width="200px">
          <el-input-number v-model="model.refreshTokenValiditySeconds" />
          毫秒
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="授权类型" prop="authorizedGrantTypes">
      <el-select v-model="model.authorizedGrantTypes" multiple style="width: 100%;">
        <el-option v-for="type in grantTypes"
                   :key="type.value"
                   :value="type.value"
                   title="adb"
                   :label="type.name" />
      </el-select>
    </el-form-item>
    <el-form-item label="授权范围" prop="scopes">
      <el-select v-model="model.scopes"
                 multiple
                 filterable
                 default-first-option
                 style="width: 100%;"
                 allow-create>
        <el-option v-for="scope in scopes" :key="scope" :value="scope" />
      </el-select>
    </el-form-item>
    <el-form-item prop="registeredRedirectUris" label="回调地址">
      <el-input v-model="registeredRedirectUris"
                type="textarea"
                autosize
                show-word-limit />
    </el-form-item>
    <el-form-item label-width="0" style="text-align: center">
      <el-button type="primary" @click="save">确定</el-button>
      <el-button type="danger" @click="$router.back()">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
  import http from '@/http'
  import { Component, Prop, Vue } from 'vue-property-decorator'
  import urls from '../URLS'
  import { ClientDto } from '@/types/service'
  import { Rules } from 'async-validator'

  @Component
  export default class Client extends Vue {
    @Prop({
      required: true,
      type: [String]
    })
    id!: string

    scopes: string[] = []

    grantTypes: { name: string, value: string }[] = [{
      name: '授权码模式',
      value: 'authorization_code'
    }, {
      name: '客户端模式',
      value: 'client_credentials'
    }]

    model: ClientDto = {}

    rules: Rules = {
      name: [{
        required: true,
        message: '名称不能为空'
      }],
      registeredRedirectUris: [{
        required: true,
        message: '授权回调地址不能为空'
      }, {
        validator: this.uriValidator
      }]
    }

    uriValidator (rule: never, value: string[], callback: (error?: string | string[] | Error | void) => void): void {
      value.forEach(value1 => console.log(value1))
      callback()
    }

    get registeredRedirectUris (): string {
      return this.model.registeredRedirectUris?.join('\n') ?? ''
    }

    set registeredRedirectUris (value: string) {
      this.model.registeredRedirectUris = value.split('\n')
    }

    save (): Promise<unknown> {
      return (this.$refs.form as any).validate()
        .then(() => {
          if (this.id === 'new') {
            return http.post(`${urls.client}`, this.model)
          } else {
            return http.put(`${urls.client}/${this.id}`, this.model)
          }
        })
        .then(() => this.$router.back())
    }

    created (): void {
      if (this.id !== 'new') {
        http.get(`${urls.client}/${this.id}`).then(({ data }) => (this.model = data))
      }
      http.get(`${urls.uScopes}`).then(({ data: { content } }) => (this.scopes = content))
    }
  }
</script>

<style scoped>

</style>
