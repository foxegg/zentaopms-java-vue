<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">看板空间</h1>
      <router-link to="/kanban/space/create" class="btn btn-primary">新建空间</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>
              <router-link :to="`/kanban/space/${s.id}`" class="btn">查看</router-link>
              <router-link :to="`/kanban/space/edit/${s.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">暂无看板空间</p>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSpaceList } from '@/api/kanban'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getSpaceList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
