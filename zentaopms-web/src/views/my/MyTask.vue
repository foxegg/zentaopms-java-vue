<template>
  <div>
    <div class="page-header"><h1>我的任务</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>ID</th><th>名称</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="t in list" :key="t.id"><td>{{ t.id }}</td><td>{{ t.name }}</td><td>{{ t.status }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyTask } from '@/api/my'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyTask()
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
