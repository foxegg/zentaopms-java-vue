import client from './client'

export function getProjectList(params = {}) {
  return client.get('/api/project/list', { params }).then((r) => r.data)
}

export function getProjectAll() {
  return client.get('/api/project/all').then((r) => r.data)
}

/** 项目下拉；params: { mode?, programID? }，mode 默认 all；programID 未传时不带该参数（后端默认 0） */
export function getProjectPairs(params = {}) {
  const { mode = 'all', programID } = params ?? {}
  const query = { mode }
  if (programID != null && programID !== '') query.programID = programID
  return client.get('/api/project/pairs', { params: query }).then((r) => r.data)
}

/** 按项目 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getProjectPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/project/pairsByList', { params: { ids: param } }).then((r) => r.data)
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

export function deleteProject(id) {
  return client.delete(`/api/project/${id}`).then((r) => r.data)
}

export function startProject(id) {
  return client.put(`/api/project/${id}/start`).then((r) => r.data)
}

export function suspendProject(id) {
  return client.put(`/api/project/${id}/suspend`).then((r) => r.data)
}

export function closeProject(id) {
  return client.put(`/api/project/${id}/close`).then((r) => r.data)
}

export function activateProject(id) {
  return client.put(`/api/project/${id}/activate`).then((r) => r.data)
}

/** 批量编辑项目；body: { projectIds: number[], fields: Record<string, any> } */
export function batchEditProject(body) {
  return client.post('/api/project/batchEdit', body).then((r) => r.data)
}

export function getProjectDynamic(projectId, params = {}) {
  return client.get(`/api/project/${projectId}/dynamic`, { params }).then((r) => r.data)
}

export function getProjectExecutions(projectId) {
  return client.get(`/api/project/${projectId}/executions`).then((r) => r.data)
}

export function getProjectSummary(projectId) {
  return client.get(`/api/project/${projectId}/summary`).then((r) => r.data)
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

/** 导出项目为 Excel；params: { status?, orderBy?, maxRows? }，触发下载 */
export function exportProject(params = {}) {
  return client.get('/api/project/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'projects.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
