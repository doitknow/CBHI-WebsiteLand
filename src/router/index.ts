import { createRouter, createWebHistory } from "vue-router";
import Landingpage from "../screens/Landingpage/Landingpage.vue";

const routes = [
  {
    path: "/",
    name: "Landingpage",
    component: Landingpage,
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

export default router;
