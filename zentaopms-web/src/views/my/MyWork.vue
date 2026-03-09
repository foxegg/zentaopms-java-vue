<template>
  <div>
    <div class="page-header"><h1>我的工作</h1></div>
    <div v-if="!loading" class="table-wrap">
      <section>
        <h3>任务</h3>
        <table class="data-table" v-if="data.tasks?.length">
          <thead><tr><th>ID</th><th>名称</th><th>状态</th></tr></thead>
          <tbody>
            <tr v-for="t in data.tasks" :key="t.id">
              <td>{{ t.id }}</td>
              <td><router-link :to="`/task/${t.id}`">{{ t.name }}</router-link></td>
              <td>{{ t.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>暂无任务</p>
      </section>
      <section class="mt-2">
        <h3>Bug</h3>
        <table class="data-table" v-if="data.bugs?.length">
          <thead><tr><th>ID</th><th>标题</th><th>状态</th></tr></thead>
          <tbody>
            <tr v-for="b in data.bugs" :key="b.id">
              <td>{{ b.id }}</td>
              <td><router-link :to="`/bug/${b.id}`">{{ b.title }}</router-link></td>
              <td>{{ b.status }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else>暂无 Bug</p>
      </section>
      <section class="mt-2">
        <h3>待办</h3>
        <ul v-if="data.todos?.length">
          <li v-for="t in data.todos" :key="t.id">
            <router-link :to="`/todo/${t.id}`">{{ t.name || t.title }}</router-link>
          </li>
        </ul>
        <p v-else>暂无待办</p>
      </section>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyWork } from '@/api/my'

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
