#!/usr/bin/env bash
# ---------------------------------------------------------------------------
# Instala e configura Docker Engine + Docker Compose (plugin) + Git + jq
# no Ubuntu da VM. Segue o repositório oficial da Docker.
#
# Roda sozinho pelo criar-vm.sh (az vm run-command, como root).
# Para rodar à mão, dentro da VM:
#   sudo bash scripts/vm/instalar-docker.sh azureuser
#
# $1 = usuário da VM que poderá usar o docker sem sudo (padrão: azureuser)
# ---------------------------------------------------------------------------
set -euo pipefail

USUARIO_VM="${1:-azureuser}"
export DEBIAN_FRONTEND=noninteractive

echo "==> Pacotes básicos"
apt-get update -y
apt-get install -y ca-certificates curl gnupg git jq

echo "==> Repositório oficial da Docker"
install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
chmod a+r /etc/apt/keyrings/docker.asc
. /etc/os-release
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu ${VERSION_CODENAME} stable" \
  > /etc/apt/sources.list.d/docker.list

echo "==> Docker Engine, Buildx e Compose"
apt-get update -y
apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "==> Serviço do Docker ligado e iniciando com a VM"
systemctl enable --now docker

echo "==> Usuário $USUARIO_VM no grupo docker (docker sem sudo)"
usermod -aG docker "$USUARIO_VM"

echo "==> Swap de 2 GB (folga de memória para o Oracle na VM de 4 GB)"
if [ ! -f /swapfile ]; then
  fallocate -l 2G /swapfile
  chmod 600 /swapfile
  mkswap /swapfile
  swapon /swapfile
  echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

echo "==> Versões instaladas"
docker --version
docker compose version
git --version
echo "Pronto. Saia e entre de novo no SSH para usar o docker sem sudo."
