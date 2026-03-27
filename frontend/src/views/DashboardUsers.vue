<script setup lang="ts">
import useFetchWithAuth from "@/composables/useFetchWithAuth";
import type {
  NotificationStatusDto,
  UserWithStatusDto,
  UserWithStatusPageDto,
} from "@/types/types";

import type { TableColumn } from "@nuxt/ui";
import { computed, h, ref, resolveComponent } from "vue";

const showDeletedAndBlocked = ref(false);
const { data, isFetching } = useFetchWithAuth("/api/bot/users/status").json();

const page = computed<UserWithStatusPageDto>(() => {
  if (showDeletedAndBlocked.value) {
    return data?.value as UserWithStatusPageDto;
  }
  if (!data.value) {
    return data?.value;
  }
  const value = data.value as UserWithStatusPageDto;
  return {
    ...value,
    content: value.content?.filter((u) => {
      const isBannded = u.user?.authorities?.includes("BANNED");
      const isDeleted = u.user?.authorities?.includes("DELETED");
      return !(isBannded || isDeleted);
    }),
  };
});

const UBadge = resolveComponent("UBadge");

const getStatusTag = (status: NotificationStatusDto) => {
  let statsTags = [];
  if (status.vk) {
    statsTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "solid", color: "primary" },
        () => "VK",
      ),
    );
  }
  if (status.tg) {
    statsTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "solid", color: "primary" },
        () => "TG",
      ),
    );
  }
  if (status.max) {
    statsTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "solid", color: "primary" },
        () => "MAX",
      ),
    );
  }
  return statsTags;
};

const getRoleTag = (roles: string[]) => {
  let rolesTags = [];
  if (roles.includes("BANNED")) {
    rolesTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "outline", color: "error" },
        () => "Заблокирован",
      ),
    );
  }
  if (roles.includes("ADMIN")) {
    rolesTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "outline", color: "warning" },
        () => "Администратор",
      ),
    );
  }
  if (roles.includes("MANAGER")) {
    rolesTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "outline", color: "secondary" },
        () => "Менеджер",
      ),
    );
  }
  if (roles.includes("AUTHOR")) {
    rolesTags.push(
      h(
        UBadge,
        { class: "me-1", variant: "outline", color: "success" },
        () => "Автор",
      ),
    );
  }
  return rolesTags;
};

const columns: TableColumn<UserWithStatusDto>[] = [
  { id: "id", header: "ID", accessorKey: "id" },
  {
    id: "username",
    header: "Имя пользователя",
    accessorKey: "user.username",
    cell: ({ row }) =>
      h("td", { class: "text-[18px] font-medium" }, row.getValue("username")),
  },
  {
    id: "status",
    accessorKey: "status",
    header: "Статус подключения",
    cell: ({ row }) => getStatusTag(row.getValue("status")),
  },
  {
    id: "authorities",
    accessorKey: "user.authorities",
    header: "Роль",
    cell: ({ row }) => getRoleTag(row.getValue("authorities")),
  },
];
</script>

<template>
  <div class="flex flex-col w-full p-3">
    <div class="flex justify-between items-center" v-if="!isFetching">
      <UCheckbox
        v-model="showDeletedAndBlocked"
        variant="card"
        label="Показывать удалённых и заблокированных"
        :ui="{
          root: 'flex justify-between items-center',
        }"
      />
      <div class="w-full text-end text-2xl">
        Всего пользователей:
        <span class="text-3xl font-medium">{{ page?.totalElements }}</span>
      </div>
    </div>
    <UTable
      class="border border-muted mt-2"
      v-bind:loading="isFetching"
      :sticky="true"
      :columns="columns"
      :data="page?.content"
      :empty="isFetching ? 'Загрузка' : 'Нет данных'"
    />
  </div>
</template>

<style scoped></style>
