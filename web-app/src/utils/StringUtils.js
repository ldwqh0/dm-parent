export default {
  stripEnd (str, stripChars) {
    if (str === undefined || str === null || str.length === 0) {
      return str
    }
    if (stripChars === undefined || stripChars == null || stripChars.length === 0) {
      return str.trimEnd()
    }
    let result = str
    while (result.endsWith(stripChars)) {
      result = result.substring(0, result.length - stripChars.length)
    }
    return result
  }
}
