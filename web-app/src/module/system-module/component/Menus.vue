<template>
  <el-container class="menus">
    <el-aside>
      <el-card shadow="never">
        <div slot="header" class="clearfix">
          <el-tooltip content="显示所有菜单树">
            <el-button type="text" @click="showRoot">菜单树</el-button>
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
               :inline="true"
               :model="search"
               class="clear-float">
        <el-row>
          <el-col :span="12">
            <el-form-item class="pull-left">
              <el-input
                placeholder="请输入关键字"
                v-model="search.search"/>
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
                       :server-params="search"
                       pagination-layout="total, sizes, prev, pager, next, jumper"
                       ref="table">
        <el-table-column prop="name" label="菜单名称"/>
        <el-table-column prop="title" label="菜单标题"/>
        <el-table-column label="菜单状态">
          <template slot-scope="scope"><span>{{ scope.row.enabled?'启用':'禁用' }}</span></template>
        </el-table-column>
        <el-table-column prop="id" label="操作" :min-width="120">
          <span slot-scope="scope">
            <el-button v-if="scope.row.enabled"
                       type="text"

                       @click="patch({id:scope.row.id,enabled:false})">
              禁用
            </el-button>
            <el-button v-if="!scope.row.enabled"
                       type="text"

                       @click="patch({id:scope.row.id,enabled:true})">
              启用
            </el-button>
            <el-button type="text" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" @click="move({id:scope.row.id,position:'UP'})">上移</el-button>
            <el-button type="text" @click="move({id:scope.row.id,position:'DOWN'})">下移</el-button>
            <el-button type="text" @click="del(scope.row)">删除</el-button>
          </span>
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

  const menuModule = namespace('system/menu')
  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Menus extends Vue {
    get treeProp () {
      return { // 树形机构显示熟属性
        children: 'children',
        label: 'title'
      }
    }

    @menuModule.Getter('tree')
    tree

    @menuModule.State('url')
    ajax

    @menuModule.Action('listAll')
    loadAll

    @menuModule.Action('move')
    moveMenu

    @menuModule.Action('patch')
    patchMenu

    @menuModule.Action('delete')
    delete

    search = {
      parentId: null,
      search: null
    }

    add () {
      this.$router.push({ name: 'menu', params: { id: 'new' } })
    }

    created () {
      this.loadAll()
    }

    // loadAll () {
    //   this.tree().then(datas => {
    //     this.tree = datas
    //   })
    // }

    edit ({ id }) {
      this.$router.push({ name: 'menu', params: { id } })
    }

    move (position) {
      this.moveMenu(position).then(res => {
        this.$message({
          message: '修改成功！',
          type: 'success'
        })
        this.$refs.table.reloadData()
      })
    }

    patch (data) {
      this.patchMenu(data).then(res => {
        this.$refs.table.reloadData()
      })
    }

    showRoot () {
      this.search.parentId = null
    }

    del (data) {
      this.$confirm('此操作将永久删除该菜单, 删除菜单可能导致系统不正常的结果，是否继续?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return this.delete(data)
      }).then(() => {
        this.$refs['table'].reloadData()
      }).catch(e => {
        console.error(e)
      })
    }

    nodeChange (data) {
      this.search.parentId = data.id
    }
  }
</script>

<style lang="less">
</style>
