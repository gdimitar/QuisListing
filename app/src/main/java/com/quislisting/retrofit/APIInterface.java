package com.quislisting.retrofit;

import com.quislisting.dto.LocationDTO;
import com.quislisting.model.AuthenticationResult;
import com.quislisting.model.BaseCategory;
import com.quislisting.model.BaseListing;
import com.quislisting.model.Listing;
import com.quislisting.model.Message;
import com.quislisting.model.User;
import com.quislisting.model.request.AuthenticateUserRequest;
import com.quislisting.model.request.ContactMessageRequest;
import com.quislisting.model.request.MessageRequest;
import com.quislisting.model.request.RegisterUserRequest;
import com.quislisting.model.request.UpdateUserRequest;
import com.quislisting.rest.endpoints.RestRouter;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET(RestRouter.Listing.GET_RECENT_LISTINGS)
    Call<Collection<BaseListing>> getBaseListings(@Query("languageCode") String languageCode);

    @GET(RestRouter.Listing.GET_LISTING)
    Call<Listing> getListing(final @Path("id") String id);

    @GET(RestRouter.Listing.GET_LISTINGS)
    Call<Collection<Listing>> getListings(@Query("languageCode") String languageCode,
                                          @Header("Authorization") String bearer);

    @GET(RestRouter.Listing.SEARCH_LISTINGS)
    Call<Collection<Listing>> searchListings(@Query("query") String query,
                                             @Query("languageCode") String languageCode);

    @POST(RestRouter.User.AUTHENTICATE_USER)
    Call<AuthenticationResult> authenticateUser(@Body AuthenticateUserRequest body);

    @GET(RestRouter.User.GET_USER)
    Call<User> getUser(@Header("Authorization") String bearer);

    @POST(RestRouter.User.REGISTER_USER)
    Call<Integer> registerUser(@Body RegisterUserRequest body);

    @POST(RestRouter.User.UPDATE_USER)
    Call<String> updateUser(@Body UpdateUserRequest body, @Header("Authorization") String bearer);

    @POST(RestRouter.Listing.SEND_LISTING_MESSAGE)
    Call<Integer> sendMessage(final @Path("id") String id, @Body MessageRequest body);

    @POST(RestRouter.ContactCenter.SEND_CONTACT_MESSAGE)
    Call<Integer> sendContactMessage(@Body ContactMessageRequest body);

    @GET(RestRouter.MessageCenter.GET_MESSAGE)
    Call<Message> getMessage(final @Path("dlMessageOverviewId") String id);

    @GET(RestRouter.MessageCenter.GET_MESSAGES)
    Call<Collection<Message>> getMessages(@Header("Authorization") String bearer);

    @GET(RestRouter.Category.GET_ALL_CATEGORIES)
    Call<Collection<BaseCategory>> getBaseCategories();

    @GET(RestRouter.Location.GET_ALL_LOCATIONS)
    Call<Collection<LocationDTO>> getLocations(@Query("parentId") Long parentId,
                                               @Query("languageCode") String languageCode);
}
