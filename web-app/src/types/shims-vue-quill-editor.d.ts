declare module 'vue-quill-editor' {
  import Vue from 'vue'

  import Quill, { QuillOptionsStatic } from 'quill'

  function install (vue: typeof Vue, options?: QuillOptionsStatic): void

  const quillEditor: typeof Vue

  export { Quill, install, quillEditor }

  const VueQuillEditor: {
    Quill: typeof Quill,
    install: typeof install,
    quillEditor: Vue
  }

  export default VueQuillEditor
}
