#!/usr/bin/env bash
# Executa scripts/evidencias/select.sql com o SQL*Plus de DENTRO do
# container do Oracle (não precisa abrir a porta 1521 para a internet).
#
#   bash scripts/evidencias/select.sh
#
# Para um console SQL interativo:
#   docker exec -it tobem-oracle sqlplus tobem/<DB_PASSWORD>@//localhost:1521/FREEPDB1
set -euo pipefail

cd "$(dirname "$0")/../.."
set -a && . ./.env && set +a

docker exec -i tobem-oracle \
  sqlplus -s "${DB_USER}/${DB_PASSWORD}@//localhost:1521/FREEPDB1" \
  < scripts/evidencias/select.sql
