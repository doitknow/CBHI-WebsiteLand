<script lang="ts">
import { ref, onMounted, onUnmounted, computed } from "vue";
import { Globe, ChevronDown, Check } from "lucide-vue-next";
import { cn } from "../../lib/utils";

export interface SelectOption {
  value: string;
  label: string;
}

export interface SelectProps {
  modelValue?: string;
  options?: SelectOption[];
  placeholder?: string;
  class?: string;
  ariaLabel?: string;
}
</script>

<script setup lang="ts">
const props = withDefaults(defineProps<SelectProps>(), {
  modelValue: "EN",
  options: () => [
    { value: "EN", label: "EN" },
    { value: "AM", label: "AM" },
    { value: "OR", label: "OR" },
    { value: "TI", label: "TI" },
  ],
  placeholder: "Select",
});

const emit = defineEmits<{
  "update:modelValue": [value: string];
}>();

const isOpen = ref(false);
const selectRef = ref<HTMLDivElement | null>(null);

const currentLabel = computed(() => {
  const match = props.options.find((opt) => opt.value === props.modelValue);
  return match ? match.label : props.modelValue || props.placeholder;
});

function toggleDropdown() {
  isOpen.value = !isOpen.value;
}

function selectOption(val: string) {
  emit("update:modelValue", val);
  isOpen.value = false;
}

function handleClickOutside(e: MouseEvent) {
  if (selectRef.value && !selectRef.value.contains(e.target as Node)) {
    isOpen.value = false;
  }
}

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<template>
  <div ref="selectRef" class="relative inline-block text-left">
    <button
      type="button"
      :aria-label="ariaLabel || 'Select'"
      :aria-expanded="isOpen"
      :class="
        cn(
          'flex h-9 items-center justify-between gap-1.5 rounded border border-[#d1d1d1] bg-white px-2.5 text-sm font-medium text-[#787878] transition-colors focus:outline-none focus:ring-1 focus:ring-[#0873b9]',
          props.class,
        )
      "
      @click="toggleDropdown"
    >
      <slot name="prefix">
        <Globe class="h-4 w-4 shrink-0 text-slate-500" />
      </slot>
      <span class="truncate">{{ currentLabel }}</span>
      <ChevronDown
        class="h-3.5 w-3.5 opacity-60 transition-transform duration-200"
        :class="{ 'rotate-180': isOpen }"
      />
    </button>

    <div
      v-if="isOpen"
      class="absolute right-0 z-50 mt-1 max-h-60 w-full min-w-[5.5rem] overflow-auto rounded-md border border-neutral-200 bg-white p-1 text-sm shadow-md"
    >
      <div
        v-for="option in options"
        :key="option.value"
        :class="
          cn(
            'flex cursor-pointer select-none items-center justify-between rounded-sm px-2 py-1.5 font-medium transition-colors hover:bg-slate-100 hover:text-[#0873b9]',
            modelValue === option.value
              ? 'bg-slate-50 font-bold text-[#0873b9]'
              : 'text-[#555]',
          )
        "
        @click="selectOption(option.value)"
      >
        <span>{{ option.label }}</span>
        <Check
          v-if="modelValue === option.value"
          class="h-3.5 w-3.5 text-[#0873b9]"
        />
      </div>
    </div>
  </div>
</template>
