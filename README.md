Order & Inventory — Spring Boot + PostgreSQL + RabbitMQ

Basit ama gerçekçi bir Sipariş & Stok Yönetimi örneği.
Backend Spring Boot ile yazıldı; veri PostgreSQL’de tutuluyor, RabbitMQ üzerinden sipariş/stoğa dair olaylar (events) yayınlanıp tüketilebiliyor.

Amaç: CRUD akışlarını, servis katmanını, JPA ile ilişkileri, RabbitMQ ile asenkron mesajlaşmayı ve Docker Compose ile yerel geliştirme ortamını tek bir projede göstermek.


<---------------------------Özellikler----------------->

Product (Ürün): Ekle/Güncelle/Sil/Listele

Inventory (Stok): Ürün bazlı stok takibi, rezervasyon

Order (Sipariş): Sipariş oluşturma, iptal, durum güncelleme

Event-Driven: OrderCreated gibi event’ler RabbitMQ üzerinden yayınlanır

Hata Yönetimi: (Örnek) Global Exception Handler ile temiz API dönüşleri

Migrations (opsiyonel): Flyway/Liquibase ile tablo ilk yükleme 

Not: Endpoint isimleri ve kapsamı projeyi büyütürken değişebilir; aşağıdaki örnekler tipik bir tasarımdır.


<-------------------Uygulamayı Çalıştırma--------------------->
Docker-compome.yml dizinde olduğundan emin olduktansa sonra ;
docker compose up -d
Sonrasınada;
mvn clean package  -  mvn spring-boot:run

Konfigürasyon ayarlayını lütfen inceyeleyin.
