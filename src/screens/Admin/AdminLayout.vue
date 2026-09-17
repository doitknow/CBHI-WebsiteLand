<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  LayoutDashboard,
  BarChart3,
  HeartPulse,
  ListOrdered,
  Newspaper,
  Settings,
  LogOut,
  ExternalLink,
  Menu,
  X,
  Database,
  UserCircle,
  Image as ImageIcon,
  Palette,
  Sun,
  Moon,
  ChevronRight,
  ChevronLeft,
  ShieldCheck,
  Sparkles,
  Activity,
  Layers
} from "lucide-vue-next";
import useAuth from "../../services/auth";
import cmsService from "../../services/cmsService";

const router = useRouter();
const route = useRoute();
const { user, logout } = useAuth();

const isMobileSidebarOpen = ref(false);
const isSidebarCollapsed = ref(false);
const isBackendOnline = ref(false);
const isDarkMode = ref(false);

const toggleSidebarCollapse = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
  try {
    localStorage.setItem("cbhi_sidebar_collapsed", String(isSidebarCollapsed.value));
  } catch {}
};

const navSections = [
  {
    title: "Core System",
    items: [
      { name: "Dashboard Overview", path: "/admin/dashboard", icon: LayoutDashboard, badge: "Live" },
    ],
  },
  {
    title: "Portal Content",
    items: [
      { name: "Hero & Impact", path: "/admin/hero-metrics", icon: BarChart3 },
      { name: "Benefits & Services", path: "/admin/benefits", icon: HeartPulse },
      { name: "Enrollment Steps", path: "/admin/enrollment", icon: ListOrdered },
      { name: "News & Bulletins", path: "/admin/news", icon: Newspaper },
      { name: "Media Assets", path: "/admin/media", icon: ImageIcon },
    ],
  },
  {
    title: "Design & Security",
    items: [
      { name: "Layout Studio", path: "/admin/layout", icon: Layers, badge: "Layouts" },
      { name: "Theme Studio", path: "/admin/theme", icon: Palette, badge: "Colors" },
      { name: "Portal Settings", path: "/admin/settings", icon: Settings },
    ],
  },
];

const allNavItems = computed(() => navSections.flatMap((section) => section.items));

const currentSectionName = computed(() => {
  const match = allNavItems.value.find((i) => i.path === route.path);
  return match ? match.name : (route.name ? String(route.name).replace("Admin", "") : "Management");
});

const handleLogout = () => {
  logout();
  router.push("/admin/login");
};

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value;
  if (isDarkMode.value) {
    document.documentElement.classList.add("dark");
    localStorage.setItem("cbhi_theme_mode", "dark");
  } else {
    document.documentElement.classList.remove("dark");
    localStorage.setItem("cbhi_theme_mode", "light");
  }
};

const checkBackend = async () => {
  try {
    const res = await cmsService.checkHealth();
    isBackendOnline.value = res.status === "UP";
  } catch {
    isBackendOnline.value = false;
  }
};

onMounted(() => {
  isDarkMode.value = document.documentElement.classList.contains("dark");
  try {
    isSidebarCollapsed.value = localStorage.getItem("cbhi_sidebar_collapsed") === "true";
  } catch {}
  checkBackend();
  const timer = setInterval(checkBackend, 30000);
  return () => clearInterval(timer);
});
</script>

<template>
  <div class="min-h-screen flex bg-[#f8fafc] dark:bg-[#021324] text-slate-800 dark:text-slate-100 font-sans selection:bg-[var(--color-primary)] selection:text-white">
    <!-- Desktop Sidebar (Collapsible) -->
    <aside
      class="hidden md:flex flex-col shrink-0 bg-white/95 dark:bg-[#031d33]/95 backdrop-blur-xl border-r border-slate-200/80 dark:border-white/[0.08] shadow-[4px_0_24px_rgba(0,0,0,0.02)] select-none relative z-30 transition-all duration-300 ease-in-out"
      :class="isSidebarCollapsed ? 'w-[76px]' : 'w-72'"
    >
      <!-- Brand Emblem / Header -->
      <div
        class="h-20 flex items-center border-b border-slate-100 dark:border-white/[0.06] bg-gradient-to-b from-slate-50/50 to-transparent dark:from-white/[0.02] transition-all duration-300"
        :class="isSidebarCollapsed ? 'px-3 justify-center' : 'px-5 justify-between'"
      >
        <div class="flex items-center gap-3 min-w-0" :class="{ 'justify-center': isSidebarCollapsed }">
          <div class="relative group shrink-0">
            <div class="w-10 h-10 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] via-[var(--color-secondary)] to-[var(--header-grad-end)] flex items-center justify-center text-white font-black text-base shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] transition-transform duration-300 group-hover:scale-105">
              <ShieldCheck class="w-5 h-5 text-white" />
            </div>
            <span class="absolute -bottom-0.5 -right-0.5 w-3 h-3 rounded-full bg-emerald-500 border-2 border-white dark:border-[#031d33] ring-1 ring-emerald-400/50"></span>
          </div>
          <div v-if="!isSidebarCollapsed" class="min-w-0 truncate">
            <div class="flex items-center gap-1.5">
              <span class="text-[10px] font-black uppercase tracking-[1.5px] text-[var(--color-primary)] truncate">Federal Ethiopia</span>
              <span class="px-1.5 py-0.2 rounded text-[9px] font-extrabold bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)] shrink-0">v2.4</span>
            </div>
            <h2 class="text-sm font-black text-slate-900 dark:text-white leading-tight mt-0.5 tracking-tight flex items-center gap-1 truncate">
              CBHI CMS Studio
            </h2>
          </div>
        </div>

        <!-- Header Collapse Toggle Icon Button -->
        <button
          v-if="!isSidebarCollapsed"
          @click="toggleSidebarCollapse"
          class="p-1.5 rounded-xl text-slate-400 hover:text-slate-700 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-white/5 transition-all"
          title="Collapse Sidebar"
        >
          <ChevronLeft class="w-4 h-4" />
        </button>
      </div>

      <!-- Navigation Links -->
      <nav class="flex-1 px-3 py-5 space-y-5 overflow-y-auto custom-scrollbar">
        <div v-for="section in navSections" :key="section.title" class="space-y-1.5">
          <p
            v-if="!isSidebarCollapsed"
            class="px-3 text-[10px] font-extrabold uppercase tracking-[1.5px] text-slate-400 dark:text-slate-500 truncate"
          >
            {{ section.title }}
          </p>
          <div v-else class="h-px bg-slate-200/60 dark:bg-white/[0.08] my-2 mx-2"></div>

          <div class="space-y-1">
            <router-link
              v-for="item in section.items"
              :key="item.path"
              :to="item.path"
              :title="item.name"
              class="group flex items-center rounded-xl text-xs font-bold transition-all duration-200 relative overflow-hidden"
              :class="[
                isSidebarCollapsed ? 'justify-center p-3' : 'justify-between px-3.5 py-2.5',
                route.path === item.path
                  ? 'bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] text-white shadow-md shadow-[rgba(var(--color-primary-rgb),0.3)]'
                  : 'text-slate-600 dark:text-slate-300 hover:bg-slate-100/80 dark:hover:bg-white/[0.06] hover:text-slate-900 dark:hover:text-white'
              ]"
            >
              <div class="flex items-center gap-3 min-w-0">
                <component
                  :is="item.icon"
                  class="w-4 h-4 shrink-0 transition-transform duration-200 group-hover:scale-110"
                  :class="route.path === item.path ? 'text-white' : 'text-slate-400 dark:text-slate-400 group-hover:text-[var(--color-primary)]'"
                />
                <span v-if="!isSidebarCollapsed" class="truncate">{{ item.name }}</span>
              </div>
              <span
                v-if="item.badge && !isSidebarCollapsed"
                class="text-[9px] font-black px-1.5 py-0.5 rounded-md transition-colors shrink-0"
                :class="route.path === item.path ? 'bg-white/20 text-white' : 'bg-slate-100 dark:bg-white/10 text-slate-500 dark:text-slate-400'"
              >
                {{ item.badge }}
              </span>
            </router-link>
          </div>
        </div>
      </nav>

      <!-- Bottom System Diagnostics Card -->
      <div class="p-3 border-t border-slate-100 dark:border-white/[0.06] bg-slate-50/50 dark:bg-white/[0.02] space-y-2.5">
        <!-- PostgreSQL & System Pulse Card -->
        <div v-if="!isSidebarCollapsed" class="p-3 rounded-xl bg-white dark:bg-[#02182b] border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
          <div class="flex items-center justify-between text-[11px] font-bold text-slate-700 dark:text-slate-300">
            <span class="flex items-center gap-1.5">
              <Database class="w-3.5 h-3.5 text-slate-400" />
              PostgreSQL DB
            </span>
            <span
              class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-black"
              :class="isBackendOnline ? 'bg-emerald-50 text-emerald-700 dark:bg-emerald-950/40 dark:text-emerald-300' : 'bg-amber-50 text-amber-700 dark:bg-amber-950/40 dark:text-amber-300'"
            >
              <span class="w-1.5 h-1.5 rounded-full" :class="isBackendOnline ? 'bg-emerald-500 animate-pulse' : 'bg-amber-500'"></span>
              {{ isBackendOnline ? "Active" : "Connecting" }}
            </span>
          </div>
          <div class="mt-2 pt-2 border-t border-slate-100 dark:border-white/[0.04] flex items-center justify-between text-[10px] text-slate-400">
            <span>Theme Accent</span>
            <div class="flex items-center gap-1">
              <span class="w-2.5 h-2.5 rounded-full shadow-sm" :style="{ backgroundColor: 'var(--color-primary)' }"></span>
              <span class="font-mono uppercase font-bold text-slate-600 dark:text-slate-400">Sync</span>
            </div>
          </div>
        </div>
        <div v-else class="flex justify-center py-1">
          <div
            class="w-8 h-8 rounded-lg flex items-center justify-center border transition-all"
            :class="isBackendOnline ? 'border-emerald-500/30 bg-emerald-500/10 text-emerald-500' : 'border-amber-500/30 bg-amber-500/10 text-amber-500'"
            :title="isBackendOnline ? 'PostgreSQL: Connected' : 'PostgreSQL: Connecting'"
          >
            <Database class="w-4 h-4" />
          </div>
        </div>

        <!-- Quick Links -->
        <div :class="isSidebarCollapsed ? 'flex flex-col items-center gap-2' : 'flex items-center gap-1.5'">
          <a
            href="/"
            target="_blank"
            :class="isSidebarCollapsed ? 'p-2.5 w-10 h-10' : 'flex-1 px-3 py-2'"
            class="flex items-center justify-center gap-1.5 rounded-xl text-xs font-bold text-slate-700 dark:text-slate-200 bg-white dark:bg-[#02182b] border border-slate-200/80 dark:border-white/[0.08] hover:border-[var(--color-primary)] hover:text-[var(--color-primary)] transition-all shadow-sm"
            :title="'Open Public Site'"
          >
            <ExternalLink class="w-4 h-4" />
            <span v-if="!isSidebarCollapsed">Public Site</span>
          </a>

          <button
            @click="handleLogout"
            :class="isSidebarCollapsed ? 'w-10 h-10' : 'p-2'"
            class="flex items-center justify-center rounded-xl text-red-500 hover:text-red-600 bg-red-50/80 hover:bg-red-100 dark:bg-red-950/30 dark:hover:bg-red-950/50 border border-red-200/60 dark:border-red-900/40 transition-colors"
            title="Log Out of CMS"
          >
            <LogOut class="w-4 h-4" />
          </button>
        </div>

        <!-- Expand button at the very bottom when collapsed -->
        <button
          v-if="isSidebarCollapsed"
          @click="toggleSidebarCollapse"
          class="w-full py-2 flex items-center justify-center rounded-xl text-slate-400 hover:text-slate-800 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-white/5 transition-all"
          title="Expand Sidebar"
        >
          <ChevronRight class="w-4 h-4 text-[var(--color-primary)]" />
        </button>
      </div>
    </aside>

    <!-- Mobile Drawer Overlay -->
    <transition
      enter-active-class="transition-opacity duration-300 ease-out"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition-opacity duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="isMobileSidebarOpen"
        class="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm md:hidden flex"
        @click="isMobileSidebarOpen = false"
      >
        <div
          class="w-72 bg-white dark:bg-[#031d33] h-full flex flex-col p-5 shadow-2xl overflow-y-auto"
          @click.stop
        >
          <div class="flex items-center justify-between pb-4 border-b border-slate-100 dark:border-white/10 mb-4">
            <div class="flex items-center gap-2.5">
              <div class="w-8 h-8 rounded-xl bg-[var(--color-primary)] flex items-center justify-center text-white font-bold text-xs">
                <ShieldCheck class="w-4 h-4" />
              </div>
              <span class="font-extrabold text-sm text-slate-900 dark:text-white">CBHI CMS Studio</span>
            </div>
            <button @click="isMobileSidebarOpen = false" class="p-1.5 rounded-lg text-slate-400 hover:bg-slate-100 dark:hover:bg-white/5">
              <X class="w-5 h-5" />
            </button>
          </div>

          <nav class="space-y-6 flex-1">
            <div v-for="section in navSections" :key="'mob-'+section.title" class="space-y-1">
              <p class="px-2 text-[10px] font-extrabold uppercase tracking-wider text-slate-400">
                {{ section.title }}
              </p>
              <router-link
                v-for="item in section.items"
                :key="'mob-'+item.path"
                :to="item.path"
                @click="isMobileSidebarOpen = false"
                class="flex items-center justify-between px-3 py-2.5 rounded-xl text-xs font-bold"
                :class="[
                  route.path === item.path
                    ? 'bg-[var(--color-primary)] text-white'
                    : 'text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5'
                ]"
              >
                <div class="flex items-center gap-2.5">
                  <component :is="item.icon" class="w-4 h-4" />
                  <span>{{ item.name }}</span>
                </div>
              </router-link>
            </div>
          </nav>

          <button
            @click="handleLogout"
            class="mt-6 w-full flex items-center justify-center gap-2 px-3 py-2.5 rounded-xl text-xs font-bold text-red-600 bg-red-50 dark:bg-red-950/40"
          >
            <LogOut class="w-4 h-4" />
            <span>Sign Out</span>
          </button>
        </div>
      </div>
    </transition>

    <!-- Main Content Stage -->
    <div class="flex-1 flex flex-col min-w-0 overflow-hidden relative">
      <!-- Ambient Glow Orbs -->
      <div class="absolute top-0 right-1/4 w-96 h-96 bg-[var(--color-primary)]/5 rounded-full blur-3xl pointer-events-none -z-0" />
      <div class="absolute bottom-10 left-10 w-80 h-80 bg-[var(--color-secondary)]/5 rounded-full blur-3xl pointer-events-none -z-0" />

      <!-- Top Glass Navbar -->
      <header class="h-20 px-4 sm:px-8 bg-white/80 dark:bg-[#031d33]/80 backdrop-blur-xl border-b border-slate-200/80 dark:border-white/[0.08] flex items-center justify-between sticky top-0 z-20 shadow-[0_4px_20px_rgba(0,0,0,0.02)]">
        <div class="flex items-center gap-3.5">
          <!-- Mobile Sidebar Toggle -->
          <button
            @click="isMobileSidebarOpen = true"
            class="md:hidden p-2 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5 rounded-xl transition-colors"
          >
            <Menu class="w-5 h-5" />
          </button>

          <!-- Desktop Sidebar Collapse Toggle Button in Top Bar -->
          <button
            @click="toggleSidebarCollapse"
            class="hidden md:flex p-2 text-slate-500 hover:text-slate-800 dark:hover:text-white rounded-xl hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
            :title="isSidebarCollapsed ? 'Expand Sidebar' : 'Collapse Sidebar'"
          >
            <ChevronRight v-if="isSidebarCollapsed" class="w-5 h-5 text-[var(--color-primary)]" />
            <ChevronLeft v-else class="w-5 h-5" />
          </button>

          <!-- Breadcrumbs -->
          <div class="flex items-center gap-2 text-xs font-semibold">
            <span class="text-slate-400 dark:text-slate-500 flex items-center gap-1.5">
              <span>Admin Studio</span>
              <ChevronRight class="w-3.5 h-3.5" />
            </span>
            <span class="text-slate-900 dark:text-white font-extrabold text-sm sm:text-base">
              {{ currentSectionName }}
            </span>
          </div>
        </div>

        <!-- Top Right Actions & User Profile -->
        <div class="flex items-center gap-3 sm:gap-4">
          <!-- Dark / Light Mode Toggle -->
          <button
            @click="toggleDarkMode"
            class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] bg-white dark:bg-[#02182b] text-slate-600 dark:text-slate-300 hover:border-[var(--color-primary)] hover:text-[var(--color-primary)] transition-all shadow-sm"
            :title="isDarkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'"
          >
            <Sun v-if="isDarkMode" class="w-4 h-4 text-amber-400" />
            <Moon v-else class="w-4 h-4 text-slate-600" />
          </button>

          <!-- Live Preview Action -->
          <a
            href="/"
            target="_blank"
            class="hidden sm:inline-flex items-center gap-1.5 px-3 py-2 rounded-xl text-xs font-bold text-[var(--color-primary)] bg-[rgba(var(--color-primary-rgb),0.08)] hover:bg-[rgba(var(--color-primary-rgb),0.15)] transition-all border border-[rgba(var(--color-primary-rgb),0.15)]"
          >
            <ExternalLink class="w-3.5 h-3.5" />
            <span>Live Portal</span>
          </a>

          <!-- User Card -->
          <div class="flex items-center gap-3 pl-3 sm:pl-4 border-l border-slate-200 dark:border-white/10">
            <div class="relative">
              <div class="w-9 h-9 rounded-xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] text-white flex items-center justify-center font-black text-xs shadow-md ring-2 ring-white dark:ring-[#031d33]">
                {{ (user?.username || "A").substring(0, 2).toUpperCase() }}
              </div>
              <span class="absolute -bottom-0.5 -right-0.5 w-2.5 h-2.5 rounded-full bg-emerald-500 border-2 border-white dark:border-[#031d33]"></span>
            </div>
            <div class="hidden sm:block text-left leading-tight">
              <p class="text-xs font-extrabold text-slate-900 dark:text-white">
                {{ user?.username || "Administrator" }}
              </p>
              <p class="text-[10px] font-semibold text-slate-400 mt-0.5 flex items-center gap-1">
                <span>Enterprise Admin</span>
              </p>
            </div>
          </div>
        </div>
      </header>

      <!-- Page Outlet with Ambient Styling -->
      <main class="flex-1 p-4 sm:p-8 overflow-y-auto custom-scrollbar relative z-10">
        <div class="max-w-7xl mx-auto space-y-6">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: rgba(150, 150, 150, 0.2);
  border-radius: 9999px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: rgba(150, 150, 150, 0.4);
}
</style>
