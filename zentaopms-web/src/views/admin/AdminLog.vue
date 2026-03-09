<template>
  <div>
    <div class="page-header">
      <h1>日志</h1>
      <router-link to="/admin" class="btn">返回后台</router-link>
    </div>
    <div v-if="!loading" class="table-wrap">
      <pre v-if="data">{{ typeof data === 'object' ? JSON.stringify(data, null, 2) : data }}</pre>
      <p v-else>暂无日志</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminLog } from '@/api/admin'

const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getAdminLog()
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
