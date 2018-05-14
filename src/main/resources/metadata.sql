CREATE TABLE ticket_metadata
(
  id bigint NOT NULL,
  business_column character varying(255),
  customer character varying(255),
  is_forecast character varying(255),
  is_knowledgement character varying(255),
  is_proactive character varying(255),
  mapping_column character varying(255),
  system_name character varying(255),
  CONSTRAINT ticket_metadata_pkey PRIMARY KEY (id)
);


INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (1, 'Service Now', 'Deloitte', 'col1', 'jobid', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (2, 'Service Now', 'Deloitte', 'col2', 'version', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (3, 'Service Now', 'Deloitte', 'col3', 'number', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (4, 'Service Now', 'Deloitte', 'col4', 'description', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (5, 'Service Now', 'Deloitte', 'col5', 'short_description', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (6, 'Service Now', 'Deloitte', 'col6', 'short_description', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (7, 'Service Now', 'Deloitte', 'col7', 'comments', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (8, 'Service Now', 'Deloitte', 'col8', 'comments', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (9, 'Service Now', 'Deloitte', 'col9', 'incident_state', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (10, 'Service Now', 'Deloitte', 'col10', 'opened_at', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (11, 'Service Now', 'Deloitte', 'col11', 'closed_at', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (12, 'Service Now', 'Deloitte', 'col12', 'resolved_at', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (13, 'Service Now', 'Deloitte', 'col13', 'assignment_group', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (14, 'Service Now', 'Deloitte', 'col14', 'priority', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (15, 'Service Now', 'Deloitte', 'col15', 'category', 'N', 'N', 'N');    

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (16, 'Service Now', 'Deloitte', 'col16', 'cmdb_ci', 'N', 'N', 'N');       

































































    