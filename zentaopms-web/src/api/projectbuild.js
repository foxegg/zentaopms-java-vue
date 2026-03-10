import client from './client'

/** 与 Java 一致：projectID≤0 时后端返回空列表 */
export function getProjectBuildList(projectID = 0) {
  return client.get('/api/projectbuild/list', { params: { projectID } }).then((r) => r.data)
}

export function getProjectBuildById(id) {
  return client.get(`/api/projectbuild/${id}`).then((r) => r.data)
}

export function createProjectBuild(data) {
  return client.post('/api/projectbuild', data).then((r) => r.data)
}

export function updateProjectBuild(id, data) {
  return client.put(`/api/projectbuild/${id}`, data).then((r) => r.data)
}

export function deleteProjectBuild(id) {
  return client.delete(`/api/projectbuild/${id}`).then((r) => r.data)
}
