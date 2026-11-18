# Roteiro do vídeo — DevOps (Tô Bem)

Requisitos do vídeo: YouTube **não listado**, **mínimo 720p** (grave em 1080p),
**com explicação em voz**. Terminal com fonte grande (zoom 130%+) para ficar legível.
Duração sugerida: 8 a 12 minutos.

| # | Tempo | O que mostrar | Comando / tela | Fala sugerida |
|---|---|---|---|---|
| 1 | 0:00 | Abertura | slide ou README | Nome da solução, integrantes, problema (idosos que moram sozinhos, sem câmeras) |
| 2 | 0:40 | **Arquitetura macro** | `docs/arquitetura.png` | Azure → VM → Docker → `tobem-api` + `tobem-oracle` → volume. Quem acessa (app, lâmpada, Swagger). Por que a 1521 fica fechada |
| 3 | 1:40 | **Azure CLI** | `scripts/azure/criar-vm.sh` aberto + execução (ou o resultado já criado e `az vm list -o table`, `az vm show -d ... --query publicIps`) | Cada comando: group create, vm create, open-port, run-command |
| 4 | 3:00 | VM e Docker | `ssh azureuser@IP` · `docker --version` · `docker compose version` | Docker instalado pelo script, rodando na VM |
| 5 | 3:30 | **Dockerfile** | `cat api/Dockerfile` | Multi-stage, WORKDIR `/app`, ENV, EXPOSE 8080, usuário `tobem` não-root, HEALTHCHECK |
| 6 | 4:30 | **docker-compose.yml** | `cat docker-compose.yml` | Serviços, rede `tobem-net`, volume nomeado, portas, variáveis do `.env`, `depends_on: service_healthy` |
| 7 | 5:30 | Subida em background | `docker compose up -d --build` · `docker compose ps` | Os dois containers `Up (healthy)` |
| 8 | 6:00 | **Logs** | `docker compose logs --tail 30 api` · `... oracle` | API conectada ao Oracle, Hibernate criando tabelas |
| 9 | 6:30 | **Acesso ao container e usuário** | `docker exec -it tobem-api sh` → `whoami`, `pwd`, `ls -la` → `exit` · `docker exec tobem-oracle whoami` | Usuário `tobem` (não-root), WORKDIR `/app` |
| 10 | 7:00 | **Estrutura de diretórios** | `bash scripts/evidencias/coletar-evidencias.sh` (seção de diretórios) ou `tree -L 3` | Onde está cada parte |
| 11 | 7:30 | **CRUD** | `bash scripts/evidencias/crud.sh` e/ou Swagger em `http://IP:8080/swagger-ui/index.html` | Create usuário → idoso+contato → dispositivo; Read; Update; Delete |
| 12 | 9:00 | **SELECT no banco** | `bash scripts/evidencias/select.sh` | Registros do CRUD, JOIN usuário ↔ contato ↔ idoso ↔ dispositivo |
| 13 | 9:40 | **Persistência** | `docker compose down` · `docker volume ls` · `docker compose up -d` · `select.sh` de novo | Os dados continuam (volume `tobem-oracle-data`) |
| 14 | 10:40 | Encerramento | (opcional) app Dart com `API_URL=http://IP:8080` | A mesma API em nuvem atende o app do familiar |

## Checklist antes de gravar

- [ ] VM ligada e IP anotado
- [ ] `docker compose ps` com os dois `healthy`
- [ ] Rodar o `crud.sh` uma vez antes (testar), mas mostrar uma execução nova no vídeo
- [ ] Fonte do terminal grande, sem senhas reais na tela (o `coletar-evidencias.sh` já oculta)
- [ ] Gravação em 1080p, áudio testado
- [ ] Depois de publicar: vídeo **não listado** abre em aba anônima
