import client from './client'

export function getBugList(params = {}) {
  return client.get('/api/bug/list', { params }).then((r) => r.data)
}

export function getBugByProduct(productId) {
  return client.get(`/api/bug/product/${productId}`).then((r) => r.data)
}

export function getBugById(id) {
  return client.get(`/api/bug/${id}`).then((r) => r.data)
}

export function createBug(data) {
  return client.post('/api/bug', data).then((r) => r.data)
}

export function updateBug(id, data) {
  return client.put(`/api/bug/${id}`, data).then((r) => r.data)
}
