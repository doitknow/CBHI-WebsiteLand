<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue';

const target = ref<HTMLElement | null>(null);
const isVisible = ref(false);
const props = defineProps({
  delay: { type: Number, default: 0 },
  direction: { type: String, default: 'up' }, // 'up', 'down', 'left', 'right'
  duration: { type: String, default: '0.8s' },
  staggerIndex: { type: Number, default: 0 }, // multiplied by staggerDelay for sequencing
  staggerDelay: { type: Number, default: 80 }, // ms between each staggered child
  threshold: { type: Number, default: 0.1 },
  rootMargin: { type: String, default: '0px 0px -50px 0px' },
  distance: { type: Number, default: 40 }, // px of translate
  once: { type: Boolean, default: true }, // only animate once
});

let observer: IntersectionObserver | null = null;

const totalDelay = computed(() => props.delay + (props.staggerIndex * props.staggerDelay));

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting) {
      setTimeout(() => {
        isVisible.value = true;
      }, totalDelay.value);
      if (props.once && observer && target.value) {
        observer.unobserve(target.value);
      }
    }
  }, { threshold: props.threshold, rootMargin: props.rootMargin });

  if (target.value) {
    observer.observe(target.value);
  }
});

onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
});

const getTransform = () => {
  if (isVisible.value) return 'translate(0,0) scale(1)';
  const d = props.distance;
  switch (props.direction) {
    case 'up': return `translateY(${d}px) scale(0.98)`;
    case 'down': return `translateY(-${d}px) scale(0.98)`;
    case 'left': return `translateX(-${d}px) scale(0.98)`;
    case 'right': return `translateX(${d}px) scale(0.98)`;
    default: return `translateY(${d}px) scale(0.98)`;
  }
};
</script>

<template>
  <div
    ref="target"
    :style="{
      opacity: isVisible ? 1 : 0,
      transform: getTransform(),
      transition: `all ${duration} cubic-bezier(0.16, 1, 0.3, 1)`,
      willChange: 'transform, opacity',
    }"
  >
    <slot />
  </div>
</template>
