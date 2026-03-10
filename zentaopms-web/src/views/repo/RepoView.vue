<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ repoLang.common }}</h1>
      <router-link to="/repo" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div v-if="repo" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ repo.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ repo.name || repo.path }}</td></tr>
        <tr><th>{{ repoLang.path }}</th><td>{{ repo.path || '-' }}</td></tr>
        <tr><th>SCM</th><td>{{ repo.scm || '-' }}</td></tr>
        <tr><th>{{ repoLang.encoding }}</th><td>{{ repo.encoding || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ repoLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getRepoById } from '@/api/repo'
import { common as commonLang, repo as repoLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const repo = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getRepoById(id.value)
    repo.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
