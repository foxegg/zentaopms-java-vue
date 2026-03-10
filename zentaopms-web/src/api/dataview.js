import client from './client'

export function getDataviewList() {
  return client.get('/api/dataview/list').then((r) => r.data)
}

export function getDataviewById(id) {
  return client.get(`/api/dataview/${id}`).then((r) => r.data)
}

export function createDataview(data) {
  return client.post('/api/dataview', data).then((r) => r.data)
}

export function updateDataview(id, data) {
  return client.put(`/api/dataview/${id}`, data).then((r) => r.data)
}

export function deleteDataview(id) {
  return client.delete(`/api/dataview/${id}`).then((r) => r.data)
}
