<script setup lang="ts">
import { computed } from "vue";
import { cn } from "../../lib/utils";

export type BadgeVariant = "default" | "secondary" | "destructive" | "outline";

const badgeVariants: Record<BadgeVariant, string> = {
  default: "border-transparent bg-primary text-primary-foreground shadow hover:bg-primary/80",
  secondary: "border-transparent bg-secondary text-secondary-foreground hover:bg-secondary/80",
  destructive: "border-transparent bg-destructive text-destructive-foreground shadow hover:bg-destructive/80",
  outline: "text-foreground",
};

export interface BadgeProps {
  variant?: BadgeVariant;
  class?: string;
}

const props = withDefaults(defineProps<BadgeProps>(), {
  variant: "default",
});

const classes = computed(() =>
  cn(
    "inline-flex items-center rounded-md border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
    badgeVariants[props.variant],
    props.class,
  ),
);
</script>

<template>
  <div :class="classes">
    <slot />
  </div>
</template>
