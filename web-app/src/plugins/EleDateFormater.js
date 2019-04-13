const $dateFormatter = (row, column, cellValue, index) => {
  try {
    if (cellValue !== undefined && cellValue !== null && cellValue !== '') {
      return new Date(cellValue).toLocaleDateString()
    } else {
      return ''
    }
  } catch (e) {
    return ''
  }
}

const $dateTimeFormatter = (row, column, cellValue, index) => {
  try {
    if (cellValue !== undefined && cellValue !== null && cellValue !== '') {
      return new Date(cellValue).toLocaleString()
    } else {
      return ''
    }
  } catch (e) {
    return ''
  }
}

export default {
  install (Vue) {
    Vue.mixin({
      methods: {
        $dateFormatter,
        $dateTimeFormatter
      }
    })
  }
}
