-- SELECTs direto no Oracle do container (esquema da aplicação).
-- Rodar com: bash scripts/evidencias/select.sh
SET LINESIZE 200
SET PAGESIZE 100
SET FEEDBACK ON
COLUMN table_name FORMAT A20
COLUMN familiar FORMAT A28
COLUMN email FORMAT A30
COLUMN parentesco FORMAT A12
COLUMN idoso FORMAT A26
COLUMN nome FORMAT A26
COLUMN telefone FORMAT A12
COLUMN data_nascimento FORMAT A15
COLUMN comodo FORMAT A12
COLUMN role FORMAT A8

PROMPT
PROMPT ===== Tabelas criadas pela aplicação =====
SELECT table_name FROM user_tables ORDER BY table_name;

PROMPT
PROMPT ===== usuario =====
SELECT id, nome, email, role FROM usuario ORDER BY id;

PROMPT
PROMPT ===== idoso =====
-- Enums sem @Enumerated são gravados pela ordem: modo 0 = NORMAL, 1 = AUSENTE
SELECT id, nome, data_nascimento, telefone, modo FROM idoso ORDER BY id;

PROMPT
PROMPT ===== Relacionamento usuario x contato x idoso (JOIN) =====
SELECT u.nome AS familiar, c.parentesco, c.ordem_escalonamento AS ordem, i.nome AS idoso
  FROM contato c
  JOIN usuario u ON u.id = c.usuario_id
  JOIN idoso   i ON i.id = c.idoso_id
 ORDER BY i.id, c.ordem_escalonamento;

PROMPT
PROMPT ===== Relacionamento idoso x dispositivo (JOIN) =====
-- tipo 0 = LAMPADA, 1 = BASE | status 0 = ONLINE, 1 = OFFLINE
SELECT d.id, i.nome AS idoso, d.tipo, d.comodo, d.status
  FROM dispositivo d
  JOIN idoso i ON i.id = d.idoso_id
 ORDER BY d.id;

PROMPT
PROMPT ===== Quantidade de registros por tabela =====
SELECT 'usuario' AS tabela, COUNT(*) AS total FROM usuario
UNION ALL SELECT 'idoso', COUNT(*) FROM idoso
UNION ALL SELECT 'contato', COUNT(*) FROM contato
UNION ALL SELECT 'dispositivo', COUNT(*) FROM dispositivo
UNION ALL SELECT 'evento', COUNT(*) FROM evento
UNION ALL SELECT 'alerta', COUNT(*) FROM alerta
UNION ALL SELECT 'rotina', COUNT(*) FROM rotina;

EXIT
