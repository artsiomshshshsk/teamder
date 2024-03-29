[![Java CI with Maven](https://github.com/artsiomshshshsk/find-project-idea/actions/workflows/maven.yml/badge.svg)](https://github.com/artsiomshshshsk/find-project-idea/actions/workflows/maven.yml)
# [Teamder](https://teamder.vercel.app) - share project idea and collaborate with others!
Many individuals who are new to software development often choose to create their own projects to showcase their skills. These projects typically involve 
developing applications, websites, or systems that require a diverse set of skills in planning, analysis, design, and implementation.

However, the individual behind the idea may not possess all the necessary skills, especially those outside of their specialization. As a result, they may need to acquire additional knowledge to complete the project rather than seeking collaboration with someone who already possesses the required skills.

But Teamder will help!

## Technology Stack 
Java 17, PostgreSQL, Spring(Boot, Data, Security), Hibernate, Lombok, JWT, Docker, Terraform, AWS, Heroku, Junit, Mockito.

## Api Specification

https://teamder-dev.herokuapp.com/swagger-ui/

## Setting up Secrets

This project requires the following secrets to be set up in a secrets.yml file:
```yaml
jwt:
  secret:
  expirationTime:

aws:
  s3:
    bucketName:
    accessKey:
    secretKey:
    region:
    endpoint:
```
## Run Locally

You'll need Docker and Terraform

Clone the project

```bash
  git clone https://github.com/artsiomshshshsk/teamder
```

Go to the project directory

```bash
  cd teamder
```

run docker-compose.yml

```bash
  docker-compose up
```

Set up S3 bucket locally with Terraform and LocalStack

```bash
  terraform init
  terraform plan
  terraform apply --auto-approve
```
