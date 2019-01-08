#ifndef GRAPHQL_HTTP_HPP__
#define GRAPHQL_HTTP_HPP__

#include <string>
#include <unordered_map>
#include "./IJsonSerializable.hpp"
#include <curl/curl.h>
#include <type_traits>

namespace GraphQL::Http
{
    // const char * greeting = "Hello, World";
    
    struct {
        std::string host;
        curl_off_t size;
        long status;
        double time;
        std::string verb;
    } typedef InfoStruct;

    class HttpClient {
        public:
            HttpClient(bool _debug = false): debug(_debug) {};

            template<typename CLAZZ, 
                typename std::enable_if<std::is_base_of<GraphQL::Json::IJsonSerializable, CLAZZ>::value>::type* = nullptr>
            std::string post(
                std::string url, const CLAZZ obj, 
                const std::unordered_map<std::string, std::string>& headers = {}
            );

            std::string get(std::string url, 
                const std::unordered_map<std::string, std::string>& queryParams = {}, 
                const std::unordered_map<std::string, std::string>& headers = {}
            );
        
        private:

            bool debug = false;
            CURL* curlObj = nullptr;
            char errorBuffer[CURL_ERROR_SIZE];

            // private method

            bool init();
            std::string setQueryParam(std::string url, const std::unordered_map<std::string, std::string>& queryParams);
            void setBodyContent(std::string* body, curl_slist* list);
            void setHttps(std::string& url);
            std::string urlEncode(std::string toEncode);
            void setHeaders(const std::unordered_map<std::string, std::string>& headers, curl_slist* list);
            void setOpt(std::string url, std::string& buffer);
            bool exec(std::string& buffer);
            void clean(curl_slist* list);
            InfoStruct getInfo(std::string httpVerb);
            template<class... Args>
            void handleError(bool showError, std::string msg, Args... args);
    };

}

#include "../cpp/GraphQLHTTP.cpp"

#endif  // GRAPHQL_HTTP_HPP__