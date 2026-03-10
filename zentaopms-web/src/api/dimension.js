import client from './client'

export function getDimensionList() {
  return client.get('/api/dimension/list').then((r) => r.data)
}

export function getDimensionById(id) {
  return client.get(`/api/dimension/${id}`).then((r) => r.data)
}

export function createDimension(data) {
  return client.post('/api/dimension', data).then((r) => r.data)
}

export function updateDimension(id, data) {
  return client.put(`/api/dimension/${id}`, data).then((r) => r.data)
}

export function deleteDimension(id) {
  return client.delete(`/api/dimension/${id}`).then((r) => r.data)
}
