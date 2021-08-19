<template>
  <p>
    应用密钥：
    <a href="javascript:void(0)" title="点击文字将密钥复制到剪贴板" @click="copyValueToClipboard">
      {{ value }}
      <el-button type="text" icon="el-icon-copy-document" />
    </a>
  </p>
</template>

<script lang="ts">
  import Vue from 'vue'

  export default Vue.extend({
    props: {
      value: {
        type: [String],
        required: true
      }
    },
    methods: {
      copyValueToClipboard (): void {
        try {
          window.navigator.clipboard.writeText(this.value)
        } catch (e) {
          console.log(e)
        }
      }
    }
  })
</script>

<style scoped lang="less">

  p {
    text-align: center;

    a {
      text-decoration: none;
      color: black;
    }
  }

</style>
