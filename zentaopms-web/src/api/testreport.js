import client from './client'

export function getTestReportList(params) {
  return client.get('/api/testreport/list', { params }).then((r) => r.data)
}

export function getTestReportById(id) {
  return client.get(`/api/testreport/${id}`).then((r) => r.data)
}

export function createTestReport(body) {
  return client.post('/api/testreport', body).then((r) => r.data)
}

export function updateTestReport(id, body) {
  return client.put(`/api/testreport/${id}`, body).then((r) => r.data)
}

export function deleteTestReport(id) {
  return client.delete(`/api/testreport/${id}`).then((r) => r.data)
}
