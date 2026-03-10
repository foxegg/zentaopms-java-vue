import client from './client'

export function getRequirementList(params = {}) {
  return client.get('/api/requirement/list', { params }).then((r) => r.data)
}

export function getRequirementByProduct(productId) {
  return client.get(`/api/requirement/product/${productId}`).then((r) => r.data)
}

export function getRequirementById(id) {
  return client.get(`/api/requirement/${id}`).then((r) => r.data)
}

export function createRequirement(data) {
  return client.post('/api/requirement', data).then((r) => r.data)
}

export function updateRequirement(id, data) {
  return client.put(`/api/requirement/${id}`, data).then((r) => r.data)
}

export function deleteRequirement(id) {
  return client.delete(`/api/requirement/${id}`).then((r) => r.data)
}

export function closeRequirement(id, body = {}) {
  return client.put(`/api/requirement/${id}/close`, body).then((r) => r.data)
}

export function activateRequirement(id) {
  return client.put(`/api/requirement/${id}/activate`).then((r) => r.data)
}

/** 批量指派；body: { storyIds: number[], assignedTo }（需求即 story type=requirement） */
export function batchAssignRequirementTo(body) {
  return client.post('/api/requirement/batchAssignTo', body).then((r) => r.data)
}

/** 批量关闭；body: { storyIds: number[], closedReason? } */
export function batchCloseRequirement(body) {
  return client.post('/api/requirement/batchClose', body).then((r) => r.data)
}
