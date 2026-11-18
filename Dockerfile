# syntax=docker/dockerfile:1
# ---------------------------------------------------------------------------
# Tô Bem API — imagem da aplicação Java (Spring Boot 4 / Java 21)
# Build pela raiz do repositório: docker compose up -d --build
#
# Etapa 1 (build): compila o projeto com Maven e gera o .jar.
# Etapa 2 (runtime): só o JRE + o .jar, rodando com usuário sem privilégios.
# ---------------------------------------------------------------------------

# ===== Etapa 1: build =====
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copia primeiro o pom.xml: se só o código mudar, as dependências
# baixadas ficam em cache e o build fica mais rápido.
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -DskipTests package \
    && cp target/to-bem-api-*.jar /build/app.jar


# ===== Etapa 2: runtime =====
FROM eclipse-temurin:21-jre-alpine

LABEL org.opencontainers.image.title="tobem-api" \
      org.opencontainers.image.description="API do Tô Bem - monitoramento de idosos (Global Solution FIAP 2026/2 - CitizenTech)"

# Usuário e grupo sem privilégios (não-root) para executar a aplicação
# (UID/GID fixos 10001 para não coincidir com usuários da VM)
RUN addgroup -S -g 10001 tobem \
    && adduser -S -u 10001 -G tobem -h /app -s /sbin/nologin tobem

# Diretório de trabalho da aplicação
WORKDIR /app

COPY --from=build --chown=tobem:tobem /build/app.jar app.jar

# Variáveis de ambiente com valores padrão (o docker-compose.yml sobrescreve).
# JAVA_TOOL_OPTIONS é lida pela própria JVM (memória e fuso horário).
ENV SERVER_PORT=8080 \
    JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m -Duser.timezone=America/Sao_Paulo"

# Porta exposta pela API
EXPOSE 8080

# A partir daqui tudo roda como o usuário "tobem" (UID 10001), sem privilégios
USER 10001:10001

# O Docker marca o container como "healthy" quando a API responde UP
# Forma "shell" de propósito: usa pipe e a variável SERVER_PORT.
# hadolint ignore=DL3025
HEALTHCHECK --interval=30s --timeout=5s --start-period=120s --retries=5 \
    CMD wget -qO- "http://localhost:${SERVER_PORT}/actuator/health" | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
