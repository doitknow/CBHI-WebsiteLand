<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import {
  UploadCloud,
  Image as ImageIcon,
  Trash2,
  Copy,
  Check,
  ExternalLink,
  RefreshCw,
  Search,
  X,
  Filter,
  Eye,
  AlertCircle,
  CheckCircle2,
  FileImage,
  FolderOpen
} from "lucide-vue-next";
import cmsService, { MediaAssetItem } from "../../../services/cmsService";

const isLoading = ref(true);
const isUploading = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);
const copiedId = ref<number | null>(null);

const mediaList = ref<MediaAssetItem[]>([]);
const searchQuery = ref("");
const selectedCategory = ref("all");

// Upload Modal state
const isUploadModalOpen = ref(false);
const selectedFile = ref<File | null>(null);
const uploadPreviewUrl = ref<string | null>(null);
const uploadCategory = ref("general");
const uploadAltText = ref("");

// Lightbox modal state
const previewAsset = ref<MediaAssetItem | null>(null);

const categories = [
  { id: "all", label: "All Images" },
  { id: "partners", label: "Partner Logos" },
  { id: "benefits", label: "Coverage Benefits" },
  { id: "hero", label: "Hero & Banners" },
  { id: "badges", label: "Badges & Security" },
  { id: "general", label: "General Uploads" },
];

const loadMedia = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const data = await cmsService.getMedia();
    mediaList.value = data || [];
  } catch (err: any) {
    console.warn("Failed to load media assets");
  } finally {
    isLoading.value = false;
  }
};

const filteredMedia = computed(() => {
  return mediaList.value.filter((item) => {
    const matchesCategory =
      selectedCategory.value === "all" ||
      item.category?.toLowerCase() === selectedCategory.value.toLowerCase();

    const matchesSearch =
      !searchQuery.value ||
      item.filename.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      (item.altText && item.altText.toLowerCase().includes(searchQuery.value.toLowerCase()));

    return matchesCategory && matchesSearch;
  });
});

const formatSize = (bytes?: number) => {
  if (!bytes) return "0 B";
  if (bytes < 1024) return bytes + " B";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
  return (bytes / (1024 * 1024)).toFixed(1) + " MB";
};

const getImageUrl = (item: MediaAssetItem) => {
  if (item.fileUrl.startsWith("http")) return item.fileUrl;
  const baseUrl = import.meta.env.VITE_API_URL || "http://localhost:8080";
  return `${baseUrl}${item.fileUrl}`;
};

const copyUrlToClipboard = async (item: MediaAssetItem) => {
  const fullUrl = getImageUrl(item);
  try {
    await navigator.clipboard.writeText(fullUrl);
    copiedId.value = item.id;
    setTimeout(() => {
      if (copiedId.value === item.id) copiedId.value = null;
    }, 2000);
  } catch (err) {
    // fallback
  }
};

const MAX_UPLOAD_BYTES = 50 * 1024 * 1024; // 50MB

const validateAndSetFile = (file: File) => {
  if (file.size > MAX_UPLOAD_BYTES) {
    message.value = {
      text: `Selected file "${file.name}" (${formatSize(file.size)}) exceeds the 50MB maximum size limit. Please compress or choose a smaller image.`,
      type: "error",
    };
    selectedFile.value = null;
    uploadPreviewUrl.value = null;
    return false;
  }
  selectedFile.value = file;
  uploadPreviewUrl.value = URL.createObjectURL(file);
  if (!uploadAltText.value) {
    uploadAltText.value = file.name.replace(/\.[^/.]+$/, "");
  }
  message.value = null;
  return true;
};

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files[0]) {
    validateAndSetFile(target.files[0]);
  }
};

const handleDrop = (event: DragEvent) => {
  event.preventDefault();
  if (event.dataTransfer?.files && event.dataTransfer.files[0]) {
    validateAndSetFile(event.dataTransfer.files[0]);
  }
};

const handleUpload = async () => {
  if (!selectedFile.value) {
    message.value = { text: "Please choose an image file first.", type: "error" };
    return;
  }

  isUploading.value = true;
  message.value = null;

  try {
    const formData = new FormData();
    formData.append("file", selectedFile.value);
    formData.append("category", uploadCategory.value);
    formData.append("altText", uploadAltText.value);

    await cmsService.uploadMedia(formData);

    message.value = {
      text: "Image uploaded and stored successfully!",
      type: "success",
    };

    isUploadModalOpen.value = false;
    selectedFile.value = null;
    uploadPreviewUrl.value = null;
    uploadAltText.value = "";
    await loadMedia();
  } catch (err: any) {
    message.value = {
      text: err?.message || "Failed to upload image to server.",
      type: "error",
    };
  } finally {
    isUploading.value = false;
  }
};

const deleteMediaItem = async (id: number) => {
  if (!confirm("Are you sure you want to permanently delete this image?")) return;

  try {
    await cmsService.deleteMedia(id);
    message.value = { text: "Image removed from server.", type: "success" };
    await loadMedia();
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to delete image.", type: "error" };
  }
};

onMounted(() => {
  loadMedia();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-indigo-500 to-purple-600 text-white flex items-center justify-center shadow-lg shadow-indigo-500/25 shrink-0">
          <ImageIcon class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Media &amp; Asset Library
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-indigo-50 dark:bg-indigo-950/50 text-indigo-700 dark:text-indigo-300">
              {{ mediaList.length }} Assets
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Manage, upload, and inspect images used across the portal, partner logos, and benefit packages
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadMedia"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh Library"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>

        <button
          @click="isUploadModalOpen = true"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-indigo-500 to-purple-600 hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-indigo-500/25 transition-all"
        >
          <UploadCloud class="w-4 h-4" />
          <span>Upload Asset</span>
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

    <!-- Filters & Search Bar -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 p-5 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <!-- Category Tabs -->
      <div class="flex items-center gap-1.5 overflow-x-auto pb-1 md:pb-0 scrollbar-none">
        <button
          v-for="cat in categories"
          :key="cat.id"
          @click="selectedCategory = cat.id"
          class="px-3.5 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition-all"
          :class="[
            selectedCategory === cat.id
              ? 'bg-gradient-to-r from-indigo-500 to-purple-600 text-white shadow-sm'
              : 'text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5'
          ]"
        >
          {{ cat.label }}
        </button>
      </div>

      <!-- Search Input -->
      <div class="relative w-full md:w-72">
        <Search class="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search by filename or tag..."
          class="w-full pl-10 pr-4 py-2 text-xs font-medium bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
      </div>
    </div>

    <!-- Gallery Grid -->
    <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
      <div
        v-for="item in filteredMedia"
        :key="item.id"
        class="group relative bg-white dark:bg-[#032035] rounded-2xl border border-slate-200 dark:border-white/10 overflow-hidden shadow-sm hover:shadow-md transition-all flex flex-col justify-between"
      >
        <!-- Image Preview Container -->
        <div
          class="aspect-square w-full bg-slate-100 dark:bg-[#02182b] flex items-center justify-center p-3 relative overflow-hidden cursor-pointer"
          @click="previewAsset = item"
        >
          <img
            :src="getImageUrl(item)"
            :alt="item.altText || item.filename"
            class="max-h-full max-w-full object-contain transition-transform duration-300 group-hover:scale-105"
            loading="lazy"
          />

          <!-- Category Pill -->
          <span
            class="absolute top-2 left-2 px-2 py-0.5 rounded-full text-[9px] font-extrabold uppercase tracking-wider bg-white/90 dark:bg-black/80 text-slate-800 dark:text-slate-200 shadow-sm backdrop-blur-sm"
          >
            {{ item.category || "General" }}
          </span>

          <!-- Quick Action Hover Overlay -->
          <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
            <button
              @click.stop="previewAsset = item"
              class="p-2 rounded-xl bg-white text-slate-900 shadow hover:scale-110 transition-transform"
              title="Preview Image"
            >
              <Eye class="w-4 h-4" />
            </button>
            <button
              @click.stop="copyUrlToClipboard(item)"
              class="p-2 rounded-xl bg-white text-slate-900 shadow hover:scale-110 transition-transform"
              title="Copy URL"
            >
              <Check v-if="copiedId === item.id" class="w-4 h-4 text-emerald-600" />
              <Copy v-else class="w-4 h-4" />
            </button>
          </div>
        </div>

        <!-- Info & Actions Footer -->
        <div class="p-3 border-t border-slate-100 dark:border-white/5 space-y-1">
          <p class="text-xs font-bold text-slate-800 dark:text-slate-200 truncate" :title="item.filename">
            {{ item.filename }}
          </p>
          <div class="flex items-center justify-between text-[10px] text-slate-400">
            <span>{{ formatSize(item.fileSize) }}</span>
            <button
              @click="deleteMediaItem(item.id)"
              class="text-slate-400 hover:text-red-600 transition-colors p-1"
              title="Delete Image"
            >
              <Trash2 class="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div
      v-if="!isLoading && filteredMedia.length === 0"
      class="p-12 text-center bg-white dark:bg-[#032035] rounded-2xl border border-dashed border-slate-300 dark:border-white/10 space-y-3"
    >
      <FolderOpen class="w-12 h-12 text-slate-400 mx-auto" />
      <h3 class="text-sm font-bold text-slate-700 dark:text-slate-300">No images match your filter</h3>
      <p class="text-xs text-slate-400 max-w-sm mx-auto">
        Try choosing a different category tab or upload a new image to the CBHI asset library.
      </p>
      <button
        @click="isUploadModalOpen = true"
        class="px-4 py-2 rounded-xl bg-[var(--color-primary)] hover:opacity-90 text-white font-bold text-xs inline-flex items-center gap-2 transition-all"
      >
        <UploadCloud class="w-4 h-4" />
        <span>Upload New Image</span>
      </button>
    </div>

    <!-- Upload Modal -->
    <div
      v-if="isUploadModalOpen"
      class="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4"
      @click.self="isUploadModalOpen = false"
    >
      <div class="w-full max-w-lg bg-white dark:bg-[#032035] rounded-2xl shadow-2xl border border-slate-200 dark:border-white/10 p-6 space-y-4">
        <div class="flex items-center justify-between pb-3 border-b border-slate-100 dark:border-white/10">
          <h3 class="text-base font-bold text-slate-900 dark:text-white flex items-center gap-2">
            <UploadCloud class="w-5 h-5 text-[var(--color-primary)]" />
            <span>Upload Image Asset</span>
          </h3>
          <button @click="isUploadModalOpen = false" class="text-slate-400 hover:text-slate-600">
            <X class="w-5 h-5" />
          </button>
        </div>

        <!-- Drag and Drop Dropzone -->
        <div
          @dragover.prevent
          @drop="handleDrop"
          class="border-2 border-dashed border-slate-300 dark:border-white/15 rounded-2xl p-6 text-center hover:border-[var(--color-primary)] transition-colors cursor-pointer bg-slate-50/50 dark:bg-[#02182b]/40 relative"
        >
          <input
            type="file"
            accept="image/*"
            @change="handleFileSelect"
            class="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
          />

          <div v-if="!uploadPreviewUrl" class="space-y-2">
            <FileImage class="w-10 h-10 text-slate-400 mx-auto" />
            <p class="text-xs font-bold text-slate-700 dark:text-slate-300">
              Drag and drop your image here, or <span class="text-[var(--color-primary)]">browse</span>
            </p>
            <p class="text-[10px] text-slate-400">
              Supports PNG, JPG, WebP, GIF, and SVG up to 50MB
            </p>
          </div>

          <div v-else class="flex flex-col items-center">
            <img :src="uploadPreviewUrl" class="h-32 object-contain rounded-xl shadow-sm mb-2" />
            <p class="text-xs font-bold text-slate-700 dark:text-slate-200">{{ selectedFile?.name }}</p>
            <span class="text-[10px] text-slate-400">{{ formatSize(selectedFile?.size) }}</span>
          </div>
        </div>

        <!-- Metadata Form -->
        <div class="space-y-3">
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">Category</label>
              <select
                v-model="uploadCategory"
                class="w-full px-3 py-2 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium"
              >
                <option value="partners">Partner Logos</option>
                <option value="benefits">Coverage Benefits</option>
                <option value="hero">Hero / Banners</option>
                <option value="badges">Badges & Certifications</option>
                <option value="general">General</option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1">Alt / Description</label>
              <input
                v-model="uploadAltText"
                type="text"
                placeholder="Accessibility text or caption"
                class="w-full px-3 py-2 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs"
              />
            </div>
          </div>
        </div>

        <!-- Action buttons -->
        <div class="flex items-center justify-end gap-3 pt-3 border-t border-slate-100 dark:border-white/10">
          <button
            @click="isUploadModalOpen = false"
            class="px-4 py-2 rounded-xl text-xs font-bold text-slate-600 hover:bg-slate-100"
          >
            Cancel
          </button>
          <button
            @click="handleUpload"
            :disabled="isUploading || !selectedFile"
            class="px-5 py-2.5 rounded-xl bg-[var(--color-primary)] hover:opacity-90 text-white font-bold text-xs shadow-md shadow-[rgba(var(--color-primary-rgb),0.25)] disabled:opacity-60 flex items-center gap-2 transition-all"
          >
            <span>{{ isUploading ? "Uploading to Server..." : "Upload Image" }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Preview Lightbox Modal -->
    <div
      v-if="previewAsset"
      class="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-center justify-center p-4"
      @click.self="previewAsset = null"
    >
      <div class="w-full max-w-2xl bg-white dark:bg-[#032035] rounded-3xl overflow-hidden shadow-2xl border border-white/10 flex flex-col">
        <!-- Header -->
        <div class="px-6 py-4 border-b border-slate-100 dark:border-white/10 flex items-center justify-between">
          <div>
            <h3 class="text-sm font-bold text-slate-900 dark:text-white">{{ previewAsset.filename }}</h3>
            <span class="text-[11px] text-slate-400 capitalize">{{ previewAsset.category }} • {{ formatSize(previewAsset.fileSize) }}</span>
          </div>
          <button @click="previewAsset = null" class="p-1 text-slate-400 hover:text-slate-600">
            <X class="w-5 h-5" />
          </button>
        </div>

        <!-- Big image display -->
        <div class="p-6 bg-slate-100/50 dark:bg-[#02182b]/80 flex items-center justify-center max-h-[60vh] overflow-hidden">
          <img :src="getImageUrl(previewAsset)" class="max-h-[50vh] object-contain rounded-xl shadow-lg" />
        </div>

        <!-- Footer with copy url -->
        <div class="p-6 border-t border-slate-100 dark:border-white/10 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div class="flex-1 text-xs">
            <label class="block font-bold text-slate-500 uppercase tracking-wider mb-1">Direct URL</label>
            <div class="flex items-center gap-2">
              <input
                :value="getImageUrl(previewAsset)"
                readonly
                class="w-full px-3 py-1.5 text-xs bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-lg font-mono select-all"
              />
              <button
                @click="copyUrlToClipboard(previewAsset)"
                class="px-3 py-1.5 rounded-lg bg-[var(--color-primary)] hover:opacity-90 text-white font-bold text-xs flex items-center gap-1.5 shrink-0 transition-all"
              >
                <Check v-if="copiedId === previewAsset.id" class="w-3.5 h-3.5" />
                <Copy v-else class="w-3.5 h-3.5" />
                <span>{{ copiedId === previewAsset.id ? "Copied" : "Copy" }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
