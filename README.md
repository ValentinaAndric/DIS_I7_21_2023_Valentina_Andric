# Restaurant Reservation System – Microservices Architecture

Ovo je distribuirani mikroservisni sistem za upravljanje restoranima, stolovima i rezervacijama. Sistem koristi savremene tehnologije kao što su Spring Boot, Eureka Service Discovery, RabbitMQ, PostgreSQL i Docker Compose.  
Arhitektura omogućava skalabilnost, otpornost na greške i nezavisno razvijanje svakog servisa.

---

## 📌 Arhitektura sistema

Sistem se sastoji iz sledećih servisa:

### **1. Eureka Server (Service Discovery)**
- Port: **8761**
- Glavna uloga: Registracija i otkrivanje svih mikroservisa.
- Omogućava automatsko povezivanje servisa bez hard-kodiranih URL-ova.
- Primer UI: *http://localhost:8761*

### **2. User Service**
- Port: **8083**
- Baza: **PostgreSQL – user_db**
- Odgovoran za:
  - Registraciju korisnika
  - Login / JWT autentifikaciju
  - Upravljanje korisničkim podacima
- Registruje se na Eureku.

### **3. Restaurant Service**
- Port: **8087**
- Baza: **PostgreSQL – restaurant_db**
- Uloge:
  - Upravljanje restoranima
  - Čuvanje podataka o lokaciji, tipu i kapacitetu
- Registruje se na Eureku.

### **4. Table Management Service**
- Port: **8084**
- Baza: **PostgreSQL – table_management_db**
- Uloge:
  - Čuvanje i upravljanje stolovima u restoranima
  - Slanje dostupnosti stolova drugim servisima
- U ovom servisu radi i **Resilience4j (Circuit Breaker + Retry)** za komunikaciju sa Restaurant Service-om.

### **5. Reservation Service**
- Port: **8085**
- Glavni servis sistema:
  - Kreira rezervacije
  - Poziva Table Management Service za dostupnost
  - Poziva Restaurant Service za detalje restorana
  - Komunicira sa RabbitMQ-om i šalje poruke Notification Service-u
  - U ovom servisu radi i **Resilience4j (Circuit Breaker + Retry)** za komunikaciju sa Table Management Service-om.
- Registruje se na Eureku.

### **6. Notification Service**
- Port: **bez HTTP porta**
- Uloge:
  - Primi rezervacije preko RabbitMQ
  - Šalje email/sms/notifikaciju (stub logika)
- Radi kao message consumer.

---

## 🐇 RabbitMQ (Message Broker)
- Portovi:
  - 5672 – komunikacija
- Koristi se za:
  - Asinhrono slanje poruka o kreiranoj rezervaciji
  - Rasterska obrada događaja u Notification servisu

RabbitMQ UI:  
**http://localhost:15672**  
User: `guest`, Pass: `guest`

---

## 🗄 PostgreSQL baze
Sistem koristi 3 odvojene baze za svaki domen:

| Servis                  | Baza                 | Port hosta |
|-------------------------|----------------------|------------|
| User Service            | user_db              | 5433       |
| Restaurant Service      | restaurant_db        | 5437       |
| Table Management        | table_management_db  | 5434       |

Svaki servis ima svoj kontejner i svoju bazu kako bi ostao izolovan.

---

## 🚢 Docker Compose arhitektura

Docker Compose pokreće ukupno **9 kontejnera**:

- 1 × Eureka server  
- 5 × Spring Boot servisa  
- 3 × PostgreSQL instance  
- 1 × RabbitMQ  

Svaki servis ima svoj Dockerfile.

### Komanda za pokretanje:
docker-compose build
docker compose up

