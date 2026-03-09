import client from './client'

export function getExecutionList(projectID) {
  return client.get('/api/execution/list', { params: { projectID } }).then((r) => r.data)
}

export function getExecutionPairs(projectID, mode = 'all') {
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

export function deleteExecution(id) {
  return client.delete(`/api/execution/${id}`).then((r) => r.data)
}

export function getExecutionTasks(id) {
  return client.get(`/api/execution/${id}/task`).then((r) => r.data)
}

export function getExecutionTeam(id) {
  return client.get(`/api/execution/${id}/team`).then((r) => r.data)
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

export function unlinkExecutionStory(executionId, storyID) {
  return client.post(`/api/execution/${executionId}/unlinkStory`, { storyID }).then((r) => r.data)
}

export function batchUnlinkExecutionStory(executionId, storyIds) {
  return client.post(`/api/execution/${executionId}/batchUnlinkStory`, { storyIds }).then((r) => r.data)
}
