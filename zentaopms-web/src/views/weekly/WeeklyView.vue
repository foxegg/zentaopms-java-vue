<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ weeklyLang.view }}</h1>
      <router-link to="/weekly" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="data" :to="`/weekly/edit/${data.id}`" class="btn btn-primary">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ weeklyLang.project }}</th><td>{{ data.project }}</td></tr>
        <tr><th>{{ weeklyLang.weekStart }}</th><td>{{ data.weekStart }}</td></tr>
        <tr><th>PV</th><td>{{ data.pv }}</td></tr>
        <tr><th>EV</th><td>{{ data.ev }}</td></tr>
        <tr><th>AC</th><td>{{ data.ac }}</td></tr>
        <tr><th>SV</th><td>{{ data.sv }}</td></tr>
        <tr><th>CV</th><td>{{ data.cv }}</td></tr>
        <tr><th>{{ weeklyLang.progress }}</th><td>{{ data.progress || '-' }}</td></tr>
        <tr><th>{{ weeklyLang.workload }}</th><td>{{ data.workload || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ weeklyLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getWeeklyById } from '@/api/weekly'
import { common as commonLang, weekly as weeklyLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getWeeklyById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
