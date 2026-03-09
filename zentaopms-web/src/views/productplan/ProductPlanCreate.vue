<template>
  <div>
    <div class="page-header">
      <h1>新建产品计划</h1>
      <router-link :to="backUrl" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>产品 *</label>
          <select v-model.number="form.product" required>
            <option :value="0">请选择</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>开始日期</label>
          <input v-model="form.begin" type="date" />
        </div>
        <div class="form-group">
          <label>结束日期</label>
          <input v-model="form.end" type="date" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="backUrl" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createProductPlan } from '@/api/productplan'
import { getProductList } from '@/api/product'

const route = useRoute()
const router = useRouter()
const form = ref({ product: 0, name: '', begin: '', end: '' })
const products = ref([])
const submitting = ref(false)

const backUrl = computed(() => form.value.product ? `/productplan?product=${form.value.product}` : '/productplan')

async function onSubmit() {
  if (!form.value.product) return
  submitting.value = true
  try {
    const res = await createProductPlan(form.value)
    router.push(`/productplan/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
  const q = route.query.product
  if (q) form.value.product = Number(q) || 0
})
</script>
