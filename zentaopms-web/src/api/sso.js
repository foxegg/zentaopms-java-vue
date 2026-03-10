import client from './client'

/** 与 PHP/Java 一致：SSO 为单一配置（zt_config system/sso/setting），非列表 */

export function getSsoSetting() {
  return client.get('/api/sso/setting').then((r) => r.data)
}

export function saveSsoSetting(data) {
  return client.put('/api/sso/setting', data).then((r) => r.data)
}

/** 兼容：list 返回与 setting 相同结构 */
export function getSsoList() {
  return client.get('/api/sso/list').then((r) => r.data)
}

export function getSsoById(id) {
  return client.get(`/api/sso/${id}`).then((r) => r.data)
}

export function createSso(data) {
  return client.post('/api/sso/create', data).then((r) => r.data)
}

export function updateSso(id, data) {
  return client.put(`/api/sso/${id}`, data).then((r) => r.data)
}

export function deleteSso(id) {
  return client.delete(`/api/sso/${id}`).then((r) => r.data)
}
