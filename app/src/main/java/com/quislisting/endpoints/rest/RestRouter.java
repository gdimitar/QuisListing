package com.quislisting.endpoints.rest;

import com.quislisting.BuildConfig;

public final class RestRouter {

    public static final String BASE_URL = BuildConfig.BASE_URL;

    public class MessageCenter {
        public static final String GET_MESSAGE = BASE_URL + "/api/dl-messages/{dlMessageOverviewId}";
        public static final String GET_MESSAGES = BASE_URL + "/api/dl-messages";

        private MessageCenter() {

        }
    }

    public class User {
        public static final String AUTHENTICATE_USER = BASE_URL + "/api/authenticate";
        public static final String REGISTER_USER = BASE_URL + "/api/register";
        public static final String UPDATE_USER = BASE_URL + "/api/account";
        public static final String GET_USER = BASE_URL + "/api/account";

        private User() {

        }
    }

    public class Listing {
        public static final String SEND_LISTING_MESSAGE = BASE_URL + "/api/dl-listings/{id}/messages";
        public static final String GET_RECENT_LISTINGS = BASE_URL + "/api/dl-listings/recent";
        public static final String GET_LISTING = BASE_URL + "/api/dl-listings/{id}";
        public static final String GET_LISTINGS = BASE_URL + "/api/dl-listings";
        public static final String SEARCH_LISTINGS = BASE_URL + "/api/dl-listings/_search";

        private Listing() {

        }
    }

    public class ContactCenter {
        public static final String SEND_CONTACT_MESSAGE = BASE_URL + "/api/contacts";

        private ContactCenter() {

        }
    }

    public class Category {
        public static final String GET_ALL_CATEGORIES = BASE_URL + "/api/dl-categories";

        private Category() {

        }
    }

    public class Location {
        public static final String GET_ALL_LOCATIONS = BASE_URL + "/api/dl-locations";

        private Location() {

        }
    }
}
