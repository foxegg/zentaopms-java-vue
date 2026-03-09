import client from './client'

export function login(account, password) {
  return client.post('/api/auth/login', { account, password }).then((r) => r.data)
}
