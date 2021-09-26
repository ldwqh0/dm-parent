<template>
  <div class="roles">
    <el-form :inline="true" :model="searchObj">
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
      <el-table-column label="角色描述" prop="description" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <div>
            <el-button v-if="row.id>3"
                       type="text"
                       size="small"
                       @click="edit(row)">
              编辑
            </el-button>
            <el-button type="text" size="small" @click="setmenu(row)">设置菜单权限</el-button>
            <el-button v-if="row.id>3"
                       type="text"
                       size="small"
                       @click="del(row)">
              删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </ele-data-tables>

    <el-dialog v-if="editVisible" :visible.sync="editVisible" title="角色编辑">
      <role :id="current.id" ref="role" v-loading="loading" />
      <template #footer>
        <el-button @click="editVisible=false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveRole">确定</el-button>
      </template>
    </el-dialog>

    <!--菜单权限-->
    <el-dialog v-if="menuAuthorityDialogVisible"
               :title="`${current.group}_${current.name}的菜单权限`"
               :close-on-click-modal="false"
               :visible.sync="menuAuthorityDialogVisible">
      <menu-authority ref="menuAuthority"
                      v-loading="loading"
                      :role-id="current.id" />
      <div slot="footer">
        <el-button @click="menuAuthorityDialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveMenuAuthority">确定</el-button>
      </div>
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
  import MenuAuthority from './MenuAuthority.vue'
  import ResourceAuthority from './ResourceAuthority.vue'
  import { namespace } from 'vuex-class'

  const httpModel = namespace('http')

  // 角色模块的vuex
  @Component({
    components: {
      Role,
      MenuAuthority,
      ResourceAuthority
    }
  })
  export default class Roles extends Vue {
    url = urls.role
    searchObj = {
      keyword: null
    }

    @httpModel.Getter('loading')
    loading!: boolean

    editVisible = false
    menuAuthorityDialogVisible = false

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
        .catch(e => console.error(e))
    }

    saveRole (): void {
      (this.$refs.role as any).submit().then(() => {
        this.editVisible = false;
        (this.$refs.table as any).reloadData()
      })
    }

    saveMenuAuthority (): void {
      (this.$refs.menuAuthority as any).submit()
        .then(() => (this.menuAuthorityDialogVisible = false))
    }

    // 设置菜单权限
    setmenu (params: RoleDto): void {
      this.menuAuthorityDialogVisible = true
      this.current = params
    }
  }
</script>

<style lang="less" scoped>

</style>
