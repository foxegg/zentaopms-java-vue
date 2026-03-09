<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">关联产品 · {{ project?.name || '' }}</h1>
      <router-link v-if="project" :to="`/project/${project.id}`" class="btn">返回项目</router-link>
    </div>
    <div v-if="project && !loading" class="table-wrap">
      <form @submit.prevent="onSubmit">
        <h3>选择要关联的产品</h3>
        <p class="mb-2">已选 {{ selectedIds.length }} 个产品</p>
        <div class="product-checkboxes">
          <label v-for="p in allProducts" :key="p.id" class="checkbox-row">
            <input type="checkbox" :value="p.id" v-model="selectedIds" />
            {{ p.name }}（{{ p.code || p.id }}）
          </label>
        </div>
        <div class="form-actions mt-2">
          <button type="submit" class="btn btn-primary" :disabled="saving">保存</button>
        </div>
      </form>
      <h3 class="mt-2">当前已关联</h3>
      <table class="data-table" v-if="products.length">
        <thead>
          <tr><th>产品</th><th>代号</th></tr>
        </thead>
        <tbody>
          <tr v-for="pp in products" :key="pp.id">
            <td>{{ pp.productName }}</td>
            <td>{{ pp.plan || '-' }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无关联产品</p>
    </div>
    <p v-else-if="loading">加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectById, getProjectProducts, manageProjectProducts } from '@/api/project'
import { getProductList } from '@/api/product'

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
