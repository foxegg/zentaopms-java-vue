<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">需求详情</h1>
      <router-link to="/story" class="btn">返回列表</router-link>
      <router-link v-if="item" :to="`/story/edit/${item.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>标题</th><td>{{ item.title }}</td></tr>
        <tr><th>状态</th><td>{{ item.status }}</td></tr>
        <tr><th>指派</th><td>{{ item.assignedTo }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getStoryById } from '@/api/story'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getStoryById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
