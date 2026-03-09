import client from './client'

export function getTestCaseList(params) {
  return client.get('/api/testcase/list', { params }).then((r) => r.data)
}

export function getTestCaseById(id) {
  return client.get('/api/testcase/' + id).then((r) => r.data)
}
