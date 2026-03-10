import client from './client'

/** 与 PHP/Java 一致：params 支持 status（all/wait/done 等），未传时返回全部 */
export function getTodoList(params = {}) {
  return client.get('/api/todo/list', { params }).then((r) => r.data)
}

export function getTodoById(id) {
  return client.get(`/api/todo/${id}`).then((r) => r.data)
}

export function createTodo(body) {
  return client.post('/api/todo', body).then((r) => r.data)
}

export function updateTodo(id, body) {
  return client.put(`/api/todo/${id}`, body).then((r) => r.data)
}

export function startTodo(id) {
  return client.put(`/api/todo/${id}/start`).then((r) => r.data)
}

export function finishTodo(id) {
  return client.put(`/api/todo/${id}/finish`).then((r) => r.data)
}

export function closeTodo(id) {
  return client.put(`/api/todo/${id}/close`).then((r) => r.data)
}

export function activateTodo(id) {
  return client.put(`/api/todo/${id}/activate`).then((r) => r.data)
}

export function assignTodo(id, assignedTo) {
  return client.put(`/api/todo/${id}/assignTo`, { assignedTo }).then((r) => r.data)
}

export function deleteTodo(id) {
  return client.delete(`/api/todo/${id}`).then((r) => r.data)
}

/** 批量添加待办；body: Todo[]，返回 data: id[] */
export function batchCreateTodo(todos) {
  return client.post('/api/todo/batchCreate', todos ?? []).then((r) => r.data)
}

export function batchFinishTodo(todoIds) {
  return client.post('/api/todo/batchFinish', { todoIds }).then((r) => r.data)
}

export function batchCloseTodo(todoIds) {
  return client.post('/api/todo/batchClose', { todoIds }).then((r) => r.data)
}

/** 批量编辑待办；body: { todoIds: number[], fields: Record<string, any> } */
export function batchEditTodo(body) {
  return client.post('/api/todo/batchEdit', body).then((r) => r.data)
}
