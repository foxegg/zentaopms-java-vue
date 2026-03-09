import client from './client'

export function getCompanyList() {
  return client.get('/api/company/list').then((r) => r.data)
}

export function getCompanyBrowse() {
  return client.get('/api/company/browse').then((r) => r.data)
}

export function getCompanyById(id) {
  return client.get(`/api/company/${id}`).then((r) => r.data)
}
