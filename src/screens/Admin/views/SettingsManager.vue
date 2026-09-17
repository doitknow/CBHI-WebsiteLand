<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  Save,
  RefreshCw,
  CheckCircle2,
  AlertCircle,
  Database,
  Phone,
  Mail,
  MapPin,
  Lock,
  KeyRound,
  ShieldCheck,
  Loader2,
  Clock,
  Settings,
  Sparkles,
  User
} from "lucide-vue-next";
import cmsService from "../../../services/cmsService";
import useAuth from "../../../services/auth";

const isLoading = ref(true);
const isSaving = ref(false);
const message = ref<{ text: string; type: "success" | "error" } | null>(null);

const settings = ref<Record<string, string>>({
  "contact.phone": "+251 911 000 000",
  "contact.support_hours": "24/7 Support",
  "contact.email": "info@edhn.dh.moh.gov.et",
  "contact.address": "Addis Ababa, Ethiopia",
});

const loadSettings = async () => {
  isLoading.value = true;
  message.value = null;
  try {
    const data = await cmsService.getSettings();
    if (data) {
      settings.value = { ...settings.value, ...data };
    }
  } catch (err) {
    console.warn("Using default settings cache");
  } finally {
    isLoading.value = false;
  }
};

const saveSettings = async () => {
  isSaving.value = true;
  message.value = null;
  try {
    await cmsService.updateSettings(settings.value);
    message.value = { text: "Portal settings saved successfully in PostgreSQL!", type: "success" };
  } catch (err: any) {
    message.value = { text: err?.message || "Failed to update settings.", type: "error" };
  } finally {
    isSaving.value = false;
  }
};

const { user, changePassword, updateProfile, verifySession } = useAuth();
const currentPassword = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");
const isChangingPassword = ref(false);
const passwordMessage = ref<{ text: string; type: "success" | "error" } | null>(null);

const adminEmail = ref(user.value?.email || "bereketmussie281@gmail.com");
const isUpdatingEmail = ref(false);
const emailMessage = ref<{ text: string; type: "success" | "error" } | null>(null);

const handleUpdateEmail = async () => {
  emailMessage.value = null;
  if (!adminEmail.value || !adminEmail.value.includes("@")) {
    emailMessage.value = { text: "Please enter a valid email address.", type: "error" };
    return;
  }
  isUpdatingEmail.value = true;
  try {
    await updateProfile(adminEmail.value.trim());
    emailMessage.value = { text: `Administrator email updated in PostgreSQL! Password recovery will dispatch to ${adminEmail.value}.`, type: "success" };
  } catch (err: any) {
    emailMessage.value = { text: err?.message || "Failed to update administrator email.", type: "error" };
  } finally {
    isUpdatingEmail.value = false;
  }
};

const handleChangePassword = async () => {
  passwordMessage.value = null;

  if (!currentPassword.value || !newPassword.value) {
    passwordMessage.value = { text: "Please provide both current and new passwords.", type: "error" };
    return;
  }

  if (newPassword.value.length < 6) {
    passwordMessage.value = { text: "New password must be at least 6 characters long.", type: "error" };
    return;
  }

  if (newPassword.value !== confirmNewPassword.value) {
    passwordMessage.value = { text: "New password and confirmation do not match.", type: "error" };
    return;
  }

  isChangingPassword.value = true;
  try {
    await changePassword(currentPassword.value, newPassword.value);
    passwordMessage.value = { text: "Administrator password updated successfully in PostgreSQL!", type: "success" };
    currentPassword.value = "";
    newPassword.value = "";
    confirmNewPassword.value = "";
  } catch (err: any) {
    passwordMessage.value = { text: err?.message || "Failed to change password.", type: "error" };
  } finally {
    isChangingPassword.value = false;
  }
};

onMounted(async () => {
  loadSettings();
  try {
    await verifySession();
    if (user.value?.email) {
      adminEmail.value = user.value.email;
    }
  } catch {}
});
</script>

<template>
  <div class="space-y-7">
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 p-6 sm:p-7 rounded-3xl bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] text-white flex items-center justify-center shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] shrink-0">
          <Settings class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center gap-2">
            <h1 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight">
              Organization &amp; Portal Settings
            </h1>
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-black uppercase tracking-wider bg-[rgba(var(--color-primary-rgb),0.1)] text-[var(--color-primary)]">
              System Config
            </span>
          </div>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Configure global contact phone lines, support channels, and administrator authentication credentials
          </p>
        </div>
      </div>

      <div class="flex items-center gap-2.5 shrink-0 self-start sm:self-auto">
        <button
          @click="loadSettings"
          class="p-2.5 rounded-xl border border-slate-200/80 dark:border-white/[0.08] hover:bg-slate-100 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="Refresh Settings"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': isLoading }" />
        </button>
        <button
          @click="saveSettings"
          :disabled="isSaving"
          class="px-5 py-2.5 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-lg shadow-[rgba(var(--color-primary-rgb),0.3)] disabled:opacity-60 transition-all"
        >
          <Save class="w-4 h-4" />
          <span>{{ isSaving ? "Saving..." : "Save Settings" }}</span>
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

    <!-- Settings Workspace Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-7">
      <!-- 1. Contact & Support Information -->
      <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
        <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06] flex items-center justify-between">
          <div>
            <h2 class="text-sm font-black text-slate-900 dark:text-white uppercase tracking-wider flex items-center gap-2">
              <Phone class="w-4 h-4 text-[var(--color-primary)]" />
              Public Contact &amp; Support Info
            </h2>
            <p class="text-xs text-slate-400 mt-0.5">Displayed across header banners and footer sections</p>
          </div>
        </div>

        <div class="space-y-4">
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
              <Phone class="w-3.5 h-3.5 text-[var(--color-primary)]" />
              Direct Support Phone Line
            </label>
            <input
              v-model="settings['contact.phone']"
              type="text"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
              <Clock class="w-3.5 h-3.5 text-[var(--color-primary)]" />
              Operating Support Hours Tag
            </label>
            <input
              v-model="settings['contact.support_hours']"
              type="text"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
              <Mail class="w-3.5 h-3.5 text-[var(--color-primary)]" />
              Official Support Email
            </label>
            <input
              v-model="settings['contact.email']"
              type="email"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
              <MapPin class="w-3.5 h-3.5 text-[var(--color-primary)]" />
              Headquarters Location
            </label>
            <input
              v-model="settings['contact.address']"
              type="text"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>
        </div>
      </div>

      <!-- 2. Administrator Account & Security -->
      <div class="space-y-7">
        <!-- Admin Profile & Recovery Email -->
        <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
          <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06] flex items-center justify-between">
            <div>
              <h2 class="text-sm font-black text-slate-900 dark:text-white uppercase tracking-wider flex items-center gap-2">
                <ShieldCheck class="w-4 h-4 text-[var(--color-primary)]" />
                Administrator Account &amp; Recovery
              </h2>
              <p class="text-xs text-slate-400 mt-0.5">Active admin identity and password reset dispatch address</p>
            </div>
            <span class="px-2.5 py-1 rounded-full text-[10px] font-black uppercase tracking-wider bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-500/20">
              ROLE_ADMIN
            </span>
          </div>

          <!-- Email status message -->
          <div
            v-if="emailMessage"
            class="p-4 rounded-2xl flex items-center gap-3 text-xs font-bold shadow-sm"
            :class="emailMessage.type === 'success' ? 'bg-emerald-50 text-emerald-800 border border-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300 dark:border-emerald-800' : 'bg-red-50 text-red-800 border border-red-200 dark:bg-red-950/40 dark:text-red-300 dark:border-red-800'"
          >
            <CheckCircle2 v-if="emailMessage.type === 'success'" class="w-4 h-4 shrink-0 text-emerald-600" />
            <AlertCircle v-else class="w-4 h-4 shrink-0 text-red-600" />
            <span>{{ emailMessage.text }}</span>
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
                <User class="w-3.5 h-3.5 text-slate-400" />
                Admin Username
              </label>
              <input
                :value="user?.username || 'admin'"
                disabled
                type="text"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-100/80 dark:bg-white/[0.04] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-500 dark:text-slate-400 cursor-not-allowed"
              />
            </div>

            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center justify-between">
                <span class="flex items-center gap-1.5">
                  <Mail class="w-3.5 h-3.5 text-[var(--color-primary)]" />
                  Admin Recovery Email
                </span>
                <span class="text-[10px] text-[var(--color-primary)] font-bold">Password Reset Dispatch</span>
              </label>
              <div class="flex gap-2">
                <input
                  v-model="adminEmail"
                  type="email"
                  placeholder="e.g. bereketmussie281@gmail.com"
                  class="flex-1 px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
                />
                <button
                  type="button"
                  @click="handleUpdateEmail"
                  :disabled="isUpdatingEmail"
                  class="px-4 py-2.5 rounded-xl bg-[var(--color-primary)] hover:opacity-95 text-white font-extrabold text-xs flex items-center gap-2 shadow-md shadow-[rgba(var(--color-primary-rgb),0.25)] disabled:opacity-50 transition-all shrink-0"
                >
                  <Loader2 v-if="isUpdatingEmail" class="w-3.5 h-3.5 animate-spin" />
                  <Save v-else class="w-3.5 h-3.5" />
                  <span>{{ isUpdatingEmail ? "Saving..." : "Update Email" }}</span>
                </button>
              </div>
              <p class="text-[11px] text-slate-400 mt-1">
                Password recovery links and one-time verification tokens will be dispatched to this email address.
              </p>
            </div>
          </div>
        </div>

        <!-- Password Change Card -->
        <div class="p-6 sm:p-7 bg-white/90 dark:bg-[#031d33]/90 backdrop-blur-xl rounded-3xl border border-slate-200/80 dark:border-white/[0.08] shadow-sm space-y-5">
          <div class="pb-3.5 border-b border-slate-100 dark:border-white/[0.06] flex items-center justify-between">
            <div>
              <h2 class="text-sm font-black text-slate-900 dark:text-white uppercase tracking-wider flex items-center gap-2">
                <ShieldCheck class="w-4 h-4 text-emerald-500" />
                Administrator Password &amp; Security
              </h2>
              <p class="text-xs text-slate-400 mt-0.5">Secure authentication credentials stored with BCrypt hashing</p>
            </div>
          </div>

        <div
          v-if="passwordMessage"
          class="p-4 rounded-2xl flex items-center gap-3 text-xs font-bold"
          :class="passwordMessage.type === 'success' ? 'bg-emerald-50 text-emerald-800 border border-emerald-200 dark:bg-emerald-950/40 dark:text-emerald-300' : 'bg-red-50 text-red-800 border border-red-200 dark:bg-red-950/40 dark:text-red-300'"
        >
          <CheckCircle2 v-if="passwordMessage.type === 'success'" class="w-4 h-4 shrink-0 text-emerald-600" />
          <AlertCircle v-else class="w-4 h-4 shrink-0 text-red-600" />
          <span>{{ passwordMessage.text }}</span>
        </div>

        <form @submit.prevent="handleChangePassword" class="space-y-4">
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
              <Lock class="w-3.5 h-3.5 text-slate-400" />
              Current Password
            </label>
            <input
              v-model="currentPassword"
              type="password"
              placeholder="••••••••••••"
              class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
            />
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3.5">
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
                <KeyRound class="w-3.5 h-3.5 text-emerald-500" />
                New Password
              </label>
              <input
                v-model="newPassword"
                type="password"
                placeholder="Min 6 chars"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
              />
            </div>

            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 mb-1.5 flex items-center gap-1.5">
                <KeyRound class="w-3.5 h-3.5 text-emerald-500" />
                Confirm New Password
              </label>
              <input
                v-model="confirmNewPassword"
                type="password"
                placeholder="Re-enter password"
                class="w-full px-3.5 py-2.5 rounded-xl bg-slate-50/70 dark:bg-white/[0.02] border border-slate-200/80 dark:border-white/[0.08] text-xs font-bold text-slate-900 dark:text-white focus:ring-2 focus:ring-[var(--color-primary)] focus:outline-none"
              />
            </div>
          </div>

          <button
            type="submit"
            :disabled="isChangingPassword"
            class="w-full py-3 rounded-2xl bg-gradient-to-r from-emerald-600 to-teal-600 hover:opacity-95 text-white font-extrabold text-xs flex items-center justify-center gap-2 shadow-lg shadow-emerald-500/25 disabled:opacity-50 transition-all mt-2"
          >
            <Loader2 v-if="isChangingPassword" class="w-4 h-4 animate-spin" />
            <KeyRound v-else class="w-4 h-4" />
            <span>{{ isChangingPassword ? "Updating Password..." : "Change Admin Password" }}</span>
          </button>
        </form>
      </div>
    </div>
  </div>
</div>
</template>
