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
  tittle: "Тема",
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
    body: JSON.stringify(state.value)
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
  <div class="w-full p-3">
    <UForm class="w-full" :state="state">
      <div class="justify-center items-center flex flex-col gap-5">
        <div class="flex justify-center">
          <div class="flex flex-col gap-5">
            <UFormField
              label="Категория пользователей"
              name="roles"
              class="min-w-96"
            >
              <USelect
                :items="categorySelectItems"
                v-model="state.roles"
                value-key="value"
                placeholder="Категория пользователей"
                multiple
                @change="state.userId = null"
              />
            </UFormField>

            <UFormField
              label="Пользователь для отправки"
              name="userId"
              class="min-w-96"
            >
              <div class="flex items-center gap-3">
                <USelectMenu
                  class="w-64"
                  v-model="state.userId"
                  value-key="value"
                  :items="userSelectItems"
                  placeholder="Выберите автора"
                  @change="state.roles = []"
                />

                <UButton
                  v-if="state.userId"
                  icon="i-lucide-trash"
                  size="sm"
                  color="error"
                  variant="solid"
                  @click="state.userId = null"
                />
              </div>
            </UFormField>
          </div>
          <div class="flex flex-col gap-5">
            <div class="w-128">
              <UFormField
                label="Тема сообщения"
                name="tittle"
                class="min-w-96"
                required
              >
                <UInput class="w-full" v-model="state.tittle" />
              </UFormField>
            </div>
            <div class="w-128">
              <UFormField
                label="Текст сообщения"
                name="discription"
                class="min-w-96"
                required
              >
                <UTextarea
                  class="w-full"
                  v-model="state.description"
                  :rows="10"
                />
              </UFormField>
            </div>

            <UButton
              class="w-full justify-center"
              icon="i-lucide-rocket"
              @click="handleSubmit"
              :disabled="
                (state.userId == null && state.roles.length == 0) ||
                state.tittle.length == 0 ||
                state.description.length == 0
              "
              >Отправить</UButton
            >
          </div>
        </div>
      </div>
    </UForm>
  </div>
</template>
