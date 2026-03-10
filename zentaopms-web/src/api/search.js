import client from './client'

/** 全局搜索；params: { words, module?, limit? }，module 为空或 bug/task/story */
export function searchQuery(params = {}) {
  return client.get('/api/search/query', { params }).then((r) => r.data)
}

/** 获取用户保存的查询列表；与 Java 一致：params 为 { module?, account? } */
export function getSavedQueries(params = {}) {
  const { module, account } = params
  return client.get('/api/search/getQuery', { params: { module, account } }).then((r) => r.data)
}

export function saveQuery(body) {
  return client.post('/api/search/saveQuery', body).then((r) => r.data)
}

/** 删除保存的查询 */
export function deleteQuery(id) {
  return client.delete(`/api/search/deleteQuery/${id}`).then((r) => r.data)
}
