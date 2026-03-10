import client from './client'

/** 与 PHP/Java 一致：rootID≤0 时后端返回空列表 */
export function getTreeBrowse(params) {
  return client.get('/api/tree/browse', { params }).then((r) => r.data)
}

/** 与 PHP tree getOptionMenu 一致：rootID≤0 时返回 { 0: '/' }；type: story/bug/case/task/doc */
export function getTreeOptionMenu(rootID = 0, type = 'story') {
  return client.get('/api/tree/optionMenu', { params: { rootID, type } }).then((r) => r.data)
}

/** 与 PHP tree fixModulePath 一致：修复模块 path/grade；rootID≤0 时后端跳过 */
export function fixTree(rootID, type = 'story') {
  return client.post('/api/tree/fix', null, { params: { rootID, type } }).then((r) => r.data)
}

export function getTreeNode(id) {
  return client.get(`/api/tree/${id}`).then((r) => r.data)
}

export function createTree(body) {
  return client.post('/api/tree', body).then((r) => r.data)
}

export function updateTree(id, body) {
  return client.put(`/api/tree/${id}`, body).then((r) => r.data)
}

export function deleteTree(id) {
  return client.delete(`/api/tree/${id}`).then((r) => r.data)
}

export function updateTreeOrder(body) {
  return client.put('/api/tree/updateOrder', body).then((r) => r.data)
}
