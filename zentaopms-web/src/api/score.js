import client from './client'

/** 积分记录列表；params: { account?, pageID?, recPerPage? }，返回 data + pager */
export function getScoreList(params = {}) {
  return client.get('/api/score/list', { params }).then((r) => r.data)
}

export function getScoreById(id) {
  return client.get(`/api/score/${id}`).then((r) => r.data)
}
