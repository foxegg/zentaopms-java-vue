import client from './client'

/** 与 Java 一致：支持 status(unclosed/closed)、pageID、recPerPage；返回 data + pager */
export function getProgramList(params = {}) {
  return client.get('/api/program/list', { params }).then((r) => r.data)
}

/** 项目集下拉 */
export function getProgramPairs() {
  return client.get('/api/program/pairs').then((r) => r.data)
}

/** 按项目集 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getProgramPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/program/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getProgramById(id) {
  return client.get(`/api/program/${id}`).then((r) => r.data)
}

export function createProgram(body) {
  return client.post('/api/program', body).then((r) => r.data)
}

export function updateProgram(id, body) {
  return client.put(`/api/program/${id}`, body).then((r) => r.data)
}

export function deleteProgram(id) {
  return client.delete(`/api/program/${id}`).then((r) => r.data)
}
