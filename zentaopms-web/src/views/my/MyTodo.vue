<template>
  <div>
    <div class="page-header"><h1>我的待办</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>ID</th><th>内容</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="t in list" :key="t.id"><td>{{ t.id }}</td><td>{{ t.name || t.title }}</td><td>{{ t.status }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyTodo } from '@/api/my'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyTodo()
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
