import client from './client'

export function getCronList() {
  return client.get('/api/cron/list').then((r) => r.data)
}

export function getCronById(id) {
  return client.get(`/api/cron/${id}`).then((r) => r.data)
}

export function createCron(data) {
  return client.post('/api/cron', data).then((r) => r.data)
}

export function updateCron(id, data) {
  return client.put(`/api/cron/${id}`, data).then((r) => r.data)
}

export function deleteCron(id) {
  return client.delete(`/api/cron/${id}`).then((r) => r.data)
}

export function toggleCron(id, data) {
  return client.put(`/api/cron/${id}/toggle`, data).then((r) => r.data)
}

export function runCron(id) {
  return client.post(`/api/cron/${id}/run`).then((r) => r.data)
}
