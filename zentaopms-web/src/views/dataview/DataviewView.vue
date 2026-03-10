<template>
  <div>
    <div class="page-header">
      <h1>{{ dataviewLang.common }}：{{ data?.name || '-' }}</h1>
      <router-link to="/dataview" class="btn">{{ commonLang.backList }}</router-link>
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
import { getDataviewById } from '@/api/dataview'
import { common as commonLang, dataview as dataviewLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getDataviewById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
