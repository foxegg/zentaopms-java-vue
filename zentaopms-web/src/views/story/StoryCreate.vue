<template>
  <div>
    <div class="page-header">
      <h1>新建需求</h1>
      <router-link to="/story" class="btn">返回列表</router-link>
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
          <label>标题 *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>优先级</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/story" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { createStory } from '@/api/story'
import { getProductList } from '@/api/product'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, title: '', pri: 3, description: '' })
const products = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.product) return
  submitting.value = true
  try {
    const res = await createStory({
      product: form.value.product,
      title: form.value.title,
      pri: form.value.pri,
      description: form.value.description || undefined
    })
    router.push(`/story/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList()
  products.value = r.data || []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
})
</script>
