# Security Policy

Learn about our security practices and how we protect our API, client library, and applications.

Last updated: May 24, 2026

## 1. Infrastructure Security
Our infrastructure is built to securely host and serve the public REST API and Web Interface:
- Hosted on SOC 2 Type II compliant cloud providers with 24/7 physical security
- Redundant multi-region deployment for high availability
- Automated encrypted backups and disaster recovery procedures
- Continuous vulnerability scanning and penetration testing

## 2. Data in Transit & at Rest
All API/Web traffic and stored logs are protected with enterprise-grade encryption:
- TLS 1.3 enforced on all endpoints (HTTPS only)
- AES-256 encryption for logs and temporary caches
- Automatic certificate rotation via Let's Encrypt / Cloudflare
- HSTS and Security Headers (CSP, XSS Protection) to prevent common web attacks

## 3. API & UI Protection
We protect our services from abuse and ensure fair usage:
- Strict IP-based and header-based rate limiting
- Automatic temporary blocks for malicious patterns
- Request validation and payload size limits
- Protection against XSS, CSRF, and SQL Injection on the Web Interface
- Blocking of known malicious User-Agents and bot signatures

## 4. Logging & Monitoring
Security events are monitored in real time:
- Centralized logging with tamper-proof storage
- Real-time alerts for anomalies and attack patterns
- Retention limited to 30 days, then automatically purged
- Zero persistent storage of request bodies containing sensitive data

## 5. Compliance & Standards
We align with industry security and privacy standards:
- SOC 2 Type II compliant infrastructure
- GDPR-ready data handling practices
- Regular third-party security audits
- Transparent vulnerability disclosure policy

## 6. Incident Response
In case of a security incident:
- 24/7 automated monitoring and on-call team
- Rapid containment and mitigation within hours
- Public disclosure if user data is affected
- Post-incident report and preventive improvements

## 7. Responsible Disclosure
Found a vulnerability? We encourage responsible disclosure:
- Report securely by contacting our Security Team at: **[support@tioprm.eu.org](mailto:support@tioprm.eu.org)**
- PGP key available on request
- No legal action against good-faith researchers
- Public acknowledgment (with consent)
