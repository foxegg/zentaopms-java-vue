import client from './client'

export function getMyIndex() {
  return client.get('/api/my/index').then((r) => r.data)
}

export function getMyTodo() {
  return client.get('/api/my/todo').then((r) => r.data)
}

export function getMyTask() {
  return client.get('/api/my/task').then((r) => r.data)
}

export function getMyBug() {
  return client.get('/api/my/bug').then((r) => r.data)
}

export function getMyStory() {
  return client.get('/api/my/story').then((r) => r.data)
}

export function getMyDynamic(params = {}) {
  return client.get('/api/my/dynamic', { params }).then((r) => r.data)
}

export function getMyScore(params = {}) {
  return client.get('/api/my/score', { params }).then((r) => r.data)
}

export function getMyWork() {
  return client.get('/api/my/work').then((r) => r.data)
}

export function getMyCalendar(params = {}) {
  return client.get('/api/my/calendar', { params }).then((r) => r.data)
}

export function getMyProfile() {
  return client.get('/api/my/profile').then((r) => r.data)
}

export function getMyPreference() {
  return client.get('/api/my/preference').then((r) => r.data)
}

export function updateMyPreference(preference) {
  return client.put('/api/my/preference', preference).then((r) => r.data)
}

export function changePassword(oldPassword, newPassword) {
  return client.put('/api/my/changePassword', { oldPassword, newPassword }).then((r) => r.data)
}

export function updateMyProfile(profile) {
  return client.put('/api/my/profile', profile).then((r) => r.data)
}
