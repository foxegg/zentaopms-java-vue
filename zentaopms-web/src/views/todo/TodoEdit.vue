<template>
  <div>
    <div class="page-header">
      <h1>编辑待办</h1>
      <router-link to="/todo" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称/内容 *</label>
          <input v-model="form.name" required />
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
    <p v-else-if="loading">加载中...</p>
    <p v-else>待办不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTodoById, updateTodo } from '@/api/todo'

const route = useRoute()
const router = useRouter()
const todoId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = todoId.value
  loading.value = true
  try {
    const res = await getTodoById(id)
    const t = res.data
    if (t) form.value = { name: t.name || t.title || t.content || '', type: t.type || 'custom', date: t.date || '', desc: t.desc || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateTodo(todoId.value, form.value)
    router.push('/todo')
  } finally {
    submitting.value = false
  }
}
</script>
