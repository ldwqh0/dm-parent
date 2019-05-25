<template>
  <router-view/>
  <!--<left-theme-template v-if="theme==='left'"/>-->
  <!--<top-theme-template v-else-if="theme==='top'"/>-->
  <!--<div v-else>你没有选择任何主题</div>-->
</template>

<script>
  import Vue from 'vue'
  import { Component, Watch } from 'vue-property-decorator'
  import { Getter } from 'vuex-class'

  @Component
  export default class App extends Vue {
    // 应用根组件

    @Getter('loading')
    loading

    @Getter('error')
    error

    @Watch('error')
    errorChange ({ message }) {
      this.$notify.error({
        title: '错误',
        message
      })
    }
  }
</script>
<style lang="less">
  @import "./style/themes/default.less";

  .el-input__icon {
    line-height: 28px;
  }
</style>
