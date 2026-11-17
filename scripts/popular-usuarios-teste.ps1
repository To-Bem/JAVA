param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$SenhaTeste = "Teste12345!"
)
$ErrorActionPreference = "Stop"
if (-not $env:ADMIN_EMAIL -or -not $env:ADMIN_PASSWORD) {
    throw "Defina ADMIN_EMAIL e ADMIN_PASSWORD nesta sessao."
}
$loginBody = @{ email = $env:ADMIN_EMAIL; password = $env:ADMIN_PASSWORD } | ConvertTo-Json
$login = Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/auth/login" -ContentType "application/json" -Body $loginBody
$headers = @{ Authorization = "Bearer $($login.token)" }
$cadastros = @(
    @{ caminho = "/api/auth/register/privilegiado"; dados = @{ name = "Gerente Teste"; email = "gerente.teste@example.com"; password = $SenhaTeste; papel = "CUIDADOR"; role = "MANAGER" } },
    @{ caminho = "/api/auth/register"; dados = @{ name = "Familiar Teste"; email = "familiar.teste@example.com"; password = $SenhaTeste } }
)
foreach ($cadastro in $cadastros) {
    try {
        $body = $cadastro.dados | ConvertTo-Json
        $null = Invoke-RestMethod -Method Post -Uri ($BaseUrl + $cadastro.caminho) -Headers $headers -ContentType "application/json" -Body $body
        Write-Host "Criado: $($cadastro.dados.email)"
    } catch {
        if ($_.Exception.Response -and [int]$_.Exception.Response.StatusCode -eq 409) {
            Write-Host "Ja existe: $($cadastro.dados.email); mantido sem alteracoes."
        } else { throw }
    }
}
