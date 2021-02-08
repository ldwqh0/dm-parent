import dayjs from 'dayjs'

export function formatDate (v: string | Date | number, pattern = 'YYYY-MM-DD'): string {
  return dayjs(v).format(pattern)
}
