<script setup lang="ts">
import { ChevronRight, MapPin, Mail, Globe, ShieldCheck } from "lucide-vue-next";

const organizationLinks = [
  { label: "About Us", href: "#philosophy" },
  { label: "Ministry Of Health", href: "https://moh.gov.et" },
  { label: "Our Scale & Reports", href: "#impact" },
  { label: "Benefits & Coverage", href: "#benefits" },
  { label: "Enroll Now", href: "#process" },
];
</script>

<template>
  <footer
    id="contact"
    aria-label="Get in touch"
    class="relative w-full bg-[linear-gradient(0deg,rgba(0,0,0,1)_20%,rgba(3,30,67,1)_80%)] pt-16 pb-8 text-white [font-family:'Inter',Helvetica]"
  >
    <div class="mx-auto max-w-[1240px] px-6 lg:px-8">
      <div
        class="grid grid-cols-1 gap-12 md:grid-cols-2 lg:grid-cols-4"
      >
        <!-- Brand column -->
        <div class="flex flex-col gap-4">
          <div class="text-4xl font-black tracking-tight">
            <span class="text-white">CBHI.</span>
            <span class="text-[#0873b9]">et</span>
          </div>
          <p class="text-xs font-semibold uppercase tracking-widest text-[#0873b9]">
            Community Based Health Insurance
          </p>
          <p class="text-sm leading-relaxed text-slate-400">
            Reimagining healthcare solidarity and accessible digital coverage for every Ethiopian family.
          </p>
        </div>

        <!-- Organization links -->
        <div>
          <h3
            class="text-lg font-bold text-white"
          >
            Quick Links
          </h3>
          <div class="mt-2 h-1 w-12 rounded bg-[#0873b9]" />
          <nav
            aria-label="Organization links"
            class="mt-4 flex flex-col gap-2"
          >
            <a
              v-for="link in organizationLinks"
              :key="link.label"
              :href="link.href"
              class="inline-flex items-center gap-2 text-sm text-slate-300 transition hover:text-[#0873b9]"
            >
              <ChevronRight class="h-3.5 w-3.5 text-[#0873b9]" />
              <span>{{ link.label }}</span>
            </a>
          </nav>
        </div>

        <!-- Contact section -->
        <div>
          <h3
            class="text-lg font-bold text-white"
          >
            Contact &amp; Location
          </h3>
          <div class="mt-2 h-1 w-12 rounded bg-[#0873b9]" />
          <address class="mt-4 flex not-italic flex-col gap-3 text-sm text-slate-300">
            <div class="flex items-center gap-3">
              <MapPin class="h-5 w-5 text-[#0873b9] shrink-0" />
              <span>Addis Ababa, Ethiopia</span>
            </div>
            <div class="flex items-center gap-3">
              <Mail class="h-5 w-5 text-[#0873b9] shrink-0" />
              <span>info@cbhi.gov.et</span>
            </div>
            <div class="flex items-center gap-3">
              <Globe class="h-5 w-5 text-[#0873b9] shrink-0" />
              <span>www.cbhi.et</span>
            </div>
          </address>
        </div>

        <!-- Security & Integration -->
        <div>
          <h3
            class="text-lg font-bold text-white"
          >
            Verification &amp; Security
          </h3>
          <div class="mt-2 h-1 w-12 rounded bg-[#0873b9]" />
          <div class="mt-4 flex flex-col gap-3">
            <div class="flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800/60 p-2.5 text-xs text-slate-300">
              <ShieldCheck class="h-5 w-5 text-green-400 shrink-0" />
              <span>Fayda ID Integrated</span>
            </div>
            <div class="flex items-center gap-2 rounded-lg border border-slate-700 bg-slate-800/60 p-2.5 text-xs text-slate-300">
              <ShieldCheck class="h-5 w-5 text-[#0873b9] shrink-0" />
              <span>INSA Security Certified</span>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-12 border-t border-slate-800 pt-6">
        <div
          class="flex flex-col gap-4 text-xs text-slate-400 sm:flex-row sm:items-center sm:justify-between"
        >
          <p>© 2024 CBHI Ethiopia. All rights reserved.</p>
          <div class="flex items-center gap-2">
            <span>Powered by</span>
            <img
              class="h-7 w-auto object-contain"
              alt="Medco"
              src="/medco update.png"
            />
            <span class="font-semibold text-slate-200">
              Medco Technology Solutions
            </span>
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>
