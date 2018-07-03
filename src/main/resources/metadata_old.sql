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
    VALUES (1, 'AMPM', 'Deloitte', 'col1', 'jobid', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (2, 'AMPM', 'Deloitte', 'col2', 'version', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (3, 'AMPM', 'Deloitte', 'col3', 'Incident ID', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (4, 'AMPM', 'Deloitte', 'col4', 'Title', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (5, 'AMPM', 'Deloitte', 'col5', 'Subarea', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (6, 'AMPM', 'Deloitte', 'col6', 'Solution', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (7, 'AMPM', 'Deloitte', 'col7', 'Open Time', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (8, 'AMPM', 'Deloitte', 'col8', 'Close Time', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (9, 'AMPM', 'Deloitte', 'col9', 'Assignment Group', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (10, 'AMPM', 'Deloitte', 'col10', 'priority', 'Y', 'N', 'Y');
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (11, 'AMPM', 'Deloitte', 'col11', 'Affected Service', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (12, 'AMPM', 'Deloitte', 'col12', 'Affective Service Captured', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (13, 'AMPM', 'Deloitte', 'col13', 'Affected CI', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (14, 'AMPM', 'Deloitte', 'col14', 'Area', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (15, 'AMPM', 'Deloitte', 'col15', 'Client Ticket', 'Y', 'N', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (16, 'AMPM', 'Deloitte', 'col16', 'Resolution Time', 'Y', 'N', 'Y');
    
    
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (17, 'Service Now', 'Deloitte', 'col1', 'jobid', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (18, 'Service Now', 'Deloitte', 'col2', 'version', 'N', 'N', 'N');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (19, 'Service Now', 'Deloitte', 'col3', 'number', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (20, 'Service Now', 'Deloitte', 'col8', 'short_description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (21, 'Service Now', 'Deloitte', 'col9', 'description', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (22, 'Service Now', 'Deloitte', 'col10', 'opened_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (23, 'Service Now', 'Deloitte', 'col11', 'closed_at', 'Y', 'Y', 'Y');

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (24, 'Service Now', 'Deloitte', 'col13', 'priority', 'Y', 'Y', 'Y');    

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (25, 'Service Now', 'Deloitte', 'col14', 'assignment_group', 'N', 'Y', 'Y');       

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (26, 'Service Now', 'Deloitte', 'col16', 'comments', 'N', 'Y', 'Y'); 
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (27, 'Service Now', 'Deloitte', 'col17', 'incident_state', 'N', 'Y', 'Y'); 

INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (28, 'Service Now', 'Deloitte', 'col18', 'resolved_at', 'N', 'Y', 'Y'); 
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (29, 'Service Now', 'Deloitte', 'col19', 'category', 'N', 'Y', 'Y'); 
    
INSERT INTO ticket_metadata(id, system_name, customer, mapping_column, business_column, is_forecast, is_knowledgement, is_proactive)
    VALUES (30, 'Service Now', 'Deloitte', 'col20', 'cmdb_ci', 'N', 'Y', 'Y'); 




    