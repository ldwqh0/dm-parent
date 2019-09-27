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
              <el-button type="primary" @click="add">新增</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <ele-data-tables :ajax="ajax"
                       ref="list"
                       pagination-layout="total, sizes, prev, pager, next, jumper"
                       :server-params="searchObj">
        <el-table-column label="应用名称" prop="name"/>
        <el-table-column label="应用ID" prop="clientId"/>
        <el-table-column label="编辑">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="editToken(scope.row)">Token管理</el-button>
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

  const clientScope = namespace('system/client')
  @Component({
    components: {
      EleDataTables
    }
  })
  export default class Clients extends Vue {
    searchObj = {}

    @clientScope.State('url')
    ajax

    edit ({ clientId: id }) {
      this.$router.push({ name: 'client', params: { id } })
    }

    editToken ({ clientId: client }) {
      this.$router.push({ name: 'tokens', params: { client } })
    }

    add () {
      this.$router.push({ name: 'client', params: { id: 'new' } })
    }
  }
</script>

<style scoped>

</style>
