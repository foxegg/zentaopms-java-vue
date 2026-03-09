import client from './client'

export function getFeedbackList() {
  return client.get('/api/feedback/list').then((r) => r.data)
}

export function getFeedbackById(id) {
  return client.get('/api/feedback/' + id).then((r) => r.data)
}

export function createFeedback(data) {
  return client.post('/api/feedback', data).then((r) => r.data)
}

export function updateFeedback(id, data) {
  return client.put('/api/feedback/' + id, data).then((r) => r.data)
}

export function deleteFeedback(id) {
  return client.delete('/api/feedback/' + id).then((r) => r.data)
}
