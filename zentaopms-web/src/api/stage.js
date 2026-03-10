import client from './client'

export function getStageList(groupID = 0) {
  return client.get('/api/stage/list', { params: groupID > 0 ? { groupID } : {} }).then((r) => r.data)
}

export function getStageById(id) {
  return client.get(`/api/stage/${id}`).then((r) => r.data)
}

export function createStage(data) {
  return client.post('/api/stage', data).then((r) => r.data)
}

export function updateStage(id, data) {
  return client.put(`/api/stage/${id}`, data).then((r) => r.data)
}

export function deleteStage(id) {
  return client.delete(`/api/stage/${id}`).then((r) => r.data)
}
