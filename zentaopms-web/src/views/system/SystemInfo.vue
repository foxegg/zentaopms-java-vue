<template>
  <div>
    <div class="page-header">
      <h1>{{ systemLang.info }}</h1>
    </div>
    <div v-if="!loading" class="table-wrap">
      <table class="data-table" v-if="info">
        <tr v-for="(v, k) in info" :key="k"><th>{{ k }}</th><td>{{ v }}</td></tr>
      </table>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSystemInfo } from '@/api/system'
import { common as commonLang, system as systemLang } from '@/lang/zh-cn'

const info = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSystemInfo()
    info.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
