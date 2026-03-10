<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ kanbanLang.space }}</h1>
      <router-link to="/kanban/space/create" class="btn btn-primary">{{ kanbanLang.spaceCreate }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>
              <router-link :to="`/kanban/space/${s.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/kanban/space/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ kanbanLang.spaceListEmpty }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSpaceList } from '@/api/kanban'
import { common as commonLang, kanban as kanbanLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

async function load() {
  loading.value = true
  try {
    const res = await getSpaceList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
