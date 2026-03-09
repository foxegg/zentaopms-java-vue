<template>
  <div>
    <div class="page-header"><h1>我的日历</h1></div>
    <div v-if="!loading" class="table-wrap">
      <p class="mb-2">按任务与待办的日期展示（可扩展为日历视图）</p>
      <section>
        <h3>任务</h3>
        <table class="data-table" v-if="data.tasks?.length">
          <thead><tr><th>ID</th><th>名称</th><th>开始/截止</th><th>状态</th></tr></thead>
          <tbody>
            <tr v-for="t in data.tasks" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
              <td>{{ t.begin || '-' }} / {{ t.deadline || t.end || '-' }}</td>
              <td>{{ t.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>暂无任务</p>
      </section>
      <section class="mt-2">
        <h3>待办</h3>
        <table class="data-table" v-if="data.todos?.length">
          <thead><tr><th>ID</th><th>标题</th><th>日期</th></tr></thead>
          <tbody>
            <tr v-for="t in data.todos" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/todo/${t.id}`">{{ t.name || t.title }}</router-link></td>
              <td>{{ t.date || t.begin || '-' }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>暂无待办</p>
      </section>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyCalendar } from '@/api/my'

const data = ref({ tasks: [], todos: [] })
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getMyCalendar()
    data.value = res.data || data.value
  } finally {
    loading.value = false
  }
})
</script>
<style scoped>
.mt-2 { margin-top: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
</style>
