<template>
  <div>
    <div class="page-header">
      <h1>{{ metricLang.common }}</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in list" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.name }}</td>
            <td><router-link :to="`/metric/${m.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMetricList } from '@/api/metric'
import { common as commonLang, metric as metricLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getMetricList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
