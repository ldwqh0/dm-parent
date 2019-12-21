<template>
  <div class="resource-authority">
    <el-form size="mini"
             ref="form"
             label-width="120px"
             :model="data">
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
  </div>
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

    submit () {
      return this.save(this.data)
    }

    created () {
      this.list(this.name).then(({ data }) => (this.data = data))
    }
  }
</script>

<style scoped>

</style>
