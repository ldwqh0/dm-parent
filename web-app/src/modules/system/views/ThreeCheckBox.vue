<template>
  <el-checkbox v-model="state"
               :true-label="1"
               :indeterminate="data===null"
               :checked="false"
               @change="valueChange" />
</template>

<script lang="ts">
  import Vue from 'vue'
  import { Component, Prop } from 'vue-property-decorator'

  /**
   * 能再三种状态之前切换的checkbox
   * 分别是选中，未选中和不确定
   * 选中的值是true,不选中的值是false,不确定的值是null
   */
  @Component
  export default class ThreeCheckBox extends Vue {
    @Prop({
      type: Boolean,
      default: () => null
    })
    value!: boolean | null

    state = 2

    data: boolean | null = null

    created (): void {
      this.data = this.value
      if (this.value === true) {
        this.state = 1
      } else {
        this.state = 0
      }
    }

    /**
     * 在几种值之前切换
     */
    valueChange (): void {
      if (this.data === true) {
        this.data = false
        this.state = 0
        this.$emit('input', false)
        return
      }
      if (this.data === null) {
        this.data = true
        this.state = 1
        this.$emit('input', true)
        return
      }
      if (!this.data) {
        this.data = null
        this.state = 0
        this.$emit('input', null)
      }
    }
  }
</script>

<style scoped>

</style>
