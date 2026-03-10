import client from './client'

/** 与 PHP/Java 一致：params 支持 mode、product、status、branch、pageID、recPerPage；分页时返回 pager */
export function getProductList(params = {}) {
  return client.get('/api/product/list', { params }).then((r) => r.data)
}

/** params: { mode?, programID? }；未传的项不加入 query，后端 mode 默认 all、programID 默认 0 */
export function getProductPairs(params = {}) {
  const { mode, programID } = params ?? {}
  const query = {}
  if (mode != null && mode !== '') query.mode = mode
  if (programID != null && programID !== '') query.programID = programID
  return client.get('/api/product/pairs', { params: query }).then((r) => r.data)
}

/** 按产品 ID 列表返回 id→name；ids 逗号分隔或数组 */
export function getProductPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/product/pairsByList', { params: { ids: param } }).then((r) => r.data)
}

export function getProductById(id) {
  return client.get(`/api/product/${id}`).then((r) => r.data)
}

/** 产品动态；params: { pageID?, recPerPage? }，与 Java ProductController.dynamic 一致 */
export function getProductDynamic(id, params = {}) {
  return client.get(`/api/product/${id}/dynamic`, { params }).then((r) => r.data)
}

/** 产品路线图（计划与发布）；params: { branch? }，与 Java ProductController.roadmap 一致 */
export function getProductRoadmap(id, params = {}) {
  return client.get(`/api/product/${id}/roadmap`, { params }).then((r) => r.data)
}

export function createProduct(data) {
  return client.post('/api/product', data).then((r) => r.data)
}

export function updateProduct(id, data) {
  return client.put(`/api/product/${id}`, data).then((r) => r.data)
}

export function deleteProduct(id) {
  return client.delete(`/api/product/${id}`).then((r) => r.data)
}

export function closeProduct(id) {
  return client.put(`/api/product/${id}/close`).then((r) => r.data)
}

export function activateProduct(id) {
  return client.put(`/api/product/${id}/activate`).then((r) => r.data)
}

/** 批量编辑产品；body: { productIds: number[], fields: Record<string, any> }，与 Java ProductController.batchEdit 一致 */
export function batchEditProduct(body) {
  return client.post('/api/product/batchEdit', body).then((r) => r.data)
}

/** 更新产品排序；body: { productIDs: number[] }，与 Java ProductController.updateOrder 一致 */
export function updateProductOrder(body) {
  return client.put('/api/product/updateOrder', body).then((r) => r.data)
}

/** 导出产品为 Excel；params: { maxRows? }，触发下载 */
export function exportProduct(params = {}) {
  return client.get('/api/product/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'products.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
