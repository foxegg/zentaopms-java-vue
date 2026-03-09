import client from './client'

export function getDesignList() {
  return client.get('/api/design/list').then((r) => r.data)
}

export function getDesignById(id) {
  return client.get('/api/design/' + id).then((r) => r.data)
}

export function createDesign(data) {
  return client.post('/api/design', data).then((r) => r.data)
}

export function updateDesign(id, data) {
  return client.put('/api/design/' + id, data).then((r) => r.data)
}

export function deleteDesign(id) {
  return client.delete('/api/design/' + id).then((r) => r.data)
}
