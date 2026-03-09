<template>
  <div>
    <div class="page-header">
      <h1>新建看板</h1>
      <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">返回</router-link>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSubmit">
        <div class="form-group">
          <label>空间 *</label>
          <select v-model.number="form.spaceID" required>
            <option :value="0">请选择</option>
            <option v-for="s in spaces" :key="s.id" :value="s.id">{{ s.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>名称 *</label>
          <input v-model="form.name" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">保存</button>
          <router-link :to="spaceId ? `/kanban/space/${spaceId}` : '/kanban'" class="btn">取消</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { createKanban, getSpaceList } from '@/api/kanban'

const route = useRoute()
const router = useRouter()
const spaceId = computed(() => Number(route.query.spaceID) || 0)
const form = ref({ spaceID: 0, name: '' })
const spaces = ref([])
const submitting = ref(false)

async function onSubmit() {
  if (!form.value.spaceID) return
  submitting.value = true
  try {
    const res = await createKanban({ space: form.value.spaceID, name: form.value.name })
    router.push(`/kanban/board/${res.id}`)
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  const r = await getSpaceList()
  spaces.value = r.data || []
  if (spaceId.value) form.value.spaceID = spaceId.value
})
</script>
