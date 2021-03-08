/* start create action_log table */
create table if not exists action_log
(
    action_log_id 			int auto_increment primary key,
    fk_user_id              int                                     not null,
    command                 int                                     null,
    status                  tinyint                                 not null,
    message                 varchar(100)                            null,
    created_at              timestamp default CURRENT_TIMESTAMP     null,
    constraint fk_user_id_action_log foreign key (fk_user_id) references user(user_id)
);
/* finish create action_log table */