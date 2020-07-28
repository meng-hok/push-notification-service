CREATE EXTENSION IF NOT EXISTS tablefunc;


CREATE VIEW vw_history_count AS 
	 SELECT application_detail.application AS app_id,
    application_detail.to_ios,
    application_detail.to_android,
    application_detail.to_web
    FROM crosstab('
				SELECT ps_history.app_id AS application,

						CASE 
							WHEN to_platform = ''1'' THEN  ''to_ios''
							WHEN to_platform = ''2'' THEN  ''to_android''
							WHEN to_platform = ''3'' THEN  ''to_web'' end as platform,
						count(*) AS count
					   FROM ps_history
				  GROUP BY application ,platform  
			'::text, 'VALUES (''to_ios''::text),(''to_android''), (''to_web'')'::text) application_detail(application character varying, to_ios integer, to_android integer, to_web integer);

CREATE VIEW vw_platform_count AS 
	SELECT ps_platform_setting.application_id AS app_id,
    count(*) AS platform
	FROM ps_platform_setting
	WHERE status <> '9'
	GROUP BY ps_platform_setting.application_id;

CREATE VIEW vw_subscriber_count AS
	SELECT ps_device_client.app_id,
	count(*) AS subscriber
	FROM ps_device_client
	WHERE status <> '9'
	GROUP BY ps_device_client.app_id;

CREATE VIEW vw_application_detail AS 
	SELECT p.id AS application,
		p.name,
		p.created_at,
		p.updated_at,
		p.user_id,
		his.app_id,
		his.to_ios,
		his.to_android,
		his.to_web,
		pf.platform,
		sc.subscriber,
		 u.username
	FROM ps_application p
		LEFT JOIN vw_history_count his ON p.id::text = his.app_id::text
		LEFT JOIN vw_platform_count pf ON p.id::text = pf.app_id::text
		LEFT JOIN vw_subscriber_count sc ON p.id::text = sc.app_id::text
		LEFT JOIN ps_user u ON p.user_id::text = u.id::text
	WHERE p.status = '1'::bpchar;

----------------------------------------------------
-- create view to read flatform by application id --
----------------------------------------------------
CREATE OR REPLACE VIEW vw_platform_detail AS
 SELECT ( SELECT pf.id
           FROM ps_platform pf
          WHERE pf.id::text = pt.platform_id::text) AS plat_id,
    ( SELECT pf.name
           FROM ps_platform pf
          WHERE pf.id::text = pt.platform_id::text) AS plat_nm,
    ( SELECT pf.icon
           FROM ps_platform pf
          WHERE pf.id::text = pt.platform_id::text) AS icon,
    ( SELECT pf.code
           FROM ps_platform pf
          WHERE pf.id::text = pt.platform_id::text) AS plat_code,
    pt.bundle_id,
    pt.key_id,
    pt.team_id,
    pt.push_url AS cert_file,
    pt.authorized_key,
    pt.status AS sts,
    pt.application_id
   FROM ps_platform_setting pt
  WHERE pt.status = '1'::bpchar
  ORDER BY (( SELECT pf.id
           FROM ps_platform pf
          WHERE pf.id::text = pt.platform_id::text));
