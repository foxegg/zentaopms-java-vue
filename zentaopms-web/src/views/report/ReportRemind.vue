<template>
  <div>
    <div class="page-header">
      <h1>{{ reportLang.remind }}</h1>
      <router-link to="/report" class="btn">{{ reportLang.backReport }}</router-link>
    </div>
    <div v-if="!loading" class="table-wrap">
      <p>{{ data.message || commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getRemind } from '@/api/report'
import { common as commonLang, report as reportLang } from '@/lang/zh-cn'

const data = ref({})
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRemind()
    data.value = res.data || {}
  } finally {
    loading.value = false
  }
})
</script>
