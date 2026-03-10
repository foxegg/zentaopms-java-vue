<template>
  <div>
    <div class="page-header">
      <h1>{{ reportLang.title }}</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <div class="mb-2">
        <label>{{ reportLang.year }} </label>
        <select v-model.number="year" @change="load">
          <option v-for="y in yearOptions" :key="y" :value="y">{{ y }}{{ reportLang.yearSuffix }}</option>
        </select>
      </div>
      <table class="data-table">
        <tr><th>{{ reportLang.year }}</th><td>{{ data.year }}</td></tr>
        <tr><th>{{ reportLang.bugs }}</th><td>{{ data.bugs }}</td></tr>
        <tr><th>{{ reportLang.tasks }}</th><td>{{ data.tasks }}</td></tr>
        <tr><th>{{ reportLang.todos }}</th><td>{{ data.todos }}</td></tr>
        <tr><th>{{ reportLang.testTasks }}</th><td>{{ data.testTasks }}</td></tr>
      </table>
      <p class="mt-2">
        <router-link to="/report/remind" class="btn">{{ reportLang.remind }}</router-link>
      </p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAnnualData } from '@/api/report'
import { common as commonLang, report as reportLang } from '@/lang/zh-cn'

const year = ref(new Date().getFullYear())
const yearOptions = ref([])
const data = ref({ year: 0, bugs: 0, tasks: 0, todos: 0, testTasks: 0 })
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getAnnualData(year.value)
    data.value = res.data || data.value
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const y = new Date().getFullYear()
  for (let i = y; i >= y - 5; i--) yearOptions.value.push(i)
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.mt-2 { margin-top: 0.5rem; }
</style>
