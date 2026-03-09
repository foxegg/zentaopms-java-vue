<template>
  <div>
    <div class="page-header">
      <h1>新建产品</h1>
      <router-link to="/product" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required maxlength="110" />
        </div>
        <div class="form-group">
          <label>代号</label>
          <input v-model="form.code" maxlength="45" />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type">
            <option value="normal">正常</option>
          </select>
        </div>
        <div class="form-group">
          <label>描述</label>
          <textarea v-model="form.description" rows="3"></textarea>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/product" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProduct } from '@/api/product'

const router = useRouter()
const form = ref({ name: '', code: '', type: 'normal', description: '' })
const submitting = ref(false)

async function onSubmit() {
  submitting.value = true
  try {
    const res = await createProduct(form.value)
    router.push('/product/' + res.id)
  } finally {
    submitting.value = false
  }
}
</script>
