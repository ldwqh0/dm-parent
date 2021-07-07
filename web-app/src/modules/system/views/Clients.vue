<template>
  <div>
    <el-form inline :model="serverParams">
      <el-row>
        <el-col :span="12">
          <el-form-item prop="keyword">
            <el-input v-model="serverParams.keyword" placeholder="请输入搜索关键字" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item style="float: right">
            <el-button type="primary" @click="$router.push({name:'client',params:{id:'new'}})">新应用</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <ele-data-tables ref="table" :ajax="clientUrl" :server-params="serverParams">
      <el-table-column prop="id" label="clientId" width="320" />
      <el-table-column prop="name" label="应用名称" />
      <el-table-column prop="type" label="应用类型">
        <template #default="{row}">
          {{ row.type | toClientType }}
        </template>
      </el-table-column>
      <!--      <el-table-column prop="accessTokenValiditySeconds" label="accessToken过期时间" />-->
      <!--      <el-table-column prop="refreshTokenValiditySeconds" label="refreshToken过期时间" />-->
      <el-table-column label="状态">
        <template #default="{row}">
          <el-button v-if="row.enabled"
                     title="点击以禁用该应用"
                     type="text"
                     style="color: green"
                     @click="toggle({id:row.id,enabled:false})">
            已启用
          </el-button>
          <el-button v-else
                     title="点击以启用该应用"
                     type="text"
                     style="color: red"
                     @click="toggle({id:row.id,enabled:true})">
            已禁用
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100px" fixed="right">
        <template #default="{row}">
          <el-button type="text" @click="$router.push({name:'client',params:{id:row.id}})">编辑</el-button>
          <el-button type="text" style="color: red" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </ele-data-tables>
  </div>
</template>

<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator'
  import urls from '../URLS'
  import { ClientDto, ClientType } from '@/types/service'
  import http from '@/http'

  const ClientTypes = {
    [ClientType.CLIENT_CONFIDENTIAL]: '机密客户端',
    [ClientType.CLIENT_PUBLIC]: '公共客户端',
    [ClientType.CLIENT_RESOURCE]: '资源服务器'
  }

  @Component({
    filters: {
      toClientType (v: ClientType): string {
        return ClientTypes[v]
      }
    }
  })
  export default class Clients extends Vue {
    clientUrl = urls.client

    serverParams = {
      keyword: ''
    }

    del ({ id }: ClientDto): Promise<unknown> {
      return this.$confirm('此操作将永久删除该客户端, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${urls.client}/${id}`))
        .then(() => this.$message({
          type: 'success',
          message: '删除成功!'
        }))
        .then(() => (this.$refs.table as any).reloadData())
        .catch((e) => {
          if (e === 'cancel') {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          }
        })
    }

    /**
     * 变更client的状态
     * @param id
     * @param enabled
     */
    toggle ({
      id,
      enabled
    }: ClientDto): Promise<unknown> {
      return this.$confirm(`你确定${enabled ? '启用' : '停用'}该客户端吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.patch(`${urls.client}/${id}`, { enabled }))
        .then(() => (this.$refs.table as any).reloadData())
        .catch((e) => {
          if (e === 'cancel') {
            this.$message({
              type: 'info',
              message: '操作已取消'
            })
          }
        })
    }
  }
</script>

<style scoped>

</style>
