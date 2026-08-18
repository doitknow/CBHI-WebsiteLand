import { createI18n } from 'vue-i18n';
import en from './locales/en.json';
import am from './locales/am.json';
import om from './locales/om.json';
import ti from './locales/ti.json';
import so from './locales/so.json';

// Type-define 'en-US' as the master schema for the resource
type MessageSchema = typeof en;

export const i18n = createI18n<[MessageSchema], 'en' | 'am' | 'om' | 'ti' | 'so'>({
  legacy: false, // Set to false to use Composition API
  locale: localStorage.getItem('language') || 'en', // Set locale
  fallbackLocale: 'en', // Set fallback locale
  messages: {
    en,
    am,
    om,
    ti,
    so
  }
});
