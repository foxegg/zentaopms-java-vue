import client from './client'

/** 与 PHP/Java 一致：未传 product/project 时后端返回全部；branch/type: normal/terminated/all */
export function getReleaseList(params = {}) {
  const raw = typeof params === 'number' ? { product: params } : params
  const product = raw.product
  const project = raw.project
  const branch = raw.branch ?? 'all'
  const type = raw.type ?? 'all'
  const query = { branch, type }
  if (product != null) query.product = product
  if (project != null) query.project = project
  return client.get('/api/release/list', { params: query }).then((r) => r.data)
}

export function getReleaseById(id) {
  return client.get('/api/release/' + id).then((r) => r.data)
}

export function createRelease(data) {
  return client.post('/api/release', data).then((r) => r.data)
}

export function updateRelease(id, data) {
  return client.put(`/api/release/${id}`, data).then((r) => r.data)
}

export function deleteRelease(id) {
  return client.delete(`/api/release/${id}`).then((r) => r.data)
}

export function getReleaseStories(id) {
  return client.get(`/api/release/${id}/stories`).then((r) => r.data)
}

/** 发布关联的缺陷列表 */
export function getReleaseBugs(id) {
  return client.get(`/api/release/${id}/bugs`).then((r) => r.data)
}

/** 变更发布状态；body: { status? } */
export function changeReleaseStatus(id, body = {}) {
  return client.put(`/api/release/${id}/changeStatus`, body).then((r) => r.data)
}

/** 发布发布（上线） */
export function publishRelease(id) {
  return client.put(`/api/release/${id}/publish`).then((r) => r.data)
}

/** 发布通知（占位，可传 body） */
export function notifyRelease(id, body = {}) {
  return client.post(`/api/release/${id}/notify`, body).then((r) => r.data)
}

/** 发布关联需求；body: { storyIds: number[] } */
export function linkReleaseStory(id, body) {
  return client.post(`/api/release/${id}/linkStory`, body).then((r) => r.data)
}

/** 发布取消关联需求；后端 @RequestParam storyID */
export function unlinkReleaseStory(id, storyID) {
  return client.post(`/api/release/${id}/unlinkStory`, null, { params: { storyID } }).then((r) => r.data)
}

export function batchUnlinkReleaseStory(id, body) {
  return client.post(`/api/release/${id}/batchUnlinkStory`, body).then((r) => r.data)
}

/** 发布关联缺陷；body: { bugIds: number[] } */
export function linkReleaseBug(id, body) {
  return client.post(`/api/release/${id}/linkBug`, body).then((r) => r.data)
}

/** 发布取消关联缺陷；后端 @RequestParam bugID */
export function unlinkReleaseBug(id, bugID) {
  return client.post(`/api/release/${id}/unlinkBug`, null, { params: { bugID } }).then((r) => r.data)
}

export function batchUnlinkReleaseBug(id, body) {
  return client.post(`/api/release/${id}/batchUnlinkBug`, body).then((r) => r.data)
}

/** 导出发布为 Excel；params: { product?, maxRows? }，触发下载 */
export function exportRelease(params = {}) {
  return client.get('/api/release/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'releases.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
