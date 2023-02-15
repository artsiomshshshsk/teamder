[![Java CI with Maven](https://github.com/artsiomshshshsk/find-project-idea/actions/workflows/maven.yml/badge.svg)](https://github.com/artsiomshshshsk/find-project-idea/actions/workflows/maven.yml)
# Teamder - share project idea and collaborate with others!
Many individuals who are new to software development often choose to create their own projects to showcase their skills. These projects typically involve developing applications, websites, or systems that require a diverse set of skills in planning, analysis, design, and implementation. However, the individual behind the idea may not possess all the necessary skills, especially those outside of their specialization. As a result, they may need to acquire additional knowledge to complete the project rather than seeking collaboration with someone who already possesses the required skills. 
## But Teamder will help!


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

Create resources with the Terraform

```bash
  terraform init
  terraform plan
  terraform apply --auto-approve
```

build with Maven

```bash
  ./mvnw clean install
```
