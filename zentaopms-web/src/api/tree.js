import client from './client'

export function getTreeBrowse(params) {
  return client.get('/api/tree/browse', { params }).then((r) => r.data)
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
