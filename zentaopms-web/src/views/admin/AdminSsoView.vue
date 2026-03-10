<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.ssoDetail }}</h1>
      <router-link to="/admin/sso" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="!loading && config" class="table-wrap">
      <table class="data-table">
        <tr><th>{{ adminLang.ssoTurnon }}</th><td>{{ config.turnon }}</td></tr>
        <tr><th>{{ adminLang.ssoAddr }}</th><td>{{ config.addr }}</td></tr>
        <tr><th>{{ adminLang.ssoCode }}</th><td>{{ config.code }}</td></tr>
        <tr><th>{{ adminLang.ssoKey }}</th><td>********</td></tr>
      </table>
      <p><router-link to="/admin/sso" class="btn">{{ commonLang.edit }}</router-link></p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSsoSetting } from '@/api/sso'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const config = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSsoSetting()
    config.value = res.data || res || null
  } finally {
    loading.value = false
  }
})
</script>
