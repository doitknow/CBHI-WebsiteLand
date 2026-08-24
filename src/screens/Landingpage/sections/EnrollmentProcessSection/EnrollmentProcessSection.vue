<script setup lang="ts">
import { ArrowRight } from "lucide-vue-next";
import { useI18n } from "vue-i18n";
import ScrollReveal from "../../../../components/ui/ScrollReveal.vue";

const { t } = useI18n();

const enrollmentSteps = [
  {
    number: "01",
    iconSvg: `<svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M16 6a5 5 0 1 1 0 10 5 5 0 0 1 0-10z" fill="#0873b9"/>
      <path d="M16 18c5.523 0 10 2.239 10 5v1H6v-1c0-2.761 4.477-5 10-5z" fill="#0873b9"/>
      <rect x="22" y="16" width="7" height="1.5" rx="0.75" fill="#0873b9"/>
      <rect x="22" y="19" width="5" height="1.5" rx="0.75" fill="#0873b9"/>
    </svg>`,
    translationKeyTitle: "enrollment.steps.step1.title",
    translationKeyDesc: "enrollment.steps.step1.description",
    translationKeyBadge: "enrollment.steps.step1.badge",
  },
  {
    number: "02",
    iconSvg: `<svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <rect x="6" y="8" width="20" height="18" rx="2.5" stroke="#0873b9" stroke-width="1.8" fill="none"/>
      <line x1="6" y1="14" x2="26" y2="14" stroke="#0873b9" stroke-width="1.8"/>
      <line x1="11" y1="5" x2="11" y2="10" stroke="#0873b9" stroke-width="1.8" stroke-linecap="round"/>
      <line x1="21" y1="5" x2="21" y2="10" stroke="#0873b9" stroke-width="1.8" stroke-linecap="round"/>
      <rect x="10" y="18" width="3.5" height="3.5" rx="0.75" fill="#0873b9"/>
      <rect x="16" y="18" width="3.5" height="3.5" rx="0.75" fill="#0873b9"/>
      <rect x="22" y="18" width="3.5" height="3.5" rx="0.75" fill="#0873b9" opacity="0.4"/>
    </svg>`,
    translationKeyTitle: "enrollment.steps.step2.title",
    translationKeyDesc: "enrollment.steps.step2.description",
    translationKeyBadge: "enrollment.steps.step2.badge",
  },
  {
    number: "03",
    iconSvg: `<svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="16" cy="16" r="11" stroke="#0873b9" stroke-width="1.8" fill="none"/>
      <path d="M11 16.5L14.5 20L21 13" stroke="#0873b9" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
    </svg>`,
    translationKeyTitle: "enrollment.steps.step3.title",
    translationKeyDesc: "enrollment.steps.step3.description",
    translationKeyBadge: "enrollment.steps.step3.badge",
  },
];
</script>

<template>
  <section
    aria-labelledby="enrollment-heading"
    class="enrollment-section dark:bg-[#021E31]"
  >
    <div class="enrollment-inner">
      <!-- Heading -->
      <ScrollReveal direction="up" duration="0.8s">
        <header class="enrollment-header">
          <h2 id="enrollment-heading" class="enrollment-title">
            <span class="enrollment-title-dark dark:text-white">{{ t('enrollment.heading_how') }}</span>
            <span class="enrollment-title-blue">{{ t('enrollment.heading_enrollment') }}</span>
            <span class="enrollment-title-dark dark:text-white">{{ t('enrollment.heading_works') }}</span>
          </h2>

          <p class="enrollment-subtitle">
            {{ t('enrollment.subtitle') }}
          </p>
        </header>
      </ScrollReveal>

      <!-- Step cards with arrow connectors -->
      <div class="enrollment-cards-wrapper">
        <!-- Dotted pattern top-right decorative -->
        <div class="enrollment-dots" aria-hidden="true">
          <svg width="90" height="72" viewBox="0 0 90 72" fill="none">
            <template v-for="row in 5" :key="'dr-'+row">
              <circle v-for="col in 6" :key="'d-'+row+'-'+col"
                :cx="8 + (col - 1) * 16" :cy="8 + (row - 1) * 14"
                r="2" fill="#0873b9" opacity="0.18"
              />
            </template>
          </svg>
        </div>

        <div class="enrollment-cards-grid">
          <template v-for="(step, i) in enrollmentSteps" :key="step.number">
            <!-- Card -->
            <ScrollReveal direction="up" :stagger-index="i" :stagger-delay="150" :delay="100" duration="0.8s">
            <div class="enrollment-card-container">
              <!-- Number badge — overlapping top-left -->
              <div class="enrollment-step-badge">
                {{ step.number }}
              </div>

              <!-- Card body -->
              <div class="enrollment-card">
                <!-- Icon circle -->
                <div class="enrollment-icon-circle">
                  <!-- eslint-disable-next-line vue/no-v-html -->
                  <span v-html="step.iconSvg" class="enrollment-icon-inner" />
                </div>

                <!-- Title -->
                <h3 class="enrollment-card-title">
                  {{ t(step.translationKeyTitle) }}
                </h3>

                <!-- Description -->
                <p class="enrollment-card-desc">
                  {{ t(step.translationKeyDesc) }}
                </p>

                <!-- Bottom badge pill -->
                <div class="enrollment-card-badge">
                  <svg width="18" height="18" viewBox="0 0 18 18" fill="none" class="enrollment-badge-check">
                    <circle cx="9" cy="9" r="9" fill="#0873b9"/>
                    <path d="M5.5 9.5L7.8 11.8L12.5 6.5" stroke="white" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  <span class="enrollment-badge-text">{{ t(step.translationKeyBadge) }}</span>
                </div>
              </div>
            </div>
            </ScrollReveal>

            <!-- Arrow connector between cards (horizontal on desktop, vertical on mobile) -->
            <div
              v-if="i < enrollmentSteps.length - 1"
              class="enrollment-arrow-connector"
            >
              <div class="enrollment-arrow-circle">
                <svg width="14" height="14" viewBox="0 0 18 18" fill="none" class="enrollment-arrow-icon enrollment-arrow-icon-horizontal">
                  <path d="M4 9H14M14 9L10 5M14 9L10 13" stroke="white" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
                <svg width="14" height="14" viewBox="0 0 18 18" fill="none" class="enrollment-arrow-icon enrollment-arrow-icon-vertical">
                  <path d="M9 4V14M9 14L5 10M9 14L13 10" stroke="white" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </div>
          </template>
        </div>
      </div>

      <!-- Start Enrollment CTA -->
      <div class="enrollment-cta-wrapper">
        <button type="button" class="enrollment-cta-btn cta-shimmer">
          <span>{{ t('enrollment.start_enrollment') }}</span>
          <span class="enrollment-cta-icon">
            <ArrowRight class="enrollment-cta-arrow" />
          </span>
        </button>
      </div>
    </div>
  </section>
</template>

<style scoped>
/* ─── Section Container ─── */
.enrollment-section {
  width: 100%;
  background-color: #f8fafc;
  padding: 72px 24px 64px;
  font-family: 'Inter', Helvetica, Arial, sans-serif;
  overflow-x: hidden;
}

.enrollment-inner {
  max-width: 1140px;
  margin: 0 auto;
}

/* ─── Header ─── */
.enrollment-header {
  text-align: center;
  max-width: 680px;
  margin: 0 auto;
}

.enrollment-title {
  font-size: clamp(32px, 5vw, 48px);
  font-weight: 800;
  line-height: 1.15;
  letter-spacing: -0.02em;
  margin: 0;
}

.enrollment-title-dark {
  color: #161616;
}

.enrollment-title-blue {
  color: #0873b9;
}

.enrollment-subtitle {
  margin-top: 16px;
  font-size: 15.5px;
  font-weight: 400;
  line-height: 1.7;
  color: #64748b;
}

.enrollment-br-desktop {
  display: none;
}

@media (min-width: 640px) {
  .enrollment-br-desktop {
    display: block;
  }
}

/* ─── Cards Wrapper ─── */
.enrollment-cards-wrapper {
  position: relative;
  margin-top: 52px;
}

/* Dotted decoration */
.enrollment-dots {
  position: absolute;
  right: -12px;
  top: -16px;
  pointer-events: none;
  display: none;
}

@media (min-width: 1024px) {
  .enrollment-dots {
    display: block;
  }
}

/* ─── Cards Grid ─── */
.enrollment-cards-grid {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

@media (min-width: 768px) {
  .enrollment-cards-grid {
    flex-direction: row;
    align-items: stretch;
    justify-content: center;
    gap: 0;
  }
}

/* ─── Card Container ─── */
.enrollment-card-container {
  position: relative;
  width: 100%;
  max-width: 100%;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .enrollment-card-container {
    flex: 1;
    max-width: none;
  }
}

/* ─── Step Badge ─── */
.enrollment-step-badge {
  position: absolute;
  left: -4px;
  top: -14px;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  font-size: 18px;
  font-weight: 800;
  color: white;
  background: linear-gradient(135deg, #0d2b6b 0%, #1a4da8 100%);
  box-shadow: 0 4px 14px rgba(13, 43, 107, 0.3);
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.enrollment-card-container:hover .enrollment-step-badge {
  transform: scale(1.12) rotate(-3deg);
}

/* ─── Card ─── */
.enrollment-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  height: 100%;
  background: #ffffff;
  border-radius: 16px;
  border: 1px solid #e8edf2;
  padding: 44px 20px 20px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.04);
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.35s ease;
}

.enrollment-card-container:hover .enrollment-card {
  transform: translateY(-8px);
  box-shadow: 0 20px 48px rgba(60, 141, 253, 0.12),
              0 0 0 1px rgba(60, 141, 253, 0.08);
}

/* ─── Icon Circle ─── */
.enrollment-icon-circle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: #e8f1ff;
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1),
              background-color 0.3s ease;
}

.enrollment-card-container:hover .enrollment-icon-circle {
  transform: scale(1.1);
  background-color: #d6e6ff;
}

.enrollment-icon-inner {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ─── Card Title ─── */
.enrollment-card-title {
  margin-top: 16px;
  font-size: 17px;
  font-weight: 700;
  line-height: 1.3;
  color: #161616;
  transition: color 0.3s ease;
}

.enrollment-card-container:hover .enrollment-card-title {
  color: #0873b9;
}

/* ─── Card Description ─── */
.enrollment-card-desc {
  margin-top: 10px;
  font-size: 13px;
  font-weight: 400;
  line-height: 1.7;
  color: #64748b;
  max-width: 260px;
}

/* ─── Card Badge ─── */
.enrollment-card-badge {
  margin-top: auto;
  padding-top: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #eaf3ff;
  border-radius: 100px;
  padding: 8px 18px;
  margin-top: 24px;
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.enrollment-card-container:hover .enrollment-card-badge {
  background-color: #d6e6ff;
  transform: translateY(-2px);
}

.enrollment-badge-check {
  flex-shrink: 0;
}

.enrollment-badge-text {
  font-size: 12.5px;
  font-weight: 600;
  color: #0873b9;
  white-space: nowrap;
}

/* ─── Arrow Connector ─── */
.enrollment-arrow-connector {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px 0;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .enrollment-arrow-connector {
    padding: 0 12px;
    align-self: center;
  }
}

@media (min-width: 1024px) {
  .enrollment-arrow-connector {
    padding: 0 20px;
  }
}

.enrollment-arrow-circle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0873b9 0%, #2b7af7 100%);
  box-shadow: 0 4px 14px rgba(60, 141, 253, 0.3);
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.35s ease;
  animation: enrollment-float 3s ease-in-out infinite;
}

.enrollment-arrow-circle:hover {
  transform: scale(1.15);
  box-shadow: 0 6px 20px rgba(60, 141, 253, 0.45);
}

.enrollment-arrow-icon {
  display: none;
}

/* Mobile: show vertical arrow */
.enrollment-arrow-icon-vertical {
  display: block;
}

.enrollment-arrow-icon-horizontal {
  display: none;
}

@media (min-width: 768px) {
  .enrollment-arrow-icon-vertical {
    display: none;
  }
  .enrollment-arrow-icon-horizontal {
    display: block;
  }
}

/* ─── CTA ─── */
.enrollment-cta-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 48px;
}

.enrollment-cta-btn {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  height: 52px;
  padding: 0 32px;
  border-radius: 100px;
  border: none;
  cursor: pointer;
  font-family: 'Inter', Helvetica, Arial, sans-serif;
  font-size: 15.5px;
  font-weight: 600;
  color: #ffffff;
  background: linear-gradient(135deg, #0873b9 0%, #2b7af7 100%);
  box-shadow: 0 6px 24px rgba(60, 141, 253, 0.3);
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1),
              box-shadow 0.35s ease,
              background 0.3s ease;
}

.enrollment-cta-btn:hover {
  transform: scale(1.06);
  box-shadow: 0 10px 32px rgba(60, 141, 253, 0.4);
  background: linear-gradient(135deg, #2b7af7 0%, #1a6ae0 100%);
}

.enrollment-cta-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.22);
  transition: transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1),
              background 0.3s ease;
}

.enrollment-cta-btn:hover .enrollment-cta-icon {
  transform: translateX(3px);
  background: rgba(255, 255, 255, 0.32);
}

.enrollment-cta-arrow {
  width: 16px;
  height: 16px;
}

/* ─── Float Animation ─── */
@keyframes enrollment-float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-4px);
  }
}

/* ─── Responsive: Tablet ─── */
@media (min-width: 640px) {
  .enrollment-section {
    padding: 72px 32px 64px;
  }
  .enrollment-step-badge {
    left: -10px;
    top: -18px;
    width: 52px;
    height: 52px;
    border-radius: 14px;
    font-size: 22px;
  }
  .enrollment-card {
    border-radius: 20px;
    padding: 52px 24px 24px;
  }
  .enrollment-icon-circle {
    width: 68px;
    height: 68px;
  }
  .enrollment-card-title {
    font-size: 19px;
    margin-top: 20px;
  }
  .enrollment-card-desc {
    font-size: 14px;
    margin-top: 12px;
  }
  .enrollment-card-container {
    max-width: 360px;
  }
}

@media (min-width: 768px) {
  .enrollment-section {
    padding: 80px 40px 72px;
  }

  .enrollment-cards-grid {
    gap: 0;
  }

  .enrollment-card {
    padding: 52px 20px 24px;
  }
}

@media (min-width: 1024px) {
  .enrollment-section {
    padding: 96px 48px 88px;
  }

  .enrollment-card {
    padding: 52px 28px 28px;
  }
}
</style>
