<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  Plus,
  Edit2,
  Trash2,
  Check,
  X,
  HeartPulse,
  RefreshCw,
  AlertCircle,
  CheckCircle2,
  Sparkles,
  Layers,
  ArrowRight
} from "lucide-vue-next";
import cmsService, { CoverageBenefitItem } from "../../../services/cmsService";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const benefits = ref<CoverageBenefitItem[]>([]);
const isModalOpen = ref(false);
const isEditing = ref(false);

const currentBenefit = ref<CoverageBenefitItem>({
  title: "",
  description: "",
  iconName: "Stethoscope",
  tag: "Essential",
  displayOrder: 1,
  active: true,
});

const loadBenefits = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const data = await cmsService.getBenefits();
    benefits.value = data || [];
  } catch (err) {
    console.warn("Failed to load benefits, fallback to defaults");
  } finally {
    isLoading.value = false;
  }
};

const openCreateModal = () => {
  isEditing.value = false;
  currentBenefit.value = {
    title: "",
    description: "",
    iconName: "Stethoscope",
    tag: "Essential",
    displayOrder: benefits.value.length + 1,
    active: true,
  };
  isModalOpen.value = true;
};

const openEditModal = (benefit: CoverageBenefitItem) => {
  isEditing.value = true;
  currentBenefit.value = { ...benefit };
  isModalOpen.value = true;
};

const saveBenefit = async () => {
  if (!currentBenefit.value.title || !currentBenefit.value.description) {
    message.value = { text: "Title and description are required.", type: "error" };
    return;
  }

  isSaving.value = true;
  try {
    await cmsService.saveBenefit(currentBenefit.value);
    message.value = { text: "Coverage benefit saved successfully in PostgreSQL!", type: "success" };
    isModalOpen.value = false;
    await loadBenefits();
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to save benefit.", type: "error" };
  } finally {
    isSaving.value = false;
  }
};

const toggleActive = async (benefit: CoverageBenefitItem) => {
  try {
    benefit.active = !benefit.active;
    await cmsService.saveBenefit(benefit);
  } catch {
    benefit.active = !benefit.active;
  }
};

const deleteBenefit = async (id?: number) => {
  if (!id) return;
  if (!confirm("Are you sure you want to delete this coverage package?")) return;

  try {
    await cmsService.deleteBenefit(id);
    message.value = { text: "Benefit removed successfully.", type: "success" };
    await loadBenefits();
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to delete benefit.", type: "error" };
  }
};

onMounted(() => {
  loadBenefits();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] text-white flex items-center justify-center shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] shrink-0">
          <HeartPulse class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Coverage Benefits &amp; Services
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)]">
              {{ benefits.length }} Packages
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Manage healthcare service cards rendered on the public landing page with live ordering
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadBenefits"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>
        <button
          @click="openCreateModal"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] transition-all"
        >
          <Plus class="w-4 h-4" />
          <span>Add New Package</span>
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

    <!-- Benefits Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <div
        v-for="benefit in benefits"
        :key="benefit.id"
        class="group relative p-6 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm hover:shadow-xl hover:-translate-y-0.5 transition-all duration-300 flex flex-col justify-between overflow-hidden"
      >
        <div class="absolute top-0 inset-x-0 h-1 bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] opacity-80" />
        <div>
          <!-- Top Row with Tag and Actions -->
          <div class="flex items-center justify-between gap-2 mb-4">
            <span
              v-if="benefit.tag"
              class="px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-wider bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)] border border-[var(--color-primary)]/20 shadow-sm"
            >
              {{ benefit.tag }}
            </span>
            <div class="flex items-center gap-1.5 ml-auto">
              <button
                @click="toggleActive(benefit)"
                class="px-2.5 py-1 rounded-lg text-[10px] font-extrabold transition-all"
                :class="benefit.active ? 'bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800' : 'bg-slate-100 dark:bg-white/10 text-slate-500 dark:text-slate-400'"
                :title="benefit.active ? 'Click to set Draft' : 'Click to publish'"
              >
                {{ benefit.active ? "Published" : "Draft" }}
              </button>
              <button
                @click="openEditModal(benefit)"
                class="p-1.5 text-slate-400 hover:text-[var(--color-primary)] rounded-lg hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
                title="Edit Package"
              >
                <Edit2 class="w-3.5 h-3.5" />
              </button>
              <button
                @click="deleteBenefit(benefit.id)"
                class="p-1.5 text-slate-400 hover:text-red-600 rounded-lg hover:bg-red-50 dark:hover:bg-red-950/20 transition-colors"
                title="Delete Package"
              >
                <Trash2 class="w-3.5 h-3.5" />
              </button>
            </div>
          </div>

          <h3 class="text-base font-black text-slate-900 dark:text-white tracking-tight">
            {{ benefit.title }}
          </h3>
          <p class="text-xs text-slate-500 dark:text-slate-400 mt-2.5 leading-relaxed font-medium">
            {{ benefit.description }}
          </p>
        </div>

        <div class="mt-5 pt-3.5 border-t border-slate-100 dark:border-white/[0.06] flex items-center justify-between text-[11px] text-slate-400 font-semibold">
          <span>Priority Order: #{{ benefit.displayOrder }}</span>
          <span class="font-mono text-[10px] px-2 py-0.5 rounded bg-slate-100 dark:bg-white/5">{{ benefit.iconName || "Stethoscope" }}</span>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div
      v-if="!isLoading && benefits.length === 0"
      class="p-12 text-center bg-white/80 dark:bg-[#031d33]/80 backdrop-blur-xl rounded-3xl border-2 border-dashed border-slate-200 dark:border-white/10"
    >
      <HeartPulse class="w-12 h-12 text-slate-400 mx-auto mb-3" />
      <h3 class="text-sm font-bold text-slate-700 dark:text-slate-300">No coverage packages configured</h3>
      <p class="text-xs text-slate-400 mt-1">Click "Add New Package" to create the first medical service card.</p>
    </div>

    <!-- Create/Edit Modal -->
    <div
      v-if="isModalOpen"
      class="fixed inset-0 z-50 bg-black/60 backdrop-blur-md flex items-center justify-center p-4"
      @click.self="isModalOpen = false"
    >
      <div class="w-full max-w-lg bg-white dark:bg-[#031d33] rounded-3xl shadow-2xl border border-slate-200/80 dark:border-white/10 p-6 sm:p-7 space-y-5">
        <div class="flex items-center justify-between pb-3.5 border-b border-slate-100 dark:border-white/10">
          <h3 class="text-base font-black text-slate-900 dark:text-white">
            {{ isEditing ? "Edit Benefit Package" : "Create New Benefit Package" }}
          </h3>
          <button @click="isModalOpen = false" class="p-1 rounded-lg text-slate-400 hover:text-slate-600 dark:hover:text-white">
            <X class="w-5 h-5" />
          </button>
        </div>

        <div class="space-y-4">
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Package Title</label>
            <input
              v-model="currentBenefit.title"
              type="text"
              placeholder="e.g., Inpatient Hospital Treatment"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Package Description</label>
            <textarea
              v-model="currentBenefit.description"
              rows="3"
              placeholder="Detailed explanation of medical coverage..."
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            ></textarea>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Category Tag</label>
              <input
                v-model="currentBenefit.tag"
                type="text"
                placeholder="e.g., Inpatient, Essential"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
              />
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Display Order</label>
              <input
                v-model.number="currentBenefit.displayOrder"
                type="number"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
              />
            </div>
          </div>

          <div class="flex items-center gap-2 pt-2">
            <input
              type="checkbox"
              id="benefit-active"
              v-model="currentBenefit.active"
              class="w-4 h-4 rounded text-[var(--color-primary)] focus:ring-[var(--color-primary)]"
            />
            <label for="benefit-active" class="text-xs font-bold text-slate-700 dark:text-slate-300 cursor-pointer">
              Publish immediately to public landing page
            </label>
          </div>
        </div>

        <div class="flex items-center justify-end gap-2.5 pt-3 border-t border-slate-100 dark:border-white/10">
          <button
            @click="isModalOpen = false"
            class="px-4 py-2.5 rounded-xl border border-slate-200 dark:border-white/10 text-xs font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5"
          >
            Cancel
          </button>
          <button
            @click="saveBenefit"
            :disabled="isSaving"
            class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] text-white text-xs font-extrabold shadow-md hover:opacity-95 disabled:opacity-50"
          >
            {{ isSaving ? "Saving..." : (isEditing ? "Update Package" : "Create Package") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
