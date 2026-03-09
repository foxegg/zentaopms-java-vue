<template>
  <div>
    <div class="page-header">
      <h1>编辑版本</h1>
      <router-link :to="`/release/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>发布日期</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/release/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getReleaseById, updateRelease } from '@/api/release'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getReleaseById(id.value)
    const r = res.data
    if (r) {
      form.value = {
        name: r.name || '',
        date: r.date ? String(r.date).slice(0, 10) : '',
        description: r.description || ''
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
    await updateRelease(id.value, {
      name: form.value.name,
      date: form.value.date || null,
      description: form.value.description
    })
    router.push(`/release/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
