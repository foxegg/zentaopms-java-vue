<template>
  <div>
    <div class="page-header">
      <h1>编辑任务</h1>
      <router-link :to="`/task/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>预估工时</label>
          <input v-model="form.estimate" type="number" step="0.5" min="0" />
        </div>
        <div class="form-group">
          <label>截止日期</label>
          <input v-model="form.deadline" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/task/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTaskById, updateTask } from '@/api/task'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getTaskById(id.value)
    const t = res.data
    if (t) {
      form.value = {
        name: t.name || '',
        estimate: t.estimate ?? '',
        deadline: t.deadline ? String(t.deadline).slice(0, 10) : '',
        description: t.description || ''
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
    const payload = { name: form.value.name, description: form.value.description }
    if (form.value.estimate !== '' && form.value.estimate != null) payload.estimate = Number(form.value.estimate)
    if (form.value.deadline !== undefined) payload.deadline = form.value.deadline || null
    await updateTask(id.value, payload)
    router.push(`/task/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
