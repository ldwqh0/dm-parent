<template>
  <el-form class="role"
           size="mini"
           :model="role"
           :rules="rules"
           label-width="120px"
           ref="roleform">
    <el-row>
      <el-col :span="24">
        <el-form-item label="角色名称：" prop="name">
          <el-input v-model="role.name"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item label="角色组：">
          <el-select v-model="role.group"
                     value-key="id"
                     placeholder="请选择角色所属组,也可以直接输入新增一个角色组"
                     allow-create
                     filterable>
            <el-option v-for="roleGroup in roleGroups"
                       :label="roleGroup.name"
                       :value="roleGroup"
                       :key="roleGroup.id"/>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <!--
    <el-row>
      <el-col :span="24">
        <el-form-item label="状态" prop="state">
          <el-checkbox v-model="role.state" true-label="ENABLED" false-label="DISABLED">启用</el-checkbox>
        </el-form-item>
      </el-col>
    </el-row>
    -->
    <el-row>
      <el-col :span="24">
        <el-form-item label="描述" prop="state">
          <el-input
            v-model="role.description"
            type="textarea"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  // import { RoleService } from '../service'
  import { namespace } from 'vuex-class'

  const roleSpace = namespace('system/role')
  const roleGroupModule = namespace('system/roleGroup')
@Component
  export default class Role extends Vue {
  @Prop({default: () => 'new'})
  id

  role = {
    state: 'ENABLED'
  }

  rules = {
    name: [{
      required: true,
      message: '名称不能为空',
      trigger: 'blur'
    }]
  }

  @roleSpace.Action('get')
  getRole

  @roleSpace.Action('save')
  save

  @roleSpace.Action('update')
  update

  @roleGroupModule.Action('listAll')
  listGroups

  @roleGroupModule.State('roleGroups')
  stateRoleGroups

  get roleGroups () {
    return this.stateRoleGroups.filter(({id}) => id > 1)
  }

  created () {
    this.listGroups()
    if (this.id !== 'new') {
      this.getRole({id: this.id}).then(({data}) => {
        this.role = data
      })
    }
  }

  submit () {
    return this.$refs.roleform.validate().then(valid => {
      // 对数据进行一下造型，主要是手动输入的角色是名称，需要在后台添加
      const data = {...this.role}
      if (typeof (data.group) === 'string') {
        data.group = {name: data.group}
      }
      return this.id === 'new' ? this.save(data) : this.update(data, {id: this.id})
    })
  }
  }
</script>
