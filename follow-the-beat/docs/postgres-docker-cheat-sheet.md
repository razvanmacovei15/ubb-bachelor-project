
# 🐘 PostgreSQL + Docker Cheat Sheet

## ✅ PostgreSQL Container Setup (example)
- Image: `postgres:15`
- Default port inside container: `5432`

## 📡 Exposed Ports
Use unique host ports to run multiple PostgreSQL containers:
- dev: `localhost:55432`
- test: `localhost:55433`
- prod: `localhost:55434`

---

## 🧠 Default Credentials (set in docker-compose.yml)
```yaml
POSTGRES_USER: devuser
POSTGRES_PASSWORD: devpass
POSTGRES_DB: devdb
```

---

## 🖥️ Connect Using Terminal (`psql`)
```bash
psql -h localhost -p 55432 -U devuser -d devdb
```

- `-h`: host (localhost)
- `-p`: port (55432 for dev)
- `-U`: username
- `-d`: database name

---

## 🧑‍💻 Inside the Container
```bash
docker exec -it postgres-dev bash
psql -U devuser -d devdb
```

---

## 🧰 Common psql Commands
```sql
\l                -- List databases
\c dbname         -- Connect to database
\dt               -- List tables
\d table_name     -- Show table schema
\q                -- Quit
```

---

## 🧠 Connect Using pgAdmin

1. Open pgAdmin in your browser
2. Create a **new server**
3. In "Connection" tab, set:
   - **Host**: `localhost`
   - **Port**: `55432` (or whatever you're using)
   - **Username**: `devuser`
   - **Password**: `devpass`

---

## 💾 Data Persistence
Make sure your `docker-compose.yml` maps a volume to:
```
/var/lib/postgresql/data
```

Example:
```yaml
volumes:
  - ./data/dev:/var/lib/postgresql/data
```
