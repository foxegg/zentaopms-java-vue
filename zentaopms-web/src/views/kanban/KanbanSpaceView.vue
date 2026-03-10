<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ kanbanLang.space }}</h1>
      <router-link to="/kanban" class="btn">{{ kanbanLang.backSpaceList }}</router-link>
      <router-link v-if="space" :to="`/kanban/space/edit/${space.id}`" class="btn">{{ commonLang.edit }}</router-link>
      <router-link v-if="space" :to="`/kanban/create?spaceID=${space.id}`" class="btn btn-primary">{{ kanbanLang.create }}</router-link>
    </div>
    <div v-if="space" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ space.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ space.name }}</td></tr>
      </table>
      <h3>{{ kanbanLang.common }}</h3>
      <table class="data-table" v-if="kanbans.length">
        <thead>
          <tr><th>ID</th><th>{{ commonLang.name }}</th><th>{{ commonLang.actions }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="k in kanbans" :key="k.id">
            <td>{{ k.id }}</td>
            <td>{{ k.name }}</td>
            <td><router-link :to="`/kanban/board/${k.id}`" class="btn">{{ commonLang.view }}</router-link></td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ kanbanLang.noBoards }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ kanbanLang.spaceNotFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getSpaceById, getKanbanList } from '@/api/kanban'
import { common as commonLang, kanban as kanbanLang } from '@/lang/zh-cn'

const route = useRoute()
const space = ref(null)
const kanbans = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getSpaceById(id)
    space.value = res.data || null
    if (space.value) {
      const kRes = await getKanbanList(id)
      kanbans.value = kRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
