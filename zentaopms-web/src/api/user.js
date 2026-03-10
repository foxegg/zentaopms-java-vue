import client from './client'

/** 与 PHP/Java 一致：params 支持 account、dept、pageID、recPerPage；返回 data、pager */
export function getUserList(params = {}) {
  return client.get('/api/user/list', { params }).then((r) => r.data)
}

export function getUserPairs(mode = 'nodeleted') {
  return client.get('/api/user/pairs', { params: { mode } }).then((r) => r.data)
}

export function getUserById(id) {
  return client.get(`/api/user/${id}`).then((r) => r.data)
}

export function getUserByAccount(account) {
  return client.get('/api/user/view', { params: { account } }).then((r) => r.data)
}

export function getUserTodo(id, params = {}) {
  return client.get(`/api/user/${id}/todo`, { params }).then((r) => r.data)
}

export function getUserTask(id, params = {}) {
  return client.get(`/api/user/${id}/task`, { params }).then((r) => r.data)
}

export function getUserBug(id, params = {}) {
  return client.get(`/api/user/${id}/bug`, { params }).then((r) => r.data)
}

export function getUserStory(id, params = {}) {
  return client.get(`/api/user/${id}/story`, { params }).then((r) => r.data)
}

export function getUserTesttask(id, params = {}) {
  return client.get(`/api/user/${id}/testtask`, { params }).then((r) => r.data)
}

export function getUserTestcase(id, params = {}) {
  return client.get(`/api/user/${id}/testcase`, { params }).then((r) => r.data)
}

export function getUserExecution(id, params = {}) {
  return client.get(`/api/user/${id}/execution`, { params }).then((r) => r.data)
}

export function getUserDynamic(account, params = {}) {
  return client.get('/api/user/dynamic', { params: { account, ...params } }).then((r) => r.data)
}

export function getUserProfile(id) {
  return client.get(`/api/user/${id}/profile`).then((r) => r.data)
}

export function createUser(user) {
  return client.post('/api/user', user).then((r) => r.data)
}

export function updateUser(id, user) {
  return client.put(`/api/user/${id}`, user).then((r) => r.data)
}

export function deleteUser(id) {
  return client.delete(`/api/user/${id}`).then((r) => r.data)
}

export function batchCreateUsers(users) {
  return client.post('/api/user/batchCreate', users).then((r) => r.data)
}

export function batchEditUsers(body) {
  return client.post('/api/user/batchEdit', body).then((r) => r.data)
}

export function unlockUser(id) {
  return client.put(`/api/user/${id}/unlock`).then((r) => r.data)
}

export function resetUserPassword(id, password) {
  return client.put(`/api/user/${id}/resetPassword`, { password }).then((r) => r.data)
}

export function unbindUser(id) {
  return client.put(`/api/user/${id}/unbind`).then((r) => r.data)
}
