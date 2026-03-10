<template>
  <div class="table-wrap" v-if="!loading">
    <div class="form-group" style="display:flex;gap:12px;flex-wrap:wrap;">
      <label>{{ userLang.type }}</label>
      <select v-model="type" @change="load(1)" style="width:auto;">
        <option value="assignedTo">{{ userLang.assignedTo }}</option>
        <option value="openedBy">{{ userLang.openedBy }}</option>
        <option value="closedBy">{{ userLang.closedBy }}</option>
        <option value="reviewedBy">{{ userLang.reviewedBy }}</option>
      </select>
      <label>{{ userLang.storyType }}</label>
      <select v-model="storyType" @change="load(1)" style="width:auto;">
        <option value="story">{{ userLang.storyTypeStory }}</option>
        <option value="requirement">{{ userLang.storyTypeRequirement }}</option>
        <option value="epic">{{ userLang.storyTypeEpic }}</option>
      </select>
      <label>{{ userLang.sort }}</label>
      <select v-model="orderBy" @change="load(1)" style="width:auto;">
        <option value="id_desc">{{ userLang.idDesc }}</option>
        <option value="id_asc">{{ userLang.idAsc }}</option>
      </select>
    </div>
    <table class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>{{ storyLang.title }}</th>
          <th>{{ storyLang.status }}</th>
          <th>{{ storyLang.pri }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="s in list" :key="s.id">
          <td>{{ s.id }}</td>
          <td>{{ s.title }}</td>
          <td>{{ s.status }}</td>
          <td>{{ s.pri }}</td>
        </tr>
      </tbody>
    </table>
    <div class="pager" v-if="pager">
      <span>{{ commonLang.totalCount.replace('{total}', pager.recTotal) }}</span>
      <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">{{ commonLang.prevPage }}</button>
      <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">{{ commonLang.nextPage }}</button>
    </div>
  </div>
  <p v-else>{{ commonLang.loading }}</p>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { getUserStory } from '@/api/user'
import { common as commonLang, story as storyLang, user as userLang } from '@/lang/zh-cn'

const props = defineProps({ user: Object })
const list = ref([])
const loading = ref(true)
const type = ref('assignedTo')
const storyType = ref('story')
const orderBy = ref('id_desc')
const pageID = ref(1)
const recPerPage = 20
const pager = ref(null)

async function load(page = 1) {
  if (!props.user?.id) return
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserStory(props.user.id, { storyType: storyType.value, type: type.value, orderBy: orderBy.value, pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || null
  } finally {
    loading.value = false
  }
}

watch(() => props.user?.id, load)
onMounted(load)
</script>

<style scoped>
.muted { color: #999; font-size: 13px; margin-top: 8px; }
</style>
