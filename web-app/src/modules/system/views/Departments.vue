<template>
  <el-container class="departments">
    <el-aside width="250px" style="border-right: solid 1px #efefef;box-sizing: content-box">
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
            <i v-if="node.data.type==='GROUP' && node.expanded" class="el-icon-folder-opened" />
            <i v-if="node.data.type==='GROUP' && !node.expanded" class="el-icon-folder" />
            <i v-if="node.data.type==='ORGANS'" class="el-icon-office-building" />
            <i v-if="node.data.type==='DEPARTMENT'" class="el-icon-coordinate" />
            &nbsp;{{ node.label }}</span>
        </template>
      </el-tree>
    </el-aside>
    <el-main style="padding: 0 0 0 10px">
      <el-tabs class="tabs" @tab-click="searchObj.keyword=''">
        <el-tab-pane label="下级部门">
          <el-form inline :model="searchObj">
            <el-row>
              <el-col :span="12">
                <el-form-item prop="keyword">
                  <el-input v-model="searchObj.keyword" placeholder="请输入查询关键字" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="float: right;">
                  <el-button type="primary" @click="editDepartment({})">添加子节点</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <ele-data-tables v-if="departmentQuery.parentId"
                           ref="table"
                           :ajax="departmentUrl"
                           :server-params="departmentQuery"
                           pagination-layout="total, sizes, prev, pager, next, jumper">
            <el-table-column label="部门名称">
              <template #default="{row}">
                <a href="javascript:void(0)" @click="selectNode(row)">{{ row.fullname }}</a>
              </template>
            </el-table-column>
            <el-table-column label="成员数" prop="userCount" />
            <el-table-column label="类型">
              <template #default="{row}">{{ row.type | toDepartment }}</template>
            </el-table-column>
            <el-table-column prop="id"
                             label="操作"
                             width="50px"
                             fixed="right">
              <!--            <span slot-scope="scope">-->
              <!--              <el-button type="text" @click="toggleState(scope.row)">{{-->
              <!--                scope.row.state === 'ENABLED' ? '禁用' : '启用'-->
              <!--              }}</el-button>-->
              <template #default="{row}">
                <el-button type="text" @click="editDepartment(row)">编辑</el-button>
                <!--                <el-button type="text" @click="up(row.id)">上移</el-button>-->
                <!--                <el-button type="text" @click="down(row.id)">下移</el-button>-->
              </template>
              <!--            </span>-->
            </el-table-column>
          </ele-data-tables>
        </el-tab-pane>
        <el-tab-pane label="部门人员">
          <el-form inline :model="searchObj">
            <el-row>
              <el-col :span="12">
                <el-form-item prop="keyword">
                  <el-input v-model="searchObj.keyword" placeholder="请输入查询关键字" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="float:right;">
                  <el-button type="primary" @click="editUser({})">新用户</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <ele-data-tables v-if="userQuery.department"
                           ref="userTable"
                           :ajax="userUrl"
                           :server-params="userQuery">
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="fullname" label="用户全名" />
            <el-table-column prop="mobile" label="联系电话" />
            <el-table-column label="用户角色">
              <template #default="{row}">
                <div v-for="(item,index) in row.roles" :key="index">
                  {{ item.authority }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="50px">
              <template #default="{row}">
                <el-button type="text" @click="editUser(row)">编辑</el-button>
              </template>
            </el-table-column>
          </ele-data-tables>
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <el-dialog v-if="departmentEditVisible"
               :visible.sync="departmentEditVisible"
               :close-on-click-modal="false">
      <department :id="currentEdit.id"
                  ref="departmentForm"
                  v-loading="loading"
                  :parent-id="current.id" />
      <template #footer>
        <el-button @click="departmentEditVisible=false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveDepartment">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-if="userEditVisible"
               :visible.sync="userEditVisible"
               :close-on-click-modal="false">
      <user :id="currentEdit.id"
            ref="user"
            v-loading="loading"
            :department-id="current.id" />
      <template #footer>
        <el-button @click="userEditVisible=false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="saveUser">确定</el-button>
      </template>
    </el-dialog>
  </el-container>
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
  import { namespace } from 'vuex-class'

  const departmentTypes = {
    DEPARTMENT: '部门',
    ORGANS: '机构',
    GROUP: '分组'
  } as { [key: string]: string }

  const httpModel = namespace('http')

  @Component({
    components: {
      User,
      Department
    },
    filters: {
      toDepartment (v: string): string {
        return departmentTypes[v]
      }
    }
  })
  export default class Departments extends Vue {
    current: DepartmentDto = {}
    departmentEditVisible = false
    // 当前的编辑项目
    currentEdit: DepartmentDto | UserDto = {}

    userEditVisible = false

    @httpModel.Getter('loading')
    loading!: boolean

    searchObj = {
      keyword: ''
    }

    get defaultExpands (): number[] {
      // 默认选择中当前项目
      // 只在第一次是生效
      if (this.current?.id) {
        return [this.current.id]
      } else {
        return []
      }
    }

    get departmentQuery (): { [key: string]: any } {
      return {
        ...this.searchObj,
        parentId: this.current?.id
      }
    }

    get userQuery (): { [key: string]: any } {
      return {
        ...this.searchObj,
        department: this.current?.id
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
      return http.get<Page<DepartmentDto>>(`${urls.department}`, {
        params: {
          parentId,
          page: 0,
          size: 10000
        }
      }).then(({ data: { content } }) => (content ?? []))
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
      return (this.$refs.departmentForm as any).submit().then(({ data }: { data: DepartmentDto }) => {
        this.departmentEditVisible = false;
        // 保存之后需要刷新当前页面
        (this.$refs.table as any).reloadData()
        // 保存刷新当前部门的树
        if (!isNil(this.current?.id)) {
          this.updateTreeNode(this.current.id)
        }
        // 如果当前树不等于修改后的部门树,说明将部门修改到了其他节点，还要刷新相应的节点
        if (this.current?.id !== data.parent?.id) {
          this.updateTreeNode(data.parent?.id)
        }
      })
    }

    editUser (user: UserDto): void {
      this.currentEdit = user
      this.userEditVisible = true
    }

    saveUser (): Promise<unknown> {
      return (this.$refs.user as any).submit().then(() => {
        this.userEditVisible = false;
        (this.$refs.userTable as any).reloadData()
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
    height: 100%;

    .tabs {
      flex: 1 auto;
    }

    display: flex;
  }
</style>
<style lang="less">
  .departments {
    .el-tree-node.is-current > .el-tree-node__content {
      background: #386be1 !important;
      color: white;
    }
  }
</style>
