<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ productplanLang.common }}</h1>
      <router-link :to="plan ? `/productplan?product=${plan.product}` : '/productplan'" class="btn">{{ commonLang.backList }}</router-link>
      <router-link v-if="plan" :to="`/productplan/edit/${plan.id}`" class="btn">{{ commonLang.edit }}</router-link>
    </div>
    <div v-if="plan" class="table-wrap">
      <table class="data-table">
        <tr><th>ID</th><td>{{ plan.id }}</td></tr>
        <tr><th>{{ commonLang.name }}</th><td>{{ plan.name }}</td></tr>
        <tr><th>{{ commonLang.status }}</th><td>{{ plan.status }}</td></tr>
        <tr><th>{{ executionLang.begin }}</th><td>{{ plan.begin || '-' }}</td></tr>
        <tr><th>{{ executionLang.end }}</th><td>{{ plan.end || '-' }}</td></tr>
      </table>
      <h3>{{ productplanLang.relatedStory }}</h3>
      <table class="data-table" v-if="stories.length">
        <thead>
          <tr><th>ID</th><th>{{ storyLang.title }}</th><th>{{ commonLang.status }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="s in stories" :key="s.id">
            <td>{{ s.id }}</td>
            <td><router-link :to="`/story/${s.id}`">{{ s.title }}</router-link></td>
            <td>{{ s.status }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ productplanLang.noStories }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
    <p v-else>{{ productplanLang.notFound }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProductPlanById, getPlanStories } from '@/api/productplan'
import { common as commonLang, productplan as productplanLang, execution as executionLang, story as storyLang } from '@/lang/zh-cn'

const route = useRoute()
const plan = ref(null)
const stories = ref([])
const loading = ref(true)

onMounted(async () => {
  const id = Number(route.params.id)
  loading.value = true
  try {
    const res = await getProductPlanById(id)
    plan.value = res.data || null
    if (plan.value) {
      const sRes = await getPlanStories(id)
      stories.value = sRes.data || []
    }
  } finally {
    loading.value = false
  }
})
</script>
