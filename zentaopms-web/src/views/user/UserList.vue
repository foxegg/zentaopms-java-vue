<template>
  <div>
    <div class="page-header" style="display:flex;align-items:center;gap:12px;">
      <h1 style="margin:0;">用户列表</h1>
      <router-link to="/user/create" class="btn btn-primary">新建用户</router-link>
      <router-link to="/user/batchCreate" class="btn">批量创建</router-link>
      <router-link to="/user/batchEdit" class="btn">批量编辑</router-link>
    </div>
    <div class="table-wrap" v-if="!loading">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>账号</th>
            <th>姓名</th>
            <th>部门</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="u in list" :key="u.id">
            <td>{{ u.id }}</td>
            <td>{{ u.account }}</td>
            <td>{{ u.realname }}</td>
            <td>{{ u.dept }}</td>
            <td>
              <router-link :to="`/user/${u.id}`" class="btn">查看</router-link>
              <router-link :to="`/user/edit/${u.id}`" class="btn">编辑</router-link>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pager" v-if="pager">
        <span>共 {{ pager.recTotal }} 条</span>
        <button class="btn" :disabled="pageID <= 1" @click="load(pageID - 1)">上一页</button>
        <span>第 {{ pageID }} 页</span>
        <button class="btn" :disabled="(pageID * recPerPage) >= pager.recTotal" @click="load(pageID + 1)">下一页</button>
      </div>
    </div>
    <p v-else>加载中...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserList } from '@/api/user'

const list = ref([])
const pager = ref(null)
const loading = ref(true)
const pageID = ref(1)
const recPerPage = 20

async function load(page = 1) {
  pageID.value = page
  loading.value = true
  try {
    const res = await getUserList({ pageID: page, recPerPage })
    list.value = res.data || []
    pager.value = res.pager || {}
  } finally {
    loading.value = false
  }
}

onMounted(() => load())
</script>
