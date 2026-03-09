<template>
  <div>
    <div class="page-header">
      <h1>统计详情</h1>
      <router-link to="/metric" class="btn">返回列表</router-link>
    </div>
    <div v-if="metric" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ metric.id }}</td></tr>
        <tr><th>名称</th><td>{{ metric.name }}</td></tr>
        <tr v-for="(v, k) in metric" :key="k" v-show="!['id','name'].includes(k)">
          <th>{{ k }}</th><td>{{ v }}</td>
        </tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>统计项不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getMetricById } from '@/api/metric'

const route = useRoute()
const metric = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const res = await getMetricById(id)
    metric.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
