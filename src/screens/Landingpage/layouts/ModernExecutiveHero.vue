<script setup lang="ts">
import { ref, computed } from "vue";
import {
  ShieldCheck,
  ArrowRight,
  CreditCard,
  CheckCircle2,
  Users,
  Activity,
  Sparkles,
  PhoneCall,
  QrCode,
  Building2
} from "lucide-vue-next";

defineProps<{
  dynamicSettings?: Record<string, string>;
}>();

const familyMembers = ref(4);
const woredaType = ref<"rural" | "urban">("urban");

// Annual contribution formula based on Ethiopian CBHI guidelines
const calculatedContribution = computed(() => {
  const baseRate = woredaType.value === "urban" ? 500 : 350;
  const perMemberAddon = 40;
  return baseRate + (familyMembers.value * perMemberAddon);
});

const isCardActive = ref(true);
</script>

<template>
  <div class="relative overflow-hidden bg-gradient-to-b from-slate-900 via-slate-900 to-[#02182b] text-white pt-10 pb-20 px-4 sm:px-6 lg:px-12 border-b border-white/10">
    <!-- Ambient glowing backgrounds -->
    <div class="absolute -top-40 -left-40 w-96 h-96 rounded-full bg-[var(--color-primary)]/20 blur-[120px] pointer-events-none"></div>
    <div class="absolute top-1/2 -right-40 w-96 h-96 rounded-full bg-emerald-500/15 blur-[120px] pointer-events-none"></div>

    <div class="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-12 items-center relative z-10">
      <!-- Left Column: Typography & CTAs -->
      <div class="lg:col-span-7 space-y-6 text-left">
        <!-- National Authority Badge -->
        <div class="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full bg-white/10 border border-white/15 backdrop-blur-md text-xs font-semibold tracking-wide text-emerald-300">
          <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
          <span>Federal Ministry of Health • Official CBHI Portal</span>
        </div>

        <h1 class="text-3xl sm:text-5xl lg:text-6xl font-extrabold tracking-tight leading-[1.15] text-white">
          Universal Healthcare for Every
          <span class="text-transparent bg-clip-text bg-gradient-to-r from-emerald-300 via-sky-300 to-blue-400">
            Ethiopian Household
          </span>
        </h1>

        <p class="text-base sm:text-lg text-slate-300 max-w-2xl leading-relaxed">
          Community-Based Health Insurance guarantees comprehensive inpatient, outpatient, and maternal medical care across 3,500+ public and partner health centers nationwide.
        </p>

        <!-- Feature pills -->
        <div class="flex flex-wrap gap-3 pt-2">
          <div class="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-xs font-medium text-slate-200">
            <CheckCircle2 class="w-4 h-4 text-emerald-400 shrink-0" />
            <span>100% Outpatient & Inpatient</span>
          </div>
          <div class="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-xs font-medium text-slate-200">
            <CheckCircle2 class="w-4 h-4 text-emerald-400 shrink-0" />
            <span>Fayda Digital ID Linked</span>
          </div>
          <div class="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-xs font-medium text-slate-200">
            <CheckCircle2 class="w-4 h-4 text-emerald-400 shrink-0" />
            <span>Instant Telebirr & Bank Payments</span>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="flex flex-wrap items-center gap-4 pt-4">
          <a
            href="#section-process"
            class="inline-flex items-center gap-2 px-6 py-3.5 rounded-xl bg-[var(--color-primary)] hover:opacity-95 text-white font-bold text-sm shadow-xl shadow-[var(--color-primary)]/30 transition-all hover:-translate-y-0.5"
          >
            <span>Enroll Online / Woreda</span>
            <ArrowRight class="w-4 h-4" />
          </a>
          <a
            href="#section-benefits"
            class="inline-flex items-center gap-2 px-5 py-3.5 rounded-xl bg-white/10 hover:bg-white/15 text-white font-semibold text-sm border border-white/20 transition-all"
          >
            <span>Explore Benefits</span>
          </a>
          <div class="flex items-center gap-2 text-xs text-slate-400 pl-2">
            <PhoneCall class="w-4 h-4 text-amber-400" />
            <span>24/7 Hotline: <strong class="text-white">8888</strong></span>
          </div>
        </div>
      </div>

      <!-- Right Column: Interactive Digital CBHI Smart Card & Simulator -->
      <div class="lg:col-span-5 space-y-6">
        <!-- 3D Digital Card -->
        <div class="relative group perspective-1000">
          <div class="relative rounded-3xl p-6 sm:p-7 bg-gradient-to-br from-slate-800/90 via-blue-950/80 to-slate-900/95 border border-white/20 shadow-2xl backdrop-blur-xl overflow-hidden transition-all duration-500 hover:shadow-blue-500/20 hover:border-white/30">
            <!-- Card background graphic accent -->
            <div class="absolute -right-16 -top-16 w-56 h-56 rounded-full bg-[var(--color-primary)]/30 blur-2xl pointer-events-none"></div>

            <!-- Card Header -->
            <div class="flex items-center justify-between pb-6 border-b border-white/10 relative z-10">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-xl bg-white/10 flex items-center justify-center border border-white/20">
                  <CreditCard class="w-5 h-5 text-emerald-400" />
                </div>
                <div>
                  <h2 class="text-xs font-black tracking-widest text-slate-300 uppercase">Federal Republic of Ethiopia</h2>
                  <p class="text-sm font-bold text-white tracking-wide">CBHI Digital Smart Card</p>
                </div>
              </div>

              <!-- Status Badge -->
              <button
                type="button"
                @click="isCardActive = !isCardActive"
                class="px-2.5 py-1 rounded-full text-[11px] font-bold transition-all flex items-center gap-1.5 cursor-pointer"
                :class="isCardActive ? 'bg-emerald-500/20 text-emerald-300 border border-emerald-500/40' : 'bg-amber-500/20 text-amber-300 border border-amber-500/40'"
                title="Click to toggle status simulation"
              >
                <span class="w-1.5 h-1.5 rounded-full" :class="isCardActive ? 'bg-emerald-400 animate-pulse' : 'bg-amber-400'"></span>
                <span>{{ isCardActive ? 'ACTIVE COVERAGE' : 'RENEWAL DUE' }}</span>
              </button>
            </div>

            <!-- Card Body -->
            <div class="py-5 space-y-3 relative z-10">
              <div class="flex justify-between items-center text-xs text-slate-400">
                <span>Beneficiary Holder</span>
                <span class="text-emerald-400 font-mono text-[11px]">FAYDA ID LINKED</span>
              </div>
              <p class="text-lg sm:text-xl font-bold text-white tracking-wider font-mono">
                ABEBE KEBEDE HAILE
              </p>

              <div class="grid grid-cols-2 gap-4 pt-2 text-xs">
                <div>
                  <span class="text-slate-400 block text-[10px] uppercase">Woreda / Kebele</span>
                  <span class="text-slate-200 font-semibold">Bole, Woreda 03</span>
                </div>
                <div>
                  <span class="text-slate-400 block text-[10px] uppercase">Household Size</span>
                  <span class="text-slate-200 font-semibold">5 Dependents</span>
                </div>
                <div>
                  <span class="text-slate-400 block text-[10px] uppercase">Card Number</span>
                  <span class="font-mono text-slate-200 font-semibold tracking-wider">ET-9842-8714-CBHI</span>
                </div>
                <div>
                  <span class="text-slate-400 block text-[10px] uppercase">Valid Thru</span>
                  <span class="text-slate-200 font-semibold">Dec 2026</span>
                </div>
              </div>
            </div>

            <!-- Card Footer -->
            <div class="flex items-center justify-between pt-4 border-t border-white/10 relative z-10 text-[11px] text-slate-400">
              <div class="flex items-center gap-2">
                <QrCode class="w-5 h-5 text-slate-300" />
                <span>Scan at any Health Facility</span>
              </div>
              <span class="text-[10px] font-mono text-slate-500">EHIS v3.4</span>
            </div>
          </div>
        </div>

        <!-- Interactive Quick Premium Calculator -->
        <div class="rounded-2xl p-5 bg-white/5 border border-white/10 backdrop-blur-md space-y-3">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <Sparkles class="w-4 h-4 text-emerald-400" />
              <h3 class="text-xs font-bold uppercase tracking-wider text-slate-200">Household Annual Premium Estimator</h3>
            </div>
            <span class="text-[11px] text-slate-400">Subsidized Tier</span>
          </div>

          <div class="grid grid-cols-2 gap-3 pt-1">
            <!-- Family Size Slider -->
            <div>
              <label class="text-[11px] text-slate-300 block mb-1">
                Household Members: <strong class="text-emerald-400">{{ familyMembers }}</strong>
              </label>
              <input
                type="range"
                min="1"
                max="10"
                v-model.number="familyMembers"
                class="w-full accent-[var(--color-primary)] cursor-pointer"
              />
            </div>

            <!-- Area Selector -->
            <div>
              <label class="text-[11px] text-slate-300 block mb-1">Residence Area</label>
              <div class="flex gap-1.5">
                <button
                  type="button"
                  @click="woredaType = 'urban'"
                  :class="woredaType === 'urban' ? 'bg-[var(--color-primary)] text-white' : 'bg-white/10 text-slate-300'"
                  class="flex-1 py-1 text-[11px] font-semibold rounded-md transition-all"
                >
                  Urban
                </button>
                <button
                  type="button"
                  @click="woredaType = 'rural'"
                  :class="woredaType === 'rural' ? 'bg-[var(--color-primary)] text-white' : 'bg-white/10 text-slate-300'"
                  class="flex-1 py-1 text-[11px] font-semibold rounded-md transition-all"
                >
                  Rural
                </button>
              </div>
            </div>
          </div>

          <!-- Calculated Output -->
          <div class="flex items-center justify-between pt-2 border-t border-white/10 text-xs">
            <span class="text-slate-300">Estimated Annual Contribution:</span>
            <div class="text-right">
              <span class="text-base font-extrabold text-emerald-400 font-mono">{{ calculatedContribution }} ETB</span>
              <span class="text-[10px] text-slate-400 block">/ entire year for all {{ familyMembers }} members</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
