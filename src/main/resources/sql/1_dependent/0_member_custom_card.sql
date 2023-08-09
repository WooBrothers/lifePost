insert into member_custom_card (member_id, contents, created_at, update_at)
values (1, 'member custom card. :0', now(), now()),
       (1, 'member custom card. :1', now() - INTERVAL '1' DAY, now() - INTERVAL '1' DAY),
       (1, 'member custom card. :2', now() - INTERVAL '2' DAY, now() - INTERVAL '2' DAY),
       (1, 'member custom card. :3', now() - INTERVAL '3' DAY, now() - INTERVAL '3' DAY),
       (1, 'member custom card. :4', now() - INTERVAL '4' DAY, now() - INTERVAL '4' DAY),
       (1, 'member custom card. :5', now() - INTERVAL '5' DAY, now() - INTERVAL '5' DAY),
       (1, 'member custom card. :6', now() - INTERVAL '6' DAY, now() - INTERVAL '6' DAY),
       (1, 'member custom card. :7', now() - INTERVAL '7' DAY, now() - INTERVAL '7' DAY),
       (1, 'member custom card. :8', now() - INTERVAL '8' DAY, now() - INTERVAL '8' DAY),
       (1, 'member custom card. :9', now() - INTERVAL '9' DAY, now() - INTERVAL '9' DAY),
       (1, 'member custom card. :10', now() - INTERVAL '10' DAY, now() - INTERVAL '10' DAY),
       (1, 'member custom card. :11', now() - INTERVAL '11' DAY, now() - INTERVAL '11' DAY),
       (1, 'member custom card. :12', now() - INTERVAL '12' DAY, now() - INTERVAL '12' DAY)
;
commit