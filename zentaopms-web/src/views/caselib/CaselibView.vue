<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ caselibLang.common }}：{{ data?.name || '-' }}</h1>
      <router-link to="/caselib" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="data" :to="`/caselib/edit/${data.id}`" class="btn btn-primary">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="!loading && data" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ data.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ data.name }}</td></tr>
        <tr><th>{{ productLang.common }}</th><td>{{ data.product || '-' }}</td></tr>
        <tr><th>{{ projectLang.common }}</th><td>{{ data.project || '-' }}</td></tr>
        <tr><th>{{ commonLang.desc }}</th><td>{{ data.description || '-' }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getCaselibById } from '@/api/caselib'
import { common as commonLang, caselib as caselibLang, product as productLang, project as projectLang } from '@/lang/zh-cn'

const route = useRoute()
const id = computed(() => Number(route.params.id))
const data = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getCaselibById(id.value)
    data.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>
