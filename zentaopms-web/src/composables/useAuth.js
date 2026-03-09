import { ref, computed } from 'vue'

const token = ref(localStorage.getItem('zentaosid'))

export function useAuth() {
  const setToken = (t) => {
    token.value = t
    if (t) localStorage.setItem('zentaosid', t)
    else localStorage.removeItem('zentaosid')
  }
  const isLoggedIn = computed(() => !!token.value)
  return { token, setToken, isLoggedIn }
}
