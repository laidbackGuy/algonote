-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: k10b203.p.ssafy.io    Database: algonote
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_en` varchar(255) DEFAULT NULL,
  `group` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'dfs','graph_theory'),(2,'graphs','graph_theory'),(3,'graph_traversal','graph_theory'),(4,'bfs','graph_theory'),(5,'math','math_theory'),(6,'mitm','optimization'),(7,'bitmask','implementation'),(8,'greedy','optimization'),(9,'mst','graph_theory'),(10,'data_structures','data_structure'),(11,'priority_queue','data_structure'),(12,'sorting','implementation'),(13,'convex_hull','optimization'),(14,'geometry','math_theory'),(15,'bruteforcing','implementation'),(16,'trees','data_structure'),(17,'eulerian_path','graph_theory'),(18,'divide_and_conquer','optimization'),(19,'recursion','optimization'),(20,'polygon_area','math_theory'),(21,'line_intersection','math_theory'),(22,'point_in_convex_polygon','math_theory'),(23,'simulation','implementation'),(24,'implementation','implementation'),(25,'number_theory','math_theory'),(26,'sieve','math_theory'),(27,'primality_test','math_theory'),(28,'string','string'),(29,'kmp','string'),(30,'dp','optimization'),(31,'dp_bitfield','optimization'),(32,'ternary_search','optimization'),(33,'scc','graph_theory'),(34,'2_sat','graph_theory'),(35,'two_pointer','implementation'),(36,'segtree','data_structure'),(37,'lazyprop','data_structure'),(38,'sweeping','implementation'),(39,'tree_set','data_structure'),(40,'NaN',NULL),(41,'binary_search','optimization'),(42,'prefix_sum','data_structure'),(43,'case_work','implementation'),(44,'arithmetic','math_theory'),(45,'shortest_path','graph_theory'),(46,'dijkstra','graph_theory'),(47,'bipartite_matching','graph_theory'),(48,'physics','math_theory'),(49,'centroid','data_structure'),(50,'tree_decomposition','graph_theory'),(51,'centroid_decomposition','data_structure'),(52,'dp_tree','optimization'),(53,'cht','optimization'),(54,'calculus','math_theory'),(55,'euler_tour_technique','graph_theory'),(56,'ad_hoc','implementation'),(57,'queue','data_structure'),(58,'stack','data_structure'),(59,'combinatorics','math_theory'),(60,'euclidean','math_theory'),(61,'coordinate_compression','data_structure'),(62,'deque','data_structure'),(63,'floyd_warshall','graph_theory'),(64,'parsing','implementation'),(65,'probability','math_theory'),(66,'disjoint_set','graph_theory'),(67,'linearity_of_expectation','math_theory'),(68,'arbitrary_precision','math_theory'),(69,'pbs','optimization'),(70,'lca','graph_theory'),(71,'knapsack','optimization'),(72,'topological_sorting','graph_theory'),(73,'dag','graph_theory'),(74,'articulation','graph_theory'),(75,'smaller_to_larger','data_structure'),(76,'flow','optimization'),(77,'game_theory','optimization'),(78,'sprague_grundy','optimization'),(79,'bitset','implementation'),(80,'hungarian',NULL),(81,'mcmf','optimization'),(82,'euler_phi','math_theory'),(83,'rotating_calipers','math_theory'),(84,'backtracking','implementation'),(85,'fft','optimization'),(86,'aho_corasick','implementation'),(87,'hashing','data_structure'),(88,'trie','string'),(89,'hash_set','data_structure'),(90,'berlekamp_massey','math_theory'),(91,'exponentiation_by_squaring','math_theory'),(92,'constructive','implementation'),(93,'hld','graph_theory'),(94,'lis','optimization'),(95,'circulation','optimization'),(96,'sliding_window','implementation'),(97,'kitamasa','math_theory'),(98,'parametric_search','optimization'),(99,'half_plane_intersection','optimization'),(100,'biconnected_component','graph_theory'),(101,'pollard_rho','math_theory'),(102,'miller_rabin','math_theory'),(103,'sparse_table','data_structure'),(104,'heuristics','implementation'),(105,'cactus','graph_theory'),(106,'linear_algebra','math_theory'),(107,'gaussian_elimination','math_theory'),(108,'modular_multiplicative_inverse','math_theory'),(109,'flt','math_theory'),(110,'permutation_cycle_decomposition','graph_theory'),(111,'randomization','math_theory'),(112,'bipartite_graph','graph_theory'),(113,'tsp','graph_theory'),(114,'geometry_3d','math_theory'),(115,'numerical_analysis','math_theory'),(116,'alien','optimization'),(117,'bidirectional_search','graph_theory'),(118,'stable_marriage','graph_theory'),(119,'pythagoras','math_theory'),(120,'0_1_bfs','graph_theory'),(121,'crt','math_theory'),(122,'bellman_ford','graph_theory'),(123,'voronoi','math_theory'),(124,'delaunay','math_theory'),(125,'mfmc','optimization'),(126,'extended_euclidean','math_theory'),(127,'precomputation','implementation'),(128,'offline_queries','data_structure'),(129,'merge_sort_tree','data_structure'),(130,'suffix_array','implementation'),(131,'inclusion_and_exclusion','math_theory'),(132,'palindrome_tree','graph_theory'),(133,'hall','data_structure'),(134,'sqrt_decomposition','math_theory'),(135,'tree_isomorphism','graph_theory'),(136,'directed_mst','graph_theory'),(137,'rabin_karp','string'),(138,'dp_connection_profile','optimization'),(139,'splay_tree','graph_theory'),(140,'lgv','math_theory'),(141,'divide_and_conquer_optimization','optimization'),(142,'monotone_queue_optimization','optimization'),(143,'link_cut_tree','data_structure'),(144,'pst','graph_theory'),(145,'discrete_log','math_theory'),(146,'planar_graph','math_theory'),(147,'generating_function','math_theory'),(148,'discrete_sqrt','math_theory'),(149,'offline_dynamic_connectivity',NULL),(150,'euler_characteristic','math_theory'),(151,'slope_trick',NULL),(152,'pigeonhole_principle',NULL),(153,'mo',NULL),(154,'linked_list','data_structure'),(155,'polynomial_interpolation',NULL),(156,'statistics','math_theory'),(157,'manacher','string'),(158,'lucas',NULL),(159,'tree_compression',NULL),(160,'linear_programming',NULL),(161,'knuth',NULL),(162,'multi_segtree',NULL),(163,'dp_sum_over_subsets',NULL),(164,'dp_deque','optimization'),(165,'deque_trick','optimization'),(166,'functional_graph',NULL),(167,'matroid',NULL),(168,'utf8',NULL),(169,'mobius_inversion',NULL),(170,'point_in_non_convex_polygon','math_theory'),(171,'lte',NULL),(172,'top_tree',NULL),(173,'general_matching',NULL),(174,'min_enclosing_circle','math_theory'),(175,'dp_digit','optimization'),(176,'burnside',NULL),(177,'multipoint_evaluation',NULL),(178,'differential_cryptanalysis',NULL),(179,'dancing_links',NULL),(180,'simulated_annealing',NULL),(181,'bayes',NULL),(182,'cartesian_tree',NULL),(183,'birthday',NULL),(184,'regex','math_theory'),(185,'degree_sequence','graph_theory'),(186,'green',NULL),(187,'geometric_boolean_operations',NULL),(188,'majority_vote',NULL),(189,'chordal_graph',NULL),(190,'geometry_hyper',NULL),(191,'gradient_descent',NULL),(192,'dominator_tree',NULL),(193,'duality',NULL),(194,'knuth_x',NULL),(195,'suffix_tree',NULL),(196,'dual_graph',NULL),(197,'pick',NULL),(198,'flood_fill','graph_theory'),(199,'z',NULL),(200,'stoer_wagner',NULL),(201,'rope',NULL),(202,'hirschberg',NULL),(203,'floor_sum',NULL),(204,'discrete_kth_root',NULL),(205,'hackenbush',NULL),(206,'rb_tree',NULL);
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-19 17:12:20
