<template>
  <el-container>
    <el-aside>
      <el-card shadow="never">
        <div slot="header" class="clearfix">
          <el-tooltip content="显示所有组织机构">
            <el-button type="text" @click="showRoot">组织机构</el-button>
          </el-tooltip>
        </div>
        <el-tree
          @current-change="nodeChange"
          :default-expand-all="true"
          :expand-on-click-node="false"
          :props="treeProp"
          :data="tree"/>
      </el-card>
    </el-aside>
    <el-main>
      <el-form size="mini"
               :model="search"
               :inline="true"
               class="clear-float">
        <div class="pull-left">
          <el-form-item>
            <el-button type="success" @click="add()">新增</el-button>
          </el-form-item>
        </div>
        <div class="pull-right">
          <el-form-item>
            <el-input
              v-model="search.search"

              placeholder="输入关键字查询"/>
          </el-form-item>
        </div>
      </el-form>
      <ele-data-tables :ajax="tableUrl"
                       :server-params="search"
                       pagination-layout="total, sizes, prev, pager, next, jumper"
                       ref="table">
        <el-table-column prop="fullname" label="部门名称"/>
        <!--<el-table-column prop="fullname" label="上级目录"/>-->
        <!--<el-table-column prop="fullname" label="子部门个数"/>-->
        <el-table-column label="状态">
          <span slot-scope="scope">
            <span v-if="scope.row.state==='ENABLED'">已启用</span>
            <span v-if="scope.row.state==='DISABLED'">已禁用</span>
          </span>
        </el-table-column>
        <!--<el-table-column prop="fullname" label="子部门个数"/>-->
        <el-table-column prop="id" label="操作">
          <span slot-scope="scope">
            <el-button type="text" @click="toggleState(scope.row)">{{ scope.row.state === 'ENABLED' ? '禁用' : '启用' }}</el-button>
            <el-button type="text" @click="edit(scope.row.id)">编辑</el-button>
            <el-button type="text" @click="up(scope.row.id)">上移</el-button>
            <el-button type="text" @click="down(scope.row.id)">下移</el-button>
          </span>
        </el-table-column>
      </ele-data-tables>
    </el-main>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import { DepartmentService, SystemUrlConfigService as UrlConfigService } from '../service'
  import EleDataTables from 'element-datatables'
  // import { namespace } from 'vuex-class'

  // const SystemModule=namespace()
  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Departments extends Vue {
    search = {
      parentId: null
    }
    tree = [] // 树形结构
    get treeProp () {
      return { // 树形机构显示熟属性
        children: 'children',
        label: 'text'
      }
    }

    tableUrl = UrlConfigService.system.departments

    created () {
      this.loadTree()// 加载时载入树形结构
    }

    loadTree () { // 载入树形结构
      DepartmentService.tree().then(datas => {
        this.tree = datas
      })
    }

    nodeChange (data, node) { // 当树形结构选择项改变时,更新表格搜索参数
      this.search.parentId = data.id
    }

    showRoot () {
      this.search.parentId = null
    }

    up (id) { // 部门上移
      DepartmentService.up(id).then(() => {
        this.loadTree()
        this.$refs.table.reloadData()
      })
    }

    down (id) { // 部门下移
      DepartmentService.down(id).then(() => {
        this.loadTree()
        this.$refs.table.reloadData()
      })
    }

    add () {
      this.$router.push({ name: 'department', params: { id: 'new' } })
    }

    edit (id) {
      this.$router.push({ name: 'department', params: { id } })
    }

    // 启用/禁用
    toggleState (row) {
      if (row.state === 'ENABLED') { // 禁用
        row.state = 'DISABLED'
      } else if (row.state === 'DISABLED') { // 启用
        row.state = 'ENABLED'
      }
      DepartmentService.update(row, { id: row.id }).then(res => {
        this.$message({
          message: '修改成功！',
          type: 'success'
        })
      }).catch(e => {
        this.$message({
          message: '修改失败！',
          type: 'success'
        })
      })
    }
  }
</script>

<style lang="less" scoped>
  .el-aside {
    width: 250px !important;
    .el-card {
      height: 99%;
    }
  }
</style>
