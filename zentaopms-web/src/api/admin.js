import client from './client'

export function getAdminIndex() {
  return client.get('/api/admin/index').then((r) => r.data)
}

export function getAdminConfig() {
  return client.get('/api/admin/config').then((r) => r.data)
}

export function getAdminSafe() {
  return client.get('/api/admin/safe').then((r) => r.data)
}

export function getAdminLog() {
  return client.get('/api/admin/log').then((r) => r.data)
}
