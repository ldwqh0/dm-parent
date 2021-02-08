<template>
  <div class="departments">
    <el-tree ref="tree"
             class="tree"
             :expand-on-click-node="false"
             node-key="id"
             :props="treeProp"
             :default-expanded-keys="defaultExpands"
             lazy
             :load="loadNode"
             @current-change="selectNode">
      <template #default="{ node }">
        <span>
          <i v-if="node.data.type==='GROUP'" class="el-icon-folder-opened" />
          <i v-if="node.data.type==='ORGANS'" class="el-icon-office-building" />
          <i v-if="node.data.type==='DEPARTMENT'" class="el-icon-school" />
          &nbsp;{{ node.label }}</span>
      </template>
    </el-tree>
    <el-tabs class="tabs">
      <el-tab-pane label="下级部门">
        <el-row>
          <el-col>
            <el-button type="primary" @click="editDepartment({})">添加子节点</el-button>
          </el-col>
        </el-row>
        <ele-data-tables v-if="departmentQuery.parentId"
                         ref="table"
                         :ajax="departmentUrl"
                         :server-params="departmentQuery"
                         pagination-layout="total, sizes, prev, pager, next, jumper">
          <el-table-column prop="fullname">
            <template #default="{row}">
              <a href="javascript:void(0)" @click="selectNode(row)">{{ row.fullname }}</a>
            </template>
          </el-table-column>
          <el-table-column label="类型">
            <template #default="{row}">
              <span v-if="row.type==='ORGANS'">组织机构</span>
              <span v-if="row.type==='DEPARTMENT'">部门</span>
              <span v-if="row.type==='GROUP'">分组</span>
            </template>
          </el-table-column>
          <el-table-column prop="id" label="操作" width="100">
            <!--            <span slot-scope="scope">-->
            <!--              <el-button type="text" @click="toggleState(scope.row)">{{-->
            <!--                scope.row.state === 'ENABLED' ? '禁用' : '启用'-->
            <!--              }}</el-button>-->
            <template #default="{row}">
              <el-button type="text" @click="editDepartment(row)">编辑</el-button>
              <!--              <el-button type="text" @click="up(scope.row.id)">上移</el-button>-->
              <!--              <el-button type="text" @click="down(scope.row.id)">下移</el-button>-->
            </template>
            <!--            </span>-->
          </el-table-column>
        </ele-data-tables>
      </el-tab-pane>
      <el-tab-pane label="部门人员">
        <el-row>
          <el-col>
            <el-button type="primary" @click="editUser({})">新用户</el-button>
          </el-col>
        </el-row>
        <ele-data-tables v-if="userQuery.department"
                         ref="userTable"
                         :ajax="userUrl"
                         :server-params="userQuery">
          <el-table-column prop="username" label="用户名" />
          <el-table-column>
            <template #default="{row}">
              <el-button type="text" @click="editUser(row)">编辑</el-button>
            </template>
          </el-table-column>
        </ele-data-tables>
      </el-tab-pane>
    </el-tabs>
    <el-dialog v-if="departmentEditVisible"
               :visible.sync="departmentEditVisible"
               :close-on-click-modal="false">
      <department :id="currentEdit.id" ref="departmentForm" :parent-id="current.id" />
      <template #footer>
        <el-button type="primary" :loading="submitting" @click="saveDepartment">确定</el-button>
        <el-button type="danger" @click="departmentEditVisible=false">取消</el-button>
      </template>
    </el-dialog>

    <el-dialog v-if="userEditVisible"
               :visible.sync="userEditVisible"
               :close-on-click-modal="false">
      <user :id="currentEdit.id" ref="user" :department-id="current.id" />
      <template #footer>
        <el-button type="primary" :loading="submitting" @click="saveUser">确定</el-button>
        <el-button type="danger" @click="userEditVisible=false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import { Component, Vue } from 'vue-property-decorator'
  import { TreeNode } from 'element-ui/types/tree'
  import http from '@/http'
  import urls from '../URLS'
  import { DepartmentDto, Page, UserDto } from '@/types/service'
  import { TreeProps } from '@/types/element'
  import isNil from 'lodash/isNil'
  import Department from './Department.vue'
  import User from './User.vue'

  @Component({
    components: { User, Department }
  })
  export default class Departments extends Vue {
    current: DepartmentDto = {}
    departmentEditVisible = false
    // 当前的编辑项目
    currentEdit: DepartmentDto | UserDto = {}

    userEditVisible = false
    // 指示是否在提交
    submitting = false

    get defaultExpands (): number[] {
      // 默认选择中当前项目
      // 只在第一次是生效
      if (this.current.id) {
        return [this.current.id]
      } else {
        return []
      }
    }

    get departmentQuery (): { [key: string]: any } {
      return {
        parentId: this.current.id
      }
    }

    get userQuery (): { [key: string]: any } {
      return {
        department: this.current.id
      }
    }

    get treeProp (): TreeProps<number, DepartmentDto> {
      return { // 树形机构显示属性
        children: 'children',
        label: 'fullname',
        disabled: '',
        // 判断某个部门是否有叶子节点
        isLeaf: (data: DepartmentDto) => !data.hasChildren
      }
    }

    departmentUrl = urls.department
    userUrl = urls.user

    loadNode ({ key }: TreeNode<number, DepartmentDto>, reslove: (v: DepartmentDto[]) => void): void {
      // 加载某个节点
      this.loadChildren(key).then(result => {
        reslove(result)
        return result
      }).then(([root] = []) => {
        // 如果加载的是根节点，选中根节点
        if (isNil(key)) {
          this.selectNode(root)
        }
      })
    }

    loadChildren (parentId: number): Promise<DepartmentDto[]> {
      return http.get<Page<DepartmentDto>>(`${urls.department}`, { params: { parentId, size: 10000 } })
        .then(({ data: { content } }) => (content ?? []))
    }

    updateTreeNode (id?: number): Promise<unknown> {
      if (id !== undefined) {
        return this.loadChildren(id).then((data) => {
          (this.$refs.tree as any).updateKeyChildren(id, data)
        })
      } else {
        return Promise.reject(new Error('需要更新的节点为空'))
      }
    }

    editDepartment (department: DepartmentDto): void {
      this.currentEdit = department
      this.departmentEditVisible = true
    }

    saveDepartment (): Promise<unknown> {
      this.submitting = true
      return (this.$refs.departmentForm as any).submit().then(({ data }: { data: DepartmentDto }) => {
        this.departmentEditVisible = false;
        // 保存之后需要刷新当前页面
        (this.$refs.table as any).reloadData()
        // 保存刷新当前部门的树
        if (!isNil(this.current.id)) {
          this.updateTreeNode(this.current.id)
        }
        // 如果当前树不等于修改后的部门树,说明将部门修改到了其他节点，还要刷新相应的节点
        if (this.current.id !== data.parent?.id) {
          this.updateTreeNode(data.parent?.id)
        }
      }).finally(() => {
        this.submitting = false
      })
    }

    editUser (user: UserDto): void {
      this.currentEdit = user
      this.userEditVisible = true
    }

    saveUser (): Promise<unknown> {
      this.submitting = true
      return (this.$refs.user as any).submit().then(() => {
        this.userEditVisible = false;
        (this.$refs.userTable as any).reloadData()
      }).finally(() => {
        this.submitting = false
      })
    }

    selectNode (data: DepartmentDto): void {
      // 当树形结构选择项改变时,更新表格搜索参数
      this.current = data
    }
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
