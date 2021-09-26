<template>
  <el-form ref="form"
           label-width="140px"
           :model="model"
           :rules="rules">
    <el-row>
      <el-col :span="12">
        <el-form-item label="资源服务名称：" prop="name">
          <el-input v-model.trim="model.name" />
        </el-form-item>
      </el-col>
      <el-col v-if="model.id" :span="12">
        <el-form-item label="clientId：">{{ model.id }}</el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="服务说明：" prop="description">
      <el-input v-model="model.description" type="textarea" maxlength="4000" />
    </el-form-item>

    <el-form-item label-width="0" style="text-align: center">
      <el-button @click="$router.back()">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
  import http from '@/http'
  import { Component, Prop, Vue } from 'vue-property-decorator'
  import urls from '../URLS'
  import { ClientDto, ClientType } from '@/types/service'
  import { Rules } from 'async-validator'

  /**
   * 资源服务器编辑页面
   */
  @Component
  export default class ClientResource extends Vue {
    @Prop({
      required: true,
      type: [String]
    })
    id!: string

    model: ClientDto = {
      type: ClientType.CLIENT_RESOURCE
    }

    get rules (): Rules {
      return {
        name: [{
          required: true,
          message: '名称不能为空'
        }, {
          validator: (rules, value, callback) => {
            // TODO 验证client是否存在
            callback()
          }
        }]
      }
    }

    save (): Promise<unknown> {
      return (this.$refs.form as any).validate()
        .then(() => {
          if (this.id === 'new') {
            return http.post(`${urls.client}`, this.model)
          } else {
            return http.put(`${urls.client}/${this.id}`, this.model)
          }
        }).then(() => this.$router.back())
    }

    created (): void {
      if (this.id !== 'new') {
        http.get(`${urls.client}/${this.id}`).then(({ data }) => (this.model = data))
      }
    }
  }
</script>

<style scoped>

</style>
