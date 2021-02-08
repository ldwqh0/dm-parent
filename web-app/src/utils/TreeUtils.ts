import property from 'lodash/property'
import cloneDeep from 'lodash/cloneDeep'
import isNil from 'lodash/isNil'

function listToTree<I, T extends I> (
  list: I[] = [],
  parentProperty = 'parent.id',
  idProperty = 'id',
  childrenProperty = 'children'): T[] {
  const listToUse = list.map(cloneDeep)

  const getObjectKey = (obj: I): string | number => {
    const objectKey = property<I, string | number>(idProperty)(obj)
    if (isNil(objectKey)) {
      throw Error(`the item object lost the id property [${idProperty}]`)
    } else {
      return objectKey
    }
  }
  const getParentKey = property<I, string | number>(parentProperty)

  const addChild = (obj: T, child: T) => {
    const children = (obj as any)[childrenProperty]
    if (isNil(children)) {
      (obj as any)[childrenProperty] = [child]
    } else if (Array.isArray(children)) {
      (children as T[]).push(child)
    } else {
      throw new Error(`the children property [${childrenProperty}]  has been exists,and it's not an array`)
    }
  }
  // 将对象转换为以列表对象中的id为对象的map结构
  const listMap: { [key: string]: T } = listToUse.reduce((acc, cur) => ({
    ...acc,
    [getObjectKey(cur)]: cur
  }), {})
  const root: T[] = []
  for (const item of listToUse) {
    const parent = listMap[getParentKey(item)]
    parent === undefined ? root.push(item as unknown as T) : addChild(parent, item as unknown as T)
  }
  return root
}

export { listToTree }
