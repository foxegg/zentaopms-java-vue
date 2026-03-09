<template>
  <div class="table-wrap" v-if="!loading">
    <table class="data-table">
      <thead>
        <tr>
          <th>待办</th>
          <th>任务</th>
          <th>Bug</th>
          <th>需求</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>{{ todoData.tasks?.length ?? 0 }}</td>
          <td>{{ todoData.tasks?.length ?? 0 }}</td>
          <td>{{ todoData.bugs?.length ?? 0 }}</td>
          <td>{{ todoData.stories?.length ?? 0 }}</td>
        </tr>
      </tbody>
    </table>
    <p class="muted">待办汇总（任务 / Bug / 需求）</p>
  </div>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserTodo } from '@/api/user'

const props = defineProps({ user: Object })
const loading = ref(true)
const todoData = ref({ tasks: [], bugs: [], stories: [] })

async function load() {
  if (!props.user?.id) return
  loading.value = true
  try {
    const res = await getUserTodo(props.user.id)
    const d = res.data || {}
    todoData.value = { tasks: d.tasks || [], bugs: d.bugs || [], stories: d.stories || [] }
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, load)
onMounted(load)
</script>

<style scoped>
.muted { color: #999; font-size: 13px; margin-top: 8px; }
</style>
