import client from './client'

export function getAdminIndex() {
  return client.get('/api/admin/index').then((r) => r.data)
}

export function getAdminConfig() {
  return client.get('/api/admin/config').then((r) => r.data)
}

/** 保存系统配置（system/common/global）；body: { key: value, ... } */
export function saveAdminConfig(configs) {
  return client.put('/api/admin/config', configs ?? {}).then((r) => r.data)
}

export function getAdminSafe() {
  return client.get('/api/admin/safe').then((r) => r.data)
}

export function getAdminLog(params = {}) {
  return client.get('/api/admin/log', { params }).then((r) => r.data)
}

/** 删除过期日志；params: { days? }，未传时后端默认 30 天 */
export function deleteAdminLog(days) {
  return client.delete('/api/admin/log', { params: days != null ? { days } : {} }).then((r) => r.data)
}

/** 将需求 linkStories 同步到 zt_relation 表（与 PHP 一致的双向关联）；返回同步条数 */
export function syncLinkStoriesToRelation() {
  return client.post('/api/admin/syncLinkStoriesToRelation').then((r) => r.data)
}
