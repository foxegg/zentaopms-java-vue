import client from './client'

/** 与 PHP/Java 一致：projectID 未传或≤0 时后端返回空列表；type: sprint/stage/kanban/all；status: wait/doing/done/closed/undone/delayed/all */
export function getExecutionList(projectID = 0, params = {}) {
  const { type = 'all', status = 'all' } = params
  return client.get('/api/execution/list', { params: { projectID, type, status } }).then((r) => r.data)
}

/** 与 PHP 一致：projectID≤0 时返回空 */
export function getExecutionPairs(projectID = 0, mode = 'all') {
  return client.get('/api/execution/pairs', { params: { projectID, mode } }).then((r) => r.data)
}

export function getExecutionById(id) {
  return client.get(`/api/execution/${id}`).then((r) => r.data)
}

export function createExecution(body) {
  return client.post('/api/execution', body).then((r) => r.data)
}

export function updateExecution(id, body) {
  return client.put(`/api/execution/${id}`, body).then((r) => r.data)
}

export function startExecution(id) {
  return client.put(`/api/execution/${id}/start`).then((r) => r.data)
}

export function closeExecution(id) {
  return client.put(`/api/execution/${id}/close`).then((r) => r.data)
}

export function suspendExecution(id) {
  return client.put(`/api/execution/${id}/suspend`).then((r) => r.data)
}

export function activateExecution(id) {
  return client.put(`/api/execution/${id}/activate`).then((r) => r.data)
}

export function putoffExecution(id, body = {}) {
  return client.put(`/api/execution/${id}/putoff`, body).then((r) => r.data)
}

export function deleteExecution(id) {
  return client.delete(`/api/execution/${id}`).then((r) => r.data)
}

export function getExecutionTasks(id) {
  return client.get(`/api/execution/${id}/task`).then((r) => r.data)
}

export function getExecutionTeam(id) {
  return client.get(`/api/execution/${id}/team`).then((r) => r.data)
}

/** 执行概览：tasks、builds、testTasks、team 聚合 */
export function getExecutionSummary(id) {
  return client.get(`/api/execution/${id}/summary`).then((r) => r.data)
}

export function getExecutionDynamic(id, params = {}) {
  return client.get(`/api/execution/${id}/dynamic`, { params }).then((r) => r.data)
}

export function manageExecutionMembers(executionId, members) {
  return client.post(`/api/execution/${executionId}/manageMembers`, { members }).then((r) => r.data)
}

export function getExecutionStories(id) {
  return client.get(`/api/execution/${id}/stories`).then((r) => r.data)
}

export function linkExecutionStory(executionId, body) {
  return client.post(`/api/execution/${executionId}/linkStory`, body).then((r) => r.data)
}

export function batchLinkExecutionStory(executionId, body) {
  return client.post(`/api/execution/${executionId}/batchLinkStory`, body).then((r) => r.data)
}

export function unlinkExecutionStory(executionId, storyID) {
  return client.post(`/api/execution/${executionId}/unlinkStory`, { storyID }).then((r) => r.data)
}

export function batchUnlinkExecutionStory(executionId, storyIds) {
  return client.post(`/api/execution/${executionId}/batchUnlinkStory`, { storyIds }).then((r) => r.data)
}

/** 批量编辑执行；body: { executionIds: number[], fields: Record<string, any> } */
export function batchEditExecution(body) {
  return client.post('/api/execution/batchEdit', body).then((r) => r.data)
}
