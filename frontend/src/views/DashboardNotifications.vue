<script setup lang="ts">
import MessagePreviewView from "@/components/MessagePreviewView.vue";
import useFetchWithAuth from "@/composables/useFetchWithAuth";
import type {
  NotificationHistoryDto,
  PageNotificationHistoryDto,
  TagStatDto,
  UserShortDto,
} from "@/types/types";
import dayjs from "dayjs";
import type { SelectItem, TableColumn } from "@nuxt/ui";
import { useRouteQuery } from "@vueuse/router";
import { computed, h, onMounted, ref, useTemplateRef, watch } from "vue";
import Chart from "chart.js/auto";

const canvasRef = useTemplateRef<HTMLCanvasElement>("canvasRef");

const userId = useRouteQuery<string | null>("userId");
const page = useRouteQuery<number>("page", 1, { transform: Number });

const params = computed(() => {
  const search = new URLSearchParams();
  if (userId.value != null) search.append("userId", userId.value as string);
  if (page.value != null) search.append("page", (page.value - 1).toString());
  return search;
});

const url = computed(() => `/api/bot/notification?${params.value.toString()}`);
const urlStat = computed(
  () => `/api/bot/notification/stat?${params.value.toString()}`,
);

const fetchNotifications = useFetchWithAuth(url, {
  refetch: true,
}).json<PageNotificationHistoryDto>();

const fetchNotificationsStat = useFetchWithAuth(urlStat, {
  refetch: true,
}).json<TagStatDto[]>();

const fetchUsers = useFetchWithAuth("/api/bot/users/short").json<
  UserShortDto[]
>();

watch(userId, (newValue) => {
  page.value = 1;
});

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
        messageShort: row.original.messageShort || "",
      });
    },
  },
  {
    id: "createdAt",
    header: "Отправлено",
    accessorKey: "createdAt",
    cell: ({ row }) =>
      dayjs(new Date(row.getValue("createdAt"))).format("DD.MM.YYYY hh:mm"),
  },
];

let chart: Chart | null = null;

watch([canvasRef, fetchNotificationsStat.data], () => {
  if (chart == null && canvasRef.value) {
    console.log("chart created");
    chart = new Chart(canvasRef.value as HTMLCanvasElement, {
      type: "bar",
      options: {
        scales: {
          y: {
            type: "logarithmic",
          },
        },
      },
      data: {
        datasets: [
          {
            data: [],
            label: "Кол-во сообщений по тегу",
          },
        ],
        labels: [],
      },
    });
  }
  if (chart != null && fetchNotificationsStat.data.value) {
    console.log("chart updated");
    chart.config.data.datasets[0]!.data = fetchNotificationsStat.data.value.map(
      (t) => t.tagCount,
    );
    chart.config.data.labels = fetchNotificationsStat.data.value.map(
      (t) => t.tag,
    );
    chart.update();
    if (fetchNotificationsStat.data.value.length == 0) {
      chart.destroy();
      chart = null;
    }
  }
});
</script>

<template>
  <div class="flex flex-col w-full p-3">
    <div class="flex w-full justify-between">
      <div class="flex gap-3 items-center">
        <USelectMenu
          class="w-64"
          v-model="userId"
          value-key="value"
          :items="userSelectItems"
          placeholder="Выберите автора"
        />

        <UButton
          v-if="userId"
          icon="i-lucide-trash"
          size="sm"
          color="error"
          variant="solid"
          @click="userId = null"
        />
      </div>

      <UPagination
        v-model:page="page"
        show-edges
        :sibling-count="3"
        :items-per-page="fetchNotifications.data?.value?.pageable?.pageSize"
        :total="fetchNotifications.data?.value?.totalElements"
      />
    </div>

    <UTable
      class="border border-muted mt-2 grow"
      v-bind:loading="fetchNotifications.isFetching.value"
      :sticky="true"
      :columns="columns"
      :data="fetchNotifications.data.value?.content ?? []"
      :empty="fetchNotifications.isFetching.value ? 'Загрузка' : 'Нет данных'"
    />

    <div class="flex w-full justify-between p-2 border border-muted mt-2">
      <canvas
        ref="canvasRef"
        id="statCanvas"
        class="max-h-64"
        aria-label="Статистика сообщений"
        role="img"
      ></canvas>
    </div>
  </div>
</template>

<style scoped></style>
