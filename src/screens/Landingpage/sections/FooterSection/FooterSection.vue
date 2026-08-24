<script setup lang="ts">
import { Mail, MapPin, Facebook, Instagram, Linkedin, ChevronRight } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";

const { t } = useI18n();
</script>

<template>
  <footer
    aria-label="Site footer"
    class="relative mt-8 w-full [font-family:'Inter',Helvetica]"
  >
    <!-- Main footer background -->
    <div
      class="relative bg-[#021326] px-4 xs:px-6 pb-6 pt-14 sm:pt-20 sm:px-8 lg:px-16"
      style="background: linear-gradient(180deg, #021429 0%, #000000 100%);"
    >
      <!-- "Get in Touch" floating pill -->
      <!-- <ScrollReveal direction="down" duration="0.7s" :distance="24">
        <div
          class="absolute -top-8 left-1/2 flex h-16 w-fit -translate-x-1/2 items-center justify-center whitespace-nowrap rounded-2xl bg-white px-12 shadow-[0_4px_25px_rgba(0,0,0,0.2)] transition-shadow duration-300 hover:shadow-[0_8px_35px_rgba(0,0,0,0.25)]"
        >
          <h2 class="text-[28px] font-extrabold tracking-tight">
            <span class="text-[#161616]">{{ t('footer.heading_get_in') }}</span>
            <span class="text-[#0873b9]">{{ t('footer.heading_touch') }}</span>
          </h2>
        </div>
      </ScrollReveal> -->

      <div class="mx-auto grid max-w-[1238px] grid-cols-1 gap-8 sm:gap-12 sm:grid-cols-2 lg:grid-cols-4">
        
        <!-- Column 1: Brand -->
        <ScrollReveal direction="up" :stagger-index="0" :stagger-delay="100" :delay="100" duration="0.8s">
          <div class="flex flex-col gap-3">
            <h3 class="text-[32px] sm:text-[38px] lg:text-[46px] font-black leading-none tracking-tight text-white">
              CBHI<span class="text-[#0873b9]">.et</span>
            </h3>
            <p class="mt-1 text-[13px] font-bold tracking-widest text-[#a1a1aa] uppercase">
              {{ t('footer.brand_subtitle') }}
            </p>
            <p class="mt-1 text-[15px] font-medium text-[#d4d4d8]">
              {{ t('footer.brand_reimagined') }}
              <span class="text-[#0873b9]">{{ t('footer.brand_digital') }}</span>
            </p>
          </div>
        </ScrollReveal>

        <!-- Column 2: Organization -->
        <ScrollReveal direction="up" :stagger-index="1" :stagger-delay="100" :delay="100" duration="0.8s">
          <div class="flex flex-col gap-6">
            <div class="flex flex-col gap-3">
              <h3 class="text-[19px] font-bold leading-none tracking-wide text-white">
                {{ t('footer.org_title') }}
              </h3>
              <!-- Custom Line Separator -->
              <div class="relative h-[1px] w-full max-w-[190px] bg-white/20">
                <div class="absolute left-0 top-1/2 h-[3px] w-8 -translate-y-1/2 rounded-full bg-[#0873b9]"></div>
              </div>
            </div>
            
            <ul class="flex flex-col gap-4">
              <li>
                <a href="#" class="link-chevron-shift group flex items-center gap-2.5 text-[15px] font-normal text-[#a1a1aa] transition-colors hover:text-white">
                  <ChevronRight class="h-4 w-4 text-[#a1a1aa] transition-all group-hover:text-white" />
                  {{ t('footer.links.about') }}
                </a>
              </li>
              <li>
                <a href="#" class="link-chevron-shift group flex items-center gap-2.5 text-[15px] font-normal text-[#a1a1aa] transition-colors hover:text-white">
                  <ChevronRight class="h-4 w-4 text-[#a1a1aa] transition-all group-hover:text-white" />
                  {{ t('footer.links.moh') }}
                </a>
              </li>
              <li>
                <a href="#" class="link-chevron-shift group flex items-center gap-2.5 text-[15px] font-normal text-[#a1a1aa] transition-colors hover:text-white">
                  <ChevronRight class="h-4 w-4 text-[#a1a1aa] transition-all group-hover:text-white" />
                  {{ t('footer.links.reports') }}
                </a>
              </li>
              <li>
                <a href="#" class="link-chevron-shift group flex items-center gap-2.5 text-[15px] font-normal text-[#a1a1aa] transition-colors hover:text-white">
                  <ChevronRight class="h-4 w-4 text-[#a1a1aa] transition-all group-hover:text-white" />
                  {{ t('footer.links.enroll') }}
                </a>
              </li>
            </ul>
          </div>
        </ScrollReveal>

        <!-- Column 3: Contact -->
        <ScrollReveal direction="up" :stagger-index="2" :stagger-delay="100" :delay="100" duration="0.8s">
          <div class="flex flex-col gap-6">
            <div class="flex flex-col gap-3">
              <h3 class="text-[19px] font-bold leading-none tracking-wide text-white">
                {{ t('footer.contact_title') }}
              </h3>
              <!-- Custom Line Separator -->
              <div class="relative h-[1px] w-full max-w-[190px] bg-white/20">
                <div class="absolute left-0 top-1/2 h-[3px] w-8 -translate-y-1/2 rounded-full bg-[#0873b9]"></div>
              </div>
            </div>
            
            <ul class="flex flex-col gap-5">
              <li class="flex items-center gap-3">
                <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-white text-[#0873b9] transition-transform duration-300 hover:scale-110">
                  <MapPin class="h-[14px] w-[14px]" stroke-width="2.5" />
                </div>
                <span class="text-[15px] font-normal text-[#a1a1aa]">
                  {{ t('footer.address') }}
                </span>
              </li>
              <li class="flex items-center gap-3">
                <div class="flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-white text-[#0873b9] transition-transform duration-300 hover:scale-110">
                  <Mail class="h-[14px] w-[14px]" stroke-width="2.5" />
                </div>
                <span class="text-[15px] font-normal text-[#a1a1aa]">
                  {{ t('footer.email') }}
                </span>
              </li>
            </ul>
          </div>
        </ScrollReveal>

        <!-- Column 4: Follow Us -->
        <ScrollReveal direction="up" :stagger-index="3" :stagger-delay="100" :delay="100" duration="0.8s">
          <div class="flex flex-col gap-6">
            <div class="flex flex-col gap-3">
              <h3 class="text-[19px] font-bold leading-none tracking-wide text-white">
                {{ t('footer.follow_title') }}
              </h3>
              <!-- Custom Line Separator -->
              <div class="relative h-[1px] w-full max-w-[190px] bg-white/20">
                <div class="absolute left-0 top-1/2 h-[3px] w-8 -translate-y-1/2 rounded-full bg-[#0873b9]"></div>
              </div>
            </div>
            
            <!-- Social Icons -->
            <div class="flex items-center gap-4 text-[#a1a1aa]">
              <a href="#" aria-label="Facebook" class="social-icon-hover transition-colors hover:text-white">
                <Facebook class="h-[22px] w-[22px]" />
              </a>
              <span class="text-white/20 text-lg leading-none">|</span>
              <a href="#" aria-label="X (Twitter)" class="social-icon-hover transition-colors hover:text-white">
                <svg viewBox="0 0 24 24" aria-hidden="true" class="h-5 w-5 fill-current"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 22.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"></path></svg>
              </a>
              <span class="text-white/20 text-lg leading-none">|</span>
              <a href="#" aria-label="LinkedIn" class="social-icon-hover transition-colors hover:text-white">
                <Linkedin class="h-[22px] w-[22px]" />
              </a>
              <span class="text-white/20 text-lg leading-none">|</span>
              <a href="#" aria-label="Instagram" class="social-icon-hover transition-colors hover:text-white">
                <Instagram class="h-[22px] w-[22px]" />
              </a>
            </div>

            <!-- Integration Buttons -->
            <div class="mt-1 flex flex-col gap-3">
              <div class="flex h-[28px] w-[140px] cursor-pointer items-center justify-center rounded-md border border-white/20 bg-transparent transition-all duration-300 hover:bg-white/5 hover:border-white/40">
                <span class="text-[11px] font-medium text-[#a1a1aa]">{{ t('footer.fayda_badge') }}</span>
              </div>
              <div class="flex h-[28px] w-[140px] cursor-pointer items-center justify-center rounded-md border border-white/20 bg-transparent transition-all duration-300 hover:bg-white/5 hover:border-white/40">
                <span class="text-[11px] font-medium text-[#a1a1aa]">{{ t('footer.inasa_badge') }}</span>
              </div>
            </div>
          </div>
        </ScrollReveal>
      </div>

      <!-- Separator -->
      <div class="mx-auto mt-12 sm:mt-20 h-px max-w-[1238px] bg-white/10" />

      <!-- Bottom row -->
      <div class="mx-auto mt-5 sm:mt-6 flex max-w-[1238px] flex-col items-center justify-between gap-3 sm:gap-4 pb-4 sm:flex-row sm:gap-0">
        <p class="text-[12px] sm:text-[13px] font-medium tracking-wide text-[#71717a] text-center sm:text-left">
          {{ t('footer.copyright') }}
        </p>
        <p class="flex items-center gap-1.5 text-[12px] sm:text-[13px] font-medium text-[#71717a] flex-wrap justify-center sm:justify-start">
          {{ t('footer.powered_by') }} 
          <span class="flex items-center tracking-wide">
            <img
              class="h-7 w-auto object-contain"
              alt="Medco"
              src="/medco update.png"
            />
          </span>
          <span class="text-[#a1a1aa]">{{ t('footer.medco_tech') }}</span>
        </p>
      </div>
    </div>
  </footer>
</template>
