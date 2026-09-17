package com.medco.cbhi.config;

import com.medco.cbhi.entity.*;
import com.medco.cbhi.repository.*;
import com.medco.cbhi.service.MediaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ImpactMetricRepository metricRepository;
    private final CoverageBenefitRepository benefitRepository;
    private final EnrollmentStepRepository stepRepository;
    private final NewsArticleRepository newsRepository;
    private final SiteSettingRepository settingRepository;
    private final PasswordEncoder passwordEncoder;
    private final MediaService mediaService;

    public DataInitializer(UserRepository userRepository,
                           ImpactMetricRepository metricRepository,
                           CoverageBenefitRepository benefitRepository,
                           EnrollmentStepRepository stepRepository,
                           NewsArticleRepository newsRepository,
                           SiteSettingRepository settingRepository,
                           PasswordEncoder passwordEncoder,
                           MediaService mediaService) {
        this.userRepository = userRepository;
        this.metricRepository = metricRepository;
        this.benefitRepository = benefitRepository;
        this.stepRepository = stepRepository;
        this.newsRepository = newsRepository;
        this.settingRepository = settingRepository;
        this.passwordEncoder = passwordEncoder;
        this.mediaService = mediaService;
    }

    @Override
    public void run(String... args) {
        // 1. Seed or Update Admin User with admin email
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new User(
                    "admin",
                    passwordEncoder.encode("Admin123!"),
                    "bereketmussie281@gmail.com",
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            System.out.println(">> [CBHI Seeder] Default admin user initialized: username=admin, email=bereketmussie281@gmail.com");
        } else {
            admin.setEmail("bereketmussie281@gmail.com");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            userRepository.save(admin);
            System.out.println(">> [CBHI Seeder] Admin user credentials synchronized: username=admin, email=bereketmussie281@gmail.com");
        }

        // 2. Seed Site Settings
        if (settingRepository.count() == 0) {
            settingRepository.save(new SiteSetting("hero.welcome", "Welcome To CBHI", "Hero top tag text"));
            settingRepository.save(new SiteSetting("hero.title_ethiopian", "Ethiopian", "Hero title part 1"));
            settingRepository.save(new SiteSetting("hero.title_health_insurance", "Health Insurance", "Hero title part 2"));
            settingRepository.save(new SiteSetting("hero.subtitle", "Empowering Ethiopians with affordable and accessible healthcare. A community solidarity program ensuring secure, digital medical coverage for every household.", "Hero description"));
            settingRepository.save(new SiteSetting("contact.phone", "+251 911 000 000", "Top header & contact phone"));
            settingRepository.save(new SiteSetting("contact.support_hours", "24/7 Support", "Support operating hours"));
            settingRepository.save(new SiteSetting("contact.email", "info@edhn.dh.moh.gov.et", "Official contact email"));
            settingRepository.save(new SiteSetting("contact.address", "Addis Ababa, Ethiopia", "Head office address"));
            System.out.println(">> [CBHI Seeder] Default site settings seeded.");
        }

        // 3. Seed Impact Metrics
        if (metricRepository.count() == 0) {
            metricRepository.save(new ImpactMetric("citizens", 45, "M+", "CITIZENS COVERED", "Across all regions", "Users", 1));
            metricRepository.save(new ImpactMetric("woredas", 980, "+", "WOREDAS REACHED", "Nationwide integration", "Globe", 2));
            metricRepository.save(new ImpactMetric("facilities", 3500, "+", "PARTNER FACILITIES", "Hospitals & clinics", "Building2", 3));
            System.out.println(">> [CBHI Seeder] Default impact metrics seeded.");
        }

        // 4. Seed Coverage Benefits
        if (benefitRepository.count() == 0) {
            benefitRepository.save(new CoverageBenefit(
                    "Primary Care",
                    "Free consultations, check-ups, and basic treatments at any partner clinic.",
                    "Stethoscope",
                    "Essential",
                    1,
                    true
            ));
            benefitRepository.save(new CoverageBenefit(
                    "Hospitalization",
                    "Full inpatient coverage including surgery, ICU, and essential post-care services.",
                    "Hospital",
                    "Inpatient Care",
                    2,
                    true
            ));
            benefitRepository.save(new CoverageBenefit(
                    "Maternal & Child",
                    "Pre-natal, delivery, pediatric and postnatal care fully covered for every family.",
                    "Baby",
                    "Family Health",
                    3,
                    true
            ));
            System.out.println(">> [CBHI Seeder] Default coverage benefits seeded.");
        }

        // 5. Seed Enrollment Steps
        if (stepRepository.count() == 0) {
            stepRepository.save(new EnrollmentStep(
                    1,
                    "Identity Check",
                    "Register at your local Woreda with your Fayda ID. Our system integrates instantly for verification.",
                    "Bring your Fayda ID or national ID",
                    1
            ));
            stepRepository.save(new EnrollmentStep(
                    2,
                    "Annual Contribution",
                    "Pay a small annual fee based on your household size. Flexible payment options available.",
                    "Affordable rates for every household",
                    2
            ));
            stepRepository.save(new EnrollmentStep(
                    3,
                    "Get Coverage",
                    "Access healthcare instantly at any partner facility with your digital or physical CBHI card.",
                    "Coverage starts same day",
                    3
            ));
            System.out.println(">> [CBHI Seeder] Default enrollment steps seeded.");
        }

        // 6. Seed Sample News
        if (newsRepository.count() == 0) {
            NewsArticle article = new NewsArticle(
                    "CBHI Digital Card Rollout Reaches 120 New Woredas",
                    "Ministry of Health announces the successful expansion of digital CBHI health ID cards across rural woredas.",
                    "The Ministry of Health and Ethiopian Health Insurance Service (EHIS) have announced the deployment of digital smart identification across 120 additional rural woredas. Beneficiaries can now instantly verify their membership using national Fayda ID integration.",
                    "Expansion",
                    null,
                    true
            );
            article.setCreatedAt(LocalDateTime.now().minusDays(3));
            newsRepository.save(article);
            System.out.println(">> [CBHI Seeder] Default news article seeded.");
        }

        // 7. Seed Media Assets
        if (mediaService.count() == 0) {
            Path publicDir = Paths.get("..", "public").toAbsolutePath().normalize();
            if (Files.exists(publicDir)) {
                mediaService.seedExistingImage(publicDir.resolve("Telebirr.png"), "partners", "Telebirr Financial Integration");
                mediaService.seedExistingImage(publicDir.resolve("Sinqee_Logo.png"), "partners", "Siinqee Bank");
                mediaService.seedExistingImage(publicDir.resolve("images-Photoroom.png"), "partners", "Sidama Bank");
                mediaService.seedExistingImage(publicDir.resolve("eth-switch-Photoroom.png"), "partners", "Eth Switch");
                mediaService.seedExistingImage(publicDir.resolve("kenema_logo.png"), "partners", "Kenema Pharmacy");
                mediaService.seedExistingImage(publicDir.resolve("1755784071306-Photoroom.png"), "partners", "Ministry of Health");
                mediaService.seedExistingImage(publicDir.resolve("fayda-Photoroom.png"), "badges", "Fayda National ID");
                mediaService.seedExistingImage(publicDir.resolve("INSA.png"), "badges", "INSA Security Certification");
                mediaService.seedExistingImage(publicDir.resolve("primary in web.png"), "benefits", "Primary Health Clinic");
                mediaService.seedExistingImage(publicDir.resolve("hospitalization.png"), "benefits", "Hospital Inpatient Care");
                mediaService.seedExistingImage(publicDir.resolve("maternal-care.jpg"), "benefits", "Maternal Care");
                mediaService.seedExistingImage(publicDir.resolve("home image.png"), "hero", "CBHI Ethiopian Solidarity");
                System.out.println(">> [CBHI Seeder] Existing project images cataloged into media library.");
            }
        }
    }
}
