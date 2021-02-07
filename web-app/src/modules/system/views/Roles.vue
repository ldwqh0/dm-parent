<template>
  <el-container>
    <el-main>
      <el-form size="mini"
               :inline="true"
               :model="searchObj"
               class="clear-float">
        <el-row>
          <el-col :span="12">
            <el-form-item class="pull-left">
              <el-input
                v-model="searchObj.search"
                placeholder="请输入关键字"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="pull-right" style="display: flex;justify-content: flex-end">
              <el-button type="primary" @click="add()">新增</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <ele-data-tables :ajax="ajax"
                       ref="list"
                       pagination-layout="total, sizes, prev, pager, next, jumper"
                       :server-params="searchObj"
                       border
                       stripe>
        <el-table-column label="角色名称" prop="name"/>
        <el-table-column label="状态" prop="state">
          <template slot-scope="scope">
            <span v-if="scope.row.state==='ENABLED'">启用</span>
            <span v-if="scope.row.state==='DISABLED'">禁用</span>
          </template>
        </el-table-column>
        <el-table-column label="角色描述" prop="describe"/>
        <el-table-column label="操作" min-width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="setmenu(scope.row)">设置菜单权限</el-button>
            <el-button type="text" size="small" @click="setResource(scope.row)">设置资源权限</el-button>
            <el-button type="text" size="small" @click="del(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </ele-data-tables>
    </el-main>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import EleDataTables from 'element-datatables'
  import { namespace } from 'vuex-class'

  // 角色模块的vuex
  const roleModule = namespace('system/role')

  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Roles extends Vue {
    @roleModule.State('url')
    ajax

    @roleModule.Action('delete')
    delete

    searchObj = {
      search: null
    }

    // 新增
    add () {
      this.$router.push({ name: 'role', params: { id: 'new' } })
    }

    // 编辑
    edit (params) {
      this.$router.push({ name: 'role', params })
    }

    // 删除
    del (data) {
      this.$confirm('此操作将永久删除该角色, 删除角色可能导致系统不正常的结果，是否继续?', '删除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return this.delete(data)
      }).then(() => {
        this.$refs.list.reloadData()
      }).catch(e => {
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
