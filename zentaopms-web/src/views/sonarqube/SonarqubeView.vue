<template>
  <div v-if="item">
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">SonarQube</h1>
      <router-link :to="`/sonarqube/edit/${item.id}`" class="btn">{{ commonLang.edit }}</router-link>
      <router-link to="/sonarqube" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <dl class="detail-dl">
      <dt>ID</dt><dd>{{ item.id }}</dd>
      <dt>{{ commonLang.name }}</dt><dd>{{ item.name }}</dd>
      <dt>URL</dt><dd>{{ item.url }}</dd>
      <dt>{{ commonLang.account }}</dt><dd>{{ item.account }}</dd>
    </dl>
  </div>
  <p v-else-if="!loading">{{ commonLang.notFound }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSonarqubeById } from '@/api/sonarqube'
import { common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getSonarqubeById(route.params.id)
    item.value = res.data || res
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.detail-dl { display: grid; grid-template-columns: auto 1fr; gap: 0.25rem 1rem; }
.detail-dl dt { font-weight: 600; }
</style>
