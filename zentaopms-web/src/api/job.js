import client from './client'

/** 构建任务列表；params: { repoID?, productID? }，至少传其一且>0 才有数据 */
export function getJobList(params = {}) {
  return client.get('/api/job/list', { params }).then((r) => r.data)
}

export function getJobById(id) {
  return client.get(`/api/job/${id}`).then((r) => r.data)
}

export function createJob(data) {
  return client.post('/api/job', data).then((r) => r.data)
}

export function updateJob(id, data) {
  return client.put(`/api/job/${id}`, data).then((r) => r.data)
}

export function deleteJob(id) {
  return client.delete(`/api/job/${id}`).then((r) => r.data)
}
