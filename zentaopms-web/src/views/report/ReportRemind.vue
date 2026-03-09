<template>
  <div>
    <div class="page-header">
      <h1>每日提醒</h1>
      <router-link to="/report" class="btn">返回报表</router-link>
    </div>
    <div v-if="!loading" class="table-wrap">
      <p>{{ data.message || '暂无提醒' }}</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRemind } from '@/api/report'

const data = ref({})
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRemind()
    data.value = res.data || {}
  } finally {
    loading.value = false
  }
})
</script>
