#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <uuid/uuid.h>
#include <curl/curl.h>

typedef char uuid_str_t[37];

#define MAX_STRUCT_SIZE 300
#define USER_NAME       "john"
#define PASSWORD        "doe"
#define BASIC_ADDRESS   "http://api.dialer.co.il/v2/calls/%s"
#define JSON "{\"unique_id\": \"%s\",\"dial_at\": 0,\"from_campaign\": false," \
        "\"leg_a\": \"97237173333\",\"cid_a\": \"037173333\",\"dtmf_a\": \"W1\",\"tts\": true," \
        "\"sound\": \"Hello world\",\"gender\": \"female\",\"answer_delay\": 2, \"ring_timeout\": 15," \
        "\"max_retries\": 3,\"retry_wait_time\": 120}"

void get_uuid(uuid_str_t *uuid_str) {
    // typedef unsigned char uuid_t[16];
    uuid_t uuid;

    // generate
    uuid_generate_random(uuid);

    // unparse (to string)
    uuid_unparse_lower(uuid, *uuid_str);
}

char * init_json(uuid_str_t unique_id) {
    char * result = calloc(MAX_STRUCT_SIZE, sizeof(char*));

    snprintf(result, MAX_STRUCT_SIZE, JSON, unique_id);
    return result;
}

const char * post_request(CURL *curl, const char * username, const char * password, const char * action, char * data) {
    int url_len = strlen(BASIC_ADDRESS) + strlen(action) + 1;
    int credential_len = strlen(username) + strlen(password) + 1;
    char * url = calloc(url_len, sizeof(char*));
    char * credentials = calloc(credential_len, sizeof(char *));
    snprintf(url, url_len, BASIC_ADDRESS, action);

    curl_easy_setopt(curl, CURLOPT_VERBOSE, 1);
    /* enable TCP keep-alive for this transfer */
    curl_easy_setopt(curl, CURLOPT_TCP_KEEPALIVE, 1L);
    /* keep-alive idle time to 120 seconds */
    curl_easy_setopt(curl, CURLOPT_TCP_KEEPIDLE, 120L);
    /* interval time between keep-alive probes: 60 seconds */
    curl_easy_setopt(curl, CURLOPT_TCP_KEEPINTVL, 60L);
    curl_easy_setopt(curl, CURLOPT_URL, url);
    curl_easy_setopt(curl, CURLOPT_POST, 1);
    curl_easy_setopt(curl, CURLOPT_HTTPAUTH, (long)CURLAUTH_ANY);
    snprintf(credentials, credential_len, "%s:%s", username, password);
    curl_easy_setopt(curl, CURLOPT_USERPWD, credentials);

    struct curl_slist *headers = NULL;
    if (data != NULL) {
        headers = curl_slist_append(headers, "Content-Type: application/json");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, (long)strlen(data));
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data);
    }

    CURLcode res = curl_easy_perform(curl);

    if (headers)
        free(headers);

    free(url);
    free(credentials);

    if (res != CURLE_OK)
        return curl_easy_strerror(res);

    return "";
}

const char * start_campaign(CURL *curl, const char * username, const char * password, uuid_str_t id) {
    char * path = calloc(60, sizeof(char*));
    strncat(path, "start_campaign/", 59);
    strncat(path, id, 59);
    const char * result = post_request(curl, username, password, path, NULL);

    if (path)
        free(path);

    return result;
}

int main(int argc, char *argv[] ) {
    uuid_str_t uuid_str;
    get_uuid(&uuid_str);

    int ret = 0;

    CURL *curl;
    /* In windows, this will init the winsock stuff */
    curl_global_init(CURL_GLOBAL_ALL);

    curl = curl_easy_init();
    if (! curl) {
        fprintf(stderr, "Unable to initialize curl\n");
        return 1;
    }

    char * json = init_json(uuid_str);
    const char * result;
    result = post_request(curl, USER_NAME, PASSWORD, "simple_dialer", json);

    if (strncmp(result, "", 1) == 0) {
        fprintf(stderr, "Unable to initiate simple campaign: %s\n", result);
        ret = 2;
        goto cleanup;
    }

    result = start_campaign(curl, USER_NAME, PASSWORD, uuid_str);
    if (strncmp(result, "", 1) == 0) {
        fprintf(stderr, "Unable to initiate campaign: %s\n", result);
        ret = 3;
        goto cleanup;
    }

    printf("Campaign %s was started successfully\n", uuid_str);

cleanup:
    if (json)
        free(json);
    curl_easy_cleanup(curl);
    curl_global_cleanup();

    return ret;
}
