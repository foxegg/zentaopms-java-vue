import client from './client'

export function getBuildList(params) {
  return client.get('/api/build/list', { params }).then((r) => r.data)
}

export function getBuildByProject(projectID) {
  return client.get('/api/build/projectBuilds', { params: { projectID } }).then((r) => r.data)
}

export function getBuildByProduct(productID) {
  return client.get('/api/build/productBuilds', { params: { productID } }).then((r) => r.data)
}

export function getBuildById(id) {
  return client.get('/api/build/' + id).then((r) => r.data)
}

export function createBuild(data) {
  return client.post('/api/build', data).then((r) => r.data)
}

export function updateBuild(id, data) {
  return client.put(`/api/build/${id}`, data).then((r) => r.data)
}
