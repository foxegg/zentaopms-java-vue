import client from './client'

export function getPipelineList() {
  return client.get('/api/pipeline/list').then((r) => r.data)
}

export function getPipelineById(id) {
  return client.get(`/api/pipeline/${id}`).then((r) => r.data)
}

export function createPipeline(data) {
  return client.post('/api/pipeline', data).then((r) => r.data)
}

export function updatePipeline(id, data) {
  return client.put(`/api/pipeline/${id}`, data).then((r) => r.data)
}

export function deletePipeline(id) {
  return client.delete(`/api/pipeline/${id}`).then((r) => r.data)
}
