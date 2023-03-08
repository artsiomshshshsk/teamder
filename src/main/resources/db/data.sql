insert into _USERS (username, email, password, role) values ('fvondrak0', 'kpaumier0@time.com', '$2a$10$z4fDDSacOqh1LkT6rqRo/.DwYXg/Zo90F5kbgw1yKLgS3w0LnMJoG', 'ADMIN');  -- 4Dbd1jQ
insert into _USERS (username, email, password, role) values ('cmackeig1', 'aletham1@cnet.com', '$2a$10$U636geqzDRBM2AId1eAv5eYeSJzC6PTHliCaAe1H.ZfFgDM70R6.S', 'ADMIN');
insert into _USERS (username, email, password, role) values ('ffills2', 'asmorfit2@addthis.com', '$2a$10$hvIqiKuehMn4HhenhYwSvOgf76kHqTq0wpvT87anrV9hj2WayXJlm', 'USER');
insert into _USERS (username, email, password, role) values ('artsiomshshshsk','artsiomshablinskisy@gmail.com', '$2a$10$VuU2I9qcYr3j1mHSju85mesAs9Jt4rUEtGgauMjV8gs1aWpr25dKi','ADMIN');
insert into _USERS (username, email, password, role) values ('zolotaya','zlata.kenda@gmail.com', '$2a$10$mIBvzp1WPdOsVEfApHzd5exagaM5IsIPfx3VTcW8Os6UQHruShU/C','USER');


insert into PROJECT (chat_invite_link, description, is_visible, name, published_at, short_description, owner_id) values ('https:///in/faucibus/orci/luctus/et/ultrices/posuere.jsp', 'tortor risus dapibus augue vel accumsan tellus nisi eu orci mauris lacinia sapien quis libero nullam sit amet turpis elementum ligula vehicula consequat morbi a ipsum integer a nibh in quis justo maecenas rhoncus aliquam lacus morbi quis tortor', true, 'Para Grass Pollen', '2022-06-08', 'magnis dis parturient montes nascetur', 5);

insert into _ROLES (name, assigned_user_id) values ('Frontend Developer', null);
insert into _ROLES (name, assigned_user_id) values ('Backend Developer', null);
insert into _ROLES (name, assigned_user_id) values ('Project Manager', 5);

insert into PROJECT_ROLES( project_id, roles_id) values (1,1);
insert into PROJECT_ROLES( project_id, roles_id) values (1,2);
insert into PROJECT_ROLES( project_id, roles_id) values (1,3);