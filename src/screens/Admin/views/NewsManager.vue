<script setup lang="ts">
import { ref, onMounted } from "vue";
import { Plus, Edit2, Trash2, Newspaper, RefreshCw, AlertCircle, CheckCircle2, X, Calendar, Tag } from "lucide-vue-next";
import cmsService, { NewsArticleItem } from "../../../services/cmsService";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const newsList = ref<NewsArticleItem[]>([]);
const isModalOpen = ref(false);
const isEditing = ref(false);

const currentNews = ref<NewsArticleItem>({
  title: "",
  summary: "",
  content: "",
  category: "Policy",
  imageUrl: "",
  published: true,
});

const loadNews = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const data = await cmsService.getNews();
    newsList.value = data || [];
  } catch (err) {
    console.warn("Failed to load news articles");
  } finally {
    isLoading.value = false;
  }
};

const openCreateModal = () => {
  isEditing.value = false;
  currentNews.value = {
    title: "",
    summary: "",
    content: "",
    category: "Expansion",
    imageUrl: "",
    published: true,
  };
  isModalOpen.value = true;
};

const openEditModal = (article: NewsArticleItem) => {
  isEditing.value = true;
  currentNews.value = { ...article };
  isModalOpen.value = true;
};

const saveNews = async () => {
  if (!currentNews.value.title || !currentNews.value.summary || !currentNews.value.content) {
    message.value = { text: "Title, summary, and content are required.", type: "error" };
    return;
  }

  isSaving.value = true;
  try {
    await cmsService.saveNews(currentNews.value);
    message.value = { text: "News article saved successfully in PostgreSQL!", type: "success" };
    isModalOpen.value = false;
    await loadNews();
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to save article.", type: "error" };
  } finally {
    isSaving.value = false;
  }
};

const togglePublished = async (article: NewsArticleItem) => {
  try {
    article.published = !article.published;
    await cmsService.saveNews(article);
  } catch {
    article.published = !article.published;
  }
};

const deleteNews = async (id?: number) => {
  if (!id) return;
  if (!confirm("Are you sure you want to delete this news article?")) return;

  try {
    await cmsService.deleteNews(id);
    message.value = { text: "Article removed successfully.", type: "success" };
    await loadNews();
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to delete article.", type: "error" };
  }
};

onMounted(() => {
  loadNews();
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-amber-500 to-orange-500 text-white flex items-center justify-center shadow-lg shadow-amber-500/25 shrink-0">
          <Newspaper class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              News &amp; Official Bulletins
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-amber-50 dark:bg-amber-950/50 text-amber-700 dark:text-amber-300">
              {{ newsList.length }} Articles
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Publish health policy circulars, woreda expansion announcements, and public advisories
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadNews"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>
        <button
          @click="openCreateModal"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-amber-500 to-orange-500 hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-amber-500/25 transition-all"
        >
          <Plus class="w-4 h-4" />
          <span>New Article</span>
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

    <!-- Articles Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
      <div
        v-for="article in newsList"
        :key="article.id"
        class="group relative p-6 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm hover:shadow-xl hover:-translate-y-0.5 transition-all duration-300 flex flex-col justify-between overflow-hidden"
      >
        <div class="absolute top-0 inset-x-0 h-1 bg-gradient-to-r from-amber-500 to-orange-400 opacity-80" />
        <div>
          <!-- Top Row -->
          <div class="flex items-center justify-between gap-2 mb-4">
            <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black uppercase tracking-wider bg-amber-50 dark:bg-amber-950/40 text-amber-700 dark:text-amber-300 border border-amber-200/60 dark:border-amber-800/40 shadow-sm">
              <Tag class="w-3 h-3" />
              {{ article.category }}
            </span>
            <div class="flex items-center gap-1.5">
              <button
                @click="togglePublished(article)"
                class="px-2.5 py-1 rounded-lg text-[10px] font-extrabold transition-all"
                :class="article.published ? 'bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-300 border border-emerald-200 dark:border-emerald-800' : 'bg-slate-100 dark:bg-white/10 text-slate-500 dark:text-slate-400'"
                :title="article.published ? 'Published on portal' : 'Draft / Hidden'"
              >
                {{ article.published ? "Published" : "Draft" }}
              </button>
              <button
                @click="openEditModal(article)"
                class="p-1.5 text-slate-400 hover:text-amber-600 rounded-lg hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
                title="Edit Article"
              >
                <Edit2 class="w-3.5 h-3.5" />
              </button>
              <button
                @click="deleteNews(article.id)"
                class="p-1.5 text-slate-400 hover:text-red-600 rounded-lg hover:bg-red-50 dark:hover:bg-red-950/20 transition-colors"
                title="Delete Article"
              >
                <Trash2 class="w-3.5 h-3.5" />
              </button>
            </div>
          </div>

          <h3 class="text-base font-black text-slate-900 dark:text-white tracking-tight leading-snug">
            {{ article.title }}
          </h3>
          <p class="text-xs text-slate-500 dark:text-slate-400 mt-2.5 line-clamp-3 leading-relaxed font-medium">
            {{ article.summary }}
          </p>
        </div>

        <div class="mt-5 pt-3.5 border-t border-slate-100 dark:border-white/[0.06] flex items-center justify-between text-[11px] text-slate-400 font-semibold">
          <span class="flex items-center gap-1.5">
            <Calendar class="w-3.5 h-3.5" />
            {{ article.createdAt ? new Date(article.createdAt).toLocaleDateString() : "Active" }}
          </span>
          <span class="text-[10px] font-bold text-amber-600 dark:text-amber-400">CBHI Editorial</span>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div
      v-if="!isLoading && newsList.length === 0"
      class="p-12 text-center bg-white/80 dark:bg-[#031d33]/80 backdrop-blur-xl rounded-3xl border-2 border-dashed border-slate-200 dark:border-white/10"
    >
      <Newspaper class="w-12 h-12 text-slate-400 mx-auto mb-3" />
      <h3 class="text-sm font-bold text-slate-700 dark:text-slate-300">No news articles published</h3>
      <p class="text-xs text-slate-400 mt-1">Click "New Article" to publish the first official bulletin.</p>
    </div>

    <!-- Create/Edit Modal -->
    <div
      v-if="isModalOpen"
      class="fixed inset-0 z-50 bg-black/60 backdrop-blur-md flex items-center justify-center p-4"
      @click.self="isModalOpen = false"
    >
      <div class="w-full max-w-xl bg-white dark:bg-[#031d33] rounded-3xl shadow-2xl border border-slate-200/80 dark:border-white/10 p-6 sm:p-7 space-y-5">
        <div class="flex items-center justify-between pb-3.5 border-b border-slate-100 dark:border-white/10">
          <h3 class="text-base font-black text-slate-900 dark:text-white">
            {{ isEditing ? "Edit News Bulletin" : "Publish News Article" }}
          </h3>
          <button @click="isModalOpen = false" class="p-1 rounded-lg text-slate-400 hover:text-slate-600 dark:hover:text-white">
            <X class="w-5 h-5" />
          </button>
        </div>

        <div class="space-y-4">
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Article Headline</label>
            <input
              v-model="currentNews.title"
              type="text"
              placeholder="e.g., CBHI Expands Digital Enrollment to 50 Additional Woredas"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-amber-500 focus:outline-none"
            />
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Category</label>
              <select
                v-model="currentNews.category"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-amber-500 focus:outline-none"
              >
                <option value="Policy">Policy</option>
                <option value="Expansion">Expansion</option>
                <option value="Community">Community</option>
                <option value="Partnership">Partnership</option>
              </select>
            </div>

            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Banner Image URL</label>
              <input
                v-model="currentNews.imageUrl"
                type="text"
                placeholder="https://... or /assets/..."
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-amber-500 focus:outline-none"
              />
            </div>
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Short Summary</label>
            <textarea
              v-model="currentNews.summary"
              rows="2"
              placeholder="Brief overview displayed on card preview..."
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-amber-500 focus:outline-none"
            ></textarea>
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5">Full Content / Body</label>
            <textarea
              v-model="currentNews.content"
              rows="4"
              placeholder="Full circular body text..."
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 text-xs font-medium text-slate-900 dark:text-white focus:ring-2 focus:ring-amber-500 focus:outline-none"
            ></textarea>
          </div>

          <div class="flex items-center gap-2 pt-1">
            <input
              type="checkbox"
              id="news-published"
              v-model="currentNews.published"
              class="w-4 h-4 rounded text-amber-500 focus:ring-amber-500"
            />
            <label for="news-published" class="text-xs font-bold text-slate-700 dark:text-slate-300 cursor-pointer">
              Publish immediately to news section
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
            @click="saveNews"
            :disabled="isSaving"
            class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-amber-500 to-orange-500 text-white text-xs font-extrabold shadow-md hover:opacity-95 disabled:opacity-50"
          >
            {{ isSaving ? "Saving..." : (isEditing ? "Update Article" : "Publish Article") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
