<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ bugLang.common }}</h1>
      <router-link to="/bug" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="item" to="/bug/create" class="btn btn-primary">{{ bugLang.create }}</router-link>
      <router-link v-if="item" :to="`/bug/edit/${item.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="item" class="table-wrap">
      <section class="mb-3">
        <h3 class="legend-title">{{ bugLang.legendSteps }}</h3>
        <div class="steps-content" v-html="item.steps || '—'"></div>
      </section>
      <section>
        <h3 class="legend-title">{{ bugLang.legendBasicInfo }}</h3>
        <table class="data-table">
          <tr><th>ID</th><td>{{ item.id }}</td></tr>
          <tr><th>{{ bugLang.title }}</th><td>{{ item.title }}</td></tr>
          <tr><th>{{ bugLang.product }}</th><td>{{ item.product }}</td></tr>
          <tr><th>{{ bugLang.openedBuild }}</th><td>{{ item.openedBuild || '—' }}</td></tr>
          <tr><th>{{ bugLang.severity }}</th><td>{{ item.severity }}</td></tr>
          <tr><th>{{ bugLang.pri }}</th><td>{{ item.pri }}</td></tr>
          <tr><th>{{ commonLang.status }}</th><td>{{ item.status }}</td></tr>
          <tr><th>{{ bugLang.openedBy }}</th><td>{{ item.openedBy || '—' }}</td></tr>
          <tr><th>{{ bugLang.assignTo }}</th><td>{{ item.assignedTo || '—' }}</td></tr>
          <tr><th>{{ bugLang.resolvedBy }}</th><td>{{ item.resolvedBy || '—' }}</td></tr>
          <tr><th>{{ bugLang.resolution }}</th><td>{{ item.resolution || '—' }}</td></tr>
        </table>
      </section>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBugById } from '@/api/bug'
import { common as commonLang, bug as bugLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getBugById(route.params.id)
    item.value = res?.data ?? res ?? null
  } catch {
    item.value = null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.legend-title { font-size: 1rem; margin: 0 0 0.5rem 0; }
.steps-content { white-space: pre-wrap; }
.mb-3 { margin-bottom: 1rem; }
</style>
