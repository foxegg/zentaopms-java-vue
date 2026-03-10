<template>
  <div>
    <div class="page-header">
      <h1>{{ metricLang.viewDetail }}</h1>
      <router-link to="/metric" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="metric" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ metric.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ metric.name }}</td></tr>
        <tr v-for="(v, k) in metric" :key="k" v-show="!['id','name'].includes(k)">
          <th>{{ k }}</th><td>{{ v }}</td>
        </tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getMetricById } from '@/api/metric'
import { common as commonLang, metric as metricLang } from '@/lang/zh-cn'

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
