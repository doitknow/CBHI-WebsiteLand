export interface LayoutPreset {
  id: string;
  name: string;
  tagline: string;
  badge: string;
  description: string;
  heroStyle: "slider" | "split" | "services-hero";
  headerStyle: "standard" | "compact" | "transparent";
  containerWidth: "wide" | "standard" | "contained";
  cardStyle: "modern" | "classic" | "glass";
  showPartners: boolean;
  showQuickServices: boolean;
  showPhilosophy: boolean;
  showMetrics: boolean;
  showBenefits: boolean;
  showEnrollment: boolean;
  showStories: boolean;
  keyFeatures: string[];
}

export interface LayoutSettings {
  activePresetId: string;
  heroStyle: "slider" | "split" | "services-hero";
  headerStyle: "standard" | "compact" | "transparent";
  containerWidth: "wide" | "standard" | "contained";
  cardStyle: "modern" | "classic" | "glass";
  showPartners: boolean;
  showQuickServices: boolean;
  showPhilosophy: boolean;
  showMetrics: boolean;
  showBenefits: boolean;
  showEnrollment: boolean;
  showStories: boolean;
}

export const LAYOUT_PRESETS: LayoutPreset[] = [
  {
    id: "classic-portal",
    name: "Classic National Portal",
    tagline: "Flagship Institutional Layout",
    badge: "Official Default",
    description: "The authoritative federal portal architecture featuring full-width rotating hero slider, partner integration marquee, health philosophy cards, and 3-step vertical enrollment guide.",
    heroStyle: "slider",
    headerStyle: "standard",
    containerWidth: "standard",
    cardStyle: "modern",
    showPartners: true,
    showQuickServices: false,
    showPhilosophy: true,
    showMetrics: true,
    showBenefits: true,
    showEnrollment: true,
    showStories: false,
    keyFeatures: [
      "Dynamic multi-slide national hero carousel",
      "Continuous marquee of financial partner institutions",
      "Full philosophy and constitutional health rights grid",
      "Vertical chronological enrollment milestone pathway"
    ]
  },
  {
    id: "modern-executive",
    name: "Modern Executive Split",
    tagline: "High-Tech Health-Tech Aesthetics",
    badge: "Modern Clean",
    description: "Sleek asymmetric split-screen hero layout pairing bold modern typography with an interactive digital CBHI card preview, compact horizontal metric ribbons, and tabbed benefit exploration.",
    heroStyle: "split",
    headerStyle: "compact",
    containerWidth: "wide",
    cardStyle: "glass",
    showPartners: true,
    showQuickServices: true,
    showPhilosophy: true,
    showMetrics: true,
    showBenefits: true,
    showEnrollment: true,
    showStories: false,
    keyFeatures: [
      "Interactive 3D-styled Digital Health Card with live status",
      "Ultra-wide spacious layout with backdrop blur accents",
      "Integrated Quick Coverage Simulator",
      "Sleek streamlined navigation and ambient glow cards"
    ]
  },
  {
    id: "citizen-service",
    name: "Citizen Services Direct Access",
    tagline: "Action-Oriented Public Portal",
    badge: "Citizen First",
    description: "Optimized for citizen self-service with a prominent Quick Action Hub right at the forefront, including eligibility check, hospital locator, premium calculator, and Fayda ID verification.",
    heroStyle: "services-hero",
    headerStyle: "standard",
    containerWidth: "standard",
    cardStyle: "classic",
    showPartners: true,
    showQuickServices: true,
    showPhilosophy: false,
    showMetrics: true,
    showBenefits: true,
    showEnrollment: true,
    showStories: false,
    keyFeatures: [
      "Immediate Citizen Action Hub with 4 interactive self-service tools",
      "Emergency medical helpline quick-dial spotlight",
      "Interactive Woreda coverage lookup",
      "High-contrast accessible cards with direct CTAs"
    ]
  },
  {
    id: "community-story",
    name: "Story & Community Driven",
    tagline: "Human-Centric Impact Experience",
    badge: "Community Focus",
    description: "A narrative layout putting Ethiopian beneficiaries and healthcare workers front and center, featuring personal member testimonies, before-and-after coverage impact, and regional woreda showcases.",
    heroStyle: "split",
    headerStyle: "standard",
    containerWidth: "standard",
    cardStyle: "modern",
    showPartners: true,
    showQuickServices: true,
    showPhilosophy: true,
    showMetrics: true,
    showBenefits: true,
    showEnrollment: true,
    showStories: true,
    keyFeatures: [
      "Featured Beneficiary Stories & personal testimonies",
      "Before CBHI vs. With CBHI real-life comparison matrix",
      "Woreda community engagement spotlights",
      "Humanized health rights storytelling with warm imagery"
    ]
  }
];

export const DEFAULT_LAYOUT = LAYOUT_PRESETS[0];

/**
 * Extracts and parses layout settings from the key-value dictionary
 */
export function getActiveLayoutSettings(settings?: Record<string, string>): LayoutSettings {
  const presetId = settings?.["layout.active_preset"] || DEFAULT_LAYOUT.id;
  const matchedPreset = LAYOUT_PRESETS.find(p => p.id === presetId) || DEFAULT_LAYOUT;

  const parseBool = (val?: string, fallback: boolean = false): boolean => {
    if (val === undefined || val === null || val === "") return fallback;
    return val === "true" || val === "1";
  };

  return {
    activePresetId: presetId,
    heroStyle: (settings?.["layout.hero_style"] as any) || matchedPreset.heroStyle,
    headerStyle: (settings?.["layout.header_style"] as any) || matchedPreset.headerStyle,
    containerWidth: (settings?.["layout.container_width"] as any) || matchedPreset.containerWidth,
    cardStyle: (settings?.["layout.card_style"] as any) || matchedPreset.cardStyle,
    showPartners: parseBool(settings?.["layout.show_partners"], matchedPreset.showPartners),
    showQuickServices: parseBool(settings?.["layout.show_quick_services"], matchedPreset.showQuickServices),
    showPhilosophy: parseBool(settings?.["layout.show_philosophy"], matchedPreset.showPhilosophy),
    showMetrics: parseBool(settings?.["layout.show_metrics"], matchedPreset.showMetrics),
    showBenefits: parseBool(settings?.["layout.show_benefits"], matchedPreset.showBenefits),
    showEnrollment: parseBool(settings?.["layout.show_enrollment"], matchedPreset.showEnrollment),
    showStories: parseBool(settings?.["layout.show_stories"], matchedPreset.showStories),
  };
}

/**
 * Converts layout settings into a key-value dictionary ready for cmsService.updateSettings
 */
export function layoutSettingsToDictionary(layout: LayoutSettings): Record<string, string> {
  return {
    "layout.active_preset": layout.activePresetId,
    "layout.hero_style": layout.heroStyle,
    "layout.header_style": layout.headerStyle,
    "layout.container_width": layout.containerWidth,
    "layout.card_style": layout.cardStyle,
    "layout.show_partners": String(layout.showPartners),
    "layout.show_quick_services": String(layout.showQuickServices),
    "layout.show_philosophy": String(layout.showPhilosophy),
    "layout.show_metrics": String(layout.showMetrics),
    "layout.show_benefits": String(layout.showBenefits),
    "layout.show_enrollment": String(layout.showEnrollment),
    "layout.show_stories": String(layout.showStories),
  };
}
