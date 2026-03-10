import client from './client'

/** 与 PHP/Java 一致：params 支持 product、project、status、moduleID、assignedTo、severity、pageID、recPerPage；返回 data、pager */
export function getBugList(params = {}) {
  return client.get('/api/bug/list', { params }).then((r) => r.data)
}

/** 与 PHP/Java 一致：product≤0 时返回空 */
export function getBugPairs(product = 0) {
  return client.get('/api/bug/pairs', { params: { product } }).then((r) => r.data)
}

/** 按缺陷 ID 列表返回 id→title；ids 逗号分隔或数组，空时后端返回空 */
export function getBugPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/bug/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getBugByProduct(productId) {
  return client.get(`/api/bug/product/${productId}`).then((r) => r.data)
}

export function getBugById(id) {
  return client.get(`/api/bug/${id}`).then((r) => r.data)
}

export function createBug(data) {
  return client.post('/api/bug', data).then((r) => r.data)
}

export function updateBug(id, data) {
  return client.put(`/api/bug/${id}`, data).then((r) => r.data)
}

export function deleteBug(id) {
  return client.delete(`/api/bug/${id}`).then((r) => r.data)
}

export function resolveBug(id, body = {}) {
  return client.put(`/api/bug/${id}/resolve`, body).then((r) => r.data)
}

export function closeBug(id, body = {}) {
  return client.put(`/api/bug/${id}/close`, body).then((r) => r.data)
}

export function activateBug(id, body = {}) {
  return client.put(`/api/bug/${id}/activate`, body).then((r) => r.data)
}

export function assignBugTo(id, assignedTo) {
  return client.put(`/api/bug/${id}/assignTo`, { assignedTo }).then((r) => r.data)
}

/** 确认缺陷；body: { comment? } */
export function confirmBug(id, body = {}) {
  return client.put(`/api/bug/${id}/confirm`, body).then((r) => r.data)
}

/** 关联需求；body: { storyID }，storyID=0 表示解除关联 */
export function setBugStory(id, body) {
  return client.put(`/api/bug/${id}/setStory`, body).then((r) => r.data)
}

/** 确认需求变更；body: { storyID } */
export function confirmBugStoryChange(id, body) {
  return client.put(`/api/bug/${id}/confirmStoryChange`, body).then((r) => r.data)
}

/** 缺陷关联其它缺陷；body: { bugIds: number[] }，返回当前 bug */
export function linkBugs(id, body) {
  return client.post(`/api/bug/${id}/linkBugs`, body).then((r) => r.data)
}

/** 缺陷取消关联；后端 @RequestParam relatedBugID */
export function unlinkBug(id, relatedBugID) {
  return client.post(`/api/bug/${id}/unlinkBug`, null, { params: { relatedBugID } }).then((r) => r.data)
}

/** 批量指派；body: { bugIds: number[], assignedTo } */
export function batchAssignBugTo(body) {
  return client.post('/api/bug/batchAssignTo', body).then((r) => r.data)
}

/** 批量解决；body: { bugIds: number[], resolution?, resolvedBuild? } */
export function batchResolveBug(body) {
  return client.post('/api/bug/batchResolve', body).then((r) => r.data)
}

/** 批量关闭；body: { bugIds: number[] } */
export function batchCloseBug(body) {
  return client.post('/api/bug/batchClose', body).then((r) => r.data)
}

/** 批量修改模块；body: { bugIds: number[], moduleID } */
export function batchChangeBugModule(body) {
  return client.post('/api/bug/batchChangeModule', body).then((r) => r.data)
}

/** 批量修改计划；body: { bugIds: number[], planID } */
export function batchChangeBugPlan(body) {
  return client.post('/api/bug/batchChangePlan', body).then((r) => r.data)
}

/** 批量确认；body: { bugIds: number[] } */
export function batchConfirmBug(body) {
  return client.post('/api/bug/batchConfirm', body).then((r) => r.data)
}

/** 批量激活；body: { bugIds: number[], openedBuild? } */
export function batchActivateBug(body) {
  return client.post('/api/bug/batchActivate', body).then((r) => r.data)
}

/** 批量创建；body: Bug[]，返回 data: id[] */
export function batchCreateBug(bugs) {
  return client.post('/api/bug/batchCreate', bugs).then((r) => r.data)
}

/** 批量编辑；body: { bugIds: number[], fields: Record<string, any> } */
export function batchEditBug(body) {
  return client.post('/api/bug/batchEdit', body).then((r) => r.data)
}

/** 批量修改分支；body: { bugIds: number[], branchID } */
export function batchChangeBugBranch(body) {
  return client.post('/api/bug/batchChangeBranch', body).then((r) => r.data)
}

/** 缺陷报表；params: { product?, project? } */
export function getBugReport(params = {}) {
  return client.get('/api/bug/report', { params }).then((r) => r.data)
}

/** 导出缺陷为 Excel；params: { product?, project?, status?, assignedTo?, maxRows? }，触发下载 */
export function exportBug(params = {}) {
  return client.get('/api/bug/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'bugs.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
