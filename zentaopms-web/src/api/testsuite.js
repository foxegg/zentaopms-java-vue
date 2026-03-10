import client from './client'

/** 与 Java 一致：支持 product、project、type */
export function getTestSuiteList(params = {}) {
  return client.get('/api/testsuite/list', { params }).then((r) => r.data)
}

/** product≤0 时后端返回空 */
export function getTestSuitePairs(product = 0) {
  return client.get('/api/testsuite/pairs', { params: { product } }).then((r) => r.data)
}

/** 按套件 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getTestSuitePairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/testsuite/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getTestSuiteById(id) {
  return client.get(`/api/testsuite/${id}`).then((r) => r.data)
}

export function getSuiteCases(id) {
  return client.get(`/api/testsuite/${id}/cases`).then((r) => r.data)
}

export function createTestSuite(body) {
  return client.post('/api/testsuite', body).then((r) => r.data)
}

export function updateTestSuite(id, body) {
  return client.put(`/api/testsuite/${id}`, body).then((r) => r.data)
}

export function deleteTestSuite(id) {
  return client.delete(`/api/testsuite/${id}`).then((r) => r.data)
}

/** 套件添加单个用例；body: { caseId, productId? } */
export function addSuiteCase(id, body) {
  return client.post(`/api/testsuite/${id}/addCase`, body).then((r) => r.data)
}

/** 套件批量关联用例；body: { caseIds, productId?, versions? } */
export function linkSuiteCases(id, body) {
  return client.post(`/api/testsuite/${id}/linkCases`, body).then((r) => r.data)
}

/** 套件移除单个用例 */
export function removeSuiteCase(id, caseId) {
  return client.delete(`/api/testsuite/${id}/removeCase/${caseId}`).then((r) => r.data)
}

/** 套件批量移除用例；body: { caseIds } */
export function batchUnlinkSuiteCases(id, body) {
  return client.post(`/api/testsuite/${id}/batchUnlinkCases`, body).then((r) => r.data)
}
