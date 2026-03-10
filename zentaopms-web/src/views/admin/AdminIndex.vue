<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.title }}</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <nav class="sub-nav mb-2">
        <router-link to="/admin/config" class="btn">{{ adminLang.config }}</router-link>
        <router-link to="/admin/safe" class="btn">{{ adminLang.safe }}</router-link>
        <router-link to="/admin/log" class="btn">{{ adminLang.log }}</router-link>
        <router-link to="/admin/backup" class="btn">{{ adminLang.backup }}</router-link>
        <router-link to="/admin/cron" class="btn">{{ adminLang.cron }}</router-link>
        <router-link to="/admin/webhook" class="btn">{{ adminLang.webhook }}</router-link>
        <router-link to="/admin/message" class="btn">{{ adminLang.message }}</router-link>
        <router-link to="/admin/cache" class="btn">{{ adminLang.cache }}</router-link>
        <router-link to="/admin/upgrade" class="btn">{{ adminLang.upgrade }}</router-link>
        <router-link to="/admin/tutorial" class="btn">{{ adminLang.tutorial }}</router-link>
        <router-link to="/admin/sso" class="btn">{{ adminLang.sso }}</router-link>
      </nav>
      <div v-if="data">
        <table class="data-table">
          <tr v-for="(v, k) in data" :key="k"><th>{{ k }}</th><td>{{ v }}</td></tr>
        </table>
      </div>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminIndex } from '@/api/admin'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getAdminIndex()
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.sub-nav { display: flex; gap: 8px; flex-wrap: wrap; }
</style>
