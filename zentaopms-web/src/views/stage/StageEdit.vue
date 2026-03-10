<template>
  <div>
    <div class="page-header">
      <h1>{{ stageLang.edit }}</h1>
      <router-link :to="`/stage/${id}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>{{ stageLang.workflowGroup }}</label>
          <input v-model.number="form.workflowGroup" type="number" />
        </div>
        <div class="form-group">
          <label>{{ stageLang.percent }}</label>
          <input v-model="form.percent" />
        </div>
        <div class="form-group">
          <label>{{ projectLang.type }}</label>
          <input v-model="form.type" />
        </div>
        <div class="form-group">
          <label>{{ stageLang.projectType }}</label>
          <input v-model="form.projectType" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="`/stage/${id}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStageById, updateStage } from '@/api/stage'
import { common as commonLang, stage as stageLang, project as projectLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => Number(route.params.id))
const form = ref({ name: '', workflowGroup: 0, percent: '', type: '', projectType: '' })
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  try {
    const res = await getStageById(id.value)
    const d = res.data || res
    if (d) form.value = { name: d.name || '', workflowGroup: d.workflowGroup ?? 0, percent: d.percent || '', type: d.type || '', projectType: d.projectType || '' }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateStage(id.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/stage/${id.value}`)
  } catch (e) {
    errorMsg.value = e?.response?.data?.message || e.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 4px; }
.form-group input { width: 100%; max-width: 320px; }
.form-actions { margin-top: 1rem; display: flex; gap: 8px; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
