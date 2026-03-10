import client from './client'

/** 与 Java 一致：支持 product、project（list 无 type，type 仅用于 pairs） */
export function getCaselibList(params = {}) {
  return client.get('/api/caselib/list', { params }).then((r) => r.data)
}

/** 用例库下拉；type 默认 all */
export function getCaselibPairs(type = 'all') {
  return client.get('/api/caselib/pairs', { params: { type } }).then((r) => r.data)
}

export function getCaselibById(id) {
  return client.get(`/api/caselib/${id}`).then((r) => r.data)
}

export function getCaselibBrowse(id) {
  return client.get(`/api/caselib/${id}/browse`).then((r) => r.data)
}

export function createCaselib(data) {
  return client.post('/api/caselib', data).then((r) => r.data)
}

export function updateCaselib(id, data) {
  return client.put(`/api/caselib/${id}`, data).then((r) => r.data)
}

export function deleteCaselib(id) {
  return client.delete(`/api/caselib/${id}`).then((r) => r.data)
}
