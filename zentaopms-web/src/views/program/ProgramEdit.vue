<template>
  <div>
    <div class="page-header">
      <h1>编辑项目集</h1>
      <router-link :to="`/program/${programId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/program/${programId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>项目集不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProgramById, updateProgram } from '@/api/program'

const route = useRoute()
const router = useRouter()
const programId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = programId.value
  loading.value = true
  try {
    const res = await getProgramById(id)
    const p = res.data
    if (p) form.value = { name: p.name }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateProgram(programId.value, form.value)
    router.push(`/program/${programId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
