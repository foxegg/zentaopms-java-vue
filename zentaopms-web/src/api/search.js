import client from './client'

export function searchQuery(params) {
  return client.get('/api/search/query', { params }).then((r) => r.data)
}

export function getQuery(id) {
  return client.get('/api/search/getQuery', { params: { id } }).then((r) => r.data)
}

export function saveQuery(body) {
  return client.post('/api/search/saveQuery', body).then((r) => r.data)
}
