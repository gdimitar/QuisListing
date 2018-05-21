package com.quislisting.endpoints.rest;

public final class BaseConfigurations {

    public class InternetCheckerConfig {

        public static final String PING_HOSTNAME = "8.8.8.8";
        public static final int PING_PORT = 53;
        public static final int TIMEOUT_MILLISECONDS = 1500;

        private InternetCheckerConfig() {

        }
    }

    public class RetrofitTimeoutConfig {
        public static final int CONNECT_TIMEOUT = 5;
        public static final int READ_TIMEOUT = 5;
        public static final int WRITE_TIMEOUT = 5;

        private RetrofitTimeoutConfig() {

        }
    }
}
