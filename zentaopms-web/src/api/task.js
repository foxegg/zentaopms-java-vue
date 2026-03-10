import client from './client'

export function getTaskList(params = {}) {
  return client.get('/api/task/list', { params }).then((r) => r.data)
}

/** 按任务 ID 列表返回 id→name；ids 逗号分隔或数组，空时后端返回空 */
export function getTaskPairsByList(ids) {
  const param = Array.isArray(ids) ? ids.join(',') : (ids ?? '')
  return client.get('/api/task/pairs', { params: { ids: param } }).then((r) => r.data)
}

export function getTaskByProject(projectId) {
  return client.get(`/api/task/project/${projectId}`).then((r) => r.data)
}

export function getTaskById(id) {
  return client.get(`/api/task/${id}`).then((r) => r.data)
}

export function createTask(data) {
  return client.post('/api/task', data).then((r) => r.data)
}

export function updateTask(id, data) {
  return client.put(`/api/task/${id}`, data).then((r) => r.data)
}

export function deleteTask(id) {
  return client.delete(`/api/task/${id}`).then((r) => r.data)
}

/** 记录工时；body: { date, consumed?, left?, work? } */
export function recordTaskWorkhour(id, body) {
  return client.post(`/api/task/${id}/recordWorkhour`, body).then((r) => r.data)
}

/** 任务工时列表；id≤0 时后端返回空 */
export function getTaskEfforts(id) {
  return client.get(`/api/task/${id}/efforts`).then((r) => r.data)
}

/** 编辑单条工时；body: { consumed?, left?, work?, date? } */
export function updateTaskEffort(estimateId, body) {
  return client.put(`/api/task/effort/${estimateId}`, body).then((r) => r.data)
}

/** 删除单条工时记录 */
export function deleteTaskEffort(estimateId) {
  return client.delete(`/api/task/effort/${estimateId}`).then((r) => r.data)
}

/** 指派任务；body: { assignedTo } */
export function assignTaskTo(id, body) {
  return client.put(`/api/task/${id}/assignTo`, body).then((r) => r.data)
}

export function startTask(id) {
  return client.put(`/api/task/${id}/start`).then((r) => r.data)
}

/** 完成任务；body: { consumed?, left? } */
export function finishTask(id, body = {}) {
  return client.put(`/api/task/${id}/finish`, body).then((r) => r.data)
}

export function pauseTask(id) {
  return client.put(`/api/task/${id}/pause`).then((r) => r.data)
}

export function restartTask(id) {
  return client.put(`/api/task/${id}/restart`).then((r) => r.data)
}

/** 关闭任务；body: { closedReason? } */
export function closeTask(id, body = {}) {
  return client.put(`/api/task/${id}/close`, body).then((r) => r.data)
}

export function cancelTask(id) {
  return client.put(`/api/task/${id}/cancel`).then((r) => r.data)
}

export function activateTask(id) {
  return client.put(`/api/task/${id}/activate`).then((r) => r.data)
}

/** 确认需求变更；body: { storyID? }，与 Java @RequestBody 一致 */
export function confirmTaskStoryChange(id, body = {}) {
  return client.put(`/api/task/${id}/confirmStoryChange`, body).then((r) => r.data)
}

/** 创建代码分支；body: { repoID } */
export function createTaskBranch(id, body) {
  return client.post(`/api/task/${id}/createBranch`, body).then((r) => r.data)
}

/** 取消关联分支 */
export function unlinkTaskBranch(id) {
  return client.post(`/api/task/${id}/unlinkBranch`).then((r) => r.data)
}

/** 批量指派；body: { taskIds: number[], assignedTo } */
export function batchAssignTaskTo(body) {
  return client.post('/api/task/batchAssignTo', body).then((r) => r.data)
}

/** 批量关闭；body: { taskIds: number[] } */
export function batchCloseTask(body) {
  return client.post('/api/task/batchClose', body).then((r) => r.data)
}

/** 批量创建；body: Task[]，返回 data: id[] */
export function batchCreateTask(tasks) {
  return client.post('/api/task/batchCreate', tasks).then((r) => r.data)
}

/** 批量编辑；body: { taskIds: number[], fields: Record<string, any> } */
export function batchEditTask(body) {
  return client.post('/api/task/batchEdit', body).then((r) => r.data)
}

/** 批量修改模块；body: { taskIds: number[], moduleID } */
export function batchChangeTaskModule(body) {
  return client.post('/api/task/batchChangeModule', body).then((r) => r.data)
}

/** 批量取消；body: { taskIds: number[] } */
export function batchCancelTask(body) {
  return client.post('/api/task/batchCancel', body).then((r) => r.data)
}

/** 任务团队管理；params: { executionID, taskID }，body: { members } 等 */
export function manageTaskTeam(params, body) {
  return client.post('/api/task/manageTeam', body, { params }).then((r) => r.data)
}

/** 任务报表；params: { project?, execution? } */
export function getTaskReport(params = {}) {
  return client.get('/api/task/report', { params }).then((r) => r.data)
}

/** 导出任务为 Excel；params: { project?, execution?, assignedTo?, status?, maxRows? }，触发下载 */
export function exportTask(params = {}) {
  return client.get('/api/task/export', { params, responseType: 'blob' }).then((res) => {
    const name = res.headers['content-disposition']?.match(/filename="?([^"]+)/)?.[1] || 'tasks.xlsx'
    const url = URL.createObjectURL(res.data)
    const a = document.createElement('a')
    a.href = url
    a.download = name
    a.click()
    URL.revokeObjectURL(url)
    return res.data
  })
}
