import client from './client'

/** 与 Java 一致：params 为 { owner?, module?, section? }，默认 system/common/global */
export function getSettingConfig(params = {}) {
  const owner = params.owner ?? 'system'
  const module = params.module ?? 'common'
  const section = params.section ?? 'global'
  return client.get('/api/setting/config', { params: { owner, module, section } }).then((r) => r.data)
}

/** 保存配置；params: { owner?, module?, section? }，body: { key: value, ... } */
export function saveSettingConfig(params, configs) {
  const owner = params?.owner ?? 'system'
  const module = params?.module ?? 'common'
  const section = params?.section ?? 'global'
  return client.put('/api/setting/config', configs, { params: { owner, module, section } }).then((r) => r.data)
}
