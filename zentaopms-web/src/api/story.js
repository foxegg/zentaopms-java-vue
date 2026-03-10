import client from './client'

/** 与 PHP/Java 一致：params 支持 product、status、branch、moduleID、pageID、recPerPage；返回 data、pager */
export function getStoryList(params = {}) {
  return client.get('/api/story/list', { params }).then((r) => r.data)
}

/** 与 PHP/Java 一致：product≤0 时返回空；planID>0 按计划过滤，hasParent=false 仅非父需求 */
export function getStoryPairs(product = 0, planID, hasParent) {
  const params = { product }
  if (planID != null && planID > 0) params.planID = planID
  if (hasParent === false) params.hasParent = false
  return client.get('/api/story/pairs', { params }).then((r) => r.data)
}

/** 按需求 ID 列表返回 id→title；ids 逗号分隔或数组，空时后端返回空；withEmptyOption=true 时含 0→""（请选择） */
export function getStoryPairsByList(ids, withEmptyOption) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  const params = { ids: param }
  if (withEmptyOption === true) params.withEmptyOption = true
  return client.get('/api/story/pairsByList', { params }).then((r) => r.data)
}

export function getStoryByProduct(productId) {
  return client.get(`/api/story/product/${productId}`).then((r) => r.data)
}

export function getStoryById(id) {
  return client.get(`/api/story/${id}`).then((r) => r.data)
}

export function createStory(data) {
  return client.post('/api/story', data).then((r) => r.data)
}

export function updateStory(id, data) {
  return client.put(`/api/story/${id}`, data).then((r) => r.data)
}

export function deleteStory(id) {
  return client.delete(`/api/story/${id}`).then((r) => r.data)
}

export function closeStory(id, body = {}) {
  return client.put(`/api/story/${id}/close`, body).then((r) => r.data)
}

export function activateStory(id) {
  return client.put(`/api/story/${id}/activate`).then((r) => r.data)
}

export function assignStoryTo(id, assignedTo) {
  return client.put(`/api/story/${id}/assignTo`, { assignedTo }).then((r) => r.data)
}

/** 需求关联需求（zt_relation）；body: { targetStoryID } */
export function linkStory(id, body) {
  return client.post(`/api/story/${id}/linkStory`, body).then((r) => r.data)
}

/** 需求取消关联需求；后端 @RequestParam targetStoryID */
export function unlinkStory(id, targetStoryID) {
  return client.post(`/api/story/${id}/unlinkStory`, null, { params: { targetStoryID } }).then((r) => r.data)
}

/** 批量指派；body: { storyIds: number[], assignedTo } */
export function batchAssignStoryTo(body) {
  return client.post('/api/story/batchAssignTo', body).then((r) => r.data)
}

/** 批量关闭；body: { storyIds: number[], closedReason? } */
export function batchCloseStory(body) {
  return client.post('/api/story/batchClose', body).then((r) => r.data)
}

/** 批量创建；body: Story[]，返回 data: id[] */
export function batchCreateStory(stories) {
  return client.post('/api/story/batchCreate', stories).then((r) => r.data)
}

/** 批量编辑；body: { storyIds: number[], fields: Record<string, any> } */
export function batchEditStory(body) {
  return client.post('/api/story/batchEdit', body).then((r) => r.data)
}

/** 批量修改计划；body: { storyIds: number[], planID } */
export function batchChangeStoryPlan(body) {
  return client.post('/api/story/batchChangePlan', body).then((r) => r.data)
}

/** 批量修改模块；body: { storyIds: number[], moduleID } */
export function batchChangeStoryModule(body) {
  return client.post('/api/story/batchChangeModule', body).then((r) => r.data)
}

/** 批量修改分支；body: { storyIds: number[], branchID } */
export function batchChangeStoryBranch(body) {
  return client.post('/api/story/batchChangeBranch', body).then((r) => r.data)
}

/** 需求报表数据；params: { product? } */
export function getStoryReport(params = {}) {
  return client.get('/api/story/report', { params }).then((r) => r.data)
}

/** 导出需求为 Excel；params: { product?, maxRows? }，触发下载 */
export function exportStory(params = {}) {
  return client.get('/api/story/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'stories.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
