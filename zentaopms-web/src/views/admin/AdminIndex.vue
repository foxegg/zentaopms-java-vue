<template>
  <div>
    <div class="page-header">
      <h1>后台管理</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <nav class="sub-nav mb-2">
        <router-link to="/admin/config" class="btn">配置</router-link>
        <router-link to="/admin/safe" class="btn">安全</router-link>
        <router-link to="/admin/log" class="btn">日志</router-link>
      </nav>
      <div v-if="data">
        <table class="data-table">
          <tr v-for="(v, k) in data" :key="k"><th>{{ k }}</th><td>{{ v }}</td></tr>
        </table>
      </div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminIndex } from '@/api/admin'

const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getAdminIndex()
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.sub-nav { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
