<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ stageLang.common }}：{{ data?.name || '-' }}</h1>
      <router-link to="/stage" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="data" :to="`/stage/edit/${data.id}`" class="btn btn-primary">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ data.name }}</td></tr>
        <tr><th>{{ stageLang.workflowGroup }}</th><td>{{ data.workflowGroup }}</td></tr>
        <tr><th>{{ stageLang.percent }}</th><td>{{ data.percent }}</td></tr>
        <tr><th>{{ userLang.type }}</th><td>{{ data.type }}</td></tr>
        <tr><th>{{ stageLang.projectType }}</th><td>{{ data.projectType || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getStageById } from '@/api/stage'
import { common as commonLang, stage as stageLang, user as userLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getStageById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
