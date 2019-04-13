<template>
  <ele-data-tables :ajax="ajax"
                   ref="table"
                   :server-params="searchObj">
    <el-table-column prop="value" label="Token值" min-width="150px"/>
    <el-table-column prop="username" label="授权用户"/>
    <el-table-column prop="scope" label="授权范围"/>
    <el-table-column prop="expiration" label="过期时间"/>
    <el-table-column label="管理" min-width="50px">
      <template slot-scope="scope">
        <el-button type="text" size="small" @click="deleteToken(scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </ele-data-tables>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const clientScope = namespace('system/client')
  @Component
  export default class Tokens extends Vue {
    @Prop({ default: () => '' })
    client

    @clientScope.Action('listToken')
    listTokens

    @clientScope.State('url')
    url

    @clientScope.Action('delToken')
    delToken

    searchObj = {}

    get ajax () {
      return `${this.url}/${this.client}/tokens`
    }

    deleteToken (token) {
      this.delToken(token).then(() => {
        this.$refs['table'].reloadData()
      })
    }
  }
</script>

<style scoped>

</style>
