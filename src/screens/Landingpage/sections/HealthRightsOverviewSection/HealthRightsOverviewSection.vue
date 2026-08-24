<script setup lang="ts">
import { ref } from "vue";
import { 
  Scale, 
  HeartHandshake, 
  ShieldCheck, 
  Activity, 
  Sparkles, 
  MapPin,
  CheckCircle2,
  Building2
} from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import Card from "../../../../components/ui/Card.vue";
import CardContent from "../../../../components/ui/CardContent.vue";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";

const { t } = useI18n();

const activeHub = ref<string | null>("addis");

const healthPrinciples = [
  {
    translationKeyTitle: "health_rights.principles.equitable.title",
    translationKeyDesc: "health_rights.principles.equitable.description",
    color: "#0873b9",
    icon: Scale,
  },
  {
    translationKeyTitle: "health_rights.principles.solidarity.title",
    translationKeyDesc: "health_rights.principles.solidarity.description",
    color: "#0873b9",
    icon: HeartHandshake,
  },
];

const regionalHubs = [
  {
    id: "addis",
    name: "Addis Ababa",
    region: "Central National Hub",
    stat: "100% Digitally Integrated",
    top: "46%",
    left: "47%",
    isPrimary: true,
  },
  {
    id: "north",
    name: "Northern Zone",
    region: "Amhara & Tigray Network",
    stat: "650+ Woreda Health Posts",
    top: "24%",
    left: "42%",
    isPrimary: false,
  },
  {
    id: "east",
    name: "Eastern Zone",
    region: "Dire Dawa & Somali Region",
    stat: "420+ Health Facilities",
    top: "45%",
    left: "68%",
    isPrimary: false,
  },
  {
    id: "south",
    name: "Southern Zone",
    region: "Sidama & South Regional Hub",
    stat: "580+ Active Centers",
    top: "70%",
    left: "44%",
    isPrimary: false,
  },
  {
    id: "west",
    name: "Western Zone",
    region: "Oromia & Gambella Network",
    stat: "490+ Community Clinics",
    top: "52%",
    left: "24%",
    isPrimary: false,
  },
];
</script>

<template>
  <section
    aria-labelledby="health-rights-heading"
    class="relative w-full overflow-hidden bg-white dark:bg-[#021E31] py-10 sm:py-14 md:py-16 [font-family:'Inter',Helvetica]"
  >
    <!-- Background subtle ambient lighting -->
    <div
      aria-hidden="true"
      class="pointer-events-none absolute -left-40 top-1/4 h-[500px] w-[500px] rounded-full bg-[#0873b9]/5 blur-[120px] dark:bg-[#0873b9]/10"
    />
    <div
      aria-hidden="true"
      class="pointer-events-none absolute -right-40 bottom-10 h-[500px] w-[500px] rounded-full bg-emerald-500/5 blur-[120px] dark:bg-emerald-500/10"
    />

    <div
      class="mx-auto grid max-w-[1240px] grid-cols-1 items-center gap-8 sm:gap-10 lg:gap-14 px-4 xs:px-6 sm:px-8 lg:grid-cols-[minmax(0,580px)_minmax(0,1fr)] lg:px-10 xl:px-6"
    >
      <!-- Left text content -->
      <div class="relative z-10 flex flex-col items-start">
        <!-- "Health is Everyone's Right" styled heading -->
        <ScrollReveal direction="left" duration="0.9s">
          <div class="flex flex-col">
            <h2
              id="health-rights-heading"
              class="text-[28px] xs:text-[32px] sm:text-[40px] md:text-[48px] lg:text-[58px] font-black leading-[1.08] tracking-tight text-[#161616] dark:text-white"
            >
              <span class="block">{{ t('health_rights.heading_health') }}</span>
              <span class="block">{{ t('health_rights.heading_is_everyone_right') }}<span class="text-[#0873b9]">{{ t('health_rights.heading_right') }}</span></span>
            </h2>
          </div>
        </ScrollReveal>

        <ScrollReveal direction="up" :delay="150" duration="0.8s">
          <p
            class="mt-4 sm:mt-5 max-w-[618px] text-[13.5px] sm:text-[14.5px] lg:text-[15.5px] font-normal leading-relaxed tracking-[0] text-[#71717a] dark:text-slate-300"
          >
            {{ t('health_rights.description') }}
          </p>
        </ScrollReveal>
        <div class="mt-8 sm:mt-10 lg:mt-12 grid w-full grid-cols-1 gap-6 sm:grid-cols-2 sm:gap-8 lg:gap-12">
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
                <div class="relative mb-3.5 flex h-[46px] items-center">
                  <span
                    aria-hidden="true"
                    class="absolute left-3 top-2 h-[34px] w-[34px] rounded-full bg-[#0873b9]/20 dark:bg-[#0873b9]/30 group-hover:scale-125 transition-transform duration-300"
                  />
                  <div class="relative z-10 flex h-[38px] w-9 items-center justify-center text-[#0873b9] dark:text-[#0873b9]">
                    <component :is="principle.icon" class="h-6 w-6" stroke-width="2.2" />
                  </div>
                </div>
                <h3 class="text-lg font-bold leading-snug tracking-tight text-[#161616] dark:text-white">
                  {{ t(principle.translationKeyTitle) }}
                </h3>
                <p class="mt-2 text-[14px] font-normal leading-relaxed text-[#71717a] dark:text-slate-400">
                  {{ t(principle.translationKeyDesc) }}
                </p>
              </CardContent>
            </Card>
          </ScrollReveal>
        </div>
      </div>

      <!-- Right map showcase with high-tech national healthcare coverage visual -->
      <ScrollReveal direction="right" duration="1s" :delay="200">
        <div class="relative mx-auto w-full max-w-[560px]">
          <!-- Main visual wrapper with responsive aspect ratio -->
          <div class="relative min-h-[280px] xs:min-h-[340px] sm:min-h-[420px] lg:min-h-[510px] w-full rounded-2xl sm:rounded-3xl p-3 sm:p-4 md:p-6 overflow-visible">
            

            <!-- Layer 2: Futuristic SVG Radar Grid & Health Coverage Matrix -->
            <svg
              viewBox="0 0 550 450"
              class="absolute inset-0 h-full w-full pointer-events-none select-none z-0"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <defs>
                <!-- Radial gradient for radar glow -->
                <radialGradient id="radarGlow" cx="50%" cy="50%" r="50%">
                  <stop offset="0%" stop-color="#0873b9" stop-opacity="0.3" />
                  <stop offset="60%" stop-color="#0873b9" stop-opacity="0.08" />
                  <stop offset="100%" stop-color="#0873b9" stop-opacity="0" />
                </radialGradient>

                <!-- Line gradient for primary telemetry paths -->
                <linearGradient id="pathGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" stop-color="#0873b9" stop-opacity="0.9" />
                  <stop offset="50%" stop-color="#0873b9" stop-opacity="0.6" />
                  <stop offset="100%" stop-color="#10b981" stop-opacity="0.9" />
                </linearGradient>

                <linearGradient id="pulseGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" stop-color="#0873b9" stop-opacity="0" />
                  <stop offset="50%" stop-color="#38bdf8" stop-opacity="0.9" />
                  <stop offset="100%" stop-color="#10b981" stop-opacity="0" />
                </linearGradient>
              </defs>

              <!-- Radar Concentric Range Rings Centered around Addis Ababa (258, 207) -->
              <circle cx="258" cy="207" r="60" stroke="#0873b9" stroke-opacity="0.25" stroke-width="1" stroke-dasharray="3 4" />
              <circle cx="258" cy="207" r="120" stroke="#0873b9" stroke-opacity="0.2" stroke-width="1" stroke-dasharray="4 6" />
              <circle cx="258" cy="207" r="185" stroke="#0873b9" stroke-opacity="0.15" stroke-width="1" />
              <circle cx="258" cy="207" r="235" stroke="#0873b9" stroke-opacity="0.08" stroke-width="1" stroke-dasharray="2 5" />

              <!-- Animated Expanding Radar Waves -->
              <circle cx="258" cy="207" r="30" stroke="#38bdf8" stroke-opacity="0.6" class="radar-ring" fill="url(#radarGlow)" />
              <circle cx="258" cy="207" r="30" stroke="#0873b9" stroke-opacity="0.5" class="radar-ring-2" fill="none" />
              <circle cx="258" cy="207" r="30" stroke="#10b981" stroke-opacity="0.4" class="radar-ring-3" fill="none" />

              <!-- Crosshair Grid Axes -->
              <line x1="40" y1="207" x2="480" y2="207" stroke="#0873b9" stroke-opacity="0.12" stroke-width="1" stroke-dasharray="4 8" />
              <line x1="258" y1="30" x2="258" y2="400" stroke="#0873b9" stroke-opacity="0.12" stroke-width="1" stroke-dasharray="4 8" />

              <!-- Health Network Curved Telemetry Arcs Connecting Ethiopian Regions -->
              <!-- Center (258,207) to North (231,108) -->
              <path d="M 258 207 L 231 108" stroke="url(#pathGradient)" stroke-width="2" class="data-flow-line" opacity="0.8" />
              <!-- Center (258,207) to East (374,202) -->
              <path d="M 258 207 L 374 202" stroke="url(#pathGradient)" stroke-width="2" class="data-flow-line" opacity="0.8" />
              <!-- Center (258,207) to South (242,315) -->
              <path d="M 258 207 L 242 315" stroke="url(#pathGradient)" stroke-width="2" class="data-flow-line" opacity="0.8" />
              <!-- Center (258,207) to West (132,234) -->
              <path d="M 258 207 L 132 234" stroke="url(#pathGradient)" stroke-width="2" class="data-flow-line" opacity="0.8" />

              <!-- Regional Interconnection Arcs -->
              <path d="M 231 108 Q 320 130 374 202" stroke="#0873b9" stroke-opacity="0.4" stroke-width="1.5" class="data-flow-line-reverse" fill="none" />
              <path d="M 374 202 Q 330 290 242 315" stroke="#10b981" stroke-opacity="0.35" stroke-width="1.5" class="data-flow-line" fill="none" />
              <path d="M 242 315 Q 170 300 132 234" stroke="#0873b9" stroke-opacity="0.4" stroke-width="1.5" class="data-flow-line-reverse" fill="none" />
              <path d="M 132 234 Q 160 150 231 108" stroke="#38bdf8" stroke-opacity="0.35" stroke-width="1.5" class="data-flow-line" fill="none" />

              <!-- Live EKG / Heartbeat Telemetry Line across bottom -->
              <path
                d="M 30 415 L 120 415 L 135 415 L 145 390 L 155 438 L 165 398 L 175 422 L 185 415 L 290 415 L 300 388 L 310 440 L 320 398 L 330 422 L 340 415 L 520 415"
                stroke="url(#pulseGradient)"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="ekg-line"
                fill="none"
              />
            </svg>

            <!-- Layer 3: Ethiopia Map Image with 3D Depth Shadow -->
            <div class="relative z-10 flex items-center justify-center w-full h-full py-4">
              <img
                class="relative h-[220px] xs:h-[280px] sm:h-[340px] lg:h-[420px] w-auto max-w-full object-contain filter drop-shadow-[0_12px_28px_rgba(8,115,185,0.22)] dark:drop-shadow-[0_16px_36px_rgba(8,115,185,0.35)] transition-transform duration-700 hover:scale-[1.01]"
                alt="Map of Ethiopia - CBHI National Coverage Network"
                src="/map-Photoroom.png"
              />
            </div>

            <!-- Layer 4: Interactive Regional Healthcare Hub Beacons -->
            <div
              v-for="hub in regionalHubs"
              :key="hub.id"
              class="absolute z-20 -translate-x-1/2 -translate-y-1/2 cursor-pointer group"
              :style="{ top: hub.top, left: hub.left }"
              @mouseenter="activeHub = hub.id"
            >
              <!-- Outer glowing pulse ring -->
              <span
                class="absolute -inset-2.5 rounded-full opacity-75"
                :class="[
                  hub.isPrimary 
                    ? 'bg-[#0873b9] animate-ping' 
                    : 'bg-emerald-400 group-hover:animate-ping'
                ]"
              />

              <!-- Core marker point -->
              <div
                class="relative flex items-center justify-center rounded-full border-2 transition-all duration-300 shadow-md"
                :class="[
                  hub.isPrimary
                    ? 'h-6 w-6 bg-gradient-to-tr from-[#0873b9] to-[#0873b9] border-white text-white scale-110'
                    : 'h-4 w-4 bg-emerald-500 border-white dark:border-slate-900 group-hover:scale-125'
                ]"
              >
                <span v-if="hub.isPrimary" class="h-2 w-2 rounded-full bg-white animate-pulse" />
                <span v-else class="h-1.5 w-1.5 rounded-full bg-white" />
              </div>

              <!-- Interactive Hover Info Card / Tooltip -->
              <div
                class="absolute left-1/2 -translate-x-1/2 bottom-full mb-3 w-max max-w-[180px] rounded-xl border border-slate-200/80 dark:border-white/10 bg-white/95 dark:bg-[#042033]/95 p-2.5 shadow-xl backdrop-blur-md transition-all duration-300 pointer-events-none z-30 flex flex-col"
                :class="[
                  activeHub === hub.id ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-2 scale-95 group-hover:opacity-100 group-hover:translate-y-0 group-hover:scale-100'
                ]"
              >
                <!-- Triangle Pointer -->
                <div class="absolute -bottom-[5px] left-1/2 -translate-x-1/2 h-2.5 w-2.5 rotate-45 border-b border-r border-slate-200/80 dark:border-white/10 bg-white/95 dark:bg-[#042033]/95"></div>
                
                <div class="relative z-10 flex items-center gap-2 border-b border-slate-100 dark:border-white/5 pb-1.5 mb-1.5">
                  <span class="relative flex h-2 w-2 shrink-0">
                    <span class="absolute inline-flex h-full w-full animate-ping rounded-full opacity-75" :class="hub.isPrimary ? 'bg-[#0873b9]' : 'bg-emerald-400'"></span>
                    <span class="relative inline-flex h-2 w-2 rounded-full" :class="hub.isPrimary ? 'bg-[#0873b9]' : 'bg-emerald-500'"></span>
                  </span>
                  <p class="text-[11px] font-bold text-slate-800 dark:text-white">{{ hub.name }}</p>
                </div>
                <div class="relative z-10 text-left">
                  <p class="text-[9px] font-bold uppercase tracking-wider text-[#0873b9] dark:text-[#0873b9]">{{ hub.region }}</p>
                  <span class="mt-0.5 block text-[10.5px] text-slate-600 dark:text-slate-300 font-medium whitespace-nowrap">{{ hub.stat }}</span>
                </div>
              </div>
            </div>

            <!-- Layer 5: Floating Glassmorphism Metric Badges to describe the content -->
            
            <!-- Top Right: Universal Access Metric -->
            <div
              class="animate-float absolute -top-2 sm:-top-3 -right-1 sm:-right-4 z-20 flex items-center gap-2 sm:gap-3 rounded-xl sm:rounded-2xl border border-white/90 dark:border-white/15 bg-white/90 dark:bg-[#062c45]/90 p-2 sm:p-2.5 md:px-3.5 md:py-2.5 shadow-[0_8px_30px_rgb(0,0,0,0.08)] backdrop-blur-md transition-all duration-300 hover:scale-105"
            >
              <div class="flex h-8 w-8 sm:h-9 sm:w-9 md:h-10 md:w-10 items-center justify-center rounded-lg sm:rounded-xl bg-gradient-to-br from-[#0873b9] to-[#0873b9] text-white shadow-md">
                <ShieldCheck class="h-5 w-5" />
              </div>
              <div>
                <div class="flex items-center gap-1.5">
                  <span class="relative flex h-2 w-2">
                    <span class="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400 opacity-75"></span>
                    <span class="relative inline-flex h-2 w-2 rounded-full bg-emerald-500"></span>
                  </span>
                  <span class="text-[9px] sm:text-[10px] font-bold uppercase tracking-wider text-emerald-600 dark:text-emerald-400">Universal Access</span>
                </div>
                <p class="text-[10px] sm:text-xs md:text-sm font-extrabold text-[#161616] dark:text-white">1,000+ Woredas Active</p>
              </div>
            </div>

            <!-- Bottom Left: Health Facilities Metric -->
            <div
              class="animate-float-delayed absolute -bottom-2 sm:-bottom-3 -left-1 sm:-left-4 z-20 flex items-center gap-2 sm:gap-3 rounded-xl sm:rounded-2xl border border-white/90 dark:border-white/15 bg-white/90 dark:bg-[#062c45]/90 p-2 sm:p-2.5 md:px-3.5 md:py-2.5 shadow-[0_8px_30px_rgb(0,0,0,0.08)] backdrop-blur-md transition-all duration-300 hover:scale-105"
            >
              <div class="flex h-8 w-8 sm:h-9 sm:w-9 md:h-10 md:w-10 items-center justify-center rounded-lg sm:rounded-xl bg-gradient-to-br from-emerald-500 to-teal-600 text-white shadow-md">
                <Activity class="h-5 w-5" />
              </div>
              <div>
                <span class="text-[9px] sm:text-[10px] md:text-[11px] font-semibold text-slate-500 dark:text-slate-400">Verified Network</span>
                <p class="text-[10px] sm:text-xs md:text-sm font-extrabold text-[#161616] dark:text-white">2,400+ Health Centers</p>
              </div>
            </div>

            <!-- Bottom Right: Solidarity Pill Badge -->
            <div
              class="animate-float absolute bottom-1 sm:bottom-4 right-0 sm:right-3 z-20 hidden sm:flex items-center gap-2 rounded-full border border-blue-200/70 dark:border-blue-500/20 bg-blue-50/90 dark:bg-[#083554]/90 px-2.5 sm:px-3 py-1 sm:py-1.5 shadow-sm backdrop-blur-md"
            >
              <HeartHandshake class="h-4 w-4 text-[#0873b9] dark:text-[#0873b9]" />
              <span class="text-[11px] font-bold text-[#0873b9] dark:text-[#0873b9]">100% Community Solidarity</span>
            </div>

          </div>
        </div>
      </ScrollReveal>
    </div>

    <!-- Bottom decorative circle -->
    <svg
      class="pointer-events-none absolute bottom-[-80px] left-[18px] h-[120px] w-[120px] opacity-40 animate-rotate-slow"
      viewBox="0 0 173 173"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <circle cx="86.5" cy="86.5" r="85" stroke="#0873b9" stroke-opacity="0.15" stroke-width="3"/>
    </svg>
  </section>
</template>

<style scoped>
/* Radar expanding ripple animations */
.radar-ring {
  transform-origin: 258px 207px;
  animation: radar-expand 4.2s cubic-bezier(0.2, 0.8, 0.4, 1) infinite;
}
.radar-ring-2 {
  transform-origin: 258px 207px;
  animation: radar-expand 4.2s cubic-bezier(0.2, 0.8, 0.4, 1) infinite 1.4s;
}
.radar-ring-3 {
  transform-origin: 258px 207px;
  animation: radar-expand 4.2s cubic-bezier(0.2, 0.8, 0.4, 1) infinite 2.8s;
}

@keyframes radar-expand {
  0% {
    r: 20px;
    opacity: 0.85;
    stroke-width: 2px;
  }
  50% {
    opacity: 0.45;
  }
  100% {
    r: 230px;
    opacity: 0;
    stroke-width: 0.7px;
  }
}

/* Flowing data telemetry lines */
.data-flow-line {
  stroke-dasharray: 6 8;
  animation: dash-flow 25s linear infinite;
}

.data-flow-line-reverse {
  stroke-dasharray: 6 8;
  animation: dash-flow-reverse 20s linear infinite;
}

@keyframes dash-flow {
  to {
    stroke-dashoffset: -240;
  }
}

@keyframes dash-flow-reverse {
  to {
    stroke-dashoffset: 240;
  }
}

/* Live EKG Pulse Line Animation */
.ekg-line {
  stroke-dasharray: 600;
  stroke-dashoffset: 600;
  animation: ekg-draw 4.5s linear infinite;
}

@keyframes ekg-draw {
  0% {
    stroke-dashoffset: 600;
    opacity: 0.2;
  }
  40% {
    opacity: 0.85;
  }
  80% {
    stroke-dashoffset: 0;
    opacity: 0.85;
  }
  100% {
    stroke-dashoffset: 0;
    opacity: 0.2;
  }
}
</style>

