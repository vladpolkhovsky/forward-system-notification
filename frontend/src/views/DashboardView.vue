<script setup lang="ts">
import type { NavigationMenuItem } from "@nuxt/ui";
import { useAuth } from "../composables/useAuth";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const { validateToken, logout } = useAuth();

validateToken().catch(e => {
  console.log(e);
  router.push("/login");
})

const navigation: NavigationMenuItem[] = [
  {
    label: "Уведомления",
    to: "/dashboard/notifications",
    class: "h-12",
    icon: "i-lucide-bell",
  },
  {
    label: "Пользователи",
    to: "/dashboard/users",
    class: "h-12",
    icon: "i-lucide-users",
  },
  {
    label: "Отправить сообщение",
    to: "/dashboard/send-message",
    class: "h-12",
    icon: "i-lucide-send",
  },
];

const handleLogout = () => {
  logout();
  router.push("/login");
};
</script>

<template>
  <UDashboardGroup>
    <UDashboardSidebar>
      <template #header>
        <div class="flex justify-start items-center w-full">
          <UIcon name="i-lucide-bot" class="size-12" />
          <p class="ms-3 font-medium text-xl">Управление уведомлениями</p>
        </div>
      </template>

      <template #default>
        <UNavigationMenu :items="navigation" orientation="vertical" />
      </template>

      <template #footer="{ collapsed }">
        <UButton
          :icon="collapsed ? 'i-lucide-log-out' : undefined"
          :label="collapsed ? undefined : 'Выйти'"
          color="error"
          variant="outline"
          @click="handleLogout"
          block
        />
      </template>
    </UDashboardSidebar>

    <router-view />

  </UDashboardGroup>
</template>

<style scoped></style>
