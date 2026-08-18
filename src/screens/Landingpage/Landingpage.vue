<script setup lang="ts">
import { Phone, Clock } from "lucide-vue-next";
import CoverageBenefitsSection from "./sections/CoverageBenefitsSection/CoverageBenefitsSection.vue";
import EnrollmentProcessSection from "./sections/EnrollmentProcessSection/EnrollmentProcessSection.vue";
import HealthRightsOverviewSection from "./sections/HealthRightsOverviewSection/HealthRightsOverviewSection.vue";
import InsuranceHeroSection from "./sections/InsuranceHeroSection/InsuranceHeroSection.vue";
import NationalImpactMetricsSection from "./sections/NationalImpactMetricsSection/NationalImpactMetricsSection.vue";
import FooterSection from "./sections/FooterSection/FooterSection.vue";
import UtilityNavigationSection from "./sections/UtilityNavigationSection/UtilityNavigationSection.vue";
import ScrollReveal from "../../components/ui/ScrollReveal.vue";

import stPeterLogo from "../../assets/partners logo/St.PeterLogo.png";
import tikurAnbessaLogo from "../../assets/partners logo/tikure-anbsa.png";
import yekatitLogo from "../../assets/partners logo/yekatit-hospital.png";

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
  <div class="w-full overflow-x-hidden bg-[#fbfcfc] dark:bg-[#021E31]">
    <!-- Top banner bar -->
    <header
      class="flex min-h-9 items-center justify-between gap-4 bg-[linear-gradient(90deg,rgba(49,115,207,1)_3%,rgba(60,141,253,1)_55%,rgba(36,84,151,1)_100%)] px-4 sm:px-[54px]"
    >
      <div class="flex items-center gap-2">
        <Phone class="h-3.5 w-3.5 shrink-0 text-white" />
        <span
          class="whitespace-nowrap [font-family:'Inter',Helvetica] text-[14px] font-medium leading-normal text-white"
        >
          | +251 911 000 000
        </span>
      </div>
      <div class="flex items-center gap-2">
        <Clock class="h-3.5 w-3.5 shrink-0 text-white" />
        <span
          class="whitespace-nowrap [font-family:'Inter',Helvetica] text-[14px] font-medium leading-normal text-white"
        >
          | 24/7 Support
        </span>
      </div>
    </header>

    <!-- Navigation -->
    <UtilityNavigationSection />

    <!-- Main Content -->
    <main class="flex flex-col">
      <!-- Hero -->
      <section id="section-home">
        <InsuranceHeroSection />
      </section>

      <!-- ─── Trusted Partners & Financial Integrations ─── -->
      <ScrollReveal :delay="200" direction="up" duration="0.9s">
        <section
          class="w-full my-12 sm:my-20 border-y border-slate-100 dark:border-white/10 bg-white dark:bg-[#021E31] py-8 sm:py-12 shadow-sm"
          aria-label="Trusted partners and financial integrations"
        >
          <!-- Label -->
          <p class="mb-5 text-center [font-family:'Inter',Helvetica] text-[11px] font-semibold uppercase tracking-[3px] text-[#9ca3af] dark:text-slate-400">
            Trusted Partners &amp; Financial Integrations
          </p>

          <!--
            Single-wrapper seamless marquee:
            The animated div holds BOTH copies; moving -50% = exactly one set width.
            No jump on reset because the two halves are visually identical at 0% and -50%.
          -->
          <div
            class="marquee-wrapper relative overflow-hidden [mask-image:linear-gradient(to_right,transparent,white_8%,white_92%,transparent)]"
          >
            <div class="animate-marquee-seamless flex w-max items-center gap-16 sm:gap-24">
              <!-- Copy A (visible) -->
              <template v-for="logo in partnerLogos" :key="`a-${logo.alt}`">
                <div class="group flex shrink-0 items-center justify-center">
                  <img
                    :src="logo.src"
                    :alt="logo.alt"
                    class="h-[52px] sm:h-[60px] w-auto max-w-[150px] object-contain opacity-50 dark:opacity-70 dark:brightness-200 grayscale transition-all duration-300 group-hover:opacity-100 group-hover:grayscale-0 group-hover:scale-105"
                    loading="lazy"
                  />
                </div>
              </template>

              <!-- Copy B (seamless duplicate, hidden from screen readers) -->
              <template v-for="logo in partnerLogos" :key="`b-${logo.alt}`">
                <div class="group flex shrink-0 items-center justify-center" aria-hidden="true">
                  <img
                    :src="logo.src"
                    :alt="logo.alt"
                    class="h-[52px] sm:h-[60px] w-auto max-w-[150px] object-contain opacity-50 dark:opacity-70 dark:brightness-200 grayscale transition-all duration-300 group-hover:opacity-100 group-hover:grayscale-0 group-hover:scale-105"
                    loading="lazy"
                  />
                </div>
              </template>
            </div>
          </div>
        </section>
      </ScrollReveal>

      <!-- ─── Philosophy ─── -->
      <ScrollReveal :delay="100" direction="up" duration="0.9s">
        <section id="section-philosophy">
          <HealthRightsOverviewSection />
        </section>
      </ScrollReveal>

      <!-- ─── National Impact ─── -->
      <ScrollReveal :delay="100" direction="up" duration="0.9s">
        <section id="section-our-impact">
          <NationalImpactMetricsSection />
        </section>
      </ScrollReveal>

      <!-- ─── Coverage Benefits ─── -->
      <ScrollReveal :delay="100" direction="up" duration="0.9s">
        <section id="section-benefits">
          <CoverageBenefitsSection />
        </section>
      </ScrollReveal>

      <!-- ─── Enrollment Process ─── -->
      <ScrollReveal :delay="100" direction="up" duration="0.9s">
        <section id="section-process">
          <EnrollmentProcessSection />
        </section>
      </ScrollReveal>
    </main>

    <FooterSection />
  </div>
</template>

