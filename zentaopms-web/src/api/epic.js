import client from './client'

/** Epic 列表（基于 Story type=epic）；params: { product?, pageID?, recPerPage? } */
export function getEpicList(params = {}) {
  return client.get('/api/epic/list', { params }).then((r) => r.data)
}

/** 按产品取 Epic 列表；productId≤0 时后端返回空 */
export function getEpicByProduct(productId) {
  return client.get(`/api/epic/product/${productId}`).then((r) => r.data)
}

export function getEpicById(id) {
  return client.get(`/api/epic/${id}`).then((r) => r.data)
}

export function createEpic(data) {
  return client.post('/api/epic', data).then((r) => r.data)
}

export function updateEpic(id, data) {
  return client.put(`/api/epic/${id}`, data).then((r) => r.data)
}

/** 关闭 Epic；body: { closedReason? } */
export function closeEpic(id, body = {}) {
  return client.put(`/api/epic/${id}/close`, body).then((r) => r.data)
}

export function activateEpic(id) {
  return client.put(`/api/epic/${id}/activate`).then((r) => r.data)
}

export function deleteEpic(id) {
  return client.delete(`/api/epic/${id}`).then((r) => r.data)
}

/** 批量指派；body: { storyIds: number[], assignedTo } */
export function batchAssignEpicTo(body) {
  return client.post('/api/epic/batchAssignTo', body).then((r) => r.data)
}

/** 批量关闭；body: { storyIds: number[], closedReason? } */
export function batchCloseEpic(body) {
  return client.post('/api/epic/batchClose', body).then((r) => r.data)
}
