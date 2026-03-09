<template>
  <div>
    <div class="page-header">
      <h1>系统信息</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <table class="data-table" v-if="info">
        <tr v-for="(v, k) in info" :key="k"><th>{{ k }}</th><td>{{ v }}</td></tr>
      </table>
      <p v-else>暂无信息</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSystemInfo } from '@/api/system'

const info = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSystemInfo()
    info.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
