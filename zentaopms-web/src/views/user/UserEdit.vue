<template>
  <div>
    <div class="page-header">
      <h1>编辑用户</h1>
      <router-link to="/user" class="btn">返回列表</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <form @submit.prevent="onSubmit">
        <div class="form-group"><label>账号 *</label><input v-model="form.account" required /></div>
        <div class="form-group"><label>姓名 *</label><input v-model="form.realname" required /></div>
        <div class="form-group"><label>新密码（不填则不修改）</label><input v-model="form.password" type="password" /></div>
        <div class="form-group"><label>邮箱</label><input v-model="form.email" type="email" /></div>
        <div class="form-group"><label>部门</label><input v-model.number="form.dept" type="number" /></div>
        <div class="form-group"><label>角色</label><input v-model="form.role" /></div>
        <button type="submit" class="btn btn-primary">保存</button>
      </form>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserById, updateUser } from '@/api/user'

const route = useRoute()
const router = useRouter()
const id = computed(() => route.params.id)
const loading = ref(true)
const form = ref({ account: '', realname: '', password: '', email: '', dept: 0, role: '' })

onMounted(async () => {
  try {
    const res = await getUserById(id.value)
    const u = res.data || {}
    form.value = { account: u.account, realname: u.realname, password: '', email: u.email || '', dept: u.dept || 0, role: u.role || '' }
  } finally { loading.value = false }
})

async function onSubmit() {
  const payload = { ...form.value }
  if (!payload.password) delete payload.password
  await updateUser(id.value, payload)
  router.push('/user')
}
</script>
