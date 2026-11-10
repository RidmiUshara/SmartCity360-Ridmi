# SmartCity360 (Ridmi) — Part A: Decorator Pattern

This folder contains NEW files for the existing `com.ridmi.smartcity360` Spring Boot project.
It is **not** Janith's project and does not replace your existing homepage or `pom.xml`.

## Install

1. Open the existing `smartcity360` folder in IntelliJ IDEA.
2. Extract this ZIP, then copy its `src` directory *into the project root*, merging with the existing `src` directory (do not delete existing files).
3. In your browser, visit **http://localhost:8080/energy** while the Spring Boot application is running.
4. Optionally add `<a href="/energy">Open Energy Security</a>` inside the Energy Security card of the current `src/main/resources/templates/index.html` homepage.
5. In the IntelliJ terminal run `./mvnw test` (macOS). To save the changes run `git add src && git commit -m "Implement Part A Decorator security pipeline" && git push origin main`.

## What to demonstrate

- Toggle GZIP, AES-256-GCM, and HMAC-SHA256 independently.
- Process a meter reading and show the matching decoded data.
- Show that a changed byte is rejected when encryption or authentication is enabled.
- Explain `DataStream` (component), `BasicDataStream` (concrete component), `StreamDecorator` (decorator base), and the three concrete decorators.

Pipeline byte order: **GZIP → AES-GCM → HMAC**, while decoding reverses it.

**Important:** This is a classroom demo, not a real energy-infrastructure service. No production key management or persistent encrypted storage is implemented. AES-GCM already authenticates ciphertext; the optional HMAC is included to demonstrate independent layering.
