import client from './client'

/** 与 Java 一致：支持 product、project、execution、status */
export function getTestTaskList(params = {}) {
  return client.get('/api/testtask/list', { params }).then((r) => r.data)
}

/** product/execution 均≤0 时后端返回空 */
export function getTestTaskPairs(product = 0, execution = 0) {
  return client.get('/api/testtask/pairs', { params: { product, execution } }).then((r) => r.data)
}

/** 按测试单 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getTestTaskPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/testtask/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getTestTaskById(id) {
  return client.get('/api/testtask/' + id).then((r) => r.data)
}

export function createTestTask(data) {
  return client.post('/api/testtask', data).then((r) => r.data)
}

export function updateTestTask(id, data) {
  return client.put(`/api/testtask/${id}`, data).then((r) => r.data)
}

export function deleteTestTask(id) {
  return client.delete(`/api/testtask/${id}`).then((r) => r.data)
}

export function startTestTask(id) {
  return client.put(`/api/testtask/${id}/start`).then((r) => r.data)
}

export function closeTestTask(id) {
  return client.put(`/api/testtask/${id}/close`).then((r) => r.data)
}

export function blockTestTask(id) {
  return client.put(`/api/testtask/${id}/block`).then((r) => r.data)
}

export function activateTestTask(id) {
  return client.put(`/api/testtask/${id}/activate`).then((r) => r.data)
}

/** 测试单下的执行结果（用例执行记录） */
export function getTestTaskResults(id) {
  return client.get(`/api/testtask/${id}/results`).then((r) => r.data)
}

/** 测试单关联的用例执行记录，与 results 一致 */
export function getTestTaskCases(id) {
  return client.get(`/api/testtask/${id}/cases`).then((r) => r.data)
}

/** 关联用例；body: { caseID, assignedTo? } */
export function linkTestTaskCase(id, body) {
  return client.post(`/api/testtask/${id}/linkCase`, body).then((r) => r.data)
}

/** 取消关联用例；后端 @RequestParam caseID */
export function unlinkTestTaskCase(id, caseID) {
  return client.post(`/api/testtask/${id}/unlinkCase`, null, { params: { caseID } }).then((r) => r.data)
}

export function batchUnlinkTestTaskCases(id, body) {
  return client.post(`/api/testtask/${id}/batchUnlinkCases`, body).then((r) => r.data)
}

/** 批量指派；body: { assignedTo, caseIds? } */
export function batchAssignTestTask(id, body) {
  return client.post(`/api/testtask/${id}/batchAssign`, body).then((r) => r.data)
}

/** 执行用例记录结果；body: { caseID, result } */
export function runTestTaskCase(id, body) {
  return client.post(`/api/testtask/${id}/runCase`, body).then((r) => r.data)
}
