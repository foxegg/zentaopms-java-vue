import client from './client'

export function getSystemInfo() {
  return client.get('/api/system/info').then((r) => r.data)
}
