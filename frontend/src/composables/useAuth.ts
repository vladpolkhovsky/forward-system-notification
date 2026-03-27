import { ref } from 'vue'

const TOKEN_KEY = 'token'

export function useAuth() {
  const token = localStorage.getItem(TOKEN_KEY);

  const login = async (loginValue: string, password: string) => {
    const response = await fetch(`/api/bot/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ login: loginValue, password }),
    });

    if (!response.ok) {
      throw new Error('Login failed')
    }

    const data = await response.json()
    const authToken = data.token || data.access_token || data

    localStorage.setItem(TOKEN_KEY, authToken)
  }

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY)
  }

  const validateToken = async () => {
    const response = await fetch(`/api/bot/auth/iam`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
    if (!response.ok) {
      throw new Error("not ok /iam");
    }
    return await response.json();
  }

  return {
    token,
    validateToken,
    login,
    logout
  }
}
