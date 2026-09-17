import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./tailwind.css";
import { i18n } from "./i18n";
import { initTheme } from "./services/theme";

// Initialize dynamic branding & theme tokens across the entire site
initTheme();

const app = createApp(App);
app.use(i18n);
app.use(router);
app.mount("#app");
