<template>
  <div>
    <div class="page-header"><h1>我的需求</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead><tr><th>ID</th><th>标题</th><th>状态</th></tr></thead>
        <tbody>
          <tr v-for="s in list" :key="s.id"><td>{{ s.id }}</td><td>{{ s.title }}</td><td>{{ s.status }}</td></tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyStory } from '@/api/my'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyStory()
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>
