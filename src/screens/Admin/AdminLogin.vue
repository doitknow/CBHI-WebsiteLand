<script setup lang="ts">
import { ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import {
  Lock,
  User,
  Eye,
  EyeOff,
  ShieldCheck,
  ArrowLeft,
  Loader2,
  AlertCircle,
  Mail,
  CheckCircle2,
  X,
  KeyRound,
  ExternalLink
} from "lucide-vue-next";
import useAuth from "../../services/auth";

const router = useRouter();
const route = useRoute();
const { login, forgotPassword, resetPassword, isLoading, error } = useAuth();

const username = ref("admin");
const password = ref("Admin123!");
const showPassword = ref(false);
const localError = ref<string | null>(
  route.query.expired ? "Your session has expired. Please log in again." : null
);

// Forgot Password Modal State
const isForgotModalOpen = ref(false);
const forgotStep = ref<"request" | "verify">("request");
const forgotEmail = ref("bereketmussie281@gmail.com");
const isSendingForgot = ref(false);
const forgotSuccessMessage = ref<string | null>(null);
const forgotDevResetUrl = ref<string | null>(null);
const forgotError = ref<string | null>(null);

// In-modal OTP Reset State
const forgotOtp = ref("");
const forgotNewPassword = ref("");
const forgotConfirmPassword = ref("");
const showForgotNewPassword = ref(false);
const isResettingWithOtp = ref(false);
const resetOtpSuccess = ref(false);

const handleLogin = async () => {
  localError.value = null;
  if (!username.value || !password.value) {
    localError.value = "Please enter both username and password.";
    return;
  }

  try {
    const success = await login(username.value, password.value);
    if (success) {
      const redirect = (route.query.redirect as string) || "/admin/dashboard";
      router.push(redirect);
    }
  } catch (err: any) {
    localError.value = err?.message || "Invalid credentials. Please check your username and password.";
  }
};

const handleForgotSubmit = async () => {
  if (!forgotEmail.value) {
    forgotError.value = "Please enter your username or registered email address.";
    return;
  }

  isSendingForgot.value = true;
  forgotError.value = null;
  forgotSuccessMessage.value = null;
  forgotDevResetUrl.value = null;

  try {
    await forgotPassword(forgotEmail.value);
    forgotSuccessMessage.value = "A 6-digit OTP verification code has been dispatched to your email address.";
    forgotOtp.value = "";
    forgotDevResetUrl.value = null;
    forgotStep.value = "verify";
  } catch (err: any) {
    forgotError.value = err?.message || "Failed to dispatch recovery OTP.";
  } finally {
    isSendingForgot.value = false;
  }
};

const handleOtpResetSubmit = async () => {
  forgotError.value = null;

  if (!forgotOtp.value || forgotOtp.value.trim().length < 6) {
    forgotError.value = "Please enter the 6-digit OTP verification code.";
    return;
  }

  if (!forgotNewPassword.value || forgotNewPassword.value.length < 6) {
    forgotError.value = "New password must be at least 6 characters in length.";
    return;
  }

  if (forgotNewPassword.value !== forgotConfirmPassword.value) {
    forgotError.value = "New password and confirmation password do not match.";
    return;
  }

  isResettingWithOtp.value = true;
  try {
    await resetPassword(forgotOtp.value.trim(), forgotNewPassword.value);
    resetOtpSuccess.value = true;
    password.value = forgotNewPassword.value;
    setTimeout(() => {
      closeForgotModal();
    }, 2200);
  } catch (err: any) {
    forgotError.value = err?.message || "Failed to reset password. The OTP code may have expired or is incorrect.";
  } finally {
    isResettingWithOtp.value = false;
  }
};

const closeForgotModal = () => {
  isForgotModalOpen.value = false;
  forgotStep.value = "request";
  forgotSuccessMessage.value = null;
  forgotDevResetUrl.value = null;
  forgotError.value = null;
  forgotOtp.value = "";
  forgotNewPassword.value = "";
  forgotConfirmPassword.value = "";
  resetOtpSuccess.value = false;
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#02182b] via-[#022742] to-[#043d63] p-4 sm:p-6 relative overflow-hidden">
    <!-- Ambient glowing backgrounds -->
    <div class="absolute -top-40 -right-40 w-96 h-96 bg-[var(--color-primary)]/25 rounded-full blur-3xl pointer-events-none"></div>
    <div class="absolute -bottom-40 -left-40 w-96 h-96 bg-[var(--color-secondary)]/35 rounded-full blur-3xl pointer-events-none"></div>

    <div class="relative w-full max-w-md">
      <!-- Back to website link -->
      <router-link
        to="/"
        class="inline-flex items-center gap-2 text-xs font-semibold text-slate-300 hover:text-white mb-6 transition-colors group"
      >
        <ArrowLeft class="w-4 h-4 transition-transform group-hover:-translate-x-1" />
        Return to CBHI Public Website
      </router-link>

      <!-- Main Login Card -->
      <div class="bg-white/95 dark:bg-[#032035]/95 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl sm:rounded-3xl shadow-2xl p-6 sm:p-8">
        <!-- Logo & Header -->
        <div class="flex flex-col items-center text-center mb-8">
          <div class="w-14 h-14 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] flex items-center justify-center text-white shadow-lg shadow-blue-500/30 mb-4 ring-4 ring-blue-500/15">
            <ShieldCheck class="w-8 h-8" />
          </div>
          <span class="text-[11px] font-bold tracking-widest text-[var(--color-primary)] uppercase mb-1">
            Official Administrative Portal
          </span>
          <h1 class="text-2xl sm:text-3xl font-black text-slate-900 dark:text-white tracking-tight">
            CBHI CMS Portal
          </h1>
          <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400 mt-1">
            Community Based Health Insurance Management System
          </p>
        </div>

        <!-- Error Notification -->
        <div
          v-if="localError || error"
          class="mb-6 flex items-start gap-3 p-3.5 rounded-xl bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-900/50 text-red-700 dark:text-red-300 text-xs animate-shake"
        >
          <AlertCircle class="w-4 h-4 shrink-0 mt-0.5 text-red-600 dark:text-red-400" />
          <span>{{ localError || error }}</span>
        </div>

        <!-- Form -->
        <form @submit.prevent="handleLogin" class="space-y-4 sm:space-y-5">
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-2">
              Admin Username
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <User class="w-4 h-4" />
              </div>
              <input
                v-model="username"
                type="text"
                required
                autocomplete="username"
                placeholder="e.g. admin"
                class="w-full pl-10 pr-4 py-2.5 sm:py-3 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-sm text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] focus:border-transparent transition-all"
              />
            </div>
          </div>

          <div>
            <div class="flex items-center justify-between mb-2">
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider">
                Password
              </label>
              <button
                type="button"
                @click="isForgotModalOpen = true"
                class="text-xs font-bold text-[var(--color-primary)] hover:underline transition-colors"
              >
                Forgot Password?
              </button>
            </div>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <Lock class="w-4 h-4" />
              </div>
              <input
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                required
                autocomplete="current-password"
                placeholder="••••••••••••"
                class="w-full pl-10 pr-11 py-2.5 sm:py-3 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-sm text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] focus:border-transparent transition-all"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute inset-y-0 right-0 pr-3.5 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 transition-colors"
                tabindex="-1"
              >
                <EyeOff v-if="showPassword" class="w-4 h-4" />
                <Eye v-else class="w-4 h-4" />
              </button>
            </div>
          </div>

          <div class="pt-2">
            <button
              type="submit"
              :disabled="isLoading"
              class="w-full py-3 px-4 rounded-xl bg-gradient-to-r from-[var(--color-primary)] to-[var(--color-secondary)] hover:opacity-95 text-white font-bold text-sm tracking-wide shadow-lg shadow-blue-500/25 transition-all duration-200 hover:shadow-xl hover:-translate-y-0.5 active:translate-y-0 disabled:opacity-60 disabled:cursor-not-allowed flex items-center justify-center gap-2"
            >
              <Loader2 v-if="isLoading" class="w-4 h-4 animate-spin" />
              <span>{{ isLoading ? "Authenticating..." : "Sign In to CMS" }}</span>
            </button>
          </div>
        </form>

        <!-- Pre-filled test credentials helper -->
        <div class="mt-6 pt-5 border-t border-slate-100 dark:border-white/5 text-center space-y-1">
          <p class="text-[11px] text-slate-500 dark:text-slate-400">
            Credentials: <span class="font-mono font-semibold text-blue-600 dark:text-blue-400">admin</span> / <span class="font-mono font-semibold text-blue-600 dark:text-blue-400">Admin123!</span>
          </p>
          <p class="text-[10px] text-slate-400 dark:text-slate-500">
            Admin Recovery Email: <span class="font-mono font-semibold text-[var(--color-primary)]">bereketmussie281@gmail.com</span>
          </p>
        </div>
      </div>
    </div>

    <!-- Forgot Password Modal Dialog -->
    <div
      v-if="isForgotModalOpen"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-slate-900/70 backdrop-blur-sm animate-fade-in"
    >
      <div
        class="bg-white dark:bg-[#032035] rounded-3xl max-w-md w-full p-6 shadow-2xl border border-slate-200 dark:border-white/10 space-y-5 relative"
      >
        <!-- Modal Close Button -->
        <button
          @click="closeForgotModal"
          class="absolute top-5 right-5 p-2 rounded-xl text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
        >
          <X class="w-4 h-4" />
        </button>

        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-2xl bg-blue-50 dark:bg-blue-950/50 text-[var(--color-primary)] flex items-center justify-center">
            <KeyRound class="w-5 h-5" />
          </div>
          <div>
            <h3 class="text-base font-bold text-slate-900 dark:text-white">Recover Administrator Access</h3>
            <p class="text-xs text-slate-500">
              {{ forgotStep === 'request' ? 'Request 6-digit OTP code to your email' : 'Verify OTP code & set new password' }}
            </p>
          </div>
        </div>

        <!-- Success view after password reset -->
        <div v-if="resetOtpSuccess" class="space-y-4 py-4 text-center">
          <div class="w-14 h-14 rounded-full bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 flex items-center justify-center mx-auto shadow-md">
            <CheckCircle2 class="w-8 h-8" />
          </div>
          <div>
            <h4 class="text-base font-black text-slate-900 dark:text-white">Password Reset Successfully!</h4>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">
              Your new password has been saved in PostgreSQL. You can now sign in to the CMS portal.
            </p>
          </div>
        </div>

        <!-- Step 2: Verify OTP & Enter New Password -->
        <form v-else-if="forgotStep === 'verify'" @submit.prevent="handleOtpResetSubmit" class="space-y-4">
          <!-- Dispatched info alert -->
          <div class="p-3.5 rounded-2xl bg-emerald-50 dark:bg-emerald-950/40 border border-emerald-200 dark:border-emerald-900/50 text-emerald-800 dark:text-emerald-300 text-xs space-y-1.5">
            <div class="flex items-center gap-2 font-bold">
              <CheckCircle2 class="w-4 h-4 text-emerald-600 shrink-0" />
              <span>OTP Code Dispatched</span>
            </div>
            <p class="text-[11px] leading-relaxed">
              We sent a 6-digit verification code to <span class="font-bold underline">{{ forgotEmail }}</span>.
            </p>
            <p class="text-[11px] text-emerald-700/90 dark:text-emerald-300/90">
              Please open your Gmail inbox (<span class="font-bold underline">{{ forgotEmail }}</span>) or spam folder to retrieve your 6-digit verification code.
            </p>
          </div>

          <div
            v-if="forgotError"
            class="flex items-start gap-2 p-3 rounded-xl bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-900/50 text-red-700 dark:text-red-300 text-xs"
          >
            <AlertCircle class="w-4 h-4 shrink-0 mt-0.5" />
            <span>{{ forgotError }}</span>
          </div>

          <!-- 6-digit OTP code input -->
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-1.5 flex items-center justify-between">
              <span>6-Digit OTP Code</span>
              <span class="text-[10px] text-[var(--color-primary)] font-semibold">15 Min Expiry</span>
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <KeyRound class="w-4 h-4" />
              </div>
              <input
                v-model="forgotOtp"
                type="text"
                required
                maxlength="6"
                placeholder="Enter 6-digit OTP from email"
                class="w-full pl-10 pr-4 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-base font-mono font-black tracking-widest text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-center"
              />
            </div>
          </div>

          <!-- New Password -->
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-1.5">
              New Password
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <Lock class="w-4 h-4" />
              </div>
              <input
                v-model="forgotNewPassword"
                :type="showForgotNewPassword ? 'text' : 'password'"
                required
                autocomplete="new-password"
                placeholder="Min 6 characters"
                class="w-full pl-10 pr-10 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)]"
              />
              <button
                type="button"
                @click="showForgotNewPassword = !showForgotNewPassword"
                class="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-slate-600"
              >
                <EyeOff v-if="showForgotNewPassword" class="w-4 h-4" />
                <Eye v-else class="w-4 h-4" />
              </button>
            </div>
          </div>

          <!-- Confirm New Password -->
          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-1.5">
              Confirm New Password
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <Lock class="w-4 h-4" />
              </div>
              <input
                v-model="forgotConfirmPassword"
                :type="showForgotNewPassword ? 'text' : 'password'"
                required
                autocomplete="new-password"
                placeholder="Re-enter new password"
                class="w-full pl-10 pr-4 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)]"
              />
            </div>
          </div>

          <div class="flex items-center justify-between pt-2">
            <button
              type="button"
              @click="forgotStep = 'request'"
              class="text-xs text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 font-semibold"
            >
              ← Resend OTP
            </button>
            <div class="flex items-center gap-2">
              <button
                type="button"
                @click="closeForgotModal"
                class="px-3 py-2 rounded-xl border border-slate-200 dark:border-white/10 text-xs font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
              >
                Cancel
              </button>
              <button
                type="submit"
                :disabled="isResettingWithOtp"
                class="px-5 py-2.5 rounded-xl bg-[var(--color-primary)] hover:opacity-95 text-white text-xs font-bold flex items-center gap-2 shadow-md shadow-blue-500/20 disabled:opacity-60 transition-all"
              >
                <Loader2 v-if="isResettingWithOtp" class="w-4 h-4 animate-spin" />
                <ShieldCheck v-else class="w-4 h-4" />
                <span>{{ isResettingWithOtp ? "Updating..." : "Reset Password" }}</span>
              </button>
            </div>
          </div>
        </form>

        <!-- Step 1: Request OTP Form view -->
        <form v-else @submit.prevent="handleForgotSubmit" class="space-y-4">
          <div
            v-if="forgotError"
            class="flex items-start gap-2 p-3 rounded-xl bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-900/50 text-red-700 dark:text-red-300 text-xs"
          >
            <AlertCircle class="w-4 h-4 shrink-0 mt-0.5" />
            <span>{{ forgotError }}</span>
          </div>

          <div>
            <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-1.5">
              Admin Email or Username
            </label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                <Mail class="w-4 h-4" />
              </div>
              <input
                v-model="forgotEmail"
                type="text"
                required
                placeholder="e.g. bereketmussie281@gmail.com or admin"
                class="w-full pl-10 pr-4 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)]"
              />
            </div>
            <p class="text-[11px] text-slate-500 mt-1">
              A 6-digit numeric OTP code will be generated and dispatched to your email address.
            </p>
          </div>

          <div class="flex items-center justify-end gap-3 pt-2">
            <button
              type="button"
              @click="closeForgotModal"
              class="px-4 py-2.5 rounded-xl border border-slate-200 dark:border-white/10 text-xs font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              :disabled="isSendingForgot"
              class="px-5 py-2.5 rounded-xl bg-[var(--color-primary)] hover:opacity-90 text-white text-xs font-bold flex items-center gap-2 shadow-md shadow-blue-500/20 disabled:opacity-60 transition-all"
            >
              <Loader2 v-if="isSendingForgot" class="w-4 h-4 animate-spin" />
              <span>{{ isSendingForgot ? "Sending..." : "Send 6-Digit OTP" }}</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
