import client from './client'

export function getSpaceList() {
  return client.get('/api/kanban/space/list').then((r) => r.data)
}

export function getSpaceById(id) {
  return client.get(`/api/kanban/space/${id}`).then((r) => r.data)
}

export function createSpace(body) {
  return client.post('/api/kanban/space', body).then((r) => r.data)
}

export function updateSpace(id, body) {
  return client.put(`/api/kanban/space/${id}`, body).then((r) => r.data)
}

export function deleteSpace(id) {
  return client.delete(`/api/kanban/space/${id}`).then((r) => r.data)
}

/** spaceID≤0 时后端返回空列表 */
export function getKanbanList(spaceID = 0) {
  return client.get('/api/kanban/list', { params: { spaceID } }).then((r) => r.data)
}

export function getKanbanById(id) {
  return client.get(`/api/kanban/${id}`).then((r) => r.data)
}

export function createKanban(body) {
  return client.post('/api/kanban', body).then((r) => r.data)
}

export function updateKanban(id, body) {
  return client.put(`/api/kanban/${id}`, body).then((r) => r.data)
}

export function deleteKanban(id) {
  return client.delete(`/api/kanban/${id}`).then((r) => r.data)
}

export function getKanbanCards(kanbanId) {
  return client.get(`/api/kanban/${kanbanId}/cards`).then((r) => r.data)
}

export function getCardById(id) {
  return client.get(`/api/kanban/card/${id}`).then((r) => r.data)
}

export function createCard(body) {
  return client.post('/api/kanban/card', body).then((r) => r.data)
}

export function updateCard(id, body) {
  return client.put(`/api/kanban/card/${id}`, body).then((r) => r.data)
}

export function deleteCard(id) {
  return client.delete(`/api/kanban/card/${id}`).then((r) => r.data)
}
