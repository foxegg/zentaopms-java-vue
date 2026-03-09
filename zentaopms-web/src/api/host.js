import client from './client'

export function getHostList() {
  return client.get('/api/host/list').then((r) => r.data)
}

export function getHostById(id) {
  return client.get('/api/host/' + id).then((r) => r.data)
}

export function createHost(host) {
  return client.post('/api/host', host).then((r) => r.data)
}

export function updateHost(id, host) {
  return client.put('/api/host/' + id, host).then((r) => r.data)
}

export function deleteHost(id) {
  return client.delete('/api/host/' + id).then((r) => r.data)
}

export function changeHostStatus(id, status, reason) {
  return client.put('/api/host/' + id + '/changeStatus', null, { params: { status, reason } }).then((r) => r.data)
}

export function getHostTreemap(type) {
  return client.get('/api/host/treemap', { params: { type: type || 'serverroom' } }).then((r) => r.data)
}

export function getHostOSOptions(type) {
  return client.get('/api/host/os', { params: { type: type || 'os' } }).then((r) => r.data)
}
