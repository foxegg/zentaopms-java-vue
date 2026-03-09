import client from './client'

export function getDeptList() {
  return client.get('/api/dept/list').then((r) => r.data)
}

export function getDeptTree() {
  return client.get('/api/dept/tree').then((r) => r.data)
}

export function getDeptById(id) {
  return client.get(`/api/dept/${id}`).then((r) => r.data)
}
