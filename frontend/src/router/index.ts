import { createRouter, createWebHistory } from "vue-router";
import { useAuth } from "../composables/useAuth";
import type { RouteRecordRaw } from "vue-router";

const routes: RouteRecordRaw[] = [
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/LoginView.vue"),
  },
  {
    path: "/",
    redirect: "/dashboard",
  },
  {
    path: "/dashboard",
    name: "dashboard",
    redirect: "/dashboard/notifications",
    component: () => import("@/views/DashboardView.vue"),
    children: [
      {
        path: "notifications",
        name: "dashboard-notifications",
        component: () => import("@/views/DashboardNotifications.vue"),
      },
      {
        path: "users",
        name: "dashboard-users",
        component: () => import("@/views/DashboardUsers.vue"),
      },
      {
        path: "send-message",
        name: "send-message",
        component: () => import("@/views/SendMessage.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
