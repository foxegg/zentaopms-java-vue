<template>
  <div>
    <div class="page-header"><h1>{{ myLang.calendarTitle }}</h1></div>
    <div v-if="!loading" class="table-wrap">
      <p class="mb-2">{{ myLang.calendarDesc }}</p>
      <section>
        <h3>{{ myLang.taskSection }}</h3>
        <table class="data-table" v-if="data.tasks?.length">
          <thead><tr><th>ID</th><th>{{ myLang.name }}</th><th>{{ myLang.beginDeadline }}</th><th>{{ commonLang.status }}</th></tr></thead>
          <tbody>
            <tr v-for="t in data.tasks" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
              <td>{{ t.begin || '-' }} / {{ t.deadline || t.end || '-' }}</td>
              <td>{{ t.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>{{ commonLang.noData }}</p>
      </section>
      <section class="mt-2">
        <h3>{{ myLang.todoSection }}</h3>
        <table class="data-table" v-if="data.todos?.length">
          <thead><tr><th>ID</th><th>{{ todoLang.name }}</th><th>{{ myLang.date }}</th></tr></thead>
          <tbody>
            <tr v-for="t in data.todos" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/todo/${t.id}`">{{ t.name || t.title }}</router-link></td>
              <td>{{ t.date || t.begin || '-' }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>{{ commonLang.noData }}</p>
      </section>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyCalendar } from '@/api/my'
import { common as commonLang, my as myLang, todo as todoLang } from '@/lang/zh-cn'

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
