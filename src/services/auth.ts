import { reactive, computed } from "vue";
import api, { ApiError } from "./api";

export interface UserProfile {
  username: string;
  email: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  username: string;
  email: string;
  role: string;
}

interface AuthState {
  token: string | null;
  user: UserProfile | null;
  isLoading: boolean;
  error: string | null;
}

const savedToken = localStorage.getItem("cbhi_auth_token");
let savedUser: UserProfile | null = null;
try {
  const raw = localStorage.getItem("cbhi_auth_user");
  if (raw) savedUser = JSON.parse(raw);
} catch {
  savedUser = null;
}

const state = reactive<AuthState>({
  token: savedToken,
  user: savedUser,
  isLoading: false,
  error: null,
});

export const useAuth = () => {
  const isAuthenticated = computed(() => !!state.token);

  const login = async (username: string, password: string): Promise<boolean> => {
    state.isLoading = true;
    state.error = null;

    try {
      const response = await api.post<AuthResponse>("/api/auth/login", {
        username,
        password,
      });

      state.token = response.token;
      state.user = {
        username: response.username,
        email: response.email,
        role: response.role,
      };

      localStorage.setItem("cbhi_auth_token", response.token);
      localStorage.setItem("cbhi_auth_user", JSON.stringify(state.user));

      return true;
    } catch (err: any) {
      const message =
        err instanceof ApiError ? err.message : "Invalid username or password";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  const logout = () => {
    state.token = null;
    state.user = null;
    state.error = null;
    localStorage.removeItem("cbhi_auth_token");
    localStorage.removeItem("cbhi_auth_user");
  };

  const verifySession = async () => {
    if (!state.token) return false;
    try {
      const user = await api.get<UserProfile>("/api/auth/me");
      state.user = user;
      localStorage.setItem("cbhi_auth_user", JSON.stringify(user));
      return true;
    } catch {
      logout();
      return false;
    }
  };

  const changePassword = async (currentPassword: string, newPassword: string): Promise<boolean> => {
    state.isLoading = true;
    state.error = null;
    try {
      await api.post("/api/auth/change-password", { currentPassword, newPassword });
      return true;
    } catch (err: any) {
      const message = err instanceof ApiError ? err.message : "Failed to change password";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  const forgotPassword = async (emailOrUsername: string): Promise<any> => {
    state.isLoading = true;
    state.error = null;
    try {
      const originUrl = window.location.origin;
      const res = await api.post("/api/auth/forgot-password", { emailOrUsername, originUrl });
      return res;
    } catch (err: any) {
      const message = err instanceof ApiError ? err.message : "Failed to request password reset";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  const resetPassword = async (token: string, newPassword: string): Promise<boolean> => {
    state.isLoading = true;
    state.error = null;
    try {
      await api.post("/api/auth/reset-password", { token, newPassword });
      return true;
    } catch (err: any) {
      const message = err instanceof ApiError ? err.message : "Failed to reset password";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  const updateProfile = async (email: string): Promise<UserProfile> => {
    state.isLoading = true;
    state.error = null;
    try {
      const updated = await api.post<UserProfile>("/api/auth/update-profile", { email });
      if (state.user) {
        state.user.email = email;
      } else {
        state.user = updated;
      }
      localStorage.setItem("cbhi_auth_user", JSON.stringify(state.user));
      return updated;
    } catch (err: any) {
      const message = err instanceof ApiError ? err.message : "Failed to update profile email";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  const verifyOtp = async (otp: string): Promise<any> => {
    state.isLoading = true;
    state.error = null;
    try {
      const res = await api.post("/api/auth/verify-otp", { otp });
      return res;
    } catch (err: any) {
      const message = err instanceof ApiError ? err.message : "Invalid or expired OTP code";
      state.error = message;
      throw err;
    } finally {
      state.isLoading = false;
    }
  };

  return {
    state,
    user: computed(() => state.user),
    isAuthenticated,
    isLoading: computed(() => state.isLoading),
    error: computed(() => state.error),
    login,
    logout,
    verifySession,
    changePassword,
    forgotPassword,
    verifyOtp,
    resetPassword,
    updateProfile,
  };
};

export default useAuth;
