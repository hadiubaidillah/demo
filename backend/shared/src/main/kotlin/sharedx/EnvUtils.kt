//package shared
//
//import java.nio.file.Files
//import java.nio.file.Paths
//
//object EnvUtils {
//    fun loadEnvVariables(filePath: String): List<Pair<String, String>> {
//        return Files.readAllLines(Paths.get(filePath))
//            .filter { it.isNotEmpty() && !it.startsWith("#") } // Ignore empty lines and comments
//            .map { line ->
//                val (key, value) = line.split("=", limit = 2)
//                key.trim() to value.trim()
//            }
//    }
//}
//
