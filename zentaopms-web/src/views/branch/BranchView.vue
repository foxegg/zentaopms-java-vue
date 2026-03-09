<template>
  <div>
    <div class="page-header">
      <h1>分支详情</h1>
      <router-link to="/branch" class="btn">返回列表</router-link>
      <router-link v-if="branch" :to="`/branch/edit/${branch.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="branch" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ branch.id }}</td></tr>
        <tr><th>名称</th><td>{{ branch.name }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>分支不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBranchById } from '@/api/branch'

const route = useRoute()
const branch = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  try {
    const res = await getBranchById(id)
    branch.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
