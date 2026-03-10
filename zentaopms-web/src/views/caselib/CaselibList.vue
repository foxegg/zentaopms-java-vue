<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ caselibLang.common }}</h1>
      <router-link to="/caselib/create" class="btn btn-primary">{{ caselibLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ productLang.common }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ storyLang.all }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <label class="ml-2">{{ projectLang.common }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ storyLang.all }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ commonLang.name }}</th>
            <th>{{ productLang.common }}</th>
            <th>{{ projectLang.common }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>{{ productName(s.product) }}</td>
            <td>{{ projectName(s.project) }}</td>
            <td>
              <router-link :to="`/caselib/${s.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/caselib/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0">{{ caselibLang.emptyTip }}</p>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCaselibList } from '@/api/caselib'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'
import { common as commonLang, product as productLang, project as projectLang, caselib as caselibLang, story as storyLang } from '@/lang/zh-cn'

const list = ref([])
const loading = ref(true)
const productId = ref(0)
const projectId = ref(0)
const products = ref([])
const projects = ref([])

function productName(id) {
  if (!id) return '-'
  const p = products.value.find((x) => x.id === id)
  return p ? p.name : id
}
function projectName(id) {
  if (!id) return '-'
  const p = projects.value.find((x) => x.id === id)
  return p ? p.name : id
}

async function load() {
  loading.value = true
  try {
    const params = {}
    if (productId.value > 0) params.product = productId.value
    if (projectId.value > 0) params.project = projectId.value
    const res = await getCaselibList(params)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const [pr, proj] = await Promise.all([getProductList(), getProjectAll()])
  products.value = pr.data || []
  projects.value = proj.data || []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.ml-2 { margin-left: 8px; }
.filter select { margin-left: 4px; }
</style>
