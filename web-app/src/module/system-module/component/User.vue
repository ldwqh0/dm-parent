<template>
  <el-form class="user"
           size="mini"
           :model="user"
           :rules="rules"
           label-width="100px"
           ref="userform">
    <el-row>
      <el-col :span="12">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="姓名" prop="fullname">
          <el-input v-model="user.fullname"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row v-if="id==='new'">
      <el-col :span="12">
        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            v-model="user.password"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="确认密码" prop="repassword">
          <el-input
            type="password"
            v-model="user.repassword"/>
        </el-form-item>
      </el-col>
    </el-row>
    <!--
    <el-row>
      <el-col :span="12">
        <el-form-item label="所属区域" prop="region">
          <el-cascader
            v-model="region"
            expand-trigger="hover"
            :options="regions"
            :props="treeProp"
            change-on-select/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item prop="scenicName" label="所属景区">
          <el-input v-model="user.scenicName"/>
        </el-form-item>
      </el-col>
    </el-row>
    -->
    <!--    {{ posts }}-->

    <el-row>
      <el-col :span="12">
        <el-form-item label="状态">
          <el-checkbox v-model="user.enabled">启用</el-checkbox>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="所属角色" prop="roles">
          <el-select multiple
                     value-key="id"
                     placeholder="请选择"
                     v-model="user.roles">
            <el-option v-for="item in roles"
                       :key="item.id"
                       :value="item"
                       :label="item.name">
              <span style="float: left">{{ item.group.name }}</span>
              <span style="float: right">{{ item.name }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <!--关于职务的验证需要单独处理-->
    <el-form ref="postForms"
             v-for="(post,index) in posts"
             :key="index"
             size="mini"
             label-width="100px"
             :model="post"
             :rules="postRules">
      <el-row>
        <el-col :span="11">
          <el-form-item prop="department">
            <template #label>
              职务 {{ index+1 }}
            </template>
            <el-cascader
              v-model="post.department"
              expand-trigger="hover"
              :options="departmentTree"
              :props="treeProp"
              change-on-select/>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="职务" prop="post">
            <el-input v-model="post.post"/>
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <!--        <el-form-item>-->
          <!--这里做一个减号图标-->
          <el-button size="mini" @click="removePost(index)">移除</el-button>
          <!--        </el-form-item>-->
        </el-col>
      </el-row>
    </el-form>
    <el-row>
      <el-col :span="24">
        <el-form-item>
          <el-button @click="addPost">添加职务</el-button>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="联系电话" prop="mobile">
          <el-input v-model="user.mobile"/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="邮箱">
          <el-input v-model="user.email"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24">
        <el-form-item label="描述信息">
          <el-input
            v-model="user.describe"
            type="textarea"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row class="">
      <el-col :span="24">
        <el-form-item class="pull-right">
          <el-button size="mini" @click="doCancel">取 消</el-button>
          <el-button size="mini" type="primary" @click="doSubmit">确 定</el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'

  const userSpace = namespace('system/user')
  const roleSpace = namespace('system/role')
  const regionSpace = namespace('system/region')
  const departmentModule = namespace('system/department')
  @Component({
    components: {}
  })
  export default class User extends Vue {
    user = {
      enabled: true,
      roles: []
    }

    // 渲染部门列表
    posts = [{}]

    treeProp = {
      children: 'children',
      label: 'name',
      value: 'id'
    }

    // @regionSpace.Getter('regionTree')
    // regions
    //
    // @regionSpace.Action('loadRegions')
    // loadRegions

    @regionSpace.Getter('codeToArray')
    codeToArray

    @Prop({ default: () => 'new' })
    id

    @departmentModule.Getter('departments')
    departmentTree

    @departmentModule.Getter('getParents')
    getParents

    // get region () {
    //   return this.codeToArray(this.user.regionCode, this.regions)
    // }

    // set region (regionCode) {
    //   if (regionCode && regionCode.length > 0) {
    //     let result = regionCode[regionCode.length - 1]
    //     this.user.regionCode = result
    //   }
    // }

    get postRules () {
      return {
        department: [{
          required: true,
          message: '请选择任职部门',
          trigger: 'blur'
        }],
        post: [{
          required: true,
          message: '请输入职务名称',
          trigger: 'blur'
        }]
      }
    }

    get rules () {
      return {
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
          validator: this.validateRePassword,
          trigger: 'blur'
        }],
        departments: [{
          required: true,
          message: '用户机构不能为空',
          trigger: 'blur'
        }],
        roles: [{
          required: true,
          message: '用户角色不能为空',
          trigger: 'blur'
        }]
      }
    }

    @roleSpace.State('roles')
    roles

    @roleSpace.Action('listAll')
    listRoles

    @userSpace.Action('save')
    save

    @userSpace.Action('get')
    getUser

    @userSpace.Action('update')
    update

    created () {
      this.listRoles()
      // this.loadRegions()
      if (this.id === 'new') {

      } else {
        // 获取用户之后，重置用户的职务信息
        this.getUser({ id: this.id }).then(({ data, data: { posts } }) => {
          this.user = data
          if (posts) {
            this.posts = Object.keys(posts).map(department => ({
              department: this.getParents(Number.parseInt(department)),
              post: posts[Number.parseInt(department)]
            }))
          } else {
            this.posts = []
          }
        })
      }
    }

    // 添加职务信息
    addPost () {
      this.posts.push({})
    }

    // 移除指定索引位置的职务
    removePost (index) {
      this.posts.splice(index, 1)
    }

    doSubmit () {
      let validations = [...this.$refs['postForms'], this.$refs['userform']]
        .map(form => new Promise((resolve, reject) => {
          form.validate(valid => {
            valid ? resolve() : reject(new Error('验证失败'))
          })
        }))

      Promise.all(validations).then(() => {
        let user = Object.assign({}, this.user)
        user.posts = this.posts.reduce((acc, { department, post }) => ({
          ...acc,
          [department[department.length - 1]]: post
        }), {})
        if (this.id === 'new') {
          this.save(user).then(response => {
            this.$emit('complete')
          })
        } else {
          this.update(user).then(response => {
            this.$emit('complete')
          })
        }
      }).catch(e => {
        console.error('校验不通过')
      })
    }

    // 取消按钮
    doCancel () {
      this.$emit('complete')
    }

    validateRePassword (rule, value, callback) {
      if (value === '') {
        callback(new Error('确认密码不能为空'))
      } else if (value !== this.user.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
  }
</script>
<style lang="less">
</style>
