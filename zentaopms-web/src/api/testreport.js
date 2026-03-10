import client from './client'

/** 与 Java 一致：支持 project、product、execution */
export function getTestReportList(params = {}) {
  return client.get('/api/testreport/list', { params }).then((r) => r.data)
}

/** product/appendID 均≤0 时后端返回空 */
export function getTestReportPairs(product = 0, appendID = 0) {
  return client.get('/api/testreport/pairs', { params: { product, appendID } }).then((r) => r.data)
}

/** 按报告 ID 列表返回 id→title；ids 逗号分隔或数组 */
export function getTestReportPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/testreport/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getTestReportById(id) {
  return client.get(`/api/testreport/${id}`).then((r) => r.data)
}

export function createTestReport(body) {
  return client.post('/api/testreport', body).then((r) => r.data)
}

export function updateTestReport(id, body) {
  return client.put(`/api/testreport/${id}`, body).then((r) => r.data)
}

export function deleteTestReport(id) {
  return client.delete(`/api/testreport/${id}`).then((r) => r.data)
}
