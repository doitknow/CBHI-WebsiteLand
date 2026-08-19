<script setup lang="ts">
import { useI18n } from "vue-i18n";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";
import { UserCheck, Stethoscope, HeartPulse } from "lucide-vue-next";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";

const { t } = useI18n();

const coverageBenefits = [
  {
    translationKeyTitle: "coverage.benefits.primary.title",
    translationKeyDesc: "coverage.benefits.primary.description",
    imageSrc: "/primary in web.png",
    icon: UserCheck,
  },
  {
    translationKeyTitle: "coverage.benefits.hospitalization.title",
    translationKeyDesc: "coverage.benefits.hospitalization.description",
    imageSrc: "/hospitalization.png",
    icon: Stethoscope,
  },
  {
    translationKeyTitle: "coverage.benefits.maternal.title",
    translationKeyDesc: "coverage.benefits.maternal.description",
    imageSrc: "/metal helth.jpg",
    icon: HeartPulse,
  },
];
</script>

<template>
  <section
    aria-labelledby="coverage-benefits-heading"
    class="relative w-full overflow-hidden bg-slate-50/70 dark:bg-[#021E31] px-6 py-14 sm:px-8 sm:py-16 lg:px-10"
  >
    <ScrollReveal direction="up" duration="0.8s">
      <header class="mx-auto flex max-w-[800px] flex-col items-center text-center">
        <span class="section-badge">{{ t('coverage.badge') }}</span>

        <h2
          id="coverage-benefits-heading"
          class="mt-4 [font-family:'Inter',Helvetica] text-[38px] font-bold leading-[1.08] tracking-tight sm:text-[50px]"
        >
          <span class="text-[#161616] dark:text-white">{{ t('coverage.heading_coverage') }}</span>
          <span class="bg-gradient-to-r from-[#0873b9] to-[#0873b9] bg-clip-text text-transparent">{{ t('coverage.heading_goes_further') }}</span>
        </h2>

        <p
          class="mt-4 [font-family:'Inter',Helvetica] text-base font-normal leading-relaxed text-[#64748b] dark:text-slate-400 sm:text-lg"
        >
          {{ t('coverage.description') }}
        </p>
      </header>
    </ScrollReveal>

    <div class="relative mx-auto mt-10 max-w-[1240px]">
      <!-- Benefit cards with hover zoom and elevation -->
      <div class="grid grid-cols-1 gap-8 md:grid-cols-3 md:gap-6 lg:gap-8">
        <ScrollReveal
          v-for="(benefit, idx) in coverageBenefits"
          :key="benefit.translationKeyTitle"
          direction="up"
          :stagger-index="idx"
          :stagger-delay="140"
          :delay="100"
          duration="0.8s"
          class="w-full"
        >
          <Card
            class="card-hover-glow group relative mx-auto h-[480px] w-full max-w-[390px] overflow-hidden rounded-[20px] border border-white dark:border-white/10 bg-white dark:bg-[#0B192C] shadow-[0_8px_24px_rgba(8,115,185,0.1)] transition-all duration-500 hover:-translate-y-2 hover:shadow-[0_20px_40px_rgba(8,115,185,0.2)]"
          >
            <CardContent class="relative h-full w-full p-0">
              <!-- Background Image with smooth zoom on hover -->
              <div class="h-full w-full overflow-hidden">
                <img
                  class="h-full w-full object-cover transition-transform duration-700 ease-out group-hover:scale-105"
                  :alt="`${benefit.translationKeyTitle} coverage`"
                  :src="benefit.imageSrc"
                  loading="lazy"
                />
              </div>

              <!-- Gradient overlay for text readability -->
              <div class="pointer-events-none absolute inset-0 bg-gradient-to-b from-white/95 via-white/80 dark:from-[#0B192C]/95 dark:via-[#0B192C]/80 to-transparent pt-8 px-6" />

              <!-- Text & Icon content -->
              <div
                class="absolute inset-x-0 top-0 flex flex-col items-center px-6 pt-7 text-center"
              >
                <!-- Icon badge with lift -->
                <div class="flex h-14 w-14 items-center justify-center rounded-2xl bg-white/90 text-[#0873b9] shadow-md backdrop-blur-md transition-all duration-300 group-hover:scale-110 group-hover:bg-[#0873b9] group-hover:text-white group-hover:shadow-[0_0_20px_rgba(8,115,185,0.3)]">
                  <component :is="benefit.icon" class="h-7 w-7" />
                </div>

                <h3
                  class="mt-4 [font-family:'Inter',Helvetica] text-[22px] font-bold leading-tight tracking-tight text-[#161616] dark:text-white transition-colors duration-300 group-hover:text-[#0873b9]"
                >
                  {{ t(benefit.translationKeyTitle) }}
                </h3>

                <p
                  class="mt-2.5 max-w-[280px] [font-family:'Inter',Helvetica] text-[14px] font-medium leading-relaxed text-[#64748b] dark:text-slate-300"
                >
                  {{ t(benefit.translationKeyDesc) }}
                </p>
              </div>
            </CardContent>
          </Card>
        </ScrollReveal>
      </div>
    </div>
  </section>
</template>
