create table medical_task
(
    id          bigint not null,
    left_left   float4,
    left_mid    float4,
    name        varchar(255),
    right_mid   float4,
    right_right float4,
    primary key (id)
);
create table medical_task_left_decisions
(
    medical_task_id   bigint not null,
    left_decisions_id bigint not null,
    primary key (medical_task_id, left_decisions_id)
);
create table medical_task_right_decisions
(
    medical_task_id    bigint not null,
    right_decisions_id bigint not null,
    primary key (medical_task_id, right_decisions_id)
);
create table medical_task_result
(
    id              bigint not null,
    value           float4,
    alt_score       float4,
    medical_task_id bigint,
    primary key (id)
);
create table medical_topic
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);
create table medical_topic_medical_tasks
(
    medical_topic_id bigint not null,
    medical_tasks_id bigint not null,
    primary key (medical_topic_id, medical_tasks_id)
);
create table medical_topic_result
(
    id               bigint not null,
    medical_topic_id bigint,
    primary key (id)
);
alter table if exists medical_topic_result add column complete_date timestamp (6);
alter table if exists medical_topic_result add column last_update_date timestamp (6);
create table medical_topic_result_results
(
    medical_topic_result_id bigint not null,
    results_id              bigint not null,
    primary key (medical_topic_result_id, results_id)
);
create table usr_medical_results
(
    usr_id             bigint not null,
    medical_results_id bigint not null,
    primary key (usr_id, medical_results_id)
);
alter table if exists medical_topic add constraint UK_r8f6dxg3w4w4aeprn1bgsny6p unique (name);
alter table if exists medical_topic_medical_tasks add constraint UK_ha88ub4lhbxqmi4purp87sohy unique (medical_tasks_id);
alter table if exists medical_topic_result_results add constraint UK_r11gs7pie2i097mr54x08kjqt unique (results_id);
alter table if exists usr_medical_results add constraint UK_b3toxu5x14mimqolyfy47oscp unique (medical_results_id);
create sequence medical_task_result_seq start with 1 increment by 50;
create sequence medical_task_seq start with 1 increment by 50;
create sequence medical_topic_result_seq start with 1 increment by 50;
create sequence medical_topic_seq start with 1 increment by 50;
alter table if exists medical_task_left_decisions add constraint FKqg2t50ia4hpdrly8s5kejcqns foreign key (left_decisions_id) references quiz_decision;
alter table if exists medical_task_left_decisions add constraint FKhryqbus369xqm8sdleq4phps3 foreign key (medical_task_id) references medical_task;
alter table if exists medical_task_right_decisions add constraint FK8f82jjg4iuek2p1qgkca4vu2y foreign key (right_decisions_id) references quiz_decision;
alter table if exists medical_task_right_decisions add constraint FKsbholv8a0wnuxg8wdjfqek8dk foreign key (medical_task_id) references medical_task;
alter table if exists medical_task_result add constraint FK24n8h1yblo2lmu4en9m8xq1g9 foreign key (medical_task_id) references medical_task;
alter table if exists medical_topic_medical_tasks add constraint FK514icbeq89lpm5e7agtvby1j5 foreign key (medical_tasks_id) references medical_task;
alter table if exists medical_topic_medical_tasks add constraint FKav7l24nloti2v4bxxsietwgws foreign key (medical_topic_id) references medical_topic;
alter table if exists medical_topic_result add constraint FK4pfyo5oton8xesd1p4n4lr4rq foreign key (medical_topic_id) references medical_topic;
alter table if exists medical_topic_result_results add constraint FKbp0kvt1254girugeletcmdys1 foreign key (results_id) references medical_task_result;
alter table if exists medical_topic_result_results add constraint FKgs74ivkhgvcy4ay28wi9oa9p2 foreign key (medical_topic_result_id) references medical_topic_result;
alter table if exists usr_medical_results add constraint FK2m9822cxgp7g9ao47ejmxmmcp foreign key (medical_results_id) references medical_topic_result;
alter table if exists usr_medical_results add constraint FKfpjqbo8x222l1i6kvajdhq8cn foreign key (usr_id) references usr;