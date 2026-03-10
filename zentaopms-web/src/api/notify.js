import client from './client'

/** 与 PHP/Java 一致：params 支持 status、createdBy、pageID、recPerPage；按 createdBy 时返回 pager */
export function getNotifyList(params = {}) {
  return client.get('/api/notify/list', { params }).then((r) => r.data)
}

export function getNotifyById(id) {
  return client.get(`/api/notify/${id}`).then((r) => r.data)
}

export function deleteNotify(id) {
  return client.delete(`/api/notify/${id}`).then((r) => r.data)
}

/** 批量删除；body: { ids: number[] } */
export function batchDeleteNotify(body) {
  return client.delete('/api/notify/batchDelete', { data: body }).then((r) => r.data)
}
