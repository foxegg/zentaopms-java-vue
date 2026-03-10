<template>
  <div>
    <div class="page-header">
      <h1>需求：{{ data?.title || '-' }}</h1>
      <router-link to="/requirement" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ storyLang.title }}</th><td>{{ data.title }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ data.status }}</td></tr>
        <tr><th>{{ commonLang.desc }}</th><td>{{ data.spec }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getRequirementById } from '@/api/requirement'
import { common as commonLang, story as storyLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRequirementById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
