import client from './client'

/** 与 Java 一致：projectID≤0 时后端返回空列表 */
export function getWeeklyList(projectID = 0) {
  return client.get('/api/weekly/list', { params: { projectID } }).then((r) => r.data)
}

export function getWeeklyById(id) {
  return client.get(`/api/weekly/${id}`).then((r) => r.data)
}

export function createWeekly(data) {
  return client.post('/api/weekly', data).then((r) => r.data)
}

export function updateWeekly(id, data) {
  return client.put(`/api/weekly/${id}`, data).then((r) => r.data)
}

export function deleteWeekly(id) {
  return client.delete(`/api/weekly/${id}`).then((r) => r.data)
}
