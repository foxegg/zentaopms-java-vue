import client from './client'

export function getRepoList() {
  return client.get('/api/repo/list').then((r) => r.data)
}

export function getRepoById(id) {
  return client.get(`/api/repo/${id}`).then((r) => r.data)
}

export function createRepo(data) {
  return client.post('/api/repo', data).then((r) => r.data)
}

export function updateRepo(id, data) {
  return client.put(`/api/repo/${id}`, data).then((r) => r.data)
}

export function deleteRepo(id) {
  return client.delete(`/api/repo/${id}`).then((r) => r.data)
}

export function getRepoLog(id, params = {}) {
  return client.get(`/api/repo/${id}/log`, { params }).then((r) => r.data)
}

export function getRepoMaintain(params = {}) {
  return client.get('/api/repo/maintain', { params }).then((r) => r.data)
}
