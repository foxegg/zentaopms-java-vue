<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ stageLang.common }}</h1>
      <router-link to="/stage/create" class="btn btn-primary">{{ stageLang.create }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ stageLang.workflowGroup }}</th>
            <th>{{ projectLang.type }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>{{ s.workflowGroup || '-' }}</td>
            <td>{{ s.type || '-' }}</td>
            <td>
              <router-link :to="`/stage/${s.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/stage/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ stageLang.emptyTip }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStageList } from '@/api/stage'
import { common as commonLang, stage as stageLang, project as projectLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getStageList()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
