<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ storyLang.browse }}</h1>
      <router-link to="/story/create" class="btn btn-primary">{{ storyLang.createStory }}</router-link>
    </div>
    <div class="filter mb-2">
      <label>{{ storyLang.product }}</label>
      <select v-model.number="productId" @change="load">
        <option :value="0">{{ storyLang.all }}</option>
        <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
      </select>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>{{ storyLang.id }}</th>
            <th>{{ storyLang.title }}</th>
            <th>{{ storyLang.status }}</th>
            <th>{{ storyLang.assignTo }}</th>
            <th>{{ commonLang.actions }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.title }}</td>
            <td>{{ s.status }}</td>
            <td>{{ s.assignedTo }}</td>
            <td>
              <router-link :to="`/story/${s.id}`" class="btn">{{ commonLang.view }}</router-link>
              <router-link :to="`/story/edit/${s.id}`" class="btn">{{ commonLang.edit }}</router-link>
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
import { getStoryList } from '@/api/story'
import { getProductList } from '@/api/product'
import { story as storyLang, common as commonLang } from '@/lang/zh-cn'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const productId = ref(0)
const products = ref([])
const pageID = ref(1)
const recPerPage = 20

async function load(page) {
  if (page) pageID.value = page
  loading.value = true
  try {
    const params = { pageID: pageID.value, recPerPage }
    if (productId.value > 0) params.product = productId.value
    const res = await getStoryList(params)
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
  load()
})
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
.filter select { margin-left: 8px; }
</style>
