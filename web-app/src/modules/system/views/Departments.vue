<template>
  <div class="departments">
    <el-tree ref="tree"
             class="tree"
             :default-expand-all="true"
             :expand-on-click-node="false"
             node-key="id"
             :props="treeProp"
             :data="tree"
             @current-change="nodeChange">
      <template #default="{ node }">
        {{ node.label }}
      </template>
    </el-tree>
    <el-tabs class="tabs">
      <el-tab-pane label="下级部门">
        <el-row>
          <el-col>
            <el-button type="primary">添加子节点</el-button>
          </el-col>
        </el-row>
        <ele-data-tables v-if="search.parentId"
                         ref="table"
                         :ajax="tableUrl"
                         :server-params="search"
                         pagination-layout="total, sizes, prev, pager, next, jumper">
          <el-table-column prop="fullname" label="部门名称" />
          <el-table-column prop="id" label="操作">
            <!--            <span slot-scope="scope">-->
            <!--              <el-button type="text" @click="toggleState(scope.row)">{{-->
            <!--                scope.row.state === 'ENABLED' ? '禁用' : '启用'-->
            <!--              }}</el-button>-->
            <!--              <el-button type="text" @click="edit(scope.row.id)">编辑</el-button>-->
            <el-button type="text" @click="up(scope.row.id)">上移</el-button>
            <el-button type="text" @click="down(scope.row.id)">下移</el-button>
            <!--            </span>-->
          </el-table-column>
        </ele-data-tables>
      </el-tab-pane>
      <el-tab-pane label="部门人员">
        <el-row>
          <el-col>
            <el-button>新用户</el-button>
          </el-col>
        </el-row>
        <ele-data-tables :ajax="userUrl" :server-params="userQuery" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator'
  import { TreeProps } from 'element-ui/types/tree'
  import http from '@/http'
  import urls from '../URLS'
  import { DepartmentDto } from '@/types/service'
  import { listToTree } from '@/utils'

  @Component
  export default class Departments extends Vue {
    search: { parentId: number | null } = {
      parentId: null
    }

    get userQuery () {
      return {
        department: this.search.parentId
      }
    }

    departments: DepartmentDto[] = [] // 树形结构
    root: DepartmentDto = {}

    get tree (): any {
      return listToTree(this.departments)
    }

    get treeProp (): TreeProps {
      return { // 树形机构显示属性
        children: 'children',
        label: 'fullname',
        disabled: '',
        isLeaf: ''
      }
    }

    tableUrl = urls.department
    userUrl = urls.user

    //
    created (): void {
      this.loadTree().then(() => (this.$refs.tree as any).setCurrentKey(this.root.id))// 加载时载入树形结构
    }

    //
    loadTree (): Promise<DepartmentDto[]> { // 载入树形结构
      return http.get<DepartmentDto[]>(`${urls.department}`).then(({ data }) => {
        this.departments = data
        const [root] = this.departments.filter(v => v.parent === undefined)
        this.$set(this, 'root', root)
        this.search.parentId = root.id ?? null
        return data
      })
    }

    nodeChange (data: DepartmentDto): void { // 当树形结构选择项改变时,更新表格搜索参数
      this.search.parentId = data.id ?? null
    }

  //
  //   showRoot () {
  //     this.search.parentId = null
  //   }
  //
  //   up (id) { // 部门上移
  //     DepartmentService.up(id).then(() => {
  //       this.loadTree()
  //       this.$refs.table.reloadData()
  //     })
  //   }
  //
  //   down (id) { // 部门下移
  //     DepartmentService.down(id).then(() => {
  //       this.loadTree()
  //       this.$refs.table.reloadData()
  //     })
  //   }
  //
  //   add () {
  //     this.$router.push({ name: 'department', params: { id: 'new' } })
  //   }
  //
  //   edit (id) {
  //     this.$router.push({ name: 'department', params: { id } })
  //   }
  //
  //   // 启用/禁用
  //   toggleState (row) {
  //     if (row.state === 'ENABLED') { // 禁用
  //       row.state = 'DISABLED'
  //     } else if (row.state === 'DISABLED') { // 启用
  //       row.state = 'ENABLED'
  //     }
  //     DepartmentService.update(row, { id: row.id }).then(res => {
  //       this.$message({
  //         message: '修改成功！',
  //         type: 'success'
  //       })
  //     }).catch(e => {
  //       this.$message({
  //         message: '修改失败！',
  //         type: 'success'
  //       })
  //     })
  //   }
  }
</script>

<style lang="less" scoped>
  .departments {
    .tree {
      width: 250px;
    }

    .tabs {
      flex: 1 auto;
    }

    display: flex;
  }
</style>
