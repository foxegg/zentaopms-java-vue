import client from './client'

export function getBranchList(params) {
  return client.get('/api/branch/list', { params }).then((r) => r.data)
}

export function getBranchById(id) {
  return client.get(`/api/branch/${id}`).then((r) => r.data)
}

export function createBranch(body) {
  return client.post('/api/branch', body).then((r) => r.data)
}

export function updateBranch(id, body) {
  return client.put(`/api/branch/${id}`, body).then((r) => r.data)
}

export function deleteBranch(id) {
  return client.delete(`/api/branch/${id}`).then((r) => r.data)
}
