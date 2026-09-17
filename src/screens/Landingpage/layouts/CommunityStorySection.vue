<script setup lang="ts">
import { ref } from "vue";
import { Heart, Quote, Check, X, Users, MapPin, Sparkles } from "lucide-vue-next";

const stories = [
  {
    name: "Almaz Tefera & Family",
    location: "Ambo Woreda, Oromia",
    householdSize: "Family of 6",
    quote: "When my youngest child required emergency appendectomy surgery, the hospital bill would have forced us to sell our farmland. With CBHI, everything was covered 100% with zero out-of-pocket costs.",
    savedAmount: "48,000 ETB Saved",
    tag: "Emergency Surgery"
  },
  {
    name: "Mulugeta Assefa",
    location: "Dessie, Amhara",
    householdSize: "Elderly Care",
    quote: "For diabetic management and hypertension medicines, monthly pharmacy expenses were unbearable. CBHI allows me to refill prescriptions every month at Kenema Pharmacy with dignity.",
    savedAmount: "1,200 ETB / Month",
    tag: "Chronic Care"
  },
  {
    name: "Fatuma Kedir",
    location: "Hawassa, Sidama",
    householdSize: "Maternal Care",
    quote: "Safe hospital delivery and neonatal checkups for my twins were completely stress-free. The midwives and doctors treated us with royal care.",
    savedAmount: "25,000 ETB Saved",
    tag: "Maternal Health"
  }
];
</script>

<template>
  <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-14 space-y-12">
    <!-- Header -->
    <div class="text-center max-w-3xl mx-auto space-y-3">
      <!-- <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-emerald-50 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-300 text-xs font-bold uppercase tracking-wider border border-emerald-200 dark:border-emerald-800">
        <Heart class="w-3.5 h-3.5 fill-emerald-500 text-emerald-500" />
        <span>Community Voices</span>
      </div> -->
      <h2 class="text-2xl sm:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">
        Real Stories of Healthcare Protection
      </h2>
      <p class="text-sm sm:text-base text-slate-600 dark:text-slate-300">
        How Community-Based Health Insurance protects millions of Ethiopian households against catastrophic out-of-pocket medical debts.
      </p>
    </div>

    <!-- Stories Grid -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        v-for="story in stories"
        :key="story.name"
        class="relative rounded-3xl p-6 bg-white dark:bg-[#03243d] border border-slate-100 dark:border-white/10 shadow-lg hover:shadow-xl transition-all duration-300 flex flex-col justify-between"
      >
        <Quote class="w-8 h-8 text-[var(--color-primary)]/20 mb-3" />
        <p class="text-xs sm:text-sm text-slate-600 dark:text-slate-300 italic leading-relaxed mb-6">
          "{{ story.quote }}"
        </p>

        <div class="pt-4 border-t border-slate-100 dark:border-white/10 space-y-2">
          <div class="flex items-center justify-between">
            <h4 class="text-sm font-bold text-slate-900 dark:text-white">{{ story.name }}</h4>
            <span class="px-2 py-0.5 rounded-full text-[10px] font-bold bg-emerald-100 dark:bg-emerald-900/40 text-emerald-800 dark:text-emerald-300">
              {{ story.savedAmount }}
            </span>
          </div>
          <div class="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400">
            <span class="flex items-center gap-1">
              <MapPin class="w-3.5 h-3.5 text-[var(--color-primary)]" />
              <span>{{ story.location }}</span>
            </span>
            <span>{{ story.tag }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Before vs. With CBHI Impact Matrix -->
    <div class="rounded-3xl p-6 sm:p-8 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10">
      <h3 class="text-lg font-bold text-center text-slate-900 dark:text-white mb-6">
        The Difference CBHI Makes for Ethiopian Families
      </h3>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Without CBHI -->
        <div class="p-5 rounded-2xl bg-white dark:bg-[#03243d] border border-rose-200 dark:border-rose-900/40 space-y-3">
          <div class="flex items-center gap-2 text-rose-600 dark:text-rose-400 font-bold text-sm">
            <X class="w-5 h-5 rounded-full bg-rose-100 dark:bg-rose-950 p-0.5" />
            <span>Without CBHI (Out of Pocket)</span>
          </div>
          <ul class="space-y-2 text-xs text-slate-600 dark:text-slate-300">
            <li class="flex items-start gap-2">
              <span class="text-rose-500 font-bold">•</span>
              <span>Families delay medical care due to fear of unpredictable expenses.</span>
            </li>
            <li class="flex items-start gap-2">
              <span class="text-rose-500 font-bold">•</span>
              <span>Emergency hospitalizations often cause distress asset and livestock sales.</span>
            </li>
            <li class="flex items-start gap-2">
              <span class="text-rose-500 font-bold">•</span>
              <span>Chronic diseases lead to ongoing debt cycles and missed treatments.</span>
            </li>
          </ul>
        </div>

        <!-- With CBHI -->
        <div class="p-5 rounded-2xl bg-white dark:bg-[#03243d] border border-emerald-200 dark:border-emerald-900/40 space-y-3">
          <div class="flex items-center gap-2 text-emerald-600 dark:text-emerald-400 font-bold text-sm">
            <Check class="w-5 h-5 rounded-full bg-emerald-100 dark:bg-emerald-950 p-0.5" />
            <span>With CBHI National Protection</span>
          </div>
          <ul class="space-y-2 text-xs text-slate-600 dark:text-slate-300">
            <li class="flex items-start gap-2">
              <span class="text-emerald-500 font-bold">•</span>
              <span>Early diagnosis and checkups at local Kebele clinics at zero point-of-care cost.</span>
            </li>
            <li class="flex items-start gap-2">
              <span class="text-emerald-500 font-bold">•</span>
              <span>Full catastrophic surgery and referral hospitalization covered by the pool.</span>
            </li>
            <li class="flex items-start gap-2">
              <span class="text-emerald-500 font-bold">•</span>
              <span>Household savings and peace of mind secured through community solidarity.</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </section>
</template>
