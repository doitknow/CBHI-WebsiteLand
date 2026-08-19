<script setup lang="ts">
import { ref, watch, onMounted, onBeforeUnmount } from "vue";
import { Moon, Sun, Globe, ChevronDown, Menu, X } from "lucide-vue-next";
import { useI18n } from "vue-i18n";

const { t, locale } = useI18n();

/* ── Navigation items ── */
const navigationItems = [
  { translationKey: "nav.home",       sectionId: "section-home"       },
  { translationKey: "nav.philosophy", sectionId: "section-philosophy"  },
  { translationKey: "nav.our_impact", sectionId: "section-our-impact"  },
  { translationKey: "nav.benefits",   sectionId: "section-benefits"    },
  { translationKey: "nav.process",    sectionId: "section-process"     },
];

const activeNavigation = ref("section-home");
const isDarkMode       = ref(false);
const isMobileOpen     = ref(false);

/* ── Scroll-aware active state (IntersectionObserver) ── */
let observer: IntersectionObserver | null = null;

onMounted(() => {
  // Initialize dark mode state
  isDarkMode.value = document.documentElement.classList.contains("dark");

  const sectionMap = new Map(navigationItems.map((n) => [n.sectionId, n.sectionId]));

  observer = new IntersectionObserver(
    (entries) => {
      let best: IntersectionObserverEntry | null = null;
      for (const e of entries) {
        if (e.isIntersecting && (!best || e.intersectionRatio > best.intersectionRatio)) {
          best = e;
        }
      }
      if (best) {
        const id = sectionMap.get(best.target.id);
        if (id) activeNavigation.value = id;
      }
    },
    { threshold: [0, 0.15, 0.3, 0.5], rootMargin: "-10% 0px -55% 0px" }
  );

  navigationItems.forEach(({ sectionId }) => {
    const el = document.getElementById(sectionId);
    if (el) observer!.observe(el);
  });
});

onBeforeUnmount(() => { observer?.disconnect(); });

watch(isDarkMode, (val) => {
  if (val) {
    document.documentElement.classList.add("dark");
  } else {
    document.documentElement.classList.remove("dark");
  }
});

/* ── Click: set active + smooth scroll + close mobile ── */
function handleNavClick(item: { translationKey: string; sectionId: string }) {
  activeNavigation.value = item.sectionId;
  isMobileOpen.value = false;
  const target = document.getElementById(item.sectionId);
  if (target) target.scrollIntoView({ behavior: "smooth", block: "start" });
}

/* ── Language Switcher ── */
const availableLocales = [
  { code: 'en', name: 'English' },
  { code: 'am', name: 'አማርኛ' },
  { code: 'om', name: 'Afaan Oromoo' },
  { code: 'ti', name: 'ትግርኛ' },
  { code: 'so', name: 'Soomaali' }
];

const isLanguageMenuOpen = ref(false);

function changeLanguage(code: string) {
  if (locale.value === code) return;
  locale.value = code;
  localStorage.setItem('language', code);
  isLanguageMenuOpen.value = false;
}

function getLocaleName(code: string) {
  return availableLocales.find(l => l.code === code)?.name || 'English';
}
</script>

<template>
  <header class="relative z-50 w-full bg-transparent [font-family:'Inter',Helvetica] transition-colors duration-300">

    <!-- ════════════════ DESKTOP BAR ════════════════ -->
    <div class="mx-auto flex h-[72px] w-full max-w-[1400px] items-center px-6 lg:px-10">

      <!-- Left: Logo -->
      <div class="flex shrink-0 items-center">
        <span class="text-[28px] font-black leading-none tracking-tight text-[#0873b9] dark:text-white">
          CBHI.et
        </span>
      </div>

      <!-- Center: Desktop nav (hidden below lg) -->
      <nav
        aria-label="Primary navigation"
        class="hidden flex-1 items-center justify-center lg:flex"
      >
        <ul class="flex items-center gap-x-1">
          <li v-for="item in navigationItems" :key="item.sectionId">
            <button
              type="button"
              :aria-current="activeNavigation === item.sectionId ? 'page' : undefined"
              class="nav-link relative inline-flex h-9 items-center justify-center whitespace-nowrap px-4 text-[15px] leading-none tracking-[0] transition-all duration-200 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#0873b9]/40"
              :class="
                activeNavigation === item.sectionId
                  ? 'active-nav font-semibold text-[#0873b9] dark:text-[#0873b9]'
                  : 'font-medium text-[#808080] dark:text-slate-300 hover:text-[#0873b9] dark:hover:text-[#0873b9]'
              "
              @click="handleNavClick(item)"
            >
              {{ t(item.translationKey) }}
            </button>
          </li>
        </ul>
      </nav>

      <!-- Right: controls -->
      <div class="ml-auto flex shrink-0 items-center gap-2">
        <!-- Dark mode icon -->
        <button
          type="button"
          aria-label="Toggle dark mode"
          class="flex h-9 w-9 shrink-0 items-center justify-center rounded-full text-[#808080] dark:text-slate-300 transition-all duration-300 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-[#0873b9] dark:hover:text-[#0873b9] hover:rotate-12"
          @click="isDarkMode = !isDarkMode"
        >
          <Transition name="icon-rotate" mode="out-in">
            <Moon v-if="!isDarkMode" key="moon" class="h-[20px] w-[20px]" />
            <Sun v-else key="sun" class="h-[20px] w-[20px]" />
          </Transition>
        </button>

        <!-- Language selector -->
        <div class="relative hidden sm:block">
          <button
            type="button"
            @click="isLanguageMenuOpen = !isLanguageMenuOpen"
            class="flex h-9 w-[138px] shrink-0 items-center gap-1.5 rounded-lg border border-[#d1d1d1] dark:border-slate-700 bg-white dark:bg-slate-800 px-3 text-[13.5px] font-medium text-[#787878] dark:text-slate-200 transition-colors duration-200 hover:border-[#0873b9]/60 dark:hover:border-[#0873b9]/60 hover:text-[#0873b9] dark:hover:text-[#0873b9] focus:outline-none"
          >
            <Globe class="h-[15px] w-[15px] shrink-0" />
            <span class="flex-1 text-left whitespace-nowrap overflow-hidden text-ellipsis">{{ getLocaleName(locale) }}</span>
            <ChevronDown class="h-3 w-3 shrink-0 opacity-50" />
          </button>
          
          <div v-if="isLanguageMenuOpen" class="absolute top-full mt-2 w-full rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 py-1 shadow-lg z-50">
            <button
              v-for="l in availableLocales"
              :key="l.code"
              @click="changeLanguage(l.code)"
              class="w-full px-3 py-2 text-left text-[13.5px] hover:bg-slate-50 dark:hover:bg-slate-700 text-[#787878] dark:text-slate-200"
              :class="{ 'font-semibold text-[#0873b9] dark:text-[#0873b9]': locale === l.code }"
            >
              {{ l.name }}
            </button>
          </div>
        </div>

        <!-- Enroll Now — same design token as hero Get Started -->
        <button
          type="button"
          class="cta-shimmer hidden sm:inline-flex h-9 shrink-0 items-center justify-center rounded-xl bg-gradient-to-r from-[#0873b9] to-[#0873b9] px-5 text-[14px] font-semibold text-white shadow-[0_3px_10px_rgba(8,115,185,0.35)] transition-all duration-[250ms] hover:scale-[1.03] hover:shadow-[0_5px_16px_rgba(8,115,185,0.45)] active:scale-[0.97] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#0873b9]/50 focus-visible:ring-offset-1"
        >
          {{ t('nav.enroll_now') }}
        </button>

        <!-- Hamburger (visible below lg) -->
        <button
          type="button"
          aria-label="Toggle menu"
          class="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg text-[#808080] dark:text-slate-300 transition-colors hover:bg-slate-100 dark:hover:bg-slate-800 lg:hidden"
          @click="isMobileOpen = !isMobileOpen"
        >
          <X v-if="isMobileOpen" class="h-5 w-5" />
          <Menu v-else class="h-5 w-5" />
        </button>
      </div>
    </div>

    <!-- ════════════════ MOBILE DRAWER ════════════════ -->
    <Transition name="drawer-slide">
    <div
      v-show="isMobileOpen"
      class="border-t border-slate-100 dark:border-slate-800 bg-white dark:bg-[#021E31] px-6 pb-5 pt-3 lg:hidden transition-colors duration-300"
    >
      <!-- Mobile nav links -->
      <nav aria-label="Mobile navigation">
        <ul class="flex flex-col gap-y-0.5">
          <li v-for="item in navigationItems" :key="item.sectionId">
            <button
              type="button"
              :aria-current="activeNavigation === item.sectionId ? 'page' : undefined"
              class="w-full rounded-xl px-4 py-2.5 text-left text-[15px] transition-all duration-150 focus-visible:outline-none"
              :class="
                activeNavigation === item.sectionId
                  ? 'bg-[#0873b9]/10 font-semibold text-[#0873b9] dark:text-[#0873b9]'
                  : 'font-medium text-[#666] dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-800/50 hover:text-[#0873b9] dark:hover:text-[#0873b9]'
              "
              @click="handleNavClick(item)"
            >
              {{ t(item.translationKey) }}
            </button>
          </li>
        </ul>
      </nav>

      <!-- Mobile: language + enroll -->
      <div class="mt-4 flex flex-col gap-2 border-t border-slate-100 dark:border-slate-800 pt-4">
        <div class="flex items-center gap-2">
          <button
            type="button"
            @click="isLanguageMenuOpen = !isLanguageMenuOpen"
            class="flex h-9 flex-1 items-center gap-1.5 rounded-lg border border-[#d1d1d1] dark:border-slate-700 bg-white dark:bg-slate-800 px-3 text-[13.5px] font-medium text-[#787878] dark:text-slate-200"
          >
            <Globe class="h-[15px] w-[15px] shrink-0" />
            <span class="flex-1 text-left">{{ getLocaleName(locale) }}</span>
            <ChevronDown class="h-3 w-3 shrink-0 opacity-50" />
          </button>
          <button
            type="button"
            class="inline-flex h-9 flex-1 items-center justify-center rounded-[12px] bg-gradient-to-r from-[#0873b9] to-[#0873b9] px-5 text-[14px] font-semibold text-white shadow-[0_3px_10px_rgba(8,115,185,0.3)]"
          >
            {{ t('nav.enroll_now') }}
          </button>
        </div>
        
        <div v-if="isLanguageMenuOpen" class="w-full rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 py-1 shadow-lg">
          <button
            v-for="l in availableLocales"
            :key="l.code"
            @click="changeLanguage(l.code)"
            class="w-full px-3 py-2 text-left text-[13.5px] hover:bg-slate-50 dark:hover:bg-slate-700 text-[#787878] dark:text-slate-200"
            :class="{ 'font-semibold text-[#0873b9] dark:text-[#0873b9]': locale === l.code }"
          >
            {{ l.name }}
          </button>
        </div>
      </div>
    </div>
    </Transition>

  </header>
</template>

<style scoped>
/* ── Active nav pill: matches "( Home )" bracket style from design ── */
.nav-link {
  border-radius: 20px;
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}

.active-nav {
  background-color: transparent;
  /* Left arc bracket */
  box-shadow:
    -10px 0 0 -2px transparent,
     10px 0 0 -2px transparent;
  border-left:  2.5px solid #0873b9;
  border-right: 2.5px solid #0873b9;
  border-top:    none;
  border-bottom: none;
  border-radius: 50% / 8px;
  padding-left:  18px;
  padding-right: 18px;
  transition: all 0.35s cubic-bezier(0.16, 1, 0.3, 1);
}

/* Inactive hover tint */
.nav-link:not(.active-nav):hover {
  background-color: rgba(60, 141, 253, 0.06);
  border-radius: 20px;
}

/* ── Dark mode icon rotation transition ── */
.icon-rotate-enter-active,
.icon-rotate-leave-active {
  transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.icon-rotate-enter-from {
  opacity: 0;
  transform: rotate(-90deg) scale(0.7);
}
.icon-rotate-leave-to {
  opacity: 0;
  transform: rotate(90deg) scale(0.7);
}

/* ── Mobile drawer slide transition ── */
.drawer-slide-enter-active {
  transition: all 0.35s cubic-bezier(0.16, 1, 0.3, 1);
}
.drawer-slide-leave-active {
  transition: all 0.25s ease-in;
}
.drawer-slide-enter-from,
.drawer-slide-leave-to {
  opacity: 0;
  transform: translateY(-12px);
  max-height: 0;
  overflow: hidden;
}
.drawer-slide-enter-to,
.drawer-slide-leave-from {
  opacity: 1;
  transform: translateY(0);
  max-height: 500px;
}
</style>
