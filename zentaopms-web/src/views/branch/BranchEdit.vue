<template>
  <div>
    <div class="page-header">
      <h1>编辑分支</h1>
      <router-link :to="`/branch/${branchId}`" class="btn">返回详情</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="`/branch/${branchId}`" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>分支不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBranchById, updateBranch } from '@/api/branch'

const route = useRoute()
const router = useRouter()
const branchId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = branchId.value
  loading.value = true
  try {
    const res = await getBranchById(id)
    const b = res.data
    if (b) form.value = { name: b.name }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateBranch(branchId.value, form.value)
    router.push(`/branch/${branchId.value}`)
  } finally {
    submitting.value = false
  }
}
</script>
