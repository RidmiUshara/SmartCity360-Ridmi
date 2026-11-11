# SmartCity360 – UML Class Diagrams

Render these PlantUML diagrams at https://www.plantuml.com/plantuml/uml/ and export PNG images into the final PDF report. The assessment explicitly requests a class diagram for Parts B and E.

## Part B: Composite Pattern

```plantuml
@startuml
title Part B - City Hierarchy (Composite)
interface CityComponent {
  +getName(): String
  +setPower(on: boolean): void
  +getPowerUsage(): int
  +getActiveBuildings(): int
}
class Building {
  -name: String
  -powerUsage: int
  -powerOn: boolean
}
class ZoneGroup {
  -name: String
  -children: List<CityComponent>
  +add(component: CityComponent): void
}
class ZoneService {
  -city: ZoneGroup
  -components: Map
  +setPower(id: String, on: boolean): boolean
}
class ZoneController {
  +showZones(model: Model): String
  +changePower(id: String, on: boolean): String
}
CityComponent <|.. Building
CityComponent <|.. ZoneGroup
ZoneGroup "1" o-- "0..*" CityComponent : contains
ZoneService --> ZoneGroup
ZoneController --> ZoneService
@enduml
```

## Part E: Interpreter Pattern

```plantuml
@startuml
title Part E - City Rules (Interpreter)
interface Expression {
  +evaluate(context: Map): boolean
}
class ComparisonExpression {
  -metric: String
  -operator: String
  -threshold: double
}
class AndExpression {
  -left: Expression
  -right: Expression
}
class OrExpression {
  -left: Expression
  -right: Expression
}
class NotExpression {
  -inner: Expression
}
class PolicyParser {
  +parse(source: String): ParsedPolicy
}
class PolicyRule {
  -condition: Expression
  -action: String
  +matches(context: Map): boolean
}
Expression <|.. ComparisonExpression
Expression <|.. AndExpression
Expression <|.. OrExpression
Expression <|.. NotExpression
AndExpression --> Expression
OrExpression --> Expression
NotExpression --> Expression
PolicyParser ..> Expression : constructs
PolicyRule --> Expression : evaluates
@enduml
```

## Architecture Overview

```plantuml
@startuml
title SmartCity360 - Application Modules
rectangle "Thymeleaf / Web Browser" as web
class "HomeController" as home
class "EnergyController" as energy
class "ZoneController" as zone
class "ComplaintController" as complaint
class "LightController" as light
class "PolicyController" as policy
web --> home
web --> energy
web --> zone
web --> complaint
web --> light
web --> policy
@enduml
```
