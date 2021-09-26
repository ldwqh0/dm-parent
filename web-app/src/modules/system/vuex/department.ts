import { Module } from 'vuex'
import { RootState } from '@/store'
import { DepartmentDto, DepartmentTreeItem } from '@/types/service'
import http from '@/http'
import URLS from '../URLS'
import { listToTree } from '@/utils'
import isNil from 'lodash/isNil'

interface DepartmentState {
  departments: DepartmentDto[]
}

export default {
  namespaced: true,
  state: {
    departments: []
  },
  getters: {
    tree ({ departments }): DepartmentTreeItem[] {
      return listToTree(departments)
    },
    map ({ departments }): { [key: string]: DepartmentDto } {
      return departments.reduce((acc, cur) => ({ ...acc, [`${cur.id}`]: cur }), {} as { [key: string]: DepartmentDto })
    }
  },
  mutations: {
    departments (state: DepartmentState, payload: DepartmentDto[]) {
      state.departments = payload
    },
    department ({ departments }: DepartmentState, payload: DepartmentDto) {
      const r = departments.find(it => it.id === payload.id)
      if (isNil(r)) {
        departments.push(payload)
      } else {
        Object.assign(r, payload)
      }
    }
  },
  actions: {
    loadAll ({ commit }): Promise<DepartmentDto[]> {
      return http.get<DepartmentDto[]>(URLS.department, { params: { scope: 'all' } })
        .then(({ data }) => {
          commit('departments', data)
          return data
        })
    }
  }
} as Module<DepartmentState, RootState>
