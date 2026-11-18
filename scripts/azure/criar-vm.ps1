# ---------------------------------------------------------------------------
# Provisiona a VM do Tô Bem no Microsoft Azure usando somente o Azure CLI.
# Versão PowerShell (Windows). Mesmo resultado de criar-vm.sh.
#
# Depois de "az login", na raiz do repositório:
#   .\scripts\azure\criar-vm.ps1
#   .\scripts\azure\criar-vm.ps1 -Location brazilsouth -VmSize Standard_D2s_v4
#
# Azure for Students só aceita algumas regiões. Para ver as permitidas:
#   az policy assignment list --query "[].parameters.listOfAllowedLocations.value" -o json
# ---------------------------------------------------------------------------
param(
    [string]$ResourceGroup = "rg-tobem-gs",
    [string]$Location = "brazilsouth",
    [string]$VmName = "vm-tobem",
    [string]$VmSize = "Standard_D2s_v4",
    [string]$VmImage = "Ubuntu2204",
    [string]$AdminUser = "azureuser",
    [int]$ApiPort = 8080
)

$ErrorActionPreference = "Stop"

function Invoke-Az {
    # Para o script se o comando "az" falhar
    az @args
    if ($LASTEXITCODE -ne 0) { throw "Falhou: az $($args -join ' ')" }
}

Write-Host "==> 1/5 Grupo de recursos: $ResourceGroup ($Location)"
Invoke-Az group create `
    --name $ResourceGroup `
    --location $Location `
    --tags projeto=tobem disciplina=devops gs=2026-2 `
    --output table

Write-Host "==> 2/5 Máquina virtual: $VmName ($VmSize, $VmImage)"
Invoke-Az vm create `
    --resource-group $ResourceGroup `
    --name $VmName `
    --image $VmImage `
    --size $VmSize `
    --admin-username $AdminUser `
    --generate-ssh-keys `
    --public-ip-sku Standard `
    --os-disk-size-gb 64 `
    --tags projeto=tobem `
    --output table

Write-Host "==> 3/5 Liberando a porta $ApiPort (API) no NSG"
# A 22 (SSH) já é liberada pelo "az vm create". A 1521 (Oracle) fica fechada.
Invoke-Az vm open-port `
    --resource-group $ResourceGroup `
    --name $VmName `
    --port $ApiPort `
    --priority 1010 `
    --output none

Write-Host "==> 4/5 Instalando Docker, Docker Compose, Git e jq na VM (run-command)"
Invoke-Az vm run-command invoke `
    --resource-group $ResourceGroup `
    --name $VmName `
    --command-id RunShellScript `
    --scripts "@scripts/vm/instalar-docker.sh" `
    --parameters $AdminUser `
    --query "value[0].message" `
    --output tsv

Write-Host "==> 5/5 Dados de acesso"
$ip = az vm show `
    --resource-group $ResourceGroup `
    --name $VmName `
    --show-details `
    --query publicIps `
    --output tsv

Write-Host ""
Write-Host "VM pronta!"
Write-Host "  IP público: $ip"
Write-Host "  SSH:        ssh $AdminUser@$ip"
Write-Host "  API (depois do compose): http://${ip}:$ApiPort/swagger-ui/index.html"
