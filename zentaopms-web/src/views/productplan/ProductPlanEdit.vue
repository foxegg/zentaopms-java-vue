<template>
  <div>
    <div class="page-header">
      <h1>编辑产品计划</h1>
      <router-link :to="`/productplan/${planId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>开始日期</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>结束日期</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/productplan/${planId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>计划不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductPlanById, updateProductPlan } from '@/api/productplan'

const route = useRoute()
const router = useRouter()
const planId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = planId.value
  loading.value = true
  try {
    const res = await getProductPlanById(id)
    const p = res.data
    if (p) form.value = { name: p.name, begin: p.begin || '', end: p.end || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateProductPlan(planId.value, form.value)
    router.push(`/productplan/${planId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
