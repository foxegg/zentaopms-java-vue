import client from './client'

/** 登录；后端返回 { result, token, user: { id, account, realname, role } } */
export function login(account, password) {
  return client.post('/api/auth/login', { account, password }).then((r) => r.data)
}

/** 登出 */
export function logout() {
  return client.post('/api/auth/logout').then((r) => r.data)
}
