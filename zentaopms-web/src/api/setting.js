import client from './client'

export function getSettingConfig() {
  return client.get('/api/setting/config').then((r) => r.data)
}
