<template>
  <div>
    <div class="page-header">
      <h1>{{ projectLang.edit }}</h1>
      <router-link :to="`/project/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
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
          <label>{{ projectLang.begin }}</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>{{ projectLang.end }}</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-group">
          <label>{{ projectLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="`/project/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProjectById, updateProject } from '@/api/project'
import { project as projectLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getProjectById(id.value)
    const p = res.data
    if (p) {
      form.value = {
        name: p.name || '',
        code: p.code || '',
        type: p.type || 'sprint',
        begin: p.begin ? p.begin.slice(0, 10) : '',
        end: p.end ? p.end.slice(0, 10) : '',
        description: p.description || ''
      }
    }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  if (form.value.begin && form.value.end && form.value.begin > form.value.end) {
    errorMsg.value = commonLang.dateRangeError
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateProject(id.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/project/${id.value}`)
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
