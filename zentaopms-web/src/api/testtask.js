import client from './client'

export function getTestTaskList(params) {
  return client.get('/api/testtask/list', { params }).then((r) => r.data)
}

export function getTestTaskById(id) {
  return client.get('/api/testtask/' + id).then((r) => r.data)
}
