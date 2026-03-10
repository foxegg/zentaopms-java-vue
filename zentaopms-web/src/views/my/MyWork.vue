<template>
  <div>
    <div class="page-header"><h1>{{ myLang.workAction }}</h1></div>
    <div v-if="!loading" class="table-wrap">
      <section>
        <h3>{{ myLang.task }}</h3>
        <table class="data-table" v-if="data.tasks?.length">
          <thead><tr><th>ID</th><th>{{ taskLang.name }}</th><th>{{ taskLang.status }}</th></tr></thead>
          <tbody>
            <tr v-for="t in data.tasks" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
              <td>{{ t.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>{{ myLang.noTask }}</p>
      </section>
      <section class="mt-2">
        <h3>{{ myLang.bug }}</h3>
        <table class="data-table" v-if="data.bugs?.length">
          <thead><tr><th>ID</th><th>{{ bugLang.title }}</th><th>{{ bugLang.status }}</th></tr></thead>
          <tbody>
            <tr v-for="b in data.bugs" :key="b.id">
              <td>{{ b.id }}</td>
              <td><router-link :to="`/bug/${b.id}`">{{ b.title }}</router-link></td>
              <td>{{ b.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>{{ myLang.noBug }}</p>
      </section>
      <section class="mt-2">
        <h3>{{ myLang.todo }}</h3>
        <ul v-if="data.todos?.length">
          <li v-for="t in data.todos" :key="t.id">
            <router-link :to="`/todo/${t.id}`">{{ t.name || t.title }}</router-link>
          </li>
        </ul>
        <p v-else>{{ todoLang.noTodo }}</p>
      </section>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyWork } from '@/api/my'
import { my as myLang, common as commonLang, task as taskLang, bug as bugLang, todo as todoLang } from '@/lang/zh-cn'

const data = ref({ tasks: [], bugs: [], todos: [] })
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getMyWork()
    data.value = res.data || data.value
  } finally {
    loading.value = false
  }
})
</script>
<style scoped>
.mt-2 { margin-top: 1rem; }
</style>
