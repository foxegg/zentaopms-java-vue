import client from './client'

/** 与 PHP/Java 一致：projectID=0 表示全局组（project 字段为 0）；vision 非空时按 project+vision 过滤 */
export function getGroupList(projectID = 0, vision) {
  const params = { projectID }
  if (vision != null && vision !== '') params.vision = vision
  return client.get('/api/group/list', { params }).then((r) => r.data)
}

/** 与 PHP/Java 一致：projectID=0 为全局组；vision 非空时按 project+vision 过滤 */
export function getGroupPairs(projectID = 0, vision) {
  const params = { projectID }
  if (vision != null && vision !== '') params.vision = vision
  return client.get('/api/group/pairs', { params }).then((r) => r.data)
}

export function getGroupById(id) {
  return client.get(`/api/group/${id}`).then((r) => r.data)
}

/** 需 ADMIN 权限 */
export function createGroup(data) {
  return client.post('/api/group', data).then((r) => r.data)
}

/** 需 ADMIN 权限 */
export function updateGroup(id, data) {
  return client.put(`/api/group/${id}`, data).then((r) => r.data)
}

export function getGroupMembers(id) {
  return client.get(`/api/group/${id}/members`).then((r) => r.data)
}

/** 与 PHP/Java 一致：仅可删除项目权限组（project>0）；全局组删除返回 result: fail, message: 不能删除全局权限组 */
export function deleteGroup(id) {
  return client.delete(`/api/group/${id}`).then((r) => r.data)
}

/** 添加成员；body: { account, project? }；需 ADMIN */
export function addGroupMember(id, body) {
  return client.post(`/api/group/${id}/members`, body).then((r) => r.data)
}

/** 移除成员；需 ADMIN */
export function removeGroupMember(id, account) {
  return client.delete(`/api/group/${id}/members/${encodeURIComponent(account)}`).then((r) => r.data)
}

/** 复制权限组；body: { groupID, options? }；groupID≤0 返回 400；需 ADMIN */
export function copyGroup(body) {
  return client.post('/api/group/copy', body).then((r) => r.data)
}

/** 获取权限组权限配置 */
export function getGroupPrivs(id) {
  return client.get(`/api/group/${id}/priv`).then((r) => r.data)
}

/** 设置权限组权限；body: List<Map<String,String>>；需 ADMIN */
export function setGroupPrivs(id, privs) {
  return client.put(`/api/group/${id}/priv`, privs).then((r) => r.data)
}
