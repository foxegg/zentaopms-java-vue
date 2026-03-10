import client from './client'

/** 与 PHP/Java 一致：按 parentID 查询，parentID=0 为顶级部门 */
export function getDeptList(parentID = 0) {
  return client.get('/api/dept/list', { params: { parentID } }).then((r) => r.data)
}

/** 浏览部门（与 list 同接口）；params: { parentID? } */
export function getDeptBrowse(params = {}) {
  return client.get('/api/dept/browse', { params }).then((r) => r.data)
}

export function getDeptTree() {
  return client.get('/api/dept/tree').then((r) => r.data)
}

export function getDeptById(id) {
  return client.get(`/api/dept/${id}`).then((r) => r.data)
}

/** 需 ADMIN 权限 */
export function createDept(data) {
  return client.post('/api/dept', data).then((r) => r.data)
}

/** 需 ADMIN 权限 */
export function updateDept(id, data) {
  return client.put(`/api/dept/${id}`, data).then((r) => r.data)
}

/** 批量更新部门排序；body: [{ id, order }, ...]；需 ADMIN */
export function updateDeptOrder(orders) {
  return client.put('/api/dept/updateOrder', orders).then((r) => r.data)
}

/** 与 PHP 一致：id≤0 时后端返回 data: [] */
export function getDeptUsers(id) {
  return client.get(`/api/dept/${id}/users`).then((r) => r.data)
}

/** 与 PHP/Java 一致：有子部门或部门下有人时后端返回 400、result: fail、message 说明原因 */
export function deleteDept(id) {
  return client.delete(`/api/dept/${id}`).then((r) => r.data)
}
