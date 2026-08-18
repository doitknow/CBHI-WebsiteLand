<script setup lang="ts">
import { Scale, HeartHandshake } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";

const { t } = useI18n();

const healthPrinciples = [
  {
    translationKeyTitle: "health_rights.principles.equitable.title",
    translationKeyDesc: "health_rights.principles.equitable.description",
    color: "#3c8dfd",
    icon: Scale,
  },
  {
    translationKeyTitle: "health_rights.principles.solidarity.title",
    translationKeyDesc: "health_rights.principles.solidarity.description",
    color: "#3c8dfd",
    icon: HeartHandshake,
  },
];
</script>

<template>
  <section
    aria-labelledby="health-rights-heading"
    class="relative w-full overflow-hidden bg-white dark:bg-[#021E31] py-14 sm:py-16 [font-family:'Inter',Helvetica]"
  >
    <div
      class="mx-auto grid max-w-[1240px] grid-cols-1 items-start gap-12 px-6 sm:px-8 lg:grid-cols-[minmax(0,618px)_minmax(0,1fr)] lg:gap-16 lg:px-10 xl:px-6"
    >
      <!-- Left text content -->
      <div class="relative z-10 flex flex-col items-start">
        <!-- "Health is Everyone's Right" styled heading -->
        <ScrollReveal direction="left" duration="0.9s">
          <div class="flex flex-col">
            <h2
              id="health-rights-heading"
              class="text-[48px] font-black leading-[1.1] tracking-tight text-[#161616] dark:text-white sm:text-[58px]"
            >
              <span class="block">{{ t('health_rights.heading_health') }}</span>
              <span class="block">{{ t('health_rights.heading_is_everyone_right') }}<span class="text-[#0873b9]">{{ t('health_rights.heading_right') }}</span></span>
            </h2>
          </div>
        </ScrollReveal>
        <ScrollReveal direction="up" :delay="150" duration="0.8s">
          <p
            class="mt-5 max-w-[618px] text-base font-light leading-normal tracking-[0] text-[#808080] dark:text-slate-400"
          >
            {{ t('health_rights.description') }}
          </p>
        </ScrollReveal>
        <div class="mt-10 sm:mt-12 grid w-full grid-cols-1 gap-8 sm:grid-cols-2 sm:gap-12">
          <ScrollReveal
            v-for="(principle, i) in healthPrinciples"
            :key="i"
            direction="up"
            :stagger-index="i"
            :stagger-delay="120"
            :delay="200"
            duration="0.8s"
          >
            <Card
              class="border-0 bg-transparent p-0 shadow-none"
            >
              <CardContent class="p-0">
                <div class="relative mb-[9px] flex h-[49px] items-center">
                  <span
                    aria-hidden="true"
                    class="absolute left-[17px] top-[12px] h-[38px] w-[38px] rounded-full bg-[#3c8dfd66] dark:bg-[#3c8dfd33] animate-breathe"
                  />
                  <div class="relative z-10 flex h-[39px] w-9 items-center justify-center text-[#0873b9] dark:text-[#3c8dfd]">
                    <component :is="principle.icon" class="h-7 w-7" stroke-width="2.5" />
                  </div>
                </div>
                <h3 class="text-xl font-bold leading-normal tracking-[0] text-[#161616] dark:text-white">
                  {{ t(principle.translationKeyTitle) }}
                </h3>
                <p class="mt-3.5 text-[15px] font-normal leading-normal tracking-[0] text-[#808080] dark:text-slate-400">
                  {{ t(principle.translationKeyDesc) }}
                </p>
              </CardContent>
            </Card>
          </ScrollReveal>
        </div>
      </div>

      <!-- Right map/network image -->
      <ScrollReveal direction="right" duration="1s" :delay="200">
        <div class="relative min-h-[360px] overflow-visible lg:min-h-[496px]">
          <div
            aria-hidden="true"
            class="absolute left-[65px] top-[30px] h-[448px] w-[448px] rotate-[-0.39deg] rounded-full bg-[#d4e4fd61] blur-[49.4px] animate-breathe"
          />
          <img
            class="absolute left-[-95px] top-[-65px] h-[586px] w-[705px] max-w-none object-cover opacity-12 blur-sm"
            alt="Network nodes"
            src="/node-Photoroom.png"
          />
          <img
            class="absolute left-[-5px] top-[-2px] h-[447px] w-[553px] max-w-none object-cover"
            alt="Map of Ethiopia"
            src="/map-Photoroom.png"
          />
        </div>
      </ScrollReveal>
    </div>

    <!-- Bottom decorative circle -->
    <svg
      class="pointer-events-none absolute bottom-[-80px] left-[18px] h-[120px] w-[120px] opacity-50 animate-rotate-slow"
      viewBox="0 0 173 173"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <circle cx="86.5" cy="86.5" r="85" stroke="#3c8dfd" stroke-opacity="0.15" stroke-width="3"/>
    </svg>
  </section>
</template>
