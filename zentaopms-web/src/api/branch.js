import client from './client'

/** 与 Java 一致：productID≤0 时后端返回空列表；可 getBranchList() 或 getBranchList({ productID }) */
export function getBranchList(params = {}) {
  const productID = typeof params === 'number' ? params : (params?.productID ?? 0)
  return client.get('/api/branch/list', { params: { productID } }).then((r) => r.data)
}

/** productID≤0 时后端返回空 */
export function getBranchPairs(productID = 0) {
  return client.get('/api/branch/pairs', { params: { productID } }).then((r) => r.data)
}

/** 按分支 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getBranchPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/branch/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getBranchById(id) {
  return client.get(`/api/branch/${id}`).then((r) => r.data)
}

export function createBranch(body) {
  return client.post('/api/branch', body).then((r) => r.data)
}

export function updateBranch(id, body) {
  return client.put(`/api/branch/${id}`, body).then((r) => r.data)
}

export function deleteBranch(id) {
  return client.delete(`/api/branch/${id}`).then((r) => r.data)
}
