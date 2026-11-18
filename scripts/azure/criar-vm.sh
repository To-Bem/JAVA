#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Provisiona a VM do Tô Bem no Microsoft Azure usando somente o Azure CLI.
#
# Onde rodar: Azure Cloud Shell (Bash), Linux, macOS ou Git Bash no Windows,
# depois de "az login". Na raiz do repositório:
#
#   bash scripts/azure/criar-vm.sh
#
# Dá para trocar qualquer valor por variável de ambiente, ex.:
#   LOCATION=eastus2 VM_SIZE=Standard_D2as_v7 bash scripts/azure/criar-vm.sh
#
# Azure for Students só aceita algumas regiões. Para ver as permitidas:
#   az policy assignment list --query "[].parameters.listOfAllowedLocations.value" -o json
# ---------------------------------------------------------------------------
set -euo pipefail

RESOURCE_GROUP="${RESOURCE_GROUP:-rg-tobem-gs}"
LOCATION="${LOCATION:-brazilsouth}"
VM_NAME="${VM_NAME:-vm-tobem}"
VM_SIZE="${VM_SIZE:-Standard_D2s_v4}"     # 2 vCPU / 8 GB: Oracle Free + API
VM_IMAGE="${VM_IMAGE:-Ubuntu2204}"        # Ubuntu Server 22.04 LTS
ADMIN_USER="${ADMIN_USER:-azureuser}"
API_PORT="${API_PORT:-8080}"

echo "==> 1/5 Grupo de recursos: $RESOURCE_GROUP ($LOCATION)"
az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION" \
  --tags projeto=tobem disciplina=devops gs=2026-2 \
  --output table

echo "==> 2/5 Máquina virtual: $VM_NAME ($VM_SIZE, $VM_IMAGE)"
az vm create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --image "$VM_IMAGE" \
  --size "$VM_SIZE" \
  --admin-username "$ADMIN_USER" \
  --generate-ssh-keys \
  --public-ip-sku Standard \
  --os-disk-size-gb 64 \
  --tags projeto=tobem \
  --output table

echo "==> 3/5 Liberando a porta $API_PORT (API) no NSG"
# A porta 22 (SSH) já é liberada pelo "az vm create".
# A porta 1521 (Oracle) NÃO é aberta para a internet: o banco só é acessado
# pela API (rede interna do Docker) ou de dentro da VM.
az vm open-port \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --port "$API_PORT" \
  --priority 1010 \
  --output none

echo "==> 4/5 Instalando Docker, Docker Compose, Git e jq na VM (run-command)"
az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts @scripts/vm/instalar-docker.sh \
  --parameters "$ADMIN_USER" \
  --query "value[0].message" \
  --output tsv

echo "==> 5/5 Dados de acesso"
IP_PUBLICO=$(az vm show \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --show-details \
  --query publicIps \
  --output tsv)

echo
echo "VM pronta!"
echo "  IP público: $IP_PUBLICO"
echo "  SSH:        ssh $ADMIN_USER@$IP_PUBLICO"
echo "  API (depois do compose): http://$IP_PUBLICO:$API_PORT/swagger-ui/index.html"
