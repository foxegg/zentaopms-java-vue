<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.config }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <div v-if="!loading" class="table-wrap">
      <pre v-if="config">{{ typeof config === 'object' ? JSON.stringify(config, null, 2) : config }}</pre>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminConfig } from '@/api/admin'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const config = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getAdminConfig()
    config.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
