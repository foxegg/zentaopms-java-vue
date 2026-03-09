<template>
  <div>
    <div class="page-header">
      <h1>报表</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <div class="mb-2">
        <label>年度 </label>
        <select v-model.number="year" @change="load">
          <option v-for="y in yearOptions" :key="y" :value="y">{{ y }}年</option>
        </select>
      </div>
      <table class="data-table">
        <tr><th>年度</th><td>{{ data.year }}</td></tr>
        <tr><th>Bug 数</th><td>{{ data.bugs }}</td></tr>
        <tr><th>任务数</th><td>{{ data.tasks }}</td></tr>
        <tr><th>待办数</th><td>{{ data.todos }}</td></tr>
        <tr><th>测试单数</th><td>{{ data.testTasks }}</td></tr>
      </table>
      <p class="mt-2">
        <router-link to="/report/remind" class="btn">每日提醒</router-link>
      </p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAnnualData } from '@/api/report'

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
