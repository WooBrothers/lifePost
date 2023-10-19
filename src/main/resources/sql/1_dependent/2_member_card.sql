insert into member_card (member_id, type, member_letter_id, affirmation_card_id,
                         member_custom_card_id,
                         focus,
                         created_at, update_at)
values (1, 'CUSTOM', null, null, 1, 'NON', now(), now()),
       (1, 'CUSTOM', null, null, 2, 'NON', now() - INTERVAL '1' DAY, now() - INTERVAL '1' DAY),
       (1, 'CUSTOM', null, null, 3, 'NON', now() - INTERVAL '2' DAY, now() - INTERVAL '2' DAY),
       (1, 'CUSTOM', null, null, 4, 'NON', now() - INTERVAL '3' DAY, now() - INTERVAL '3' DAY),
       (1, 'CUSTOM', null, null, 5, 'NON', now() - INTERVAL '4' DAY, now() - INTERVAL '4' DAY),
       (1, 'CUSTOM', null, null, 6, 'NON', now() - INTERVAL '5' DAY, now() - INTERVAL '5' DAY),
       (1, 'CUSTOM', null, null, 7, 'NON', now() - INTERVAL '6' DAY, now() - INTERVAL '6' DAY),
       (1, 'CUSTOM', null, null, 8, 'NON', now() - INTERVAL '7' DAY, now() - INTERVAL '7' DAY),
       (1, 'CUSTOM', null, null, 9, 'NON', now() - INTERVAL '8' DAY, now() - INTERVAL '8' DAY),
       (1, 'CUSTOM', null, null, 10, 'NON', now() - INTERVAL '9' DAY, now() - INTERVAL '9' DAY),
       (1, 'CUSTOM', null, null, 11, 'NON', now() - INTERVAL '10' DAY, now() - INTERVAL '10' DAY),
       (1, 'CUSTOM', null, null, 12, 'NON', now() - INTERVAL '11' DAY, now() - INTERVAL '11' DAY),
       (1, 'CUSTOM', null, null, 13, 'NON', now() - INTERVAL '12' DAY, now() - INTERVAL '12' DAY)
;
commit



























