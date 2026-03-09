<template>
  <div v-if="form">
    <div class="page-header">
      <h1>编辑主机</h1>
      <router-link to="/host" class="btn">返回列表</router-link>
    </div>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>名称 *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>类型</label>
        <input v-model="form.type" />
      </div>
      <div class="form-group">
        <label>状态</label>
        <input v-model="form.status" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
        <router-link to="/host" class="btn">取消</router-link>
      </div>
    </form>
  </div>
  <p v-else-if="!loading">未找到</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getHostById, updateHost } from '@/api/host'

const route = useRoute()
const router = useRouter()
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  try {
    const res = await getHostById(route.params.id)
    form.value = res.data || res
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  submitting.value = true
  try {
    await updateHost(route.params.id, form.value)
    router.push('/host')
  } finally {
    submitting.value = false
  }
}
</script>
