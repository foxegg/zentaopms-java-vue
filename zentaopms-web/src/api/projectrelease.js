import client from './client'

/** 与 Java 一致：projectID≤0 时后端返回空列表 */
export function getProjectReleaseList(projectID = 0) {
  return client.get('/api/projectrelease/list', { params: { projectID } }).then((r) => r.data)
}

export function getProjectReleaseById(id) {
  return client.get(`/api/projectrelease/${id}`).then((r) => r.data)
}

export function createProjectRelease(data) {
  return client.post('/api/projectrelease', data).then((r) => r.data)
}

export function updateProjectRelease(id, data) {
  return client.put(`/api/projectrelease/${id}`, data).then((r) => r.data)
}

export function deleteProjectRelease(id) {
  return client.delete(`/api/projectrelease/${id}`).then((r) => r.data)
}
