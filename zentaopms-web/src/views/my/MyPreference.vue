<template>
  <div>
    <div class="page-header"><h1>偏好设置</h1></div>
    <div class="table-wrap" v-if="!loading">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>每页条数</label>
          <input v-model.number="form.recPerPage" type="number" min="10" max="100" />
        </div>
        <button type="submit" class="btn btn-primary">保存</button>
      </form>
      <hr />
      <h3>修改密码</h3>
      <form @submit.prevent="onChangePassword">
        <div class="form-group"><label>旧密码</label><input v-model="oldPwd" type="password" /></div>
        <div class="form-group"><label>新密码</label><input v-model="newPwd" type="password" /></div>
        <button type="submit" class="btn btn-primary">修改密码</button>
      </form>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyPreference, updateMyPreference, changePassword } from '@/api/my'

const loading = ref(true)
const form = ref({ recPerPage: 20 })
const oldPwd = ref('')
const newPwd = ref('')

onMounted(async () => {
  try {
    const res = await getMyPreference()
    if (res.data) form.value = { ...form.value, ...res.data }
  } finally { loading.value = false }
})

async function onSubmit() {
  await updateMyPreference(form.value)
  alert('已保存')
}

async function onChangePassword() {
  await changePassword(oldPwd.value, newPwd.value)
  oldPwd.value = ''
  newPwd.value = ''
  alert('密码已修改')
}
</script>
