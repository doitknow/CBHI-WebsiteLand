import { createRouter, createWebHistory } from "vue-router";
import Landingpage from "../screens/Landingpage/Landingpage.vue";
import AdminLogin from "../screens/Admin/AdminLogin.vue";
import AdminLayout from "../screens/Admin/AdminLayout.vue";
import DashboardOverview from "../screens/Admin/views/DashboardOverview.vue";
import HeroMetricsManager from "../screens/Admin/views/HeroMetricsManager.vue";
import BenefitsManager from "../screens/Admin/views/BenefitsManager.vue";
import EnrollmentManager from "../screens/Admin/views/EnrollmentManager.vue";
import NewsManager from "../screens/Admin/views/NewsManager.vue";
import SettingsManager from "../screens/Admin/views/SettingsManager.vue";
import MediaManager from "../screens/Admin/views/MediaManager.vue";
import ThemeManager from "../screens/Admin/views/ThemeManager.vue";
import LayoutManager from "../screens/Admin/views/LayoutManager.vue";
import AdminResetPassword from "../screens/Admin/AdminResetPassword.vue";

const routes = [
  {
    path: "/",
    name: "Landingpage",
    component: Landingpage,
  },
  {
    path: "/admin/login",
    name: "AdminLogin",
    component: AdminLogin,
  },
  {
    path: "/admin/reset-password",
    name: "AdminResetPassword",
    component: AdminResetPassword,
  },
  {
    path: "/admin",
    component: AdminLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: "",
        redirect: "/admin/dashboard",
      },
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: DashboardOverview,
      },
      {
        path: "hero-metrics",
        name: "AdminHeroMetrics",
        component: HeroMetricsManager,
      },
      {
        path: "benefits",
        name: "AdminBenefits",
        component: BenefitsManager,
      },
      {
        path: "layout",
        name: "AdminLayoutStudio",
        component: LayoutManager,
      },
      {
        path: "theme",
        name: "AdminTheme",
        component: ThemeManager,
      },
      {
        path: "media",
        name: "AdminMedia",
        component: MediaManager,
      },
      {
        path: "enrollment",
        name: "AdminEnrollment",
        component: EnrollmentManager,
      },
      {
        path: "news",
        name: "AdminNews",
        component: NewsManager,
      },
      {
        path: "settings",
        name: "AdminSettings",
        component: SettingsManager,
      },
    ],
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }
    if (to.hash) {
      return {
        el: to.hash,
        behavior: "smooth",
      };
    }
    return { top: 0, behavior: "smooth" };
  },
});

// Navigation Guard for Admin Routes
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("cbhi_auth_token");

  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!token) {
      next({
        path: "/admin/login",
        query: { redirect: to.fullPath },
      });
      return;
    }
  }

  // If already logged in and going to /admin/login, redirect to dashboard
  if (to.path === "/admin/login" && token) {
    next("/admin/dashboard");
    return;
  }

  next();
});

export default router;
