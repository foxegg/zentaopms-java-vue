<template>
  <div>
    <div class="page-header">
      <h1>系统设置</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <pre v-if="config">{{ typeof config === 'object' ? JSON.stringify(config, null, 2) : config }}</pre>
      <p v-else>暂无配置</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSettingConfig } from '@/api/setting'

const config = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSettingConfig()
    config.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
