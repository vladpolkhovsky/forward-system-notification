<script setup lang="ts">
import { useAuth } from "@/composables/useAuth";
import useFetchWithAuth from "@/composables/useFetchWithAuth";
import type { UserShortDto } from "@/types/types";
import type { SelectItem } from "@nuxt/ui";
import { useToast } from "@nuxt/ui/composables/useToast";
import { computed, ref } from "vue";

const { token } = useAuth();
const toast = useToast();

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

const idSendDisabled = computed(
  () =>
    (state.value.userId == null && state.value.roles.length == 0) ||
    !state.value.tittle ||
    !state.value.description,
);

const resolveUsername = (userId: number) => {
  return (
    fetchUsers.data?.value
      ?.filter((t) => t.id == userId)
      ?.map((t) => t.username)?.[0] ?? "<Не определён или заблокирован>"
  );
};

interface SendFormState {
  roles: string[];
  userId: number | null;
  tittle: string;
  description: string;
}

const state = ref<SendFormState>({
  roles: [],
  userId: null,
  tittle: "",
  description: "",
});

const categorySelectItems = ref<SelectItem[]>([
  {
    value: "AUTHOR",
    label: "Авторы",
  },
  {
    value: "MANAGER",
    label: "Менеджеры",
  },
  {
    value: "ADMIN",
    label: "Админы",
  },
]);

const handleSubmit = async () => {
  toast.add({
    title: "Отправка",
    icon: "i-lucide-rocket",
  });
  fetch("/api/bot/notification/ui", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(state.value),
  })
    .then((resp) => {
      if (resp.ok) {
        toast.add({
          title: "Отправлено",
          icon: "i-lucide-mail-check",
        });
        state.value.roles = [];
        state.value.userId = null;
        state.value.tittle = "";
        state.value.description = "";
      } else {
        toast.add({
          title: "Ошибка",
          color: "error",
          icon: "i-lucide-refresh-cw-off",
        });
      }
    })
    .catch((err) => {
      toast.add({
        title: "Ошибка",
        color: "error",
        icon: "i-lucide-refresh-cw-off",
      });
    });
};
</script>

<template>
  <div class="flex items-center justify-center w-full min-h-[80vh]">
    <UForm
      class="border border-muted rounded-2xl p-8 w-full max-w-2xl"
      :state="state"
      @submit="handleSubmit"
    >
      <div class="flex flex-col gap-6">
        <span class="text-3xl font-semibold text-center mb-2"
          >Отправка уведомлений</span
        >

        <hr />

        <UFormField label="Тема сообщения" name="tittle" required>
          <UInput
            v-model="state.tittle"
            placeholder="Введите тему сообщения"
            size="lg"
            class="w-full"
          />
        </UFormField>

        <UFormField label="Текст сообщения" name="description" required>
          <UTextarea
            v-model="state.description"
            :rows="8"
            placeholder="Введите текст сообщения"
            size="lg"
            class="w-full"
          />
        </UFormField>

        <hr />

        <UFormField
          label="Необходимо выбрать пользователя или категорию пользователей"
          :required="state.roles.length == 0 && state.userId == null"
          :error="
            state.roles.length == 0 && state.userId == null
              ? 'Не выбран пользователь или категория'
              : ''
          "
          name="need"
        ></UFormField>

        <hr />

        <UFormField label="Категория пользователей" name="roles">
          <USelect
            class="w-full"
            :items="categorySelectItems"
            v-model="state.roles"
            value-key="value"
            placeholder="Выберите категорию"
            multiple
            size="lg"
            @change="state.userId = null"
          />
        </UFormField>
        <UFormField label="Пользователь для отправки" name="userId">
          <div class="flex items-center gap-3">
            <USelect
              class="w-full"
              v-model="state.userId"
              value-key="value"
              :items="userSelectItems"
              placeholder="Выберите пользователя"
              size="lg"
              @change="state.roles = []"
            />
            <UButton
              v-if="state.userId"
              icon="i-lucide-trash"
              size="lg"
              color="error"
              variant="soft"
              @click="state.userId = null"
            />
          </div>
        </UFormField>

        <UButton
          type="submit"
          class="w-full justify-center mt-4"
          icon="i-lucide-send"
          size="lg"
          :color="idSendDisabled ? 'warning' : 'primary'"
          :disabled="idSendDisabled"
        >
          Отправить
        </UButton>
      </div>
    </UForm>
  </div>
</template>
