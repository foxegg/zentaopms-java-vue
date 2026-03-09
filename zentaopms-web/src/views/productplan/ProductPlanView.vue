<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">计划详情</h1>
      <router-link :to="plan ? `/productplan?product=${plan.product}` : '/productplan'" class="btn">返回列表</router-link>
      <router-link v-if="plan" :to="`/productplan/edit/${plan.id}`" class="btn">编辑</router-link>
    </div>
    <div v-if="plan" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ plan.id }}</td></tr>
        <tr><th>名称</th><td>{{ plan.name }}</td></tr>
        <tr><th>状态</th><td>{{ plan.status }}</td></tr>
        <tr><th>开始日期</th><td>{{ plan.begin || '-' }}</td></tr>
        <tr><th>结束日期</th><td>{{ plan.end || '-' }}</td></tr>
      </table>
      <h3>关联需求</h3>
      <table class="data-table" v-if="stories.length">
        <thead>
          <tr><th>ID</th><th>标题</th><th>状态</th></tr>
        </thead>
        <tbody>
          <tr v-for="s in stories" :key="s.id">
            <td>{{ s.id }}</td>
            <td><router-link :to="`/story/${s.id}`">{{ s.title }}</router-link></td>
            <td>{{ s.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无关联需求</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>计划不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductPlanById, getPlanStories } from '@/api/productplan'

const route = useRoute()
const plan = ref(null)
const stories = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProductPlanById(id)
    plan.value = res.data || null
    if (plan.value) {
      const sRes = await getPlanStories(id)
      stories.value = sRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
