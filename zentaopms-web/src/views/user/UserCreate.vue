<template>
  <div>
    <div class="page-header">
      <h1>{{ userLang.create }}</h1>
      <router-link to="/user" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ userLang.account }} *</label>
          <input v-model="form.account" required />
        </div>
        <div class="form-group">
          <label>{{ userLang.realname }} *</label>
          <input v-model="form.realname" required />
        </div>
        <div class="form-group">
          <label>{{ userLang.password }} *</label>
          <input v-model="form.password" type="password" required />
        </div>
        <div class="form-group">
          <label>{{ userLang.password2 }} *</label>
          <input v-model="form.password2" type="password" required />
        </div>
        <div class="form-group">
          <label>{{ userLang.email }}</label>
          <input v-model="form.email" type="email" />
        </div>
        <div class="form-group">
          <label>{{ userLang.dept }}</label>
          <input v-model.number="form.dept" type="number" />
        </div>
        <div class="form-group">
          <label>{{ userLang.role }}</label>
          <input v-model="form.role" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">{{ commonLang.save }}</button>
          <router-link to="/user" class="btn">{{ commonLang.cancel }}</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createUser } from '@/api/user'
import { user as userLang, common as commonLang } from '@/lang/zh-cn'

const router = useRouter()
const form = ref({ account: '', realname: '', password: '', password2: '', email: '', dept: 0, role: 'dev' })
const submitting = ref(false)
const errorMsg = ref('')

async function onSubmit() {
  if (form.value.password !== form.value.password2) {
    errorMsg.value = commonLang.passwordNotMatch
    return
  }
  errorMsg.value = ''
  submitting.value = true
  try {
    const res = await createUser({
      account: form.value.account,
      realname: form.value.realname,
      password: form.value.password,
      email: form.value.email || undefined,
      dept: form.value.dept || undefined,
      role: form.value.role || undefined
    })
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/user')
  } catch (err) {
    const msg = err.response?.data?.message || err.message || commonLang.operateFail
    errorMsg.value = msg
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
