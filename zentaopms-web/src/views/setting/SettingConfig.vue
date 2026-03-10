<template>
  <div>
    <div class="page-header">
      <h1>{{ settingLang.title }}</h1>
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
import { getSettingConfig } from '@/api/setting'
import { common as commonLang, setting as settingLang } from '@/lang/zh-cn'

const config = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSettingConfig()
    config.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
