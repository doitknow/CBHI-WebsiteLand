<script setup lang="ts">
import { Users, Globe, Building2, ShieldCheck, CheckCircle2 } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";
import { ref, onMounted, onUnmounted } from "vue";

const { t } = useI18n();

const impactMetrics = [
  { value: 45, suffix: "M+", translationKeyLabel: "impact.metrics.citizens.label", icon: Users, translationKeySubtext: "impact.metrics.citizens.subtext" },
  { value: 980, suffix: "+", translationKeyLabel: "impact.metrics.woredas.label", icon: Globe, translationKeySubtext: "impact.metrics.woredas.subtext" },
  { value: 3500, suffix: "+", translationKeyLabel: "impact.metrics.facilities.label", icon: Building2, translationKeySubtext: "impact.metrics.facilities.subtext" },
];

const counts = ref([0, 0, 0]);
const sectionRef = ref<HTMLElement | null>(null);
let observer: IntersectionObserver | null = null;
let started = false;

const animateValue = (index: number, start: number, end: number, duration: number) => {
  let startTimestamp: number | null = null;
  const step = (timestamp: number) => {
    if (!startTimestamp) startTimestamp = timestamp;
    const progress = Math.min((timestamp - startTimestamp) / duration, 1);
    // easeOutQuart
    const easeProgress = 1 - Math.pow(1 - progress, 4);
    counts.value[index] = Math.floor(easeProgress * (end - start) + start);
    if (progress < 1) {
      window.requestAnimationFrame(step);
    }
  };
  window.requestAnimationFrame(step);
};

onMounted(() => {
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && !started) {
      started = true;
      impactMetrics.forEach((metric, idx) => {
        setTimeout(() => {
           animateValue(idx, 0, metric.value, 2000);
        }, idx * 150);
      });
      if (observer && sectionRef.value) {
        observer.unobserve(sectionRef.value);
      }
    }
  }, { threshold: 0.2 });

  if (sectionRef.value) {
    observer.observe(sectionRef.value);
  }
});

onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
});
</script>

<template>
  <section
    ref="sectionRef"
    class="relative w-full overflow-hidden bg-white dark:bg-[#021E31] px-4 py-16 sm:px-8 lg:px-[101px]"
    aria-labelledby="national-impact-heading"
  >
    <!-- Heading pill -->
    <ScrollReveal direction="down" duration="0.7s" class="relative z-20">
      <div
        class="mx-auto flex h-24 w-full max-w-[686px] items-center justify-center rounded-2xl bg-white dark:bg-[#0B192C] px-6 shadow-md ring-1 ring-slate-100 dark:ring-white/10"
      >
        <h2
          id="national-impact-heading"
          class="text-center [font-family:'Inter',Helvetica] text-[34px] font-extrabold leading-none tracking-tight sm:text-[42px]"
        >
          <span class="text-[#161616] dark:text-white">{{ t('impact.heading_national') }}</span>
          <span class="text-[#161616] dark:text-white">{{ t('impact.heading_and') }}</span>
          <span class="text-[#0873b9] dark:text-[#3c8dfd]">{{ t('impact.heading_scale') }}</span>
        </h2>
      </div>
    </ScrollReveal>

    <!-- Blue gradient banner -->
    <ScrollReveal direction="up" :delay="100" duration="0.9s" class="relative z-10">
      <div
        class="mx-auto -mt-12 flex w-full max-w-[1100px] flex-col items-center rounded-[20px] bg-gradient-to-b from-[#0873b9] to-[#064e82] dark:from-[#083a63] dark:to-[#041d33] px-4 pt-20 pb-28 shadow-xl animate-gradient-shift"
        style="background-size: 200% 200%;"
      >
        <p
          class="max-w-[600px] text-center [font-family:'Inter',Helvetica] text-[14.5px] font-normal leading-relaxed text-[#e1e1e1]"
        >
          {{ t('impact.subtitle') }}
        </p>

        <!-- Separator line -->
        <div class="mt-6 flex w-full max-w-[700px] items-center justify-center gap-3">
          <div class="h-px w-full flex-1 bg-white/30" />
          <div class="flex gap-1">
            <div class="h-1 w-1 rounded-full bg-white/50" />
            <div class="h-1 w-1 rounded-full bg-white/50" />
            <div class="h-1 w-1 rounded-full bg-white/50" />
          </div>
          <div class="h-px w-full flex-1 bg-white/30" />
        </div>
      </div>
    </ScrollReveal>

    <!-- Metric cards overlapping bottom of banner (Stay white in dark mode) -->
    <div
      class="relative z-30 mx-auto -mt-20 grid w-full max-w-[800px] grid-cols-1 gap-6 sm:grid-cols-3"
    >
      <ScrollReveal
        v-for="(metric, idx) in impactMetrics"
        :key="idx"
        direction="up"
        :stagger-index="idx"
        :stagger-delay="120"
        :delay="300"
        duration="0.8s"
      >
        <Card
          class="card-hover-glow group flex h-[160px] flex-col items-center justify-center rounded-xl border border-slate-200/80 bg-white shadow-lg transition-transform hover:-translate-y-1"
        >
          <CardContent class="flex flex-col items-center p-0">
            <div
              class="mb-3 flex h-10 w-14 items-center justify-center rounded-xl bg-blue-50 text-[#0873b9] transition-all duration-300 group-hover:bg-[#0873b9] group-hover:text-white"
            >
              <component :is="metric.icon" class="h-6 w-6" />
            </div>

            <p
              class="text-center [font-family:'Inter',Helvetica] text-[36px] font-black leading-none text-[#0b4d8c]"
            >
              {{ counts[idx] }}{{ metric.suffix }}
            </p>

            <p
              class="mt-1.5 text-center [font-family:'Inter',Helvetica] text-[12px] font-bold tracking-widest text-[#64748b] uppercase"
            >
              {{ t(metric.translationKeyLabel) }}
            </p>
          </CardContent>
        </Card>
      </ScrollReveal>
    </div>

    <!-- Fayda + INSA verification badges with hover elevation -->
    <div
      class="mx-auto mt-12 flex w-full max-w-[840px] flex-col items-center justify-center gap-4 sm:flex-row sm:gap-0"
    >
      <!-- Fayda Integrated -->
      <ScrollReveal direction="left" :delay="400" duration="0.8s">
        <div
          class="card-hover-glow group relative flex h-[82px] w-full items-center rounded-2xl border border-slate-200/80 dark:border-[#103d63] bg-white dark:bg-[#041527] pl-[88px] pr-6 shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md sm:w-[390px] sm:rounded-l-[50px] sm:rounded-r-none"
        >
          <img
            class="absolute left-3 h-[60px] w-[60px] rounded-full object-cover transition-transform duration-300 group-hover:scale-105 sm:left-2"
            alt="Fayda National ID"
            src="/fayda-Photoroom.png"
            loading="lazy"
          />
          <div>
            <p
              class="[font-family:'Inter',Helvetica] text-[20px] font-bold tracking-tight text-[#1F576E] dark:text-[#2daae1]"
            >
              {{ t('impact.fayda.title') }}
            </p>
            <p
              class="mt-1 text-[13px] font-medium text-slate-500 dark:text-[#70889c]"
            >
              {{ t('impact.fayda.subtitle') }}
            </p>
          </div>
        </div>
      </ScrollReveal>

      <!-- Connector line with pulse -->
      <div class="hidden h-3 w-[45px] shrink-0 items-center justify-center sm:flex">
        <svg width="45" height="12" viewBox="0 0 45 12" fill="none">
          <line x1="0" y1="6" x2="45" y2="6" stroke="#3c8dfd" stroke-width="2" stroke-dasharray="4 3" class="animate-pulse" />
        </svg>
      </div>

      <!-- INSA Certified -->
      <ScrollReveal direction="right" :delay="500" duration="0.8s">
        <div
          class="card-hover-glow group relative flex h-[82px] w-full items-center justify-end rounded-2xl border border-slate-200/80 dark:border-[#1a2d42] bg-white dark:bg-[#041527] pl-6 pr-[88px] shadow-sm transition-all duration-300 hover:-translate-y-0.5 hover:shadow-md sm:w-[390px] sm:rounded-l-none sm:rounded-r-[50px]"
        >
          <div class="text-right">
            <p
              class="bg-[linear-gradient(79deg,rgba(236,32,35,1)_0%,rgba(61,97,173,1)_100%)] dark:bg-[linear-gradient(79deg,#ff4d4d_0%,#3c8dfd_100%)] bg-clip-text [font-family:'Inter',Helvetica] text-[20px] font-bold tracking-tight text-transparent"
              style="-webkit-text-fill-color: transparent"
            >
              {{ t('impact.insa.title') }}
            </p>
            <p
              class="mt-1 text-[13px] font-medium text-slate-500 dark:text-[#70889c]"
            >
              {{ t('impact.insa.subtitle') }}
            </p>
          </div>
          <img
            class="absolute right-3 h-[60px] w-[60px] rounded-full object-cover transition-transform duration-300 group-hover:scale-105 sm:right-2"
            alt="INSA Security Certification"
            src="/INSA.png"
            loading="lazy"
          />
        </div>
      </ScrollReveal>
    </div>
  </section>
</template>
