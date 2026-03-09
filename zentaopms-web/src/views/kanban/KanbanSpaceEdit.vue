<template>
  <div>
    <div class="page-header">
      <h1>编辑看板空间</h1>
      <router-link :to="`/kanban/space/${spaceId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/kanban/space/${spaceId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>空间不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSpaceById, updateSpace } from '@/api/kanban'

const route = useRoute()
const router = useRouter()
const spaceId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = spaceId.value
  loading.value = true
  try {
    const res = await getSpaceById(id)
    const s = res.data
    if (s) form.value = { name: s.name }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateSpace(spaceId.value, form.value)
    router.push(`/kanban/space/${spaceId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
