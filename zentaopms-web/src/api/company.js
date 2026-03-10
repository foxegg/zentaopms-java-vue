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

/** 需 ADMIN 权限 */
export function createCompany(data) {
  return client.post('/api/company', data).then((r) => r.data)
}

/** 需 ADMIN 权限 */
export function updateCompany(id, data) {
  return client.put(`/api/company/${id}`, data).then((r) => r.data)
}

/** 与 PHP/Java 一致：该公司下有用户时后端返回 400、result: fail、message 说明原因 */
export function deleteCompany(id) {
  return client.delete(`/api/company/${id}`).then((r) => r.data)
}

/** 公司动态；companyID≤0 时后端返回空列表 */
export function getCompanyDynamic(companyID) {
  return client.get('/api/company/dynamic', { params: { companyID } }).then((r) => r.data)
}
