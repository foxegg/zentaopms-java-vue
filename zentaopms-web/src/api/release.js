import client from './client'

export function getReleaseList(product) {
  return client.get('/api/release/list', { params: { product } }).then((r) => r.data)
}

export function getReleaseById(id) {
  return client.get('/api/release/' + id).then((r) => r.data)
}

export function createRelease(data) {
  return client.post('/api/release', data).then((r) => r.data)
}

export function updateRelease(id, data) {
  return client.put(`/api/release/${id}`, data).then((r) => r.data)
}
