<template>
  <el-form ref="form"
           inline
           class="resource-authority"
           label-width="120px"
           :model="data">
    <ele-data-tables :data="data.authorities">
      <el-table-column label="资源名称" prop="name" />
      <el-table-column label="资源路径" prop="matcher" />

      <el-table-column label="GET">
        <template #default="scope">
          <three-check-box v-model="scope.row.getAble" />
        </template>
      </el-table-column>
      <el-table-column label="POST">
        <template #default="scope">
          <three-check-box v-model="scope.row.postAble" />
        </template>
      </el-table-column>
      <el-table-column label="PUT">
        <template #default="scope">
          <three-check-box v-model="scope.row.putAble" />
        </template>
      </el-table-column>
      <el-table-column label="DELETE">
        <template #default="scope">
          <three-check-box v-model="scope.row.deleteAble" />
        </template>
      </el-table-column>
      <el-table-column type="expand" label="其它">
        <template #default="scope">
          <el-form-item label="PATCH" label-width="auto">
            <three-check-box v-model="scope.row.patchAble" />
          </el-form-item>
          <el-form-item label="OPTIONS" label-width="auto">
            <three-check-box v-model="scope.row.optionsAble" />
          </el-form-item>
        </template>
      </el-table-column>
    </ele-data-tables>
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

    data = { authorities: [] }

    submit (): Promise<unknown> {
      const resourceAuthorities = this.data.authorities.filter(item => this.notNull(item))
        .reduce((acc: unknown, cur: { id: any }) => {
          (acc as any)[`${cur.id}`] = cur
          return acc
        }, {})
      return http.post(URLS.resourceAuthorities, {
        roleId: this.roleId,
        roleName: (this as any).roleName,
        resourceAuthorities
      })
    }

    // TODO 这里需要处理
    notNull (item: unknown): boolean {
      return !(isEmpty(item) || isNil(item))
    }

    created (): void {
      Promise.all([http.get(`${URLS.resource}`), http.get(`${URLS.resourceAuthorities}/${this.roleId}`)])
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
