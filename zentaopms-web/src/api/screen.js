import client from './client'

export function getScreenList() {
  return client.get('/api/screen/list').then((r) => r.data)
}

export function getScreenById(id) {
  return client.get(`/api/screen/${id}`).then((r) => r.data)
}

export function createScreen(data) {
  return client.post('/api/screen', data).then((r) => r.data)
}

export function updateScreen(id, data) {
  return client.put(`/api/screen/${id}`, data).then((r) => r.data)
}

export function deleteScreen(id) {
  return client.delete(`/api/screen/${id}`).then((r) => r.data)
}
