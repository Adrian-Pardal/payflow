### Subir o banco:
    docker compose up -d
    docker compose ps
### Conectar pelo IntelliJ
1. Abra a aba Database (na lateral direita).
2. Clique em + → Data Source → PostgreSQL.
3. Preencha: host localhost, porta 5432, e o banco, usuário e senha do seu .env.
4. Clique em Test Connection. Se pedir para baixar o driver, aceite.

### Recrie o banco do zero:

    docker compose down -v
    docker compose up -d