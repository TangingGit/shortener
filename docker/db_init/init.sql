-- demo.shorten_url definition
USE demo;
CREATE TABLE `shorten_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `short_url` varchar(62) NOT NULL,
  `original_url` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `shorten_url_idx01` (`short_url`) USING BTREE,
  KEY `shorten_url_idx02` (`original_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;


-- demo.`user` definition

CREATE TABLE `user` (
  `email` varchar(30) NOT NULL,
  `password` varchar(64) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE OR REPLACE SEQUENCE `base62_counter_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB