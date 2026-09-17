<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { KeyRound, Lock, Eye, EyeOff, ShieldCheck, ArrowLeft, CheckCircle2, AlertCircle, Loader2 } from "lucide-vue-next";
import useAuth from "../../services/auth";

const router = useRouter();
const route = useRoute();
const { resetPassword, isLoading } = useAuth();

const token = ref("");
const newPassword = ref("");
const confirmPassword = ref("");
const showPassword = ref(false);

const error = ref<string | null>(null);
const isSuccess = ref(false);

onMounted(() => {
  if (route.query.otp) {
    token.value = (route.query.otp as string).trim();
  } else if (route.query.token) {
    token.value = (route.query.token as string).trim();
  }
});

const handleReset = async () => {
  error.value = null;

  if (!token.value) {
    error.value = "Please enter the 6-digit OTP code sent to your email.";
    return;
  }

  if (!newPassword.value) {
    error.value = "Please enter a new password.";
    return;
  }

  if (newPassword.value.length < 6) {
    error.value = "Password must be at least 6 characters in length.";
    return;
  }

  if (newPassword.value !== confirmPassword.value) {
    error.value = "New password and confirmation password do not match.";
    return;
  }

  try {
    await resetPassword(token.value.trim(), newPassword.value);
    isSuccess.value = true;
  } catch (err: any) {
    error.value = err?.message || "Failed to reset password. The OTP code may be invalid or expired.";
  }
};
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#02182b] via-[#022742] to-[#043d63] p-4 sm:p-6 relative overflow-hidden">
    <!-- Ambient background glow -->
    <div class="absolute -top-40 -right-40 w-96 h-96 bg-[var(--color-primary)]/25 rounded-full blur-3xl pointer-events-none"></div>
    <div class="absolute -bottom-40 -left-40 w-96 h-96 bg-[var(--color-secondary)]/35 rounded-full blur-3xl pointer-events-none"></div>

    <div class="relative w-full max-w-md">
      <!-- Back Link -->
      <router-link
        to="/admin/login"
        class="inline-flex items-center gap-2 text-xs font-semibold text-slate-300 hover:text-white mb-6 transition-colors group"
      >
        <ArrowLeft class="w-4 h-4 transition-transform group-hover:-translate-x-1" />
        Return to CMS Login
      </router-link>

      <!-- Main Card -->
      <div class="bg-white/95 dark:bg-[#032035]/95 backdrop-blur-xl border border-white/20 dark:border-white/10 rounded-2xl sm:rounded-3xl shadow-2xl p-6 sm:p-8">
        <!-- Success State -->
        <div v-if="isSuccess" class="text-center py-6 space-y-4">
          <div class="w-16 h-16 rounded-full bg-emerald-100 dark:bg-emerald-950/60 text-emerald-600 dark:text-emerald-400 flex items-center justify-center mx-auto shadow-md">
            <CheckCircle2 class="w-10 h-10" />
          </div>
          <div>
            <h2 class="text-xl font-extrabold text-slate-900 dark:text-white">Password Reset Complete</h2>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">
              Your administrative credentials have been successfully updated in PostgreSQL. You can now log into the CMS.
            </p>
          </div>
          <router-link
            to="/admin/login"
            class="inline-flex w-full justify-center items-center py-3 px-4 rounded-xl bg-[var(--color-primary)] hover:opacity-90 text-white font-bold text-xs shadow-lg transition-all"
          >
            Sign in with New Password
          </router-link>
        </div>

        <!-- Form State -->
        <div v-else>
          <div class="flex flex-col items-center text-center mb-6">
            <div class="w-12 h-12 rounded-2xl bg-gradient-to-tr from-[var(--color-primary)] to-[var(--color-secondary)] flex items-center justify-center text-white shadow-lg mb-3">
              <KeyRound class="w-6 h-6" />
            </div>
            <span class="text-[10px] font-bold tracking-widest text-[var(--color-primary)] uppercase">Security Recovery</span>
            <h1 class="text-2xl font-black text-slate-900 dark:text-white tracking-tight mt-0.5">Reset Admin Password</h1>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">
              Enter the 6-digit OTP code sent to your registered email (<span class="font-mono text-[var(--color-primary)] font-semibold">bereketmussie281@gmail.com</span>)
            </p>
          </div>

          <!-- Error Alert -->
          <div
            v-if="error"
            class="mb-5 flex items-start gap-3 p-3.5 rounded-xl bg-red-50 dark:bg-red-950/40 border border-red-200 dark:border-red-900/50 text-red-700 dark:text-red-300 text-xs"
          >
            <AlertCircle class="w-4 h-4 shrink-0 mt-0.5 text-red-600 dark:text-red-400" />
            <span>{{ error }}</span>
          </div>

          <form @submit.prevent="handleReset" class="space-y-4">
            <!-- 6-Digit OTP Input -->
            <div>
              <div class="flex items-center justify-between mb-1.5">
                <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider">
                  6-Digit OTP Code
                </label>
                <span class="text-[10px] font-semibold text-[var(--color-primary)]">Dispatched via Email</span>
              </div>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <KeyRound class="w-4 h-4" />
                </div>
                <input
                  v-model="token"
                  type="text"
                  required
                  maxlength="6"
                  placeholder="Enter 6-digit OTP from email"
                  class="w-full pl-10 pr-4 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-base font-mono font-bold tracking-widest text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] text-center"
                />
              </div>
              <p class="text-[11px] text-slate-400 mt-1">
                Check your inbox or spam folder for the one-time code. Valid for 15 minutes.
              </p>
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
                  v-model="newPassword"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  autocomplete="new-password"
                  placeholder="At least 6 characters"
                  class="w-full pl-10 pr-10 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)]"
                />
                <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-slate-600"
                >
                  <EyeOff v-if="showPassword" class="w-4 h-4" />
                  <Eye v-else class="w-4 h-4" />
                </button>
              </div>
            </div>

            <!-- Confirm Password -->
            <div>
              <label class="block text-xs font-bold text-slate-700 dark:text-slate-300 uppercase tracking-wider mb-1.5">
                Confirm New Password
              </label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
                  <Lock class="w-4 h-4" />
                </div>
                <input
                  v-model="confirmPassword"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  autocomplete="new-password"
                  placeholder="Re-enter password"
                  class="w-full pl-10 pr-4 py-2.5 bg-slate-50 dark:bg-[#02182b] border border-slate-200 dark:border-white/10 rounded-xl text-xs text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)]"
                />
              </div>
            </div>

            <button
              type="submit"
              :disabled="isLoading"
              class="w-full mt-2 py-3 px-4 rounded-xl bg-[var(--color-primary)] hover:opacity-90 text-white font-bold text-xs flex items-center justify-center gap-2 shadow-lg shadow-blue-500/20 disabled:opacity-60 transition-all"
            >
              <Loader2 v-if="isLoading" class="w-4 h-4 animate-spin" />
              <ShieldCheck v-else class="w-4 h-4" />
              <span>{{ isLoading ? "Updating Password..." : "Set New Password" }}</span>
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
