package com.jgm.cloudmedialog

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.datastore.generated.model.MediaData
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.amplifyframework.storage.StorageAccessLevel
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import java.io.File
import java.io.FileInputStream

object Backend
{
    private const val TAG = "Backend"
    fun initialize(applicationContext: Context) : Backend
    {
        try
        {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            Amplify.configure(applicationContext)
            Log.i(TAG, "Initialized Amplify")
        }
        catch (e: AmplifyException)
        {
            Log.e(TAG, "Could not initialize Amplify", e)
        }
        Amplify.Hub.subscribe(HubChannel.AUTH) { hubEvent: HubEvent<*> ->
            when (hubEvent.name)
            {
                InitializationStatus.SUCCEEDED.toString() ->
                {
                    Log.i(TAG, "Amplify successfully initialized")
                }
                InitializationStatus.FAILED.toString() ->
                {
                    Log.i(TAG, "Amplify initialization failed")
                }
                else ->
                {
                    when (AuthChannelEventName.valueOf(hubEvent.name))
                    {
                        AuthChannelEventName.SIGNED_IN ->
                        {
                            updateUserData(true)
                            Log.i(TAG, "HUB : SIGNED_IN")
                        }
                        AuthChannelEventName.SIGNED_OUT ->
                        {
                            updateUserData(false)
                            Log.i(TAG, "HUB : SIGNED_OUT")
                        }
                        else -> Log.i(TAG, """HUB EVENT:${hubEvent.name}""")
                    }
                }
            }
        }
        Amplify.Auth.fetchAuthSession(
            { result ->
                Log.i(TAG, result.toString())
                val cognitoAuthSession = result as AWSCognitoAuthSession
                this.updateUserData(cognitoAuthSession.isSignedIn)
                when (cognitoAuthSession.identityIdResult.type)
                {
                    AuthSessionResult.Type.SUCCESS ->  Log.i(TAG, "IdentityId: " + cognitoAuthSession.identityIdResult.value)
                    AuthSessionResult.Type.FAILURE -> Log.i(TAG, "IdentityId not present because: " + cognitoAuthSession.identityIdResult.error.toString())
                }
            },
            { error -> Log.i(TAG, error.toString()) }
        )
        return this
    }
    private fun updateUserData(withSignedInStatus : Boolean)
    {
        UserData.setSignedIn(withSignedInStatus)
        val medias = UserData.medias().value
        val isEmpty = medias?.isEmpty() ?: false
        if (withSignedInStatus && isEmpty )
        {
            this.queryMedias()
        }
        else
        {
            UserData.resetMedias()
        }
    }
    fun signOut()
    {
        Log.i(TAG, "Initiate Signout Sequence")
        Amplify.Auth.signOut { signOutResult ->
            when(signOutResult) {
                is AWSCognitoAuthSignOutResult.CompleteSignOut ->
                {
                    Log.i("AuthQuickStart", "Signed out successfully")
                }
                is AWSCognitoAuthSignOutResult.PartialSignOut ->
                {
                    signOutResult.hostedUIError?.let {
                        Log.e("AuthQuickStart", "HostedUI Error", it.exception)
                    }
                    signOutResult.globalSignOutError?.let {
                        Log.e("AuthQuickStart", "GlobalSignOut Error", it.exception)
                    }
                    signOutResult.revokeTokenError?.let {
                        Log.e("AuthQuickStart", "RevokeToken Error", it.exception)
                    }
                }
                is AWSCognitoAuthSignOutResult.FailedSignOut ->
                {
                    Log.e("AuthQuickStart", "Sign out Failed", signOutResult.exception)
                }
            }
        }
    }
    fun signIn(callingActivity: Activity)
    {
        Amplify.Auth.signInWithWebUI(callingActivity, { result: AuthSignInResult ->  Log.i(TAG, result.toString()) }, { error: AuthException -> Log.e(TAG, error.toString()) })
    }
    private fun queryMedias()
    {
        Amplify.API.query(ModelQuery.list(MediaData::class.java),
            { response ->
                for (mediaData in response.data) {
                    Log.i(TAG, mediaData.name)

                    UserData.addMedia(UserData.Media.from(mediaData))
                }
            },
            { error -> Log.e(TAG, "Query failure", error) }
        )
    }
    fun createMedia(media : UserData.Media)
    {
        Log.i(TAG, "Creating media")
        Amplify.API.mutate(
            ModelMutation.create(media.data),
            { response ->
                Log.i(TAG, "Created")
                if (response.hasErrors())
                {
                    Log.e(TAG, response.errors.first().message)
                }
                else
                {
                    Log.i(TAG, "Created Media with id: " + response.data.id)
                }
            }, { error -> Log.e(TAG, "Create failed", error) }
        )
    }
    fun deleteMedia(media : UserData.Media?)
    {
        if (media == null) return
        Log.i(TAG, "Deleting media $media")
        Amplify.API.mutate(
            ModelMutation.delete(media.data),
            { response ->
                Log.i(TAG, "Deleted")
                if (response.hasErrors())
                {
                    Log.e(TAG, response.errors.first().message)
                }
                else
                {
                    Log.i(TAG, "Deleted Media $response")
                }
            }, { error -> Log.e(TAG, "Delete failed", error) }
        )
    }
    fun storeImage(filePath: String, key: String)
    {
        val file = File(filePath)
        val options = StorageUploadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()
        Amplify.Storage.uploadFile(
            key,
            file,
            options,
            { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
            { result -> Log.i(TAG, "Successfully uploaded: " + result.key) },
            { error -> Log.e(TAG, "Upload failed", error) }
        )
    }
    fun deleteImage(key : String)
    {
        val options = StorageRemoveOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()
        Amplify.Storage.remove(
            key,
            options,
            { result -> Log.i(TAG, "Successfully removed: " + result.key) }, { error -> Log.e(TAG, "Remove failure", error) }
        )
    }
    fun retrieveImage(key: String, completed : (image: Bitmap) -> Unit)
    {
        val options = StorageDownloadFileOptions.builder()
            .accessLevel(StorageAccessLevel.PRIVATE)
            .build()
        val file = File.createTempFile("image", ".image")
        Amplify.Storage.downloadFile(
            key,
            file,
            options,
            { progress -> Log.i(TAG, "Fraction completed: ${progress.fractionCompleted}") },
            { result ->
                Log.i(TAG, "Successfully downloaded: ${result.file.name}")
                val imageStream = FileInputStream(file)
                val image = BitmapFactory.decodeStream(imageStream)
                completed(image)
            }, { error -> Log.e(TAG, "Download Failure", error) }
        )
    }
}