<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ taskLang.view }}</h1>
      <router-link to="/task" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="item" :to="`/task/edit/${item.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>{{ taskLang.name }}</th><td>{{ item.name }}</td></tr>
        <tr><th>{{ taskLang.status }}</th><td>{{ item.status }}</td></tr>
        <tr><th>{{ taskLang.executionName }}</th><td>{{ item.execution }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTaskById } from '@/api/task'
import { task as taskLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getTaskById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
