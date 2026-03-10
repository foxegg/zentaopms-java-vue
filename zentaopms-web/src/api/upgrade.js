import client from './client'

export function getUpgradeIndex() {
  return client.get('/api/upgrade/index').then((r) => r.data)
}

export function getUpgradeById(id) {
  return client.get(`/api/upgrade/${id}`).then((r) => r.data)
}

export function createUpgrade(data) {
  return client.post('/api/upgrade/create', data).then((r) => r.data)
}

export function updateUpgrade(id, data) {
  return client.put(`/api/upgrade/${id}`, data).then((r) => r.data)
}

export function deleteUpgrade(id) {
  return client.delete(`/api/upgrade/${id}`).then((r) => r.data)
}
