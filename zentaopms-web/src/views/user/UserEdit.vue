<template>
  <div>
    <div class="page-header">
      <h1>{{ userLang.edit }}</h1>
      <router-link to="/user" class="btn">{{ commonLang.backList }}</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
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
          <label>{{ userLang.newPassword }}</label>
          <input v-model="form.password" type="password" :placeholder="userLang.newPasswordPlaceholder" />
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
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserById, updateUser } from '@/api/user'
import { user as userLang, common as commonLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => route.params.id)
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')
const form = ref({ account: '', realname: '', password: '', email: '', dept: 0, role: '' })

onMounted(async () => {
  try {
    const res = await getUserById(id.value)
    const u = res?.data ?? res ?? {}
    form.value = { account: u.account, realname: u.realname, password: '', email: u.email || '', dept: u.dept || 0, role: u.role || '' }
  } finally { loading.value = false }
})

async function onSubmit() {
  errorMsg.value = ''
  submitting.value = true
  try {
    const payload = { ...form.value }
    if (!payload.password) delete payload.password
    const res = await updateUser(id.value, payload)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/user')
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
