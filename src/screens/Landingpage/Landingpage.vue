<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from "vue";
import { Phone, Clock, ShieldCheck } from "lucide-vue-next";
import CoverageBenefitsSection from "./sections/CoverageBenefitsSection/CoverageBenefitsSection.vue";
import EnrollmentProcessSection from "./sections/EnrollmentProcessSection/EnrollmentProcessSection.vue";
import HealthRightsOverviewSection from "./sections/HealthRightsOverviewSection/HealthRightsOverviewSection.vue";
import InsuranceHeroSection from "./sections/InsuranceHeroSection/InsuranceHeroSection.vue";
import NationalImpactMetricsSection from "./sections/NationalImpactMetricsSection/NationalImpactMetricsSection.vue";
import FooterSection from "./sections/FooterSection/FooterSection.vue";
import UtilityNavigationSection from "./sections/UtilityNavigationSection/UtilityNavigationSection.vue";
import ScrollReveal from "../../components/ui/ScrollReveal.vue";
import cmsService, { PublicContentData } from "../../services/cmsService";
import { applyTheme } from "../../services/theme";
import { getActiveLayoutSettings, LayoutSettings, DEFAULT_LAYOUT } from "../../services/layout";

import ModernExecutiveHero from "./layouts/ModernExecutiveHero.vue";
import CitizenServicesHub from "./layouts/CitizenServicesHub.vue";
import CommunityStorySection from "./layouts/CommunityStorySection.vue";

import stPeterLogo from "../../assets/partners logo/St.PeterLogo.png";
import tikurAnbessaLogo from "../../assets/partners logo/tikure-anbsa.png";
import yekatitLogo from "../../assets/partners logo/yekatit-hospital.png";

const dynamicContent = ref<PublicContentData | null>(null);
const showCmsLogin = ref(false);

const layoutSettings = computed<LayoutSettings>(() => {
  return getActiveLayoutSettings(dynamicContent.value?.settings);
});

const handleKeyDown = (event: KeyboardEvent) => {
  // Reveal/toggle CMS login when Ctrl+C (or Cmd+C on Mac) is pressed
  if (
    (event.ctrlKey || event.metaKey) &&
    !event.shiftKey &&
    !event.altKey &&
    (event.key === "c" || event.key === "C" || event.code === "KeyC")
  ) {
    const target = event.target as HTMLElement | null;
    const isInput =
      target &&
      (target.tagName === "INPUT" ||
        target.tagName === "TEXTAREA" ||
        target.isContentEditable);
    if (!isInput) {
      showCmsLogin.value = !showCmsLogin.value;
    }
  }
};

onMounted(async () => {
  window.addEventListener("keydown", handleKeyDown);
  try {
    const data = await cmsService.getPublicContent();
    if (data) {
      dynamicContent.value = data;
      if (data.settings) {
        applyTheme(data.settings);
      }
    }
  } catch {
    // Graceful fallback to default locale content
  }
});

onBeforeUnmount(() => {
  window.removeEventListener("keydown", handleKeyDown);
});

const partnerLogos = [
  { alt: "Eth Switch",        src: "/eth-switch-Photoroom.png"    },
  { alt: "Sidama Bank",       src: "/images-Photoroom.png"        },
  { alt: "Siinqee Bank",      src: "/Sinqee_Logo.png"             },
  { alt: "Telebirr",          src: "/Telebirr.png"                },
  { alt: "Kenema Pharmacy",   src: "/kenema_logo.png"             },
  { alt: "Ministry of Health",src: "/1755784071306-Photoroom.png" },
  { alt: "St. Peter Hospital",src: stPeterLogo                    },
  { alt: "Tikur Anbessa",     src: tikurAnbessaLogo               },
  { alt: "Yekatit Hospital",  src: yekatitLogo                    },
];
</script>


<template>
  <div class="w-full bg-[#fbfcfc] dark:bg-[#021E31]">
    <!-- Top banner bar -->
    <header
      class="flex min-h-8 sm:min-h-9 items-center justify-between gap-2 sm:gap-4 px-[50px] transition-colors duration-300"
      :style="{ background: 'linear-gradient(90deg, var(--header-grad-start, #3173cf) 3%, var(--color-primary, #0873b9) 55%, var(--header-grad-end, #245497) 100%)' }"
    >
      <div class="flex items-center gap-1.5 sm:gap-2">
        <Phone class="h-3 w-3 sm:h-3.5 sm:w-3.5 shrink-0 text-white" />
        <span
          class="whitespace-nowrap [font-family:'Inter',Helvetica] text-[11px] xs:text-[12px] sm:text-[14px] font-medium leading-normal text-white"
        >
          | {{ dynamicContent?.settings?.['contact.phone'] || '+251 911 000 000' }}
        </span>
      </div>
      <div class="flex items-center gap-3 sm:gap-4">
        <div class="flex items-center gap-1.5 sm:gap-2">
          <Clock class="h-3 w-3 sm:h-3.5 sm:w-3.5 shrink-0 text-white" />
          <span
            class="whitespace-nowrap [font-family:'Inter',Helvetica] text-[11px] xs:text-[12px] sm:text-[14px] font-medium leading-normal text-white"
          >
            | {{ dynamicContent?.settings?.['contact.support_hours'] || '24/7 Support' }}
          </span>
        </div>

        <!-- CMS Login Button (Visible only when Admin clicks Ctrl+C) -->
        <transition
          enter-active-class="transition-all duration-300 ease-out"
          enter-from-class="opacity-0 scale-90 translate-y-[-2px]"
          enter-to-class="opacity-100 scale-100 translate-y-0"
          leave-active-class="transition-all duration-200 ease-in"
          leave-from-class="opacity-100 scale-100 translate-y-0"
          leave-to-class="opacity-0 scale-90 translate-y-[-2px]"
        >
          <router-link
            v-if="showCmsLogin"
            id="cms-login-header-btn"
            to="/admin/login"
            class="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-md bg-white/20 hover:bg-white/30 text-white text-[11px] font-bold transition-all border border-white/30 shadow-sm"
            title="Admin CMS Login"
          >
            <ShieldCheck class="w-3.5 h-3.5 text-emerald-300" />
            <span>CMS Login</span>
          </router-link>
        </transition>
      </div>
    </header>

    <!-- Navigation -->
    <UtilityNavigationSection />

    <!-- Main Content -->
    <main class="flex flex-col">
      <!-- Hero Variant Selector -->
      <section id="section-home">
        <template v-if="layoutSettings.heroStyle === 'split'">
          <ModernExecutiveHero :dynamic-settings="dynamicContent?.settings" />
        </template>
        <template v-else-if="layoutSettings.heroStyle === 'services-hero'">
          <ModernExecutiveHero :dynamic-settings="dynamicContent?.settings" />
          <CitizenServicesHub />
        </template>
        <template v-else>
          <InsuranceHeroSection :dynamic-settings="dynamicContent?.settings" />
        </template>
      </section>

      <!-- ─── Trusted Partners & Financial Integrations ─── -->
      <ScrollReveal v-if="layoutSettings.showPartners" :delay="200" direction="up" duration="0.9s">
        <section
          class="w-full my-8 sm:my-12 md:my-20 border-y border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-[#02182b] py-8 sm:py-12 md:py-16 shadow-sm relative"
          aria-label="Trusted partners and financial integrations"
        >
          <!-- Label -->
          <p class="mb-6 sm:mb-8 md:mb-10 text-center [font-family:'Inter',Helvetica] text-[10px] sm:text-[12px] font-bold uppercase tracking-[2px] sm:tracking-[4px] text-slate-500 dark:text-slate-400 px-4">
            Trusted Partners &amp; Financial Integrations
          </p>

          <!--
            Single-wrapper seamless marquee:
            The animated div holds BOTH copies; moving -50% = exactly one set width.
            No jump on reset because the two halves are visually identical at 0% and -50%.
          -->
          <div
            class="marquee-wrapper relative overflow-hidden py-4 [mask-image:linear-gradient(to_right,transparent,white_10%,white_90%,transparent)]"
          >
            <div class="animate-marquee-seamless flex w-max items-center gap-3 xs:gap-4 sm:gap-6 lg:gap-8 hover:[animation-play-state:paused] transition-all">
              <!-- Copy A (visible) -->
              <template v-for="logo in partnerLogos" :key="`a-${logo.alt}`">
                <div class="group relative flex shrink-0 items-center justify-center w-[110px] h-[65px] xs:w-[130px] xs:h-[75px] sm:w-[160px] sm:h-[90px] lg:w-[200px] lg:h-[110px] rounded-xl sm:rounded-2xl bg-white dark:bg-[#032a45] shadow-[0_4px_15px_rgb(0,0,0,0.03)] dark:shadow-[0_4px_20px_rgb(0,0,0,0.2)] border border-slate-100 dark:border-white/5 transition-all duration-300 hover:-translate-y-1.5 hover:shadow-[0_12px_30px_rgb(0,0,0,0.08)] dark:hover:shadow-[0_12px_30px_rgb(0,0,0,0.3)] hover:border-blue-200 dark:hover:border-blue-500/30 cursor-pointer overflow-hidden">
                  <!-- Subtle gradient shine on hover -->
                  <div class="absolute inset-0 bg-gradient-to-tr from-transparent via-blue-50/50 dark:via-blue-400/5 to-transparent opacity-0 transition-opacity duration-300 group-hover:opacity-100"></div>
                  
                  <img
                    :src="logo.src"
                    :alt="logo.alt"
                    class="relative z-10 h-[32px] xs:h-[38px] sm:h-[45px] lg:h-[55px] w-auto max-w-[80px] xs:max-w-[95px] sm:max-w-[110px] lg:max-w-[140px] object-contain transition-all duration-300 group-hover:scale-110"
                    loading="lazy"
                  />
                </div>
              </template>

              <!-- Copy B (seamless duplicate, hidden from screen readers) -->
              <template v-for="logo in partnerLogos" :key="`b-${logo.alt}`">
                <div class="group relative flex shrink-0 items-center justify-center w-[160px] h-[90px] sm:w-[200px] sm:h-[110px] rounded-2xl bg-white dark:bg-[#032a45] shadow-[0_4px_15px_rgb(0,0,0,0.03)] dark:shadow-[0_4px_20px_rgb(0,0,0,0.2)] border border-slate-100 dark:border-white/5 transition-all duration-300 hover:-translate-y-1.5 hover:shadow-[0_12px_30px_rgb(0,0,0,0.08)] dark:hover:shadow-[0_12px_30px_rgb(0,0,0,0.3)] hover:border-blue-200 dark:hover:border-blue-500/30 cursor-pointer overflow-hidden" aria-hidden="true">
                  <!-- Subtle gradient shine on hover -->
                  <div class="absolute inset-0 bg-gradient-to-tr from-transparent via-blue-50/50 dark:via-blue-400/5 to-transparent opacity-0 transition-opacity duration-300 group-hover:opacity-100"></div>
                  
                  <img
                    :src="logo.src"
                    :alt="logo.alt"
                    class="relative z-10 h-[45px] sm:h-[55px] w-auto max-w-[110px] sm:max-w-[140px] object-contain transition-all duration-300 group-hover:scale-110"
                    loading="lazy"
                  />
                </div>
              </template>
            </div>
          </div>
        </section>
      </ScrollReveal>

      <!-- ─── Citizen Quick Services Hub (if enabled outside services-hero) ─── -->
      <ScrollReveal v-if="layoutSettings.showQuickServices && layoutSettings.heroStyle !== 'services-hero'" :delay="100" direction="up" duration="0.9s">
        <CitizenServicesHub />
      </ScrollReveal>

      <!-- ─── Philosophy ─── -->
      <ScrollReveal v-if="layoutSettings.showPhilosophy" :delay="100" direction="up" duration="0.9s">
        <section id="section-philosophy">
          <HealthRightsOverviewSection />
        </section>
      </ScrollReveal>

      <!-- ─── National Impact ─── -->
      <ScrollReveal v-if="layoutSettings.showMetrics" :delay="100" direction="up" duration="0.9s">
        <section id="section-our-impact">
          <NationalImpactMetricsSection :dynamic-metrics="dynamicContent?.metrics" />
        </section>
      </ScrollReveal>

      <!-- ─── Coverage Benefits ─── -->
      <ScrollReveal v-if="layoutSettings.showBenefits" :delay="100" direction="up" duration="0.9s">
        <section id="section-benefits">
          <CoverageBenefitsSection :dynamic-benefits="dynamicContent?.benefits" />
        </section>
      </ScrollReveal>

      <!-- ─── Community Beneficiary Stories ─── -->
      <ScrollReveal v-if="layoutSettings.showStories" :delay="100" direction="up" duration="0.9s">
        <CommunityStorySection />
      </ScrollReveal>

      <!-- ─── Enrollment Process ─── -->
      <ScrollReveal v-if="layoutSettings.showEnrollment" :delay="100" direction="up" duration="0.9s">
        <section id="section-process">
          <EnrollmentProcessSection :dynamic-steps="dynamicContent?.enrollmentSteps" />
        </section>
      </ScrollReveal>
    </main>

    <FooterSection />

    <!-- Floating Admin Portal Quick Access (Revealed via Ctrl+C) -->
    <transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 translate-y-4 scale-95"
      enter-to-class="opacity-100 translate-y-0 scale-100"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 translate-y-0 scale-100"
      leave-to-class="opacity-0 translate-y-4 scale-95"
    >
      <div
        v-if="showCmsLogin"
        id="cms-login-floating-pill"
        class="fixed bottom-6 right-6 z-50 flex items-center gap-2 p-1.5 pl-3 rounded-full bg-slate-900/90 text-white shadow-2xl backdrop-blur-md border border-white/20"
      >
        <span class="text-xs font-semibold text-slate-300">Admin Mode</span>
        <router-link
          to="/admin/login"
          class="flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-[var(--color-primary)] hover:opacity-90 text-white text-xs font-bold transition-all shadow-md"
        >
          <ShieldCheck class="w-3.5 h-3.5" />
          <span>CMS Login</span>
        </router-link>
        <button
          type="button"
          @click="showCmsLogin = false"
          class="p-1 rounded-full text-slate-400 hover:text-white hover:bg-white/10 text-xs transition-colors"
          title="Hide CMS Button"
        >
          ✕
        </button>
      </div>
    </transition>
  </div>
</template>

