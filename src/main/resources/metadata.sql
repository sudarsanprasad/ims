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
    VALUES (3, 'Service Now', 'Deloitte', 'col3', 'number', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (4, 'Service Now', 'Deloitte', 'col4', 'description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (5, 'Service Now', 'Deloitte', 'col5', 'short_description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (6, 'Service Now', 'Deloitte', 'col6', 'comments', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (7, 'Service Now', 'Deloitte', 'col7', 'incident_state', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (8, 'Service Now', 'Deloitte', 'col8', 'opened_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (9, 'Service Now', 'Deloitte', 'col9', 'closed_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (10, 'Service Now', 'Deloitte', 'col10', 'resolved_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (11, 'Service Now', 'Deloitte', 'col11', 'assignment_group', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (12, 'Service Now', 'Deloitte', 'col12', 'priority', 'Y', 'N', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (13, 'Service Now', 'Deloitte', 'col13', 'category', 'Y', 'Y', 'Y');    

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (14, 'Service Now', 'Deloitte', 'col14', 'cmdb_ci', 'N', 'Y', 'Y');       




INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (15, 'AMPM', 'Deloitte', 'col1', 'jobid', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (16, 'AMPM', 'Deloitte', 'col2', 'version', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (17, 'AMPM', 'Deloitte', 'col3', 'Incident ID', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (18, 'AMPM', 'Deloitte', 'col4', 'Title', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (19, 'AMPM', 'Deloitte', 'col5', 'Subarea', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (20, 'AMPM', 'Deloitte', 'col6', 'Solution', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (21, 'AMPM', 'Deloitte', 'col8', 'Open Time', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (22, 'AMPM', 'Deloitte', 'col9', 'Close Time', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (23, 'AMPM', 'Deloitte', 'col11', 'Assignment Group', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (24, 'AMPM', 'Deloitte', 'col12', 'priority', 'Y', 'N', 'Y');
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (25, 'AMPM', 'Deloitte', 'col15', 'Affected Service', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (26, 'AMPM', 'Deloitte', 'col16', 'Affective Service Captured', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (27, 'AMPM', 'Deloitte', 'col17', 'Affected CI', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (28, 'AMPM', 'Deloitte', 'col18', 'Area', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (29, 'AMPM', 'Deloitte', 'col19', 'Client Ticket', 'Y', 'N', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (30, 'AMPM', 'Deloitte', 'col20', 'Resolution Time', 'Y', 'N', 'Y');


















































    