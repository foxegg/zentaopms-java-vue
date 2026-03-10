<template>
  <div>
    <div class="page-header">
      <h1>{{ todoLang.create }}</h1>
      <router-link to="/todo" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
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
          <input v-model="form.name" required :placeholder="todoLang.namePlaceholder" />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createTodo } from '@/api/todo'
import { todo as todoLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', type: 'custom', date: '', desc: '', pri: 3 })
const submitting = ref(false)
const errorMsg = ref('')

onMounted(() => {
  if (!form.value.date) form.value.date = new Date().toISOString().slice(0, 10)
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createTodo(form.value)
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
