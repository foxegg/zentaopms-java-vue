import client from './client'

export function getReportIndex() {
  return client.get('/api/report/index').then((r) => r.data)
}

export function getAnnualData(year) {
  return client.get('/api/report/annualData', { params: { year } }).then((r) => r.data)
}

export function getRemind() {
  return client.get('/api/report/remind').then((r) => r.data)
}
