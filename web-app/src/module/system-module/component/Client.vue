<template>
  <el-form size="mini"
           :model="data"
           ref="form"
           :rules="rules"
           label-width="150px">
    <el-row>
      <el-button type="success"
                 size="mini"
                 icon="el-icon-arrow-left"
                 @click="goBack"/>
      <el-button type="success"
                 size="mini"
                 @click="submit">
        提交
      </el-button>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="应用名称:" prop="name">
          <el-input type="text" v-model="data.name"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="应用ID:" prop="id" v-if="id!=='new'">
          <el-input
            disabled
            type="text"
            v-model="data.clientId"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="应用密钥:" prop="clientSecret">
          <el-input type="password" v-model="data.clientSecret"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="确认应用密钥:" prop="clientSecret">
          <el-input type="password" v-model="data.clientSecret"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="授权类型:" prop="authorizedGrantTypes">
          <el-select v-model="data.authorizedGrantTypes" multiple>
            <el-option value="authorization_code" key="authorization_code" label="授权码模式"/>
            <el-option value="password" key="password" label="密码模式"/>
            <el-option value="refresh_token" key="refresh_token" label="Refresh Token"/>
            <el-option value="implicit" key="implicit" label="简化模式"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="许可范围" prop="许可范围">
          <el-select v-model="data.scope"
                     filterable
                     multiple
                     default-first-option
                     allow-create>
            <el-option v-for="scope in scopes"
                       :key="scope"
                       :value="scope"/>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <!--允许访问的资源列表-->
      <el-form-item label="认证回调地址:" prop="registeredRedirectUri">
        <el-select v-model="data.registeredRedirectUri"
                   filterable
                   multiple
                   default-first-option
                   allow-create/>
      </el-form-item>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="token有效期" prop="accessTokenValiditySeconds">
          <el-input type="number"
                    :min="0"
                    :step="60"
                    :max="31636000"
                    v-model="data.accessTokenValiditySeconds"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="Refresh Token有效期"
                      prop="refreshTokenValiditySeconds">
          <el-input type="number"
                    :min="0"
                    :step="60"
                    :max="31636000"
                    v-model="data.refreshTokenValiditySeconds"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item prop="autoApprove">
          <el-checkbox v-model="data.autoApprove">自动授权</el-checkbox>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const clientScope = namespace('system/client')
  const resourceScope = namespace('system/resource')
  @Component
  export default class Client extends Vue {
    @Prop({ default: () => 'new' })
    id

    scopes = []

    data = {
      accessTokenValiditySeconds: 15818000,
      refreshTokenValiditySeconds: 15818000,
      autoApprove: false,
      authorizedGrantTypes: ['authorization_code'],
      scopes: ['app'],
      registeredRedirectUri: []
    }

    @clientScope.Action('save')
    save

    @clientScope.Action('update')
    update

    @clientScope.Action('get')
    getClient

    @resourceScope.Action('listScopes')
    listScopes

    get rules () {
      return {
        name: [{
          required: true,
          message: '请输入应用名称',
          trigger: 'blur'
        }],
        clientSecret: [{
          required: true,
          message: '请输入应用密钥',
          trigger: 'blur'
        }],
        registeredRedirectUri: [{
          required: true,
          message: '服务地址不能为空',
          trigger: 'blur'
        }]
      }
    }

    created () {
      this.listScopes().then(scopes => (this.scopes = scopes))
      if (this.id !== null && this.id !== undefined && this.id !== 'new') {
        this.getClient({ id: this.id }).then((rsp) => {
          this.data = rsp
        })
      }
    }

    goBack () {
      this.$router.go(-1)
    }

    submit () {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 提交
          (() => {
            if (this.id === null || this.id === undefined || this.id === 'new') {
              return this.save(this.data)
            } else {
              return this.update({ id: this.id, client: this.data })
            }
          })().then(rsp => {
            this.$router.go(-1)
          })
        }
      })
    }
  }
</script>

<style scoped>

</style>
