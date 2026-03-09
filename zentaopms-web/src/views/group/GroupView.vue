<template>
  <div>
    <div class="page-header">
      <h1>权限组详情</h1>
      <router-link to="/group" class="btn">返回列表</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>名称</th><td>{{ item.name }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getGroupById } from '@/api/group'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getGroupById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
