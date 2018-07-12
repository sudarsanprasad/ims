INSERT INTO public.field_mask(id, customer, field_name, mask_enabled, system_name) VALUES (1,'Deloitte','Phone number','X','Service Now');
INSERT INTO public.field_mask(id, customer, field_name, mask_enabled, system_name) VALUES (2,'Deloitte','Signature','X','Service Now');
INSERT INTO public.field_mask(id, customer, field_name, mask_enabled, system_name) VALUES (3,'Deloitte','URL','X','Service Now');
INSERT INTO public.field_mask(id, customer, field_name, mask_enabled, system_name) VALUES (4,'Deloitte','Email','X','Service Now');
INSERT INTO public.field_mask(id, customer, field_name, mask_enabled, system_name) VALUES (5,'Deloitte','SSN','X','Service Now');

INSERT INTO public.field_configuration(id, property, system_name) VALUES (1,'jobid','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (2,'version','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (3,'number','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (4,'short_description','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (5,'description','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (6,'opened_at','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (7,'closed_at','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (8,'priority','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (9,'assignment_group','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (10,'comments','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (11,'customername','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (12,'systemname','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (13,'incident_state','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (14,'resolved_at','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (15,'category ID','Service Now');
INSERT INTO public.field_configuration(id, property, system_name) VALUES (16,'cmdb_ci ID','Service Now');

INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (1,'jobid','Deloitte','Y','N','N','col1','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (2,'version','Deloitte','Y','N','N','col2','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (3,'Incident_ID','Deloitte','Y','Y','Y','col3','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (4,'IA_Interaction_ID','Deloitte','Y','N','Y','col4','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (5,'Affected_Service','Deloitte','Y','Y','Y','col5','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (6,'Affective_Service_Captured','Deloitte','Y','N','Y','col6','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (7,'Affected_CI','Deloitte','Y','Y','Y','col7','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (8,'Area','Deloitte','Y','N','Y','col8','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (9,'Subarea','Deloitte','Y','N','Y','col9','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (10,'Title','Deloitte','Y','Y','Y','col10','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (11,'Open_Time','Deloitte','Y','Y','Y','col11','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (12,'Close_Time','Deloitte','Y','Y','Y','col12','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (13,'Resolution_Time','Deloitte','Y','N','Y','col13','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (14,'Call_Source','Deloitte','Y','N','Y','col14','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (15,'priority','Deloitte','Y','N','Y','col15','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (16,'Urgency','Deloitte','Y','N','Y','col16','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (17,'Assignment_Group','Deloitte','Y','Y','Y','col17','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (18,'Assignee','Deloitte','Y','N','Y','col18','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (19,'Client_Ticket','Deloitte','Y','N','Y','col19','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (20,'Vendor','Deloitte','Y','N','Y','col20','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (21,'Country','Deloitte','Y','N','Y','col21','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (22,'Vendor_Ticket','Deloitte','Y','N','Y','col22','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (23,'Opened_By','Deloitte','Y','N','Y','col23','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (24,'SLO_Results_Breached','Deloitte','Y','N','Y','col24','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (25,'Solution','Deloitte','Y','Y','Y','col25','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (26,'customername','Deloitte','Y','Y','N','col26','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (27,'systemname','Deloitte','Y','Y','N','col27','AMPM');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (28,'jobid','Deloitte','Y','N','Y','col1','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (29,'version','Deloitte','Y','N','Y','col2','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (30,'number','Deloitte','Y','Y','Y','col3','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (31,'short_description','Deloitte','Y','Y','Y','col9','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (32,'description','Deloitte','Y','Y','Y','col10','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (33,'opened_at','Deloitte','Y','Y','Y','col11','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (34,'closed_at','Deloitte','Y','Y','Y','col12','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (35,'priority','Deloitte','Y','N','Y','col15','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (36,'assignment_group','Deloitte','Y','Y','Y','col17','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (37,'comments','Deloitte','Y','Y','Y','col25','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (38,'customername','Deloitte','Y','Y','Y','col26','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (39,'systemname','Deloitte','Y','Y','Y','col27','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (40,'incident_state','Deloitte','Y','Y','Y','col28','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (41,'resolved_at','Deloitte','Y','Y','Y','col29','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (42,'category','Deloitte','Y','Y','Y','col30','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (43,'cmdb_ci','Deloitte','Y','Y','Y','col31','Service Now');
INSERT INTO public.ticket_metadata(id, business_column, customer, is_forecast, is_knowledgement, is_proactive, mapping_column, system_name) VALUES (44,'state','Deloitte','Y','N','N','col32','Service Now');

INSERT INTO public.ticket_configuration(id, property, value) VALUES (1,'apischedulerflag','N');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (2,'ftpschedulerflag','N');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (3,'apischedulertime','*/240 * * * * *');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (4,'ftpschedulertime','*/240 * * * * *');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (5,'servicenow.lastrundate','2017-07-05 16:52:01');
INSERT INTO public.ticket_configuration(id, property, value, frequency_type, frequency_value) VALUES (6,'forecast.cronvalue','0 0/20 * 1/1 * ? *','Minutes','20');
INSERT INTO public.ticket_configuration(id, property, value, frequency_type, frequency_value) VALUES (7,'kr.cronvalue','0 0/50 * 1/1 * ? *','Minutes','50');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (8,'forecast.model.status','OPEN');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (9,'kr.build.status','OPEN');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (10,'service now.filter','?sysparm_query=sys_updated_on>javascript:gs.dateGenerate');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (11,'servicenow.comments.url','https://dev29786.service-now.com/api/now/v2/table/incident?sysparm_display_value=true&sysparm_fields=comments&sysparm_query=number=');
INSERT INTO public.ticket_configuration(id, property, value) VALUES (12,'ppm.scheduler.status','OPEN');
INSERT INTO public.ticket_configuration(id, property, value, frequency_type, frequency_value) VALUES (13,'ppm.cronvalue','0 0/50 * 1/1 * ? *','Minutes','50');

