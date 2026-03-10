<template>
  <div v-if="user">
    <div class="page-header" style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
      <h1 style="margin:0;">{{ user.realname }}（{{ user.account }}）</h1>
      <router-link :to="`/user/edit/${id}`" class="btn">{{ commonLang.edit }}</router-link>
      <button type="button" class="btn" @click="onUnlock" :disabled="actioning">{{ userLang.unlock }}</button>
      <button type="button" class="btn" @click="showReset = true" :disabled="actioning">{{ userLang.resetPassword }}</button>
      <button type="button" class="btn" @click="onUnbind" :disabled="actioning">{{ userLang.unbind }}</button>
      <button type="button" class="btn btn-danger" @click="onDelete" :disabled="actioning">{{ commonLang.delete }}</button>
    </div>
    <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
    <div v-if="showReset" class="modal-mask" @click.self="showReset = false">
      <div class="modal-box">
        <h3>{{ userLang.resetPassword }}</h3>
        <div class="form-group"><label>{{ userLang.newPassword }}</label><input v-model="resetPassword" type="password" /></div>
        <div class="form-actions">
          <button class="btn btn-primary" @click="onResetPassword" :disabled="!resetPassword || actioning">{{ commonLang.confirm }}</button>
          <button class="btn" @click="showReset = false">{{ commonLang.cancel }}</button>
        </div>
      </div>
    </div>
    <nav class="tabs">
      <router-link :to="`/user/${id}/todo`">{{ myLang.todo }}</router-link>
      <router-link :to="`/user/${id}/task`">{{ taskLang.common }}</router-link>
      <router-link :to="`/user/${id}/bug`">{{ bugLang.common }}</router-link>
      <router-link :to="`/user/${id}/story`">{{ storyLang.common }}</router-link>
      <router-link :to="`/user/${id}/testtask`">{{ commonLang.testtask }}</router-link>
      <router-link :to="`/user/${id}/testcase`">{{ commonLang.testcase }}</router-link>
      <router-link :to="`/user/${id}/execution`">{{ executionLang.common }}</router-link>
      <router-link :to="`/user/${id}/dynamic`">{{ executionLang.dynamic }}</router-link>
      <router-link :to="`/user/${id}/profile`">{{ myLang.profile }}</router-link>
    </nav>
    <router-view :user="user" />
  </div>
  <p v-else-if="!loading">{{ commonLang.notFound }}</p>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserById, unlockUser, resetUserPassword, unbindUser, deleteUser } from '@/api/user'
import { user as userLang, common as commonLang, my as myLang, task as taskLang, bug as bugLang, story as storyLang, execution as executionLang } from '@/lang/zh-cn'

const route = useRoute()
const router = useRouter()
const id = computed(() => route.params.id)
const user = ref(null)
const loading = ref(true)
const actioning = ref(false)
const showReset = ref(false)
const resetPassword = ref('')
const errorMsg = ref('')

async function fetchUser() {
  if (!id.value) return
  loading.value = true
  try {
    const res = await getUserById(id.value)
    user.value = res.data || null
  } catch {
    user.value = null
  } finally {
    loading.value = false
  }
}

async function onUnlock() {
  if (!user.value) return
  actioning.value = true
  try {
    await unlockUser(user.value.id)
    await fetchUser()
  } finally {
    actioning.value = false
  }
}

async function onResetPassword() {
  if (!user.value || !resetPassword.value) return
  actioning.value = true
  try {
    await resetUserPassword(user.value.id, resetPassword.value)
    showReset.value = false
    resetPassword.value = ''
  } finally {
    actioning.value = false
  }
}

async function onUnbind() {
  if (!user.value || !confirm(userLang.confirmUnbind)) return
  errorMsg.value = ''
  actioning.value = true
  try {
    const res = await unbindUser(user.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    await fetchUser()
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    actioning.value = false
  }
}

async function onDelete() {
  if (!user.value || !confirm(userLang.confirmDelete)) return
  errorMsg.value = ''
  actioning.value = true
  try {
    const res = await deleteUser(user.value.id)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    router.push('/user')
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  } finally {
    actioning.value = false
  }
}

watch(id, fetchUser)
onMounted(fetchUser)
</script>

<style scoped>
.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.4); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-box { background: #fff; padding: 1rem; border-radius: 8px; min-width: 280px; }
.form-actions { margin-top: 0.5rem; display: flex; gap: 8px; }
.btn-danger { background: #c00; color: #fff; }
.text-danger { color: #c00; margin-bottom: 0.5rem; }
</style>
