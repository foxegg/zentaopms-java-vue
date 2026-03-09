<template>
  <div>
    <div class="page-header">
      <h1>新建项目</h1>
      <router-link to="/project" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required maxlength="90" />
        </div>
        <div class="form-group">
          <label>代号</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type">
            <option value="sprint">冲刺</option>
            <option value="kanban">看板</option>
            <option value="stage">阶段</option>
          </select>
        </div>
        <div class="form-group">
          <label>开始日期</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>结束日期</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/project" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProject } from '@/api/project'

const router = useRouter()
const form = ref({
  name: '',
  code: '',
  type: 'sprint',
  begin: '',
  end: '',
  description: ''
})
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    const res = await createProject(form.value)
    router.push(`/project/${res.id}`)
  } finally {
    submitting.value = false
  }
}
</script>
