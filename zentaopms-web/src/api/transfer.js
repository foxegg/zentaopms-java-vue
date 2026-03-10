import client from './client'

export function getTransferList() {
  return client.get('/api/transfer/list').then((r) => r.data)
}

export function getTransferById(id) {
  return client.get(`/api/transfer/${id}`).then((r) => r.data)
}

export function createTransfer(data) {
  return client.post('/api/transfer/create', data).then((r) => r.data)
}

export function updateTransfer(id, data) {
  return client.put(`/api/transfer/${id}`, data).then((r) => r.data)
}

export function deleteTransfer(id) {
  return client.delete(`/api/transfer/${id}`).then((r) => r.data)
}
