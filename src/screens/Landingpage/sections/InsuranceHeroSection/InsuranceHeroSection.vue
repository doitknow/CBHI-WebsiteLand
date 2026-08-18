<script setup lang="ts">
import { ArrowRight } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import Button from "../../../../components/ui/Button.vue";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";

const { t } = useI18n();

const partnerCards = [
  {
    image: "/ehis-logo_1.png",
    imageAlt: "CBHI Ethiopian logo",
    // name: "CBHI Ethiopian",
    imageClassName: "h-[85px] w-[145px] object-contain transition-transform duration-300 group-hover:scale-105",
    contentClassName:
      "items-center rounded-l-[20px] border-b-[3px] border-l border-r-[3px] border-[#3c8dfd] px-8 py-[12px] sm:pl-[48px]",
  },
  {
    image: "/moh_1.png",
    imageAlt: "Ministry of Health logo",
    // name: "Ministry of Health",
    imageClassName: "h-[85px] w-[150px] object-contain transition-transform duration-300 group-hover:scale-105",
    contentClassName:
      "items-center rounded-r-[20px] border-b-[3px] border-l-[3px] border-r border-[#3c8dfd] px-8 py-[12px] sm:pr-[36px]",
  },
];
</script>

<template>
  <section
    aria-labelledby="insurance-hero-title"
    class="relative w-full bg-[#fcfcfd] dark:bg-[#021E31] [font-family:'Inter',Helvetica]"
  >
    <!-- Hero image area — image is the full background, has blue wave baked in -->
    <div
      class="relative w-full"
      style="
        min-height: 640px;
        padding-bottom: 100px;
      "
    >
      <!-- Smooth crossfade background images -->
      <img
        src="/home image.png"
        alt=""
        aria-hidden="true"
        class="pointer-events-none absolute inset-0 h-full w-full object-cover object-top opacity-100 transition-opacity duration-[800ms] ease-in-out dark:opacity-0"
      />
      <img
        src="/home image dark.png"
        alt=""
        aria-hidden="true"
        class="pointer-events-none absolute inset-0 h-full w-full object-cover object-top opacity-0 transition-opacity duration-[800ms] ease-in-out dark:opacity-100"
      />

      <!-- Subtle ambient gradient overlay -->
      <div
        aria-hidden="true"
        class="pointer-events-none absolute inset-0 bg-gradient-to-br from-[#3c8dfd]/[0.03] via-transparent to-[#0873b9]/[0.03] animate-gradient-shift"
      />

      <!-- Text content sits in the left white area of the image -->
      <div
        class="relative z-10 flex flex-col items-start px-8 pt-[50px] sm:px-16 lg:w-[46%] lg:px-[101px] lg:pt-[52px]"
      >
        <p class="animate-hero-reveal inline-flex items-center gap-1.5  bg-emerald-50 px-3 py-1 text-[15px] font-semibold text-emerald-600 shadow-sm ">
          <span class="h-2 w-2 rounded-full bg-emerald-500 animate-ping" />
          {{ t('hero.welcome') }}
        </p>

        <h1
          id="insurance-hero-title"
          class="mt-1 text-[46px] font-bold leading-[1] tracking-[0] sm:text-[62px] lg:text-[82px]"
        >
          <span class="block text-[#161616] dark:text-white animate-hero-reveal anim-delay-1">{{ t('hero.title_ethiopian') }}</span>
          <span class="block bg-gradient-to-r from-[#3c8dfd] to-[#0873b9] bg-clip-text text-transparent animate-hero-reveal anim-delay-3">
            {{ t('hero.title_health_insurance') }}
          </span>
        </h1>

        <p
          class="animate-slide-up-fade anim-delay-5 mt-6 max-w-[420px] text-[15.5px] font-medium leading-[1.65] text-[#71717a] dark:text-slate-300 lg:mt-[36px]"
        >
          {{ t('hero.subtitle') }}
        </p>

        <div class="mt-8 flex flex-wrap items-center gap-3 lg:mt-[38px] animate-slide-up-fade anim-delay-6">
          <Button
            type="button"
            class="cta-shimmer group inline-flex h-11 items-center gap-2 rounded-xl bg-gradient-to-r from-[#3c8dfd] to-[#0873b9] px-6 text-[15px] font-semibold text-white shadow-[0_3px_12px_rgba(60,141,253,0.35)] transition-all duration-300 hover:scale-[1.02] hover:shadow-[0_6px_20px_rgba(60,141,253,0.45)] active:scale-[0.98]"
          >
            {{ t('hero.get_started') }}
            <ArrowRight class="h-4 w-4 transition-transform duration-300 group-hover:translate-x-1" />
          </Button>

          <Button
            type="button"
            variant="outline"
            class="h-11 rounded-xl border-2 border-[#3c8dfd]/80 bg-white/70 px-6 text-[15px] font-semibold text-[#0873b9] backdrop-blur-sm transition-all duration-300 hover:border-[#3c8dfd] hover:bg-[#3c8dfd]/8 active:scale-[0.98]"
          >
            {{ t('hero.learn_more') }}
          </Button>
        </div>

        <!-- Member count badge with subtle float animation -->
        <div class="animate-float animate-scale-in anim-delay-8 mt-8 inline-flex items-center gap-4 rounded-2xl bg-white/80 dark:bg-slate-900/80 p-2.5 shadow-sm backdrop-blur-sm ring-1 ring-slate-100 dark:ring-white/10 lg:mt-[42px]">
          <div class="flex items-center">
            <div class="h-8 w-8 rounded-full border-2 border-white bg-[#c4d6f0] shadow-sm" />
            <div class="-ml-2.5 h-8 w-8 rounded-full border-2 border-white bg-[#a8c4e4] shadow-sm" />
            <div class="-ml-2.5 h-8 w-8 rounded-full border-2 border-white bg-[#8cb2d8] shadow-sm" />
            <span
              class="-ml-2.5 flex h-8 w-8 items-center justify-center rounded-full border-2 border-white bg-[#22c55e] text-[9px] font-bold text-white shadow-sm"
            >
              2M+
            </span>
          </div>
          <p class="pr-2 text-[10px] font-semibold leading-[1.35]">
            <span class="block text-slate-900 dark:text-white">{{ t('hero.members_count') }}</span>
            <span class="text-slate-500 dark:text-slate-400">{{ t('hero.and_growing') }}</span>
          </p>
        </div>
      </div>
    </div>

    <!-- "Endorsed & Operated By" overlapping upward into the hero image -->
    <div
      class="relative z-20 mx-auto w-full max-w-[1021px] px-4 lg:px-0"
      style="margin-top: -110px;"
    >
      <!-- Pill label -->
      <div class="flex justify-center">
        <div
          class="flex h-[32px] w-[310px] items-center justify-center rounded-[20px] border border-white/70 bg-white/40 shadow-sm backdrop-blur-[6px] transition-all hover:bg-white/60"
        >
          <span class="text-sm font-semibold tracking-wide text-slate-700">
            {{ t('hero.endorsed_by') }}
          </span>
        </div>
      </div>

      <!-- Partner cards with smooth hover lift + 3D tilt -->
      <div class="grid w-full grid-cols-2">
        <Card
          v-for="partner in partnerCards"
          :key="partner.name"
          class="card-tilt group h-[126px] rounded-none border-0 bg-transparent shadow-none transition-all duration-300 hover:shadow-lg"
        >
          <CardContent
            :class="[
              'relative flex h-full flex-col justify-center gap-2.5 bg-white transition-shadow duration-300',
              partner.contentClassName,
            ]"
          >
            <img
              :class="partner.imageClassName"
              :alt="partner.imageAlt"
              :src="partner.image"
            />
            <p
              class="absolute left-[54%] top-1/2 -translate-y-1/2 whitespace-nowrap text-[15px] font-bold tracking-tight text-[#0873b9] sm:text-[20.8px]"
            >
              {{ partner.name }}
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  </section>
</template>
