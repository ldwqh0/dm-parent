<template>
  <el-checkbox @change="valueChange"
               :true-label="1"
               v-model="state"
               :indeterminate="data===undefined || data===null"
               :checked="false"/>
</template>

<script>
  import Vue from 'vue'
  import { Prop, Component } from 'vue-property-decorator'

  @Component
  export default class ThreeCheckBox extends Vue {
    @Prop({
      type: Boolean,
      default: () => null
    })
    value

    state = 2

    data = null

    created () {
      this.data = this.value
      if (this.value === undefined || this.value === null) {
        this.state = 0
      }
      if (this.value === true) {
        this.state = 1
      }
      if (this.value === false) {
        this.state = 0
      }
    }

    valueChange (o, n) {
      if (this.data === true) {
        this.data = false
        this.state = 0
        this.$emit('input', false)
        return
      }
      if (this.data === null || this.data === undefined) {
        this.data = true
        this.state = 1
        this.$emit('input', true)
        return
      }
      if (this.data === false) {
        this.data = null
        this.state = 0
        this.$emit('input', null)
      }
    }
  }
</script>

<style scoped>

</style>
