<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">发布详情</h1>
      <router-link to="/release" class="btn">返回列表</router-link>
      <router-link v-if="item" :to="`/release/edit/${item.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>名称</th><td>{{ item.name }}</td></tr>
        <tr><th>日期</th><td>{{ item.date }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getReleaseById } from '@/api/release'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getReleaseById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
