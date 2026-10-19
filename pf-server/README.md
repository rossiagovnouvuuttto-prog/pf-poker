# PF Poker backend — Railway deployment

This branch contains the PF Poker Android APK workflow and the deployable NestJS/Prisma poker server. Source archive: `pf-server/server-src.tar.gz.base64`, extracted by the Dockerfile.

## Deploy on Railway

1. Connect Railway and select GitHub repository `rossiagovnouvuuttto-prog/pf-poker`, branch `pf-poker-android`.
2. Add a PostgreSQL service to the Railway project.
3. Add backend from GitHub using the root `Dockerfile`.
4. Add backend variables:
   - `DATABASE_URL=${{Postgres.DATABASE_URL}}` (rename Postgres to match actual database service).
   - `JWT_SECRET` = generated secret of at least 32 characters, keep private.
   - `CORS_ORIGIN=*` (MVP only; restrict later).
5. Enable a public HTTPS domain. Test `https://YOUR-DOMAIN/health`, which should return `{"status":"ok","service":"pf-poker-api"}`.
6. Open PF Poker APK; set *Server URL* to `https://YOUR-DOMAIN`, tap *Save server address*, then register.

## Important notes

- Mobile app needs no rebuild to change the endpoint.
- Backend and PostgreSQL hosting may incur costs; check the provider plan.
- Virtual chips only; no payments.
- Single process server; no horizontally scaled WebSocket yet.
- `prisma db push` is for initial MVP setup; use real versioned migrations for production.
- Do not commit passwords, JWT secrets or DB URLs.
