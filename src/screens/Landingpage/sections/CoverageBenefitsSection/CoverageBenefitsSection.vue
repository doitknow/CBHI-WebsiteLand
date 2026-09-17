<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";
import { UserCheck, Stethoscope, HeartPulse } from "lucide-vue-next";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";
import type { CoverageBenefitItem } from "../../../../services/cmsService";

const { t } = useI18n();

const props = withDefaults(
  defineProps<{
    dynamicBenefits?: CoverageBenefitItem[] | null;
  }>(),
  {
    dynamicBenefits: null,
  }
);

const defaultBenefits = [
  {
    title: "coverage.benefits.primary.title",
    description: "coverage.benefits.primary.description",
    isTranslationKey: true,
    imageSrc: "/primary in web.png",
    icon: UserCheck,
    tag: "Essential",
  },
  {
    title: "coverage.benefits.hospitalization.title",
    description: "coverage.benefits.hospitalization.description",
    isTranslationKey: true,
    imageSrc: "/hospitalization.png",
    icon: Stethoscope,
    tag: "Inpatient Care",
  },
  {
    title: "coverage.benefits.maternal.title",
    description: "coverage.benefits.maternal.description",
    isTranslationKey: true,
    imageSrc: "/maternal-care.jpg",
    icon: HeartPulse,
    tag: "Family Health",
  },
];

const fallbackImages = ["/primary in web.png", "/hospitalization.png", "/maternal-care.jpg"];
const fallbackIcons = [UserCheck, Stethoscope, HeartPulse];

const renderedBenefits = computed(() => {
  if (props.dynamicBenefits && props.dynamicBenefits.length > 0) {
    return props.dynamicBenefits.map((item, idx) => ({
      title: item.title,
      description: item.description,
      isTranslationKey: false,
      imageSrc: fallbackImages[idx % fallbackImages.length],
      icon: fallbackIcons[idx % fallbackIcons.length],
      tag: item.tag,
    }));
  }
  return defaultBenefits;
});
</script>

<template>
  <section
    aria-labelledby="coverage-benefits-heading"
    class="relative w-full overflow-hidden bg-slate-50/70 dark:bg-[#021E31] px-[50px] py-10 sm:py-14 md:py-16"
  >
    <ScrollReveal direction="up" duration="0.8s">
      <header class="mx-auto flex max-w-[800px] flex-col items-center text-center">
        <h2
          id="coverage-benefits-heading"
          class="mt-3 sm:mt-4 [font-family:'Inter',Helvetica] text-[26px] xs:text-[30px] sm:text-[38px] md:text-[44px] lg:text-[50px] font-bold leading-[1.08] tracking-tight"
        >
          <span class="text-[#161616] dark:text-white">{{ t('coverage.heading_coverage') }}</span>
          <span class="bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] bg-clip-text text-transparent">{{ t('coverage.heading_goes_further') }}</span>
        </h2>

        <p
          class="mt-3 sm:mt-4 [font-family:'Inter',Helvetica] text-[14px] sm:text-base font-normal leading-relaxed text-[#64748b] dark:text-slate-400 md:text-lg"
        >
          {{ t('coverage.description') }}
        </p>
      </header>
    </ScrollReveal>

    <div class="relative mx-auto mt-8 sm:mt-10 max-w-[1240px]">
      <!-- Benefit cards with hover zoom and elevation -->
      <div class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 sm:gap-6 lg:gap-8">
        <ScrollReveal
          v-for="(benefit, idx) in renderedBenefits"
          :key="benefit.title"
          direction="up"
          :stagger-index="idx"
          :stagger-delay="140"
          :delay="100"
          duration="0.8s"
          class="w-full"
        >
          <Card
            class="card-hover-glow group relative mx-auto h-[320px] xs:h-[350px] sm:h-[420px] md:h-[480px] w-full max-w-[390px] sm:max-w-none overflow-hidden rounded-[16px] sm:rounded-[20px] border border-white dark:border-white/10 bg-white dark:bg-[#0B192C] shadow-[0_8px_24px_rgba(var(--color-primary-rgb),0.1)] transition-all duration-500 hover:-translate-y-2 hover:shadow-[0_20px_40px_rgba(var(--color-primary-rgb),0.2)]"
          >
            <CardContent class="relative h-full w-full p-0">
              <!-- Background Image with smooth zoom on hover -->
              <div class="h-full w-full overflow-hidden">
                <img
                  class="h-full w-full object-cover transition-transform duration-700 ease-out group-hover:scale-105"
                  :alt="`${benefit.title} coverage`"
                  :src="benefit.imageSrc"
                  loading="lazy"
                />
              </div>

              <!-- Gradient overlay for text readability -->
              <div class="pointer-events-none absolute inset-0 bg-gradient-to-b from-white/95 via-white/80 dark:from-[#0B192C]/95 dark:via-[#0B192C]/80 to-transparent pt-8 px-5 sm:px-6" />

              <!-- Text & Icon content -->
              <div
                class="absolute inset-x-0 top-0 flex flex-col items-center px-5 sm:px-6 pt-5 sm:pt-7 text-center"
              >
                <!-- Icon badge with lift -->
                <div class="flex h-10 w-10 sm:h-14 sm:w-14 items-center justify-center rounded-xl sm:rounded-2xl bg-white/90 text-[var(--color-primary)] shadow-md backdrop-blur-md transition-all duration-300 group-hover:scale-110 group-hover:bg-[var(--color-primary)] group-hover:text-white group-hover:shadow-[0_0_20px_rgba(var(--color-primary-rgb),0.3)]">
                  <component :is="benefit.icon" class="h-5 w-5 sm:h-7 sm:w-7" />
                </div>

                <h3
                  class="mt-2.5 sm:mt-4 [font-family:'Inter',Helvetica] text-[17px] xs:text-[18px] sm:text-[20px] md:text-[22px] font-bold leading-tight tracking-tight text-[#161616] dark:text-white transition-colors duration-300 group-hover:text-[var(--color-primary)]"
                >
                  {{ benefit.isTranslationKey ? t(benefit.title) : benefit.title }}
                </h3>

                <p
                  class="mt-1.5 sm:mt-2.5 max-w-[280px] [font-family:'Inter',Helvetica] text-[13px] sm:text-[14px] font-medium leading-relaxed text-[#64748b] dark:text-slate-300 line-clamp-4"
                >
                  {{ benefit.isTranslationKey ? t(benefit.description) : benefit.description }}
                </p>
              </div>
            </CardContent>
          </Card>
        </ScrollReveal>
      </div>
    </div>
  </section>
</template>
