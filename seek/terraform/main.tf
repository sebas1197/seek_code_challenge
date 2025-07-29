###############################################################################
#  Terraform & Providers
###############################################################################
terraform {
  required_version = ">= 1.4"
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 5.17"
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
variable "project_id" {}
variable "region"     { default = "us-central1" }
variable "jwt_secret" { sensitive = true }

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
    ip_configuration { ipv4_enabled = true }
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
#  Secret Manager (provider 5.x ‑ sintaxis BLOQUE)
###############################################################################
resource "google_secret_manager_secret" "jwt_secret" {
  secret_id = "jwt-secret"
  replication { automatic {} }
}

resource "google_secret_manager_secret_version" "jwt_secret_ver" {
  secret      = google_secret_manager_secret.jwt_secret.id
  secret_data = base64encode(var.jwt_secret)
}

resource "google_secret_manager_secret" "db_pwd" {
  secret_id = "customer-db-pwd"
  replication { automatic {} }
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
#  IAM – Invocar Cloud Run
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
  value = google_sql_database_instance.mysql.connection_name
}

output "db_password" {
  value       = random_password.db.result
  sensitive   = true
}
