import client from './client'

export function getTestSuiteList(params) {
  return client.get('/api/testsuite/list', { params }).then((r) => r.data)
}

export function getTestSuiteById(id) {
  return client.get(`/api/testsuite/${id}`).then((r) => r.data)
}

export function getSuiteCases(id) {
  return client.get(`/api/testsuite/${id}/cases`).then((r) => r.data)
}

export function createTestSuite(body) {
  return client.post('/api/testsuite', body).then((r) => r.data)
}

export function updateTestSuite(id, body) {
  return client.put(`/api/testsuite/${id}`, body).then((r) => r.data)
}

export function deleteTestSuite(id) {
  return client.delete(`/api/testsuite/${id}`).then((r) => r.data)
}
