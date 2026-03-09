<template>
  <div v-if="item">
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">主机详情</h1>
      <router-link :to="`/host/edit/${item.id}`" class="btn">编辑</router-link>
      <router-link to="/host" class="btn">返回列表</router-link>
    </div>
    <dl class="detail-dl">
      <dt>ID</dt><dd>{{ item.id }}</dd>
      <dt>名称</dt><dd>{{ item.name }}</dd>
      <dt>类型</dt><dd>{{ item.type }}</dd>
      <dt>状态</dt><dd>{{ item.status }}</dd>
      <dt>操作系统</dt><dd>{{ item.osName }} {{ item.osVersion }}</dd>
      <dt>内网</dt><dd>{{ item.intranet || '-' }}</dd>
      <dt>外网</dt><dd>{{ item.extranet || '-' }}</dd>
    </dl>
  </div>
  <p v-else-if="!loading">未找到</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getHostById } from '@/api/host'

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
