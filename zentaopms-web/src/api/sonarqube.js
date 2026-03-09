import client from './client'

export function getSonarqubeList() {
  return client.get('/api/sonarqube/list').then((r) => r.data)
}

export function getSonarqubeById(id) {
  return client.get('/api/sonarqube/' + id).then((r) => r.data)
}

export function createSonarqube(data) {
  return client.post('/api/sonarqube', data).then((r) => r.data)
}

export function updateSonarqube(id, data) {
  return client.put('/api/sonarqube/' + id, data).then((r) => r.data)
}

export function deleteSonarqube(id) {
  return client.delete('/api/sonarqube/' + id).then((r) => r.data)
}
