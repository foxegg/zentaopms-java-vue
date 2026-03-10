<template>
  <div>
    <div class="page-header">
      <h1>{{ stakeholderLang.viewDetail }}</h1>
      <router-link to="/stakeholder" class="btn">{{ commonLang.backList }}</router-link>
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
import { getStakeholderById } from '@/api/stakeholder'
import { common as commonLang, stakeholder as stakeholderLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getStakeholderById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
