<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.webhook }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ projectLang.type }}</th>
            <th>URL</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="w in list" :key="w.id">
            <td>{{ w.id }}</td>
            <td>{{ w.name }}</td>
            <td>{{ w.type || 'default' }}</td>
            <td>{{ w.url || '-' }}</td>
            <td>
              <router-link :to="`/admin/webhook/${w.id}`" class="btn">{{ commonLang.view }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getWebhookList } from '@/api/webhook'
import { common as commonLang, project as projectLang, admin as adminLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getWebhookList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
