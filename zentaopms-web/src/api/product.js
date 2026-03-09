import client from './client'

export function getProductList() {
  return client.get('/api/product/list').then((r) => r.data)
}

export function getProductPairs(params = {}) {
  return client.get('/api/product/pairs', { params }).then((r) => r.data)
}

export function getProductById(id) {
  return client.get(`/api/product/${id}`).then((r) => r.data)
}

export function createProduct(data) {
  return client.post('/api/product', data).then((r) => r.data)
}

export function updateProduct(id, data) {
  return client.put(`/api/product/${id}`, data).then((r) => r.data)
}
