<template>
  <el-form ref="form"
           label-width="100px"
           :model="model"
           :rules="rules">
    <el-form-item label="clientId">{{ model.id }}</el-form-item>
    <el-form-item label="应用名称" prop="name">
      <el-input v-model.trim="model.name" />
    </el-form-item>
    <el-row>
      <el-col :span="8">
        <el-form-item label="应用类型" prop="type">
          <el-select v-model="model.type">
            <el-option value="CLIENT" label="客户端" />
            <el-option value="RESOURCE" label="资源服务器" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item prop="requirePkce" label="需要pkce">
          <el-checkbox v-model="model.requirePkce" />
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item prop="autoApprove" label="允许自动授权">
          <el-checkbox v-model="model.autoApprove" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item prop="accessTokenValiditySeconds" label="ACCESS_TOKEN 有效期" label-width="200px">
          <el-input-number v-model="model.accessTokenValiditySeconds" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="refreshTokenValiditySeconds" label="REFRESH_TOKEN 有效期" label-width="200px">
          <el-input-number v-model="model.refreshTokenValiditySeconds" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="授权类型" prop="authorizedGrantTypes">
      <el-select v-model="model.authorizedGrantTypes" multiple style="width: 100%;">
        <el-option v-for="type in grantTypes"
                   :key="type.value"
                   :value="type.value"
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
      <el-input v-model="registeredRedirectUris" type="textarea" autosize />
    </el-form-item>
    <el-form-item label-width="0" style="text-align: center">
      <el-button type="primary" @click="save">确定</el-button>
      <el-button type="danger">取消</el-button>
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
        .then(() => http.put(`${urls.client}/${this.id}`, this.model))
        .then(() => this.$router.back())
    }

    created (): void {
      http.get(`${urls.client}/${this.id}`).then(({ data }) => (this.model = data))
      http.get(`${urls.uScopes}`).then(({ data: { content } }) => (this.scopes = content))
    }
  }
</script>

<style scoped>

</style>
