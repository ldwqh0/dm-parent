<template>
  <el-container class="users">
    <el-aside>
      <el-form>
        <el-form-item>
          <el-input placeholder="请输入搜索关键字" v-model="treeFilterKeyWords"/>
        </el-form-item>
      </el-form>
      <el-tabs v-model="active" @tab-click="switchToTab">
        <el-tab-pane label="组织机构" name="department">
          <el-tree
            default-expand-all
            :expand-on-click-node="false"
            :props="departmentTreeProps"
            :data="departmentTree"
            :filter-node-method="filterNodes"
            ref="departmentTree"
            @node-click="({id})=> currentNode=id">
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <el-dropdown class="node-operation">
                <span>操作<i class="el-icon-arrow-down el-icon--right"/></span>
                <el-dropdown-menu>
                  <el-dropdown-item @click.native="editDepartment(data)">编辑</el-dropdown-item>
                  <el-dropdown-item @click.native="deleteDepartment(data)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </el-tree>
        </el-tab-pane>
        <el-tab-pane label="角色" name="role">
          <el-tree
            :expand-on-click-node="false"
            :data="roleTree"
            :props="roleTreeProps"
            @node-click="switchRoleNode"
            :filter-node-method="filterNodes"
            ref="roleTree"
            default-expand-all>
            <span class="custom-tree-node" slot-scope="{node,data}">
              <span>{{ node.label }}</span>
              <el-dropdown class="node-operation" v-if="data.type==='role'">
                <span>操作<i class="el-icon-arrow-down el-icon--right"/></span>
                <el-dropdown-menu>
                  <el-dropdown-item @click.native="editRole(data)">编辑</el-dropdown-item>
                  <el-dropdown-item @click.native="editMenuAuthority(data)">菜单权限</el-dropdown-item>
                  <el-dropdown-item @click.native="editResourceAuthority(data)">资源权限</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </span>
          </el-tree>
        </el-tab-pane>
      </el-tabs>
    </el-aside>
    <el-container>
      <el-main style="padding: 0;">
        <el-row>
          <el-col :span="24">
            <el-form :inline="true"
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
                    <el-button type="primary" @click="editDepartment()">新增部门</el-button>
                    <el-button type="primary" @click="editRole()">新增角色</el-button>
                    <!--                <el-button type="primary" @click="doSyncDepartments()">同步组织机构</el-button>-->
                    <!--                <el-button type="primary" @click="doSyncRoles()">同步角色</el-button>-->
                    <el-button type="primary" @click="editUser()">新增用户</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
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
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <el-dialog title="用户编辑"
               :visible.sync="userDialogVisible"
               v-if="userDialogVisible">
      <user ref="user"
            :id="currentUser"/>
      <span slot="footer">
        <el-button @click="userDialogVisible=false">取消</el-button>
        <el-button @click="saveUser" type="primary">确定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="组织机构编辑"
               :visible.sync="departmentDialogVisible"
               v-if="departmentDialogVisible">
      <department :id="currentDepartment"
                  ref="department"/>
      <span slot="footer">
        <el-button @click="departmentDialogVisible=false">取消</el-button>
        <el-button @click="saveDepartment" type="primary">确定</el-button>
      </span>
    </el-dialog>
    <!--角色编辑-->
    <el-dialog title="角色编辑"
               :visible.sync="roleDialogVisible"
               v-if="roleDialogVisible">
      <role :id="currentRole"
            ref="role"/>
      <template slot="footer">
        <el-button @click="roleDialogVisible=false">取消</el-button>
        <el-button @click="saveRole" type="primary">确定</el-button>
      </template>
    </el-dialog>

    <!--菜单权限-->
    <el-dialog :title="`${currentAuthority.group.name}_${currentAuthority.name}的菜单权限`"
               v-if="menuAuthorityDialogVisible"
               :visible.sync="menuAuthorityDialogVisible">
      <menu-authority ref="menu-authority"
                      :name="`${currentAuthority.group.name}_${currentAuthority.name}`"/>
      <div slot="footer">
        <el-button @click="menuAuthorityDialogVisible=false">取消</el-button>
        <el-button @click="saveMenuAuthority" type="primary">确定</el-button>
      </div>
    </el-dialog>

    <!--资源权限-->
    <el-dialog :title="`${currentAuthority.group.name}_${currentAuthority.name}的资源权限`"
               v-if="resourceAuthorityDialogVisible"
               :visible.sync="resourceAuthorityDialogVisible">
      <resource-authority ref="resource-authority"
                          :name="`${currentAuthority.group.name}_${currentAuthority.name}`"/>
      <div slot="footer">
        <el-button @click="resourceAuthorityDialogVisible=false">取消</el-button>
        <el-button @click="saveResourceAuthority" type="primary">确定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { namespace } from 'vuex-class'
  import { Component, Watch } from 'vue-property-decorator'
  import User from './User'
  import Department from './Department'
  import Role from './Role'
  import MenuAuthority from './MenuAuthority'
  import ResourceAuthority from './ResourceAuthority'

  const userModule = namespace('system/user')
  const roleModule = namespace('system/role')
  const departmentModule = namespace('system/department')

  @Component({
    components: {
      ResourceAuthority,
      MenuAuthority,
      User,
      Department,
      Role
    }
  })
  export default class Users extends Vue {
    active = 'department' // 当前激活的选项卡
    filterType = 'department' // 过滤类型
    userSearchKey = '' // 用户搜索的关键字
    userDialogVisible = false // 用户对话框是否可见
    departmentDialogVisible = false // 部门编辑对话框
    roleDialogVisible = false// 角色编辑对话框
    menuAuthorityDialogVisible = false// 菜单权限对话框
    resourceAuthorityDialogVisible = false // 资源权限对话框
    currentUser = 'new' // 显示当前用户
    currentDepartment = 'new' // 当前组织机构
    currentRole = 'new' // 当前角色
    currentAuthority = {} // 当前授权对象
    currentNode = '' // 当前选中的节点
    treeFilterKeyWords = ''// 过滤树型结构的关键字

    departmentTreeProps = {
      children: 'children',
      label: 'shortname'
    }

    roleTreeProps = {
      children: 'children',
      label: 'name'
    }

    get searchObj () {
      return {
        search: this.userSearchKey,
        [`${this.filterType}`]: this.currentNode
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
    @departmentModule.Getter('tree')
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

    editMenuAuthority (data) {
      this.menuAuthorityDialogVisible = true
      this.currentAuthority = data
    }

    editResourceAuthority (data) {
      this.resourceAuthorityDialogVisible = true
      this.currentAuthority = data
    }

    deleteDepartment ({ id }) {
      this.$confirm('是否永久删除该部门?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.delDepartment({ id }).then(() => {
          this.loadDepartments()
        })
      })
    }

    /**
     * 将用户过滤的条件切换到按角色过滤
     * @param id
     */
    switchRoleNode ({ id }) {
      if (typeof (id) === 'string' && id.startsWith('group')) {
        this.filterType = 'roleGroup'
        this.currentNode = id.split('-')[1]
      } else {
        this.filterType = 'role'
        this.currentNode = id
      }
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

    /**
     * 定义如何过滤树型结构的内容
     * @param value
     * @param data
     * @returns {boolean}
     */
    filterNodes (value, data) {
      return data.name.indexOf(value) !== -1
    }

    /**
     * 监控关键字的改变，实现过滤
     * @param val
     */
    @Watch('treeFilterKeyWords')
    filterTree (val) {
      this.$refs.departmentTree.filter(val)
      this.$refs.roleTree.filter(val)
    }

    /**
     * 切换tab页面
     * @param name
     */
    switchToTab ({ name }) {
      // 更新用户表格过滤类型
      this.filterType = name
      // 重置关键字
      this.treeFilterKeyWords = ''
    }

    saveUser () {
      this.$refs.user.submit().then((res) => {
        this.userDialogVisible = false
        this.$refs.list.reloadData()
      })
    }

    saveDepartment () {
      this.$refs.department.submit().then(() => {
        this.departmentDialogVisible = false
        this.loadDepartments()
      })
    }

    saveRole () {
      this.$refs.role.submit().then(({ data }) => {
        this.roleDialogVisible = false
      })
    }

    saveMenuAuthority () {
      this.$refs['menu-authority'].submit().then(() => {
        this.menuAuthorityDialogVisible = false
      })
    }

    saveResourceAuthority () {
      this.$refs['resource-authority'].submit().then(() => {
        this.resourceAuthorityDialogVisible = false
      })
    }
  }
</script>

<style lang="less" scoped>
  .users {
    .custom-tree-node {
      width: 100%;

      .node-operation {
        float: right;
        color: rgba(255, 255, 255, 0);
      }

      &:hover {
        .node-operation {
          color: rgba(0, 0, 0);
        }
      }
    }
  }
</style>
