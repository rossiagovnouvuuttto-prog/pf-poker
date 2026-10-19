FROM node:22-bookworm-slim
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends openssl ca-certificates && rm -rf /var/lib/apt/lists/*
COPY pf-server/server-src.tar.gz.base64 /tmp/source.base64
RUN base64 -d /tmp/source.base64 | tar -xz -C /app && rm /tmp/source.base64
RUN npm install --no-audit --no-fund --include=dev && npm run build
ENV NODE_ENV=production
ENV PORT=3000
EXPOSE 3000
CMD ["sh", "-c", "npm run db:push -w @pf/api && npm run start -w @pf/api"]
