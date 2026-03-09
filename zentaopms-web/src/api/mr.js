import client from './client'

export function getMrList() {
  return client.get('/api/mr/list').then((r) => r.data)
}

export function getMrById(id) {
  return client.get('/api/mr/' + id).then((r) => r.data)
}

export function createMr(data) {
  return client.post('/api/mr', data).then((r) => r.data)
}

export function updateMr(id, data) {
  return client.put('/api/mr/' + id, data).then((r) => r.data)
}

export function deleteMr(id) {
  return client.delete('/api/mr/' + id).then((r) => r.data)
}
