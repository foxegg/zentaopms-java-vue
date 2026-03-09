<template>
  <div>
    <div class="page-header">
      <h1>系统配置</h1>
      <router-link to="/admin" class="btn">返回后台</router-link>
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
import { getAdminConfig } from '@/api/admin'

const config = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getAdminConfig()
    config.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
