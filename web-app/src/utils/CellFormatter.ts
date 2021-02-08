import dayjs from 'dayjs'
import isEmpty from 'lodash/isEmpty'

export function formatDate (row: never, column: never, cellValue: Date | string): string {
  return isEmpty(cellValue) ? '' : dayjs(cellValue).format('YYYY-MM-DD')
}

export function formatDateTime (row: never, column: never, cellValue: Date | string): string {
  return isEmpty(cellValue) ? '' : dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
}
