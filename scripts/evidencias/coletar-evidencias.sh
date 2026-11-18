#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Mostra, em sequência, as evidências técnicas pedidas na entrega:
# containers rodando, imagens, volume, rede, usuário de cada container,
# estrutura de diretórios, variáveis de ambiente, healthcheck e logs.
#
# Dentro da VM, na raiz do repositório:
#   bash scripts/evidencias/coletar-evidencias.sh | tee evidencias.txt
# ---------------------------------------------------------------------------
set -uo pipefail

cd "$(dirname "$0")/../.." || exit 1

secao() { printf '\n\033[1;36m===== %s =====\033[0m\n$ %s\n' "$1" "$2"; bash -c "$2"; }

secao "VM e Docker" "hostname; uname -a; docker --version; docker compose version"
secao "Containers do compose (em background)" "docker compose ps"
secao "Todos os containers" "docker ps --format 'table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}'"
secao "Imagens" "docker images | grep -E 'REPOSITORY|tobem-api|oracle-free'"
secao "Volume nomeado do banco" "docker volume ls --filter name=tobem; docker volume inspect tobem-oracle-data --format 'Volume {{.Name}} em {{.Mountpoint}}'"
secao "Rede entre os containers" "docker network inspect tobem-net --format '{{range .Containers}}{{.Name}} -> {{.IPv4Address}}{{println}}{{end}}'"
secao "Usuário da API (não-root)" "docker exec tobem-api whoami; docker exec tobem-api id"
secao "Usuário do banco" "docker exec tobem-oracle whoami"
secao "WORKDIR e arquivos da API" "docker exec tobem-api pwd; docker exec tobem-api ls -la /app"
secao "Variáveis de ambiente da API (senhas ocultas)" "docker exec tobem-api env | grep -E '^(SERVER_PORT|DB_URL|DB_USERNAME|DB_PASSWORD|JWT_SECRET|ADMIN_EMAIL|JAVA_TOOL_OPTIONS)=' | sed -E 's/^((DB_PASSWORD|JWT_SECRET)=).*/\1******/'"
secao "Healthcheck dos containers" "docker inspect --format '{{.Name}}: {{.State.Health.Status}}' tobem-api tobem-oracle"
secao "API respondendo" "curl -s http://localhost:\${API_PORT:-8080}/actuator/health; echo"
secao "Estrutura de diretórios do projeto" "find . -maxdepth 3 \\( -path ./.git -o -path ./target -o -path ./src -o -path ./.idea -o -path ./.mvn \\) -prune -o -print | sort; echo; echo \"(código-fonte em ./src/main/java/br/com/fiap_tdsps/to_bem_api)\""
secao "Logs da API (últimas 30 linhas)" "docker compose logs --tail 30 api"
secao "Logs do Oracle (últimas 15 linhas)" "docker compose logs --tail 15 oracle"
