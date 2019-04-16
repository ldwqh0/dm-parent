<template>
  <el-form class="user"
           size="mini"
           :model="user"
           :rules="rules"
           label-width="120px"
           ref="userform">
    <!--        <el-row style="margin-bottom: 20px;margin-top:7px;">
               <el-col :span="5" :offset="19" style="display: flex;justify-content: flex-end;">
              <el-button @click="goBack" size="mini">返回</el-button>
               <el-button type="primary" @click="onSubmit" size="mini">提交</el-button>
            </el-col>
           </el-row>-->
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
                       :label="item.name"/>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row v-for="post in posts" :key="post.key">
      <el-col :span="12">
        <el-form-item label="部门">
          <el-cascader
            v-model="post.department"
            expand-trigger="hover"
            :options="departmentTree"
            :props="treeProp"
            change-on-select/>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="职务">
          <el-input v-model="post.post"/>
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
  @Component
  export default class User extends Vue {
    user = {
      enabled: true,
      // posts: {},
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

    goBack () {
      this.$router.push({ name: 'users' })
    }

    doSubmit () {
      // 提交时修改职务信息
      this.$refs.userform.validate(valid => {
        if (valid) {
          let user = Object.assign({}, this.user)
          user.posts = this.posts.reduce((acc, { department, post }) => ({
            ...acc,
            [department[department.length - 1]]: post
          }), {})
          if (this.id === 'new') {
            this.save(user).then(response => {
              this.$emit('complete')
              // this.$router.push({ name: 'users' })
            })
          } else {
            this.update(user).then(response => {
              this.$emit('complete')
              // this.$router.push({ name: 'users' })
            })
          }
        } else {
          console.error('校验不通过')
        }
      })
    }

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
