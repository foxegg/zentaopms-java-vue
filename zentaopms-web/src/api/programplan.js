import client from './client'

/** 与 Java 一致：params 为 { projectID?, executionID? }；executionID>0 返回该执行，projectID>0 返回项目下执行列表，否则空 */
export function getProgramplanList(params = {}) {
  return client.get('/api/programplan/list', { params }).then((r) => r.data)
}

export function getProgramplanById(id) {
  return client.get(`/api/programplan/${id}`).then((r) => r.data)
}
