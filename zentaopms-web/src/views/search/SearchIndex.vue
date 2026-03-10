<template>
  <div>
    <div class="page-header">
      <h1>{{ commonLang.search }}</h1>
    </div>
    <div class="table-wrap">
      <form @submit.prevent="onSearch" class="mb-2">
        <input v-model="keyword" type="text" :placeholder="commonLang.keywordPlaceholder" style="margin-right:8px;padding:6px;" />
        <select v-model="module">
          <option value="">{{ commonLang.all }}</option>
          <option value="task">{{ taskLang.common }}</option>
          <option value="bug">{{ bugLang.common }}</option>
          <option value="story">{{ storyLang.common }}</option>
          <option value="project">{{ projectLang.common }}</option>
          <option value="product">{{ productLang.common }}</option>
        </select>
        <button type="submit" class="btn btn-primary" style="margin-left:8px;">{{ commonLang.search }}</button>
      </form>
      <div v-if="result">
        <p v-if="!result.data || result.data.length === 0">{{ commonLang.noResult }}</p>
        <table v-else class="data-table">
          <thead>
            <tr><th>{{ userLang.type }}</th><th>ID</th><th>{{ commonLang.titleName }}</th><th>{{ commonLang.actions }}</th></tr>
          </thead>
          <tbody>
            <tr v-for="r in result.data" :key="r.type + r.id">
              <td>{{ r.type }}</td>
              <td>{{ r.id }}</td>
              <td>{{ r.title || r.name }}</td>
              <td>
                <router-link v-if="r.type === 'task'" :to="`/task/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
                <router-link v-else-if="r.type === 'bug'" :to="`/bug/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
                <router-link v-else-if="r.type === 'story'" :to="`/story/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
                <router-link v-else-if="r.type === 'project'" :to="`/project/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
                <router-link v-else-if="r.type === 'product'" :to="`/product/${r.id}`" class="btn">{{ commonLang.view }}</router-link>
                <span v-else>-</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { searchQuery } from '@/api/search'
import { common as commonLang, user as userLang, task as taskLang, bug as bugLang, story as storyLang, project as projectLang, product as productLang } from '@/lang/zh-cn'

const keyword = ref('')
const module = ref('')
const result = ref(null)

async function onSearch() {
  if (!keyword.value.trim()) return
  try {
    const res = await searchQuery({ words: keyword.value, module: module.value })
    result.value = res
  } catch (e) {
    result.value = { data: [] }
  }
}
</script>

<style scoped>
.mb-2 { margin-bottom: 0.5rem; }
</style>
