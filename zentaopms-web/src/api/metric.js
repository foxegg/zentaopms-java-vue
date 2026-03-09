import client from './client'

export function getMetricList() {
  return client.get('/api/metric/list').then((r) => r.data)
}

export function getMetricById(id) {
  return client.get(`/api/metric/${id}`).then((r) => r.data)
}
