<template>
  <div>
    <div class="page-header">
      <h1>{{ testtaskLang.viewDetail }}</h1>
      <router-link to="/testtask" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ item.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ item.name }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ item.status }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getTestTaskById } from '@/api/testtask'
import { common as commonLang, testtask as testtaskLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getTestTaskById(route.params.id)
    item.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
