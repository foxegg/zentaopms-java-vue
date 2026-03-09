import client from './client'

export function getProjectList(params = {}) {
  return client.get('/api/project/list', { params }).then((r) => r.data)
}

export function getProjectAll() {
  return client.get('/api/project/all').then((r) => r.data)
}

export function getProjectById(id) {
  return client.get(`/api/project/${id}`).then((r) => r.data)
}

export function createProject(data) {
  return client.post('/api/project', data).then((r) => r.data)
}

export function updateProject(id, data) {
  return client.put(`/api/project/${id}`, data).then((r) => r.data)
}

export function getProjectTeam(projectId) {
  return client.get(`/api/project/${projectId}/team`).then((r) => r.data)
}

export function manageProjectMembers(projectId, members) {
  return client.post(`/api/project/${projectId}/manageMembers`, { members }).then((r) => r.data)
}

export function getProjectProducts(projectId) {
  return client.get(`/api/project/${projectId}/products`).then((r) => r.data)
}

export function manageProjectProducts(projectId, body) {
  return client.post(`/api/project/${projectId}/manageProducts`, body).then((r) => r.data)
}
