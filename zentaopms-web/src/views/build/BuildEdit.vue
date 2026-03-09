<template>
  <div>
    <div class="page-header">
      <h1>编辑构建</h1>
      <router-link :to="`/build/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>日期</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/build/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBuildById, updateBuild } from '@/api/build'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getBuildById(id.value)
    const b = res.data
    if (b) {
      form.value = {
        name: b.name || '',
        date: b.date ? String(b.date).slice(0, 10) : '',
        description: b.description || ''
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
    await updateBuild(id.value, {
      name: form.value.name,
      date: form.value.date || null,
      description: form.value.description
    })
    router.push(`/build/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
