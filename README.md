# SmartCity360 – Ridmi (OODP II)

Spring Boot 4.1.2 / Java 21 / Thymeleaf classroom simulation of five Object-Oriented Design Patterns.

| Assessment | Design pattern | UI route |
|---|---|---|
| Part A: Secure smart meter streams | Decorator | /energy |
| Part B: City/District/Street/Building hierarchy | Composite | /zones |
| Part C: Citizen complaint escalation | Chain of Responsibility | /complaints |
| Part D: 10,000 street lights | Flyweight | /lights |
| Part E: City policy DSL | Interpreter | /policies |

## Setup

Use **JDK 21 or newer** for Maven (check `./mvnw -version`), then:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Open http://localhost:8080. If 8080 is occupied, stop the other server or use `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.

## What to demonstrate

1. Energy: try compression, encryption and authentication, view decoded reading and tamper detection.
2. Zones: switch Market Street OFF, observe usage decrease from 440 to 210 kW, then switch ON.
3. Complaints: submit LOW and CRITICAL complaints, compare the handler routes and assigned officers.
4. Lights: compare 10,000 physical instances with only 3 cached configurations; toggle pole 1 independently.
5. Policies: evaluate a rule against sensors, add `WHEN (noise > 70 OR occupancy > 80) AND hour >= 22 THEN ALERT`, evaluate again and show rejection of invalid syntax.

## UML and academic report

See `docs/UML.md` for UML diagrams to export into the final report. The assessment also requires written analysis of benefits/drawbacks for each pattern and your own screenshots.

## Limitations

This is an educational, in-memory simulation (no database persistence, device control, user authentication or actual city notifications). Data resets on restart. Do not deploy publicly. The Flyweight demo shows reference sharing; **heap savings were not benchmarked**.

## Submission

Student Portal requires a **PDF report**, **full source ZIP** and **10–15-minute MP4 demonstration**. GitHub alone is not a substitute. Review the assignment PDF for the full marking criteria. Record your own application demonstration and explain each pattern in your own words.

## Sources

- Gamma, Helm, Johnson and Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley (1994).
- Spring Framework/Spring Boot official documentation.
