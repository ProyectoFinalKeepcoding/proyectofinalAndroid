package com.mockknights.petshelter.ui.components.detailImage

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mockknights.petshelter.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class DetailImageViewModel@Inject constructor(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences,
    private val coroutineDispatcher: CoroutineDispatcher
    ): ViewModel() {

    fun onSelectedImage(uri: Uri?, shelterId: String, localContext: Context) {
        // Check token from shared preferences
        sharedPreferences.getString("TOKEN", null)?.let { _ ->
            // Upload image to the server
            uri?.let { unwrappedUri ->
                viewModelScope.launch(coroutineDispatcher) {
                    val sourceFile = withContext(coroutineDispatcher) {
                        // Get file from unwrappedUri
                    val contentResolver = localContext.contentResolver
                    val cacheDir = localContext.cacheDir
                    val inputStream = contentResolver.openInputStream(unwrappedUri)
                    if (inputStream != null) {
                        val file = File.createTempFile("image", null, cacheDir)
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                            inputStream.close()
                        }
                            return@withContext file
                    } else {
                        return@withContext null
                        }
                    }
                    if (sourceFile != null) repository.uploadPhoto(shelterId,getMultiPartFile(sourceFile, shelterId))
                }
            }
        }
    }

    private fun getMultiPartFile(file: File, shelterId: String): MultipartBody.Part {
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "picture",
            "$shelterId.png",
            requestBody
        )
    }

}

