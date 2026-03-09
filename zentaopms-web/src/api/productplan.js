import client from './client'

export function getProductPlanList(productID) {
  return client.get('/api/productplan/list', { params: { productID } }).then((r) => r.data)
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
