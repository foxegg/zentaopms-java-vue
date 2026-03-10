<template>
  <div>
    <div class="page-header">
      <h1>{{ todoLang.edit }}</h1>
      <router-link to="/todo" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ todoLang.date }}</label>
          <input v-model="form.date" type="date" />
        </div>
        <div class="form-group">
          <label>{{ todoLang.type }} *</label>
          <select v-model="form.type" required>
            <option value="custom">{{ todoLang.typeList.custom }}</option>
            <option value="bug">{{ todoLang.typeList.bug }}</option>
            <option value="task">{{ todoLang.typeList.task }}</option>
            <option value="story">{{ todoLang.typeList.story }}</option>
            <option value="testtask">{{ todoLang.typeList.testtask }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ todoLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ todoLang.pri }}</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ todoLang.desc }}</label>
          <textarea v-model="form.desc" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/todo" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ commonLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTodoById, updateTodo } from '@/api/todo'
import { todo as todoLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const todoId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  const id = todoId.value
  loading.value = true
  try {
    const res = await getTodoById(id)
    const t = res.data
    if (t) form.value = {
      name: t.name || t.title || t.content || '',
      type: t.type || 'custom',
      date: t.date || '',
      desc: t.desc || '',
      pri: t.pri ?? 3
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateTodo(todoId.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/todo')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
