provider "google" {
  project = var.project_id
  region  = var.region
}

# Cloud SQL (MySQL)
resource "google_sql_database_instance" "mysql" {
  name             = "customer-mysql"
  database_version = "MYSQL_8_0"
  region           = var.region

  settings {
    tier = "db-f1-micro"
    ip_configuration {
      ipv4_enabled    = true
      authorized_networks = []
    }
  }
}

resource "google_sql_user" "api_user" {
  name     = "api_user"
  instance = google_sql_database_instance.mysql.name
  password = random_password.db.result
}

resource "google_sql_database" "customerdb" {
  name     = "customerdb"
  instance = google_sql_database_instance.mysql.name
}

# Random password for DB user
resource "random_password" "db" {
  length  = 16
  special = true
}

# Secret Manager entries
resource "google_secret_manager_secret" "jwt_secret" {
  secret_id = "jwt-secret"
  replication { automatic = true }
}
resource "google_secret_manager_secret_version" "jwt_secret_ver" {
  secret = google_secret_manager_secret.jwt_secret.id
  secret_data = base64encode(var.jwt_secret)
}

resource "google_secret_manager_secret" "db_pwd" {
  secret_id = "customer-db-pwd"
  replication { automatic = true }
}
resource "google_secret_manager_secret_version" "db_pwd_ver" {
  secret = google_secret_manager_secret.db_pwd.id
  secret_data = base64encode(random_password.db.result)
}

# Pub/Sub topic
resource "google_pubsub_topic" "customer_created" {
  name = "customer.created"
}

# Cloud Run service IAM (allow authenticated invoke)
resource "google_cloud_run_service_iam_member" "invoker" {
  location = var.region
  project  = var.project_id
  service  = "customer-api"
  role     = "roles/run.invoker"
  member   = "allAuthenticatedUsers"
}

# ───────────────────────────────────────────────────────────────
#  Cloud SQL Instance
# ───────────────────────────────────────────────────────────────
resource "google_sql_database_instance" "mysql" {
  name             = "customer-mysql"
  database_version = "MYSQL_8_0"
  region           = var.region

  settings {
    tier = "db-f1-micro"

    ip_configuration {
      ipv4_enabled = true
    }
  }
}


# ───────────────────────────────────────────────────────────────
#  Secret Manager: JWT secret
# ───────────────────────────────────────────────────────────────
resource "google_secret_manager_secret" "jwt_secret" {
  secret_id = "jwt-secret"

  replication {
    automatic {}   
  }
}

resource "google_secret_manager_secret_version" "jwt_secret_ver" {
  secret      = google_secret_manager_secret.jwt_secret.id
  secret_data = base64encode(var.jwt_secret)
}


# ───────────────────────────────────────────────────────────────
#  Secret Manager: DB password
# ───────────────────────────────────────────────────────────────
resource "google_secret_manager_secret" "db_pwd" {
  secret_id = "customer-db-pwd"

  replication {
    automatic {}
  }
}

resource "google_secret_manager_secret_version" "db_pwd_ver" {
  secret      = google_secret_manager_secret.db_pwd.id
  secret_data = base64encode(random_password.db.result)
}
