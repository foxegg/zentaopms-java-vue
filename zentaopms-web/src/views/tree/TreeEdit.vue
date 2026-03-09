<template>
  <div>
    <div class="page-header">
      <h1>编辑树节点</h1>
      <router-link to="/tree" class="btn">返回浏览</router-link>
    </div>
    <div class="table-wrap" v-if="form">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link to="/tree" class="btn">取消</router-link>
        </div>
      </form>
    </div>
    <p v-else-if="loading">加载中...</p>
    <p v-else>节点不存在</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getTreeNode, updateTree } from '@/api/tree'

const route = useRoute()
const router = useRouter()
const nodeId = computed(() => Number(route.params.id))
const form = ref(null)
const loading = ref(true)
const submitting = ref(false)

onMounted(async () => {
  const id = nodeId.value
  loading.value = true
  try {
    const res = await getTreeNode(id)
    const n = res.data
    if (n) form.value = { name: n.name }
  } finally {
    loading.value = false
  }
})

async function onSubmit() {
  if (!form.value) return
  submitting.value = true
  try {
    await updateTree(nodeId.value, form.value)
    router.push('/tree')
  } finally {
    submitting.value = false
  }
}
</script>
