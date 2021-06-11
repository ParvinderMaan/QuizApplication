package com.app.armygyan.helper

import android.content.Context
import android.media.MediaScannerConnection
import java.io.File

// Tells the media scanner about the new file so that it is
// immediately available to the user.

/**
 *
 * https://stackoverflow.com/questions/30506301
 */
class MediaScannerUtility(
	private val context: Context
) {

	fun scan(file: File?) {

		if (file == null) {
			return
		}

		if (!file.exists()) {
			return
		}

		MediaScannerConnection
			.scanFile(
				context,
				arrayOf(file.absolutePath),
				null
			) { _, _ ->  /* ignored */ }
	}
}
