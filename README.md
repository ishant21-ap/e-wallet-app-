# 💳 E-Wallet App

A **Microservices-based E-Wallet application** built using **Java Spring Boot**, integrating **Kafka** for messaging, **MySQL** as the database, and **Hibernate** for ORM.

---

## 🚀 Project Overview

The **E-Wallet App** is designed to simulate a digital wallet system where users can perform wallet-related operations like crediting, debiting, and transferring funds. The architecture follows the **Microservice pattern** with distributed services communicating via **Apache Kafka**.

---

## 🧩 Microservices Structure

| Service              | Description                                             |
|----------------------|---------------------------------------------------------|
| **User-Service**     | Handles user registration and authentication            |
| **Wallet-Service**   | Manages wallet balance, credit, and debit operations    |
| **Transaction-Service** | Logs and monitors all wallet transactions          |
| **Notification-Service** | Sends alerts and notifications via Kafka consumers |
| **Utils**            | Common utilities shared across services                 |



