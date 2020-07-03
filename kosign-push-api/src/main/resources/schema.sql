CREATE EXTENSION IF NOT EXISTS tablefunc;

CREATE  VIEW vw_read_application_detail_by_app_id as
	SELECT * FROM crosstab(
		$$
					SELECT app_id as application ,CASE
					WHEN platform_id = '1' THEN  'ios'
					WHEN platform_id = '2' THEN  'android'
					WHEN platform_id = '3' THEN  'web' end as platform
					, count(*) as counter FROM ps_device_client group by app_id , platform_id ORDER BY 1,2

		$$,$$
		     VALUES ('ios'::text),('android'), ('web')

		$$

	)AS application_detail(application varchar, "ios" int, "android" int , "web" int);

CREATE VIEW vw_history_count AS 
	SELECT ps_history.app_id,
		count(*) AS count
	FROM ps_history
	GROUP BY ps_history.app_id;

CREATE VIEW vw_application_detail AS
	SELECT p.id as application,
		v.ios,
		v.android,
		v.web,
		p.name,
		p.created_at,
		p.updated_at,
		p.user_id,
		his.count
	FROM vw_read_application_detail_by_app_id v
		RIGHT JOIN ps_application p ON v.application::text = p.id::text
		LEFT JOIN vw_history_count his ON v.application::text = his.app_id::text
	WHERE p.status = '1'::bpchar; 

----------------------------------------------------
-- create view to read flatform by application id --
----------------------------------------------------
CREATE OR REPLACE VIEW public.vw_platform_detail AS
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
