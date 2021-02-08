<template>
  <div class="roles">
    <el-form :inline="true"
             :model="searchObj">
      <el-row>
        <el-col :span="12">
          <el-form-item class="pull-left">
            <el-input
              v-model="searchObj.keyword"
              placeholder="请输入关键字" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="pull-right" style="display: flex;justify-content: flex-end">
            <el-button type="primary" @click="edit({})">新增</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <ele-data-tables ref="table"
                     :ajax="url"
                     pagination-layout="total, sizes, prev, pager, next, jumper"
                     :server-params="searchObj"
                     border
                     stripe>
      <el-table-column label="角色名称" prop="name" />
      <el-table-column label="角色组" prop="group" />
      <el-table-column label="状态" prop="state">
        <template slot-scope="scope">
          <span v-if="scope.row.state==='ENABLED'">启用</span>
          <span v-if="scope.row.state==='DISABLED'">禁用</span>
        </template>
      </el-table-column>
      <el-table-column label="角色描述" prop="describe" />
      <el-table-column label="操作" min-width="150">
        <template #default="{row}">
          <el-button type="text" size="small" @click="edit(row)">编辑</el-button>
          <el-button type="text" size="small" @click="setmenu(row)">设置菜单权限</el-button>
          <el-button type="text" size="small" @click="setResource(row)">设置资源权限</el-button>
          <el-button v-if="row.id>>3"
                     type="text"
                     size="small"
                     @click="del(row)">
            删除{{ row.id }}
          </el-button>
        </template>
      </el-table-column>
    </ele-data-tables>

    <el-dialog v-if="editVisible" :visible.sync="editVisible" title="角色编辑">
      <role :id="current.id" ref="role" />
      <template #footer>
        <el-button type="primary" :loading="submitting" @click="saveRole">确定</el-button>
        <el-button type="danger" @click="editVisible=false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { RoleDto } from '@/types/service'
  import urls from '../URLS'
  import Role from './Role.vue'
  import http from '@/http'

  // 角色模块的vuex
  @Component({
    components: { Role }
  })
  export default class Roles extends Vue {
    url = urls.role
    searchObj = {
      keyword: null
    }

    submitting = false
    editVisible = false

    current: RoleDto = {}

    // 编辑
    edit (role: RoleDto): void {
      this.current = role
      this.editVisible = true
    }

    // 删除
    del ({ id }: RoleDto): Promise<unknown> {
      return this.$confirm('此操作将永久删除该角色, 删除角色可能导致系统不正常的结果，是否继续?', '删除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${urls.role}/${id}`))
        .then(() => (this.$refs.table as any).reloadData())
        .catch(e => {
          console.error(e)
        })
    }

    saveRole (): void {
      this.submitting = true;
      (this.$refs.role as any).submit().then(() => {
        this.editVisible = false;
        (this.$refs.table as any).reloadData()
      }).finally(() => {
        this.submitting = false
      })
    }

    // 设置菜单权限
    setmenu (params) {
      this.$router.push({ name: 'menuAuthority', params })
    }

    setResource (params) {
      this.$router.push({ name: 'resourceAuthority', params })
    }
  }
</script>

<style lang="less" scoped>

</style>
