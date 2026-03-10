import client from './client'

export function getWebhookList() {
  return client.get('/api/webhook/list').then((r) => r.data)
}

export function getWebhookById(id) {
  return client.get(`/api/webhook/${id}`).then((r) => r.data)
}

export function createWebhook(data) {
  return client.post('/api/webhook', data).then((r) => r.data)
}

export function updateWebhook(id, data) {
  return client.put(`/api/webhook/${id}`, data).then((r) => r.data)
}

export function deleteWebhook(id) {
  return client.delete(`/api/webhook/${id}`).then((r) => r.data)
}

export function getWebhookLog(id) {
  return client.get(`/api/webhook/${id}/log`).then((r) => r.data)
}
