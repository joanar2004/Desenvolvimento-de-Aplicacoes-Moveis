package dam.A50829.dogbreedxplorer.api

import dam.A50829.dogbreedxplorer.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/image/random/{count}")
    suspend fun getRandomDogImages(@Path("count") count: Int): ApiResponse
}
