<template>
  <div>
    <div class="page-header">
      <h1>{{ kanbanLang.spaceEdit }}</h1>
      <router-link :to="`/kanban/space/${spaceId}`" class="btn">{{ commonLang.backDetail }}</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="`/kanban/space/${spaceId}`" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ kanbanLang.spaceNotFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSpaceById, updateSpace } from '@/api/kanban'
import { common as commonLang, kanban as kanbanLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const spaceId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

onMounted(async () => {
  const id = spaceId.value
  loading.value = true
  try {
    const res = await getSpaceById(id)
    const s = res?.data ?? res
    if (s) form.value = { name: s.name }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await updateSpace(spaceId.value, form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push(`/kanban/space/${spaceId.value}`)
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
