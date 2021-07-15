const c = [
  [1, 1, 0, 1],
  [2, 3, 2, 2],
  [3, 8, 3, 3],
  [4, 4, 4, 0]
]

const b = [[10, 50, 100, 35, 55, 30, 20], [15, 25, 90, 40, 26, 30, 20]]

function cal (arr) {
  return arr.reduce((acc, row) => {
    return [...acc, row.map((value, colIdx) => {
      // 拿到当前行指定列的前一次的计算结果，如果拿不到，就为0，再加上当前行特定列的数据，就是当前行指定列的新值
      return ((acc.slice(-1)[0])?.[colIdx] ?? 0) + row[colIdx]
    })]
  }, [])
}

console.log(cal(c))
console.log(cal(b))

