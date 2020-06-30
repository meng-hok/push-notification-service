CREATE EXTENSION IF NOT EXISTS tablefunc;

CREATE OR REPLACE VIEW vw_read_application_detail_by_app_id as
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

CREATE OR REPLACE VIEW vw_history_count AS 
	SELECT ps_history.app_id,
		count(*) AS count
	FROM ps_history
	GROUP BY ps_history.app_id;

CREATE OR REPLACE VIEW vw_application_detail AS
	SELECT v.application,
		v.ios,
		v.android,
		v.web,
		p.name,
		p.created_at,
		p.updated_at,
		p.user_id,
		his.count
	FROM vw_read_application_detail_by_app_id v
		JOIN ps_application p ON v.application::text = p.id::text
		LEFT JOIN vw_history_count his ON v.application::text = his.app_id::text
	WHERE p.status = '1'::bpchar;

-- static data
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('1', '', NULL, 'Apple IOS', NULL, '1', NULL);
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('2', 'FCM', NULL, 'Android', NULL, '1', NULL);
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('3', 'Web FCM', NULL, 'Web', NULL, '1', NULL);
