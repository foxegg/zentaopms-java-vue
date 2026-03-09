import client from './client'

/**
 * 节假日 API，与 PHP module/holiday 及 Java HolidayController 一致。
 * list: 按年查询，返回 data + yearList + currentYear
 */
export function getHolidayList(params = {}) {
  return client.get('/api/holiday/list', { params }).then((r) => r.data)
}

export function getHolidayYearPairs() {
  return client.get('/api/holiday/yearPairs').then((r) => r.data)
}

export function getHolidayById(id) {
  return client.get('/api/holiday/' + id).then((r) => r.data)
}

export function createHoliday(holiday) {
  return client.post('/api/holiday', holiday).then((r) => r.data)
}

export function updateHoliday(id, holiday) {
  return client.put('/api/holiday/' + id, holiday).then((r) => r.data)
}

export function deleteHoliday(id) {
  return client.delete('/api/holiday/' + id).then((r) => r.data)
}
