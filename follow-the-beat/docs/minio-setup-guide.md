# MinIO Setup Guide

This guide provides step-by-step instructions for setting up a MinIO server using Docker and configuring it for use with the application.

## 1. Docker Setup

### Pull and Run MinIO Container
```bash
# Create a directory for MinIO data persistence
mkdir -p ~/minio-data

# Run MinIO container
docker run -d \
  --name minio \
  -p 9000:9000 \
  -p 9001:9001 \
  -v ~/minio-data:/data \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  quay.io/minio/minio server /data --console-address ":9001"
```

### Verify MinIO is Running
```bash
# Check container status
docker ps

# View container logs
docker logs minio
```

## 2. Access MinIO

### Web Console
- URL: http://localhost:9001
- Username: minioadmin
- Password: minioadmin

### Create Bucket
1. Log in to the web console
2. Click "Create Bucket"
3. Name: `follow-the-beat` (or your preferred bucket name)
4. Click "Create Bucket"

## 3. Application Configuration

### Add to application.properties
```properties
# MinIO Configuration
minio.endpoint=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket-name=follow-the-beat
minio.presigned-url-expiration=60
```

### Required Dependencies
Add to pom.xml:
```xml
<dependency>
    <groupId>io.minio</groupId>
    <artifactId>minio</artifactId>
    <version>8.5.7</version>
</dependency>
```

## 4. Common Operations

### Start/Stop Container
```bash
# Start container
docker start minio

# Stop container
docker stop minio

# Restart container
docker restart minio
```

### Backup Data
```bash
# Create backup
tar -czf minio-backup.tar.gz ~/minio-data

# Restore backup
tar -xzf minio-backup.tar.gz -C ~/
```

### Troubleshooting

#### Container Won't Start
```bash
# Check logs
docker logs minio

# Remove and recreate container
docker rm minio
# Then run the docker run command again
```

#### Permission Issues
```bash
# Fix permissions on data directory
sudo chown -R 1000:1000 ~/minio-data
```

#### Connection Issues
- Verify MinIO is running: `docker ps`
- Check port availability: `netstat -tuln | grep 9000`
- Verify firewall settings
- Check if the endpoint URL is correct in application.properties

## 5. Security Considerations

### Change Default Credentials
```bash
# Stop the container
docker stop minio

# Remove the container
docker rm minio

# Run with new credentials
docker run -d \
  --name minio \
  -p 9000:9000 \
  -p 9001:9001 \
  -v ~/minio-data:/data \
  -e "MINIO_ROOT_USER=your-new-username" \
  -e "MINIO_ROOT_PASSWORD=your-new-password" \
  quay.io/minio/minio server /data --console-address ":9001"
```

### Enable SSL/TLS
For production use, it's recommended to enable SSL/TLS. See [MinIO TLS Guide](https://docs.min.io/docs/how-to-secure-access-to-minio-server-with-tls.html).

## 6. Development Tips

### Using Different Environments
Create separate application-{env}.properties files:
```properties
# application-dev.properties
minio.endpoint=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin

# application-prod.properties
minio.endpoint=https://your-production-minio.com
minio.access-key=your-prod-access-key
minio.secret-key=your-prod-secret-key
```

### Testing MinIO Connection
```bash
# Using curl
curl http://localhost:9000/minio/health/live

# Using MinIO client (mc)
mc alias set myminio http://localhost:9000 minioadmin minioadmin
mc ls myminio
```

## 7. Cleanup

### Remove Container and Data
```bash
# Stop container
docker stop minio

# Remove container
docker rm minio

# Remove data (if needed)
rm -rf ~/minio-data
```

## 8. References

- [MinIO Documentation](https://docs.min.io/)
- [MinIO Docker Hub](https://hub.docker.com/r/minio/minio)
- [MinIO GitHub](https://github.com/minio/minio) 