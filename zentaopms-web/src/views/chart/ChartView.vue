<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ chartLang.common }}：{{ data?.name || '-' }}</h1>
      <router-link to="/chart" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="data" :to="`/chart/edit/${data.id}`" class="btn btn-primary">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ data.name }}</td></tr>
        <tr><th>{{ chartLang.code }}</th><td>{{ data.code || '-' }}</td></tr>
        <tr><th>{{ projectLang.type }}</th><td>{{ data.type || '-' }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ data.stage || 'draft' }}</td></tr>
        <tr><th>{{ productLang.desc }}</th><td>{{ data.description || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getChartById } from '@/api/chart'
import { common as commonLang, chart as chartLang, project as projectLang, product as productLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getChartById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
