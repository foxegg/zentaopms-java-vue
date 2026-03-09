<template>
  <div>
    <div class="page-header">
      <h1>新建主机</h1>
      <router-link to="/host" class="btn">返回列表</router-link>
    </div>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>名称 *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>类型</label>
        <input v-model="form.type" placeholder="normal" />
      </div>
      <div class="form-group">
        <label>状态</label>
        <input v-model="form.status" placeholder="online" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
        <router-link to="/host" class="btn">取消</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createHost } from '@/api/host'

const router = useRouter()
const form = ref({ name: '', type: 'normal', status: 'online' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    await createHost(form.value)
    router.push('/host')
  } finally {
    submitting.value = false
  }
}
</script>
