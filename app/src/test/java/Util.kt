
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

fun readFileContent(file: File): String {
    val fileContentBuilder = StringBuilder()
    if (file.exists()) {
        var stringLine: String?
        try {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            stringLine = bufferedReader.readLine()
            while (stringLine != null) {
                fileContentBuilder.append(stringLine).append("\n")
                stringLine = bufferedReader.readLine()
            }
            bufferedReader.close()
            fileReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        
    }
    var result = fileContentBuilder.toString()
    result = result.replace("\n", "")
    return result
}