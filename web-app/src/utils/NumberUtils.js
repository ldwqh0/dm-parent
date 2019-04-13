export default {
  toTenThousand (number, digits = 2) {
    return Number.parseFloat((number / 10000).toFixed(digits))
  },

  toMillion (number, digits = 2) {
    return Number.parseFloat((number / 100000000).toFixed(digits))
  }
}
