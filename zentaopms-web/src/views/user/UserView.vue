<template>
  <div v-if="user">
    <div class="page-header" style="display:flex;align-items:center;gap:8px;flex-wrap:wrap;">
      <h1 style="margin:0;">{{ user.realname }}（{{ user.account }}）</h1>
      <router-link :to="`/user/edit/${id}`" class="btn">编辑</router-link>
      <button type="button" class="btn" @click="onUnlock" :disabled="actioning">解锁</button>
      <button type="button" class="btn" @click="showReset = true" :disabled="actioning">重置密码</button>
      <button type="button" class="btn" @click="onUnbind" :disabled="actioning">解绑</button>
      <button type="button" class="btn btn-danger" @click="onDelete" :disabled="actioning">删除</button>
    </div>
    <div v-if="showReset" class="modal-mask" @click.self="showReset = false">
      <div class="modal-box">
        <h3>重置密码</h3>
        <div class="form-group"><label>新密码</label><input v-model="resetPassword" type="password" /></div>
        <div class="form-actions">
          <button class="btn btn-primary" @click="onResetPassword" :disabled="!resetPassword || actioning">确定</button>
          <button class="btn" @click="showReset = false">取消</button>
        </div>
      </div>
    </div>
    <nav class="tabs">
      <router-link :to="`/user/${id}/todo`">待办</router-link>
      <router-link :to="`/user/${id}/task`">任务</router-link>
      <router-link :to="`/user/${id}/bug`">Bug</router-link>
      <router-link :to="`/user/${id}/story`">需求</router-link>
      <router-link :to="`/user/${id}/testtask`">测试单</router-link>
      <router-link :to="`/user/${id}/testcase`">测试用例</router-link>
      <router-link :to="`/user/${id}/execution`">执行</router-link>
      <router-link :to="`/user/${id}/dynamic`">动态</router-link>
      <router-link :to="`/user/${id}/profile`">档案</router-link>
    </nav>
    <router-view :user="user" />
  </div>
  <p v-else-if="!loading">用户不存在</p>
  <p v-else>加载中...</p>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getUserById, unlockUser, resetUserPassword, unbindUser, deleteUser } from '@/api/user'

const route = useRoute()
const router = useRouter()
const id = computed(() => route.params.id)
const user = ref(null)
const loading = ref(true)
const actioning = ref(false)
const showReset = ref(false)
const resetPassword = ref('')

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
  if (!user.value || !confirm('确定解绑该用户？')) return
  actioning.value = true
  try {
    await unbindUser(user.value.id)
    await fetchUser()
  } finally {
    actioning.value = false
  }
}

async function onDelete() {
  if (!user.value || !confirm('确定删除该用户？删除后不可恢复。')) return
  actioning.value = true
  try {
    await deleteUser(user.value.id)
    router.push('/user')
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
</style>
