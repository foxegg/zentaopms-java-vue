<template>
  <div>
    <div class="page-header">
      <h1>流水线：{{ data?.name || '-' }}</h1>
      <router-link to="/pipeline" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr v-for="(v, k) in data" :key="k"><th>{{ k }}</th><td>{{ v }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getPipelineById } from '@/api/pipeline'
import { common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getPipelineById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
