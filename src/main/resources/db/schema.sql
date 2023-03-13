CREATE TABLE _users
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username           VARCHAR(255),
    email              VARCHAR(255),
    password           VARCHAR(255),
    contact            VARCHAR(255),
    resumeurl          VARCHAR(255),
    profile_pictureurl VARCHAR(255),
    verification_token VARCHAR(255),
    is_verified        BOOLEAN,
    role               VARCHAR(255),
    CONSTRAINT pk__users PRIMARY KEY (id)
);

CREATE TABLE application
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    applicant_id     BIGINT,
    resumeurl        VARCHAR(255),
    message          VARCHAR(255),
    project_id       BIGINT,
    role_request_id  BIGINT,
    status           INTEGER,
    application_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_application PRIMARY KEY (id)
);

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_APPLICANT FOREIGN KEY (applicant_id) REFERENCES _users (id);

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_PROJECT FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_ROLEREQUEST FOREIGN KEY (role_request_id) REFERENCES _roles (id);


CREATE TABLE project
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name              VARCHAR(255),
    short_description VARCHAR(300),
    description       VARCHAR(1000),
    owner_id          BIGINT,
    published_at      TIMESTAMP WITHOUT TIME ZONE,
    chat_invite_link  VARCHAR(255),
    is_visible        BOOLEAN,
    CONSTRAINT pk_project PRIMARY KEY (id)
);

CREATE TABLE project_roles
(
    project_id BIGINT NOT NULL,
    roles_id   BIGINT NOT NULL
);

ALTER TABLE project_roles
    ADD CONSTRAINT uc_project_roles_roles UNIQUE (roles_id);

ALTER TABLE project
    ADD CONSTRAINT FK_PROJECT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES _users (id);

ALTER TABLE project_roles
    ADD CONSTRAINT fk_prorol_on_project FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE project_roles
    ADD CONSTRAINT fk_prorol_on_role FOREIGN KEY (roles_id) REFERENCES _roles (id);


CREATE TABLE _roles
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name             VARCHAR(255),
    assigned_user_id BIGINT,
    CONSTRAINT pk__roles PRIMARY KEY (id)
);

ALTER TABLE _roles
    ADD CONSTRAINT FK__ROLES_ON_ASSIGNEDUSER FOREIGN KEY (assigned_user_id) REFERENCES _users (id);