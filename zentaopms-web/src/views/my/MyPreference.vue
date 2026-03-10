<template>
  <div>
    <div class="page-header"><h1>{{ myLang.preference }}</h1></div>
    <div class="table-wrap" v-if="!loading">
      <p v-if="errorMsg" class="text-danger">{{ errorMsg }}</p>
      <p v-if="successMsg" class="text-success">{{ successMsg }}</p>
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>{{ myLang.recPerPage }}</label>
          <input v-model.number="form.recPerPage" type="number" min="10" max="100" />
        </div>
        <button type="submit" class="btn btn-primary">{{ commonLang.save }}</button>
      </form>
      <hr />
      <h3>{{ userLang.changePassword }}</h3>
      <form @submit.prevent="onChangePassword">
        <div class="form-group"><label>{{ userLang.originalPassword }}</label><input v-model="oldPwd" type="password" /></div>
        <div class="form-group"><label>{{ userLang.newPassword }}</label><input v-model="newPwd" type="password" /></div>
        <button type="submit" class="btn btn-primary">{{ userLang.changePassword }}</button>
      </form>
    </div>
    <p v-else>{{ commonLang.loading }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyPreference, updateMyPreference, changePassword } from '@/api/my'
import { my as myLang, user as userLang, common as commonLang } from '@/lang/zh-cn'

const loading = ref(true)
const form = ref({ recPerPage: 20 })
const oldPwd = ref('')
const newPwd = ref('')
const errorMsg = ref('')
const successMsg = ref('')

onMounted(async () => {
  try {
    const res = await getMyPreference()
    if (res?.data) form.value = { ...form.value, ...res.data }
  } finally { loading.value = false }
})

async function onSubmit() {
  errorMsg.value = ''
  successMsg.value = ''
  try {
    const res = await updateMyPreference(form.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    successMsg.value = commonLang.saved
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  }
}

async function onChangePassword() {
  errorMsg.value = ''
  successMsg.value = ''
  try {
    const res = await changePassword(oldPwd.value, newPwd.value)
    if (res?.result === 'fail') {
      errorMsg.value = res.message || commonLang.operateFail
      return
    }
    oldPwd.value = ''
    newPwd.value = ''
    successMsg.value = userLang.passwordChanged
  } catch (err) {
    errorMsg.value = err.response?.data?.message || err.message || commonLang.operateFail
  }
}
</script>

<style scoped>
.text-danger { color: #c00; margin-bottom: 0.5rem; }
.text-success { color: #0a0; margin-bottom: 0.5rem; }
</style>
