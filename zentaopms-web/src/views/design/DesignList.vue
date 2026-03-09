<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">设计</h1>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="d in list" :key="d.id">
            <td>{{ d.id }}</td>
            <td>{{ d.name || '-' }}</td>
            <td>{{ d.status || '-' }}</td>
            <td>
              <button type="button" class="btn" @click="onDelete(d.id)">删除</button>
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
import { getDesignList, deleteDesign } from '@/api/design'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getFeedbackList()
    list.value = (res.data || [])
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteDesign(id)
  load()
}

onMounted(() => load())
</script>
