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

/** 创建代码分支；与 Java repo createBranch 一致；params: { objectID, repoID }（objectID 通常为 taskID） */
export function createRepoBranch(params) {
  return client.post('/api/repo/createBranch', null, { params }).then((r) => r.data)
}

/** 解除代码分支关联；与 Java repo unlinkBranch 一致；objectID 通常为 taskID */
export function unlinkRepoBranch(objectID) {
  return client.post('/api/repo/unlinkBranch', null, { params: { objectID } }).then((r) => r.data)
}

export function getRepoLog(id, params = {}) {
  return client.get(`/api/repo/${id}/log`, { params }).then((r) => r.data)
}

export function getRepoMaintain(params = {}) {
  return client.get('/api/repo/maintain', { params }).then((r) => r.data)
}
