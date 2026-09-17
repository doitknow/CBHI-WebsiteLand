<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  Palette,
  Check,
  RotateCcw,
  Save,
  Sparkles,
  Eye,
  CheckCircle2,
  AlertCircle,
  RefreshCw,
  HeartPulse,
  ArrowRight,
  Sliders,
  Copy,
  CheckCheck,
  Globe
} from "lucide-vue-next";
import cmsService from "../../../services/cmsService";
import { THEME_PRESETS, applyTheme, ColorTheme } from "../../../services/theme";

interface ThemeState {
  primary: string;
  secondary: string;
  accent: string;
  headerStart: string;
  headerEnd: string;
}

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);
const copiedField = ref<string | null>(null);

const activeTheme = ref<ThemeState>({
  primary: "#0873b9",
  secondary: "#044e82",
  accent: "#22c55e",
  headerStart: "#3173cf",
  headerEnd: "#245497",
});

const selectedPresetId = ref<string>("cbhi-blue");

const loadThemeSettings = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const settings = await cmsService.getSettings();
    if (settings) {
      if (settings["theme.primary_color"]) activeTheme.value.primary = settings["theme.primary_color"];
      if (settings["theme.secondary_color"]) activeTheme.value.secondary = settings["theme.secondary_color"];
      if (settings["theme.accent_color"]) activeTheme.value.accent = settings["theme.accent_color"];
      if (settings["theme.header_gradient_start"]) activeTheme.value.headerStart = settings["theme.header_gradient_start"];
      if (settings["theme.header_gradient_end"]) activeTheme.value.headerEnd = settings["theme.header_gradient_end"];
      if (settings["theme.preset_id"]) selectedPresetId.value = settings["theme.preset_id"];
    }
  } catch (err) {
    console.warn("Could not load remote theme settings, using defaults");
  } finally {
    isLoading.value = false;
  }
};

const selectPreset = (preset: ColorTheme) => {
  selectedPresetId.value = preset.id;
  activeTheme.value = {
    primary: preset.primary,
    secondary: preset.secondary,
    accent: preset.accent,
    headerStart: preset.headerStart,
    headerEnd: preset.headerEnd,
  };
  applyTheme(activeTheme.value);
};

const handleCustomColorChange = () => {
  selectedPresetId.value = "custom";
  applyTheme(activeTheme.value);
};

const copyHex = (text: string, fieldName: string) => {
  navigator.clipboard.writeText(text);
  copiedField.value = fieldName;
  setTimeout(() => {
    copiedField.value = null;
  }, 1500);
};

const handleResetDefault = () => {
  const defaultPreset = THEME_PRESETS[0];
  selectPreset(defaultPreset);
  message.value = {
    text: "Reset to default Federal CBHI Blue theme. Click 'Save Brand Colors' to apply permanently.",
    type: "success",
  };
};

const handleSaveTheme = async () => {
  isSaving.value = true;
  message.value = null;

  const payload: Record<string, string> = {
    "theme.primary_color": activeTheme.value.primary,
    "theme.secondary_color": activeTheme.value.secondary,
    "theme.accent_color": activeTheme.value.accent,
    "theme.header_gradient_start": activeTheme.value.headerStart,
    "theme.header_gradient_end": activeTheme.value.headerEnd,
    "theme.preset_id": selectedPresetId.value,
  };

  try {
    await cmsService.updateSettings(payload);
    applyTheme(payload);
    message.value = {
      text: "Theme & brand colors successfully saved to PostgreSQL and applied across portal!",
      type: "success",
    };
  } catch (err: any) {
    message.value = {
      text: err?.message || "Failed to persist theme settings.",
      type: "error",
    };
  } finally {
    isSaving.value = false;
  }
};

onMounted(() => {
  loadThemeSettings();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header Section -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] text-white flex items-center justify-center shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] shrink-0">
          <Palette class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Color &amp; Theme Studio
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)]">
              Realtime Sync
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Configure primary palettes, header gradients, and accents dynamically applied across public portal &amp; admin views
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadThemeSettings"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh Theme"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>

        <button
          @click="handleResetDefault"
          class="px-4 py-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-700 dark:text-slate-200 font-bold text-xs flex items-center gap-2 transition-all shadow-sm"
        >
          <RotateCcw class="w-4 h-4" />
          <span>Reset Defaults</span>
        </button>

        <button
          @click="handleSaveTheme"
          :disabled="isSaving"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] disabled:opacity-60 transition-all"
        >
          <Save class="w-4 h-4" />
          <span>{{ isSaving ? "Saving..." : "Save Brand Colors" }}</span>
        </button>
      </div>
    </div>

    <!-- Alert Notification Banner -->
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

    <!-- Main Workspace Grid: Presets & Live Customization -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-7">
      <!-- Left Column: Palette Presets & Fine-Tune Pickers (2 cols) -->
      <div class="lg:col-span-2 space-y-7">
        <!-- 1. Curated Palette Presets -->
        <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
          <div class="flex items-center justify-between pb-3.5 border-b border-slate-100 dark:border-white/[0.06]">
            <div>
              <h2 class="text-sm font-black text-slate-900 dark:text-white flex items-center gap-2">
                <Sparkles class="w-4 h-4 text-amber-500" />
                Curated Theme Presets
              </h2>
              <p class="text-xs text-slate-400 mt-0.5">Select a pre-designed, accessible health insurance color scheme</p>
            </div>
            <span
              v-if="selectedPresetId !== 'custom'"
              class="px-2.5 py-1 rounded-full text-[10px] font-black bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)]"
            >
              Preset Active
            </span>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div
              v-for="preset in THEME_PRESETS"
              :key="preset.id"
              @click="selectPreset(preset)"
              class="p-4 sm:p-5 rounded-2xl border-2 transition-all cursor-pointer relative overflow-hidden group flex flex-col justify-between"
              :class="[
                selectedPresetId === preset.id
                  ? 'border-[var(--color-primary)] bg-[rgba(var(--color-primary-rgb),0.05)] shadow-md ring-2 ring-[var(--color-primary)]/20'
                  : 'border-slate-200/80 dark:border-white/[0.06] hover:border-slate-300 dark:hover:border-white/20 bg-slate-50/60 dark:bg-white/[0.02]'
              ]"
            >
              <div class="flex items-start justify-between">
                <div>
                  <h3 class="text-xs font-black text-slate-900 dark:text-white">{{ preset.name }}</h3>
                  <p class="text-[11px] text-slate-500 dark:text-slate-400 mt-1 leading-snug">{{ preset.description }}</p>
                </div>
                <div
                  v-if="selectedPresetId === preset.id"
                  class="w-6 h-6 rounded-full bg-[var(--color-primary)] text-white flex items-center justify-center shrink-0 shadow-sm"
                >
                  <Check class="w-3.5 h-3.5" />
                </div>
              </div>

              <!-- Swatches bar -->
              <div class="mt-4 flex items-center gap-1.5 pt-3.5 border-t border-slate-200/50 dark:border-white/[0.06]">
                <div class="w-7 h-7 rounded-lg shadow-inner ring-1 ring-black/5" :style="{ backgroundColor: preset.primary }" title="Primary Brand" />
                <div class="w-7 h-7 rounded-lg shadow-inner ring-1 ring-black/5" :style="{ backgroundColor: preset.secondary }" title="Secondary Tone" />
                <div class="w-7 h-7 rounded-lg shadow-inner ring-1 ring-black/5" :style="{ backgroundColor: preset.accent }" title="Accent Highlight" />
                <div
                  class="flex-1 h-7 rounded-lg shadow-inner ring-1 ring-black/5"
                  :style="{ background: `linear-gradient(90deg, ${preset.headerStart}, ${preset.headerEnd})` }"
                  title="Gradient Banner"
                />
              </div>
            </div>
          </div>
        </div>

        <!-- 2. Fine-Tune Custom Color Inputs -->
        <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
          <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06]">
            <h2 class="text-sm font-black text-slate-900 dark:text-white flex items-center gap-2">
              <Sliders class="w-4 h-4 text-[var(--color-primary)]" />
              Custom Color Calibration
            </h2>
            <p class="text-xs text-slate-400 mt-0.5">Click swatches to open native color pickers, or type direct HEX codes</p>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <!-- Primary Color -->
            <div class="p-4 rounded-2xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-2">
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300">
                Primary Brand Color
              </label>
              <div class="flex items-center gap-2.5">
                <input
                  type="color"
                  v-model="activeTheme.primary"
                  @input="handleCustomColorChange"
                  class="w-10 h-10 rounded-xl cursor-pointer border-0 bg-transparent p-0 shrink-0"
                />
                <input
                  type="text"
                  v-model="activeTheme.primary"
                  @input="handleCustomColorChange"
                  class="flex-1 px-3 py-2 bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs font-mono font-bold uppercase text-slate-800 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-inner"
                />
                <button
                  type="button"
                  @click="copyHex(activeTheme.primary, 'primary')"
                  class="p-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-slate-400 hover:text-slate-700 dark:hover:text-white transition-colors"
                  title="Copy Hex"
                >
                  <CheckCheck v-if="copiedField === 'primary'" class="w-3.5 h-3.5 text-emerald-500" />
                  <Copy v-else class="w-3.5 h-3.5" />
                </button>
              </div>
              <p class="text-[11px] text-slate-400">Buttons, active tabs, header elements, main badges</p>
            </div>

            <!-- Secondary Color -->
            <div class="p-4 rounded-2xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-2">
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300">
                Secondary Dark Tone
              </label>
              <div class="flex items-center gap-2.5">
                <input
                  type="color"
                  v-model="activeTheme.secondary"
                  @input="handleCustomColorChange"
                  class="w-10 h-10 rounded-xl cursor-pointer border-0 bg-transparent p-0 shrink-0"
                />
                <input
                  type="text"
                  v-model="activeTheme.secondary"
                  @input="handleCustomColorChange"
                  class="flex-1 px-3 py-2 bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs font-mono font-bold uppercase text-slate-800 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-inner"
                />
                <button
                  type="button"
                  @click="copyHex(activeTheme.secondary, 'secondary')"
                  class="p-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-slate-400 hover:text-slate-700 dark:hover:text-white transition-colors"
                  title="Copy Hex"
                >
                  <CheckCheck v-if="copiedField === 'secondary'" class="w-3.5 h-3.5 text-emerald-500" />
                  <Copy v-else class="w-3.5 h-3.5" />
                </button>
              </div>
              <p class="text-[11px] text-slate-400">Sidebar highlights, dark contrast surfaces, gradients</p>
            </div>

            <!-- Accent Color -->
            <div class="p-4 rounded-2xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-2">
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300">
                Highlight Accent Color
              </label>
              <div class="flex items-center gap-2.5">
                <input
                  type="color"
                  v-model="activeTheme.accent"
                  @input="handleCustomColorChange"
                  class="w-10 h-10 rounded-xl cursor-pointer border-0 bg-transparent p-0 shrink-0"
                />
                <input
                  type="text"
                  v-model="activeTheme.accent"
                  @input="handleCustomColorChange"
                  class="flex-1 px-3 py-2 bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs font-mono font-bold uppercase text-slate-800 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-inner"
                />
                <button
                  type="button"
                  @click="copyHex(activeTheme.accent, 'accent')"
                  class="p-2 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-slate-400 hover:text-slate-700 dark:hover:text-white transition-colors"
                  title="Copy Hex"
                >
                  <CheckCheck v-if="copiedField === 'accent'" class="w-3.5 h-3.5 text-emerald-500" />
                  <Copy v-else class="w-3.5 h-3.5" />
                </button>
              </div>
              <p class="text-[11px] text-slate-400">Verified status pills, counter highlights, metrics</p>
            </div>

            <!-- Header Gradient Start & End -->
            <div class="p-4 rounded-2xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-2">
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300">
                Top Header Gradient (Start / End)
              </label>
              <div class="flex items-center gap-2">
                <input
                  type="color"
                  v-model="activeTheme.headerStart"
                  @input="handleCustomColorChange"
                  class="w-9 h-9 rounded-xl cursor-pointer border-0 bg-transparent p-0 shrink-0"
                  title="Gradient Start"
                />
                <input
                  type="text"
                  v-model="activeTheme.headerStart"
                  @input="handleCustomColorChange"
                  class="w-20 px-2 py-2 bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-[11px] font-mono font-bold uppercase text-slate-800 dark:text-white shadow-inner"
                />
                <span class="text-xs text-slate-400 font-bold">→</span>
                <input
                  type="color"
                  v-model="activeTheme.headerEnd"
                  @input="handleCustomColorChange"
                  class="w-9 h-9 rounded-xl cursor-pointer border-0 bg-transparent p-0 shrink-0"
                  title="Gradient End"
                />
                <input
                  type="text"
                  v-model="activeTheme.headerEnd"
                  @input="handleCustomColorChange"
                  class="w-20 px-2 py-2 bg-white dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-[11px] font-mono font-bold uppercase text-slate-800 dark:text-white shadow-inner"
                />
              </div>
              <p class="text-[11px] text-slate-400">Top notification bar &amp; banner backgrounds</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Live Mockup Preview Window -->
      <div class="space-y-6">
        <div class="p-6 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5 sticky top-24">
          <!-- Mock Browser Header -->
          <div class="flex items-center justify-between pb-3.5 border-b border-slate-100 dark:border-white/[0.06]">
            <div class="flex items-center gap-2">
              <div class="w-2.5 h-2.5 rounded-full bg-red-400"></div>
              <div class="w-2.5 h-2.5 rounded-full bg-amber-400"></div>
              <div class="w-2.5 h-2.5 rounded-full bg-emerald-400"></div>
              <span class="text-[11px] font-mono text-slate-400 ml-2">cbhi.gov.et</span>
            </div>
            <span class="text-[10px] font-extrabold text-emerald-600 dark:text-emerald-400 bg-emerald-50 dark:bg-emerald-950/50 px-2 py-0.5 rounded-full">
              Live Preview
            </span>
          </div>

          <!-- Preview Component 1: Top Notification Banner -->
          <div class="space-y-2">
            <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-wider">Top Banner Bar</span>
            <div
              class="h-10 px-4 rounded-xl flex items-center justify-between text-white text-xs font-semibold shadow-sm transition-all duration-300"
              :style="{ background: `linear-gradient(90deg, ${activeTheme.headerStart}, ${activeTheme.headerEnd})` }"
            >
              <div class="flex items-center gap-2">
                <span>+251 911 000 000</span>
              </div>
              <span class="px-2 py-0.5 rounded-md bg-white/20 text-[10px] font-bold">24/7 Support</span>
            </div>
          </div>

          <!-- Preview Component 2: Primary & Outline Buttons -->
          <div class="space-y-2">
            <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-wider">Call to Action Buttons</span>
            <div class="flex flex-wrap items-center gap-2.5">
              <button
                type="button"
                class="px-4 py-2.5 rounded-xl text-white text-xs font-black flex items-center gap-2 shadow-md transition-all hover:scale-105 active:scale-95"
                :style="{ backgroundColor: activeTheme.primary, boxShadow: `0 6px 18px ${activeTheme.primary}40` }"
              >
                <span>Get Started</span>
                <ArrowRight class="w-3.5 h-3.5" />
              </button>
              <button
                type="button"
                class="px-4 py-2.5 rounded-xl text-xs font-black border-2 transition-all hover:bg-slate-50 dark:hover:bg-white/5"
                :style="{ borderColor: activeTheme.primary, color: activeTheme.primary }"
              >
                Learn More
              </button>
            </div>
          </div>

          <!-- Preview Component 3: Benefit Card Sample -->
          <div class="space-y-2">
            <span class="text-[10px] font-extrabold text-slate-400 uppercase tracking-wider">Public Coverage Card</span>
            <div class="p-4 rounded-2xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.06] space-y-3">
              <div class="flex items-center justify-between">
                <div
                  class="w-9 h-9 rounded-xl flex items-center justify-center text-white shadow-sm"
                  :style="{ backgroundColor: activeTheme.primary }"
                >
                  <HeartPulse class="w-5 h-5" />
                </div>
                <span
                  class="px-2.5 py-0.5 rounded-full text-[10px] font-black text-white shadow-sm"
                  :style="{ backgroundColor: activeTheme.accent }"
                >
                  Essential
                </span>
              </div>
              <div>
                <h4 class="text-xs font-bold text-slate-900 dark:text-white">Primary Health Services</h4>
                <p class="text-[11px] text-slate-500 mt-1 leading-relaxed">Free consultations, routine health check-ups, and basic clinical treatments.</p>
              </div>
            </div>
          </div>

          <!-- Save Button in Preview -->
          <button
            @click="handleSaveTheme"
            :disabled="isSaving"
            class="w-full py-3.5 rounded-2xl text-white font-extrabold text-xs flex items-center justify-center gap-2 shadow-xl hover:opacity-95 active:scale-95 transition-all"
            :style="{ backgroundColor: activeTheme.primary, boxShadow: `0 8px 24px ${activeTheme.primary}45` }"
          >
            <Save class="w-4 h-4" />
            <span>{{ isSaving ? "Saving to PostgreSQL..." : "Save Brand Colors" }}</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
