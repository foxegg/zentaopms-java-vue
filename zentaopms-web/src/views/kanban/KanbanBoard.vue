<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">看板 - {{ kanban?.name }}</h1>
      <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">返回</router-link>
      <button class="btn btn-primary" @click="showCreateCard = true">新建卡片</button>
    </div>
    <div v-if="kanban" class="table-wrap">
      <h3>卡片列表</h3>
      <table class="data-table" v-if="cards.length">
        <thead>
          <tr><th>ID</th><th>标题</th><th>状态</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="c in cards" :key="c.id">
            <td>{{ c.id }}</td>
            <td>{{ c.name || c.title }}</td>
            <td>{{ c.status || '-' }}</td>
            <td>
              <button class="btn" @click="editCard(c)">编辑</button>
              <button class="btn" @click="delCard(c.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无卡片</p>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>看板不存在</p>
    <div v-if="showCreateCard" class="modal-wrap">
      <div class="modal">
        <h3>新建卡片</h3>
        <form @submit.prevent="onCreateCard">
          <div class="form-group">
            <label>标题 *</label>
            <input v-model="cardForm.name" required />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
            <button type="button" class="btn" @click="showCreateCard = false">取消</button>
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

const route = useRoute()
const kanban = ref(null)
const cards = ref([])
const loading = ref(true)
const showCreateCard = ref(false)
const cardForm = ref({ name: '' })
const submitting = ref(false)

const kanbanId = computed(() => Number(route.params.id))
const spaceId = computed(() => kanban.value?.spaceID || kanban.value?.space)

async function load() {
  const id = kanbanId.value
  loading.value = true
  try {
    const res = await getKanbanById(id)
    kanban.value = res.data || null
    if (kanban.value) {
      const cRes = await getKanbanCards(id)
      cards.value = cRes.data || []
    }
  } finally {
    loading.value = false
  }
}

function editCard(c) {
  alert('编辑卡片：' + (c.name || c.title) + '（可后续扩展弹窗编辑）')
}

async function delCard(id) {
  if (!confirm('确定删除？')) return
  try {
    await apiDeleteCard(id)
    load()
  } catch (e) {
    alert(e.response?.data?.message || '删除失败')
  }
}

async function onCreateCard() {
  if (!cardForm.value.name) return
  submitting.value = true
  try {
    await createCard({ kanban: kanbanId.value, name: cardForm.value.name })
    cardForm.value = { name: '' }
    showCreateCard.value = false
    load()
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.modal-wrap { position: fixed; inset: 0; background: rgba(0,0,0,.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; padding: 20px; border-radius: 8px; min-width: 320px; }
</style>
