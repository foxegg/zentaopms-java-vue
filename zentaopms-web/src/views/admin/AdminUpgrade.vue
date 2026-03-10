<template>
  <div>
    <div class="page-header">
      <h1>{{ adminLang.upgradeTitle }}</h1>
      <router-link to="/admin" class="btn">{{ commonLang.backAdmin }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <pre v-if="data">{{ typeof data === 'object' ? JSON.stringify(data, null, 2) : data }}</pre>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUpgradeIndex } from '@/api/upgrade'
import { common as commonLang, admin as adminLang } from '@/lang/zh-cn'

const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getUpgradeIndex()
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
