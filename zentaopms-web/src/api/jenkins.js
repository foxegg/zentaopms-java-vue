import client from './client'

/** Jenkins API，与 PHP module/jenkins 及 Java JenkinsController 一致（zt_pipeline type=jenkins） */
export function getJenkinsList() {
  return client.get('/api/jenkins/list').then((r) => r.data)
}

export function getJenkinsById(id) {
  return client.get('/api/jenkins/' + id).then((r) => r.data)
}

export function createJenkins(jenkins) {
  return client.post('/api/jenkins', jenkins).then((r) => r.data)
}

export function updateJenkins(id, jenkins) {
  return client.put('/api/jenkins/' + id, jenkins).then((r) => r.data)
}

export function deleteJenkins(id) {
  return client.delete('/api/jenkins/' + id).then((r) => r.data)
}

export function getJenkinsTasks(id, depth = 0) {
  return client.get('/api/jenkins/' + id + '/tasks', { params: { depth } }).then((r) => r.data)
}
