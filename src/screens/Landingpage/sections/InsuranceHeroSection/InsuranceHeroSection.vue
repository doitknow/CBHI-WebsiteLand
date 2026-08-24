<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import { ArrowRight, ChevronLeft, ChevronRight } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import Button from "../../../../components/ui/Button.vue";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";

import slide1 from "../../../../assets/slider/slide 1.png";
import slide2 from "../../../../assets/slider/slide 2.png";
import slide3 from "../../../../assets/slider/slide 3.png";

const { t } = useI18n();

const slideImages = [ slide2, slide3];
const currentSlide = ref(0);
const heroLoaded = ref(false);
let slideInterval: number | undefined;

const startInterval = () => {
  slideInterval = window.setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % slideImages.length;
  }, 6000);
};

const resetInterval = () => {
  if (slideInterval) {
    clearInterval(slideInterval);
  }
  startInterval();
};

const prevSlide = () => {
  currentSlide.value = (currentSlide.value - 1 + slideImages.length) % slideImages.length;
  resetInterval();
};

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % slideImages.length;
  resetInterval();
};

onMounted(() => {
  startInterval();
  // Trigger entrance animation after a tiny paint delay
  requestAnimationFrame(() => {
    heroLoaded.value = true;
  });
});

onUnmounted(() => {
  if (slideInterval) {
    clearInterval(slideInterval);
  }
});

const partnerCards = [
  {
    image: "/ehis-logo_1.png",
    imageAlt: "CBHI Ethiopian logo",
    // name: "CBHI Ethiopian",
    imageClassName: "h-[85px] w-[145px] object-contain transition-transform duration-300 group-hover:scale-105",
    contentClassName:
      "items-center rounded-l-[20px] border-b-[3px] border-l border-r-[3px] border-[#0873b9] px-8 py-[12px] sm:pl-[48px]",
  },
  {
    image: "/moh_1.png",
    imageAlt: "Ministry of Health logo",
    // name: "Ministry of Health",
    imageClassName: "h-[85px] w-[150px] object-contain transition-transform duration-300 group-hover:scale-105",
    contentClassName:
      "items-center rounded-r-[20px] border-b-[3px] border-l-[3px] border-r border-[#0873b9] px-8 py-[12px] sm:pr-[36px]",
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
      <!-- Animated SVG Wave Background with Cinematic Entrance -->
      <div
        class="pointer-events-none absolute inset-0 overflow-hidden bg-transparent hero-image-area"
        :class="{ 'hero-image-entered': heroLoaded }"
      >
        <!-- Glow ring behind the image area -->
        <div class="hero-glow-ring" />

        <!-- Floating particle dots -->
        <div class="hero-particles" aria-hidden="true">
          <span class="particle p1" />
          <span class="particle p2" />
          <span class="particle p3" />
          <span class="particle p4" />
          <span class="particle p5" />
          <span class="particle p6" />
        </div>

        <!-- Light sweep overlay -->
        <div class="hero-light-sweep" />

        <div class="wave">
          <svg
            viewBox="0 0 900 397"
            preserveAspectRatio="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <defs>
              <clipPath id="main-wave-clip">
                <path
                  class="main-wave"
                  d="M0 397 L0 250 C 0 397 227 426 240 397 C253 368 285 354 296 315 C308 270 321 244 358 216 C397 187 445 168 477 139 C510 109 516 70 550 35 C571 14 595 3 630 0 L900 0 L900 397 Z"
                />
              </clipPath>
              <clipPath id="image-curve-clip">
                <path d="M 300 0 C 500 150, 200 250, 450 397 L 900 397 L 900 0 Z" />
              </clipPath>
            </defs>
            <path
              class="dark-wave"
              d="M0 397 L0 80 C 0 397 120 429 155 397 C190 365 225 350 232 305 C240 255 263 225 300 194 C347 155 397 137 430 105 C465 71 475 35 526 12 C548 2 575 0 610 0 L900 0 L900 397 Z"
              fill="#0873b9"
              opacity="0.08"
              filter="blur(2px)"
            />
            <path
              class="middle-wave"
              d="M0 397 L0 150 C 0 397 175 445 205 397 C225 365 260 354 270 313 C282 264 298 236 335 208 C377 176 426 157 457 128 C489 98 501 57 535 28 C552 13 575 4 605 0 L900 0 L900 397 Z"
              fill="#0873b9"
              opacity="0.14"
  
              
            />
            <g clip-path="url(#main-wave-clip)">
              <rect width="100%" height="100%" fill="#0873b9" />
              <g 
                class="transition-transform duration-[1800ms] ease-[cubic-bezier(0.25,0.1,0.25,1)]"
                :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
              >
                <image
                  v-for="(slide, index) in slideImages"
                  :key="index"
                  :href="slide"
                  width="100%"
                  height="100%"
                  :x="`${index * 100}%`"
                  preserveAspectRatio="xMidYMid slice"
                  :class="['hero-slide-image', { 'slide-active': currentSlide === index }]"
                />
              </g>
            </g>
            <path
              class="main-wave"
              d="M0 250 C 0 397 227 426 240 397 C253 368 285 354 296 315 C308 270 321 244 358 216 C397 187 445 168 477 139 C510 109 516 70 550 35 C571 14 595 3 630 0 L900 0"
              fill="none"
              stroke="#388BF8"
              stroke-width="3"
              opacity="0.8"
            />
          </svg>
        </div>
      </div>

      <!-- Subtle ambient gradient overlay -->
      <div
        aria-hidden="true"
        class="pointer-events-none absolute inset-0 bg-gradient-to-br from-[#0873b9]/[0.03] via-transparent to-[#0873b9]/[0.03] animate-gradient-shift"
      />

      <!-- Text content sits in the left white area of the image -->
      <div
        class="relative z-10 flex flex-col items-start px-8 pt-[50px] sm:px-16 lg:w-[46%] lg:px-[101px] lg:pt-[52px]"
      >

        <h1
          id="insurance-hero-title"
          class="mt-1 text-[46px] font-bold leading-[1] tracking-[0] sm:text-[62px] lg:text-[82px]"
        >
          <span class="inline-block text-[#161616] dark:text-white animate-hero-reveal anim-delay-1 mr-3 lg:mr-5">{{ t('hero.title_ethiopian') }}</span>
          <span class="inline-block bg-gradient-to-r from-[#0873b9] to-[#0873b9] bg-clip-text text-transparent animate-hero-reveal anim-delay-3">
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
            class="cta-shimmer group inline-flex h-11 items-center gap-2 rounded-xl bg-gradient-to-r from-[#0873b9] to-[#0873b9] px-6 text-[15px] font-semibold text-white shadow-[0_3px_12px_rgba(8,115,185,0.35)] transition-all duration-300 hover:scale-[1.02] hover:shadow-[0_6px_20px_rgba(8,115,185,0.45)] active:scale-[0.98]"
          >
            {{ t('hero.get_started') }}
            <ArrowRight class="h-4 w-4 transition-transform duration-300 group-hover:translate-x-1" />
          </Button>

          <Button
            type="button"
            variant="outline"
            class="h-11 rounded-xl border-2 border-[#0873b9]/80 bg-white/70 px-6 text-[15px] font-semibold text-[#0873b9] backdrop-blur-sm transition-all duration-300 hover:border-[#0873b9] hover:bg-[#0873b9]/8 active:scale-[0.98]"
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

      <!-- Slider Navigation Controls -->
      <div class="absolute bottom-[130px] right-8 z-30 flex gap-3 sm:right-16 lg:bottom-[150px] lg:right-[101px]">
        <button
          type="button"
          @click="prevSlide"
          aria-label="Previous slide"
          class="flex h-11 w-11 items-center justify-center rounded-full bg-black/20 text-white shadow-sm backdrop-blur-md transition-all duration-300 hover:scale-110 hover:bg-black/40 active:scale-95"
        >
          <ChevronLeft class="h-6 w-6" />
        </button>
        <button
          type="button"
          @click="nextSlide"
          aria-label="Next slide"
          class="flex h-11 w-11 items-center justify-center rounded-full bg-black/20 text-white shadow-sm backdrop-blur-md transition-all duration-300 hover:scale-110 hover:bg-black/40 active:scale-95"
        >
          <ChevronRight class="h-6 w-6" />
        </button>
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
          class="flex h-[32px] w-[310px] items-center justify-center rounded-tl-[20px] rounded-tr-[20px] rounded-br-[0px] rounded-bl-[0px] border border-white/70 bg-white/40 shadow-sm backdrop-blur-[6px] transition-all hover:bg-white/60"
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

<style scoped>
/* ── Base wave styles ── */
.wave {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.wave svg {
  width: 100%;
  height: 100%;
  display: block;
}

.dark-wave {
  animation: darkFlow 8s ease-in-out infinite alternate;
}

.middle-wave {
  animation: middleFlow 7s ease-in-out infinite alternate;
}

.thin-wave {
  animation: thinFlow 6.5s ease-in-out infinite alternate;
}

.main-wave {
  animation: mainFlow 6s ease-in-out infinite alternate;
}

@keyframes darkFlow {
  from { transform: translateX(0); }
  to   { transform: translateX(-8px); }
}

@keyframes middleFlow {
  from { transform: translateX(0); }
  to   { transform: translateX(-5px); }
}

@keyframes mainFlow {
  from { transform: translateX(0); }
  to   { transform: translateX(-3px); }
}

@keyframes thinFlow {
  from { transform: translateX(0); }
  to   { transform: translateX(-4px); }
}

/* ═══════════════════════════════════════════
   ✦ Cinematic Hero Entrance Effects
   ═══════════════════════════════════════════ */

/* 1. Curtain reveal — the whole image area slides in and fades up */
.hero-image-area {
  opacity: 0;
  transform: scale(1.06) translateY(18px);
  filter: blur(6px);
  transition:
    opacity 1.4s cubic-bezier(0.16, 1, 0.3, 1),
    transform 1.6s cubic-bezier(0.16, 1, 0.3, 1),
    filter 1.2s ease-out;
}

.hero-image-entered {
  opacity: 1;
  transform: scale(1) translateY(0);
  filter: blur(0);
}

/* 2. Ken Burns — slow zoom on active slide image */
.hero-slide-image {
  transform-origin: center center;
  transition: transform 0.8s ease;
}

.slide-active {
  animation: ken-burns 7s ease-in-out forwards;
}

@keyframes ken-burns {
  0%   { transform: scale(1); }
  100% { transform: scale(1.08); }
}

/* 3. Glow ring — soft pulsing accent behind the image */
.hero-glow-ring {
  position: absolute;
  top: 10%;
  right: -5%;
  width: 55%;
  height: 80%;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(8,115,185,0.18) 0%, transparent 70%);
  animation: glow-ring-pulse 5s ease-in-out infinite;
  pointer-events: none;
  z-index: 0;
}

@keyframes glow-ring-pulse {
  0%, 100% {
    opacity: 0.5;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.12);
  }
}

/* 4. Light sweep — diagonal shine across the image on load */
.hero-light-sweep {
  position: absolute;
  top: 0;
  left: -120%;
  width: 60%;
  height: 100%;
  background: linear-gradient(
    105deg,
    transparent 0%,
    rgba(255, 255, 255, 0.08) 35%,
    rgba(255, 255, 255, 0.18) 50%,
    rgba(255, 255, 255, 0.08) 65%,
    transparent 100%
  );
  z-index: 2;
  pointer-events: none;
  animation: light-sweep-pass 2.8s 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
}

@keyframes light-sweep-pass {
  0%   { left: -120%; opacity: 0; }
  10%  { opacity: 1; }
  100% { left: 180%; opacity: 0; }
}

/* 5. Floating particles — soft glowing dots that drift upward */
.hero-particles {
  position: absolute;
  inset: 0;
  z-index: 1;
  pointer-events: none;
  overflow: hidden;
}

.particle {
  position: absolute;
  border-radius: 50%;
  background: rgba(60, 141, 253, 0.5);
  box-shadow: 0 0 6px rgba(60, 141, 253, 0.4);
  animation: particle-float linear infinite;
}

.p1 {
  width: 4px; height: 4px;
  left: 62%; bottom: -8px;
  animation-duration: 9s;
  animation-delay: 0.3s;
}
.p2 {
  width: 3px; height: 3px;
  left: 75%; bottom: -6px;
  animation-duration: 11s;
  animation-delay: 1.5s;
  background: rgba(56, 139, 248, 0.6);
}
.p3 {
  width: 5px; height: 5px;
  left: 55%; bottom: -10px;
  animation-duration: 8s;
  animation-delay: 3s;
}
.p4 {
  width: 3px; height: 3px;
  left: 82%; bottom: -6px;
  animation-duration: 12s;
  animation-delay: 0.8s;
  background: rgba(255, 255, 255, 0.35);
}
.p5 {
  width: 4px; height: 4px;
  left: 68%; bottom: -8px;
  animation-duration: 10s;
  animation-delay: 4s;
  background: rgba(34, 197, 94, 0.4);
  box-shadow: 0 0 6px rgba(34, 197, 94, 0.3);
}
.p6 {
  width: 2px; height: 2px;
  left: 90%; bottom: -4px;
  animation-duration: 14s;
  animation-delay: 2s;
  background: rgba(255, 255, 255, 0.25);
}

@keyframes particle-float {
  0% {
    transform: translateY(0) translateX(0) scale(0);
    opacity: 0;
  }
  10% {
    transform: translateY(-40px) translateX(5px) scale(1);
    opacity: 1;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    transform: translateY(-700px) translateX(-20px) scale(0.3);
    opacity: 0;
  }
}
</style>
