<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">看板空间</h1>
      <router-link to="/kanban" class="btn">返回空间列表</router-link>
      <router-link v-if="space" :to="`/kanban/space/edit/${space.id}`" class="btn">编辑</router-link>
      <router-link v-if="space" :to="`/kanban/create?spaceID=${space.id}`" class="btn btn-primary">新建看板</router-link>
    </div>
    <div v-if="space" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ space.id }}</td></tr>
        <tr><th>名称</th><td>{{ space.name }}</td></tr>
      </table>
      <h3>看板列表</h3>
      <table class="data-table" v-if="kanbans.length">
        <thead>
          <tr><th>ID</th><th>名称</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="k in kanbans" :key="k.id">
            <td>{{ k.id }}</td>
            <td>{{ k.name }}</td>
            <td><router-link :to="`/kanban/board/${k.id}`" class="btn">打开看板</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无看板</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>空间不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSpaceById, getKanbanList } from '@/api/kanban'

const route = useRoute()
const space = ref(null)
const kanbans = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getSpaceById(id)
    space.value = res.data || null
    if (space.value) {
      const kRes = await getKanbanList(id)
      kanbans.value = kRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
