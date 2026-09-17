<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Save, RefreshCw, CheckCircle2, AlertCircle, ListOrdered, Sparkles, ArrowRight } from "lucide-vue-next";
import cmsService, { EnrollmentStepItem } from "../../../services/cmsService";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const steps = ref<EnrollmentStepItem[]>([
  { stepNumber: 1, title: "Identity Check", description: "Register at your local Woreda with your Fayda ID. Our system integrates instantly for verification.", badgeText: "Bring your Fayda ID or national ID", displayOrder: 1 },
  { stepNumber: 2, title: "Annual Contribution", description: "Pay a small annual fee based on your household size. Flexible payment options available.", badgeText: "Affordable rates for every household", displayOrder: 2 },
  { stepNumber: 3, title: "Get Coverage", description: "Access healthcare instantly at any partner facility with your digital or physical CBHI card.", badgeText: "Coverage starts same day", displayOrder: 3 },
]);

const loadSteps = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const data = await cmsService.getSteps();
    if (data && data.length > 0) {
      steps.value = data;
    }
  } catch (err) {
    console.warn("Using offline enrollment steps");
  } finally {
    isLoading.value = false;
  }
};

const saveAllSteps = async () => {
  isSaving.value = true;
  message.value = null;

  try {
    for (const step of steps.value) {
      await cmsService.saveStep(step);
    }
    message.value = { text: "Enrollment procedures updated successfully in PostgreSQL!", type: "success" };
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to update enrollment steps.", type: "error" };
  } finally {
    isSaving.value = false;
  }
};

onMounted(() => {
  loadSteps();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-blue-500 to-indigo-600 text-white flex items-center justify-center shadow-lg shadow-blue-500/25 shrink-0">
          <ListOrdered class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Citizen Enrollment Procedures
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-blue-50 dark:bg-blue-950/50 text-blue-700 dark:text-blue-300">
              3 Step Workflow
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Configure the 3-step citizen registration flow and verification prerequisites displayed on the portal
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadSteps"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>
        <button
          @click="saveAllSteps"
          :disabled="isSaving"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-blue-600 to-indigo-600 hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-blue-500/25 disabled:opacity-60 transition-all"
        >
          <Save class="w-4 h-4" />
          <span>{{ isSaving ? "Saving..." : "Save Procedures" }}</span>
        </button>
      </div>
    </div>

    <!-- Alert status -->
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

    <!-- Steps Grid -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div
        v-for="step in steps"
        :key="step.stepNumber"
        class="group relative p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm hover:shadow-xl hover:-translate-y-0.5 transition-all duration-300 flex flex-col justify-between overflow-hidden space-y-4"
      >
        <div class="absolute top-0 inset-x-0 h-1 bg-gradient-to-r from-blue-500 to-indigo-500 opacity-80" />

        <div class="flex items-center justify-between pb-3.5 border-b border-slate-100 dark:border-white/[0.06]">
          <div class="flex items-center gap-2.5">
            <span class="w-8 h-8 rounded-xl bg-gradient-to-tr from-blue-600 to-indigo-600 text-white font-black text-xs flex items-center justify-center shadow-md">
              0{{ step.stepNumber }}
            </span>
            <span class="text-xs font-extrabold text-slate-800 dark:text-slate-200 uppercase tracking-wider">
              Stage {{ step.stepNumber }}
            </span>
          </div>
          <span class="text-[10px] font-bold text-slate-400">Step #{{ step.displayOrder }}</span>
        </div>

        <div class="space-y-3.5">
          <div>
            <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Step Title</label>
            <input
              v-model="step.title"
              type="text"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Prerequisite / Badge Note</label>
            <input
              v-model="step.badgeText"
              type="text"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-[11px] font-bold text-slate-600 dark:text-slate-400 mb-1">Description</label>
            <textarea
              v-model="step.description"
              rows="3"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:outline-none leading-relaxed"
            ></textarea>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
