<template>
  <div class="resource">
    <el-form inline :model="searchObj">
      <el-row>
        <el-col :span="12">
          <el-form-item>
            <el-input
              v-model="searchObj.search"
              placeholder="请输入关键字" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item class="float-right">
            <el-button type="primary" @click="add()">新增</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <ele-data-tables ref="list"
                     :ajax="ajax"
                     pagination-layout="total, sizes, prev, pager, next, jumper"
                     :server-params="searchObj"
                     border
                     stripe>
      <el-table-column label="资源名称" prop="name" />
      <el-table-column label="匹配路径" prop="matcher" />
      <el-table-column label="资源描述" prop="description" />
      <el-table-column label="操作" min-width="100">
        <template #default="scope">
          <el-button type="text" size="small" @click="edit(scope.row)">编辑</el-button>
          <!--<el-button type="text" size="small" @click="setmenu(scope.row)">设置菜单权限</el-button>-->
          <el-button type="text" size="small" @click="del(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </ele-data-tables>
    <el-dialog v-if="dialogVisible"
               :visible.sync="dialogVisible"
               title="资源编辑"
               :close-on-click-modal="false"
               :close-on-press-escape="false">
      <resource :id="currentAuthority.id" ref="resource" />
      <template #footer>
        <el-button type="primary" :loading="submitting" @click="saveResource">确定</el-button>
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component } from 'vue-property-decorator'
  import Resource from './Resource.vue'
  import URLS from '../URLS'
  import http from '@/http'
  import { ResourceDto } from '@/types/service'

  @Component({
    components: {
      Resource
    }
  })
  export default class Resources extends Vue {
    searchObj = {}
    ajax = URLS.resource
    submitting = false

    dialogVisible = false

    currentAuthority = {}

    del ({ id }: { id: number }): Promise<any> {
      return this.$confirm('此操作将永久删除该资源，是否继续?', '删除资源', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => http.delete(`${URLS.resource}/${id}`))
        .then(() => (this.$refs.list as any).reloadData())
        .catch(e => {
          if (e === 'cancel') {
            this.$message.info('已取消删除')
          }
        })
    }

    add (): void {
      this.currentAuthority = {}
      this.dialogVisible = true
    }

    edit (data: ResourceDto): void {
      this.currentAuthority = data
      this.dialogVisible = true
    }

    saveResource (): Promise<any> {
      this.submitting = true
      return (this.$refs.resource as any).submit()
        .then(() => (this.$refs.list as any).reloadData())
        .then(() => (this.dialogVisible = false))
        .finally(() => (this.submitting = false))
    }
  }
</script>

<style lang="less">
</style>
