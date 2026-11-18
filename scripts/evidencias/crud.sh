#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Demonstra o CRUD completo da API (que grava no Oracle do container),
# usando tabelas relacionadas:
#   usuario 1--N contato N--1 idoso 1--N dispositivo
#
# Dentro da VM, na raiz do repositório:
#   bash scripts/evidencias/crud.sh
# De fora (seu PC), apontando para a VM:
#   API_URL=http://<IP da VM>:8080 bash scripts/evidencias/crud.sh
#
# Cria um idoso que FICA no banco (para mostrar a persistência depois) e
# outro que é apagado no final (DELETE).
# ---------------------------------------------------------------------------
set -euo pipefail

cd "$(dirname "$0")/../.."
[ -f .env ] && set -a && . ./.env && set +a

API_URL="${API_URL:-http://localhost:${API_PORT:-8080}}"
ADMIN_EMAIL="${ADMIN_EMAIL:-admin@tobem.com}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-Admin@12345}"
SUFIXO="$(date +%H%M%S)"

titulo() { printf '\n\033[1;36m===== %s =====\033[0m\n' "$1"; }

# chamar MÉTODO CAMINHO [CORPO_JSON] -> mostra a requisição, o status e o JSON
chamar() {
  local metodo="$1" caminho="$2" corpo="${3:-}"
  local -a args=(-s -w '\n%{http_code}' -X "$metodo" "$API_URL$caminho"
    -H 'Content-Type: application/json')
  [ -n "${TOKEN:-}" ] && args+=(-H "Authorization: Bearer $TOKEN")
  [ -n "$corpo" ] && args+=(-d "$corpo")

  local resposta status
  resposta="$(curl "${args[@]}")"
  status="$(tail -n1 <<< "$resposta")"
  RESPOSTA="$(sed '$d' <<< "$resposta")"

  printf '\033[1m%s %s\033[0m -> HTTP %s\n' "$metodo" "$caminho" "$status" >&2
  if [ -n "$RESPOSTA" ]; then jq . <<< "$RESPOSTA" >&2 2>/dev/null || echo "$RESPOSTA" >&2; fi
  case "$status" in 2*) ;; *) echo "Falhou (HTTP $status)." >&2; exit 1 ;; esac
}

titulo "0. API no ar?"
chamar GET /actuator/health

titulo "1. Login do administrador (JWT)"
chamar POST /api/auth/login "{\"email\":\"$ADMIN_EMAIL\",\"password\":\"$ADMIN_PASSWORD\"}"
TOKEN="$(jq -r .token <<< "$RESPOSTA")"

titulo "2. CREATE usuario (familiar)"
chamar POST /api/auth/register \
  "{\"name\":\"Ana Souza $SUFIXO\",\"email\":\"ana.$SUFIXO@tobem.com\",\"password\":\"Senha12345\"}"
USUARIO_ID="$(jq -r .id <<< "$RESPOSTA")"

titulo "3. CREATE idoso + contato (ligado ao usuario $USUARIO_ID)"
chamar POST "/api/usuarios/$USUARIO_ID/idoso?parentesco=Filha&ordemEscalonamento=1" \
  '{"nome":"Dona Maria","dataNascimento":"1948-03-12","telefone":"11988887777","endereco":"Rua das Flores, 120","modo":"NORMAL"}'
IDOSO_ID="$(jq -r .id <<< "$RESPOSTA")"

titulo "4. CREATE dispositivo (lampada do idoso $IDOSO_ID)"
chamar POST "/api/idosos/$IDOSO_ID/dispositivo" \
  '{"tipo":"LAMPADA","comodo":"Sala","status":"ONLINE"}'

titulo "5. READ idoso com contatos, dispositivos, alertas e rotinas"
chamar GET "/api/idosos/$IDOSO_ID/withAll"

titulo "6. READ lista paginada de idosos"
chamar GET "/api/idosos?page=0&size=10&sort=id&direction=ASC"

titulo "7. UPDATE idoso $IDOSO_ID (telefone e modo fora de casa)"
chamar PUT "/api/idosos/$IDOSO_ID" \
  '{"nome":"Dona Maria","dataNascimento":"1948-03-12","telefone":"11977776666","endereco":"Rua das Flores, 120","modo":"AUSENTE"}'

titulo "8. CREATE + DELETE de outro idoso"
chamar POST "/api/usuarios/$USUARIO_ID/idoso?parentesco=Neto&ordemEscalonamento=2" \
  '{"nome":"Seu Joao (sera apagado)","dataNascimento":"1944-07-05","telefone":"11966665555","endereco":"Avenida Brasil, 45","modo":"NORMAL"}'
APAGAR_ID="$(jq -r .id <<< "$RESPOSTA")"
chamar DELETE "/api/idosos/$APAGAR_ID"

titulo "9. READ do idoso apagado (esperado: 404)"
STATUS="$(curl -s -o /dev/null -w '%{http_code}' -H "Authorization: Bearer $TOKEN" "$API_URL/api/idosos/$APAGAR_ID")"
echo "GET /api/idosos/$APAGAR_ID -> HTTP $STATUS"

titulo "Resumo"
echo "Usuario criado: $USUARIO_ID | Idoso mantido: $IDOSO_ID | Idoso apagado: $APAGAR_ID"
echo "Confira direto no banco: bash scripts/evidencias/select.sh"
