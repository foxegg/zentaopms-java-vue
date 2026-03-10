<template>
  <div>
    <div class="page-header">
      <h1>{{ executionLang.create }}</h1>
      <router-link :to="backUrl" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ executionLang.project }} *</label>
          <select v-model.number="form.project" required>
            <option :value="0">{{ executionLang.selectProject }}</option>
            <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ executionLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ executionLang.code }}</label>
          <input v-model="form.code" />
        </div>
        <div class="form-group">
          <label>{{ executionLang.begin }}</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>{{ executionLang.end }}</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="backUrl" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createExecution } from '@/api/execution'
import { getProjectAll } from '@/api/project'
import { execution as executionLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const form = ref({ project: 0, name: '', code: '', begin: '', end: '' })
const projects = ref([])
const submitting = ref(false)
const errorMsg = ref('')

const backUrl = computed(() => form.value.project ? `/execution?project=${form.value.project}` : '/execution')

async function onSubmit() {
  if (!form.value.project) return
  submitting.value = true
  errorMsg.value = ''
  try {
    const res = await createExecution(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/execution/${id}`)
    else router.push(backUrl.value)
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProjectAll()
  projects.value = r.data || []
  const q = route.query.project
  if (q) form.value.project = Number(q) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
