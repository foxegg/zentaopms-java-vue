import client from './client'

/** 与 Java 一致：productID≤0 时后端返回空列表；可 getProductPlanList(productID) 或 getProductPlanList({ productID, branch? }) */
export function getProductPlanList(productIDOrParams = 0) {
  const productID = typeof productIDOrParams === 'object' ? (productIDOrParams?.productID ?? 0) : (productIDOrParams ?? 0)
  const branch = typeof productIDOrParams === 'object' ? (productIDOrParams?.branch ?? 'all') : 'all'
  return client.get('/api/productplan/list', { params: { productID, branch } }).then((r) => r.data)
}

/** productID≤0 时后端返回空；支持 branch */
export function getProductPlanPairs(productID = 0, branch = 'all') {
  return client.get('/api/productplan/pairs', { params: { productID, branch } }).then((r) => r.data)
}

/** 按计划 ID 列表返回 id→title；ids 逗号分隔或数组 */
export function getProductPlanPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/productplan/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getProductPlanById(id) {
  return client.get(`/api/productplan/${id}`).then((r) => r.data)
}

export function createProductPlan(body) {
  return client.post('/api/productplan', body).then((r) => r.data)
}

export function updateProductPlan(id, body) {
  return client.put(`/api/productplan/${id}`, body).then((r) => r.data)
}

export function getPlanStories(id) {
  return client.get(`/api/productplan/${id}/stories`).then((r) => r.data)
}

/** 计划关联的缺陷列表；id≤0 时后端返回空 */
export function getPlanBugs(id) {
  return client.get(`/api/productplan/${id}/bugs`).then((r) => r.data)
}

export function startPlan(id) {
  return client.put(`/api/productplan/${id}/start`).then((r) => r.data)
}

export function finishPlan(id) {
  return client.put(`/api/productplan/${id}/finish`).then((r) => r.data)
}

export function closePlan(id, closedReason) {
  return client.put(`/api/productplan/${id}/close`, { closedReason }).then((r) => r.data)
}

export function activatePlan(id) {
  return client.put(`/api/productplan/${id}/activate`).then((r) => r.data)
}

export function deletePlan(id) {
  return client.delete(`/api/productplan/${id}`).then((r) => r.data)
}

/** 计划关联单条需求；body: { storyID } */
export function linkPlanStory(id, body) {
  return client.post(`/api/productplan/${id}/linkStory`, body).then((r) => r.data)
}

export function batchLinkPlanStory(id, body) {
  return client.post(`/api/productplan/${id}/batchLinkStory`, body).then((r) => r.data)
}

/** 计划取消关联需求；后端 @RequestParam storyID */
export function unlinkPlanStory(id, storyID) {
  return client.post(`/api/productplan/${id}/unlinkStory`, null, { params: { storyID } }).then((r) => r.data)
}

export function batchUnlinkPlanStory(id, body) {
  return client.post(`/api/productplan/${id}/batchUnlinkStory`, body).then((r) => r.data)
}

/** 计划关联缺陷；body: { bugIds: number[] } */
export function linkPlanBug(id, body) {
  return client.post(`/api/productplan/${id}/linkBug`, body).then((r) => r.data)
}

/** 计划取消关联缺陷；后端 @RequestParam bugID */
export function unlinkPlanBug(id, bugID) {
  return client.post(`/api/productplan/${id}/unlinkBug`, null, { params: { bugID } }).then((r) => r.data)
}

export function batchUnlinkPlanBug(id, body) {
  return client.post(`/api/productplan/${id}/batchUnlinkBug`, body).then((r) => r.data)
}
