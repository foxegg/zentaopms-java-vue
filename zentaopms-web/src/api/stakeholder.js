import client from './client'

export function getStakeholderList(objectID, objectType = 'project') {
  return client.get('/api/stakeholder/list', { params: { objectID, objectType } }).then((r) => r.data)
}

export function getStakeholderById(id) {
  return client.get(`/api/stakeholder/${id}`).then((r) => r.data)
}

export function createStakeholder(data) {
  return client.post('/api/stakeholder', data).then((r) => r.data)
}

export function updateStakeholder(id, data) {
  return client.put(`/api/stakeholder/${id}`, data).then((r) => r.data)
}

export function deleteStakeholder(id) {
  return client.delete(`/api/stakeholder/${id}`).then((r) => r.data)
}
