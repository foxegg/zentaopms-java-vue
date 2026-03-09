<template>
  <div>
    <div class="page-header">
      <h1>编辑需求</h1>
      <router-link :to="`/story/${id}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>标题 *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>优先级</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/story/${id}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStoryById, updateStory } from '@/api/story'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getStoryById(id.value)
    const s = res.data
    if (s) {
      form.value = {
        title: s.title || '',
        pri: s.pri ?? 3,
        description: s.description || ''
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
    await updateStory(id.value, {
      title: form.value.title,
      pri: form.value.pri,
      description: form.value.description
    })
    router.push(`/story/${id.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
