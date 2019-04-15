<template>
  <el-container>
    <el-aside>
      <el-form size="mini">
        <el-form-item>
          <el-input placeholder="请输入搜索关键字"/>
        </el-form-item>
      </el-form>
      <el-tabs>
        <el-tab-pane label="组织机构">
          <el-tree
            :data="departmentTree"/>
        </el-tab-pane>
        <el-tab-pane label="角色">
          <el-tree
            :data="roleTree"
            :props="defaultTreeProps"
            default-expand-all/>
        </el-tab-pane>
      </el-tabs>
    </el-aside>
    <el-container>
      <el-header>
        <el-form size="mini"
                 :inline="true"
                 :model="searchObj"
                 class="clear-float">
          <el-row>
            <el-col :span="12">
              <el-form-item class="pull-left">
                <el-input
                  placeholder="请输入关键字"
                  v-model="searchObj.search"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="pull-right">
                <el-button type="primary" @click="add()">新增</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-main>
          <ele-data-tables :ajax="ajax"
                           ref="list"
                           pagination-layout="total, sizes, prev, pager, next, jumper"
                           :server-params="searchObj">
            <!--<el-table-column label="序号" type="generator-vue.js">-->
            <!--<template slot-scope="scope">{{ scope.$generator-vue.js + 1 }}</template>-->
            <!--</el-table-column>-->
            <el-table-column label="登录名" prop="username" sortable="custom"/>
            <el-table-column label="用户名全名" prop="fullname" sortable="custom"/>
            <!--<el-table-column label="组织机构" prop=""/>-->
            <!--<el-table-column label="角色" prop="username"/>-->
            <el-table-column label="状态" prop="enabled" sortable="custom">
              <template slot-scope="scope">{{ scope.row.enabled ? '启用' : '禁用' }}</template>
            </el-table-column>
            <el-table-column label="操作" :min-width="60">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
                <el-button type="text" size="small" @click="toggleEnabled(scope.row)">
                  {{ scope.row.enabled ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </ele-data-tables>
        </el-main>
      </el-header>
    </el-container>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  // import EleDataTables from 'element-datatables'

  import { namespace } from 'vuex-class'

  const userSpace = namespace('system/user')
  const roleModule = namespace('system/role')
  const departmentModule = namespace('system/department')

  @Component({
    // components: {
    //   EleDataTables
    // }
  })
  export default class Users extends Vue {
    get defaultProps () {
      return {
        children: 'children',
        label: 'text'
      }
    }

    get defaultTreeProps () {
      return {
        children: 'children',
        label: 'name'
      }
    }

    searchObj = {
      search: null
    }

    @roleModule.Getter('roleTree')
    roleTree

    @userSpace.State('url')
    ajax

    @userSpace.Action('update')
    update

    @roleModule.Action('listAll')
    listRoles

    @departmentModule.Action('loadDepartments')
    loadDepartments

    // 新增
    add () {
      this.$router.push({ name: 'user', params: { id: 'new' } })
    }

    edit (params) {
      this.$router.push({ name: 'user', params })
    }

    nodeChange (data) {
      this.searchObj.department = data.id
    }

    // 启用、禁用
    toggleEnabled (row) {
      row.enabled = !row.enabled
      this.update(row, { id: row.id }).then(res => {
        this.$message({
          message: row.enabled ? '启用成功！' : '禁用成功！',
          type: 'success'
        })
      }).catch(() => {
        this.$message({
          message: row.enabled ? '启用失败！' : '禁用失败！',
          type: 'error'
        })
      })
    }

    created () {
      this.listRoles()
    }
  }
</script>

<style lang="less" scoped>
  /*.el-aside {*/
  /*width: 250px !important;*/
  /*.el-card {*/
  /*height: 99%;*/
  /*}*/
  /*}*/
</style>
