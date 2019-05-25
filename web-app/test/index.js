const datas = require('./result')
const axios = require('axios')
const fs = require('fs')
const path = require('path')

function buildFullName (data, datas) {
  let parents = []
  let findParent = (data, datas, collector) => {
    let [parent] = datas.filter(item => item.code_ === data.parent_code_)
    if (parent !== null && parent !== undefined) {
      collector.push(parent)
      findParent(parent, datas, collector)
    }
  }
  findParent(data, datas, parents)
  return parents.map(item => item.name_).reverse().concat(data.name_).join('')
}

function getRegion (data) {
  return axios.get('http://api.map.baidu.com/geocoder/v2/', {
    params: {
      address: data.fullname,
      output: 'json',
      ak: 'SaG8ZDsPZVd14syOmAdwL9BGZq2u3DIw',
      'ret_coordtype': 'gcj02ll'
    }
  })
}

function f (datas) {
  return new Promise((resolve, reject) => {
    let result = []

    function rebuildRegion (datas) {
      if (datas.length > 0) {
        let data = datas.shift()
        return getRegion(data).then(({ data: { result: { location } } }) => {
          // console.log('get regionInfo', data)
          result.push({ ...data, ...location })
          rebuildRegion(datas)
        }).catch(e => {
          console.error('error', data, e)
          rebuildRegion(datas)
        })
      } else {
        resolve(result)
      }
    }

    rebuildRegion(datas)
  })
}

let allData = datas.map(data => ({
  ...data,
  fullname: buildFullName(data, datas)
}))

let data1 = allData.slice(0, 500)
let data2 = allData.slice(500, 1000)
let data3 = allData.slice(1000, 1500)
let data4 = allData.slice(1500, 2000)
let data5 = allData.slice(2000)

Promise.all([f(data1), f(data2), f(data3), f(data4), f(data5)])
  .then(([r1, r2, r3, r4, r5]) => {
    fs.writeFile(path.resolve(__dirname, 'data.json'), JSON.stringify([...r1, ...r2, ...r3, ...r4, ...r5]), 'utf8', err => {
      console.log('success')
    })
  })

// f(allData).then(res => {
//   fs.writeFile(path.resolve(__dirname, 'regions.json'), JSON.stringify(res), 'utf8')
//   console.log('success')
// })
