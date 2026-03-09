<template>
  <div>
    <div class="page-header">
      <h1>新建测试报告</h1>
      <router-link to="/testreport" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>项目ID</label>
          <input v-model.number="form.project" type="number" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/testreport" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createTestReport } from '@/api/testreport'

const router = useRouter()
const form = ref({ name: '', project: '' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    const res = await createTestReport(form.value)
    router.push(`/testreport/${res.id}`)
  } finally {
    submitting.value = false
  }
}
</script>
