<template>
  <div>
    <div class="page-header">
      <h1>{{ kanbanLang.create }}</h1>
      <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ kanbanLang.space }} *</label>
          <select v-model.number="form.spaceID" required>
            <option :value="0">{{ commonLang.pleaseSelect }}</option>
            <option v-for="s in spaces" :key="s.id" :value="s.id">{{ s.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ commonLang.name }} *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createKanban, getSpaceList } from '@/api/kanban'
import { common as commonLang, kanban as kanbanLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const spaceId = computed(() => Number(route.query.spaceID) || 0)
const form = ref({ spaceID: 0, name: '' })
const spaces = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.spaceID) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createKanban({ space: form.value.spaceID, name: form.value.name })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/kanban/board/${id}`)
    else router.push(spaceId.value ? `/kanban/space/${spaceId.value}` : '/kanban')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getSpaceList()
  spaces.value = r?.data ?? []
  if (spaceId.value) form.value.spaceID = spaceId.value
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
