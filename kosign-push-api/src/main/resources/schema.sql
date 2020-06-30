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



CREATE OR REPLACE VIEW vw_count_sender_history AS
    SELECT v.*, p.name, p.created_at,p.updated_at ,p.user_id  from vw_read_application_detail_by_app_id v INNER JOIN ps_application p ON
    v.application = p.id;

-- static data
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('1', '', NULL, 'Apple IOS', NULL, '1', NULL);
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('2', 'FCM', NULL, 'Android', NULL, '1', NULL);
INSERT INTO public.ps_platform (id, code, icon, name, registered_at, status, updated_at) VALUES ('3', 'Web FCM', NULL, 'Web', NULL, '1', NULL);
