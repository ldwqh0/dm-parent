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
          <el-col :span="12" :ref="'ab'">
            <el-form-item class="pull-right">
              <el-button type="primary" @click="goto({ name: 'roleGroup', params: { id: 'new' } })">新增角色组</el-button>
            </el-form-item>
            <el-form-item class="pull-right">
              <el-button type="primary" @click="goto({ name: 'role', params: { id: 'new' } })">新增角色</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-tabs>
        <el-tab-pane :label="roleGroup.name" v-for="(roleGroup,tabIndex) in roleGroups" :key="roleGroup.id">
          <!--如何处理组角色组编辑的问题-->
          <template #label>
            {{ roleGroup.name }}
          </template>
          <ele-data-tables :ajax="ajax"
                           ref="list"
                           pagination-layout="total, sizes, prev, pager, next, jumper"
                           :server-params="{...searchObj,groupId:roleGroup.id}"
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
                <el-button type="text" size="small" @click="goto({name:'role',params:scope.row})">编辑</el-button>
                <el-button type="text" size="small" @click="setmenu(scope.row)">设置菜单权限</el-button>
                <el-button type="text" size="small" @click="setResource(scope.row)">设置资源权限</el-button>
                <el-button type="text" size="small" @click="del(scope.row,tabIndex)">删除</el-button>
              </template>
            </el-table-column>
          </ele-data-tables>
        </el-tab-pane>
      </el-tabs>
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
  const roleGroupModule = namespace('system/roleGroup')
  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Role extends Vue {
    @roleModule.State('url')
    ajax

    @roleModule.Action('delete')
    delete

    @roleGroupModule.Action('listAll')
    listGroups

    @roleGroupModule.State('roleGroups')
    roleGroups

    searchObj = {
      search: null
    }

    // 新增
    goto (target) {
      this.$router.push(target)
    }

    /**
     * 删除一个指定角色
     * @param id
     * 要删除的角色
     * @param tabIndex
     * tab索引，用户在用户删除之后，刷新指定的表格内容
     */
    del ({ id }, tabIndex) {
      this.$confirm('此操作将永久删除该角色, 删除角色可能导致系统不正常的结果，是否继续?', '删除角色', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return this.delete({ id })
      }).then(() => {
        this.$refs['list'][tabIndex].reloadData()
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

    created () {
      this.listGroups()
    }
  }
</script>

<style lang="less" scoped>

</style>
