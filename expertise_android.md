# Hawk Android Engineering Standard

## 1. Purpose of this document

This document defines the architectural rules, code standards, implementation conventions, and development discipline for the Hawk Android application.

The goal is to make every new screen, feature, and integration follow the same engineering model so the codebase stays:

1. Predictable.
2. Testable.
3. Scalable.
4. Offline-first friendly.
5. Easy to review.
6. Safe to evolve over time.

This document is intentionally top-down. It starts with architectural intent, then goes layer by layer, then finishes with coding, testing, performance, and CLI workflow rules.

This standard is based on the current project structure in `com.hawk`, especially the current `authentication`, `products`, `common`, `home`, and `utils` packages.

---

## 2. Architectural vision

### 2.1 Core architecture style

The Hawk Android app follows a feature-based Clean Architecture with a vertical slice structure inside the app module.

Each feature owns its own:

1. `ui`
2. `domain`
3. `data`

Cross-cutting infrastructure is placed in:

1. `common`
2. `utils`
3. `designsystem`
4. `home`

The project is currently implemented in a single Android app module, but it should behave architecturally as if each feature were already a separate module. This means boundaries must be respected even when code lives in the same Gradle module.

### 2.2 Architectural principles

All implementation must respect these principles:

1. `ui` depends on `domain`, never on `data` implementation details.
2. `domain` must be pure Kotlin whenever possible.
3. `data` implements domain contracts and owns IO.
4. Android framework types must not leak into domain entities or use cases.
5. DTOs must never cross into UI or domain untransformed.
6. Repository interfaces represent business-facing contracts.
7. Repository implementations coordinate remote/local data sources and mapping.
8. Dependency direction always points inward toward domain.
9. Features should be vertically cohesive and cross-feature coupling must remain low.
10. Shared utilities must be truly cross-cutting. If a class belongs to only one feature, keep it inside that feature.

### 2.3 App mental model

The app should be understood as a POS client with these characteristics:

1. It is offline-first by design.
2. Remote calls are important but must not define the entire architecture.
3. Local state and persistence are first-class concerns.
4. UI must be resilient to partial connectivity.
5. Error handling must be explicit and user-friendly.
6. Business rules belong in the domain layer, not in Composables.

### 2.4 Current package strategy

The current app already reflects the intended direction:

```text
com.hawk
|- authentication
|  |- data
|  |- domain
|  |- ui
|- products
|  |- data
|  |- domain
|  |- ui
|- common
|  |- database
|  |- environment
|  |- ui
|- designsystem
|- home
|- utils
   |- coroutines
   |- network
```

This structure must continue for every new feature.

---

## 3. Golden rules before writing any code

Before implementing anything, follow this sequence:

1. Read the feature folder first.
2. Read the current navigation entry point.
3. Read the current ViewModel and ViewState if the screen already exists.
4. Read the repository interface before touching data flow.
5. Read the remote and local data source contracts before adding new service calls.
6. Confirm whether the feature already has domain entities or draft models that should be reused.
7. Check if strings, colors, spacing, and common UI patterns already exist.
8. Check whether the behavior belongs in `common` or inside the feature.
9. Check whether the new work changes business rules or only UI behavior.
10. Only then implement.

Never start from the network layer first unless the task is explicitly an API integration task.

---

## 4. Project-wide engineering rules

## 4.1 Language and platform

1. Kotlin is the default language.
2. Java should not be added for new code unless there is a very strong reason.
3. Use JDK 17 compatible code.
4. Prefer immutable data structures.
5. Prefer sealed hierarchies for constrained state models.
6. Prefer explicit nullability handling over defensive scattered checks.
7. Prefer value objects and typed models over raw maps, bundles, and strings.

## 4.2 Framework stack

The default stack for Hawk Android is:

1. Jetpack Compose for UI.
2. ViewModel for screen state ownership.
3. Flows and StateFlow for reactive state.
4. Hilt for dependency injection.
5. Retrofit for HTTP.
6. Kotlinx Serialization for DTO serialization.
7. Room for local persistence.
8. Coroutines for async execution.
9. Navigation Compose for screen navigation.

Any deviation from this stack must be justified.

Compose is not treated in Hawk as a simple XML replacement. It is the main presentation runtime and must be used with these principles:

1. Unidirectional data flow.
2. State hoisting.
3. Immutable screen models.
4. Small reusable UI components.
5. Explicit side-effect handling.
6. Lifecycle-aware state collection.
7. Recomposition-aware implementation choices.

## 4.3 Dependency rules

1. Do not instantiate dependencies directly in screens or ViewModels.
2. Use constructor injection whenever possible.
3. Use Hilt modules only for framework types, factories, bindings, and external clients.
4. Bind interfaces to implementations.
5. Do not inject a concrete repository into UI if an interface exists.
6. Keep DI modules simple and declarative.
7. Avoid service-locator style code.

## 4.4 Code quality rules

1. Favor small classes with one clear responsibility.
2. Favor short, intention-revealing method names.
3. Avoid large God ViewModels.
4. Avoid utility classes that mix unrelated concerns.
5. Avoid hidden mutable state.
6. Avoid boolean parameter ambiguity.
7. Replace magic strings and magic numbers with named constants or domain models.
8. Prefer domain-specific types over generic primitives when it improves clarity.
9. Use extension functions when they clarify behavior, not to hide business logic.
10. Remove dead code quickly.

---

## 5. Feature blueprint

Every new feature should follow this blueprint:

```text
feature-name
|- data
|  |- datasources
|  |  |- local
|  |  |- remote
|  |  |- interfaces
|  |- dto
|  |- mappers
|  |- repository
|  |  |- interfaces
|  |- di
|- domain
|  |- entities
|  |- errors
|  |- usecases
|  |  |- interfaces
|  |  |- impl
|  |- di
|- ui
|  |- screens
|  |- viewmodels
|  |- viewstates
|  |- uimodels
```

Optional subfolders may be added only when complexity justifies them, for example:

1. `navigation`
2. `components`
3. `events`
4. `effects`
5. `validators`
6. `paging`
7. `previews`
8. `transformers`
9. `contracts`

Compose-specific folder guidance:

1. Put screen-level composables in `screens`.
2. Put reusable feature-local composables in `components`.
3. Put preview-only samples in `previews` only when they provide real maintenance value.
4. Do not mix app-level design-system components into feature-local `components`.

---

## 6. UI layer standard

### 6.1 Responsibilities of the UI layer

The UI layer is responsible for:

1. Rendering state.
2. Collecting user input.
3. Sending user actions to the ViewModel.
4. Displaying loading, success, empty, and error states.
5. Displaying navigation effects.
6. Applying design system components and accessibility rules.

The UI layer is not responsible for:

1. Business rules.
2. DTO mapping.
3. Repository orchestration.
4. Remote call composition.
5. Direct database access.

### 6.2 Compose screen rules

1. Screens should be stateless when possible.
2. Route composables own ViewModel collection and effect handling.
3. Pure screen composables should accept immutable state plus callbacks.
4. Use state hoisting consistently.
5. Avoid reading mutable objects directly inside deeply nested Composables.
6. Keep screen functions focused on layout and rendering.
7. Extract reusable composables when a UI piece repeats or becomes conceptually meaningful.
8. Do not place long business conditionals directly in composables.
9. Prefer explicit `when` branches for screen state rendering.
10. Respect keyboard, insets, and accessibility from the first implementation.
11. Prefer parameter lists that describe intent clearly instead of passing whole ViewModels down the tree.
12. Avoid passing mutable collections or mutable state holders deep into the UI tree.
13. Prefer feature-local component extraction before giant single-screen composables become hard to scan.
14. Use `collectAsStateWithLifecycle` at route boundaries for `StateFlow` collection.
15. Use `rememberSaveable` only for UI-local state that must survive configuration changes and does not belong in the ViewModel.

### 6.3 Route and screen pattern

Use this mental split:

1. `Route`
   - gets the ViewModel
   - collects `StateFlow` with lifecycle awareness
   - collects one-off effects
   - connects navigation callbacks
   - owns `LaunchedEffect` blocks for effect collection when needed

2. `Screen`
   - receives immutable state
   - renders UI
   - emits callbacks
   - does not know how navigation is executed

This pattern is already visible in the current navigation and feature setup and should continue.

### 6.4 ViewModel rules

ViewModels are the presentation logic owners.

A ViewModel should:

1. Expose immutable `StateFlow`.
2. Optionally expose `SharedFlow` for one-time effects.
3. Receive use cases via constructor injection.
4. Transform user events into state updates and side effects.
5. Coordinate loading states.
6. Map domain results into UI state.
7. Launch work using injected dispatchers.
8. Expose state that is Compose-friendly and stable to render.

A ViewModel should not:

1. Know DTO details.
2. Call Retrofit directly.
3. Use Android `Context` unless truly unavoidable.
4. Contain navigation controller references.
5. Hold composable-specific types.
6. Depend on resource resolution logic unless represented as IDs or UI models.
7. Expose `MutableStateFlow` publicly.
8. Expose Compose `mutableStateOf` as the default feature-state mechanism when `StateFlow` is the project standard.

### 6.5 ViewState rules

ViewState classes should:

1. Model the screen completely.
2. Be immutable.
3. Prefer `data class` or sealed interface hierarchies.
4. Include enough information to render without hidden lookups.
5. Separate persistent state from transient effects.

Recommended patterns:

1. Simple forms: one `data class` with fields, validation, loading flags, and message references.
2. Multi-phase screens: sealed hierarchy such as `Loading`, `Content`, `Empty`, `Error`.
3. Submission flows: sealed hierarchy such as `Form`, `Submitting`, `Result`.

Compose-specific state rules:

1. Keep screen state coarse enough to describe the UI, but not so coarse that every keystroke causes avoidable full-tree work.
2. Keep transient UI-only flags local to composables only when the state is truly local and disposable.
3. If state affects business flow, process death recovery, submission, or navigation, it belongs in the ViewModel-backed state.

### 6.6 UI models

UI models exist to adapt domain data to presentation needs.

Use UI models when:

1. The screen needs formatted labels.
2. The layout combines multiple domain values.
3. The UI requires icon, color, and text decisions.
4. The screen should not know domain internals.

Do not use UI models as DTO replacements.

### 6.7 Effects

One-off actions must not be stored as normal screen state forever.

Use effects for:

1. Navigation.
2. Snackbar messages.
3. Toasts if ever used.
4. Opening dialogs triggered by momentary events.
5. External app launches.

Use `SharedFlow` or another explicit effect channel, not a persistent state field that keeps re-triggering on recomposition.

### 6.8 Form handling rules

For forms such as login and product creation:

1. Keep raw user input in ViewState.
2. Validate incrementally while typing if it improves UX.
3. Force full validation on submit.
4. Disable submit if required fields are invalid.
5. Show inline errors close to fields.
6. Show form-level errors when the failure is not field-specific.
7. Keep submission state explicit.
8. Reset state deliberately, not accidentally.

### 6.9 Accessibility and usability rules

1. Every click target must be large enough.
2. Text contrast must be readable.
3. Inputs must have labels and useful placeholders when appropriate.
4. Keyboard types must match the input type.
5. Focus order must make sense.
6. Screen behavior under IME must be tested.
7. Scrollable forms must remain usable when the keyboard appears.
8. Important actions must remain visible or reachable.
9. Success and error states must be explicit, not subtle.

### 6.10 Compose performance rules

1. Keep state stable and minimal.
2. Avoid triggering whole-screen recomposition for local changes when smaller state holders can help.
3. Use derived UI state deliberately.
4. Do not overuse `remember` as a band-aid.
5. Avoid expensive work inside composables.
6. Avoid converting data repeatedly during recomposition.
7. Prefer lazy containers for long lists.
8. Use immutable models to help Compose reason about state changes.

Additional Compose performance rules:

1. Supply stable item keys to lazy lists when item identity matters.
2. Prefer `derivedStateOf` for cheap derived UI decisions that would otherwise be recalculated excessively.
3. Avoid sorting, filtering, grouping, and formatting large collections directly inside composables.
4. Avoid creating new lambdas, formatters, regexes, or collections in hot recomposition paths without a reason.
5. Use `rememberUpdatedState` when a side-effect block needs the latest lambda without restarting the effect.
6. Do not hide recomposition issues behind aggressive `remember` usage that makes state harder to reason about.
7. Prefer splitting large composables into smaller render units before reaching for micro-optimizations.

### 6.11 Jetpack Compose implementation standard

This section defines the Compose-specific engineering standard for Hawk.

#### 6.11.1 Compose mental model

Compose in Hawk must be treated as a function of state.

The preferred flow is:

1. User action happens in the composable.
2. Callback goes to the ViewModel.
3. ViewModel updates `StateFlow`.
4. Route collects state with lifecycle awareness.
5. Screen re-renders from immutable state.

Do not shortcut this flow by letting composables own business orchestration.

#### 6.11.2 State ownership rules

1. Business state belongs in the ViewModel.
2. Screen state that drives feature behavior belongs in the ViewModel.
3. Ephemeral presentation state can stay local if it is disposable and not business-relevant.
4. Scroll state, focus requesters, and animation state may stay local to composables unless the feature truly needs to control them from the ViewModel.
5. Text input can live in the ViewModel when it affects validation, submit enablement, or process-death resilience, which is the current Hawk direction.

#### 6.11.3 Side-effect rules

Use Compose side-effect APIs intentionally:

1. `LaunchedEffect` for collecting one-time effects or running suspend side effects tied to composition.
2. `DisposableEffect` for lifecycle-bound registrations and cleanups.
3. `SideEffect` only for publishing state outward to non-Compose objects after successful recomposition.
4. `rememberCoroutineScope` only for UI-local coroutine launches that should remain in the composition scope.

Avoid:

1. Launching coroutines directly in the composable body.
2. Triggering navigation directly from recomposition without effect control.
3. Using `LaunchedEffect(Unit)` everywhere without thinking about the actual key.

#### 6.11.4 Component API design rules

Reusable composables should:

1. Take explicit parameters.
2. Prefer state plus callbacks over passing controller objects.
3. Keep modifiers as the first optional UI parameter when appropriate.
4. Expose slots only when they add real flexibility.
5. Keep naming aligned with domain or design-system intent.

Avoid:

1. Components with extremely long ambiguous parameter lists.
2. Components that know feature navigation details.
3. Components that internally fetch data.

#### 6.11.5 Form component rules

For Compose forms:

1. Use keyboard options that match the input purpose.
2. Connect IME actions intentionally.
3. Manage focus movement explicitly when it improves flow.
4. Keep action buttons reachable under keyboard pressure.
5. Treat validation text, helper text, and error text as part of the UI contract.

#### 6.11.6 Preview rules

Compose previews are encouraged, but they are not a replacement for tests.

Rules:

1. Preview pure composables, not route-level composables with DI.
2. Feed previews with stable fake state.
3. Add previews for key states when it saves real iteration time, such as loading, success, error, and populated content.
4. Do not maintain previews that no longer compile or no longer represent real screen states.

#### 6.11.7 Recomposition anti-patterns to avoid

1. Building large derived lists inside the composable on every frame.
2. Passing unstable mutable objects through many composable levels.
3. Reading broad state at the top of the screen when only a small subsection needs it.
4. Putting expensive formatting directly in item lambdas for long lazy lists.
5. Using `remember` without understanding what should survive recomposition and what should not.
6. Triggering business actions from composition instead of from explicit events or controlled effects.

---

## 7. Domain layer standard

### 7.1 Responsibilities of the domain layer

The domain layer owns:

1. Business entities.
2. Value objects.
3. Repository interfaces.
4. Use cases.
5. Domain errors.
6. Validation rules that are business-oriented.

The domain layer must be independent from:

1. Retrofit.
2. Room.
3. Android framework APIs.
4. Compose.
5. Resource resolution.

Domain rules related to Compose:

1. Domain models must not be shaped to satisfy composable parameter convenience.
2. Domain use cases must not expose UI widget concepts.
3. Domain validation should stay reusable across Compose screens and future non-Compose surfaces.

### 7.2 Domain entity rules

Entities should:

1. Represent business meaning, not transport shape.
2. Use clear names from the Hawk domain.
3. Avoid nullable fields unless the business meaning truly allows absence.
4. Prefer nested value objects when they improve clarity.
5. Encode invariants where possible.

Examples in this app:

1. `AuthenticationSession`
2. `Product`
3. `CreateProductDraft`

### 7.3 Use case rules

Use cases are the primary entry points from presentation into domain behavior.

Use cases should:

1. Represent one business action.
2. Have a focused API.
3. Depend only on domain contracts.
4. Return domain models or explicit result types.
5. Be easy to unit test.

Examples:

1. `AuthenticateUserUseCase`
2. `ValidateLoginEmailUseCase`
3. `GetProductsUseCase`
4. `CreateProductUseCase`

Do not put multiple unrelated business actions in one use case.

### 7.4 Domain validation rules

Validation belongs in domain when:

1. It represents a business invariant.
2. It will be reused in multiple screens.
3. It should stay consistent across UI implementations.

Validation may stay in UI when:

1. It is purely presentation-oriented.
2. It only controls field touched state or inline display timing.

### 7.5 Domain errors

Use domain error types to represent meaningful failure categories.

Examples:

1. Invalid credentials.
2. No connection.
3. Invalid payload.
4. Product write failed.

This is better than passing raw exceptions directly to the UI.

### 7.6 Repository interface rules

Repository interfaces belong in domain-facing contracts even if stored under feature data interfaces today.

They should:

1. Speak in domain language.
2. Hide data-source complexity.
3. Avoid exposing DTOs.
4. Model capabilities the feature needs, not transport-level operations.

---

## 8. Data layer standard

### 8.1 Responsibilities of the data layer

The data layer owns:

1. API interfaces.
2. DTOs.
3. Local database models.
4. DAO integration.
5. Mapper implementations.
6. Repository implementations.
7. Remote and local data source implementations.
8. Caching and synchronization logic.

### 8.2 Data layer rules

1. DTOs are transport models only.
2. Database models are storage models only.
3. Domain entities are business models only.
4. Mappers are the boundary translators.
5. Repositories choose where data comes from.
6. Data sources should stay simple and focused.

Data layer and Compose relationship rules:

1. Data layer classes must never depend on Compose runtime types.
2. Data layer output should already be stable enough for ViewModels to transform into UI state cleanly.
3. Formatting for composables belongs above the repository layer.

### 8.3 Remote data source rules

Remote data sources should:

1. Own the direct Retrofit interaction.
2. Return typed results from API calls.
3. Convert response failures into meaningful exceptions or result wrappers.
4. Avoid UI logic.
5. Avoid domain formatting.
6. Avoid direct navigation or analytics calls.
7. Avoid owning repository-level connectivity routing or remote-vs-local selection policy.

Remote data sources should not:

1. Parse JSON manually when Retrofit with Kotlinx Serialization can do it.
2. Construct raw `RequestBody` unless a converter is genuinely required.
3. Leak `ResponseBody` or raw JSON to upper layers.

### 8.4 Retrofit rules

These rules are mandatory for Hawk:

1. API suspend functions should return `retrofit2.Response<T>`.
2. Use the most conventional Retrofit annotation style for the request type whenever the request is small and simple.
3. If a request has more than 6 fields, nested structure, repeated reuse, or meaningful transport complexity, model it as a DTO object.
4. Use DTOs by default for JSON bodies and larger payloads.
5. Use Retrofit native styles such as `@Field`, `@Query`, `@Path`, `@Part`, or `@Header` when they are the clearest fit for a small request.
6. The shared HTTP infrastructure belongs in `NetworkModule`.
7. Feature APIs should be created from the shared `Retrofit.Builder`.
8. Feature-specific base URLs must be applied in the feature DI module.
9. Feature-specific converters must be added only when needed.
10. Common JSON conversion should come from Kotlinx Serialization.
11. Avoid duplicate Retrofit client creation patterns across features.
12. Prefer relative paths with a feature base URL instead of building full URLs everywhere.
13. Keep endpoint path constants in environment or configuration objects.

### 8.5 Current Retrofit architecture rule

The current project has a shared `NetworkModule` and feature-specific API creation.

That is the correct direction:

1. `NetworkModule` owns shared `OkHttpClient`, logging interceptor, and JSON converter factory.
2. Feature DI modules use the shared `Retrofit.Builder`.
3. `authentication` uses standard Retrofit `@FormUrlEncoded` request fields because the token request is small and conventional.
4. `products` uses the shared JSON converter for DTO-based payloads.

This pattern should be preserved for all new features.

### 8.6 DTO rules

DTOs must:

1. Live in `data/dto`.
2. Be annotated for Kotlinx Serialization when needed.
3. Mirror the API contract accurately.
4. Use nested DTOs when the payload is nested.
5. Never be passed into UI directly.
6. Never contain UI-specific formatting logic.

### 8.7 Mapper rules

Mappers are mandatory boundaries.

Use mappers for:

1. DTO to domain.
2. Domain to DTO.
3. Entity to UI model, when done outside the ViewModel.
4. DB model to domain.
5. Domain to DB model.

Mapping rules:

1. Keep mapping deterministic.
2. Avoid hidden side effects.
3. Keep domain transformation logic close to the mapper, not scattered.
4. Prefer small explicit mapping functions.
5. Test complicated mappers.

### 8.8 Repository implementation rules

Repository implementations should:

1. Implement repository interfaces.
2. Coordinate remote and local sources.
3. Decide caching, synchronization, and fallback behavior.
4. Expose domain-oriented outputs.
5. Remain free from UI wording and screen concepts.
6. Own the connectivity-based source selection policy.
7. Treat best-effort cache refresh writes as repository concerns, not remote data source concerns.

Repository implementations should not:

1. Return DTOs.
2. Depend on composables.
3. Depend on Android views.
4. Build navigation decisions.

### 8.9 Local data source rules

Local data sources should:

1. Own DAO usage.
2. Own Room query integration.
3. Return storage results or mapped outputs.
4. Be reusable by repository implementations.
5. Prefer `upsert` by primary key for paginated cache refreshes instead of whole-table replacement.
6. Use business-purpose naming for entities, DAOs, and tables instead of storage-strategy naming.

Local naming rules:

1. Prefer names like `CustomerEntity`, `CustomerDao`, and `customers`.
2. Do not use `Cache` as a default prefix or suffix in Room entity, DAO, table, or local data source names.
3. Reserve technical names such as `sync_queue`, `remote_keys`, or `drafts` only for tables that truly have those roles.

Use local data storage for:

1. Session persistence.
2. Cached catalogs.
3. Offline-first screens.
4. Retry-safe draft persistence.

### 8.10 Offline-first rules

Because Hawk is a POS app, offline-first is a strategic rule, not an optional enhancement.

For every new feature, ask:

1. What should work without internet?
2. What data should be cached locally?
3. What actions must be queued or retried?
4. What does the user see when the device is offline?
5. How do we reconcile remote and local truth later?

Every feature does not need full sync on day one, but the architecture must leave room for it.

When a remote paginated response is used to refresh local cache data:

1. Do not clear the whole table from a partial page response.
2. Upsert only the rows present in the remote payload using stable primary keys.
3. Treat deletions and full reconciliation as a separate sync policy, not as an implicit side effect of one page fetch.
4. If the cache write is best-effort and should not delay the remote result, run it from an application coroutine scope owned by DI.

---

## 9. Common and cross-cutting layer standard

### 9.1 `common`

Use `common` for foundational, stable, cross-feature pieces such as:

1. Database container classes.
2. Environment configuration.
3. Shared UI infrastructure.
4. Reusable app-wide primitives.

If a class is useful only to one feature, do not put it in `common`.

### 9.2 `utils`

Use `utils` for infrastructural helpers that are not business-specific, such as:

1. Coroutine dispatchers and helpers.
2. Network observation.
3. Shared DI provider modules.
4. Low-level framework integrations.

Do not turn `utils` into a dumping ground.

### 9.3 `designsystem`

Use `designsystem` for shared UI tokens and components:

1. Typography definitions.
2. Color system.
3. Common buttons.
4. Text fields.
5. Cards.
6. Status components.
7. Spacing or shape tokens if formalized.

Feature-specific visuals should not pollute the design system.

### 9.4 `home`

Use `home` for app-level navigation and entry orchestration, not business logic.

Navigation decisions should stay close to the app shell and route layer.

---

## 10. Dependency injection standard

### 10.1 Hilt rules

1. Use constructor injection by default.
2. Use `@Binds` for interface to implementation.
3. Use `@Provides` only when constructor injection is not possible or not appropriate.
4. Keep modules small and purpose-driven.
5. Prefer one module per feature layer concern when that improves readability.
6. Qualify ambiguous dependencies such as dispatchers.

### 10.2 Module organization

Recommended module ownership:

1. `feature/data/di`
   - API wiring
   - repository bindings
   - data source bindings

2. `feature/domain/di`
   - use case bindings

3. `utils/network/di`
   - shared HTTP infrastructure

4. `utils/coroutines`
   - dispatcher qualifiers and providers

### 10.3 Qualifier rules

Use qualifiers for:

1. Dispatchers.
2. Multiple Retrofit clients if needed in the future.
3. Multiple OkHttp instances if truly required.
4. Multiple persistence stores if introduced.

Avoid ambiguous global types without qualifiers when multiple implementations may appear later.

---

## 11. Networking and API integration standard

### 11.1 HTTP client rules

1. Keep one shared base HTTP stack unless there is a strong need for separation.
2. Add interceptors centrally.
3. Configure logging based on build type.
4. Keep timeouts and retry rules explicit if added later.
5. Avoid feature-specific networking duplication.

### 11.2 Error handling rules

Every API integration must explicitly handle:

1. No connection.
2. Timeout or IO issues.
3. Non-success HTTP codes.
4. Empty response body when body is required.
5. Invalid payload mapping.
6. Domain-specific failure states.

Do not rely on generic `Exception` messages in the UI.

### 11.3 Auth-specific rules

The authentication flow currently integrates with Keycloak and requires form-url-encoded request conversion.

Rules:

1. Use Retrofit `@FormUrlEncoded` with `@Field` parameters when the token request remains small and conventional.
2. Prefer a request DTO only if the auth payload grows beyond 6 fields, becomes nested, or is reused in a way that improves clarity.
3. Keep authentication exceptions meaningful for the login flow.
4. Never leak Keycloak transport details into UI code.
5. Keep auth-specific transport choices inside the auth feature.

### 11.4 Product-specific rules

The products feature currently uses typed DTOs and typed Retrofit responses.

Rules:

1. Keep product transport models nested when the API is nested.
2. Map product DTOs to domain entities in the mapper layer.
3. Handle product write failures as explicit domain error categories.
4. Preserve create, list, and future inventory update flows as separate use cases.

---

## 12. Persistence and Room standard

### 12.1 Room rules

1. Room is the default local persistence mechanism.
2. Each table should have a clear business purpose.
3. DAO interfaces should be focused and cohesive.
4. Entities used by Room should stay in the data layer.
5. Do not leak Room entities into domain or UI.
6. Use mappers between Room models and domain models.
7. Keep Room naming business-oriented.
8. Prefer table names that describe the stored concept, such as `customers`, not the implementation strategy.

### 12.2 Database evolution rules

1. During early development, while schema is still moving, destructive recreation is acceptable.
2. Once the schema starts stabilizing or real user data matters, move to explicit migrations.
3. Keep table names and columns consistent, descriptive, and business-oriented.
4. If schema creation SQL is externalized, keep it in dedicated schema script files and load it from the database setup layer.
5. Room entities still define the runtime schema contract even when bootstrap SQL files exist.
6. Be careful with destructive changes because POS data often matters operationally.

### 12.3 Caching rules

When introducing caching:

1. Define cache ownership in the repository.
2. Define refresh triggers.
3. Define stale data policy.
4. Define write-through or write-behind behavior explicitly.
5. Define how errors are surfaced when local and remote disagree.

---

## 13. Navigation standard

### 13.1 Navigation ownership

1. App-level navigation graphs belong in `home`.
2. Route composables connect navigation callbacks.
3. ViewModels emit effects, not direct `NavController` calls.
4. Screen composables stay navigation-agnostic.

### 13.2 Navigation rules

1. Use clear destination names.
2. Pass only the minimum safe navigation arguments.
3. Do not pass huge objects through navigation.
4. Prefer IDs or compact route arguments when state can be reloaded.
5. Handle back-stack rules intentionally.

### 13.3 Result screen rules

For submission flows:

1. Success screens should clearly confirm completion.
2. Error screens should provide recovery paths.
3. Continue or retry actions must be explicit.
4. Navigation after submission should avoid duplicated destinations.

---

## 14. UI design and design-system rules

### 14.1 General visual rules

1. Follow the Figma source closely for product work unless a technical adaptation is needed.
2. Preserve the design language of Hawk.
3. Keep spacing and typography consistent.
4. Avoid random local style decisions inside feature code.

Compose design translation rules:

1. Convert repeated Figma patterns into reusable composables instead of cloning large UI trees.
2. Prefer theme and token-driven styling over local one-off values.
3. Keep layout intent explicit through Compose structure, not accidental spacer accumulation.

### 14.2 Resource rules

1. User-facing strings belong in `strings.xml`.
2. Colors should be tokenized when shared.
3. Avoid hardcoded display text in composables unless it is temporary and about to be externalized.
4. Use vector or themed drawables where appropriate.

### 14.3 Compose design-system implementation rules

1. Shared Compose components should live in `designsystem` only when they are truly cross-feature.
2. Feature composables should consume the design system rather than re-implement core controls repeatedly.
3. Design-system composables should be stateless by default.
4. Theme tokens should be the first place to look before introducing a new raw color, typography, or spacing value.
5. Components should support accessibility semantics as part of their default contract.

### 14.4 Keyboard and insets rules

This project already needs keyboard-aware behavior.

Rules:

1. Screens with inputs must behave correctly when the IME opens.
2. Scrollable forms must remain reachable.
3. Shared keyboard behavior should live in reusable common UI helpers.
4. Visible scrollbar behavior, IME padding, and resize-safe layout should be considered standard for form screens.

---

## 15. Coroutines and Flow standard

### 15.1 Dispatcher rules

1. Do not hardcode `Dispatchers.IO` or `Dispatchers.Default` in business logic classes.
2. Inject dispatchers via qualifiers.
3. ViewModels should launch through injected dispatchers or standardized helpers.
4. IO work should run on `IoDispatcher`.

### 15.2 Flow rules

1. Use `StateFlow` for persistent UI state.
2. Use `SharedFlow` for one-time effects.
3. Use `flowOn` at the source boundary for IO-heavy work.
4. Keep Flow chains readable.
5. Avoid overcomplicated reactive pipelines when a direct suspend operation is clearer.

### 15.3 Coroutine safety rules

1. Avoid leaking coroutines outside lifecycle-aware scopes.
2. Avoid launching duplicate jobs for the same action unless required.
3. Explicitly protect against repeated submit taps when needed.
4. Ensure loading flags are consistent on success and failure.

---

## 16. Error modeling and resilience standard

### 16.1 Error categories

Prefer these categories when modeling failures:

1. Validation error.
2. Authentication error.
3. Authorization error.
4. Connectivity error.
5. Remote service failure.
6. Empty or invalid payload.
7. Persistence error.
8. Unknown error.

### 16.2 UI error rules

1. User-facing error messages must be actionable when possible.
2. Do not expose raw backend messages unless safe and meaningful.
3. Distinguish between recoverable and unrecoverable failures.
4. Distinguish between field errors and screen-level errors.

### 16.3 Technical error rules

1. Preserve useful technical context at lower layers.
2. Translate low-level exceptions into domain-friendly categories before reaching UI.
3. Avoid swallowing exceptions silently.

---

## 17. Testing standard

### 17.1 Testing priorities

Test these in order of value:

1. Domain use cases.
2. Mappers.
3. Repository behavior.
4. ViewModel state transitions.
5. Critical UI flows.

### 17.2 Unit test rules

1. Domain tests must be pure and fast.
2. Mapper tests should verify boundary correctness.
3. ViewModel tests should assert state progression and effects.
4. Repository tests should verify remote/local orchestration and failure behavior.
5. Use fake or mock dependencies intentionally, not excessively.

### 17.3 UI test rules

Use UI tests for:

1. Authentication flow.
2. Create product flow.
3. Keyboard behavior on forms.
4. Success and error state rendering.
5. Navigation-critical flows.

Compose-specific UI testing rules:

1. Prefer testing screen behavior through semantics and user-visible outcomes, not internal implementation details.
2. Test important branches of screen state rendering.
3. Test lazy list rendering and action callbacks for major feature screens.
4. Add regression tests for IME-sensitive forms and result-state screens when they are business-critical.

### 17.4 What must be tested when adding a feature

At minimum:

1. Use case happy path.
2. ViewModel happy path.
3. One representative failure path.
4. Mapper correctness if mapping is non-trivial.

---

## 18. Performance, ANR, and memory rules

### 18.1 Main-thread rules

1. Never perform network work on the main thread.
2. Never perform Room writes on the main thread.
3. Avoid heavy parsing in Composables or on the main thread.
4. Keep recomposition light.
5. Avoid expensive image, formatting, and collection work in lazy item content lambdas.

### 18.2 ANR prevention rules

1. Do not block the UI thread.
2. Do not perform synchronous disk or network work from UI callbacks.
3. Be careful with oversized lists and expensive sort/filter operations in composables.

Compose performance and responsiveness rules:

1. Use lazy layouts for potentially growing lists instead of eager `Column` rendering.
2. Provide stable keys for lazy items to reduce visual churn and state loss.
3. Avoid deeply nested scroll containers unless the behavior is necessary and tested.
4. Keep screen-level state reads as narrow as practical so small updates do not fan out unnecessarily.

### 18.3 Memory rules

1. Do not retain `Context` unnecessarily.
2. Do not keep screen-scoped objects in static state.
3. Do not leak listeners or callbacks.
4. Be careful with long-lived observers.
5. Keep large payloads out of navigation arguments and saved state when possible.
6. Dispose composition-bound registrations and listeners correctly with the proper side-effect API.

---

## 19. Security and configuration rules

### 19.1 Secrets and config

1. Never hardcode secrets in source.
2. Use `BuildConfig` or secure configuration strategies for environment values.
3. Centralize environment access through `AppEnvironment`.
4. Avoid duplicating endpoint strings in features.

### 19.2 Auth handling

1. Token and session management must be explicit.
2. Authentication transport details should remain in the auth feature.
3. Sensitive values must not be logged in plaintext.

---

## 20. Naming conventions

### 20.1 Package names

1. Use lowercase package names.
2. Organize by feature first, then layer.
3. Avoid overly generic packages like `helpers` or `misc`.

### 20.2 Class naming

Use clear suffixes consistently:

1. `ViewModel`
2. `ViewState`
3. `UiModel`
4. `UseCase`
5. `Repository`
6. `RepositoryImpl`
7. `RemoteDataSource`
8. `LocalDataSource`
9. `Dto`
10. `Entity`
11. `Route`
12. `Screen`

### 20.3 Method naming

1. Use verbs for actions.
2. Use `onXChanged`, `onXClicked`, `resetX`, `loadX`, `createX`, `validateX`.
3. Avoid vague names such as `doStuff`, `processData`, or `handle`.

---

## 21. Anti-patterns that are not allowed

Do not introduce these patterns:

1. Composables calling Retrofit directly.
2. ViewModels depending on DTOs.
3. Repositories returning UI models.
4. Manual JSON parsing where typed converters exist.
5. Raw `RequestBody` construction or custom converters when standard Retrofit annotations already solve the request cleanly.
6. Hardcoded strings spread across Composables.
7. Android `Context` inside domain logic.
8. Business rules buried in navigation lambdas.
9. Feature logic hidden inside `common` or `utils`.
10. Mutable singleton state for screen data.
11. Large monolithic ViewModels that own multiple screens.
12. Generic catch-all errors shown to the user without classification.
13. Passing a ViewModel through many composable layers instead of passing only required state and callbacks.
14. Collecting Flow directly in deeply nested UI without lifecycle-aware ownership at the route boundary.
15. Using `rememberSaveable` to replace proper ViewModel state ownership.
16. Triggering navigation or repository work directly from recomposition.
17. Putting heavy list transformations inside `LazyColumn` item blocks.
18. Creating feature-specific design-system copies inside screen files.

---

## 22. CLI development workflow standard

This app is often developed from the CLI, so workflow discipline matters.

### 22.1 Before coding

1. Read the target feature folder.
2. Read the DI module.
3. Read the ViewModel.
4. Read the ViewState.
5. Read the repository contract.
6. Read the current navigation wiring.
7. Read the current composable structure to understand where state is collected and where pure rendering begins.

### 22.2 During coding

1. Make incremental changes.
2. Keep edits scoped to the feature when possible.
3. Avoid drive-by refactors unless they unblock the feature or fix a real defect.
4. Keep architecture boundaries intact even under time pressure.
5. Check whether a new composable belongs in `screens`, `components`, or `designsystem` before creating it.
6. Review whether state should be local, saved, or ViewModel-owned before adding it.

### 22.3 Verification commands

At minimum, use:

```powershell
.\gradlew.bat :app:compileDebugKotlin --no-daemon
```

Useful follow-up commands:

```powershell
.\gradlew.bat :app:assembleDebug --no-daemon
.\gradlew.bat :app:testDebugUnitTest --no-daemon
```

### 22.4 CLI review checklist

Before finishing a task, verify:

1. The feature compiles.
2. Imports are clean.
3. No DTO leaks into UI.
4. No Android framework leaks into domain.
5. Network and persistence logic stay in data.
6. Strings are externalized when user-facing.
7. ViewState fully represents the screen.
8. Navigation still works.
9. Keyboard and insets still work for forms.
10. Error states are covered.
11. Compose state collection happens in the right layer.
12. Large composables are still readable and decomposed appropriately.
13. No obvious recomposition hotspot was introduced by local formatting or list transformation logic.

---

## 23. Definition of done for a new screen

A screen is not done when it only looks correct.

A screen is done when:

1. It matches the intended UX.
2. It has a route and screen split when appropriate.
3. It has a ViewModel with explicit state.
4. It uses domain use cases.
5. It respects feature boundaries.
6. It handles loading.
7. It handles success.
8. It handles failure.
9. It handles keyboard/insets if it has inputs.
10. It compiles.
11. It is testable.
12. It does not introduce architectural shortcuts that future work will regret.

---

## 24. Definition of done for a new API integration

An API integration is done when:

1. DTOs are defined.
2. Retrofit interface returns `Response<T>`.
3. A data source owns the HTTP call.
4. Repository implementation maps the result to domain.
5. Domain use case exposes the business operation.
6. UI consumes the use case through a ViewModel.
7. Connectivity and HTTP failure paths are handled.
8. Success and failure user feedback exist.
9. The feature compiles.

---

## 25. Recommended implementation order for new work

When building a new feature or screen, follow this order:

1. Confirm the user flow and state machine.
2. Define or update domain entities.
3. Define repository contract if needed.
4. Define use case interface and implementation.
5. Define DTOs and API contract.
6. Implement remote/local data sources.
7. Implement repository mapping and orchestration.
8. Wire DI.
9. Implement ViewState and effects.
10. Implement ViewModel.
11. Implement route and screen composables.
12. Hook navigation.
13. Add strings and resources.
14. Compile.
15. Add or update tests.

This order reduces rework and keeps the design consistent.

---

## 26. Final engineering posture for Hawk Android

When in doubt, choose the option that is:

1. More explicit.
2. More typed.
3. More testable.
4. More feature-local.
5. More respectful of clean boundaries.
6. More resilient to offline behavior.
7. Easier for the next engineer to understand.

The codebase should feel like a serious POS client, not like a demo app with networking attached to screens.

Every feature should be able to grow without collapsing into:

1. DTO-driven UI.
2. ViewModel bloat.
3. Retrofit leakage.
4. Shared utility chaos.
5. Navigation-driven business logic.

If a new implementation makes the boundaries clearer, the change is probably moving in the right direction.
