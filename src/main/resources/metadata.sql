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
    VALUES (1, 'Service Now', 'Deloitte', 'col1', 'number', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (2, 'Service Now', 'Deloitte', 'col2', 'description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (3, 'Service Now', 'Deloitte', 'col3', 'short_description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (4, 'Service Now', 'Deloitte', 'col4', 'comments', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (5, 'Service Now', 'Deloitte', 'col5', 'incident_state', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (6, 'Service Now', 'Deloitte', 'col6', 'opened_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (7, 'Service Now', 'Deloitte', 'col7', 'sys_created_by', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (8, 'Service Now', 'Deloitte', 'col8', 'sys_updated_on', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (9, 'Service Now', 'Deloitte', 'col9', 'category', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (10, 'Service Now', 'Deloitte', 'col10', 'priority', 'Y', 'Y', 'Y');

    