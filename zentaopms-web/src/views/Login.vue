<template>
  <div class="login-page">
    <div class="login-box">
      <h1>{{ appLang.title }}</h1>
      <form @submit.prevent="onSubmit" class="login-form">
        <div class="form-group">
          <label>{{ userLang.account }}</label>
          <input v-model="account" type="text" required :placeholder="userLang.placeholderAccount" />
        </div>
        <div class="form-group">
          <label>{{ userLang.password }}</label>
          <input v-model="password" type="password" required :placeholder="userLang.placeholderPassword" />
        </div>
        <div v-if="error" class="error-msg">{{ error }}</div>
        <button type="submit" class="btn btn-primary" :disabled="loading">{{ userLang.login }}</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuth } from '@/composables/useAuth'
import { login as apiLogin } from '@/api/auth'
import { app as appLang, user as userLang } from '@/lang/zh-cn'

const router = useRouter()
const route = useRoute()
const { setToken } = useAuth()

const account = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const onSubmit = async () => {
  error.value = ''
  loading.value = true
  try {
    const res = await apiLogin(account.value, password.value)
    if (res?.token) {
      setToken(res.token)
      const redirect = route.query.redirect || '/'
      router.push(redirect)
    } else {
      error.value = res?.message || userLang.loginFailed
    }
  } catch (e) {
    error.value = e.response?.data?.message || e.message || userLang.loginFailed
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a73e8 0%, #0d47a1 100%);
}
.login-box {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 380px;
}
.login-box h1 {
  margin: 0 0 24px;
  font-size: 20px;
  text-align: center;
  color: #333;
}
.login-form .form-group input {
  max-width: none;
}
.login-form .btn {
  width: 100%;
  padding: 10px;
  margin-top: 8px;
}
.error-msg {
  color: #c00;
  font-size: 13px;
  margin-bottom: 8px;
}
</style>
