<template>
  <div>
    <div class="page-header">
      <h1>新建 SonarQube</h1>
      <router-link to="/sonarqube" class="btn">返回列表</router-link>
    </div>
    <form @submit.prevent="onSubmit" class="form-wrap">
      <div class="form-group">
        <label>名称 *</label>
        <input v-model="form.name" required />
      </div>
      <div class="form-group">
        <label>URL *</label>
        <input v-model="form.url" required />
      </div>
      <div class="form-group">
        <label>账号</label>
        <input v-model="form.account" />
      </div>
      <div class="form-actions">
        <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
        <router-link to="/sonarqube" class="btn">取消</router-link>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createSonarqube } from '@/api/sonarqube'

const router = useRouter()
const form = ref({ name: '', url: '', account: '' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    await createSonarqube(form.value)
    router.push('/sonarqube')
  } finally {
    submitting.value = false
  }
}
</script>
