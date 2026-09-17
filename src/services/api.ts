// API Client for Spring Boot REST API
const BASE_URL = import.meta.env.VITE_API_URL !== undefined ? import.meta.env.VITE_API_URL : "";

export interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  data: T;
}

export class ApiError extends Error {
  status: number;
  data?: any;

  constructor(message: string, status: number, data?: any) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.data = data;
  }
}

export async function request<T = any>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const token = localStorage.getItem("cbhi_auth_token");

  const headers: Record<string, string> = {
    Accept: "application/json",
    ...((options.headers as Record<string, string>) || {}),
  };

  // Only set application/json if body is not FormData
  if (!(options.body instanceof FormData)) {
    headers["Content-Type"] = "application/json";
  }

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const normalizedEndpoint = endpoint.startsWith("/") ? endpoint : `/${endpoint}`;
  const url = BASE_URL ? `${BASE_URL}${normalizedEndpoint}` : normalizedEndpoint;

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });

    // If 401 Unauthorized or 403 Forbidden on admin endpoints, clear stale token
    if ((response.status === 401 || response.status === 403) && !endpoint.includes("/api/auth/login")) {
      localStorage.removeItem("cbhi_auth_token");
      localStorage.removeItem("cbhi_auth_user");
      if (window.location.pathname.startsWith("/admin") && window.location.pathname !== "/admin/login") {
        window.location.href = "/admin/login?expired=1";
      }
    }

    const text = await response.text().catch(() => "");
    let json: any = null;
    if (text) {
      try {
        json = JSON.parse(text);
      } catch {
        json = null;
      }
    }

    if (!response.ok) {
      const errMsg =
        json?.message ||
        (response.status === 413 ? "File is too large (maximum size is 50MB). Please select a smaller file or compress the image." : null) ||
        (response.status === 403 ? "Access Forbidden. Your admin session may have expired or lacks permission. Please log in again." : null) ||
        (response.status === 401 ? "Unauthorized. Please log in to your admin account." : null) ||
        json?.error ||
        (typeof json === "string" ? json : null) ||
        (text && text.length < 200 && !text.includes("<html") ? text : null) ||
        `Request failed with status ${response.status}`;
      throw new ApiError(errMsg, response.status, json);
    }

    if (json && typeof json === "object" && json.success === false) {
      throw new ApiError(json.message || "Operation failed", response.status, json);
    }

    if (json && typeof json === "object" && "data" in json) {
      return json.data as T;
    }

    return (json !== null ? json : text) as T;
  } catch (err: any) {
    if (err instanceof ApiError) throw err;
    throw new ApiError(err.message || "Network error. Please ensure backend is running.", 0);
  }
}

export const api = {
  get: <T = any>(endpoint: string, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: "GET" }),
  post: <T = any>(endpoint: string, body?: any, options?: RequestInit) =>
    request<T>(endpoint, {
      ...options,
      method: "POST",
      body: body instanceof FormData ? body : (body !== undefined ? JSON.stringify(body) : undefined),
    }),
  put: <T = any>(endpoint: string, body?: any, options?: RequestInit) =>
    request<T>(endpoint, {
      ...options,
      method: "PUT",
      body: body instanceof FormData ? body : (body !== undefined ? JSON.stringify(body) : undefined),
    }),
  delete: <T = any>(endpoint: string, options?: RequestInit) =>
    request<T>(endpoint, { ...options, method: "DELETE" }),
  upload: <T = any>(endpoint: string, formData: FormData, options?: RequestInit) =>
    request<T>(endpoint, {
      ...options,
      method: "POST",
      body: formData,
    }),
};

export default api;

