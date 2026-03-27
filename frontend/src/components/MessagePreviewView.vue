<script setup lang="ts">
const props = defineProps<{ messageFull: string; messageShort: string }>();
</script>

<template>
  <UPopover
    mode="hover"
    :content="{
      align: 'center',
      side: 'right',
      sideOffset: 8,
    }"
  >
    <pre>{{ props.messageShort }}</pre>
    <template #content>
      <pre>{{ props.messageFull }}</pre>
    </template>
  </UPopover>
</template>

<style scoped>
pre {
  max-width: 650px;
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-wrap: break-word;
}
</style>
