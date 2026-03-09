import client from './client'

export function getTaskList(params = {}) {
  return client.get('/api/task/list', { params }).then((r) => r.data)
}

export function getTaskByProject(projectId) {
  return client.get(`/api/task/project/${projectId}`).then((r) => r.data)
}

export function getTaskById(id) {
  return client.get(`/api/task/${id}`).then((r) => r.data)
}

export function createTask(data) {
  return client.post('/api/task', data).then((r) => r.data)
}

export function updateTask(id, data) {
  return client.put(`/api/task/${id}`, data).then((r) => r.data)
}
