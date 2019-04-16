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
        <el-form-item label="角色组：" placeholder="请选择角色所属组">
          <el-select v-model="role.group.id"
                     allow-create
                     filterable>
            <el-option v-for="roleGroup in roleGroups"
                       :label="roleGroup.name"
                       :value="roleGroup.id"
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
    <el-row>
      <el-col :span="24" class="flex-right">
        <el-button size="mini"
                   @click="doCancel">
          取消
        </el-button>
        <el-button type="primary"
                   size="mini"
                   @click="onSubmit">
          提交
        </el-button>
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
    @Prop({ default: () => 'new' })
    id
    role = {
      group: {
        // id: 1
      },
      state: 'ENABLED'
    }

    get rules () {
      return {
        name: [{
          required: true,
          message: '名称不能为空',
          trigger: 'blur'
        }]
      }
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
    roleGroups

    created () {
      this.listGroups()
      if (this.id !== 'new') {
        this.getRole({ id: this.id }).then(data => {
          this.role = data
        })
      }
    }

    doCancel () {
      this.$emit('complete')
      // this.$router.push({ name: 'roles' })
    }

    onSubmit () {
      this.$refs['roleform'].validate().then(valid => {
        if (valid) {
          // 对数据进行一下造型，主要是手动输入的角色是名称，需要在后台添加
          let data = { ...this.role }
          let { id: groupId } = data.group = { ...data.group }
          if (!this.roleGroups.some(item => item.id === groupId)) {
            data.group = { name: groupId }
          }
          if (this.id === 'new') {
            this.save(data).then(() => this.$emit('complete'))
          } else {
            this.update(data, { id: this.id }).then(() => this.$emit('complete'))
          }
        }
      })
    }
  }
</script>
