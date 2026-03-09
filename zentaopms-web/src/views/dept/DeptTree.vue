<template>
  <div>
    <div class="page-header">
      <h1>部门</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <table class="data-table">
        <thead>
          <tr><th>ID</th><th>名称</th></tr>
        </thead>
        <tbody>
          <tr v-for="d in tree" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.name }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDeptTree } from '@/api/dept'

const tree = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getDeptTree()
    tree.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>

