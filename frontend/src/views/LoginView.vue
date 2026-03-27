<script setup lang="ts">
import type { AuthFormField, FormSubmitEvent } from "@nuxt/ui";
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useAuth } from "../composables/useAuth";

const router = useRouter();
const { login } = useAuth();

interface AuthFormState {
  username: string,
  password: string
}

const state = reactive({
  username: undefined,
  password: undefined
})

const handleLogin = async (form: FormSubmitEvent<AuthFormState>) => {
  try {
    await login(form.data.username, form.data.password);
    router.push("/dashboard");
  } catch (error) {
    console.error("Login failed:", error);
    alert("Неверное имя пользователя или пароль");
  }
};

const fields = ref<AuthFormField[]>([
  {
    name: "username",
    type: "text",
    label: "Имя пользователя",
  },
  {
    name: "password",
    type: "password",
    label: "Пароль",
  },
]);
</script>

<template>
  <UContainer class="flex items-center justify-center h-screen">
    <UCard class="min-w-md">
      <UAuthForm
        title="Вход в систему"
        :fields="fields"
        :submit="{ label: 'Вход', block: true }"
        @submit="handleLogin"
        ref="authForm"
      />
    </UCard>
  </UContainer>
</template>

<style scoped></style>
