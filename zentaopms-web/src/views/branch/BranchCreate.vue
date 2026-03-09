<template>
  <div>
    <div class="page-header">
      <h1>新建分支</h1>
      <router-link to="/branch" class="btn">返回列表</router-link>
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
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/branch" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createBranch } from '@/api/branch'
import { getProductList } from '@/api/product'

const route = useRoute()
const router = useRouter()
const form = ref({ product: 0, name: '' })
const products = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.product) return
  submitting.value = true
  try {
    const res = await createBranch(form.value)
    router.push(`/branch/${res.id}`)
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
