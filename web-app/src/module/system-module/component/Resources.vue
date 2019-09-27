<template>
  <el-container class="resource">
    <el-main>
      <el-form>
        <el-row>
          <el-col :span="6">
            <el-form-item class="pull-left">
              <el-input
                v-model="searchObj.search"
                placeholder="请输入关键字"/>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item class="pull-right">
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
        <el-table-column label="资源名称" prop="name"/>
        <el-table-column label="匹配路径" prop="matcher"/>
        <el-table-column label="资源描述" prop="description"/>
        <el-table-column label="操作" min-width="100">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
            <!--<el-button type="text" size="small" @click="setmenu(scope.row)">设置菜单权限</el-button>-->
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
  import { namespace } from 'vuex-class'
  import EleDataTables from 'element-datatables'

  const resourceModule = namespace('system/resource')
  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Resources extends Vue {
    searchObj = {}
    @resourceModule.State('url')
    ajax

    @resourceModule.Action('delete')
    delete

    del ({ id }) {
      this.$confirm('此操作将永久删除该资源，是否继续?', '删除资源', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return this.delete(id)
      }).then(() => {
        this.$refs['list'].reloadData()
      }).catch(e => {
      })
    }

    add () {
      this.$router.push({ name: 'resource', params: { id: 'new' } })
    }

    edit ({ id }) {
      this.$router.push({ name: 'resource', params: { id } })
    }
  }
</script>

<style lang="less">
</style>
