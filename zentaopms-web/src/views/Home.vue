<template>
  <div class="page-header">
    <h1>{{ myLang.index }}</h1>
    <p>{{ appLang.welcome }}</p>
    <p><router-link to="/user">{{ userLang.browse }}</router-link></p>
  </div>
</template>

<script setup>
import { app as appLang, my as myLang, user as userLang } from '@/lang/zh-cn'
</script>
