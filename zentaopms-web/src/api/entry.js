import client from './client'

export function getEntryList() {
  return client.get('/api/entry/list').then((r) => r.data)
}

export function getEntryById(id) {
  return client.get(`/api/entry/${id}`).then((r) => r.data)
}

export function createEntry(data) {
  return client.post('/api/entry', data).then((r) => r.data)
}

export function updateEntry(id, data) {
  return client.put(`/api/entry/${id}`, data).then((r) => r.data)
}

export function deleteEntry(id) {
  return client.delete(`/api/entry/${id}`).then((r) => r.data)
}
