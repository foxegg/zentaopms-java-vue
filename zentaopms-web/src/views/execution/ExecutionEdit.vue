<template>
  <div>
    <div class="page-header">
      <h1>编辑执行</h1>
      <router-link :to="`/execution/${executionId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>代号</label>
          <input v-model="form.code" />
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
          <router-link :to="`/execution/${executionId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>执行不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getExecutionById, updateExecution } from '@/api/execution'

const route = useRoute()
const router = useRouter()
const executionId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = executionId.value
  loading.value = true
  try {
    const res = await getExecutionById(id)
    const e = res.data
    if (e) form.value = { name: e.name, code: e.code || '', begin: e.begin || '', end: e.end || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateExecution(executionId.value, form.value)
    router.push(`/execution/${executionId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
