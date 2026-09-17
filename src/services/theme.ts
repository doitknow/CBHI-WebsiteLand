export interface ColorTheme {
  name: string;
  id: string;
  primary: string;
  secondary: string;
  accent: string;
  headerStart: string;
  headerEnd: string;
  description: string;
}

export const THEME_PRESETS: ColorTheme[] = [
  {
    id: "cbhi-blue",
    name: "CBHI Classic Blue (National Default)",
    primary: "#0873b9",
    secondary: "#044e82",
    accent: "#22c55e",
    headerStart: "#3173cf",
    headerEnd: "#245497",
    description: "Official Ethiopian Community Based Health Insurance brand palette",
  },
  {
    id: "emerald-health",
    name: "Ethiopian Emerald Healthcare",
    primary: "#059669",
    secondary: "#064e3b",
    accent: "#d97706",
    headerStart: "#10b981",
    headerEnd: "#047857",
    description: "Lush botanical health & wellness theme symbolizing life and renewal",
  },
  {
    id: "oceanic-rift",
    name: "Oceanic Rift Teal",
    primary: "#0284c7",
    secondary: "#0369a1",
    accent: "#14b8a6",
    headerStart: "#38bdf8",
    headerEnd: "#075985",
    description: "Modern, crisp deep-cyan and azure digital medical aesthetic",
  },
  {
    id: "royal-purple",
    name: "Royal Ministry Violet",
    primary: "#6366f1",
    secondary: "#3730a3",
    accent: "#ec4899",
    headerStart: "#818cf8",
    headerEnd: "#4338ca",
    description: "Dignified, high-tech governmental institutional tone",
  },
  {
    id: "crimson-care",
    name: "Crimson Solidarity Red",
    primary: "#dc2626",
    secondary: "#991b1b",
    accent: "#f59e0b",
    headerStart: "#ef4444",
    headerEnd: "#7f1d1d",
    description: "Urgent emergency medical care and vital life-saving protection",
  },
];

export const DEFAULT_THEME = THEME_PRESETS[0];

/**
 * Converts Hex (#RRGGBB) to RGB channels string "r, g, b"
 */
function hexToRgb(hex: string): string {
  hex = hex.replace("#", "");
  if (hex.length === 3) {
    hex = hex.split("").map((c) => c + c).join("");
  }
  const r = parseInt(hex.substring(0, 2), 16) || 8;
  const g = parseInt(hex.substring(2, 4), 16) || 115;
  const b = parseInt(hex.substring(4, 6), 16) || 185;
  return `${r}, ${g}, ${b}`;
}

/**
 * Converts Hex (#RRGGBB) to HSL string values (H S% L%) for Tailwind variables
 */
function hexToHsl(hex: string): string {
  hex = hex.replace("#", "");
  if (hex.length === 3) {
    hex = hex.split("").map((c) => c + c).join("");
  }
  const r = parseInt(hex.substring(0, 2), 16) / 255;
  const g = parseInt(hex.substring(2, 4), 16) / 255;
  const b = parseInt(hex.substring(4, 6), 16) / 255;

  const max = Math.max(r, g, b);
  const min = Math.min(r, g, b);
  let h = 0;
  let s = 0;
  const l = (max + min) / 2;

  if (max !== min) {
    const d = max - min;
    s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
    switch (max) {
      case r:
        h = (g - b) / d + (g < b ? 6 : 0);
        break;
      case g:
        h = (b - r) / d + 2;
        break;
      case b:
        h = (r - g) / d + 4;
        break;
    }
    h /= 6;
  }

  const hDeg = Math.round(h * 360);
  const sPct = Math.round(s * 100);
  const lPct = Math.round(l * 100);

  return `${hDeg} ${sPct}% ${lPct}%`;
}

/**
 * Injects color properties into the document root
 */
export function applyTheme(settings?: Record<string, string>) {
  if (typeof document === "undefined") return;

  const primary = settings?.["theme.primary_color"] || DEFAULT_THEME.primary;
  const secondary = settings?.["theme.secondary_color"] || DEFAULT_THEME.secondary;
  const accent = settings?.["theme.accent_color"] || DEFAULT_THEME.accent;
  const headerStart = settings?.["theme.header_gradient_start"] || DEFAULT_THEME.headerStart;
  const headerEnd = settings?.["theme.header_gradient_end"] || DEFAULT_THEME.headerEnd;

  const root = document.documentElement;

  root.style.setProperty("--color-primary", primary);
  root.style.setProperty("--color-primary-lt", primary);
  root.style.setProperty("--color-secondary", secondary);
  root.style.setProperty("--color-accent", accent);
  root.style.setProperty("--header-grad-start", headerStart);
  root.style.setProperty("--header-grad-end", headerEnd);

  const primaryRgb = hexToRgb(primary);
  const secondaryRgb = hexToRgb(secondary);
  const accentRgb = hexToRgb(accent);

  root.style.setProperty("--color-primary-rgb", primaryRgb);
  root.style.setProperty("--color-secondary-rgb", secondaryRgb);
  root.style.setProperty("--color-accent-rgb", accentRgb);

  try {
    const primaryHsl = hexToHsl(primary);
    root.style.setProperty("--primary", primaryHsl);
    const secondaryHsl = hexToHsl(secondary);
    root.style.setProperty("--secondary", secondaryHsl);
    const accentHsl = hexToHsl(accent);
    root.style.setProperty("--accent", accentHsl);
  } catch {
    // fallback safe
  }

  if (settings && typeof localStorage !== "undefined") {
    try {
      localStorage.setItem("cbhi_theme_settings", JSON.stringify(settings));
    } catch {
      // LocalStorage quota safe
    }
  }
}

/**
 * Initializes the theme immediately from local storage cache,
 * then queries the live backend for any new theme changes.
 */
export async function initTheme() {
  if (typeof window === "undefined") return;

  // 1. Instant hydration from cached settings
  try {
    const cached = localStorage.getItem("cbhi_theme_settings");
    if (cached) {
      applyTheme(JSON.parse(cached));
    } else {
      applyTheme();
    }
  } catch {
    applyTheme();
  }

  // 2. Fetch live settings in background from backend
  try {
    const res = await fetch("/api/public/content");
    if (res.ok) {
      const data = await res.json();
      if (data?.settings) {
        applyTheme(data.settings);
      }
    }
  } catch {
    // Fallback safe
  }
}
