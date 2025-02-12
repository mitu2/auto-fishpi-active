package ink.metoo.auto.fishpi

import com.google.gson.Gson
import ink.metoo.auto.fishpi.call.BaseResult
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.reflect.KClass


object Requests {

    private val gson = Gson()

    private val okhttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (request.url.toString().startsWith(Settings.fishpiClient.baseUrl)) {
                    response.body?.let {
                        try {
                            if (response.code == 401) {
                                ClientCaches.refresh()
                                return@let
                            }
                            val base = gson.fromJson(it.source().readUtf8LineStrict(), BaseResult::class.java)
                            if (base.code == -1 && base.msg == "Invalid Api Key.") {
                                ClientCaches.refresh()
                                return@let
                            }
                        } catch (_: Exception) {

                        }
                    }
                }
                response
            }
            .build()
    }


    private fun createRequest(block: Request.Builder.() -> Unit = {}): Call {
        val builder = Request.Builder()
            .header("User-Agent", Settings.fishpiClient.userAgent)
        builder.block()
        return okhttpClient.newCall(builder.build())
    }

    fun watchWebSocket(url: String, listener: WebSocketListener): WebSocket {
        val request = Request.Builder()
            .header("User-Agent", Settings.fishpiClient.userAgent)
            .url(url)
            .build()
        return okhttpClient.newWebSocket(request, listener)
    }

    fun <T : Any> sendJsonRequest(
        path: String,
        body: Any? = null,
        clazz: KClass<T>,
        block: Request.Builder.() -> Unit = {}
    ): T {
        val call = createRequest {
            if (body != null) {
                url(Settings.fishpiClient.baseUrl + path)
                post(gson.toJson(body).toRequestBody("application/json".toMediaTypeOrNull()))
            }
            block()
        }
        val response = call.execute()
        val bodyString = response.body?.string() ?: throw RuntimeException("response body is null")
        return gson.fromJson(bodyString, clazz.java)
    }

    inline fun <reified T : Any> sendJsonRequest(
        path: String,
        body: Any? = null,
        noinline block: Request.Builder.() -> Unit = {}
    ): T {
        return sendJsonRequest(path, body, T::class, block)
    }

    fun <T : Any> sendGetRequest(
        path: String,
        queryParam: Array<Pair<String, String?>>? = null,
        clazz: KClass<T>,

        block: Request.Builder.() -> Unit = {}
    ): T {
        val url = (Settings.fishpiClient.baseUrl + path).toHttpUrl().run {
            val builder = newBuilder()
            queryParam?.forEach {
                builder.addQueryParameter(it.first, it.second)
            }
            builder.build()
        }
        val call = createRequest {
            get()
            url(url)
            block()
        }
        val response = call.execute()
        val bodyString = response.body?.string() ?: throw RuntimeException("response body is null")
        return gson.fromJson(bodyString, clazz.java)
    }

    inline fun <reified T : Any> sendGetRequest(
        path: String,
        queryParam: Array<Pair<String, String?>>? = null,
        noinline block: Request.Builder.() -> Unit = {}
    ): T {
        return sendGetRequest(path, queryParam, T::class, block)
    }

}