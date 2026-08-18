<script setup lang="ts">
import { computed, type InputTypeHTMLAttributes } from "vue";
import { cn } from "../../lib/utils";

export interface InputProps {
  type?: InputTypeHTMLAttributes;
  class?: string;
  placeholder?: string;
  ariaLabel?: string;
  modelValue?: string | number;
}

const props = withDefaults(defineProps<InputProps>(), {
  type: "text",
});
const emit = defineEmits<{
  "update:modelValue": [value: string];
}>();

const classes = computed(() =>
  cn(
    "flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-base shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:text-foreground placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50 md:text-sm",
    props.class,
  ),
);

function onInput(e: Event) {
  emit("update:modelValue", (e.target as HTMLInputElement).value);
}
</script>

<template>
  <input
    :type="type"
    :class="classes"
    :placeholder="placeholder"
    :aria-label="ariaLabel"
    :value="modelValue"
    @input="onInput"
  />
</template>
