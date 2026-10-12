# AGENTS.md — MiniLeetCode

Compact repo guide for OpenCode sessions. If a fact is obvious from filenames, it is not here.

## Project shape

- **Backend**: Spring Boot 4.1.1, Java 17, Maven wrapper (`mvnw`/`mvnw.cmd`).
- **Frontend**: Vue 3 (currently pinned to the `rc` release), Vite, TypeScript, Vuetify 4. Code lives in `frontend/`.
- **Database**: PostgreSQL 17. JPA `ddl-auto` is `update` in dev, `create-drop` in CI.
- **Cache/rate-limiting**: Redis on `localhost:6379`.
- **Code execution**: Docker (locally installed Docker required). The backend spawns per-submission images.

## Dev environment prerequisites

1. Java 17 JDK and Maven wrapper work out of the box (wrapper is committed).
2. Node.js `^22.18.0 || >=24.12.0` and `npm` for the frontend.
3. PostgreSQL running with a database named `minileetcode`.
4. Redis running on `localhost:6379` (no password configured).
5. Docker available to the user running the backend for code execution.

## Configuration

- Copy `.env.example` to `.env` in the repo root. `.env` is gitignored.
- Required variables: `POSTGRES_URL`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD`, `ADMIN_USERNAME`, `ADMIN_PASSWORD`.
- Optional: `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM`, `FRONTEND_URL` enable the forgot-password mail flow. Without `MAIL_HOST` the app still boots; the reset endpoints answer 503 "not configured".
- `application.properties` imports `.env` via `spring.config.import=optional:file:.env[.properties]`.
- Backend port is `8082`; frontend dev server is `5173`; CORS is already wired to `http://localhost:5173`.
- The default admin account is created automatically at startup with username/password from `ADMIN_USERNAME`/`ADMIN_PASSWORD` (fallback hardcoded `admin`/`admin` in `SetupDataLoader`).

## Running locally

Backend:

```bash
./mvnw spring-boot:run
```

Frontend (from `frontend/`):

```bash
npm install
npm run dev
```

Both must run simultaneously. The frontend talks to `http://localhost:8082` via credentials-included requests.

## Build and verify

Backend (also runs tests; requires Postgres because integration tests spin up the context):

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

Frontend:

```bash
cd frontend
npm ci
npm run lint      # oxlint + eslint, cached
npm run build     # runs type-check then vite build
npm test          # vitest once
npm run test:watch
```

CI order (`.github/workflows/ci.yml` and `main.yml`, identical): `mvnw verify`, then frontend `npm ci` → `npm run lint` → `npm run build`.

## Testing notes

- Backend tests:
  - `MiniLeetCodeApplicationTests` is a `@SpringBootTest`; it needs a database connection.
  - `MiniLeetCodeSignupUnitTest` is a pure unit test.
  - Run a single test class: `./mvnw test -Dtest=MiniLeetCodeSignupUnitTest`
  - Skip tests during packaging: `./mvnw verify -DskipTests`
- Frontend tests live in `frontend/src/tests/**/*.spec.ts` and run under Vitest in a Node environment (`vite.config.ts`).

## Architecture gotchas

- **Authentication is session-based**, not JWT despite the README mention. `JSESSIONID` is the cookie; `SecurityConfig` disables form login and HTTP basic. The frontend relies on `credentials: "include"` everywhere.
- **Role model**: users get a single `UserRole` enum value plus entries in the `users_roles` join table. Spring Security authorities are built from the role name (`ROLE_USER`/`ROLE_ADMIN`) plus attached `Permissions`. Many endpoints still do manual `checkIfAdmin` checks in controllers.
- **Rate limiting** on `/api/user/signup` uses Redis (`RateLimitingFilter`). Without Redis, the filter will fail on signup.
- **Code execution flow**:
  1. `CodeController.submitCode` creates a temporary directory under `CodeEnv/<language>/tempEnv-...`.
  2. It copies the language `Dockerfile` from `CodeEnv/<language>`, the test runner script from `CodeEnv/TestSuite/runCommandTest.bs`, and the test cases from the challenge.
  3. It builds and runs a one-off Docker image, then deletes the temp directory.
  4. Output must match the pattern `Passed: N, Failed: M` parsed by `DockerCodeStrategy`.
- **Supported languages**: `python`, `java`, `csharp`, `cplusplus`. The UI still displays them as Python, Java, C#, and C++. The runner/template combination order differs by language family in `prepareTemporaryEnvForSubmissionTest`.
- **Language metadata seeding**: `SetupDataLoader` reads `CodeEnv/<language>/Dockerfile` and `CodeEnv/TestSuite/runCommandTest.bs` on first startup and stores them in `Container` and `TestRunner` tables. Changing those files requires restarting the backend to re-seed.
- **Admin endpoints** are under `/api/admin/**` and require `ROLE_ADMIN`. Some stats endpoints additionally require `VIEW_STATISTICS`/`VIEW_USERS`/`VIEW_SUBMISSIONS`/`MODIFY_USERS` permissions.

## Things likely to trip up an agent

- `frontend/package.json` pins `vue` to `rc` and uses `overrides` to force all `@vue/*` packages to `rc`. Do not upgrade Vue piecemeal; use the override block or wait for a stable migration.
- `npm run build` is not just Vite; it invokes `vue-tsc --build` first via `run-p`.
- The frontend `tsconfig.json` is a project-references setup (`tsconfig.app.json` + `tsconfig.node.json`). Do not run plain `tsc`; use `npm run type-check`.
- `npm run format` is scoped to `src/` and is not run automatically; lint does not auto-fix.
- The backend source root is `src/main/java/minicode/com/`, not the older `entity/` top-level package. The root Spring Boot application is `minicode.com.MiniLeetCodeApplication`.
- `CodeEnv/*/submission.*` and other generated execution files are gitignored. Do not commit files inside `submissions/` or `CodeEnv/*/execution-*/`.
- `target/` and `frontend/dist/` are build outputs and gitignored.
