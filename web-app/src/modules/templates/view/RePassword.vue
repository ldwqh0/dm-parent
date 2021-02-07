<template>
  <el-form ref="form"
           label-width="120px"
           :model="rePasswords"
           :rules="rules">
    <el-form-item label="原密码：" prop="password">
      <el-input v-model="rePasswords.password" type="password" />
    </el-form-item>
    <el-form-item label="新密码：" prop="newPassword">
      <el-input v-model="rePasswords.newPassword" type="password" />
    </el-form-item>
    <el-form-item label="确认新密码：" prop="rePassword">
      <el-input v-model="rePasswords.rePassword" type="password" />
    </el-form-item>
  </el-form>
</template>
<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator'
  import { Rules } from 'async-validator'
  import urls from '../../mweb/myInfo/urls'
  import http from '@/http'

  @Component
  export default class RePassword extends Vue {
    rePasswords = {
      password: '',
      newPassword: '',
      rePassword: ''
    }

    rules: Rules = {
      password: [{
        required: true,
        message: '原始密码不能为空'
      }],
      newPassword: [{
        required: true,
        message: '新密码不能为空'
      }, {
        type: 'string',
        min: 6,
        max: 50,
        message: '密码长度要在6-50个字符之间'
      }],
      rePassword: [{
        required: true,
        message: '重复密码不能为空'
      }, {
        validator: this.rePasswordValidator
      }]
    }

    rePasswordValidator (rules: Rules, value: string, callback: (error?: Error) => void): void {
      if (value === this.rePasswords.newPassword) {
        callback()
      } else {
        callback(new Error('两次密码不相同'))
      }
    }

    submit (): Promise<any> {
      return (this.$refs.form as any).validate()
        .then(() => http.patch(`${urls.myInfo}/password`, this.rePasswords))
        .then(() => this.$message.success('密码修改成功'))
        .catch((e: any) => Promise.reject(e))
    }
  }
</script>
<style lang="less" scoped>

</style>
