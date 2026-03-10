import client from './client'

/** 与 PHP/Java 一致：objectType、objectID 必填；params 可含 pageID、recPerPage，分页时返回 pager */
export function getActionList(objectType, objectID, params = {}) {
  const query = { objectType, objectID, ...params }
  return client.get('/api/action/list', { params: query }).then((r) => r.data)
}

/** 添加评论 */
export function createComment(data) {
  return client.post('/api/action/comment', data).then((r) => r.data)
}

/** 获取单条记录 */
export function getActionById(id) {
  return client.get(`/api/action/${id}`).then((r) => r.data)
}

/** 编辑评论 */
export function updateComment(id, data) {
  return client.put(`/api/action/${id}`, data).then((r) => r.data)
}
