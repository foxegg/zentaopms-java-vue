<template>
  <div>
    <div class="page-header"><h1>{{ myLang.story }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ storyLang.id }}</th>
            <th>{{ storyLang.title }}</th>
            <th>{{ storyLang.status }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.title }}</td>
            <td>{{ s.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ myLang.noData }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyStory } from '@/api/my'
import { my as myLang, story as storyLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
onMounted(async () => {
  try {
    const res = await getMyStory()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
})
</script>
