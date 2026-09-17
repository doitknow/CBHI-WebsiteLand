<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Save, RefreshCw, CheckCircle2, AlertCircle, Users, Globe, Building2, BarChart3, Sparkles } from "lucide-vue-next";
import cmsService, { ImpactMetricItem } from "../../../services/cmsService";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const metrics = ref<ImpactMetricItem[]>([
  { metricKey: "citizens", valueNumber: 45, suffix: "M+", labelText: "CITIZENS COVERED", subtext: "Across all regions", iconName: "Users", displayOrder: 1 },
  { metricKey: "woredas", valueNumber: 980, suffix: "+", labelText: "WOREDAS REACHED", subtext: "Nationwide integration", iconName: "Globe", displayOrder: 2 },
  { metricKey: "facilities", valueNumber: 3500, suffix: "+", labelText: "PARTNER FACILITIES", subtext: "Hospitals & clinics", iconName: "Building2", displayOrder: 3 },
]);

const heroSettings = ref({
  "hero.welcome": "Welcome To CBHI",
  "hero.title_ethiopian": "Ethiopian",
  "hero.title_health_insurance": "Health Insurance",
  "hero.subtitle": "Empowering Ethiopians with affordable and accessible healthcare. A community solidarity program ensuring secure, digital medical coverage for every household.",
});

const loadData = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const [fetchedMetrics, fetchedSettings] = await Promise.all([
      cmsService.getMetrics(),
      cmsService.getSettings(),
    ]);

    if (fetchedMetrics && fetchedMetrics.length > 0) {
      metrics.value = fetchedMetrics;
    }

    if (fetchedSettings) {
      Object.keys(heroSettings.value).forEach((key) => {
        if (fetchedSettings[key]) {
          heroSettings.value[key as keyof typeof heroSettings.value] = fetchedSettings[key];
        }
      });
    }
  } catch (err) {
    console.warn("Using offline / fallback metrics values");
  } finally {
    isLoading.value = false;
  }
};

const handleSaveAll = async () => {
  isSaving.value = true;
  message.value = null;

  try {
    await cmsService.updateSettings(heroSettings.value);
    for (const metric of metrics.value) {
      await cmsService.saveMetric(metric);
    }

    message.value = {
      text: "Hero headlines and national impact metrics updated successfully in PostgreSQL!",
      type: "success",
    };
  } catch (err: any) {
    message.value = {
      text: err?.message || "Failed to save changes to the database.",
      type: "error",
    };
  } finally {
    isSaving.value = false;
  }
};

onMounted(() => {
  loadData();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] text-white flex items-center justify-center shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] shrink-0">
          <BarChart3 class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Hero Headlines &amp; Impact Metrics
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)]">
              Public Display
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Configure landing page headline slogans, welcome banners, and real-time nationwide coverage metrics
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadData"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh from PostgreSQL"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>
        <button
          @click="handleSaveAll"
          :disabled="isSaving"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] disabled:opacity-60 transition-all"
        >
          <Save class="w-4 h-4" />
          <span>{{ isSaving ? "Saving to DB..." : "Save All Changes" }}</span>
        </button>
      </div>
    </div>

    <!-- Alert Notification -->
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
        class="p-4 rounded-2xl flex items-center gap-3 text-xs font-bold shadow-sm"
        :class="message.type === 'success' ? 'bg-emerald-50 text-emerald-800 border border-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300 dark:border-emerald-800' : 'bg-red-50 text-red-800 border border-red-200 dark:bg-red-950/40 dark:text-red-300 dark:border-red-800'"
      >
        <CheckCircle2 v-if="message.type === 'success'" class="w-4 h-4 shrink-0 text-emerald-600" />
        <AlertCircle v-else class="w-4 h-4 shrink-0 text-red-600" />
        <span>{{ message.text }}</span>
      </div>
    </transition>

    <!-- Section 1: Hero Headlines -->
    <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
      <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06] flex items-center justify-between">
        <div>
          <h2 class="text-sm font-black text-slate-900 dark:text-white uppercase tracking-wider">
            Hero Section Text Content
          </h2>
          <p class="text-xs text-slate-400 mt-0.5">Customizable titles displayed inside the landing page hero area</p>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div>
          <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Welcome Pill Badge</label>
          <input
            v-model="heroSettings['hero.welcome']"
            type="text"
            class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
          />
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Headline Part 1 (Primary)</label>
          <input
            v-model="heroSettings['hero.title_ethiopian']"
            type="text"
            class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
          />
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Headline Part 2 (Gradient Accent)</label>
          <input
            v-model="heroSettings['hero.title_health_insurance']"
            type="text"
            class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
          />
        </div>
      </div>

      <div>
        <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Hero Subtitle Paragraph</label>
        <textarea
          v-model="heroSettings['hero.subtitle']"
          rows="3"
          class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none leading-relaxed"
        ></textarea>
      </div>
    </div>

    <!-- Section 2: 3 National Impact Metrics -->
    <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
      <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06]">
        <h2 class="text-sm font-black text-slate-900 dark:text-white uppercase tracking-wider">
          National Impact Counters (3 Live Metric Cards)
        </h2>
        <p class="text-xs text-slate-400 mt-0.5">Values formatted as high-impact summary statistics on the portal</p>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div
          v-for="(metric, idx) in metrics"
          :key="metric.metricKey || idx"
          class="p-5 rounded-2xl bg-slate-50/60 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-4 shadow-sm"
        >
          <!-- Live Preview Tile -->
          <div class="p-5 rounded-2xl bg-white dark:bg-[#02182b] border border-slate-200/80 dark:border-white/[0.06] shadow-sm text-center">
            <span class="text-4xl font-black text-[var(--color-primary)] tracking-tight">
              {{ metric.valueNumber }}{{ metric.suffix }}
            </span>
            <p class="text-[11px] font-black uppercase tracking-wider text-slate-600 dark:text-slate-300 mt-1">
              {{ metric.labelText }}
            </p>
            <p class="text-xs text-slate-400 mt-0.5">
              {{ metric.subtext }}
            </p>
          </div>

          <!-- Input Fields -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Number</label>
              <input
                v-model.number="metric.valueNumber"
                type="number"
                class="w-full px-3 py-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-bold"
              />
            </div>
            <div>
              <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Suffix</label>
              <input
                v-model="metric.suffix"
                type="text"
                class="w-full px-3 py-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-bold"
              />
            </div>
          </div>

          <div>
            <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Label Text</label>
            <input
              v-model="metric.labelText"
              type="text"
              class="w-full px-3 py-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-bold"
            />
          </div>

          <div>
            <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Subtext / Region</label>
            <input
              v-model="metric.subtext"
              type="text"
              class="w-full px-3 py-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
