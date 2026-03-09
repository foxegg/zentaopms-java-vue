<template>
  <div>
    <div class="page-header">
      <h1>新建待办</h1>
      <router-link to="/todo" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称/内容 *</label>
          <input v-model="form.name" required placeholder="待办标题或内容" />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type">
            <option value="custom">自定义</option>
            <option value="bug">Bug</option>
            <option value="task">任务</option>
            <option value="story">需求</option>
          </select>
        </div>
        <div class="form-group">
          <label>日期</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.desc" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/todo" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createTodo } from '@/api/todo'

const router = useRouter()
const form = ref({ name: '', type: 'custom', date: '', desc: '' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    await createTodo(form.value)
    router.push('/todo')
  } finally {
    submitting.value = false
  }
}
</script>
