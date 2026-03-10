import client from './client'

export function getTutorialIndex() {
  return client.get('/api/tutorial/index').then((r) => r.data)
}

export function getTutorialById(id) {
  return client.get(`/api/tutorial/${id}`).then((r) => r.data)
}

export function createTutorial(data) {
  return client.post('/api/tutorial/create', data).then((r) => r.data)
}

export function updateTutorial(id, data) {
  return client.put(`/api/tutorial/${id}`, data).then((r) => r.data)
}

export function deleteTutorial(id) {
  return client.delete(`/api/tutorial/${id}`).then((r) => r.data)
}
