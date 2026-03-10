<template>
  <div>
    <div class="page-header">
      <h1>{{ productLang.create }}</h1>
      <router-link to="/product" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ productLang.name }} *</label>
          <input v-model="form.name" required maxlength="110" />
        </div>
        <div class="form-group">
          <label>{{ productLang.code }}</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>{{ productLang.type }}</label>
          <select v-model="form.type">
            <option value="normal">{{ productLang.typeList.normal }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>{{ productLang.desc }}</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/product" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProduct } from '@/api/product'
import { product as productLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ name: '', code: '', type: 'normal', description: '' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createProduct(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    const id = res?.id ?? res?.data?.id
    if (id) router.push('/product/' + id)
    else router.push('/product')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
