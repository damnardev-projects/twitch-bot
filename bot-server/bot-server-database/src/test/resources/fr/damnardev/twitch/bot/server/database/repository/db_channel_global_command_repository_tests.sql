ALTER SEQUENCE s_channel_command RESTART WITH 1;

INSERT INTO t_channel(id, name, online, bot_enabled)
VALUES (1, 'channel_01', true, true),
       (2, 'channel_02', true, false);

INSERT INTO t_channel_global_command(id, name, enabled, channel_id, cooldown,
                                     last_execution,
                                     type)
VALUES (nextval('s_channel_command'), '!foo', true, 1, 60, null, 'SUGGEST_GAME');
