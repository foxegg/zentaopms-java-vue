<template>
  <div>
    <div class="page-header">
      <h1>新建节假日</h1>
      <router-link to="/holiday" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required maxlength="30" />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type">
            <option value="holiday">节假日</option>
            <option value="working">调休</option>
          </select>
        </div>
        <div class="form-group">
          <label>开始日期 *</label>
          <input v-model="form.begin" type="date" required />
        </div>
        <div class="form-group">
          <label>结束日期 *</label>
          <input v-model="form.end" type="date" required />
        </div>
        <div class="form-group">
          <label>备注</label>
          <textarea v-model="form.description" rows="2"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/holiday" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createHoliday } from '@/api/holiday'

const router = useRouter()
const form = ref({ name: '', type: 'holiday', begin: '', end: '', description: '' })
const submitting = ref(false)

async function onSubmit() {
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    alert('开始日期不能大于结束日期')
    return
  }
  submitting.value = true
  try {
    await createHoliday(form.value)
    router.push('/holiday')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 4px; }
.form-group input, .form-group select, .form-group textarea { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
</style>
