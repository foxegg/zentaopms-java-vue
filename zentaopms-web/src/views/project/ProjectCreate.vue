<template>
  <div>
    <div class="page-header">
      <h1>{{ projectLang.create }}</h1>
      <router-link to="/project" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ projectLang.name }} *</label>
          <input v-model="form.name" required maxlength="90" />
        </div>
        <div class="form-group">
          <label>{{ projectLang.code }}</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>{{ projectLang.type }}</label>
          <select v-model="form.type">
            <option value="sprint">{{ projectLang.typeList.sprint }}</option>
            <option value="kanban">{{ projectLang.typeList.kanban }}</option>
            <option value="stage">{{ projectLang.typeList.stage }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ projectLang.begin }} *</label>
          <input v-model="form.begin" type="date" required />
        </div>
        <div class="form-group">
          <label>{{ projectLang.end }} *</label>
          <input v-model="form.end" type="date" required />
        </div>
        <div class="form-group">
          <label>{{ projectLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/project" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProject } from '@/api/project'
import { project as projectLang, common as commonLang } from '@/lang/zh-cn'

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
const errorMsg = ref('')

async function onSubmit() {
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    errorMsg.value = commonLang.dateRangeError
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createProject(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/project/${id}`)
    else router.push('/project')
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
