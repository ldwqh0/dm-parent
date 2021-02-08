import { TreeNode } from 'element-ui/types/tree'

export interface TreeProps<K, T> {
  label: ((data: T, node: TreeNode<K, T>) => string) | string;
  disabled?: ((data: T, node: TreeNode<K, T>) => boolean) | string;
  isLeaf?: ((data: T, node: TreeNode<K, T>) => boolean) | string;
  children: string;
}
