import client from './client'

export function getTodoList() {
  return client.get('/api/todo/list').then((r) => r.data)
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

export function batchFinishTodo(todoIds) {
  return client.post('/api/todo/batchFinish', { todoIds }).then((r) => r.data)
}

export function batchCloseTodo(todoIds) {
  return client.post('/api/todo/batchClose', { todoIds }).then((r) => r.data)
}
