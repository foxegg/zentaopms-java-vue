<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ programLang.common }}</h1>
      <router-link to="/program" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="program" :to="`/program/edit/${program.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="program" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ program.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ program.name }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ program.status }}</td></tr>
      </table>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ programLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProgramById } from '@/api/program'
import { common as commonLang, program as programLang } from '@/lang/zh-cn'

const route = useRoute()
const program = ref(null)
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProgramById(id)
    program.value = res.data || null
  } finally {
    loading.value = false
  }
})
</script>
