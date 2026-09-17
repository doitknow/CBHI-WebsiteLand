<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import {
  Layout,
  LayoutGrid,
  Check,
  Save,
  RotateCcw,
  Eye,
  ExternalLink,
  Sparkles,
  Sliders,
  CheckCircle2,
  AlertCircle,
  Layers,
  Smartphone,
  Monitor,
  Heart,
  Users,
  ShieldCheck,
  ArrowRight
} from "lucide-vue-next";
import cmsService from "../../../services/cmsService";
import {
  LAYOUT_PRESETS,
  DEFAULT_LAYOUT,
  getActiveLayoutSettings,
  layoutSettingsToDictionary,
  LayoutSettings,
  LayoutPreset
} from "../../../services/layout";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const activeLayout = ref<LayoutSettings>({
  activePresetId: DEFAULT_LAYOUT.id,
  heroStyle: DEFAULT_LAYOUT.heroStyle,
  headerStyle: DEFAULT_LAYOUT.headerStyle,
  containerWidth: DEFAULT_LAYOUT.containerWidth,
  cardStyle: DEFAULT_LAYOUT.cardStyle,
  showPartners: DEFAULT_LAYOUT.showPartners,
  showQuickServices: DEFAULT_LAYOUT.showQuickServices,
  showPhilosophy: DEFAULT_LAYOUT.showPhilosophy,
  showMetrics: DEFAULT_LAYOUT.showMetrics,
  showBenefits: DEFAULT_LAYOUT.showBenefits,
  showEnrollment: DEFAULT_LAYOUT.showEnrollment,
  showStories: DEFAULT_LAYOUT.showStories,
});

const loadLayoutSettings = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const settings = await cmsService.getSettings();
    if (settings) {
      activeLayout.value = getActiveLayoutSettings(settings);
    }
  } catch {
    // Graceful fallback to default
    activeLayout.value = getActiveLayoutSettings();
  } finally {
    isLoading.value = false;
  }
};

const selectPreset = (preset: LayoutPreset) => {
  activeLayout.value = {
    activePresetId: preset.id,
    heroStyle: preset.heroStyle,
    headerStyle: preset.headerStyle,
    containerWidth: preset.containerWidth,
    cardStyle: preset.cardStyle,
    showPartners: preset.showPartners,
    showQuickServices: preset.showQuickServices,
    showPhilosophy: preset.showPhilosophy,
    showMetrics: preset.showMetrics,
    showBenefits: preset.showBenefits,
    showEnrollment: preset.showEnrollment,
    showStories: preset.showStories,
  };
};

const handleSaveLayout = async () => {
  isSaving.value = true;
  message.value = null;
  try {
    const dict = layoutSettingsToDictionary(activeLayout.value);
    await cmsService.updateSettings(dict);
    localStorage.setItem("cbhi_layout_settings", JSON.stringify(dict));
    message.value = {
      text: `Layout "${currentPreset.value?.name || activeLayout.value.activePresetId}" successfully saved to PostgreSQL! Public website updated.`,
      type: "success",
    };
  } catch (err: any) {
    message.value = {
      text: err?.message || "Failed to persist layout settings to database.",
      type: "error",
    };
  } finally {
    isSaving.value = false;
  }
};

const currentPreset = computed(() => {
  return LAYOUT_PRESETS.find((p) => p.id === activeLayout.value.activePresetId);
});

onMounted(() => {
  loadLayoutSettings();
});
</script>

<template>
  <div class="space-y-8 max-w-7xl mx-auto pb-16">
    <!-- Top Header Banner -->
    <div
      class="relative overflow-hidden rounded-3xl p-6 sm:p-8 bg-gradient-to-r from-slate-900 via-indigo-950 to-slate-900 text-white shadow-xl border border-white/10"
    >
      <div
        class="absolute -right-10 -top-10 w-72 h-72 rounded-full bg-indigo-500/20 blur-3xl pointer-events-none"
      ></div>
      <div class="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <div class="space-y-2">
          <div
            class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-indigo-500/20 text-indigo-300 text-xs font-semibold border border-indigo-400/30"
          >
            <Layers class="w-3.5 h-3.5" />
            <span>Architecture & Experience Engine</span>
          </div>
          <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight">Website Layout Studio</h1>
          <p class="text-sm text-slate-300 max-w-xl">
            Choose from different structural website layouts, customize hero presentations, toggle
            citizen services hubs, and preview the public portal experience in real time.
          </p>
        </div>

        <div class="flex flex-wrap items-center gap-3">
          <a
            href="/"
            target="_blank"
            class="inline-flex items-center gap-2 px-4 py-2.5 rounded-xl bg-white/10 hover:bg-white/15 text-white font-medium text-xs border border-white/20 transition-all shadow-sm"
          >
            <Eye class="w-4 h-4 text-emerald-300" />
            <span>View Public Site</span>
            <ExternalLink class="w-3.5 h-3.5 text-slate-400" />
          </a>

          <button
            type="button"
            @click="handleSaveLayout"
            :disabled="isSaving || isLoading"
            class="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-indigo-500 to-blue-600 hover:from-indigo-600 hover:to-blue-700 text-white font-bold text-xs shadow-lg shadow-indigo-500/25 transition-all hover:scale-[1.02] active:scale-[0.98] disabled:opacity-50"
          >
            <Save class="w-4 h-4" />
            <span>{{ isSaving ? 'Saving Layout...' : 'Save & Publish Layout' }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Notification Toast -->
    <transition
      enter-active-class="transition-all duration-300 ease-out"
      enter-from-class="opacity-0 -translate-y-2"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition-all duration-200 ease-in"
      leave-from-class="opacity-100 translate-y-0"
      leave-to-class="opacity-0 -translate-y-2"
    >
      <div
        v-if="message"
        :class="message.type === 'success' ? 'bg-emerald-500/10 border-emerald-500/30 text-emerald-700 dark:text-emerald-300' : 'bg-rose-500/10 border-rose-500/30 text-rose-700 dark:text-rose-300'"
        class="flex items-center gap-3 p-4 rounded-2xl border text-sm shadow-sm"
      >
        <CheckCircle2 v-if="message.type === 'success'" class="w-5 h-5 shrink-0 text-emerald-500" />
        <AlertCircle v-else class="w-5 h-5 shrink-0 text-rose-500" />
        <span class="font-medium">{{ message.text }}</span>
      </div>
    </transition>

    <!-- Main Grid: Layout Presets & Live Interactive Preview -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
      <!-- Left Column: Preset Cards (7 cols) -->
      <div class="lg:col-span-7 space-y-6">
        <div class="flex items-center justify-between">
          <div class="space-y-1">
            <h2 class="text-lg font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <LayoutGrid class="w-5 h-5 text-indigo-500" />
              <span>Available Layout Presets</span>
            </h2>
            <p class="text-xs text-slate-500 dark:text-slate-400">
              Select an architecture to transform the structure and flow of the public portal.
            </p>
          </div>
          <span class="text-xs font-semibold px-2.5 py-1 rounded-full bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-300">
            4 Layouts Available
          </span>
        </div>

        <!-- Presets Selection Cards -->
        <div class="space-y-4">
          <div
            v-for="preset in LAYOUT_PRESETS"
            :key="preset.id"
            @click="selectPreset(preset)"
            :class="activeLayout.activePresetId === preset.id ? 'border-indigo-500 ring-2 ring-indigo-500/20 bg-indigo-50/20 dark:bg-indigo-950/20 shadow-lg' : 'border-slate-200 dark:border-white/10 bg-white dark:bg-slate-900/60 hover:border-indigo-300 dark:hover:border-indigo-700 shadow-sm'"
            class="group relative rounded-2xl p-5 border transition-all duration-200 cursor-pointer overflow-hidden"
          >
            <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div class="space-y-1.5 flex-1">
                <div class="flex items-center gap-2 flex-wrap">
                  <span
                    class="px-2 py-0.5 rounded-md text-[10px] font-extrabold uppercase tracking-wide bg-indigo-100 dark:bg-indigo-900/50 text-indigo-700 dark:text-indigo-300"
                  >
                    {{ preset.badge }}
                  </span>
                  <h3 class="text-base font-bold text-slate-900 dark:text-white group-hover:text-indigo-600 dark:group-hover:text-indigo-400 transition-colors">
                    {{ preset.name }}
                  </h3>
                  <span
                    v-if="activeLayout.activePresetId === preset.id"
                    class="inline-flex items-center gap-1 text-[11px] font-bold text-emerald-600 dark:text-emerald-400 bg-emerald-100/70 dark:bg-emerald-950/50 px-2 py-0.5 rounded-full"
                  >
                    <Check class="w-3 h-3" />
                    <span>Active Selected</span>
                  </span>
                </div>
                <p class="text-xs text-slate-500 dark:text-slate-400 leading-relaxed">
                  {{ preset.description }}
                </p>
              </div>

              <!-- Wireframe Visual Thumbnail -->
              <div
                class="w-32 h-20 rounded-xl p-1.5 shrink-0 bg-slate-100 dark:bg-slate-800 border border-slate-200 dark:border-white/10 flex flex-col justify-between"
              >
                <!-- Mini wireframe representations -->
                <div class="h-2 w-full rounded bg-indigo-400/40"></div>
                <div class="flex gap-1 h-7">
                  <div
                    v-if="preset.heroStyle === 'slider'"
                    class="w-full rounded bg-blue-500/50 flex items-center justify-center text-[7px] text-white font-mono"
                  >
                    SLIDER
                  </div>
                  <template v-else-if="preset.heroStyle === 'split'">
                    <div class="w-1/2 rounded bg-indigo-500/40"></div>
                    <div class="w-1/2 rounded bg-emerald-500/40 flex items-center justify-center text-[7px] text-white font-mono">
                      CARD
                    </div>
                  </template>
                  <template v-else>
                    <div class="w-full rounded bg-amber-500/40 flex items-center justify-center text-[7px] text-white font-mono">
                      HUB
                    </div>
                  </template>
                </div>
                <div class="flex gap-1 h-2.5">
                  <div class="flex-1 rounded bg-slate-400/30"></div>
                  <div class="flex-1 rounded bg-slate-400/30"></div>
                  <div class="flex-1 rounded bg-slate-400/30"></div>
                </div>
              </div>
            </div>

            <!-- Features bullets -->
            <div class="mt-4 pt-3 border-t border-slate-100 dark:border-white/5 grid grid-cols-1 sm:grid-cols-2 gap-2 text-[11px] text-slate-600 dark:text-slate-300">
              <div v-for="feat in preset.keyFeatures" :key="feat" class="flex items-center gap-1.5">
                <CheckCircle2 class="w-3.5 h-3.5 text-indigo-500 shrink-0" />
                <span class="truncate">{{ feat }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Detailed Layout Options -->
        <div class="rounded-3xl p-6 bg-white dark:bg-slate-900 border border-slate-200 dark:border-white/10 shadow-sm space-y-6">
          <div class="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-white/10">
            <h3 class="text-sm font-bold text-slate-900 dark:text-white flex items-center gap-2">
              <Sliders class="w-4 h-4 text-indigo-500" />
              <span>Section Visibility & Display Controls</span>
            </h3>
            <span class="text-xs text-slate-400">Fine-tune active components</span>
          </div>

          <!-- Section Toggles -->
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <!-- Partners Marquee -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Partners & Banks Marquee</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">EthSwitch, Telebirr & Banks ticker</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showPartners" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Quick Services Hub -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Citizen Quick Services Hub</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">Eligibility & hospital search tools</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showQuickServices" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Philosophy & Rights -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Philosophy & Health Rights</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">Constitutional healthcare rights grid</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showPhilosophy" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Impact Metrics -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">National Impact Metrics</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">45M+ citizens covered banner</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showMetrics" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Coverage Benefits -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Coverage Benefits Section</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">Inpatient, outpatient, maternal</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showBenefits" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Enrollment Steps -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Enrollment Milestone Guide</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">3-Step Woreda & Fayda process</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showEnrollment" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>

            <!-- Community Stories -->
            <label class="flex items-center justify-between p-3.5 rounded-xl border border-slate-100 dark:border-white/5 bg-slate-50/50 dark:bg-slate-800/40 cursor-pointer hover:border-indigo-200 dark:hover:border-indigo-700 transition-all sm:col-span-2">
              <div class="space-y-0.5">
                <span class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Beneficiary Stories & Impact Matrix</span>
                <span class="text-[11px] text-slate-500 dark:text-slate-400 block">Personal testimonials and before/after CBHI comparisons</span>
              </div>
              <input type="checkbox" v-model="activeLayout.showStories" class="w-4 h-4 accent-indigo-600 rounded cursor-pointer" />
            </label>
          </div>

          <!-- Hero Presentation Style -->
          <div class="pt-4 border-t border-slate-100 dark:border-white/10 space-y-3">
            <label class="text-xs font-bold text-slate-800 dark:text-slate-200 block">Hero Presentation Style</label>
            <div class="grid grid-cols-3 gap-3">
              <button
                type="button"
                @click="activeLayout.heroStyle = 'slider'"
                :class="activeLayout.heroStyle === 'slider' ? 'bg-indigo-600 text-white font-bold' : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300'"
                class="py-2.5 px-3 rounded-xl text-xs transition-all text-center"
              >
                Multi-Slide Slider
              </button>
              <button
                type="button"
                @click="activeLayout.heroStyle = 'split'"
                :class="activeLayout.heroStyle === 'split' ? 'bg-indigo-600 text-white font-bold' : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300'"
                class="py-2.5 px-3 rounded-xl text-xs transition-all text-center"
              >
                Split + Digital Card
              </button>
              <button
                type="button"
                @click="activeLayout.heroStyle = 'services-hero'"
                :class="activeLayout.heroStyle === 'services-hero' ? 'bg-indigo-600 text-white font-bold' : 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300'"
                class="py-2.5 px-3 rounded-xl text-xs transition-all text-center"
              >
                Action Hub Centric
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Live Miniature Preview Panel (5 cols) -->
      <div class="lg:col-span-5 sticky top-6 space-y-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center gap-2">
            <Monitor class="w-4 h-4 text-indigo-500" />
            <h3 class="text-sm font-bold text-slate-900 dark:text-white">Live Layout Wireframe Preview</h3>
          </div>
          <span class="text-[11px] font-mono text-slate-400">Real-Time Simulation</span>
        </div>

        <!-- Interactive Miniature Device Mockup -->
        <div class="rounded-3xl p-3 bg-slate-900 border-4 border-slate-800 shadow-2xl space-y-2 overflow-hidden">
          <!-- Browser address bar simulation -->
          <div class="flex items-center gap-1.5 px-2 py-1 rounded-lg bg-slate-800/80 text-[10px] text-slate-400 font-mono">
            <span class="w-2 h-2 rounded-full bg-rose-500"></span>
            <span class="w-2 h-2 rounded-full bg-amber-500"></span>
            <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
            <span class="ml-2 text-slate-300">cbhi.moh.gov.et</span>
          </div>

          <!-- Miniature Render Content -->
          <div class="rounded-xl bg-slate-950 p-3 space-y-2.5 text-white max-h-[550px] overflow-y-auto custom-scrollbar">
            <!-- Mini Header Bar -->
            <div class="p-2 rounded-lg bg-gradient-to-r from-blue-700 to-indigo-700 flex items-center justify-between text-[9px]">
              <div class="flex items-center gap-1">
                <span class="font-bold tracking-wider">CBHI ETHIOPIA</span>
              </div>
              <div class="flex gap-2 text-[8px] opacity-80">
                <span>About</span>
                <span>Benefits</span>
                <span>Services</span>
                <span>News</span>
              </div>
            </div>

            <!-- Mini Hero Section -->
            <div
              v-if="activeLayout.heroStyle === 'slider'"
              class="p-3.5 rounded-lg bg-gradient-to-b from-blue-900 to-slate-900 border border-blue-800/40 space-y-1.5"
            >
              <div class="w-16 h-2 rounded bg-emerald-400/70"></div>
              <div class="w-3/4 h-3.5 rounded bg-white/90"></div>
              <div class="w-1/2 h-2 rounded bg-slate-400/60"></div>
              <div class="flex gap-1.5 pt-1">
                <div class="w-12 h-3.5 rounded bg-[var(--color-primary)]"></div>
                <div class="w-10 h-3.5 rounded bg-white/20"></div>
              </div>
            </div>

            <!-- Mini Split Hero -->
            <div
              v-else-if="activeLayout.heroStyle === 'split'"
              class="p-3 rounded-lg bg-gradient-to-br from-slate-900 to-blue-950 border border-indigo-500/30 grid grid-cols-12 gap-2"
            >
              <div class="col-span-7 space-y-1">
                <div class="w-12 h-1.5 rounded bg-emerald-400/80"></div>
                <div class="w-full h-3 rounded bg-white/90"></div>
                <div class="w-4/5 h-2 rounded bg-slate-400/50"></div>
                <div class="w-10 h-3 rounded bg-indigo-500 mt-1"></div>
              </div>
              <div class="col-span-5 p-1.5 rounded-lg bg-slate-800 border border-white/20 space-y-1">
                <div class="flex justify-between items-center text-[7px] text-emerald-400">
                  <span>DIGITAL CARD</span>
                  <span>ACTIVE</span>
                </div>
                <div class="w-full h-2 rounded bg-white/40"></div>
                <div class="w-2/3 h-1.5 rounded bg-slate-400/40"></div>
              </div>
            </div>

            <!-- Mini Services Hero -->
            <div
              v-else
              class="p-3 rounded-lg bg-slate-900 border border-amber-500/30 space-y-2"
            >
              <div class="text-center space-y-1">
                <div class="w-24 h-2 mx-auto rounded bg-amber-400/80"></div>
                <div class="w-3/4 h-3 mx-auto rounded bg-white/90"></div>
              </div>
              <div class="grid grid-cols-4 gap-1">
                <div class="h-6 rounded bg-slate-800 border border-slate-700 text-[6px] flex items-center justify-center text-slate-300">Eligibility</div>
                <div class="h-6 rounded bg-slate-800 border border-slate-700 text-[6px] flex items-center justify-center text-slate-300">Hospitals</div>
                <div class="h-6 rounded bg-slate-800 border border-slate-700 text-[6px] flex items-center justify-center text-slate-300">Payments</div>
                <div class="h-6 rounded bg-slate-800 border border-slate-700 text-[6px] flex items-center justify-center text-rose-300">8888</div>
              </div>
            </div>

            <!-- Mini Partners Marquee -->
            <div
              v-if="activeLayout.showPartners"
              class="p-1.5 rounded bg-slate-900/80 border border-slate-800 flex items-center justify-between text-[7px] text-slate-400"
            >
              <span>PARTNERS:</span>
              <div class="flex gap-1.5">
                <span class="px-1 bg-slate-800 rounded">EthSwitch</span>
                <span class="px-1 bg-slate-800 rounded">Telebirr</span>
                <span class="px-1 bg-slate-800 rounded">CBE</span>
              </div>
            </div>

            <!-- Mini Quick Services -->
            <div
              v-if="activeLayout.showQuickServices"
              class="p-2 rounded-lg bg-blue-950/40 border border-blue-500/20 space-y-1"
            >
              <span class="text-[8px] font-bold text-blue-300 block">Citizen Action Hub</span>
              <div class="grid grid-cols-3 gap-1">
                <div class="h-4 rounded bg-slate-800"></div>
                <div class="h-4 rounded bg-slate-800"></div>
                <div class="h-4 rounded bg-slate-800"></div>
              </div>
            </div>

            <!-- Mini Philosophy -->
            <div
              v-if="activeLayout.showPhilosophy"
              class="p-2 rounded-lg bg-slate-900 border border-slate-800 space-y-1"
            >
              <div class="w-16 h-1.5 rounded bg-slate-400/50"></div>
              <div class="grid grid-cols-3 gap-1">
                <div class="h-5 rounded bg-slate-800"></div>
                <div class="h-5 rounded bg-slate-800"></div>
                <div class="h-5 rounded bg-slate-800"></div>
              </div>
            </div>

            <!-- Mini Metrics -->
            <div
              v-if="activeLayout.showMetrics"
              class="p-2 rounded-lg bg-emerald-950/30 border border-emerald-500/20 grid grid-cols-3 gap-1 text-center"
            >
              <div class="text-[8px] font-bold text-emerald-400">45M+</div>
              <div class="text-[8px] font-bold text-emerald-400">980+</div>
              <div class="text-[8px] font-bold text-emerald-400">3500+</div>
            </div>

            <!-- Mini Benefits -->
            <div
              v-if="activeLayout.showBenefits"
              class="p-2 rounded-lg bg-slate-900 border border-slate-800 space-y-1"
            >
              <div class="w-20 h-1.5 rounded bg-slate-400/50"></div>
              <div class="grid grid-cols-3 gap-1">
                <div class="h-7 rounded bg-slate-800/80"></div>
                <div class="h-7 rounded bg-slate-800/80"></div>
                <div class="h-7 rounded bg-slate-800/80"></div>
              </div>
            </div>

            <!-- Mini Stories -->
            <div
              v-if="activeLayout.showStories"
              class="p-2 rounded-lg bg-indigo-950/40 border border-indigo-500/20 space-y-1"
            >
              <span class="text-[8px] font-bold text-indigo-300 block">Community Stories & Impact</span>
              <div class="grid grid-cols-2 gap-1">
                <div class="h-6 rounded bg-slate-800/80"></div>
                <div class="h-6 rounded bg-slate-800/80"></div>
              </div>
            </div>

            <!-- Mini Footer -->
            <div class="p-2 rounded-lg bg-slate-900 text-center text-[7px] text-slate-500 border border-slate-800">
              Federal Democratic Republic of Ethiopia • Ministry of Health
            </div>
          </div>
        </div>

        <!-- Summary Status Box -->
        <div class="rounded-2xl p-4 bg-slate-100 dark:bg-slate-900/80 border border-slate-200 dark:border-white/10 space-y-2 text-xs">
          <div class="flex justify-between items-center">
            <span class="text-slate-500 dark:text-slate-400">Current Selected:</span>
            <span class="font-bold text-indigo-600 dark:text-indigo-400">{{ currentPreset?.name }}</span>
          </div>
          <div class="flex justify-between items-center">
            <span class="text-slate-500 dark:text-slate-400">Hero Architecture:</span>
            <span class="font-semibold text-slate-800 dark:text-slate-200 capitalize">{{ activeLayout.heroStyle }}</span>
          </div>
          <div class="flex justify-between items-center">
            <span class="text-slate-500 dark:text-slate-400">Active Components:</span>
            <span class="font-semibold text-emerald-600 dark:text-emerald-400">
              {{
                [
                  activeLayout.showPartners,
                  activeLayout.showQuickServices,
                  activeLayout.showPhilosophy,
                  activeLayout.showMetrics,
                  activeLayout.showBenefits,
                  activeLayout.showEnrollment,
                  activeLayout.showStories
                ].filter(Boolean).length
              }} of 7 Enabled
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
