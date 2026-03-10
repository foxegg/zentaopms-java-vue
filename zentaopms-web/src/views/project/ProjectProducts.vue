<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">{{ projectLang.products }} · {{ project?.name || '' }}</h1>
      <router-link v-if="project" :to="`/project/${project.id}`" class="btn">{{ projectLang.backProject }}</router-link>
    </div>
    <div v-if="project && !loading" class="table-wrap">
      <form @submit.prevent="onSubmit">
        <h3>{{ projectLang.selectProducts }}</h3>
        <p class="mb-2">{{ projectLang.selectedCount.replace('{n}', selectedIds.length) }}</p>
        <div class="product-checkboxes">
          <label v-for="p in allProducts" :key="p.id" class="checkbox-row">
            <input type="checkbox" :value="p.id" v-model="selectedIds" />
            {{ p.name }}（{{ p.code || p.id }}）
          </label>
        </div>
        <div class="form-actions mt-2">
          <button type="submit" class="btn btn-primary" :disabled="saving">{{ commonLang.save }}</button>
        </div>
      </form>
      <h3 class="mt-2">{{ projectLang.linkedProducts }}</h3>
      <table class="data-table" v-if="products.length">
        <thead>
          <tr><th>{{ productLang.common }}</th><th>{{ productLang.code }}</th></tr>
        </thead>
        <tbody>
          <tr v-for="pp in products" :key="pp.id">
            <td>{{ pp.productName }}</td>
            <td>{{ pp.plan || '-' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>{{ commonLang.noData }}</p>
    </div>
    <p v-else-if="loading">{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById, getProjectProducts, manageProjectProducts } from '@/api/project'
import { getProductList } from '@/api/product'
import { common as commonLang, project as projectLang, product as productLang } from '@/lang/zh-cn'

const route = useRoute()
const projectId = computed(() => Number(route.params.id))
const project = ref(null)
const products = ref([])
const allProducts = ref([])
const selectedIds = ref([])
const loading = ref(true)
const saving = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const [pRes, ppRes, listRes] = await Promise.all([
      getProjectById(projectId.value),
      getProjectProducts(projectId.value),
      getProductList()
    ])
    project.value = pRes.data || null
    products.value = ppRes.data || []
    allProducts.value = listRes.data || []
    selectedIds.value = products.value.map(pp => pp.product)
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  saving.value = true
  try {
    await manageProjectProducts(projectId.value, { productIDs: selectedIds.value })
    const ppRes = await getProjectProducts(projectId.value)
    products.value = ppRes.data || []
  } finally {
    saving.value = false
  }
}
</script>
<style scoped>
.product-checkboxes { display: flex; flex-direction: column; gap: 6px; max-height: 320px; overflow-y: auto; }
.checkbox-row { display: flex; align-items: center; gap: 8px; }
.mt-2 { margin-top: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
</style>
