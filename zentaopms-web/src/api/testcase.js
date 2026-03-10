import client from './client'

/** 与 PHP/Java 一致：params 支持 product、lib、module、branch、type、pageID、recPerPage；分页时返回 pager */
export function getTestCaseList(params = {}) {
  return client.get('/api/testcase/list', { params }).then((r) => r.data)
}

/** 与 PHP/Java 一致：product≤0 时返回空 */
export function getTestCasePairs(product = 0) {
  return client.get('/api/testcase/pairs', { params: { product } }).then((r) => r.data)
}

/** 按用例 ID 列表返回 id→title；ids 逗号分隔或数组 */
export function getTestCasePairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/testcase/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getTestCaseById(id) {
  return client.get('/api/testcase/' + id).then((r) => r.data)
}

export function createTestCase(data) {
  return client.post('/api/testcase', data).then((r) => r.data)
}

export function updateTestCase(id, data) {
  return client.put(`/api/testcase/${id}`, data).then((r) => r.data)
}

export function deleteTestCase(id) {
  return client.delete(`/api/testcase/${id}`).then((r) => r.data)
}

/** 由用例创建缺陷；body: { title?, openedBuild? }，响应体含 id */
export function createBugFromTestCase(id, body = {}) {
  return client.post(`/api/testcase/${id}/createBug`, body).then((r) => r.data)
}

/** 确认需求变更；body: { storyID }，与 Java confirmStoryChange 一致 */
export function confirmTestCaseStoryChange(id, body = {}) {
  return client.put(`/api/testcase/${id}/confirmStoryChange`, body).then((r) => r.data)
}

/** 批量创建；body: TestCase[]，返回 data: id[] */
export function batchCreateTestCase(cases) {
  return client.post('/api/testcase/batchCreate', cases).then((r) => r.data)
}

/** 批量编辑；body: { caseIds: number[], fields: Record<string, any> } */
export function batchEditTestCase(body) {
  return client.post('/api/testcase/batchEdit', body).then((r) => r.data)
}

/** 批量删除；body: { caseIds: number[] } */
export function batchDeleteTestCase(body) {
  return client.post('/api/testcase/batchDelete', body).then((r) => r.data)
}

/** 单个评审；body: { result? } */
export function reviewTestCase(id, body = {}) {
  return client.put(`/api/testcase/${id}/review`, body).then((r) => r.data)
}

/** 批量评审；body: { caseIds: number[], result? } */
export function batchReviewTestCase(body) {
  return client.post('/api/testcase/batchReview', body).then((r) => r.data)
}

/** 批量修改模块；body: { caseIds: number[], moduleID } */
export function batchChangeTestCaseModule(body) {
  return client.post('/api/testcase/batchChangeModule', body).then((r) => r.data)
}

/** 批量修改分支；body: { caseIds: number[], branchID } */
export function batchChangeTestCaseBranch(body) {
  return client.post('/api/testcase/batchChangeBranch', body).then((r) => r.data)
}

/** 批量修改类型；body: { caseIds: number[], type } */
export function batchChangeTestCaseType(body) {
  return client.post('/api/testcase/batchChangeType', body).then((r) => r.data)
}

/** 导出用例为 Excel；params: { product?, maxRows? }，触发下载 */
export function exportTestCase(params = {}) {
  return client.get('/api/testcase/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'testcases.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
