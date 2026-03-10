<template>
  <div>
    <div class="page-header">
      <h1>{{ storyLang.create }}</h1>
      <router-link to="/story" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ storyLang.product }} *</label>
          <select v-model.number="form.product" required>
            <option :value="0">{{ storyLang.selectProduct }}</option>
            <option v-for="p in products" :key="p.id" :value="p.id">{{ p.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ storyLang.title }} *</label>
          <input v-model="form.title" required />
        </div>
        <div class="form-group">
          <label>{{ storyLang.pri }}</label>
          <select v-model.number="form.pri">
            <option v-for="n in 4" :key="n" :value="n">{{ n }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ storyLang.spec }}</label>
          <textarea v-model="form.description" rows="4"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/story" class="btn">{{ commonLang.cancel }}</router-link>
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
import { story as storyLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const form = ref({ product: 0, title: '', pri: 3, description: '' })
const products = ref([])
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (!form.value.product || !form.value.title?.trim()) return
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createStory({
      product: form.value.product,
      title: form.value.title,
      pri: form.value.pri,
      description: form.value.description || undefined
    })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push(`/story/${id}`)
    else router.push('/story')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getProductList({ mode: 'noclosed' })
  products.value = r?.data ?? r ?? []
  if (route.query.product) form.value.product = Number(route.query.product) || 0
})
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
