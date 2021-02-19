<template>
  <el-form ref="form"
           class="resource"
           :model="data"
           :rules="rules"
           label-width="120px">
    <el-row>
      <el-col :span="24">
        <el-form-item label="资源名称" prop="name">
          <el-input v-model="data.name" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="资源路径" prop="matcher">
          <el-input v-model="data.matcher" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="资源匹配模式" prop="matchType">
          <el-select v-model="data.matchType" placeholder="请选择" style="width: 100%;">
            <el-option value="REGEXP" label="正则表达式" />
            <el-option value="ANT_PATH" label="路径匹配" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
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
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="描述信息" prop="description">
          <el-input v-model="data.description" type="textarea" />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import http from '@/http'
  import URLS from '../URLS'
  import { Rules } from 'async-validator'
  import { MatchType, ResourceDto } from '@/types/service'

  @Component
  export default class Resource extends Vue {
    data: ResourceDto = {
      name: '',
      matcher: '',
      matchType: MatchType.ANT_PATH
    }

    scopes = []

    @Prop({
      type: [Number],
      default: () => 0
    })
    id!: number

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
        }]
      }
    }

    created (): void {
      if (this.id !== 0) {
        http.get(`${URLS.resource}/${this.id}`).then(({ data }) => (this.data = data))
      }
      http.get(URLS.scope).then(({ data }) => (this.scopes = data))
    }

    submit (): Promise<any> {
      return (this.$refs.form as any).validate().then(() => {
        return this.id === 0
          ? http.post(URLS.resource, this.data)
          : http.put(`${URLS.resource}/${this.id}`, this.data)
      })
    }
  }
</script>

<style lang="less">
</style>
