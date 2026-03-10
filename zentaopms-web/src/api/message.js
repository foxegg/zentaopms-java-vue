import client from './client'

/** 与后端一致：对应 module/mail，Java 为 MailController /api/mail */
export function getMessageConfig() {
  return client.get('/api/mail/config').then((r) => r.data)
}

export function saveMessageConfig(data) {
  return client.put('/api/mail/config', data).then((r) => r.data)
}

/** 测试邮件发送；body 可选（与 config 同结构） */
export function testMessage(body = {}) {
  return client.post('/api/mail/test', body).then((r) => r.data)
}

/** 邮件队列列表；params: { orderBy?, status?, recTotal?, recPerPage?, pageID? }，status=all|wait|sent|fail */
export function getMessageBrowse(params = {}) {
  return client.get('/api/mail/browse', { params }).then((r) => r.data)
}
