<script setup lang="ts">
import { ref } from "vue";
import {
  Search,
  Building2,
  PhoneCall,
  CheckCircle2,
  FileText,
  CreditCard,
  MapPin,
  HelpCircle,
  ExternalLink,
  ChevronRight,
  ShieldAlert,
  Sparkles
} from "lucide-vue-next";

const activeTab = ref<"eligibility" | "facilities" | "payment" | "emergency">("eligibility");

// Mock interactive eligibility query state
const idInput = ref("");
const lookupResult = ref<{ found: boolean; name: string; woreda: string; status: string } | null>(null);

const handleCheckEligibility = () => {
  if (!idInput.value.trim()) return;
  // Demo interactive simulator
  lookupResult.value = {
    found: true,
    name: "Tadesse Mengistu",
    woreda: "Yeka Subcity, Woreda 04",
    status: "Active - Fully Subsidized"
  };
};

const facilitySearch = ref("");
const partnerFacilities = [
  { name: "Tikur Anbessa (Black Lion) Specialized Hospital", type: "Tertiary Referral", region: "Addis Ababa", phone: "+251 115 511 211" },
  { name: "St. Peter's Specialized Hospital", type: "Specialized Referral", region: "Addis Ababa", phone: "+251 112 757 676" },
  { name: "Yekatit 12 Hospital Medical College", type: "General Hospital", region: "Addis Ababa", phone: "+251 111 553 066" },
  { name: "Hawassa Comprehensive Specialized Hospital", type: "Referral Hospital", region: "Sidama", phone: "+251 462 206 000" },
  { name: "Adama General Hospital & Medical College", type: "General Hospital", region: "Oromia", phone: "+251 221 114 949" },
  { name: "Gondar University Comprehensive Specialized Hospital", type: "Teaching Hospital", region: "Amhara", phone: "+251 581 141 232" },
];
</script>

<template>
  <section class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
    <!-- Header -->
    <div class="text-center max-w-3xl mx-auto mb-10 space-y-3">
      <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-50 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300 text-xs font-bold uppercase tracking-wider border border-blue-200 dark:border-blue-800">
        <Sparkles class="w-3.5 h-3.5" />
        <span>Citizen Direct Access</span>
      </div>
      <h2 class="text-2xl sm:text-4xl font-extrabold text-slate-900 dark:text-white tracking-tight">
        Citizen Quick Services Hub
      </h2>
      <p class="text-sm sm:text-base text-slate-600 dark:text-slate-300">
        Instant self-service tools for Ethiopian households to verify coverage, locate health centers, and pay annual premiums.
      </p>
    </div>

    <!-- Interactive Hub Container -->
    <div class="rounded-3xl bg-white dark:bg-[#03243d] border border-slate-200 dark:border-white/10 shadow-xl overflow-hidden">
      <!-- Tabs Navigation -->
      <div class="grid grid-cols-2 sm:grid-cols-4 border-b border-slate-200 dark:border-white/10 bg-slate-50 dark:bg-[#02182b]">
        <button
          type="button"
          @click="activeTab = 'eligibility'"
          :class="activeTab === 'eligibility' ? 'bg-white dark:bg-[#03243d] text-[var(--color-primary)] font-bold border-b-2 border-[var(--color-primary)]' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          class="flex items-center justify-center gap-2 py-4 px-3 text-xs sm:text-sm transition-all text-center"
        >
          <CheckCircle2 class="w-4 h-4 shrink-0" />
          <span>Verify Eligibility</span>
        </button>

        <button
          type="button"
          @click="activeTab = 'facilities'"
          :class="activeTab === 'facilities' ? 'bg-white dark:bg-[#03243d] text-[var(--color-primary)] font-bold border-b-2 border-[var(--color-primary)]' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          class="flex items-center justify-center gap-2 py-4 px-3 text-xs sm:text-sm transition-all text-center"
        >
          <Building2 class="w-4 h-4 shrink-0" />
          <span>Find Health Facility</span>
        </button>

        <button
          type="button"
          @click="activeTab = 'payment'"
          :class="activeTab === 'payment' ? 'bg-white dark:bg-[#03243d] text-[var(--color-primary)] font-bold border-b-2 border-[var(--color-primary)]' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          class="flex items-center justify-center gap-2 py-4 px-3 text-xs sm:text-sm transition-all text-center"
        >
          <CreditCard class="w-4 h-4 shrink-0" />
          <span>Payment Channels</span>
        </button>

        <button
          type="button"
          @click="activeTab = 'emergency'"
          :class="activeTab === 'emergency' ? 'bg-white dark:bg-[#03243d] text-rose-600 dark:text-rose-400 font-bold border-b-2 border-rose-500' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          class="flex items-center justify-center gap-2 py-4 px-3 text-xs sm:text-sm transition-all text-center"
        >
          <ShieldAlert class="w-4 h-4 shrink-0" />
          <span>Emergency & Rights</span>
        </button>
      </div>

      <!-- Tab Content Area -->
      <div class="p-6 sm:p-8">
        <!-- Tab 1: Eligibility Check -->
        <div v-if="activeTab === 'eligibility'" class="max-w-2xl mx-auto space-y-6">
          <div class="space-y-2 text-center sm:text-left">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white">Verify Your Household Coverage Status</h3>
            <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400">
              Enter your 16-digit Fayda National ID number or Kebele CBHI membership card number to check active coverage.
            </p>
          </div>

          <form @submit.prevent="handleCheckEligibility" class="flex flex-col sm:flex-row gap-3">
            <input
              type="text"
              v-model="idInput"
              placeholder="e.g. FAYDA-9841-2093 or ET-CBHI-883"
              class="flex-1 px-4 py-3 rounded-xl border border-slate-200 dark:border-white/10 bg-slate-50 dark:bg-slate-900 text-slate-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] font-mono"
            />
            <button
              type="submit"
              class="px-6 py-3 rounded-xl bg-[var(--color-primary)] text-white font-bold text-sm shadow-md hover:opacity-95 transition-all flex items-center justify-center gap-2"
            >
              <Search class="w-4 h-4" />
              <span>Lookup Status</span>
            </button>
          </form>

          <!-- Result card -->
          <div v-if="lookupResult" class="p-4 rounded-2xl bg-emerald-50 dark:bg-emerald-950/30 border border-emerald-200 dark:border-emerald-800 space-y-2">
            <div class="flex items-center justify-between">
              <span class="text-xs font-bold text-emerald-800 dark:text-emerald-300">Coverage Verification Result</span>
              <span class="px-2 py-0.5 rounded-full text-[11px] font-bold bg-emerald-600 text-white">{{ lookupResult.status }}</span>
            </div>
            <p class="text-sm font-bold text-slate-800 dark:text-slate-100">{{ lookupResult.name }}</p>
            <p class="text-xs text-slate-600 dark:text-slate-300">Registered at: {{ lookupResult.woreda }}</p>
            <p class="text-[11px] text-emerald-700 dark:text-emerald-400 font-medium">
              ✓ Eligible for free services at all public clinics and contracted tertiary referral hospitals.
            </p>
          </div>
        </div>

        <!-- Tab 2: Health Facilities -->
        <div v-if="activeTab === 'facilities'" class="space-y-6">
          <div class="flex flex-col sm:flex-row items-center justify-between gap-4">
            <div>
              <h3 class="text-lg font-bold text-slate-900 dark:text-white">Contracted Public & Referral Facilities</h3>
              <p class="text-xs text-slate-500 dark:text-slate-400">Over 3,500 facilities accept CBHI cards across Ethiopia.</p>
            </div>
            <div class="relative w-full sm:w-72">
              <Search class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
              <input
                type="text"
                v-model="facilitySearch"
                placeholder="Search hospital or city..."
                class="w-full pl-9 pr-3 py-2 rounded-lg border border-slate-200 dark:border-white/10 bg-slate-50 dark:bg-slate-900 text-xs text-slate-900 dark:text-white"
              />
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div
              v-for="facility in partnerFacilities.filter(f => !facilitySearch || f.name.toLowerCase().includes(facilitySearch.toLowerCase()) || f.region.toLowerCase().includes(facilitySearch.toLowerCase()))"
              :key="facility.name"
              class="p-4 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-900/50 hover:border-blue-200 dark:hover:border-blue-700 transition-all space-y-2"
            >
              <div class="flex items-start justify-between">
                <span class="text-[10px] font-bold px-2 py-0.5 rounded-md bg-blue-100 dark:bg-blue-900/40 text-blue-800 dark:text-blue-300">
                  {{ facility.type }}
                </span>
                <span class="text-xs font-semibold text-slate-500 dark:text-slate-400">{{ facility.region }}</span>
              </div>
              <h4 class="text-xs sm:text-sm font-bold text-slate-800 dark:text-white">{{ facility.name }}</h4>
              <p class="text-xs text-slate-500 dark:text-slate-400 flex items-center gap-1.5">
                <PhoneCall class="w-3.5 h-3.5 text-emerald-500" />
                <span>{{ facility.phone }}</span>
              </p>
            </div>
          </div>
        </div>

        <!-- Tab 3: Payment Channels -->
        <div v-if="activeTab === 'payment'" class="max-w-3xl mx-auto space-y-6">
          <div class="text-center space-y-2">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white">Seamless Digital Payment Integrations</h3>
            <p class="text-xs text-slate-500 dark:text-slate-400">Pay your annual household premium without standing in long queues.</p>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div class="p-4 rounded-2xl bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-white/10 space-y-3 text-center">
              <div class="w-10 h-10 mx-auto rounded-xl bg-amber-500/10 flex items-center justify-center text-amber-500 font-bold">1</div>
              <h4 class="text-sm font-bold text-slate-800 dark:text-white">Telebirr SuperApp</h4>
              <p class="text-xs text-slate-500 dark:text-slate-400">Open Telebirr > Services > Government Payments > CBHI > Enter Woreda Code.</p>
            </div>

            <div class="p-4 rounded-2xl bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-white/10 space-y-3 text-center">
              <div class="w-10 h-10 mx-auto rounded-xl bg-blue-500/10 flex items-center justify-center text-blue-500 font-bold">2</div>
              <h4 class="text-sm font-bold text-slate-800 dark:text-white">Commercial Bank of Ethiopia</h4>
              <p class="text-xs text-slate-500 dark:text-slate-400">CBE Birr or direct bank branch deposit to your regional CBHI pooled account.</p>
            </div>

            <div class="p-4 rounded-2xl bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-white/10 space-y-3 text-center">
              <div class="w-10 h-10 mx-auto rounded-xl bg-emerald-500/10 flex items-center justify-center text-emerald-500 font-bold">3</div>
              <h4 class="text-sm font-bold text-slate-800 dark:text-white">Siinqee & Sidama Banks</h4>
              <p class="text-xs text-slate-500 dark:text-slate-400">Dedicated teller desks across all regional branch locations.</p>
            </div>
          </div>
        </div>

        <!-- Tab 4: Emergency Rights -->
        <div v-if="activeTab === 'emergency'" class="max-w-2xl mx-auto space-y-6 text-center sm:text-left">
          <div class="p-5 rounded-2xl bg-rose-50 dark:bg-rose-950/20 border border-rose-200 dark:border-rose-900 flex flex-col sm:flex-row items-center gap-4">
            <div class="w-12 h-12 rounded-2xl bg-rose-500 text-white flex items-center justify-center shrink-0">
              <PhoneCall class="w-6 h-6" />
            </div>
            <div class="space-y-1">
              <h4 class="text-sm font-bold text-rose-900 dark:text-rose-200">24/7 National Health Rights Hotline</h4>
              <p class="text-xs text-rose-700 dark:text-rose-300">
                If a contracted health center refuses services or demands unauthorized co-payments, call our free toll-free citizen rights desk immediately:
              </p>
              <p class="text-lg font-black text-rose-600 dark:text-rose-400 font-mono">Dial 8888 (Toll Free)</p>
            </div>
          </div>

          <div class="space-y-3">
            <h4 class="text-xs font-bold uppercase tracking-wider text-slate-500 dark:text-slate-400">Citizen Entitlements Under CBHI Law:</h4>
            <ul class="space-y-2 text-xs text-slate-600 dark:text-slate-300">
              <li class="flex items-center gap-2">
                <CheckCircle2 class="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Zero fees for outpatient consultations and essential diagnostic laboratory tests.</span>
              </li>
              <li class="flex items-center gap-2">
                <CheckCircle2 class="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Full inpatient accommodation, surgical care, and intensive care bed fees.</span>
              </li>
              <li class="flex items-center gap-2">
                <CheckCircle2 class="w-4 h-4 text-emerald-500 shrink-0" />
                <span>Essential medication dispensed directly through hospital or Kenema pharmacies.</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
