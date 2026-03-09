import client from './client'

export function getStoryList(params = {}) {
  return client.get('/api/story/list', { params }).then((r) => r.data)
}

export function getStoryByProduct(productId) {
  return client.get(`/api/story/product/${productId}`).then((r) => r.data)
}

export function getStoryById(id) {
  return client.get(`/api/story/${id}`).then((r) => r.data)
}

export function createStory(data) {
  return client.post('/api/story', data).then((r) => r.data)
}

export function updateStory(id, data) {
  return client.put(`/api/story/${id}`, data).then((r) => r.data)
}
