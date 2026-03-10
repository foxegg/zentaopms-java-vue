import client from './client'

/** 与 Java 一致：支持 project、product、execution；未传时返回全部 */
export function getBuildList(params = {}) {
  return client.get('/api/build/list', { params }).then((r) => r.data)
}

/** 与 PHP/Java 一致：product≤0 时返回空 */
export function getBuildPairs(product = 0) {
  return client.get('/api/build/pairs', { params: { product } }).then((r) => r.data)
}

/** 按构建 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getBuildPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/build/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getBuildByProject(projectID) {
  return client.get('/api/build/projectBuilds', { params: { projectID } }).then((r) => r.data)
}

export function getBuildByProduct(productID) {
  return client.get('/api/build/productBuilds', { params: { productID } }).then((r) => r.data)
}

/** 按执行获取构建列表；executionID≤0 时后端返回空 */
export function getBuildByExecution(executionID) {
  return client.get('/api/build/executionBuilds', { params: { executionID } }).then((r) => r.data)
}

export function getBuildById(id) {
  return client.get('/api/build/' + id).then((r) => r.data)
}

/** 构建关联的需求列表；id≤0 时后端返回 data: [] */
export function getBuildStories(id) {
  return client.get(`/api/build/${id}/stories`).then((r) => r.data)
}

/** 构建关联的缺陷列表；id≤0 时后端返回 data: [] */
export function getBuildBugs(id) {
  return client.get(`/api/build/${id}/bugs`).then((r) => r.data)
}

export function createBuild(data) {
  return client.post('/api/build', data).then((r) => r.data)
}

export function updateBuild(id, data) {
  return client.put(`/api/build/${id}`, data).then((r) => r.data)
}

/** 构建关联需求；body: { storyIds: number[] } */
export function linkBuildStory(id, body) {
  return client.post(`/api/build/${id}/linkStory`, body).then((r) => r.data)
}

/** 构建取消关联需求；后端 @RequestParam storyID */
export function unlinkBuildStory(id, storyID) {
  return client.post(`/api/build/${id}/unlinkStory`, null, { params: { storyID } }).then((r) => r.data)
}

export function batchUnlinkBuildStory(id, body) {
  return client.post(`/api/build/${id}/batchUnlinkStory`, body).then((r) => r.data)
}

/** 构建关联缺陷；body: { bugIds: number[] } */
export function linkBuildBug(id, body) {
  return client.post(`/api/build/${id}/linkBug`, body).then((r) => r.data)
}

/** 构建取消关联缺陷；后端 @RequestParam bugID */
export function unlinkBuildBug(id, bugID) {
  return client.post(`/api/build/${id}/unlinkBug`, null, { params: { bugID } }).then((r) => r.data)
}

export function batchUnlinkBuildBug(id, body) {
  return client.post(`/api/build/${id}/batchUnlinkBug`, body).then((r) => r.data)
}

/** 导出构建为 Excel；params: { product?, maxRows? }，触发下载 */
export function exportBuild(params = {}) {
  return client.get('/api/build/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'builds.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
