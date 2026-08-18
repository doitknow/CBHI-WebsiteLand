<script setup lang="ts">
import { computed } from "vue";
import { cn } from "../../lib/utils";

export interface CardProps {
  class?: string;
}

const props = defineProps<CardProps>();
const classes = computed(() =>
  cn("rounded-xl border bg-card text-card-foreground shadow", props.class),
);
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
