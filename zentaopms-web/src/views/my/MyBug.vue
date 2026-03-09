<template>
  <div>
    <div class="page-header"><h1>我的 Bug</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>ID</th><th>标题</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="b in list" :key="b.id"><td>{{ b.id }}</td><td>{{ b.title }}</td><td>{{ b.status }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyBug } from '@/api/my'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyBug()
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
