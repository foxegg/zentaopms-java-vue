<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ kanbanLang.common }} - {{ kanban?.name }}</h1>
      <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">{{ commonLang.backList }}</router-link>
      <button class="btn btn-primary" @click="showCreateCard = true">{{ kanbanLang.cardCreate }}</button>
    </div>
    <p v-if="tipMsg" class="text-info">{{ tipMsg }}</p>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div v-if="kanban" class="table-wrap">
      <h3>{{ kanbanLang.cardList }}</h3>
      <table class="data-table" v-if="cards.length">
        <thead>
          <tr><th>ID</th><th>{{ docLang.title }}</th><th>{{ commonLang.status }}</th><th>{{ commonLang.actions }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="c in cards" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.name || c.title }}</td>
            <td>{{ c.status || '-' }}</td>
            <td>
              <button class="btn" @click="editCard(c)">{{ commonLang.edit }}</button>
              <button class="btn" @click="delCard(c.id)">{{ commonLang.delete }}</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ kanbanLang.noCards }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ kanbanLang.notFound }}</p>
    <div v-if="showCreateCard" class="modal-wrap">
      <div class="modal">
        <h3>{{ kanbanLang.cardCreate }}</h3>
        <form @submit.prevent="onCreateCard">
          <div class="form-group">
            <label>{{ docLang.title }} *</label>
            <input v-model="cardForm.name" required />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
            <button type="button" class="btn" @click="showCreateCard = false">{{ commonLang.cancel }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getKanbanById, getKanbanCards, createCard, deleteCard as apiDeleteCard } from '@/api/kanban'
import { common as commonLang, kanban as kanbanLang, doc as docLang } from '@/lang/zh-cn'

const route = useRoute()
const kanban = ref(null)
const cards = ref([])
const loading = ref(true)
const showCreateCard = ref(false)
const cardForm = ref({ name: '' })
const submitting = ref(false)
const errorMsg = ref('')
const tipMsg = ref('')

const kanbanId = computed(() => Number(route.params.id))
const spaceId = computed(() => kanban.value?.spaceID || kanban.value?.space)

async function load() {
  const id = kanbanId.value
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getKanbanById(id)
    kanban.value = res?.data ?? res
    if (kanban.value) {
      const cRes = await getKanbanCards(id)
      cards.value = cRes?.data ?? []
    }
  } finally {
    loading.value = false
  }
}

function editCard(c) {
  tipMsg.value = kanbanLang.cardEditTip + '：' + (c.name || c.title)
}

async function delCard(id) {
  if (!confirm(commonLang.confirmDelete)) return
  errorMsg.value = ''
  try {
    const res = await apiDeleteCard(id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    load()
  } catch (e) {
    errorMsg.value = e.response?.data?.message || e.message || commonLang.operateFail
  }
}

async function onCreateCard() {
  if (!cardForm.value.name) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createCard({ kanban: kanbanId.value, name: cardForm.value.name })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    cardForm.value = { name: '' }
    showCreateCard.value = false
    load()
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.modal-wrap { position: fixed; inset: 0; background: rgba(0,0,0,.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; padding: 20px; border-radius: 8px; min-width: 320px; }
.text-info { color: #06c; margin-bottom: 0.5rem; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
