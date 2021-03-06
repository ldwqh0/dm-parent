<template>
  <el-form ref="form"
           label-width="100px"
           :rules="rules"
           :model="user">
    <el-form-item label="用户名：" prop="username">
      <el-input v-model="user.username" :maxlength="50" />
    </el-form-item>
    <el-form-item v-if="id==='0'" label="密码：" prop="password">
      <el-input v-model="user.password" type="password" :maxlength="50" />
    </el-form-item>
    <el-form-item v-if="id==='0'" label="确认密码：" prop="repassword">
      <el-input v-model="user.repassword" type="password" :maxlength="50" />
    </el-form-item>
    <el-form-item label="姓名：" prop="fullname">
      <el-input v-model="user.fullname" :maxlength="20" />
    </el-form-item>
    <el-form-item label="用户角色：" prop="roles">
      <el-select v-model="user.roles"
                 value-key="id"
                 style="width: 100%;"
                 multiple>
        <el-option-group v-for="group in roleGroups" :key="group.name" :label="group.name">
          <el-option v-for="role in group.roles"
                     :key="role.id"
                     :label="role.name"
                     :value="role" />
        </el-option-group>
      </el-select>
    </el-form-item>
    <el-form-item label="联系电话：" prop="mobile">
      <el-input v-model="user.mobile" :maxlength="20" />
    </el-form-item>

    <el-row>
      <el-col>
        <div style="width: 100px;text-align: right;padding-right: 12px;box-sizing: border-box;">用户职务：</div>
      </el-col>
    </el-row>
    <el-row v-for="(post,index) in user.posts" :key="index">
      <el-col :span="14">
        <el-form-item :label="`职务 ${index+1}`"
                      :prop="`posts[${index}].department.id`"
                      :rules="[{
                        required:true,
                        message:'请选择一个部门',
                      },{
                        validator:validateDepartment,
                      }]">
          <el-cascader
            v-model="post.department.id"
            clearable
            :options="departmentTree"
            :props="cascadeProps" />
        </el-form-item>
      </el-col>
      <el-col :span="7">
        <el-form-item label="职务"
                      label-width="60px"
                      :prop="`posts[${index}].post`"
                      :rules="[{
                        required: true,
                        message:'请输入职务名称',
                      }]">
          <el-input v-model="post.post" />
        </el-form-item>
      </el-col>
      <el-col :span="3">
        <!--        <el-form-item>-->
        <!--这里做一个减号图标-->
        <el-button v-if="user.posts.length>1" @click="removePost(index)">移除</el-button>
        <!--        </el-form-item>-->
      </el-col>
    </el-row>

    <el-form-item>
      <el-button type="success" @click="addPost">添加职务</el-button>
    </el-form-item>

    <el-form-item label="简介：" prop="profile">
      <el-input v-model="user.description" type="textarea" :maxlength="4000" />
    </el-form-item>
  </el-form>
</template>

<script lang="ts">
  import { Component, Prop, Vue } from 'vue-property-decorator'
  import { DepartmentDto, DepartmentTreeItem, RoleDto, Types, UserDto } from '@/types/service'
  import http from '@/http'
  import urls from '../URLS'
  import isNil from 'lodash/isNil'
  import { Rules } from 'async-validator'
  import { listToTree } from '@/utils'
  import { CascaderProps } from 'element-ui/types/cascader-panel'

  @Component
  export default class User extends Vue {
    @Prop({
      type: [String, Number],
      required: false,
      default: () => '0'
    })
    id!: string

    @Prop({
      type: [String, Number],
      required: true
    })
    departmentId!: string

    allDepartments: DepartmentDto[] = []

    get departmentTree (): DepartmentTreeItem[] {
      return listToTree(this.allDepartments)
    }

    cascadeProps: CascaderProps<number, DepartmentDto> = {
      children: 'children',
      label: 'fullname',
      value: 'id',
      emitPath: false,
      checkStrictly: true,
      expandTrigger: 'hover'
    }

    user: UserDto = {}
    roles: RoleDto[] = []

    rules: Rules = {
      username: [{
        required: true,
        message: '请输入用户名',
        trigger: 'blur'
      }],
      fullname: [{
        required: true,
        message: '用户姓名不能为空',
        trigger: 'blur'
      }],
      password: [{
        required: true,
        message: '密码不能为空',
        trigger: 'blur'
      }],
      repassword: [{
        required: true,
        message: '确认密码不能为空'
      }, {
        validator: this.validateRePassword,
        trigger: 'blur'
      }],
      mobile: [{
        required: true,
        message: '用户手机号不能为空',
        trigger: 'blur'
      }, {
        len: 11,
        message: '请输入正确的手机号码'
      }],
      roles: [{
        required: true,
        message: '用户角色不能为空',
        trigger: 'blur'
      }],
      email: [{
        type: 'email',
        message: '请输入正确的邮箱地址',
        trigger: 'blur'
      }]
    }

    validateRePassword (rule: Rules, value: string, callback: (error?: Error) => void): void {
      if (value === '') {
        callback(new Error('确认密码不能为空'))
      } else if (value !== this.user.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    validateDepartment (rule: Rules, value: number, callback: (error?: Error) => void): void {
      const [selected] = this.allDepartments.filter(v => v.id === value)
      if (!isNil(selected) && selected.type !== Types.GROUP) {
        callback()
      } else {
        callback(new Error('不能将用户添加到分组中'))
      }
    }

    get roleGroups (): { name: string, roles: RoleDto[] }[] {
      const results: { name: string, roles: RoleDto[] }[] = []
      this.roles.reduce((acc, cur) => {
        const group = cur.group ?? 'unknown'
        const groupRoles = acc.get(group)
        if (isNil(groupRoles)) {
          acc.set(group, [cur])
        } else {
          groupRoles.push(cur)
        }
        return acc
      }, new Map<string, RoleDto[]>())
        .forEach((value, key) => {
          results.push({
            name: key,
            roles: value
          })
        })
      return results
    }

    created (): void {
      http.get(`${urls.role}`).then(({ data }) => (this.roles = data))
      http.get(`${urls.department}`, { params: { scope: 'all' } }).then(({ data }) => {
        this.allDepartments = data
      })
      if (Number.parseInt(this.id) > 0) {
        http.get(`${urls.user}/${this.id}`).then(({ data }) => (this.$set(this, 'user', data)))
      } else {
        this.$set(this.user, 'posts', [{ department: { id: Number.parseInt(this.departmentId) } }])
      }
    }

    addPost (): void {
      this.user.posts?.push({ department: {} })
    }

    removePost (index: number): void {
      this.user.posts?.splice(index, 1)
    }

    submit (): Promise<any> {
      return (this.$refs.form as any).validate().then(() => {
        if (Number.parseInt(this.id) > 0) {
          return http.put(`${urls.user}/${this.id}`, this.user)
        } else {
          return http.post(`${urls.user}`, this.user)
        }
      })
    }
  }
</script>

<style scoped>

</style>
