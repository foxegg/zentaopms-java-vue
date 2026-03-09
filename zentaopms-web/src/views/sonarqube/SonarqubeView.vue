<template>
  <div v-if="item">
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">SonarQube 详情</h1>
      <router-link :to="`/sonarqube/edit/${item.id}`" class="btn">编辑</router-link>
      <router-link to="/sonarqube" class="btn">返回列表</router-link>
    </div>
    <dl class="detail-dl">
      <dt>ID</dt><dd>{{ item.id }}</dd>
      <dt>名称</dt><dd>{{ item.name }}</dd>
      <dt>URL</dt><dd>{{ item.url }}</dd>
      <dt>账号</dt><dd>{{ item.account }}</dd>
    </dl>
  </div>
  <p v-else-if="!loading">未找到</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSonarqubeById } from '@/api/sonarqube'

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
