CREATE TABLE showcase.my_user (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `patronymic` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  `post` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mail` (`mail`)
);

CREATE TABLE showcase.project_full_info (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `department` varchar(255) NOT NULL,
  `status` enum('FREE','ON_WORK','COMPLETED','ON_VERIFICATION','CANCELED') NOT NULL,
  `goal` varchar(500) NOT NULL,
  `barrier` varchar(1000) NOT NULL,
  `decisions` varchar(1000) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_full_info_ibfk_1` (`customer_id`),
  CONSTRAINT `project_full_info_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `my_user` (`id`)
);

CREATE TABLE showcase.team (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `teamlid_id` bigint DEFAULT NULL,
  `current_project_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `teamlid_id` (`teamlid_id`),
  KEY `current_project_id` (`current_project_id`),
  CONSTRAINT `team_ibfk_1` FOREIGN KEY (`teamlid_id`) REFERENCES `my_user` (`id`),
  CONSTRAINT `team_ibfk_2` FOREIGN KEY (`current_project_id`) REFERENCES `project_full_info` (`id`)
);

CREATE TABLE showcase.team_completed_projects (
  `team_id` bigint NOT NULL,
  `completed_projects_id` bigint NOT NULL,
  PRIMARY KEY (`team_id`,`completed_projects_id`),
  KEY `completed_projects_id` (`completed_projects_id`),
  CONSTRAINT `team_completed_projects_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
  CONSTRAINT `team_completed_projects_ibfk_2` FOREIGN KEY (`completed_projects_id`) REFERENCES `project_full_info` (`id`)
);

CREATE TABLE showcase.team_refused_projects (
  `team_id` bigint NOT NULL,
  `refused_projects_id` bigint NOT NULL,
  PRIMARY KEY (`team_id`,`refused_projects_id`),
  KEY `refused_projects_id` (`refused_projects_id`),
  CONSTRAINT `team_refused_projects_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
  CONSTRAINT `team_refused_projects_ibfk_2` FOREIGN KEY (`refused_projects_id`) REFERENCES `project_full_info` (`id`)
);

CREATE TABLE showcase.team_teammates (
  `team_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`team_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `team_teammates_ibfk_1` FOREIGN KEY (`team_id`) REFERENCES `team` (`id`),
  CONSTRAINT `team_teammates_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `my_user` (`id`)
);
