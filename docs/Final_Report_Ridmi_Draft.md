# SmartCity360 — Final Project Report (Editing Draft)

**Unit:** Object Oriented Design Patterns II (JIAT/OODP II)  
**Assessment:** JIAT/OODP II /EX/01  
**Student:** [ENTER YOUR FULL NAME]  
**Registration number:** [ENTER YOUR STUDENT NUMBER]  
**Submission date:** [ENTER OFFICIAL DATE]  
**Source:** https://github.com/RidmiUshara/SmartCity360-Ridmi

**Important:** This is a draft aligned with the RidmiUshara/SmartCity360-Ridmi repository, not a different SmartCity360 repository. Complete all student information and insert real screenshots and rendered UML images before exporting a PDF.

## Executive summary

SmartCity360 is a Java web application simulating five city-management scenarios. Different object-oriented design patterns solve different design problems: Decorator for dynamic meter-stream protection, Composite for a hierarchy of city zones, Chain of Responsibility for citizen complaint escalation, Flyweight for shared streetlight configurations and Interpreter for dynamically evaluated city policies. The prototype uses Spring Boot 4.1.2, Java target 21, Spring MVC, Thymeleaf and Maven. It is an in-memory demonstration; it does not connect to real municipal devices.

## 1. Introduction and objectives

The assessment scenario presents a digital city platform that manages energy, public administration, devices and services. The implementation focuses on maintainability, extensibility and the ability to demonstrate design decisions through working software. Each of Parts A–E uses exactly one primary design pattern.

The application provides a browser dashboard showing current model values; separate pages for the five scenarios; automated pattern tests; and a navigation interface usable in light or dark mode. Domain logic is placed in the stream, zone, complaint, light and policy packages rather than embedded in the presentation templates.

| Part | Requirement | Pattern | Route |
|---|---|---|---|
| A | Optional stream security and compression | Decorator | /energy |
| B | Uniform commands across a city hierarchy | Composite | /zones |
| C | Severity-based escalation to responsible officers | Chain of Responsibility | /complaints |
| D | Reduce repeated streetlight configuration objects | Flyweight | /lights |
| E | Parse and evaluate an operational policy mini-language | Interpreter | /policies |

**Figure 1:** [INSERT ORIGINAL DASHBOARD SCREENSHOT]

## 2. System architecture

The browser sends requests to Spring MVC controllers. Controllers delegate to services and Java model objects; Thymeleaf templates render server-side HTML. Main components are:

- EnergyController and SecurityPipeline with DataStream decorators.
- ZoneController and ZoneService with CityComponent, ZoneGroup and Building.
- ComplaintController and ComplaintService with ComplaintHandler subclasses.
- LightController and LightService with StreetLight, LightFactory and LightConfiguration.
- PolicyController and PolicyService with PolicyParser and Expression implementations.
- HomeController, which gathers data for the dashboard.

The repository keeps Java model classes under src/main/java/com/ridmi/smartcity360, HTML templates under src/main/resources/templates, styles/scripts under static, and automated tests under src/test/java. Although H2 appears among the Maven dependencies, these demonstrated scenarios do not store their domain state in a database.

## 3. Part A — Decorator

**Selection (3 marks).** Decorator is suitable because compression, encryption and authentication can be added in different combinations to meter readings without editing BasicDataStream or creating subclasses for every combination (Gamma et al., 1994).

**Mechanism (4 marks).** DataStream defines the common processing interface. StreamDecorator wraps another DataStream. CompressionDecorator, EncryptionDecorator and AuthenticationDecorator add GZIP, AES-GCM and HMAC processing. SecurityPipeline builds the requested composition. For all three selected layers, the intended outgoing transformation order is GZIP → AES-GCM → HMAC, with the inverse during decoding.

**Evaluation (8 marks).** Benefits include modularity, reuse, flexibility and easier future extension. A new wrapper can be added without rewriting BasicDataStream. Drawbacks include more objects, order-sensitive processing and harder debugging of long decorator stacks. Real cryptographic systems would additionally require managed keys, secure configuration and independent security review. The sample screenshot shows 39 bytes of input and 118 bytes of protected output: this increase is plausible for small payloads because protection adds overhead. It is not proof that compression failed.

**Figure 2:** [INSERT ENERGY SECURITY INPUT SCREENSHOT]  
**Figure 3:** [INSERT ENCODED/DECODED OUTPUT WITH TAMPER DETECTION SCREENSHOT]

## 4. Part B — Composite

**Selection (3 marks).** Composite lets single buildings and groups of buildings share the CityComponent interface. Building is the leaf; ZoneGroup is the composite; both provide setPower and report methods. The model represents city → district → street → building.

**Required UML (4 marks).** The matching class diagram is documented in docs/UML.md, under “Part B: Composite Pattern”. Export the PlantUML diagram and insert it in the report.

**Figure 4:** [INSERT RENDERED PART B UML CLASS DIAGRAM]

**Demonstration.** ZoneService initially creates five buildings with 440 kW combined usage. North District has 270 kW; South District has 170 kW. Market Street contains City Hospital (150 kW) and Central School (80 kW). Turning Market Street OFF removes 230 kW, reducing the city total to 210 kW and active buildings from five to three. The same command can also target an entire district or one building.

**Evaluation (8 marks).** Advantages are uniform operations, simpler recursive aggregation and an extensible nested design. In a much larger hierarchy, traversal takes time proportional to the number of descendants. A real power system would require authorization, asynchronous commands, audit trails, device acknowledgements, persistence and failure recovery.

**Figure 5:** [INSERT SMART ZONES HIERARCHY SCREENSHOT]  
**Figure 6:** [INSERT MARKET STREET OFF RESULT SCREENSHOT]

## 5. Part C — Chain of Responsibility

**Selection (3 marks).** Chain of Responsibility is appropriate when the complaint destination is selected by a series of handlers. It removes the need for the complaint-form controller to know every officer role.

**Mechanism (4 marks).** ComplaintService connects LocalStaffHandler → SupervisorHandler → CityManagerHandler. LOW complaints go to Local Staff, MEDIUM to Supervisor, and HIGH/CRITICAL to City Manager. ComplaintHandler records the path of visited handlers before forwarding or assigning the request.

**Evaluation (8 marks).** The chain is easy to extend and is loosely coupled to submission code. Recording escalationPath improves explainability. However, ordering matters, incorrect configuration can leave requests unhandled and the current trace lacks timestamps. A production complaint system also needs permanent tickets, user permissions, notifications and service-level rules.

The screenshots show a MEDIUM complaint assigned to Supervisor with path Local Staff → Supervisor. The final HIGH/CRITICAL test was reported by the user as completed; attach its genuine result screenshot if available.

**Figure 7:** [INSERT COMPLAINT SUBMISSION SCREENSHOT]  
**Figure 8:** [INSERT ROUTING HISTORY SCREENSHOT]

## 6. Part D — Flyweight

**Selection (3 marks).** Flyweight reduces duplication of intrinsic configuration values across many light objects. LightFactory returns a cached immutable LightConfiguration rather than generating the same settings for each StreetLight.

**Intrinsic and extrinsic properties (4 marks).**

| Shared (intrinsic) | Unique per light (extrinsic) |
|---|---|
| Model | Pole ID |
| Wattage | Location |
| Light colour | ON/OFF state |

The service creates 10,000 poles using three configurations: Eco-LED 40 W (Warm White), Road-LED 70 W (Cool White), and Park-LED 30 W (Neutral White). Individual poles can be toggled without affecting another pole that shares the same configuration reference.

**Evaluation (8 marks).** Configuration caching avoids creating 10,000 separate configuration objects. Pole-specific data still grows linearly with the number of poles. No controlled JVM heap-memory benchmark was performed, so the project demonstrates the sharing mechanism rather than claiming a measured memory saving. The interface displays an initial sample of 15 light records.

**Figure 9:** [INSERT STREETLIGHT STATISTICS AND INVENTORY SCREENSHOT]  
**Figure 10:** [INSERT POLE TOGGLE RESULT SCREENSHOT]

## 7. Part E — Interpreter

**Selection (3 marks).** A small domain-specific language for noise and parking policies can be represented as expressions with a shared evaluate operation. This lets users add policy conditions at runtime.

**Required UML (4 marks).** The Part E diagram in docs/UML.md illustrates Expression; ComparisonExpression; AndExpression, OrExpression and NotExpression; PolicyParser; and PolicyRule.

**Figure 11:** [INSERT RENDERED PART E UML CLASS DIAGRAM]

**Mechanism.** PolicyParser handles WHEN ... THEN rules using a bounded recursive-descent parser. Supported sensor names are noise, hour, occupancy and parkedMinutes; supported comparisons are >, >=, <, <= and ==; logical operators include AND, OR, NOT and parentheses. The supported simulated actions are ALERT, ISSUE_FINE and LOG. For noise 80 at hour 23, the rule WHEN noise > 70 AND hour >= 22 THEN ALERT matches. A parking duration of 135 minutes matches WHEN parkedMinutes > 120 THEN ISSUE_FINE.

**Evaluation (8 marks).** Expression objects separate parsing from evaluation, making a compact grammar modular and testable. Larger grammars make parsing and validation harder. A production system would require permission controls, change approvals, policy versioning and audited execution. This application previews actions; it does not send real city notifications or fines.

**Figure 12:** [INSERT POLICY EVALUATION RESULT SCREENSHOT]  
**Figure 13:** [INSERT SAVED POLICY RULES SCREENSHOT]

## 8. Testing and verification

The repository contains JUnit tests for the patterns and Spring Boot rendering smoke tests. The GitHub Actions Maven tests passed after the UI changes. Local verification uses:

    ./mvnw clean test

| Test | Expected result | Evidence |
|---|---|---|
| Dashboard / module routes | Pages render and navigation works | Screenshots supplied |
| Decorator with all three options | Original data decodes; protected tamper is rejected | Result screenshot supplied |
| Market Street OFF | 440 → 210 kW and five → three active buildings | User reported complete; attach matching result screenshot |
| MEDIUM complaint | Assigned to Supervisor; path Local Staff → Supervisor | Screenshot supplied |
| HIGH/CRITICAL complaint | Assigned to City Manager | User reported complete |
| Light 1 OFF | Count 10,000 → 9,999; light 4 remains independent | User reported complete |
| Noise 80, hour 23 and parking 135 | Quiet hours ALERT and parking ISSUE_FINE | User reported complete |
| Light and dark themes | CSS changes but domain values are unchanged | Screenshots supplied |

Testing evidence must not be fabricated. Mark tests as independently verified only when their output or screenshots are saved.

## 9. Limitations and future work

SmartCity360 is an in-memory educational prototype. It has no permanent domain database, login system, role-based protection, real IoT device network or governmental backend. All data resets on restart. Improvements would include persistence, safer production key handling, asynchronous hardware commands, audit logging, pagination, user authorization, device monitoring and rigorous performance benchmarking.

## 10. Conclusion

The five patterns address different software design issues. Decorator composes optional behaviour, Composite organises nested infrastructure, Chain of Responsibility separates complaint routing, Flyweight shares repeatable configurations and Interpreter evaluates runtime rules. The Java classes and working web pages make these ideas observable. The project meets the demonstration goal as a simulation while identifying the work required for a real production city platform.

## References (Harvard style)

Gamma, E., Helm, R., Johnson, R. and Vlissides, J. (1994) *Design Patterns: Elements of Reusable Object-Oriented Software*. Reading, MA: Addison-Wesley.

Java Institute (2026) *Object Oriented Design Patterns II, Assessment JIAT/OODP II /EX/01*. Department of Examinations. Supplied assessment brief.

Oracle (2026) *Java Platform, Standard Edition Security Developer’s Guide*. Available at: https://docs.oracle.com/en/java/javase/21/security/ (Accessed: 12 November 2026).

Spring (2026) *Spring Boot Reference Documentation*. Available at: https://docs.spring.io/spring-boot/reference/ (Accessed: 12 November 2026).

RidmiUshara (2026) *SmartCity360-Ridmi*. GitHub source repository. Available at: https://github.com/RidmiUshara/SmartCity360-Ridmi (Accessed: 12 November 2026; private repository).

## Final submission checklist

- [ ] Replace personal details on the cover.
- [ ] Review and personalise analysis in your own words.
- [ ] Export UML diagrams in docs/UML.md and insert them.
- [ ] Insert genuine screenshots and captions.
- [ ] Export and proofread the final PDF.
- [ ] Download the full source-code ZIP from GitHub.
- [ ] Record a 10–15 minute MP4 demonstrating the actual application.
- [ ] Upload PDF, ZIP and MP4 separately to the Student Portal.
