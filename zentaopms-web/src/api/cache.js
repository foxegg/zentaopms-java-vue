import client from './client'

export function getCacheList() {
  return client.get('/api/cache/list').then((r) => r.data)
}

export function getCacheSetting() {
  return client.get('/api/cache/setting').then((r) => r.data)
}

export function saveCacheSetting(data) {
  return client.post('/api/cache/setting', data).then((r) => r.data)
}

export function flushCache() {
  return client.post('/api/cache/flush').then((r) => r.data)
}
