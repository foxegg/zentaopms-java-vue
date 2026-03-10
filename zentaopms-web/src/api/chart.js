import client from './client'

export function getChartList() {
  return client.get('/api/chart/list').then((r) => r.data)
}

export function getChartById(id) {
  return client.get(`/api/chart/${id}`).then((r) => r.data)
}

export function createChart(data) {
  return client.post('/api/chart', data).then((r) => r.data)
}

export function updateChart(id, data) {
  return client.put(`/api/chart/${id}`, data).then((r) => r.data)
}

export function deleteChart(id) {
  return client.delete(`/api/chart/${id}`).then((r) => r.data)
}
