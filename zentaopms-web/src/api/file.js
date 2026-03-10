import client from './client'

/** 获取对象下的文件列表 */
export function getFileList(objectType, objectID) {
  return client.get('/api/file/list', { params: { objectType, objectID } }).then((r) => r.data)
}

/** 获取文件详情 */
export function getFileById(id) {
  return client.get(`/api/file/${id}`).then((r) => r.data)
}

/** 上传文件 (使用 FormData: file, objectType?, objectID?, extra?) */
export function uploadFile(formData) {
  return client.post('/api/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }).then((r) => r.data)
}

/** 删除文件 */
export function deleteFile(id) {
  return client.delete(`/api/file/${id}`).then((r) => r.data)
}

/** 下载文件 (blob，前端可创建 a.href 触发保存) */
export function downloadFile(id, filename) {
  return client.get(`/api/file/${id}/download`, { responseType: 'blob' }).then((res) => {
    const name = filename || res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'download'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res
  })
}

/** 预览文件（inline，返回 blob；可用于 img/iframe 的 src） */
export function previewFile(id) {
  return client.get(`/api/file/${id}/preview`, { responseType: 'blob' }).then((r) => r.data)
}
