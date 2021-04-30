<template>
  <div>
    <ele-data-tables :ajax="clientUrl">
      <el-table-column prop="id" label="clientId" width="320" />
      <el-table-column prop="name" label="应用名称" />
      <el-table-column prop="accessTokenValiditySeconds" label="accessToken过期时间" />
      <el-table-column prop="refreshTokenValiditySeconds" label="refreshToken过期时间" />
      <el-table-column label="编辑">
        <template #default="{row}">
          <router-link :to="{name:'client',params:{id:row.id}}">编辑</router-link>
        </template>
      </el-table-column>
    </ele-data-tables>
  </div>
</template>

<script>
  import { Component, Vue } from 'vue-property-decorator'
  import urls from '../URLS'

  @Component
  export default class Clients extends Vue {
    clientUrl = urls.client
  }
</script>

<style scoped>

</style>
