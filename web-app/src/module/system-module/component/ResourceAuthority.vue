<template>
  <el-container>
    <el-main>
      <el-form size="mini"
               ref="form"
               label-width="120px"
               :model="data">
        <el-row>
          <el-col :span="12">{{ data.roleName }}&nbsp;&nbsp;的资源权限</el-col>
          <el-col :span="12" class="flex-right">
            <router-link class="el-button el-button--default el-button--mini" :to="{name:'roles'}">
              取消
            </router-link>
            <el-button type="primary"

                       @click="doSubmit">
              提交
            </el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <ele-datatable :data="data.resourceAuthorities">
              <el-table-column label="资源名称" prop="resource.name"/>
              <el-table-column label="资源路径" prop="resource.matcher"/>

              <el-table-column label="可读取">
                <template slot-scope="scope">
                  <three-check-box v-model="scope.row.readable"/>
                </template>
              </el-table-column>
              <el-table-column label="可新增">
                <template slot-scope="scope">
                  <three-check-box v-model="scope.row.saveable"/>
                </template>
              </el-table-column>
              <el-table-column label="可修改">
                <template slot-scope="scope">
                  <three-check-box v-model="scope.row.updateable"/>
                </template>
              </el-table-column>
              <el-table-column label="可删除">
                <template slot-scope="scope">
                  <three-check-box v-model="scope.row.deleteable"/>
                </template>
              </el-table-column>
            </ele-datatable>
          </el-col>
        </el-row>
      </el-form>
    </el-main>
  </el-container>
</template>

<script>
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import { namespace } from 'vuex-class'
  import ThreeCheckBox from './ThreeCheckBox'
  import EleDatatable from 'element-datatables'

  const resourceAuthority = namespace('system/resourceAuthority')

  @Component({
    components: {
      ThreeCheckBox,
      EleDatatable
    }
  })
  export default class ResourceAuthority extends Vue {
    @Prop({ default: () => 'new' })
    name

    @resourceAuthority.Action('list')
    list

    @resourceAuthority.Action('save')
    save

    data = { resourceAuthorities: [] }

    doSubmit () {
      this.save(this.data).then(() => {
        this.$router.push({ name: 'roles' })
      })
    }

    created () {
      this.list(this.name).then(rsp => {
        this.data = rsp
      })
    }
  }
</script>

<style scoped>

</style>
