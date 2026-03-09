import client from './client'

export function getProgramList() {
  return client.get('/api/program/list').then((r) => r.data)
}

export function getProgramById(id) {
  return client.get(`/api/program/${id}`).then((r) => r.data)
}

export function createProgram(body) {
  return client.post('/api/program', body).then((r) => r.data)
}

export function updateProgram(id, body) {
  return client.put(`/api/program/${id}`, body).then((r) => r.data)
}

export function deleteProgram(id) {
  return client.delete(`/api/program/${id}`).then((r) => r.data)
}
