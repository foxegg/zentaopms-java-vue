import client from './client'

export function getMetricList() {
  return client.get('/api/metric/list').then((r) => r.data)
}

export function getMetricById(id) {
  return client.get(`/api/metric/${id}`).then((r) => r.data)
}

/** 与 Java MetricController 一致 */
export function createMetric(data) {
  return client.post('/api/metric', data).then((r) => r.data)
}

export function updateMetric(id, data) {
  return client.put(`/api/metric/${id}`, data).then((r) => r.data)
}

export function deleteMetric(id) {
  return client.delete(`/api/metric/${id}`).then((r) => r.data)
}
