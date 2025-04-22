
# 🐳 Docker CLI Cheat Sheet

## 🧠 Docker Concepts
- **Image**: A snapshot/blueprint of an app or environment (e.g., postgres:15)
- **Container**: A running instance of an image
- **Volume**: Persistent storage for container data

---

## 🔍 Docker System Info
```bash
docker info          # Detailed info about Docker system
docker version       # Version info
```

---

## 📦 Images
```bash
docker images        # List downloaded images
docker pull <image>  # Download image (e.g., postgres:15)
```

---

## 🚀 Containers
```bash
docker ps            # Running containers
docker ps -a         # All containers (including stopped)
docker stop <id>     # Stop a running container
docker start <id>    # Start a stopped container
docker restart <id>  # Restart a container
docker rm <id>       # Delete a stopped container
```

---

## ⚙️ docker-compose
```bash
docker-compose up -d        # Start services
docker-compose down         # Stop and remove containers
docker-compose restart      # Restart services
docker-compose logs -f      # Follow logs
```

---

## 📡 Networking
```bash
lsof -i :5432               # Check if port is in use
```

---

## 📺 Monitoring
```bash
docker stats                # Real-time container usage
```

---

## 🐚 Interact with Containers
```bash
docker exec -it <container> bash  # Open shell inside container
```

---

## 📂 Volumes
```bash
docker volume ls           # List volumes
docker volume prune        # Remove unused volumes
```

---

## 🧽 System Cleanup
```bash
docker system prune        # Remove unused containers, networks, images
docker container prune     # Remove stopped containers
docker image prune         # Remove dangling images
```
