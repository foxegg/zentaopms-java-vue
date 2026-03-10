import client from './client'

/** 与 Java 一致：projectID≤0 时后端返回空列表 */
export function getProjectStoryList(projectID = 0) {
  return client.get('/api/projectstory/list', { params: { projectID } }).then((r) => r.data)
}

/** 批量移除前查询已关联到执行的需求；projectID≤0 返回空；storyIds 逗号分隔或数组 */
export function getProjectStoryExecutionStories(projectID = 0, storyIds = '') {
  const param = Array.isArray(storyIds) ? storyIds.join(',') : (storyIds ?? '')
  return client.get('/api/projectstory/executionStories', { params: { projectID, storyIds: param } }).then((r) => r.data)
}

/** body: { projectID, storyID, productID?, branch? } */
export function linkProjectStory(data) {
  return client.post('/api/projectstory/linkStory', data).then((r) => r.data)
}

/** 批量关联；body: { projectID, storyIds, productID?, branch? } */
export function batchLinkProjectStory(data) {
  return client.post('/api/projectstory/batchLinkStory', data).then((r) => r.data)
}

/** body: { projectID, storyID } */
export function unlinkProjectStory(data) {
  return client.post('/api/projectstory/unlinkStory', data).then((r) => r.data)
}

/** body: { projectID, storyIds } */
export function batchUnlinkProjectStory(data) {
  return client.post('/api/projectstory/batchUnlinkStory', data).then((r) => r.data)
}
