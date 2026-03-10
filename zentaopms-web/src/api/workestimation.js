import client from './client'

/** 工时/预算列表；params: { project? }，project>0 时按项目筛选 */
export function getWorkestimationList(params = {}) {
  return client.get('/api/workestimation/list', { params }).then((r) => r.data)
}

/** 按项目取预算；project≤0 时后端返回 data: null */
export function getWorkestimationBudget(project = 0) {
  return client.get('/api/workestimation/budget', { params: { project } }).then((r) => r.data)
}

export function getWorkestimationById(id) {
  return client.get(`/api/workestimation/${id}`).then((r) => r.data)
}

export function createWorkestimation(data) {
  return client.post('/api/workestimation', data).then((r) => r.data)
}

export function updateWorkestimation(id, data) {
  return client.put(`/api/workestimation/${id}`, data).then((r) => r.data)
}

export function deleteWorkestimation(id) {
  return client.delete(`/api/workestimation/${id}`).then((r) => r.data)
}
