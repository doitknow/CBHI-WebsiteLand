import api, { request } from "./api";

export interface ImpactMetricItem {
  id?: number;
  metricKey: string;
  valueNumber: number;
  suffix: string;
  labelText: string;
  subtext: string;
  iconName?: string;
  displayOrder: number;
}

export interface CoverageBenefitItem {
  id?: number;
  title: string;
  description: string;
  iconName?: string;
  tag?: string;
  displayOrder: number;
  active: boolean;
}

export interface EnrollmentStepItem {
  id?: number;
  stepNumber: number;
  title: string;
  description: string;
  badgeText?: string;
  displayOrder: number;
}

export interface NewsArticleItem {
  id?: number;
  title: string;
  summary: string;
  content: string;
  category?: string;
  imageUrl?: string;
  published: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface PublicContentData {
  settings: Record<string, string>;
  metrics: ImpactMetricItem[];
  benefits: CoverageBenefitItem[];
  enrollmentSteps: EnrollmentStepItem[];
  latestNews: NewsArticleItem[];
}

export interface MediaAssetItem {
  id: number;
  filename: string;
  originalName: string;
  fileUrl: string;
  contentType: string;
  fileSize: number;
  category: string;
  altText?: string;
  uploadedAt: string;
}

export interface AdminOverviewData {
  totalMetrics: number;
  totalBenefits: number;
  activeBenefits: number;
  totalSteps: number;
  totalNews: number;
  publishedNews: number;
  totalMedia: number;
  databaseConnected: boolean;
  databaseVersion: string;
}

export const cmsService = {
  // Public APIs
  getPublicContent: () => api.get<PublicContentData>("/api/public/content"),
  checkHealth: () => api.get<{ status: string; system: string; database: string }>("/api/public/health"),

  // Admin Overview
  getOverview: () => api.get<AdminOverviewData>("/api/admin/overview"),

  // Media Management
  getMedia: (category?: string) =>
    api.get<MediaAssetItem[]>(`/api/admin/media${category && category !== "all" ? "?category=" + category : ""}`),
  uploadMedia: (formData: FormData) =>
    api.upload<MediaAssetItem>("/api/admin/media/upload", formData),
  updateMedia: (id: number, data: { altText?: string; category?: string }) =>
    api.put<MediaAssetItem>(`/api/admin/media/${id}`, data),
  deleteMedia: (id: number) => api.delete(`/api/admin/media/${id}`),

  // Metrics
  getMetrics: () => api.get<ImpactMetricItem[]>("/api/admin/metrics"),
  saveMetric: (metric: ImpactMetricItem) => {
    if (metric.id) {
      return api.put<ImpactMetricItem>(`/api/admin/metrics/${metric.id}`, metric);
    }
    return api.post<ImpactMetricItem>("/api/admin/metrics", metric);
  },
  deleteMetric: (id: number) => api.delete(`/api/admin/metrics/${id}`),

  // Benefits
  getBenefits: () => api.get<CoverageBenefitItem[]>("/api/admin/benefits"),
  saveBenefit: (benefit: CoverageBenefitItem) => {
    if (benefit.id) {
      return api.put<CoverageBenefitItem>(`/api/admin/benefits/${benefit.id}`, benefit);
    }
    return api.post<CoverageBenefitItem>("/api/admin/benefits", benefit);
  },
  deleteBenefit: (id: number) => api.delete(`/api/admin/benefits/${id}`),

  // Steps
  getSteps: () => api.get<EnrollmentStepItem[]>("/api/admin/enrollment-steps"),
  saveStep: (step: EnrollmentStepItem) => {
    if (step.id) {
      return api.put<EnrollmentStepItem>(`/api/admin/enrollment-steps/${step.id}`, step);
    }
    return api.post<EnrollmentStepItem>("/api/admin/enrollment-steps", step);
  },
  deleteStep: (id: number) => api.delete(`/api/admin/enrollment-steps/${id}`),

  // News
  getNews: () => api.get<NewsArticleItem[]>("/api/admin/news"),
  saveNews: (news: NewsArticleItem) => {
    if (news.id) {
      return api.put<NewsArticleItem>(`/api/admin/news/${news.id}`, news);
    }
    return api.post<NewsArticleItem>("/api/admin/news", news);
  },
  deleteNews: (id: number) => api.delete(`/api/admin/news/${id}`),

  // Settings
  getSettings: () => api.get<Record<string, string>>("/api/admin/settings"),
  updateSettings: (settings: Record<string, string>) =>
    api.put<Record<string, string>>("/api/admin/settings", settings),
};

export default cmsService;
