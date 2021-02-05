import dayjs from 'dayjs'

export function formatDatetime (v: string | number | Date, pattern = 'YYYY-MM-DD HH:mm:ss'): string {
  const r = dayjs(v)
  return r.isValid() ? r.format(pattern) : ''
}
