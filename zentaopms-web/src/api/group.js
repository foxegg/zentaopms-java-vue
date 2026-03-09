import client from './client'

export function getGroupList() {
  return client.get('/api/group/list').then((r) => r.data)
}

export function getGroupById(id) {
  return client.get(`/api/group/${id}`).then((r) => r.data)
}

export function getGroupMembers(id) {
  return client.get(`/api/group/${id}/members`).then((r) => r.data)
}
