package com.app.armygyan.output

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException

/**
 * Generate a file and get it back because it is you to take.
 */
class OutputTargetGenerator private constructor(
    private var rootDirectory: File
) {

    companion object {

        fun fromCacheDirectory(context: Context): OutputTargetGenerator {
            return OutputTargetGenerator(context.cacheDir)
        }

        fun fromExternalCacheDirectory(internalFolderName: String? = null): OutputTargetGenerator {

			var path = "${Environment.getExternalStorageDirectory()}/.postermaker"
			if (!internalFolderName.isNullOrBlank()) {
				path = "$path/$internalFolderName"
			}

            return OutputTargetGenerator(
                File(path)
            )
        }

        fun fromDefaultExternalCacheDirectory(): OutputTargetGenerator {
            return fromExternalCacheDirectory("default")
        }

        fun fromAppDirectory(): OutputTargetGenerator {
            return OutputTargetGenerator(
				File("${Environment.getExternalStorageDirectory()}/PosterMaker")
            )
        }
    }

    private lateinit var outputFile: File

	init {
	    rootDirectory.mkdirs()
	}

    fun getRootDirectory () = rootDirectory

    fun changeRootDirectory (directory: File): OutputTargetGenerator {
        rootDirectory = directory
        return this
    }

    fun setDefaultOutputDestination(
        _childFolderName: String? = null,
        _fileName: String? = null,
        _fileExtension: String? = "",
        useExtension: Boolean = true,
        createFile: Boolean = true
    ): OutputTargetGenerator {

        val childFolderName = _childFolderName ?: OutputTarget.FOLDER_OUTPUT

        val fileName = if (_fileName.isNullOrBlank()) {
            "com.app.armygyan.android.${System.nanoTime()}"
        } else _fileName

        val fileExtension = if (useExtension) {
            if (_fileExtension.isNullOrBlank())
				/* Danger */
                OutputExtension.PNG
			else _fileExtension
        } else _fileExtension

        setOutputDestination(File(rootDirectory, "$childFolderName/$fileName$fileExtension"), createFile)
        return this
    }

    fun getDefaultOutputDestination(
         childFolderName: String? = null,
        _fileName: String? = null,
        _fileExtension: String? = "",
        useExtension: Boolean = true,
        createFile: Boolean = true
    ): OutputTargetGenerator {



       if(_fileName.isNullOrBlank()) {
           this.outputFile = File(rootDirectory, childFolderName)
           if (outputFile.exists()) {
               return this
           }else{
               outputFile.parentFile?.mkdirs()
               return this
           }

        }

        val fileExtension = if (useExtension) {
            if (_fileExtension.isNullOrBlank())
            /* Danger */
                OutputExtension.PNG
            else _fileExtension
        } else _fileExtension

        this.outputFile = File(rootDirectory, "$childFolderName/$_fileName$fileExtension")

        if (outputFile.exists()) {
//            val deleted = outputFile.delete()
//            if (!deleted) {
//                Log.e("ERROR","Output file exists, and we couldn't delete it.")
//            }
            return this
        }

        outputFile.parentFile?.mkdirs()

        if (createFile) {
            outputFile.createNewFile()
        }
        return this
    }

    fun setOutputDestination(path: String): OutputTargetGenerator {
        return setOutputDestination(File(path))
    }

    fun setOutputDestination(file: File, createFile: Boolean = true): OutputTargetGenerator {
        this.outputFile = file

        if (outputFile.exists()) {
//            val deleted = outputFile.delete()
//            if (!deleted) {
//                Log.e("ERROR","Output file exists, and we couldn't delete it.")
//            }
            return this
        }

        outputFile.parentFile?.mkdirs()

        if (createFile) {
            outputFile.createNewFile()
        }
        return this
    }


    fun getOutputDirectory (): File {

        if (!::outputFile.isInitialized) {
            throw NullPointerException("No output destination was specified.")
        }

        return outputFile.parentFile ?: throw FileNotFoundException("Parent dir not found for output file.")
    }

    fun getOutputFile (): File {

        if (!::outputFile.isInitialized) {
            throw NullPointerException("No output destination was specified.")
        }

        return outputFile
    }


    fun existsInRootDirectory(file: File): Boolean {

        if (file.exists()) {
            if (file.absolutePath.contains(rootDirectory.absolutePath)) {
                return true
            }
        }

        return false
    }


}
