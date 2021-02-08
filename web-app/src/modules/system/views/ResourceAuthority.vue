<template>
  <el-form ref="form"
           class="resource-authority"
           size="mini"
           label-width="120px"
           :model="data">
    <el-row>
      <el-col :span="24">
        <ele-datatable :data="data.authorities">
          <el-table-column label="资源名称" prop="name" />
          <el-table-column label="资源路径" prop="matcher" />

          <el-table-column label="可读取">
            <template #default="scope">
              <three-check-box v-model="scope.row.readable" />
            </template>
          </el-table-column>
          <el-table-column label="可新增">
            <template #default="scope">
              <three-check-box v-model="scope.row.saveable" />
            </template>
          </el-table-column>
          <el-table-column label="可修改">
            <template #default="scope">
              <three-check-box v-model="scope.row.updateable" />
            </template>
          </el-table-column>
          <el-table-column label="可删除">
            <template #default="scope">
              <three-check-box v-model="scope.row.deleteable" />
            </template>
          </el-table-column>
        </ele-datatable>
      </el-col>
    </el-row>
  </el-form>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'
  import ThreeCheckBox from './ThreeCheckBox.vue'
  import http from '@/http'
  import URLS from '../URLS'
  import isNil from 'lodash/isNil'
  import isEmpty from 'lodash/isEmpty'
  // const resourceAuthority = namespace('system/resourceAuthority')

  @Component({
    components: {
      ThreeCheckBox
    }
  })
  export default class ResourceAuthority extends Vue {
    @Prop({ required: true })
    roleId!: string | number

    @Prop({ required: true })
    roleName!: string

    data = { authorities: [] }

    submit (): Promise<unknown> {
      const resourceAuthorities = this.data.authorities.filter(item => this.notNull(item))
        .reduce((acc: unknown, cur: { id: any }) => {
          (acc as any)[`${cur.id}`] = cur
          return acc
        }, {})
      return http.post(URLS.resourceAuthorities, {
        roleId: this.roleId,
        roleName: this.roleName,
        resourceAuthorities
      })
    }

    // TODO 这里需要处理
    notNull (item: unknown): boolean {
      return !(isEmpty(item) || isNil(item))
    }

    created (): void {
      Promise.all([http.get(`${URLS.resource}`), http.get(`${URLS.resourceAuthorities}/${this.roleName}`)])
        .then(([{ data: resources }, { data: { resourceAuthorities = {} } }]) => resources.map((resource: any) => ({
            ...resource,
            ...resourceAuthorities[resource.id]
          }))
        ).then(result => (this.data.authorities = result))
    }
  }
</script>

<style scoped>

</style>
