<template>
  <div>
    <div class="page-header">
      <h1>新建项目集</h1>
      <router-link to="/program" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/program" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProgram } from '@/api/program'

const router = useRouter()
const form = ref({ name: '' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    const res = await createProgram(form.value)
    router.push(`/program/${res.id}`)
  } finally {
    submitting.value = false
  }
}
</script>
