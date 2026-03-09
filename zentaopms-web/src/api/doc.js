import client from './client'

export function getDocLibsByProduct(productId) {
  return client.get(`/api/doc/libs/product/${productId}`).then((r) => r.data)
}

export function getDocLibsByProject(projectId) {
  return client.get(`/api/doc/libs/project/${projectId}`).then((r) => r.data)
}

export function getDocList(libID) {
  return client.get('/api/doc/list', { params: { libID } }).then((r) => r.data)
}

export function getDocById(id) {
  return client.get(`/api/doc/${id}`).then((r) => r.data)
}
