import client from './client'

export function getBackupList() {
  return client.get('/api/backup/list').then((r) => r.data)
}

export function runBackup() {
  return client.post('/api/backup/backup').then((r) => r.data)
}

export function restoreBackup(data) {
  return client.post('/api/backup/restore', data).then((r) => r.data)
}

/** 按备份 base 名删除（如 2025_01_01）；fileName 会做 encodeURIComponent */
export function deleteBackup(fileName) {
  const encoded = encodeURIComponent(String(fileName))
  return client.delete(`/api/backup/${encoded}`).then((r) => r.data)
}

export function getBackupDiskSpace() {
  return client.get('/api/backup/diskSpace').then((r) => r.data)
}

export function getBackupProgress() {
  return client.get('/api/backup/progress').then((r) => r.data)
}

export function getBackupSetting() {
  return client.get('/api/backup/setting').then((r) => r.data)
}

export function saveBackupSetting(data) {
  return client.put('/api/backup/setting', data).then((r) => r.data)
}
