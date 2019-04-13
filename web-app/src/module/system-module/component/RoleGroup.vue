<!--角色组表单-->
<template>
  <el-container>
    <el-main>
      <el-form size="mini"
               :model="roleGroup"
               :rules="rules"
               label-width="120px"
               ref="roleform">
        <el-row style="margin-bottom: 20px;margin-top:7px;">
          <el-col :span="24" class="flex-right">
            <el-button size="mini"
                       @click="goBack">
              取消
            </el-button>
            <el-button type="primary"
                       size="mini"
                       @click="onSubmit">
              提交
            </el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色组名称：" prop="name">
              <el-input v-model="roleGroup.name"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述" prop="state">
              <el-input
                v-model="roleGroup.description"
                type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-main>
  </el-container>
</template>
<script>
  import { Component, Prop } from 'vue-property-decorator'
  import Vue from 'vue'
  // import { RoleService } from '../service'
  import { namespace } from 'vuex-class'

  const roleSpace = namespace('system/roleGroup')

  @Component
  export default class RoleGroup extends Vue {
    @Prop({ default: () => 'new' })
    id
    roleGroup = {}

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
    getRoleGroup

    @roleSpace.Action('save')
    save

    @roleSpace.Action('update')
    update

    created () {
      if (this.id !== 'new') {
        this.getRoleGroup({ id: this.id }).then(data => {
          this.roleGroup = data
        })
      }
    }

    goBack () {
      this.$router.push({ name: 'roles' })
    }

    onSubmit () {
      this.$refs['roleform'].validate().then(valid => {
        if (valid) {
          if (this.id === 'new') {
            this.save(this.roleGroup).then(() => {
              this.goBack()
            })
          } else {
            this.update(this.roleGroup, { id: this.id }).then(() => this.goBack())
          }
        }
      })
    }
  }
</script>
