<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.webhookDetail }}</h1>
      <router-link to="/admin/webhook" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ data.name }}</td></tr>
        <tr><th>{{ projectLang.type }}</th><td>{{ data.type }}</td></tr>
        <tr><th>URL</th><td>{{ data.url }}</td></tr>
        <tr><th>{{ adminLang.domain }}</th><td>{{ data.domain || '-' }}</td></tr>
        <tr><th>Content-Type</th><td>{{ data.contentType }}</td></tr>
        <tr><th>{{ adminLang.sendType }}</th><td>{{ data.sendType }}</td></tr>
        <tr><th>{{ commonLang.desc }}</th><td>{{ data.description || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getWebhookById } from '@/api/webhook'
import { common as commonLang, project as projectLang, admin as adminLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getWebhookById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
