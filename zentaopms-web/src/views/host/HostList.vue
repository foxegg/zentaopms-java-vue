<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">主机</h1>
      <router-link to="/host/create" class="btn btn-primary">新建</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>类型</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="h in list" :key="h.id">
            <td>{{ h.id }}</td>
            <td><router-link :to="`/host/${h.id}`">{{ h.name || '-' }}</router-link></td>
            <td>{{ h.type || '-' }}</td>
            <td>{{ h.status || '-' }}</td>
            <td>
              <router-link :to="`/host/edit/${h.id}`" class="btn">编辑</router-link>
              <button type="button" class="btn" @click="onDelete(h.id)">删除</button>
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
import { getHostList, deleteHost } from '@/api/host'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getHostList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

async function onDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteHost(id)
  load()
}

onMounted(() => load())
</script>
