<template>
  <div v-if="item">
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ hostLang.common }}</h1>
      <router-link :to="`/host/edit/${item.id}`" class="btn">{{ commonLang.edit }}</router-link>
      <router-link to="/host" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <dl class="detail-dl">
      <dt>ID</dt><dd>{{ item.id }}</dd>
      <dt>{{ commonLang.name }}</dt><dd>{{ item.name }}</dd>
      <dt>{{ hostLang.type }}</dt><dd>{{ item.type }}</dd>
      <dt>{{ hostLang.status }}</dt><dd>{{ item.status }}</dd>
      <dt>{{ hostLang.os }}</dt><dd>{{ item.osName }} {{ item.osVersion }}</dd>
      <dt>{{ hostLang.intranet }}</dt><dd>{{ item.intranet || '-' }}</dd>
      <dt>{{ hostLang.extranet }}</dt><dd>{{ item.extranet || '-' }}</dd>
    </dl>
  </div>
  <p v-else-if="!loading">{{ commonLang.notFound }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getHostById } from '@/api/host'
import { common as commonLang, host as hostLang } from '@/lang/zh-cn'

const route = useRoute()
const item = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await getHostById(route.params.id)
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
