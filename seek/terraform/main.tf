###############################################################################
#  Terraform & Providers
###############################################################################
terraform {
  required_version = ">= 1.4"
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 4.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }
}

###############################################################################
#  Variables
###############################################################################
variable "project_id" {
  description = "ID del proyecto de GCP (p.ej. seek-467323)"
  type        = string
}

variable "region" {
  description = "Región principal"
  type        = string
  default     = "us-central1"
}

variable "jwt_secret" {
  description = "Clave HMAC de 32+ caracteres para firmar JWT"
  type        = string
  sensitive   = true
}

###############################################################################
#  Provider
###############################################################################
provider "google" {
  project = var.project_id
  region  = var.region
}

###############################################################################
#  Cloud SQL (MySQL 8)
###############################################################################
resource "random_password" "db" {
  length  = 16
  special = true
}

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

resource "google_sql_user" "api_user" {
  name     = "api_user"
  instance = google_sql_database_instance.mysql.name
  password = random_password.db.result
}

resource "google_sql_database" "customerdb" {
  name     = "customerdb"
  instance = google_sql_database_instance.mysql.name
}

###############################################################################
#  Secret Manager
###############################################################################
# JWT secret
resource "google_secret_manager_secret" "jwt_secret" {
  secret_id = "jwt-secret"

  replication {
    automatic = true
  }
}

resource "google_secret_manager_secret_version" "jwt_secret_ver" {
  secret      = google_secret_manager_secret.jwt_secret.id
  secret_data = base64encode(var.jwt_secret)
}

# DB password secret
resource "google_secret_manager_secret" "db_pwd" {
  secret_id = "customer-db-pwd"

  replication {
    automatic = true
  }
}

resource "google_secret_manager_secret_version" "db_pwd_ver" {
  secret      = google_secret_manager_secret.db_pwd.id
  secret_data = base64encode(random_password.db.result)
}

###############################################################################
#  Pub/Sub
###############################################################################
resource "google_pubsub_topic" "customer_created" {
  name = "customer.created"
}

###############################################################################
#  IAM para invocar Cloud Run (invocación autenticada opcional)
###############################################################################
resource "google_cloud_run_service_iam_member" "invoker" {
  location = var.region
  project  = var.project_id
  service  = "customer-api"
  role     = "roles/run.invoker"
  member   = "allAuthenticatedUsers"
}

###############################################################################
#  Outputs
###############################################################################
output "cloud_sql_connection_name" {
  description = "connection_name para usar en Cloud Run"
  value       = google_sql_database_instance.mysql.connection_name
}

output "db_password" {
  description = "Contraseña generada para api_user (sensible)"
  value       = random_password.db.result
  sensitive   = true
}
