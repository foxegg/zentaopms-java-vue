<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">合并请求</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>标题</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="m in list" :key="m.id">
            <td>{{ m.id }}</td>
            <td>{{ m.title || '-' }}</td>
            <td>{{ m.status || '-' }}</td>
            <td>
              <button type="button" class="btn" @click="onDelete(m.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="text-muted">暂无数据</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getFeedbackList, deleteFeedback } from '@/api/feedback'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getMrList()
    list.value = (res.data || [])
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteMr(id)
  load()
}

onMounted(() => load())
</script>
