#!/usr/bin/env bash
# Apaga TUDO o que o criar-vm.sh criou (VM, disco, IP, rede, NSG) para não
# gastar crédito. Só rode depois da apresentação e da gravação do vídeo.
#
#   bash scripts/azure/excluir-recursos.sh
set -euo pipefail

RESOURCE_GROUP="${RESOURCE_GROUP:-rg-tobem-gs}"

echo "Isto vai apagar o grupo de recursos '$RESOURCE_GROUP' e tudo dentro dele."
read -r -p "Digite o nome do grupo para confirmar: " CONFIRMACAO
if [ "$CONFIRMACAO" != "$RESOURCE_GROUP" ]; then
  echo "Nome diferente. Nada foi apagado."
  exit 1
fi

az group delete --name "$RESOURCE_GROUP" --yes --no-wait
echo "Exclusão iniciada (leva alguns minutos). Acompanhe com: az group list -o table"
