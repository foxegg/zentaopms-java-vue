import client from './client'

/** 文档首页：全部文档库列表 */
export function getDocIndex() {
  return client.get('/api/doc/index').then((r) => r.data)
}

/** 我的空间；params: { objectID?, libID?, pageID?, recPerPage? } */
export function getDocMySpace(params = {}) {
  return client.get('/api/doc/mySpace', { params }).then((r) => r.data)
}

/** 文档库下拉；params: { product? }，product≤0 时返回全部 */
export function getDocLibPairs(params = {}) {
  return client.get('/api/doc/libPairs', { params }).then((r) => r.data)
}

export function getDocLibsByProduct(productId) {
  return client.get(`/api/doc/libs/product/${productId}`).then((r) => r.data)
}

export function getDocLibsByProject(projectId) {
  return client.get(`/api/doc/libs/project/${projectId}`).then((r) => r.data)
}

/** 与 PHP/Java 一致：libID≤0 时后端返回空列表；可 getDocList(libID) 或 getDocList({ libID, moduleID }) */
export function getDocList(libIDOrParams, moduleID) {
  const libID = typeof libIDOrParams === 'object' ? (libIDOrParams?.libID ?? 0) : (libIDOrParams ?? 0)
  const modID = typeof libIDOrParams === 'object' ? (libIDOrParams?.moduleID ?? 0) : (moduleID ?? 0)
  return client.get('/api/doc/list', { params: { libID, moduleID: modID } }).then((r) => r.data)
}

export function getDocById(id) {
  return client.get(`/api/doc/${id}`).then((r) => r.data)
}

export function createDoc(data) {
  return client.post('/api/doc', data).then((r) => r.data)
}

export function updateDoc(id, data) {
  return client.put(`/api/doc/${id}`, data).then((r) => r.data)
}

export function deleteDoc(id) {
  return client.delete(`/api/doc/${id}`).then((r) => r.data)
}

/** 批量移动文档；body: { docIds: number[], lib: number, module: number }，lib≤0 后端返回 400 */
export function batchMoveDoc(body) {
  return client.post('/api/doc/batchMoveDoc', body).then((r) => r.data)
}
