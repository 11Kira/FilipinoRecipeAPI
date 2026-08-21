# FilipinoRecipeAPI (Unified Production Backend)

The core engine of the Filipino Recipe platform ecosystem. A high-performance, secure REST API built with Kotlin and Spring Boot, serving data concurrently to both the native Android client and the Kotlin Multiplatform (KMP) client application.

## 🛠️ Infrastructure & Tech Stack
* **Framework:** Spring Boot (Kotlin)
* **Database:** MongoDB (NoSQL)
* **Hosting Platform:** DigitalOcean VPS (Linux Ubuntu Droplet)
* **Reverse Proxy:** Nginx with Let's Encrypt SSL/TLS termination
* **Process Automation:** systemd service management for isolated environment configurations
* **CI/CD:** Continuous Deployment automated completely via GitHub Actions

## 🔒 Security & Systems Engineering Features
* **Advanced Authentication Flow:** Implements a robust 3-step cryptographic OTP verification process for secure user account recovery (Forgot Password framework).
* **Enterprise Password Hashing:** Seamless integration of Spring Security's `BCryptPasswordEncoder` bean for bulletproof data protection at rest.
* **Zero-Downtime Environment Separation:** Hardened configurations using automated server daemon-reloads and systemd process lifecycle management to separate critical API keys out of public source control.
* **24/7 Availability:** Bypasses standard server cold-start latencies, guaranteeing lightning-fast, sub-100ms request parsing for client applications.