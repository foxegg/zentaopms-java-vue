<template>
  <div>
    <div class="page-header">
      <h1>编辑 Bug</h1>
      <router-link :to="`/bug/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>标题 *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>严重程度</label>
          <select v-model.number="form.severity">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
          </select>
        </div>
        <div class="form-group">
          <label>优先级</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>复现步骤</label>
          <textarea v-model="form.steps" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/bug/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBugById, updateBug } from '@/api/bug'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getBugById(id.value)
    const b = res.data
    if (b) {
      form.value = {
        title: b.title || '',
        severity: b.severity ?? 3,
        pri: b.pri ?? 2,
        steps: b.steps || ''
      }
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateBug(id.value, {
      title: form.value.title,
      severity: form.value.severity,
      pri: form.value.pri,
      steps: form.value.steps
    })
    router.push(`/bug/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
