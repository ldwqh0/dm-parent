<template>
  <el-container>
    <el-aside>
      <el-form size="mini">
        <el-form-item>
          <el-input placeholder="请输入搜索关键字"/>
        </el-form-item>
      </el-form>
      <el-tabs v-model="filter">
        <el-tab-pane label="组织机构" name="department">
          <el-tree
            default-expand-all
            :expand-on-click-node="false"
            :props="departmentTreeProps"
            :data="departmentTree"
            @node-click="({id})=> currentNode=id">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <a @click="editDepartment(data)">编辑</a>
                <a @click="deleteDepartment(data)">删除</a>
              </span>
            </span>
          </el-tree>
        </el-tab-pane>
        <el-tab-pane label="角色" name="role">
          <el-tree
            :expand-on-click-node="false"
            :data="roleTree"
            :props="roleTreeProps"
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
                  v-model="userSearchKey"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="pull-right">
                <el-button type="primary" @click="editDepartment()">新增组织机构</el-button>
                <el-button type="primary" @click="editRole()">新增角色</el-button>
                <el-button type="primary" @click="editUser()">新增用户</el-button>
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
                <el-button type="text" size="small" @click="editUser(scope.row)">编辑</el-button>
                <el-button type="text" size="small" @click="toggleEnabled(scope.row)">
                  {{ scope.row.enabled ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </ele-data-tables>
        </el-main>
      </el-header>
    </el-container>

    <el-dialog title="用户编辑"
               :visible.sync="userDialogVisible"
               v-if="userDialogVisible">
      <user :id="currentUser" @complete="(userDialogVisible=false) || reloadUser()"/>
    </el-dialog>
    <el-dialog title="组织机构编辑"
               :visible.sync="departmentDialogVisible"
               v-if="departmentDialogVisible">
      <department :id="currentDepartment"
                  @complete="(departmentDialogVisible=false) || loadDepartments()"/>
    </el-dialog>
    <el-dialog title="角色编辑"
               :visible.sync="roleDialogVisible"
               v-if="roleDialogVisible">
      <role :id="currentRole"
            @complete="(roleDialogVisible=false) || loadRoles()"/>
    </el-dialog>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import User from '../component/User'
  import Department from '../component/Department'
  import Role from '../component/Role'
  // import EleDataTables from 'element-datatables'

  import { namespace } from 'vuex-class'

  const userModule = namespace('system/user')
  const roleModule = namespace('system/role')
  const departmentModule = namespace('system/department')

  @Component({
    components: {
      User,
      Department,
      Role
    }
  })
  export default class Users extends Vue {
    filter = 'department' // 过滤类型
    userSearchKey = '' // 用户搜索的关键字
    userDialogVisible = false // 用户对话框是否可见
    departmentDialogVisible = false
    roleDialogVisible = false
    currentUser = 'new' // 显示当前用户
    currentDepartment = 'new' // 当前组织机构
    currentRole = 'new'
    currentNode = '' // 当前选中的节点

    get departmentTreeProps () {
      return {
        children: 'children',
        label: 'name'
      }
    }

    get roleTreeProps () {
      return {
        children: 'children',
        label: 'name'
      }
    }

    get searchObj () {
      return {
        search: this.userSearchKey,
        [`${this.filter}`]: this.currentNode
      }
    }

    @userModule.State('url')
    ajax

    @userModule.Action('update')
    update

    @roleModule.Getter('roleTree')
    roleTree

    @roleModule.Action('listAll')
    loadRoles

    // 获取部门树
    @departmentModule.Getter('departments')
    departmentTree

    // 加载部门树
    @departmentModule.Action('loadDepartments')
    loadDepartments

    // 删除部门
    @departmentModule.Action('del')
    delDepartment

    // 新增
    editUser ({ id = 'new' } = { id: 'new' }) {
      this.currentUser = id
      this.userDialogVisible = true
      // this.$router.push({ name: 'user', params: { id: 'new' } })
    }

    editDepartment ({ id = 'new' } = { id: 'new' }) {
      this.currentDepartment = id
      this.departmentDialogVisible = true
    }

    editRole ({ id = 'new' } = { id: 'new' }) {
      this.currentRole = id
      this.roleDialogVisible = true
    }

    deleteDepartment ({ id }) {
      this.delDepartment({ id }).then(() => {
        this.loadDepartments()
      })
    }

    nodeChange ({ id }, b, c) {
      this.currentNode = id
      // debugger
      // this.searchObj.department = data.id
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
      this.loadRoles()
      this.loadDepartments()
    }

    reloadUser () {
      this.$refs['list'].reloadData()
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

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
</style>
