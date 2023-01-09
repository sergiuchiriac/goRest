
# GO REST API Technical challenge

Configure first db variable in your hosts file if not configured:

```sh
sudo nano /etc/hosts
YOUR_IP    db
```
There are two ways to run app:

- By docker
- By java class

### Docker approach

Run docker command

```sh
docker-compose up --build  
```

### Java approach

Or run java GoRestApplication.

### API details
Application will start at port 8080
these are the fallowing REST calls
```sh
 POST /v1/job/start 
 GET /v1/job/status
 POST /v1/job/stop
 POST /v1/users 
 GET /v1/users
 PUT /v1/users/{userId}
 DELETE /v1/users/{userId}
 POST /v1/users/{userId}/files
 GET /v1/users/{userId}/files/{fileId}
 GET /v1/users/{userId}/files
 GET /v1/users/posts?body={bodyText}&title={titeText}&name={userName}
   ```

You can use postman collection available in the `postman_collections` folder to test this API.

