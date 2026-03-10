import client from './client'

export function getServerroomList() {
  return client.get('/api/serverroom/list').then((r) => r.data)
}

export function getServerroomById(id) {
  return client.get(`/api/serverroom/${id}`).then((r) => r.data)
}

export function createServerroom(data) {
  return client.post('/api/serverroom', data).then((r) => r.data)
}

export function updateServerroom(id, data) {
  return client.put(`/api/serverroom/${id}`, data).then((r) => r.data)
}

export function deleteServerroom(id) {
  return client.delete(`/api/serverroom/${id}`).then((r) => r.data)
}
