<script setup lang="ts">
import MessagePreviewView from "@/components/MessagePreviewView.vue";
import useFetchWithAuth from "@/composables/useFetchWithAuth";
import type {
  NotificationHistoryDto,
  PageNotificationHistoryDto,
  UserShortDto,
} from "@/types/types";
import dayjs from 'dayjs'
import type { SelectItem, TableColumn } from "@nuxt/ui";
import { useRouteQuery } from "@vueuse/router";
import { computed, h, watch } from "vue";

const userId = useRouteQuery<string>("userId");
const page = useRouteQuery<number>("page", 1, { transform: Number });

const params = computed(() => {
  const search = new URLSearchParams();
  if (userId.value) search.append("userId", userId.value as string);
  if (page.value) search.append("page", page.value.toString());
  return search;
});

const url = computed(() => `/api/bot/notification?${params.value.toString()}`);

const fetchNotifications = useFetchWithAuth(url, {
  refetch: true,
}).json<PageNotificationHistoryDto>();

const fetchUsers = useFetchWithAuth("/api/bot/users/short").json<
  UserShortDto[]
>();

const userSelectItems = computed<SelectItem[]>(() => {
  return (
    fetchUsers?.data?.value?.map((t) => {
      return {
        label: t.username,
        value: t.id?.toString(),
      };
    }) ?? []
  );
});

const resolveUsername = (userId: number) => {
  return (
    fetchUsers.data?.value
      ?.filter((t) => t.id == userId)
      ?.map((t) => t.username)?.[0] ?? "<Не определён или заблокирован>"
  );
};

const columns: TableColumn<NotificationHistoryDto>[] = [
  {
    id: "targetUserId",
    header: "Пользователь",
    accessorKey: "targetUserId",
    cell: ({ row }) => resolveUsername(row.getValue("targetUserId")),
  },
  { id: "telegram", header: "Мессенджер", accessorKey: "messenger" },
  {
    id: "message",
    header: "Сообщение",
    cell: ({ row }) => {
      return h(MessagePreviewView, { 
        messageFull: row.original.messageFull || "", 
        messageShort: row.original.messageShort || ""
      });
    },
  },
  {
    id: "createdAt",
    header: "Отправлено",
    accessorKey: "createdAt",
    cell: ({ row }) => dayjs(new Date(row.getValue('createdAt'))).format('DD.MM.YYYY hh:mm')
  },
];
</script>

<template>
  <div class="flex flex-col w-full p-3">
    <div class="flex w-full justify-between">
      <USelect
        class="w-64"
        v-model="userId"
        value-key="value"
        :items="userSelectItems"
        placeholder="Выберите автора"
      />
      <UPagination
        v-model:page="page"
        show-edges
        :sibling-count="1"
        :total="fetchNotifications.data.value?.totalPages"
      />
    </div>
    <UTable
      class="border border-muted mt-2"
      v-bind:loading="fetchNotifications.isFetching.value"
      :sticky="true"
      :columns="columns"
      :data="fetchNotifications.data.value?.content ?? []"
      :empty="fetchNotifications.isFetching.value ? 'Загрузка' : 'Нет данных'"
    />
  </div>
</template>

<style scoped></style>
