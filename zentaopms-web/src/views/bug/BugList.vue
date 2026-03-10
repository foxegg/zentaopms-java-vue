<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ bugLang.browse }}</h1>
      <router-link to="/bug/create" class="btn btn-primary">{{ bugLang.create }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ bugLang.product }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ bugLang.all }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
      <label class="ml-2">{{ bugLang.project }}</label>
      <select v-model.number="projectId" @change="load">
        <option :value="0">{{ bugLang.all }}</option>
        <option v-for="p in projects" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ bugLang.title }}</th>
            <th>{{ bugLang.severity }}</th>
            <th>{{ bugLang.pri }}</th>
            <th>{{ bugLang.openedBy }}</th>
            <th>{{ bugLang.assignTo }}</th>
            <th>{{ bugLang.resolvedBy }}</th>
            <th>{{ bugLang.resolution }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="b in list" :key="b.id">
            <td>{{ b.id }}</td>
            <td>{{ b.title }}</td>
            <td>{{ b.severity }}</td>
            <td>{{ b.pri }}</td>
            <td>{{ b.openedBy || '—' }}</td>
            <td>{{ b.assignedTo || '—' }}</td>
            <td>{{ b.resolvedBy || '—' }}</td>
            <td>{{ b.resolution || '—' }}</td>
            <td>
              <router-link :to="`/bug/${b.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/bug/edit/${b.id}`" class="btn">{{ commonLang.edit }}</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager && pager.recTotal > 0">
        <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
        <span>{{ commonLang.pageNum.replace('{n}', pageID) }}</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
      </div>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getBugList } from '@/api/bug'
import { getProductList } from '@/api/product'
import { getProjectAll } from '@/api/project'
import { bug as bugLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const productId = ref(0)
const projectId = ref(0)
const products = ref([])
const projects = ref([])
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (page) pageID.value = page
  loading.value = true
  try {
    const params = { pageID: pageID.value, recPerPage }
    if (productId.value > 0) params.product = productId.value
    if (projectId.value > 0) params.project = projectId.value
    const res = await getBugList(params)
    list.value = res?.data ?? []
    pager.value = res?.pager ?? {}
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const q = route.query.project
  if (q) projectId.value = Number(q) || 0
  const [pr, proj] = await Promise.all([getProductList(), getProjectAll()])
  products.value = pr?.data ?? pr ?? []
  projects.value = proj?.data ?? proj ?? []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.ml-2 { margin-left: 8px; }
.filter select { margin-left: 4px; }
</style>
