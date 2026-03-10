import client from './client'

/** 区块/仪表盘列表；params: { dashboard? }，按当前用户与 dashboard 筛选 */
export function getBlockList(params = {}) {
  return client.get('/api/block/list', { params }).then((r) => r.data)
}

export function getBlockById(id) {
  return client.get(`/api/block/${id}`).then((r) => r.data)
}

export function createBlock(data) {
  return client.post('/api/block', data).then((r) => r.data)
}

export function updateBlock(id, data) {
  return client.put(`/api/block/${id}`, data).then((r) => r.data)
}

export function deleteBlock(id) {
  return client.delete(`/api/block/${id}`).then((r) => r.data)
}
